package com.facebook.widget;

import android.content.Context;
import android.os.Handler;
import android.support.v4.content.Loader;
import com.facebook.FacebookException;
import com.facebook.FacebookRequestError;
import com.facebook.Request;
import com.facebook.RequestBatch;
import com.facebook.Response;
import com.facebook.internal.CacheableRequestBatch;
import com.facebook.model.GraphObject;
import com.facebook.model.GraphObjectList;
/* loaded from: classes.dex */
class GraphObjectPagingLoader<T extends GraphObject> extends Loader<SimpleGraphObjectCursor<T>> {
    private boolean appendResults;
    private Request currentRequest;
    private SimpleGraphObjectCursor<T> cursor;
    private final Class<T> graphObjectClass;
    private boolean loading;
    private Request nextRequest;
    private OnErrorListener onErrorListener;
    private Request originalRequest;
    private boolean skipRoundtripIfCached;

    /* loaded from: classes.dex */
    public interface OnErrorListener {
        void onError(FacebookException facebookException, GraphObjectPagingLoader<?> graphObjectPagingLoader);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public interface PagedResults extends GraphObject {
        GraphObjectList<GraphObject> getData();
    }

    @Override // android.support.v4.content.Loader
    public /* bridge */ /* synthetic */ void deliverResult(Object obj) {
        deliverResult((SimpleGraphObjectCursor) ((SimpleGraphObjectCursor) obj));
    }

    public GraphObjectPagingLoader(Context context, Class<T> graphObjectClass) {
        super(context);
        this.appendResults = false;
        this.loading = false;
        this.graphObjectClass = graphObjectClass;
    }

    public OnErrorListener getOnErrorListener() {
        return this.onErrorListener;
    }

    public void setOnErrorListener(OnErrorListener listener) {
        this.onErrorListener = listener;
    }

    public SimpleGraphObjectCursor<T> getCursor() {
        return this.cursor;
    }

    public void clearResults() {
        this.nextRequest = null;
        this.originalRequest = null;
        this.currentRequest = null;
        deliverResult((SimpleGraphObjectCursor) null);
    }

    public boolean isLoading() {
        return this.loading;
    }

    public void startLoading(Request request, boolean skipRoundtripIfCached) {
        this.originalRequest = request;
        startLoading(request, skipRoundtripIfCached, 0L);
    }

    public void refreshOriginalRequest(long afterDelay) {
        if (this.originalRequest == null) {
            throw new FacebookException("refreshOriginalRequest may not be called until after startLoading has been called.");
        }
        startLoading(this.originalRequest, false, afterDelay);
    }

    public void followNextLink() {
        if (this.nextRequest != null) {
            this.appendResults = true;
            this.currentRequest = this.nextRequest;
            this.currentRequest.setCallback(new Request.Callback() { // from class: com.facebook.widget.GraphObjectPagingLoader.1
                @Override // com.facebook.Request.Callback
                public void onCompleted(Response response) {
                    GraphObjectPagingLoader.this.requestCompleted(response);
                }
            });
            this.loading = true;
            CacheableRequestBatch batch = putRequestIntoBatch(this.currentRequest, this.skipRoundtripIfCached);
            Request.executeBatchAsync((RequestBatch) batch);
        }
    }

    public void deliverResult(SimpleGraphObjectCursor<T> cursor) {
        SimpleGraphObjectCursor<T> oldCursor = this.cursor;
        this.cursor = cursor;
        if (isStarted()) {
            super.deliverResult((GraphObjectPagingLoader<T>) cursor);
            if (oldCursor != null && oldCursor != cursor && !oldCursor.isClosed()) {
                oldCursor.close();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.support.v4.content.Loader
    public void onStartLoading() {
        super.onStartLoading();
        if (this.cursor != null) {
            deliverResult((SimpleGraphObjectCursor) this.cursor);
        }
    }

    private void startLoading(Request request, boolean skipRoundtripIfCached, long afterDelay) {
        this.skipRoundtripIfCached = skipRoundtripIfCached;
        this.appendResults = false;
        this.nextRequest = null;
        this.currentRequest = request;
        this.currentRequest.setCallback(new Request.Callback() { // from class: com.facebook.widget.GraphObjectPagingLoader.2
            @Override // com.facebook.Request.Callback
            public void onCompleted(Response response) {
                GraphObjectPagingLoader.this.requestCompleted(response);
            }
        });
        this.loading = true;
        final RequestBatch batch = putRequestIntoBatch(request, skipRoundtripIfCached);
        Runnable r = new Runnable() { // from class: com.facebook.widget.GraphObjectPagingLoader.3
            @Override // java.lang.Runnable
            public void run() {
                Request.executeBatchAsync(batch);
            }
        };
        if (afterDelay == 0) {
            r.run();
            return;
        }
        Handler handler = new Handler();
        handler.postDelayed(r, afterDelay);
    }

    private CacheableRequestBatch putRequestIntoBatch(Request request, boolean skipRoundtripIfCached) {
        CacheableRequestBatch batch = new CacheableRequestBatch(request);
        batch.setForceRoundTrip(skipRoundtripIfCached ? false : true);
        return batch;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void requestCompleted(Response response) {
        Request request = response.getRequest();
        if (request == this.currentRequest) {
            this.loading = false;
            this.currentRequest = null;
            FacebookRequestError requestError = response.getError();
            FacebookException exception = requestError == null ? null : requestError.getException();
            if (response.getGraphObject() == null && exception == null) {
                exception = new FacebookException("GraphObjectPagingLoader received neither a result nor an error.");
            }
            if (exception != null) {
                this.nextRequest = null;
                if (this.onErrorListener != null) {
                    this.onErrorListener.onError(exception, this);
                    return;
                }
                return;
            }
            addResults(response);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void addResults(Response response) {
        SimpleGraphObjectCursor<T> cursorToModify = (this.cursor == null || !this.appendResults) ? new SimpleGraphObjectCursor<>() : new SimpleGraphObjectCursor<>(this.cursor);
        PagedResults result = (PagedResults) response.getGraphObjectAs(PagedResults.class);
        boolean fromCache = response.getIsFromCache();
        GraphObjectList<U> castToListOf = result.getData().castToListOf((Class<T>) this.graphObjectClass);
        boolean haveData = castToListOf.size() > 0;
        if (haveData) {
            this.nextRequest = response.getRequestForPagedResults(Response.PagingDirection.NEXT);
            cursorToModify.addGraphObjects(castToListOf, fromCache);
            if (this.nextRequest != null) {
                cursorToModify.setMoreObjectsAvailable(true);
            } else {
                cursorToModify.setMoreObjectsAvailable(false);
            }
        }
        if (!haveData) {
            cursorToModify.setMoreObjectsAvailable(false);
            cursorToModify.setFromCache(fromCache);
            this.nextRequest = null;
        }
        if (!fromCache) {
            this.skipRoundtripIfCached = false;
        }
        deliverResult((SimpleGraphObjectCursor) cursorToModify);
    }
}

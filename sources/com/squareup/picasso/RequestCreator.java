package com.squareup.picasso;

import android.app.Notification;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.ImageView;
import android.widget.RemoteViews;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RemoteViewsAction;
import com.squareup.picasso.Request;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
/* loaded from: classes.dex */
public class RequestCreator {
    private static int nextId = 0;
    private final Request.Builder data;
    private boolean deferred;
    private Drawable errorDrawable;
    private int errorResId;
    private boolean noFade;
    private final Picasso picasso;
    private Drawable placeholderDrawable;
    private int placeholderResId;
    private boolean setPlaceholder;
    private boolean skipMemoryCache;
    private Object tag;

    static /* synthetic */ int access$000() {
        return getRequestId();
    }

    private static int getRequestId() {
        if (Utils.isMain()) {
            int i = nextId;
            nextId = i + 1;
            return i;
        }
        final CountDownLatch latch = new CountDownLatch(1);
        final AtomicInteger id = new AtomicInteger();
        Picasso.HANDLER.post(new Runnable() { // from class: com.squareup.picasso.RequestCreator.1
            @Override // java.lang.Runnable
            public void run() {
                id.set(RequestCreator.access$000());
                latch.countDown();
            }
        });
        try {
            latch.await();
        } catch (InterruptedException e) {
            Utils.sneakyRethrow(e);
        }
        return id.get();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public RequestCreator(Picasso picasso, Uri uri, int resourceId) {
        this.setPlaceholder = true;
        if (picasso.shutdown) {
            throw new IllegalStateException("Picasso instance already shut down. Cannot submit new requests.");
        }
        this.picasso = picasso;
        this.data = new Request.Builder(uri, resourceId);
    }

    RequestCreator() {
        this.setPlaceholder = true;
        this.picasso = null;
        this.data = new Request.Builder((Uri) null, 0);
    }

    public RequestCreator noPlaceholder() {
        if (this.placeholderResId != 0) {
            throw new IllegalStateException("Placeholder resource already set.");
        }
        if (this.placeholderDrawable != null) {
            throw new IllegalStateException("Placeholder image already set.");
        }
        this.setPlaceholder = false;
        return this;
    }

    public RequestCreator placeholder(int placeholderResId) {
        if (!this.setPlaceholder) {
            throw new IllegalStateException("Already explicitly declared as no placeholder.");
        }
        if (placeholderResId == 0) {
            throw new IllegalArgumentException("Placeholder image resource invalid.");
        }
        if (this.placeholderDrawable != null) {
            throw new IllegalStateException("Placeholder image already set.");
        }
        this.placeholderResId = placeholderResId;
        return this;
    }

    public RequestCreator placeholder(Drawable placeholderDrawable) {
        if (!this.setPlaceholder) {
            throw new IllegalStateException("Already explicitly declared as no placeholder.");
        }
        if (this.placeholderResId != 0) {
            throw new IllegalStateException("Placeholder image already set.");
        }
        this.placeholderDrawable = placeholderDrawable;
        return this;
    }

    public RequestCreator error(int errorResId) {
        if (errorResId == 0) {
            throw new IllegalArgumentException("Error image resource invalid.");
        }
        if (this.errorDrawable != null) {
            throw new IllegalStateException("Error image already set.");
        }
        this.errorResId = errorResId;
        return this;
    }

    public RequestCreator error(Drawable errorDrawable) {
        if (errorDrawable == null) {
            throw new IllegalArgumentException("Error image may not be null.");
        }
        if (this.errorResId != 0) {
            throw new IllegalStateException("Error image already set.");
        }
        this.errorDrawable = errorDrawable;
        return this;
    }

    public RequestCreator tag(Object tag) {
        if (tag == null) {
            throw new IllegalArgumentException("Tag invalid.");
        }
        if (this.tag != null) {
            throw new IllegalStateException("Tag already set.");
        }
        this.tag = tag;
        return this;
    }

    public RequestCreator fit() {
        this.deferred = true;
        return this;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public RequestCreator unfit() {
        this.deferred = false;
        return this;
    }

    public RequestCreator resizeDimen(int targetWidthResId, int targetHeightResId) {
        Resources resources = this.picasso.context.getResources();
        int targetWidth = resources.getDimensionPixelSize(targetWidthResId);
        int targetHeight = resources.getDimensionPixelSize(targetHeightResId);
        return resize(targetWidth, targetHeight);
    }

    public RequestCreator resize(int targetWidth, int targetHeight) {
        this.data.resize(targetWidth, targetHeight);
        return this;
    }

    public RequestCreator centerCrop() {
        this.data.centerCrop();
        return this;
    }

    public RequestCreator centerInside() {
        this.data.centerInside();
        return this;
    }

    public RequestCreator rotate(float degrees) {
        this.data.rotate(degrees);
        return this;
    }

    public RequestCreator rotate(float degrees, float pivotX, float pivotY) {
        this.data.rotate(degrees, pivotX, pivotY);
        return this;
    }

    public RequestCreator config(Bitmap.Config config) {
        this.data.config(config);
        return this;
    }

    public RequestCreator stableKey(String stableKey) {
        this.data.stableKey(stableKey);
        return this;
    }

    public RequestCreator priority(Picasso.Priority priority) {
        this.data.priority(priority);
        return this;
    }

    public RequestCreator transform(Transformation transformation) {
        this.data.transform(transformation);
        return this;
    }

    public RequestCreator skipMemoryCache() {
        this.skipMemoryCache = true;
        return this;
    }

    public RequestCreator noFade() {
        this.noFade = true;
        return this;
    }

    public Bitmap get() throws IOException {
        long started = System.nanoTime();
        Utils.checkNotMain();
        if (this.deferred) {
            throw new IllegalStateException("Fit cannot be used with get.");
        }
        if (!this.data.hasImage()) {
            return null;
        }
        Request finalData = createRequest(started);
        String key = Utils.createKey(finalData, new StringBuilder());
        Action action = new GetAction(this.picasso, finalData, this.skipMemoryCache, key, this.tag);
        return BitmapHunter.forRequest(this.picasso, this.picasso.dispatcher, this.picasso.cache, this.picasso.stats, action).hunt();
    }

    public void fetch() {
        long started = System.nanoTime();
        if (this.deferred) {
            throw new IllegalStateException("Fit cannot be used with fetch.");
        }
        if (this.data.hasImage()) {
            if (!this.data.hasPriority()) {
                this.data.priority(Picasso.Priority.LOW);
            }
            Request request = createRequest(started);
            String key = Utils.createKey(request, new StringBuilder());
            Action action = new FetchAction(this.picasso, request, this.skipMemoryCache, key, this.tag);
            this.picasso.submit(action);
        }
    }

    public void into(Target target) {
        Bitmap bitmap;
        long started = System.nanoTime();
        Utils.checkMain();
        if (target == null) {
            throw new IllegalArgumentException("Target must not be null.");
        }
        if (this.deferred) {
            throw new IllegalStateException("Fit cannot be used with a Target.");
        }
        if (!this.data.hasImage()) {
            this.picasso.cancelRequest(target);
            target.onPrepareLoad(this.setPlaceholder ? getPlaceholderDrawable() : null);
            return;
        }
        Request request = createRequest(started);
        String requestKey = Utils.createKey(request);
        if (!this.skipMemoryCache && (bitmap = this.picasso.quickMemoryCacheCheck(requestKey)) != null) {
            this.picasso.cancelRequest(target);
            target.onBitmapLoaded(bitmap, Picasso.LoadedFrom.MEMORY);
            return;
        }
        target.onPrepareLoad(this.setPlaceholder ? getPlaceholderDrawable() : null);
        Action action = new TargetAction(this.picasso, target, request, this.skipMemoryCache, this.errorResId, this.errorDrawable, requestKey, this.tag);
        this.picasso.enqueueAndSubmit(action);
    }

    public void into(RemoteViews remoteViews, int viewId, int notificationId, Notification notification) {
        long started = System.nanoTime();
        Utils.checkMain();
        if (remoteViews == null) {
            throw new IllegalArgumentException("RemoteViews must not be null.");
        }
        if (notification == null) {
            throw new IllegalArgumentException("Notification must not be null.");
        }
        if (this.deferred) {
            throw new IllegalStateException("Fit cannot be used with RemoteViews.");
        }
        if (this.placeholderDrawable != null || this.placeholderResId != 0 || this.errorDrawable != null) {
            throw new IllegalArgumentException("Cannot use placeholder or error drawables with remote views.");
        }
        Request request = createRequest(started);
        String key = Utils.createKey(request);
        RemoteViewsAction action = new RemoteViewsAction.NotificationAction(this.picasso, request, remoteViews, viewId, notificationId, notification, this.skipMemoryCache, this.errorResId, key, this.tag);
        performRemoteViewInto(action);
    }

    public void into(RemoteViews remoteViews, int viewId, int[] appWidgetIds) {
        long started = System.nanoTime();
        Utils.checkMain();
        if (remoteViews == null) {
            throw new IllegalArgumentException("remoteViews must not be null.");
        }
        if (appWidgetIds == null) {
            throw new IllegalArgumentException("appWidgetIds must not be null.");
        }
        if (this.deferred) {
            throw new IllegalStateException("Fit cannot be used with remote views.");
        }
        if (this.placeholderDrawable != null || this.placeholderResId != 0 || this.errorDrawable != null) {
            throw new IllegalArgumentException("Cannot use placeholder or error drawables with remote views.");
        }
        Request request = createRequest(started);
        String key = Utils.createKey(request);
        RemoteViewsAction action = new RemoteViewsAction.AppWidgetAction(this.picasso, request, remoteViews, viewId, appWidgetIds, this.skipMemoryCache, this.errorResId, key, this.tag);
        performRemoteViewInto(action);
    }

    public void into(ImageView target) {
        into(target, null);
    }

    public void into(ImageView target, Callback callback) {
        Bitmap bitmap;
        long started = System.nanoTime();
        Utils.checkMain();
        if (target == null) {
            throw new IllegalArgumentException("Target must not be null.");
        }
        if (!this.data.hasImage()) {
            this.picasso.cancelRequest(target);
            if (this.setPlaceholder) {
                PicassoDrawable.setPlaceholder(target, getPlaceholderDrawable());
                return;
            }
            return;
        }
        if (this.deferred) {
            if (this.data.hasSize()) {
                throw new IllegalStateException("Fit cannot be used with resize.");
            }
            int width = target.getWidth();
            int height = target.getHeight();
            if (width == 0 || height == 0) {
                if (this.setPlaceholder) {
                    PicassoDrawable.setPlaceholder(target, getPlaceholderDrawable());
                }
                this.picasso.defer(target, new DeferredRequestCreator(this, target, callback));
                return;
            }
            this.data.resize(width, height);
        }
        Request request = createRequest(started);
        String requestKey = Utils.createKey(request);
        if (!this.skipMemoryCache && (bitmap = this.picasso.quickMemoryCacheCheck(requestKey)) != null) {
            this.picasso.cancelRequest(target);
            PicassoDrawable.setBitmap(target, this.picasso.context, bitmap, Picasso.LoadedFrom.MEMORY, this.noFade, this.picasso.indicatorsEnabled);
            if (this.picasso.loggingEnabled) {
                Utils.log("Main", "completed", request.plainId(), "from " + Picasso.LoadedFrom.MEMORY);
            }
            if (callback != null) {
                callback.onSuccess();
                return;
            }
            return;
        }
        if (this.setPlaceholder) {
            PicassoDrawable.setPlaceholder(target, getPlaceholderDrawable());
        }
        Action action = new ImageViewAction(this.picasso, target, request, this.skipMemoryCache, this.noFade, this.errorResId, this.errorDrawable, requestKey, this.tag, callback);
        this.picasso.enqueueAndSubmit(action);
    }

    private Drawable getPlaceholderDrawable() {
        return this.placeholderResId != 0 ? this.picasso.context.getResources().getDrawable(this.placeholderResId) : this.placeholderDrawable;
    }

    private Request createRequest(long started) {
        int id = getRequestId();
        Request request = this.data.build();
        request.id = id;
        request.started = started;
        boolean loggingEnabled = this.picasso.loggingEnabled;
        if (loggingEnabled) {
            Utils.log("Main", "created", request.plainId(), request.toString());
        }
        Request transformed = this.picasso.transformRequest(request);
        if (transformed != request) {
            transformed.id = id;
            transformed.started = started;
            if (loggingEnabled) {
                Utils.log("Main", "changed", transformed.logId(), "into " + transformed);
            }
        }
        return transformed;
    }

    private void performRemoteViewInto(RemoteViewsAction action) {
        Bitmap bitmap;
        if (!this.skipMemoryCache && (bitmap = this.picasso.quickMemoryCacheCheck(action.getKey())) != null) {
            action.complete(bitmap, Picasso.LoadedFrom.MEMORY);
            return;
        }
        if (this.placeholderResId != 0) {
            action.setImageResource(this.placeholderResId);
        }
        this.picasso.enqueueAndSubmit(action);
    }
}

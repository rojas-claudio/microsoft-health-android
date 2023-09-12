package com.facebook;

import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Executor;
@TargetApi(3)
/* loaded from: classes.dex */
public class RequestAsyncTask extends AsyncTask<Void, Void, List<Response>> {
    private static final String TAG = RequestAsyncTask.class.getCanonicalName();
    private static Method executeOnExecutorMethod;
    private final HttpURLConnection connection;
    private Exception exception;
    private final RequestBatch requests;

    static {
        Method[] methods;
        for (Method method : AsyncTask.class.getMethods()) {
            if ("executeOnExecutor".equals(method.getName())) {
                Class[] parameters = method.getParameterTypes();
                if (parameters.length == 2 && parameters[0] == Executor.class && parameters[1].isArray()) {
                    executeOnExecutorMethod = method;
                    return;
                }
            }
        }
    }

    public RequestAsyncTask(Request... requests) {
        this((HttpURLConnection) null, new RequestBatch(requests));
    }

    public RequestAsyncTask(Collection<Request> requests) {
        this((HttpURLConnection) null, new RequestBatch(requests));
    }

    public RequestAsyncTask(RequestBatch requests) {
        this((HttpURLConnection) null, requests);
    }

    public RequestAsyncTask(HttpURLConnection connection, Request... requests) {
        this(connection, new RequestBatch(requests));
    }

    public RequestAsyncTask(HttpURLConnection connection, Collection<Request> requests) {
        this(connection, new RequestBatch(requests));
    }

    public RequestAsyncTask(HttpURLConnection connection, RequestBatch requests) {
        this.requests = requests;
        this.connection = connection;
    }

    protected final Exception getException() {
        return this.exception;
    }

    protected final RequestBatch getRequests() {
        return this.requests;
    }

    public String toString() {
        return "{RequestAsyncTask:  connection: " + this.connection + ", requests: " + this.requests + "}";
    }

    @Override // android.os.AsyncTask
    protected void onPreExecute() {
        super.onPreExecute();
        if (this.requests.getCallbackHandler() == null) {
            this.requests.setCallbackHandler(new Handler());
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.os.AsyncTask
    public void onPostExecute(List<Response> result) {
        super.onPostExecute((RequestAsyncTask) result);
        if (this.exception != null) {
            Log.d(TAG, String.format("onPostExecute: exception encountered during request: %s", this.exception.getMessage()));
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.os.AsyncTask
    public List<Response> doInBackground(Void... params) {
        List<Response> executeConnectionAndWait;
        try {
            if (this.connection == null) {
                executeConnectionAndWait = this.requests.executeAndWait();
            } else {
                executeConnectionAndWait = Request.executeConnectionAndWait(this.connection, this.requests);
            }
            return executeConnectionAndWait;
        } catch (Exception e) {
            this.exception = e;
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public RequestAsyncTask executeOnSettingsExecutor() {
        if (executeOnExecutorMethod != null) {
            executeOnExecutorMethod.invoke(this, Settings.getExecutor(), null);
            return this;
        }
        execute(new Void[0]);
        return this;
    }
}

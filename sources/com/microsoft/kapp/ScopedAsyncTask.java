package com.microsoft.kapp;

import android.os.AsyncTask;
import com.microsoft.kapp.logging.KLog;
import java.lang.ref.WeakReference;
import java.util.concurrent.Executor;
/* loaded from: classes.dex */
public abstract class ScopedAsyncTask<Params, Progress, Result> {
    protected final String TAG = getClass().getSimpleName();
    private final AsyncTask<Params, Progress, Result> mAsyncTask = new AsyncTask<Params, Progress, Result>() { // from class: com.microsoft.kapp.ScopedAsyncTask.1
        @Override // android.os.AsyncTask
        protected Result doInBackground(Params... params) {
            return (Result) ScopedAsyncTask.this.doInBackground(params);
        }

        @Override // android.os.AsyncTask
        protected void onPostExecute(Result result) {
            if (ScopedAsyncTask.this.isListenerWaiting()) {
                super.onPostExecute(result);
                ScopedAsyncTask.this.onPostExecute(result);
            }
        }

        @Override // android.os.AsyncTask
        protected void onPreExecute() {
            if (ScopedAsyncTask.this.isListenerWaiting()) {
                super.onPreExecute();
                ScopedAsyncTask.this.onPreExecute();
            }
        }

        @Override // android.os.AsyncTask
        protected void onCancelled() {
            if (ScopedAsyncTask.this.isListenerWaiting()) {
                super.onCancelled();
                ScopedAsyncTask.this.onCancelled();
            }
        }
    };
    private Exception mException;
    private final WeakReference<OnTaskListener> mWeakListener;

    protected abstract Result doInBackground(Params... paramsArr);

    protected abstract void onPostExecute(Result result);

    public ScopedAsyncTask(OnTaskListener onTaskListener) {
        this.mWeakListener = new WeakReference<>(onTaskListener);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isListenerWaiting() {
        OnTaskListener taskListener = this.mWeakListener.get();
        return taskListener != null && taskListener.isWaitingForResult();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void onPreExecute() {
    }

    protected void onCancelled() {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void onCancelled(Result result) {
        KLog.i(this.TAG, "Task was Cancelled with result: " + result.toString());
    }

    public final boolean cancel(boolean mayInterruptIfActive) {
        return this.mAsyncTask.cancel(mayInterruptIfActive);
    }

    public final AsyncTask.Status getStatus() {
        return this.mAsyncTask.getStatus();
    }

    public final void execute(Params... params) {
        this.mAsyncTask.execute(params);
    }

    public final void executeOnExecutor(Executor exec, Params... params) {
        this.mAsyncTask.executeOnExecutor(exec, params);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setException(Exception exception) {
        this.mException = exception;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public Exception getException() {
        return this.mException;
    }
}

package com.microsoft.kapp;

import android.app.Activity;
/* loaded from: classes.dex */
public abstract class ActivityScopedCallbackAsyncTask<Params, Progress, Result> extends ScopedAsyncTask<Params, Progress, Result> {
    private ActivityScopedCallback<Result> mCallback;

    public ActivityScopedCallbackAsyncTask(Activity activity, Callback<Result> callback) {
        super((OnTaskListener) activity);
        this.mCallback = new ActivityScopedCallback<>(activity, callback);
    }

    protected ActivityScopedCallback<Result> getCallback() {
        return this.mCallback;
    }

    @Override // com.microsoft.kapp.ScopedAsyncTask
    protected void onPostExecute(Result result) {
        if (getException() == null) {
            this.mCallback.callback(result);
        } else {
            this.mCallback.onError(getException());
        }
    }
}

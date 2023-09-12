package com.microsoft.kapp.tasks;

import com.microsoft.kapp.Callback;
import com.microsoft.kapp.OnTaskListener;
/* loaded from: classes.dex */
public class OnTaskListenerWithCallback<TResult> implements OnTaskListener, Callback<TResult> {
    private Callback<TResult> mCallback;
    private OnTaskListener mOnTaskListener;

    public OnTaskListenerWithCallback(OnTaskListener onTaskListener, Callback<TResult> callback) {
        this.mOnTaskListener = onTaskListener;
        this.mCallback = callback;
    }

    @Override // com.microsoft.kapp.OnTaskListener
    public boolean isWaitingForResult() {
        return this.mOnTaskListener.isWaitingForResult();
    }

    @Override // com.microsoft.kapp.Callback
    public void callback(TResult result) {
        this.mCallback.callback(result);
    }

    @Override // com.microsoft.kapp.Callback
    public void onError(Exception ex) {
        this.mCallback.onError(ex);
    }
}

package com.microsoft.kapp.tasks;

import com.microsoft.kapp.Callback;
import com.microsoft.kapp.OnTaskListener;
import com.microsoft.kapp.diagnostics.Validate;
import com.microsoft.kapp.tasks.OnTaskStateChangedListenerV1;
import java.lang.ref.WeakReference;
/* loaded from: classes.dex */
public abstract class RestQueryTaskBaseV1<TResult, TListener extends OnTaskStateChangedListenerV1> implements Callback<TResult> {
    private WeakReference<OnTaskListener> mWeakTaskListener;

    public abstract void execute();

    public abstract void onFailure(Exception exc);

    public abstract void onSuccess(TResult tresult);

    /* JADX INFO: Access modifiers changed from: protected */
    public RestQueryTaskBaseV1(OnTaskListener onTaskListener) {
        Validate.notNull(onTaskListener, "onTaskListener");
        this.mWeakTaskListener = new WeakReference<>(onTaskListener);
    }

    @Override // com.microsoft.kapp.Callback
    public void callback(TResult result) {
        OnTaskListener listener = this.mWeakTaskListener.get();
        if (listener != null && listener.isWaitingForResult()) {
            onSuccess(result);
        }
    }

    @Override // com.microsoft.kapp.Callback
    public void onError(Exception ex) {
        OnTaskListener listener = this.mWeakTaskListener.get();
        if (listener != null && listener.isWaitingForResult()) {
            onFailure(ex);
        }
    }
}

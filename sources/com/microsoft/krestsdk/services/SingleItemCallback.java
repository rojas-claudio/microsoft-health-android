package com.microsoft.krestsdk.services;

import com.microsoft.kapp.Callback;
import com.microsoft.kapp.diagnostics.Validate;
import java.util.List;
/* loaded from: classes.dex */
public class SingleItemCallback<TResponse> implements Callback<List<TResponse>> {
    private final Callback<TResponse> mCallback;

    @Override // com.microsoft.kapp.Callback
    public /* bridge */ /* synthetic */ void callback(Object x0) {
        callback((List) ((List) x0));
    }

    public SingleItemCallback(Callback<TResponse> callback) {
        Validate.notNull(callback, "callback");
        this.mCallback = callback;
    }

    public void callback(List<TResponse> result) {
        if (result.size() == 1) {
            this.mCallback.callback(result.get(0));
        } else if (result.size() > 1) {
            this.mCallback.onError(new IllegalStateException("More than one item returned."));
        } else {
            this.mCallback.onError(new IllegalStateException("No item returned."));
        }
    }

    @Override // com.microsoft.kapp.Callback
    public void onError(Exception ex) {
        this.mCallback.onError(ex);
    }
}

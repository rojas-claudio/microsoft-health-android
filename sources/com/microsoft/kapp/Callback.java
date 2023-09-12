package com.microsoft.kapp;
/* loaded from: classes.dex */
public interface Callback<T> {
    void callback(T t);

    void onError(Exception exc);
}

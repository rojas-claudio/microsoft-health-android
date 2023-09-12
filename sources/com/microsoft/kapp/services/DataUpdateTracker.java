package com.microsoft.kapp.services;
/* loaded from: classes.dex */
public interface DataUpdateTracker {
    void prepare();

    boolean waitForDataUpdate();
}

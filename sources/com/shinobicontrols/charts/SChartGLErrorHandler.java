package com.shinobicontrols.charts;
/* loaded from: classes.dex */
class SChartGLErrorHandler {
    void logMessageImpl(String msg) {
        cx.a(msg);
    }

    void handleErrorImpl(String msg) {
        cx.c(msg);
        throw new Error(msg);
    }
}

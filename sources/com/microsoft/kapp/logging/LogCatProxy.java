package com.microsoft.kapp.logging;

import android.util.Log;
/* loaded from: classes.dex */
public class LogCatProxy {
    public void d(String tag, String message, Throwable exception) {
        Log.d(tag, message, exception);
    }

    public void e(String tag, String message, Throwable exception) {
        Log.e(tag, message, exception);
    }

    public void i(String tag, String message, Throwable exception) {
        Log.i(tag, message, exception);
    }

    public void v(String tag, String message, Throwable exception) {
        Log.v(tag, message, exception);
    }

    public void w(String tag, String message, Throwable exception) {
        Log.w(tag, message, exception);
    }
}

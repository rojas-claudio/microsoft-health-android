package org.acra.log;

import android.util.Log;
/* loaded from: classes.dex */
public final class AndroidLogDelegate implements ACRALog {
    @Override // org.acra.log.ACRALog
    public int v(String tag, String msg) {
        return Log.v(tag, msg);
    }

    @Override // org.acra.log.ACRALog
    public int v(String tag, String msg, Throwable tr) {
        return Log.v(tag, msg, tr);
    }

    @Override // org.acra.log.ACRALog
    public int d(String tag, String msg) {
        return Log.d(tag, msg);
    }

    @Override // org.acra.log.ACRALog
    public int d(String tag, String msg, Throwable tr) {
        return Log.d(tag, msg, tr);
    }

    @Override // org.acra.log.ACRALog
    public int i(String tag, String msg) {
        return Log.i(tag, msg);
    }

    @Override // org.acra.log.ACRALog
    public int i(String tag, String msg, Throwable tr) {
        return Log.i(tag, msg, tr);
    }

    @Override // org.acra.log.ACRALog
    public int w(String tag, String msg) {
        return Log.w(tag, msg);
    }

    @Override // org.acra.log.ACRALog
    public int w(String tag, String msg, Throwable tr) {
        return Log.w(tag, msg, tr);
    }

    @Override // org.acra.log.ACRALog
    public int w(String tag, Throwable tr) {
        return Log.w(tag, tr);
    }

    @Override // org.acra.log.ACRALog
    public int e(String tag, String msg) {
        return Log.e(tag, msg);
    }

    @Override // org.acra.log.ACRALog
    public int e(String tag, String msg, Throwable tr) {
        return Log.e(tag, msg, tr);
    }

    @Override // org.acra.log.ACRALog
    public String getStackTraceString(Throwable tr) {
        return Log.getStackTraceString(tr);
    }
}

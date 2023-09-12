package com.microsoft.kapp.utils;

import com.microsoft.kapp.diagnostics.Compatibility;
import com.microsoft.kapp.logging.KLog;
/* loaded from: classes.dex */
public class Debug {
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !Debug.class.desiredAssertionStatus();
    }

    public static void Fail(String messageFormat, Object... args) {
        if (!$assertionsDisabled) {
            throw new AssertionError(String.format(messageFormat, args));
        }
    }

    public static void LogExceptionAndFail(String tag, Exception exception, String messageFormat, Object... args) {
        KLog.e(tag, String.format(messageFormat, args), exception);
        if (!Compatibility.isPublicRelease() && !$assertionsDisabled) {
            throw new AssertionError(exception);
        }
    }
}

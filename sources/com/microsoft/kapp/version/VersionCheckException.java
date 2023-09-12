package com.microsoft.kapp.version;
/* loaded from: classes.dex */
public class VersionCheckException extends Exception {
    private static final long serialVersionUID = 7853010856840586646L;

    public VersionCheckException(String detailMessage) {
        super(detailMessage);
    }

    public VersionCheckException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }
}

package com.microsoft.band;
/* loaded from: classes.dex */
public class BandIOException extends BandException {
    private static final long serialVersionUID = 4131752574718833761L;

    public BandIOException(String detailMessage, BandErrorType errorType) {
        super(detailMessage, errorType);
    }

    public BandIOException(String detailMessage, Throwable throwable, BandErrorType errorType) {
        super(detailMessage, throwable, errorType);
    }
}

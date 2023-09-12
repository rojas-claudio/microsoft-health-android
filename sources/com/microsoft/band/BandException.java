package com.microsoft.band;
/* loaded from: classes.dex */
public class BandException extends Exception {
    private static final long serialVersionUID = 179049556855660742L;
    private final BandErrorType mErrorType;

    public BandException(String detailMessage, BandErrorType errorType) {
        super(detailMessage);
        this.mErrorType = errorType;
    }

    public BandException(String detailMessage, Throwable throwable, BandErrorType errorType) {
        super(detailMessage, throwable);
        this.mErrorType = errorType;
    }

    public BandErrorType getErrorType() {
        return this.mErrorType;
    }
}

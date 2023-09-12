package com.facebook.android;
/* loaded from: classes.dex */
public class FacebookError extends RuntimeException {
    private static final long serialVersionUID = 1;
    private int mErrorCode;
    private String mErrorType;

    @Deprecated
    public FacebookError(String message) {
        super(message);
        this.mErrorCode = 0;
    }

    @Deprecated
    public FacebookError(String message, String type, int code) {
        super(message);
        this.mErrorCode = 0;
        this.mErrorType = type;
        this.mErrorCode = code;
    }

    @Deprecated
    public int getErrorCode() {
        return this.mErrorCode;
    }

    @Deprecated
    public String getErrorType() {
        return this.mErrorType;
    }
}

package com.microsoft.band.webtiles;
/* loaded from: classes.dex */
public class WebTileException extends Exception {
    private static final long serialVersionUID = 1;
    private final WebTileErrorType mErrorType;

    public WebTileException(Exception e) {
        super(e);
        this.mErrorType = WebTileErrorType.UNKNOWN_ERROR;
    }

    public WebTileException(String e) {
        super(e);
        this.mErrorType = WebTileErrorType.UNKNOWN_ERROR;
    }

    public WebTileException(String detailMessage, WebTileErrorType errorType) {
        super(detailMessage);
        this.mErrorType = errorType;
    }

    public WebTileErrorType getErrorType() {
        return this.mErrorType;
    }
}

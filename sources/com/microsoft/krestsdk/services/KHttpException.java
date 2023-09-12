package com.microsoft.krestsdk.services;

import java.io.IOException;
/* loaded from: classes.dex */
public class KHttpException extends IOException {
    private static final long serialVersionUID = -7485366101276399976L;
    private String mResponseBody;
    private int mStatusCode;
    private String mStatusReason;

    public KHttpException(int statusCode, String statusReason, String responseBody) {
        this.mStatusCode = statusCode;
        this.mStatusReason = statusReason;
        this.mResponseBody = responseBody;
    }

    public int getStatusCode() {
        return this.mStatusCode;
    }

    public String getStatusReason() {
        return this.mStatusReason;
    }

    public String getResponseBody() {
        return this.mResponseBody;
    }
}

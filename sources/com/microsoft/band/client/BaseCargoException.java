package com.microsoft.band.client;

import com.microsoft.band.internal.BandServiceMessage;
/* loaded from: classes.dex */
public abstract class BaseCargoException extends Exception {
    public static final String EXCEPTION = "Error, unhandled exception: %s";
    public static final String EXCEPTION_IO = "Cannot access cloud: %s";
    public static final String EXCEPTION_JSON = "Error parsing JSON formatted data: %s";
    public static final String EXCEPTION_RESPONSE_HEADER_WITH_STATUS = "Response is missing expected header: %s, Status(%s).";
    public static final String EXCEPTION_TIMEOUT = "Timeout when accessing cloud: %s";
    private static final long serialVersionUID = -3408329599597615785L;
    protected final BandServiceMessage.Response mResponse;

    public BaseCargoException(String msg, BandServiceMessage.Response response) {
        super(msg);
        this.mResponse = response;
    }

    public BaseCargoException(String msg, Throwable cause, BandServiceMessage.Response response) {
        super(msg, cause);
        this.mResponse = response;
    }

    public BandServiceMessage.Response getResponse() {
        return this.mResponse == null ? BandServiceMessage.Response.SERVICE_COMMAND_ERROR : this.mResponse;
    }
}

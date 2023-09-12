package com.microsoft.band.client;

import com.microsoft.band.internal.BandServiceMessage;
/* loaded from: classes.dex */
public class CargoException extends BaseCargoException {
    private static final long serialVersionUID = -5931640873207040590L;

    public CargoException(String msg, BandServiceMessage.Response response) {
        super(msg, response);
    }

    public CargoException(String msg, Throwable cause, BandServiceMessage.Response response) {
        super(msg, cause, response);
    }
}

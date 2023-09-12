package com.microsoft.band.service;

import com.microsoft.band.client.BaseCargoException;
import com.microsoft.band.internal.BandServiceMessage;
/* loaded from: classes.dex */
public class CargoServiceException extends BaseCargoException {
    private static final long serialVersionUID = 9026980051524279966L;

    public CargoServiceException(String msg, BandServiceMessage.Response response) {
        super(msg, response);
    }

    public CargoServiceException(String msg, Throwable cause, BandServiceMessage.Response response) {
        super(msg, cause, response);
    }
}

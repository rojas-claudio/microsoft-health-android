package com.microsoft.band.service;

import android.content.Context;
import com.microsoft.band.internal.BandServiceMessage;
/* loaded from: classes.dex */
public abstract class CargoServiceProvider {
    public static final long MILLISECONDS_IN_AN_HOUR = 3600000;

    public Context getContext() throws CargoServiceException {
        BandService serviceContext = BandService.getInstance();
        if (serviceContext == null || serviceContext.isTerminating()) {
            throw new CargoServiceException("Service terminated.", BandServiceMessage.Response.SERVICE_TERMINATED_ERROR);
        }
        return serviceContext;
    }
}

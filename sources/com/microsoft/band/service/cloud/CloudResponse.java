package com.microsoft.band.service.cloud;

import com.microsoft.band.internal.BandServiceMessage;
import com.microsoft.band.service.CargoServiceException;
import org.apache.http.HttpResponse;
/* loaded from: classes.dex */
public interface CloudResponse<T> {
    HttpResponse getHttpResponse();

    BandServiceMessage.Response getResponse() throws CargoServiceException;

    T getResponseData() throws CargoServiceException;
}

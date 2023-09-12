package com.microsoft.band.service.cloud;

import com.microsoft.band.service.CargoServiceException;
import org.apache.http.Header;
import org.apache.http.params.HttpParams;
/* loaded from: classes.dex */
public interface CloudRequest {
    CloudResponse<?> execute() throws CargoServiceException;

    Header[] getHeaders();

    HttpParams getHttpParams();
}

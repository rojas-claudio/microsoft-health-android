package com.microsoft.band.service.cloud;

import com.microsoft.band.internal.BandServiceMessage;
import com.microsoft.band.internal.util.KDKLog;
import com.microsoft.band.service.CargoServiceException;
import org.apache.http.HttpResponse;
/* loaded from: classes.dex */
public abstract class CloudResponseBase<T> implements CloudResponse<T> {
    private static final String TAG = CloudResponseBase.class.getSimpleName();
    private final HttpResponse mHttpResponse;
    protected final String mUri;

    @Override // com.microsoft.band.service.cloud.CloudResponse
    public abstract BandServiceMessage.Response getResponse() throws CargoServiceException;

    @Override // com.microsoft.band.service.cloud.CloudResponse
    public abstract T getResponseData() throws CargoServiceException;

    public CloudResponseBase(HttpResponse httpResponse, String uri) {
        this.mHttpResponse = httpResponse;
        this.mUri = uri;
        KDKLog.d(TAG, "Response URI=%s StatusLine=%s", this.mUri, this.mHttpResponse.getStatusLine());
    }

    @Override // com.microsoft.band.service.cloud.CloudResponse
    public HttpResponse getHttpResponse() {
        return this.mHttpResponse;
    }
}

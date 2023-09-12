package com.microsoft.band.service.cloud;

import android.os.Build;
import com.microsoft.band.client.BaseCargoException;
import com.microsoft.band.cloud.CargoServiceInfo;
import com.microsoft.band.cloud.CloudJSONDataModel;
import com.microsoft.band.internal.BandServiceMessage;
import com.microsoft.band.internal.util.KDKLog;
import com.microsoft.band.service.CargoServiceException;
import com.microsoft.kapp.utils.Constants;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.security.InvalidParameterException;
import java.util.Timer;
import java.util.TimerTask;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONException;
/* loaded from: classes.dex */
public abstract class CloudRequestBase implements CloudRequest {
    private static final String DEFAULT_SDK_VERSION = "1.0.0.0";
    protected static final String HEADER_USER_AGENT = "User-Agent";
    private static final String HTTPS = "HTTPS";
    private static final int HTTP_DEFAULT_SO_TIMEOUT = 60000;
    protected static final int HTTP_DEFAULT_TIMEOUT_PADDING = 5000;
    private static final String TAG = CloudRequestBase.class.getSimpleName();
    private final CargoServiceInfo mCargoServiceInfo;
    protected HttpParams mHttpParams;
    protected HttpRequestBase mHttpRequest;

    @Override // com.microsoft.band.service.cloud.CloudRequest
    public abstract CloudResponse<?> execute() throws CargoServiceException;

    protected abstract void setEntity() throws CargoServiceException;

    protected abstract void setHttpRequest() throws CargoServiceException;

    public CloudRequestBase(CargoServiceInfo cargoServiceInfo) {
        this.mCargoServiceInfo = cargoServiceInfo;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public CargoServiceInfo getCargoServiceInfo() {
        return this.mCargoServiceInfo;
    }

    protected void addHttpParams() {
        HttpConnectionParams.setSoTimeout(this.mHttpParams, getSocketTimeout());
    }

    protected HttpClient getHttpClient(HttpParams httpParams) {
        return new DefaultHttpClient(httpParams);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public HttpResponse executeRequest() throws CargoServiceException {
        return doRequest();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void addHeaders() throws CargoServiceException {
        if (addAuthHeader()) {
            if (this.mCargoServiceInfo != null) {
                this.mHttpRequest.addHeader(Constants.AUTHORIZATION_HEADER_NAME, this.mCargoServiceInfo.getAccessToken());
            } else {
                throw new InvalidParameterException("CargoServiceInfo should not be null if adding auth header");
            }
        }
        if (addUserAgent()) {
            if (this.mCargoServiceInfo != null && this.mCargoServiceInfo.getUserAgent() != null && !this.mCargoServiceInfo.getUserAgent().isEmpty()) {
                this.mHttpRequest.setHeader(HEADER_USER_AGENT, this.mCargoServiceInfo.getUserAgent());
            } else {
                this.mHttpRequest.setHeader(HEADER_USER_AGENT, getUserAgent());
            }
        }
    }

    protected static String getUserAgent() {
        return String.format("KDK/%s (Android/%s; Brand/%s; Model/%s; Device/%s)", DEFAULT_SDK_VERSION, Integer.valueOf(Build.VERSION.SDK_INT), Build.BRAND, Build.MODEL, Build.DEVICE);
    }

    protected void setHttpParams() {
        this.mHttpParams = new BasicHttpParams();
    }

    protected int getSocketTimeout() {
        return 60000;
    }

    private void initRequest() throws CargoServiceException {
        setHttpRequest();
        setHttpParams();
        KDKLog.d(TAG, "Request URL: %s", this.mHttpRequest.getURI());
        addHeaders();
        Header[] arr$ = this.mHttpRequest.getAllHeaders();
        for (Header header : arr$) {
            KDKLog.d(TAG, "RequestHeader: %s::%s", header.getName(), header.getValue());
        }
        setEntity();
    }

    private HttpResponse doRequest() throws CargoServiceException {
        initRequest();
        addHttpParams();
        if (!HTTPS.equalsIgnoreCase(this.mHttpRequest.getURI().getScheme())) {
            throw new IllegalArgumentException("CloudRequestBase must be https");
        }
        try {
            addRequestTimeout(this.mHttpRequest, getSocketTimeout() + 5000);
            HttpResponse response = getHttpClient(this.mHttpParams).execute(this.mHttpRequest);
            return response;
        } catch (SocketTimeoutException e) {
            throw new CargoServiceException(String.format(BaseCargoException.EXCEPTION_TIMEOUT, e.getMessage()), e, BandServiceMessage.Response.SERVICE_CLOUD_REQUEST_TIMEOUT_ERROR);
        } catch (IOException e2) {
            if (this.mHttpRequest.isAborted()) {
                throw new CargoServiceException(String.format(BaseCargoException.EXCEPTION_TIMEOUT, e2.getMessage()), e2, BandServiceMessage.Response.SERVICE_CLOUD_REQUEST_TIMEOUT_ERROR);
            }
            throw new CargoServiceException(String.format(BaseCargoException.EXCEPTION_IO, e2.getMessage()), e2, BandServiceMessage.Response.SERVICE_CLOUD_REQUEST_FAILED_ERROR);
        } catch (Exception e3) {
            throw new CargoServiceException(String.format(BaseCargoException.EXCEPTION, e3.getMessage()), e3, BandServiceMessage.Response.SERVICE_CLOUD_DATA_ERROR);
        }
    }

    private void addRequestTimeout(final HttpUriRequest request, int timeout) {
        TimerTask task = new TimerTask() { // from class: com.microsoft.band.service.cloud.CloudRequestBase.1
            @Override // java.util.TimerTask, java.lang.Runnable
            public void run() {
                if (request != null) {
                    try {
                        KDKLog.d(CloudRequestBase.TAG, "Attempting Abort on request");
                        request.abort();
                    } catch (UnsupportedOperationException e) {
                        KDKLog.d(CloudRequestBase.TAG, "Unable to abort request: %s", e.getMessage());
                    }
                }
            }
        };
        new Timer(true).schedule(task, timeout);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static <T extends CloudJSONDataModel> String getCleanedJsonString(T obj) throws CargoServiceException {
        try {
            String s = obj.toJSONString().replaceAll("\\\\", "");
            return s.replace("\"{", "{").replace("}\"", "}");
        } catch (JSONException e) {
            throw new CargoServiceException("Error creating JSON formatted StringEntity", e, BandServiceMessage.Response.SERVICE_CLOUD_DATA_ERROR);
        } catch (Exception e2) {
            throw new CargoServiceException("Error to clean json string ", e2, BandServiceMessage.Response.SERVICE_CLOUD_DATA_ERROR);
        }
    }

    protected boolean addUserAgent() {
        return true;
    }

    protected boolean addAuthHeader() {
        return true;
    }

    @Override // com.microsoft.band.service.cloud.CloudRequest
    public Header[] getHeaders() {
        if (this.mHttpRequest == null) {
            return null;
        }
        return this.mHttpRequest.getAllHeaders();
    }

    @Override // com.microsoft.band.service.cloud.CloudRequest
    public HttpParams getHttpParams() {
        return this.mHttpParams;
    }
}

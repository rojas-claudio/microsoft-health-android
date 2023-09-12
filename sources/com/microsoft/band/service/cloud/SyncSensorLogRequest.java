package com.microsoft.band.service.cloud;

import com.microsoft.band.cloud.CargoServiceInfo;
import com.microsoft.band.internal.BandServiceMessage;
import com.microsoft.band.internal.util.KDKLog;
import com.microsoft.band.service.CargoServiceException;
import java.net.URI;
import java.net.URISyntaxException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
/* loaded from: classes.dex */
public class SyncSensorLogRequest extends CloudRequestBase {
    protected static final String APPLICATION_OCTET_STREAM = "application/octet-stream";
    protected static final String HEADER_NAME_MSBLOB_TYPE = "x-ms-blob-type";
    protected static final String HEADER_UPLOAD_ID = "UploadId";
    protected static final String HEADER_UPLOAD_METADATA = "UploadMetaData";
    protected static final String HEADER_VAL_BLOCKBLOB = "BlockBlob";
    protected static final int HTTP_UPLOAD_FILE_SOCKET_TIMEOUT = 120000;
    protected static final String PUT_SYNC_SENSOR_LOG_ENDPOINT = "v2/MultiDevice/UploadSensorPayload?logType=%s";
    private static final String TAG = SyncSensorLogRequest.class.getSimpleName();
    private static ByteArrayEntity mEntity;
    private final CloudDataResource mCdr;

    public SyncSensorLogRequest(CargoServiceInfo apiInfo, CloudDataResource cdr, byte[] data) {
        super(apiInfo);
        mEntity = new ByteArrayEntity(data);
        this.mCdr = cdr;
    }

    @Override // com.microsoft.band.service.cloud.CloudRequestBase, com.microsoft.band.service.cloud.CloudRequest
    public CloudResponse<?> execute() throws CargoServiceException {
        return new SyncSensorLogResponse(executeRequest(), this.mCdr.getLogFileType(), this.mHttpRequest.getURI().toString());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.microsoft.band.service.cloud.CloudRequestBase
    public void addHeaders() throws CargoServiceException {
        super.addHeaders();
        this.mHttpRequest.addHeader(HEADER_NAME_MSBLOB_TYPE, HEADER_VAL_BLOCKBLOB);
        this.mHttpRequest.addHeader(HEADER_UPLOAD_ID, this.mCdr.getUploadId());
        this.mHttpRequest.addHeader(HEADER_UPLOAD_METADATA, getCleanedJsonString(this.mCdr.getMetaData()));
        this.mHttpRequest.addHeader("Content-Type", APPLICATION_OCTET_STREAM);
    }

    @Override // com.microsoft.band.service.cloud.CloudRequestBase
    protected void setEntity() throws CargoServiceException {
        this.mHttpRequest.setEntity(mEntity);
        KDKLog.d(TAG, "Entity size=%s", Long.valueOf(mEntity.getContentLength()));
    }

    @Override // com.microsoft.band.service.cloud.CloudRequestBase
    protected void setHttpRequest() throws CargoServiceException {
        this.mHttpRequest = new HttpPost(getUri().toString());
    }

    private URI getUri() throws CargoServiceException {
        String syncSensorEndpoint = String.format(PUT_SYNC_SENSOR_LOG_ENDPOINT, this.mCdr.getLogFileType().name());
        String url = getCargoServiceInfo().getUrl().toExternalForm();
        if (!url.endsWith("/")) {
            url = url + "/";
        }
        String syncSensorLogUrl = url + syncSensorEndpoint;
        try {
            return new URI(syncSensorLogUrl);
        } catch (URISyntaxException e) {
            throw new CargoServiceException(String.format("Endpoint '%s' is malformed.", syncSensorLogUrl), e, BandServiceMessage.Response.SERVICE_CLOUD_INVALID_URL_ERROR);
        }
    }

    @Override // com.microsoft.band.service.cloud.CloudRequestBase
    protected int getSocketTimeout() {
        return HTTP_UPLOAD_FILE_SOCKET_TIMEOUT;
    }
}

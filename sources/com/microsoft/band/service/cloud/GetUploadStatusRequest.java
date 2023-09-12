package com.microsoft.band.service.cloud;

import com.microsoft.band.cloud.CargoServiceInfo;
import com.microsoft.band.internal.BandServiceMessage;
import com.microsoft.band.service.CargoServiceException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import org.apache.http.client.methods.HttpGet;
/* loaded from: classes.dex */
public class GetUploadStatusRequest extends CloudRequestBase {
    protected static final String GET_UPLOAD_STATUS_ENDPOINT = "v2/MultiDevice/GetUploadStatus?uploadIds=%s";
    private final String mUploadIds;

    public GetUploadStatusRequest(CargoServiceInfo apiInfo, List<CloudDataResource> cdrList) {
        super(apiInfo);
        this.mUploadIds = getUploadIds(cdrList);
    }

    private String getUploadIds(List<CloudDataResource> cdrList) {
        StringBuilder sb = new StringBuilder();
        int count = 0;
        for (CloudDataResource cdr : cdrList) {
            sb.append(cdr.getUploadId());
            if (count < cdrList.size() - 1) {
                sb.append(',');
            }
            count++;
        }
        return sb.toString();
    }

    @Override // com.microsoft.band.service.cloud.CloudRequestBase, com.microsoft.band.service.cloud.CloudRequest
    public CloudResponse<?> execute() throws CargoServiceException {
        return new GetUploadStatusResponse(executeRequest(), this.mHttpRequest.getURI().toString());
    }

    @Override // com.microsoft.band.service.cloud.CloudRequestBase
    protected void setEntity() throws CargoServiceException {
    }

    @Override // com.microsoft.band.service.cloud.CloudRequestBase
    protected void setHttpRequest() throws CargoServiceException {
        this.mHttpRequest = new HttpGet(getUri().toString());
    }

    private URI getUri() throws CargoServiceException {
        String syncSensorEndpoint = String.format(GET_UPLOAD_STATUS_ENDPOINT, this.mUploadIds);
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
}

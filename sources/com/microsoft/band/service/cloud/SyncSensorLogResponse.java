package com.microsoft.band.service.cloud;

import com.microsoft.band.internal.BandServiceMessage;
import com.microsoft.band.internal.util.KDKLog;
import com.microsoft.band.service.CargoServiceException;
import com.microsoft.band.service.cloud.CloudDataResource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
/* loaded from: classes.dex */
public class SyncSensorLogResponse extends CloudResponseBase<String> {
    public static String TAG = SyncSensorLogResponse.class.getSimpleName();
    private final CloudDataResource.LogFileTypes mLogFileType;

    public SyncSensorLogResponse(HttpResponse httpResponse, CloudDataResource.LogFileTypes logFileType, String uri) {
        super(httpResponse, uri);
        this.mLogFileType = logFileType;
    }

    @Override // com.microsoft.band.service.cloud.CloudResponseBase, com.microsoft.band.service.cloud.CloudResponse
    public BandServiceMessage.Response getResponse() {
        StatusLine responseStatus = getHttpResponse().getStatusLine();
        KDKLog.d(TAG, "Upload logtype=%s", this.mLogFileType.toString());
        int statusCode = responseStatus.getStatusCode();
        if (statusCode == 200) {
            return BandServiceMessage.Response.SUCCESS;
        }
        if (this.mLogFileType == CloudDataResource.LogFileTypes.BANDBINARY && responseStatus.getStatusCode() == 202) {
            return BandServiceMessage.Response.PENDING;
        }
        parseError();
        if (statusCode == 417) {
            return BandServiceMessage.Response.SERVICE_CLOUD_UPLOAD_DATA_CORRUPT;
        }
        return BandServiceMessage.Response.SERVICE_CLOUD_REQUEST_FAILED_ERROR;
    }

    @Override // com.microsoft.band.service.cloud.CloudResponseBase, com.microsoft.band.service.cloud.CloudResponse
    public String getResponseData() throws CargoServiceException {
        throw new UnsupportedOperationException("No response data for SyncSensorLogResponse");
    }

    private void parseError() {
        HttpResponse response = getHttpResponse();
        StringBuilder builder = new StringBuilder();
        String exceptionMessage = null;
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
            while (true) {
                String line = reader.readLine();
                if (line == null) {
                    break;
                }
                builder.append(line).append("\n");
            }
        } catch (UnsupportedEncodingException e) {
            exceptionMessage = e.getMessage();
        } catch (IOException e2) {
            exceptionMessage = e2.getMessage();
        } catch (IllegalStateException e3) {
            exceptionMessage = e3.getMessage();
        }
        if (exceptionMessage != null) {
            KDKLog.d(TAG, "Exception while parseing error e=", exceptionMessage);
        } else {
            KDKLog.d(TAG, "Error Response : %s", builder.toString());
        }
    }
}

package com.microsoft.band.service.cloud;

import com.microsoft.band.internal.BandServiceMessage;
import com.microsoft.band.internal.util.KDKLog;
import com.microsoft.band.service.CargoServiceException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashSet;
import java.util.Set;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
/* loaded from: classes.dex */
public class GetUploadStatusResponse extends CloudResponseBase<Set<String>> {
    private static final String EVENTS_PROCESSING_DONE = "EventsProcessingDone";
    private static final String TAG = GetUploadStatusResponse.class.getSimpleName();
    private static final String UPLOAD_DONE = "UploadDone";
    private static final String UPLOAD_STATUS = "UploadStatus";
    Set<String> mCompletedUploadIds;
    boolean mJsonParsed;

    public GetUploadStatusResponse(HttpResponse httpResponse, String uri) {
        super(httpResponse, uri);
        this.mCompletedUploadIds = new HashSet();
        this.mJsonParsed = false;
    }

    @Override // com.microsoft.band.service.cloud.CloudResponseBase, com.microsoft.band.service.cloud.CloudResponse
    public BandServiceMessage.Response getResponse() throws CargoServiceException {
        StatusLine responseStatus = getHttpResponse().getStatusLine();
        if (responseStatus.getStatusCode() == 200) {
            try {
                if (!this.mJsonParsed) {
                    parseJson();
                    this.mJsonParsed = true;
                }
                return BandServiceMessage.Response.SUCCESS;
            } catch (UnsupportedEncodingException e) {
                throw new CargoServiceException(e.getMessage(), BandServiceMessage.Response.SERVICE_CLOUD_DATA_ERROR);
            } catch (IOException e2) {
                throw new CargoServiceException(e2.getMessage(), BandServiceMessage.Response.SERVICE_FILE_IO_ERROR);
            } catch (IllegalStateException e3) {
                throw new CargoServiceException(e3.getMessage(), BandServiceMessage.Response.SERVICE_CLOUD_DATA_ERROR);
            } catch (JSONException e4) {
                throw new CargoServiceException(e4.getMessage(), BandServiceMessage.Response.SERVICE_CLOUD_DATA_ERROR);
            }
        }
        return BandServiceMessage.Response.SERVICE_CLOUD_REQUEST_FAILED_ERROR;
    }

    private void parseJson() throws UnsupportedEncodingException, IllegalStateException, IOException, JSONException {
        HttpResponse response = getHttpResponse();
        BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
        StringBuilder builder = new StringBuilder();
        while (true) {
            String line = reader.readLine();
            if (line == null) {
                break;
            }
            builder.append(line).append("\n");
        }
        String responseString = builder.toString();
        KDKLog.d(TAG, "GetUploadStatus %s", responseString);
        JSONObject json = new JSONObject(responseString);
        JSONArray names = json.names();
        for (int i = 0; i < names.length(); i++) {
            String uploadId = names.getString(i);
            JSONObject uploadInfo = json.getJSONObject(uploadId);
            String status = uploadInfo.getString(UPLOAD_STATUS);
            if (EVENTS_PROCESSING_DONE.equalsIgnoreCase(status) || UPLOAD_DONE.equalsIgnoreCase(status)) {
                this.mCompletedUploadIds.add(uploadId);
            }
        }
        KDKLog.d(TAG, "JSON Response: %s", builder.toString());
    }

    @Override // com.microsoft.band.service.cloud.CloudResponseBase, com.microsoft.band.service.cloud.CloudResponse
    public Set<String> getResponseData() throws CargoServiceException {
        return this.mCompletedUploadIds;
    }
}

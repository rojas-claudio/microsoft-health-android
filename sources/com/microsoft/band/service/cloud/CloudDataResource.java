package com.microsoft.band.service.cloud;

import android.os.Parcel;
import android.os.Parcelable;
import com.microsoft.band.client.BaseCargoException;
import com.microsoft.band.client.CargoException;
import com.microsoft.band.cloud.CloudJSONDataModel;
import com.microsoft.band.internal.BandServiceMessage;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import org.json.JSONException;
import org.json.JSONObject;
/* loaded from: classes.dex */
public class CloudDataResource extends CloudJSONDataModel {
    private static final String JSON_KEY_COMMITTED = "Committed";
    private static final String JSON_KEY_LOCATION = "Location";
    private static final String JSON_KEY_LOG_TYPE = "LogType";
    private static final String JSON_KEY_META_DATA = "UploadMetadata";
    private static final String JSON_KEY_SIZE_IN_KB = "UploadSizeInKb";
    private static final String JSON_KEY_UPLOAD_ID = "UploadId";
    private static final String JSON_KEY_UPLOAD_STATUS = "UploadStatus";
    private static final long serialVersionUID = 1;
    private boolean mCommitted;
    private String mLocation;
    private LogFileTypes mLogFileType;
    private UploadMetadata mMetaData;
    private UploadStatus mStatus;
    private String mUploadId;
    private float mUploadSizeInKb;
    public static final String CLOUD_UPLOADID_FORMAT = "yyyyMMddHHmmssSSS";
    public static final SimpleDateFormat UPLOAD_ID_FORMATTER = new SimpleDateFormat(CLOUD_UPLOADID_FORMAT, Locale.US);
    public static final Parcelable.Creator<CloudDataResource> CREATOR = new Parcelable.Creator<CloudDataResource>() { // from class: com.microsoft.band.service.cloud.CloudDataResource.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public CloudDataResource createFromParcel(Parcel in) {
            return new CloudDataResource(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public CloudDataResource[] newArray(int size) {
            return new CloudDataResource[size];
        }
    };

    /* loaded from: classes.dex */
    public enum UploadStatus {
        Unknown,
        UploadPathSent,
        UploadDone,
        QueuedForETL,
        ActivitiesProcessingDone,
        EventsProcessingDone,
        EventsProcessingBlocked
    }

    public CloudDataResource() {
    }

    public String getUploadId() {
        return this.mUploadId;
    }

    public void setUploadId(String uploadId) {
        this.mUploadId = uploadId;
    }

    public void setUploadId(Date time) {
        UPLOAD_ID_FORMATTER.setTimeZone(TimeZone.getTimeZone("UTC"));
        this.mUploadId = UPLOAD_ID_FORMATTER.format(time);
    }

    public LogFileTypes getLogFileType() {
        return this.mLogFileType;
    }

    private String getLogFileTypeString() {
        if (this.mLogFileType == null || this.mLogFileType.equals(LogFileTypes.UNKNOWN)) {
            return null;
        }
        return this.mLogFileType.name().toLowerCase();
    }

    private void setLogFileType(String logTypeString) {
        this.mLogFileType = LogFileTypes.valueOf(logTypeString.toUpperCase());
    }

    public void setLogFileType(LogFileTypes logFileType) {
        this.mLogFileType = logFileType;
    }

    public String getLocation() {
        return this.mLocation;
    }

    public void setLocation(String location) {
        this.mLocation = location;
    }

    public boolean isCommitted() {
        return this.mCommitted;
    }

    public void setCommitted(boolean committed) {
        this.mCommitted = committed;
    }

    public UploadMetadata getMetaData() {
        return this.mMetaData;
    }

    public void setMetaData(UploadMetadata metaData) {
        this.mMetaData = metaData;
    }

    public UploadStatus getStatus() {
        return this.mStatus;
    }

    private String getStatusString() {
        if (this.mStatus == null) {
            return null;
        }
        return this.mStatus.toString();
    }

    public void setStatus(UploadStatus status) {
        this.mStatus = status;
    }

    private void setStatus(String status) {
        this.mStatus = UploadStatus.valueOf(status);
    }

    public float getUploadSizeInKb() {
        return this.mUploadSizeInKb;
    }

    public void setUploadSizeInKb(float uploadSizeInKb) {
        this.mUploadSizeInKb = uploadSizeInKb;
    }

    /* loaded from: classes.dex */
    public enum LogFileTypes {
        UNKNOWN(0),
        PHONEGENERAL(1),
        LUMIA(2),
        APPLEPHONE(3),
        ANDROID(4),
        BANDBINARY(5),
        CRASHDUMP(6),
        APPDUMP(7),
        TELEMETRY(8),
        PERFLOGS(9);
        
        private int mType;

        LogFileTypes(int in) {
            this.mType = in;
        }

        public int getType() {
            return this.mType;
        }

        public static LogFileTypes lookup(int type) {
            LogFileTypes[] arr$ = values();
            for (LogFileTypes s : arr$) {
                if (s.getType() == type) {
                    return s;
                }
            }
            return UNKNOWN;
        }

        public static LogFileTypes lookup(String typeString) {
            return valueOf(typeString);
        }
    }

    @Override // com.microsoft.band.cloud.CloudJSONDataModel
    public String toJSONString() throws JSONException {
        JSONObject cloudDataResource = new JSONObject();
        cloudDataResource.put(JSON_KEY_UPLOAD_ID, this.mUploadId);
        cloudDataResource.put(JSON_KEY_LOG_TYPE, getLogFileTypeString());
        cloudDataResource.put(JSON_KEY_LOCATION, this.mLocation);
        cloudDataResource.put(JSON_KEY_COMMITTED, this.mCommitted);
        cloudDataResource.put(JSON_KEY_META_DATA, this.mMetaData == null ? null : this.mMetaData.toJSONString());
        cloudDataResource.put(JSON_KEY_SIZE_IN_KB, this.mUploadSizeInKb);
        cloudDataResource.put(JSON_KEY_UPLOAD_STATUS, getStatusString());
        String json = cloudDataResource.toString();
        return json;
    }

    @Override // com.microsoft.band.cloud.CloudJSONDataModel
    protected void initWithJSONObject(JSONObject json) throws CargoException {
        try {
            setUploadId(json.getString(JSON_KEY_UPLOAD_ID));
            setLogFileType(json.getString(JSON_KEY_LOG_TYPE));
            setLocation(json.getString(JSON_KEY_LOCATION));
            setCommitted(json.getBoolean(JSON_KEY_COMMITTED));
            setUploadSizeInKb((float) json.getDouble(JSON_KEY_SIZE_IN_KB));
            setStatus(json.getString(JSON_KEY_UPLOAD_STATUS));
        } catch (JSONException e) {
            throw new CargoException(String.format(BaseCargoException.EXCEPTION, e.getMessage()), e, BandServiceMessage.Response.SERVICE_CLOUD_DATA_ERROR);
        }
    }

    public final boolean isProcessingCompleted() {
        return getStatus() != null && getStatus().ordinal() == UploadStatus.EventsProcessingDone.ordinal();
    }

    public final boolean isProcessingBlocked() {
        return getStatus() != null && getStatus().ordinal() >= UploadStatus.EventsProcessingBlocked.ordinal();
    }

    public static CloudDataResource getCloudDataResource(String data) throws CargoException {
        CloudDataResource cdr = new CloudDataResource();
        cdr.initWithJSONString(data);
        return cdr;
    }

    protected CloudDataResource(Parcel in) {
        super(in);
    }
}

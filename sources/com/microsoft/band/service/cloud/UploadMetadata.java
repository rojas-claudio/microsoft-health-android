package com.microsoft.band.service.cloud;

import android.os.Build;
import com.microsoft.band.client.BaseCargoException;
import com.microsoft.band.client.CargoException;
import com.microsoft.band.cloud.CloudJSONDataModel;
import com.microsoft.band.internal.BandServiceMessage;
import com.microsoft.band.internal.device.DeviceInfo;
import com.microsoft.kapp.logging.LogConstants;
import com.microsoft.kapp.utils.Constants;
import java.util.Date;
import java.util.TimeZone;
import org.joda.time.DateTimeConstants;
import org.json.JSONException;
import org.json.JSONObject;
/* loaded from: classes.dex */
public class UploadMetadata extends CloudJSONDataModel {
    private static final String JSON_KEY_COMPRESSED_FILE_CRC = "CompressedFileCRC";
    private static final String JSON_KEY_COMPRESSION_ALGO = "CompressionAlgorithm";
    private static final String JSON_KEY_DEVICE_ID = "DeviceId";
    private static final String JSON_KEY_DEVICE_VER = "DeviceVersion";
    private static final String JSON_KEY_END_SEQ_ID = "EndSequenceId";
    private static final String JSON_KEY_HOSTOS = "HostOS";
    private static final String JSON_KEY_HOSTOS_VER = "HostOSVersion";
    private static final String JSON_KEY_HOST_APP_VER = "HostAppVersion";
    private static final String JSON_KEY_LOG_VERSION = "LogVersion";
    private static final String JSON_KEY_PCBID = "Pcbid";
    private static final String JSON_KEY_SERIAL_NUMBER = "DeviceSerialNumber";
    private static final String JSON_KEY_START_SEQ_ID = "StartSequenceId";
    private static final String JSON_KEY_UTC_TZ_OFF = "UTCTimeZoneOffsetInMinutes";
    private static final long serialVersionUID = 1;
    private String mCompressedFileCrc;
    private String mCompressionAlgorithm;
    private String mDeviceId;
    private String mDeviceVersion;
    private int mEndSequenceId;
    private String mHostAppVersion;
    private String mHostOs;
    private String mHostOsVersion;
    private int mLogVersion;
    private int mPcbId;
    private String mSerialNumber;
    private int mStartSequenceId;
    private int mUtcOffsetMinutes;
    private static int MILLISECS_PER_MINUTE = DateTimeConstants.MILLIS_PER_MINUTE;
    private static String HOST_OS_NAME = Constants.ANDROID_PHONE_IDENTIFIER;

    public UploadMetadata() {
        this.mHostOs = HOST_OS_NAME;
        this.mHostOsVersion = Build.VERSION.RELEASE;
        this.mHostAppVersion = LogConstants.METADATA_VERSION;
        this.mDeviceVersion = LogConstants.METADATA_VERSION;
        this.mCompressionAlgorithm = LogCompressionAlgorithm.uncompressed.name();
        this.mCompressedFileCrc = "";
        this.mStartSequenceId = 0;
        this.mEndSequenceId = 0;
        this.mUtcOffsetMinutes = 0;
        this.mLogVersion = 0;
        this.mDeviceId = "";
        this.mSerialNumber = "";
        this.mPcbId = 0;
        TimeZone zone = TimeZone.getDefault();
        Date now = new Date();
        int timeZoneOffset = zone.getOffset(now.getTime());
        this.mUtcOffsetMinutes = timeZoneOffset / MILLISECS_PER_MINUTE;
    }

    public UploadMetadata(JSONObject json) throws CargoException {
        this();
        initWithJSONObject(json);
    }

    @Override // com.microsoft.band.cloud.CloudJSONDataModel
    public String toJSONString() throws JSONException {
        JSONObject json = new JSONObject();
        json.put(JSON_KEY_HOSTOS, this.mHostOs);
        json.put(JSON_KEY_HOSTOS_VER, this.mHostOsVersion);
        json.put(JSON_KEY_HOST_APP_VER, this.mHostAppVersion);
        json.put(JSON_KEY_DEVICE_VER, this.mDeviceVersion);
        json.put(JSON_KEY_COMPRESSION_ALGO, this.mCompressionAlgorithm);
        json.put(JSON_KEY_START_SEQ_ID, this.mStartSequenceId);
        json.put(JSON_KEY_END_SEQ_ID, this.mEndSequenceId);
        json.put(JSON_KEY_UTC_TZ_OFF, this.mUtcOffsetMinutes);
        json.put(JSON_KEY_COMPRESSED_FILE_CRC, this.mCompressedFileCrc);
        json.put(JSON_KEY_LOG_VERSION, this.mLogVersion);
        json.put(JSON_KEY_DEVICE_ID, this.mDeviceId);
        json.put(JSON_KEY_SERIAL_NUMBER, this.mSerialNumber);
        json.put(JSON_KEY_PCBID, this.mPcbId);
        return json.toString();
    }

    public void setDeviceMetadata(DeviceInfo deviceInfo) {
        if (deviceInfo != null && deviceInfo.getDeviceUUID() != null) {
            this.mDeviceId = deviceInfo.getDeviceUUID().toString();
            this.mDeviceVersion = deviceInfo.getFWVersion();
            this.mLogVersion = deviceInfo.getLogVersion();
            this.mSerialNumber = deviceInfo.getSerialNumber();
            this.mPcbId = deviceInfo.getHardwareVersion();
        }
    }

    public void setStartSequenceId(long beginSeqId) {
        this.mStartSequenceId = (int) beginSeqId;
    }

    public void setEndSequenceId(long endSeqId) {
        this.mEndSequenceId = (int) endSeqId;
    }

    public int getStartSequenceId() {
        return this.mStartSequenceId;
    }

    public int getEndSequenceId() {
        return this.mEndSequenceId;
    }

    public void setAppVersion(int appVersion) {
        this.mHostAppVersion = Integer.toString(appVersion);
    }

    public void setCompressionAlgorithm(LogCompressionAlgorithm algorithm) {
        if (algorithm == null) {
            this.mCompressionAlgorithm = LogCompressionAlgorithm.uncompressed.name();
        } else {
            this.mCompressionAlgorithm = algorithm.name();
        }
    }

    public void setCompressionAlgorithm(String algorithm) {
        if (algorithm == null) {
            this.mCompressionAlgorithm = LogCompressionAlgorithm.uncompressed.name();
        } else {
            this.mCompressionAlgorithm = LogCompressionAlgorithm.lookup(algorithm).name();
        }
    }

    public void setCompressedFileCRC(String crc) {
        this.mCompressedFileCrc = crc;
    }

    protected String getCompressedFileCRC() {
        return this.mCompressedFileCrc;
    }

    @Override // com.microsoft.band.cloud.CloudJSONDataModel
    protected void initWithJSONObject(JSONObject json) throws CargoException {
        try {
            if (json.has(JSON_KEY_START_SEQ_ID)) {
                setStartSequenceId(json.getInt(JSON_KEY_START_SEQ_ID));
            }
            if (json.has(JSON_KEY_END_SEQ_ID)) {
                setEndSequenceId(json.getInt(JSON_KEY_END_SEQ_ID));
            }
            if (json.has(JSON_KEY_UTC_TZ_OFF)) {
                this.mUtcOffsetMinutes = json.getInt(JSON_KEY_UTC_TZ_OFF);
            }
            if (json.has(JSON_KEY_LOG_VERSION)) {
                this.mLogVersion = json.getInt(JSON_KEY_LOG_VERSION);
            }
            if (json.has(JSON_KEY_COMPRESSION_ALGO)) {
                setCompressionAlgorithm(json.getString(JSON_KEY_COMPRESSION_ALGO));
            }
            if (json.has(JSON_KEY_DEVICE_ID)) {
                this.mDeviceId = json.getString(JSON_KEY_DEVICE_ID);
            }
            if (json.has(JSON_KEY_SERIAL_NUMBER)) {
                this.mSerialNumber = json.getString(JSON_KEY_SERIAL_NUMBER);
            }
            if (json.has(JSON_KEY_DEVICE_VER)) {
                this.mDeviceVersion = json.getString(JSON_KEY_DEVICE_VER);
            }
            if (json.has(JSON_KEY_COMPRESSED_FILE_CRC)) {
                setCompressedFileCRC(json.getString(JSON_KEY_COMPRESSED_FILE_CRC));
            }
            if (json.has(JSON_KEY_PCBID)) {
                this.mPcbId = json.getInt(JSON_KEY_PCBID);
            }
            this.mHostOs = json.getString(JSON_KEY_HOSTOS);
            this.mHostOsVersion = json.getString(JSON_KEY_HOSTOS_VER);
            this.mHostAppVersion = json.getString(JSON_KEY_HOST_APP_VER);
        } catch (JSONException e) {
            throw new CargoException(String.format(BaseCargoException.EXCEPTION, e.getMessage()), e, BandServiceMessage.Response.SERVICE_CLOUD_DATA_ERROR);
        }
    }

    /* loaded from: classes.dex */
    public enum LogCompressionAlgorithm {
        winzip,
        uncompressed;

        public static LogCompressionAlgorithm lookup(String algorithm) {
            LogCompressionAlgorithm[] arr$ = values();
            for (LogCompressionAlgorithm s : arr$) {
                if (s.name().equalsIgnoreCase(algorithm)) {
                    return s;
                }
            }
            return uncompressed;
        }
    }
}

package com.microsoft.band.cloud;

import android.os.Parcel;
import android.os.Parcelable;
import com.facebook.internal.ServerProtocol;
import com.microsoft.band.client.BaseCargoException;
import com.microsoft.band.client.CargoException;
import com.microsoft.band.internal.BandServiceMessage;
import com.microsoft.band.internal.util.StringUtil;
import org.json.JSONException;
import org.json.JSONObject;
/* loaded from: classes.dex */
public class CargoFirmwareUpdateInfo extends CloudJSONDataModel {
    public static final Parcelable.Creator<CargoFirmwareUpdateInfo> CREATOR = new Parcelable.Creator<CargoFirmwareUpdateInfo>() { // from class: com.microsoft.band.cloud.CargoFirmwareUpdateInfo.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public CargoFirmwareUpdateInfo createFromParcel(Parcel in) {
            return new CargoFirmwareUpdateInfo(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public CargoFirmwareUpdateInfo[] newArray(int size) {
            return new CargoFirmwareUpdateInfo[size];
        }
    };
    private static final String JSON_KEY_CURRENT_VERSION = "CurrentVersion";
    private static final String JSON_KEY_DEVICE_FAMILY = "DeviceFamily";
    private static final String JSON_KEY_FALLBACK_URL = "FallbackUrl";
    private static final String JSON_KEY_FIRMWARE_VERSION = "FirmwareVersion";
    private static final String JSON_KEY_HASH_MD5 = "HashMd5";
    private static final String JSON_KEY_IS_FIRMWARE_UPDATE_AVAILABLE = "IsFirmwareUpdateAvailable";
    private static final String JSON_KEY_IS_FIRMWARE_UPDATE_OPTIONAL = "IsFirmwareUpdateOptional";
    private static final String JSON_KEY_MIRROR_URL = "MirrorUrl";
    private static final String JSON_KEY_PRIMARY_URL = "PrimaryUrl";
    private static final String JSON_KEY_SIZE_IN_BYTES = "SizeInBytes";
    private static final String JSON_KEY_UNIQUE_VERSION = "UniqueVersion";
    private static final long serialVersionUID = 1;
    private String mCurrentVersion;
    private String mDeviceFamily;
    private String mFallbackUrl;
    private String mFirmwareVersion;
    private String mHashMd5;
    private boolean mIsFirmwareUpdateAvailable;
    private boolean mIsFirmwareUpdateOptional;
    private String mMirrorUrl;
    private String mPrimaryUrl;
    private long mSizeInBytes;
    private String mUniqueVersion;

    private CargoFirmwareUpdateInfo() {
    }

    CargoFirmwareUpdateInfo(Parcel in) {
        super(in);
    }

    public static CargoFirmwareUpdateInfo getCloudFirmwareFromJson(String data, String deviceFamily, String currentVersion) throws CargoException {
        CargoFirmwareUpdateInfo firmware = new CargoFirmwareUpdateInfo();
        firmware.initWithJSONString(data);
        firmware.mCurrentVersion = currentVersion;
        if (StringUtil.isNullOrEmpty(firmware.mDeviceFamily)) {
            firmware.mDeviceFamily = deviceFamily;
        }
        return firmware;
    }

    @Override // com.microsoft.band.cloud.CloudJSONDataModel
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append(String.format("CloudFirmwareInfo:%s", System.getProperty("line.separator")));
        Object[] objArr = new Object[1];
        objArr[0] = this.mIsFirmwareUpdateAvailable ? ServerProtocol.DIALOG_RETURN_SCOPES_TRUE : "false";
        result.append(String.format("     |--isFirmwareUpdateAvailable= %s ", objArr)).append(System.getProperty("line.separator"));
        Object[] objArr2 = new Object[1];
        objArr2[0] = this.mIsFirmwareUpdateOptional ? ServerProtocol.DIALOG_RETURN_SCOPES_TRUE : "false";
        result.append(String.format("     |--isFirmwareUpdateOptional= %s ", objArr2)).append(System.getProperty("line.separator"));
        if (this.mDeviceFamily != null) {
            result.append(String.format("     |--DeviceFamily= %s  %s", this.mDeviceFamily, System.getProperty("line.separator")));
        }
        if (this.mCurrentVersion != null) {
            result.append(String.format("     |--CurrentVersion= %s  %s", this.mCurrentVersion, System.getProperty("line.separator")));
        }
        if (this.mIsFirmwareUpdateAvailable) {
            result.append(String.format("     |--FallbackUrl= %s ", this.mFallbackUrl)).append(System.getProperty("line.separator"));
            result.append(String.format("     |--FirmwareVersion= %s", this.mFirmwareVersion)).append(System.getProperty("line.separator"));
            result.append(String.format("     |--HashMd5= %s", this.mHashMd5)).append(System.getProperty("line.separator"));
            result.append(String.format("     |--MirrorUrl= %s", this.mMirrorUrl)).append(System.getProperty("line.separator"));
            result.append(String.format("     |--PrimaryUrl= %s", this.mPrimaryUrl)).append(System.getProperty("line.separator"));
            result.append(String.format("     |--SizeInBytes= %d", Long.valueOf(this.mSizeInBytes))).append(System.getProperty("line.separator"));
            result.append(String.format("     |--uniqueVersion= %s", this.mUniqueVersion)).append(System.getProperty("line.separator"));
        }
        return result.toString();
    }

    public String getDeviceFamily() {
        return this.mDeviceFamily;
    }

    public String getCurrentVersion() {
        return this.mCurrentVersion;
    }

    public String getFallbackUrl() {
        return this.mFallbackUrl;
    }

    public String getFirmwareVersion() {
        return this.mFirmwareVersion;
    }

    public boolean isFirmwareUpdateAvailable() {
        return this.mIsFirmwareUpdateAvailable;
    }

    public boolean isFirmwareUpdateOptional() {
        return this.mIsFirmwareUpdateOptional;
    }

    public String getMirrorUrl() {
        return this.mMirrorUrl;
    }

    public String getPrimaryUrl() {
        return this.mPrimaryUrl;
    }

    public long getSizeInBytes() {
        return this.mSizeInBytes;
    }

    public String getUniqueVersion() {
        return this.mUniqueVersion;
    }

    public int getBuildNumber() {
        String[] cloudVersionComponents = this.mFirmwareVersion.split("[.]");
        return Integer.parseInt(cloudVersionComponents[2]);
    }

    @Override // com.microsoft.band.cloud.CloudJSONDataModel
    public String toJSONString() throws JSONException {
        JSONObject firmware = new JSONObject();
        firmware.put(JSON_KEY_DEVICE_FAMILY, this.mDeviceFamily);
        firmware.put(JSON_KEY_FALLBACK_URL, this.mFallbackUrl);
        firmware.put(JSON_KEY_FIRMWARE_VERSION, this.mFirmwareVersion);
        firmware.put(JSON_KEY_HASH_MD5, this.mHashMd5);
        firmware.put(JSON_KEY_IS_FIRMWARE_UPDATE_AVAILABLE, this.mIsFirmwareUpdateAvailable);
        firmware.put(JSON_KEY_IS_FIRMWARE_UPDATE_OPTIONAL, this.mIsFirmwareUpdateOptional);
        firmware.put(JSON_KEY_MIRROR_URL, this.mMirrorUrl);
        firmware.put(JSON_KEY_PRIMARY_URL, this.mPrimaryUrl);
        firmware.put(JSON_KEY_SIZE_IN_BYTES, this.mSizeInBytes);
        firmware.put(JSON_KEY_UNIQUE_VERSION, this.mUniqueVersion);
        firmware.put(JSON_KEY_CURRENT_VERSION, this.mCurrentVersion);
        String json = firmware.toString();
        return json;
    }

    @Override // com.microsoft.band.cloud.CloudJSONDataModel
    protected void initWithJSONObject(JSONObject json) throws CargoException {
        try {
            this.mIsFirmwareUpdateAvailable = json.getBoolean(JSON_KEY_IS_FIRMWARE_UPDATE_AVAILABLE);
            this.mIsFirmwareUpdateOptional = json.getBoolean(JSON_KEY_IS_FIRMWARE_UPDATE_OPTIONAL);
            if (json.has(JSON_KEY_DEVICE_FAMILY)) {
                this.mDeviceFamily = json.getString(JSON_KEY_DEVICE_FAMILY);
            }
            if (json.has(JSON_KEY_CURRENT_VERSION)) {
                this.mCurrentVersion = json.getString(JSON_KEY_CURRENT_VERSION);
            }
            if (this.mIsFirmwareUpdateAvailable) {
                this.mFallbackUrl = json.getString(JSON_KEY_FALLBACK_URL);
                this.mFirmwareVersion = json.getString(JSON_KEY_FIRMWARE_VERSION);
                this.mHashMd5 = json.getString(JSON_KEY_HASH_MD5);
                this.mMirrorUrl = json.getString(JSON_KEY_MIRROR_URL);
                this.mPrimaryUrl = json.getString(JSON_KEY_PRIMARY_URL);
                this.mSizeInBytes = json.getLong(JSON_KEY_SIZE_IN_BYTES);
                this.mUniqueVersion = json.getString(JSON_KEY_UNIQUE_VERSION);
            }
        } catch (JSONException e) {
            throw new CargoException(String.format(BaseCargoException.EXCEPTION, e.getMessage()), e, BandServiceMessage.Response.SERVICE_CLOUD_DATA_ERROR);
        }
    }
}

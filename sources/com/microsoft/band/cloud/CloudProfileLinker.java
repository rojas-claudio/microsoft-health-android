package com.microsoft.band.cloud;

import android.os.Parcel;
import android.os.Parcelable;
import com.microsoft.band.client.BaseCargoException;
import com.microsoft.band.client.CargoException;
import com.microsoft.band.internal.BandServiceMessage;
import com.microsoft.band.internal.util.KDKLog;
import java.text.ParseException;
import java.util.Date;
import java.util.UUID;
import org.json.JSONException;
import org.json.JSONObject;
/* loaded from: classes.dex */
public class CloudProfileLinker extends CloudJSONDataModel {
    private static final String JSON_KEY_APPLICATION_SETTINGS = "ApplicationSettings";
    private static final String JSON_KEY_DEVICE_ID = "DeviceId";
    private static final String JSON_KEY_DEVICE_SETTINGS = "DeviceSettings";
    private static final String JSON_KEY_LAST_KDK_SYNC_UPDATE_ON = "LastKDKSyncUpdateOn";
    private static final String JSON_KEY_PAIRED_DEVICE_ID = "PairedDeviceId";
    private static final String JSON_KEY_SERIAL_NUMBER = "SerialNumber";
    private static final long serialVersionUID = 1;
    private UUID mAppPairingDeviceID;
    private UUID mDeviceID;
    private String mLastKDKSyncUpdateOn;
    private String mSerialNumber;
    private static final String TAG = CloudProfileLinker.class.getSimpleName();
    public static UUID EMPTY_DEVICE_ID = new UUID(0, 0);
    public static final Parcelable.Creator<CloudProfileLinker> CREATOR = new Parcelable.Creator<CloudProfileLinker>() { // from class: com.microsoft.band.cloud.CloudProfileLinker.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public CloudProfileLinker createFromParcel(Parcel in) {
            return new CloudProfileLinker(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public CloudProfileLinker[] newArray(int size) {
            return new CloudProfileLinker[size];
        }
    };

    public CloudProfileLinker() {
    }

    @Override // com.microsoft.band.cloud.CloudJSONDataModel
    public String toJSONString() throws JSONException {
        JSONObject profile = new JSONObject();
        JSONObject aSettings = new JSONObject();
        aSettings.put(JSON_KEY_PAIRED_DEVICE_ID, this.mAppPairingDeviceID != null ? this.mAppPairingDeviceID.toString() : null);
        profile.put(JSON_KEY_APPLICATION_SETTINGS, checkIfEmpty(aSettings));
        profile.put(JSON_KEY_LAST_KDK_SYNC_UPDATE_ON, this.mLastKDKSyncUpdateOn);
        JSONObject dSettings = new JSONObject();
        dSettings.put(JSON_KEY_DEVICE_ID, this.mDeviceID != null ? this.mDeviceID.toString() : EMPTY_DEVICE_ID.toString());
        dSettings.put(JSON_KEY_SERIAL_NUMBER, this.mSerialNumber);
        profile.put(JSON_KEY_DEVICE_SETTINGS, checkIfEmpty(dSettings));
        String json = profile.toString();
        return json;
    }

    private JSONObject checkIfEmpty(JSONObject jsonObject) {
        if (jsonObject == null || jsonObject.length() == 0) {
            return null;
        }
        return jsonObject;
    }

    CloudProfileLinker(Parcel in) {
        super(in);
    }

    @Override // com.microsoft.band.cloud.CloudJSONDataModel
    protected void initWithJSONObject(JSONObject json) throws CargoException, IllegalArgumentException {
        try {
            if (json.has(JSON_KEY_APPLICATION_SETTINGS)) {
                JSONObject asJSON = new JSONObject(json.getString(JSON_KEY_APPLICATION_SETTINGS));
                if (asJSON.has(JSON_KEY_PAIRED_DEVICE_ID)) {
                    setAppPairingDeviceID(UUID.fromString(asJSON.getString(JSON_KEY_PAIRED_DEVICE_ID)));
                }
            }
            if (json.has(JSON_KEY_LAST_KDK_SYNC_UPDATE_ON)) {
                setLastKDKSyncUpdateOn(json.getString(JSON_KEY_LAST_KDK_SYNC_UPDATE_ON));
            }
            if (json.has(JSON_KEY_DEVICE_SETTINGS)) {
                JSONObject dsJSON = new JSONObject(json.getString(JSON_KEY_DEVICE_SETTINGS));
                if (dsJSON.has(JSON_KEY_DEVICE_ID)) {
                    setDeviceID(UUID.fromString(dsJSON.getString(JSON_KEY_DEVICE_ID)));
                }
                if (dsJSON.has(JSON_KEY_SERIAL_NUMBER)) {
                    setSerialNumber(dsJSON.getString(JSON_KEY_SERIAL_NUMBER));
                }
            }
        } catch (JSONException e) {
            KDKLog.e(TAG, e.getMessage(), e);
            throw new CargoException(String.format(BaseCargoException.EXCEPTION, e.getMessage()), e, BandServiceMessage.Response.SERVICE_CLOUD_DATA_ERROR);
        }
    }

    @Override // com.microsoft.band.cloud.CloudJSONDataModel
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append(String.format("CloudProfileLinker:%s", System.getProperty("line.separator")));
        if (this.mDeviceID != null) {
            result.append(String.format("     |--Device Id= %s %s", this.mDeviceID, System.getProperty("line.separator")));
        }
        if (this.mAppPairingDeviceID != null) {
            result.append(String.format("     |--App Paired Device Id= %s %s", this.mAppPairingDeviceID, System.getProperty("line.separator")));
        }
        return result.toString();
    }

    public Date getLastKDKSyncUpdateOn() {
        if (this.mLastKDKSyncUpdateOn == null) {
            return null;
        }
        try {
            return getDateFromCloudTime(this.mLastKDKSyncUpdateOn);
        } catch (ParseException e) {
            KDKLog.e(TAG, e.getLocalizedMessage(), e);
            return null;
        }
    }

    public void setLastKDKSyncUpdateOn(long lastKDKSyncUpdateOn) {
        this.mLastKDKSyncUpdateOn = getCloudTimeStringFromDate(new Date(lastKDKSyncUpdateOn));
    }

    protected void setLastKDKSyncUpdateOn(String lastKDKSyncUpdateOn) {
        this.mLastKDKSyncUpdateOn = lastKDKSyncUpdateOn;
    }

    public String getSerialNumber() {
        return this.mSerialNumber;
    }

    public CloudProfileLinker setDeviceID(UUID deviceID) {
        this.mDeviceID = deviceID;
        return this;
    }

    public CloudProfileLinker setSerialNumber(String serialNumber) {
        this.mSerialNumber = serialNumber;
        return this;
    }

    public UUID getAppPairingDeviceID() {
        return this.mAppPairingDeviceID;
    }

    public CloudProfileLinker setAppPairingDeviceID(UUID appPairingDeviceID) {
        this.mAppPairingDeviceID = appPairingDeviceID;
        return this;
    }
}

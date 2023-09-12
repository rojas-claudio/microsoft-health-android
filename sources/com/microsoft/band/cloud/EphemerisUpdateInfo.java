package com.microsoft.band.cloud;

import android.content.SharedPreferences;
import android.os.Parcel;
import android.os.Parcelable;
import com.microsoft.band.CargoConstants;
import com.microsoft.band.client.BaseCargoException;
import com.microsoft.band.client.CargoException;
import com.microsoft.band.internal.BandServiceMessage;
import com.microsoft.band.internal.util.KDKLog;
import java.text.ParseException;
import java.util.Date;
import org.json.JSONException;
import org.json.JSONObject;
/* loaded from: classes.dex */
public class EphemerisUpdateInfo extends CloudJSONDataModel {
    public static final Parcelable.Creator<EphemerisUpdateInfo> CREATOR = new Parcelable.Creator<EphemerisUpdateInfo>() { // from class: com.microsoft.band.cloud.EphemerisUpdateInfo.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public EphemerisUpdateInfo createFromParcel(Parcel in) {
            return new EphemerisUpdateInfo(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public EphemerisUpdateInfo[] newArray(int size) {
            return new EphemerisUpdateInfo[size];
        }
    };
    private static final String JSON_LAST_FILE_UPDATED_TIME = "LastFileUpdatedTime";
    private static final String JSON_PROCESSED_FILE_DATA_URL = "EphemerisProcessedFileDataUrl";
    private static final long serialVersionUID = 1;
    private String mLastModifiedDateTime;
    private String mURL;

    public String getURL() {
        return this.mURL;
    }

    public void setURL(String uRL) {
        this.mURL = uRL;
    }

    public Date getLastModifiedDateTime() {
        try {
            return getDateFromCloudTime(this.mLastModifiedDateTime);
        } catch (ParseException e) {
            KDKLog.e(EphemerisUpdateInfo.class.getSimpleName(), e.getLocalizedMessage(), e);
            return null;
        }
    }

    protected void setLastModifiedDateTime(String lastModifiedDateTime) {
        this.mLastModifiedDateTime = lastModifiedDateTime;
    }

    public String getLMTString() {
        return this.mLastModifiedDateTime;
    }

    public EphemerisUpdateInfo() {
        this.mURL = null;
        this.mLastModifiedDateTime = null;
    }

    EphemerisUpdateInfo(Parcel in) {
        super(in);
    }

    public static EphemerisUpdateInfo fromSharedPreferences(SharedPreferences sharedPreferences) throws CargoException {
        String jsonData = sharedPreferences.getString(CargoConstants.last_ephemeris_json, null);
        if (jsonData == null) {
            EphemerisUpdateInfo ephemerisUpdateInfo = new EphemerisUpdateInfo();
            ephemerisUpdateInfo.setLastModifiedDateTime("1980-01-01T00:00:00.000+00:00");
            ephemerisUpdateInfo.setURL("");
            return ephemerisUpdateInfo;
        }
        return getEphemerisInfoFromJson(jsonData);
    }

    public static EphemerisUpdateInfo getEphemerisInfoFromJson(String data) throws CargoException {
        EphemerisUpdateInfo ephemeris = new EphemerisUpdateInfo();
        ephemeris.initWithJSONString(data);
        return ephemeris;
    }

    @Override // com.microsoft.band.cloud.CloudJSONDataModel
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("\n ---{}\n");
        result.append(String.format("     |--URL= %s ", this.mURL)).append(System.getProperty("line.separator"));
        result.append(String.format("     |--TimeZoneOffset= %s", this.mLastModifiedDateTime));
        result.append(System.getProperty("line.separator"));
        return result.toString();
    }

    @Override // com.microsoft.band.cloud.CloudJSONDataModel
    public String toJSONString() throws JSONException {
        JSONObject data = new JSONObject();
        data.put(JSON_PROCESSED_FILE_DATA_URL, this.mURL);
        data.put(JSON_LAST_FILE_UPDATED_TIME, this.mLastModifiedDateTime);
        return data.toString();
    }

    @Override // com.microsoft.band.cloud.CloudJSONDataModel
    protected void initWithJSONObject(JSONObject json) throws CargoException {
        try {
            setURL(json.getString(JSON_PROCESSED_FILE_DATA_URL));
            setLastModifiedDateTime(json.getString(JSON_LAST_FILE_UPDATED_TIME));
        } catch (JSONException e) {
            throw new CargoException(String.format(BaseCargoException.EXCEPTION, e.getMessage()), e, BandServiceMessage.Response.SERVICE_CLOUD_DATA_ERROR);
        }
    }
}

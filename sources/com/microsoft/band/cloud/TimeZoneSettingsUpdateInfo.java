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
public class TimeZoneSettingsUpdateInfo extends CloudJSONDataModel {
    public static final Parcelable.Creator<TimeZoneSettingsUpdateInfo> CREATOR = new Parcelable.Creator<TimeZoneSettingsUpdateInfo>() { // from class: com.microsoft.band.cloud.TimeZoneSettingsUpdateInfo.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public TimeZoneSettingsUpdateInfo createFromParcel(Parcel in) {
            return new TimeZoneSettingsUpdateInfo(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public TimeZoneSettingsUpdateInfo[] newArray(int size) {
            return new TimeZoneSettingsUpdateInfo[size];
        }
    };
    private static final String JSON_KEY_LAST_MODIFIED_DATE_TIME = "LastModifiedDateTime";
    private static final String JSON_KEY_URL = "Url";
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
            KDKLog.e(TimeZoneSettingsUpdateInfo.class.getSimpleName(), e.getLocalizedMessage(), e);
            return null;
        }
    }

    protected void setLastModifiedDateTime(String lastModifiedDateTime) {
        this.mLastModifiedDateTime = lastModifiedDateTime;
    }

    public String getLMTString() {
        return this.mLastModifiedDateTime;
    }

    public TimeZoneSettingsUpdateInfo() {
        this.mURL = null;
        this.mLastModifiedDateTime = null;
    }

    TimeZoneSettingsUpdateInfo(Parcel in) {
        super(in);
    }

    public static TimeZoneSettingsUpdateInfo fromSharedPreferences(SharedPreferences sharedPreferences) throws CargoException {
        String jsonData = sharedPreferences.getString(CargoConstants.last_timezone_json, null);
        if (jsonData == null) {
            TimeZoneSettingsUpdateInfo timeZoneSettinsUpdateInfo = new TimeZoneSettingsUpdateInfo();
            timeZoneSettinsUpdateInfo.setLastModifiedDateTime("1980-01-01T00:00:00.000+00:00");
            timeZoneSettinsUpdateInfo.setURL("");
            return timeZoneSettinsUpdateInfo;
        }
        return getTimeZoneSettingsFromJson(jsonData);
    }

    public static TimeZoneSettingsUpdateInfo getTimeZoneSettingsFromJson(String data) throws CargoException {
        TimeZoneSettingsUpdateInfo timeZoneSettings = new TimeZoneSettingsUpdateInfo();
        timeZoneSettings.initWithJSONString(data);
        return timeZoneSettings;
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
        JSONObject json = new JSONObject();
        json.put(JSON_KEY_URL, this.mURL);
        json.put(JSON_KEY_LAST_MODIFIED_DATE_TIME, this.mLastModifiedDateTime);
        return json.toString();
    }

    @Override // com.microsoft.band.cloud.CloudJSONDataModel
    protected void initWithJSONObject(JSONObject json) throws CargoException {
        try {
            setURL(json.getString(JSON_KEY_URL));
            setLastModifiedDateTime(json.getString(JSON_KEY_LAST_MODIFIED_DATE_TIME));
        } catch (JSONException e) {
            throw new CargoException(String.format(BaseCargoException.EXCEPTION, e.getMessage()), e, BandServiceMessage.Response.SERVICE_CLOUD_DATA_ERROR);
        }
    }
}

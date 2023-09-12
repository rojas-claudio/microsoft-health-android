package com.microsoft.band.cloud;

import android.os.Parcel;
import com.facebook.AppEventsConstants;
import com.microsoft.band.client.BaseCargoException;
import com.microsoft.band.client.CargoException;
import com.microsoft.band.internal.BandServiceMessage;
import com.microsoft.kapp.utils.Constants;
import com.unnamed.b.atv.model.TreeNode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
/* loaded from: classes.dex */
public abstract class CloudJSONDataModel extends CloudDataModel {
    protected static final SimpleDateFormat CLOUD_DATE_FORMAT = new SimpleDateFormat(Constants.CLOUD_DATE_FORMAT, Locale.US);
    protected static final String JSON_KEY_ODATA_METADATA = "odata.metadata";
    protected static final String JSON_KEY_VALUE = "value";
    private static final long serialVersionUID = 1;

    protected abstract void initWithJSONObject(JSONObject jSONObject) throws CargoException;

    public abstract String toJSONString() throws JSONException;

    /* JADX INFO: Access modifiers changed from: protected */
    public CloudJSONDataModel() {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public CloudJSONDataModel(Parcel in) {
        super(in);
        String jsonString = in.readString();
        if (jsonString != null && jsonString.length() > 0) {
            try {
                initWithJSONString(jsonString);
            } catch (Exception e) {
            }
        }
    }

    @Override // com.microsoft.band.cloud.CloudDataModel, android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        try {
            dest.writeString(toJSONString());
        } catch (JSONException e) {
            dest.writeString("");
        }
    }

    public String toString() {
        String str;
        try {
            str = toJSONString();
        } catch (Exception e) {
            str = String.format("{\"error\": \"%s\"}", e.getMessage());
        }
        return String.format("%s: %s", getClass().getSimpleName(), str);
    }

    public void initWithJSONString(String jsonString) throws CargoException {
        try {
            JSONObject json = new JSONObject(jsonString);
            if (json.has(JSON_KEY_ODATA_METADATA) && json.has(JSON_KEY_VALUE)) {
                JSONArray jsonArray = json.getJSONArray(JSON_KEY_VALUE);
                if (jsonArray.length() == 0) {
                    throw new CargoException(String.format(BaseCargoException.EXCEPTION_JSON, String.format("%s value array is empty, json: %s", JSON_KEY_ODATA_METADATA, jsonString)), BandServiceMessage.Response.SERVICE_CLOUD_DATA_ERROR);
                }
                json = jsonArray.getJSONObject(0);
            }
            initWithJSONObject(json);
        } catch (CargoException e) {
            if (e.getResponse() == null) {
                throw new CargoException(e.getMessage(), e.getCause(), BandServiceMessage.Response.SERVICE_CLOUD_DATA_ERROR);
            }
            throw e;
        } catch (JSONException e2) {
            throw new CargoException(String.format(BaseCargoException.EXCEPTION_JSON, String.format("%s, json: %s", e2.getMessage(), jsonString)), e2, BandServiceMessage.Response.SERVICE_CLOUD_DATA_ERROR);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public Date getDateFromCloudTime(String cloudTime) throws ParseException {
        StringBuilder sb = new StringBuilder(28);
        sb.append(cloudTime.substring(0, 19));
        switch (cloudTime.length()) {
            case 25:
                sb.append(".000");
                break;
            case 26:
            default:
                sb.append(".000");
                break;
            case 27:
                sb.append(cloudTime.substring(19, 21) + "00");
                break;
            case 28:
                sb.append(cloudTime.substring(19, 22) + AppEventsConstants.EVENT_PARAM_VALUE_NO);
                break;
            case 29:
                sb.append(cloudTime.substring(19, 23));
                break;
        }
        sb.append(cloudTime.substring(cloudTime.length() - 6, cloudTime.length() - 3));
        sb.append(cloudTime.substring(cloudTime.length() - 2));
        return CLOUD_DATE_FORMAT.parse(sb.toString());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public String getCloudTimeStringFromDate(Date d) {
        String androidTime = CLOUD_DATE_FORMAT.format(d);
        return androidTime.substring(0, 26) + TreeNode.NODES_ID_SEPARATOR + androidTime.substring(26);
    }
}

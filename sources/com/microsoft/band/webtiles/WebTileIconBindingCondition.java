package com.microsoft.band.webtiles;

import android.os.Parcel;
import android.os.Parcelable;
import com.microsoft.band.internal.util.Validation;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;
/* loaded from: classes.dex */
public class WebTileIconBindingCondition implements Parcelable {
    public static final Parcelable.Creator<WebTileIconBindingCondition> CREATOR = new Parcelable.Creator<WebTileIconBindingCondition>() { // from class: com.microsoft.band.webtiles.WebTileIconBindingCondition.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public WebTileIconBindingCondition createFromParcel(Parcel in) {
            return new WebTileIconBindingCondition(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public WebTileIconBindingCondition[] newArray(int size) {
            return new WebTileIconBindingCondition[size];
        }
    };
    private static final String JSON_KEY_PAGE_ICON_BINDING_CONDITION = "condition";
    private static final String JSON_KEY_PAGE_ICON_BINDING_ICON = "icon";
    private String mCondition;
    private String mIconName;

    public WebTileIconBindingCondition(JSONObject json) throws JSONException {
        if (json.has(JSON_KEY_PAGE_ICON_BINDING_CONDITION)) {
            setCondition(json.getString(JSON_KEY_PAGE_ICON_BINDING_CONDITION));
        }
        if (json.has(JSON_KEY_PAGE_ICON_BINDING_ICON)) {
            setIconName(json.getString(JSON_KEY_PAGE_ICON_BINDING_ICON));
            return;
        }
        throw new IllegalArgumentException("IconName is required for WebTile resource.");
    }

    public String getCondition() {
        return this.mCondition;
    }

    public void setCondition(String condition) {
        Validation.validateNullParameter(condition, "IconBinding Condition");
        Validation.validateStringEmptyOrWhiteSpace(condition, "IconBinding Condition");
        this.mCondition = condition;
    }

    public String getIconName() {
        return this.mIconName;
    }

    public void setIconName(String iconName) {
        Validation.validateNullParameter(iconName, "IconBinding iconName");
        Validation.validateStringEmptyOrWhiteSpace(iconName, "IconBinding iconName");
        this.mIconName = iconName;
    }

    public boolean isTrue(Map<String, String> contentMap) {
        return true;
    }

    WebTileIconBindingCondition(Parcel in) {
        this.mCondition = in.readString();
        this.mIconName = in.readString();
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mCondition);
        dest.writeString(this.mIconName);
    }
}

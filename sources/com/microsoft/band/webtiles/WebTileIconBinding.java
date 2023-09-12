package com.microsoft.band.webtiles;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
/* loaded from: classes.dex */
public class WebTileIconBinding implements Parcelable {
    public static final Parcelable.Creator<WebTileIconBinding> CREATOR = new Parcelable.Creator<WebTileIconBinding>() { // from class: com.microsoft.band.webtiles.WebTileIconBinding.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public WebTileIconBinding createFromParcel(Parcel in) {
            return new WebTileIconBinding(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public WebTileIconBinding[] newArray(int size) {
            return new WebTileIconBinding[size];
        }
    };
    private static final String JSON_KEY_PAGE_ELEMENT_ID = "elementId";
    private static final String JSON_KEY_PAGE_ICON_BINDING_CONDITIONS = "conditions";
    private List<WebTileIconBindingCondition> mConditions = new ArrayList();
    private int mId;

    public WebTileIconBinding(JSONObject json) throws JSONException {
        if (json.has(JSON_KEY_PAGE_ELEMENT_ID)) {
            setId(json.getInt(JSON_KEY_PAGE_ELEMENT_ID));
            if (json.has(JSON_KEY_PAGE_ICON_BINDING_CONDITIONS)) {
                setConditions(json.getJSONArray(JSON_KEY_PAGE_ICON_BINDING_CONDITIONS));
                return;
            }
            throw new IllegalArgumentException("IconBindingConditions is required for WebTile Icon.");
        }
        throw new IllegalArgumentException("ElementId is required for WebTile Icon.");
    }

    public int getId() {
        return this.mId;
    }

    public void setId(int id) {
        if (id >= 1 && id < 32767) {
            this.mId = id;
            return;
        }
        throw new IllegalArgumentException("Invalid icon element ID");
    }

    public List<WebTileIconBindingCondition> getConditions() {
        return this.mConditions;
    }

    public void setConditions(JSONArray conditionJSONArray) throws JSONException {
        int size = conditionJSONArray.length();
        for (int i = 0; i < size; i++) {
            JSONObject webTileCondition = (JSONObject) conditionJSONArray.get(i);
            if (webTileCondition != null) {
                this.mConditions.add(new WebTileIconBindingCondition(webTileCondition));
            }
        }
    }

    public String getIconBindingData(Map<String, String> contentMap) {
        for (int i = 0; i < this.mConditions.size(); i++) {
            WebTileIconBindingCondition condition = this.mConditions.get(i);
            if (condition.isTrue(contentMap)) {
                return condition.getIconName();
            }
        }
        return null;
    }

    WebTileIconBinding(Parcel in) {
        this.mId = in.readInt();
        in.readList(this.mConditions, WebTileIconBindingCondition.class.getClassLoader());
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mId);
        dest.writeList(this.mConditions);
    }
}

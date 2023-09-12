package com.microsoft.band.webtiles;

import android.os.Parcel;
import android.os.Parcelable;
import org.json.JSONException;
import org.json.JSONObject;
/* loaded from: classes.dex */
public class WebTileTextBinding implements Parcelable {
    public static final Parcelable.Creator<WebTileTextBinding> CREATOR = new Parcelable.Creator<WebTileTextBinding>() { // from class: com.microsoft.band.webtiles.WebTileTextBinding.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public WebTileTextBinding createFromParcel(Parcel in) {
            return new WebTileTextBinding(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public WebTileTextBinding[] newArray(int size) {
            return new WebTileTextBinding[size];
        }
    };
    private static final String JSON_KEY_PAGE_ELEMENT_ID = "elementId";
    private static final String JSON_KEY_PAGE_TEXT_BINDING_VALUE = "value";
    private int mId;
    private String mValue;

    public WebTileTextBinding(JSONObject json) throws JSONException {
        if (json.has(JSON_KEY_PAGE_ELEMENT_ID)) {
            setId(json.getInt(JSON_KEY_PAGE_ELEMENT_ID));
            if (json.has(JSON_KEY_PAGE_TEXT_BINDING_VALUE)) {
                setValue(json.getString(JSON_KEY_PAGE_TEXT_BINDING_VALUE));
                return;
            }
            throw new IllegalArgumentException("Text value is required for WebTile Text.");
        }
        throw new IllegalArgumentException("ElementId is required for WebTile Text.");
    }

    public int getId() {
        return this.mId;
    }

    public void setId(int id) {
        if (id >= 1 && id < 32767) {
            this.mId = id;
            return;
        }
        throw new IllegalArgumentException("Invalid element ID");
    }

    public String getValue() {
        return this.mValue;
    }

    public void setValue(String value) {
        if (value == null || value.matches("\\s*")) {
            this.mValue = "";
        } else {
            this.mValue = value;
        }
    }

    WebTileTextBinding(Parcel in) {
        setId(in.readInt());
        this.mValue = in.readString();
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mId);
        dest.writeString(this.mValue);
    }
}

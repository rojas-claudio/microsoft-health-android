package com.microsoft.krestsdk.models;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
/* loaded from: classes.dex */
public class OOBEConfiguration implements Parcelable {
    public static final Parcelable.Creator<OOBEConfiguration> CREATOR = new Parcelable.Creator<OOBEConfiguration>() { // from class: com.microsoft.krestsdk.models.OOBEConfiguration.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public OOBEConfiguration createFromParcel(Parcel in) {
            return new OOBEConfiguration(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public OOBEConfiguration[] newArray(int size) {
            return new OOBEConfiguration[size];
        }
    };
    @SerializedName("defaults")
    private OOBEDefaults mOOBEDefaults;

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public OOBEDefaults getOOBEDefaults() {
        return this.mOOBEDefaults;
    }

    public void setOOBEDefaults(OOBEDefaults mOOBEDefaults) {
        this.mOOBEDefaults = mOOBEDefaults;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(getOOBEDefaults());
    }

    protected OOBEConfiguration(Parcel in) {
        setOOBEDefaults((OOBEDefaults) in.readValue(OOBEDefaults.class.getClassLoader()));
    }
}

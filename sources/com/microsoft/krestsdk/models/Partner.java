package com.microsoft.krestsdk.models;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
import com.microsoft.kapp.diagnostics.TelemetryConstants;
/* loaded from: classes.dex */
public class Partner implements Parcelable {
    public static final Parcelable.Creator<Partner> CREATOR = new Parcelable.Creator<Partner>() { // from class: com.microsoft.krestsdk.models.Partner.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public Partner createFromParcel(Parcel in) {
            return new Partner(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public Partner[] newArray(int size) {
            return new Partner[size];
        }
    };
    @SerializedName(TelemetryConstants.TimedEvents.Cloud.Golf.SEARCH_TYPE_NAME)
    private String mName;

    public String getName() {
        return this.mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    protected Partner(Parcel in) {
        this.mName = in.readString();
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mName);
    }
}

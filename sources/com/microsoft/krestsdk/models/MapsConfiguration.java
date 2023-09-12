package com.microsoft.krestsdk.models;

import android.os.Parcel;
import android.os.Parcelable;
import com.facebook.internal.ServerProtocol;
import com.google.gson.annotations.SerializedName;
/* loaded from: classes.dex */
public class MapsConfiguration implements Parcelable {
    public static final Parcelable.Creator<MapsConfiguration> CREATOR = new Parcelable.Creator<MapsConfiguration>() { // from class: com.microsoft.krestsdk.models.MapsConfiguration.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public MapsConfiguration createFromParcel(Parcel in) {
            return new MapsConfiguration(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public MapsConfiguration[] newArray(int size) {
            return new MapsConfiguration[size];
        }
    };
    @SerializedName("roadUriFormat")
    private String mRoadUriFormat;
    @SerializedName("serviceUrl")
    private String mServiceUrl;
    @SerializedName(ServerProtocol.DIALOG_RESPONSE_TYPE_TOKEN)
    private String mToken;

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mServiceUrl);
        dest.writeString(this.mToken);
        dest.writeString(this.mRoadUriFormat);
    }

    protected MapsConfiguration(Parcel in) {
        this.mServiceUrl = in.readString();
        this.mToken = in.readString();
        this.mRoadUriFormat = in.readString();
    }
}

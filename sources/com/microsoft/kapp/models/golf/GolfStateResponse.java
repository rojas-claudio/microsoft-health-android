package com.microsoft.kapp.models.golf;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
/* loaded from: classes.dex */
public class GolfStateResponse implements Parcelable {
    public static final Parcelable.Creator<GolfStateResponse> CREATOR = new Parcelable.Creator<GolfStateResponse>() { // from class: com.microsoft.kapp.models.golf.GolfStateResponse.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public GolfStateResponse createFromParcel(Parcel in) {
            return new GolfStateResponse(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public GolfStateResponse[] newArray(int size) {
            return new GolfStateResponse[size];
        }
    };
    @SerializedName("region_id")
    private String mRegionId;
    @SerializedName("states")
    private GolfRegion[] mStates;
    @SerializedName("type")
    private String mTypeId;

    public String getCourseID() {
        return this.mTypeId;
    }

    public void setRegionID(String typeId) {
        this.mTypeId = typeId;
    }

    public GolfRegion[] getStates() {
        return this.mStates;
    }

    public void setStates(GolfRegion[] regions) {
        this.mStates = regions;
    }

    protected GolfStateResponse(Parcel in) {
        this.mTypeId = in.readString();
        this.mStates = (GolfRegion[]) in.createTypedArray(GolfRegion.CREATOR);
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mTypeId);
        dest.writeTypedArray(this.mStates, flags);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }
}

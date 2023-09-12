package com.microsoft.kapp.models.golf;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
/* loaded from: classes.dex */
public class GolfRegionResponse implements Parcelable {
    public static final Parcelable.Creator<GolfRegionResponse> CREATOR = new Parcelable.Creator<GolfRegionResponse>() { // from class: com.microsoft.kapp.models.golf.GolfRegionResponse.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public GolfRegionResponse createFromParcel(Parcel in) {
            return new GolfRegionResponse(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public GolfRegionResponse[] newArray(int size) {
            return new GolfRegionResponse[size];
        }
    };
    @SerializedName("regions")
    private GolfRegion[] mRegions;
    @SerializedName("type")
    private String mTypeId;

    public String getCourseID() {
        return this.mTypeId;
    }

    public void setRegionID(String typeId) {
        this.mTypeId = typeId;
    }

    public GolfRegion[] getRegions() {
        return this.mRegions;
    }

    public void setRegions(GolfRegion[] regions) {
        this.mRegions = regions;
    }

    protected GolfRegionResponse(Parcel in) {
        this.mTypeId = in.readString();
        this.mRegions = (GolfRegion[]) in.createTypedArray(GolfRegion.CREATOR);
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mTypeId);
        dest.writeTypedArray(this.mRegions, flags);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }
}

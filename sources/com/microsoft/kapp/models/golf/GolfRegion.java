package com.microsoft.kapp.models.golf;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
import com.microsoft.kapp.services.healthandfitness.models.WorkoutSummary;
/* loaded from: classes.dex */
public class GolfRegion implements Parcelable {
    public static final Parcelable.Creator<GolfRegion> CREATOR = new Parcelable.Creator<GolfRegion>() { // from class: com.microsoft.kapp.models.golf.GolfRegion.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public GolfRegion createFromParcel(Parcel in) {
            return new GolfRegion(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public GolfRegion[] newArray(int size) {
            return new GolfRegion[size];
        }
    };
    @SerializedName("numberOfCourses")
    private int mNumberOfCourses;
    @SerializedName("id")
    private int mRegionId;
    @SerializedName(WorkoutSummary.NAME)
    private String mRegionName;

    public int getRegionID() {
        return this.mRegionId;
    }

    public void setRegionID(int regionID) {
        this.mRegionId = regionID;
    }

    public String getRegionName() {
        return this.mRegionName;
    }

    public void setRegionName(String regionName) {
        this.mRegionName = regionName;
    }

    public int getNumberOfCourses() {
        return this.mNumberOfCourses;
    }

    public void setNumberOfCourses(int numberOfCourses) {
        this.mNumberOfCourses = numberOfCourses;
    }

    protected GolfRegion(Parcel in) {
        this.mRegionId = in.readInt();
        this.mRegionName = in.readString();
        this.mNumberOfCourses = in.readInt();
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mRegionId);
        dest.writeString(this.mRegionName);
        dest.writeInt(this.mNumberOfCourses);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }
}

package com.microsoft.krestsdk.models;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
import com.microsoft.kapp.services.healthandfitness.models.WorkoutSummary;
/* loaded from: classes.dex */
public class GolfHole implements Parcelable {
    public static final Parcelable.Creator<GolfHole> CREATOR = new Parcelable.Creator<GolfHole>() { // from class: com.microsoft.krestsdk.models.GolfHole.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public GolfHole createFromParcel(Parcel in) {
            return new GolfHole(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public GolfHole[] newArray(int size) {
            return new GolfHole[size];
        }
    };
    @SerializedName("id")
    private long mId;
    @SerializedName(WorkoutSummary.NAME)
    private String mName;
    @SerializedName("tees")
    private GolfTee[] mTees;

    public long getId() {
        return this.mId;
    }

    public void setId(long id) {
        this.mId = id;
    }

    public String getName() {
        return this.mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public GolfTee[] getTees() {
        return this.mTees;
    }

    public void setTees(GolfTee[] tees) {
        this.mTees = tees;
    }

    protected GolfHole(Parcel in) {
        this.mId = in.readLong();
        this.mName = in.readString();
        this.mTees = (GolfTee[]) in.createTypedArray(GolfTee.CREATOR);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.mId);
        dest.writeString(this.mName);
        dest.writeTypedArray(this.mTees, flags);
    }
}

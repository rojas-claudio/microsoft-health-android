package com.microsoft.krestsdk.models;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
import com.microsoft.kapp.fragments.guidedworkout.GuidedWorkoutCarouselFragment;
import com.microsoft.kapp.services.healthandfitness.models.WorkoutSummary;
/* loaded from: classes.dex */
public class GolfTee implements Parcelable {
    public static final Parcelable.Creator<GolfTee> CREATOR = new Parcelable.Creator<GolfTee>() { // from class: com.microsoft.krestsdk.models.GolfTee.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public GolfTee createFromParcel(Parcel in) {
            return new GolfTee(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public GolfTee[] newArray(int size) {
            return new GolfTee[size];
        }
    };
    @SerializedName("id")
    private long mId;
    @SerializedName(GuidedWorkoutCarouselFragment.DEFAULT_IMAGE)
    private boolean mIsDefault;
    @SerializedName(WorkoutSummary.NAME)
    private String mName;
    @SerializedName("par")
    private int mPar;
    @SerializedName("rating")
    private double mRating;
    @SerializedName("slope")
    private double mSlope;
    @SerializedName("strokeIndex")
    private int mStrokeIndex;
    @SerializedName("yards")
    private double mYards;

    public String getName() {
        return this.mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public int getPar() {
        return this.mPar;
    }

    public void setPar(int par) {
        this.mPar = par;
    }

    public double getRating() {
        return this.mRating;
    }

    public void setRating(double rating) {
        this.mRating = rating;
    }

    public double getSlope() {
        return this.mSlope;
    }

    public void setSlope(double slope) {
        this.mSlope = slope;
    }

    public double getYards() {
        return this.mYards;
    }

    public void setYards(double yards) {
        this.mYards = yards;
    }

    public long getId() {
        return this.mId;
    }

    public void setId(long id) {
        this.mId = id;
    }

    public boolean isDefault() {
        return this.mIsDefault;
    }

    public void setIsDefault(boolean isDefault) {
        this.mIsDefault = isDefault;
    }

    public void setStrokeIndex(int strokeIndex) {
        this.mStrokeIndex = strokeIndex;
    }

    public int getStrokeIndex() {
        return this.mStrokeIndex;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mName);
        dest.writeInt(this.mPar);
        dest.writeDouble(this.mRating);
        dest.writeDouble(this.mSlope);
        dest.writeDouble(this.mYards);
        dest.writeLong(this.mId);
        dest.writeByte((byte) (this.mIsDefault ? 1 : 0));
        dest.writeInt(this.mStrokeIndex);
    }

    protected GolfTee(Parcel in) {
        this.mName = in.readString();
        this.mPar = in.readInt();
        this.mRating = in.readDouble();
        this.mSlope = in.readDouble();
        this.mYards = in.readDouble();
        this.mId = in.readLong();
        this.mIsDefault = in.readByte() != 0;
        this.mStrokeIndex = in.readInt();
    }
}

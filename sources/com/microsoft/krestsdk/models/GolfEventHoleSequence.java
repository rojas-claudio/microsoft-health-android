package com.microsoft.krestsdk.models;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
/* loaded from: classes.dex */
public class GolfEventHoleSequence extends UserEventSequence implements Parcelable {
    public static final Parcelable.Creator<GolfEventHoleSequence> CREATOR = new Parcelable.Creator<GolfEventHoleSequence>() { // from class: com.microsoft.krestsdk.models.GolfEventHoleSequence.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public GolfEventHoleSequence createFromParcel(Parcel in) {
            return new GolfEventHoleSequence(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public GolfEventHoleSequence[] newArray(int size) {
            return new GolfEventHoleSequence[size];
        }
    };
    @SerializedName("DistanceToPinInCm")
    private int mDistanceToPinInCm;
    @SerializedName("DistanceWalkedInCm")
    private int mDistanceWalkedInCm;
    @SerializedName("HoleDifficultyIndex")
    private int mHoleDifficultyIndex;
    @SerializedName("HoleNumber")
    private int mHoleNumber;
    @SerializedName("HolePar")
    private int mHolePar;
    @SerializedName("HoleShotOverlayImageUrl")
    private String mHoleShotOverlayImageUrl;
    @SerializedName("StepCount")
    private int mStepCount;
    @SerializedName("UserScore")
    private int mUserScore;

    public GolfEventHoleSequence() {
    }

    public int getHoleNumber() {
        return this.mHoleNumber;
    }

    public void setHoleNumber(int holeNumber) {
        this.mHoleNumber = holeNumber;
    }

    public int getDistanceToPinInCm() {
        return this.mDistanceToPinInCm;
    }

    public void setDistanceToPinInCm(int distanceToPinInCm) {
        this.mDistanceToPinInCm = distanceToPinInCm;
    }

    public int getDistanceWalkedInCm() {
        return this.mDistanceWalkedInCm;
    }

    public void setDistanceWalkedCm(int distanceWalkedInCm) {
        this.mDistanceWalkedInCm = distanceWalkedInCm;
    }

    public int getHolePar() {
        return this.mHolePar;
    }

    public void setHolePar(int holePar) {
        this.mHolePar = holePar;
    }

    public int getHoleDifficultyIndex() {
        return this.mHoleDifficultyIndex;
    }

    public void setHoleDifficultyIndex(int holeDifficultyIndex) {
        this.mHoleDifficultyIndex = holeDifficultyIndex;
    }

    public int getUserScore() {
        return this.mUserScore;
    }

    public void setUserScore(int userScore) {
        this.mUserScore = userScore;
    }

    public String getHoleShotOverlayImageUrl() {
        return this.mHoleShotOverlayImageUrl;
    }

    public void setHoleShotOverlayImageUrl(String holeShotOverlayImageUrl) {
        this.mHoleShotOverlayImageUrl = holeShotOverlayImageUrl;
    }

    public int getStepCount() {
        return this.mStepCount;
    }

    public void setStepCount(int stepsTaken) {
        this.mStepCount = stepsTaken;
    }

    protected GolfEventHoleSequence(Parcel in) {
        super(in);
        this.mHoleNumber = in.readInt();
        this.mDistanceToPinInCm = in.readInt();
        this.mHolePar = in.readInt();
        this.mHoleDifficultyIndex = in.readInt();
        this.mUserScore = in.readInt();
        this.mStepCount = in.readInt();
        this.mHoleShotOverlayImageUrl = in.readString();
        this.mDistanceWalkedInCm = in.readInt();
    }

    @Override // com.microsoft.krestsdk.models.UserEventSequence, android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(this.mHoleNumber);
        dest.writeInt(this.mDistanceToPinInCm);
        dest.writeInt(this.mHolePar);
        dest.writeInt(this.mHoleDifficultyIndex);
        dest.writeInt(this.mUserScore);
        dest.writeInt(this.mStepCount);
        dest.writeString(this.mHoleShotOverlayImageUrl);
        dest.writeInt(this.mDistanceWalkedInCm);
    }
}

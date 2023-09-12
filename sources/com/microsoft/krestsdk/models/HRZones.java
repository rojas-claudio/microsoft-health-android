package com.microsoft.krestsdk.models;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
/* loaded from: classes.dex */
public class HRZones implements Parcelable {
    public static final Parcelable.Creator<HRZones> CREATOR = new Parcelable.Creator<HRZones>() { // from class: com.microsoft.krestsdk.models.HRZones.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public HRZones createFromParcel(Parcel in) {
            return new HRZones(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public HRZones[] newArray(int size) {
            return new HRZones[size];
        }
    };
    @SerializedName("Aerobic")
    private int mAerobic;
    @SerializedName("Anaerobic")
    private int mAnaerobic;
    @SerializedName("FitnessZone")
    private int mFitnessZone;
    @SerializedName("HealthyHeart")
    private int mHealthyHeart;
    @SerializedName("MaxHr")
    private int mMaxHr;
    @SerializedName("Over")
    private int mOver;
    @SerializedName("RedLine")
    private int mRedLine;
    @SerializedName("Under")
    private int mUnder;

    public int getUnder() {
        return this.mUnder;
    }

    public void setUnder(int under) {
        this.mUnder = under;
    }

    public int getAerobic() {
        return this.mAerobic;
    }

    public void setAerobic(int aerobic) {
        this.mAerobic = aerobic;
    }

    public int getAnaerobic() {
        return this.mAnaerobic;
    }

    public void setAnaerobic(int anaerobic) {
        this.mAnaerobic = anaerobic;
    }

    public int getFitnessZone() {
        return this.mFitnessZone;
    }

    public void setFitnessZone(int fitnessZone) {
        this.mFitnessZone = fitnessZone;
    }

    public int getMaxHr() {
        return this.mMaxHr;
    }

    public void setMaxHr(int maxHr) {
        this.mMaxHr = maxHr;
    }

    public int getHealthyHeart() {
        return this.mHealthyHeart;
    }

    public void setHealthyHeart(int healthyHeart) {
        this.mHealthyHeart = healthyHeart;
    }

    public int getRedLine() {
        return this.mRedLine;
    }

    public void setRedLine(int redLine) {
        this.mRedLine = redLine;
    }

    public int getOver() {
        return this.mOver;
    }

    public void setOver(int over) {
        this.mOver = over;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    private HRZones(Parcel in) {
        this.mUnder = in.readInt();
        this.mAerobic = in.readInt();
        this.mAnaerobic = in.readInt();
        this.mFitnessZone = in.readInt();
        this.mHealthyHeart = in.readInt();
        this.mRedLine = in.readInt();
        this.mOver = in.readInt();
        this.mMaxHr = in.readInt();
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mUnder);
        dest.writeInt(this.mAerobic);
        dest.writeInt(this.mAnaerobic);
        dest.writeInt(this.mFitnessZone);
        dest.writeInt(this.mHealthyHeart);
        dest.writeInt(this.mRedLine);
        dest.writeInt(this.mOver);
        dest.writeInt(this.mMaxHr);
    }
}

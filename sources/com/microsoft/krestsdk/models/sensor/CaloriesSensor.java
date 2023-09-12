package com.microsoft.krestsdk.models.sensor;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
/* loaded from: classes.dex */
public class CaloriesSensor extends SensorBase implements Parcelable {
    public static final Parcelable.Creator<CaloriesSensor> CREATOR = new Parcelable.Creator<CaloriesSensor>() { // from class: com.microsoft.krestsdk.models.sensor.CaloriesSensor.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public CaloriesSensor createFromParcel(Parcel in) {
            return new CaloriesSensor(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public CaloriesSensor[] newArray(int size) {
            return new CaloriesSensor[size];
        }
    };
    @SerializedName("Cals")
    private int mCalories;
    @SerializedName("CalsHr")
    private int mCaloriesHr;
    @SerializedName("CalsMotion")
    private int mCaloriesMotion;
    @SerializedName("CalsNotWorn")
    private int mCaloriesNotWorn;

    public int getCalories() {
        return this.mCalories;
    }

    public void setCalories(int mCalories) {
        this.mCalories = mCalories;
    }

    public int getCaloriesHr() {
        return this.mCaloriesHr;
    }

    public void setCaloriesHr(int mCaloriesHr) {
        this.mCaloriesHr = mCaloriesHr;
    }

    public int getCaloriesMotion() {
        return this.mCaloriesMotion;
    }

    public void setCaloriesMotion(int mCaloriesMotion) {
        this.mCaloriesMotion = mCaloriesMotion;
    }

    public int getCaloriesNotWorn() {
        return this.mCaloriesNotWorn;
    }

    public void setCaloriesNotWorn(int mCaloriesNotWorn) {
        this.mCaloriesNotWorn = mCaloriesNotWorn;
    }

    protected CaloriesSensor(Parcel in) {
        super(in);
        this.mCalories = in.readInt();
        this.mCaloriesHr = in.readInt();
        this.mCaloriesMotion = in.readInt();
        this.mCaloriesNotWorn = in.readInt();
    }

    @Override // com.microsoft.krestsdk.models.sensor.SensorBase, android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // com.microsoft.krestsdk.models.sensor.SensorBase, android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(this.mCalories);
        dest.writeInt(this.mCaloriesHr);
        dest.writeInt(this.mCaloriesMotion);
        dest.writeInt(this.mCaloriesNotWorn);
    }
}

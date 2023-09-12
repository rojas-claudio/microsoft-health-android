package com.google.android.gms.location;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
/* loaded from: classes.dex */
public class DetectedActivity implements SafeParcelable {
    public static final DetectedActivityCreator CREATOR = new DetectedActivityCreator();
    public static final int IN_VEHICLE = 0;
    public static final int ON_BICYCLE = 1;
    public static final int ON_FOOT = 2;
    public static final int STILL = 3;
    public static final int TILTING = 5;
    public static final int UNKNOWN = 4;
    private final int iM;
    int oy;
    int oz;

    public DetectedActivity(int activityType, int confidence) {
        this.iM = 1;
        this.oy = activityType;
        this.oz = confidence;
    }

    public DetectedActivity(int versionCode, int activityType, int confidence) {
        this.iM = versionCode;
        this.oy = activityType;
        this.oz = confidence;
    }

    private int W(int i) {
        if (i > 6) {
            return 4;
        }
        return i;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public int getConfidence() {
        return this.oz;
    }

    public int getType() {
        return W(this.oy);
    }

    public int getVersionCode() {
        return this.iM;
    }

    public String toString() {
        return "DetectedActivity [type=" + getType() + ", confidence=" + this.oz + "]";
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        DetectedActivityCreator.a(this, out, flags);
    }
}

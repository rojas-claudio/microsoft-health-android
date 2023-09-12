package com.microsoft.krestsdk.models;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
/* loaded from: classes.dex */
public class GoalValueCurrent implements Parcelable {
    public static final Parcelable.Creator<GoalValueCurrent> CREATOR = new Parcelable.Creator<GoalValueCurrent>() { // from class: com.microsoft.krestsdk.models.GoalValueCurrent.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public GoalValueCurrent createFromParcel(Parcel source) {
            return new GoalValueCurrent(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public GoalValueCurrent[] newArray(int size) {
            return new GoalValueCurrent[size];
        }
    };
    @SerializedName("Extension")
    private String mExtension;

    public void setExtension(String extension) {
        this.mExtension = extension;
    }

    public String getExtension() {
        return this.mExtension;
    }

    public GoalValueCurrent(Parcel source) {
        readFromParcel(source);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mExtension);
    }

    public void readFromParcel(Parcel in) {
        this.mExtension = in.readString();
    }
}

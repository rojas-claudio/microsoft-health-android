package com.microsoft.krestsdk.models.sensor;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
/* loaded from: classes.dex */
public class TimeZoneSensor extends SensorBase implements Parcelable {
    public static final Parcelable.Creator<TimeZoneSensor> CREATOR = new Parcelable.Creator<TimeZoneSensor>() { // from class: com.microsoft.krestsdk.models.sensor.TimeZoneSensor.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public TimeZoneSensor createFromParcel(Parcel in) {
            return new TimeZoneSensor(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public TimeZoneSensor[] newArray(int size) {
            return new TimeZoneSensor[size];
        }
    };
    @SerializedName("OffsetMinutes")
    private int mOffsetMinutes;
    @SerializedName("TimezoneCode")
    private String mTimezoneCode;

    protected TimeZoneSensor(Parcel in) {
        super(in);
        this.mOffsetMinutes = in.readInt();
        this.mTimezoneCode = in.readString();
    }

    @Override // com.microsoft.krestsdk.models.sensor.SensorBase, android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // com.microsoft.krestsdk.models.sensor.SensorBase, android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(this.mOffsetMinutes);
        dest.writeString(this.mTimezoneCode);
    }
}

package com.microsoft.band.internal.device.subscription;

import android.os.Parcel;
import android.os.Parcelable;
import com.microsoft.band.sensors.BandCaloriesEvent;
import java.nio.ByteBuffer;
/* loaded from: classes.dex */
public final class CaloriesData extends SubscriptionDataModel implements BandCaloriesEvent {
    public static final Parcelable.Creator<CaloriesData> CREATOR = new Parcelable.Creator<CaloriesData>() { // from class: com.microsoft.band.internal.device.subscription.CaloriesData.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public CaloriesData createFromParcel(Parcel in) {
            return new CaloriesData(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public CaloriesData[] newArray(int size) {
            return new CaloriesData[size];
        }
    };
    private final long mCalories;
    private final long mHeartRateCalories;
    private final long mMotionCalories;
    private final long mNotWornCalories;
    private final long mSpeedCalories;

    protected CaloriesData(Parcel in) {
        super(in);
        long[] longArray = new long[5];
        in.readLongArray(longArray);
        this.mCalories = longArray[0];
        this.mHeartRateCalories = longArray[1];
        this.mMotionCalories = longArray[2];
        this.mNotWornCalories = longArray[3];
        this.mSpeedCalories = longArray[4];
    }

    public CaloriesData(ByteBuffer buffer) {
        super(buffer);
        this.mCalories = buffer.getInt();
        this.mHeartRateCalories = buffer.getInt();
        this.mMotionCalories = buffer.getInt();
        this.mNotWornCalories = buffer.getInt();
        this.mSpeedCalories = buffer.getInt();
    }

    @Override // com.microsoft.band.sensors.BandCaloriesEvent
    public final long getCalories() {
        return this.mCalories;
    }

    @Override // com.microsoft.band.internal.device.subscription.SubscriptionDataModel
    protected final void format(StringBuffer sb) {
        sb.append(String.format("Calories = %d kilocalories\n", Long.valueOf(getCalories())));
    }

    @Override // com.microsoft.band.internal.device.subscription.SubscriptionDataModel, com.microsoft.band.internal.device.DeviceDataModel, android.os.Parcelable
    public final void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeLongArray(new long[]{this.mCalories, this.mHeartRateCalories, this.mMotionCalories, this.mNotWornCalories, this.mSpeedCalories});
    }
}

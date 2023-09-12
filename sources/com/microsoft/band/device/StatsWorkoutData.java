package com.microsoft.band.device;

import com.microsoft.band.internal.BandDeviceConstants;
import com.microsoft.band.internal.util.BitHelper;
import java.io.Serializable;
import java.nio.ByteBuffer;
/* loaded from: classes.dex */
public class StatsWorkoutData implements Serializable, CommandStruct {
    private static final int STRUCT_SIZE = 38;
    private static final long serialVersionUID = 7699052811688261578L;
    private long mPreviousWorkoutAverageHeartrate;
    private long mPreviousWorkoutCalories;
    private long mPreviousWorkoutDuration;
    private FileTime mPreviousWorkoutEndTime;
    private long mPreviousWorkoutFeeling;
    private long mPreviousWorkoutMaximumHeartrate;
    private FileTime mTimestamp;
    private int mVersion;

    @Override // com.microsoft.band.device.CommandStruct
    public void parseData(ByteBuffer data) {
        this.mTimestamp = FileTime.valueOf(data);
        this.mVersion = data.getShort() & 65535;
        this.mPreviousWorkoutDuration = BitHelper.unsignedIntegerToLong(data.getInt());
        this.mPreviousWorkoutCalories = BitHelper.unsignedIntegerToLong(data.getInt());
        this.mPreviousWorkoutAverageHeartrate = BitHelper.unsignedIntegerToLong(data.getInt());
        this.mPreviousWorkoutMaximumHeartrate = BitHelper.unsignedIntegerToLong(data.getInt());
        this.mPreviousWorkoutEndTime = FileTime.valueOf(data);
        this.mPreviousWorkoutFeeling = BitHelper.unsignedIntegerToLong(data.getInt());
    }

    @Override // com.microsoft.band.device.CommandStruct
    public BandDeviceConstants.Command getCommand() {
        return BandDeviceConstants.Command.CargoPersistedStatisticsWorkoutGet;
    }

    @Override // com.microsoft.band.device.CommandStruct
    public int getSize() {
        return 38;
    }

    public FileTime getTimestamp() {
        return this.mTimestamp;
    }

    public StatsWorkoutData setTimestamp(FileTime timestamp) {
        this.mTimestamp = timestamp;
        return this;
    }

    public int getVersion() {
        return this.mVersion;
    }

    public StatsWorkoutData setVersion(int version) {
        this.mVersion = version;
        return this;
    }

    public long getPreviousWorkoutDuration() {
        return this.mPreviousWorkoutDuration;
    }

    public StatsWorkoutData setPreviousWorkoutDuration(long previousWorkoutDuration) {
        this.mPreviousWorkoutDuration = previousWorkoutDuration;
        return this;
    }

    public long getPreviousWorkoutCalories() {
        return this.mPreviousWorkoutCalories;
    }

    public StatsWorkoutData setPreviousWorkoutCalories(long previousWorkoutCalories) {
        this.mPreviousWorkoutCalories = previousWorkoutCalories;
        return this;
    }

    public long getPreviousWorkoutAverageHeartrate() {
        return this.mPreviousWorkoutAverageHeartrate;
    }

    public StatsWorkoutData setPreviousWorkoutAverageHeartrate(long previousWorkoutAverageHeartrate) {
        this.mPreviousWorkoutAverageHeartrate = previousWorkoutAverageHeartrate;
        return this;
    }

    public long getPreviousWorkoutMaximumHeartrate() {
        return this.mPreviousWorkoutMaximumHeartrate;
    }

    public StatsWorkoutData setPreviousWorkoutMaximumHeartrate(long previousWorkoutMaximumHeartrate) {
        this.mPreviousWorkoutMaximumHeartrate = previousWorkoutMaximumHeartrate;
        return this;
    }

    public FileTime getPreviousWorkoutEndTime() {
        return this.mPreviousWorkoutEndTime;
    }

    public StatsWorkoutData setPreviousWorkoutEndTime(FileTime previousWorkoutEndTime) {
        this.mPreviousWorkoutEndTime = previousWorkoutEndTime;
        return this;
    }

    public long getPreviousWorkoutFeeling() {
        return this.mPreviousWorkoutFeeling;
    }

    public StatsWorkoutData setPreviousWorkoutFeeling(long previousWorkoutFeeling) {
        this.mPreviousWorkoutFeeling = previousWorkoutFeeling;
        return this;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("StatsWorkoutData{");
        sb.append("timestamp=").append(this.mTimestamp);
        sb.append(", version=").append(this.mVersion);
        sb.append(", previousWorkoutDuration=").append(this.mPreviousWorkoutDuration);
        sb.append(", previousWorkoutCalories=").append(this.mPreviousWorkoutCalories);
        sb.append(", previousWorkoutAverageHeartrate=").append(this.mPreviousWorkoutAverageHeartrate);
        sb.append(", previousWorkoutMaximumHeartrate=").append(this.mPreviousWorkoutMaximumHeartrate);
        sb.append(", previousWorkoutEndTime=").append(this.mPreviousWorkoutEndTime);
        sb.append(", previousWorkoutFeeling=").append(this.mPreviousWorkoutFeeling);
        sb.append('}');
        return sb.toString();
    }
}

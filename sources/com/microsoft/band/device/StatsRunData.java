package com.microsoft.band.device;

import com.microsoft.band.internal.BandDeviceConstants;
import com.microsoft.band.internal.util.BitHelper;
import java.io.Serializable;
import java.nio.ByteBuffer;
/* loaded from: classes.dex */
public class StatsRunData implements Serializable, CommandStruct {
    public static final int STRUCT_SIZE = 50;
    private static final long serialVersionUID = 6605481159591829300L;
    private long mPreviousRunAverageHeartrate;
    private long mPreviousRunAveragePace;
    private long mPreviousRunBestSplit;
    private long mPreviousRunCalories;
    private long mPreviousRunDistance;
    private long mPreviousRunDuration;
    private FileTime mPreviousRunEndTime;
    private long mPreviousRunFeeling;
    private long mPreviousRunMaximumHeartrate;
    private FileTime mTimeStamp;
    private int mVersion;

    @Override // com.microsoft.band.device.CommandStruct
    public void parseData(ByteBuffer data) {
        this.mTimeStamp = FileTime.valueOf(data);
        this.mVersion = BitHelper.unsignedShortToInteger(data.getShort());
        this.mPreviousRunDuration = BitHelper.unsignedIntegerToLong(data.getInt());
        this.mPreviousRunDistance = BitHelper.unsignedIntegerToLong(data.getInt());
        this.mPreviousRunAveragePace = BitHelper.unsignedIntegerToLong(data.getInt());
        this.mPreviousRunBestSplit = BitHelper.unsignedIntegerToLong(data.getInt());
        this.mPreviousRunCalories = BitHelper.unsignedIntegerToLong(data.getInt());
        this.mPreviousRunAverageHeartrate = BitHelper.unsignedIntegerToLong(data.getInt());
        this.mPreviousRunMaximumHeartrate = BitHelper.unsignedIntegerToLong(data.getInt());
        this.mPreviousRunEndTime = FileTime.valueOf(data);
        this.mPreviousRunFeeling = BitHelper.unsignedIntegerToLong(data.getInt());
    }

    @Override // com.microsoft.band.device.CommandStruct
    public BandDeviceConstants.Command getCommand() {
        return BandDeviceConstants.Command.CargoPersistedStatisticsRunGet;
    }

    @Override // com.microsoft.band.device.CommandStruct
    public int getSize() {
        return 50;
    }

    public FileTime getTimeStamp() {
        return this.mTimeStamp;
    }

    public StatsRunData setTimeStamp(FileTime timeStamp) {
        this.mTimeStamp = timeStamp;
        return this;
    }

    public int getVersion() {
        return this.mVersion;
    }

    public StatsRunData setVersion(int version) {
        this.mVersion = version;
        return this;
    }

    public long getPreviousRunDuration() {
        return this.mPreviousRunDuration;
    }

    public StatsRunData setPreviousRunDuration(long previousRunDuration) {
        this.mPreviousRunDuration = previousRunDuration;
        return this;
    }

    public long getPreviousRunDistance() {
        return this.mPreviousRunDistance;
    }

    public StatsRunData setPreviousRunDistance(long previousRunDistance) {
        this.mPreviousRunDistance = previousRunDistance;
        return this;
    }

    public long getPreviousRunBestSplit() {
        return this.mPreviousRunBestSplit;
    }

    public StatsRunData setPreviousRunBestSplit(long previousRunBestSplit) {
        this.mPreviousRunBestSplit = previousRunBestSplit;
        return this;
    }

    public long getPreviousRunCalories() {
        return this.mPreviousRunCalories;
    }

    public StatsRunData setPreviousRunCalories(long previousRunCalories) {
        this.mPreviousRunCalories = previousRunCalories;
        return this;
    }

    public long getPreviousRunAverageHeartrate() {
        return this.mPreviousRunAverageHeartrate;
    }

    public StatsRunData setPreviousRunAverageHeartrate(long previousRunAverageHeartrate) {
        this.mPreviousRunAverageHeartrate = previousRunAverageHeartrate;
        return this;
    }

    public long getPreviousRunMaximumHeartrate() {
        return this.mPreviousRunMaximumHeartrate;
    }

    public StatsRunData setPreviousRunMaximumHeartrate(long previousRunMaximumHeartrate) {
        this.mPreviousRunMaximumHeartrate = previousRunMaximumHeartrate;
        return this;
    }

    public FileTime getPreviousRunEndTime() {
        return this.mPreviousRunEndTime;
    }

    public StatsRunData setPreviousRunEndTime(FileTime previousRunEndTime) {
        this.mPreviousRunEndTime = previousRunEndTime;
        return this;
    }

    public long getPreviousRunFeeling() {
        return this.mPreviousRunFeeling;
    }

    public StatsRunData setPreviousRunFeeling(long previousRunFeeling) {
        this.mPreviousRunFeeling = previousRunFeeling;
        return this;
    }

    public long getPreviousRunAveragePace() {
        return this.mPreviousRunAveragePace;
    }

    public StatsRunData setPreviousRunAveragePace(long previousRunAveragePace) {
        this.mPreviousRunAveragePace = previousRunAveragePace;
        return this;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("SummationRunEventEntry{");
        sb.append("timeStamp=").append(this.mTimeStamp);
        sb.append(", version=").append(this.mVersion);
        sb.append(", previousRunDuration=").append(this.mPreviousRunDuration);
        sb.append(", previousRunDistance=").append(this.mPreviousRunDistance);
        sb.append(", previousRunAveragePace").append(this.mPreviousRunAveragePace);
        sb.append(", previousRunBestSplit=").append(this.mPreviousRunBestSplit);
        sb.append(", previousRunCalories=").append(this.mPreviousRunCalories);
        sb.append(", previousRunAverageHeartrate=").append(this.mPreviousRunAverageHeartrate);
        sb.append(", previousRunMaximumHeartrate=").append(this.mPreviousRunMaximumHeartrate);
        sb.append(", previousRunEndTime=").append(this.mPreviousRunEndTime);
        sb.append(", previousRunFeeling=").append(this.mPreviousRunFeeling);
        sb.append('}');
        return sb.toString();
    }
}

package com.microsoft.band.device;

import com.microsoft.band.internal.BandDeviceConstants;
import com.microsoft.band.internal.util.BitHelper;
import java.io.Serializable;
import java.nio.ByteBuffer;
/* loaded from: classes.dex */
public class StatsSleepData implements Serializable, CommandStruct {
    private static final int STRUCT_SIZE = 54;
    private static final long serialVersionUID = 6225932086478992855L;
    private long mPreviousCalsBurned;
    private long mPreviousHowYouFelt;
    private long mPreviousRestingBPM;
    private long mPreviousSleepDuration;
    private long mPreviousSleepEfficiency;
    private FileTime mPreviousSleepEndTime;
    private long mPreviousTimeAsleep;
    private long mPreviousTimeAwake;
    private long mPreviousTimeToFallAsleep;
    private long mPreviousTimesWokeUp;
    private FileTime mTimestamp;
    private int mVersion;

    @Override // com.microsoft.band.device.CommandStruct
    public void parseData(ByteBuffer data) {
        this.mTimestamp = FileTime.valueOf(data);
        this.mVersion = BitHelper.unsignedShortToInteger(data.getShort());
        this.mPreviousSleepDuration = BitHelper.unsignedIntegerToLong(data.getInt());
        this.mPreviousTimesWokeUp = BitHelper.unsignedIntegerToLong(data.getInt());
        this.mPreviousTimeAwake = BitHelper.unsignedIntegerToLong(data.getInt());
        this.mPreviousTimeAsleep = BitHelper.unsignedIntegerToLong(data.getInt());
        this.mPreviousCalsBurned = BitHelper.unsignedIntegerToLong(data.getInt());
        this.mPreviousRestingBPM = BitHelper.unsignedIntegerToLong(data.getInt());
        this.mPreviousSleepEfficiency = BitHelper.unsignedIntegerToLong(data.getInt());
        this.mPreviousSleepEndTime = FileTime.valueOf(data);
        this.mPreviousTimeToFallAsleep = BitHelper.unsignedIntegerToLong(data.getInt());
        this.mPreviousHowYouFelt = BitHelper.unsignedIntegerToLong(data.getInt());
    }

    @Override // com.microsoft.band.device.CommandStruct
    public BandDeviceConstants.Command getCommand() {
        return BandDeviceConstants.Command.CargoPersistedStatisticsSleepGet;
    }

    @Override // com.microsoft.band.device.CommandStruct
    public int getSize() {
        return 54;
    }

    public FileTime getTimestamp() {
        return this.mTimestamp;
    }

    public StatsSleepData setTimestamp(FileTime timestamp) {
        this.mTimestamp = timestamp;
        return this;
    }

    public int getVersion() {
        return this.mVersion;
    }

    public StatsSleepData setVersion(int version) {
        this.mVersion = version;
        return this;
    }

    public long getPreviousSleepDuration() {
        return this.mPreviousSleepDuration;
    }

    public StatsSleepData setPreviousSleepDuration(long previousSleepDuration) {
        this.mPreviousSleepDuration = previousSleepDuration;
        return this;
    }

    public long getPreviousTimesWokeUp() {
        return this.mPreviousTimesWokeUp;
    }

    public StatsSleepData setPreviousTimesWokeUp(long previousTimesWokeUp) {
        this.mPreviousTimesWokeUp = previousTimesWokeUp;
        return this;
    }

    public long getPreviousTimeAwake() {
        return this.mPreviousTimeAwake;
    }

    public StatsSleepData setPreviousTimeAwake(long previousTimeAwake) {
        this.mPreviousTimeAwake = previousTimeAwake;
        return this;
    }

    public long getPreviousTimeAsleep() {
        return this.mPreviousTimeAsleep;
    }

    public StatsSleepData setPreviousTimeAsleep(long previousTimeAsleep) {
        this.mPreviousTimeAsleep = previousTimeAsleep;
        return this;
    }

    public long getPreviousCalsBurned() {
        return this.mPreviousCalsBurned;
    }

    public StatsSleepData setPreviousCalsBurned(long previousCalsBurned) {
        this.mPreviousCalsBurned = previousCalsBurned;
        return this;
    }

    public long getPreviousRestingBPM() {
        return this.mPreviousRestingBPM;
    }

    public StatsSleepData setPreviousRestingBPM(long previousRestingBPM) {
        this.mPreviousRestingBPM = previousRestingBPM;
        return this;
    }

    public long getPreviousSleepEfficiency() {
        return this.mPreviousSleepEfficiency;
    }

    public StatsSleepData setPreviousSleepEfficiency(long previousSleepEfficiency) {
        this.mPreviousSleepEfficiency = previousSleepEfficiency;
        return this;
    }

    public FileTime getPreviousSleepEndTime() {
        return this.mPreviousSleepEndTime;
    }

    public StatsSleepData setPreviousSleepEndTime(FileTime previousSleepEndTime) {
        this.mPreviousSleepEndTime = previousSleepEndTime;
        return this;
    }

    public long getPreviousTimeToFallAsleep() {
        return this.mPreviousTimeToFallAsleep;
    }

    public StatsSleepData setPreviousTimeToFallAsleep(long previousTimeToFallAsleep) {
        this.mPreviousTimeToFallAsleep = previousTimeToFallAsleep;
        return this;
    }

    public long getPreviousHowYouFelt() {
        return this.mPreviousHowYouFelt;
    }

    public StatsSleepData setPreviousHowYouFelt(long previousHowYouFelt) {
        this.mPreviousHowYouFelt = previousHowYouFelt;
        return this;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("StatsSleepData{");
        sb.append("timestamp=").append(this.mTimestamp);
        sb.append(", version=").append(this.mVersion);
        sb.append(", previousSleepDuration=").append(this.mPreviousSleepDuration);
        sb.append(", previousTimesWokeUp=").append(this.mPreviousTimesWokeUp);
        sb.append(", previousTimeAwake=").append(this.mPreviousTimeAwake);
        sb.append(", previousTimeAsleep=").append(this.mPreviousTimeAsleep);
        sb.append(", previousCalsBurned=").append(this.mPreviousCalsBurned);
        sb.append(", previousRestingBPM=").append(this.mPreviousRestingBPM);
        sb.append(", previousSleepEfficiency=").append(this.mPreviousSleepEfficiency);
        sb.append(", previousSleepEndTime=").append(this.mPreviousSleepEndTime);
        sb.append(", previousTimeToFallAsleep=").append(this.mPreviousTimeToFallAsleep);
        sb.append(", previousHowYouFelt=").append(this.mPreviousHowYouFelt);
        sb.append('}');
        return sb.toString();
    }
}

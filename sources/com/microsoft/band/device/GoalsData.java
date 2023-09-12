package com.microsoft.band.device;

import com.microsoft.band.internal.util.BitHelper;
import com.microsoft.band.internal.util.BufferUtil;
import com.microsoft.band.internal.util.VersionCheck;
import java.nio.ByteBuffer;
/* loaded from: classes.dex */
public class GoalsData {
    public static final int STRUCT_SIZE = 32;
    public static final int STRUCT_SIZE_V2 = 76;
    private boolean mCaloriesEnabled;
    private long mCaloriesGoal;
    private boolean mDistanceEnabled;
    private long mDistanceGoal;
    private boolean mFloorCountEnabled;
    private long mFloorCountGoal;
    private boolean mStepsEnabled;
    private long mStepsGoal;
    private FileTime mTimestamp;
    private long mVersion;

    public GoalsData() {
        this.mTimestamp = new FileTime(System.currentTimeMillis());
    }

    public GoalsData(ByteBuffer data, int hardwareVersion) {
        if (VersionCheck.isV2DeviceOrGreater(hardwareVersion)) {
            this.mVersion = BitHelper.unsignedIntegerToLong(data.getInt());
        }
        this.mStepsEnabled = data.getInt() != 0;
        this.mCaloriesEnabled = data.getInt() != 0;
        this.mDistanceEnabled = data.getInt() != 0;
        if (VersionCheck.isV2DeviceOrGreater(hardwareVersion)) {
            this.mFloorCountEnabled = data.getInt() != 0;
            data.getInt();
            data.getInt();
            data.getInt();
            data.getInt();
        }
        this.mStepsGoal = BitHelper.unsignedIntegerToLong(data.getInt());
        this.mCaloriesGoal = BitHelper.unsignedIntegerToLong(data.getInt());
        this.mDistanceGoal = BitHelper.unsignedIntegerToLong(data.getInt());
        if (VersionCheck.isV2DeviceOrGreater(hardwareVersion)) {
            this.mFloorCountGoal = BitHelper.unsignedIntegerToLong(data.getInt());
            data.getInt();
            data.getInt();
            data.getInt();
            data.getInt();
        }
        this.mTimestamp = FileTime.valueOf(data);
    }

    public byte[] toBytes(int hardwareVersion) {
        ByteBuffer buffer = BufferUtil.allocateLittleEndian(getDataSize(hardwareVersion));
        if (VersionCheck.isV2DeviceOrGreater(hardwareVersion)) {
            buffer.putInt(BitHelper.longToUnsignedInt(this.mVersion));
        }
        buffer.putInt(this.mStepsEnabled ? 1 : 0);
        buffer.putInt(this.mCaloriesEnabled ? 1 : 0);
        buffer.putInt(this.mDistanceEnabled ? 1 : 0);
        if (VersionCheck.isV2DeviceOrGreater(hardwareVersion)) {
            buffer.putInt(this.mFloorCountEnabled ? 1 : 0);
            buffer.putInt(0);
            buffer.putInt(0);
            buffer.putInt(0);
            buffer.putInt(0);
        }
        buffer.putInt(BitHelper.longToUnsignedInt(this.mStepsGoal));
        buffer.putInt(BitHelper.longToUnsignedInt(this.mCaloriesGoal));
        buffer.putInt(BitHelper.longToUnsignedInt(this.mDistanceGoal));
        if (VersionCheck.isV2DeviceOrGreater(hardwareVersion)) {
            buffer.putInt(BitHelper.longToUnsignedInt(this.mFloorCountGoal));
            buffer.putInt(0);
            buffer.putInt(0);
            buffer.putInt(0);
            buffer.putInt(0);
        }
        buffer.put(this.mTimestamp.toBytes());
        return buffer.array();
    }

    public static int getDataSize(int hardwareVersion) {
        if (!VersionCheck.isV2DeviceOrGreater(hardwareVersion)) {
            return 32;
        }
        return 76;
    }

    public boolean isStepsEnabled() {
        return this.mStepsEnabled;
    }

    public GoalsData setStepsEnabled(boolean value) {
        this.mStepsEnabled = value;
        return this;
    }

    public boolean isCaloriesEnabled() {
        return this.mCaloriesEnabled;
    }

    public GoalsData setCaloriesEnabled(boolean value) {
        this.mCaloriesEnabled = value;
        return this;
    }

    public boolean isDistanceEnabled() {
        return this.mDistanceEnabled;
    }

    public GoalsData setDistanceEnabled(boolean value) {
        this.mDistanceEnabled = value;
        return this;
    }

    public long getStepsGoal() {
        return this.mStepsGoal;
    }

    public GoalsData setStepsGoal(long value) {
        BitHelper.checkUInt32RangeException(value);
        this.mStepsGoal = value;
        return this;
    }

    public long getCaloriesGoal() {
        return this.mCaloriesGoal;
    }

    public GoalsData setCaloriesGoal(long value) {
        BitHelper.checkUInt32RangeException(value);
        this.mCaloriesGoal = value;
        return this;
    }

    public long getDistanceGoal() {
        return this.mDistanceGoal;
    }

    public GoalsData setDistanceGoal(long distanceGoal) {
        BitHelper.checkUInt32RangeException(distanceGoal);
        this.mDistanceGoal = distanceGoal;
        return this;
    }

    public FileTime getTimestamp() {
        return this.mTimestamp;
    }

    public GoalsData setTimestamp(FileTime timestamp) {
        this.mTimestamp = timestamp;
        return this;
    }

    public boolean isFloorCountEnabled() {
        return this.mFloorCountEnabled;
    }

    public GoalsData setFloorCountEnabled(boolean isEnabled) {
        this.mFloorCountEnabled = isEnabled;
        return this;
    }

    public long getFloorCountGoal() {
        return this.mFloorCountGoal;
    }

    public GoalsData setFloorCountGoal(long goal) {
        this.mFloorCountGoal = goal;
        return this;
    }

    public long getVersion() {
        return this.mVersion;
    }

    public GoalsData setVersion(long mVersion) {
        this.mVersion = mVersion;
        return this;
    }
}

package com.microsoft.band.device;

import android.os.Parcel;
import android.os.Parcelable;
import com.microsoft.band.internal.device.DeviceDataModel;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Date;
/* loaded from: classes.dex */
public class FileTime extends DeviceDataModel {
    public static final Parcelable.Creator<FileTime> CREATOR = new Parcelable.Creator<FileTime>() { // from class: com.microsoft.band.device.FileTime.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public FileTime createFromParcel(Parcel in) {
            return new FileTime(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public FileTime[] newArray(int size) {
            return new FileTime[size];
        }
    };
    private static final long EPOCH_SYSTEM_DIFF_MS = 11644473600000L;
    public static final int FILETIME_STRUCT_SIZE = 8;
    private static final long FILETIME_UNITS_PER_MS = 10000;
    private static final long MAX_UNSIGNED_INTEGER = 4294967295L;
    private long mHighDateTime;
    private long mLowDateTime;

    public FileTime() {
        setFromTicks(System.currentTimeMillis());
    }

    public FileTime(long lowDateTime, long highDateTime) {
        setLowDateTime(lowDateTime);
        setHighDateTime(highDateTime);
    }

    public FileTime(long systemTime) {
        setFromTicks(systemTime);
    }

    public FileTime(Date time) {
        setFromTicks(time.getTime());
    }

    public FileTime(Parcel in) {
        super(in);
        this.mLowDateTime = in.readLong();
        this.mHighDateTime = in.readLong();
    }

    public FileTime(ByteBuffer buffer) {
        super(buffer);
        if (buffer == null || buffer.limit() < 8) {
            throw new IllegalArgumentException(String.format("FileTime buffer must contain %d bytes.", 8));
        }
        this.mLowDateTime = buffer.getInt() & 4294967295L;
        this.mHighDateTime = buffer.getInt() & 4294967295L;
    }

    private void setFromTicks(long ticks) {
        long time = (EPOCH_SYSTEM_DIFF_MS + ticks) * FILETIME_UNITS_PER_MS;
        setLowDateTime(4294967295L & time);
        setHighDateTime(time >> 32);
    }

    public long getLowDateTime() {
        return this.mLowDateTime;
    }

    public void setLowDateTime(long lowDateTime) {
        if (lowDateTime > 4294967295L) {
            throw new IllegalArgumentException("lowDateTime");
        }
        this.mLowDateTime = lowDateTime;
    }

    public long getHighDateTime() {
        return this.mHighDateTime;
    }

    public void setHighDateTime(long highDateTime) {
        if (highDateTime > 4294967295L) {
            throw new IllegalArgumentException("highDateTime");
        }
        this.mHighDateTime = highDateTime;
    }

    public long toSystemTime() {
        return toSystemTime(this.mHighDateTime, this.mLowDateTime);
    }

    public Date toDate() {
        return new Date(toSystemTime());
    }

    public static long toSystemTime(long highDateTime, long lowDateTime) {
        long time = (highDateTime & 4294967295L) << 32;
        return ((time | (lowDateTime & 4294967295L)) / FILETIME_UNITS_PER_MS) - EPOCH_SYSTEM_DIFF_MS;
    }

    public byte[] toBytes() {
        byte[] bytes = new byte[8];
        ByteBuffer record = ByteBuffer.wrap(bytes, 0, 4);
        record.order(ByteOrder.LITTLE_ENDIAN);
        record.putInt((int) this.mLowDateTime);
        ByteBuffer record2 = ByteBuffer.wrap(bytes, 4, 4);
        record2.order(ByteOrder.LITTLE_ENDIAN);
        record2.putInt((int) this.mHighDateTime);
        return bytes;
    }

    public static FileTime valueOf(byte[] bytes) {
        if (bytes == null || bytes.length < 8) {
            throw new IllegalArgumentException(String.format("FileTime buffer must contain %d bytes.", 8));
        }
        return valueOf(ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN));
    }

    public static FileTime valueOf(ByteBuffer record) {
        return new FileTime(record);
    }

    public String toString() {
        Date date = new Date(toSystemTime());
        return date.toString();
    }

    @Override // com.microsoft.band.internal.device.DeviceDataModel, android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeLong(this.mLowDateTime);
        dest.writeLong(this.mHighDateTime);
    }
}

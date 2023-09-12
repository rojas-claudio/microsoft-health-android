package com.microsoft.band.device;

import com.microsoft.band.internal.util.BufferUtil;
import com.microsoft.band.internal.util.Validation;
import java.nio.ByteBuffer;
/* loaded from: classes.dex */
public final class NotificationTime {
    public static final int STRUCT_SIZE = 2;
    private byte mHour;
    private boolean mIsEnabled;
    private byte mMinute;

    public NotificationTime() {
    }

    public NotificationTime(int hour, int minute, boolean enabled) {
        setHour(hour);
        setMinute(minute);
        this.mIsEnabled = enabled;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public NotificationTime(ByteBuffer data) {
        setHour(data.get());
        setMinute(data.get());
    }

    public int getHour() {
        return this.mHour;
    }

    public void setHour(int hour) {
        Validation.validateInRange("Hour", hour, 0, 23);
        this.mHour = (byte) hour;
    }

    public int getMinute() {
        return this.mMinute;
    }

    public void setMinute(int minute) {
        Validation.validateInRange("Minute", minute, 0, 59);
        this.mMinute = (byte) minute;
    }

    public boolean isEnabled() {
        return this.mIsEnabled;
    }

    public void setEnabled(boolean isEnabled) {
        this.mIsEnabled = isEnabled;
    }

    public byte[] toBytes() {
        ByteBuffer buffer = BufferUtil.allocateLittleEndian(2);
        buffer.put(this.mHour);
        buffer.put(this.mMinute);
        return buffer.array();
    }
}

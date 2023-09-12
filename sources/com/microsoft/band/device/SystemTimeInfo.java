package com.microsoft.band.device;

import com.microsoft.band.internal.util.BitHelper;
import com.microsoft.band.internal.util.BufferUtil;
import com.unnamed.b.atv.model.TreeNode;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import org.joda.time.DateTime;
import org.joda.time.base.BaseDateTime;
/* loaded from: classes.dex */
public class SystemTimeInfo implements Serializable {
    public static final short SYSTEM_TIME_LENGTH = 16;
    private static final long serialVersionUID = -6747936435909218808L;
    private int mDay;
    private int mDayOfWeek;
    private int mHour;
    private int mMilliseconds;
    private int mMinute;
    private int mMonth;
    private int mSecond;
    private int mYear;

    public SystemTimeInfo() {
        this.mYear = 0;
        this.mMonth = 0;
        this.mDayOfWeek = 0;
        this.mDay = 0;
        this.mHour = 0;
        this.mMinute = 0;
        this.mSecond = 0;
        this.mMilliseconds = 0;
    }

    public SystemTimeInfo(BaseDateTime dateTime) {
        this.mYear = 0;
        this.mMonth = dateTime.getMonthOfYear();
        this.mDayOfWeek = 0;
        this.mDay = dateTime.getDayOfMonth();
        this.mHour = dateTime.getHourOfDay();
        this.mMinute = dateTime.getMinuteOfHour();
        this.mSecond = dateTime.getSecondOfMinute();
        this.mMilliseconds = dateTime.getMillisOfSecond();
    }

    public SystemTimeInfo(byte[] data) {
        this(ByteBuffer.wrap(data).order(ByteOrder.LITTLE_ENDIAN));
    }

    public SystemTimeInfo(ByteBuffer buffer) {
        this.mYear = BitHelper.unsignedShortToInteger(buffer.getShort());
        this.mMonth = BitHelper.unsignedShortToInteger(buffer.getShort());
        this.mDayOfWeek = BitHelper.unsignedShortToInteger(buffer.getShort());
        this.mDay = BitHelper.unsignedShortToInteger(buffer.getShort());
        this.mHour = BitHelper.unsignedShortToInteger(buffer.getShort());
        this.mMinute = BitHelper.unsignedShortToInteger(buffer.getShort());
        this.mSecond = BitHelper.unsignedShortToInteger(buffer.getShort());
        this.mMilliseconds = BitHelper.unsignedShortToInteger(buffer.getShort());
    }

    public byte[] toByte() {
        ByteBuffer buffer = BufferUtil.allocateLittleEndian(16);
        buffer.putShort(BitHelper.intToUnsignedShort(this.mYear));
        buffer.putShort(BitHelper.intToUnsignedShort(this.mMonth));
        buffer.putShort(BitHelper.intToUnsignedShort(this.mDayOfWeek));
        buffer.putShort(BitHelper.intToUnsignedShort(this.mDay));
        buffer.putShort(BitHelper.intToUnsignedShort(this.mHour));
        buffer.putShort(BitHelper.intToUnsignedShort(this.mMinute));
        buffer.putShort(BitHelper.intToUnsignedShort(this.mSecond));
        buffer.putShort(BitHelper.intToUnsignedShort(this.mMilliseconds));
        return buffer.array();
    }

    public String toString() {
        return this.mYear + "-" + this.mMonth + "-" + this.mDay + ", " + this.mHour + TreeNode.NODES_ID_SEPARATOR + this.mMinute + TreeNode.NODES_ID_SEPARATOR + this.mSecond + "." + this.mMilliseconds;
    }

    public DateTime toDateTime() {
        if (this.mYear == 0) {
            throw new IllegalArgumentException("Year is set to Zero");
        }
        DateTime dt = new DateTime(this.mYear, this.mMonth, this.mDay, this.mHour, this.mMinute, this.mSecond);
        return dt;
    }

    public int getYear() {
        return this.mYear;
    }

    public void setYear(int year) {
        this.mYear = year;
    }

    public int getMonth() {
        return this.mMonth;
    }

    public void setMonth(int month) {
        this.mMonth = month;
    }

    public int getDayOfWeek() {
        return this.mDayOfWeek;
    }

    public void setDayOfWeek(int dayOfWeek) {
        this.mDayOfWeek = dayOfWeek;
    }

    public int getDay() {
        return this.mDay;
    }

    public void setDay(int day) {
        this.mDay = day;
    }

    public int getHour() {
        return this.mHour;
    }

    public void setHour(int hour) {
        this.mHour = hour;
    }

    public int getMinute() {
        return this.mMinute;
    }

    public void setMinute(int minute) {
        this.mMinute = minute;
    }

    public int getSecond() {
        return this.mSecond;
    }

    public void setSecond(int second) {
        this.mSecond = second;
    }

    public int getMilliseconds() {
        return this.mMilliseconds;
    }

    public void setMilliseconds(int milliseconds) {
        this.mMilliseconds = milliseconds;
    }
}

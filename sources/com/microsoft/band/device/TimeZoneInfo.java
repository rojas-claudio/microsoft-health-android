package com.microsoft.band.device;

import com.microsoft.band.internal.util.BufferUtil;
import com.microsoft.band.internal.util.Validation;
import com.microsoft.band.util.StringHelper;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.Locale;
import java.util.TimeZone;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeZone;
import org.joda.time.MutableDateTime;
/* loaded from: classes.dex */
public class TimeZoneInfo implements Serializable {
    public static final int TIMEZONE_NAME_LENGTH = 30;
    public static final int TIMEZONE_STRUCTURE_LENGTH = 96;
    private static final long serialVersionUID = 4165214879750847112L;
    private short mDaylightOffsetMinutes;
    private String mName;
    private SystemTimeInfo mTransitionToDaylight;
    private SystemTimeInfo mTransitionToStandard;
    private short mZoneOffsetMinutes;

    public TimeZoneInfo(ByteBuffer buffer) {
        byte[] nameBytes = new byte[60];
        buffer.get(nameBytes);
        this.mName = StringHelper.valueOf(nameBytes).trim();
        this.mZoneOffsetMinutes = buffer.getShort();
        this.mDaylightOffsetMinutes = buffer.getShort();
        this.mTransitionToStandard = new SystemTimeInfo(buffer);
        this.mTransitionToDaylight = new SystemTimeInfo(buffer);
    }

    public TimeZoneInfo(DateTime dateTime) {
        init(dateTime);
    }

    public TimeZoneInfo() {
        DateTime dateTime = new DateTime();
        init(dateTime);
    }

    private void init(DateTime dateTime) {
        TimeZone currentTimeZone = TimeZone.getDefault();
        DateTimeZone zone = DateTimeZone.forTimeZone(currentTimeZone);
        this.mName = currentTimeZone.getDisplayName(false, 0, Locale.getDefault());
        this.mDaylightOffsetMinutes = (short) (currentTimeZone.getDSTSavings() / DateTimeConstants.MILLIS_PER_MINUTE);
        this.mZoneOffsetMinutes = (short) (zone.getOffset(dateTime.getMillis()) / DateTimeConstants.MILLIS_PER_MINUTE);
        MutableDateTime prevTransition = new MutableDateTime(dateTime.minusDays(1).getMillis(), zone);
        if (this.mDaylightOffsetMinutes != 0) {
            MutableDateTime nextTransition = new MutableDateTime(zone.nextTransition(dateTime.getMillis()), zone);
            if (nextTransition.equals(dateTime)) {
                this.mDaylightOffsetMinutes = (short) 0;
                this.mTransitionToDaylight = new SystemTimeInfo();
                this.mTransitionToStandard = new SystemTimeInfo();
                return;
            }
            MutableDateTime nextTransition2 = new MutableDateTime(zone.previousTransition(nextTransition.getMillis()), zone);
            if (currentTimeZone.inDaylightTime(dateTime.toDate())) {
                this.mDaylightOffsetMinutes = (short) (-this.mDaylightOffsetMinutes);
            }
            this.mTransitionToDaylight = new SystemTimeInfo(nextTransition2);
            this.mTransitionToStandard = new SystemTimeInfo(prevTransition);
            return;
        }
        this.mDaylightOffsetMinutes = (short) 0;
        this.mTransitionToDaylight = new SystemTimeInfo();
        this.mTransitionToStandard = new SystemTimeInfo();
    }

    public byte[] toBytes() {
        ByteBuffer buffer = BufferUtil.allocateLittleEndian(96);
        byte[] nameBytesPadded = new byte[60];
        byte[] nameBytes = StringHelper.getBytes(this.mName);
        System.arraycopy(nameBytes, 0, nameBytesPadded, 0, nameBytes.length);
        return buffer.put(nameBytesPadded).putShort(this.mZoneOffsetMinutes).putShort(this.mDaylightOffsetMinutes).put(this.mTransitionToStandard.toByte()).put(this.mTransitionToDaylight.toByte()).array();
    }

    public String getName() {
        return this.mName;
    }

    public void setName(String name) {
        Validation.validStringNullAndLength(name, 30, "TimeZone Name");
        this.mName = name;
    }

    public short getZoneOffsetMinutes() {
        return this.mZoneOffsetMinutes;
    }

    public void setZoneOffsetMinutes(short zoneOffsetMinutes) {
        this.mZoneOffsetMinutes = zoneOffsetMinutes;
    }

    public short getDaylightOffsetMinutes() {
        return this.mDaylightOffsetMinutes;
    }

    public void setDaylightOffsetMinutes(short daylightOffsetMinutes) {
        this.mDaylightOffsetMinutes = daylightOffsetMinutes;
    }

    public void setTransitionToStandard(DateTime transitionToStandardDate) {
        if (transitionToStandardDate == null) {
            this.mTransitionToStandard = new SystemTimeInfo();
        } else {
            this.mTransitionToStandard = new SystemTimeInfo(transitionToStandardDate);
        }
    }

    public void setTransitionToDaylight(DateTime transitionToDaylightDate) {
        if (transitionToDaylightDate == null) {
            this.mTransitionToDaylight = new SystemTimeInfo();
        } else {
            this.mTransitionToDaylight = new SystemTimeInfo(transitionToDaylightDate);
        }
    }

    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append(String.format("TimeZoneInfo:%s", System.getProperty("line.separator")));
        result.append(String.format("     |--name= %s ", this.mName)).append(System.getProperty("line.separator"));
        result.append(String.format("     |--zoneOffsetMinutes= %d ", Short.valueOf(this.mZoneOffsetMinutes))).append(System.getProperty("line.separator"));
        result.append(String.format("     |--daylightOffsetMinutes= %d ", Short.valueOf(this.mDaylightOffsetMinutes))).append(System.getProperty("line.separator"));
        result.append(String.format("     |--transitionToStandard= %s ", this.mTransitionToStandard.toString())).append(System.getProperty("line.separator"));
        result.append(String.format("     |--transitionToDaylight= %s ", this.mTransitionToDaylight.toString())).append(System.getProperty("line.separator"));
        return result.toString();
    }

    public String toShortString() {
        return String.format("TZI:", new Object[0]) + String.format(" name= %s ", this.mName) + String.format(" zoneOffset= %d ", Short.valueOf(this.mZoneOffsetMinutes)) + String.format(" daylightOffse= %d ", Short.valueOf(this.mDaylightOffsetMinutes)) + String.format(" tranzStandard= %s ", this.mTransitionToStandard.toString()) + String.format(" tranzDaylight= %s ", this.mTransitionToDaylight.toString());
    }
}

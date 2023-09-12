package com.microsoft.band.device;

import com.microsoft.band.internal.util.BufferUtil;
import com.microsoft.band.internal.util.StringUtil;
import com.microsoft.band.internal.util.Validation;
import com.microsoft.band.util.StringHelper;
import java.nio.ByteBuffer;
/* loaded from: classes.dex */
public final class SleepNotification {
    private static final int DAYS_ENABLED_SIZE = 2;
    private static final int FRIDAY = 4;
    private static final int MONDAY = 0;
    private static final int NUM_DAYS = 7;
    private static final int SATURDAY = 5;
    private static final int SLEEP_NOTIFICATION_MAX_BODY_TEXT = 40;
    private static final int SLEEP_NOTIFICATION_MAX_HEADER_TEXT = 80;
    private static final int START_DAY_BIT = 128;
    public static final int STRUCT_SIZE = 264;
    private static final int SUNDAY = 6;
    private static final int THURSDAY = 3;
    private static final int TUESDAY = 1;
    private static final int WEDNESDAY = 2;
    private String mBodyText;
    private String mHeaderText;
    private final NotificationTime[] mSleepNotificationData;

    public SleepNotification() {
        this.mSleepNotificationData = new NotificationTime[]{new NotificationTime(), new NotificationTime(), new NotificationTime(), new NotificationTime(), new NotificationTime(), new NotificationTime(), new NotificationTime()};
    }

    public SleepNotification(ByteBuffer data) {
        this.mSleepNotificationData = new NotificationTime[7];
        for (int i = 0; i < 7; i++) {
            this.mSleepNotificationData[i] = new NotificationTime(data);
        }
        short daysEnabled = data.getShort();
        int startBit = 128;
        NotificationTime[] arr$ = this.mSleepNotificationData;
        for (NotificationTime notificationTime : arr$) {
            notificationTime.setEnabled((daysEnabled & startBit) == startBit);
            startBit >>= 1;
        }
        byte[] headerByte = new byte[160];
        data.get(headerByte);
        this.mHeaderText = StringHelper.valueOf(headerByte).trim();
        byte[] bodyByte = new byte[80];
        data.get(bodyByte);
        this.mBodyText = StringHelper.valueOf(bodyByte).trim();
        data.getLong();
    }

    private SleepNotification setNotification(int day, NotificationTime notificationTime) {
        if (day < 0 || day > 6) {
            throw new IllegalArgumentException("Invalid day passed into setNotification");
        }
        this.mSleepNotificationData[day] = notificationTime;
        return this;
    }

    public NotificationTime getMonday() {
        return this.mSleepNotificationData[0];
    }

    public NotificationTime getTuesday() {
        return this.mSleepNotificationData[1];
    }

    public NotificationTime getWednesday() {
        return this.mSleepNotificationData[2];
    }

    public NotificationTime getThursday() {
        return this.mSleepNotificationData[3];
    }

    public NotificationTime getFriday() {
        return this.mSleepNotificationData[4];
    }

    public NotificationTime getSaturday() {
        return this.mSleepNotificationData[5];
    }

    public NotificationTime getSunday() {
        return this.mSleepNotificationData[6];
    }

    public SleepNotification setMonday(NotificationTime notificationTime) {
        return setNotification(0, notificationTime);
    }

    public SleepNotification setTuesday(NotificationTime notificationTime) {
        return setNotification(1, notificationTime);
    }

    public SleepNotification setWednesday(NotificationTime notificationTime) {
        return setNotification(2, notificationTime);
    }

    public SleepNotification setThursday(NotificationTime notificationTime) {
        return setNotification(3, notificationTime);
    }

    public SleepNotification setFriday(NotificationTime notificationTime) {
        return setNotification(4, notificationTime);
    }

    public SleepNotification setSaturday(NotificationTime notificationTime) {
        return setNotification(5, notificationTime);
    }

    public SleepNotification setSunday(NotificationTime notificationTime) {
        return setNotification(6, notificationTime);
    }

    public String getHeaderText() {
        return this.mHeaderText;
    }

    public SleepNotification setHeaderText(String mHeaderText) {
        Validation.validateNullParameter(mHeaderText, "Header Text");
        Validation.validateStringEmptyOrWhiteSpace(mHeaderText, "Header Text");
        this.mHeaderText = StringUtil.truncateString(mHeaderText, 79);
        return this;
    }

    public String getBodyText() {
        return this.mBodyText;
    }

    public SleepNotification setBodyText(String mBodyText) {
        Validation.validateNullParameter(mBodyText, "Body Text");
        Validation.validateStringEmptyOrWhiteSpace(mBodyText, "Body Text");
        this.mBodyText = StringUtil.truncateString(mBodyText, 39);
        return this;
    }

    public byte[] toBytes() {
        ByteBuffer buffer = BufferUtil.allocateLittleEndian(STRUCT_SIZE);
        int startBit = 128;
        short daysEnabled = 0;
        NotificationTime[] arr$ = this.mSleepNotificationData;
        for (NotificationTime notificationTime : arr$) {
            buffer.put(notificationTime.toBytes());
            byte enabled = (byte) (notificationTime.isEnabled() ? 255 : 0);
            daysEnabled = (short) ((startBit & enabled) | daysEnabled);
            startBit >>= 1;
        }
        buffer.putShort(daysEnabled);
        int position = buffer.position();
        buffer.put(StringHelper.getBytes(this.mHeaderText));
        buffer.position(position + 160);
        int position2 = buffer.position();
        buffer.put(StringHelper.getBytes(this.mBodyText));
        buffer.position(position2 + 80);
        return buffer.array();
    }
}

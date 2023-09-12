package com.microsoft.band.device;

import com.microsoft.band.internal.util.BitHelper;
import com.microsoft.band.internal.util.BufferUtil;
import com.microsoft.band.internal.util.StringUtil;
import com.microsoft.band.internal.util.Validation;
import com.microsoft.band.util.StringHelper;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Date;
import org.apache.commons.lang3.time.DateUtils;
/* loaded from: classes.dex */
public class CalendarEvent implements Serializable {
    public static final int CALENDAR_BASIC_STRUCTURE_SIZE = 22;
    private static final short DEFAULT_NOTIFICATION_TIME = 15;
    private static final long serialVersionUID = 0;
    private CalendarEventAcceptedState mAcceptedState;
    private int mDurationInMin;
    private boolean mIsAllDay;
    private boolean mIsCancelled;
    private String mLocation;
    private int mNotificationTime;
    private Date mStartTime;
    private String mTitle;

    /* loaded from: classes.dex */
    public enum CalendarEventAcceptedState {
        ACCEPTED(0),
        TENTATIVE(1),
        FREE(2);
        
        private int mState;

        CalendarEventAcceptedState(int in) {
            this.mState = in;
        }

        public int getState() {
            return this.mState;
        }
    }

    public CalendarEvent(String title, Date startTime, Date endTime) {
        setCalendarEvent(title, startTime, endTime);
    }

    public CalendarEvent(String title, String location, Date startTime, Date endTime) {
        setCalendarEvent(title, startTime, endTime);
        setLocation(location);
    }

    public CalendarEvent(String title, String location, Date startTime, int notificationTime, int durationInMin, CalendarEventAcceptedState acceptedState, boolean isAllDay, boolean isCancelled) {
        setTitle(title);
        setLocation(location);
        setStartTime(startTime);
        setNotificationTime(notificationTime);
        setDurationInMin(durationInMin);
        setAcceptedState(acceptedState);
        setAllDay(isAllDay);
        setCancelled(isCancelled);
    }

    public CalendarEvent(byte[] data) {
        ByteBuffer buffer = ByteBuffer.wrap(data);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        int titleLength = BitHelper.unsignedShortToInteger(buffer.getShort());
        int locationLength = BitHelper.unsignedShortToInteger(buffer.getShort());
        byte[] time = new byte[8];
        buffer.get(time, 0, 8);
        this.mStartTime = FileTime.valueOf(time).toDate();
        this.mNotificationTime = BitHelper.unsignedShortToInteger(buffer.getShort());
        this.mDurationInMin = BitHelper.unsignedShortToInteger(buffer.getShort());
        this.mAcceptedState = getAcceptedStateFromDevice(buffer.getShort());
        this.mIsAllDay = buffer.get() == 1;
        this.mIsCancelled = buffer.get() == 1;
        buffer.get();
        buffer.get();
        byte[] titleByte = new byte[titleLength];
        buffer.get(titleByte);
        this.mTitle = titleByte.toString();
        if (locationLength > 0) {
            byte[] locationByte = new byte[locationLength];
            buffer.get(locationByte);
            this.mLocation = locationByte.toString();
        }
    }

    private static CalendarEventAcceptedState getAcceptedStateFromDevice(short state) {
        switch (state) {
            case 0:
                return CalendarEventAcceptedState.ACCEPTED;
            case 1:
                return CalendarEventAcceptedState.TENTATIVE;
            default:
                return CalendarEventAcceptedState.FREE;
        }
    }

    public byte[] toBytes() {
        if (this.mLocation != null && this.mLocation.length() > 0) {
            this.mTitle = StringUtil.truncateString(this.mTitle, 20);
        }
        byte[] titleBytes = StringHelper.getBytes(this.mTitle);
        int titleBytesLength = titleBytes.length;
        byte[] locationBytes = StringHelper.getBytes(this.mLocation);
        int locationBytesLength = locationBytes.length;
        ByteBuffer buffer = BufferUtil.allocateLittleEndian(titleBytesLength + 22 + locationBytesLength);
        FileTime time = new FileTime(this.mStartTime);
        buffer.putShort((short) titleBytesLength).putShort((short) locationBytesLength).put(time.toBytes()).putShort((short) this.mNotificationTime).putShort((short) this.mDurationInMin).putShort((short) this.mAcceptedState.getState()).put((byte) (this.mIsAllDay ? 1 : 0)).put((byte) (this.mIsCancelled ? 1 : 0)).put((byte) 0).put((byte) 0).put(titleBytes);
        if (locationBytesLength > 0) {
            buffer.put(locationBytes);
        }
        return buffer.array();
    }

    public String getTitle() {
        return this.mTitle;
    }

    public String getLocation() {
        return this.mLocation;
    }

    public Date getStartTime() {
        return this.mStartTime;
    }

    public int getNotificationTime() {
        return this.mNotificationTime;
    }

    public int getDurationInMin() {
        return this.mDurationInMin;
    }

    public CalendarEventAcceptedState getAcceptedState() {
        return this.mAcceptedState;
    }

    public boolean isCancelled() {
        return this.mIsCancelled;
    }

    public void setTitle(String title) {
        Validation.validateNullParameter(title, "Calendar Event Title");
        this.mTitle = StringUtil.truncateString(title, 160);
    }

    public void setLocation(String location) {
        if (location != null) {
            this.mLocation = StringUtil.truncateString(location, 160);
        }
        this.mLocation = location;
    }

    public void setStartTime(Date startTime) {
        Validation.validateNullParameter(startTime, "Calendar Event starttime");
        this.mStartTime = startTime;
    }

    public void setNotificationTime(int notificationTime) {
        Validation.validateInRange("CalendarEvent:notificationTime", notificationTime, -1, 65534);
        this.mNotificationTime = notificationTime;
    }

    public void setDurationInMin(int durationInMin) {
        this.mDurationInMin = durationInMin;
    }

    public void setAcceptedState(CalendarEventAcceptedState acceptedState) {
        Validation.validateNullParameter(acceptedState, "Calendar Event AcceptedState");
        this.mAcceptedState = acceptedState;
    }

    public void setCancelled(boolean isCancelled) {
        this.mIsCancelled = isCancelled;
    }

    public boolean isAllDay() {
        return this.mIsAllDay;
    }

    public void setAllDay(boolean isAllDay) {
        this.mIsAllDay = isAllDay;
    }

    private void setCalendarEvent(String title, Date startTime, Date endTime) {
        setTitle(title);
        this.mLocation = "";
        setStartTime(startTime);
        Validation.validateNullParameter(endTime, "Calendar Event endTime");
        if (endTime.before(startTime)) {
            throw new IllegalArgumentException("Calendar Event endTime before startTime was illegal");
        }
        this.mDurationInMin = (int) ((endTime.getTime() - startTime.getTime()) / DateUtils.MILLIS_PER_MINUTE);
        this.mNotificationTime = 15;
        this.mAcceptedState = CalendarEventAcceptedState.ACCEPTED;
        this.mIsAllDay = false;
        this.mIsCancelled = false;
    }
}

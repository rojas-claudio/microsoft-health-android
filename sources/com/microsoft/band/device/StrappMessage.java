package com.microsoft.band.device;

import com.microsoft.band.device.DeviceConstants;
import com.microsoft.band.internal.util.BufferUtil;
import com.microsoft.band.internal.util.StringUtil;
import com.microsoft.band.notifications.MessageFlags;
import com.microsoft.band.util.StringHelper;
import java.nio.ByteBuffer;
import java.util.Date;
/* loaded from: classes.dex */
public class StrappMessage {
    private static final int STRAPP_MESSAGE_BASIC_STRUCTURE_SIZE = 14;
    private String mBody;
    private DeviceConstants.NotificationFlag mNotificationFlag;
    private Date mTimestamp;
    private String mTitle;

    public StrappMessage(String title, String body, Date timestamp, DeviceConstants.NotificationFlag flag) {
        setTitleAndBody(title, body);
        this.mTimestamp = timestamp == null ? new Date() : timestamp;
        setNotificationFlag(flag);
    }

    public StrappMessage(String title, String body, Date timestamp, MessageFlags flag) {
        setTitleAndBody(title, body);
        this.mTimestamp = timestamp == null ? new Date() : timestamp;
        setNotificationFlag(flag);
    }

    public StrappMessage(String title, String body, Date timestamp) {
        this(title, body, timestamp, DeviceConstants.NotificationFlag.UNMODIFIED_SETTING);
    }

    private void setTitleAndBody(String title, String body) {
        if (title == null) {
            title = "";
        }
        if (body == null) {
            body = "";
        }
        if (StringUtil.isWhitespace(title) && StringUtil.isWhitespace(body)) {
            throw new IllegalArgumentException("Both title and body are Null or Empty");
        }
        this.mTitle = StringUtil.truncateString(title, 40);
        this.mBody = StringUtil.truncateString(body, 160);
    }

    public DeviceConstants.NotificationFlag getNotificationFlag() {
        return this.mNotificationFlag;
    }

    public String getTitle() {
        return this.mTitle;
    }

    public String getBody() {
        return this.mBody;
    }

    public Date getTimestamp() {
        return this.mTimestamp;
    }

    public StrappMessage setNotificationFlag(DeviceConstants.NotificationFlag flag) {
        if (flag == null) {
            this.mNotificationFlag = DeviceConstants.NotificationFlag.UNMODIFIED_SETTING;
        } else {
            this.mNotificationFlag = flag;
        }
        return this;
    }

    public StrappMessage setNotificationFlag(MessageFlags flag) {
        if (flag == null) {
            this.mNotificationFlag = DeviceConstants.NotificationFlag.UNMODIFIED_SETTING;
        } else {
            this.mNotificationFlag = flag == MessageFlags.SHOW_DIALOG ? DeviceConstants.NotificationFlag.FORCE_GENERIC_DIALOG : DeviceConstants.NotificationFlag.SUPPRESS_NOTIFICATION_DIALOG;
        }
        return this;
    }

    public byte[] toBytes() {
        byte[] titleBytes = StringHelper.getBytes(this.mTitle);
        byte[] bodyBytes = StringHelper.getBytes(this.mBody);
        int titleBytesLength = titleBytes.length;
        int bodyBytesLength = bodyBytes.length;
        FileTime fileTime = new FileTime(this.mTimestamp);
        ByteBuffer buffer = BufferUtil.allocateLittleEndian(titleBytesLength + 14 + bodyBytesLength);
        byte rsvd1 = this.mNotificationFlag.getFlag();
        return buffer.putShort((short) titleBytesLength).putShort((short) bodyBytesLength).put(fileTime.toBytes()).put(rsvd1).put((byte) 0).put(titleBytes).put(bodyBytes).array();
    }
}

package com.microsoft.band.device;

import com.microsoft.band.device.DeviceConstants;
import com.microsoft.band.internal.util.BufferUtil;
import com.microsoft.band.internal.util.StringUtil;
import com.microsoft.band.internal.util.Validation;
import com.microsoft.band.util.StringHelper;
import java.nio.ByteBuffer;
import java.util.Date;
/* loaded from: classes.dex */
public class CargoCall {
    public static final int CALL_BASIC_STRUCTURE_SIZE = 16;
    private DeviceConstants.NotificationFlag mCallFlag;
    private NotificationCallType mCallType;
    private int mCallerId;
    private String mCallerName;
    private Date mTimestamp;

    /* loaded from: classes.dex */
    public enum NotificationCallType {
        INCOMING(0, DeviceConstants.NotificationID.INCOMING_CALL),
        ANSWERED(1, DeviceConstants.NotificationID.ANSWERED_CALL),
        MISSED(2, DeviceConstants.NotificationID.MISSED_CALL),
        HANGUP(3, DeviceConstants.NotificationID.HANGUP_CALL),
        VOICEMAIL(4, DeviceConstants.NotificationID.VOICEMAIL);
        
        private final DeviceConstants.NotificationID mNotificationId;
        private final int mType;

        NotificationCallType(int id, DeviceConstants.NotificationID notificationId) {
            this.mType = id;
            this.mNotificationId = notificationId;
        }

        public short getType() {
            return (short) this.mType;
        }

        public DeviceConstants.NotificationID getNotificationId() {
            return this.mNotificationId;
        }

        public static NotificationCallType valueOf(int type) {
            switch (type) {
                case 0:
                    return INCOMING;
                case 1:
                    return ANSWERED;
                case 2:
                    return MISSED;
                case 3:
                    return HANGUP;
                default:
                    return VOICEMAIL;
            }
        }
    }

    public CargoCall(int callerId, String callerName, Date timestamp, NotificationCallType type, DeviceConstants.NotificationFlag flag) {
        this.mCallerId = callerId;
        setCallerName(callerName);
        setTimestamp(timestamp);
        this.mCallType = type;
        setCallFlag(flag);
    }

    public CargoCall(int callerId, String callerName, Date timestamp, NotificationCallType type) {
        this(callerId, callerName, timestamp, type, null);
    }

    public DeviceConstants.NotificationID getNotificationId() {
        return this.mCallType.getNotificationId();
    }

    public int getCallerId() {
        return this.mCallerId;
    }

    public void setCallerId(int callerId) {
        this.mCallerId = callerId;
    }

    public String getCallerName() {
        return this.mCallerName;
    }

    public void setCallerName(String callerName) {
        Validation.validateNullParameter(callerName, "Caller Name");
        this.mCallerName = StringUtil.truncateString(callerName, 40);
    }

    public Date getTimestamp() {
        return this.mTimestamp;
    }

    public void setTimestamp(Date timestamp) {
        Validation.validateNullParameter(timestamp, "Call timestamp");
        this.mTimestamp = timestamp;
    }

    public DeviceConstants.NotificationFlag getCallFlag() {
        return this.mCallFlag;
    }

    public void setCallFlag(DeviceConstants.NotificationFlag callFlag) {
        if (callFlag == null) {
            this.mCallFlag = DeviceConstants.NotificationFlag.UNMODIFIED_SETTING;
        } else {
            this.mCallFlag = callFlag;
        }
    }

    public byte[] toBytes() {
        byte[] nameBytes = StringHelper.getBytes(this.mCallerName);
        int nameBytesLength = nameBytes.length;
        byte rsvd1 = this.mCallFlag.getFlag();
        ByteBuffer buffer = BufferUtil.allocateLittleEndian(nameBytesLength + 16);
        FileTime fileTime = new FileTime(this.mTimestamp);
        return buffer.putShort((short) nameBytesLength).putInt(this.mCallerId).put(fileTime.toBytes()).put(rsvd1).put((byte) 0).put(nameBytes).array();
    }
}

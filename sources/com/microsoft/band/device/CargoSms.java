package com.microsoft.band.device;

import com.microsoft.band.device.DeviceConstants;
import com.microsoft.band.internal.util.BufferUtil;
import com.microsoft.band.internal.util.StringUtil;
import com.microsoft.band.internal.util.Validation;
import com.microsoft.band.util.StringHelper;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
/* loaded from: classes.dex */
public class CargoSms {
    public static final int SMS_BASIC_STRUCTURE_SIZE = 21;
    private String mBody;
    private NotificationMmsType mMmsType;
    private List<String> mParticipants;
    private String mSender;
    private DeviceConstants.NotificationFlag mSmsFlag;
    private int mThreadID;
    private Date mTimestamp;

    /* loaded from: classes.dex */
    public enum NotificationMmsType {
        NONE(0),
        IMAGE(1),
        VIDEO(2),
        UNKNOWN(4);
        
        private int mType;

        NotificationMmsType(int id) {
            this.mType = id;
        }

        public byte getType() {
            return (byte) this.mType;
        }

        public static NotificationMmsType valueOf(int type) {
            switch (type) {
                case 0:
                    return NONE;
                case 1:
                    return IMAGE;
                case 2:
                    return VIDEO;
                default:
                    return UNKNOWN;
            }
        }
    }

    public CargoSms(String name, String body, Date time, int threadID, DeviceConstants.NotificationFlag flag) {
        setSender(name);
        setBody(body);
        setTime(time);
        setThreadID(threadID);
        this.mParticipants = new ArrayList();
        this.mMmsType = NotificationMmsType.NONE;
        setSmsFlag(flag);
    }

    public CargoSms(String name, String body, Date time, int threadID) {
        this(name, body, time, threadID, null);
    }

    public String getSender() {
        return this.mSender;
    }

    public void setSender(String name) {
        Validation.validateNullParameter(name, "SMS Sender Name");
        this.mSender = StringUtil.truncateString(name, 40);
    }

    public String getBody() {
        return this.mBody;
    }

    public void setBody(String body) {
        Validation.validateNullParameter(body, "SMS Message Body");
        this.mBody = StringUtil.truncateString(body, 160);
    }

    public Date getTime() {
        return this.mTimestamp;
    }

    public void setTime(Date time) {
        Validation.validateNullParameter(time, "SMS send time");
        this.mTimestamp = time;
    }

    public List<String> getParticipants() {
        return this.mParticipants;
    }

    public void setParticipants(List<String> participants) {
        this.mParticipants = participants;
    }

    public NotificationMmsType getMultimedia() {
        return this.mMmsType;
    }

    public void setMultimedia(NotificationMmsType type) {
        this.mMmsType = type;
    }

    public int getThreadID() {
        return this.mThreadID;
    }

    public void setThreadID(int threadID) {
        this.mThreadID = threadID;
    }

    public DeviceConstants.NotificationFlag getSmsFlag() {
        return this.mSmsFlag;
    }

    public void setSmsFlag(DeviceConstants.NotificationFlag smsFlag) {
        if (smsFlag == null) {
            this.mSmsFlag = DeviceConstants.NotificationFlag.UNMODIFIED_SETTING;
        } else {
            this.mSmsFlag = smsFlag;
        }
    }

    public byte[] toBytes() {
        int participantCount = 2;
        StringBuilder nameToSend = new StringBuilder();
        nameToSend.append(this.mSender);
        if (this.mParticipants != null) {
            participantCount = 2 + this.mParticipants.size();
            int i = 0;
            while (true) {
                if (i >= this.mParticipants.size()) {
                    break;
                }
                nameToSend.append(",");
                if (this.mParticipants.get(i).length() + nameToSend.length() <= 40) {
                    nameToSend.append(this.mParticipants.get(i));
                    i++;
                } else {
                    int remaining = 40 - nameToSend.length();
                    nameToSend.append(this.mParticipants.get(i).substring(0, remaining));
                    break;
                }
            }
        }
        byte rsvd1 = this.mSmsFlag.getFlag();
        byte[] nameBytes = StringHelper.getBytes(nameToSend.toString());
        byte[] bodyBytes = StringHelper.getBytes(this.mBody);
        int nameBytesLength = nameBytes.length;
        int bodyBytesLength = bodyBytes.length;
        ByteBuffer buffer = BufferUtil.allocateLittleEndian(nameBytesLength + 21 + bodyBytesLength);
        FileTime fileTime = new FileTime(this.mTimestamp);
        return buffer.putShort((short) nameBytesLength).putShort((short) bodyBytesLength).putInt(this.mThreadID).putShort((short) participantCount).put(fileTime.toBytes()).put(this.mMmsType.getType()).put(rsvd1).put((byte) 0).put(nameBytes).put(bodyBytes).array();
    }
}

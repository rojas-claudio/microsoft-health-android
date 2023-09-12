package com.microsoft.band.device.subscription;

import android.os.Parcel;
import android.os.Parcelable;
import com.microsoft.band.internal.device.subscription.SubscriptionDataModel;
import com.microsoft.band.internal.util.BitHelper;
import com.microsoft.band.internal.util.BufferUtil;
import java.nio.ByteBuffer;
import java.util.UUID;
/* loaded from: classes.dex */
public class TextMessageData extends SubscriptionDataModel {
    public static final Parcelable.Creator<TextMessageData> CREATOR = new Parcelable.Creator<TextMessageData>() { // from class: com.microsoft.band.device.subscription.TextMessageData.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public TextMessageData createFromParcel(Parcel in) {
            return new TextMessageData(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public TextMessageData[] newArray(int size) {
            return new TextMessageData[size];
        }
    };
    private static final int LONG_STRING_LENGTH = 161;
    private int mLength;
    private String mMessage;
    private int mMessageLength;
    private int mSubType;
    private int mThreadId;
    private UUID mUUID;

    protected TextMessageData(Parcel in) {
        super(in);
        long uuidMostSigBits = in.readLong();
        long uuidLeastSigBits = in.readLong();
        this.mUUID = new UUID(uuidMostSigBits, uuidLeastSigBits);
        this.mSubType = in.readInt();
        this.mLength = in.readInt();
        this.mThreadId = in.readInt();
        this.mMessageLength = in.readInt();
        this.mMessage = in.readString();
    }

    public TextMessageData(ByteBuffer buffer) {
        super(buffer);
        this.mUUID = BufferUtil.getUUID(buffer);
        this.mSubType = BitHelper.unsignedShortToInteger(buffer.getShort());
        this.mLength = BitHelper.unsignedShortToInteger(buffer.getShort());
        this.mThreadId = buffer.getInt();
        this.mMessageLength = BitHelper.unsignedShortToInteger(buffer.getShort());
        this.mMessage = BufferUtil.getString(buffer, LONG_STRING_LENGTH);
    }

    public UUID getStrappUUID() {
        return this.mUUID;
    }

    public int getSubType() {
        return this.mSubType;
    }

    public int getLength() {
        return this.mLength;
    }

    public int getThreadID() {
        return this.mThreadId;
    }

    public String getMessage() {
        return this.mMessage;
    }

    public void setThreadID(int newVal) {
        this.mThreadId = newVal;
    }

    @Override // com.microsoft.band.internal.device.subscription.SubscriptionDataModel
    protected void format(StringBuffer sb) {
        sb.append(String.format("     |--UUID = %s\n", this.mUUID)).append(String.format("     |--Subtype = %d\n", Integer.valueOf(this.mSubType))).append(String.format("     |--Length = %d\n", Integer.valueOf(this.mLength))).append(String.format("     |--Thread ID = %d\n", Integer.valueOf(this.mThreadId))).append(String.format("     |--Message = %s\n", this.mMessage));
    }

    @Override // com.microsoft.band.internal.device.subscription.SubscriptionDataModel, com.microsoft.band.internal.device.DeviceDataModel, android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        if (this.mUUID == null) {
            dest.writeLong(0L);
            dest.writeLong(0L);
        } else {
            dest.writeLong(this.mUUID.getMostSignificantBits());
            dest.writeLong(this.mUUID.getLeastSignificantBits());
        }
        dest.writeInt(this.mSubType);
        dest.writeInt(this.mLength);
        dest.writeInt(this.mThreadId);
        dest.writeInt(this.mMessageLength);
        dest.writeString(this.mMessage);
    }
}

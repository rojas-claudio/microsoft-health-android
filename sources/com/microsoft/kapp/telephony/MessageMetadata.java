package com.microsoft.kapp.telephony;

import android.os.Parcel;
import android.os.Parcelable;
import com.microsoft.kapp.diagnostics.Validate;
/* loaded from: classes.dex */
public class MessageMetadata implements Parcelable {
    public static final Parcelable.Creator<MessageMetadata> CREATOR = new Parcelable.Creator<MessageMetadata>() { // from class: com.microsoft.kapp.telephony.MessageMetadata.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public MessageMetadata createFromParcel(Parcel in) {
            return new MessageMetadata(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public MessageMetadata[] newArray(int size) {
            return new MessageMetadata[size];
        }
    };
    private int mId;
    private MessageState mState;
    private MessageType mType;

    public MessageMetadata(int id, MessageType type, MessageState state) {
        Validate.isTrue(id > 0, "id must be greater than 0");
        this.mId = id;
        this.mType = type;
        this.mState = state;
    }

    private MessageMetadata(Parcel in) {
        Validate.notNull(in, "in");
        this.mId = in.readInt();
        this.mType = (MessageType) Enum.valueOf(MessageType.class, in.readString());
        this.mState = (MessageState) Enum.valueOf(MessageState.class, in.readString());
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        Validate.notNull(dest, "dest");
        dest.writeInt(this.mId);
        dest.writeString(this.mType.toString());
        dest.writeString(this.mState.toString());
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public int getId() {
        return this.mId;
    }

    public MessageType getMessageType() {
        return this.mType;
    }

    public MessageState getMessageState() {
        return this.mState;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Id: ").append(getId()).append(" Type: ").append(getMessageType()).append(" State: ").append(getMessageState());
        return sb.toString();
    }
}

package com.google.android.gms.games.multiplayer.realtime;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.internal.dm;
/* loaded from: classes.dex */
public final class RealTimeMessage implements Parcelable {
    public static final Parcelable.Creator<RealTimeMessage> CREATOR = new Parcelable.Creator<RealTimeMessage>() { // from class: com.google.android.gms.games.multiplayer.realtime.RealTimeMessage.1
        @Override // android.os.Parcelable.Creator
        /* renamed from: U */
        public RealTimeMessage[] newArray(int i) {
            return new RealTimeMessage[i];
        }

        @Override // android.os.Parcelable.Creator
        /* renamed from: x */
        public RealTimeMessage createFromParcel(Parcel parcel) {
            return new RealTimeMessage(parcel);
        }
    };
    public static final int RELIABLE = 1;
    public static final int UNRELIABLE = 0;
    private final String oa;
    private final byte[] ob;
    private final int oc;

    private RealTimeMessage(Parcel parcel) {
        this(parcel.readString(), parcel.createByteArray(), parcel.readInt());
    }

    public RealTimeMessage(String senderParticipantId, byte[] messageData, int isReliable) {
        this.oa = (String) dm.e(senderParticipantId);
        this.ob = (byte[]) ((byte[]) dm.e(messageData)).clone();
        this.oc = isReliable;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public byte[] getMessageData() {
        return this.ob;
    }

    public String getSenderParticipantId() {
        return this.oa;
    }

    public boolean isReliable() {
        return this.oc == 1;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int flag) {
        parcel.writeString(this.oa);
        parcel.writeByteArray(this.ob);
        parcel.writeInt(this.oc);
    }
}

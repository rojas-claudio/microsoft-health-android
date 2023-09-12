package com.google.android.gms.games.multiplayer.realtime;

import android.database.CharArrayBuffer;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.games.Player;
import com.google.android.gms.games.multiplayer.Participant;
import com.google.android.gms.games.multiplayer.ParticipantEntity;
import com.google.android.gms.internal.dl;
import com.google.android.gms.internal.eg;
import com.google.android.gms.internal.en;
import com.microsoft.kapp.diagnostics.TelemetryConstants;
import java.util.ArrayList;
/* loaded from: classes.dex */
public final class RoomEntity extends en implements Room {
    public static final Parcelable.Creator<RoomEntity> CREATOR = new a();
    private final int iM;
    private final String mo;
    private final long nN;
    private final ArrayList<ParticipantEntity> nQ;
    private final int nR;
    private final String nb;
    private final Bundle oh;
    private final String ol;
    private final int om;
    private final int on;

    /* loaded from: classes.dex */
    static final class a extends b {
        a() {
        }

        @Override // com.google.android.gms.games.multiplayer.realtime.b, android.os.Parcelable.Creator
        /* renamed from: y */
        public RoomEntity createFromParcel(Parcel parcel) {
            if (RoomEntity.c(RoomEntity.bQ()) || RoomEntity.y(RoomEntity.class.getCanonicalName())) {
                return super.createFromParcel(parcel);
            }
            String readString = parcel.readString();
            String readString2 = parcel.readString();
            long readLong = parcel.readLong();
            int readInt = parcel.readInt();
            String readString3 = parcel.readString();
            int readInt2 = parcel.readInt();
            Bundle readBundle = parcel.readBundle();
            int readInt3 = parcel.readInt();
            ArrayList arrayList = new ArrayList(readInt3);
            for (int i = 0; i < readInt3; i++) {
                arrayList.add(ParticipantEntity.CREATOR.createFromParcel(parcel));
            }
            return new RoomEntity(2, readString, readString2, readLong, readInt, readString3, readInt2, readBundle, arrayList, -1);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public RoomEntity(int versionCode, String roomId, String creatorId, long creationTimestamp, int roomStatus, String description, int variant, Bundle autoMatchCriteria, ArrayList<ParticipantEntity> participants, int autoMatchWaitEstimateSeconds) {
        this.iM = versionCode;
        this.nb = roomId;
        this.ol = creatorId;
        this.nN = creationTimestamp;
        this.om = roomStatus;
        this.mo = description;
        this.nR = variant;
        this.oh = autoMatchCriteria;
        this.nQ = participants;
        this.on = autoMatchWaitEstimateSeconds;
    }

    public RoomEntity(Room room) {
        this.iM = 2;
        this.nb = room.getRoomId();
        this.ol = room.getCreatorId();
        this.nN = room.getCreationTimestamp();
        this.om = room.getStatus();
        this.mo = room.getDescription();
        this.nR = room.getVariant();
        this.oh = room.getAutoMatchCriteria();
        ArrayList<Participant> participants = room.getParticipants();
        int size = participants.size();
        this.nQ = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            this.nQ.add((ParticipantEntity) participants.get(i).freeze());
        }
        this.on = room.getAutoMatchWaitEstimateSeconds();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int a(Room room) {
        return dl.hashCode(room.getRoomId(), room.getCreatorId(), Long.valueOf(room.getCreationTimestamp()), Integer.valueOf(room.getStatus()), room.getDescription(), Integer.valueOf(room.getVariant()), room.getAutoMatchCriteria(), room.getParticipants(), Integer.valueOf(room.getAutoMatchWaitEstimateSeconds()));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean a(Room room, Object obj) {
        if (obj instanceof Room) {
            if (room != obj) {
                Room room2 = (Room) obj;
                return dl.equal(room2.getRoomId(), room.getRoomId()) && dl.equal(room2.getCreatorId(), room.getCreatorId()) && dl.equal(Long.valueOf(room2.getCreationTimestamp()), Long.valueOf(room.getCreationTimestamp())) && dl.equal(Integer.valueOf(room2.getStatus()), Integer.valueOf(room.getStatus())) && dl.equal(room2.getDescription(), room.getDescription()) && dl.equal(Integer.valueOf(room2.getVariant()), Integer.valueOf(room.getVariant())) && dl.equal(room2.getAutoMatchCriteria(), room.getAutoMatchCriteria()) && dl.equal(room2.getParticipants(), room.getParticipants()) && dl.equal(Integer.valueOf(room2.getAutoMatchWaitEstimateSeconds()), Integer.valueOf(room.getAutoMatchWaitEstimateSeconds()));
            }
            return true;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String b(Room room) {
        return dl.d(room).a("RoomId", room.getRoomId()).a("CreatorId", room.getCreatorId()).a("CreationTimestamp", Long.valueOf(room.getCreationTimestamp())).a("RoomStatus", Integer.valueOf(room.getStatus())).a(TelemetryConstants.Events.SendFeedbackComplete.Dimensions.DESCRIPTION, room.getDescription()).a("Variant", Integer.valueOf(room.getVariant())).a("AutoMatchCriteria", room.getAutoMatchCriteria()).a("Participants", room.getParticipants()).a("AutoMatchWaitEstimateSeconds", Integer.valueOf(room.getAutoMatchWaitEstimateSeconds())).toString();
    }

    static /* synthetic */ Integer bQ() {
        return aW();
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public boolean equals(Object obj) {
        return a(this, obj);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.google.android.gms.common.data.Freezable
    public Room freeze() {
        return this;
    }

    @Override // com.google.android.gms.games.multiplayer.realtime.Room
    public Bundle getAutoMatchCriteria() {
        return this.oh;
    }

    @Override // com.google.android.gms.games.multiplayer.realtime.Room
    public int getAutoMatchWaitEstimateSeconds() {
        return this.on;
    }

    @Override // com.google.android.gms.games.multiplayer.realtime.Room
    public long getCreationTimestamp() {
        return this.nN;
    }

    @Override // com.google.android.gms.games.multiplayer.realtime.Room
    public String getCreatorId() {
        return this.ol;
    }

    @Override // com.google.android.gms.games.multiplayer.realtime.Room
    public String getDescription() {
        return this.mo;
    }

    @Override // com.google.android.gms.games.multiplayer.realtime.Room
    public void getDescription(CharArrayBuffer dataOut) {
        eg.b(this.mo, dataOut);
    }

    @Override // com.google.android.gms.games.multiplayer.realtime.Room
    public String getParticipantId(String playerId) {
        int size = this.nQ.size();
        for (int i = 0; i < size; i++) {
            ParticipantEntity participantEntity = this.nQ.get(i);
            Player player = participantEntity.getPlayer();
            if (player != null && player.getPlayerId().equals(playerId)) {
                return participantEntity.getParticipantId();
            }
        }
        return null;
    }

    @Override // com.google.android.gms.games.multiplayer.realtime.Room
    public ArrayList<String> getParticipantIds() {
        int size = this.nQ.size();
        ArrayList<String> arrayList = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            arrayList.add(this.nQ.get(i).getParticipantId());
        }
        return arrayList;
    }

    @Override // com.google.android.gms.games.multiplayer.realtime.Room
    public int getParticipantStatus(String participantId) {
        int size = this.nQ.size();
        for (int i = 0; i < size; i++) {
            ParticipantEntity participantEntity = this.nQ.get(i);
            if (participantEntity.getParticipantId().equals(participantId)) {
                return participantEntity.getStatus();
            }
        }
        throw new IllegalStateException("Participant " + participantId + " is not in room " + getRoomId());
    }

    @Override // com.google.android.gms.games.multiplayer.Participatable
    public ArrayList<Participant> getParticipants() {
        return new ArrayList<>(this.nQ);
    }

    @Override // com.google.android.gms.games.multiplayer.realtime.Room
    public String getRoomId() {
        return this.nb;
    }

    @Override // com.google.android.gms.games.multiplayer.realtime.Room
    public int getStatus() {
        return this.om;
    }

    @Override // com.google.android.gms.games.multiplayer.realtime.Room
    public int getVariant() {
        return this.nR;
    }

    public int getVersionCode() {
        return this.iM;
    }

    public int hashCode() {
        return a(this);
    }

    @Override // com.google.android.gms.common.data.Freezable
    public boolean isDataValid() {
        return true;
    }

    public String toString() {
        return b(this);
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        if (!aX()) {
            b.a(this, dest, flags);
            return;
        }
        dest.writeString(this.nb);
        dest.writeString(this.ol);
        dest.writeLong(this.nN);
        dest.writeInt(this.om);
        dest.writeString(this.mo);
        dest.writeInt(this.nR);
        dest.writeBundle(this.oh);
        int size = this.nQ.size();
        dest.writeInt(size);
        for (int i = 0; i < size; i++) {
            this.nQ.get(i).writeToParcel(dest, flags);
        }
    }
}

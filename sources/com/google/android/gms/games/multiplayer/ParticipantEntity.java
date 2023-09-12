package com.google.android.gms.games.multiplayer;

import android.database.CharArrayBuffer;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.games.Player;
import com.google.android.gms.games.PlayerEntity;
import com.google.android.gms.internal.dl;
import com.google.android.gms.internal.eg;
import com.google.android.gms.internal.en;
/* loaded from: classes.dex */
public final class ParticipantEntity extends en implements Participant {
    public static final Parcelable.Creator<ParticipantEntity> CREATOR = new a();
    private final int iM;
    private final String ml;
    private final Uri mq;
    private final Uri mr;
    private final int nU;
    private final String nV;
    private final boolean nW;
    private final PlayerEntity nX;
    private final int nY;
    private final String nd;

    /* loaded from: classes.dex */
    static final class a extends c {
        a() {
        }

        @Override // com.google.android.gms.games.multiplayer.c, android.os.Parcelable.Creator
        /* renamed from: w */
        public ParticipantEntity createFromParcel(Parcel parcel) {
            if (ParticipantEntity.c(ParticipantEntity.bQ()) || ParticipantEntity.y(ParticipantEntity.class.getCanonicalName())) {
                return super.createFromParcel(parcel);
            }
            String readString = parcel.readString();
            String readString2 = parcel.readString();
            String readString3 = parcel.readString();
            Uri parse = readString3 == null ? null : Uri.parse(readString3);
            String readString4 = parcel.readString();
            return new ParticipantEntity(1, readString, readString2, parse, readString4 == null ? null : Uri.parse(readString4), parcel.readInt(), parcel.readString(), parcel.readInt() > 0, parcel.readInt() > 0 ? PlayerEntity.CREATOR.createFromParcel(parcel) : null, 7);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ParticipantEntity(int versionCode, String participantId, String displayName, Uri iconImageUri, Uri hiResImageUri, int status, String clientAddress, boolean connectedToRoom, PlayerEntity player, int capabilities) {
        this.iM = versionCode;
        this.nd = participantId;
        this.ml = displayName;
        this.mq = iconImageUri;
        this.mr = hiResImageUri;
        this.nU = status;
        this.nV = clientAddress;
        this.nW = connectedToRoom;
        this.nX = player;
        this.nY = capabilities;
    }

    public ParticipantEntity(Participant participant) {
        this.iM = 1;
        this.nd = participant.getParticipantId();
        this.ml = participant.getDisplayName();
        this.mq = participant.getIconImageUri();
        this.mr = participant.getHiResImageUri();
        this.nU = participant.getStatus();
        this.nV = participant.ci();
        this.nW = participant.isConnectedToRoom();
        Player player = participant.getPlayer();
        this.nX = player == null ? null : new PlayerEntity(player);
        this.nY = participant.getCapabilities();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int a(Participant participant) {
        return dl.hashCode(participant.getPlayer(), Integer.valueOf(participant.getStatus()), participant.ci(), Boolean.valueOf(participant.isConnectedToRoom()), participant.getDisplayName(), participant.getIconImageUri(), participant.getHiResImageUri(), Integer.valueOf(participant.getCapabilities()));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean a(Participant participant, Object obj) {
        if (obj instanceof Participant) {
            if (participant != obj) {
                Participant participant2 = (Participant) obj;
                return dl.equal(participant2.getPlayer(), participant.getPlayer()) && dl.equal(Integer.valueOf(participant2.getStatus()), Integer.valueOf(participant.getStatus())) && dl.equal(participant2.ci(), participant.ci()) && dl.equal(Boolean.valueOf(participant2.isConnectedToRoom()), Boolean.valueOf(participant.isConnectedToRoom())) && dl.equal(participant2.getDisplayName(), participant.getDisplayName()) && dl.equal(participant2.getIconImageUri(), participant.getIconImageUri()) && dl.equal(participant2.getHiResImageUri(), participant.getHiResImageUri()) && dl.equal(Integer.valueOf(participant2.getCapabilities()), Integer.valueOf(participant.getCapabilities()));
            }
            return true;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String b(Participant participant) {
        return dl.d(participant).a("Player", participant.getPlayer()).a("Status", Integer.valueOf(participant.getStatus())).a("ClientAddress", participant.ci()).a("ConnectedToRoom", Boolean.valueOf(participant.isConnectedToRoom())).a("DisplayName", participant.getDisplayName()).a("IconImage", participant.getIconImageUri()).a("HiResImage", participant.getHiResImageUri()).a("Capabilities", Integer.valueOf(participant.getCapabilities())).toString();
    }

    static /* synthetic */ Integer bQ() {
        return aW();
    }

    @Override // com.google.android.gms.games.multiplayer.Participant
    public String ci() {
        return this.nV;
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
    public Participant freeze() {
        return this;
    }

    @Override // com.google.android.gms.games.multiplayer.Participant
    public int getCapabilities() {
        return this.nY;
    }

    @Override // com.google.android.gms.games.multiplayer.Participant
    public String getDisplayName() {
        return this.nX == null ? this.ml : this.nX.getDisplayName();
    }

    @Override // com.google.android.gms.games.multiplayer.Participant
    public void getDisplayName(CharArrayBuffer dataOut) {
        if (this.nX == null) {
            eg.b(this.ml, dataOut);
        } else {
            this.nX.getDisplayName(dataOut);
        }
    }

    @Override // com.google.android.gms.games.multiplayer.Participant
    public Uri getHiResImageUri() {
        return this.nX == null ? this.mr : this.nX.getHiResImageUri();
    }

    @Override // com.google.android.gms.games.multiplayer.Participant
    public Uri getIconImageUri() {
        return this.nX == null ? this.mq : this.nX.getIconImageUri();
    }

    @Override // com.google.android.gms.games.multiplayer.Participant
    public String getParticipantId() {
        return this.nd;
    }

    @Override // com.google.android.gms.games.multiplayer.Participant
    public Player getPlayer() {
        return this.nX;
    }

    @Override // com.google.android.gms.games.multiplayer.Participant
    public int getStatus() {
        return this.nU;
    }

    public int getVersionCode() {
        return this.iM;
    }

    public int hashCode() {
        return a(this);
    }

    @Override // com.google.android.gms.games.multiplayer.Participant
    public boolean isConnectedToRoom() {
        return this.nW;
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
            c.a(this, dest, flags);
            return;
        }
        dest.writeString(this.nd);
        dest.writeString(this.ml);
        dest.writeString(this.mq == null ? null : this.mq.toString());
        dest.writeString(this.mr != null ? this.mr.toString() : null);
        dest.writeInt(this.nU);
        dest.writeString(this.nV);
        dest.writeInt(this.nW ? 1 : 0);
        dest.writeInt(this.nX != null ? 1 : 0);
        if (this.nX != null) {
            this.nX.writeToParcel(dest, flags);
        }
    }
}

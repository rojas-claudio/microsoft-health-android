package com.google.android.gms.games.multiplayer;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.games.Game;
import com.google.android.gms.games.GameEntity;
import com.google.android.gms.internal.dl;
import com.google.android.gms.internal.dm;
import com.google.android.gms.internal.en;
import java.util.ArrayList;
/* loaded from: classes.dex */
public final class InvitationEntity extends en implements Invitation {
    public static final Parcelable.Creator<InvitationEntity> CREATOR = new a();
    private final int iM;
    private final GameEntity nL;
    private final String nM;
    private final long nN;
    private final int nO;
    private final ParticipantEntity nP;
    private final ArrayList<ParticipantEntity> nQ;
    private final int nR;

    /* loaded from: classes.dex */
    static final class a extends com.google.android.gms.games.multiplayer.a {
        a() {
        }

        @Override // com.google.android.gms.games.multiplayer.a, android.os.Parcelable.Creator
        /* renamed from: v */
        public InvitationEntity createFromParcel(Parcel parcel) {
            if (InvitationEntity.c(InvitationEntity.bQ()) || InvitationEntity.y(InvitationEntity.class.getCanonicalName())) {
                return super.createFromParcel(parcel);
            }
            GameEntity createFromParcel = GameEntity.CREATOR.createFromParcel(parcel);
            String readString = parcel.readString();
            long readLong = parcel.readLong();
            int readInt = parcel.readInt();
            ParticipantEntity createFromParcel2 = ParticipantEntity.CREATOR.createFromParcel(parcel);
            int readInt2 = parcel.readInt();
            ArrayList arrayList = new ArrayList(readInt2);
            for (int i = 0; i < readInt2; i++) {
                arrayList.add(ParticipantEntity.CREATOR.createFromParcel(parcel));
            }
            return new InvitationEntity(1, createFromParcel, readString, readLong, readInt, createFromParcel2, arrayList, -1);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public InvitationEntity(int versionCode, GameEntity game, String invitationId, long creationTimestamp, int invitationType, ParticipantEntity inviter, ArrayList<ParticipantEntity> participants, int variant) {
        this.iM = versionCode;
        this.nL = game;
        this.nM = invitationId;
        this.nN = creationTimestamp;
        this.nO = invitationType;
        this.nP = inviter;
        this.nQ = participants;
        this.nR = variant;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public InvitationEntity(Invitation invitation) {
        this.iM = 1;
        this.nL = new GameEntity(invitation.getGame());
        this.nM = invitation.getInvitationId();
        this.nN = invitation.getCreationTimestamp();
        this.nO = invitation.ch();
        this.nR = invitation.getVariant();
        String participantId = invitation.getInviter().getParticipantId();
        Participant participant = null;
        ArrayList<Participant> participants = invitation.getParticipants();
        int size = participants.size();
        this.nQ = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            Participant participant2 = participants.get(i);
            if (participant2.getParticipantId().equals(participantId)) {
                participant = participant2;
            }
            this.nQ.add((ParticipantEntity) participant2.freeze());
        }
        dm.a(participant, "Must have a valid inviter!");
        this.nP = (ParticipantEntity) participant.freeze();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int a(Invitation invitation) {
        return dl.hashCode(invitation.getGame(), invitation.getInvitationId(), Long.valueOf(invitation.getCreationTimestamp()), Integer.valueOf(invitation.ch()), invitation.getInviter(), invitation.getParticipants(), Integer.valueOf(invitation.getVariant()));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean a(Invitation invitation, Object obj) {
        if (obj instanceof Invitation) {
            if (invitation != obj) {
                Invitation invitation2 = (Invitation) obj;
                return dl.equal(invitation2.getGame(), invitation.getGame()) && dl.equal(invitation2.getInvitationId(), invitation.getInvitationId()) && dl.equal(Long.valueOf(invitation2.getCreationTimestamp()), Long.valueOf(invitation.getCreationTimestamp())) && dl.equal(Integer.valueOf(invitation2.ch()), Integer.valueOf(invitation.ch())) && dl.equal(invitation2.getInviter(), invitation.getInviter()) && dl.equal(invitation2.getParticipants(), invitation.getParticipants()) && dl.equal(Integer.valueOf(invitation2.getVariant()), Integer.valueOf(invitation.getVariant()));
            }
            return true;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String b(Invitation invitation) {
        return dl.d(invitation).a("Game", invitation.getGame()).a("InvitationId", invitation.getInvitationId()).a("CreationTimestamp", Long.valueOf(invitation.getCreationTimestamp())).a("InvitationType", Integer.valueOf(invitation.ch())).a("Inviter", invitation.getInviter()).a("Participants", invitation.getParticipants()).a("Variant", Integer.valueOf(invitation.getVariant())).toString();
    }

    static /* synthetic */ Integer bQ() {
        return aW();
    }

    @Override // com.google.android.gms.games.multiplayer.Invitation
    public int ch() {
        return this.nO;
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
    public Invitation freeze() {
        return this;
    }

    @Override // com.google.android.gms.games.multiplayer.Invitation
    public long getCreationTimestamp() {
        return this.nN;
    }

    @Override // com.google.android.gms.games.multiplayer.Invitation
    public Game getGame() {
        return this.nL;
    }

    @Override // com.google.android.gms.games.multiplayer.Invitation
    public String getInvitationId() {
        return this.nM;
    }

    @Override // com.google.android.gms.games.multiplayer.Invitation
    public Participant getInviter() {
        return this.nP;
    }

    @Override // com.google.android.gms.games.multiplayer.Participatable
    public ArrayList<Participant> getParticipants() {
        return new ArrayList<>(this.nQ);
    }

    @Override // com.google.android.gms.games.multiplayer.Invitation
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
            com.google.android.gms.games.multiplayer.a.a(this, dest, flags);
            return;
        }
        this.nL.writeToParcel(dest, flags);
        dest.writeString(this.nM);
        dest.writeLong(this.nN);
        dest.writeInt(this.nO);
        this.nP.writeToParcel(dest, flags);
        int size = this.nQ.size();
        dest.writeInt(size);
        for (int i = 0; i < size; i++) {
            this.nQ.get(i).writeToParcel(dest, flags);
        }
    }
}

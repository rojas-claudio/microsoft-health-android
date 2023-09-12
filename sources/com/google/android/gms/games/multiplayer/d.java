package com.google.android.gms.games.multiplayer;

import android.database.CharArrayBuffer;
import android.net.Uri;
import android.os.Parcel;
import com.google.android.gms.games.Player;
/* loaded from: classes.dex */
public final class d extends com.google.android.gms.common.data.b implements Participant {
    private final com.google.android.gms.games.d nZ;

    public d(com.google.android.gms.common.data.d dVar, int i) {
        super(dVar, i);
        this.nZ = new com.google.android.gms.games.d(dVar, i);
    }

    @Override // com.google.android.gms.games.multiplayer.Participant
    public String ci() {
        return getString("client_address");
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // com.google.android.gms.common.data.b
    public boolean equals(Object obj) {
        return ParticipantEntity.a(this, obj);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.google.android.gms.common.data.Freezable
    public Participant freeze() {
        return new ParticipantEntity(this);
    }

    @Override // com.google.android.gms.games.multiplayer.Participant
    public int getCapabilities() {
        return getInteger("capabilities");
    }

    @Override // com.google.android.gms.games.multiplayer.Participant
    public String getDisplayName() {
        return v("external_player_id") ? getString("default_display_name") : this.nZ.getDisplayName();
    }

    @Override // com.google.android.gms.games.multiplayer.Participant
    public void getDisplayName(CharArrayBuffer dataOut) {
        if (v("external_player_id")) {
            a("default_display_name", dataOut);
        } else {
            this.nZ.getDisplayName(dataOut);
        }
    }

    @Override // com.google.android.gms.games.multiplayer.Participant
    public Uri getHiResImageUri() {
        if (v("external_player_id")) {
            return null;
        }
        return this.nZ.getHiResImageUri();
    }

    @Override // com.google.android.gms.games.multiplayer.Participant
    public Uri getIconImageUri() {
        return v("external_player_id") ? u("default_display_image_uri") : this.nZ.getIconImageUri();
    }

    @Override // com.google.android.gms.games.multiplayer.Participant
    public String getParticipantId() {
        return getString("external_participant_id");
    }

    @Override // com.google.android.gms.games.multiplayer.Participant
    public Player getPlayer() {
        if (v("external_player_id")) {
            return null;
        }
        return this.nZ;
    }

    @Override // com.google.android.gms.games.multiplayer.Participant
    public int getStatus() {
        return getInteger("player_status");
    }

    @Override // com.google.android.gms.common.data.b
    public int hashCode() {
        return ParticipantEntity.a(this);
    }

    @Override // com.google.android.gms.games.multiplayer.Participant
    public boolean isConnectedToRoom() {
        return getInteger("connected") > 0;
    }

    public String toString() {
        return ParticipantEntity.b(this);
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        ((ParticipantEntity) freeze()).writeToParcel(dest, flags);
    }
}

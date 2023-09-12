package com.google.android.gms.games;

import android.database.CharArrayBuffer;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.internal.db;
import com.google.android.gms.internal.dl;
import com.google.android.gms.internal.eg;
import com.google.android.gms.internal.en;
/* loaded from: classes.dex */
public final class PlayerEntity extends en implements Player {
    public static final Parcelable.Creator<PlayerEntity> CREATOR = new a();
    private final int iM;
    private final String mD;
    private final long mE;
    private final String ml;
    private final Uri mq;
    private final Uri mr;

    /* loaded from: classes.dex */
    static final class a extends c {
        a() {
        }

        @Override // com.google.android.gms.games.c, android.os.Parcelable.Creator
        /* renamed from: u */
        public PlayerEntity createFromParcel(Parcel parcel) {
            if (PlayerEntity.c(PlayerEntity.bQ()) || PlayerEntity.y(PlayerEntity.class.getCanonicalName())) {
                return super.createFromParcel(parcel);
            }
            String readString = parcel.readString();
            String readString2 = parcel.readString();
            String readString3 = parcel.readString();
            String readString4 = parcel.readString();
            return new PlayerEntity(1, readString, readString2, readString3 == null ? null : Uri.parse(readString3), readString4 != null ? Uri.parse(readString4) : null, parcel.readLong());
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public PlayerEntity(int versionCode, String playerId, String displayName, Uri iconImageUri, Uri hiResImageUri, long retrievedTimestamp) {
        this.iM = versionCode;
        this.mD = playerId;
        this.ml = displayName;
        this.mq = iconImageUri;
        this.mr = hiResImageUri;
        this.mE = retrievedTimestamp;
    }

    public PlayerEntity(Player player) {
        this.iM = 1;
        this.mD = player.getPlayerId();
        this.ml = player.getDisplayName();
        this.mq = player.getIconImageUri();
        this.mr = player.getHiResImageUri();
        this.mE = player.getRetrievedTimestamp();
        db.c(this.mD);
        db.c(this.ml);
        db.k(this.mE > 0);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int a(Player player) {
        return dl.hashCode(player.getPlayerId(), player.getDisplayName(), player.getIconImageUri(), player.getHiResImageUri(), Long.valueOf(player.getRetrievedTimestamp()));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean a(Player player, Object obj) {
        if (obj instanceof Player) {
            if (player != obj) {
                Player player2 = (Player) obj;
                return dl.equal(player2.getPlayerId(), player.getPlayerId()) && dl.equal(player2.getDisplayName(), player.getDisplayName()) && dl.equal(player2.getIconImageUri(), player.getIconImageUri()) && dl.equal(player2.getHiResImageUri(), player.getHiResImageUri()) && dl.equal(Long.valueOf(player2.getRetrievedTimestamp()), Long.valueOf(player.getRetrievedTimestamp()));
            }
            return true;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String b(Player player) {
        return dl.d(player).a("PlayerId", player.getPlayerId()).a("DisplayName", player.getDisplayName()).a("IconImageUri", player.getIconImageUri()).a("HiResImageUri", player.getHiResImageUri()).a("RetrievedTimestamp", Long.valueOf(player.getRetrievedTimestamp())).toString();
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
    public Player freeze() {
        return this;
    }

    @Override // com.google.android.gms.games.Player
    public String getDisplayName() {
        return this.ml;
    }

    @Override // com.google.android.gms.games.Player
    public void getDisplayName(CharArrayBuffer dataOut) {
        eg.b(this.ml, dataOut);
    }

    @Override // com.google.android.gms.games.Player
    public Uri getHiResImageUri() {
        return this.mr;
    }

    @Override // com.google.android.gms.games.Player
    public Uri getIconImageUri() {
        return this.mq;
    }

    @Override // com.google.android.gms.games.Player
    public String getPlayerId() {
        return this.mD;
    }

    @Override // com.google.android.gms.games.Player
    public long getRetrievedTimestamp() {
        return this.mE;
    }

    public int getVersionCode() {
        return this.iM;
    }

    @Override // com.google.android.gms.games.Player
    public boolean hasHiResImage() {
        return getHiResImageUri() != null;
    }

    @Override // com.google.android.gms.games.Player
    public boolean hasIconImage() {
        return getIconImageUri() != null;
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
            c.a(this, dest, flags);
            return;
        }
        dest.writeString(this.mD);
        dest.writeString(this.ml);
        dest.writeString(this.mq == null ? null : this.mq.toString());
        dest.writeString(this.mr != null ? this.mr.toString() : null);
        dest.writeLong(this.mE);
    }
}

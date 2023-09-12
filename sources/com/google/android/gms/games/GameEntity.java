package com.google.android.gms.games;

import android.database.CharArrayBuffer;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.internal.dl;
import com.google.android.gms.internal.eg;
import com.google.android.gms.internal.en;
import com.microsoft.kapp.diagnostics.TelemetryConstants;
/* loaded from: classes.dex */
public final class GameEntity extends en implements Game {
    public static final Parcelable.Creator<GameEntity> CREATOR = new a();
    private final int iM;
    private final String mk;
    private final String ml;
    private final String mm;
    private final String mn;
    private final String mo;
    private final String mp;
    private final Uri mq;
    private final Uri mr;
    private final Uri ms;
    private final boolean mt;
    private final boolean mu;
    private final String mv;
    private final int mw;
    private final int mx;
    private final int my;

    /* loaded from: classes.dex */
    static final class a extends com.google.android.gms.games.a {
        a() {
        }

        @Override // com.google.android.gms.games.a, android.os.Parcelable.Creator
        /* renamed from: t */
        public GameEntity createFromParcel(Parcel parcel) {
            if (GameEntity.c(GameEntity.bQ()) || GameEntity.y(GameEntity.class.getCanonicalName())) {
                return super.createFromParcel(parcel);
            }
            String readString = parcel.readString();
            String readString2 = parcel.readString();
            String readString3 = parcel.readString();
            String readString4 = parcel.readString();
            String readString5 = parcel.readString();
            String readString6 = parcel.readString();
            String readString7 = parcel.readString();
            Uri parse = readString7 == null ? null : Uri.parse(readString7);
            String readString8 = parcel.readString();
            Uri parse2 = readString8 == null ? null : Uri.parse(readString8);
            String readString9 = parcel.readString();
            return new GameEntity(1, readString, readString2, readString3, readString4, readString5, readString6, parse, parse2, readString9 == null ? null : Uri.parse(readString9), parcel.readInt() > 0, parcel.readInt() > 0, parcel.readString(), parcel.readInt(), parcel.readInt(), parcel.readInt());
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public GameEntity(int versionCode, String applicationId, String displayName, String primaryCategory, String secondaryCategory, String description, String developerName, Uri iconImageUri, Uri hiResImageUri, Uri featuredImageUri, boolean playEnabledGame, boolean instanceInstalled, String instancePackageName, int gameplayAclStatus, int achievementTotalCount, int leaderboardCount) {
        this.iM = versionCode;
        this.mk = applicationId;
        this.ml = displayName;
        this.mm = primaryCategory;
        this.mn = secondaryCategory;
        this.mo = description;
        this.mp = developerName;
        this.mq = iconImageUri;
        this.mr = hiResImageUri;
        this.ms = featuredImageUri;
        this.mt = playEnabledGame;
        this.mu = instanceInstalled;
        this.mv = instancePackageName;
        this.mw = gameplayAclStatus;
        this.mx = achievementTotalCount;
        this.my = leaderboardCount;
    }

    public GameEntity(Game game) {
        this.iM = 1;
        this.mk = game.getApplicationId();
        this.mm = game.getPrimaryCategory();
        this.mn = game.getSecondaryCategory();
        this.mo = game.getDescription();
        this.mp = game.getDeveloperName();
        this.ml = game.getDisplayName();
        this.mq = game.getIconImageUri();
        this.mr = game.getHiResImageUri();
        this.ms = game.getFeaturedImageUri();
        this.mt = game.isPlayEnabledGame();
        this.mu = game.isInstanceInstalled();
        this.mv = game.getInstancePackageName();
        this.mw = game.getGameplayAclStatus();
        this.mx = game.getAchievementTotalCount();
        this.my = game.getLeaderboardCount();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int a(Game game) {
        return dl.hashCode(game.getApplicationId(), game.getDisplayName(), game.getPrimaryCategory(), game.getSecondaryCategory(), game.getDescription(), game.getDeveloperName(), game.getIconImageUri(), game.getHiResImageUri(), game.getFeaturedImageUri(), Boolean.valueOf(game.isPlayEnabledGame()), Boolean.valueOf(game.isInstanceInstalled()), game.getInstancePackageName(), Integer.valueOf(game.getGameplayAclStatus()), Integer.valueOf(game.getAchievementTotalCount()), Integer.valueOf(game.getLeaderboardCount()));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean a(Game game, Object obj) {
        if (obj instanceof Game) {
            if (game != obj) {
                Game game2 = (Game) obj;
                return dl.equal(game2.getApplicationId(), game.getApplicationId()) && dl.equal(game2.getDisplayName(), game.getDisplayName()) && dl.equal(game2.getPrimaryCategory(), game.getPrimaryCategory()) && dl.equal(game2.getSecondaryCategory(), game.getSecondaryCategory()) && dl.equal(game2.getDescription(), game.getDescription()) && dl.equal(game2.getDeveloperName(), game.getDeveloperName()) && dl.equal(game2.getIconImageUri(), game.getIconImageUri()) && dl.equal(game2.getHiResImageUri(), game.getHiResImageUri()) && dl.equal(game2.getFeaturedImageUri(), game.getFeaturedImageUri()) && dl.equal(Boolean.valueOf(game2.isPlayEnabledGame()), Boolean.valueOf(game.isPlayEnabledGame())) && dl.equal(Boolean.valueOf(game2.isInstanceInstalled()), Boolean.valueOf(game.isInstanceInstalled())) && dl.equal(game2.getInstancePackageName(), game.getInstancePackageName()) && dl.equal(Integer.valueOf(game2.getGameplayAclStatus()), Integer.valueOf(game.getGameplayAclStatus())) && dl.equal(Integer.valueOf(game2.getAchievementTotalCount()), Integer.valueOf(game.getAchievementTotalCount())) && dl.equal(Integer.valueOf(game2.getLeaderboardCount()), Integer.valueOf(game.getLeaderboardCount()));
            }
            return true;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String b(Game game) {
        return dl.d(game).a("ApplicationId", game.getApplicationId()).a("DisplayName", game.getDisplayName()).a("PrimaryCategory", game.getPrimaryCategory()).a("SecondaryCategory", game.getSecondaryCategory()).a(TelemetryConstants.Events.SendFeedbackComplete.Dimensions.DESCRIPTION, game.getDescription()).a("DeveloperName", game.getDeveloperName()).a("IconImageUri", game.getIconImageUri()).a("HiResImageUri", game.getHiResImageUri()).a("FeaturedImageUri", game.getFeaturedImageUri()).a("PlayEnabledGame", Boolean.valueOf(game.isPlayEnabledGame())).a("InstanceInstalled", Boolean.valueOf(game.isInstanceInstalled())).a("InstancePackageName", game.getInstancePackageName()).a("GameplayAclStatus", Integer.valueOf(game.getGameplayAclStatus())).a("AchievementTotalCount", Integer.valueOf(game.getAchievementTotalCount())).a("LeaderboardCount", Integer.valueOf(game.getLeaderboardCount())).toString();
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
    public Game freeze() {
        return this;
    }

    @Override // com.google.android.gms.games.Game
    public int getAchievementTotalCount() {
        return this.mx;
    }

    @Override // com.google.android.gms.games.Game
    public String getApplicationId() {
        return this.mk;
    }

    @Override // com.google.android.gms.games.Game
    public String getDescription() {
        return this.mo;
    }

    @Override // com.google.android.gms.games.Game
    public void getDescription(CharArrayBuffer dataOut) {
        eg.b(this.mo, dataOut);
    }

    @Override // com.google.android.gms.games.Game
    public String getDeveloperName() {
        return this.mp;
    }

    @Override // com.google.android.gms.games.Game
    public void getDeveloperName(CharArrayBuffer dataOut) {
        eg.b(this.mp, dataOut);
    }

    @Override // com.google.android.gms.games.Game
    public String getDisplayName() {
        return this.ml;
    }

    @Override // com.google.android.gms.games.Game
    public void getDisplayName(CharArrayBuffer dataOut) {
        eg.b(this.ml, dataOut);
    }

    @Override // com.google.android.gms.games.Game
    public Uri getFeaturedImageUri() {
        return this.ms;
    }

    @Override // com.google.android.gms.games.Game
    public int getGameplayAclStatus() {
        return this.mw;
    }

    @Override // com.google.android.gms.games.Game
    public Uri getHiResImageUri() {
        return this.mr;
    }

    @Override // com.google.android.gms.games.Game
    public Uri getIconImageUri() {
        return this.mq;
    }

    @Override // com.google.android.gms.games.Game
    public String getInstancePackageName() {
        return this.mv;
    }

    @Override // com.google.android.gms.games.Game
    public int getLeaderboardCount() {
        return this.my;
    }

    @Override // com.google.android.gms.games.Game
    public String getPrimaryCategory() {
        return this.mm;
    }

    @Override // com.google.android.gms.games.Game
    public String getSecondaryCategory() {
        return this.mn;
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

    @Override // com.google.android.gms.games.Game
    public boolean isInstanceInstalled() {
        return this.mu;
    }

    @Override // com.google.android.gms.games.Game
    public boolean isPlayEnabledGame() {
        return this.mt;
    }

    public String toString() {
        return b(this);
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        if (!aX()) {
            com.google.android.gms.games.a.a(this, dest, flags);
            return;
        }
        dest.writeString(this.mk);
        dest.writeString(this.ml);
        dest.writeString(this.mm);
        dest.writeString(this.mn);
        dest.writeString(this.mo);
        dest.writeString(this.mp);
        dest.writeString(this.mq == null ? null : this.mq.toString());
        dest.writeString(this.mr == null ? null : this.mr.toString());
        dest.writeString(this.ms != null ? this.ms.toString() : null);
        dest.writeInt(this.mt ? 1 : 0);
        dest.writeInt(this.mu ? 1 : 0);
        dest.writeString(this.mv);
        dest.writeInt(this.mw);
        dest.writeInt(this.mx);
        dest.writeInt(this.my);
    }
}

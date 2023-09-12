package com.google.android.gms.games.leaderboard;

import android.database.CharArrayBuffer;
import android.net.Uri;
import com.google.android.gms.games.Player;
import com.google.android.gms.games.PlayerEntity;
import com.google.android.gms.internal.dl;
import com.google.android.gms.internal.dm;
import com.google.android.gms.internal.eg;
/* loaded from: classes.dex */
public final class c implements LeaderboardScore {
    private final long nA;
    private final long nB;
    private final String nC;
    private final Uri nD;
    private final Uri nE;
    private final PlayerEntity nF;
    private final String nG;
    private final long nx;
    private final String ny;
    private final String nz;

    public c(LeaderboardScore leaderboardScore) {
        this.nx = leaderboardScore.getRank();
        this.ny = (String) dm.e(leaderboardScore.getDisplayRank());
        this.nz = (String) dm.e(leaderboardScore.getDisplayScore());
        this.nA = leaderboardScore.getRawScore();
        this.nB = leaderboardScore.getTimestampMillis();
        this.nC = leaderboardScore.getScoreHolderDisplayName();
        this.nD = leaderboardScore.getScoreHolderIconImageUri();
        this.nE = leaderboardScore.getScoreHolderHiResImageUri();
        Player scoreHolder = leaderboardScore.getScoreHolder();
        this.nF = scoreHolder == null ? null : (PlayerEntity) scoreHolder.freeze();
        this.nG = leaderboardScore.getScoreTag();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int a(LeaderboardScore leaderboardScore) {
        return dl.hashCode(Long.valueOf(leaderboardScore.getRank()), leaderboardScore.getDisplayRank(), Long.valueOf(leaderboardScore.getRawScore()), leaderboardScore.getDisplayScore(), Long.valueOf(leaderboardScore.getTimestampMillis()), leaderboardScore.getScoreHolderDisplayName(), leaderboardScore.getScoreHolderIconImageUri(), leaderboardScore.getScoreHolderHiResImageUri(), leaderboardScore.getScoreHolder());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean a(LeaderboardScore leaderboardScore, Object obj) {
        if (obj instanceof LeaderboardScore) {
            if (leaderboardScore != obj) {
                LeaderboardScore leaderboardScore2 = (LeaderboardScore) obj;
                return dl.equal(Long.valueOf(leaderboardScore2.getRank()), Long.valueOf(leaderboardScore.getRank())) && dl.equal(leaderboardScore2.getDisplayRank(), leaderboardScore.getDisplayRank()) && dl.equal(Long.valueOf(leaderboardScore2.getRawScore()), Long.valueOf(leaderboardScore.getRawScore())) && dl.equal(leaderboardScore2.getDisplayScore(), leaderboardScore.getDisplayScore()) && dl.equal(Long.valueOf(leaderboardScore2.getTimestampMillis()), Long.valueOf(leaderboardScore.getTimestampMillis())) && dl.equal(leaderboardScore2.getScoreHolderDisplayName(), leaderboardScore.getScoreHolderDisplayName()) && dl.equal(leaderboardScore2.getScoreHolderIconImageUri(), leaderboardScore.getScoreHolderIconImageUri()) && dl.equal(leaderboardScore2.getScoreHolderHiResImageUri(), leaderboardScore.getScoreHolderHiResImageUri()) && dl.equal(leaderboardScore2.getScoreHolder(), leaderboardScore.getScoreHolder()) && dl.equal(leaderboardScore2.getScoreTag(), leaderboardScore.getScoreTag());
            }
            return true;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String b(LeaderboardScore leaderboardScore) {
        return dl.d(leaderboardScore).a("Rank", Long.valueOf(leaderboardScore.getRank())).a("DisplayRank", leaderboardScore.getDisplayRank()).a("Score", Long.valueOf(leaderboardScore.getRawScore())).a("DisplayScore", leaderboardScore.getDisplayScore()).a("Timestamp", Long.valueOf(leaderboardScore.getTimestampMillis())).a("DisplayName", leaderboardScore.getScoreHolderDisplayName()).a("IconImageUri", leaderboardScore.getScoreHolderIconImageUri()).a("HiResImageUri", leaderboardScore.getScoreHolderHiResImageUri()).a("Player", leaderboardScore.getScoreHolder() == null ? null : leaderboardScore.getScoreHolder()).a("ScoreTag", leaderboardScore.getScoreTag()).toString();
    }

    @Override // com.google.android.gms.common.data.Freezable
    /* renamed from: cd */
    public LeaderboardScore freeze() {
        return this;
    }

    public boolean equals(Object obj) {
        return a(this, obj);
    }

    @Override // com.google.android.gms.games.leaderboard.LeaderboardScore
    public String getDisplayRank() {
        return this.ny;
    }

    @Override // com.google.android.gms.games.leaderboard.LeaderboardScore
    public void getDisplayRank(CharArrayBuffer dataOut) {
        eg.b(this.ny, dataOut);
    }

    @Override // com.google.android.gms.games.leaderboard.LeaderboardScore
    public String getDisplayScore() {
        return this.nz;
    }

    @Override // com.google.android.gms.games.leaderboard.LeaderboardScore
    public void getDisplayScore(CharArrayBuffer dataOut) {
        eg.b(this.nz, dataOut);
    }

    @Override // com.google.android.gms.games.leaderboard.LeaderboardScore
    public long getRank() {
        return this.nx;
    }

    @Override // com.google.android.gms.games.leaderboard.LeaderboardScore
    public long getRawScore() {
        return this.nA;
    }

    @Override // com.google.android.gms.games.leaderboard.LeaderboardScore
    public Player getScoreHolder() {
        return this.nF;
    }

    @Override // com.google.android.gms.games.leaderboard.LeaderboardScore
    public String getScoreHolderDisplayName() {
        return this.nF == null ? this.nC : this.nF.getDisplayName();
    }

    @Override // com.google.android.gms.games.leaderboard.LeaderboardScore
    public void getScoreHolderDisplayName(CharArrayBuffer dataOut) {
        if (this.nF == null) {
            eg.b(this.nC, dataOut);
        } else {
            this.nF.getDisplayName(dataOut);
        }
    }

    @Override // com.google.android.gms.games.leaderboard.LeaderboardScore
    public Uri getScoreHolderHiResImageUri() {
        return this.nF == null ? this.nE : this.nF.getHiResImageUri();
    }

    @Override // com.google.android.gms.games.leaderboard.LeaderboardScore
    public Uri getScoreHolderIconImageUri() {
        return this.nF == null ? this.nD : this.nF.getIconImageUri();
    }

    @Override // com.google.android.gms.games.leaderboard.LeaderboardScore
    public String getScoreTag() {
        return this.nG;
    }

    @Override // com.google.android.gms.games.leaderboard.LeaderboardScore
    public long getTimestampMillis() {
        return this.nB;
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
}

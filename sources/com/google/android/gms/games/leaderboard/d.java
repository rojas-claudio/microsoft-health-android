package com.google.android.gms.games.leaderboard;

import android.database.CharArrayBuffer;
import android.net.Uri;
import com.google.android.gms.games.Player;
/* loaded from: classes.dex */
public final class d extends com.google.android.gms.common.data.b implements LeaderboardScore {
    private final com.google.android.gms.games.d nH;

    /* JADX INFO: Access modifiers changed from: package-private */
    public d(com.google.android.gms.common.data.d dVar, int i) {
        super(dVar, i);
        this.nH = new com.google.android.gms.games.d(dVar, i);
    }

    @Override // com.google.android.gms.common.data.Freezable
    /* renamed from: cd */
    public LeaderboardScore freeze() {
        return new c(this);
    }

    @Override // com.google.android.gms.common.data.b
    public boolean equals(Object obj) {
        return c.a(this, obj);
    }

    @Override // com.google.android.gms.games.leaderboard.LeaderboardScore
    public String getDisplayRank() {
        return getString("display_rank");
    }

    @Override // com.google.android.gms.games.leaderboard.LeaderboardScore
    public void getDisplayRank(CharArrayBuffer dataOut) {
        a("display_rank", dataOut);
    }

    @Override // com.google.android.gms.games.leaderboard.LeaderboardScore
    public String getDisplayScore() {
        return getString("display_score");
    }

    @Override // com.google.android.gms.games.leaderboard.LeaderboardScore
    public void getDisplayScore(CharArrayBuffer dataOut) {
        a("display_score", dataOut);
    }

    @Override // com.google.android.gms.games.leaderboard.LeaderboardScore
    public long getRank() {
        return getLong("rank");
    }

    @Override // com.google.android.gms.games.leaderboard.LeaderboardScore
    public long getRawScore() {
        return getLong("raw_score");
    }

    @Override // com.google.android.gms.games.leaderboard.LeaderboardScore
    public Player getScoreHolder() {
        if (v("external_player_id")) {
            return null;
        }
        return this.nH;
    }

    @Override // com.google.android.gms.games.leaderboard.LeaderboardScore
    public String getScoreHolderDisplayName() {
        return v("external_player_id") ? getString("default_display_name") : this.nH.getDisplayName();
    }

    @Override // com.google.android.gms.games.leaderboard.LeaderboardScore
    public void getScoreHolderDisplayName(CharArrayBuffer dataOut) {
        if (v("external_player_id")) {
            a("default_display_name", dataOut);
        } else {
            this.nH.getDisplayName(dataOut);
        }
    }

    @Override // com.google.android.gms.games.leaderboard.LeaderboardScore
    public Uri getScoreHolderHiResImageUri() {
        if (v("external_player_id")) {
            return null;
        }
        return this.nH.getHiResImageUri();
    }

    @Override // com.google.android.gms.games.leaderboard.LeaderboardScore
    public Uri getScoreHolderIconImageUri() {
        return v("external_player_id") ? u("default_display_image_uri") : this.nH.getIconImageUri();
    }

    @Override // com.google.android.gms.games.leaderboard.LeaderboardScore
    public String getScoreTag() {
        return getString("score_tag");
    }

    @Override // com.google.android.gms.games.leaderboard.LeaderboardScore
    public long getTimestampMillis() {
        return getLong("achieved_timestamp");
    }

    @Override // com.google.android.gms.common.data.b
    public int hashCode() {
        return c.a(this);
    }

    public String toString() {
        return c.b(this);
    }
}

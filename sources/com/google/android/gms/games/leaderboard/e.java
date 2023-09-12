package com.google.android.gms.games.leaderboard;

import com.google.android.gms.internal.dl;
import com.google.android.gms.internal.eu;
import com.google.android.gms.internal.ev;
import com.microsoft.krestsdk.services.KCloudConstants;
/* loaded from: classes.dex */
public final class e extends com.google.android.gms.common.data.b implements LeaderboardVariant {
    /* JADX INFO: Access modifiers changed from: package-private */
    public e(com.google.android.gms.common.data.d dVar, int i) {
        super(dVar, i);
    }

    public String ce() {
        return getString("top_page_token_next");
    }

    public String cf() {
        return getString("window_page_token_prev");
    }

    public String cg() {
        return getString("window_page_token_next");
    }

    @Override // com.google.android.gms.games.leaderboard.LeaderboardVariant
    public int getCollection() {
        return getInteger("collection");
    }

    @Override // com.google.android.gms.games.leaderboard.LeaderboardVariant
    public String getDisplayPlayerRank() {
        return getString("player_display_rank");
    }

    @Override // com.google.android.gms.games.leaderboard.LeaderboardVariant
    public String getDisplayPlayerScore() {
        return getString("player_display_score");
    }

    @Override // com.google.android.gms.games.leaderboard.LeaderboardVariant
    public long getNumScores() {
        if (v("total_scores")) {
            return -1L;
        }
        return getLong("total_scores");
    }

    @Override // com.google.android.gms.games.leaderboard.LeaderboardVariant
    public long getPlayerRank() {
        if (v("player_rank")) {
            return -1L;
        }
        return getLong("player_rank");
    }

    @Override // com.google.android.gms.games.leaderboard.LeaderboardVariant
    public String getPlayerScoreTag() {
        return getString("player_score_tag");
    }

    @Override // com.google.android.gms.games.leaderboard.LeaderboardVariant
    public long getRawPlayerScore() {
        if (v("player_raw_score")) {
            return -1L;
        }
        return getLong("player_raw_score");
    }

    @Override // com.google.android.gms.games.leaderboard.LeaderboardVariant
    public int getTimeSpan() {
        return getInteger(KCloudConstants.TIMESPAN);
    }

    @Override // com.google.android.gms.games.leaderboard.LeaderboardVariant
    public boolean hasPlayerInfo() {
        return !v("player_raw_score");
    }

    public String toString() {
        return dl.d(this).a("TimeSpan", ev.R(getTimeSpan())).a("Collection", eu.R(getCollection())).a("RawPlayerScore", hasPlayerInfo() ? Long.valueOf(getRawPlayerScore()) : "none").a("DisplayPlayerScore", hasPlayerInfo() ? getDisplayPlayerScore() : "none").a("PlayerRank", hasPlayerInfo() ? Long.valueOf(getPlayerRank()) : "none").a("DisplayPlayerRank", hasPlayerInfo() ? getDisplayPlayerRank() : "none").a("NumScores", Long.valueOf(getNumScores())).a("TopPageNextToken", ce()).a("WindowPageNextToken", cg()).a("WindowPagePrevToken", cf()).toString();
    }
}

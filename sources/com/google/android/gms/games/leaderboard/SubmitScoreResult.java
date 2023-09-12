package com.google.android.gms.games.leaderboard;

import com.google.android.gms.internal.dl;
import com.google.android.gms.internal.dm;
import com.google.android.gms.internal.ev;
import java.util.HashMap;
/* loaded from: classes.dex */
public final class SubmitScoreResult {
    private static final String[] nI = {"leaderboardId", "playerId", "timeSpan", "hasResult", "rawScore", "formattedScore", "newBest", "scoreTag"};
    private int iC;
    private String mD;
    private String nJ;
    private HashMap<Integer, Result> nK;

    /* loaded from: classes.dex */
    public static final class Result {
        public final String formattedScore;
        public final boolean newBest;
        public final long rawScore;
        public final String scoreTag;

        public Result(long rawScore, String formattedScore, String scoreTag, boolean newBest) {
            this.rawScore = rawScore;
            this.formattedScore = formattedScore;
            this.scoreTag = scoreTag;
            this.newBest = newBest;
        }

        public String toString() {
            return dl.d(this).a("RawScore", Long.valueOf(this.rawScore)).a("FormattedScore", this.formattedScore).a("ScoreTag", this.scoreTag).a("NewBest", Boolean.valueOf(this.newBest)).toString();
        }
    }

    public SubmitScoreResult(int statusCode, String leaderboardId, String playerId) {
        this(statusCode, leaderboardId, playerId, new HashMap());
    }

    public SubmitScoreResult(int statusCode, String leaderboardId, String playerId, HashMap<Integer, Result> results) {
        this.iC = statusCode;
        this.nJ = leaderboardId;
        this.mD = playerId;
        this.nK = results;
    }

    public SubmitScoreResult(com.google.android.gms.common.data.d dataHolder) {
        this.iC = dataHolder.getStatusCode();
        this.nK = new HashMap<>();
        int count = dataHolder.getCount();
        dm.m(count == 3);
        for (int i = 0; i < count; i++) {
            int q = dataHolder.q(i);
            if (i == 0) {
                this.nJ = dataHolder.c("leaderboardId", i, q);
                this.mD = dataHolder.c("playerId", i, q);
            }
            if (dataHolder.d("hasResult", i, q)) {
                a(new Result(dataHolder.a("rawScore", i, q), dataHolder.c("formattedScore", i, q), dataHolder.c("scoreTag", i, q), dataHolder.d("newBest", i, q)), dataHolder.b("timeSpan", i, q));
            }
        }
    }

    private void a(Result result, int i) {
        this.nK.put(Integer.valueOf(i), result);
    }

    public String getLeaderboardId() {
        return this.nJ;
    }

    public String getPlayerId() {
        return this.mD;
    }

    public Result getScoreResult(int timeSpan) {
        return this.nK.get(Integer.valueOf(timeSpan));
    }

    public int getStatusCode() {
        return this.iC;
    }

    public String toString() {
        dl.a a = dl.d(this).a("PlayerId", this.mD).a("StatusCode", Integer.valueOf(this.iC));
        int i = 0;
        while (true) {
            int i2 = i;
            if (i2 >= 3) {
                return a.toString();
            }
            Result result = this.nK.get(Integer.valueOf(i2));
            a.a("TimesSpan", ev.R(i2));
            a.a("Result", result == null ? "null" : result.toString());
            i = i2 + 1;
        }
    }
}

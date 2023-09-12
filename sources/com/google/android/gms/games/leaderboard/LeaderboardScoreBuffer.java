package com.google.android.gms.games.leaderboard;

import com.google.android.gms.common.data.DataBuffer;
/* loaded from: classes.dex */
public final class LeaderboardScoreBuffer extends DataBuffer<LeaderboardScore> {
    private final b nv;

    public LeaderboardScoreBuffer(com.google.android.gms.common.data.d dataHolder) {
        super(dataHolder);
        this.nv = new b(dataHolder.aM());
    }

    public b cb() {
        return this.nv;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.google.android.gms.common.data.DataBuffer
    public LeaderboardScore get(int position) {
        return new d(this.jf, position);
    }
}

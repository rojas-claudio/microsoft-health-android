package com.google.android.gms.games.leaderboard;

import android.database.CharArrayBuffer;
import android.net.Uri;
import com.google.android.gms.internal.dl;
import com.microsoft.kapp.services.healthandfitness.models.WorkoutSummary;
import java.util.ArrayList;
/* loaded from: classes.dex */
public final class a extends com.google.android.gms.common.data.b implements Leaderboard {
    private final int nu;

    /* JADX INFO: Access modifiers changed from: package-private */
    public a(com.google.android.gms.common.data.d dVar, int i, int i2) {
        super(dVar, i);
        this.nu = i2;
    }

    @Override // com.google.android.gms.games.leaderboard.Leaderboard
    public String getDisplayName() {
        return getString(WorkoutSummary.NAME);
    }

    @Override // com.google.android.gms.games.leaderboard.Leaderboard
    public void getDisplayName(CharArrayBuffer dataOut) {
        a(WorkoutSummary.NAME, dataOut);
    }

    @Override // com.google.android.gms.games.leaderboard.Leaderboard
    public Uri getIconImageUri() {
        return u("board_icon_image_uri");
    }

    @Override // com.google.android.gms.games.leaderboard.Leaderboard
    public String getLeaderboardId() {
        return getString("external_leaderboard_id");
    }

    @Override // com.google.android.gms.games.leaderboard.Leaderboard
    public int getScoreOrder() {
        return getInteger("score_order");
    }

    @Override // com.google.android.gms.games.leaderboard.Leaderboard
    public ArrayList<LeaderboardVariant> getVariants() {
        ArrayList<LeaderboardVariant> arrayList = new ArrayList<>(this.nu);
        for (int i = 0; i < this.nu; i++) {
            arrayList.add(new e(this.jf, this.ji + i));
        }
        return arrayList;
    }

    public String toString() {
        return dl.d(this).a("ID", getLeaderboardId()).a("DisplayName", getDisplayName()).a("IconImageURI", getIconImageUri()).a("ScoreOrder", Integer.valueOf(getScoreOrder())).toString();
    }
}

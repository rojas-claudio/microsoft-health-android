package com.google.android.gms.games.achievement;

import android.database.CharArrayBuffer;
import android.net.Uri;
import com.google.android.gms.common.data.b;
import com.google.android.gms.common.data.d;
import com.google.android.gms.games.Player;
import com.google.android.gms.internal.db;
import com.google.android.gms.internal.dl;
import com.google.android.gms.plus.PlusShare;
import com.microsoft.kapp.services.healthandfitness.models.WorkoutSummary;
import com.microsoft.kapp.utils.Constants;
/* loaded from: classes.dex */
public final class a extends b implements Achievement {
    /* JADX INFO: Access modifiers changed from: package-private */
    public a(d dVar, int i) {
        super(dVar, i);
    }

    @Override // com.google.android.gms.games.achievement.Achievement
    public String getAchievementId() {
        return getString("external_achievement_id");
    }

    @Override // com.google.android.gms.games.achievement.Achievement
    public int getCurrentSteps() {
        db.k(getType() == 1);
        return getInteger("current_steps");
    }

    @Override // com.google.android.gms.games.achievement.Achievement
    public String getDescription() {
        return getString(PlusShare.KEY_CONTENT_DEEP_LINK_METADATA_DESCRIPTION);
    }

    @Override // com.google.android.gms.games.achievement.Achievement
    public void getDescription(CharArrayBuffer dataOut) {
        a(PlusShare.KEY_CONTENT_DEEP_LINK_METADATA_DESCRIPTION, dataOut);
    }

    @Override // com.google.android.gms.games.achievement.Achievement
    public String getFormattedCurrentSteps() {
        db.k(getType() == 1);
        return getString("formatted_current_steps");
    }

    @Override // com.google.android.gms.games.achievement.Achievement
    public void getFormattedCurrentSteps(CharArrayBuffer dataOut) {
        db.k(getType() == 1);
        a("formatted_current_steps", dataOut);
    }

    @Override // com.google.android.gms.games.achievement.Achievement
    public String getFormattedTotalSteps() {
        db.k(getType() == 1);
        return getString("formatted_total_steps");
    }

    @Override // com.google.android.gms.games.achievement.Achievement
    public void getFormattedTotalSteps(CharArrayBuffer dataOut) {
        db.k(getType() == 1);
        a("formatted_total_steps", dataOut);
    }

    @Override // com.google.android.gms.games.achievement.Achievement
    public long getLastUpdatedTimestamp() {
        return getLong("last_updated_timestamp");
    }

    @Override // com.google.android.gms.games.achievement.Achievement
    public String getName() {
        return getString(WorkoutSummary.NAME);
    }

    @Override // com.google.android.gms.games.achievement.Achievement
    public void getName(CharArrayBuffer dataOut) {
        a(WorkoutSummary.NAME, dataOut);
    }

    @Override // com.google.android.gms.games.achievement.Achievement
    public Player getPlayer() {
        return new com.google.android.gms.games.d(this.jf, this.ji);
    }

    @Override // com.google.android.gms.games.achievement.Achievement
    public Uri getRevealedImageUri() {
        return u("revealed_icon_image_uri");
    }

    @Override // com.google.android.gms.games.achievement.Achievement
    public int getState() {
        return getInteger("state");
    }

    @Override // com.google.android.gms.games.achievement.Achievement
    public int getTotalSteps() {
        db.k(getType() == 1);
        return getInteger("total_steps");
    }

    @Override // com.google.android.gms.games.achievement.Achievement
    public int getType() {
        return getInteger("type");
    }

    @Override // com.google.android.gms.games.achievement.Achievement
    public Uri getUnlockedImageUri() {
        return u("unlocked_icon_image_uri");
    }

    public String toString() {
        dl.a a = dl.d(this).a("id", getAchievementId()).a(WorkoutSummary.NAME, getName()).a("state", Integer.valueOf(getState())).a("type", Integer.valueOf(getType()));
        if (getType() == 1) {
            a.a(Constants.GOAL_ID_STEPS, getCurrentSteps() + "/" + getTotalSteps());
        }
        return a.toString();
    }
}

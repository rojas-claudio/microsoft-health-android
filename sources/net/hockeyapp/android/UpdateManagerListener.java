package net.hockeyapp.android;

import java.util.Date;
import org.json.JSONArray;
/* loaded from: classes.dex */
public abstract class UpdateManagerListener extends StringListener {
    public Class<? extends UpdateActivity> getUpdateActivityClass() {
        return UpdateActivity.class;
    }

    public Class<? extends UpdateFragment> getUpdateFragmentClass() {
        return UpdateFragment.class;
    }

    public void onNoUpdateAvailable() {
    }

    public void onUpdateAvailable() {
    }

    public void onUpdateAvailable(JSONArray data, String url) {
        onUpdateAvailable();
    }

    public Date getExpiryDate() {
        return null;
    }

    public boolean onBuildExpired() {
        return true;
    }

    public boolean canUpdateInMarket() {
        return false;
    }
}

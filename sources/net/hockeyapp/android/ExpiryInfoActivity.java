package net.hockeyapp.android;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import net.hockeyapp.android.views.ExpiryInfoView;
/* loaded from: classes.dex */
public class ExpiryInfoActivity extends Activity {
    @Override // android.app.Activity
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getStringResource(768));
        setContentView(getLayoutView());
    }

    protected View getLayoutView() {
        return new ExpiryInfoView(this, getStringResource(Strings.EXPIRY_INFO_TEXT_ID));
    }

    protected String getStringResource(int resourceID) {
        UpdateManagerListener listener = UpdateManager.getLastListener();
        return Strings.get(listener, resourceID);
    }
}

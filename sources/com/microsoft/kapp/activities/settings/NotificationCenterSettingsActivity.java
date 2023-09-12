package com.microsoft.kapp.activities.settings;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import com.microsoft.kapp.R;
import com.microsoft.kapp.activities.BaseFragmentActivity;
import com.microsoft.kapp.fragments.NotificationCenterSettingsFragment;
import com.microsoft.kapp.utils.ActivityUtils;
/* loaded from: classes.dex */
public class NotificationCenterSettingsActivity extends BaseFragmentActivity {
    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.microsoft.kapp.activities.BaseFragmentActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finance_settings);
        NotificationCenterSettingsFragment notificationCenterFragment = new NotificationCenterSettingsFragment();
        FragmentTransaction ft = ActivityUtils.getFragmentTransaction(this, false);
        ft.replace(R.id.current_fragment, notificationCenterFragment);
        ft.commit();
    }
}

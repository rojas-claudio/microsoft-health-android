package com.microsoft.kapp.activities;

import android.os.Bundle;
import com.microsoft.kapp.R;
import com.microsoft.kapp.diagnostics.Validate;
import com.microsoft.kapp.fragments.SettingsProfileFragment;
import com.microsoft.kapp.models.FreStatus;
/* loaded from: classes.dex */
public class OobeProfileActivity extends OobeBaseActivity implements SettingsProfileFragment.SettingsProfileFragmentCalls {
    private static final int CUSTOM_ENTER_ANIMATION = 2130968577;
    private static final int CUSTOM_EXIT_ANIMATION = 2130968578;
    private static final String TAG_PROFILE_FRAGMENT = "profile_fragment";

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.microsoft.kapp.activities.OobeBaseActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oobe_profile);
        Validate.notNull(findViewById(R.id.profile_fragment_container), "profile_fragment_container");
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in, R.anim.fade_out).add(R.id.profile_fragment_container, SettingsProfileFragment.newInstance(true), TAG_PROFILE_FRAGMENT).commit();
        }
    }

    @Override // com.microsoft.kapp.fragments.SettingsProfileFragment.SettingsProfileFragmentCalls
    public void onProfileCompleted() {
        moveToNextOobeTask(FreStatus.CREATED_PROFILE);
    }
}

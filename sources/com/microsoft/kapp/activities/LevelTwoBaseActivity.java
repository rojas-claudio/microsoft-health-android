package com.microsoft.kapp.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import com.microsoft.kapp.R;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.kapp.utils.Constants;
/* loaded from: classes.dex */
public class LevelTwoBaseActivity extends BaseFragmentActivity implements KAppHostActivity {
    private final String TAG = getClass().getSimpleName();
    private boolean mBackButtonDisabled;

    @Override // com.microsoft.kapp.activities.BaseFragmentActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.level_two_base_activity);
        if (savedInstanceState == null) {
            Fragment fragment = null;
            String fragmentClass = getIntent().getStringExtra(Constants.KEY_FRAGMENT_CLASS);
            try {
                Class<?> className = Class.forName(fragmentClass);
                fragment = (Fragment) className.newInstance();
                Bundle fragmentArgs = getIntent().getBundleExtra(Constants.KEY_FRAGMENT_PARAMS);
                fragment.setArguments(fragmentArgs);
            } catch (Exception ex) {
                KLog.e(this.TAG, String.format("Unable to create fragment with class name %s", fragmentClass), ex);
            }
            navigateToFragment(fragment);
        }
    }

    @Override // android.support.v4.app.FragmentActivity, android.app.Activity
    public void onBackPressed() {
        if (!this.mBackButtonDisabled) {
            super.onBackPressed();
        }
    }

    @Override // com.microsoft.kapp.activities.KAppHostActivity
    public void setBackButtonState(boolean disable) {
        this.mBackButtonDisabled = disable;
    }

    @Override // com.microsoft.kapp.activities.KAppHostActivity
    public void navigateToFragment(Fragment fragment) {
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.main_content, fragment);
            try {
                ft.commit();
                return;
            } catch (Exception ex) {
                KLog.w(this.TAG, "Unable to launch fragment: %s", ex);
                return;
            }
        }
        KLog.w(this.TAG, "Unable to launch fragment");
        finish();
    }
}

package com.microsoft.kapp.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import com.microsoft.kapp.R;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.kapp.utils.Constants;
import com.microsoft.kapp.widgets.HeaderBar;
/* loaded from: classes.dex */
public class LevelTwoPagesActivity extends HeaderBarFragmentActivity {
    private static final String TAG = LevelTwoPagesActivity.class.getSimpleName();

    @Override // com.microsoft.kapp.activities.BaseFragmentActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_two_pages);
        HeaderBar header = getHeaderBar();
        header.setLeftButtonType(2);
        header.setSupportInitialProgressAnimation(false);
        header.setLeftButtonOnClickListener(new View.OnClickListener() { // from class: com.microsoft.kapp.activities.LevelTwoPagesActivity.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                LevelTwoPagesActivity.this.setResult(0);
                LevelTwoPagesActivity.this.finish();
            }
        });
        Fragment fragment = null;
        String fragmentClass = getIntent().getStringExtra(Constants.KEY_FRAGMENT_CLASS);
        try {
            Class<?> className = Class.forName(fragmentClass);
            fragment = (Fragment) className.newInstance();
            Bundle fragmentArgs = getIntent().getBundleExtra(Constants.KEY_FRAGMENT_PARAMS);
            fragment.setArguments(fragmentArgs);
        } catch (Exception ex) {
            KLog.e(TAG, String.format("Unable to create fragment with class name %s", fragmentClass), ex);
        }
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.main_content, fragment);
            ft.commit();
            return;
        }
        KLog.w(TAG, "Unable to launch fragment");
        finish();
    }
}

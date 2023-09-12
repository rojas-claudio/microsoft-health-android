package com.microsoft.kapp.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.FacebookDialog;
import com.microsoft.kapp.CargoConnection;
import com.microsoft.kapp.KApplication;
import com.microsoft.kapp.R;
import com.microsoft.kapp.diagnostics.Compatibility;
import com.microsoft.kapp.services.SettingsProvider;
import com.microsoft.kapp.utils.DialogManagerImpl;
import com.microsoft.kapp.utils.StubDialogManager;
import com.microsoft.kapp.version.VersionUpdateInteractionCoordinator;
import javax.inject.Inject;
/* loaded from: classes.dex */
public class BaseActivityAdapter {
    private Activity mActivity;
    @Inject
    CargoConnection mCargoConnection;
    @Inject
    DialogManagerImpl mDialogManager;
    private boolean mIsInForeground;
    @Inject
    SettingsProvider mSettingsProvider;
    @Inject
    StubDialogManager mStubDialogManager;
    @Inject
    VersionUpdateInteractionCoordinator mVersionUpdateInteractionCoordinator;
    private UiLifecycleHelper uiHelper;

    public BaseActivityAdapter(Activity activity) {
        this.mActivity = activity;
    }

    public void onCreate(Bundle savedInstanceState) {
        KApplication application = (KApplication) this.mActivity.getApplication();
        this.uiHelper = new UiLifecycleHelper(this.mActivity, null);
        this.uiHelper.onCreate(savedInstanceState);
        application.inject(this.mActivity);
        application.inject(this);
    }

    public void onResume() {
        this.uiHelper.onResume();
        this.mVersionUpdateInteractionCoordinator.requestApplicationVersionUpdateCheck();
        this.mVersionUpdateInteractionCoordinator.displayApplicationUpdateNotificationIfNecessary(this.mActivity);
        this.mIsInForeground = true;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void onPause() {
        this.uiHelper.onPause();
        this.mDialogManager.dismiss();
        this.mIsInForeground = false;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data, FacebookDialog.Callback callback) {
        this.uiHelper.onActivityResult(requestCode, resultCode, data, callback);
    }

    protected void onSaveInstanceState(Bundle outState) {
        this.uiHelper.onSaveInstanceState(outState);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        this.mActivity.getMenuInflater().inflate(R.menu.kapp_common_debug_menu, menu);
        return true;
    }

    public boolean onPrepareOptionsMenu(Menu menu) {
        if (isDebugMenuEnabled()) {
            int[] menuItemIds = {R.id.action_debug};
            for (int id : menuItemIds) {
                MenuItem item = menu.findItem(id);
                if (item != null) {
                    item.setVisible(true);
                }
            }
        }
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (isDebugMenuEnabled()) {
            switch (item.getItemId()) {
                case R.id.action_debug /* 2131100608 */:
                    this.mActivity.startActivity(new Intent(this.mActivity, DebugActivity.class));
                    return true;
            }
        }
        return false;
    }

    public DialogManager getDialogManager() {
        return this.mIsInForeground ? this.mDialogManager : this.mStubDialogManager;
    }

    private static boolean isDebugMenuEnabled() {
        return !Compatibility.isPublicRelease();
    }

    protected void onDestroy() {
        this.uiHelper.onDestroy();
    }
}

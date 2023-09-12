package com.microsoft.kapp.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import com.microsoft.kapp.CargoConnection;
import com.microsoft.kapp.FontManager;
import com.microsoft.kapp.KApplication;
import com.microsoft.kapp.OnTaskListener;
import com.microsoft.kapp.R;
import com.microsoft.kapp.activities.DialogManager;
import com.microsoft.kapp.activities.OnBackButtonPressedListener;
import com.microsoft.kapp.activities.OnNavigateToFragmentListener;
import com.microsoft.kapp.device.CargoSyncResult;
import com.microsoft.kapp.diagnostics.Validate;
import com.microsoft.kapp.event.SyncCompletedEvent;
import com.microsoft.kapp.event.SyncStartedEvent;
import com.microsoft.kapp.event.SyncStatusListener;
import com.microsoft.kapp.fragments.TopMenuFragment;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.kapp.multidevice.MultiDeviceManager;
import com.microsoft.kapp.services.timeZone.TimeZoneChangeHandler;
import com.microsoft.kapp.services.timeZone.TimeZoneChangeListner;
import com.microsoft.kapp.utils.DialogManagerImpl;
import com.microsoft.kapp.version.VersionUpdateInteractionCoordinator;
import javax.inject.Inject;
import org.joda.time.DateTime;
/* loaded from: classes.dex */
public abstract class BaseFragment extends Fragment implements SyncStatusListener, OnBackButtonPressedListener, OnTaskListener, OnNavigateToFragmentListener, TimeZoneChangeListner {
    public static final String ARG_REFERRER = "referrer";
    private static final String LAST_REFRESH_TIME = "mLastRefreshTime";
    protected final String TAG = getClass().getSimpleName();
    @Inject
    protected CargoConnection mCargoConnection;
    @Inject
    protected FontManager mFontManager;
    protected DateTime mLastRefreshTime;
    @Inject
    protected MultiDeviceManager mMultiDeviceManager;
    @Inject
    TimeZoneChangeHandler mTimeZoneChangeHandler;
    @Inject
    VersionUpdateInteractionCoordinator mVersionUpdateInteractionCoordinator;

    public DialogManager getDialogManager() {
        Activity activity = getActivity();
        return DialogManagerImpl.getDialogManager(activity);
    }

    @Override // android.support.v4.app.Fragment
    public void onCreate(Bundle savedInstanceState) {
        KApplication application = (KApplication) getActivity().getApplication();
        application.inject(this);
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            this.mLastRefreshTime = new DateTime(savedInstanceState.getLong(LAST_REFRESH_TIME));
        }
    }

    @Override // android.support.v4.app.Fragment
    public void onResume() {
        super.onResume();
        DateTime lastSyncTime = this.mMultiDeviceManager.getLastSyncTime();
        if (doForceRefresh() || this.mLastRefreshTime == null || (lastSyncTime != null && this.mLastRefreshTime.isBefore(lastSyncTime))) {
            refreshData();
        }
        this.mMultiDeviceManager.addSyncStatusListener(this);
        Activity activity = getActivity();
        this.mVersionUpdateInteractionCoordinator.requestApplicationVersionUpdateCheck();
        this.mVersionUpdateInteractionCoordinator.displayApplicationUpdateNotificationIfNecessary(activity);
        setTopMenuDividerVisibility();
    }

    @Override // android.support.v4.app.Fragment
    public void onDestroyView() {
        super.onDestroyView();
        this.mLastRefreshTime = null;
    }

    @Override // android.support.v4.app.Fragment
    public void onPause() {
        super.onPause();
        this.mMultiDeviceManager.removeSyncStatusListener(this);
    }

    @Override // android.support.v4.app.Fragment
    public void onStop() {
        super.onStop();
        setTopMenuBarColor(getResources().getColor(R.color.top_menu_color));
    }

    protected boolean doForceRefresh() {
        return false;
    }

    @Override // com.microsoft.kapp.event.SyncStatusListener
    public void onSyncStarted(SyncStartedEvent e) {
    }

    @Override // com.microsoft.kapp.event.SyncStatusListener
    public void onSyncCompleted(SyncCompletedEvent e) {
        Validate.notNull(e, "e");
        CargoSyncResult result = e.getSyncResult();
        if (result != null && result.isSyncSuccess()) {
            refreshData();
        }
    }

    @Override // com.microsoft.kapp.event.SyncStatusListener
    public void onSyncPreComplete(SyncCompletedEvent e) {
    }

    @Override // com.microsoft.kapp.event.SyncStatusListener
    public void onSyncProgress(int progressPercentage) {
    }

    @Override // com.microsoft.kapp.event.SyncStatusListener
    public void onSyncTerminated() {
    }

    @Override // com.microsoft.kapp.services.timeZone.TimeZoneChangeListner
    public void onTimeZoneChanged() {
    }

    private void refreshData() {
        if (isAdded()) {
            load();
            this.mLastRefreshTime = DateTime.now();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void load() {
        KLog.v(this.TAG, "On Load");
    }

    @Override // com.microsoft.kapp.activities.OnBackButtonPressedListener
    public boolean handleBackButton() {
        return false;
    }

    @Override // com.microsoft.kapp.activities.OnNavigateToFragmentListener
    public boolean handleNavigateToFragment(Class fragmentClass, boolean addToBackStack, boolean shouldToggleSlidingMenu) {
        return false;
    }

    @Override // com.microsoft.kapp.OnTaskListener
    public boolean isWaitingForResult() {
        return isAdded();
    }

    @Override // android.support.v4.app.Fragment
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (this.mLastRefreshTime != null) {
            outState.putLong(LAST_REFRESH_TIME, this.mLastRefreshTime.getMillis());
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setTopMenuBarColor(int color) {
        Activity activity = getActivity();
        if (activity instanceof TopMenuFragment.TopMenuConfiguration) {
            ((TopMenuFragment.TopMenuConfiguration) activity).setTopMenuColor(color);
        }
    }

    protected void setTopMenuDividerVisibility() {
        int visibility = getTopMenuDividerVisibility();
        Activity activity = getActivity();
        if (activity instanceof TopMenuFragment.TopMenuConfiguration) {
            ((TopMenuFragment.TopMenuConfiguration) activity).setTopMenuDividerVisibility(visibility);
        }
    }

    protected int getTopMenuDividerVisibility() {
        return 8;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean runOnUiThread(Runnable runnable) {
        Activity activity;
        if (!isAdded() || (activity = getActivity()) == null) {
            return false;
        }
        activity.runOnUiThread(runnable);
        return true;
    }
}

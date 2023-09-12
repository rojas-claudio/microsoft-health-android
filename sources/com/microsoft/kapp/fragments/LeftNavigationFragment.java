package com.microsoft.kapp.fragments;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import com.microsoft.band.internal.util.BluetoothAdapterHelper;
import com.microsoft.kapp.R;
import com.microsoft.kapp.ScopedAsyncTask;
import com.microsoft.kapp.UserProfileFetcher;
import com.microsoft.kapp.activities.DebugActivity;
import com.microsoft.kapp.activities.DialogPriority;
import com.microsoft.kapp.activities.FeedbackActivity;
import com.microsoft.kapp.activities.HomeActivity;
import com.microsoft.kapp.activities.SignInActivity;
import com.microsoft.kapp.activities.SplashActivity;
import com.microsoft.kapp.cache.CacheService;
import com.microsoft.kapp.debug.KappConfig;
import com.microsoft.kapp.diagnostics.Compatibility;
import com.microsoft.kapp.diagnostics.Telemetry;
import com.microsoft.kapp.diagnostics.TelemetryConstants;
import com.microsoft.kapp.diagnostics.Validate;
import com.microsoft.kapp.event.SyncCompletedEvent;
import com.microsoft.kapp.fragments.golf.GolfLandingPageFragment;
import com.microsoft.kapp.fragments.guidedworkout.BrowseGuidedWorkoutsFragment;
import com.microsoft.kapp.fragments.history.HistorySummaryFragment;
import com.microsoft.kapp.fragments.whatsnew.WhatsNewFragment;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.kapp.logging.http.FiddlerLogger;
import com.microsoft.kapp.models.FreStatus;
import com.microsoft.kapp.multidevice.MultiDeviceConstants;
import com.microsoft.kapp.navigations.ActivityNavigationCommandV1;
import com.microsoft.kapp.navigations.FragmentNavigationCommandV1;
import com.microsoft.kapp.navigations.NavigationCommandV1;
import com.microsoft.kapp.navigations.NavigationHeaderCommand;
import com.microsoft.kapp.navigations.NavigationManagerV1;
import com.microsoft.kapp.navigations.NavigationSeparator;
import com.microsoft.kapp.navigations.RunnableNavigationCommand;
import com.microsoft.kapp.sensor.SensorUtils;
import com.microsoft.kapp.services.SettingsProvider;
import com.microsoft.kapp.services.guidedworkout.GuidedWorkoutService;
import com.microsoft.kapp.utils.Constants;
import com.microsoft.kapp.utils.FreUtils;
import com.microsoft.kapp.utils.KAppDateFormatter;
import com.microsoft.kapp.utils.StrappUtils;
import com.microsoft.kapp.utils.ToastUtils;
import com.microsoft.kapp.utils.ViewUtils;
import com.microsoft.krestsdk.auth.MsaAuth;
import com.microsoft.krestsdk.auth.credentials.CredentialsManager;
import java.lang.ref.WeakReference;
import java.util.Iterator;
import javax.inject.Inject;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
/* loaded from: classes.dex */
public class LeftNavigationFragment extends BaseNavigationFragment {
    private static final int REQUEST_ENABLE_BT = 1;
    @Inject
    MsaAuth mAuthService;
    @Inject
    CacheService mCacheService;
    @Inject
    CredentialsManager mCredentialsManager;
    @Inject
    FiddlerLogger mFiddlerLogger;
    @Inject
    GuidedWorkoutService mGuidedWorkoutService;
    private TextView mLastSyncIconBand;
    private TextView mLastSyncIconPhone;
    private TextView mLastSyncTimeBand;
    private TextView mLastSyncTimePhone;
    private BroadcastReceiver mLeftNavNotificationBroadReceiver = new BroadcastReceiver() { // from class: com.microsoft.kapp.fragments.LeftNavigationFragment.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String targetCommandText = intent.getStringExtra(Constants.LEFTNAV_NOTIFICATION_CLEAR);
            if (!TextUtils.isEmpty(targetCommandText)) {
                Iterator i$ = LeftNavigationFragment.this.mNavigationManager.getCommands().iterator();
                while (true) {
                    if (!i$.hasNext()) {
                        break;
                    }
                    NavigationCommandV1 command = i$.next();
                    if (command.getTitle().equals(targetCommandText)) {
                        command.setNotification(false);
                        break;
                    }
                }
                LeftNavigationFragment.this.refreshNavigationList();
            }
        }
    };
    @Inject
    SensorUtils mSensorUtils;
    @Inject
    SettingsProvider mSettingsProvider;
    private TextView mSyncButton;
    private WeakReference<SyncButtonClickListener> mSyncButtonClickListenerWeakRef;
    private View mSyncHeaderView;
    @Inject
    UserProfileFetcher mUserProfileFetcher;

    /* loaded from: classes.dex */
    public interface SyncButtonClickListener {
        void onSyncButtonClick();
    }

    public static Fragment newInstance() {
        return new LeftNavigationFragment();
    }

    @Override // com.microsoft.kapp.fragments.BaseNavigationFragment, android.support.v4.app.Fragment
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof SyncButtonClickListener) {
            setSyncButtonClickListenerWeakRef((SyncButtonClickListener) activity);
        }
    }

    @Override // com.microsoft.kapp.fragments.BaseNavigationFragment, android.support.v4.app.Fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_left_navigation, container, false);
        Validate.notNull(rootView, "rootView");
        this.mNavigationList = (ListView) ListView.class.cast(rootView.findViewById(R.id.left_nav_list));
        this.mSyncHeaderView = inflater.inflate(R.layout.navigation_drawer_header, (ViewGroup) null);
        this.mNavigationList.addHeaderView(this.mSyncHeaderView, null, false);
        this.mLastSyncTimePhone = (TextView) ViewUtils.getValidView(rootView, R.id.last_sync_time_phone, TextView.class);
        this.mLastSyncTimeBand = (TextView) ViewUtils.getValidView(rootView, R.id.last_sync_time_band, TextView.class);
        this.mLastSyncIconPhone = (TextView) ViewUtils.getValidView(rootView, R.id.ic_phone, TextView.class);
        this.mLastSyncIconBand = (TextView) ViewUtils.getValidView(rootView, R.id.ic_band, TextView.class);
        this.mSyncButton = (TextView) ViewUtils.getValidView(this.mNavigationList, R.id.sync_button, TextView.class);
        this.mSyncButton.setOnClickListener(new View.OnClickListener() { // from class: com.microsoft.kapp.fragments.LeftNavigationFragment.2
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                if (!LeftNavigationFragment.this.mMultiDeviceManager.hasBand() || BluetoothAdapterHelper.isBluetoothEnabled()) {
                    LeftNavigationFragment.this.startManualSync();
                    return;
                }
                Intent enableIntent = new Intent("android.bluetooth.adapter.action.REQUEST_ENABLE");
                LeftNavigationFragment.this.startActivityForResult(enableIntent, 1);
            }
        });
        return rootView;
    }

    @Override // com.microsoft.kapp.fragments.BaseFragment, android.support.v4.app.Fragment
    public void onResume() {
        super.onResume();
        updateLastSyncTimeText();
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(this.mLeftNavNotificationBroadReceiver, new IntentFilter(Constants.LEFTNAV_NOTIFICATION_BROADCAST_FILTER));
    }

    @Override // com.microsoft.kapp.fragments.BaseFragment, android.support.v4.app.Fragment
    public void onPause() {
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(this.mLeftNavNotificationBroadReceiver);
        super.onPause();
    }

    @Override // com.microsoft.kapp.fragments.BaseFragment, com.microsoft.kapp.event.SyncStatusListener
    public void onSyncCompleted(SyncCompletedEvent e) {
        super.onSyncCompleted(e);
        updateLastSyncTimeText();
    }

    @Override // com.microsoft.kapp.fragments.BaseNavigationFragment
    protected NavigationManagerV1 createNavigationManager() {
        NavigationManagerV1 manager = new NavigationManagerV1((FragmentActivity) this.mContext);
        Bundle args = new Bundle();
        args.putString(BaseFragment.ARG_REFERRER, TelemetryConstants.PageViews.Referrers.LEFT_NAV);
        manager.addNavigationCommand(new FragmentNavigationCommandV1(getActivity(), R.string.navigation_item_title_home, R.string.glyph_home, true, HomeTilesFragment.class));
        manager.addNavigationCommand(new FragmentNavigationCommandV1(getActivity(), R.string.navigation_item_title_history, R.string.glyph_bing_sports, true, HistorySummaryFragment.class));
        manager.addNavigationCommand(new FragmentNavigationCommandV1(getActivity(), R.string.navigation_item_title_profile, R.string.glyph_profile, true, SettingsProfileFragment.class));
        manager.addNavigationCommand(new FragmentNavigationCommandV1(getActivity(), R.string.navigation_item_title_workouts, R.string.glyph_guided_workout, true, BrowseGuidedWorkoutsFragment.class));
        manager.addNavigationCommand(new FragmentNavigationCommandV1((Context) getActivity(), (int) R.string.navigation_item_title_find_a_golf_course, (int) R.string.glyph_longest_drive, true, GolfLandingPageFragment.class, args));
        manager.addNavigationCommand(new FragmentNavigationCommandV1((Context) getActivity(), (int) R.string.navigation_item_title_what_s_new, (int) R.string.glyph_messaging, true, WhatsNewFragment.class, false, this.mSettingsProvider.isNotificationWhatsNewEnabled()));
        manager.addNavigationCommand(new RunnableNavigationCommand(getActivity(), R.string.navigation_item_title_connected_apps, R.string.glyph_molecule, new Runnable() { // from class: com.microsoft.kapp.fragments.LeftNavigationFragment.3
            @Override // java.lang.Runnable
            public void run() {
                String thirdPartyPartnersPortalEndpoint = LeftNavigationFragment.this.mSettingsProvider.getThirdPartyPartnersPortalEndpoint();
                boolean launchingBrowserSucceeded = false;
                if (thirdPartyPartnersPortalEndpoint != null) {
                    try {
                        Intent viewIntent = new Intent("android.intent.action.VIEW", Uri.parse(thirdPartyPartnersPortalEndpoint));
                        LeftNavigationFragment.this.mContext.startActivity(viewIntent);
                        launchingBrowserSucceeded = true;
                    } catch (Exception ex) {
                        KLog.e(LeftNavigationFragment.this.TAG, "Launching the browser failed", ex);
                    }
                }
                if (!launchingBrowserSucceeded) {
                    LeftNavigationFragment.this.mUserProfileFetcher.updateLocallyStoredValuesAsync();
                }
            }
        }));
        manager.addNavigationCommand(new NavigationHeaderCommand(getActivity(), R.string.navigation_header_band_settings));
        if (this.mMultiDeviceManager.hasBand()) {
            manager.addNavigationCommand(new FragmentNavigationCommandV1(getActivity(), R.string.navigation_item_title_band, R.string.glyph_band_neon, true, SettingsMyBandFragment.class));
            manager.addNavigationCommand(new FragmentNavigationCommandV1((Context) getActivity(), (int) R.string.navigation_item_title_personalize_band, (int) R.string.glyph_edit, true, SettingsPersonalizeFragment.class, true));
            manager.addNavigationCommand(new FragmentNavigationCommandV1((Context) getActivity(), (int) R.string.navigation_item_title_strapps, (int) R.string.glyph_manage_tiles, true, ManageTilesFragment.class, true));
        }
        manager.addNavigationCommand(new RunnableNavigationCommand(getActivity(), this.mMultiDeviceManager.hasBand() ? R.string.navigation_item_title_connect_another_band : R.string.navigation_item_title_connect_band, this.mMultiDeviceManager.hasBand() ? R.string.glyph_gototoday : R.string.glyph_plus, new Runnable() { // from class: com.microsoft.kapp.fragments.LeftNavigationFragment.4
            @Override // java.lang.Runnable
            public void run() {
                if (LeftNavigationFragment.this.mMultiDeviceManager.hasBand()) {
                    LeftNavigationFragment.this.getDialogManager().showDialog(LeftNavigationFragment.this.getActivity(), Integer.valueOf((int) R.string.add_another_band_dialog_header), Integer.valueOf((int) R.string.add_another_band_dialog_text), new DialogInterface.OnClickListener() { // from class: com.microsoft.kapp.fragments.LeftNavigationFragment.4.1
                        @Override // android.content.DialogInterface.OnClickListener
                        public void onClick(DialogInterface dialog, int which) {
                            FreUtils.devicePairingRedirect(LeftNavigationFragment.this.getActivity(), LeftNavigationFragment.this.mSettingsProvider, false);
                        }
                    }, (DialogInterface.OnClickListener) null, DialogPriority.LOW);
                } else {
                    FreUtils.devicePairingRedirect(LeftNavigationFragment.this.getActivity(), LeftNavigationFragment.this.mSettingsProvider, false);
                }
            }
        }));
        if (this.mSensorUtils.isKitkatWithStepSensor()) {
            manager.addNavigationCommand(new FragmentNavigationCommandV1(getActivity().getString(R.string.navigation_item_title_my_phone), R.string.glyph_smartphone, true, MotionSettingsFragment.class));
        }
        manager.addNavigationCommand(new NavigationHeaderCommand(getActivity(), R.string.navigation_header_settings));
        manager.addNavigationCommand(new FragmentNavigationCommandV1(getActivity(), R.string.navigation_item_title_preference, R.string.glyph_settings, true, SettingsPreferencesFragment.class));
        manager.addNavigationCommand(new FragmentNavigationCommandV1(getActivity(), R.string.navigation_item_title_about, R.string.glyph_tooltip, true, AboutFragment.class));
        manager.addNavigationCommand(new ActivityNavigationCommandV1(getActivity(), R.string.navigation_item_title_feedback, R.string.glyph_help_feedback, FeedbackActivity.class));
        manager.addNavigationCommand(new RunnableNavigationCommand(getActivity(), R.string.navigation_item_title_log_out, R.string.glyph_power, new Runnable() { // from class: com.microsoft.kapp.fragments.LeftNavigationFragment.5
            @Override // java.lang.Runnable
            public void run() {
                LeftNavigationFragment.this.logout();
            }
        }));
        if (!Compatibility.isPublicRelease() && KappConfig.isDebbuging) {
            manager.addNavigationCommand(new NavigationSeparator());
            manager.addNavigationCommand(new ActivityNavigationCommandV1(getActivity(), R.string.navigation_item_title_debug, R.string.glyph_reminders, DebugActivity.class));
            manager.addNavigationCommand(new RunnableNavigationCommand(getActivity(), R.string.navigation_item_title_reset_oobe, R.string.glyph_auto_time, new Runnable() { // from class: com.microsoft.kapp.fragments.LeftNavigationFragment.6
                @Override // java.lang.Runnable
                public void run() {
                    ScopedAsyncTask<Void, Void, Void> task = new ScopedAsyncTask<Void, Void, Void>(LeftNavigationFragment.this) { // from class: com.microsoft.kapp.fragments.LeftNavigationFragment.6.1
                        private Exception mException;

                        /* JADX INFO: Access modifiers changed from: protected */
                        @Override // com.microsoft.kapp.ScopedAsyncTask
                        public void onPreExecute() {
                            ToastUtils.showShortToast(LeftNavigationFragment.this.getActivity(), (int) R.string.debug_message_reset_oobe_in_progress);
                        }

                        /* JADX INFO: Access modifiers changed from: protected */
                        @Override // com.microsoft.kapp.ScopedAsyncTask
                        public Void doInBackground(Void... params) {
                            try {
                                LeftNavigationFragment.this.mCargoConnection.resetOOBEComplete();
                                return null;
                            } catch (Exception ex) {
                                this.mException = ex;
                                return null;
                            }
                        }

                        /* JADX INFO: Access modifiers changed from: protected */
                        @Override // com.microsoft.kapp.ScopedAsyncTask
                        public void onPostExecute(Void result) {
                            Activity activity = LeftNavigationFragment.this.getActivity();
                            if (Validate.isActivityAlive(activity)) {
                                if (this.mException != null) {
                                    ToastUtils.showLongToast(activity, this.mException.getMessage());
                                    return;
                                }
                                LeftNavigationFragment.this.mSettingsProvider.setIsDeviceConnectionFlow(false);
                                LeftNavigationFragment.this.mSettingsProvider.setFreStatus(FreStatus.NOT_SHOWN);
                                activity.deleteFile(Constants.CREDENTIALS_FILE);
                                LeftNavigationFragment.this.mCacheService.handleLogout();
                                Intent freIntent = new Intent(activity, SplashActivity.class);
                                freIntent.putExtra(Constants.MINI_OOBE_FLAG, true);
                                LeftNavigationFragment.this.startActivity(freIntent);
                            }
                        }
                    };
                    task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
                }
            }));
        }
        return manager;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void logout() {
        final Activity activity = getActivity();
        if (activity != null && Validate.isActivityAlive(activity)) {
            getDialogManager().showDialog(activity, Integer.valueOf((int) R.string.navigation_sign_out_message), (Integer) null, new DialogInterface.OnClickListener() { // from class: com.microsoft.kapp.fragments.LeftNavigationFragment.7
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialog, int which) {
                    LeftNavigationFragment.this.mCredentialsManager.deleteCredentials(activity);
                    LeftNavigationFragment.this.mCacheService.handleLogout();
                    LeftNavigationFragment.this.mSettingsProvider.handleLogout();
                    LeftNavigationFragment.this.mGuidedWorkoutService.clearAllData();
                    LeftNavigationFragment.this.mFiddlerLogger.cleanup();
                    Telemetry.removeUserId();
                    if (Validate.isActivityAlive(activity)) {
                        activity.startActivity(new Intent(activity, SignInActivity.class));
                        activity.finish();
                    }
                }
            }, (DialogInterface.OnClickListener) null, DialogPriority.HIGH);
        }
    }

    public void setSyncButtonClickListenerWeakRef(SyncButtonClickListener syncButtonClickListener) {
        if (syncButtonClickListener == null) {
            this.mSyncButtonClickListenerWeakRef = null;
        } else {
            this.mSyncButtonClickListenerWeakRef = new WeakReference<>(syncButtonClickListener);
        }
    }

    private void updateLastSyncTimeText() {
        if (isAdded()) {
            boolean showPhoneSync = this.mSensorUtils.isKitkatWithStepSensor() && this.mSettingsProvider.isSensorLoggingEnabled();
            boolean hasBand = this.mMultiDeviceManager.hasBand();
            this.mLastSyncTimePhone.setVisibility(showPhoneSync ? 0 : 8);
            this.mLastSyncIconPhone.setVisibility(showPhoneSync ? 0 : 8);
            this.mLastSyncTimeBand.setVisibility(hasBand ? 0 : 8);
            this.mLastSyncIconBand.setVisibility(hasBand ? 0 : 8);
            String lastSyncTimeStringBand = getLastSyncTime(MultiDeviceConstants.DeviceType.BAND);
            this.mLastSyncTimeBand.setText(lastSyncTimeStringBand);
            String lastSyncTimeStringPhone = getLastSyncTime(MultiDeviceConstants.DeviceType.PHONE);
            this.mLastSyncTimePhone.setText(lastSyncTimeStringPhone);
        }
    }

    private String getLastSyncTime(MultiDeviceConstants.DeviceType deviceType) {
        String lastSyncFormatted;
        DateTime lastSyncTime = this.mMultiDeviceManager.getLastSyncTime(deviceType);
        DateTime today = DateTime.now();
        Context context = getActivity();
        boolean is24Hour = context == null ? false : DateFormat.is24HourFormat(context);
        if (lastSyncTime != null) {
            if (today.getDayOfYear() == lastSyncTime.getDayOfYear()) {
                int formatId = is24Hour ? R.string.last_sync_time_format_today_24h : R.string.last_sync_time_format_today;
                lastSyncFormatted = DateTimeFormat.forPattern(getString(formatId)).print(lastSyncTime);
            } else {
                lastSyncFormatted = KAppDateFormatter.formatToMonthDay(lastSyncTime);
            }
            String lastSyncTimeString = lastSyncFormatted;
            return lastSyncTimeString;
        }
        String lastSyncTimeString2 = getString(R.string.no_value);
        return lastSyncTimeString2;
    }

    @Override // android.support.v4.app.Fragment
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        startManualSync();
        super.onActivityResult(requestCode, resultCode, data);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startManualSync() {
        SyncButtonClickListener syncButtonClickListener;
        StrappUtils.clearStrappAndCalendarCacheData(this.mSettingsProvider, this.mCargoConnection);
        this.mMultiDeviceManager.startSync();
        ((HomeActivity) HomeActivity.class.cast(getActivity())).onLeftNavClick();
        if (this.mSyncButtonClickListenerWeakRef != null && (syncButtonClickListener = this.mSyncButtonClickListenerWeakRef.get()) != null) {
            syncButtonClickListener.onSyncButtonClick();
        }
    }
}

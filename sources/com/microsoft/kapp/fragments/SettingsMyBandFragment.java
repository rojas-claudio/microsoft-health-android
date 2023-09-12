package com.microsoft.kapp.fragments;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.microsoft.band.client.CargoException;
import com.microsoft.kapp.CargoConnection;
import com.microsoft.kapp.CargoExtensions;
import com.microsoft.kapp.DeviceErrorState;
import com.microsoft.kapp.R;
import com.microsoft.kapp.ScopedAsyncTask;
import com.microsoft.kapp.activities.DialogPriority;
import com.microsoft.kapp.activities.HomeActivity;
import com.microsoft.kapp.activities.OobeFirmwareUpdateActivity;
import com.microsoft.kapp.diagnostics.Telemetry;
import com.microsoft.kapp.diagnostics.TelemetryConstants;
import com.microsoft.kapp.diagnostics.Validate;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.kapp.multidevice.SingleDeviceEnforcementManager;
import com.microsoft.kapp.navigations.FragmentNavigationCommandV1;
import com.microsoft.kapp.personalization.DeviceTheme;
import com.microsoft.kapp.personalization.PersonalizationManagerFactory;
import com.microsoft.kapp.services.SettingsProvider;
import com.microsoft.kapp.utils.ActivityUtils;
import com.microsoft.kapp.utils.CommonUtils;
import com.microsoft.kapp.utils.SettingsUtils;
import com.microsoft.kapp.utils.ViewUtils;
import com.microsoft.kapp.version.CheckedFirmwareUpdateInfo;
import com.microsoft.kapp.widgets.ConfirmationBar;
import com.microsoft.kapp.widgets.Interstitial;
import com.microsoft.krestsdk.models.BandVersion;
import java.lang.ref.WeakReference;
import javax.inject.Inject;
/* loaded from: classes.dex */
public class SettingsMyBandFragment extends BaseFragment {
    private Button mApplyUpdateButton;
    private View mBandContainer;
    private TextView mBandFirmwareVersion;
    private String mBandName;
    private EditText mBandNameEditText;
    private final TextWatcher mBandNameWatcher = new TextWatcher() { // from class: com.microsoft.kapp.fragments.SettingsMyBandFragment.1
        @Override // android.text.TextWatcher
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override // android.text.TextWatcher
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override // android.text.TextWatcher
        public void afterTextChanged(Editable s) {
            SettingsMyBandFragment.this.mConfirmationBar.setVisibility(0);
        }
    };
    private TextView mBandSerialNo;
    private BandVersion mBandVersion;
    private int mBandWallpaperThemeId;
    private RelativeLayout mBatteryLayout;
    private ProgressBar mBatteryLevelBar;
    private View mCargoBackground;
    private View mCargoImage;
    private ConfirmationBar mConfirmationBar;
    private ImageView mDeviceImageDisplay;
    private Interstitial mInterstitialScreen;
    private int mMinBandNameLength;
    private TextView mMyBandErrorMessage;
    private View mNeonBackground;
    private ImageView mNeonDeviceImageDisplay;
    private View mNeonImage;
    @Inject
    PersonalizationManagerFactory mPersonalizationManagerFactory;
    @Inject
    SettingsProvider mSettingsProvider;
    @Inject
    SingleDeviceEnforcementManager mSingleDeviceEnforcementManager;
    private Button mUnregisterBandButton;

    @Override // android.support.v4.app.Fragment
    public void onStart() {
        super.onStart();
    }

    private void updateDeviceRepresentation() {
        DeviceTheme deviceTheme = this.mPersonalizationManagerFactory.getPersonalizationManager(this.mBandVersion).getThemeById(this.mBandWallpaperThemeId);
        int wallpaperResId = deviceTheme.getWallpaperById(this.mBandWallpaperThemeId).getResId();
        if (this.mBandVersion == BandVersion.NEON) {
            this.mNeonImage.setVisibility(0);
            this.mCargoImage.setVisibility(4);
            this.mNeonBackground.setVisibility(0);
            this.mCargoBackground.setVisibility(4);
            this.mNeonDeviceImageDisplay.setImageResource(wallpaperResId);
        } else if (this.mBandVersion == BandVersion.CARGO) {
            this.mNeonImage.setVisibility(4);
            this.mCargoImage.setVisibility(0);
            this.mNeonBackground.setVisibility(4);
            this.mCargoBackground.setVisibility(0);
            this.mDeviceImageDisplay.setImageResource(wallpaperResId);
        } else {
            this.mNeonImage.setVisibility(4);
            this.mCargoImage.setVisibility(4);
            this.mNeonBackground.setVisibility(4);
            this.mCargoBackground.setVisibility(4);
        }
    }

    @Override // android.support.v4.app.Fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.settings_my_band_fragment, container, false);
        this.mBandVersion = BandVersion.values()[getActivity().getIntent().getIntExtra(PersonalizationManagerFactory.BAND_VERSION_ID, 1)];
        this.mBandNameEditText = (EditText) ViewUtils.getValidView(rootView, R.id.my_band_name, EditText.class);
        this.mBandSerialNo = (TextView) ViewUtils.getValidView(rootView, R.id.my_band_serial_no, TextView.class);
        this.mApplyUpdateButton = (Button) ViewUtils.getValidView(rootView, R.id.firmware_update_button, Button.class);
        this.mBandFirmwareVersion = (TextView) ViewUtils.getValidView(rootView, R.id.my_band_firmware_version, TextView.class);
        this.mMyBandErrorMessage = (TextView) ViewUtils.getValidView(rootView, R.id.settings_my_band_error_message, TextView.class);
        this.mConfirmationBar = (ConfirmationBar) ViewUtils.getValidView(rootView, R.id.confirmation_bar, ConfirmationBar.class);
        this.mUnregisterBandButton = (Button) ViewUtils.getValidView(rootView, R.id.my_band_unregister_button, Button.class);
        this.mDeviceImageDisplay = (ImageView) ViewUtils.getValidView(rootView, R.id.device_representation_background, View.class);
        this.mNeonDeviceImageDisplay = (ImageView) rootView.findViewById(R.id.neon_device_representation_background);
        this.mInterstitialScreen = (Interstitial) ViewUtils.getValidView(rootView, R.id.settings_my_band_sync_indicator, Interstitial.class);
        this.mBatteryLevelBar = (ProgressBar) ViewUtils.getValidView(rootView, R.id.battery_level_bar, ProgressBar.class);
        this.mBatteryLayout = (RelativeLayout) ViewUtils.getValidView(rootView, R.id.battery_layout, RelativeLayout.class);
        this.mCargoImage = (View) ViewUtils.getValidView(rootView, R.id.device_representation_image, View.class);
        this.mNeonImage = (View) ViewUtils.getValidView(rootView, R.id.neon_device_representation_image, View.class);
        this.mCargoBackground = (View) ViewUtils.getValidView(rootView, R.id.device_representation_background, View.class);
        this.mNeonBackground = (View) ViewUtils.getValidView(rootView, R.id.neon_device_representation_background, View.class);
        this.mBandContainer = (View) ViewUtils.getValidView(rootView, R.id.device_representation_container, View.class);
        this.mMinBandNameLength = getResources().getInteger(R.integer.min_band_name_length);
        this.mBatteryLevelBar.setMax(getResources().getInteger(R.integer.max_battery_level));
        updatingElementMessage(this.mBandSerialNo, R.string.settings_my_band_serial_no);
        updatingElementMessage(this.mBandFirmwareVersion, R.string.settings_my_band_firmware);
        this.mInterstitialScreen.setBackgroundResource(R.color.settingsBackground);
        this.mApplyUpdateButton.setOnClickListener(new View.OnClickListener() { // from class: com.microsoft.kapp.fragments.SettingsMyBandFragment.2
            @Override // android.view.View.OnClickListener
            public void onClick(View arg0) {
                SettingsMyBandFragment.this.startFirmwareUpdateActivity();
            }
        });
        this.mUnregisterBandButton.setOnClickListener(new View.OnClickListener() { // from class: com.microsoft.kapp.fragments.SettingsMyBandFragment.3
            @Override // android.view.View.OnClickListener
            public void onClick(View arg0) {
                SettingsMyBandFragment.this.unregisterBand();
            }
        });
        this.mConfirmationBar.setOnConfirmButtonClickListener(new View.OnClickListener() { // from class: com.microsoft.kapp.fragments.SettingsMyBandFragment.4
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                if (SettingsUtils.validateDeviceName(SettingsMyBandFragment.this.mBandNameEditText, SettingsMyBandFragment.this.mMinBandNameLength)) {
                    SettingsMyBandFragment.this.mMyBandErrorMessage.setVisibility(4);
                    SettingsMyBandFragment.this.setBandName();
                    return;
                }
                SettingsMyBandFragment.this.mMyBandErrorMessage.setVisibility(0);
            }
        });
        this.mConfirmationBar.setOnCancelButtonClickListener(new View.OnClickListener() { // from class: com.microsoft.kapp.fragments.SettingsMyBandFragment.5
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                SettingsMyBandFragment.this.mBandNameEditText.setText(SettingsMyBandFragment.this.mBandName);
                SettingsMyBandFragment.this.mConfirmationBar.setVisibility(8);
                SettingsMyBandFragment.this.clearBandNameErrorState();
            }
        });
        getBandInfo();
        return rootView;
    }

    @Override // com.microsoft.kapp.fragments.BaseFragment, android.support.v4.app.Fragment
    public void onResume() {
        super.onResume();
        Telemetry.logPage(TelemetryConstants.PageViews.SETTINGS_BAND_MY_BAND);
        setTopMenuBarColor(getResources().getColor(R.color.settingsMenuBar));
    }

    @Override // com.microsoft.kapp.fragments.BaseFragment, com.microsoft.kapp.activities.OnNavigateToFragmentListener
    public boolean handleNavigateToFragment(Class fragmentClass, boolean addToBackStack, boolean shouldToggleSlidingMenu) {
        if (fragmentClass == null || fragmentClass.equals(SettingsMyBandFragment.class) || !isAdded() || this.mConfirmationBar.getVisibility() != 0) {
            return false;
        }
        showPendingChangesDialog(false, fragmentClass, addToBackStack);
        return true;
    }

    @Override // com.microsoft.kapp.fragments.BaseFragment, com.microsoft.kapp.activities.OnBackButtonPressedListener
    public boolean handleBackButton() {
        if (isAdded() && this.mConfirmationBar.getVisibility() == 0) {
            showPendingChangesDialog(true, null, false);
            return true;
        }
        return false;
    }

    private void showPendingChangesDialog(final boolean exitBackPressed, final Class fragmentClass, final boolean addToBackStack) {
        getDialogManager().showDialog(getActivity(), Integer.valueOf((int) R.string.strapps_lose_changes_confirmation_title), Integer.valueOf((int) R.string.strapps_lose_changes_confirmation_message), R.string.strapps_button_continue, new DialogInterface.OnClickListener() { // from class: com.microsoft.kapp.fragments.SettingsMyBandFragment.6
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) {
                SettingsMyBandFragment.this.mBandNameEditText.setText(SettingsMyBandFragment.this.mBandName);
                SettingsMyBandFragment.this.mConfirmationBar.setVisibility(8);
                SettingsMyBandFragment.this.clearBandNameErrorState();
                Activity activity = SettingsMyBandFragment.this.getActivity();
                if (Validate.isActivityAlive(activity)) {
                    if (exitBackPressed) {
                        ActivityUtils.performBackButton(activity);
                        return;
                    } else if (fragmentClass != null && (activity instanceof FragmentNavigationCommandV1.FragmentNavigationListener)) {
                        ((FragmentNavigationCommandV1.FragmentNavigationListener) activity).navigateToFragment(fragmentClass, null, addToBackStack, false);
                        return;
                    } else {
                        return;
                    }
                }
                KLog.w(SettingsMyBandFragment.this.TAG, "Activity is no longer alive.");
            }
        }, R.string.strapps_button_cancel, null, DialogPriority.HIGH);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void unregisterBand() {
        if (!CommonUtils.isNetworkAvailable(getActivity())) {
            getDialogManager().showNetworkErrorDialog(getActivity());
        } else {
            getDialogManager().showDialog(getActivity(), Integer.valueOf((int) R.string.app_name), Integer.valueOf((int) R.string.single_device_unregister_dialog_text), R.string.single_device_unregister_continue, new DialogInterface.OnClickListener() { // from class: com.microsoft.kapp.fragments.SettingsMyBandFragment.7
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialog, int which) {
                    UnlinkDeviceTask retrieveTask = new UnlinkDeviceTask(SettingsMyBandFragment.this.mCargoConnection, SettingsMyBandFragment.this.getActivity(), SettingsMyBandFragment.this, SettingsMyBandFragment.this.mSettingsProvider);
                    retrieveTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
                }
            }, (DialogInterface.OnClickListener) null, DialogPriority.HIGH);
        }
    }

    public void getBandInfo() {
        CheckedFirmwareUpdateInfo cachedFirmwareUpdateInfo = this.mSettingsProvider.getCheckedFirmwareUpdateInfo();
        if (cachedFirmwareUpdateInfo != null && cachedFirmwareUpdateInfo.isUpdateNeeded() && cachedFirmwareUpdateInfo.isIsUpdateOptional()) {
            this.mApplyUpdateButton.setVisibility(0);
        } else {
            this.mApplyUpdateButton.setVisibility(8);
        }
        MyBandSettingsRetrieveTask retrieveTask = new MyBandSettingsRetrieveTask(this.mCargoConnection, this);
        retrieveTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startFirmwareUpdateActivity() {
        Activity currentActivity = getActivity();
        if (Validate.isActivityAlive(currentActivity)) {
            Intent intent = new Intent(getActivity(), OobeFirmwareUpdateActivity.class);
            intent.putExtra(OobeFirmwareUpdateActivity.ARG_IN_EXTRA_IS_IN_APP_UPDATE, true);
            intent.putExtra(OobeFirmwareUpdateActivity.ARG_IN_EXTRA_IS_OPTIONAL_APP_UPDATE, true);
            intent.putExtra(PersonalizationManagerFactory.BAND_VERSION_ID, this.mBandVersion.ordinal());
            currentActivity.startActivity(intent);
            this.mLastRefreshTime = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setBandName() {
        this.mBandName = this.mBandNameEditText.getText().toString();
        BandNameSettingsSaveTask saveTask = new BandNameSettingsSaveTask(this.mCargoConnection, this.mBandName, this);
        saveTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void changeLoadingScreen(int interstitialScreen) {
        this.mInterstitialScreen.setSlide(interstitialScreen);
        this.mConfirmationBar.setVisibility(8);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void clearBandNameErrorState() {
        this.mMyBandErrorMessage.setVisibility(4);
        this.mBandNameEditText.setBackgroundResource(R.color.device_name_background);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void hideUnregister() {
        this.mUnregisterBandButton.setVisibility(8);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onSaveBandNameSucced() {
        this.mInterstitialScreen.setSlide(Interstitial.SLIDE_GONE);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onSaveBandNameFailed(Exception exception) {
        this.mInterstitialScreen.setSlide(Interstitial.SLIDE_GONE);
        this.mConfirmationBar.setVisibility(0);
        boolean isCloudError = false;
        int errorMessageId = R.string.band_save_failed_message;
        if (exception instanceof CargoException) {
            CargoException cargoException = (CargoException) exception;
            if (cargoException.getResponse().getCategory() == 4) {
                isCloudError = true;
                errorMessageId = R.string.profile_saving_cloud_error;
            }
        }
        if (!CommonUtils.isNetworkAvailable(getActivity())) {
            getDialogManager().showNetworkErrorDialog(getActivity());
        } else {
            getDialogManager().showDialog(getActivity(), Integer.valueOf(isCloudError ? R.string.connection_with_cloud_error_title : R.string.connection_with_device_error_title), Integer.valueOf(errorMessageId), DialogPriority.LOW);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onSettingsFromBandRetrievedSuccessfully(MyBandSettings result) {
        this.mInterstitialScreen.setSlide(Interstitial.SLIDE_GONE);
        this.mBandVersion = result.bandVersion;
        this.mBandName = result.name;
        clearBandNameErrorState();
        this.mBandNameEditText.setText(result.name);
        this.mBandNameEditText.addTextChangedListener(this.mBandNameWatcher);
        this.mBandNameEditText.setEnabled(true);
        this.mBandSerialNo.setText(String.format(getResources().getString(R.string.settings_my_band_serial_no), result.serialNo));
        this.mBandFirmwareVersion.setText(String.format(getResources().getString(R.string.settings_my_band_firmware), result.firmwareVersion));
        this.mBatteryLevelBar.setProgress(result.batteryLevel);
        this.mBandWallpaperThemeId = result.currentWallpaperThemeID;
        this.mBatteryLayout.setVisibility(0);
        this.mConfirmationBar.setVisibility(8);
        updateDeviceRepresentation();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onSettingsFromBandRetrievedFailed(Exception exception) {
        this.mInterstitialScreen.setSlide(Interstitial.SLIDE_GONE);
        if ((exception instanceof CargoExtensions.SingleDeviceCheckFailedException) && ((CargoExtensions.SingleDeviceCheckFailedException) exception).getDeviceErrorState() == DeviceErrorState.MULTIPLE_DEVICES_BONDED) {
            getDialogManager().showMultipleDevicesConnectedError(getActivity());
        } else {
            getDialogManager().showDialog(getActivity(), Integer.valueOf((int) R.string.connection_with_device_error_title), Integer.valueOf((int) R.string.band_retrieve_failed_message), DialogPriority.HIGH);
        }
        onBandNotFound();
    }

    private void updatingElementMessage(TextView elementText, int elementResId) {
        String updatingMessage = String.format(getResources().getString(elementResId), getResources().getString(R.string.updating_element));
        elementText.setText(updatingMessage);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class MyBandSettings {
        public BandVersion bandVersion;
        public int batteryLevel;
        public int currentWallpaperThemeID;
        public String firmwareVersion;
        public String name;
        public String serialNo;

        private MyBandSettings() {
        }
    }

    /* loaded from: classes.dex */
    private static class UnlinkDeviceTask extends ScopedAsyncTask<Void, Void, Boolean> {
        private CargoConnection mCargoConnection;
        private SettingsProvider mSettingsProvider;
        private WeakReference<Activity> mWeakActivity;
        private WeakReference<SettingsMyBandFragment> mWeakFragment;

        public UnlinkDeviceTask(CargoConnection cargoConnection, Activity activity, SettingsMyBandFragment onTaskListener, SettingsProvider settingsProvider) {
            super(onTaskListener);
            this.mCargoConnection = cargoConnection;
            this.mSettingsProvider = settingsProvider;
            this.mWeakActivity = new WeakReference<>(activity);
            this.mWeakFragment = new WeakReference<>(onTaskListener);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.microsoft.kapp.ScopedAsyncTask
        public Boolean doInBackground(Void... params) {
            try {
                this.mCargoConnection.unlinkDeviceToProfile();
                return null;
            } catch (CargoException ex) {
                setException(ex);
                return null;
            }
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.microsoft.kapp.ScopedAsyncTask
        public void onPostExecute(Boolean result) {
            SettingsMyBandFragment fragment;
            Activity activity = this.mWeakActivity.get();
            if (Validate.isActivityAlive(activity) && (fragment = this.mWeakFragment.get()) != null) {
                if (getException() != null) {
                    fragment.getDialogManager().showNetworkErrorDialog(activity);
                    return;
                }
                this.mSettingsProvider.clearSettingsWhileDeviceUnregister();
                fragment.hideUnregister();
                activity.finish();
                Intent intent = new Intent(activity, HomeActivity.class);
                intent.addFlags(32768);
                intent.addFlags(268435456);
                activity.startActivity(intent);
            }
        }
    }

    /* loaded from: classes.dex */
    public static class MyBandSettingsRetrieveTask extends ScopedAsyncTask<Void, Void, MyBandSettings> {
        private final String TASK_TAG;
        private CargoConnection mCargoConnection;
        private WeakReference<SettingsMyBandFragment> mWeakFragment;

        public MyBandSettingsRetrieveTask(CargoConnection cargoConnection, SettingsMyBandFragment onTaskListener) {
            super(onTaskListener);
            this.TASK_TAG = MyBandSettingsRetrieveTask.class.getSimpleName();
            this.mCargoConnection = cargoConnection;
            this.mWeakFragment = new WeakReference<>(onTaskListener);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.microsoft.kapp.ScopedAsyncTask
        public void onPreExecute() {
            super.onPreExecute();
            SettingsMyBandFragment fragment = this.mWeakFragment.get();
            if (fragment != null) {
                fragment.changeLoadingScreen(Interstitial.SLIDE_GETTING_BAND_INFO);
            }
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.microsoft.kapp.ScopedAsyncTask
        public MyBandSettings doInBackground(Void... params) {
            try {
                MyBandSettings bandSettings = new MyBandSettings();
                bandSettings.bandVersion = this.mCargoConnection.getBandVersion();
                bandSettings.name = this.mCargoConnection.getDeviceName();
                bandSettings.firmwareVersion = this.mCargoConnection.getDeviceFirmwareVersion().toString();
                bandSettings.serialNo = this.mCargoConnection.getDeviceSerialNo();
                bandSettings.batteryLevel = this.mCargoConnection.getDeviceBatteryLevel();
                bandSettings.currentWallpaperThemeID = this.mCargoConnection.getDeviceWallpaperId();
                return bandSettings;
            } catch (Exception exception) {
                KLog.d(this.TASK_TAG, "error retrieving band settings", exception);
                setException(exception);
                return null;
            }
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.microsoft.kapp.ScopedAsyncTask
        public void onPostExecute(MyBandSettings result) {
            SettingsMyBandFragment fragment = this.mWeakFragment.get();
            if (fragment != null) {
                if (getException() == null) {
                    fragment.onSettingsFromBandRetrievedSuccessfully(result);
                } else {
                    fragment.onSettingsFromBandRetrievedFailed(getException());
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class BandNameSettingsSaveTask extends ScopedAsyncTask<Void, Void, Boolean> {
        private final String TASK_TAG;
        private String mBandName;
        private CargoConnection mCargoConnection;
        private WeakReference<SettingsMyBandFragment> mWeakFragment;

        public BandNameSettingsSaveTask(CargoConnection cargoConnection, String bandName, SettingsMyBandFragment onTaskListener) {
            super(onTaskListener);
            this.TASK_TAG = BandNameSettingsSaveTask.class.getSimpleName();
            this.mCargoConnection = cargoConnection;
            this.mBandName = bandName;
            this.mWeakFragment = new WeakReference<>(onTaskListener);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.microsoft.kapp.ScopedAsyncTask
        public void onPreExecute() {
            super.onPreExecute();
            SettingsMyBandFragment fragment = this.mWeakFragment.get();
            if (fragment != null) {
                fragment.changeLoadingScreen(Interstitial.SLIDE_UPDATING);
            }
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.microsoft.kapp.ScopedAsyncTask
        public Boolean doInBackground(Void... params) {
            KLog.v(this.TASK_TAG, "Executing BandNameSave Task");
            try {
                this.mCargoConnection.checkSingleDeviceEnforcement();
                this.mCargoConnection.setDeviceName(this.mBandName);
                return true;
            } catch (CargoException exception) {
                KLog.d(this.TASK_TAG, "error saving my band name", exception);
                setException(exception);
                return false;
            }
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.microsoft.kapp.ScopedAsyncTask
        public void onPostExecute(Boolean result) {
            SettingsMyBandFragment fragment = this.mWeakFragment.get();
            if (fragment != null) {
                if (getException() == null) {
                    fragment.onSaveBandNameSucced();
                } else {
                    fragment.onSaveBandNameFailed(getException());
                }
            }
        }
    }

    private void onBandNotFound() {
        if (isAdded()) {
            this.mBandNameEditText.setText(getResources().getString(R.string.settings_my_band_error));
            this.mBandNameEditText.setEnabled(false);
            this.mBandSerialNo.setText(String.format(getResources().getString(R.string.settings_my_band_serial_no), getResources().getString(R.string.settings_my_band_error)));
            this.mBandFirmwareVersion.setText(String.format(getResources().getString(R.string.settings_my_band_firmware), getResources().getString(R.string.settings_my_band_error)));
            this.mBatteryLayout.setVisibility(4);
            this.mBandContainer.setVisibility(4);
        }
    }
}

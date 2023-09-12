package com.microsoft.kapp.fragments;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.microsoft.band.client.CargoException;
import com.microsoft.kapp.ActivityScopedCallback;
import com.microsoft.kapp.Callback;
import com.microsoft.kapp.CargoConnection;
import com.microsoft.kapp.CargoExtensions;
import com.microsoft.kapp.DeviceErrorState;
import com.microsoft.kapp.OnTaskListener;
import com.microsoft.kapp.R;
import com.microsoft.kapp.ScopedAsyncTask;
import com.microsoft.kapp.activities.DeviceColorPersonalizationActivity;
import com.microsoft.kapp.activities.DeviceWallpaperPersonalizationActivity;
import com.microsoft.kapp.activities.DialogPriority;
import com.microsoft.kapp.diagnostics.Telemetry;
import com.microsoft.kapp.diagnostics.TelemetryConstants;
import com.microsoft.kapp.diagnostics.Validate;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.kapp.navigations.FragmentNavigationCommandV1;
import com.microsoft.kapp.personalization.DeviceTheme;
import com.microsoft.kapp.personalization.GetDeviceWallpaperIdAsyncTask;
import com.microsoft.kapp.personalization.PersonalizationManagerFactory;
import com.microsoft.kapp.services.SettingsProvider;
import com.microsoft.kapp.tasks.PersonalizationSaveTask;
import com.microsoft.kapp.utils.ActivityUtils;
import com.microsoft.kapp.utils.ViewUtils;
import com.microsoft.kapp.widgets.ConfirmationBar;
import com.microsoft.kapp.widgets.Interstitial;
import com.microsoft.krestsdk.models.BandVersion;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.inject.Inject;
/* loaded from: classes.dex */
public class SettingsPersonalizeFragment extends BaseFragment {
    private static final String BAND_THEME_ID = "band_theme_id";
    private static final String BAND_VERSION = "band_version";
    private static final String CURRENT_WALLPAPER_INDEX = "current_wallpaper_index";
    public static final String FRAGMENT_NAME = "CUSTOM_COLOR_ACTIVITY";
    private static final String SETTINGS_CHANGED = "settings_changed";
    private static final String WALLPAPER_INDEX = "wallpaper_index";
    private int mBandThemeId;
    private BandVersion mBandVersion;
    private View mCargoBackground;
    private View mCargoImage;
    @Inject
    CargoConnection mCargoconnection;
    private ImageView mChangeColorButton;
    private ImageView mChangeHighlightColorButton;
    private ImageView mChangeWallpaperButton;
    private ConfirmationBar mConfirmationBar;
    private ImageView mDeviceImageDisplay;
    private DeviceTheme mDeviceTheme;
    private boolean mIsTherePendingChange;
    private View mNeonBackground;
    private ImageView mNeonDeviceImageDisplay;
    private View mNeonImage;
    @Inject
    PersonalizationManagerFactory mPersonalizationManagerFactory;
    private Interstitial mProgressBar;
    private DeviceTheme mSaveColorSetting;
    private int mSaveWallpaperPatternId;
    @Inject
    SettingsProvider mSettingsProvider;
    private int mWallpaperId;
    private int mWallpaperPatternId;
    private boolean mIsOpeningActivity = false;
    private AtomicBoolean mIsActionInProgress = new AtomicBoolean(false);

    /* loaded from: classes.dex */
    private class GetBandVersionTask extends ScopedAsyncTask<Void, Void, Void> {
        public GetBandVersionTask(OnTaskListener onTaskListener) {
            super(onTaskListener);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.microsoft.kapp.ScopedAsyncTask
        public Void doInBackground(Void... params) {
            try {
                SettingsPersonalizeFragment.this.mBandVersion = SettingsPersonalizeFragment.this.mCargoconnection.getBandVersion();
                return null;
            } catch (CargoException e) {
                SettingsPersonalizeFragment.this.mBandVersion = BandVersion.CARGO;
                KLog.i(this.TAG, "could not retrieve the band version, the comunication with the band failed. Defaulted to cargo");
                return null;
            }
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.microsoft.kapp.ScopedAsyncTask
        public void onPostExecute(Void result) {
        }
    }

    @Override // com.microsoft.kapp.fragments.BaseFragment, android.support.v4.app.Fragment
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            this.mWallpaperPatternId = savedInstanceState.getInt(WALLPAPER_INDEX);
            this.mSaveWallpaperPatternId = savedInstanceState.getInt(CURRENT_WALLPAPER_INDEX);
            this.mIsTherePendingChange = Boolean.TRUE.booleanValue() == savedInstanceState.getBoolean(SETTINGS_CHANGED);
            this.mBandVersion = (BandVersion) savedInstanceState.getSerializable(BAND_VERSION);
            this.mBandThemeId = savedInstanceState.getInt(BAND_THEME_ID);
        }
    }

    @Override // com.microsoft.kapp.fragments.BaseFragment, android.support.v4.app.Fragment
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(WALLPAPER_INDEX, this.mWallpaperPatternId);
        outState.putInt(CURRENT_WALLPAPER_INDEX, this.mSaveWallpaperPatternId);
        outState.putBoolean(SETTINGS_CHANGED, this.mIsTherePendingChange);
        outState.putSerializable(BAND_VERSION, this.mBandVersion);
        outState.putSerializable(BAND_THEME_ID, Integer.valueOf(this.mBandThemeId));
    }

    @Override // android.support.v4.app.Fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.settings_personalize_fragment, container, false);
        this.mChangeColorButton = (ImageView) ViewUtils.getValidView(rootView, R.id.settings_change_color_button, ImageView.class);
        this.mChangeHighlightColorButton = (ImageView) ViewUtils.getValidView(rootView, R.id.settings_change_highlight_color_button, ImageView.class);
        this.mDeviceImageDisplay = (ImageView) rootView.findViewById(R.id.device_representation_background);
        this.mNeonDeviceImageDisplay = (ImageView) rootView.findViewById(R.id.neon_device_representation_background);
        this.mChangeWallpaperButton = (ImageView) ViewUtils.getValidView(rootView, R.id.settings_change_wallpaper_button, ImageView.class);
        this.mConfirmationBar = (ConfirmationBar) ViewUtils.getValidView(rootView, R.id.settings_profile_confirmation_bar, ConfirmationBar.class);
        this.mProgressBar = (Interstitial) ViewUtils.getValidView(rootView, R.id.settings_personalize_sync_indicator, Interstitial.class);
        this.mCargoImage = (View) ViewUtils.getValidView(rootView, R.id.device_representation_image, View.class);
        this.mNeonImage = (View) ViewUtils.getValidView(rootView, R.id.neon_device_representation_image, View.class);
        this.mCargoBackground = (View) ViewUtils.getValidView(rootView, R.id.device_representation_background, View.class);
        this.mNeonBackground = (View) ViewUtils.getValidView(rootView, R.id.neon_device_representation_background, View.class);
        this.mProgressBar.setSlide(Interstitial.SLIDE_GETTING_BAND_INFO);
        this.mChangeColorButton.setOnClickListener(new View.OnClickListener() { // from class: com.microsoft.kapp.fragments.SettingsPersonalizeFragment.1
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                SettingsPersonalizeFragment.this.openColorPicker(SettingsPersonalizeFragment.this.mDeviceTheme.getThemeId());
            }
        });
        this.mChangeWallpaperButton.setOnClickListener(new View.OnClickListener() { // from class: com.microsoft.kapp.fragments.SettingsPersonalizeFragment.2
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                SettingsPersonalizeFragment.this.openBackgroundPicker(SettingsPersonalizeFragment.this.mDeviceTheme.getThemeId(), SettingsPersonalizeFragment.this.mWallpaperPatternId);
            }
        });
        this.mConfirmationBar.setOnCancelButtonClickListener(new View.OnClickListener() { // from class: com.microsoft.kapp.fragments.SettingsPersonalizeFragment.3
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                SettingsPersonalizeFragment.this.confirmationBarOnCancel();
            }
        });
        this.mConfirmationBar.setOnConfirmButtonClickListener(new View.OnClickListener() { // from class: com.microsoft.kapp.fragments.SettingsPersonalizeFragment.4
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                SettingsPersonalizeFragment.this.confirmationBarOnConfirm();
            }
        });
        if (this.mIsTherePendingChange) {
            this.mConfirmationBar.setVisibility(0);
        }
        return rootView;
    }

    @Override // com.microsoft.kapp.fragments.BaseFragment, com.microsoft.kapp.activities.OnNavigateToFragmentListener
    public boolean handleNavigateToFragment(Class fragmentClass, boolean addToBackStack, boolean shouldToggleSlidingMenu) {
        if (!fragmentClass.equals(SettingsPersonalizeFragment.class) && isAdded() && this.mIsTherePendingChange) {
            showPendingChangesDialog(false, fragmentClass, addToBackStack);
            return true;
        }
        return false;
    }

    @Override // com.microsoft.kapp.fragments.BaseFragment, com.microsoft.kapp.activities.OnBackButtonPressedListener
    public boolean handleBackButton() {
        if (isAdded() && this.mIsTherePendingChange) {
            showPendingChangesDialog(true, null, false);
            return true;
        }
        return false;
    }

    private void showPendingChangesDialog(final boolean exitBackPressed, final Class fragmentClass, final boolean addToBackStack) {
        getDialogManager().showDialog(getActivity(), Integer.valueOf((int) R.string.strapps_lose_changes_confirmation_title), Integer.valueOf((int) R.string.strapps_lose_changes_confirmation_message), R.string.strapps_button_continue, new DialogInterface.OnClickListener() { // from class: com.microsoft.kapp.fragments.SettingsPersonalizeFragment.5
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) {
                SettingsPersonalizeFragment.this.mIsTherePendingChange = false;
                Activity activity = SettingsPersonalizeFragment.this.getActivity();
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
                KLog.w(SettingsPersonalizeFragment.this.TAG, "Activity is no longer alive.");
            }
        }, R.string.strapps_button_cancel, null, DialogPriority.HIGH);
    }

    private void loadCurrentWallpaper() {
        new GetDeviceWallpaperIdAsyncTask(this.mCargoConnection, this.mSettingsProvider, new ActivityScopedCallback(this, new Callback<Integer>() { // from class: com.microsoft.kapp.fragments.SettingsPersonalizeFragment.6
            @Override // com.microsoft.kapp.Callback
            public void onError(Exception ex) {
                final Activity activity = SettingsPersonalizeFragment.this.getActivity();
                DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() { // from class: com.microsoft.kapp.fragments.SettingsPersonalizeFragment.6.1
                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityUtils.performBackButton(activity);
                    }
                };
                if ((ex instanceof CargoExtensions.SingleDeviceCheckFailedException) && ((CargoExtensions.SingleDeviceCheckFailedException) ex).getDeviceErrorState() == DeviceErrorState.MULTIPLE_DEVICES_BONDED) {
                    SettingsPersonalizeFragment.this.getDialogManager().showMultipleDevicesConnectedError(SettingsPersonalizeFragment.this.getActivity(), listener);
                } else {
                    SettingsPersonalizeFragment.this.getDialogManager().showDeviceErrorDialogWithCallback(activity, listener);
                }
            }

            @Override // com.microsoft.kapp.Callback
            public void callback(Integer result) {
                try {
                    SettingsPersonalizeFragment.this.mWallpaperPatternId = result.intValue();
                    SettingsPersonalizeFragment.this.mSaveWallpaperPatternId = SettingsPersonalizeFragment.this.mWallpaperPatternId;
                    SettingsPersonalizeFragment.this.mDeviceTheme = SettingsPersonalizeFragment.this.mPersonalizationManagerFactory.getPersonalizationManager(SettingsPersonalizeFragment.this.mBandVersion).getThemeById(SettingsPersonalizeFragment.this.mWallpaperPatternId);
                    SettingsPersonalizeFragment.this.mSaveColorSetting = SettingsPersonalizeFragment.this.mDeviceTheme;
                    SettingsPersonalizeFragment.this.updateDeviceRepresentation();
                    SettingsPersonalizeFragment.this.mProgressBar.setSlide(Interstitial.SLIDE_GONE);
                } catch (Exception ex) {
                    KLog.e(SettingsPersonalizeFragment.this.TAG, "Unexpected Error!", ex);
                }
            }
        }), this).execute(new Void[0]);
    }

    @Override // android.support.v4.app.Fragment
    public void onStart() {
        super.onStart();
        new GetBandVersionTask(this).execute(new Void[0]);
        setTopMenuBarColor(getResources().getColor(R.color.settingsMenuBar));
    }

    @Override // com.microsoft.kapp.fragments.BaseFragment, android.support.v4.app.Fragment
    public void onResume() {
        super.onResume();
        if (!this.mIsTherePendingChange) {
            loadCurrentWallpaper();
        }
        Telemetry.logPage(TelemetryConstants.PageViews.SETTINGS_PERSONALIZE_THEME_CHOOSER);
    }

    @Override // android.support.v4.app.Fragment
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        this.mIsOpeningActivity = false;
        if (resultCode == -1) {
            switch (requestCode) {
                case 0:
                    int themeId = data.getIntExtra(DeviceColorPersonalizationActivity.ARG_OUT_DEVICE_THEME_ID, -1);
                    this.mBandThemeId = themeId;
                    break;
                case 1:
                    this.mWallpaperPatternId = data.getIntExtra(DeviceWallpaperPersonalizationActivity.ARG_OUT_WALLPAPER_PATTERN_ID, this.mWallpaperPatternId);
                    break;
            }
            this.mDeviceTheme = this.mPersonalizationManagerFactory.getPersonalizationManager(this.mBandVersion).getThemeById(this.mBandThemeId);
            this.mWallpaperPatternId = this.mDeviceTheme.getThemeId() | (this.mWallpaperPatternId & 65535);
            updateConfirmationBarVisibility(checkIfAnyValuesHaveChanged(this.mDeviceTheme, Integer.valueOf(this.mWallpaperPatternId)));
            updateDeviceRepresentation();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateDeviceRepresentation() {
        this.mWallpaperId = this.mDeviceTheme.getWallpaperById(this.mWallpaperPatternId).getResId();
        this.mChangeColorButton.setBackgroundColor(this.mDeviceTheme.getBase());
        this.mChangeHighlightColorButton.setImageResource(this.mDeviceTheme.getHighlight());
        this.mChangeWallpaperButton.setImageResource(this.mWallpaperId);
        this.mDeviceImageDisplay.setImageResource(this.mWallpaperId);
        if (this.mBandVersion == BandVersion.NEON) {
            this.mNeonImage.setVisibility(0);
            this.mCargoImage.setVisibility(4);
            this.mNeonBackground.setVisibility(0);
            this.mCargoBackground.setVisibility(4);
            this.mNeonDeviceImageDisplay.setImageResource(this.mWallpaperId);
        } else {
            this.mNeonImage.setVisibility(4);
            this.mCargoImage.setVisibility(0);
            this.mNeonBackground.setVisibility(4);
            this.mCargoBackground.setVisibility(0);
            this.mDeviceImageDisplay.setImageResource(this.mWallpaperId);
        }
        this.mProgressBar.setSlide(Interstitial.SLIDE_GONE);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void confirmationBarOnCancel() {
        if (this.mIsActionInProgress.compareAndSet(false, true)) {
            updateConfirmationBarVisibility(false);
            this.mDeviceTheme = this.mSaveColorSetting;
            this.mWallpaperPatternId = this.mSaveWallpaperPatternId;
            updateDeviceRepresentation();
            this.mIsActionInProgress.set(false);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void confirmationBarOnConfirm() {
        if (this.mIsActionInProgress.compareAndSet(false, true)) {
            setDeviceBackgroundAsync();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateConfirmationBarVisibility(boolean changed) {
        this.mIsTherePendingChange = changed;
        this.mConfirmationBar.setVisibility(changed ? 0 : 8);
    }

    private boolean checkIfAnyValuesHaveChanged(DeviceTheme theme, Integer wallpaperIndex) {
        return (this.mSaveColorSetting == theme && this.mSaveWallpaperPatternId == wallpaperIndex.intValue()) ? false : true;
    }

    private void saveOobeBackground(final DeviceTheme deviceTheme, final int wallpaperPatternId, Bitmap deviceImage) {
        PersonalizationSaveTask saveTask = new PersonalizationSaveTask(this.mCargoConnection, deviceTheme, deviceImage, wallpaperPatternId, this) { // from class: com.microsoft.kapp.fragments.SettingsPersonalizeFragment.7
            /* JADX INFO: Access modifiers changed from: protected */
            @Override // com.microsoft.kapp.ScopedAsyncTask
            public void onPreExecute() {
                SettingsPersonalizeFragment.this.mProgressBar.setSlide(Interstitial.SLIDE_UPDATING_BAND_INFO);
                super.onPreExecute();
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // com.microsoft.kapp.ScopedAsyncTask
            public void onPostExecute(Boolean result) {
                SettingsPersonalizeFragment.this.mProgressBar.setSlide(Interstitial.SLIDE_GONE);
                if (getException() == null && result.booleanValue()) {
                    SettingsPersonalizeFragment.this.updateConfirmationBarVisibility(false);
                    SettingsPersonalizeFragment.this.mSaveColorSetting = deviceTheme;
                    SettingsPersonalizeFragment.this.mSaveWallpaperPatternId = wallpaperPatternId;
                    SettingsPersonalizeFragment.this.mSettingsProvider.setCurrentWallpaperId(wallpaperPatternId);
                    HashMap<String, String> telemetryProperties = new HashMap<>();
                    telemetryProperties.put("In OOBE", String.valueOf(false));
                    telemetryProperties.put(TelemetryConstants.Events.ThemeChange.Dimensions.THEME_COLOR_NAME, deviceTheme.getThemeName());
                    telemetryProperties.put(TelemetryConstants.Events.ThemeChange.Dimensions.THEME_WALLPAPER_NAME, String.valueOf(wallpaperPatternId));
                    Telemetry.logEvent(TelemetryConstants.Events.RenameBand.EVENT_NAME, telemetryProperties, null);
                } else {
                    SettingsPersonalizeFragment.this.getDialogManager().showDeviceErrorDialog(SettingsPersonalizeFragment.this.getActivity());
                }
                SettingsPersonalizeFragment.this.mIsActionInProgress.set(false);
            }
        };
        saveTask.execute(new Void[0]);
    }

    private void setDeviceBackgroundAsync() {
        Bitmap deviceImage = BitmapFactory.decodeStream(getResources().openRawResource(this.mWallpaperId));
        saveOobeBackground(this.mDeviceTheme, this.mWallpaperPatternId, deviceImage);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void openColorPicker(int themeId) {
        if (!this.mIsOpeningActivity) {
            Intent intent = new Intent(getActivity(), DeviceColorPersonalizationActivity.class);
            intent.putExtra(DeviceColorPersonalizationActivity.ARG_IN_CURRENT_DEVICE_THEME_ID, themeId);
            intent.putExtra(PersonalizationManagerFactory.BAND_VERSION_ID, this.mBandVersion.ordinal());
            startActivityForResult(intent, 0);
            this.mIsOpeningActivity = true;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void openBackgroundPicker(int themeId, int wallpaperPatternId) {
        if (!this.mIsOpeningActivity) {
            Intent intent = new Intent(getActivity(), DeviceWallpaperPersonalizationActivity.class);
            intent.putExtra(DeviceWallpaperPersonalizationActivity.ARG_IN_DEVICE_THEME_ID, themeId);
            intent.putExtra(DeviceWallpaperPersonalizationActivity.ARG_IN_CURRENT_WALLPAPER_RES_ID, wallpaperPatternId);
            intent.putExtra(PersonalizationManagerFactory.BAND_VERSION_ID, this.mBandVersion.ordinal());
            startActivityForResult(intent, 1);
            this.mIsOpeningActivity = true;
        }
    }
}

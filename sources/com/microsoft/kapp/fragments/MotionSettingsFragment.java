package com.microsoft.kapp.fragments;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import com.microsoft.kapp.R;
import com.microsoft.kapp.UserProfileFetcher;
import com.microsoft.kapp.diagnostics.Telemetry;
import com.microsoft.kapp.diagnostics.TelemetryConstants;
import com.microsoft.kapp.diagnostics.Validate;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.kapp.navigations.FragmentNavigationCommandV1;
import com.microsoft.kapp.sensor.SensorUtils;
import com.microsoft.kapp.sensor.service.SensorService;
import com.microsoft.kapp.services.SettingsProvider;
import com.microsoft.kapp.tasks.ConnectPhoneTask;
import com.microsoft.kapp.utils.ActivityUtils;
import com.microsoft.kapp.utils.DialogManager;
import com.microsoft.kapp.utils.ViewUtils;
import com.microsoft.kapp.widgets.ConfirmationBar;
import com.microsoft.krestsdk.auth.credentials.CredentialsManager;
import java.util.HashMap;
import javax.inject.Inject;
/* loaded from: classes.dex */
public class MotionSettingsFragment extends BaseFragment implements ConnectPhoneTask.IConnectPhoneTaskListener {
    private ConfirmationBar mConfirmationBar;
    @Inject
    CredentialsManager mCredentialsManager;
    private TextView mDescriptionText;
    private Switch mEnabledSwitch;
    private TextView mEnabledText;
    @Inject
    UserProfileFetcher mProfileFetcher;
    @Inject
    SensorUtils mSensorUtils;
    @Inject
    SettingsProvider mSettingsProvider;

    @Override // android.support.v4.app.Fragment
    public void onStart() {
        super.onStart();
        setTopMenuBarColor(getResources().getColor(R.color.settingsMenuBar));
    }

    @Override // com.microsoft.kapp.fragments.BaseFragment, android.support.v4.app.Fragment
    public void onResume() {
        super.onResume();
        Telemetry.logPage("Phone/Motion tracking");
    }

    @Override // android.support.v4.app.Fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_motion_settings, container, false);
        this.mConfirmationBar = (ConfirmationBar) ViewUtils.getValidView(rootView, R.id.confirmation_bar, ConfirmationBar.class);
        this.mEnabledSwitch = (Switch) ViewUtils.getValidView(rootView, R.id.switchMotionEnabled, Switch.class);
        this.mEnabledText = (TextView) ViewUtils.getValidView(rootView, R.id.txtMotionEnabled, TextView.class);
        this.mDescriptionText = (TextView) ViewUtils.getValidView(rootView, R.id.txtMotionCaptureDescription, TextView.class);
        boolean enabled = this.mSettingsProvider.isSensorLoggingEnabled();
        this.mEnabledText.setText(enabled ? R.string.motion_settings_enabled : R.string.motion_settings_disabled);
        this.mEnabledSwitch.setChecked(enabled);
        this.mDescriptionText.setText(enabled ? R.string.motion_settings_description_enabled : R.string.motion_settings_description);
        this.mConfirmationBar.setOnConfirmButtonClickListener(new View.OnClickListener() { // from class: com.microsoft.kapp.fragments.MotionSettingsFragment.1
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                if (!MotionSettingsFragment.this.mEnabledSwitch.isChecked() || MotionSettingsFragment.this.mMultiDeviceManager.isPhoneRegistered()) {
                    MotionSettingsFragment.this.saveChanges();
                    MotionSettingsFragment.this.logMotionSettingsEvent(false);
                    return;
                }
                ConnectPhoneTask deviceTimeTask = new ConnectPhoneTask(MotionSettingsFragment.this.mMultiDeviceManager, MotionSettingsFragment.this, MotionSettingsFragment.this.mSettingsProvider, MotionSettingsFragment.this.mProfileFetcher, MotionSettingsFragment.this.mSensorUtils);
                deviceTimeTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
                MotionSettingsFragment.this.logMotionSettingsEvent(true);
            }
        });
        this.mConfirmationBar.setOnCancelButtonClickListener(new View.OnClickListener() { // from class: com.microsoft.kapp.fragments.MotionSettingsFragment.2
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                MotionSettingsFragment.this.reset();
            }
        });
        this.mEnabledSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { // from class: com.microsoft.kapp.fragments.MotionSettingsFragment.3
            @Override // android.widget.CompoundButton.OnCheckedChangeListener
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                MotionSettingsFragment.this.mEnabledText.setText(isChecked ? R.string.motion_settings_enabled : R.string.motion_settings_disabled);
                MotionSettingsFragment.this.mConfirmationBar.setVisibility(0);
            }
        });
        return rootView;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void reset() {
        this.mEnabledSwitch.setChecked(this.mSettingsProvider.isSensorLoggingEnabled());
        this.mConfirmationBar.setVisibility(4);
    }

    @Override // com.microsoft.kapp.fragments.BaseFragment, com.microsoft.kapp.activities.OnBackButtonPressedListener
    public boolean handleBackButton() {
        if (isChangePending()) {
            showPendingChangesDialog(true, null, false);
            return true;
        }
        return false;
    }

    @Override // com.microsoft.kapp.fragments.BaseFragment, com.microsoft.kapp.activities.OnNavigateToFragmentListener
    public boolean handleNavigateToFragment(Class fragmentClass, boolean addToBackStack, boolean shouldToggleSlidingMenu) {
        if (fragmentClass.equals(MotionSettingsFragment.class) || !isChangePending()) {
            return false;
        }
        showPendingChangesDialog(false, fragmentClass, addToBackStack);
        return true;
    }

    private boolean isChangePending() {
        return this.mConfirmationBar.getVisibility() == 0;
    }

    private void showPendingChangesDialog(final boolean exitOnSaveSuccess, final Class fragmentClass, final boolean addToBackStack) {
        DialogManager.showDialog(getActivity(), Integer.valueOf((int) R.string.lose_changes_confirmation_title), Integer.valueOf((int) R.string.motion_settings_lose_changes_confirmation_message), (int) R.string.lose_changes_button_continue, new DialogInterface.OnClickListener() { // from class: com.microsoft.kapp.fragments.MotionSettingsFragment.4
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) {
                MotionSettingsFragment.this.reset();
                MotionSettingsFragment.this.performNavigateAway(exitOnSaveSuccess, fragmentClass, addToBackStack);
            }
        }, (int) R.string.lose_changes_button_cancel, (DialogInterface.OnClickListener) null, DialogManager.Priority.HIGH);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void performNavigateAway(boolean exitBackPressed, Class navigateFragmentClass, boolean navigateAddToBackStack) {
        Activity activity = getActivity();
        if (Validate.isActivityAlive(activity)) {
            if (exitBackPressed) {
                ActivityUtils.performBackButton(activity);
                return;
            } else if (navigateFragmentClass != null && (activity instanceof FragmentNavigationCommandV1.FragmentNavigationListener)) {
                ((FragmentNavigationCommandV1.FragmentNavigationListener) activity).navigateToFragment(navigateFragmentClass, null, navigateAddToBackStack, false);
                return;
            } else {
                return;
            }
        }
        KLog.w(this.TAG, "Activity is no longer alive.");
    }

    protected void logMotionSettingsEvent(boolean isEnabled) {
        HashMap<String, String> properties = new HashMap<>();
        properties.put(TelemetryConstants.Events.Phone.Dimensions.TRACKING_STATE, isEnabled ? "Enabled" : "Disabled");
        Telemetry.logEvent(TelemetryConstants.Events.Phone.ENABLE_MOTION_TRACKING_EVENT_NAME, properties, null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void saveChanges() {
        this.mSettingsProvider.setIsSensorLoggingEnabled(this.mEnabledSwitch.isChecked());
        this.mDescriptionText.setText(this.mEnabledSwitch.isChecked() ? R.string.motion_settings_description_enabled : R.string.motion_settings_description);
        this.mConfirmationBar.setVisibility(4);
        if (this.mSettingsProvider.isSensorLoggingEnabled()) {
            if (this.mSensorUtils.isKitkatWithStepSensor()) {
                Intent sensorServiceIntent = new Intent(getActivity(), SensorService.class);
                getActivity().startService(sensorServiceIntent);
                return;
            }
            return;
        }
        getActivity().stopService(new Intent(getActivity(), SensorService.class));
    }

    @Override // com.microsoft.kapp.tasks.ConnectPhoneTask.IConnectPhoneTaskListener
    public void onConnectPhoneSucceeded() {
        saveChanges();
    }

    @Override // com.microsoft.kapp.tasks.ConnectPhoneTask.IConnectPhoneTaskListener
    public void onConnectPhoneFailed(Exception ex, boolean isCloudError, boolean isBandError) {
        getDialogManager().showNetworkErrorDialog(getActivity());
    }
}

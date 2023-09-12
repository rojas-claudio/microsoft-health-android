package com.microsoft.kapp.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Switch;
import com.microsoft.kapp.ActivityScopedCallback;
import com.microsoft.kapp.Callback;
import com.microsoft.kapp.R;
import com.microsoft.kapp.UserProfileFetcher;
import com.microsoft.kapp.activities.DialogPriority;
import com.microsoft.kapp.diagnostics.Telemetry;
import com.microsoft.kapp.diagnostics.TelemetryConstants;
import com.microsoft.kapp.diagnostics.Validate;
import com.microsoft.kapp.models.SettingsPreferences;
import com.microsoft.kapp.sensor.SensorUtils;
import com.microsoft.kapp.services.SettingsProvider;
import com.microsoft.kapp.tasks.SettingsPreferencesRetrieveTask;
import com.microsoft.kapp.tasks.SettingsPreferencesSaveTask;
import com.microsoft.kapp.utils.ActivityUtils;
import com.microsoft.kapp.utils.CommonUtils;
import com.microsoft.kapp.utils.Constants;
import com.microsoft.kapp.utils.StrappUtils;
import com.microsoft.kapp.utils.SyncUtils;
import com.microsoft.kapp.utils.ViewUtils;
import com.microsoft.kapp.widgets.ConfirmationBar;
import com.microsoft.krestsdk.models.BandVersion;
import javax.inject.Inject;
/* loaded from: classes.dex */
public class SettingsPreferencesFragment extends BaseFragmentWithOfflineSupport {
    private static final String EXTRA_PREFERENCES = "extra_preferences";
    private Spinner mBandRegionSpinner;
    private boolean mCloudPreferencesChanged;
    ConfirmationBar mConfirmationBar;
    private BandVersion mDevice;
    private int mDistanceHeightImperialIndex;
    private int mDistanceHeightMetricIndex;
    private Spinner mDistanceHeightSpinner;
    @Inject
    SensorUtils mSensorUtils;
    private SettingsPreferences mSettingsPreferences;
    @Inject
    SettingsProvider mSettingsProvider;
    Switch mShakeToSendFeedbackSwitch;
    private int mTemperatureImperialIndex;
    private int mTemperatureMetricIndex;
    private Spinner mTemperatureSpinner;
    @Inject
    UserProfileFetcher mUserProfileFetcher;
    private int mWeightImperialIndex;
    private int mWeightMetricIndex;
    private Spinner mWeightSpinner;

    @Override // com.microsoft.kapp.fragments.BaseFragment, android.support.v4.app.Fragment
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            this.mSettingsPreferences = (SettingsPreferences) (savedInstanceState.containsKey(EXTRA_PREFERENCES) ? savedInstanceState.getSerializable(EXTRA_PREFERENCES) : null);
        }
    }

    @Override // com.microsoft.kapp.fragments.BaseFragment, android.support.v4.app.Fragment
    public void onResume() {
        super.onResume();
        Telemetry.logPage(TelemetryConstants.PageViews.SETTINGS_USER_PREFERENCES);
        setTopMenuBarColor(getResources().getColor(R.color.settingsMenuBar));
    }

    @Override // com.microsoft.kapp.fragments.BaseFragment, android.support.v4.app.Fragment
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (this.mSettingsPreferences != null) {
            outState.putSerializable(EXTRA_PREFERENCES, this.mSettingsPreferences);
        }
    }

    @Override // com.microsoft.kapp.fragments.BaseFragmentWithOfflineSupport
    public View onCreateChildView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_settings_preferences, container, false);
        if (!CommonUtils.isNetworkAvailable(getActivity())) {
            DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() { // from class: com.microsoft.kapp.fragments.SettingsPreferencesFragment.1
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialog, int which) {
                    ActivityUtils.performBackButton(SettingsPreferencesFragment.this.getActivity());
                }
            };
            getDialogManager().showNetworkErrorDialogWithCallback(getActivity(), listener);
        }
        this.mWeightSpinner = (Spinner) ViewUtils.getValidView(rootView, R.id.settings_preferences_weight_spinner, Spinner.class);
        this.mTemperatureSpinner = (Spinner) ViewUtils.getValidView(rootView, R.id.settings_preferences_temperature_spinner, Spinner.class);
        this.mDistanceHeightSpinner = (Spinner) ViewUtils.getValidView(rootView, R.id.settings_preferences_distance_height_spinner, Spinner.class);
        this.mConfirmationBar = (ConfirmationBar) ViewUtils.getValidView(rootView, R.id.confirmation_bar, ConfirmationBar.class);
        this.mShakeToSendFeedbackSwitch = (Switch) ViewUtils.getValidView(rootView, R.id.shake_to_send_feedback_switch, Switch.class);
        ArrayAdapter<CharSequence> weightSpinnerAdapter = createSpinnerAdapterFromStringArray(getActivity(), R.array.settings_preferences_units_weight);
        this.mWeightMetricIndex = weightSpinnerAdapter.getPosition(getString(R.string.settings_preferences_units_weight_metric));
        this.mWeightImperialIndex = weightSpinnerAdapter.getPosition(getString(R.string.settings_preferences_units_weight_imperial));
        this.mWeightSpinner.setAdapter((SpinnerAdapter) weightSpinnerAdapter);
        ArrayAdapter<CharSequence> temperatureSpinnerAdapter = createSpinnerAdapterFromStringArray(getActivity(), R.array.settings_preferences_units_temperature);
        this.mTemperatureMetricIndex = temperatureSpinnerAdapter.getPosition(getString(R.string.settings_preferences_units_temperature_metric));
        this.mTemperatureImperialIndex = temperatureSpinnerAdapter.getPosition(getString(R.string.settings_preferences_units_temperature_imperial));
        this.mTemperatureSpinner.setAdapter((SpinnerAdapter) temperatureSpinnerAdapter);
        ArrayAdapter<CharSequence> distanceHeightSpinnerAdapter = createSpinnerAdapterFromStringArray(getActivity(), R.array.settings_preferences_units_distance_height);
        this.mDistanceHeightMetricIndex = distanceHeightSpinnerAdapter.getPosition(getString(R.string.settings_preferences_units_distance_height_metric));
        this.mDistanceHeightImperialIndex = distanceHeightSpinnerAdapter.getPosition(getString(R.string.settings_preferences_units_distance_height_imperial));
        this.mDistanceHeightSpinner.setAdapter((SpinnerAdapter) distanceHeightSpinnerAdapter);
        this.mShakeToSendFeedbackSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { // from class: com.microsoft.kapp.fragments.SettingsPreferencesFragment.2
            @Override // android.widget.CompoundButton.OnCheckedChangeListener
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SettingsPreferencesFragment.this.updateConfirmationBarVisibility();
            }
        });
        this.mConfirmationBar.setOnCancelButtonClickListener(new View.OnClickListener() { // from class: com.microsoft.kapp.fragments.SettingsPreferencesFragment.3
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                SettingsPreferencesFragment.this.setUIValuesFrom(SettingsPreferencesFragment.this.mSettingsPreferences);
                SettingsPreferencesFragment.this.updateConfirmationBarVisibility();
                SettingsPreferencesFragment.this.mCloudPreferencesChanged = false;
            }
        });
        this.mConfirmationBar.setOnConfirmButtonClickListener(new View.OnClickListener() { // from class: com.microsoft.kapp.fragments.SettingsPreferencesFragment.4
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                SettingsPreferences prefs = new SettingsPreferences();
                SettingsPreferencesFragment.this.readUIValuesInto(prefs);
                if (SettingsPreferencesFragment.this.mCloudPreferencesChanged) {
                    SettingsPreferencesFragment.this.saveSettingsPreferences(prefs);
                } else {
                    SettingsPreferencesFragment.this.updateConfirmationBarVisibility();
                }
                SettingsPreferencesFragment.this.mCloudPreferencesChanged = false;
            }
        });
        this.mBandRegionSpinner = (Spinner) ViewUtils.getValidView(rootView, R.id.settings_preferences_band_region_spinner, Spinner.class);
        ArrayAdapter<CharSequence> bandRegionAdapter = createSpinnerAdapterFromStringArray(getActivity(), R.array.settings_preferences_regions);
        this.mBandRegionSpinner.setAdapter((SpinnerAdapter) bandRegionAdapter);
        return rootView;
    }

    @Override // com.microsoft.kapp.fragments.BaseFragment
    public void load() {
        super.load();
        Validate.notNull(this.mSettingsProvider, "mSettingsProvider");
        if (this.mSettingsPreferences == null) {
            retrieveSettingsPreferences();
        } else {
            setState(1234);
        }
    }

    private void retrieveSettingsPreferences() {
        setState(1233);
        SettingsPreferencesRetrieveTask retrieveTask = new SettingsPreferencesRetrieveTask(this.mSettingsProvider, this.mUserProfileFetcher, this.mCargoConnection, new ActivityScopedCallback(this, new Callback<Object>() { // from class: com.microsoft.kapp.fragments.SettingsPreferencesFragment.5
            @Override // com.microsoft.kapp.Callback
            public void callback(Object result) {
                if (result != null) {
                    Object[] results = (Object[]) result;
                    SettingsPreferencesFragment.this.mDevice = (BandVersion) results[0];
                    SettingsPreferences settingsPrefs = (SettingsPreferences) results[1];
                    SettingsPreferencesFragment.this.setUIValuesFrom(settingsPrefs);
                    SettingsPreferencesFragment.this.mSettingsPreferences = settingsPrefs;
                    SettingsPreferencesFragment.this.setState(1234);
                    SettingsPreferencesFragment.this.setItemChangedListener();
                }
            }

            @Override // com.microsoft.kapp.Callback
            public void onError(Exception ex) {
                SettingsPreferencesFragment.this.setState(1235);
            }
        }));
        retrieveTask.execute(new Void[0]);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void saveSettingsPreferences(final SettingsPreferences settingsPreferences) {
        Validate.notNull(settingsPreferences, "settingsPreferences");
        setState(1233);
        SettingsPreferencesSaveTask saveTask = new SettingsPreferencesSaveTask(this.mSettingsProvider, settingsPreferences, this.mCargoConnection, this.mSensorUtils, getActivity(), new ActivityScopedCallback(this, new Callback<Integer>() { // from class: com.microsoft.kapp.fragments.SettingsPreferencesFragment.6
            @Override // com.microsoft.kapp.Callback
            public void callback(Integer result) {
                if (result.intValue() == 1000 || result.intValue() == 1001) {
                    SettingsPreferencesFragment.this.setUIValuesFrom(settingsPreferences);
                    SettingsPreferencesFragment.this.mSettingsPreferences = settingsPreferences;
                    SettingsPreferencesFragment.this.updateConfirmationBarVisibility();
                    StrappUtils.clearStrappAndCalendarCacheData(SettingsPreferencesFragment.this.mSettingsProvider, SettingsPreferencesFragment.this.mCargoConnection);
                    SyncUtils.startStrappsDataSync(SettingsPreferencesFragment.this.getActivity());
                    if (result.intValue() == 1001) {
                        SettingsPreferencesFragment.this.getDialogManager().showDialog(SettingsPreferencesFragment.this.getActivity(), Integer.valueOf((int) R.string.oobe_device_error_dialog_title), Integer.valueOf((int) R.string.profile_band_saving_error), DialogPriority.LOW);
                    }
                } else {
                    SettingsPreferencesFragment.this.getDialogManager().showDialog(SettingsPreferencesFragment.this.getActivity(), Integer.valueOf((int) R.string.oobe_network_error_dialog_title), Integer.valueOf((int) R.string.profile_saving_cloud_error), DialogPriority.LOW);
                }
                SettingsPreferencesFragment.this.setState(1234);
            }

            @Override // com.microsoft.kapp.Callback
            public void onError(Exception ex) {
                SettingsPreferencesFragment.this.setState(1235);
            }
        }));
        saveTask.execute(new Void[0]);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setUIValuesFrom(SettingsPreferences settingsPreferences) {
        Validate.notNull(settingsPreferences, "settingsPreferences");
        this.mWeightSpinner.setSelection(getWeightSelectedIndex(settingsPreferences));
        this.mTemperatureSpinner.setSelection(getTemperatureSelectedIndex(settingsPreferences));
        this.mDistanceHeightSpinner.setSelection(getDistanceHeightSelectedIndex(settingsPreferences));
        this.mShakeToSendFeedbackSwitch.setChecked(this.mSettingsProvider.isShakeToSendFeedbackEnabled());
        if (this.mDevice == BandVersion.NEON) {
            ((View) ViewUtils.getValidView(getView(), R.id.region_format_layout, View.class)).setVisibility(0);
            this.mBandRegionSpinner.setSelection(getRegionSelectedIndex());
            return;
        }
        ((View) ViewUtils.getValidView(getView(), R.id.region_format_layout, View.class)).setVisibility(8);
    }

    private int getRegionSelectedIndex() {
        String region = this.mSettingsProvider.getBandRegionSettings();
        for (int i = 0; i < Constants.REGION_IDENTIFIERS.length; i++) {
            if (Constants.REGION_IDENTIFIERS[i].equalsIgnoreCase(region)) {
                return i;
            }
        }
        return 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void readUIValuesInto(SettingsPreferences settingsPreferences) {
        Validate.notNull(settingsPreferences, "settingsPreferences");
        settingsPreferences.setIsWeightMetric(this.mWeightSpinner.getSelectedItemPosition() == this.mWeightMetricIndex);
        settingsPreferences.setIsTemperatureMetric(this.mTemperatureSpinner.getSelectedItemPosition() == this.mTemperatureMetricIndex);
        settingsPreferences.setIsDistanceHeightMetric(this.mDistanceHeightSpinner.getSelectedItemPosition() == this.mDistanceHeightMetricIndex);
        this.mSettingsProvider.setShakeToSendFeedbackEnabled(this.mShakeToSendFeedbackSwitch.isChecked());
        if (this.mDevice == BandVersion.NEON) {
            settingsPreferences.setUserSelectedBandRegion(Constants.REGION_IDENTIFIERS[this.mBandRegionSpinner.getSelectedItemPosition()]);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setItemChangedListener() {
        AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() { // from class: com.microsoft.kapp.fragments.SettingsPreferencesFragment.7
            @Override // android.widget.AdapterView.OnItemSelectedListener
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SettingsPreferencesFragment.this.updateConfirmationBarVisibility();
                SettingsPreferencesFragment.this.mCloudPreferencesChanged = true;
            }

            @Override // android.widget.AdapterView.OnItemSelectedListener
            public void onNothingSelected(AdapterView<?> parent) {
            }
        };
        this.mWeightSpinner.setOnItemSelectedListener(itemSelectedListener);
        this.mTemperatureSpinner.setOnItemSelectedListener(itemSelectedListener);
        this.mDistanceHeightSpinner.setOnItemSelectedListener(itemSelectedListener);
        this.mBandRegionSpinner.setOnItemSelectedListener(itemSelectedListener);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateConfirmationBarVisibility() {
        if (this.mSettingsPreferences == null) {
            this.mConfirmationBar.setVisibility(8);
        } else if (this.mWeightSpinner.getSelectedItemPosition() != getWeightSelectedIndex(this.mSettingsPreferences) || this.mTemperatureSpinner.getSelectedItemPosition() != getTemperatureSelectedIndex(this.mSettingsPreferences) || this.mDistanceHeightSpinner.getSelectedItemPosition() != getDistanceHeightSelectedIndex(this.mSettingsPreferences) || this.mShakeToSendFeedbackSwitch.isChecked() != this.mSettingsProvider.isShakeToSendFeedbackEnabled() || (this.mDevice == BandVersion.NEON && this.mBandRegionSpinner.getSelectedItemPosition() != getRegionSelectedIndex())) {
            this.mConfirmationBar.setVisibility(0);
        } else {
            this.mConfirmationBar.setVisibility(8);
        }
    }

    private int getWeightSelectedIndex(SettingsPreferences settingsPreferences) {
        return settingsPreferences.isWeightMetric() ? this.mWeightMetricIndex : this.mWeightImperialIndex;
    }

    private int getTemperatureSelectedIndex(SettingsPreferences settingsPreferences) {
        return settingsPreferences.isTemperatureMetric() ? this.mTemperatureMetricIndex : this.mTemperatureImperialIndex;
    }

    private int getDistanceHeightSelectedIndex(SettingsPreferences settingsPreferences) {
        return settingsPreferences.isDistanceHeightMetric() ? this.mDistanceHeightMetricIndex : this.mDistanceHeightImperialIndex;
    }

    private static ArrayAdapter<CharSequence> createSpinnerAdapterFromStringArray(Context context, int stringArrayResourceId) {
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(context, stringArrayResourceId, R.drawable.settings_spinner_item);
        spinnerAdapter.setDropDownViewResource(R.drawable.settings_spinner_dropdown_item);
        return spinnerAdapter;
    }
}

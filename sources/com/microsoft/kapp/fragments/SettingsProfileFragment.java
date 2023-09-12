package com.microsoft.kapp.fragments;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import com.facebook.AppEventsConstants;
import com.microsoft.band.cloud.UserProfileInfo;
import com.microsoft.kapp.R;
import com.microsoft.kapp.UserProfileFetcher;
import com.microsoft.kapp.activities.DialogPriority;
import com.microsoft.kapp.device.CargoUserProfile;
import com.microsoft.kapp.device.ProfileUpdateCallback;
import com.microsoft.kapp.device.ProfileUpdateTask;
import com.microsoft.kapp.diagnostics.Telemetry;
import com.microsoft.kapp.diagnostics.TelemetryConstants;
import com.microsoft.kapp.diagnostics.Validate;
import com.microsoft.kapp.models.Length;
import com.microsoft.kapp.models.Weight;
import com.microsoft.kapp.services.SettingsProvider;
import com.microsoft.kapp.tasks.SettingsProfileSaveTask;
import com.microsoft.kapp.utils.ActivityUtils;
import com.microsoft.kapp.utils.AppConfigurationManager;
import com.microsoft.kapp.utils.CommonUtils;
import com.microsoft.kapp.utils.GsonUtils;
import com.microsoft.kapp.utils.ProfileUtils;
import com.microsoft.kapp.utils.ViewUtils;
import com.microsoft.kapp.widgets.ConfirmationBar;
import com.microsoft.krestsdk.auth.MsaAuth;
import com.microsoft.krestsdk.auth.credentials.CredentialsManager;
import com.microsoft.krestsdk.models.Configuration;
import com.microsoft.krestsdk.models.TemperatureType;
import com.microsoft.krestsdk.models.UnitType;
import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.inject.Inject;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
/* loaded from: classes.dex */
public class SettingsProfileFragment extends BaseFragmentWithOfflineSupport implements ProfileUpdateCallback {
    static final /* synthetic */ boolean $assertionsDisabled;
    private static final String EXTRA_IS_OOBE_MODE_ACTIVE = "extra_is_oobe_mode_active";
    private static final String EXTRA_PROFILE = "extra_profile";
    private static int mDefaultBirthMonth;
    private static int mDefaultBirthYear;
    private static int mEditTextColor;
    private static int mFocusTextColor;
    private CheckBox mAllowMarketingEmailCheckBox;
    @Inject
    AppConfigurationManager mAppConfigurationManager;
    @Inject
    MsaAuth mAuthService;
    private Spinner mBirthMonthSpinner;
    private TextView mBirthYearError;
    private int mBirthYearMax;
    private int mBirthYearMin;
    private Spinner mBirthYearSpinner;
    private ArrayAdapter<String> mBirthYearSpinnerAdapter;
    private ConfirmationBar mConfirmationBar;
    @Inject
    CredentialsManager mCredentialsManager;
    private int mGenderFemaleIndex;
    private int mGenderMaleIndex;
    private Spinner mGenderSpinner;
    private int mHeightCentimetersMax;
    private int mHeightCentimetersMin;
    private EditText mHeightCentimetersTextView;
    private TextView mHeightEntryErrorMessage;
    private int mHeightFeetMax;
    private int mHeightFeetMin;
    private EditText mHeightFeetTextView;
    private ViewGroup mHeightImperialViewGroup;
    private int mHeightInchesMax;
    private int mHeightInchesMin;
    private EditText mHeightInchesTextView;
    private ViewGroup mHeightMetricViewGroup;
    private int mImperialHeightMax;
    private int mImperialHeightMin;
    private LinearLayout mMainUIContainer;
    private int mMaxHeightInches;
    private int mMinHeightInches;
    private EditText mNameTextView;
    private ViewGroup mOobeButtonGroup;
    private Button mOobeDoneButton;
    private CargoUserProfile mProfile;
    private TextView mProfileBirthYearLabel;
    private TextView mProfileHeightFeetLabel;
    private TextView mProfileHeightInchesLabel;
    private TextView mProfileImperialWeightLabel;
    private TextView mProfileMetricHeightLabel;
    private TextView mProfileMetricWeightLabel;
    private TextView mProfileNameLabel;
    private ProfileUpdateTask mProfileTask;
    private TextView mProfileZipCodeLabel;
    @Inject
    SettingsProvider mSettingsProvider;
    private TextView mSubtitle;
    @Inject
    UserProfileFetcher mUserProfileFetcher;
    private MeasureValidation mValidateHeight;
    private MeasureValidation mValidateWeight;
    private TextView mWeightEntryErrorMessage;
    private ViewGroup mWeightImperialViewGroup;
    private int mWeightKilogramsMax;
    private int mWeightKilogramsMin;
    private EditText mWeightKilogramsTextView;
    private ViewGroup mWeightMetricViewGroup;
    private int mWeightPoundsMax;
    private int mWeightPoundsMin;
    private EditText mWeightPoundsTextView;
    private TextView mZipCodeEntryErrorMessage;
    private EditText mZipCodeTextView;
    private MeasureValidation mValidateInches = null;
    private boolean mIsWeightMetric = true;
    private boolean mIsDistanceHeightMetric = true;
    private boolean mIsOobeModeActive = false;
    private boolean mIsSaveInProgress = false;
    private AtomicBoolean mIsDataLoaded = new AtomicBoolean(false);
    private final View.OnClickListener mOnCheckBoxClicked = new View.OnClickListener() { // from class: com.microsoft.kapp.fragments.SettingsProfileFragment.9
        @Override // android.view.View.OnClickListener
        public void onClick(View v) {
            SettingsProfileFragment.this.updateConfirmationBarVisibility(SettingsProfileFragment.this.mProfile, SettingsProfileFragment.this.mIsWeightMetric, SettingsProfileFragment.this.mIsDistanceHeightMetric);
        }
    };

    /* loaded from: classes.dex */
    public interface SettingsProfileFragmentCalls {
        void onProfileCompleted();
    }

    static {
        $assertionsDisabled = !SettingsProfileFragment.class.desiredAssertionStatus();
    }

    public static SettingsProfileFragment newInstance(boolean isOobeModeActive) {
        SettingsProfileFragment newInstance = new SettingsProfileFragment();
        newInstance.mIsOobeModeActive = isOobeModeActive;
        return newInstance;
    }

    @Override // com.microsoft.kapp.fragments.BaseFragment, android.support.v4.app.Fragment
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            this.mIsOobeModeActive = savedInstanceState.getBoolean(EXTRA_IS_OOBE_MODE_ACTIVE, false);
            this.mProfile = (CargoUserProfile) (savedInstanceState.containsKey(EXTRA_PROFILE) ? savedInstanceState.getSerializable(EXTRA_PROFILE) : null);
        }
        this.mBirthYearMax = new GregorianCalendar().get(1);
        this.mBirthYearMin = this.mBirthYearMax - getInteger(R.integer.settings_profile_age_max);
        mDefaultBirthMonth = getInteger(R.integer.settings_profile_default_birth_month);
        mDefaultBirthYear = getInteger(R.integer.settings_profile_default_birth_year);
        this.mWeightKilogramsMin = getInteger(R.integer.settings_profile_weight_kilograms_min);
        this.mWeightKilogramsMax = getInteger(R.integer.settings_profile_weight_kilograms_max);
        this.mWeightPoundsMin = getInteger(R.integer.settings_profile_weight_pounds_min);
        this.mWeightPoundsMax = getInteger(R.integer.settings_profile_weight_pounds_max);
        this.mHeightCentimetersMin = getInteger(R.integer.settings_profile_height_centimeters_min);
        this.mHeightCentimetersMax = getInteger(R.integer.settings_profile_height_centimeters_max);
        this.mHeightFeetMin = getInteger(R.integer.settings_profile_height_feet_min);
        this.mHeightFeetMax = getInteger(R.integer.settings_profile_height_feet_max);
        this.mHeightInchesMin = getInteger(R.integer.settings_profile_height_inches_min);
        this.mHeightInchesMax = getInteger(R.integer.settings_profile_height_inches_max);
        this.mMinHeightInches = getInteger(R.integer.settings_profile_min_height_inches);
        this.mMaxHeightInches = getInteger(R.integer.settings_profile_max_height_inches);
        this.mImperialHeightMin = (this.mHeightFeetMin * 12) + this.mMinHeightInches;
        this.mImperialHeightMax = (this.mHeightFeetMax * 12) + this.mMaxHeightInches;
        mFocusTextColor = getResources().getColor(R.color.oobe_edit_text_text_color_focused);
        mEditTextColor = getResources().getColor(this.mIsOobeModeActive ? R.color.BlackColor : R.color.WhiteColor);
    }

    private int getInteger(int resource) {
        return getResources().getInteger(resource);
    }

    @Override // com.microsoft.kapp.fragments.BaseFragment, android.support.v4.app.Fragment
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(EXTRA_IS_OOBE_MODE_ACTIVE, this.mIsOobeModeActive);
        outState.putSerializable(EXTRA_PROFILE, this.mProfile);
    }

    @Override // com.microsoft.kapp.fragments.BaseFragmentWithOfflineSupport
    public View onCreateChildView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (!this.mIsOobeModeActive && !CommonUtils.isNetworkAvailable(getActivity())) {
            DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() { // from class: com.microsoft.kapp.fragments.SettingsProfileFragment.1
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialog, int which) {
                    ActivityUtils.performBackButton(SettingsProfileFragment.this.getActivity());
                }
            };
            getDialogManager().showNetworkErrorDialogWithCallback(getActivity(), listener);
        }
        View rootView = inflater.inflate(this.mIsOobeModeActive ? R.layout.fragment_settings_profile_oobe : R.layout.fragment_settings_profile, container, false);
        TextWatcher genericTextWatcher = new TextWatcher() { // from class: com.microsoft.kapp.fragments.SettingsProfileFragment.2
            @Override // android.text.TextWatcher
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override // android.text.TextWatcher
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override // android.text.TextWatcher
            public void afterTextChanged(Editable s) {
                SettingsProfileFragment.this.updateConfirmationBarVisibility(SettingsProfileFragment.this.mProfile, SettingsProfileFragment.this.mIsWeightMetric, SettingsProfileFragment.this.mIsDistanceHeightMetric);
            }
        };
        AdapterView.OnItemSelectedListener genericSpinnerChangedListener = new AdapterView.OnItemSelectedListener() { // from class: com.microsoft.kapp.fragments.SettingsProfileFragment.3
            @Override // android.widget.AdapterView.OnItemSelectedListener
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SettingsProfileFragment.this.updateConfirmationBarVisibility(SettingsProfileFragment.this.mProfile, SettingsProfileFragment.this.mIsWeightMetric, SettingsProfileFragment.this.mIsDistanceHeightMetric);
            }

            @Override // android.widget.AdapterView.OnItemSelectedListener
            public void onNothingSelected(AdapterView<?> parent) {
            }
        };
        int spinnerItem = this.mIsOobeModeActive ? R.drawable.oobe_spinner_item : R.drawable.settings_spinner_item;
        this.mMainUIContainer = (LinearLayout) ViewUtils.getValidView(rootView, R.id.settings_profile_linear_layout, LinearLayout.class);
        this.mSubtitle = (TextView) ViewUtils.getValidView(rootView, R.id.settings_profile_subtitle, TextView.class);
        if (this.mIsOobeModeActive) {
            this.mSubtitle.setText(getString(R.string.oobe_profile_subtitle));
            this.mSubtitle.setVisibility(0);
            this.mMainUIContainer.setPadding(0, 0, 0, 0);
        }
        this.mProfileNameLabel = (TextView) ViewUtils.getValidView(rootView, R.id.settings_profile_name_prompt, TextView.class);
        this.mProfileMetricWeightLabel = (TextView) ViewUtils.getValidView(rootView, R.id.settings_profile_weight_metric_prompt, TextView.class);
        this.mProfileMetricHeightLabel = (TextView) ViewUtils.getValidView(rootView, R.id.settings_profile_height_metric_prompt, TextView.class);
        this.mProfileImperialWeightLabel = (TextView) ViewUtils.getValidView(rootView, R.id.settings_profile_weight_imperial_prompt, TextView.class);
        this.mProfileHeightFeetLabel = (TextView) ViewUtils.getValidView(rootView, R.id.settings_profile_height_feet_prompt, TextView.class);
        this.mProfileHeightInchesLabel = (TextView) ViewUtils.getValidView(rootView, R.id.settings_profile_height_inches_prompt, TextView.class);
        this.mProfileZipCodeLabel = (TextView) ViewUtils.getValidView(rootView, R.id.settings_profile_zip_code_prompt, TextView.class);
        this.mWeightEntryErrorMessage = (TextView) ViewUtils.getValidView(rootView, R.id.settings_profile_weight_error_message, TextView.class);
        this.mHeightEntryErrorMessage = (TextView) ViewUtils.getValidView(rootView, R.id.settings_profile_height_error_message, TextView.class);
        this.mSubtitle = (TextView) ViewUtils.getValidView(rootView, R.id.settings_profile_subtitle, TextView.class);
        this.mNameTextView = (EditText) ViewUtils.getValidView(rootView, R.id.settings_profile_name, EditText.class);
        this.mNameTextView.addTextChangedListener(genericTextWatcher);
        this.mGenderSpinner = (Spinner) ViewUtils.getValidView(rootView, R.id.settings_profile_gender_spinner, Spinner.class);
        ArrayAdapter<CharSequence> genderSpinnerAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.profile_gender_values, spinnerItem);
        genderSpinnerAdapter.setDropDownViewResource(R.drawable.settings_spinner_dropdown_item);
        this.mGenderMaleIndex = genderSpinnerAdapter.getPosition(getString(R.string.profile_gender_male));
        this.mGenderFemaleIndex = genderSpinnerAdapter.getPosition(getString(R.string.profile_gender_female));
        this.mProfileBirthYearLabel = (TextView) ViewUtils.getValidView(rootView, R.id.settings_profile_birth_year_prompt, TextView.class);
        this.mGenderSpinner.setAdapter((SpinnerAdapter) genderSpinnerAdapter);
        this.mGenderSpinner.setOnItemSelectedListener(genericSpinnerChangedListener);
        this.mBirthYearError = (TextView) ViewUtils.getValidView(rootView, R.id.settings_profile_birth_year_error, TextView.class);
        this.mBirthMonthSpinner = (Spinner) ViewUtils.getValidView(rootView, R.id.settings_profile_birth_month, Spinner.class);
        String[] months = new DateFormatSymbols(Locale.getDefault()).getMonths();
        ArrayAdapter<String> birthMonthSpinnerAdapter = new ArrayAdapter<>(getActivity(), spinnerItem, months);
        birthMonthSpinnerAdapter.setDropDownViewResource(R.drawable.settings_spinner_dropdown_item);
        this.mBirthMonthSpinner.setAdapter((SpinnerAdapter) birthMonthSpinnerAdapter);
        this.mBirthYearSpinner = (Spinner) ViewUtils.getValidView(rootView, R.id.settings_profile_birth_year, Spinner.class);
        ArrayList<String> years = new ArrayList<>();
        for (int i = this.mBirthYearMin; i <= this.mBirthYearMax; i++) {
            years.add(Integer.toString(i));
        }
        this.mBirthYearSpinnerAdapter = new ArrayAdapter<>(getActivity(), spinnerItem, years);
        this.mBirthYearSpinnerAdapter.setDropDownViewResource(R.drawable.settings_spinner_dropdown_item);
        this.mBirthYearSpinner.setAdapter((SpinnerAdapter) this.mBirthYearSpinnerAdapter);
        this.mBirthYearSpinner.setOnItemSelectedListener(genericSpinnerChangedListener);
        this.mBirthMonthSpinner.setOnItemSelectedListener(genericSpinnerChangedListener);
        this.mWeightKilogramsTextView = (EditText) ViewUtils.getValidView(rootView, R.id.settings_profile_weight_kilograms, EditText.class);
        this.mWeightKilogramsTextView.addTextChangedListener(genericTextWatcher);
        this.mWeightPoundsTextView = (EditText) ViewUtils.getValidView(rootView, R.id.settings_profile_weight_pounds, EditText.class);
        this.mWeightPoundsTextView.addTextChangedListener(genericTextWatcher);
        this.mHeightCentimetersTextView = (EditText) ViewUtils.getValidView(rootView, R.id.settings_profile_height_centimeters, EditText.class);
        this.mHeightCentimetersTextView.addTextChangedListener(genericTextWatcher);
        this.mHeightFeetTextView = (EditText) ViewUtils.getValidView(rootView, R.id.settings_profile_height_feet, EditText.class);
        this.mHeightFeetTextView.addTextChangedListener(genericTextWatcher);
        this.mHeightInchesTextView = (EditText) ViewUtils.getValidView(rootView, R.id.settings_profile_height_inches, EditText.class);
        this.mHeightInchesTextView.addTextChangedListener(genericTextWatcher);
        this.mZipCodeTextView = (EditText) ViewUtils.getValidView(rootView, R.id.settings_profile_zip_code, EditText.class);
        this.mZipCodeTextView.addTextChangedListener(genericTextWatcher);
        this.mWeightMetricViewGroup = (ViewGroup) ViewUtils.getValidView(rootView, R.id.settings_profile_weight_metric_container, ViewGroup.class);
        this.mWeightImperialViewGroup = (ViewGroup) ViewUtils.getValidView(rootView, R.id.settings_profile_weight_imperial_container, ViewGroup.class);
        setWeightParameters(this.mIsWeightMetric);
        this.mHeightMetricViewGroup = (ViewGroup) ViewUtils.getValidView(rootView, R.id.settings_profile_height_metric_container, ViewGroup.class);
        this.mHeightImperialViewGroup = (ViewGroup) ViewUtils.getValidView(rootView, R.id.settings_profile_height_imperial_container, ViewGroup.class);
        setHeightParameters(this.mIsDistanceHeightMetric);
        this.mAllowMarketingEmailCheckBox = (CheckBox) ViewUtils.getValidView(rootView, R.id.marketing_opt_in, CheckBox.class);
        this.mAllowMarketingEmailCheckBox.setOnClickListener(this.mOnCheckBoxClicked);
        if (this.mIsOobeModeActive) {
            this.mOobeButtonGroup = (ViewGroup) ViewUtils.getValidView(rootView, R.id.settings_profile_oobe_button_group, ViewGroup.class);
            this.mOobeDoneButton = (Button) ViewUtils.getValidView(rootView, R.id.oobe_confirm, Button.class);
            this.mOobeDoneButton.setText(R.string.label_next);
            ((Button) ViewUtils.getValidView(rootView, R.id.oobe_cancel, Button.class)).setVisibility(8);
            this.mOobeButtonGroup.setVisibility(0);
            this.mOobeDoneButton.setOnClickListener(new View.OnClickListener() { // from class: com.microsoft.kapp.fragments.SettingsProfileFragment.4
                @Override // android.view.View.OnClickListener
                public void onClick(View v) {
                    if (!SettingsProfileFragment.this.mIsSaveInProgress) {
                        SettingsProfileFragment.this.onOobeDonePressed(SettingsProfileFragment.this.mProfile, SettingsProfileFragment.this.mIsWeightMetric, SettingsProfileFragment.this.mIsDistanceHeightMetric);
                    }
                }
            });
        } else {
            this.mConfirmationBar = (ConfirmationBar) ViewUtils.getValidView(rootView, R.id.settings_profile_confirmation_bar, ConfirmationBar.class);
            this.mConfirmationBar.setOnCancelButtonClickListener(new View.OnClickListener() { // from class: com.microsoft.kapp.fragments.SettingsProfileFragment.5
                @Override // android.view.View.OnClickListener
                public void onClick(View v) {
                    if (!SettingsProfileFragment.this.mIsSaveInProgress) {
                        SettingsProfileFragment.this.confirmationBarOnCancel(SettingsProfileFragment.this.mProfile, SettingsProfileFragment.this.mIsWeightMetric, SettingsProfileFragment.this.mIsDistanceHeightMetric);
                    }
                }
            });
            this.mConfirmationBar.setOnConfirmButtonClickListener(new View.OnClickListener() { // from class: com.microsoft.kapp.fragments.SettingsProfileFragment.6
                @Override // android.view.View.OnClickListener
                public void onClick(View v) {
                    if (!SettingsProfileFragment.this.mIsSaveInProgress) {
                        SettingsProfileFragment.this.confirmationBarOnConfirm(SettingsProfileFragment.this.mProfile, SettingsProfileFragment.this.mIsWeightMetric, SettingsProfileFragment.this.mIsDistanceHeightMetric);
                    }
                }
            });
        }
        View.OnKeyListener spinnerKeyListener = new View.OnKeyListener() { // from class: com.microsoft.kapp.fragments.SettingsProfileFragment.7
            @Override // android.view.View.OnKeyListener
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                ViewUtils.closeSoftKeyboard(SettingsProfileFragment.this.getActivity(), v);
                return false;
            }
        };
        View.OnTouchListener spinnerTouchListener = new View.OnTouchListener() { // from class: com.microsoft.kapp.fragments.SettingsProfileFragment.8
            @Override // android.view.View.OnTouchListener
            public boolean onTouch(View v, MotionEvent event) {
                ViewUtils.closeSoftKeyboard(SettingsProfileFragment.this.getActivity(), v);
                return false;
            }
        };
        this.mBirthMonthSpinner.setOnTouchListener(spinnerTouchListener);
        this.mBirthYearSpinner.setOnTouchListener(spinnerTouchListener);
        this.mGenderSpinner.setOnTouchListener(spinnerTouchListener);
        this.mBirthMonthSpinner.setOnKeyListener(spinnerKeyListener);
        this.mBirthYearSpinner.setOnKeyListener(spinnerKeyListener);
        this.mGenderSpinner.setOnKeyListener(spinnerKeyListener);
        return rootView;
    }

    @Override // android.support.v4.app.Fragment
    public void onStart() {
        super.onStart();
        loadUserProfile();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void loadUserProfile() {
        if (this.mProfileTask == null || (this.mProfile == null && this.mProfileTask.getStatus() == AsyncTask.Status.FINISHED)) {
            setState(1233);
            this.mProfileTask = new ProfileUpdateTask(this.mCargoConnection, this.mSettingsProvider, this.mUserProfileFetcher, this.mAuthService, this, this);
            this.mProfileTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
        }
    }

    @Override // com.microsoft.kapp.fragments.BaseFragment, android.support.v4.app.Fragment
    public void onResume() {
        super.onResume();
        if (this.mIsOobeModeActive) {
            Telemetry.logPage(TelemetryConstants.PageViews.OOBE_PROFILE_EDITING);
        } else {
            Telemetry.logPage(TelemetryConstants.PageViews.SETTINGS_USER_PROFILE);
        }
        setTopMenuBarColor(getResources().getColor(R.color.settingsMenuBar));
        if (this.mIsDataLoaded.get()) {
            setState(1234);
        }
    }

    @Override // android.support.v4.app.Fragment
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (this.mIsOobeModeActive && !CommonUtils.isNetworkAvailable(getActivity())) {
            ensureNetworkConnectivityAtStartup();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void ensureNetworkConnectivityAtStartup() {
        getDialogManager().showDialog(getActivity(), Integer.valueOf((int) R.string.oobe_network_error_dialog_title), Integer.valueOf((int) R.string.oobe_network_error_dialog_message), new DialogInterface.OnClickListener() { // from class: com.microsoft.kapp.fragments.SettingsProfileFragment.10
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) {
                if (!CommonUtils.isNetworkAvailable(SettingsProfileFragment.this.getActivity())) {
                    SettingsProfileFragment.this.ensureNetworkConnectivityAtStartup();
                } else {
                    SettingsProfileFragment.this.loadUserProfile();
                }
            }
        }, DialogPriority.HIGH);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void confirmationBarOnCancel(CargoUserProfile profile, boolean isWeightMetric, boolean isDistanceHeightMetric) {
        clearAllProfileErrorViews();
        setUIValuesFrom(profile, isWeightMetric, isDistanceHeightMetric);
        updateConfirmationBarVisibility(profile, isWeightMetric, isDistanceHeightMetric);
        ViewUtils.closeSoftKeyboard(getActivity(), this.mConfirmationBar);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void confirmationBarOnConfirm(CargoUserProfile profile, boolean isWeightMetric, boolean isDistanceHeightMetric) {
        clearAllProfileErrorViews();
        boolean error = validateUIValues();
        if (!error) {
            CargoUserProfile mutableProfile = (CargoUserProfile) profile.clone();
            readUIValuesInto(mutableProfile, isWeightMetric, isDistanceHeightMetric);
            saveSettingsProfile(mutableProfile);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onOobeDonePressed(CargoUserProfile profile, boolean isWeightMetric, boolean isDistanceHeightMetric) {
        clearAllProfileErrorViews();
        boolean error = validateUIValues();
        if (!error) {
            CargoUserProfile mutableProfile = (CargoUserProfile) profile.clone();
            mutableProfile.setDisplayDistanceHeightMetric(isDistanceHeightMetric);
            mutableProfile.setDisplayTemperatureMetric(this.mSettingsProvider.isTemperatureMetric());
            mutableProfile.setDisplayWeightMetric(isWeightMetric);
            readUIValuesInto(mutableProfile, isWeightMetric, isDistanceHeightMetric);
            String hwagJson = GsonUtils.getCustomSerializer().toJson(mutableProfile);
            if (this.mSettingsProvider.setOobeUserProfile(hwagJson)) {
                notifyActivityOfCompletion();
            }
        }
    }

    @Override // com.microsoft.kapp.device.ProfileUpdateCallback
    public void onProfileRetrieved(CargoUserProfile cargoUserProfile) {
        if (cargoUserProfile == null || getView() == null) {
            setState(1235);
            return;
        }
        if (this.mIsOobeModeActive && this.mAppConfigurationManager != null && this.mAppConfigurationManager.getConfiguration() != null) {
            Configuration config = this.mAppConfigurationManager.getConfiguration();
            if (config.getOOBE() != null && config.getOOBE().getOOBEDefaults() != null) {
                this.mSettingsProvider.setIsWeightMetric(config.getOOBE().getOOBEDefaults().getWeightUnit() == UnitType.METRIC);
                this.mSettingsProvider.setIsDistanceHeightMetric(config.getOOBE().getOOBEDefaults().getDistanceUnit() == UnitType.METRIC);
                this.mSettingsProvider.setIsTemperatureMetric(config.getOOBE().getOOBEDefaults().getTemperatureUnit() == TemperatureType.CELSIUS);
            }
        }
        this.mIsWeightMetric = this.mSettingsProvider.isWeightMetric();
        this.mIsDistanceHeightMetric = this.mSettingsProvider.isDistanceHeightMetric();
        setUIValuesFrom(cargoUserProfile, this.mIsWeightMetric, this.mIsDistanceHeightMetric);
        this.mProfile = cargoUserProfile;
        if (this.mIsOobeModeActive) {
            this.mOobeDoneButton.setEnabled(true);
        }
        this.mIsDataLoaded.compareAndSet(false, true);
        setState(1234);
    }

    private void setUIValuesFrom(CargoUserProfile profile, boolean isWeightMetric, boolean isDistanceHeightMetric) {
        if (!$assertionsDisabled && profile == null) {
            throw new AssertionError();
        }
        String firstName = getFirstName(profile);
        if (StringUtils.isNotBlank(firstName)) {
            this.mNameTextView.setText(firstName);
        }
        this.mBirthMonthSpinner.setSelection(getBirthMonth(profile) - 1);
        int currentSelection = this.mBirthYearSpinnerAdapter.getPosition(getBirthYearString(profile));
        if (currentSelection >= 0) {
            this.mBirthYearSpinner.setSelection(currentSelection);
        }
        this.mGenderSpinner.setSelection(getGenderIndex(profile, this.mGenderMaleIndex, this.mGenderFemaleIndex));
        this.mWeightKilogramsTextView.setText(getWeightKilogramsString(profile));
        this.mWeightPoundsTextView.setText(getWeightPoundsString(profile));
        this.mHeightCentimetersTextView.setText(getHeightCentimetersString(profile));
        this.mHeightFeetTextView.setText(getHeightFeetString(profile));
        this.mHeightInchesTextView.setText(getHeightInchesString(profile));
        setWeightParameters(isWeightMetric);
        setHeightParameters(isDistanceHeightMetric);
        String zipCode = getZipCodeString(profile);
        this.mZipCodeTextView.setText(zipCode);
        if (this.mIsOobeModeActive) {
            boolean checkFlag = false;
            if (this.mAppConfigurationManager.getConfiguration() != null) {
                Configuration config = this.mAppConfigurationManager.getConfiguration();
                if (config.getOOBE() != null && config.getOOBE().getOOBEDefaults() != null) {
                    checkFlag = this.mAppConfigurationManager.getConfiguration().getOOBE().getOOBEDefaults().isMarketingOptIn();
                }
            }
            this.mAllowMarketingEmailCheckBox.setChecked(checkFlag);
            return;
        }
        this.mAllowMarketingEmailCheckBox.setChecked(getMarketingEmailOption(profile));
    }

    private void readUIValuesInto(CargoUserProfile profile, boolean isWeightMetric, boolean isDistanceHeightMetric) {
        profile.setFirstName(getUIFirstName());
        profile.setBirthdate(ProfileUtils.getBirthdate(Integer.parseInt(getUIBirthMonth()), Integer.parseInt(getUIBirthYear())));
        profile.setGender(getUIGenderIndex() == this.mGenderMaleIndex ? UserProfileInfo.Gender.male : UserProfileInfo.Gender.female);
        if (isWeightMetric) {
            profile.setWeight(Weight.fromKilograms(Integer.parseInt(getUIWeightKilograms())));
        } else {
            profile.setWeight(Weight.fromPounds(Integer.parseInt(getUIWeightPounds())));
        }
        if (isDistanceHeightMetric) {
            profile.setHeight(Length.fromCentimeters(Integer.parseInt(getUIHeightCentimeters())));
        } else {
            profile.setHeight(Length.fromImperial(Integer.parseInt(getUIHeightFeet()), Integer.parseInt(getUIHeightInches())));
        }
        profile.setZipCode(getZipCode());
        profile.setAllowMarketingEmail(isAllowMarketingEmailEnabled());
    }

    private boolean validateUIValues() {
        boolean errorFlag = false;
        int maxAllowedYear = this.mBirthYearMax - getInteger(R.integer.settings_profile_age_min);
        if (Integer.valueOf((String) this.mBirthYearSpinner.getSelectedItem()).intValue() > maxAllowedYear) {
            setProfileErrorView((TextView) this.mBirthYearSpinner.getChildAt(0), this.mProfileBirthYearLabel, this.mBirthYearError, String.format(getResources().getString(R.string.settings_profile_birth_year_invalid_message), Integer.valueOf(getInteger(R.integer.settings_profile_age_min)), Integer.valueOf(getInteger(R.integer.settings_profile_age_max))));
            this.mBirthYearSpinner.setBackgroundResource(this.mIsOobeModeActive ? R.drawable.oobe_edit_text_background_selector_error : R.drawable.settings_edit_text_background_selector_error);
            errorFlag = true;
        }
        if (this.mNameTextView.getText().toString().trim().length() == 0) {
            setProfileErrorView(this.mNameTextView, this.mProfileNameLabel, null, null);
            errorFlag = true;
        }
        boolean errorFlag2 = this.mValidateWeight.invalidIntegerRange(this.mValidateWeight.getEditTextValue()) || errorFlag;
        int editTextValue = this.mValidateHeight.getEditTextValue();
        if (this.mIsDistanceHeightMetric) {
            return this.mValidateHeight.invalidIntegerRange(editTextValue) || errorFlag2;
        }
        int inchesValue = this.mValidateInches.getEditTextValue();
        if (this.mValidateInches.invalidIntegerRange(inchesValue)) {
            return true;
        }
        return this.mValidateHeight.invalidIntegerRange((editTextValue * 12) + inchesValue) || errorFlag2;
    }

    public final void setProfileErrorView(TextView editTextView, TextView labelTextView, TextView errorTextView, String errorMessage) {
        int i = R.drawable.oobe_edit_text_background_selector_error;
        if (labelTextView == this.mProfileHeightFeetLabel || labelTextView == this.mProfileHeightInchesLabel) {
            resetEditTextColorsWithFocus(this.mHeightFeetTextView);
            this.mHeightFeetTextView.setBackgroundResource(this.mIsOobeModeActive ? R.drawable.oobe_edit_text_background_selector_error : R.drawable.settings_edit_text_background_selector_error);
            resetEditTextColorsWithFocus(this.mHeightInchesTextView);
            EditText editText = this.mHeightInchesTextView;
            if (!this.mIsOobeModeActive) {
                i = R.drawable.settings_edit_text_background_selector_error;
            }
            editText.setBackgroundResource(i);
        } else {
            resetEditTextColorsWithFocus(editTextView);
            if (!this.mIsOobeModeActive) {
                i = R.drawable.settings_edit_text_background_selector_error;
            }
            editTextView.setBackgroundResource(i);
        }
        if (errorTextView != null) {
            errorTextView.setVisibility(0);
            errorTextView.setText(errorMessage);
        }
    }

    private void clearProfileErrorView(EditText editTextView, TextView labelTextView) {
        clearProfileErrorView(editTextView, labelTextView, null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void clearProfileErrorView(TextView editTextView, TextView labelTextView, TextView errorTextView) {
        int i = R.drawable.oobe_edit_text_background_selector;
        if (labelTextView == this.mProfileHeightFeetLabel || labelTextView == this.mProfileHeightInchesLabel) {
            resetEditTextColorsWithFocus(this.mHeightFeetTextView);
            this.mHeightFeetTextView.setBackgroundResource(this.mIsOobeModeActive ? R.drawable.oobe_edit_text_background_selector : R.drawable.settings_edit_text_background_selector);
            resetEditTextColorsWithFocus(this.mHeightInchesTextView);
            EditText editText = this.mHeightInchesTextView;
            if (!this.mIsOobeModeActive) {
                i = R.drawable.settings_edit_text_background_selector;
            }
            editText.setBackgroundResource(i);
        } else {
            resetEditTextColorsWithFocus(editTextView);
            if (!this.mIsOobeModeActive) {
                i = R.drawable.settings_edit_text_background_selector;
            }
            editTextView.setBackgroundResource(i);
        }
        if (errorTextView != null) {
            errorTextView.setVisibility(8);
        }
    }

    private void clearAllProfileErrorViews() {
        clearProfileErrorView(this.mNameTextView, this.mProfileNameLabel);
        this.mValidateWeight.clearErrorView();
        this.mValidateHeight.clearErrorView();
        if (!this.mIsDistanceHeightMetric) {
            this.mValidateInches.clearErrorView();
        }
        clearProfileErrorView((TextView) this.mBirthYearSpinner.getChildAt(0), this.mProfileBirthYearLabel, this.mBirthYearError);
        this.mBirthYearSpinner.setBackgroundResource(this.mIsOobeModeActive ? R.drawable.oobe_spinner_background_selector : R.drawable.settings_spinner_background_selector);
        this.mBirthYearSpinner.getChildAt(0).setBackgroundResource(R.color.transparent);
        clearProfileErrorView(this.mZipCodeTextView, this.mProfileZipCodeLabel, this.mZipCodeEntryErrorMessage);
    }

    private static void resetEditTextColorsWithFocus(TextView editTextView) {
        int[][] states = {new int[]{16842908}, new int[]{16842910}};
        int[] colors = {mFocusTextColor, mEditTextColor};
        ColorStateList list = new ColorStateList(states, colors);
        editTextView.setTextColor(list);
    }

    private void saveSettingsProfile(final CargoUserProfile cargoUserProfile) {
        Validate.notNull(cargoUserProfile, "cargoUserProfile");
        SettingsProfileSaveTask saveTask = new SettingsProfileSaveTask(this.mCargoConnection, this.mSettingsProvider, cargoUserProfile, this.mUserProfileFetcher, this, Boolean.valueOf(this.mIsOobeModeActive), false, getActivity()) { // from class: com.microsoft.kapp.fragments.SettingsProfileFragment.11
            /* JADX INFO: Access modifiers changed from: protected */
            @Override // com.microsoft.kapp.ScopedAsyncTask
            public void onPreExecute() {
                SettingsProfileFragment.this.mIsSaveInProgress = true;
                SettingsProfileFragment.this.setState(1233);
                super.onPreExecute();
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // com.microsoft.kapp.ScopedAsyncTask
            public void onCancelled(Integer result) {
                super.onCancelled((AnonymousClass11) result);
                SettingsProfileFragment.this.mIsSaveInProgress = false;
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // com.microsoft.kapp.ScopedAsyncTask
            public void onPostExecute(Integer result) {
                SettingsProfileFragment.this.mIsSaveInProgress = false;
                SettingsProfileFragment.this.setState(1234);
                if (result.intValue() == 9003 || result.intValue() == 9001) {
                    SettingsProfileFragment.this.mProfile = cargoUserProfile;
                    if (SettingsProfileFragment.this.mIsOobeModeActive) {
                        ViewUtils.closeSoftKeyboard(SettingsProfileFragment.this.getActivity(), SettingsProfileFragment.this.mOobeDoneButton);
                    } else {
                        ViewUtils.closeSoftKeyboard(SettingsProfileFragment.this.getActivity(), SettingsProfileFragment.this.mConfirmationBar);
                        SettingsProfileFragment.this.mConfirmationBar.setVisibility(8);
                    }
                    if (result.intValue() == 9001 && SettingsProfileFragment.this.mMultiDeviceManager.hasBand()) {
                        SettingsProfileFragment.this.getDialogManager().showDialog(SettingsProfileFragment.this.getActivity(), Integer.valueOf((int) R.string.oobe_device_error_dialog_title), Integer.valueOf((int) R.string.profile_band_saving_error), DialogPriority.LOW);
                    }
                    SettingsProfileFragment.this.notifyActivityOfCompletion();
                    return;
                }
                SettingsProfileFragment.this.getDialogManager().showDialog(SettingsProfileFragment.this.getActivity(), Integer.valueOf((int) R.string.oobe_network_error_dialog_title), Integer.valueOf((int) R.string.oobe_connection_with_cloud_error), DialogPriority.LOW);
            }
        };
        saveTask.execute(new Void[0]);
    }

    private void setWeightParameters(boolean isWeightMetric) {
        if (isWeightMetric) {
            String errorMessage = String.format(getResources().getString(R.string.settings_profile_weight_kilograms_invalid_message), Integer.valueOf(this.mWeightKilogramsMin), Integer.valueOf(this.mWeightKilogramsMax));
            this.mValidateWeight = new MeasureValidation(this.mWeightKilogramsTextView, this.mProfileMetricWeightLabel, this.mWeightKilogramsMin, this.mWeightKilogramsMax, this.mWeightEntryErrorMessage, errorMessage);
            this.mWeightMetricViewGroup.setVisibility(0);
            this.mWeightImperialViewGroup.setVisibility(8);
            return;
        }
        String errorMessage2 = String.format(getResources().getString(R.string.settings_profile_weight_pounds_invalid_message), Integer.valueOf(this.mWeightPoundsMin), Integer.valueOf(this.mWeightPoundsMax));
        this.mValidateWeight = new MeasureValidation(this.mWeightPoundsTextView, this.mProfileImperialWeightLabel, this.mWeightPoundsMin, this.mWeightPoundsMax, this.mWeightEntryErrorMessage, errorMessage2);
        this.mWeightMetricViewGroup.setVisibility(8);
        this.mWeightImperialViewGroup.setVisibility(0);
    }

    private void setHeightParameters(boolean isDistanceHeightMetric) {
        if (isDistanceHeightMetric) {
            this.mValidateHeight = new MeasureValidation(this.mHeightCentimetersTextView, this.mProfileMetricHeightLabel, this.mHeightCentimetersMin, this.mHeightCentimetersMax, this.mHeightEntryErrorMessage, String.format(getResources().getString(R.string.settings_profile_height_centimeters_invalid_message), Integer.valueOf(this.mHeightCentimetersMin), Integer.valueOf(this.mHeightCentimetersMax)));
            this.mHeightMetricViewGroup.setVisibility(0);
            this.mHeightImperialViewGroup.setVisibility(8);
            return;
        }
        String errorMessage = String.format(getResources().getString(R.string.settings_profile_height_invalid_message), Integer.valueOf(this.mHeightFeetMin), Integer.valueOf(this.mMinHeightInches), Integer.valueOf(this.mHeightFeetMax), Integer.valueOf(this.mMaxHeightInches));
        this.mValidateHeight = new MeasureValidation(this.mHeightFeetTextView, this.mProfileHeightFeetLabel, this.mImperialHeightMin, this.mImperialHeightMax, this.mHeightEntryErrorMessage, errorMessage);
        this.mValidateInches = new MeasureValidation(this.mHeightInchesTextView, this.mProfileHeightInchesLabel, this.mHeightInchesMin, this.mHeightInchesMax, this.mHeightEntryErrorMessage, errorMessage);
        this.mHeightMetricViewGroup.setVisibility(8);
        this.mHeightImperialViewGroup.setVisibility(0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateConfirmationBarVisibility(CargoUserProfile profile, boolean isWeightMetric, boolean isDistanceHeightMetric) {
        if (this.mIsOobeModeActive) {
            this.mOobeDoneButton.setEnabled(profile != null);
        } else if (profile == null) {
            this.mConfirmationBar.setVisibility(8);
        } else if (checkIfAnyValuesHaveChanged(profile, isWeightMetric, isDistanceHeightMetric)) {
            this.mConfirmationBar.setVisibility(0);
        } else {
            this.mConfirmationBar.setVisibility(8);
        }
    }

    private boolean checkIfAnyValuesHaveChanged(CargoUserProfile profile, boolean isWeightMetric, boolean isDistanceHeightMetric) {
        if (getUIFirstName().equals(profile.getFirstName()) && getUIBirthMonth().equals(getBirthMonthString(profile)) && getUIBirthYear().equals(getBirthYearString(profile)) && getUIGenderIndex() == getGenderIndex(profile, this.mGenderMaleIndex, this.mGenderFemaleIndex) && getZipCode().equals(getZipCodeString(profile)) && isAllowMarketingEmailEnabled() == getMarketingEmailOption(profile)) {
            if ((!isWeightMetric || getUIWeightKilograms().equals(getWeightKilogramsString(profile))) && getUIWeightPounds().equals(getWeightPoundsString(profile))) {
                return ((!isDistanceHeightMetric || getUIHeightCentimeters().equals(getHeightCentimetersString(profile))) && getUIHeightFeet().equals(getHeightFeetString(profile)) && getUIHeightInches().equals(getHeightInchesString(profile))) ? false : true;
            }
            return true;
        }
        return true;
    }

    public static boolean isInteger(EditText editText) {
        try {
            Integer.parseInt(editText.getText().toString());
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isWithinRange(int testValue, int min, int max) {
        return testValue >= min && testValue <= max;
    }

    private String getUIFirstName() {
        return this.mNameTextView.getText().toString();
    }

    private String getUIBirthMonth() {
        return String.valueOf(this.mBirthMonthSpinner.getSelectedItemPosition() + 1);
    }

    private String getUIBirthYear() {
        return this.mBirthYearSpinner.getSelectedItem().toString();
    }

    private int getUIGenderIndex() {
        return this.mGenderSpinner.getSelectedItemPosition();
    }

    private String getUIWeightKilograms() {
        return this.mWeightKilogramsTextView.getText().toString();
    }

    private String getUIWeightPounds() {
        return this.mWeightPoundsTextView.getText().toString();
    }

    private String getZipCode() {
        return this.mZipCodeTextView.getText().toString();
    }

    private String getUIHeightCentimeters() {
        return this.mHeightCentimetersTextView.getText().toString();
    }

    private String getUIHeightFeet() {
        String heightString = this.mHeightFeetTextView.getText().toString();
        return !heightString.isEmpty() ? heightString : AppEventsConstants.EVENT_PARAM_VALUE_NO;
    }

    private String getUIHeightInches() {
        String heightString = this.mHeightInchesTextView.getText().toString();
        return !heightString.isEmpty() ? heightString : AppEventsConstants.EVENT_PARAM_VALUE_NO;
    }

    private boolean isAllowMarketingEmailEnabled() {
        return this.mAllowMarketingEmailCheckBox.isChecked();
    }

    private static String getFirstName(CargoUserProfile profile) {
        return profile.getFirstName();
    }

    private static int getBirthMonth(CargoUserProfile profile) {
        DateTime birthdate = profile.getBirthdate();
        if (birthdate == null) {
            return 1;
        }
        return ProfileUtils.getMonth(birthdate);
    }

    private static String getBirthMonthString(CargoUserProfile profile) {
        DateTime birthdate = profile.getBirthdate();
        return birthdate == null ? Integer.toString(mDefaultBirthMonth) : Integer.toString(ProfileUtils.getMonth(birthdate));
    }

    private static String getBirthYearString(CargoUserProfile profile) {
        DateTime birthdate = profile.getBirthdate();
        return birthdate == null ? Integer.toString(mDefaultBirthYear) : Integer.toString(ProfileUtils.getYear(birthdate));
    }

    private static int getGenderIndex(CargoUserProfile profile, int genderMaleIndex, int genderFemaleIndex) {
        return profile.getGender() == UserProfileInfo.Gender.male ? genderMaleIndex : genderFemaleIndex;
    }

    private static String getWeightKilogramsString(CargoUserProfile profile) {
        Weight weight = Weight.fromGrams(profile.getWeightInGrams());
        return Integer.toString(weight.getKilograms());
    }

    private static String getWeightPoundsString(CargoUserProfile profile) {
        Weight weight = Weight.fromGrams(profile.getWeightInGrams());
        return Integer.toString(weight.getPounds());
    }

    private static String getHeightCentimetersString(CargoUserProfile profile) {
        Length height = Length.fromMillimeters(profile.getHeightInMM());
        return Integer.toString(height.getCentimeters());
    }

    private static String getHeightFeetString(CargoUserProfile profile) {
        Length height = Length.fromMillimeters(profile.getHeightInMM());
        return Integer.toString(height.getFeet());
    }

    private static String getHeightInchesString(CargoUserProfile profile) {
        Length height = Length.fromMillimeters(profile.getHeightInMM());
        return Integer.toString(height.getInches());
    }

    private static String getZipCodeString(CargoUserProfile profile) {
        return profile.getZipCode();
    }

    private static boolean getMarketingEmailOption(CargoUserProfile profile) {
        return profile.isAllowMarketingEmail();
    }

    /* loaded from: classes.dex */
    public class MeasureValidation {
        TextView mEditLabel;
        EditText mEditTextToValidate;
        String mErrorMessage;
        TextView mErrorMessageView;
        int mMaxRange;
        int mMinRange;

        MeasureValidation(EditText editTextToValidate, TextView editLabel, int minRange, int maxRange, TextView errorMessageView, String errorMessageResId) {
            this.mEditTextToValidate = editTextToValidate;
            this.mEditLabel = editLabel;
            this.mMinRange = minRange;
            this.mMaxRange = maxRange;
            this.mErrorMessageView = errorMessageView;
            this.mErrorMessage = errorMessageResId;
        }

        public int getEditTextValue() {
            if (!SettingsProfileFragment.isInteger(this.mEditTextToValidate)) {
                return 0;
            }
            int editTextValue = Integer.parseInt(this.mEditTextToValidate.getText().toString());
            return editTextValue;
        }

        public boolean invalidIntegerRange(int testValue) {
            if (SettingsProfileFragment.isWithinRange(testValue, this.mMinRange, this.mMaxRange)) {
                return false;
            }
            SettingsProfileFragment.this.setProfileErrorView(this.mEditTextToValidate, this.mEditLabel, this.mErrorMessageView, String.format(this.mErrorMessage, Integer.valueOf(this.mMinRange), Integer.valueOf(this.mMaxRange)));
            return true;
        }

        public void clearErrorView() {
            SettingsProfileFragment.this.clearProfileErrorView(this.mEditTextToValidate, this.mEditLabel, this.mErrorMessageView);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyActivityOfCompletion() {
        Activity activity = getActivity();
        if (!isDetached() && (activity instanceof SettingsProfileFragmentCalls)) {
            ((SettingsProfileFragmentCalls) activity).onProfileCompleted();
        }
    }
}

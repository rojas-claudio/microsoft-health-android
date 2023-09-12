package com.microsoft.kapp.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Process;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;
import com.microsoft.kapp.R;
import com.microsoft.kapp.activities.DialogPriority;
import com.microsoft.kapp.activities.SplashActivity;
import com.microsoft.kapp.cache.CacheService;
import com.microsoft.kapp.feedback.FeedbackService;
import com.microsoft.kapp.models.CloudEnvironment;
import com.microsoft.kapp.models.FreStatus;
import com.microsoft.kapp.services.KAppBroadcastReceiver;
import com.microsoft.kapp.services.SettingsProvider;
import com.microsoft.kapp.utils.Constants;
import com.microsoft.kapp.utils.ViewUtils;
import com.microsoft.krestsdk.auth.MsaAuth;
import com.microsoft.krestsdk.auth.credentials.CredentialsManager;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.inject.Inject;
/* loaded from: classes.dex */
public class DebugSettingsFragment extends BaseFragment {
    private Button mApplyDebugHeaderButton;
    private Button mApplyEnvironmentButton;
    @Inject
    MsaAuth mAuthService;
    @Inject
    CacheService mCacheService;
    private Spinner mCachingSpinner;
    @Inject
    CredentialsManager mCredentialsManager;
    private EditText mDebugHeaderText;
    private Spinner mDynamicConfigurationSpinner;
    private Spinner mEmulatePublicRelease;
    private Spinner mEnvironmentSpinner;
    @Inject
    FeedbackService mFeedbackService;
    private Button mForceQuitButton;
    private Button mLaunchBackgroundSyncButton;
    private Spinner mNotificationLoggingSpinner;
    private Spinner mRaisedInsightsSpinner;
    private Spinner mRefreshTokenSpinner;
    @Inject
    SettingsProvider mSettingsProvider;
    private Spinner mTMAGConnectionSpinner;

    @Override // android.support.v4.app.Fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View rootView = inflater.inflate(R.layout.debug_fragment_settings, container, false);
        this.mEnvironmentSpinner = (Spinner) rootView.findViewById(R.id.debug_settings_environment_spinner);
        this.mCachingSpinner = (Spinner) rootView.findViewById(R.id.debug_settings_caching_spinner);
        this.mNotificationLoggingSpinner = (Spinner) rootView.findViewById(R.id.logging_settings_caching_spinner);
        this.mRefreshTokenSpinner = (Spinner) ViewUtils.getValidView(rootView, R.id.debug_settings_token_refresh_spinner, Spinner.class);
        this.mEmulatePublicRelease = (Spinner) ViewUtils.getValidView(rootView, R.id.debug_settings_emulate_public_release, Spinner.class);
        this.mRaisedInsightsSpinner = (Spinner) rootView.findViewById(R.id.debug_settings_raised_insights_feature);
        this.mTMAGConnectionSpinner = (Spinner) rootView.findViewById(R.id.debug_settings_tmag_connected_feature);
        this.mApplyEnvironmentButton = (Button) rootView.findViewById(R.id.debug_env_apply);
        this.mDebugHeaderText = (EditText) rootView.findViewById(R.id.debug_session_header_value_edit);
        this.mApplyDebugHeaderButton = (Button) rootView.findViewById(R.id.debug_session_header_apply);
        this.mForceQuitButton = (Button) rootView.findViewById(R.id.debug_force_quit_app);
        this.mLaunchBackgroundSyncButton = (Button) rootView.findViewById(R.id.debug_launch_background_sync);
        this.mDynamicConfigurationSpinner = (Spinner) rootView.findViewById(R.id.debug_settings_dynamic_configuration_feature);
        List<CloudEnvironment> environments = getEnvironmentList();
        CloudEnvironment currentEnvironment = this.mSettingsProvider.getEnvironment();
        ArrayAdapter<CloudEnvironment> adapter = new ArrayAdapter<>(getActivity(), 17367049, environments);
        this.mEnvironmentSpinner.setAdapter((SpinnerAdapter) adapter);
        int selectedEnvironmentIndex = environments.indexOf(currentEnvironment);
        this.mEnvironmentSpinner.setSelection(selectedEnvironmentIndex);
        this.mCachingSpinner.setSelection(this.mSettingsProvider.isCachingEnabled() ? 0 : 1);
        this.mNotificationLoggingSpinner.setSelection(this.mSettingsProvider.isNotificationCenterLoggingEnabled() ? 0 : 1);
        this.mRefreshTokenSpinner.setSelection(this.mSettingsProvider.isTokenRefreshEnabled() ? 0 : 1);
        this.mEmulatePublicRelease.setSelection(this.mSettingsProvider.shouldEmulatePublicReleaseForLogging() ? 0 : 1);
        this.mRaisedInsightsSpinner.setSelection(this.mSettingsProvider.isRaisedInsightsEnabled() ? 0 : 1);
        this.mTMAGConnectionSpinner.setSelection(this.mSettingsProvider.isTMAGConnectionEnabled() ? 0 : 1);
        this.mDynamicConfigurationSpinner.setSelection(this.mSettingsProvider.canDynamicConfigurationLoadFromCloud() ? 0 : 1);
        this.mCachingSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() { // from class: com.microsoft.kapp.fragments.DebugSettingsFragment.1
            @Override // android.widget.AdapterView.OnItemSelectedListener
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                DebugSettingsFragment.this.mSettingsProvider.setIsCachingEnabled(DebugSettingsFragment.this.mCachingSpinner.getSelectedItemPosition() == 0);
            }

            @Override // android.widget.AdapterView.OnItemSelectedListener
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
        this.mNotificationLoggingSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() { // from class: com.microsoft.kapp.fragments.DebugSettingsFragment.2
            @Override // android.widget.AdapterView.OnItemSelectedListener
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                DebugSettingsFragment.this.mSettingsProvider.seNotificationCenterLoggingEnabled(DebugSettingsFragment.this.mNotificationLoggingSpinner.getSelectedItemPosition() == 0);
            }

            @Override // android.widget.AdapterView.OnItemSelectedListener
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
        this.mRefreshTokenSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() { // from class: com.microsoft.kapp.fragments.DebugSettingsFragment.3
            @Override // android.widget.AdapterView.OnItemSelectedListener
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                DebugSettingsFragment.this.mSettingsProvider.setIsTokenRefreshEnabled(DebugSettingsFragment.this.mRefreshTokenSpinner.getSelectedItemPosition() == 0);
            }

            @Override // android.widget.AdapterView.OnItemSelectedListener
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
        this.mEmulatePublicRelease.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() { // from class: com.microsoft.kapp.fragments.DebugSettingsFragment.4
            @Override // android.widget.AdapterView.OnItemSelectedListener
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                DebugSettingsFragment.this.mSettingsProvider.setShouldEmulatePublicReleaseForLogging(DebugSettingsFragment.this.mEmulatePublicRelease.getSelectedItemPosition() == 0);
            }

            @Override // android.widget.AdapterView.OnItemSelectedListener
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
        this.mRaisedInsightsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() { // from class: com.microsoft.kapp.fragments.DebugSettingsFragment.5
            @Override // android.widget.AdapterView.OnItemSelectedListener
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                DebugSettingsFragment.this.mSettingsProvider.setRaisedInsightsEnabled(DebugSettingsFragment.this.mRaisedInsightsSpinner.getSelectedItemPosition() == 0);
            }

            @Override // android.widget.AdapterView.OnItemSelectedListener
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
        this.mDynamicConfigurationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() { // from class: com.microsoft.kapp.fragments.DebugSettingsFragment.6
            @Override // android.widget.AdapterView.OnItemSelectedListener
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                DebugSettingsFragment.this.mSettingsProvider.setDynamicConfigurationLoadFromCloud(DebugSettingsFragment.this.mDynamicConfigurationSpinner.getSelectedItemPosition() == 0);
            }

            @Override // android.widget.AdapterView.OnItemSelectedListener
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
        this.mTMAGConnectionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() { // from class: com.microsoft.kapp.fragments.DebugSettingsFragment.7
            @Override // android.widget.AdapterView.OnItemSelectedListener
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                DebugSettingsFragment.this.mSettingsProvider.setTMAGConnectionEnabled(DebugSettingsFragment.this.mTMAGConnectionSpinner.getSelectedItemPosition() == 0);
            }

            @Override // android.widget.AdapterView.OnItemSelectedListener
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
        this.mDebugHeaderText.setText(this.mSettingsProvider.getSessionHeaderValue());
        this.mApplyEnvironmentButton.setOnClickListener(new View.OnClickListener() { // from class: com.microsoft.kapp.fragments.DebugSettingsFragment.8
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                DebugSettingsFragment.this.applyEnvironment();
            }
        });
        this.mApplyDebugHeaderButton.setOnClickListener(new View.OnClickListener() { // from class: com.microsoft.kapp.fragments.DebugSettingsFragment.9
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                DebugSettingsFragment.this.applyDebugHeader();
            }
        });
        this.mForceQuitButton.setOnClickListener(new View.OnClickListener() { // from class: com.microsoft.kapp.fragments.DebugSettingsFragment.10
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                int pid = Process.myPid();
                Process.killProcess(pid);
            }
        });
        this.mLaunchBackgroundSyncButton.setOnClickListener(new View.OnClickListener() { // from class: com.microsoft.kapp.fragments.DebugSettingsFragment.11
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                DebugSettingsFragment.this.launchBackgroundSync();
            }
        });
        return rootView;
    }

    private List<CloudEnvironment> getEnvironmentList() {
        List<CloudEnvironment> environments = new ArrayList<>(Arrays.asList(CloudEnvironment.values()));
        Collections.sort(environments, new Comparator<CloudEnvironment>() { // from class: com.microsoft.kapp.fragments.DebugSettingsFragment.12
            @Override // java.util.Comparator
            public int compare(CloudEnvironment lhs, CloudEnvironment rhs) {
                return lhs.toString().compareTo(rhs.toString());
            }
        });
        return environments;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void launchBackgroundSync() {
        Context context = getActivity().getApplicationContext();
        Intent intent = new Intent(getActivity(), KAppBroadcastReceiver.class);
        intent.setAction(Constants.INTENT_TRIGGER_SYNC);
        context.sendBroadcast(intent);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void applyEnvironment() {
        CloudEnvironment selectedEnvironment = (CloudEnvironment) this.mEnvironmentSpinner.getSelectedItem();
        Context context = getActivity();
        this.mCredentialsManager.deleteCredentials(context);
        Toast.makeText(context, "Logging out", 0).show();
        this.mCacheService.handleLogout();
        this.mSettingsProvider.setEnvironment(selectedEnvironment);
        this.mSettingsProvider.setFreStatus(FreStatus.NOT_SHOWN);
        Intent homeIntent = new Intent(getActivity(), SplashActivity.class);
        startActivity(homeIntent);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void applyDebugHeader() {
        Context context = getActivity();
        String debugSessionHeaderValue = this.mDebugHeaderText.getText().toString();
        if (debugSessionHeaderValue == null) {
            getDialogManager().showDialog(context, Integer.valueOf((int) R.string.error_invalid_url), null, DialogPriority.LOW);
        } else {
            this.mSettingsProvider.setSessionHeaderValue(debugSessionHeaderValue);
        }
    }

    private static boolean isValidUrl(String urlSpec) {
        try {
            new URL(urlSpec);
            return true;
        } catch (MalformedURLException e) {
            return false;
        }
    }
}

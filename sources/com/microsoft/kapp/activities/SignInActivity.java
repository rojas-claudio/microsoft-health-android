package com.microsoft.kapp.activities;

import android.annotation.SuppressLint;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import com.microsoft.band.CargoConstants;
import com.microsoft.kapp.Callback;
import com.microsoft.kapp.R;
import com.microsoft.kapp.device.CargoUserProfile;
import com.microsoft.kapp.fragments.MarketingSignInFragment;
import com.microsoft.kapp.fragments.SignInFragment;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.kapp.models.CloudEnvironment;
import com.microsoft.kapp.models.FreStatus;
import com.microsoft.kapp.services.SettingsProvider;
import com.microsoft.kapp.utils.AppConfigurationManager;
import com.microsoft.krestsdk.auth.ServiceInfo;
import com.microsoft.krestsdk.auth.SignInContext;
import com.microsoft.krestsdk.auth.credentials.CredentialsManager;
import com.microsoft.krestsdk.auth.credentials.KCredential;
import javax.inject.Inject;
@SuppressLint({"SetJavaScriptEnabled"})

/* loaded from: classes.dex */
public class SignInActivity extends OobeBaseActivity implements SignInFragment.Callbacks, MarketingSignInFragment.Callbacks {
    public static final String ARG_IN_LOG_IN_URL = "arg_in_log_in_url";
    public static final String ARG_IN_SIGN_CONTEXT = "arg_in_sign_context";
    private static final String TAG = SignInActivity.class.getSimpleName();
    private static final String TAG_SIGN_IN_FRAGMENT = "sign_in_fragment";
    @Inject
    AppConfigurationManager mAppConfigurationManager;
    private SignInContext mContext;
    @Inject
    CredentialsManager mCredentialsManager;
    private String mLoginUrl;
    @Inject
    SettingsProvider mSettingProvider;
    private SignInFragment mSignInFragment;

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.microsoft.kapp.activities.OobeBaseActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            this.mContext = (SignInContext) bundle.get(ARG_IN_SIGN_CONTEXT);
            this.mLoginUrl = bundle.getString(ARG_IN_LOG_IN_URL);
        }
        FragmentManager fragmentManager = getFragmentManager();
        if (savedInstanceState != null) {
            this.mSignInFragment = (SignInFragment) fragmentManager.findFragmentByTag(TAG_SIGN_IN_FRAGMENT);
        } else if (SignInContext.TOKEN_REFRESH == this.mContext) {
            onSkipMarketScreen();
        } else {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.fragment_container, MarketingSignInFragment.newInstance());
            fragmentTransaction.commit();
        }
        if (this.mLoginUrl == null) {
            this.mLoginUrl = getLoginUrl(this.mSettingProvider);
        }
    }

    private String getLoginUrl(SettingsProvider settingsProvider) {
        CloudEnvironment cloudEnvironment = settingsProvider.getEnvironment();
        return String.format(CargoConstants.OAUTH_LOGIN_URL, cloudEnvironment.getRealm());
        //https://login.live.com/oauth20_authorize.srf?client_id=000000004811DB42&scope=service::%s::MBI_SSL&response_type=token&redirect_uri=https://login.live.com/oauth20_desktop.srf
    }

    @Override // com.microsoft.kapp.activities.OobeBaseActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    public void onBackPressed() {
        if (this.mSignInFragment != null && this.mSignInFragment.isAdded()) {
            trySignIn();
        }
    }

    @Override // com.microsoft.kapp.fragments.SignInFragment.Callbacks
    public int trySignIn() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService("connectivity");
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo == null || !networkInfo.isConnected()) {
            return 1;
        }
        this.mSignInFragment.setLoginUrl(this.mLoginUrl);
        return 0;
    }

    @Override // com.microsoft.kapp.fragments.SignInFragment.Callbacks
    public void onSuccessfulLogin(KCredential credentials, ServiceInfo serviceInfo, CargoUserProfile userProfile) {
        SaveCredentialsTask saveCredentialsTask = new SaveCredentialsTask(credentials, serviceInfo, userProfile);
        saveCredentialsTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
    }

    @Override // com.microsoft.kapp.fragments.SignInFragment.Callbacks
    public String getAuthUrl() {
        return this.mSettingsProvider.getAuthUrl();
    }

    @Override // com.microsoft.kapp.fragments.MarketingSignInFragment.Callbacks
    public void onSkipMarketScreen() {
        try {
            this.mSignInFragment = SignInFragment.newInstance();
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, this.mSignInFragment, TAG_SIGN_IN_FRAGMENT);
            fragmentTransaction.commit();
        } catch (IllegalStateException e) {
            KLog.w(TAG, "Illegal state due to commit after onSavedInstanceState");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void postUserProfileFetch(KCredential credential, final ServiceInfo serviceInfo, final CargoUserProfile userProfile) {
        Callback<Void> callback = new Callback<Void>() { // from class: com.microsoft.kapp.activities.SignInActivity.1
            @Override // com.microsoft.kapp.Callback
            public void callback(Void result) {
                if (SignInActivity.this.mContext == SignInContext.TOKEN_REFRESH) {
                    SignInActivity.this.finish();
                } else if (!serviceInfo.IsNewlyCreatedProfile && userProfile.isOobeComplete()) {
                    SignInActivity.this.mSettingsProvider.setUserProfile(userProfile);
                    SignInActivity.this.moveToNextOobeTask(FreStatus.SKIP_REMAINING_OOBE_STEPS);
                } else {
                    //Need this to happen
                    SignInActivity.this.moveToNextOobeTask(FreStatus.LOGGED_IN);
                }
            }

            @Override // com.microsoft.kapp.Callback
            public void onError(Exception ex) {
            }
        };
        this.mSettingProvider.setFUSEndPoint(credential.getFUSEndPoint());
        this.mAppConfigurationManager.downloadAndApplyAppConfiguration(this, callback);
    }

    /* loaded from: classes.dex */
    private class SaveCredentialsTask extends AsyncTask<Void, Void, Void> {
        private KCredential mCredentials;
        private ServiceInfo mServiceInfo;
        private CargoUserProfile mUserProfile;

        SaveCredentialsTask(KCredential credentials, ServiceInfo serviceInfo, CargoUserProfile userProfile) {
            this.mCredentials = credentials;
            this.mServiceInfo = serviceInfo;
            this.mUserProfile = userProfile;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public Void doInBackground(Void... params) {
            SignInActivity.this.mCredentialsManager.setCredentialsNonUithread(this.mCredentials);
            return null;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public void onPostExecute(Void result) {
            SignInActivity.this.postUserProfileFetch(this.mCredentials, this.mServiceInfo, this.mUserProfile);
        }
    }
}

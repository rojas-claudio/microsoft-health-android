package com.microsoft.kapp.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.facebook.android.Facebook;
import com.j256.ormlite.stmt.query.SimpleComparison;
import com.microsoft.band.client.CargoException;
import com.microsoft.kapp.Callback;
import com.microsoft.kapp.CargoConnection;
import com.microsoft.kapp.KApplication;
import com.microsoft.kapp.R;
import com.microsoft.kapp.activities.DialogManager;
import com.microsoft.kapp.activities.DialogPriority;
import com.microsoft.kapp.device.CargoUserProfile;
import com.microsoft.kapp.diagnostics.Telemetry;
import com.microsoft.kapp.diagnostics.TelemetryConstants;
import com.microsoft.kapp.diagnostics.Validate;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.kapp.utils.DialogManagerImpl;
import com.microsoft.kapp.utils.ViewUtils;
import com.microsoft.kapp.widgets.Interstitial;
import com.microsoft.krestsdk.auth.KdsFetcher;
import com.microsoft.krestsdk.auth.ServiceInfo;
import com.microsoft.krestsdk.auth.credentials.KCredential;
import com.microsoft.krestsdk.auth.credentials.KdsCredential;
import com.microsoft.krestsdk.auth.credentials.KdsRetrieverAsync;
import com.microsoft.krestsdk.auth.credentials.MsaCredential;
import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;
import org.joda.time.DateTime;
/* loaded from: classes.dex */
public class SignInFragment extends Fragment {
    private static final String MSA_ERROR = "error=access_denied";
    private static final String MSA_SUCCESS = "access_token=";
    public static final int NO_ERRORS = 0;
    public static final int NO_INTERNET_ERROR = 1;
    public static final int SIGN_IN_ERROR = 2;
    private static final String TAG = SignInFragment.class.getSimpleName();
    private String mAcsToken;
    @Inject
    CargoConnection mCargoConnection;
    private int mExpiresIn;
    private Interstitial mInterstitial;
    @Inject
    KdsFetcher mKdsFetcher;
    private String mLoginUrl;
    private String mRefreshToken;
    private boolean mRetrievingServiceInfo = false;
    private boolean mSignInError;
    private WeakReference<Callbacks> mWeakListener;
    private WebView mWebView;

    /* loaded from: classes.dex */
    public interface Callbacks {
        String getAuthUrl();

        void onSuccessfulLogin(KCredential kCredential, ServiceInfo serviceInfo, CargoUserProfile cargoUserProfile);

        int trySignIn();
    }

    public static SignInFragment newInstance() {
        SignInFragment fragment = new SignInFragment();
        return fragment;
    }

    @Override // android.app.Fragment
    public void onCreate(Bundle savedInstanceState) {
        KApplication application = (KApplication) getActivity().getApplication();
        application.inject(this);
        super.onCreate(savedInstanceState);
    }

    @Override // android.app.Fragment
    @SuppressLint({"SetJavaScriptEnabled"})
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_sign_in, container, false);
        if (rootView != null) {
            this.mInterstitial = (Interstitial) ViewUtils.getValidView(rootView, R.id.sign_in_interstitial, Interstitial.class);
            this.mWebView = (WebView) ViewUtils.getValidView(rootView, R.id.sign_in_webview, WebView.class);
            this.mWebView.setBackgroundColor(getResources().getColor(R.color.WhiteColor));
            this.mWebView.getSettings().setJavaScriptEnabled(true);
            this.mWebView.setWebViewClient(new WebViewClient() { // from class: com.microsoft.kapp.fragments.SignInFragment.1
                @Override // android.webkit.WebViewClient
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    if (url.contains(SignInFragment.MSA_SUCCESS)) {
                        SignInFragment.this.onUserLoggedIn(url);
                        return true;
                    } else if (url.contains(SignInFragment.MSA_ERROR)) {
                        CookieManager.getInstance().removeAllCookie();
                        SignInFragment.this.loadLogin();
                        return true;
                    } else {
                        return false;
                    }
                }

                @Override // android.webkit.WebViewClient
                public void onPageFinished(WebView view, String url) {
                    if (!SignInFragment.this.mSignInError && !SignInFragment.this.mRetrievingServiceInfo) {
                        SignInFragment.this.mWebView.setVisibility(0);
                        SignInFragment.this.mInterstitial.setVisibility(8);
                    }
                }

                @Override // android.webkit.WebViewClient
                public void onReceivedError(WebView view, int errorCod, String description, String failingUrl) {
                    KLog.e(SignInFragment.TAG, "Loading sign-in page with WebView failed. Error code: " + errorCod + " URL: " + failingUrl);
                    SignInFragment.this.showError(R.string.sign_in_error_server);
                    SignInFragment.this.mSignInError = true;
                }
            });
        }
        return rootView;
    }

    @Override // android.app.Fragment
    public void onStart() {
        super.onStart();
        this.mRetrievingServiceInfo = false;
        signIn();
    }

    @Override // android.app.Fragment
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mWeakListener = new WeakReference<>((Callbacks) activity);
    }

    public DialogManager getDialogManager() {
        Activity activity = getActivity();
        return DialogManagerImpl.getDialogManager(activity);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onUserLoggedIn(String url) {
        try {
            Map<String, String> params = getUrlParameters(url);
            this.mAcsToken = params.get("access_token");
            this.mRefreshToken = params.get("refresh_token");
            this.mExpiresIn = Integer.valueOf(params.get(Facebook.EXPIRES)).intValue();
            notifySuccessfulLogin();
        } catch (UnsupportedEncodingException e) {
            KLog.e(TAG, "UTF-8 is not supported by the device");
        }
    }

    public static Map<String, String> getUrlParameters(String url) throws UnsupportedEncodingException {
        Map<String, String> params = new HashMap<>();
        String[] urlParts = url.split("\\?");
        if (urlParts.length > 1) {
            String query = urlParts[1];
            String[] arr$ = query.split("[&#]");
            for (String param : arr$) {
                String[] pair = param.split(SimpleComparison.EQUAL_TO_OPERATION);
                String key = URLDecoder.decode(pair[0], "UTF-8");
                String value = URLDecoder.decode(pair[1], "UTF-8");
                params.put(key, value);
            }
        }
        return params;
    }

    public void notifySuccessfulLogin(String acsToken, String refreshToken, int expiresIn) {
        this.mInterstitial.setSlide(Interstitial.SLIDE_GETTING_ACCOUNT_INFO);
        this.mAcsToken = acsToken;
        this.mRefreshToken = refreshToken;
        this.mExpiresIn = expiresIn;
        notifySuccessfulLogin();
    }

    private void notifySuccessfulLogin() {
        ViewUtils.closeSoftKeyboard(getActivity(), this.mWebView);
        Telemetry.logPage(TelemetryConstants.PageViews.OOBE_LOGIN_AUTHENTICATE);
        KdsRetrieverAsync kdsBackground = new KdsRetrieverAsync(this.mKdsFetcher, this.mAcsToken, getAuthUrl());
        kdsBackground.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Callback<ServiceInfo>() { // from class: com.microsoft.kapp.fragments.SignInFragment.2
            @Override // com.microsoft.kapp.Callback
            public void callback(ServiceInfo result) {
                SignInFragment.this.postKdsCall(result);
            }

            @Override // com.microsoft.kapp.Callback
            public void onError(Exception ex) {
                SignInFragment.this.handleError(ex);
            }
        });
        this.mRetrievingServiceInfo = true;
        Activity activity = getActivity();
        if (Validate.isActivityAlive(activity)) {
            activity.runOnUiThread(new Runnable() { // from class: com.microsoft.kapp.fragments.SignInFragment.3
                @Override // java.lang.Runnable
                public void run() {
                    SignInFragment.this.mInterstitial.setSlide(Interstitial.SLIDE_GETTING_ACCOUNT_INFO);
                    SignInFragment.this.showProgress();
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void signIn() {
        if (this.mWeakListener != null) {
            this.mInterstitial.setSlide(5000);
            Callbacks listener = this.mWeakListener.get();
            if (listener != null) {
                showProgress();
                switch (listener.trySignIn()) {
                    case 1:
                        showError(R.string.internet_required_message);
                        return;
                    case 2:
                        showError(R.string.sign_in_error);
                        return;
                    default:
                        return;
                }
            }
        }
    }

    private String getAuthUrl() {
        Callbacks listener;
        return (this.mWeakListener == null || (listener = this.mWeakListener.get()) == null) ? "" : listener.getAuthUrl();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showProgress() {
        this.mInterstitial.setVisibility(0);
        this.mWebView.setVisibility(8);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleError(Exception e) {
        KLog.e(TAG, "Error trying to sign in", e);
        showError(R.string.sign_in_error);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void postKdsCall(ServiceInfo serviceInfo) {
        if (serviceInfo != null) {
            String accessToken = this.mAcsToken;
            String refreshToken = this.mRefreshToken;
            DateTime expiresIn = DateTime.now().plusSeconds(this.mExpiresIn);
            MsaCredential msaCredential = new MsaCredential(accessToken, refreshToken, expiresIn);
            KdsCredential kdsCredential = new KdsCredential(serviceInfo, getAuthUrl(), this.mLoginUrl);
            KCredential credentials = new KCredential(msaCredential, kdsCredential);
            UserFetcherAsync profileFetcher = new UserFetcherAsync(this.mCargoConnection, this.mWeakListener, credentials, serviceInfo);
            profileFetcher.execute(new Void[0]);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class UserFetcherAsync extends AsyncTask<Void, Void, CargoUserProfile> {
        private CargoConnection mCargoConnection;
        private KCredential mCredential;
        private ServiceInfo mServiceInfo;
        private WeakReference<Callbacks> mWeakListener;

        public UserFetcherAsync(CargoConnection cargoConnection, WeakReference<Callbacks> weakListener, KCredential credential, ServiceInfo serviceInfo) {
            this.mCargoConnection = cargoConnection;
            this.mWeakListener = weakListener;
            this.mCredential = credential;
            this.mServiceInfo = serviceInfo;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public CargoUserProfile doInBackground(Void... params) {
            try {
                CargoUserProfile userProfile = this.mCargoConnection.getUserCloudProfile(this.mServiceInfo.PodAddress, this.mServiceInfo.AccessToken, this.mServiceInfo.FUSEndpoint);
                return userProfile;
            } catch (CargoException e) {
                KLog.e(SignInFragment.TAG, "Couldn't retrieve the user profile from the cloud", e);
                return null;
            }
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public void onPostExecute(CargoUserProfile result) {
            Callbacks listener;
            if (result == null) {
                SignInFragment.this.showError(R.string.sign_in_error);
            } else if (this.mWeakListener != null && (listener = this.mWeakListener.get()) != null) {
                listener.onSuccessfulLogin(this.mCredential, this.mServiceInfo, result);
            }
        }
    }

    @Override // android.app.Fragment
    public void onResume() {
        super.onResume();
        Telemetry.logPage(TelemetryConstants.PageViews.OOBE_LOGIN_MSA);
    }

    public void loadLogin() {
        CookieManager.getInstance().setAcceptCookie(true);
        this.mSignInError = false;
        this.mWebView.loadUrl(this.mLoginUrl);
    }

    public void setLoginUrl(String loginUrl) {
        this.mLoginUrl = loginUrl;
        if (this.mLoginUrl != null) {
            loadLogin();
        }
    }

    public void onError(Exception exception) {
        handleError(exception);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showError(int messageResId) {
        if (isAdded()) {
            Context context = getActivity();
            getDialogManager().showDialog(context, Integer.valueOf((int) R.string.network_error_loading_data_title), Integer.valueOf(messageResId), R.string.retry, new DialogInterface.OnClickListener() { // from class: com.microsoft.kapp.fragments.SignInFragment.4
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialog, int which) {
                    SignInFragment.this.signIn();
                }
            }, DialogPriority.HIGH);
        }
    }
}

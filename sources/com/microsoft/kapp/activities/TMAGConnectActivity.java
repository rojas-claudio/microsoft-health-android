package com.microsoft.kapp.activities;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import com.microsoft.kapp.R;
import com.microsoft.kapp.cache.CacheService;
import com.microsoft.kapp.cache.CacheUtils;
import com.microsoft.kapp.fragments.golf.GolfLandingPageFragment;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.kapp.utils.CommonUtils;
import com.microsoft.kapp.utils.ViewUtils;
import com.microsoft.kapp.views.CustomFontButton;
import com.microsoft.krestsdk.services.KCloudConstants;
import javax.inject.Inject;
@SuppressLint({"SetJavaScriptEnabled"})
/* loaded from: classes.dex */
public class TMAGConnectActivity extends BaseFragmentActivityWithOfflineSupport {
    private static final String SUCCESS_STRING = "bind=success";
    private static final String TAG = TMAGConnectActivity.class.getSimpleName();
    @Inject
    CacheService mCacheService;
    private CustomFontButton mDoneButton;
    private ViewGroup mDoneContainer;
    private WebView mWebView;

    @Override // com.microsoft.kapp.activities.BaseFragmentActivityWithOfflineSupport
    protected void onCreate(ViewGroup container, Bundle savedInstanceState) {
        setContentView(R.layout.tmag_connect);
        View view = getWindow().getDecorView();
        if (view != null) {
            this.mWebView = (WebView) ViewUtils.getValidView(view, R.id.tmag_connect_webview, WebView.class);
            this.mDoneButton = (CustomFontButton) ViewUtils.getValidView(view, R.id.disconnect_done, CustomFontButton.class);
            this.mDoneContainer = (ViewGroup) ViewUtils.getValidView(view, R.id.disconnect_done_container, FrameLayout.class);
            Bundle extras = getIntent().getExtras();
            final Boolean isPartnerConnected = extras != null ? Boolean.valueOf(extras.getBoolean(GolfLandingPageFragment.IS_PARTNER_CONNECTED)) : null;
            this.mWebView.setBackgroundColor(getResources().getColor(R.color.WhiteColor));
            this.mWebView.getSettings().setJavaScriptEnabled(true);
            this.mWebView.setWebViewClient(new WebViewClient() { // from class: com.microsoft.kapp.activities.TMAGConnectActivity.1
                @Override // android.webkit.WebViewClient
                public void onPageFinished(WebView webView, String url) {
                    if (Boolean.TRUE.equals(isPartnerConnected)) {
                        TMAGConnectActivity.this.mDoneContainer.setVisibility(0);
                        TMAGConnectActivity.this.mDoneButton.setOnClickListener(new View.OnClickListener() { // from class: com.microsoft.kapp.activities.TMAGConnectActivity.1.1
                            @Override // android.view.View.OnClickListener
                            public void onClick(View v) {
                                TMAGConnectActivity.this.mCacheService.removeForTag(CacheUtils.CONNECTEDAPPS);
                                TMAGConnectActivity.this.finish();
                            }
                        });
                    } else {
                        TMAGConnectActivity.this.mDoneContainer.setVisibility(8);
                    }
                    TMAGConnectActivity.this.setState(1234);
                }

                @Override // android.webkit.WebViewClient
                public boolean shouldOverrideUrlLoading(WebView webView, String url) {
                    if (url.contains(TMAGConnectActivity.SUCCESS_STRING)) {
                        TMAGConnectActivity.this.mCacheService.removeForTag(CacheUtils.CONNECTEDAPPS);
                        TMAGConnectActivity.this.finish();
                        return true;
                    }
                    return false;
                }

                @Override // android.webkit.WebViewClient
                public void onReceivedError(WebView webView, int errorCod, String description, String failingUrl) {
                    KLog.e(TMAGConnectActivity.TAG, "Loading partner connect page with WebView failed. Error code: " + errorCod + " URL: " + failingUrl);
                    TMAGConnectActivity.this.setState(1235);
                }
            });
        }
    }

    @Override // android.support.v4.app.FragmentActivity, android.app.Activity
    public void onStart() {
        super.onStart();
        signIn();
    }

    private void signIn() {
        if (CommonUtils.isNetworkAvailable(this)) {
            loadURL();
        } else {
            setState(1235);
        }
    }

    private void loadURL() {
        String thirdPartyPartnersPortalEndpoint = this.mSettingsProvider.getThirdPartyPartnersPortalEndpoint();
        if (thirdPartyPartnersPortalEndpoint != null) {
            Uri partnerURI = Uri.parse(thirdPartyPartnersPortalEndpoint);
            Uri partnerURI2 = partnerURI.buildUpon().appendQueryParameter(KCloudConstants.PARTNER_TMAG_URL_KEY, KCloudConstants.PARTNER_TMAG_URL_VALUE).build();
            CookieManager.getInstance().setAcceptCookie(true);
            this.mWebView.loadUrl(partnerURI2.toString());
            return;
        }
        KLog.e(TAG, "unable to get the partner portal url");
        setState(1235);
    }
}

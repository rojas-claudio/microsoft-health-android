package com.microsoft.kapp.fragments.golf;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.microsoft.kapp.ActivityScopedCallback;
import com.microsoft.kapp.Callback;
import com.microsoft.kapp.R;
import com.microsoft.kapp.activities.TMAGConnectActivity;
import com.microsoft.kapp.activities.golf.GolfFindCourseByNameActivity;
import com.microsoft.kapp.activities.golf.GolfFindCourseByRegionActivity;
import com.microsoft.kapp.activities.golf.GolfRequestACourseActivity;
import com.microsoft.kapp.activities.golf.GolfSearchActivity;
import com.microsoft.kapp.diagnostics.Telemetry;
import com.microsoft.kapp.diagnostics.TelemetryConstants;
import com.microsoft.kapp.diagnostics.Validate;
import com.microsoft.kapp.fragments.BaseFragment;
import com.microsoft.kapp.fragments.BaseFragmentWithOfflineSupport;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.kapp.services.SettingsProvider;
import com.microsoft.kapp.services.golf.GolfService;
import com.microsoft.kapp.utils.CommonUtils;
import com.microsoft.kapp.utils.Constants;
import com.microsoft.kapp.utils.VideoUtils;
import com.microsoft.kapp.utils.ViewUtils;
import com.microsoft.kapp.views.CustomFontButton;
import com.microsoft.kapp.views.CustomFontTextView;
import com.microsoft.kapp.views.CustomGlyphView;
import com.microsoft.kapp.views.TextWithChevronView;
import com.microsoft.kapp.widgets.Interstitial;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.inject.Inject;
/* loaded from: classes.dex */
public class GolfLandingPageFragment extends BaseFragmentWithOfflineSupport {
    private static final int CONNECTED = 8;
    private static final int CONNECTING = 4;
    private static final int CONNECTION_ERROR = 16;
    public static final String IS_PARTNER_CONNECTED = "Is_Partenr_Connected";
    public static final String PARTNER_CONNECT_STATE = "Partner_Connect_State";
    private static final int REQUIRE_CONNECT = 2;
    private TextWithChevronView mBrowseByCountry;
    private boolean mCheckConnectState;
    private CustomFontButton mConnectButton;
    private CustomGlyphView mConnectCheck;
    private CustomFontTextView mConnectErrorDetail;
    private CustomFontTextView mConnectManage;
    private CustomFontTextView mConnectRetry;
    private CustomFontTextView mConnectState;
    private Interstitial mConnectionProgress;
    private TextView mFindCourseIcon;
    @Inject
    GolfService mGolfService;
    private boolean mIsPartnerConnected;
    private TextWithChevronView mNearByGolfCourses;
    private CustomGlyphView mPartnerConnectPlay;
    private TextWithChevronView mRecentGolfCourses;
    @Inject
    SettingsProvider mSettingsProvider;
    private TextWithChevronView mSuggestACourse;
    private View mView;

    public static final GolfLandingPageFragment newInstance(String referrer) {
        GolfLandingPageFragment fragment = new GolfLandingPageFragment();
        Bundle arguments = new Bundle();
        arguments.putString(BaseFragment.ARG_REFERRER, referrer);
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override // com.microsoft.kapp.fragments.BaseFragmentWithOfflineSupport
    public View onCreateChildView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.mView = inflater.inflate(R.layout.golf_landing_page, container, false);
        if (savedInstanceState != null) {
            this.mCheckConnectState = savedInstanceState.getBoolean(PARTNER_CONNECT_STATE);
        }
        setViewControls();
        setClickListeners();
        setState(1234);
        loadPartnerConnectedState();
        return this.mView;
    }

    @Override // com.microsoft.kapp.fragments.BaseFragment, android.support.v4.app.Fragment
    public void onResume() {
        super.onResume();
        if (this.mCheckConnectState) {
            loadPartnerConnectedState();
            this.mCheckConnectState = false;
        }
        Telemetry.logPage(TelemetryConstants.PageViews.FITNESS_GOLF_LANDING_PAGE);
    }

    @Override // com.microsoft.kapp.fragments.BaseFragment, android.support.v4.app.Fragment
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(PARTNER_CONNECT_STATE, this.mCheckConnectState);
    }

    private void setViewControls() {
        this.mRecentGolfCourses = (TextWithChevronView) ViewUtils.getValidView(this.mView, R.id.recent_golf_courses, TextWithChevronView.class);
        this.mNearByGolfCourses = (TextWithChevronView) ViewUtils.getValidView(this.mView, R.id.near_by_golf_courses, TextWithChevronView.class);
        this.mBrowseByCountry = (TextWithChevronView) ViewUtils.getValidView(this.mView, R.id.browse_by_country_region, TextWithChevronView.class);
        this.mSuggestACourse = (TextWithChevronView) ViewUtils.getValidView(this.mView, R.id.request_a_course, TextWithChevronView.class);
        this.mFindCourseIcon = (TextView) ViewUtils.getValidView(this.mView, R.id.header_find_icon, TextView.class);
        this.mConnectButton = (CustomFontButton) ViewUtils.getValidView(this.mView, R.id.golf_connect_button, CustomFontButton.class);
        this.mConnectionProgress = (Interstitial) ViewUtils.getValidView(this.mView, R.id.golf_connect_circle, Interstitial.class);
        this.mConnectCheck = (CustomGlyphView) ViewUtils.getValidView(this.mView, R.id.golf_connect_check, CustomGlyphView.class);
        this.mConnectState = (CustomFontTextView) ViewUtils.getValidView(this.mView, R.id.golf_connect_state, CustomFontTextView.class);
        this.mConnectManage = (CustomFontTextView) ViewUtils.getValidView(this.mView, R.id.golf_connect_manage, CustomFontTextView.class);
        this.mConnectErrorDetail = (CustomFontTextView) ViewUtils.getValidView(this.mView, R.id.golf_connect_error_detail, CustomFontTextView.class);
        this.mConnectRetry = (CustomFontTextView) ViewUtils.getValidView(this.mView, R.id.golf_connect_retry, CustomFontTextView.class);
        this.mPartnerConnectPlay = (CustomGlyphView) ViewUtils.getValidView(this.mView, R.id.golf_play, CustomGlyphView.class);
        CharSequence manageText = this.mConnectManage.getText();
        SpannableString spannableManageText = new SpannableString(manageText);
        spannableManageText.setSpan(new UnderlineSpan(), 0, manageText.length(), 33);
        this.mConnectManage.setText(spannableManageText);
        CharSequence retryText = this.mConnectRetry.getText();
        SpannableString spannableRetryText = new SpannableString(retryText);
        spannableRetryText.setSpan(new UnderlineSpan(), 0, retryText.length(), 33);
        this.mConnectRetry.setText(spannableRetryText);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void loadPartnerConnectedState() {
        setPartnerConnectState(4);
        this.mGolfService.isPartnerConnected(new ActivityScopedCallback(this, new Callback<Boolean>() { // from class: com.microsoft.kapp.fragments.golf.GolfLandingPageFragment.1
            @Override // com.microsoft.kapp.Callback
            public void callback(Boolean result) {
                if (result == null || !result.booleanValue()) {
                    GolfLandingPageFragment.this.mIsPartnerConnected = false;
                    GolfLandingPageFragment.this.setPartnerConnectState(2);
                    return;
                }
                GolfLandingPageFragment.this.mIsPartnerConnected = true;
                GolfLandingPageFragment.this.setPartnerConnectState(8);
            }

            @Override // com.microsoft.kapp.Callback
            public void onError(Exception ex) {
                KLog.d(GolfLandingPageFragment.this.TAG, "is partner connect failed.", ex);
                if (CommonUtils.isNetworkAvailable(GolfLandingPageFragment.this.getActivity())) {
                    GolfLandingPageFragment.this.setPartnerConnectState(16);
                } else if (GolfLandingPageFragment.this.isAdded()) {
                    GolfLandingPageFragment.this.getDialogManager().showNetworkErrorDialogWithCallback(GolfLandingPageFragment.this.getActivity(), new DialogInterface.OnClickListener() { // from class: com.microsoft.kapp.fragments.golf.GolfLandingPageFragment.1.1
                        @Override // android.content.DialogInterface.OnClickListener
                        public void onClick(DialogInterface dialog, int which) {
                            GolfLandingPageFragment.this.setPartnerConnectState(16);
                        }
                    });
                }
            }
        }));
    }

    private void setClickListeners() {
        this.mConnectButton.setOnClickListener(new ConnectToGolfPartnerListnerInWebView());
        this.mConnectRetry.setOnClickListener(new View.OnClickListener() { // from class: com.microsoft.kapp.fragments.golf.GolfLandingPageFragment.2
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                GolfLandingPageFragment.this.loadPartnerConnectedState();
            }
        });
        this.mConnectManage.setOnClickListener(new ConnectToGolfPartnerListnerInWebView());
        this.mRecentGolfCourses.setOnClickListener(new View.OnClickListener() { // from class: com.microsoft.kapp.fragments.golf.GolfLandingPageFragment.3
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                GolfLandingPageFragment.this.startGolfSearchActivity(false);
            }
        });
        this.mNearByGolfCourses.setOnClickListener(new View.OnClickListener() { // from class: com.microsoft.kapp.fragments.golf.GolfLandingPageFragment.4
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                GolfLandingPageFragment.this.startGolfSearchActivity(true);
            }
        });
        this.mBrowseByCountry.setOnClickListener(new View.OnClickListener() { // from class: com.microsoft.kapp.fragments.golf.GolfLandingPageFragment.5
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                Intent intent = new Intent(GolfLandingPageFragment.this.getActivity(), GolfFindCourseByRegionActivity.class);
                GolfLandingPageFragment.this.startActivity(intent);
            }
        });
        this.mSuggestACourse.setOnClickListener(new View.OnClickListener() { // from class: com.microsoft.kapp.fragments.golf.GolfLandingPageFragment.6
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                Intent intent = new Intent(GolfLandingPageFragment.this.getActivity(), GolfRequestACourseActivity.class);
                GolfLandingPageFragment.this.startActivity(intent);
            }
        });
        this.mFindCourseIcon.setOnClickListener(new View.OnClickListener() { // from class: com.microsoft.kapp.fragments.golf.GolfLandingPageFragment.7
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                Intent intent = new Intent(GolfLandingPageFragment.this.getActivity(), GolfFindCourseByNameActivity.class);
                GolfLandingPageFragment.this.startActivity(intent);
            }
        });
        this.mPartnerConnectPlay.setOnClickListener(new AnonymousClass8());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.microsoft.kapp.fragments.golf.GolfLandingPageFragment$8  reason: invalid class name */
    /* loaded from: classes.dex */
    public class AnonymousClass8 implements View.OnClickListener {
        AtomicBoolean loading = new AtomicBoolean(false);

        AnonymousClass8() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View v) {
            Telemetry.logEvent("Fitness/Golf/Intro Video");
            VideoUtils.playVideo(Constants.COURSE_VIDEO_URL, GolfLandingPageFragment.this.getActivity(), new Callback<Void>() { // from class: com.microsoft.kapp.fragments.golf.GolfLandingPageFragment.8.1
                @Override // com.microsoft.kapp.Callback
                public void onError(Exception ex) {
                    AnonymousClass8.this.loading.set(false);
                }

                @Override // com.microsoft.kapp.Callback
                public void callback(Void result) {
                    AnonymousClass8.this.loading.set(false);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startGolfSearchActivity(boolean isNearby) {
        Activity activity = getActivity();
        if (Validate.isActivityAlive(activity)) {
            Intent intent = new Intent(activity, GolfSearchActivity.class);
            intent.putExtra(GolfSearchActivity.GOLF_SEARCH_TYPE, isNearby ? GolfSearchActivity.GOLF_SEARCH_TYPE_NEARBY : GolfSearchActivity.GOLF_SEARCH_TYPE_RECENT);
            startActivity(intent);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setPartnerConnectState(int state) {
        int requireConnectStateShow = (state & 2) == 2 ? 0 : 8;
        int connectingStateShow = (state & 4) == 4 ? 0 : 8;
        int connectedStateShow = (state & 8) == 8 ? 0 : 8;
        int connectErrorStateShow = (state & 16) != 16 ? 8 : 0;
        this.mConnectButton.setVisibility(requireConnectStateShow);
        this.mConnectCheck.setVisibility(connectedStateShow);
        this.mConnectState.setVisibility(connectedStateShow);
        this.mConnectManage.setVisibility(connectedStateShow);
        this.mConnectErrorDetail.setVisibility(connectErrorStateShow);
        this.mConnectRetry.setVisibility(connectErrorStateShow);
        this.mConnectionProgress.setVisibility(connectingStateShow);
        if (connectingStateShow == 0) {
            this.mConnectionProgress.setSlide(5000);
        } else {
            this.mConnectionProgress.setSlide(Interstitial.SLIDE_GONE);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class ConnectToGolfPartnerListnerInWebView implements View.OnClickListener {
        private ConnectToGolfPartnerListnerInWebView() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View v) {
            GolfLandingPageFragment.this.setPartnerConnectState(4);
            GolfLandingPageFragment.this.mCheckConnectState = true;
            Intent intent = new Intent(GolfLandingPageFragment.this.getActivity(), TMAGConnectActivity.class);
            intent.putExtra(GolfLandingPageFragment.IS_PARTNER_CONNECTED, GolfLandingPageFragment.this.mIsPartnerConnected);
            GolfLandingPageFragment.this.startActivity(intent);
        }
    }
}

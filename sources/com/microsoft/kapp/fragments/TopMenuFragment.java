package com.microsoft.kapp.fragments;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import com.microsoft.kapp.KApplication;
import com.microsoft.kapp.R;
import com.microsoft.kapp.activities.DialogPriority;
import com.microsoft.kapp.diagnostics.Telemetry;
import com.microsoft.kapp.diagnostics.TelemetryConstants;
import com.microsoft.kapp.diagnostics.Validate;
import com.microsoft.kapp.event.SyncCompletedEvent;
import com.microsoft.kapp.event.SyncStartedEvent;
import com.microsoft.kapp.event.SyncStatusListener;
import com.microsoft.kapp.models.SyncState;
import com.microsoft.kapp.multidevice.KSyncResult;
import com.microsoft.kapp.services.SettingsProvider;
import com.microsoft.kapp.utils.CommonUtils;
import com.microsoft.kapp.utils.Constants;
import com.microsoft.kapp.utils.SyncUtils;
import com.microsoft.kapp.utils.ViewUtils;
import com.microsoft.kapp.views.CustomGlyphView;
import com.microsoft.krestsdk.auth.MsaAuth;
import com.microsoft.krestsdk.auth.credentials.CredentialsManager;
import java.lang.ref.WeakReference;
import javax.inject.Inject;
/* loaded from: classes.dex */
public class TopMenuFragment extends BaseFragment implements SyncStatusListener, ValueAnimator.AnimatorUpdateListener {
    private static final int SYNC_ANIMATION_DELAY = 90;
    @Inject
    MsaAuth mAuthService;
    @Inject
    CredentialsManager mCredentialsManager;
    private View mDivider;
    private RelativeLayout mRootLayout;
    @Inject
    SettingsProvider mSettingsProvider;
    private int mSyncProgress;
    private ValueAnimator mSyncProgressAnimator;
    private ProgressBar mSyncProgressBar;
    private WeakReference<OnTopMenuClickListener> mWeakListener;
    private ImageView mWhatsNewNotificationDot;
    private final View.OnClickListener mOnLeftButtonPressed = new View.OnClickListener() { // from class: com.microsoft.kapp.fragments.TopMenuFragment.1
        @Override // android.view.View.OnClickListener
        public void onClick(View v) {
            Telemetry.logPage(TelemetryConstants.PageViews.NAVIGATION_LEFT_NAVIGATION);
            TopMenuFragment.this.openLeftNav();
        }
    };
    private BroadcastReceiver mLeftNavNotificationBroadReceiver = new BroadcastReceiver() { // from class: com.microsoft.kapp.fragments.TopMenuFragment.2
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String itemTarget = intent.getStringExtra(Constants.LEFTNAV_NOTIFICATION_CLEAR);
            String whatsNewString = TopMenuFragment.this.getResources().getString(R.string.navigation_item_title_what_s_new);
            if (!TextUtils.isEmpty(itemTarget) && itemTarget.equals(whatsNewString) && TopMenuFragment.this.mWhatsNewNotificationDot.getVisibility() != 4) {
                TopMenuFragment.this.mWhatsNewNotificationDot.setVisibility(4);
            }
        }
    };

    /* loaded from: classes.dex */
    public interface OnTopMenuClickListener {
        void onLeftNavClick();
    }

    /* loaded from: classes.dex */
    public interface TopMenuConfiguration {
        void setTopMenuColor(int i);

        void setTopMenuDividerVisibility(int i);
    }

    public static Fragment newInstance() {
        return new TopMenuFragment();
    }

    @Override // com.microsoft.kapp.fragments.BaseFragment, android.support.v4.app.Fragment
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        KApplication application = (KApplication) getActivity().getApplication();
        application.inject(this);
    }

    @Override // android.support.v4.app.Fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_top_menu, container, false);
        CustomGlyphView leftHinge = (CustomGlyphView) ViewUtils.getValidView(rootView, R.id.left_nav_button, CustomGlyphView.class);
        this.mSyncProgressBar = (ProgressBar) ViewUtils.getValidView(rootView, R.id.sync_progress_bar, ProgressBar.class);
        this.mRootLayout = (RelativeLayout) ViewUtils.getValidView(rootView, R.id.top_menu_layout, RelativeLayout.class);
        this.mWhatsNewNotificationDot = (ImageView) ViewUtils.getValidView(rootView, R.id.whatsnew_notification_dot, ImageView.class);
        this.mDivider = (View) ViewUtils.getValidView(rootView, R.id.top_menu_divider, View.class);
        if (!this.mSettingsProvider.isNotificationWhatsNewEnabled()) {
            this.mWhatsNewNotificationDot.setVisibility(4);
        }
        this.mSyncProgressBar.setVisibility(4);
        leftHinge.setOnClickListener(this.mOnLeftButtonPressed);
        ViewTreeObserver viewTreeObserver = rootView.getViewTreeObserver();
        ViewTreeObserver.OnGlobalLayoutListener layoutListener = new ViewTreeObserver.OnGlobalLayoutListener() { // from class: com.microsoft.kapp.fragments.TopMenuFragment.3
            @Override // android.view.ViewTreeObserver.OnGlobalLayoutListener
            public void onGlobalLayout() {
                rootView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        };
        if (viewTreeObserver.isAlive()) {
            viewTreeObserver.addOnGlobalLayoutListener(layoutListener);
        }
        return rootView;
    }

    @Override // com.microsoft.kapp.fragments.BaseFragment, android.support.v4.app.Fragment
    public void onResume() {
        super.onResume();
        int currentProgress = this.mMultiDeviceManager.getCurrentSyncProgress();
        if (currentProgress != 0) {
            startOrUpdateSyncAnimation(currentProgress, true);
        }
        updateSyncProgessUI();
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(this.mLeftNavNotificationBroadReceiver, new IntentFilter(Constants.LEFTNAV_NOTIFICATION_BROADCAST_FILTER));
    }

    @Override // com.microsoft.kapp.fragments.BaseFragment, android.support.v4.app.Fragment
    public void onPause() {
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(this.mLeftNavNotificationBroadReceiver);
        super.onPause();
    }

    @Override // android.support.v4.app.Fragment
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mWeakListener = new WeakReference<>(OnTopMenuClickListener.class.cast(activity));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void openLeftNav() {
        OnTopMenuClickListener listener;
        if (this.mWeakListener != null && (listener = this.mWeakListener.get()) != null) {
            listener.onLeftNavClick();
        }
    }

    public void setBackgroundColor(int color) {
        this.mRootLayout.setBackgroundColor(color);
    }

    @Override // com.microsoft.kapp.fragments.BaseFragment, com.microsoft.kapp.event.SyncStatusListener
    public void onSyncStarted(SyncStartedEvent e) {
        updateSyncProgessUI();
    }

    @Override // com.microsoft.kapp.fragments.BaseFragment
    public void setTopMenuDividerVisibility() {
    }

    public void setDividerVisibility(int visibility) {
        this.mDivider.setVisibility(visibility);
    }

    private void updateSyncProgessUI() {
        SyncState syncState = this.mMultiDeviceManager.getState();
        Activity activity = getActivity();
        if (Validate.isActivityAlive(activity)) {
            switch (syncState) {
                case COMPLETED:
                case NOT_STARTED:
                    this.mSyncProgressBar.setVisibility(4);
                    endSyncAnimation();
                    return;
                case PRE_COMPLETE:
                    if (this.mSyncProgressBar.getVisibility() == 4) {
                        return;
                    }
                    break;
            }
            this.mSyncProgressBar.setVisibility(0);
        }
    }

    @Override // com.microsoft.kapp.fragments.BaseFragment, com.microsoft.kapp.event.SyncStatusListener
    public void onSyncCompleted(SyncCompletedEvent event) {
        if (event != null && event.getSyncResult() != null) {
            boolean isForgroundSync = event.getSyncResult().getIsForgroundSync();
            boolean isPopUpErrorAllowed = event.getSyncResult().getIsPopUpErrorAllowed();
            KSyncResult syncResult = event.getSyncResult().getSyncResult();
            if (syncResult != null) {
                if (isAdded() && isForgroundSync && !syncResult.isSuccess()) {
                    if (!syncResult.isFirmwareUpdateError() && isPopUpErrorAllowed) {
                        if (syncResult.isMultipleBandsPairedError()) {
                            getDialogManager().showMultipleDevicesConnectedError(getActivity());
                        } else {
                            getDialogManager().showDialog(getActivity(), Integer.valueOf(CommonUtils.getCargoExceptionErrorMesgTitle(syncResult)), Integer.valueOf(CommonUtils.getCargoExceptionErrorMesgBody(syncResult)), DialogPriority.LOW);
                        }
                    }
                } else if (!isForgroundSync && syncResult.isFirmwareUpdateError()) {
                    SyncUtils.buildFirmwareUpdateRequiredSyncNotification(getActivity(), 0);
                }
            }
            if (isAdded()) {
                updateSyncProgessUI();
            }
        }
    }

    @Override // com.microsoft.kapp.fragments.BaseFragment, com.microsoft.kapp.event.SyncStatusListener
    public void onSyncPreComplete(SyncCompletedEvent e) {
    }

    @Override // com.microsoft.kapp.fragments.BaseFragment, com.microsoft.kapp.event.SyncStatusListener
    public void onSyncProgress(int progressPercentage) {
        startOrUpdateSyncAnimation(progressPercentage);
    }

    @Override // com.microsoft.kapp.fragments.BaseFragment, com.microsoft.kapp.event.SyncStatusListener
    public void onSyncTerminated() {
        updateSyncProgessUI();
    }

    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
    public void onAnimationUpdate(ValueAnimator animation) {
        this.mSyncProgress = ((Integer) this.mSyncProgressAnimator.getAnimatedValue()).intValue();
        if (this.mSyncProgress > 100) {
            this.mSyncProgress = 100;
        }
        this.mSyncProgressBar.setProgress(this.mSyncProgress);
    }

    private void endSyncAnimation() {
        if (this.mSyncProgressAnimator != null && isAdded()) {
            this.mSyncProgressAnimator.cancel();
            this.mSyncProgress = 0;
            this.mSyncProgressBar.setProgress(this.mSyncProgress);
        }
    }

    protected void startOrUpdateSyncAnimation(int syncProgress) {
        startOrUpdateSyncAnimation(syncProgress, false);
    }

    protected void startOrUpdateSyncAnimation(int syncProgress, boolean jumpToProgress) {
        updateSyncProgessUI();
        if (jumpToProgress) {
            this.mSyncProgress = syncProgress;
        }
        if (this.mSyncProgressAnimator != null && this.mSyncProgressAnimator.isRunning()) {
            this.mSyncProgressAnimator.setIntValues(this.mSyncProgress, syncProgress);
            return;
        }
        this.mSyncProgressAnimator = ValueAnimator.ofInt(this.mSyncProgress, syncProgress);
        this.mSyncProgressAnimator.addUpdateListener(this);
        this.mSyncProgressAnimator.setDuration(Math.abs(syncProgress - this.mSyncProgress) * 90);
        this.mSyncProgressAnimator.setInterpolator(new LinearInterpolator());
        this.mSyncProgressAnimator.start();
    }
}

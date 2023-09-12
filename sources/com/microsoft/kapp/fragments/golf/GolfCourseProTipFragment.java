package com.microsoft.kapp.fragments.golf;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.microsoft.kapp.R;
import com.microsoft.kapp.activities.DialogPriority;
import com.microsoft.kapp.activities.KAppHostActivity;
import com.microsoft.kapp.diagnostics.Telemetry;
import com.microsoft.kapp.diagnostics.TelemetryConstants;
import com.microsoft.kapp.diagnostics.Validate;
import com.microsoft.kapp.fragments.BaseFragmentWithOfflineSupport;
import com.microsoft.kapp.services.golf.GolfService;
import com.microsoft.kapp.utils.CommonUtils;
import com.microsoft.kapp.utils.ViewUtils;
import com.microsoft.kapp.widgets.Interstitial;
import javax.inject.Inject;
/* loaded from: classes.dex */
public class GolfCourseProTipFragment extends BaseFragmentWithOfflineSupport implements GolfCourseSyncBroadcastListener {
    @Inject
    GolfService golfService;
    private KAppHostActivity mActivity;
    private String mCourseId;
    private GolfCourseBroadcastSyncReceiver mGolfCourseBroadcastSyncReceiver;
    private Interstitial mInterstitial;
    private String mTeeId;

    public static GolfCourseProTipFragment newInstance(String courseId, String teeId) {
        GolfCourseProTipFragment fragment = new GolfCourseProTipFragment();
        Bundle bundle = new Bundle();
        bundle.putString(GolfCourseDetailsFragment.COURSE_ID, courseId);
        bundle.putString(GolfCourseTeePickFragment.TEE_ID, teeId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override // com.microsoft.kapp.fragments.BaseFragmentWithOfflineSupport
    public View onCreateChildView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View childView = inflater.inflate(R.layout.golf_course_pro_tip_fragment, container, false);
        Bundle bundle = savedInstanceState == null ? getArguments() : savedInstanceState;
        this.mTeeId = bundle.getString(GolfCourseTeePickFragment.TEE_ID);
        this.mCourseId = bundle.getString(GolfCourseDetailsFragment.COURSE_ID);
        this.mInterstitial = (Interstitial) ViewUtils.getValidView(childView, R.id.golf_sync_interstitial, Interstitial.class);
        this.mGolfCourseBroadcastSyncReceiver = new GolfCourseBroadcastSyncReceiver(this);
        IntentFilter filter = new IntentFilter();
        filter.addAction("Sync");
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(this.mGolfCourseBroadcastSyncReceiver, filter);
        this.mActivity = (KAppHostActivity) getActivity();
        this.mActivity.setBackButtonState(true);
        setState(1234);
        return childView;
    }

    @Override // android.support.v4.app.Fragment
    public void onStart() {
        super.onStart();
        syncToDevice();
    }

    @Override // com.microsoft.kapp.fragments.BaseFragment, android.support.v4.app.Fragment
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(GolfCourseTeePickFragment.TEE_ID, this.mTeeId);
        outState.putString(GolfCourseDetailsFragment.COURSE_ID, this.mCourseId);
    }

    @Override // com.microsoft.kapp.fragments.BaseFragment, android.support.v4.app.Fragment
    public void onResume() {
        super.onResume();
        Telemetry.logPage(TelemetryConstants.PageViews.FITNESS_GOLF_COURSE_PRO_TIP);
    }

    @Override // android.support.v4.app.Fragment
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(this.mGolfCourseBroadcastSyncReceiver);
    }

    @Override // com.microsoft.kapp.fragments.golf.GolfCourseSyncBroadcastListener
    public void onGolfCourseSyncBroadcastReceived(Context context, Intent data) {
        int status = data.getIntExtra("Action", Integer.MIN_VALUE);
        int errorId = data.getIntExtra("ErrorId", Integer.MIN_VALUE);
        if (Validate.isActivityAlive(getActivity())) {
            if (1 == status) {
                getActivity().finish();
            } else if (2 == status) {
                this.mActivity.setBackButtonState(false);
                this.mInterstitial.setVisibility(8);
                if (isAdded()) {
                    if (errorId == 1) {
                        handleTileOpenError();
                    } else if (errorId == 4) {
                        handleBTError();
                    } else if (errorId == 3) {
                        handleNetworkError(R.string.network_error_detail_text);
                    } else if (errorId == 5) {
                        handleTimeoutError();
                    } else if (errorId == 6) {
                        handleNetworkError(R.string.connection_error_detail_text);
                    } else if (errorId == 0) {
                        handleGenericError();
                    }
                }
            }
        }
    }

    private void handleTileOpenError() {
        getDialogManager().showDialog(getActivity(), Integer.valueOf((int) R.string.try_again), Integer.valueOf((int) R.string.golf_sync_try_again_detail_text), R.string.retry, new RetryListener(), new CancelListener(), DialogPriority.LOW);
    }

    private void handleGenericError() {
        if (isAdded()) {
            getDialogManager().showDialog(getActivity(), Integer.valueOf((int) R.string.system_error), Integer.valueOf((int) R.string.sync_system_error_detail_text), new CancelListener(), DialogPriority.LOW);
        }
    }

    private void handleTimeoutError() {
        if (isAdded()) {
            getDialogManager().showDialog(getActivity(), Integer.valueOf((int) R.string.try_again), Integer.valueOf((int) R.string.sync_try_again_detail_text), R.string.retry, new RetryListener(), new CancelListener(), DialogPriority.LOW);
        }
    }

    private void handleNetworkError(int errorDetailResId) {
        if (isAdded()) {
            if (CommonUtils.isNetworkAvailable(getActivity())) {
                getDialogManager().showDialog(getActivity(), Integer.valueOf((int) R.string.network_error), Integer.valueOf(errorDetailResId), R.string.retry, new RetryListener(), new CancelListener(), DialogPriority.LOW);
                return;
            }
            this.mActivity.setBackButtonState(false);
            setState(1235);
        }
    }

    private void handleBTError() {
        if (isAdded()) {
            getDialogManager().showDialog(getActivity(), Integer.valueOf((int) R.string.band_error), Integer.valueOf((int) R.string.band_error_detail_text), R.string.retry, new RetryListener(), R.string.cancel, new CancelListener(), DialogPriority.LOW);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void syncToDevice() {
        if (this.mInterstitial.getVisibility() == 8) {
            this.mInterstitial.setVisibility(0);
        }
        this.golfService.pushGolfCourseDetailToDevice(this.mCourseId, this.mTeeId);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class RetryListener implements DialogInterface.OnClickListener {
        private RetryListener() {
        }

        @Override // android.content.DialogInterface.OnClickListener
        public void onClick(DialogInterface dialog, int which) {
            GolfCourseProTipFragment.this.syncToDevice();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class CancelListener implements DialogInterface.OnClickListener {
        private CancelListener() {
        }

        @Override // android.content.DialogInterface.OnClickListener
        public void onClick(DialogInterface dialog, int which) {
            GolfCourseProTipFragment.this.getActivity().finish();
        }
    }
}

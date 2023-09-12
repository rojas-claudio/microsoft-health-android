package com.microsoft.kapp.fragments.golf;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.microsoft.kapp.ActivityScopedCallback;
import com.microsoft.kapp.Callback;
import com.microsoft.kapp.R;
import com.microsoft.kapp.activities.DialogPriority;
import com.microsoft.kapp.activities.HomeActivity;
import com.microsoft.kapp.activities.LevelTwoBaseActivity;
import com.microsoft.kapp.diagnostics.Telemetry;
import com.microsoft.kapp.diagnostics.TelemetryConstants;
import com.microsoft.kapp.fragments.BaseFragment;
import com.microsoft.kapp.fragments.BaseFragmentWithOfflineSupport;
import com.microsoft.kapp.fragments.ManageTilesFragment;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.kapp.models.golf.CourseDetails;
import com.microsoft.kapp.models.strapp.DefaultStrappUUID;
import com.microsoft.kapp.services.SettingsProvider;
import com.microsoft.kapp.services.golf.GolfService;
import com.microsoft.kapp.utils.ActivityUtils;
import com.microsoft.kapp.utils.CommonUtils;
import com.microsoft.kapp.utils.SyncUtils;
import com.microsoft.kapp.utils.ViewUtils;
import com.microsoft.kapp.views.CustomGlyphView;
import com.microsoft.kapp.views.SyncToBand;
import com.microsoft.kapp.views.TrackerStatsWidget;
import javax.inject.Inject;
/* loaded from: classes.dex */
public class GolfCourseDetailsFragment extends BaseFragmentWithOfflineSupport implements GolfCourseSyncBroadcastListener {
    public static final String COURSE_ID = "Course_ID";
    public static final String FROM_SPLIT_TILE = "From_Split_Tile";
    private TextView mAddress;
    private CustomGlyphView mBackArrow;
    private View mChildView;
    private CourseDetails mCourseDetails;
    private String mCourseId;
    private TextView mCourseName;
    private RelativeLayout mCourseSyncContainer;
    private TextView mFindOtherCourses;
    private GolfCourseBroadcastSyncReceiver mGolfCourseBroadcaseSyncReceiver;
    @Inject
    GolfService mGolfService;
    private TextView mPhone;
    @Inject
    SettingsProvider mSettingsProvider;
    private TextView mSyncTeeText;
    private SyncToBand mSyncToBand;
    private CustomGlyphView mTeeEditLink;
    private TrackerStatsWidget mTrackerStats;
    private TextView mWebsite;

    public static BaseFragment newInstance(String courseId, String referrer) {
        GolfCourseDetailsFragment fragment = new GolfCourseDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putString(COURSE_ID, courseId);
        bundle.putString(BaseFragment.ARG_REFERRER, referrer);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override // com.microsoft.kapp.fragments.BaseFragment, android.support.v4.app.Fragment
    public void onResume() {
        super.onResume();
        Telemetry.logPage(TelemetryConstants.PageViews.FITNESS_GOLF_COURSE_DEATILS, Telemetry.createSingleProperty("Referrer", getArguments().getString(BaseFragment.ARG_REFERRER)));
    }

    @Override // com.microsoft.kapp.fragments.BaseFragmentWithOfflineSupport
    public View onCreateChildView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.mChildView = inflater.inflate(R.layout.golf_course_details_fragment, container, false);
        setViewControls();
        wireEventHandlers();
        Bundle bundle = savedInstanceState == null ? getArguments() : savedInstanceState;
        this.mCourseId = bundle.getString(COURSE_ID);
        Boolean mWasCreatedFromSplitTile = Boolean.valueOf(bundle.getBoolean(FROM_SPLIT_TILE));
        this.mGolfCourseBroadcaseSyncReceiver = new GolfCourseBroadcastSyncReceiver(this);
        IntentFilter filter = new IntentFilter();
        filter.addAction("Sync");
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(this.mGolfCourseBroadcaseSyncReceiver, filter);
        if (mWasCreatedFromSplitTile.booleanValue()) {
            this.mBackArrow.setVisibility(8);
        }
        return this.mChildView;
    }

    @Override // android.support.v4.app.Fragment
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setState(1233);
        this.mGolfService.getCourseDetail(this.mCourseId, getActivity(), new ActivityScopedCallback(this, new Callback<CourseDetails>() { // from class: com.microsoft.kapp.fragments.golf.GolfCourseDetailsFragment.1
            @Override // com.microsoft.kapp.Callback
            public void callback(CourseDetails courseDetails) {
                if (courseDetails != null) {
                    GolfCourseDetailsFragment.this.setState(1234);
                    GolfCourseDetailsFragment.this.mCourseDetails = courseDetails;
                    GolfCourseDetailsFragment.this.showCourseDetails();
                    GolfCourseDetailsFragment.this.setSyncState();
                }
            }

            @Override // com.microsoft.kapp.Callback
            public void onError(Exception ex) {
                KLog.d(GolfCourseDetailsFragment.this.TAG, "Get call for course id: %s failed", GolfCourseDetailsFragment.this.mCourseId);
                GolfCourseDetailsFragment.this.setState(1235);
            }
        }));
    }

    @Override // com.microsoft.kapp.fragments.BaseFragment, android.support.v4.app.Fragment
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(COURSE_ID, this.mCourseId);
    }

    @Override // android.support.v4.app.Fragment
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(this.mGolfCourseBroadcaseSyncReceiver);
    }

    @Override // com.microsoft.kapp.fragments.golf.GolfCourseSyncBroadcastListener
    public void onGolfCourseSyncBroadcastReceived(Context context, Intent data) {
        int status = data.getIntExtra("Action", -1);
        if (1 == status) {
            this.mCourseDetails.setHasCourseBeenSynced(true);
            setSyncState();
        }
    }

    private void setViewControls() {
        this.mCourseName = (TextView) ViewUtils.getValidView(this.mChildView, R.id.golf_course_name, TextView.class);
        this.mTrackerStats = (TrackerStatsWidget) ViewUtils.getValidView(this.mChildView, R.id.golf_course_details_trackers, TrackerStatsWidget.class);
        this.mWebsite = (TextView) ViewUtils.getValidView(this.mChildView, R.id.golf_course_website, TextView.class);
        this.mPhone = (TextView) ViewUtils.getValidView(this.mChildView, R.id.golf_course_phone, TextView.class);
        this.mAddress = (TextView) ViewUtils.getValidView(this.mChildView, R.id.golf_course_address, TextView.class);
        this.mBackArrow = (CustomGlyphView) ViewUtils.getValidView(this.mChildView, R.id.back, CustomGlyphView.class);
        this.mSyncToBand = (SyncToBand) ViewUtils.getValidView(this.mChildView, R.id.sync_to_band, SyncToBand.class);
        this.mFindOtherCourses = (TextView) ViewUtils.getValidView(this.mChildView, R.id.golf_find_other_courses, TextView.class);
        this.mTeeEditLink = (CustomGlyphView) ViewUtils.getValidView(this.mChildView, R.id.golf_sync_tee_edit_link, CustomGlyphView.class);
        this.mCourseSyncContainer = (RelativeLayout) ViewUtils.getValidView(this.mChildView, R.id.golf_course_synced_container, RelativeLayout.class);
        this.mSyncTeeText = (TextView) ViewUtils.getValidView(this.mChildView, R.id.golf_tee_text, TextView.class);
        this.mTeeEditLink = (CustomGlyphView) ViewUtils.getValidView(this.mChildView, R.id.golf_sync_tee_edit_link, CustomGlyphView.class);
    }

    private void wireEventHandlers() {
        this.mBackArrow.setOnClickListener(new View.OnClickListener() { // from class: com.microsoft.kapp.fragments.golf.GolfCourseDetailsFragment.2
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                GolfCourseDetailsFragment.this.getActivity().finish();
            }
        });
        this.mFindOtherCourses.setOnClickListener(new View.OnClickListener() { // from class: com.microsoft.kapp.fragments.golf.GolfCourseDetailsFragment.3
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                Intent intent = new Intent(GolfCourseDetailsFragment.this.getActivity(), HomeActivity.class);
                intent.putExtra(HomeActivity.STARTING_PAGE, GolfLandingPageFragment.class.getSimpleName());
                intent.putExtra("Referrer", TelemetryConstants.PageViews.Referrers.GOLF_DETAILS);
                GolfCourseDetailsFragment.this.getActivity().startActivity(intent);
            }
        });
        this.mTeeEditLink.setOnClickListener(new View.OnClickListener() { // from class: com.microsoft.kapp.fragments.golf.GolfCourseDetailsFragment.4
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                GolfCourseDetailsFragment.this.goToTeeSelectionPage();
            }
        });
        this.mSyncToBand.setSyncClickListnere(new View.OnClickListener() { // from class: com.microsoft.kapp.fragments.golf.GolfCourseDetailsFragment.5
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                int strappCanSyncResult = SyncUtils.canStrappSync(DefaultStrappUUID.STRAPP_GOLF, GolfCourseDetailsFragment.this.mSettingsProvider, GolfCourseDetailsFragment.this.mMultiDeviceManager);
                if (strappCanSyncResult != 1) {
                    GolfCourseDetailsFragment.this.goToTeeSelectionPage();
                    return;
                }
                LevelTwoBaseActivity activity = (LevelTwoBaseActivity) GolfCourseDetailsFragment.this.getActivity();
                activity.getDialogManager().showDialog(activity, Integer.valueOf((int) R.string.manage_tiles), Integer.valueOf((int) R.string.golf_sync_go_to_manage_tiles), new DialogInterface.OnClickListener() { // from class: com.microsoft.kapp.fragments.golf.GolfCourseDetailsFragment.5.1
                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(GolfCourseDetailsFragment.this.getActivity(), HomeActivity.class);
                        intent.putExtra(HomeActivity.STARTING_PAGE, ManageTilesFragment.class.getSimpleName());
                        GolfCourseDetailsFragment.this.getActivity().startActivity(intent);
                    }
                }, (DialogInterface.OnClickListener) null, DialogPriority.LOW);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showCourseDetails() {
        this.mCourseName.setText(this.mCourseDetails.getCourseName());
        this.mTrackerStats.addStat(this.mCourseDetails.getTotalNumberOfHolesStat());
        this.mTrackerStats.addStat(this.mCourseDetails.getCourseTypeStat());
        this.mTrackerStats.addStat(this.mCourseDetails.getCourseRatingStat());
        this.mTrackerStats.addStat(this.mCourseDetails.getSlopeRatingStat());
        ensureLinkText(this.mWebsite, this.mCourseDetails.getWebsite());
        ensureLinkText(this.mPhone, this.mCourseDetails.getPhoneNumber());
        ensureLinkText(this.mAddress, this.mCourseDetails.getAddress());
    }

    private void ensureLinkText(TextView textView, CharSequence text) {
        if (TextUtils.isEmpty(text)) {
            textView.setVisibility(8);
            return;
        }
        textView.setText(text);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setSyncState() {
        int syncState = SyncUtils.canStrappSync(DefaultStrappUUID.STRAPP_GOLF, this.mSettingsProvider, this.mMultiDeviceManager);
        boolean hasTees = this.mCourseDetails.hasTees();
        if (!CommonUtils.isNetworkAvailable(getActivity()) || syncState == 2 || !hasTees) {
            this.mSyncToBand.setVisibility(8);
            this.mCourseSyncContainer.setVisibility(8);
        } else if (this.mCourseDetails.hasCourseBeenSynced()) {
            this.mSyncToBand.setState(SyncToBand.SyncStatus.Synced);
            this.mCourseSyncContainer.setVisibility(0);
            this.mSyncTeeText.setText(getActivity().getString(R.string.golf_tee_set_to_play, new Object[]{this.mCourseDetails.getSyncTeeText()}));
        } else {
            this.mSyncToBand.setState(SyncToBand.SyncStatus.SyncRequired);
            this.mCourseSyncContainer.setVisibility(8);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void goToTeeSelectionPage() {
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(GolfCourseTeePickFragment.GOLF_TEE_PAIR, this.mCourseDetails.getUniqueTees());
        bundle.putString(COURSE_ID, this.mCourseId);
        ActivityUtils.launchLevelTwoActivity(getActivity(), GolfCourseTeePickFragment.class, bundle);
    }
}

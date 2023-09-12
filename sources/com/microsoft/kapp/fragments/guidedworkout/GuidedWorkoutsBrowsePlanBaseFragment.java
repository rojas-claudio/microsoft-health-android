package com.microsoft.kapp.fragments.guidedworkout;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.microsoft.kapp.ActivityScopedCallback;
import com.microsoft.kapp.Callback;
import com.microsoft.kapp.R;
import com.microsoft.kapp.adapters.GuidedWorkoutPlanPagerAdapter;
import com.microsoft.kapp.fragments.BaseFragmentWithOfflineSupport;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.kapp.models.LoadStatus;
import com.microsoft.kapp.multidevice.MultiDeviceManager;
import com.microsoft.kapp.services.SettingsProvider;
import com.microsoft.kapp.services.guidedworkout.GuidedWorkoutService;
import com.microsoft.kapp.services.healthandfitness.models.WorkoutPlan;
import com.microsoft.kapp.utils.MultipleRequestManager;
import com.microsoft.kapp.utils.ViewUtils;
import com.microsoft.kapp.widgets.PagerTitleStrip;
import com.microsoft.krestsdk.models.ScheduledWorkout;
import java.util.List;
import javax.inject.Inject;
/* loaded from: classes.dex */
public abstract class GuidedWorkoutsBrowsePlanBaseFragment extends BaseFragmentWithOfflineSupport {
    private final String TAG = getClass().getSimpleName();
    private TextView mBackButton;
    @Inject
    GuidedWorkoutService mGuidedWorkoutService;
    @Inject
    MultiDeviceManager mMultiDeviceManager;
    private MultipleRequestManager mMultipleRequestManager;
    private GuidedWorkoutPlanPagerAdapter mPagerAdapter;
    private PagerTitleStrip mPagerTitleStrip;
    @Inject
    SettingsProvider mSettings;
    private ViewPager mViewPager;
    private TextView mWorkoutName;
    private WorkoutPlan mWorkoutPlan;
    private String mWorkoutPlanId;
    private List<ScheduledWorkout> mWorkoutPlanSchedule;

    protected abstract boolean getIsHomeTileMode();

    protected abstract int getLayoutResourceId();

    protected abstract int getPagerTitleStripColor();

    @Override // com.microsoft.kapp.fragments.BaseFragment, android.support.v4.app.Fragment
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mWorkoutPlanId = getArguments().getString("workoutPlanId");
    }

    @Override // com.microsoft.kapp.fragments.BaseFragmentWithOfflineSupport
    public View onCreateChildView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutResourceId(), container, false);
        this.mViewPager = (ViewPager) ViewUtils.getValidView(view, R.id.pager, ViewPager.class);
        this.mPagerTitleStrip = (PagerTitleStrip) ViewUtils.getValidView(view, R.id.pager_title_strip, PagerTitleStrip.class);
        this.mPagerTitleStrip.setBackgroundColor(getResources().getColor(getPagerTitleStripColor()));
        this.mWorkoutName = (TextView) ViewUtils.getValidView(view, R.id.workout_name, TextView.class);
        this.mBackButton = (TextView) ViewUtils.getValidView(view, R.id.back, TextView.class);
        this.mBackButton.setOnClickListener(new View.OnClickListener() { // from class: com.microsoft.kapp.fragments.guidedworkout.GuidedWorkoutsBrowsePlanBaseFragment.1
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                GuidedWorkoutsBrowsePlanBaseFragment.this.exit();
            }
        });
        return view;
    }

    @Override // android.support.v4.app.Fragment
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.mMultipleRequestManager = new MultipleRequestManager(2, new MultipleRequestManager.OnRequestCompleteListener() { // from class: com.microsoft.kapp.fragments.guidedworkout.GuidedWorkoutsBrowsePlanBaseFragment.2
            @Override // com.microsoft.kapp.utils.MultipleRequestManager.OnRequestCompleteListener
            public void requestComplete(LoadStatus status) {
                if (status != LoadStatus.LOADED || GuidedWorkoutsBrowsePlanBaseFragment.this.mWorkoutPlan == null) {
                    KLog.d(GuidedWorkoutsBrowsePlanBaseFragment.this.TAG, "Error fetching WorkoutPlan data (%s)! Browsing the workout plan details aborted!", GuidedWorkoutsBrowsePlanBaseFragment.this.mWorkoutPlanId);
                    GuidedWorkoutsBrowsePlanBaseFragment.this.setState(1235);
                    return;
                }
                GuidedWorkoutsBrowsePlanBaseFragment.this.setState(1234);
                GuidedWorkoutsBrowsePlanBaseFragment.this.showWorkoutPlanDetails();
            }
        });
        fetchWorkoutPlan(this.mWorkoutPlanId);
        fetchWorkoutPlanSchedule(this.mWorkoutPlanId);
    }

    private void fetchWorkoutPlan(final String workoutPlanId) {
        this.mGuidedWorkoutService.getHnFWorkoutPlanDetails(workoutPlanId, new ActivityScopedCallback(this, new Callback<WorkoutPlan>() { // from class: com.microsoft.kapp.fragments.guidedworkout.GuidedWorkoutsBrowsePlanBaseFragment.3
            @Override // com.microsoft.kapp.Callback
            public void callback(WorkoutPlan workoutPlan) {
                if (workoutPlan != null) {
                    GuidedWorkoutsBrowsePlanBaseFragment.this.mWorkoutPlan = workoutPlan;
                    GuidedWorkoutsBrowsePlanBaseFragment.this.mMultipleRequestManager.notifyRequestSucceeded();
                    return;
                }
                GuidedWorkoutsBrowsePlanBaseFragment.this.mMultipleRequestManager.notifyRequestFailed();
            }

            @Override // com.microsoft.kapp.Callback
            public void onError(Exception ex) {
                KLog.d(GuidedWorkoutsBrowsePlanBaseFragment.this.TAG, "Error during Fetching WorkoutPlan: %s", workoutPlanId);
                GuidedWorkoutsBrowsePlanBaseFragment.this.mMultipleRequestManager.notifyRequestFailed();
            }
        }));
    }

    private void fetchWorkoutPlanSchedule(final String workoutPlanId) {
        this.mGuidedWorkoutService.getWorkoutPlanSchedules(workoutPlanId, new ActivityScopedCallback(this, new Callback<List<ScheduledWorkout>>() { // from class: com.microsoft.kapp.fragments.guidedworkout.GuidedWorkoutsBrowsePlanBaseFragment.4
            @Override // com.microsoft.kapp.Callback
            public void callback(List<ScheduledWorkout> workoutPlanSchedule) {
                if (workoutPlanSchedule != null) {
                    GuidedWorkoutsBrowsePlanBaseFragment.this.mWorkoutPlanSchedule = workoutPlanSchedule;
                    GuidedWorkoutsBrowsePlanBaseFragment.this.mMultipleRequestManager.notifyRequestSucceeded();
                    return;
                }
                GuidedWorkoutsBrowsePlanBaseFragment.this.mMultipleRequestManager.notifyRequestFailed();
            }

            @Override // com.microsoft.kapp.Callback
            public void onError(Exception ex) {
                KLog.d(GuidedWorkoutsBrowsePlanBaseFragment.this.TAG, "Error during Fetching WorkoutPlanSchedule: %s", workoutPlanId);
                GuidedWorkoutsBrowsePlanBaseFragment.this.mMultipleRequestManager.notifyRequestFailed();
            }
        }));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showWorkoutPlanDetails() {
        this.mPagerAdapter = new GuidedWorkoutPlanPagerAdapter(getChildFragmentManager(), getActivity(), this.mWorkoutPlan, this.mWorkoutPlanSchedule, getIsHomeTileMode(), !this.mMultiDeviceManager.hasBand());
        this.mViewPager.setAdapter(this.mPagerAdapter);
        this.mPagerTitleStrip.setViewPager(this.mViewPager, 0);
        this.mPagerTitleStrip.setBackgroundColor(getResources().getColor(getPagerTitleStripColor()));
        this.mWorkoutName.setText(this.mWorkoutPlan.getName());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void exit() {
        getActivity().finish();
    }

    @Override // android.support.v4.app.Fragment
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        List<Fragment> nestedFragments = getChildFragmentManager().getFragments();
        for (Fragment childFragment : nestedFragments) {
            if (childFragment != null && childFragment.isAdded()) {
                childFragment.onActivityResult(requestCode, resultCode, data);
            }
        }
    }
}

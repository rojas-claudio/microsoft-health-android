package com.microsoft.kapp.fragments.guidedworkout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.microsoft.kapp.R;
import com.microsoft.kapp.activities.WorkoutPlanDiscoveryActivity;
import com.microsoft.kapp.diagnostics.Telemetry;
import com.microsoft.kapp.diagnostics.TelemetryConstants;
import com.microsoft.kapp.diagnostics.Validate;
import com.microsoft.kapp.fragments.BaseFragmentWithOfflineSupport;
import com.microsoft.kapp.services.SettingsProvider;
import com.microsoft.kapp.services.guidedworkout.GuidedWorkoutService;
import com.microsoft.kapp.utils.ActivityUtils;
import com.microsoft.kapp.utils.Constants;
import com.microsoft.kapp.utils.ViewUtils;
import javax.inject.Inject;
/* loaded from: classes.dex */
public class BrowseGuidedWorkoutsFragment extends BaseFragmentWithOfflineSupport {
    private TextView mAllWorkoutsPlan;
    private TextView mFindWorkoutIcon;
    @Inject
    GuidedWorkoutService mGuidedWorkoutService;
    private TextView mPlanByCreated;
    private TextView mPlanByPros;
    private TextView mPlanByTypes;
    private LinearLayout mPlanFavorite;
    @Inject
    SettingsProvider mSettingsProvider;

    @Override // com.microsoft.kapp.fragments.BaseFragmentWithOfflineSupport
    public View onCreateChildView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.guided_workout_fragment, container, false);
        this.mPlanByTypes = (TextView) ViewUtils.getValidView(view, R.id.plan_by_goals_title, TextView.class);
        this.mPlanByPros = (TextView) ViewUtils.getValidView(view, R.id.plan_by_pros_title, TextView.class);
        this.mPlanByCreated = (TextView) ViewUtils.getValidView(view, R.id.plan_by_created_title, TextView.class);
        this.mPlanFavorite = (LinearLayout) ViewUtils.getValidView(view, R.id.plan_by_favorite, LinearLayout.class);
        this.mAllWorkoutsPlan = (TextView) ViewUtils.getValidView(view, R.id.plan_by_all_title, TextView.class);
        this.mFindWorkoutIcon = (TextView) ViewUtils.getValidView(view, R.id.header_find_icon, TextView.class);
        setFeaturedWorkoutCarousel();
        this.mPlanByTypes.setOnClickListener(new View.OnClickListener() { // from class: com.microsoft.kapp.fragments.guidedworkout.BrowseGuidedWorkoutsFragment.1
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                BrowseGuidedWorkoutsFragment.this.startGuidedWorkoutDiscoveryActivity(0);
            }
        });
        this.mPlanByPros.setOnClickListener(new View.OnClickListener() { // from class: com.microsoft.kapp.fragments.guidedworkout.BrowseGuidedWorkoutsFragment.2
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                BrowseGuidedWorkoutsFragment.this.startGuidedWorkoutDiscoveryActivity(1);
            }
        });
        this.mPlanByCreated.setOnClickListener(new View.OnClickListener() { // from class: com.microsoft.kapp.fragments.guidedworkout.BrowseGuidedWorkoutsFragment.3
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                ActivityUtils.launchLevelTwoActivityForResult(BrowseGuidedWorkoutsFragment.this.getActivity(), GuidedWorkoutBrowseCustomFragment.class, null, BrowseGuidedWorkoutsFragment.this, 10004);
            }
        });
        this.mPlanFavorite.setOnClickListener(new View.OnClickListener() { // from class: com.microsoft.kapp.fragments.guidedworkout.BrowseGuidedWorkoutsFragment.4
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                ActivityUtils.launchLevelTwoActivityForResult(BrowseGuidedWorkoutsFragment.this.getActivity(), GuidedWorkoutsFavoritesFragment.class, null, BrowseGuidedWorkoutsFragment.this, 10004);
            }
        });
        this.mAllWorkoutsPlan.setOnClickListener(new View.OnClickListener() { // from class: com.microsoft.kapp.fragments.guidedworkout.BrowseGuidedWorkoutsFragment.5
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                ActivityUtils.launchLevelTwoActivityForResult(BrowseGuidedWorkoutsFragment.this.getActivity(), GuidedWorkoutsBrowseAllFragment.class, null, BrowseGuidedWorkoutsFragment.this, 10004);
            }
        });
        this.mFindWorkoutIcon.setOnClickListener(new View.OnClickListener() { // from class: com.microsoft.kapp.fragments.guidedworkout.BrowseGuidedWorkoutsFragment.6
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                BrowseGuidedWorkoutsFragment.this.startWorkoutPlanSearchActivity();
            }
        });
        setState(1234);
        return view;
    }

    @Override // com.microsoft.kapp.fragments.BaseFragment, android.support.v4.app.Fragment
    public void onResume() {
        super.onResume();
        Telemetry.logPage(TelemetryConstants.PageViews.FITNESS_GUIDED_WORKOUTS_FIND_A_WORKOUT);
    }

    private void setFeaturedWorkoutCarousel() {
        FragmentManager manager = getChildFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        GuidedWorkoutCarouselFragment fragment = GuidedWorkoutCarouselFragment.newInstance();
        ft.replace(R.id.featured_workout_framelayout, fragment);
        ft.commit();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startWorkoutPlanSearchActivity() {
        Activity activity = getActivity();
        if (Validate.isActivityAlive(activity)) {
            Bundle bundle = new Bundle();
            ActivityUtils.launchLevelTwoActivityForResult(activity, GuidedWorkoutsSearchFragment.class, bundle, this, 10003);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startGuidedWorkoutDiscoveryActivity(int planDiscoveryType) {
        Activity activity = getActivity();
        if (Validate.isActivityAlive(activity)) {
            Intent intent = new Intent(activity, WorkoutPlanDiscoveryActivity.class);
            intent.putExtra(Constants.GUIDED_WORKOUT_DISCOVERY_PLAN_TYPE, planDiscoveryType);
            startActivityForResult(intent, 10004);
        }
    }

    @Override // com.microsoft.kapp.fragments.BaseFragment, android.support.v4.app.Fragment
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override // android.support.v4.app.Fragment
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10002 && resultCode == 4) {
            getDialogManager().showNetworkErrorDialog(getActivity());
        }
    }
}

package com.microsoft.kapp.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import com.microsoft.kapp.R;
import com.microsoft.kapp.diagnostics.Telemetry;
import com.microsoft.kapp.diagnostics.TelemetryConstants;
import com.microsoft.kapp.fragments.BaseFragment;
import com.microsoft.kapp.fragments.guidedworkout.GuidedWorkoutNextFragment;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.kapp.utils.Constants;
import com.microsoft.krestsdk.models.ScheduledWorkout;
/* loaded from: classes.dex */
public class WorkoutDetailActivity extends BaseFragmentActivity {
    private static final String TAG = WorkoutDetailActivity.class.getSimpleName();

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.microsoft.kapp.activities.BaseFragmentActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.workout_details_activity);
        if (savedInstanceState == null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            boolean showHeaderTitle = getIntent().getExtras().getBoolean(Constants.KEY_WORKOUT_HEADER_DETAIL);
            boolean isHomeTileMode = getIntent().getExtras().getBoolean(Constants.KEY_MODE);
            ScheduledWorkout scheduledWorkout = (ScheduledWorkout) getIntent().getExtras().getParcelable(Constants.KEY_GUIDED_WORKOUT_SCHEDULED_WORKOUT);
            BaseFragment pairingFragment = GuidedWorkoutNextFragment.newInstance(scheduledWorkout, showHeaderTitle, isHomeTileMode, false);
            ft.replace(R.id.base_layout, pairingFragment);
            try {
                ft.commit();
            } catch (IllegalStateException ex) {
                KLog.e(TAG, "fragment commit transaction failed! this Activity will be aborted", ex);
                finish();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.microsoft.kapp.activities.BaseFragmentActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    public void onResume() {
        super.onResume();
        Telemetry.logPage(TelemetryConstants.PageViews.FITNESS_GUIDED_WORKOUTS_PLAN_WORKOUT_DETAIL);
    }
}

package com.microsoft.kapp.fragments.guidedworkout;

import android.widget.TextView;
import com.microsoft.kapp.R;
import com.microsoft.kapp.diagnostics.Telemetry;
import com.microsoft.kapp.diagnostics.TelemetryConstants;
import com.microsoft.kapp.services.healthandfitness.WorkoutProviderType;
/* loaded from: classes.dex */
public class GuidedWorkoutBrowseCustomFragment extends GuidedWorkoutsBrowseAllFragment {
    @Override // com.microsoft.kapp.fragments.guidedworkout.GuidedWorkoutsByCategoryFragment, com.microsoft.kapp.fragments.BaseFragment, android.support.v4.app.Fragment
    public void onResume() {
        super.onResume();
        Telemetry.logPage(TelemetryConstants.PageViews.FITNESS_GUIDED_WORKOUTS_BROWSE_MYWORKOUTS);
    }

    @Override // com.microsoft.kapp.fragments.guidedworkout.GuidedWorkoutsByCategoryFragment
    protected WorkoutProviderType getWorkoutProviderType() {
        return WorkoutProviderType.Me;
    }

    @Override // com.microsoft.kapp.fragments.guidedworkout.GuidedWorkoutsBrowseAllFragment, com.microsoft.kapp.fragments.guidedworkout.GuidedWorkoutsByCategoryFragment
    protected boolean isSearchFunctionEnabled() {
        return false;
    }

    @Override // com.microsoft.kapp.fragments.guidedworkout.GuidedWorkoutsByCategoryFragment
    public void updateEmptyListTextView(TextView textView) {
        textView.setText(getString(R.string.workout_browse_custom_no_results));
        textView.setGravity(16);
    }
}

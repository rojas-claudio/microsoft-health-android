package com.microsoft.kapp.fragments.guidedworkout;

import android.widget.TextView;
import com.microsoft.kapp.R;
import com.microsoft.kapp.diagnostics.Telemetry;
import com.microsoft.kapp.diagnostics.TelemetryConstants;
import com.microsoft.kapp.guidedworkout.WorkoutPlanSummaryDetails;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.kapp.services.healthandfitness.WorkoutProviderType;
import com.microsoft.kapp.services.healthandfitness.models.WorkoutSummary;
import com.microsoft.kapp.services.healthandfitness.models.WorkoutsResponse;
import com.microsoft.kapp.services.healthandfitness.models.WorkoutsResponseEnvelope;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
/* loaded from: classes.dex */
public class GuidedWorkoutsFavoritesFragment extends GuidedWorkoutsByCategoryFragment {
    @Override // com.microsoft.kapp.fragments.guidedworkout.GuidedWorkoutsByCategoryFragment
    protected List<WorkoutPlanSummaryDetails> filterWorkoutPlansSummaries(WorkoutsResponseEnvelope responseEnvelope) {
        ArrayList<WorkoutPlanSummaryDetails> workoutPlanSummaryDetailsList = null;
        if (responseEnvelope == null) {
            KLog.d(this.TAG, "Corrupt WorkoutsResponseEnvelope for guidedWorkout!");
        } else {
            WorkoutsResponse workoutsResponse = responseEnvelope.getResponse();
            if (workoutsResponse == null) {
                KLog.d(this.TAG, "Corrupt WorkoutsResponseEnvelope for guidedWorkout!");
            } else {
                WorkoutSummary[] workoutSummariesArray = workoutsResponse.getWorkoutSummaries();
                if (workoutSummariesArray == null) {
                    KLog.d(this.TAG, "Corrupt WorkoutsResponseEnvelope for guidedWorkout!");
                } else {
                    workoutPlanSummaryDetailsList = new ArrayList<>();
                    Set<String> favoritePlanIdSet = getFavoritesPlanIdSet();
                    for (WorkoutSummary summary : workoutSummariesArray) {
                        if (summary != null) {
                            try {
                                WorkoutPlanSummaryDetails workoutPlanSummaryDetails = new WorkoutPlanSummaryDetails(summary);
                                if (favoritePlanIdSet.contains(summary.getId())) {
                                    workoutPlanSummaryDetails.setIsFavorite(true);
                                    workoutPlanSummaryDetailsList.add(workoutPlanSummaryDetails);
                                }
                            } catch (Exception ex) {
                                KLog.e(this.TAG, "Corrupt data in Favorites list!", ex);
                            }
                        }
                    }
                }
            }
        }
        return workoutPlanSummaryDetailsList;
    }

    @Override // com.microsoft.kapp.fragments.guidedworkout.GuidedWorkoutsByCategoryFragment, com.microsoft.kapp.fragments.BaseFragment, android.support.v4.app.Fragment
    public void onResume() {
        super.onResume();
        Telemetry.logPage(TelemetryConstants.PageViews.FITNESS_GUIDED_WORKOUTS_BROWSE_FAVORITES);
    }

    @Override // com.microsoft.kapp.fragments.guidedworkout.GuidedWorkoutsByCategoryFragment
    protected boolean isSearchFunctionEnabled() {
        return false;
    }

    @Override // com.microsoft.kapp.fragments.guidedworkout.GuidedWorkoutsByCategoryFragment
    protected String getCategoryName() {
        return getString(R.string.guided_workout_favorites);
    }

    @Override // com.microsoft.kapp.fragments.guidedworkout.GuidedWorkoutsByCategoryFragment
    protected int getCategoryType() {
        return 1;
    }

    @Override // com.microsoft.kapp.fragments.guidedworkout.GuidedWorkoutsByCategoryFragment
    protected WorkoutProviderType getWorkoutProviderType() {
        return WorkoutProviderType.All;
    }

    @Override // com.microsoft.kapp.fragments.guidedworkout.GuidedWorkoutsByCategoryFragment
    protected boolean isFavoritePage() {
        return true;
    }

    @Override // com.microsoft.kapp.fragments.guidedworkout.GuidedWorkoutsByCategoryFragment
    public void updateEmptyListTextView(TextView textView) {
        textView.setText(getString(R.string.workout_browse_favorites_no_results));
        textView.setGravity(16);
    }
}

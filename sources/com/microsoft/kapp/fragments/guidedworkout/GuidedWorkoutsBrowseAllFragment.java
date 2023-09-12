package com.microsoft.kapp.fragments.guidedworkout;

import com.microsoft.kapp.guidedworkout.WorkoutPlanSummaryDetails;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.kapp.services.healthandfitness.models.WorkoutSummary;
import com.microsoft.kapp.services.healthandfitness.models.WorkoutsResponse;
import com.microsoft.kapp.services.healthandfitness.models.WorkoutsResponseEnvelope;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes.dex */
public class GuidedWorkoutsBrowseAllFragment extends GuidedWorkoutsByCategoryFragment {
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
                    for (WorkoutSummary summary : workoutSummariesArray) {
                        if (summary != null) {
                            try {
                                WorkoutPlanSummaryDetails workoutPlanSummaryDetails = new WorkoutPlanSummaryDetails(summary);
                                workoutPlanSummaryDetailsList.add(workoutPlanSummaryDetails);
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

    @Override // com.microsoft.kapp.fragments.guidedworkout.GuidedWorkoutsByCategoryFragment
    protected boolean isSearchFunctionEnabled() {
        return true;
    }

    @Override // com.microsoft.kapp.fragments.guidedworkout.GuidedWorkoutsByCategoryFragment
    protected String getCategoryName() {
        return null;
    }

    @Override // com.microsoft.kapp.fragments.guidedworkout.GuidedWorkoutsByCategoryFragment
    protected int getCategoryType() {
        return 0;
    }

    @Override // com.microsoft.kapp.fragments.guidedworkout.GuidedWorkoutsByCategoryFragment
    protected boolean isFavoritePage() {
        return false;
    }
}

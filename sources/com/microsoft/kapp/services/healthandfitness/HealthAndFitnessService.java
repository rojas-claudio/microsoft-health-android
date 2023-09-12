package com.microsoft.kapp.services.healthandfitness;

import com.microsoft.kapp.Callback;
import com.microsoft.kapp.services.healthandfitness.models.WorkoutPlan;
import com.microsoft.kapp.services.healthandfitness.models.WorkoutsResponseEnvelope;
import com.microsoft.krestsdk.services.KRestException;
/* loaded from: classes.dex */
public interface HealthAndFitnessService {
    void getHnFStrengthExerciseAndWorkoutPlans(Callback<WorkoutsResponseEnvelope> callback);

    void getHnFStrengthWorkoutPlans(WorkoutProviderType workoutProviderType, Callback<WorkoutsResponseEnvelope> callback);

    void getHnFStrengthWorkoutPlans(String str, WorkoutProviderType workoutProviderType, Callback<WorkoutsResponseEnvelope> callback);

    void getHnFStrengthWorkoutPlans(String str, String str2, WorkoutProviderType workoutProviderType, Callback<WorkoutsResponseEnvelope> callback);

    WorkoutPlan getHnFWorkoutPlanDetails(String str) throws KRestException;

    void getHnFWorkoutPlanDetails(String str, Callback<WorkoutPlan> callback);

    void getHnFWorkoutPlans(Callback<WorkoutsResponseEnvelope> callback);
}

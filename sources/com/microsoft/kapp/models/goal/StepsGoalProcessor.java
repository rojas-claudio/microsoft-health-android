package com.microsoft.kapp.models.goal;

import android.content.Context;
import com.microsoft.band.cloud.UserProfileInfo;
import com.microsoft.kapp.R;
import com.microsoft.kapp.device.CargoUserProfile;
import com.microsoft.kapp.diagnostics.Compatibility;
import com.microsoft.kapp.services.SettingsProvider;
/* loaded from: classes.dex */
public class StepsGoalProcessor extends GoalProcessor {
    private static final int FEMALE_STEPS_RATE = 146;
    private static final float FEMALE_WALKING_STRIDE_LENGTH_RATIO = 0.47f;
    private static final int MALE_STEPS_RATE = 142;
    private static final float MALE_WALKING_STRIDE_LENGTH_RATIO = 0.48f;
    private static final int MM_PER_CM = 10;
    private final int mRate;
    private final float mWalkingStrideLength;

    public StepsGoalProcessor(Context context, SettingsProvider settingsProvider) {
        super(context, settingsProvider);
        CargoUserProfile userProfile = getUserProfile();
        float userHeightMM = 0.0f;
        Integer rate = null;
        Float stepLengthRatio = null;
        if (userProfile != null) {
            UserProfileInfo.Gender gender = userProfile.getGender();
            userHeightMM = userProfile.getHeightInMM();
            if (gender == UserProfileInfo.Gender.female) {
                rate = Integer.valueOf((int) FEMALE_STEPS_RATE);
                stepLengthRatio = Float.valueOf((float) FEMALE_WALKING_STRIDE_LENGTH_RATIO);
            } else if (gender == UserProfileInfo.Gender.male) {
                rate = Integer.valueOf((int) MALE_STEPS_RATE);
                stepLengthRatio = Float.valueOf((float) MALE_WALKING_STRIDE_LENGTH_RATIO);
            }
        }
        if (rate == null || stepLengthRatio == null) {
            if (Compatibility.isPublicRelease()) {
                throw new IllegalStateException("Gender is not specified.");
            }
            rate = Integer.valueOf((int) FEMALE_STEPS_RATE);
            stepLengthRatio = Float.valueOf((float) FEMALE_WALKING_STRIDE_LENGTH_RATIO);
        }
        this.mRate = rate.intValue();
        this.mWalkingStrideLength = (stepLengthRatio.floatValue() * userHeightMM) / 10.0f;
    }

    @Override // com.microsoft.kapp.models.goal.GoalProcessor
    protected int getEditGoalTitleTextResourceId() {
        return R.string.edit_goal_dialog_fragment_steps_title;
    }

    @Override // com.microsoft.kapp.models.goal.GoalProcessor
    protected int getEditGoalEstimateTextResourceId() {
        return R.string.edit_goal_dialog_fragment_steps_estimate;
    }

    @Override // com.microsoft.kapp.models.goal.GoalProcessor
    public int calculateNumberOfSecondsRequired(int value) {
        return (int) (100.0f * (value / this.mRate));
    }

    @Override // com.microsoft.kapp.models.goal.GoalProcessor
    public double calculateDistanceRequired(int stepsValue) {
        return stepsValue * this.mWalkingStrideLength;
    }

    @Override // com.microsoft.kapp.models.goal.GoalProcessor
    public boolean isTooEasyForUser(int caloriesGoal) {
        return false;
    }
}

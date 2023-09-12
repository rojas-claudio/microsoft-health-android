package com.microsoft.kapp.models.goal;

import android.content.Context;
import com.microsoft.band.cloud.UserProfileInfo;
import com.microsoft.kapp.R;
import com.microsoft.kapp.device.CargoUserProfile;
import com.microsoft.kapp.diagnostics.Compatibility;
import com.microsoft.kapp.diagnostics.Validate;
import com.microsoft.kapp.models.Length;
import com.microsoft.kapp.models.Weight;
import com.microsoft.kapp.services.SettingsProvider;
import com.microsoft.kapp.utils.Constants;
import com.microsoft.kapp.utils.ProfileUtils;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.Years;
/* loaded from: classes.dex */
public class CaloriesGoalProcessor extends GoalProcessor {
    private static final float WEIGHT_CALORIES_BURN_RATIO = 2.8f;
    private final int mAge;
    private final float mBasalMetabolicRate;
    private final float mHeightInCentimeter;
    private final int mWeightInKilograms;

    public CaloriesGoalProcessor(Context context, SettingsProvider settingsProvider) {
        super(context, settingsProvider);
        UserProfileInfo.Gender gender;
        CargoUserProfile userProfile = getUserProfile();
        if (userProfile == null) {
            if (Compatibility.isPublicRelease()) {
                throw new IllegalStateException("Profile is missing.");
            }
            gender = Constants.DEFAULT_GENDER;
            this.mWeightInKilograms = 70;
            this.mHeightInCentimeter = 170.0f;
            this.mAge = 30;
        } else {
            gender = userProfile.getGender();
            this.mWeightInKilograms = Weight.fromGrams(userProfile.getWeightInGrams()).getKilograms();
            this.mHeightInCentimeter = Length.fromMillimeters(userProfile.getHeightInMM()).getCentimeters();
            DateTime birthdate = userProfile.getBirthdate();
            if (birthdate == null) {
                this.mAge = 0;
            } else {
                LocalDate birthDate = birthdate.toLocalDate();
                LocalDate now = new LocalDate();
                this.mAge = Years.yearsBetween(birthDate, now).getYears();
            }
        }
        this.mBasalMetabolicRate = ProfileUtils.calculateBMR(gender, this.mWeightInKilograms, this.mHeightInCentimeter, this.mAge);
    }

    @Override // com.microsoft.kapp.models.goal.GoalProcessor
    public String getEditGoalEstimateText(int value) {
        Validate.isTrue(value >= 0, "value should be greater than or equal to zero.");
        int numberOfSeconds = calculateNumberOfSecondsRequired(value);
        if (numberOfSeconds <= 0) {
            return getContext().getString(R.string.edit_goal_dialog_fragment_steps_estimate_too_easy);
        }
        return formatEditGoalEstimateText(numberOfSeconds);
    }

    @Override // com.microsoft.kapp.models.goal.GoalProcessor
    protected int getEditGoalTitleTextResourceId() {
        return R.string.edit_goal_dialog_fragment_calories_title;
    }

    @Override // com.microsoft.kapp.models.goal.GoalProcessor
    protected int getEditGoalEstimateTextResourceId() {
        return R.string.edit_goal_dialog_fragment_calories_estimate;
    }

    @Override // com.microsoft.kapp.models.goal.GoalProcessor
    public int calculateNumberOfSecondsRequired(int value) {
        float caloriesDiff = value - this.mBasalMetabolicRate;
        if (caloriesDiff < 0.0f) {
            return 0;
        }
        return (int) ((3600.0f * caloriesDiff) / (this.mWeightInKilograms * WEIGHT_CALORIES_BURN_RATIO));
    }

    @Override // com.microsoft.kapp.models.goal.GoalProcessor
    public double calculateDistanceRequired(int value) {
        float distanceMile = (calculateNumberOfSecondsRequired(value) * 2) / 3600.0f;
        return Length.fromImperial((int) (5280.0f * distanceMile), 0).getTotalDistanceInPreferredUnits(this.mSettingsProvider.isDistanceHeightMetric());
    }

    @Override // com.microsoft.kapp.models.goal.GoalProcessor
    public boolean isTooEasyForUser(int caloriesGoal) {
        return ((float) caloriesGoal) <= this.mBasalMetabolicRate;
    }
}

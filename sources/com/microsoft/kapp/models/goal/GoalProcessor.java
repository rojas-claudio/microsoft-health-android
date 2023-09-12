package com.microsoft.kapp.models.goal;

import android.content.Context;
import com.microsoft.kapp.device.CargoUserProfile;
import com.microsoft.kapp.diagnostics.Validate;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.kapp.services.SettingsProvider;
import com.microsoft.kapp.utils.Constants;
import com.microsoft.kapp.utils.Formatter;
/* loaded from: classes.dex */
public abstract class GoalProcessor {
    private static final String TAG = GoalProcessor.class.getSimpleName();
    private final Context mContext;
    private String mEditGoalEstimateFormat;
    private String mEditGoalTitleText;
    protected final SettingsProvider mSettingsProvider;
    private CargoUserProfile mUserProfile;

    public abstract double calculateDistanceRequired(int i);

    public abstract int calculateNumberOfSecondsRequired(int i);

    protected abstract int getEditGoalEstimateTextResourceId();

    protected abstract int getEditGoalTitleTextResourceId();

    public abstract boolean isTooEasyForUser(int i);

    /* JADX INFO: Access modifiers changed from: protected */
    public GoalProcessor(Context context, SettingsProvider settingsProvider) {
        Validate.notNull(context, Constants.FRE_INTENT_EXTRA_INFO);
        Validate.notNull(settingsProvider, "settingsProvider");
        this.mSettingsProvider = settingsProvider;
        this.mContext = context;
        boolean hasProfile = false;
        CargoUserProfile userProfile = settingsProvider.getUserProfile();
        if (userProfile != null) {
            hasProfile = true;
            this.mUserProfile = userProfile;
        }
        if (!hasProfile) {
            KLog.w(TAG, "The user profile does not exist.");
        }
    }

    public String getEditGoalTitleText() {
        if (this.mEditGoalTitleText == null) {
            this.mEditGoalTitleText = this.mContext.getString(getEditGoalTitleTextResourceId());
        }
        return this.mEditGoalTitleText;
    }

    public String getEditGoalEstimateText(int value) {
        Validate.isTrue(value >= 0, "value should be greater than or equal to zero.");
        int numberOfSeconds = calculateNumberOfSecondsRequired(value);
        return formatEditGoalEstimateText(numberOfSeconds);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public Context getContext() {
        return this.mContext;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public CargoUserProfile getUserProfile() {
        return this.mUserProfile;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public String formatEditGoalEstimateText(int seconds) {
        if (this.mEditGoalEstimateFormat == null) {
            this.mEditGoalEstimateFormat = this.mContext.getString(getEditGoalEstimateTextResourceId());
        }
        String duration = Formatter.formatDurationSecondsToHrMin(this.mContext, seconds);
        return String.format(this.mEditGoalEstimateFormat, duration);
    }
}

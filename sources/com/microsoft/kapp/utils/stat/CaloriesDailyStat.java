package com.microsoft.kapp.utils.stat;

import com.microsoft.krestsdk.models.UserActivity;
/* loaded from: classes.dex */
public class CaloriesDailyStat {
    public static final int DEFAULT_HIGHEST_BURNED_CALORIES = Integer.MIN_VALUE;
    public static final int DEFAULT_LOWEST_BURNED_CALORIES = Integer.MAX_VALUE;
    private UserActivity highestBurnActivity = new UserActivity();
    private UserActivity lowestBurnActivity = new UserActivity();

    public CaloriesDailyStat() {
        this.highestBurnActivity.setCaloriesBurned(Integer.MIN_VALUE);
        this.lowestBurnActivity.setCaloriesBurned(Integer.MAX_VALUE);
    }

    public UserActivity getHighestBurnActivity() {
        return this.highestBurnActivity;
    }

    public void setHighestBurnActivity(UserActivity highestBurnActivity) {
        this.highestBurnActivity = highestBurnActivity;
    }

    public UserActivity getLowestBurnActivity() {
        return this.lowestBurnActivity;
    }

    public void setLowestBurnActivity(UserActivity lowestBurnActivity) {
        this.lowestBurnActivity = lowestBurnActivity;
    }
}

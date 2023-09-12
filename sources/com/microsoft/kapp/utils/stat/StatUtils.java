package com.microsoft.kapp.utils.stat;

import com.microsoft.krestsdk.models.UserActivity;
import java.util.List;
import org.joda.time.DateTime;
import org.joda.time.Interval;
/* loaded from: classes.dex */
public class StatUtils {
    public CaloriesDailyStat getCaloriesDailyStat(List<UserActivity> activityList) {
        CaloriesDailyStat mCaloriesDailyStat = new CaloriesDailyStat();
        DateTime currentHour = DateTime.now().withMillisOfSecond(0).withSecondOfMinute(0).withMinuteOfHour(0);
        Interval currentHourPeriod = new Interval(currentHour, currentHour.plusHours(1));
        if (!isListNullOrEmpty(activityList)) {
            for (UserActivity userActivity : activityList) {
                if (userActivity != null && userActivity.getTimeOfDay() != null) {
                    int caloriesBurned = userActivity.getCaloriesBurned();
                    if (caloriesBurned > 0 && mCaloriesDailyStat.getHighestBurnActivity().getCaloriesBurned() < caloriesBurned) {
                        mCaloriesDailyStat.setHighestBurnActivity(userActivity);
                    }
                    if (!currentHourPeriod.contains(userActivity.getTimeOfDay()) && caloriesBurned > 0 && mCaloriesDailyStat.getLowestBurnActivity().getCaloriesBurned() > caloriesBurned) {
                        mCaloriesDailyStat.setLowestBurnActivity(userActivity);
                    }
                }
            }
        }
        return mCaloriesDailyStat;
    }

    private <T> boolean isListNullOrEmpty(List<T> list) {
        return list == null || list.size() == 0;
    }
}

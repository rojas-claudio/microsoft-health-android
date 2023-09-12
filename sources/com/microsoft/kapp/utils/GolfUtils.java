package com.microsoft.kapp.utils;

import android.content.Context;
import com.microsoft.kapp.R;
import com.microsoft.kapp.services.SettingsProvider;
import com.microsoft.kapp.views.BaseTrackerStat;
import com.microsoft.krestsdk.models.GolfEvent;
import java.util.ArrayList;
/* loaded from: classes.dex */
public class GolfUtils {
    public ArrayList<BaseTrackerStat> getGolfTrackerStats(Context context, SettingsProvider settingsProvider, GolfEvent event) {
        ArrayList<BaseTrackerStat> stats = new ArrayList<>();
        stats.add(TrackerStatUtils.getLongestDrive(event.getLongestDriveInCm(), settingsProvider.isDistanceHeightMetric(), context));
        stats.add(TrackerStatUtils.getNumberAtParOrBetter(event.getParOrBetterCount(), context));
        stats.add(TrackerStatUtils.getTotalDistance(event.getTotalDistanceWalkedInCm(), settingsProvider.isDistanceHeightMetric(), context));
        stats.add(TrackerStatUtils.getAverageTimePerGolfCourse(event.getPaceOfPlayInSeconds(), context));
        stats.add(TrackerStatUtils.getTotalSteps(event.getTotalStepCount(), context));
        stats.add(TrackerStatUtils.getCaloriesBurned(event.getCaloriesBurned(), context));
        stats.add(TrackerStatUtils.getAvgBpmStat(event.getAverageHeartRate(), event.getPeakHeartRate(), event.getLowestHeartRate(), context.getResources(), context));
        BaseTrackerStat stat = TrackerStatUtils.getDurationStat(event.getDuration(), event.getStartTime(), context.getResources(), context, true, R.array.MerticSmallUnitFormat);
        stats.add(stat);
        stats.add(TrackerStatUtils.getUvExposure(event, context.getResources(), context));
        return stats;
    }
}

package com.microsoft.kapp.utils;

import com.microsoft.band.device.command.BikeDisplayMetricType;
import com.microsoft.band.device.command.RunDisplayMetricType;
import com.microsoft.kapp.diagnostics.Validate;
import com.microsoft.krestsdk.models.BandVersion;
import com.microsoft.krestsdk.models.BikeEvent;
import com.microsoft.krestsdk.models.BikeEventSequence;
import com.microsoft.krestsdk.models.Location;
import com.microsoft.krestsdk.models.MeasuredEvent;
import com.microsoft.krestsdk.models.MeasuredEventMapPoint;
import com.microsoft.krestsdk.models.RunEventSequence;
import com.microsoft.krestsdk.models.UserEvent;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes.dex */
public class EventUtils {
    public static List<MeasuredEventMapPoint> getMapPointsWithLocation(MeasuredEvent event) {
        Validate.notNull(event, "event");
        MeasuredEventMapPoint[] points = event.getMapPoints();
        List<MeasuredEventMapPoint> mapPoints = new ArrayList<>();
        if (points != null) {
            for (MeasuredEventMapPoint point : points) {
                Location location = point.getLocation();
                if (location != null) {
                    mapPoints.add(point);
                }
            }
        }
        return mapPoints;
    }

    public static boolean hasGPSTurnedOn(MeasuredEvent event) {
        Validate.notNull(event, "event");
        MeasuredEventMapPoint[] points = event.getMapPoints();
        if (points != null) {
            for (MeasuredEventMapPoint point : points) {
                Location location = point.getLocation();
                if (location != null) {
                    return true;
                }
            }
        }
        return false;
    }

    public static RunDisplayMetricType[] getDefaultRunMetricsOrder(BandVersion version) {
        return version == BandVersion.CARGO ? new RunDisplayMetricType[]{RunDisplayMetricType.DURATION, RunDisplayMetricType.HEART_RATE, RunDisplayMetricType.CALORIES, RunDisplayMetricType.DISTANCE, RunDisplayMetricType.PACE} : new RunDisplayMetricType[]{RunDisplayMetricType.DURATION, RunDisplayMetricType.HEART_RATE, RunDisplayMetricType.CALORIES, RunDisplayMetricType.DISTANCE, RunDisplayMetricType.PACE, RunDisplayMetricType.ELEVATION_GAIN, RunDisplayMetricType.AVERAGE_PACE, RunDisplayMetricType.TIME};
    }

    public static BikeDisplayMetricType[] getDefaultBikingMetricsOrder(BandVersion version) {
        return version == BandVersion.CARGO ? new BikeDisplayMetricType[]{BikeDisplayMetricType.DURATION, BikeDisplayMetricType.HEART_RATE, BikeDisplayMetricType.CALORIES, BikeDisplayMetricType.DISTANCE, BikeDisplayMetricType.SPEED} : new BikeDisplayMetricType[]{BikeDisplayMetricType.DURATION, BikeDisplayMetricType.HEART_RATE, BikeDisplayMetricType.CALORIES, BikeDisplayMetricType.DISTANCE, BikeDisplayMetricType.SPEED, BikeDisplayMetricType.ELEVATION_GAIN, BikeDisplayMetricType.AVERAGE_SPEED, BikeDisplayMetricType.TIME};
    }

    public static int calculateBestSplit(RunEventSequence[] sequences, boolean isMetric) {
        double completeSplitDistance = isMetric ? 100000.0d : 160934.0d;
        int bestTime = Integer.MAX_VALUE;
        if (sequences != null) {
            for (RunEventSequence event : sequences) {
                int duration = event.getDuration();
                if (duration < bestTime && isCompleteSplit(event.getSplitDistance(), completeSplitDistance)) {
                    event.getSplitDistance();
                    bestTime = duration;
                }
            }
        }
        if (bestTime == Integer.MAX_VALUE) {
            return 0;
        }
        return bestTime;
    }

    private static boolean isCompleteSplit(double splitDistance, double completeSplitDistance) {
        double diff = Math.abs(completeSplitDistance - splitDistance);
        double epsilon = completeSplitDistance * 0.1d;
        return diff <= epsilon && diff >= (-epsilon);
    }

    public static double calculateBestSplitBike(BikeEvent event, boolean isMetric) {
        double completeSplitDistance = isMetric ? 100000.0d : 160934.0d;
        int bestSplit = Integer.MAX_VALUE;
        int lastIndex = event.getSequences().length - 1;
        double distanceCM = Constants.SPLITS_ACCURACY;
        double durationSec = Constants.SPLITS_ACCURACY;
        int i = 0;
        int groupIndex = 0;
        while (i < lastIndex) {
            BikeEventSequence sequence = event.getSequences()[i];
            distanceCM += sequence.getSplitDistance();
            durationSec += sequence.getDuration();
            int groupIndex2 = groupIndex + 1;
            if (groupIndex == event.getSplitGroupSize() - 1) {
                if (distanceCM > event.getSplitGroupSize() * completeSplitDistance * 0.99d && durationSec > Constants.SPLITS_ACCURACY) {
                    int pace = (int) ((1000.0d * durationSec) / (distanceCM / 100000.0d));
                    bestSplit = Math.min(pace, bestSplit);
                }
                groupIndex2 = 0;
                distanceCM = Constants.SPLITS_ACCURACY;
                durationSec = Constants.SPLITS_ACCURACY;
            }
            i++;
            groupIndex = groupIndex2;
        }
        if (bestSplit == Integer.MAX_VALUE) {
            bestSplit = 0;
        }
        int bestSplit2 = bestSplit * event.getSplitGroupSize();
        return isMetric ? bestSplit2 : ConversionUtils.MilesToKilometers(bestSplit2);
    }

    public static int getUVDisclaimerViewVisibility(UserEvent event) {
        if (event.getUvExposure() > 0) {
        }
        return 0;
    }
}

package com.shinobicontrols.kcompanionapp.properties;

import android.content.Context;
import android.graphics.PointF;
import com.microsoft.kapp.R;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.kapp.models.Length;
import com.microsoft.kapp.models.TimeSpan;
import com.microsoft.kapp.models.Velocity;
import com.microsoft.kapp.utils.Constants;
import com.microsoft.kapp.utils.ConversionUtils;
import com.microsoft.krestsdk.models.BikeEvent;
import com.microsoft.krestsdk.models.ExerciseEventBase;
import com.microsoft.krestsdk.models.GolfEvent;
import com.microsoft.krestsdk.models.GolfEventHoleSequence;
import com.microsoft.krestsdk.models.MeasuredEvent;
import com.microsoft.krestsdk.models.MeasuredEventMapPoint;
import com.microsoft.krestsdk.models.RunEvent;
import com.microsoft.krestsdk.models.UserActivity;
import com.shinobicontrols.charts.DataAdapter;
import com.shinobicontrols.charts.DataPoint;
import com.shinobicontrols.charts.SimpleDataAdapter;
import com.shinobicontrols.kcompanionapp.charts.internal.TypedValueGetter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.joda.time.DateTime;
/* loaded from: classes.dex */
public class ChartDataPropertiesUtils {
    public static ChartDataProperties getParChartDataProperties(GolfEvent event, TypedValueGetter valueGetter) {
        int strokeMax = (int) valueGetter.getFloat(R.id.shinobicharts_golf_strokes_max);
        int strokeMin = (int) valueGetter.getFloat(R.id.shinobicharts_golf_strokes_min);
        ParChartSeriesList seriesList = new ParChartSeriesList(valueGetter);
        GolfEventHoleSequence[] sortedHoles = event.getSortedSequences();
        if (sortedHoles != null) {
            int lastHoleNumber = -1;
            boolean isGolfEventValid = true;
            for (GolfEventHoleSequence hole : sortedHoles) {
                if (hole != null) {
                    if (lastHoleNumber == hole.getHoleNumber()) {
                        isGolfEventValid = false;
                    } else {
                        if (hole.getUserScore() == 0) {
                            seriesList.addSkippedHole(hole.getHoleNumber());
                        } else {
                            double parDelta = hole.getUserScore() - hole.getHolePar();
                            seriesList.addHoleData(hole.getHoleNumber(), (int) Math.max(Math.min(parDelta, strokeMax), strokeMin));
                        }
                        lastHoleNumber = hole.getHoleNumber();
                    }
                }
            }
            if (!isGolfEventValid) {
                KLog.e("GolfEvent", "invalid golf event - duplicate holes");
            }
        }
        return new ChartDataProperties(strokeMin, strokeMax, Constants.SPLITS_ACCURACY, Constants.SPLITS_ACCURACY, null, seriesList);
    }

    public static ChartDataProperties getElevationDataProperties(MeasuredEvent event, boolean isMetric) {
        double maxElevation = Double.MIN_VALUE;
        double distanceAtMaxElevation = Constants.SPLITS_ACCURACY;
        double minElevation = Double.MAX_VALUE;
        double elevationValue = Constants.SPLITS_ACCURACY;
        ArrayList<PointF> elevationPausePoints = new ArrayList<>();
        DataAdapter<?, ?> simpleDataAdapter = new SimpleDataAdapter<>();
        if (event.getMapPoints() != null) {
            boolean pauseStarted = false;
            PointF elevationPausePoint = null;
            MeasuredEventMapPoint[] arr$ = event.getMapPoints();
            for (MeasuredEventMapPoint mapPoint : arr$) {
                Length distance = Length.fromCentimeters(mapPoint.getTotalDistance());
                if (mapPoint.getLocation() != null) {
                    Length elevation = Length.fromCentimeters(mapPoint.getLocation().getAltitudeFromMSL());
                    if (elevation != null) {
                        if (isMetric) {
                            elevationValue = elevation.getTotalMeters();
                        } else {
                            elevationValue = elevation.getTotalFeet();
                        }
                        simpleDataAdapter.add(new DataPoint<>(Double.valueOf(distance.getTotalDistanceInPreferredUnits(isMetric)), Double.valueOf(elevationValue)));
                    }
                    if (mapPoint.getIsPause()) {
                        if (!pauseStarted) {
                            if (elevation != null) {
                                if (isMetric) {
                                    elevationValue = elevation.getTotalMeters();
                                } else {
                                    elevationValue = elevation.getTotalFeet();
                                }
                                elevationPausePoint = new PointF((float) distance.getTotalDistanceInPreferredUnits(isMetric), (float) elevationValue);
                            }
                            pauseStarted = true;
                        }
                    } else if (pauseStarted) {
                        if (elevationPausePoint != null) {
                            elevationPausePoints.add(elevationPausePoint);
                        }
                        pauseStarted = false;
                    }
                    if (maxElevation < elevationValue) {
                        maxElevation = elevationValue;
                        distanceAtMaxElevation = distance.getTotalDistanceInPreferredUnits(isMetric);
                    }
                    if (minElevation > elevationValue) {
                        minElevation = elevationValue;
                    }
                }
            }
        }
        List<DataAdapter<?, ?>> seriesList = new ArrayList<>();
        seriesList.add(simpleDataAdapter);
        return new ChartDataProperties(minElevation, maxElevation, distanceAtMaxElevation, Constants.SPLITS_ACCURACY, elevationPausePoints, seriesList);
    }

    public static ChartDataProperties getHeartRateDataProperties(ExerciseEventBase exerciseEvent, TypedValueGetter valueGetter, int age, int hrMax) {
        int peakHeartRate;
        int peakHeartRate2 = Integer.MIN_VALUE;
        double timeAtpeakHeartRate = Constants.SPLITS_ACCURACY;
        ArrayList<PointF> heartRatePausePoints = new ArrayList<>();
        long totalPauseDuration = 0;
        DataAdapter<?, ?> simpleDataAdapter = new SimpleDataAdapter<>();
        boolean isPauseStarted = false;
        if (exerciseEvent.getInfo() != null && exerciseEvent.getInfo().length > 0) {
            long start = exerciseEvent.getInfo()[0].getTimeOfDay().getMillis();
            int i = 0;
            while (i < exerciseEvent.getInfo().length) {
                UserActivity userActivity = exerciseEvent.getInfo()[i];
                UserActivity nextActivity = i < exerciseEvent.getInfo().length + (-1) ? exerciseEvent.getInfo()[i + 1] : null;
                DateTime timeOfDay = userActivity.getTimeOfDay();
                TimeSpan duration = TimeSpan.fromMilliseconds((timeOfDay.getMillis() - start) - totalPauseDuration);
                int hrValue = userActivity.getAverageHeartRate();
                if (userActivity.isPaused()) {
                    if (!isPauseStarted) {
                        isPauseStarted = true;
                        int heartRateAtPausePoint = i > 0 ? exerciseEvent.getInfo()[i - 1].getAverageHeartRate() : exerciseEvent.getInfo()[i].getAverageHeartRate();
                        PointF heartRatePausePoint = new PointF(((float) duration.getTotalMinutes()) - 1.0f, heartRateAtPausePoint);
                        heartRatePausePoints.add(heartRatePausePoint);
                    }
                    if (nextActivity != null) {
                        totalPauseDuration = (nextActivity.getTimeOfDay().getMillis() + totalPauseDuration) - timeOfDay.getMillis();
                    }
                } else if (isPauseStarted) {
                    isPauseStarted = false;
                }
                if (!userActivity.isPaused()) {
                    simpleDataAdapter.add(new DataPoint<>(Double.valueOf(duration.getTotalMinutes()), Integer.valueOf(hrValue)));
                }
                if (peakHeartRate2 < userActivity.getAverageHeartRate()) {
                    peakHeartRate2 = userActivity.getAverageHeartRate();
                    timeAtpeakHeartRate = duration.getTotalMinutes();
                }
                i++;
            }
        }
        float f = valueGetter.getFloat(R.id.shinobicharts_heart_rate_min_proportion);
        if (hrMax == 0) {
            hrMax = ((int) valueGetter.getFloat(R.id.shinobicharts_run_heart_rate_max)) - age;
        }
        double minValue = f * hrMax;
        if (peakHeartRate2 > minValue) {
            peakHeartRate = exerciseEvent.getPeakHeartRate();
        } else {
            peakHeartRate = 0;
        }
        List<DataAdapter<?, ?>> seriesList = new ArrayList<>();
        seriesList.add(simpleDataAdapter);
        return new ChartDataProperties(Constants.SPLITS_ACCURACY, peakHeartRate, timeAtpeakHeartRate, Constants.SPLITS_ACCURACY, heartRatePausePoints, seriesList);
    }

    public static ChartDataProperties getHeartRateDataPropertiesForDistance(MeasuredEvent measuredEvent, TypedValueGetter valueGetter, boolean isMetric) {
        double minHeartRateValue = valueGetter.getFloat(R.id.shinobicharts_run_heart_rate_min);
        int peakHeartRate = 0;
        double distanceAtMaxAverageHeartRate = Constants.SPLITS_ACCURACY;
        ArrayList<PointF> heartRatePausePoints = new ArrayList<>();
        DataAdapter<?, ?> simpleDataAdapter = new SimpleDataAdapter<>();
        PointF heartRatePausePoint = null;
        boolean pauseStarted = false;
        if (measuredEvent.getMapPoints() != null) {
            MeasuredEventMapPoint[] arr$ = measuredEvent.getMapPoints();
            for (MeasuredEventMapPoint mapPoint : arr$) {
                if (mapPoint != null) {
                    Length distance = Length.fromCentimeters(mapPoint.getTotalDistance());
                    int hrValue = mapPoint.getheartRate();
                    simpleDataAdapter.add(new DataPoint<>(Double.valueOf(distance.getTotalDistanceInPreferredUnits(isMetric)), Integer.valueOf(hrValue)));
                    if (mapPoint.getIsPause()) {
                        if (!pauseStarted) {
                            heartRatePausePoint = new PointF((float) distance.getTotalDistanceInPreferredUnits(isMetric), hrValue);
                            pauseStarted = true;
                        }
                    } else if (pauseStarted) {
                        if (heartRatePausePoint != null) {
                            heartRatePausePoints.add(heartRatePausePoint);
                        }
                        pauseStarted = false;
                    }
                    if (peakHeartRate < mapPoint.getheartRate()) {
                        peakHeartRate = mapPoint.getheartRate();
                        distanceAtMaxAverageHeartRate = distance.getTotalDistanceInPreferredUnits(isMetric);
                    }
                }
            }
        }
        List<DataAdapter<?, ?>> seriesList = new ArrayList<>();
        seriesList.add(simpleDataAdapter);
        return new ChartDataProperties(minHeartRateValue, peakHeartRate, distanceAtMaxAverageHeartRate, Constants.SPLITS_ACCURACY, heartRatePausePoints, seriesList);
    }

    public static ChartDataProperties getSpeedDataProperties(BikeEvent event, boolean isMetric) {
        double maxSpeed = Double.MIN_VALUE;
        double distanceAtMaxSpeed = Constants.SPLITS_ACCURACY;
        ArrayList<PointF> speedPausePoints = new ArrayList<>();
        DataAdapter<?, ?> simpleDataAdapter = new SimpleDataAdapter<>();
        boolean pauseStarted = false;
        PointF pausePoint = null;
        if (event.getMapPoints() != null) {
            MeasuredEventMapPoint[] arr$ = event.getMapPoints();
            for (MeasuredEventMapPoint mapPoint : arr$) {
                Length distance = Length.fromCentimeters(mapPoint.getTotalDistance());
                double speed = isMetric ? mapPoint.getSpeed() * 0.036d : mapPoint.getSpeed() * 0.022369362906d;
                DataPoint<Double, Double> dp = new DataPoint<>(Double.valueOf(distance.getTotalDistanceInPreferredUnits(isMetric)), Double.valueOf(speed));
                simpleDataAdapter.add(dp);
                if (mapPoint.getIsPause()) {
                    if (!pauseStarted) {
                        pausePoint = new PointF((float) distance.getTotalDistanceInPreferredUnits(isMetric), (float) speed);
                        pauseStarted = true;
                    }
                } else if (pauseStarted) {
                    if (pausePoint != null) {
                        speedPausePoints.add(pausePoint);
                    }
                    pauseStarted = false;
                }
                if (maxSpeed < speed) {
                    maxSpeed = speed;
                    distanceAtMaxSpeed = distance.getTotalDistanceInPreferredUnits(isMetric);
                }
            }
        }
        double averageSpeed = isMetric ? ConversionUtils.CentimeterPerSecondToKilometersPerHour(event.getAverageSpeed()) : ConversionUtils.CentimeterPerSecondToMilesPerHour(event.getAverageSpeed());
        List<DataAdapter<?, ?>> seriesList = new ArrayList<>();
        seriesList.add(simpleDataAdapter);
        return new ChartDataProperties(Constants.SPLITS_ACCURACY, maxSpeed, distanceAtMaxSpeed, averageSpeed, speedPausePoints, seriesList);
    }

    public static ChartDataProperties getPaceDataProperties(RunEvent event, boolean isMetric, Context context) {
        ArrayList<PointF> pacePausePoints = new ArrayList<>();
        DataAdapter<?, ?> simpleDataAdapter = new SimpleDataAdapter<>();
        double maxPace = -2.147483648E9d;
        double minPace = 2.147483647E9d;
        int scaledPaceFilter = context.getResources().getInteger(R.integer.shinobicharts_run_pace_scaled_pace_filter);
        boolean pauseStarted = false;
        PointF pacePausePoint = null;
        LinkedList<Double> pacePointsForAvgCalculation = new LinkedList<>();
        int movingAvgFactor = context.getResources().getInteger(R.integer.shinobicharts_moving_average_factor);
        double movingAveragePaceValue = Constants.SPLITS_ACCURACY;
        if (event.getMapPoints() != null) {
            MeasuredEventMapPoint[] arr$ = event.getMapPoints();
            int len$ = arr$.length;
            int i$ = 0;
            while (true) {
                int i$2 = i$;
                if (i$2 >= len$) {
                    break;
                }
                MeasuredEventMapPoint mapPoint = arr$[i$2];
                if (mapPoint != null) {
                    Length distance = Length.fromCentimeters(mapPoint.getTotalDistance());
                    Velocity pace = new Velocity(TimeSpan.fromMilliseconds(mapPoint.getPace()), Length.fromMeters(1000));
                    double negatedPaceValue = -pace.getTimePerRegionalUnit(isMetric).getTotalMinutes();
                    if (negatedPaceValue != Constants.SPLITS_ACCURACY) {
                        maxPace = Math.max(maxPace, negatedPaceValue);
                        if (minPace > negatedPaceValue && mapPoint.getScaledPace() >= scaledPaceFilter) {
                            minPace = negatedPaceValue;
                        }
                        if (pacePointsForAvgCalculation.size() < movingAvgFactor) {
                            pacePointsForAvgCalculation.addFirst(Double.valueOf(negatedPaceValue));
                        } else {
                            pacePointsForAvgCalculation.removeLast();
                            pacePointsForAvgCalculation.addFirst(Double.valueOf(negatedPaceValue));
                        }
                        double movingAvgSumForPace = Constants.SPLITS_ACCURACY;
                        Iterator i$3 = pacePointsForAvgCalculation.iterator();
                        while (i$3.hasNext()) {
                            Double paceValue = i$3.next();
                            movingAvgSumForPace += paceValue.doubleValue();
                        }
                        movingAveragePaceValue = movingAvgSumForPace / pacePointsForAvgCalculation.size();
                        simpleDataAdapter.add(new DataPoint<>(Double.valueOf(distance.getTotalDistanceInPreferredUnits(isMetric)), Double.valueOf(movingAveragePaceValue)));
                    }
                    if (mapPoint.getIsPause()) {
                        if (!pauseStarted) {
                            pacePausePoint = new PointF((float) distance.getTotalDistanceInPreferredUnits(isMetric), (float) movingAveragePaceValue);
                            pauseStarted = true;
                        }
                    } else if (pauseStarted) {
                        if (pacePausePoint != null) {
                            pacePausePoints.add(pacePausePoint);
                        }
                        pauseStarted = false;
                    }
                }
                i$ = i$2 + 1;
            }
        }
        Velocity averagePace = new Velocity(TimeSpan.fromMilliseconds(event.getPace()), Length.fromMeters(1000));
        double averagePaceInMinutesAndNegated = -averagePace.getTimePerRegionalUnit(isMetric).getTotalMinutes();
        List<DataAdapter<?, ?>> seriesList = new ArrayList<>();
        seriesList.add(simpleDataAdapter);
        return new ChartDataProperties(minPace, maxPace, Constants.SPLITS_ACCURACY, averagePaceInMinutesAndNegated, pacePausePoints, seriesList);
    }

    public static ChartDataProperties getBikeEventDataProperties(BikeEvent event, boolean isMetric) {
        double totalDistance = Length.fromCentimeters(event.getTotalDistance()).getTotalDistanceInPreferredUnits(isMetric);
        return new ChartDataProperties(Constants.SPLITS_ACCURACY, totalDistance, event.getSplitGroupSize(), null);
    }

    public static ChartDataProperties getRunEventDataProperties(RunEvent event, boolean isMetric) {
        double totalDistance = Length.fromCentimeters(event.getTotalDistance()).getTotalDistanceInPreferredUnits(isMetric);
        return new ChartDataProperties(Constants.SPLITS_ACCURACY, totalDistance, 1, null);
    }

    public static ChartDataProperties getExerciseEventDataProperties(ExerciseEventBase event) {
        double maxValue = Constants.SPLITS_ACCURACY;
        if (event != null && event.getInfo() != null && event.getInfo().length > 0) {
            TimeSpan duration = TimeSpan.fromSeconds(event.getDuration());
            maxValue = duration.getTotalMinutes();
        }
        return new ChartDataProperties(Constants.SPLITS_ACCURACY, maxValue, 1, null);
    }
}

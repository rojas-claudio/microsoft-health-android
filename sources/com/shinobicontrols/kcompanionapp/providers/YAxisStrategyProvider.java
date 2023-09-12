package com.shinobicontrols.kcompanionapp.providers;

import android.content.Context;
import com.microsoft.kapp.R;
import com.microsoft.kapp.utils.Constants;
import com.shinobicontrols.charts.Axis;
import com.shinobicontrols.kcompanionapp.charts.internal.RangeAndFrequencySettingStrategy;
import com.shinobicontrols.kcompanionapp.charts.internal.Rounder;
import com.shinobicontrols.kcompanionapp.charts.internal.TypedValueGetter;
import com.shinobicontrols.kcompanionapp.properties.ChartDataProperties;
/* loaded from: classes.dex */
public enum YAxisStrategyProvider implements IStrategyProvider<ChartDataProperties> {
    ELEVATION { // from class: com.shinobicontrols.kcompanionapp.providers.YAxisStrategyProvider.1
        @Override // com.shinobicontrols.kcompanionapp.providers.IStrategyProvider
        public RangeAndFrequencySettingStrategy getRangeAndFrequencyStrategy(Context context, Axis axis, double maxValueHint, double minValueHint, ChartDataProperties dataProperties) {
            TypedValueGetter valueGetter = new TypedValueGetter(context.getResources());
            double minValue = dataProperties.getMinValue();
            double maxValue = dataProperties.getMaxValue();
            double padding = valueGetter.getFloat(R.id.shinobicharts_run_elevation_padding);
            double defaultMax = valueGetter.getFloat(R.id.shinobicharts_run_elevation_default_max);
            int toTheNearest = context.getResources().getInteger(R.integer.shinobicharts_run_elevation_round_to_nearest_value);
            double minFrequancy = context.getResources().getInteger(R.integer.shinobicharts_run_elevation_min_frequency);
            int maxNumberOfTicks = context.getResources().getInteger(R.integer.shinobicharts_run_elevation_max_number_of_ticks);
            int minNumberOfTicks = context.getResources().getInteger(R.integer.shinobicharts_run_elevation_min_number_of_ticks);
            double minValue2 = Math.max((double) Constants.SPLITS_ACCURACY, Rounder.roundDown(minValue - padding, toTheNearest));
            double maxValue2 = Math.max(defaultMax, Rounder.roundUp(maxValue + padding, toTheNearest));
            int numTicks = maxNumberOfTicks;
            while (numTicks >= minNumberOfTicks && (maxValue2 - minValue2) % (numTicks - 1) != Constants.SPLITS_ACCURACY) {
                numTicks--;
            }
            int numberOfIntervals = numTicks - 1;
            return new RangeAndFrequencySettingStrategy.FixedNumberOfIntervalsRangeAndFrequencySettingStrategy(axis, minValue2, maxValue2, numberOfIntervals, toTheNearest, Double.valueOf(minFrequancy));
        }
    },
    HEART_RATE { // from class: com.shinobicontrols.kcompanionapp.providers.YAxisStrategyProvider.2
        @Override // com.shinobicontrols.kcompanionapp.providers.IStrategyProvider
        public RangeAndFrequencySettingStrategy getRangeAndFrequencyStrategy(Context context, Axis axis, double maxValueHint, double minValueHint, ChartDataProperties dataProperties) {
            double maxAverageHeartRate = dataProperties.getMaxValue();
            double maxValue = Math.max(maxAverageHeartRate, maxValueHint);
            int numberOfIntervals = context.getResources().getInteger(R.integer.shinobicharts_workout_heart_rate_number_of_intervals);
            int toTheNearest = context.getResources().getInteger(R.integer.shinobicharts_workout_heart_rate_round_to_nearest_value);
            return new RangeAndFrequencySettingStrategy.FixedNumberOfIntervalsRangeAndFrequencySettingStrategy(axis, minValueHint, maxValue, numberOfIntervals, toTheNearest, null);
        }
    },
    SPEED { // from class: com.shinobicontrols.kcompanionapp.providers.YAxisStrategyProvider.3
        @Override // com.shinobicontrols.kcompanionapp.providers.IStrategyProvider
        public RangeAndFrequencySettingStrategy getRangeAndFrequencyStrategy(Context context, Axis axis, double maxValueHint, double minValueHint, ChartDataProperties dataProperties) {
            int numberOfIntervals = context.getResources().getInteger(R.integer.shinobicharts_run_pace_number_of_intervals);
            int toTheNearest = context.getResources().getInteger(R.integer.shinobicharts_run_pace_round_to_nearest_value);
            double minValue = dataProperties.getMinValue();
            double maxValue = dataProperties.getMaxValue();
            double avg = dataProperties.getAverageValue();
            double variance = Math.max(Math.abs(minValue - avg), Math.abs(maxValue - avg));
            double minValue2 = Math.floor((avg - variance) / 2.0d) * 2.0d;
            double maxValue2 = Math.ceil((avg + variance) / 2.0d) * 2.0d;
            if (maxValue2 > Constants.SPLITS_ACCURACY) {
                maxValue2 = Constants.SPLITS_ACCURACY;
                minValue2 = Math.ceil(avg) * 2.0d;
            }
            return new RangeAndFrequencySettingStrategy.FixedNumberOfIntervalsRangeAndRoundedFrequencySettingStrategy(axis, minValue2, maxValue2, numberOfIntervals, toTheNearest, null, true);
        }
    },
    PACE { // from class: com.shinobicontrols.kcompanionapp.providers.YAxisStrategyProvider.4
        @Override // com.shinobicontrols.kcompanionapp.providers.IStrategyProvider
        public RangeAndFrequencySettingStrategy getRangeAndFrequencyStrategy(Context context, Axis axis, double maxValueHint, double minValueHint, ChartDataProperties dataProperties) {
            int numberOfIntervals = context.getResources().getInteger(R.integer.shinobicharts_run_pace_number_of_intervals);
            int toTheNearest = context.getResources().getInteger(R.integer.shinobicharts_run_pace_round_to_nearest_value);
            double avg = dataProperties.getAverageValue();
            double variance = Math.max(Math.abs(minValueHint - avg), Math.abs(maxValueHint - avg));
            double minValue = Math.floor((avg - variance) / 2.0d) * 2.0d;
            double maxValue = Math.ceil((avg + variance) / 2.0d) * 2.0d;
            if (maxValue > Constants.SPLITS_ACCURACY) {
                maxValue = Constants.SPLITS_ACCURACY;
                minValue = Math.ceil(avg) * 2.0d;
            }
            return new RangeAndFrequencySettingStrategy.FixedNumberOfIntervalsRangeAndRoundedFrequencySettingStrategy(axis, minValue, maxValue, numberOfIntervals, toTheNearest, null, true);
        }
    },
    GOLF { // from class: com.shinobicontrols.kcompanionapp.providers.YAxisStrategyProvider.5
        @Override // com.shinobicontrols.kcompanionapp.providers.IStrategyProvider
        public RangeAndFrequencySettingStrategy getRangeAndFrequencyStrategy(Context context, Axis axis, double maxValueHint, double minValueHint, ChartDataProperties dataProperties) {
            return new RangeAndFrequencySettingStrategy.GolfHeartRateIntervalsRangeAndRoundedFrequencySettingStrategy(axis, Double.valueOf(dataProperties.getMinValue()), Double.valueOf(dataProperties.getMaxValue()));
        }
    }
}

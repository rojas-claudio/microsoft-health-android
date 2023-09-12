package com.shinobicontrols.kcompanionapp.providers;

import android.content.Context;
import com.microsoft.kapp.R;
import com.shinobicontrols.charts.Axis;
import com.shinobicontrols.kcompanionapp.charts.internal.RangeAndFrequencySettingStrategy;
import com.shinobicontrols.kcompanionapp.charts.internal.TypedValueGetter;
import com.shinobicontrols.kcompanionapp.properties.ChartDataProperties;
/* loaded from: classes.dex */
public enum XAxisStrategyProvider implements IStrategyProvider<ChartDataProperties> {
    GOLF { // from class: com.shinobicontrols.kcompanionapp.providers.XAxisStrategyProvider.1
        private static final double CHART_OFFSET = 0.5d;

        @Override // com.shinobicontrols.kcompanionapp.providers.IStrategyProvider
        public RangeAndFrequencySettingStrategy getRangeAndFrequencyStrategy(Context context, Axis axis, double maxValueHint, double minValueHint, ChartDataProperties dataProperties) {
            TypedValueGetter valueGetter = new TypedValueGetter(context.getResources());
            double maxValue = dataProperties.getMaxValue() > 9.0d ? 18.0d : 9.0d;
            return new RangeAndFrequencySettingStrategy.DefaultRangeAndFrequencySettingStrategy(axis, Double.valueOf((double) CHART_OFFSET), Double.valueOf(CHART_OFFSET + maxValue), Double.valueOf(valueGetter.getFloat(R.id.shinobicharts_golf_tick_frequency)));
        }
    },
    BIKE { // from class: com.shinobicontrols.kcompanionapp.providers.XAxisStrategyProvider.2
        public static final int MAX_X_AXIS_INTERVALS = 5;

        @Override // com.shinobicontrols.kcompanionapp.providers.IStrategyProvider
        public RangeAndFrequencySettingStrategy getRangeAndFrequencyStrategy(Context context, Axis axis, double maxValueHint, double minValueHint, ChartDataProperties dataProperties) {
            TypedValueGetter valueGetter = new TypedValueGetter(context.getResources());
            double minValue = valueGetter.getFloat(R.integer.shinobicharts_run_distance_min_value);
            double maxValue = dataProperties.getMaxValue();
            double tickFrequency = dataProperties.getSegmentCount();
            double xAxisRange = maxValue - minValue;
            double intervals = xAxisRange / tickFrequency;
            if (Math.floor(intervals) > 5.0d) {
                tickFrequency = (int) Math.max(Math.floor(xAxisRange / 5.0d), 1.0d);
            }
            return new RangeAndFrequencySettingStrategy.DefaultRangeAndFrequencySettingStrategy(axis, Double.valueOf(minValue), Double.valueOf(maxValue), Double.valueOf(tickFrequency));
        }
    },
    RUN { // from class: com.shinobicontrols.kcompanionapp.providers.XAxisStrategyProvider.3
        public static final int MAX_X_AXIS_INTERVALS = 5;

        @Override // com.shinobicontrols.kcompanionapp.providers.IStrategyProvider
        public RangeAndFrequencySettingStrategy getRangeAndFrequencyStrategy(Context context, Axis axis, double maxValueHint, double minValueHint, ChartDataProperties dataProperties) {
            TypedValueGetter valueGetter = new TypedValueGetter(context.getResources());
            double minValue = valueGetter.getFloat(R.integer.shinobicharts_run_distance_min_value);
            double maxValue = dataProperties.getMaxValue();
            double tickFrequency = valueGetter.getFloat(R.id.shinobicharts_run_splits_distance_frequency);
            double xAxisRange = maxValue - minValue;
            double intervals = xAxisRange / tickFrequency;
            if (Math.floor(intervals) > 5.0d) {
                tickFrequency = (int) Math.max(Math.floor(xAxisRange / 5.0d), 1.0d);
            }
            return new RangeAndFrequencySettingStrategy.DefaultRangeAndFrequencySettingStrategy(axis, Double.valueOf(minValue), Double.valueOf(maxValue), Double.valueOf(tickFrequency));
        }
    },
    EXERCISE { // from class: com.shinobicontrols.kcompanionapp.providers.XAxisStrategyProvider.4
        @Override // com.shinobicontrols.kcompanionapp.providers.IStrategyProvider
        public RangeAndFrequencySettingStrategy getRangeAndFrequencyStrategy(Context context, Axis axis, double maxValueHint, double minValueHint, ChartDataProperties dataProperties) {
            double minFrequency;
            TypedValueGetter valueGetter = new TypedValueGetter(context.getResources());
            double minValue = valueGetter.getFloat(R.id.shinobicharts_workout_time_min);
            double maxValue = dataProperties.getMaxValue();
            double duration = maxValue - minValue;
            valueGetter.getFloat(R.id.shinobicharts_workout_time_default_frequency);
            if (duration <= 4.0d) {
                minFrequency = 0.5d;
            } else if (duration > 4.0d && duration <= 10.0d) {
                minFrequency = 1.0d;
            } else if (duration > 10.0d && duration <= 45.0d) {
                minFrequency = 5.0d;
            } else if (duration > 45.0d && duration <= 100.0d) {
                minFrequency = 10.0d;
            } else if (duration > 100.0d && duration <= 600.0d) {
                minFrequency = 60.0d;
            } else {
                minFrequency = 120.0d;
            }
            return new RangeAndFrequencySettingStrategy.DefaultRangeAndFrequencySettingStrategy(axis, Double.valueOf(minValue), Double.valueOf(maxValue), Double.valueOf(minFrequency));
        }
    }
}

package com.shinobicontrols.kcompanionapp.charts.internal;

import android.content.Context;
import android.content.res.Resources;
import android.text.format.DateFormat;
import android.util.SparseArray;
import com.microsoft.kapp.R;
import com.shinobicontrols.charts.Axis;
import com.shinobicontrols.charts.TickMark;
import java.util.ArrayList;
import java.util.List;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
/* loaded from: classes.dex */
public interface TickLabelUpdateStrategy {
    void onUpdateTickMark(Resources resources, TickMark tickMark, Axis<Double, Double> axis);

    /* loaded from: classes.dex */
    public static class NullTickLabelUpdateStrategy implements TickLabelUpdateStrategy {
        @Override // com.shinobicontrols.kcompanionapp.charts.internal.TickLabelUpdateStrategy
        public void onUpdateTickMark(Resources resources, TickMark tickMark, Axis<Double, Double> axis) {
        }
    }

    /* loaded from: classes.dex */
    public static class MapAnchoredTickValueToDefinedStringTickLabelUpdateStrategy implements TickLabelUpdateStrategy {
        private final double anchor;
        private final SparseArray<String> tickMarkLabelMap;

        public MapAnchoredTickValueToDefinedStringTickLabelUpdateStrategy(SparseArray<String> tickMarkLabelMap, double anchor) {
            if (tickMarkLabelMap == null) {
                throw new IllegalArgumentException("Cannot specify null map.");
            }
            this.tickMarkLabelMap = tickMarkLabelMap;
            this.anchor = anchor;
        }

        @Override // com.shinobicontrols.kcompanionapp.charts.internal.TickLabelUpdateStrategy
        public void onUpdateTickMark(Resources resources, TickMark tickMark, Axis<Double, Double> axis) {
            double value = ((Double) tickMark.getValue()).doubleValue();
            int index = (int) (value - this.anchor);
            String mappedString = this.tickMarkLabelMap.get(index);
            if (mappedString != null) {
                tickMark.setLabelText(mappedString);
            }
        }
    }

    /* loaded from: classes.dex */
    public static class MapTickValueToDefinedStringHidingNonMappedTickLabelUpdateStrategy implements TickLabelUpdateStrategy {
        private final SparseArray<String> tickMarkLabelMap;

        public MapTickValueToDefinedStringHidingNonMappedTickLabelUpdateStrategy(SparseArray<String> tickMarkLabelMap) {
            if (tickMarkLabelMap == null) {
                throw new IllegalArgumentException("Cannot specify null map.");
            }
            this.tickMarkLabelMap = tickMarkLabelMap;
        }

        @Override // com.shinobicontrols.kcompanionapp.charts.internal.TickLabelUpdateStrategy
        public void onUpdateTickMark(Resources resources, TickMark tickMark, Axis<Double, Double> axis) {
            int value = ((Double) tickMark.getValue()).intValue();
            String mappedString = this.tickMarkLabelMap.get(value);
            if (mappedString != null) {
                tickMark.setLabelText(mappedString);
                return;
            }
            tickMark.setLabelShown(false);
            tickMark.setLineShown(false);
        }
    }

    /* loaded from: classes.dex */
    public static class PaceTickLabelUpdateStrategy implements TickLabelUpdateStrategy {
        @Override // com.shinobicontrols.kcompanionapp.charts.internal.TickLabelUpdateStrategy
        public void onUpdateTickMark(Resources resources, TickMark tickMark, Axis<Double, Double> axis) {
            if (tickMark.isLabelShown()) {
                String labelText = tickMark.getLabelText();
                String firstChar = labelText.substring(0, 1);
                if (firstChar.equals(resources.getString(R.string.shinobicharts_minus_sign))) {
                    String labelTextWithoutMinusSign = labelText.substring(1, labelText.length());
                    tickMark.setLabelText(labelTextWithoutMinusSign);
                }
            }
        }
    }

    /* loaded from: classes.dex */
    public static class ConvertMillisToHoursTickLabelUpdateStrategy implements TickLabelUpdateStrategy {
        private final DateTimeFormatter formatter;
        private boolean is24HourFormat;
        private long previousMillis = Long.MAX_VALUE;

        public ConvertMillisToHoursTickLabelUpdateStrategy(Resources resources, Context context) {
            this.is24HourFormat = false;
            if (DateFormat.is24HourFormat(context)) {
                this.is24HourFormat = true;
                this.formatter = DateTimeFormat.forPattern(resources.getString(R.string.shinobicharts_sleep_time_label_format_24hr));
                return;
            }
            this.formatter = DateTimeFormat.forPattern(resources.getString(R.string.shinobicharts_sleep_time_label_format));
        }

        @Override // com.shinobicontrols.kcompanionapp.charts.internal.TickLabelUpdateStrategy
        public void onUpdateTickMark(Resources resources, TickMark tickMark, Axis<Double, Double> axis) {
            if (tickMark.isLabelShown()) {
                long millis = ((Double) tickMark.getValue()).longValue();
                DateTime dateTime = new DateTime(millis);
                int hourOfDay = dateTime.hourOfDay().get();
                String formattedTime = dateTime.toString(this.formatter);
                if (this.is24HourFormat || !(isFirstVisibleTick(this.previousMillis, millis) || hourOfDay == 0 || hourOfDay == 12)) {
                    tickMark.setLabelText(formattedTime);
                } else {
                    tickMark.setLabelText(String.format(getSuffix(hourOfDay, resources), formattedTime));
                }
                this.previousMillis = millis;
            }
        }

        private String getSuffix(int hourOfDay, Resources resources) {
            return hourOfDay < 12 ? resources.getString(R.string.shinobicharts_am_suffix) : resources.getString(R.string.shinobicharts_pm_suffix);
        }

        private boolean isFirstVisibleTick(long previousMillis1, long millis) {
            return previousMillis1 > millis;
        }
    }

    /* loaded from: classes.dex */
    public static class MultiTickLabelUpdateStrategy implements TickLabelUpdateStrategy {
        private final List<TickLabelUpdateStrategy> tickMarkLabelUpdateStrategies = new ArrayList();

        public MultiTickLabelUpdateStrategy(TickLabelUpdateStrategy... tickMarkLabelUpdateStrategies) {
            for (TickLabelUpdateStrategy strategy : tickMarkLabelUpdateStrategies) {
                this.tickMarkLabelUpdateStrategies.add(strategy);
            }
        }

        @Override // com.shinobicontrols.kcompanionapp.charts.internal.TickLabelUpdateStrategy
        public void onUpdateTickMark(Resources resources, TickMark tickMark, Axis<Double, Double> axis) {
            for (TickLabelUpdateStrategy strategy : this.tickMarkLabelUpdateStrategies) {
                strategy.onUpdateTickMark(resources, tickMark, axis);
            }
        }
    }
}

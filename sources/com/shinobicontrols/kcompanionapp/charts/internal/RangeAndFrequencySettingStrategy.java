package com.shinobicontrols.kcompanionapp.charts.internal;

import com.microsoft.kapp.utils.Constants;
import com.shinobicontrols.charts.Axis;
import com.shinobicontrols.charts.NumberRange;
/* loaded from: classes.dex */
public abstract class RangeAndFrequencySettingStrategy {
    final Axis<Double, Double> axis;

    public abstract void setFrequency();

    public abstract void setRange();

    public RangeAndFrequencySettingStrategy(Axis<Double, Double> axis) {
        this.axis = axis;
    }

    public void setRangeAndFrequency() {
        setRange();
        setFrequency();
    }

    /* loaded from: classes.dex */
    public static class DefaultRangeAndFrequencySettingStrategy extends RangeAndFrequencySettingStrategy {
        private final Double majorTickFrequency;
        private final Double maxValue;
        private final Double minValue;

        public DefaultRangeAndFrequencySettingStrategy(Axis<Double, Double> axis, Double minValue, Double maxValue, Double majorTickFrequency) {
            super(axis);
            this.minValue = minValue;
            this.maxValue = maxValue;
            this.majorTickFrequency = majorTickFrequency;
        }

        @Override // com.shinobicontrols.kcompanionapp.charts.internal.RangeAndFrequencySettingStrategy
        public void setRange() {
            if (hasValidRange()) {
                this.axis.setDefaultRange(new NumberRange(this.minValue, this.maxValue));
            }
        }

        @Override // com.shinobicontrols.kcompanionapp.charts.internal.RangeAndFrequencySettingStrategy
        public void setFrequency() {
            if (this.majorTickFrequency != null) {
                this.axis.setMajorTickFrequency(this.majorTickFrequency);
            }
        }

        private boolean hasValidRange() {
            return (this.minValue == null || this.maxValue == null || this.maxValue.doubleValue() - this.minValue.doubleValue() <= Constants.SPLITS_ACCURACY) ? false : true;
        }
    }

    /* loaded from: classes.dex */
    public static class FixedNumberOfIntervalsRangeAndFrequencySettingStrategy extends RangeAndFrequencySettingStrategy {
        private final double frequency;
        private final NumberRange range;

        public FixedNumberOfIntervalsRangeAndFrequencySettingStrategy(Axis<Double, Double> axis, double minValue, double maxValue, int numberOfIntervals, int toTheNearest, Double minFrequency) {
            super(axis);
            this.range = Utils.calculateRange(minValue, maxValue, toTheNearest);
            this.frequency = Utils.calculateFrequency(this.range.getSpan(), numberOfIntervals, minFrequency);
        }

        @Override // com.shinobicontrols.kcompanionapp.charts.internal.RangeAndFrequencySettingStrategy
        public void setRange() {
            if (this.range.getSpan() > Constants.SPLITS_ACCURACY) {
                this.axis.setDefaultRange(this.range);
            }
        }

        @Override // com.shinobicontrols.kcompanionapp.charts.internal.RangeAndFrequencySettingStrategy
        public void setFrequency() {
            if (this.range.getSpan() > Constants.SPLITS_ACCURACY) {
                this.axis.setMajorTickFrequency(Double.valueOf(this.frequency));
            }
        }
    }

    /* loaded from: classes.dex */
    public static class FixedNumberOfIntervalsRangeAndRoundedFrequencySettingStrategy extends RangeAndFrequencySettingStrategy {
        private final double frequency;
        private final NumberRange range;

        public FixedNumberOfIntervalsRangeAndRoundedFrequencySettingStrategy(Axis<Double, Double> axis, double minValue, double maxValue, int numberOfIntervals, int toTheNearest, Double minFrequency, boolean increaseMin) {
            super(axis);
            double tempRange = maxValue - minValue;
            this.frequency = Utils.calculateRoundedFrequency(tempRange, numberOfIntervals, minFrequency, toTheNearest);
            this.range = Utils.updateRange(minValue, maxValue, this.frequency, numberOfIntervals, increaseMin);
        }

        @Override // com.shinobicontrols.kcompanionapp.charts.internal.RangeAndFrequencySettingStrategy
        public void setRange() {
            if (this.range.getSpan() > Constants.SPLITS_ACCURACY) {
                this.axis.setDefaultRange(this.range);
            }
        }

        @Override // com.shinobicontrols.kcompanionapp.charts.internal.RangeAndFrequencySettingStrategy
        public void setFrequency() {
            if (this.range.getSpan() > Constants.SPLITS_ACCURACY) {
                this.axis.setMajorTickFrequency(Double.valueOf(this.frequency));
            }
        }
    }

    /* loaded from: classes.dex */
    public static class GolfHeartRateIntervalsRangeAndRoundedFrequencySettingStrategy extends RangeAndFrequencySettingStrategy {
        private Double majorTickFrequency;
        private Double maxValue;
        private Double minValue;

        public GolfHeartRateIntervalsRangeAndRoundedFrequencySettingStrategy(Axis<Double, Double> axis, Double minValue, Double maxValue) {
            super(axis);
            calculateMinMaxFrequency(minValue, maxValue);
        }

        public Double getMinValue() {
            return this.minValue;
        }

        public Double getMaxValue() {
            return this.maxValue;
        }

        public Double getMajorFrequency() {
            return this.majorTickFrequency;
        }

        public void calculateMinMaxFrequency(Double min, Double max) {
            double tempMinValue = min.doubleValue() - 10.0d;
            double tempMaxValue = max.doubleValue() + 10.0d;
            if (tempMinValue % 5.0d != Constants.SPLITS_ACCURACY) {
                tempMinValue = Rounder.roundDown(tempMinValue, 5);
            }
            this.minValue = Double.valueOf(Math.max(tempMinValue, (double) Constants.SPLITS_ACCURACY));
            this.majorTickFrequency = Double.valueOf(Rounder.roundUp((Rounder.roundUp(tempMaxValue, 5) - this.minValue.doubleValue()) / 5.0d, 5));
            this.maxValue = Double.valueOf((this.majorTickFrequency.doubleValue() * 5.0d) + this.minValue.doubleValue());
        }

        @Override // com.shinobicontrols.kcompanionapp.charts.internal.RangeAndFrequencySettingStrategy
        public void setRange() {
            if (hasValidRange()) {
                this.axis.setDefaultRange(new NumberRange(this.minValue, this.maxValue));
            }
        }

        @Override // com.shinobicontrols.kcompanionapp.charts.internal.RangeAndFrequencySettingStrategy
        public void setFrequency() {
            if (this.majorTickFrequency != null) {
                this.axis.setMajorTickFrequency(this.majorTickFrequency);
            }
        }

        private boolean hasValidRange() {
            return (this.minValue == null || this.maxValue == null || this.maxValue.doubleValue() - this.minValue.doubleValue() <= Constants.SPLITS_ACCURACY) ? false : true;
        }
    }
}

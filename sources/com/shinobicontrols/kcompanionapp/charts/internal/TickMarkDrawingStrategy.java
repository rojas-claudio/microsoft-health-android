package com.shinobicontrols.kcompanionapp.charts.internal;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import com.microsoft.kapp.R;
import com.microsoft.kapp.style.utils.MetricSpannerUtils;
import com.microsoft.kapp.utils.Constants;
import com.microsoft.kapp.utils.Formatter;
import com.shinobicontrols.charts.Axis;
import com.shinobicontrols.charts.ChartUtils;
import com.shinobicontrols.charts.TickMark;
import com.shinobicontrols.kcompanionapp.charts.ChartTheme;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
/* loaded from: classes.dex */
public interface TickMarkDrawingStrategy {
    void onDrawTickMark(Resources resources, Canvas canvas, TickMark tickMark, Rect rect, Rect rect2, Axis<Double, Double> axis);

    /* loaded from: classes.dex */
    public static class NullTickMarkDrawingStrategy implements TickMarkDrawingStrategy {
        @Override // com.shinobicontrols.kcompanionapp.charts.internal.TickMarkDrawingStrategy
        public void onDrawTickMark(Resources resources, Canvas canvas, TickMark tickMark, Rect labelBackgroundRect, Rect tickMarkRect, Axis<Double, Double> axis) {
        }
    }

    /* loaded from: classes.dex */
    public static class YAxisLableUnderTickLineTickMarkDrawingStrategy implements TickMarkDrawingStrategy {
        @Override // com.shinobicontrols.kcompanionapp.charts.internal.TickMarkDrawingStrategy
        public void onDrawTickMark(Resources resources, Canvas canvas, TickMark tickMark, Rect labelBackgroundRect, Rect tickMarkRect, Axis<Double, Double> axis) {
            String label = Formatter.formatNumberWithCommas(resources, tickMark.getLabelText());
            boolean isLabelOutsidePlot = false;
            if (tickMark.isLabelShown()) {
                tickMark.getLabelPaint().setTextAlign(Paint.Align.LEFT);
                int x = canvas.getClipBounds().left;
                int y = tickMarkRect.bottom + ((int) (Utils.getPaintTextHeight(tickMark.getLabelPaint()) / 2.0d));
                float textHeight = tickMarkRect.height() + Utils.getPaintTextHeight(tickMark.getLabelPaint());
                if (Utils.stillInPlotAreaOnYaxis(axis, textHeight, y)) {
                    ChartUtils.drawText(canvas, label, x, y, tickMark.getLabelPaint());
                } else {
                    isLabelOutsidePlot = true;
                }
            }
            if (tickMark.isLineShown()) {
                tickMarkRect.left = canvas.getClipBounds().left;
                tickMarkRect.right = resources.getDimensionPixelSize(R.dimen.shinobicharts_y_axis_tick_line_length);
                if (!tickMark.isLabelShown() || !isLabelOutsidePlot) {
                    canvas.drawRect(tickMarkRect, tickMark.getLinePaint());
                }
            }
        }
    }

    /* loaded from: classes.dex */
    public static class YAxisLableAboveOrBelowTickLineDependingOnBaselineTickMarkDrawingStrategy implements TickMarkDrawingStrategy {
        private final double baseline;

        public YAxisLableAboveOrBelowTickLineDependingOnBaselineTickMarkDrawingStrategy(double baseline) {
            this.baseline = baseline;
        }

        @Override // com.shinobicontrols.kcompanionapp.charts.internal.TickMarkDrawingStrategy
        public void onDrawTickMark(Resources resources, Canvas canvas, TickMark tickMark, Rect labelBackgroundRect, Rect tickMarkRect, Axis<Double, Double> axis) {
            int y;
            if (tickMark.isLineShown()) {
                tickMarkRect.left = canvas.getClipBounds().left;
                tickMarkRect.right = resources.getDimensionPixelSize(R.dimen.shinobicharts_y_axis_tick_line_length);
                Paint paint = tickMark.getLinePaint();
                paint.setColor(resources.getColor(R.color.LightLineColor));
                canvas.drawRect(tickMarkRect, paint);
            }
            if (tickMark.isLabelShown()) {
                tickMark.getLabelPaint().setTextAlign(Paint.Align.LEFT);
                int x = canvas.getClipBounds().left;
                double value = ((Double) tickMark.getValue()).doubleValue();
                if (value >= this.baseline) {
                    y = tickMarkRect.bottom + ((int) (Utils.getPaintTextHeight(tickMark.getLabelPaint()) / 2.0d));
                } else {
                    y = tickMarkRect.top - ((int) (Utils.getPaintTextHeight(tickMark.getLabelPaint()) / 2.0d));
                }
                ChartUtils.drawText(canvas, tickMark.getLabelText(), x, y, tickMark.getLabelPaint());
            }
        }
    }

    /* loaded from: classes.dex */
    public static class XAxisLabelLeftAndTickAcrossCanvasTickMarkDrawingStrategy implements TickMarkDrawingStrategy {
        @Override // com.shinobicontrols.kcompanionapp.charts.internal.TickMarkDrawingStrategy
        public void onDrawTickMark(Resources resources, Canvas canvas, TickMark tickMark, Rect labelBackgroundRect, Rect tickMarkRect, Axis<Double, Double> axis) {
            if (tickMark.isLineShown()) {
                tickMarkRect.top = 0;
                tickMarkRect.bottom = canvas.getHeight();
                canvas.drawRect(tickMarkRect, tickMark.getLinePaint());
            }
            if (tickMark.isLabelShown()) {
                int padding = resources.getDimensionPixelSize(R.dimen.shinobicharts_axis_tick_label_horizontal_padding);
                tickMark.getLabelPaint().setTextAlign(Paint.Align.RIGHT);
                int x = tickMarkRect.left - padding;
                int y = tickMark.getLabelCenter().y;
                ChartUtils.drawText(canvas, tickMark.getLabelText(), x, y, tickMark.getLabelPaint());
            }
        }
    }

    /* loaded from: classes.dex */
    public static class CenterAlignXAxisLabelWithTickLineTickMarkDrawingStrategy implements TickMarkDrawingStrategy {
        private Rect previousTickMark;

        @Override // com.shinobicontrols.kcompanionapp.charts.internal.TickMarkDrawingStrategy
        public void onDrawTickMark(Resources resources, Canvas canvas, TickMark tickMark, Rect labelBackgroundRect, Rect tickMarkRect, Axis<Double, Double> axis) {
            int tickMarkCenterPadding;
            if (tickMark.isLabelShown()) {
                int padding = resources.getDimensionPixelSize(R.dimen.shinobicharts_axis_tick_label_horizontal_padding);
                tickMark.getLabelPaint().setTextAlign(Paint.Align.CENTER);
                int length = (int) tickMark.getLabelPaint().measureText(tickMark.getLabelText());
                int x = tickMarkRect.left - (length / 2);
                if (this.previousTickMark != null && (tickMarkCenterPadding = (tickMarkRect.left - this.previousTickMark.right) / 2) > 0) {
                    x = (tickMarkRect.left - tickMarkCenterPadding) - (length / 4);
                }
                int y = tickMark.getLabelCenter().y + padding;
                if (Utils.stillInPlotAreaOnXaxis(axis.getChart(), tickMark.getLabelText(), tickMark.getLabelPaint(), x + length)) {
                    ChartUtils.drawText(canvas, tickMark.getLabelText(), x, y, tickMark.getLabelPaint());
                }
            }
            this.previousTickMark = tickMarkRect;
        }
    }

    /* loaded from: classes.dex */
    public static class XAxisLabelCenteredWithTickLineToLeftTickMarkDrawingStrategy implements TickMarkDrawingStrategy {
        @Override // com.shinobicontrols.kcompanionapp.charts.internal.TickMarkDrawingStrategy
        public void onDrawTickMark(Resources resources, Canvas canvas, TickMark tickMark, Rect labelBackgroundRect, Rect tickMarkRect, Axis<Double, Double> axis) {
            int tickCenter = (int) axis.getPixelValueForUserValue((Double) tickMark.getValue());
            int tickWidth = resources.getDimensionPixelSize(R.dimen.shinobicharts_axis_tick_line_width);
            if (tickMark.isLineShown()) {
                tickMarkRect.top = 0;
                tickMarkRect.bottom = canvas.getHeight();
                int left = ((int) axis.getPixelValueForUserValue(Double.valueOf(((Double) tickMark.getValue()).doubleValue() - 0.5d))) - (tickWidth / 2);
                tickMarkRect.left = left;
                tickMarkRect.right = left + tickWidth;
                canvas.drawRect(tickMarkRect, tickMark.getLinePaint());
            }
            tickMark.getLabelPaint().setTextAlign(Paint.Align.CENTER);
            int y = tickMark.getLabelCenter().y;
            ChartUtils.drawText(canvas, tickMark.getLabelText(), tickCenter, y, tickMark.getLabelPaint());
        }
    }

    /* loaded from: classes.dex */
    public static class RightAlignXAxisLabelWithTickLineTickMarkDrawingStrategy implements TickMarkDrawingStrategy {
        @Override // com.shinobicontrols.kcompanionapp.charts.internal.TickMarkDrawingStrategy
        public void onDrawTickMark(Resources resources, Canvas canvas, TickMark tickMark, Rect labelBackgroundRect, Rect tickMarkRect, Axis<Double, Double> axis) {
            if (tickMark.isLabelShown()) {
                int padding = resources.getDimensionPixelSize(R.dimen.shinobicharts_axis_tick_label_horizontal_padding);
                tickMark.getLabelPaint().setTextAlign(Paint.Align.RIGHT);
                int x = tickMarkRect.left - padding;
                int y = tickMark.getLabelCenter().y + padding;
                int length = (int) tickMark.getLabelPaint().measureText(tickMark.getLabelText());
                if (Utils.stillInPlotAreaOnXaxis(axis.getChart(), tickMark.getLabelText(), tickMark.getLabelPaint(), x + length)) {
                    ChartUtils.drawText(canvas, tickMark.getLabelText(), x, y, tickMark.getLabelPaint());
                }
            }
        }
    }

    /* loaded from: classes.dex */
    public static class LeftAlignXAxisLabelWithTickLineTickMarkDrawingStrategy implements TickMarkDrawingStrategy {
        @Override // com.shinobicontrols.kcompanionapp.charts.internal.TickMarkDrawingStrategy
        public void onDrawTickMark(Resources resources, Canvas canvas, TickMark tickMark, Rect labelBackgroundRect, Rect tickMarkRect, Axis<Double, Double> axis) {
            if (tickMark.isLabelShown()) {
                int padding = resources.getDimensionPixelSize(R.dimen.shinobicharts_axis_tick_label_horizontal_padding);
                tickMark.getLabelPaint().setTextAlign(Paint.Align.RIGHT);
                int length = (int) tickMark.getLabelPaint().measureText(tickMark.getLabelText());
                int x = tickMarkRect.right + padding + length;
                int y = tickMark.getLabelCenter().y;
                if (Utils.stillInPlotAreaOnXaxis(axis.getChart(), tickMark.getLabelText(), tickMark.getLabelPaint(), x)) {
                    ChartUtils.drawText(canvas, tickMark.getLabelText(), x, y, tickMark.getLabelPaint());
                }
            }
        }
    }

    /* loaded from: classes.dex */
    public static class FixedNumberOfColumnWithMappedLabelsTickMarkDrawingStrategy implements TickMarkDrawingStrategy {
        private final double anchor;
        private final double frequency;

        public FixedNumberOfColumnWithMappedLabelsTickMarkDrawingStrategy(double anchor, double frequency) {
            this.anchor = anchor;
            this.frequency = frequency;
        }

        @Override // com.shinobicontrols.kcompanionapp.charts.internal.TickMarkDrawingStrategy
        public void onDrawTickMark(Resources resources, Canvas canvas, TickMark tickMark, Rect labelBackgroundRect, Rect tickMarkRect, Axis<Double, Double> axis) {
            if (shouldBeShown(tickMark)) {
                tickMarkRect.top = 0;
                tickMarkRect.bottom = canvas.getHeight();
                canvas.drawRect(tickMarkRect, tickMark.getLinePaint());
                tickMark.setLabelShown(true);
                return;
            }
            tickMark.setLineShown(false);
            tickMark.setLabelShown(false);
        }

        private boolean shouldBeShown(TickMark tickMark) {
            double value = ((Double) tickMark.getValue()).doubleValue();
            return tickMark.isLineShown() && (value - this.anchor) % this.frequency == Constants.SPLITS_ACCURACY;
        }
    }

    /* loaded from: classes.dex */
    public static class YAxisZonesTickMarkDrawingStrategy implements TickMarkDrawingStrategy {
        private final AxisZoneCollection axisZoneCollection;
        private final int padding;
        private final Paint titlePaint = new Paint();

        public YAxisZonesTickMarkDrawingStrategy(AxisZoneCollection axisZoneCollection, Resources resources, ChartThemeCache themeCache) {
            this.axisZoneCollection = axisZoneCollection;
            this.padding = resources.getDimensionPixelSize(R.dimen.shinobicharts_axis_zone_tick_label_padding);
            this.titlePaint.setTypeface(Typeface.createFromAsset(resources.getAssets(), themeCache.getPathToAxisTickLabelTypeface()));
            this.titlePaint.setColor(themeCache.getAxisZoneTickLabelTitleColor());
            this.titlePaint.setTextSize(resources.getDimension(R.dimen.XSmallTextSize));
        }

        @Override // com.shinobicontrols.kcompanionapp.charts.internal.TickMarkDrawingStrategy
        public void onDrawTickMark(Resources resources, Canvas canvas, TickMark tickMark, Rect labelBackgroundRect, Rect tickMarkRect, Axis<Double, Double> axis) {
            if (tickMark.isLineShown()) {
                tickMarkRect.left = canvas.getClipBounds().left;
                tickMarkRect.right = canvas.getClipBounds().right;
                canvas.drawRect(tickMarkRect, tickMark.getLinePaint());
            }
            if (tickMark.isLabelShown()) {
                tickMark.getLabelPaint().setTextAlign(Paint.Align.LEFT);
                AxisZone axisZone = getAxisZoneForTick(tickMark);
                if (axisZone != null) {
                    int x = canvas.getClipBounds().left;
                    int y = tickMarkRect.bottom + ((int) (Utils.getPaintTextHeight(tickMark.getLabelPaint()) / 3.0d));
                    ChartUtils.drawText(canvas, axisZone.getTitle(), x, y, this.titlePaint);
                    if (!axisZone.isShowValueOnNewLine()) {
                        x = (int) (x + this.titlePaint.measureText(axisZone.getTitle() + MetricSpannerUtils.METRIC_UNITS_SEPARATOR_SPACE));
                    } else {
                        y += this.padding + ((int) (Utils.getPaintTextHeight(tickMark.getLabelPaint()) / 3.0d));
                    }
                    ChartUtils.drawText(canvas, axisZone.getValueText() + axisZone.getValuePostFix(), x, y, tickMark.getLabelPaint());
                }
            }
        }

        private AxisZone getAxisZoneForTick(TickMark tickMark) {
            double value = ((Double) tickMark.getValue()).doubleValue();
            Iterator i$ = this.axisZoneCollection.iterator();
            while (i$.hasNext()) {
                AxisZone axisZone = i$.next();
                if (value == axisZone.getValue()) {
                    return axisZone;
                }
            }
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static class YAxisZonesMidValueIndendTickMarkDrawingStrategy implements TickMarkDrawingStrategy {
        private final AxisZoneCollection axisZoneCollection;
        private final int padding;
        private final Paint titlePaint = new Paint();

        public YAxisZonesMidValueIndendTickMarkDrawingStrategy(AxisZoneCollection axisZoneCollection, Resources resources, ChartThemeCache themeCache) {
            this.axisZoneCollection = axisZoneCollection;
            this.padding = resources.getDimensionPixelSize(R.dimen.shinobicharts_axis_zone_tick_label_padding);
            this.titlePaint.setTypeface(Typeface.createFromAsset(resources.getAssets(), themeCache.getPathToAxisTickLabelTypeface()));
            this.titlePaint.setColor(themeCache.getAxisZoneTickLabelTitleColor());
            this.titlePaint.setTextSize(resources.getDimension(R.dimen.XSmallTextSize));
        }

        @Override // com.shinobicontrols.kcompanionapp.charts.internal.TickMarkDrawingStrategy
        public void onDrawTickMark(Resources resources, Canvas canvas, TickMark tickMark, Rect labelBackgroundRect, Rect tickMarkRect, Axis<Double, Double> axis) {
            if (tickMark.isLabelShown()) {
                tickMark.getLabelPaint().setTextAlign(Paint.Align.LEFT);
                AxisZone axisZone = getAxisZoneForTick(tickMark);
                if (axisZone != null) {
                    int x = canvas.getClipBounds().left;
                    int y = tickMarkRect.top - ((int) (Utils.getPaintTextHeight(tickMark.getLabelPaint()) / 3.0d));
                    if (axisZone.getValue() == Constants.SPLITS_ACCURACY) {
                        y = tickMarkRect.top - ((int) ((Utils.getPaintTextHeight(tickMark.getLabelPaint()) / 3.0d) / 2.0d));
                    }
                    ChartUtils.drawText(canvas, axisZone.getTitle(), x, y, this.titlePaint);
                    if (!axisZone.isShowValueOnNewLine()) {
                        x = (int) (x + this.titlePaint.measureText(axisZone.getTitle() + MetricSpannerUtils.METRIC_UNITS_SEPARATOR_SPACE));
                    } else {
                        y += this.padding + ((int) (Utils.getPaintTextHeight(tickMark.getLabelPaint()) / 3.0d));
                    }
                    ChartUtils.drawText(canvas, axisZone.getValueText() + axisZone.getValuePostFix(), x, y, tickMark.getLabelPaint());
                }
            }
        }

        private AxisZone getAxisZoneForTick(TickMark tickMark) {
            double value = ((Double) tickMark.getValue()).doubleValue();
            Iterator i$ = this.axisZoneCollection.iterator();
            while (i$.hasNext()) {
                AxisZone axisZone = i$.next();
                if (value == axisZone.getValue()) {
                    return axisZone;
                }
            }
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static class AddUnitToUpperMostTickMarkLabel implements TickMarkDrawingStrategy {
        private TickMark previousTickMark;
        private Rect previousTickMarkRect;
        private final String unitText;
        private final Rect clearerRect = new Rect();
        private final Paint clearPaint = new Paint();

        public AddUnitToUpperMostTickMarkLabel(String unitString, ChartThemeCache theme) {
            this.unitText = unitString;
            this.clearPaint.setStyle(Paint.Style.FILL);
            this.clearPaint.setColor(theme.getChartBackgroundColor());
        }

        public void updateWithTheme(ChartTheme theme) {
            this.clearPaint.setColor(theme.getChartBackgroundColor());
        }

        @Override // com.shinobicontrols.kcompanionapp.charts.internal.TickMarkDrawingStrategy
        public void onDrawTickMark(Resources resources, Canvas canvas, TickMark tickMark, Rect labelBackgroundRect, Rect tickMarkRect, Axis<Double, Double> axis) {
            if (tickMark.isLabelShown()) {
                drawUnitNextToTickMark(canvas, tickMark, tickMarkRect, tickMark.getLabelPaint());
                drawRectOverUnitOnPreviousTcikMark(canvas, labelBackgroundRect);
                this.previousTickMark = tickMark;
                this.previousTickMarkRect = tickMarkRect;
            }
        }

        private void drawUnitNextToTickMark(Canvas canvas, TickMark tickMark, Rect tickMarkRect, Paint paint) {
            tickMark.getLabelPaint().setTextAlign(Paint.Align.LEFT);
            int x = canvas.getClipBounds().left + ((int) tickMark.getLabelPaint().measureText(tickMark.getLabelText()));
            int y = tickMarkRect.bottom + ((int) (Utils.getPaintTextHeight(tickMark.getLabelPaint()) / 2.0d));
            ChartUtils.drawText(canvas, MetricSpannerUtils.METRIC_UNITS_SEPARATOR_SPACE + this.unitText, x, y, paint);
        }

        private void drawRectOverUnitOnPreviousTcikMark(Canvas canvas, Rect labelBackgroundRect) {
            if (this.previousTickMark != null && this.previousTickMarkRect != null) {
                this.clearerRect.left = canvas.getClipBounds().left + ((int) this.previousTickMark.getLabelPaint().measureText(this.previousTickMark.getLabelText()));
                this.clearerRect.right = this.clearerRect.left + ((int) this.previousTickMark.getLabelPaint().measureText(MetricSpannerUtils.METRIC_UNITS_SEPARATOR_SPACE + this.unitText));
                this.clearerRect.top = this.previousTickMarkRect.bottom;
                this.clearerRect.bottom = this.clearerRect.top + ((int) Utils.getPaintTextHeight(this.previousTickMark.getLabelPaint()));
                canvas.drawRect(this.clearerRect, this.clearPaint);
            }
        }
    }

    /* loaded from: classes.dex */
    public static class MultiTickMarkDrawingStrategy implements TickMarkDrawingStrategy {
        private final List<TickMarkDrawingStrategy> tickMarkDrawingStrategies = new ArrayList();

        public MultiTickMarkDrawingStrategy(TickMarkDrawingStrategy... tickMarkDrawingStrategies) {
            for (TickMarkDrawingStrategy strategy : tickMarkDrawingStrategies) {
                this.tickMarkDrawingStrategies.add(strategy);
            }
        }

        @Override // com.shinobicontrols.kcompanionapp.charts.internal.TickMarkDrawingStrategy
        public void onDrawTickMark(Resources resources, Canvas canvas, TickMark tickMark, Rect labelBackgroundRect, Rect tickMarkRect, Axis<Double, Double> axis) {
            for (TickMarkDrawingStrategy strategy : this.tickMarkDrawingStrategies) {
                strategy.onDrawTickMark(resources, canvas, tickMark, labelBackgroundRect, tickMarkRect, axis);
            }
        }
    }
}

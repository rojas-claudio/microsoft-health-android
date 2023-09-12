package com.shinobicontrols.kcompanionapp.charts.internal;

import android.content.res.Resources;
import android.graphics.Paint;
import android.util.TypedValue;
import com.microsoft.kapp.R;
import com.shinobicontrols.charts.Axis;
import com.shinobicontrols.charts.NumberRange;
import com.shinobicontrols.charts.ShinobiChart;
/* loaded from: classes.dex */
public class Utils {
    public static float getPaintTextHeight(Paint paint) {
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        return fontMetrics.descent - fontMetrics.ascent;
    }

    public static double getValueForEmptyData(Resources resources, double rangeSpan) {
        TypedValue outValue = new TypedValue();
        resources.getValue(R.id.shinobicharts_empty_data_point_proportion, outValue, true);
        float emptyDataProportion = outValue.getFloat();
        return emptyDataProportion * rangeSpan;
    }

    public static boolean stillInPlotAreaOnXaxis(ShinobiChart shinobiChart, String text, Paint paint, int xCoordOfEndOfText) {
        int leftBoundary = shinobiChart.getPlotAreaRect().left - shinobiChart.getCanvasRect().left;
        int textWidth = (int) Math.ceil(paint.measureText(text));
        int xCoordOfStartOfText = xCoordOfEndOfText - textWidth;
        return xCoordOfStartOfText >= leftBoundary;
    }

    public static boolean stillInPlotAreaOnYaxis(Axis<Double, Double> axis, float height, int YCoordOfTopOfLabel) {
        double min = ((NumberRange) axis.getCurrentDisplayedRange()).getMinimum().doubleValue();
        float axisMinPixel = axis.getPixelValueForUserValue(Double.valueOf(min));
        float yCoordOfBottumOfLabel = YCoordOfTopOfLabel + height;
        return yCoordOfBottumOfLabel <= axisMinPixel;
    }

    public static NumberRange calculateRange(double minValue, double maxValue, int toTheNearest) {
        double roundedMin = Rounder.roundDown(minValue, toTheNearest);
        double roundedMax = Rounder.roundUp(maxValue, toTheNearest);
        return new NumberRange(Double.valueOf(roundedMin), Double.valueOf(roundedMax));
    }

    public static double calculateRoundedFrequency(double rangeSpan, int numberOfIntervals, Double minFrequency, int toTheNearest) {
        double calculatedFrequency = rangeSpan / numberOfIntervals;
        double roundedcalculatedFrequency = Rounder.roundUp(calculatedFrequency, toTheNearest);
        return minFrequency == null ? roundedcalculatedFrequency : Math.max(minFrequency.doubleValue(), roundedcalculatedFrequency);
    }

    public static double calculateFrequency(double rangeSpan, int numberOfIntervals, Double minFrequency) {
        double calculatedFrequency = rangeSpan / numberOfIntervals;
        return minFrequency == null ? calculatedFrequency : Math.max(minFrequency.doubleValue(), calculatedFrequency);
    }

    public static NumberRange updateRange(double minimum, double maximum, double frequency, int numberOfIntervals, boolean increaseMin) {
        if (increaseMin) {
            double min = maximum - (numberOfIntervals * frequency);
            return new NumberRange(Double.valueOf(min), Double.valueOf(maximum));
        }
        double max = (numberOfIntervals * frequency) + minimum;
        return new NumberRange(Double.valueOf(minimum), Double.valueOf(max));
    }
}

package com.shinobicontrols.kcompanionapp.charts.internal;

import com.shinobicontrols.charts.Axis;
import com.shinobicontrols.charts.NumberRange;
/* loaded from: classes.dex */
public class ColumnWidthCalculator {
    public static double getColumnWidthIncludingPadding(Axis<Double, Double> axis, int numberOfColumns) {
        NumberRange axisRange = (NumberRange) axis.getCurrentDisplayedRange();
        double rangeMaxInPixels = axis.getPixelValueForUserValue(axisRange.getMaximum());
        double rangeMinInPixels = axis.getPixelValueForUserValue(axisRange.getMinimum());
        double rangeSpanInPixels = rangeMaxInPixels - rangeMinInPixels;
        double pixelsPerColumn = rangeSpanInPixels / numberOfColumns;
        return pixelsPerColumn;
    }

    public static double getColumnWidthExcludingPadding(Axis<Double, Double> axis, int numberOfColumns) {
        double paddingFactor = 1.0d - axis.getStyle().getInterSeriesPadding();
        return getColumnWidthIncludingPadding(axis, numberOfColumns) * paddingFactor;
    }
}

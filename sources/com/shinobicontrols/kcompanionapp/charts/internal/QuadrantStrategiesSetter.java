package com.shinobicontrols.kcompanionapp.charts.internal;

import android.view.View;
import com.microsoft.kapp.R;
import com.shinobicontrols.charts.Axis;
import com.shinobicontrols.charts.NumberRange;
import com.shinobicontrols.kcompanionapp.charts.internal.FlagPlacementStrategy;
/* loaded from: classes.dex */
public class QuadrantStrategiesSetter {
    public static void setStrategiesBasedOnData(FlagView flagView, double xDataValue, double yDataValue, Axis<Double, Double> xAxis, Axis<Double, Double> yAxis) {
        int triangleHeight = flagView.getResources().getDimensionPixelSize(R.dimen.shinobicharts_triangle_height);
        int widthFlagViewText = flagView.getChildAt(0).getMeasuredWidth();
        int heightFlagViewText = flagView.getChildAt(0).getMeasuredHeight();
        float xPixelValue = xAxis.getPixelValueForUserValue(Double.valueOf(xDataValue));
        float yPixelValue = yAxis.getPixelValueForUserValue(Double.valueOf(yDataValue));
        NumberRange xAxisRange = (NumberRange) xAxis.getCurrentDisplayedRange();
        NumberRange yAxisRange = (NumberRange) yAxis.getCurrentDisplayedRange();
        float xAxisMinPixelValue = xAxis.getPixelValueForUserValue(xAxisRange.getMinimum());
        float xAxisMaxPixelValue = xAxis.getPixelValueForUserValue(xAxisRange.getMaximum());
        float yAxisMinPixelValue = yAxis.getPixelValueForUserValue(yAxisRange.getMinimum());
        float yAxisMaxPixelValue = yAxis.getPixelValueForUserValue(yAxisRange.getMaximum());
        float xAxisMidPixelValue = (xAxisMaxPixelValue - xAxisMinPixelValue) / 2.0f;
        float yAxisMidPixelValue = (yAxisMinPixelValue - yAxisMaxPixelValue) / 2.0f;
        if (fitsInTopRightQuadrant(xPixelValue, yPixelValue, xAxisMaxPixelValue, yAxisMaxPixelValue, xAxisMidPixelValue, yAxisMidPixelValue, triangleHeight, widthFlagViewText, heightFlagViewText)) {
            flagView.setFlagPlacementStrategy(FlagPlacementStrategy.TOP_RIGHT_QUADRANT_FLAG_PLACEMENT_STRATEGY);
            flagView.setFlagPointerDrawingStrategy(FlagPointerDrawingStrategy.TOP_RIGHT_QUADRANT_FLAG_POINTER_DRAWING_STRATEGY);
        } else if (fitsInTopLeftQuadrant(xPixelValue, yPixelValue, xAxisMinPixelValue, yAxisMaxPixelValue, xAxisMidPixelValue, yAxisMidPixelValue, triangleHeight, widthFlagViewText, heightFlagViewText)) {
            flagView.setFlagPlacementStrategy(FlagPlacementStrategy.TOP_LEFT_QUADRANT_FLAG_PLACEMENT_STRATEGY);
            flagView.setFlagPointerDrawingStrategy(FlagPointerDrawingStrategy.TOP_LEFT_QUADRANT_FLAG_POINTER_DRAWING_STRATEGY);
        } else if (fitsInBottomLeftQuadrant(xPixelValue, yPixelValue, xAxisMinPixelValue, yAxisMinPixelValue, xAxisMidPixelValue, yAxisMidPixelValue, triangleHeight, widthFlagViewText, heightFlagViewText)) {
            flagView.setFlagPlacementStrategy(FlagPlacementStrategy.BOTTOM_LEFT_QUADRANT_FLAG_PLACEMENT_STRATEGY);
            flagView.setFlagPointerDrawingStrategy(FlagPointerDrawingStrategy.BOTTOM_LEFT_QUADRANT_FLAG_POINTER_DRAWING_STRATEGY);
        } else if (fitsInBottomRightQuadrant(xPixelValue, yPixelValue, xAxisMaxPixelValue, yAxisMinPixelValue, xAxisMidPixelValue, yAxisMidPixelValue, triangleHeight, widthFlagViewText, heightFlagViewText)) {
            flagView.setFlagPlacementStrategy(FlagPlacementStrategy.BOTTOM_RIGHT_QUADRANT_FLAG_PLACEMENT_STRATEGY);
            flagView.setFlagPointerDrawingStrategy(FlagPointerDrawingStrategy.BOTTOM_RIGHT_QUADRANT_FLAG_POINTER_DRAWING_STRATEGY);
        } else {
            flagView.setFlagPlacementStrategy(new FlagPlacementStrategy.XYPositionsWithinRangeFlagPlacementStrategy(xPixelValue, yPixelValue, xAxisMinPixelValue, xAxisMaxPixelValue, yAxisMinPixelValue, yAxisMaxPixelValue));
            flagView.setFlagPointerDrawingStrategy(FlagPointerDrawingStrategy.TOP_RIGHT_QUADRANT_FLAG_POINTER_DRAWING_STRATEGY);
        }
        int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, 0);
        int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, 0);
        flagView.measure(widthMeasureSpec, heightMeasureSpec);
    }

    private static boolean fitsInTopRightQuadrant(float xPixelValue, float yPixelValue, float xAxisMaxPixelValue, float yAxisMaxPixelValue, float xAxisMidPixelValue, float yAxisMidPixelValue, int triangleHeight, int widthFlagViewText, int heightFlagViewText) {
        return ((float) widthFlagViewText) + xPixelValue <= xAxisMaxPixelValue && xPixelValue >= xAxisMidPixelValue && (yPixelValue - ((float) triangleHeight)) - ((float) heightFlagViewText) >= yAxisMaxPixelValue && yPixelValue <= yAxisMidPixelValue;
    }

    private static boolean fitsInTopLeftQuadrant(float xPixelValue, float yPixelValue, float xAxisMinPixelValue, float yAxisMaxPixelValue, float xAxisMidPixelValue, float yAxisMidPixelValue, int triangleHeight, int widthFlagViewText, int heightFlagViewText) {
        return xPixelValue >= xAxisMinPixelValue && ((float) widthFlagViewText) + xPixelValue <= xAxisMidPixelValue && (yPixelValue - ((float) triangleHeight)) - ((float) heightFlagViewText) >= yAxisMaxPixelValue && yPixelValue <= yAxisMidPixelValue;
    }

    private static boolean fitsInBottomLeftQuadrant(float xPixelValue, float yPixelValue, float xAxisMinPixelValue, float yAxisMinPixelValue, float xAxisMidPixelValue, float yAxisMidPixelValue, int triangleHeight, int widthFlagViewText, int heightFlagViewText) {
        return xPixelValue >= xAxisMinPixelValue && ((float) widthFlagViewText) + xPixelValue <= xAxisMidPixelValue && yPixelValue <= yAxisMinPixelValue && (yPixelValue - ((float) triangleHeight)) - ((float) heightFlagViewText) >= yAxisMidPixelValue;
    }

    private static boolean fitsInBottomRightQuadrant(float xPixelValue, float yPixelValue, float xAxisMaxPixelValue, float yAxisMinPixelValue, float xAxisMidPixelValue, float yAxisMidPixelValue, int triangleHeight, int widthFlagViewText, int heightFlagViewText) {
        return ((float) widthFlagViewText) + xPixelValue <= xAxisMaxPixelValue && xPixelValue >= xAxisMidPixelValue && yPixelValue <= yAxisMinPixelValue && (yPixelValue - ((float) triangleHeight)) - ((float) heightFlagViewText) >= yAxisMidPixelValue;
    }
}

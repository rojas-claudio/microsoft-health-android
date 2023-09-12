package com.shinobicontrols.kcompanionapp.properties;

import android.graphics.PointF;
import com.microsoft.kapp.utils.Constants;
import com.shinobicontrols.charts.DataAdapter;
import java.util.List;
/* loaded from: classes.dex */
public class ChartDataProperties {
    private double mAverageValue;
    protected double mMaxValue;
    protected double mMinValue;
    private List<PointF> mPausePoints;
    private double mPositionAtMaxValue;
    private int mSegments;
    private List<DataAdapter<?, ?>> mSeriesData;

    public ChartDataProperties() {
    }

    public ChartDataProperties(double minValue, double maxValue, int segments, List<DataAdapter<?, ?>> seriesData) {
        this(minValue, maxValue, Constants.SPLITS_ACCURACY, Constants.SPLITS_ACCURACY, segments, null, seriesData);
    }

    public ChartDataProperties(double minValue, double maxValue, double positionAtMaxValue, double averageValue, List<PointF> pausePoints, List<DataAdapter<?, ?>> seriesData) {
        this(minValue, maxValue, positionAtMaxValue, averageValue, 0, pausePoints, seriesData);
    }

    public ChartDataProperties(double minValue, double maxValue, double positionAtMaxValue, double averageValue, int segments, List<PointF> pausePoints, List<DataAdapter<?, ?>> seriesData) {
        this.mPausePoints = pausePoints;
        this.mMinValue = minValue;
        this.mMaxValue = maxValue;
        this.mPositionAtMaxValue = positionAtMaxValue;
        this.mAverageValue = averageValue;
        this.mSegments = segments;
        this.mSeriesData = seriesData;
    }

    public List<PointF> getPausePoints() {
        return this.mPausePoints;
    }

    public void addPausePoint(PointF pausePoint) {
        this.mPausePoints.add(pausePoint);
    }

    public double getMinValue() {
        return this.mMinValue;
    }

    public double getMaxValue() {
        return this.mMaxValue;
    }

    public double getPositionAtMaxValue() {
        return this.mPositionAtMaxValue;
    }

    public double getAverageValue() {
        return this.mAverageValue;
    }

    public int getSegmentCount() {
        return this.mSegments;
    }

    public List<DataAdapter<?, ?>> getSeriesData() {
        return this.mSeriesData;
    }

    public DataAdapter<?, ?> getDefaultSeriesData() {
        if (this.mSeriesData == null || this.mSeriesData.size() < 1) {
            return null;
        }
        return this.mSeriesData.get(0);
    }
}

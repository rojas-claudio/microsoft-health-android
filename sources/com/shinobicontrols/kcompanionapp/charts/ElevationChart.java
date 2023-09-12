package com.shinobicontrols.kcompanionapp.charts;

import android.graphics.PointF;
import android.view.View;
import android.widget.FrameLayout;
import com.microsoft.kapp.R;
import com.microsoft.kapp.style.utils.MetricSpannerUtils;
import com.microsoft.kapp.utils.Constants;
import com.shinobicontrols.charts.AnnotationsManager;
import com.shinobicontrols.charts.Axis;
import com.shinobicontrols.charts.LineSeriesStyle;
import com.shinobicontrols.charts.NumberRange;
import com.shinobicontrols.charts.Series;
import com.shinobicontrols.charts.ShinobiChart;
import com.shinobicontrols.charts.TickMark;
import com.shinobicontrols.kcompanionapp.charts.internal.AxisMapping;
import com.shinobicontrols.kcompanionapp.charts.internal.FlagView;
import com.shinobicontrols.kcompanionapp.charts.internal.IconFactory;
import com.shinobicontrols.kcompanionapp.charts.internal.Line;
import com.shinobicontrols.kcompanionapp.charts.internal.PauseLinesView;
import com.shinobicontrols.kcompanionapp.charts.internal.QuadrantStrategiesSetter;
import com.shinobicontrols.kcompanionapp.charts.internal.RangeAndFrequencySettingStrategy;
import com.shinobicontrols.kcompanionapp.charts.internal.SeriesCreator;
import com.shinobicontrols.kcompanionapp.charts.internal.StyledNumberAxis;
import com.shinobicontrols.kcompanionapp.charts.internal.TickLabelUpdateStrategy;
import com.shinobicontrols.kcompanionapp.charts.internal.TickMarkDrawingStrategy;
import com.shinobicontrols.kcompanionapp.charts.internal.TypedValueGetter;
import com.shinobicontrols.kcompanionapp.properties.ChartConfig;
import com.shinobicontrols.kcompanionapp.properties.ChartDataProperties;
import com.shinobicontrols.kcompanionapp.providers.YAxisStrategyProvider;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes.dex */
public class ElevationChart extends BaseChart {
    private RangeAndFrequencySettingStrategy mElevationYAxisRangeAndFrequencySettingStrategy;
    private View mIcon;
    private PauseLinesView mPauseLinesView;
    private FlagView mPeakElevationFlag;
    private Series<?> mSeries;
    private Axis<Double, Double> mYAxis;

    public ElevationChart(ChartConfig config, YAxisStrategyProvider strategyProvider, ChartDataProperties chartData) {
        this(config, strategyProvider, chartData, config.getContext().getString(R.string.shinobicharts_estimated_elevation));
    }

    public ElevationChart(ChartConfig config, YAxisStrategyProvider strategyProvider, ChartDataProperties chartData, String chartTitle) {
        super(config, strategyProvider, chartData, chartTitle);
    }

    @Override // com.shinobicontrols.kcompanionapp.charts.IChartView
    public Axis<Double, Double> getYAxis() {
        TypedValueGetter valueGetter = new TypedValueGetter(getContext().getResources());
        StyledNumberAxis axis = new StyledNumberAxis(getContext(), this.mThemeCache);
        axis.setTickMarkClippingModeLow(TickMark.ClippingMode.NEITHER_PERSIST);
        axis.setTickLabelUpdateStrategy(new TickLabelUpdateStrategy.NullTickLabelUpdateStrategy());
        String unit = getContext().getString(isMetric() ? R.string.shinobicharts_feet_metric : R.string.shinobicharts_feet_imperial);
        TickMarkDrawingStrategy.AddUnitToUpperMostTickMarkLabel addUnitToUpperMostTickMarkLabel = new TickMarkDrawingStrategy.AddUnitToUpperMostTickMarkLabel(unit, this.mThemeCache);
        axis.setTickMarkDrawingStrategy(new TickMarkDrawingStrategy.MultiTickMarkDrawingStrategy(new TickMarkDrawingStrategy.YAxisLableUnderTickLineTickMarkDrawingStrategy(), addUnitToUpperMostTickMarkLabel));
        double min = valueGetter.getFloat(R.id.shinobicharts_run_elevation_default_min);
        double max = valueGetter.getFloat(R.id.shinobicharts_run_elevation_default_max);
        int numberOfIntervals = getContext().getResources().getInteger(R.integer.shinobicharts_run_elevation_default_number_of_intervals);
        int toTheNearest = getContext().getResources().getInteger(R.integer.shinobicharts_run_elevation_round_to_nearest_value);
        this.mElevationYAxisRangeAndFrequencySettingStrategy = new RangeAndFrequencySettingStrategy.FixedNumberOfIntervalsRangeAndFrequencySettingStrategy(axis, min, max, numberOfIntervals, toTheNearest, null);
        this.mElevationYAxisRangeAndFrequencySettingStrategy.setRangeAndFrequency();
        this.mYAxis = axis;
        return this.mYAxis;
    }

    @Override // com.shinobicontrols.kcompanionapp.charts.IChartView
    public void createSeries() {
        this.mSeries = SeriesCreator.LINE.create(getContext().getResources(), this.mThemeCache);
        LineSeriesStyle style = (LineSeriesStyle) this.mSeries.getStyle();
        style.setLineColor(0);
        style.setAreaColor(0);
    }

    @Override // com.shinobicontrols.kcompanionapp.charts.IChartView
    public View createIcon() {
        this.mIcon = IconFactory.create(IconFactory.ELEVATION, getContext(), this.mThemeCache);
        return this.mIcon;
    }

    @Override // com.shinobicontrols.kcompanionapp.charts.IChartView
    public AxisMapping getAxisMapping() {
        List<Series<?>> seriesList = new ArrayList<>();
        seriesList.add(this.mSeries);
        List<View> viewsToShowWhenSelected = new ArrayList<>();
        viewsToShowWhenSelected.add(this.mPeakElevationFlag);
        viewsToShowWhenSelected.add(this.mPauseLinesView);
        return new AxisMapping(this.mIcon, getChartTitle(), seriesList, this.mYAxis, viewsToShowWhenSelected);
    }

    @Override // com.shinobicontrols.kcompanionapp.charts.IChartView
    public void createAnnotations() {
        this.mPeakElevationFlag = super.createFlagView(null);
    }

    @Override // com.shinobicontrols.kcompanionapp.charts.IChartView
    public void createAdditionalViews(FrameLayout chartHolder) {
        this.mPauseLinesView = super.createPauseLineView(chartHolder);
    }

    @Override // com.shinobicontrols.kcompanionapp.charts.IChartView
    public void reload(Axis xAxis) {
        this.mSeries.setDataAdapter(getChartDataProperties().getDefaultSeriesData());
        this.mElevationYAxisRangeAndFrequencySettingStrategy = this.mYAxisStrategy.getRangeAndFrequencyStrategy(getContext(), this.mYAxis, Constants.SPLITS_ACCURACY, Constants.SPLITS_ACCURACY, getChartDataProperties());
        this.mElevationYAxisRangeAndFrequencySettingStrategy.setRangeAndFrequency();
        updateFlags(getChartDataProperties(), xAxis);
    }

    @Override // com.shinobicontrols.kcompanionapp.charts.IChartView
    public void onInternalLayout(ShinobiChart chart, Axis xAxis) {
        if (getChartDataProperties() != null && xAxis != null) {
            QuadrantStrategiesSetter.setStrategiesBasedOnData(this.mPeakElevationFlag, getChartDataProperties().getPositionAtMaxValue(), getChartDataProperties().getMaxValue(), xAxis, this.mYAxis);
            if (this.mYAxis != null && this.mPauseLinesView != null) {
                this.mPauseLinesView.clearLines();
                double elevationTopY = this.mYAxis.getDataRange() != null ? ((NumberRange) this.mYAxis.getDataRange()).getMaximum().doubleValue() : getChartDataProperties().getMaxValue();
                for (PointF d : getChartDataProperties().getPausePoints()) {
                    this.mPauseLinesView.addLine(new Line(xAxis.getPixelValueForUserValue(Double.valueOf(d.x)), this.mYAxis.getPixelValueForUserValue(Double.valueOf(elevationTopY)), xAxis.getPixelValueForUserValue(Double.valueOf(d.x)), this.mYAxis.getPixelValueForUserValue(Double.valueOf(d.y))));
                }
            }
        }
    }

    private void updateFlags(ChartDataProperties dataProperties, Axis xAxis) {
        AnnotationsManager annotationsManager = this.mYAxis.getChart().getAnnotationsManager();
        String unit = getContext().getString(isMetric() ? R.string.shinobicharts_feet_metric : R.string.shinobicharts_feet_imperial);
        double maxElevation = dataProperties.getMaxValue();
        this.mPeakElevationFlag.setValue(Math.round(maxElevation) + MetricSpannerUtils.METRIC_UNITS_SEPARATOR_SPACE + unit);
        annotationsManager.addViewAnnotation(this.mPeakElevationFlag, Double.valueOf(dataProperties.getPositionAtMaxValue()), Double.valueOf(maxElevation), xAxis, this.mYAxis);
    }
}

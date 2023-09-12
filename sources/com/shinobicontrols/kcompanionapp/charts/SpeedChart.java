package com.shinobicontrols.kcompanionapp.charts;

import android.graphics.Color;
import android.graphics.PointF;
import android.view.View;
import android.widget.FrameLayout;
import com.microsoft.kapp.R;
import com.microsoft.kapp.utils.Constants;
import com.shinobicontrols.charts.AnnotationsManager;
import com.shinobicontrols.charts.Axis;
import com.shinobicontrols.charts.CartesianSeries;
import com.shinobicontrols.charts.LineSeriesStyle;
import com.shinobicontrols.charts.NumberRange;
import com.shinobicontrols.charts.Series;
import com.shinobicontrols.charts.ShinobiChart;
import com.shinobicontrols.charts.TickMark;
import com.shinobicontrols.kcompanionapp.charts.internal.AveragePaceLineView;
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
import com.shinobicontrols.kcompanionapp.properties.ChartConfig;
import com.shinobicontrols.kcompanionapp.properties.ChartDataProperties;
import com.shinobicontrols.kcompanionapp.providers.YAxisStrategyProvider;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes.dex */
public class SpeedChart extends BaseChart {
    private AveragePaceLineView mAverageSpeedView;
    private View mIcon;
    private PauseLinesView mPausePointsView;
    private FlagView mPeakSpeedFlag;
    private Series<?> mSeriesLine;
    private Axis<Double, Double> mYAxis;
    private RangeAndFrequencySettingStrategy mYAxisRangeAndFrequencySettingStrategy;

    public SpeedChart(ChartConfig config, YAxisStrategyProvider yAxisStrategyProvider, ChartDataProperties chartData) {
        this(config, yAxisStrategyProvider, chartData, "");
    }

    public SpeedChart(ChartConfig config, YAxisStrategyProvider yAxisStrategyProvider, ChartDataProperties chartData, String chartTitle) {
        super(config, yAxisStrategyProvider, chartData, chartTitle);
    }

    @Override // com.shinobicontrols.kcompanionapp.charts.IChartView
    public Axis<Double, Double> getYAxis() {
        StyledNumberAxis axis = new StyledNumberAxis(getContext(), this.mThemeCache);
        axis.setTickMarkClippingModeLow(TickMark.ClippingMode.NEITHER_PERSIST);
        axis.setTickLabelUpdateStrategy(new TickLabelUpdateStrategy.PaceTickLabelUpdateStrategy());
        String unit = getContext().getString(isMetric() ? R.string.unit_kmperhr : R.string.unit_mileperhr);
        TickMarkDrawingStrategy.AddUnitToUpperMostTickMarkLabel addUnitToUpperMostTickMarkLabel = new TickMarkDrawingStrategy.AddUnitToUpperMostTickMarkLabel(unit, this.mThemeCache);
        axis.setTickMarkDrawingStrategy(new TickMarkDrawingStrategy.MultiTickMarkDrawingStrategy(new TickMarkDrawingStrategy.YAxisLableUnderTickLineTickMarkDrawingStrategy(), addUnitToUpperMostTickMarkLabel));
        this.mYAxis = axis;
        return this.mYAxis;
    }

    @Override // com.shinobicontrols.kcompanionapp.charts.IChartView
    public void createSeries() {
        this.mSeriesLine = SeriesCreator.LINE.create(getContext().getResources(), this.mThemeCache);
        LineSeriesStyle paceLineStyle = (LineSeriesStyle) this.mSeriesLine.getStyle();
        paceLineStyle.setAreaColor(Color.argb(0, 1, 0, 0));
        paceLineStyle.setAreaColorBelowBaseline(Color.argb(0, 1, 0, 0));
        LineSeriesStyle paceLineSelectedStyle = (LineSeriesStyle) this.mSeriesLine.getSelectedStyle();
        paceLineSelectedStyle.setAreaLineColor(this.mThemeCache.getAboveAveragePaceColor());
        paceLineSelectedStyle.setAreaLineColorBelowBaseline(this.mThemeCache.getBelowAveragePaceColor());
    }

    @Override // com.shinobicontrols.kcompanionapp.charts.IChartView
    public View createIcon() {
        this.mIcon = IconFactory.create(IconFactory.SPEED, getContext(), this.mThemeCache);
        return this.mIcon;
    }

    @Override // com.shinobicontrols.kcompanionapp.charts.IChartView
    public AxisMapping getAxisMapping() {
        List<Series<?>> seriesList = new ArrayList<>();
        seriesList.add(this.mSeriesLine);
        List<View> viewsToShowWhenSelected = new ArrayList<>();
        viewsToShowWhenSelected.add(this.mAverageSpeedView);
        viewsToShowWhenSelected.add(this.mPausePointsView);
        viewsToShowWhenSelected.add(this.mPeakSpeedFlag);
        return new AxisMapping(this.mIcon, seriesList, this.mYAxis, viewsToShowWhenSelected);
    }

    @Override // com.shinobicontrols.kcompanionapp.charts.IChartView
    public void createAnnotations() {
        this.mPeakSpeedFlag = super.createFlagView(null);
    }

    @Override // com.shinobicontrols.kcompanionapp.charts.IChartView
    public void createAdditionalViews(FrameLayout chartHolder) {
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(-1, -1);
        lp.leftMargin = getContext().getResources().getDimensionPixelSize(R.dimen.shinobicharts_chart_horizontal_margin);
        lp.rightMargin = getContext().getResources().getDimensionPixelSize(R.dimen.shinobicharts_chart_horizontal_margin);
        this.mPausePointsView = super.createPauseLineView(chartHolder);
        this.mAverageSpeedView = new AveragePaceLineView(getContext(), this.mThemeCache, getContext().getString(R.string.shinobicharts_average_speed));
        this.mAverageSpeedView.setBackgroundColor(0);
        this.mAverageSpeedView.setVisibility(8);
        chartHolder.addView(this.mAverageSpeedView, 0, lp);
        chartHolder.bringChildToFront(this.mAverageSpeedView);
    }

    @Override // com.shinobicontrols.kcompanionapp.charts.IChartView
    public void reload(Axis xAxis) {
        ChartDataProperties dataProperties = getChartDataProperties();
        this.mYAxisRangeAndFrequencySettingStrategy = this.mYAxisStrategy.getRangeAndFrequencyStrategy(getContext(), this.mYAxis, Constants.SPLITS_ACCURACY, Constants.SPLITS_ACCURACY, dataProperties);
        this.mYAxisRangeAndFrequencySettingStrategy.setRangeAndFrequency();
        updatePeakSpeedFlag(dataProperties, xAxis);
        this.mSeriesLine.setDataAdapter(dataProperties.getDefaultSeriesData());
        ((CartesianSeries) this.mSeriesLine).setBaseline(Double.valueOf(dataProperties.getAverageValue()));
    }

    @Override // com.shinobicontrols.kcompanionapp.charts.IChartView
    public void onInternalLayout(ShinobiChart chart, Axis xAxis) {
        ChartDataProperties dataProperties = getChartDataProperties();
        if (dataProperties != null && xAxis != null && this.mYAxis != null) {
            QuadrantStrategiesSetter.setStrategiesBasedOnData(this.mPeakSpeedFlag, dataProperties.getPositionAtMaxValue(), dataProperties.getMaxValue(), xAxis, this.mYAxis);
            if (this.mAverageSpeedView != null) {
                this.mAverageSpeedView.setChartCanvasRect(chart.getCanvasRect());
                this.mAverageSpeedView.setChartPlotAreaRect(chart.getPlotAreaRect());
                NumberRange dataRange = (NumberRange) xAxis.getDataRange();
                this.mAverageSpeedView.setXPixelValue(xAxis.getPixelValueForUserValue(dataRange.getMinimum()));
                this.mAverageSpeedView.setYPixelValue(this.mYAxis.getPixelValueForUserValue(Double.valueOf(dataProperties.getAverageValue())));
            }
            double speedTopY = this.mYAxis.getDataRange() != null ? ((NumberRange) this.mYAxis.getDataRange()).getMaximum().doubleValue() : dataProperties.getAverageValue();
            if (this.mPausePointsView != null) {
                this.mPausePointsView.clearLines();
                for (PointF d : dataProperties.getPausePoints()) {
                    this.mPausePointsView.addLine(new Line(xAxis.getPixelValueForUserValue(Double.valueOf(d.x)), this.mYAxis.getPixelValueForUserValue(Double.valueOf(speedTopY)), xAxis.getPixelValueForUserValue(Double.valueOf(d.x)), this.mYAxis.getPixelValueForUserValue(Double.valueOf(d.y))));
                }
            }
        }
    }

    private void updatePeakSpeedFlag(ChartDataProperties dataProperties, Axis xAxis) {
        AnnotationsManager annotationsManager = this.mYAxis.getChart().getAnnotationsManager();
        String unit = getContext().getString(isMetric() ? R.string.shinobicharts_kmperhr : R.string.shinobicharts_mileperhr);
        double maxSpeed = dataProperties.getMaxValue();
        this.mPeakSpeedFlag.setValue(String.format(unit, Long.valueOf(Math.round(maxSpeed))));
        annotationsManager.addViewAnnotation(this.mPeakSpeedFlag, Double.valueOf(dataProperties.getPositionAtMaxValue()), Double.valueOf(maxSpeed), xAxis, this.mYAxis);
    }
}

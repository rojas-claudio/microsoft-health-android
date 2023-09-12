package com.shinobicontrols.kcompanionapp.charts;

import android.graphics.Color;
import android.graphics.PointF;
import android.view.View;
import android.widget.FrameLayout;
import com.microsoft.kapp.R;
import com.shinobicontrols.charts.Axis;
import com.shinobicontrols.charts.CartesianSeries;
import com.shinobicontrols.charts.LineSeriesStyle;
import com.shinobicontrols.charts.NumberRange;
import com.shinobicontrols.charts.Series;
import com.shinobicontrols.charts.SeriesStyle;
import com.shinobicontrols.charts.ShinobiChart;
import com.shinobicontrols.charts.TickMark;
import com.shinobicontrols.kcompanionapp.charts.internal.AveragePaceLineView;
import com.shinobicontrols.kcompanionapp.charts.internal.AxisMapping;
import com.shinobicontrols.kcompanionapp.charts.internal.IconFactory;
import com.shinobicontrols.kcompanionapp.charts.internal.Line;
import com.shinobicontrols.kcompanionapp.charts.internal.PauseLinesView;
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
public class PaceChart extends BaseChart {
    private AveragePaceLineView mAveragePaceView;
    private View mIcon;
    private PauseLinesView mPausePointsView;
    private Series<?> mSeriesFill;
    private Series<?> mSeriesLine;
    private Axis<Double, Double> mYAxis;
    private RangeAndFrequencySettingStrategy mYAxisRangeAndFrequencySettingStrategy;

    public PaceChart(ChartConfig config, YAxisStrategyProvider yAxisStrategyProvider, ChartDataProperties chartData) {
        this(config, yAxisStrategyProvider, chartData, "");
    }

    public PaceChart(ChartConfig config, YAxisStrategyProvider yAxisStrategyProvider, ChartDataProperties chartData, String chartTitle) {
        super(config, yAxisStrategyProvider, chartData, chartTitle);
    }

    @Override // com.shinobicontrols.kcompanionapp.charts.IChartView
    public Axis<Double, Double> getYAxis() {
        StyledNumberAxis axis = new StyledNumberAxis(getContext(), this.mThemeCache);
        axis.setTickMarkClippingModeLow(TickMark.ClippingMode.NEITHER_PERSIST);
        axis.setTickLabelUpdateStrategy(new TickLabelUpdateStrategy.PaceTickLabelUpdateStrategy());
        String unit = getContext().getString(isMetric() ? R.string.shinobicharts_minperkm : R.string.shinobicharts_minpermile);
        TickMarkDrawingStrategy.AddUnitToUpperMostTickMarkLabel addUnitToUpperMostTickMarkLabel = new TickMarkDrawingStrategy.AddUnitToUpperMostTickMarkLabel(unit, this.mThemeCache);
        axis.setTickMarkDrawingStrategy(new TickMarkDrawingStrategy.MultiTickMarkDrawingStrategy(new TickMarkDrawingStrategy.YAxisLableUnderTickLineTickMarkDrawingStrategy(), addUnitToUpperMostTickMarkLabel));
        this.mYAxis = axis;
        return axis;
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
        paceLineSelectedStyle.setAreaColor(Color.argb(0, 1, 0, 0));
        paceLineSelectedStyle.setAreaColorBelowBaseline(Color.argb(0, 1, 0, 0));
        paceLineSelectedStyle.setFillStyle(SeriesStyle.FillStyle.FLAT);
        this.mSeriesFill = SeriesCreator.LINE.create(getContext().getResources(), this.mThemeCache);
        LineSeriesStyle paceFillStyle = (LineSeriesStyle) this.mSeriesFill.getStyle();
        paceFillStyle.setLineShown(false);
        LineSeriesStyle paceFillSelectedStyle = (LineSeriesStyle) this.mSeriesFill.getSelectedStyle();
        paceFillSelectedStyle.setLineShown(false);
    }

    @Override // com.shinobicontrols.kcompanionapp.charts.IChartView
    public View createIcon() {
        this.mIcon = IconFactory.create(IconFactory.RUN, getContext(), this.mThemeCache);
        return this.mIcon;
    }

    @Override // com.shinobicontrols.kcompanionapp.charts.IChartView
    public AxisMapping getAxisMapping() {
        List<Series<?>> seriesList = new ArrayList<>();
        seriesList.add(this.mSeriesFill);
        seriesList.add(this.mSeriesLine);
        List<View> viewsToShowWhenSelected = new ArrayList<>();
        viewsToShowWhenSelected.add(this.mAveragePaceView);
        viewsToShowWhenSelected.add(this.mPausePointsView);
        return new AxisMapping(this.mIcon, seriesList, this.mYAxis, viewsToShowWhenSelected);
    }

    @Override // com.shinobicontrols.kcompanionapp.charts.IChartView
    public void createAnnotations() {
    }

    @Override // com.shinobicontrols.kcompanionapp.charts.IChartView
    public void createAdditionalViews(FrameLayout chartHolder) {
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(-1, -1);
        lp.leftMargin = getContext().getResources().getDimensionPixelSize(R.dimen.shinobicharts_chart_horizontal_margin);
        lp.rightMargin = getContext().getResources().getDimensionPixelSize(R.dimen.shinobicharts_chart_horizontal_margin);
        this.mPausePointsView = super.createPauseLineView(chartHolder);
        this.mAveragePaceView = new AveragePaceLineView(getContext(), this.mThemeCache, getContext().getString(R.string.shinobicharts_average_pace));
        this.mAveragePaceView.setBackgroundColor(0);
        this.mAveragePaceView.setVisibility(8);
        chartHolder.addView(this.mAveragePaceView, 0, lp);
        chartHolder.bringChildToFront(this.mAveragePaceView);
    }

    @Override // com.shinobicontrols.kcompanionapp.charts.IChartView
    public void reload(Axis xAxis) {
        ChartDataProperties dataProperties = getChartDataProperties();
        this.mYAxisRangeAndFrequencySettingStrategy = this.mYAxisStrategy.getRangeAndFrequencyStrategy(getContext(), this.mYAxis, dataProperties.getMaxValue(), dataProperties.getMinValue(), dataProperties);
        this.mYAxisRangeAndFrequencySettingStrategy.setRangeAndFrequency();
        this.mSeriesLine.setDataAdapter(dataProperties.getDefaultSeriesData());
        this.mSeriesFill.setDataAdapter(dataProperties.getDefaultSeriesData());
        ((CartesianSeries) this.mSeriesLine).setBaseline(Double.valueOf(dataProperties.getAverageValue()));
    }

    @Override // com.shinobicontrols.kcompanionapp.charts.IChartView
    public void onInternalLayout(ShinobiChart chart, Axis xAxis) {
        ChartDataProperties dataProperties = getChartDataProperties();
        if (dataProperties != null && xAxis != null && this.mYAxis != null) {
            if (this.mAveragePaceView != null) {
                this.mAveragePaceView.setChartCanvasRect(chart.getCanvasRect());
                this.mAveragePaceView.setChartPlotAreaRect(chart.getPlotAreaRect());
                NumberRange dataRange = (NumberRange) xAxis.getDataRange();
                this.mAveragePaceView.setXPixelValue(xAxis.getPixelValueForUserValue(dataRange.getMinimum()));
                this.mAveragePaceView.setYPixelValue(this.mYAxis.getPixelValueForUserValue(Double.valueOf(dataProperties.getAverageValue())));
            }
            double paceTopY = this.mYAxis.getDataRange() != null ? ((NumberRange) this.mYAxis.getDataRange()).getMaximum().doubleValue() : dataProperties.getAverageValue();
            if (this.mPausePointsView != null) {
                this.mPausePointsView.clearLines();
                for (PointF d : dataProperties.getPausePoints()) {
                    this.mPausePointsView.addLine(new Line(xAxis.getPixelValueForUserValue(Double.valueOf(d.x)), this.mYAxis.getPixelValueForUserValue(Double.valueOf(paceTopY)), xAxis.getPixelValueForUserValue(Double.valueOf(d.x)), this.mYAxis.getPixelValueForUserValue(Double.valueOf(d.y))));
                }
            }
        }
    }
}

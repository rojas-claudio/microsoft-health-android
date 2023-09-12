package com.shinobicontrols.kcompanionapp.charts;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.FrameLayout;
import com.microsoft.kapp.R;
import com.shinobicontrols.charts.Axis;
import com.shinobicontrols.charts.DataAdapter;
import com.shinobicontrols.charts.LineSeries;
import com.shinobicontrols.charts.LineSeriesStyle;
import com.shinobicontrols.charts.PointStyle;
import com.shinobicontrols.charts.Series;
import com.shinobicontrols.charts.SeriesStyle;
import com.shinobicontrols.charts.ShinobiChart;
import com.shinobicontrols.charts.TickMark;
import com.shinobicontrols.kcompanionapp.charts.ViewDataHelper.HRPeakChartViewDataHelper;
import com.shinobicontrols.kcompanionapp.charts.internal.AxisMapping;
import com.shinobicontrols.kcompanionapp.charts.internal.IconFactory;
import com.shinobicontrols.kcompanionapp.charts.internal.RangeAndFrequencySettingStrategy;
import com.shinobicontrols.kcompanionapp.charts.internal.SeriesCreator;
import com.shinobicontrols.kcompanionapp.charts.internal.StyledNumberAxis;
import com.shinobicontrols.kcompanionapp.charts.internal.TickMarkDrawingStrategy;
import com.shinobicontrols.kcompanionapp.charts.internal.TypedValueGetter;
import com.shinobicontrols.kcompanionapp.properties.ChartConfig;
import com.shinobicontrols.kcompanionapp.properties.ChartDataProperties;
import com.shinobicontrols.kcompanionapp.providers.YAxisStrategyProvider;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes.dex */
public class HeartRatePeakDisplayChart extends BaseChart {
    private final String ICON_HEART_RATE;
    private List<Series<?>> mAllLineSeries;
    private HRPeakChartViewDataHelper mDataProperties;
    private RangeAndFrequencySettingStrategy mHeartRateRangeAndFrequencySettingStrategy;
    private View mIcon;
    private double mMaxValue;
    private double mMinValue;
    private LineSeries mPointSeries;
    private Axis<Double, Double> mYAxis;

    public HeartRatePeakDisplayChart(ChartConfig config, YAxisStrategyProvider yAxisStrategy, ChartDataProperties chartProperties) {
        this(config, yAxisStrategy, chartProperties, config.getContext().getString(R.string.header_max_hr));
    }

    public HeartRatePeakDisplayChart(ChartConfig config, YAxisStrategyProvider yAxisStrategy, ChartDataProperties chartProperties, String chartTitle) {
        super(config, yAxisStrategy, chartProperties, chartTitle);
        this.ICON_HEART_RATE = IconFactory.HEART_RATE;
        this.mDataProperties = (HRPeakChartViewDataHelper) chartProperties;
    }

    @Override // com.shinobicontrols.kcompanionapp.charts.IChartView
    public Axis<Double, Double> getYAxis() {
        TypedValueGetter valueGetter = new TypedValueGetter(getContext().getResources());
        StyledNumberAxis axis = new StyledNumberAxis(getContext(), this.mThemeCache);
        int maxHeartRateForUser = getMaxHeartRate();
        if (maxHeartRateForUser == 0) {
            maxHeartRateForUser = ((int) valueGetter.getFloat(R.id.shinobicharts_run_heart_rate_max)) - getAge();
        }
        axis.setLabelFormat(new DecimalFormat(getContext().getResources().getString(R.string.shinobicharts_workout_heart_rate_label_format)));
        axis.setTickMarkClippingModeLow(TickMark.ClippingMode.NEITHER_PERSIST);
        axis.setTickMarkDrawingStrategy(new TickMarkDrawingStrategy.YAxisLableUnderTickLineTickMarkDrawingStrategy());
        this.mMinValue = valueGetter.getFloat(R.id.shinobicharts_heart_rate_min_proportion) * maxHeartRateForUser;
        this.mMaxValue = valueGetter.getFloat(R.id.shinobicharts_heart_rate_chart_max_zone_proportion) * maxHeartRateForUser;
        int numberOfIntervals = getContext().getResources().getInteger(R.integer.shinobicharts_workout_heart_rate_number_of_intervals);
        int toTheNearest = getContext().getResources().getInteger(R.integer.shinobicharts_workout_heart_rate_round_to_nearest_value);
        this.mHeartRateRangeAndFrequencySettingStrategy = new RangeAndFrequencySettingStrategy.FixedNumberOfIntervalsRangeAndFrequencySettingStrategy(axis, this.mMinValue, this.mMaxValue, numberOfIntervals, toTheNearest, null);
        this.mHeartRateRangeAndFrequencySettingStrategy.setRange();
        this.mYAxis = axis;
        return this.mYAxis;
    }

    @Override // com.shinobicontrols.kcompanionapp.charts.IChartView
    public View createIcon() {
        this.mIcon = IconFactory.create(IconFactory.HEART_RATE, getContext(), this.mThemeCache);
        return this.mIcon;
    }

    @Override // com.shinobicontrols.kcompanionapp.charts.IChartView
    public void createSeries() {
        int seriesCount = this.mDataProperties.getTotalLineSeries();
        Resources resources = getContext().getResources();
        int color = resources.getColor(R.color.shinobicharts_red);
        this.mAllLineSeries = new ArrayList();
        for (int i = 0; i < seriesCount; i++) {
            LineSeries series = (LineSeries) SeriesCreator.LINE.create(resources, this.mThemeCache);
            LineSeriesStyle style = (LineSeriesStyle) series.getStyle();
            style.setFillStyle(SeriesStyle.FillStyle.NONE);
            style.setLineWidth(1.0f);
            LineSeriesStyle selectedStyle = (LineSeriesStyle) series.getSelectedStyle();
            selectedStyle.setFillStyle(SeriesStyle.FillStyle.NONE);
            selectedStyle.setLineColor(color);
            selectedStyle.setLineWidth(1.0f);
            this.mAllLineSeries.add(series);
        }
        this.mPointSeries = (LineSeries) SeriesCreator.POINT.create(getContext().getResources(), this.mThemeCache);
        PointStyle pointStyle = ((LineSeriesStyle) this.mPointSeries.getSelectedStyle()).getPointStyle();
        pointStyle.setInnerColor(color);
        pointStyle.setColor(color);
        pointStyle.setInnerRadius(1.0f);
        pointStyle.setRadius(2.0f);
    }

    @Override // com.shinobicontrols.kcompanionapp.charts.IChartView
    public void createAnnotations() {
    }

    @Override // com.shinobicontrols.kcompanionapp.charts.IChartView
    public AxisMapping getAxisMapping() {
        List<Series<?>> seriesList = new ArrayList<>();
        seriesList.addAll(this.mAllLineSeries);
        seriesList.add(this.mPointSeries);
        Drawable legendSign = getContext().getResources().getDrawable(R.drawable.circle_fill_red);
        AxisMapping mapping = new AxisMapping(this.mIcon, getChartTitle(), seriesList, this.mYAxis, new ArrayList(), legendSign);
        mapping.setAxisWidth(getContext().getResources().getDimensionPixelSize(R.dimen.shinobicharts_axis_width_small));
        return mapping;
    }

    @Override // com.shinobicontrols.kcompanionapp.charts.IChartView
    public void reload(Axis xAxis) {
        if (this.mDataProperties.hasDataToPlot()) {
            List<DataAdapter<Integer, Integer>> lineSeriesDataAdapter = this.mDataProperties.getAllSeriesData();
            DataAdapter<Integer, Integer> pointDataAdapter = this.mDataProperties.getPointSeriesData();
            for (int i = 0; i < this.mAllLineSeries.size(); i++) {
                this.mAllLineSeries.get(i).setDataAdapter(lineSeriesDataAdapter.get(i));
            }
            this.mPointSeries.setDataAdapter(pointDataAdapter);
            this.mHeartRateRangeAndFrequencySettingStrategy = this.mYAxisStrategy.getRangeAndFrequencyStrategy(getContext(), this.mYAxis, this.mMaxValue, this.mMinValue, this.mDataProperties);
            this.mHeartRateRangeAndFrequencySettingStrategy.setRangeAndFrequency();
        }
    }

    @Override // com.shinobicontrols.kcompanionapp.charts.IChartView
    public void onInternalLayout(ShinobiChart chart, Axis<?, ?> xAxis) {
    }

    @Override // com.shinobicontrols.kcompanionapp.charts.IChartView
    public void createAdditionalViews(FrameLayout chartHolder) {
    }
}

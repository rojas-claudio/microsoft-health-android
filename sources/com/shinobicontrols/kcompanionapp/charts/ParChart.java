package com.shinobicontrols.kcompanionapp.charts;

import android.content.res.Resources;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import com.microsoft.kapp.R;
import com.microsoft.kapp.utils.Constants;
import com.shinobicontrols.charts.Axis;
import com.shinobicontrols.charts.ColumnSeries;
import com.shinobicontrols.charts.ColumnSeriesStyle;
import com.shinobicontrols.charts.DataAdapter;
import com.shinobicontrols.charts.Series;
import com.shinobicontrols.charts.ShinobiChart;
import com.shinobicontrols.charts.TickMark;
import com.shinobicontrols.kcompanionapp.charts.internal.AxisMapping;
import com.shinobicontrols.kcompanionapp.charts.internal.AxisZone;
import com.shinobicontrols.kcompanionapp.charts.internal.AxisZoneCollection;
import com.shinobicontrols.kcompanionapp.charts.internal.IconFactory;
import com.shinobicontrols.kcompanionapp.charts.internal.RangeAndFrequencySettingStrategy;
import com.shinobicontrols.kcompanionapp.charts.internal.SeriesCreator;
import com.shinobicontrols.kcompanionapp.charts.internal.StyledNumberAxis;
import com.shinobicontrols.kcompanionapp.charts.internal.TickLabelUpdateStrategy;
import com.shinobicontrols.kcompanionapp.charts.internal.TickMarkDrawingStrategy;
import com.shinobicontrols.kcompanionapp.charts.internal.TypedValueGetter;
import com.shinobicontrols.kcompanionapp.charts.internal.ValueHighlightView;
import com.shinobicontrols.kcompanionapp.properties.ChartConfig;
import com.shinobicontrols.kcompanionapp.properties.ChartDataProperties;
import com.shinobicontrols.kcompanionapp.providers.YAxisStrategyProvider;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes.dex */
public class ParChart extends BaseChart {
    private View mIcon;
    private ValueHighlightView mParHighlightView;
    private List<Series<?>> mSeries;
    private Axis<Double, Double> mYAxis;
    private RangeAndFrequencySettingStrategy mYAxisRangeAndFrequencySettingStrategy;

    public ParChart(ChartConfig config, YAxisStrategyProvider yAxisStrategyProvider, ChartDataProperties dataProperties, String chartTitle) {
        super(config, yAxisStrategyProvider, dataProperties, chartTitle);
    }

    @Override // com.shinobicontrols.kcompanionapp.charts.BaseChart, com.shinobicontrols.kcompanionapp.charts.IChartView
    public void onChartViewCreated(ShinobiChart chart) {
        chart.getStyle().setBackgroundColor(0);
        chart.getStyle().setCanvasBackgroundColor(0);
        chart.getStyle().setPlotAreaBackgroundColor(0);
    }

    @Override // com.shinobicontrols.kcompanionapp.charts.IChartView
    public View createIcon() {
        this.mIcon = IconFactory.create(IconFactory.PAR, getContext(), this.mThemeCache);
        return this.mIcon;
    }

    @Override // com.shinobicontrols.kcompanionapp.charts.IChartView
    public void createSeries() {
        Resources res = getContext().getResources();
        ColumnSeries albatrossSeries = (ColumnSeries) SeriesCreator.SOLID_COLOR_COLUMN.create(res, this.mThemeCache);
        ((ColumnSeriesStyle) albatrossSeries.getSelectedStyle()).setAreaColorBelowBaseline(res.getColor(R.color.BelowParDarkColor));
        ((ColumnSeriesStyle) albatrossSeries.getSelectedStyle()).setAreaColor(res.getColor(R.color.BelowParDarkColor));
        ColumnSeries eagleSeries = (ColumnSeries) SeriesCreator.SOLID_COLOR_COLUMN.create(res, this.mThemeCache);
        ((ColumnSeriesStyle) eagleSeries.getSelectedStyle()).setAreaColorBelowBaseline(res.getColor(R.color.BelowParMediumColor));
        ((ColumnSeriesStyle) eagleSeries.getSelectedStyle()).setAreaColor(res.getColor(R.color.BelowParMediumColor));
        ColumnSeries birdieSeries = (ColumnSeries) SeriesCreator.SOLID_COLOR_COLUMN.create(res, this.mThemeCache);
        ((ColumnSeriesStyle) birdieSeries.getSelectedStyle()).setAreaColorBelowBaseline(res.getColor(R.color.BelowParLightColor));
        ((ColumnSeriesStyle) birdieSeries.getSelectedStyle()).setAreaColor(res.getColor(R.color.BelowParLightColor));
        ColumnSeries parSeries = (ColumnSeries) SeriesCreator.SOLID_COLOR_COLUMN.create(res, this.mThemeCache);
        ((ColumnSeriesStyle) parSeries.getSelectedStyle()).setAreaColorBelowBaseline(res.getColor(R.color.ParColor));
        ((ColumnSeriesStyle) parSeries.getSelectedStyle()).setAreaColor(res.getColor(R.color.ParColor));
        ColumnSeries bogeySeries = (ColumnSeries) SeriesCreator.SOLID_COLOR_COLUMN.create(res, this.mThemeCache);
        ((ColumnSeriesStyle) bogeySeries.getSelectedStyle()).setAreaColorBelowBaseline(res.getColor(R.color.AboveParLightColor));
        ((ColumnSeriesStyle) bogeySeries.getSelectedStyle()).setAreaColor(res.getColor(R.color.AboveParLightColor));
        ColumnSeries doubleBogeySeries = (ColumnSeries) SeriesCreator.SOLID_COLOR_COLUMN.create(res, this.mThemeCache);
        ((ColumnSeriesStyle) doubleBogeySeries.getSelectedStyle()).setAreaColorBelowBaseline(res.getColor(R.color.AboveParMediumColor));
        ((ColumnSeriesStyle) doubleBogeySeries.getSelectedStyle()).setAreaColor(res.getColor(R.color.AboveParMediumColor));
        ColumnSeries tripleBogeySeries = (ColumnSeries) SeriesCreator.SOLID_COLOR_COLUMN.create(res, this.mThemeCache);
        ((ColumnSeriesStyle) tripleBogeySeries.getSelectedStyle()).setAreaColorBelowBaseline(res.getColor(R.color.AboveParDarkColor));
        ((ColumnSeriesStyle) tripleBogeySeries.getSelectedStyle()).setAreaColor(res.getColor(R.color.AboveParDarkColor));
        ColumnSeries belowBaslineOffset = (ColumnSeries) SeriesCreator.INVISIBLE_COLUMN.create(res, this.mThemeCache);
        ColumnSeries aboveBaselineOffset = (ColumnSeries) SeriesCreator.INVISIBLE_COLUMN.create(res, this.mThemeCache);
        ColumnSeries baselineOffset = (ColumnSeries) SeriesCreator.INVISIBLE_COLUMN.create(res, this.mThemeCache);
        ColumnSeries skippedSeries = (ColumnSeries) SeriesCreator.INVISIBLE_COLUMN.create(res, this.mThemeCache);
        this.mSeries = new ArrayList();
        this.mSeries.add(baselineOffset);
        this.mSeries.add(belowBaslineOffset);
        this.mSeries.add(aboveBaselineOffset);
        this.mSeries.add(albatrossSeries);
        this.mSeries.add(eagleSeries);
        this.mSeries.add(birdieSeries);
        this.mSeries.add(parSeries);
        this.mSeries.add(bogeySeries);
        this.mSeries.add(doubleBogeySeries);
        this.mSeries.add(tripleBogeySeries);
        this.mSeries.add(skippedSeries);
    }

    @Override // com.shinobicontrols.kcompanionapp.charts.IChartView
    public void createAnnotations() {
    }

    @Override // com.shinobicontrols.kcompanionapp.charts.IChartView
    public AxisMapping getAxisMapping() {
        List<View> viewsToShowWhenSelected = new ArrayList<>();
        viewsToShowWhenSelected.add(this.mParHighlightView);
        AxisMapping mapping = new AxisMapping(this.mIcon, this.mSeries, this.mYAxis, viewsToShowWhenSelected);
        mapping.setAxisWidth(getContext().getResources().getDimensionPixelSize(R.dimen.shinobicharts_axis_width_small));
        return mapping;
    }

    @Override // com.shinobicontrols.kcompanionapp.charts.IChartView
    public Axis<Double, Double> getYAxis() {
        TypedValueGetter valueGetter = new TypedValueGetter(getContext().getResources());
        StyledNumberAxis axis = new StyledNumberAxis(getContext(), this.mThemeCache);
        AxisZoneCollection mAxisZoneCollection = new AxisZoneCollection();
        axis.setLabelFormat(new DecimalFormat(getContext().getResources().getString(R.string.shinobicharts_workout_heart_rate_label_format)));
        mAxisZoneCollection.add(new AxisZone(-3.0d, getContext().getString(R.string.golf_chart_under_3), false, "", ""));
        mAxisZoneCollection.add(new AxisZone(-2.0d, getContext().getString(R.string.golf_chart_under_2), false, "", ""));
        mAxisZoneCollection.add(new AxisZone(-1.0d, getContext().getString(R.string.golf_chart_under_1), false, "", ""));
        mAxisZoneCollection.add(new AxisZone(Constants.SPLITS_ACCURACY, getContext().getString(R.string.golf_chart_par), false, "", ""));
        mAxisZoneCollection.add(new AxisZone(1.0d, getContext().getString(R.string.golf_chart_over_1), false, "", ""));
        mAxisZoneCollection.add(new AxisZone(2.0d, getContext().getString(R.string.golf_chart_over_2), false, "", ""));
        mAxisZoneCollection.add(new AxisZone(3.0d, getContext().getString(R.string.golf_chart_over_3), false, "", ""));
        axis.setTickMarkDrawingStrategy(new TickMarkDrawingStrategy.YAxisZonesMidValueIndendTickMarkDrawingStrategy(mAxisZoneCollection, getContext().getResources(), this.mThemeCache));
        axis.setTickMarkClippingModeLow(TickMark.ClippingMode.TICKS_AND_LABELS_PERSIST);
        axis.setTickLabelUpdateStrategy(new TickLabelUpdateStrategy.NullTickLabelUpdateStrategy());
        double min = valueGetter.getFloat(R.id.shinobicharts_golf_strokes_min);
        double max = valueGetter.getFloat(R.id.shinobicharts_golf_strokes_max);
        this.mYAxisRangeAndFrequencySettingStrategy = new RangeAndFrequencySettingStrategy.DefaultRangeAndFrequencySettingStrategy(axis, Double.valueOf(min), Double.valueOf(max), Double.valueOf(valueGetter.getFloat(R.id.shinobicharts_golf_tick_frequency)));
        this.mYAxisRangeAndFrequencySettingStrategy.setRangeAndFrequency();
        this.mYAxis = axis;
        return this.mYAxis;
    }

    @Override // com.shinobicontrols.kcompanionapp.charts.IChartView
    public void reload(Axis xAxis) {
        List<DataAdapter<?, ?>> data = getChartDataProperties().getSeriesData();
        if (data.size() == this.mSeries.size()) {
            for (int i = 0; i < data.size(); i++) {
                this.mSeries.get(i).setDataAdapter(data.get(i));
            }
        }
    }

    @Override // com.shinobicontrols.kcompanionapp.charts.IChartView
    public void createAdditionalViews(FrameLayout chartHolder) {
        this.mParHighlightView = new ValueHighlightView(getContext(), this.mThemeCache);
        this.mParHighlightView.setVisibility(8);
        chartHolder.addView(this.mParHighlightView, 0, new ViewGroup.LayoutParams(-1, -1));
    }

    @Override // com.shinobicontrols.kcompanionapp.charts.IChartView
    public void onInternalLayout(ShinobiChart chart, Axis<?, ?> xAxis) {
        TypedValueGetter getter = new TypedValueGetter(getContext().getResources());
        float high = this.mYAxis.getPixelValueForUserValue(Double.valueOf(getter.getFloat(R.id.shinobicharts_golf_par_zone_max)));
        float low = this.mYAxis.getPixelValueForUserValue(Double.valueOf(getter.getFloat(R.id.shinobicharts_golf_par_zone_min)));
        this.mParHighlightView.setPixelYValues(high, low);
    }
}

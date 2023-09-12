package com.shinobicontrols.kcompanionapp.charts;

import android.graphics.PointF;
import android.view.View;
import android.widget.FrameLayout;
import com.microsoft.kapp.R;
import com.microsoft.kapp.utils.Constants;
import com.shinobicontrols.charts.AnnotationsManager;
import com.shinobicontrols.charts.Axis;
import com.shinobicontrols.charts.NumberRange;
import com.shinobicontrols.charts.Series;
import com.shinobicontrols.charts.ShinobiChart;
import com.shinobicontrols.charts.TickMark;
import com.shinobicontrols.kcompanionapp.charts.internal.AxisMapping;
import com.shinobicontrols.kcompanionapp.charts.internal.AxisZoneCollection;
import com.shinobicontrols.kcompanionapp.charts.internal.FlagView;
import com.shinobicontrols.kcompanionapp.charts.internal.IconFactory;
import com.shinobicontrols.kcompanionapp.charts.internal.Line;
import com.shinobicontrols.kcompanionapp.charts.internal.PauseLinesView;
import com.shinobicontrols.kcompanionapp.charts.internal.QuadrantStrategiesSetter;
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
public class HeartRateChart extends BaseChart {
    private final String ICON_HEART_RATE;
    private AxisZoneCollection.HeartRateAxisZoneCollection mAxisZoneCollection;
    private PauseLinesView mHeartRatePausePointsView;
    private RangeAndFrequencySettingStrategy mHeartRateRangeAndFrequencySettingStrategy;
    private View mIcon;
    private double mMaxValue;
    private double mMinValue;
    private FlagView mPeakHeartRateFlag;
    private Series<?> mSeries;
    private Axis<Double, Double> mYAxis;

    public HeartRateChart(ChartConfig config, YAxisStrategyProvider yAxisStrategy, ChartDataProperties chartProperties) {
        this(config, yAxisStrategy, chartProperties, "");
    }

    public HeartRateChart(ChartConfig config, YAxisStrategyProvider yAxisStrategy, ChartDataProperties chartProperties, String chartTitle) {
        super(config, yAxisStrategy, chartProperties, chartTitle);
        this.ICON_HEART_RATE = IconFactory.HEART_RATE;
    }

    @Override // com.shinobicontrols.kcompanionapp.charts.IChartView
    public Axis<Double, Double> getYAxis() {
        TypedValueGetter valueGetter = new TypedValueGetter(getContext().getResources());
        StyledNumberAxis axis = new StyledNumberAxis(getContext(), this.mThemeCache);
        this.mAxisZoneCollection = new AxisZoneCollection.HeartRateAxisZoneCollection(getContext().getResources(), getMaxHeartRate(), getAge());
        int maxHeartRateForUser = getMaxHeartRate();
        if (maxHeartRateForUser == 0) {
            maxHeartRateForUser = ((int) valueGetter.getFloat(R.id.shinobicharts_run_heart_rate_max)) - getAge();
        }
        axis.setLabelFormat(new DecimalFormat(getContext().getResources().getString(R.string.shinobicharts_workout_heart_rate_label_format)));
        axis.setTickMarkClippingModeLow(TickMark.ClippingMode.NEITHER_PERSIST);
        axis.setTickMarkDrawingStrategy(new TickMarkDrawingStrategy.YAxisZonesTickMarkDrawingStrategy(this.mAxisZoneCollection, getContext().getResources(), this.mThemeCache));
        this.mMinValue = valueGetter.getFloat(R.id.shinobicharts_heart_rate_min_proportion) * maxHeartRateForUser;
        this.mMaxValue = valueGetter.getFloat(R.id.shinobicharts_heart_rate_chart_max_zone_proportion) * maxHeartRateForUser;
        int numberOfIntervals = getContext().getResources().getInteger(R.integer.shinobicharts_workout_heart_rate_number_of_intervals);
        int toTheNearest = getContext().getResources().getInteger(R.integer.shinobicharts_workout_heart_rate_round_to_nearest_value);
        this.mHeartRateRangeAndFrequencySettingStrategy = new RangeAndFrequencySettingStrategy.FixedNumberOfIntervalsRangeAndFrequencySettingStrategy(axis, this.mMinValue, this.mMaxValue, numberOfIntervals, toTheNearest, null);
        this.mHeartRateRangeAndFrequencySettingStrategy.setRange();
        axis.setMajorTickMarkValues(this.mAxisZoneCollection.getTickMarkValues());
        this.mYAxis = axis;
        return this.mYAxis;
    }

    @Override // com.shinobicontrols.kcompanionapp.charts.IChartView
    public void createSeries() {
        this.mSeries = SeriesCreator.FILLED_LINE.create(getContext().getResources(), this.mThemeCache);
    }

    @Override // com.shinobicontrols.kcompanionapp.charts.IChartView
    public View createIcon() {
        this.mIcon = IconFactory.create(IconFactory.HEART_RATE, getContext(), this.mThemeCache);
        return this.mIcon;
    }

    @Override // com.shinobicontrols.kcompanionapp.charts.IChartView
    public AxisMapping getAxisMapping() {
        List<Series<?>> seriesList = new ArrayList<>();
        seriesList.add(this.mSeries);
        List<View> viewsToShowWhenSelected = new ArrayList<>();
        viewsToShowWhenSelected.add(this.mPeakHeartRateFlag);
        viewsToShowWhenSelected.add(this.mHeartRatePausePointsView);
        return new AxisMapping(this.mIcon, getChartTitle(), seriesList, this.mYAxis, viewsToShowWhenSelected);
    }

    @Override // com.shinobicontrols.kcompanionapp.charts.IChartView
    public void reload(Axis xAxis) {
        this.mSeries.setDataAdapter(getChartDataProperties().getDefaultSeriesData());
        this.mHeartRateRangeAndFrequencySettingStrategy = this.mYAxisStrategy.getRangeAndFrequencyStrategy(getContext(), this.mYAxis, this.mMaxValue, this.mMinValue, getChartDataProperties());
        this.mHeartRateRangeAndFrequencySettingStrategy.setRange();
        if (getChartDataProperties().getMaxValue() > Constants.SPLITS_ACCURACY) {
            updateHeartRateFlags((int) getChartDataProperties().getMaxValue(), getChartDataProperties().getPositionAtMaxValue(), xAxis);
        }
    }

    @Override // com.shinobicontrols.kcompanionapp.charts.IChartView
    public void onInternalLayout(ShinobiChart chart, Axis xAxis) {
        if (getChartDataProperties() != null && this.mYAxis != null) {
            QuadrantStrategiesSetter.setStrategiesBasedOnData(this.mPeakHeartRateFlag, getChartDataProperties().getPositionAtMaxValue(), getChartDataProperties().getMaxValue(), xAxis, this.mYAxis);
            double heartRateTopY = this.mYAxis.getDataRange() != null ? ((NumberRange) this.mYAxis.getDataRange()).getMaximum().doubleValue() : getChartDataProperties().getMaxValue();
            if (this.mHeartRatePausePointsView != null) {
                this.mHeartRatePausePointsView.clearLines();
                for (PointF d : getChartDataProperties().getPausePoints()) {
                    this.mHeartRatePausePointsView.addLine(new Line(xAxis.getPixelValueForUserValue(Double.valueOf(d.x)), this.mYAxis.getPixelValueForUserValue(Double.valueOf(heartRateTopY)), xAxis.getPixelValueForUserValue(Double.valueOf(d.x)), this.mYAxis.getPixelValueForUserValue(Double.valueOf(d.y))));
                }
            }
        }
    }

    private void updateHeartRateFlags(int peakHeartRate, double positionAtPeakHeartRate, Axis xAxis) {
        AnnotationsManager annotationsManager = this.mYAxis.getChart().getAnnotationsManager();
        String flagValue = getContext().getResources().getString(R.string.shinobicharts_max_heart_rate_flag_label_format, Integer.valueOf(peakHeartRate));
        this.mPeakHeartRateFlag.setValue(flagValue);
        annotationsManager.addViewAnnotation(this.mPeakHeartRateFlag, Double.valueOf(positionAtPeakHeartRate), Integer.valueOf(peakHeartRate), xAxis, this.mYAxis);
    }

    @Override // com.shinobicontrols.kcompanionapp.charts.IChartView
    public void createAnnotations() {
        this.mPeakHeartRateFlag = super.createFlagView(IconFactory.HEART_RATE);
    }

    @Override // com.shinobicontrols.kcompanionapp.charts.IChartView
    public void createAdditionalViews(FrameLayout chartHolder) {
        this.mHeartRatePausePointsView = super.createPauseLineView(chartHolder);
    }
}

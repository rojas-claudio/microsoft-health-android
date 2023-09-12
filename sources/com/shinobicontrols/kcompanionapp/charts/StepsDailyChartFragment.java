package com.shinobicontrols.kcompanionapp.charts;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.microsoft.kapp.R;
import com.microsoft.kapp.utils.Constants;
import com.microsoft.krestsdk.models.UserActivity;
import com.shinobicontrols.charts.AnnotationsManager;
import com.shinobicontrols.charts.Axis;
import com.shinobicontrols.charts.ChartView;
import com.shinobicontrols.charts.DataAdapter;
import com.shinobicontrols.charts.DataPoint;
import com.shinobicontrols.charts.NumberRange;
import com.shinobicontrols.charts.Series;
import com.shinobicontrols.charts.ShinobiChart;
import com.shinobicontrols.charts.SimpleDataAdapter;
import com.shinobicontrols.charts.TickMark;
import com.shinobicontrols.charts.TickStyle;
import com.shinobicontrols.kcompanionapp.charts.internal.AxisMapping;
import com.shinobicontrols.kcompanionapp.charts.internal.BaseChartFragment;
import com.shinobicontrols.kcompanionapp.charts.internal.ChartManager;
import com.shinobicontrols.kcompanionapp.charts.internal.ChartThemeCache;
import com.shinobicontrols.kcompanionapp.charts.internal.DailyGoal;
import com.shinobicontrols.kcompanionapp.charts.internal.FlagView;
import com.shinobicontrols.kcompanionapp.charts.internal.FlagViewCreator;
import com.shinobicontrols.kcompanionapp.charts.internal.HasDailyGoal;
import com.shinobicontrols.kcompanionapp.charts.internal.HasHighActivityThreshold;
import com.shinobicontrols.kcompanionapp.charts.internal.IconFactory;
import com.shinobicontrols.kcompanionapp.charts.internal.PresetTickMarkLabelMaps;
import com.shinobicontrols.kcompanionapp.charts.internal.QuadrantStrategiesSetter;
import com.shinobicontrols.kcompanionapp.charts.internal.RangeAndFrequencySettingStrategy;
import com.shinobicontrols.kcompanionapp.charts.internal.SeriesCreator;
import com.shinobicontrols.kcompanionapp.charts.internal.StyledNumberAxis;
import com.shinobicontrols.kcompanionapp.charts.internal.TickLabelUpdateStrategy;
import com.shinobicontrols.kcompanionapp.charts.internal.TickMarkDrawingStrategy;
import com.shinobicontrols.kcompanionapp.charts.internal.TypedValueGetter;
import com.shinobicontrols.kcompanionapp.charts.internal.Utils;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes.dex */
public class StepsDailyChartFragment extends BaseChartFragment implements HasDailyGoal, HasHighActivityThreshold, ShinobiChart.OnInternalLayoutListener {
    private Series<?> aboveAverageSteps;
    private TickMarkDrawingStrategy.AddUnitToUpperMostTickMarkLabel addUnitToUpperMostTickMarkLabel;
    private Series<?> belowAverageSteps;
    private final DailyGoal dailyGoal = new DailyGoal();
    private DataProperties dataProperties;
    private Series<?> emptySteps;
    private View heartIcon;
    private Series<?> heartRate;
    private RangeAndFrequencySettingStrategy heartRateRangeAndFrequencySettingStrategy;
    private Axis<Double, Double> heartRateYAxis;
    private double highActivityThreshold;
    private Series<?> peakHeartRate;
    private FlagView peakHeartRateFlag;
    private View shoeIcon;
    private RangeAndFrequencySettingStrategy stepsRangeAndFrequencySettingStrategy;
    private Axis<Double, Double> stepsYAxis;
    private TypedValueGetter valueGetter;
    private Axis<Double, Double> xAxis;

    public static StepsDailyChartFragment newInstance(double dailyGoal, double highActivityThreshold) {
        StepsDailyChartFragment chartFragment = new StepsDailyChartFragment();
        chartFragment.setDailyGoal(dailyGoal);
        chartFragment.setHighActivityThreshold(highActivityThreshold);
        return chartFragment;
    }

    @Override // com.shinobicontrols.kcompanionapp.charts.internal.BaseChartFragment, android.support.v4.app.Fragment
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.dailyGoal.setResources(getResources());
        this.valueGetter = new TypedValueGetter(getResources());
    }

    @Override // com.shinobicontrols.kcompanionapp.charts.internal.BaseChartFragment, android.support.v4.app.Fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        if (savedInstanceState != null) {
            double dailyGoalValue = savedInstanceState.getDouble(HasDailyGoal.DAILY_GOAL);
            this.dailyGoal.setValue(dailyGoalValue);
            this.highActivityThreshold = savedInstanceState.getDouble(HasHighActivityThreshold.HIGH_ACTIVITY_THRESHOLD);
        }
        ViewGroup iconGroup = (ViewGroup) rootView.findViewById(R.id.icon_group);
        createAndAddIcons(iconGroup);
        ChartView chartView = (ChartView) rootView.findViewById(R.id.chart);
        ShinobiChart chart = chartView.getShinobiChart();
        this.xAxis = createAndSetupXAxis();
        chart.setXAxis(this.xAxis);
        this.stepsYAxis = createAndSetupStepsYAxis();
        chart.setYAxis(this.stepsYAxis);
        this.heartRateYAxis = createAndSetupHeartRateYAxis();
        chart.addYAxis(this.heartRateYAxis);
        createStepsSeries(chart);
        createHeartRateSeries(chart);
        createChartAnnotations();
        ChartManager chartManager = getChartManager();
        chartManager.addMapping(createStepsMapping(chart));
        chartManager.addMapping(createHeartRateMapping(chart));
        chart.setOnInternalLayoutListener(this);
        return rootView;
    }

    private Axis<Double, Double> createAndSetupXAxis() {
        StyledNumberAxis axis = new StyledNumberAxis(getActivity(), getChartThemeCache());
        axis.setPosition(Axis.Position.REVERSE);
        axis.getStyle().setInterSeriesPadding(this.valueGetter.getFloat(R.id.shinobicharts_steps_daily_interseries_padding));
        axis.getStyle().setInterSeriesSetPadding(this.valueGetter.getFloat(R.id.shinobicharts_steps_daily_interseries_set_padding));
        SparseArray<String> tickMarkLabelMap = PresetTickMarkLabelMaps.createHoursOfTheDayMap(getActivity());
        double anchor = this.valueGetter.getFloat(R.id.shinobicharts_fixed_number_of_columns_chart_anchor);
        axis.setTickLabelUpdateStrategy(new TickLabelUpdateStrategy.MapAnchoredTickValueToDefinedStringTickLabelUpdateStrategy(tickMarkLabelMap, anchor));
        double skipFrequency = this.valueGetter.getFloat(R.id.shinobicharts_steps_daily_skip_frequency);
        axis.setTickMarkDrawingStrategy(new TickMarkDrawingStrategy.MultiTickMarkDrawingStrategy(new TickMarkDrawingStrategy.FixedNumberOfColumnWithMappedLabelsTickMarkDrawingStrategy(anchor, skipFrequency), new TickMarkDrawingStrategy.LeftAlignXAxisLabelWithTickLineTickMarkDrawingStrategy()));
        axis.setMajorTickFrequency(Double.valueOf(this.valueGetter.getFloat(R.id.shinobicharts_fixed_number_of_columns_chart_tick_frequency)));
        return axis;
    }

    private StyledNumberAxis createAndSetupStepsYAxis() {
        StyledNumberAxis axis = new StyledNumberAxis(getActivity(), getChartThemeCache());
        axis.setTickMarkClippingModeLow(TickMark.ClippingMode.NEITHER_PERSIST);
        axis.setLabelFormat(new DecimalFormat(getResources().getString(R.string.shinobicharts_steps_daily_label_format)));
        axis.setTickMarkDrawingStrategy(new TickMarkDrawingStrategy.YAxisLableUnderTickLineTickMarkDrawingStrategy());
        double minValue = this.valueGetter.getFloat(R.id.shinobicharts_steps_daily_range_min);
        int numberOfIntervals = getResources().getInteger(R.integer.shinobicharts_steps_daily_number_of_intervals);
        int toTheNearest = getResources().getInteger(R.integer.shinobicharts_steps_daily_round_to_nearest_value);
        double maxValue = this.dailyGoal.getAxisMaxBasedOnDailyGoalForDailyChartsV1(null, numberOfIntervals, toTheNearest);
        this.stepsRangeAndFrequencySettingStrategy = new RangeAndFrequencySettingStrategy.FixedNumberOfIntervalsRangeAndFrequencySettingStrategy(axis, minValue, maxValue, numberOfIntervals, toTheNearest, null);
        this.stepsRangeAndFrequencySettingStrategy.setRangeAndFrequency();
        return axis;
    }

    private Axis<Double, Double> createAndSetupHeartRateYAxis() {
        StyledNumberAxis axis = new StyledNumberAxis(getActivity(), getChartThemeCache());
        axis.setLabelFormat(new DecimalFormat(getResources().getString(R.string.shinobicharts_steps_daily_heart_rate_label_format)));
        axis.setTickMarkClippingModeLow(TickMark.ClippingMode.NEITHER_PERSIST);
        this.addUnitToUpperMostTickMarkLabel = new TickMarkDrawingStrategy.AddUnitToUpperMostTickMarkLabel(getResources().getString(R.string.shinobicharts_bpm), getChartThemeCache());
        axis.setTickMarkDrawingStrategy(new TickMarkDrawingStrategy.MultiTickMarkDrawingStrategy(new TickMarkDrawingStrategy.YAxisLableUnderTickLineTickMarkDrawingStrategy(), this.addUnitToUpperMostTickMarkLabel));
        double minValue = this.valueGetter.getFloat(R.id.shinobicharts_steps_daily_heart_rate_min);
        double maxValue = this.valueGetter.getFloat(R.id.shinobicharts_steps_daily_heart_rate_default_max);
        axis.setDefaultRange(new NumberRange(Double.valueOf(minValue), Double.valueOf(maxValue)));
        axis.setMajorTickFrequency(Double.valueOf(this.valueGetter.getFloat(R.id.shinobicharts_steps_daily_heart_rate_default_frequency)));
        return axis;
    }

    private void createStepsSeries(ShinobiChart chart) {
        this.belowAverageSteps = SeriesCreator.BELOW_TARGET_COLUMN.create(getResources(), getChartThemeCache());
        this.aboveAverageSteps = SeriesCreator.ABOVE_TARGET_DAILY_COLUMN.create(getResources(), getChartThemeCache());
        this.emptySteps = SeriesCreator.EMPTY_DATA_COLUMN.create(getResources(), getChartThemeCache());
    }

    private void createHeartRateSeries(ShinobiChart chart) {
        this.heartRate = SeriesCreator.LINE.create(getResources(), getChartThemeCache());
        this.peakHeartRate = SeriesCreator.POINT.create(getResources(), getChartThemeCache());
    }

    private void createChartAnnotations() {
        ChartThemeCache themeCache = getChartThemeCache();
        this.peakHeartRateFlag = FlagViewCreator.createDataPointFlag(getActivity(), IconFactory.HEART_RATE, null, themeCache.getPeakDataPointFlagColor(), themeCache.getPeakDataPointFlagTextColor(), themeCache.getPathToDataPointFlagTypeface(), themeCache.getPathToGlyphTypeface());
        this.peakHeartRateFlag.setVisibility(8);
    }

    private AxisMapping createStepsMapping(ShinobiChart chart) {
        List<Series<?>> seriesList = new ArrayList<>();
        seriesList.add(this.belowAverageSteps);
        seriesList.add(this.aboveAverageSteps);
        seriesList.add(this.emptySteps);
        return new AxisMapping(this.shoeIcon, seriesList, this.stepsYAxis);
    }

    private AxisMapping createHeartRateMapping(ShinobiChart chart) {
        List<Series<?>> seriesList = new ArrayList<>();
        seriesList.add(this.heartRate);
        seriesList.add(this.peakHeartRate);
        List<View> viewsToShowWhenSelected = new ArrayList<>();
        viewsToShowWhenSelected.add(this.peakHeartRateFlag);
        return new AxisMapping(this.heartIcon, seriesList, this.heartRateYAxis, viewsToShowWhenSelected);
    }

    private void createAndAddIcons(ViewGroup iconGroup) {
        this.shoeIcon = IconFactory.create(IconFactory.STEPS, getActivity(), getChartThemeCache());
        this.heartIcon = IconFactory.create(IconFactory.HEART_RATE, getActivity(), getChartThemeCache());
        iconGroup.addView(this.shoeIcon);
        iconGroup.addView(this.heartIcon);
    }

    @Override // com.shinobicontrols.kcompanionapp.charts.internal.Provideable
    public void reload() {
        if (isReadyForReload()) {
            AnnotationsManager annotationsManager = this.heartRate.getChart().getAnnotationsManager();
            annotationsManager.removeAllAnnotations();
            DataAdapter<Integer, Integer> heartRateData = new SimpleDataAdapter<>();
            DataAdapter<Integer, Integer> peakHeartRateData = new SimpleDataAdapter<>();
            DataAdapter<Integer, Double> belowAverageStepsData = new SimpleDataAdapter<>();
            DataAdapter<Integer, Double> aboveAverageStepsData = new SimpleDataAdapter<>();
            DataAdapter<Integer, Double> emptyStepsData = new SimpleDataAdapter<>();
            UserActivity[] hourlyUserActivitiesForDay = getDataProvider().getHourlyUserActivitiesForDay();
            if (hourlyUserActivitiesForDay != null && hourlyUserActivitiesForDay.length != 0) {
                this.dataProperties = getDataProperties(hourlyUserActivitiesForDay);
                if (this.dataProperties != null) {
                    updateStepsAxisRangeAndFrequency(this.dataProperties);
                    updateHeartRateAxisRangeAndFrequency(this.dataProperties);
                    if (this.stepsYAxis != null && this.stepsYAxis.getDefaultRange() != null) {
                        double valueForEmptyData = Utils.getValueForEmptyData(getResources(), this.stepsYAxis.getDefaultRange().getSpan());
                        for (int i = 0; i < hourlyUserActivitiesForDay.length; i++) {
                            int averageHeartRate = hourlyUserActivitiesForDay[i].getAverageHeartRate();
                            heartRateData.add(new DataPoint<>(Integer.valueOf(i), Integer.valueOf(averageHeartRate)));
                            double stepCount = hourlyUserActivitiesForDay[i].getStepsTaken();
                            if (stepCount == Constants.SPLITS_ACCURACY) {
                                emptyStepsData.add(new DataPoint<>(Integer.valueOf(i), Double.valueOf(valueForEmptyData)));
                            } else {
                                DataPoint<Integer, Double> dataPoint = new DataPoint<>(Integer.valueOf(i), Double.valueOf(stepCount));
                                if (stepCount >= this.highActivityThreshold) {
                                    aboveAverageStepsData.add(dataPoint);
                                } else {
                                    belowAverageStepsData.add(dataPoint);
                                }
                            }
                        }
                    }
                    peakHeartRateData.add(new DataPoint<>(Integer.valueOf(this.dataProperties.maxAverageHeartRateIndex), Integer.valueOf(this.dataProperties.maxAverageHeartRate)));
                    this.heartRate.setDataAdapter(heartRateData);
                    this.peakHeartRate.setDataAdapter(peakHeartRateData);
                    this.belowAverageSteps.setDataAdapter(belowAverageStepsData);
                    this.aboveAverageSteps.setDataAdapter(aboveAverageStepsData);
                    this.emptySteps.setDataAdapter(emptyStepsData);
                    updatePeakHeartRateFlag(this.dataProperties.maxAverageHeartRate, this.dataProperties.maxAverageHeartRateIndex);
                }
            }
        }
    }

    private void updatePeakHeartRateFlag(int maxAverageHeartRate, int maxAverageHeartRateIndex) {
        AnnotationsManager annotationsManager = this.heartRate.getChart().getAnnotationsManager();
        this.peakHeartRateFlag.setValue(String.valueOf(maxAverageHeartRate));
        annotationsManager.addViewAnnotation(this.peakHeartRateFlag, Integer.valueOf(maxAverageHeartRateIndex), Integer.valueOf(maxAverageHeartRate), this.xAxis, this.heartRateYAxis);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class DataProperties {
        final int maxAverageHeartRate;
        final int maxAverageHeartRateIndex;
        final int maxStepCount;

        public DataProperties(int maxStepCount, int maxAverageHeartRate, int maxAverageHeartRateIndex) {
            this.maxStepCount = maxStepCount;
            this.maxAverageHeartRate = maxAverageHeartRate;
            this.maxAverageHeartRateIndex = maxAverageHeartRateIndex;
        }
    }

    private DataProperties getDataProperties(UserActivity[] hourlyUserActivitiesForDay) {
        int maxStepCount = 0;
        int maxAverageHeartRate = 0;
        int maxAverageHeartRateIndex = 0;
        for (int i = 0; i < hourlyUserActivitiesForDay.length; i++) {
            maxStepCount = Math.max(maxStepCount, hourlyUserActivitiesForDay[i].getStepsTaken());
            if (maxAverageHeartRate < hourlyUserActivitiesForDay[i].getAverageHeartRate()) {
                maxAverageHeartRate = hourlyUserActivitiesForDay[i].getAverageHeartRate();
                maxAverageHeartRateIndex = i;
            }
        }
        return new DataProperties(maxStepCount, maxAverageHeartRate, maxAverageHeartRateIndex);
    }

    private void updateStepsAxisRangeAndFrequency(DataProperties dataProperties1) {
        double minValue = this.valueGetter.getFloat(R.id.shinobicharts_steps_daily_range_min);
        int numberOfIntervals = getResources().getInteger(R.integer.shinobicharts_steps_daily_number_of_intervals);
        int toTheNearest = getResources().getInteger(R.integer.shinobicharts_steps_daily_round_to_nearest_value);
        double maxValue = this.dailyGoal.getAxisMaxBasedOnDailyGoalForDailyChartsV1(Integer.valueOf(dataProperties1.maxStepCount), numberOfIntervals, toTheNearest);
        this.stepsRangeAndFrequencySettingStrategy = new RangeAndFrequencySettingStrategy.FixedNumberOfIntervalsRangeAndFrequencySettingStrategy(this.stepsYAxis, minValue, maxValue, numberOfIntervals, toTheNearest, null);
        this.stepsRangeAndFrequencySettingStrategy.setRangeAndFrequency();
    }

    private void updateHeartRateAxisRangeAndFrequency(DataProperties dataProperties1) {
        double minValue = this.valueGetter.getFloat(R.id.shinobicharts_steps_daily_heart_rate_min);
        double maxValue = this.valueGetter.getFloat(R.id.shinobicharts_steps_daily_heart_rate_default_max);
        double additionToMax = this.valueGetter.getFloat(R.id.shinobicharts_calories_daily_heart_rate_addition_to_max);
        if (maxValue < dataProperties1.maxAverageHeartRate) {
            maxValue = dataProperties1.maxAverageHeartRate + additionToMax;
        }
        int numberOfIntervals = getResources().getInteger(R.integer.shinobicharts_steps_daily_heart_rate_number_of_intervals);
        int toTheNearest = getResources().getInteger(R.integer.shinobicharts_steps_daily_heart_rate_round_to_nearest_value);
        this.heartRateRangeAndFrequencySettingStrategy = new RangeAndFrequencySettingStrategy.FixedNumberOfIntervalsRangeAndRoundedFrequencySettingStrategy(this.heartRateYAxis, minValue, maxValue, numberOfIntervals, toTheNearest, null, false);
        this.heartRateRangeAndFrequencySettingStrategy.setRangeAndFrequency();
    }

    @Override // com.shinobicontrols.kcompanionapp.charts.internal.HasDailyGoal
    public double getDailyGoal() {
        return this.dailyGoal.getValue();
    }

    @Override // com.shinobicontrols.kcompanionapp.charts.internal.HasDailyGoal
    public void setDailyGoal(double dailyGoal) {
        this.dailyGoal.setValue(dailyGoal);
    }

    @Override // com.shinobicontrols.kcompanionapp.charts.internal.HasHighActivityThreshold
    public double getHighActivityThreshold() {
        return this.highActivityThreshold;
    }

    @Override // com.shinobicontrols.kcompanionapp.charts.internal.HasHighActivityThreshold
    public void setHighActivityThreshold(double highActivityThreshold) {
        this.highActivityThreshold = highActivityThreshold;
    }

    @Override // com.shinobicontrols.kcompanionapp.charts.internal.BaseChartFragment, android.support.v4.app.Fragment
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putDouble(HasDailyGoal.DAILY_GOAL, this.dailyGoal.getValue());
        outState.putDouble(HasHighActivityThreshold.HIGH_ACTIVITY_THRESHOLD, this.highActivityThreshold);
    }

    @Override // com.shinobicontrols.charts.ShinobiChart.OnInternalLayoutListener
    public void onInternalLayout(ShinobiChart chart) {
        if (this.dataProperties != null) {
            QuadrantStrategiesSetter.setStrategiesBasedOnData(this.peakHeartRateFlag, this.dataProperties.maxAverageHeartRateIndex, this.dataProperties.maxAverageHeartRate, this.xAxis, this.heartRateYAxis);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.shinobicontrols.kcompanionapp.charts.internal.BaseChartFragment
    public void applyTheme() {
        super.applyTheme();
        ChartThemeCache themeCache = getChartThemeCache();
        ColorStateList colorStateList = new ColorStateList(IconFactory.states, new int[]{themeCache.getSelectedIconColor(), themeCache.getUnselectedIconColor()});
        ((TextView) this.shoeIcon).setTextColor(colorStateList);
        ((TextView) this.heartIcon).setTextColor(colorStateList);
        this.peakHeartRateFlag.setFlagColor(themeCache.getPeakDataPointFlagColor());
        this.peakHeartRateFlag.setValueColor(themeCache.getPeakDataPointFlagTextColor());
        this.peakHeartRateFlag.setValueTypeface(Typeface.createFromAsset(getResources().getAssets(), themeCache.getPathToDataPointFlagTypeface()));
        this.peakHeartRateFlag.setSymbolColor(themeCache.getPeakDataPointFlagTextColor());
        this.peakHeartRateFlag.setSymbolTypeface(Typeface.createFromAsset(getResources().getAssets(), themeCache.getPathToGlyphTypeface()));
        TickStyle xAxisTickStyle = this.xAxis.getStyle().getTickStyle();
        xAxisTickStyle.setLineColor(themeCache.getAxisTickLineColor());
        xAxisTickStyle.setLabelColor(themeCache.getAxisTickLabelColor());
        TickStyle stepsAxisTickStyle = this.stepsYAxis.getStyle().getTickStyle();
        stepsAxisTickStyle.setLineColor(themeCache.getAxisTickLineColor());
        stepsAxisTickStyle.setLabelColor(themeCache.getAxisTickLabelColor());
        TickStyle heartRateAxisTickStyle = this.heartRateYAxis.getStyle().getTickStyle();
        heartRateAxisTickStyle.setLineColor(themeCache.getAxisTickLineColor());
        heartRateAxisTickStyle.setLabelColor(themeCache.getAxisTickLabelColor());
        SeriesCreator.BELOW_TARGET_COLUMN.applyTheme(this.belowAverageSteps, themeCache);
        SeriesCreator.ABOVE_TARGET_DAILY_COLUMN.applyTheme(this.aboveAverageSteps, themeCache);
        SeriesCreator.EMPTY_DATA_COLUMN.applyTheme(this.emptySteps, themeCache);
        SeriesCreator.LINE.applyTheme(this.heartRate, themeCache);
        SeriesCreator.POINT.applyTheme(this.peakHeartRate, themeCache);
        this.addUnitToUpperMostTickMarkLabel.updateWithTheme(themeCache);
        ChartView chartView = (ChartView) getView().findViewById(R.id.chart);
        chartView.getShinobiChart().redrawChart();
    }
}

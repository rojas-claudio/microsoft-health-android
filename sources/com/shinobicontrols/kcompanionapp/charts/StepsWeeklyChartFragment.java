package com.shinobicontrols.kcompanionapp.charts;

import android.app.Activity;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.microsoft.kapp.R;
import com.microsoft.kapp.utils.Constants;
import com.microsoft.krestsdk.models.UserDailySummary;
import com.shinobicontrols.charts.Axis;
import com.shinobicontrols.charts.ChartView;
import com.shinobicontrols.charts.DataAdapter;
import com.shinobicontrols.charts.DataPoint;
import com.shinobicontrols.charts.Series;
import com.shinobicontrols.charts.ShinobiChart;
import com.shinobicontrols.charts.SimpleDataAdapter;
import com.shinobicontrols.charts.TickMark;
import com.shinobicontrols.kcompanionapp.charts.internal.AxisMapping;
import com.shinobicontrols.kcompanionapp.charts.internal.BaseChartFragment;
import com.shinobicontrols.kcompanionapp.charts.internal.ChartManager;
import com.shinobicontrols.kcompanionapp.charts.internal.ChartThemeCache;
import com.shinobicontrols.kcompanionapp.charts.internal.HasWeeklyGoals;
import com.shinobicontrols.kcompanionapp.charts.internal.RangeAndFrequencySettingStrategy;
import com.shinobicontrols.kcompanionapp.charts.internal.SeriesCreator;
import com.shinobicontrols.kcompanionapp.charts.internal.StyledNumberAxis;
import com.shinobicontrols.kcompanionapp.charts.internal.TickLabelUpdateStrategy;
import com.shinobicontrols.kcompanionapp.charts.internal.TickMarkDrawingStrategy;
import com.shinobicontrols.kcompanionapp.charts.internal.TypedValueGetter;
import com.shinobicontrols.kcompanionapp.charts.internal.Utils;
import com.shinobicontrols.kcompanionapp.charts.internal.WeeklyGoals;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.joda.time.DateTime;
/* loaded from: classes.dex */
public class StepsWeeklyChartFragment extends BaseChartFragment implements HasWeeklyGoals {
    private Series<?> belowGoalSteps;
    private Series<?> emptySteps;
    private Series<?> goalMetSteps;
    private RangeAndFrequencySettingStrategy stepsRangeAndFrequencySettingStrategy;
    private Axis<Double, Double> stepsYAxis;
    private TypedValueGetter valueGetter;
    private Axis<Double, Double> xAxis;
    private final WeeklyGoals weeklyGoals = new WeeklyGoals();
    private final SparseArray<String> tickMarkLabelMap = new SparseArray<>();

    public static StepsWeeklyChartFragment newInstance(HashMap<DateTime, Double> goalValues) {
        StepsWeeklyChartFragment chartFragment = new StepsWeeklyChartFragment();
        chartFragment.setWeeklyGoals(goalValues);
        return chartFragment;
    }

    @Override // com.shinobicontrols.kcompanionapp.charts.internal.BaseChartFragment, android.support.v4.app.Fragment
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.valueGetter = new TypedValueGetter(getResources());
    }

    @Override // com.shinobicontrols.kcompanionapp.charts.internal.BaseChartFragment, android.support.v4.app.Fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        if (savedInstanceState != null) {
            HashMap<DateTime, Double> weeklyGoalValues = (HashMap) savedInstanceState.getSerializable("WeeklyGoals");
            this.weeklyGoals.setGoals(weeklyGoalValues);
        }
        ChartView chartView = (ChartView) rootView.findViewById(R.id.chart);
        ShinobiChart chart = chartView.getShinobiChart();
        this.xAxis = createAndSetupXAxis();
        chart.setXAxis(this.xAxis);
        this.stepsYAxis = createAndSetupStepsYAxis();
        chart.setYAxis(this.stepsYAxis);
        createStepsSeries(chart);
        ChartManager chartManager = getChartManager();
        chartManager.addMapping(createStepsMapping(chart));
        return rootView;
    }

    @Override // com.shinobicontrols.kcompanionapp.charts.internal.HasWeeklyGoals
    public WeeklyGoals getWeeklyGoals() {
        return this.weeklyGoals;
    }

    @Override // com.shinobicontrols.kcompanionapp.charts.internal.HasWeeklyGoals
    public void setWeeklyGoals(HashMap<DateTime, Double> weeklyGoals) {
        this.weeklyGoals.setGoals(weeklyGoals);
    }

    private Axis<Double, Double> createAndSetupXAxis() {
        StyledNumberAxis axis = new StyledNumberAxis(getActivity(), getChartThemeCache());
        axis.setPosition(Axis.Position.REVERSE);
        axis.getStyle().setInterSeriesPadding(this.valueGetter.getFloat(R.id.shinobicharts_steps_weekly_interseries_padding));
        axis.getStyle().setInterSeriesSetPadding(this.valueGetter.getFloat(R.id.shinobicharts_steps_weekly_interseries_set_padding));
        double anchor = this.valueGetter.getFloat(R.id.shinobicharts_fixed_number_of_columns_chart_anchor);
        double skipFrequency = this.valueGetter.getFloat(R.id.shinobicharts_steps_weekly_skip_frequency);
        axis.setTickLabelUpdateStrategy(new TickLabelUpdateStrategy.MapAnchoredTickValueToDefinedStringTickLabelUpdateStrategy(this.tickMarkLabelMap, anchor));
        axis.setTickMarkDrawingStrategy(new TickMarkDrawingStrategy.MultiTickMarkDrawingStrategy(new TickMarkDrawingStrategy.FixedNumberOfColumnWithMappedLabelsTickMarkDrawingStrategy(anchor, skipFrequency), new TickMarkDrawingStrategy.CenterAlignXAxisLabelWithTickLineTickMarkDrawingStrategy()));
        axis.setMajorTickFrequency(Double.valueOf(this.valueGetter.getFloat(R.id.shinobicharts_fixed_number_of_columns_chart_tick_frequency)));
        return axis;
    }

    private Axis<Double, Double> createAndSetupStepsYAxis() {
        StyledNumberAxis axis = new StyledNumberAxis(getActivity(), getChartThemeCache());
        axis.setTickMarkClippingModeLow(TickMark.ClippingMode.NEITHER_PERSIST);
        axis.setLabelFormat(new DecimalFormat(getResources().getString(R.string.shinobicharts_steps_weekly_label_format)));
        axis.setTickMarkDrawingStrategy(new TickMarkDrawingStrategy.YAxisLableUnderTickLineTickMarkDrawingStrategy());
        double minValue = this.valueGetter.getFloat(R.id.shinobicharts_steps_weekly_range_min);
        int numberOfIntervals = getResources().getInteger(R.integer.shinobicharts_steps_weekly_number_of_intervals);
        int toTheNearest = getResources().getInteger(R.integer.shinobicharts_steps_weekly_round_to_nearest_value);
        double maxValue = this.weeklyGoals.getAxisMaxBasedOnDailyGoalForWeeklyChartsV1(null, numberOfIntervals, toTheNearest);
        if (maxValue <= Constants.SPLITS_ACCURACY) {
            maxValue = this.valueGetter.getFloat(R.id.shinobicharts_steps_weekly_range_default_max);
        }
        this.stepsRangeAndFrequencySettingStrategy = new RangeAndFrequencySettingStrategy.FixedNumberOfIntervalsRangeAndFrequencySettingStrategy(axis, minValue, maxValue, numberOfIntervals, toTheNearest, null);
        this.stepsRangeAndFrequencySettingStrategy.setRangeAndFrequency();
        return axis;
    }

    private void createStepsSeries(ShinobiChart chart) {
        this.belowGoalSteps = SeriesCreator.BELOW_TARGET_COLUMN.create(getResources(), getChartThemeCache());
        this.goalMetSteps = SeriesCreator.ABOVE_TARGET_WEEKLY_COLUMN.create(getResources(), getChartThemeCache());
        this.emptySteps = SeriesCreator.EMPTY_DATA_COLUMN.create(getResources(), getChartThemeCache());
    }

    private AxisMapping createStepsMapping(ShinobiChart chart) {
        List<Series<?>> seriesList = new ArrayList<>();
        seriesList.add(this.belowGoalSteps);
        seriesList.add(this.goalMetSteps);
        seriesList.add(this.emptySteps);
        return new AxisMapping(null, seriesList, this.stepsYAxis);
    }

    @Override // com.shinobicontrols.kcompanionapp.charts.internal.Provideable
    public void reload() {
        if (isReadyForReload()) {
            DataAdapter<Integer, Double> belowGoalStepsData = new SimpleDataAdapter<>();
            DataAdapter<Integer, Double> goalMetStepsData = new SimpleDataAdapter<>();
            DataAdapter<Integer, Double> emptyStepsData = new SimpleDataAdapter<>();
            UserDailySummary[] dailySummariesForWeek = getDataProvider().getDailySummariesForWeek();
            if (dailySummariesForWeek != null && dailySummariesForWeek.length != 0) {
                updateStepsAxisRangeAndFrequency(dailySummariesForWeek);
                this.tickMarkLabelMap.clear();
                if (this.stepsYAxis != null && this.stepsYAxis.getDefaultRange() != null) {
                    double valueForEmptyData = Utils.getValueForEmptyData(getResources(), this.stepsYAxis.getDefaultRange().getSpan());
                    for (int i = 0; i < dailySummariesForWeek.length; i++) {
                        UserDailySummary dailySummary = dailySummariesForWeek[i];
                        if (dailySummary != null) {
                            DateTime dateTime = dailySummary.getTimeOfDay();
                            String label = getLabelForDate(dateTime);
                            this.tickMarkLabelMap.put(i + 1, label);
                            double stepCount = dailySummary.getStepsTaken();
                            if (stepCount == Constants.SPLITS_ACCURACY) {
                                emptyStepsData.add(new DataPoint<>(Integer.valueOf(i), Double.valueOf(valueForEmptyData)));
                            } else {
                                DataPoint<Integer, Double> dataPoint = new DataPoint<>(Integer.valueOf(i), Double.valueOf(stepCount));
                                if (this.weeklyGoals.getGoal(dateTime) > Constants.SPLITS_ACCURACY && stepCount >= this.weeklyGoals.getGoal(dateTime)) {
                                    goalMetStepsData.add(dataPoint);
                                } else {
                                    belowGoalStepsData.add(dataPoint);
                                }
                            }
                        }
                    }
                    padWithEmptyDataPointsToTheEndOfTheWeek(dailySummariesForWeek, emptyStepsData, valueForEmptyData);
                }
                this.belowGoalSteps.setDataAdapter(belowGoalStepsData);
                this.goalMetSteps.setDataAdapter(goalMetStepsData);
                this.emptySteps.setDataAdapter(emptyStepsData);
            }
        }
    }

    private void updateStepsAxisRangeAndFrequency(UserDailySummary[] dailySummariesForWeek) {
        double minValue = this.valueGetter.getFloat(R.id.shinobicharts_steps_weekly_range_min);
        double maxStepCount = getMaxStepCount(dailySummariesForWeek);
        int numberOfIntervals = getResources().getInteger(R.integer.shinobicharts_steps_weekly_number_of_intervals);
        int toTheNearest = getResources().getInteger(R.integer.shinobicharts_steps_weekly_round_to_nearest_value);
        double maxValue = this.weeklyGoals.getAxisMaxBasedOnDailyGoalForWeeklyChartsV1(Double.valueOf(maxStepCount), numberOfIntervals, toTheNearest);
        this.stepsRangeAndFrequencySettingStrategy = new RangeAndFrequencySettingStrategy.FixedNumberOfIntervalsRangeAndFrequencySettingStrategy(this.stepsYAxis, minValue, maxValue, numberOfIntervals, toTheNearest, null);
        this.stepsRangeAndFrequencySettingStrategy.setRangeAndFrequency();
    }

    private void padWithEmptyDataPointsToTheEndOfTheWeek(UserDailySummary[] dailySummariesForWeek, DataAdapter<Integer, Double> emptyStepsData, double valueForEmptyData) {
        DateTime lastDateTime;
        int expectedNumberOfDataPoints = getResources().getInteger(R.integer.shinobicharts_weekly_chart_expected_number_data_points);
        int indexForNextDataPoint = dailySummariesForWeek.length;
        if (indexForNextDataPoint < expectedNumberOfDataPoints) {
            int numberToAdd = expectedNumberOfDataPoints - indexForNextDataPoint;
            if (numberToAdd == expectedNumberOfDataPoints) {
                lastDateTime = new DateTime();
            } else {
                lastDateTime = dailySummariesForWeek[indexForNextDataPoint - 1].getTimeOfDay();
            }
            for (int i = 0; i < numberToAdd; i++) {
                DateTime nextDay = lastDateTime.plusDays(i + 1);
                String label = getLabelForDate(nextDay);
                this.tickMarkLabelMap.put(indexForNextDataPoint + 1, label);
                DataPoint<Integer, Double> dataPoint = new DataPoint<>(Integer.valueOf(indexForNextDataPoint), Double.valueOf(valueForEmptyData));
                emptyStepsData.add(dataPoint);
                indexForNextDataPoint++;
            }
        }
    }

    private int getMaxStepCount(UserDailySummary[] dailySummariesForWeek) {
        int maxStepCount = 0;
        for (UserDailySummary userDailySummary : dailySummariesForWeek) {
            maxStepCount = Math.max(maxStepCount, userDailySummary.getStepsTaken());
        }
        return maxStepCount;
    }

    @Override // com.shinobicontrols.kcompanionapp.charts.internal.BaseChartFragment, android.support.v4.app.Fragment
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("WeeklyGoals", this.weeklyGoals.getGoalValues());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.shinobicontrols.kcompanionapp.charts.internal.BaseChartFragment
    public void applyTheme() {
        super.applyTheme();
        ChartThemeCache themeCache = getChartThemeCache();
        SeriesCreator.BELOW_TARGET_COLUMN.applyTheme(this.belowGoalSteps, themeCache);
        SeriesCreator.ABOVE_TARGET_WEEKLY_COLUMN.applyTheme(this.goalMetSteps, themeCache);
        SeriesCreator.EMPTY_DATA_COLUMN.applyTheme(this.emptySteps, themeCache);
        ChartView chartView = (ChartView) getView().findViewById(R.id.chart);
        chartView.getShinobiChart().redrawChart();
    }
}

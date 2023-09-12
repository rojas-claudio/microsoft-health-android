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
public class CaloriesWeeklyChartFragment extends BaseChartFragment implements HasWeeklyGoals {
    private Series<?> belowGoalBurn;
    private RangeAndFrequencySettingStrategy caloriesRangeAndFrequencySettingStrategy;
    private Axis<Double, Double> caloriesYAxis;
    private Series<?> emptyBurn;
    private Series<?> goalMetBurn;
    private TypedValueGetter valueGetter;
    private Axis<Double, Double> xAxis;
    private final WeeklyGoals weeklyGoals = new WeeklyGoals();
    private final SparseArray<String> tickMarkLabelMap = new SparseArray<>();

    public static CaloriesWeeklyChartFragment newInstance(HashMap<DateTime, Double> goalValues) {
        CaloriesWeeklyChartFragment chartFragment = new CaloriesWeeklyChartFragment();
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
        this.caloriesYAxis = createAndSetupCaloriesYAxis();
        chart.setYAxis(this.caloriesYAxis);
        createCaloriesBurnedSeries(chart);
        ChartManager chartManager = getChartManager();
        chartManager.addMapping(createCaloriesBurnedMapping(chart));
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
        axis.getStyle().setInterSeriesPadding(this.valueGetter.getFloat(R.id.shinobicharts_calories_weekly_interseries_padding));
        axis.getStyle().setInterSeriesSetPadding(this.valueGetter.getFloat(R.id.shinobicharts_calories_weekly_interseries_set_padding));
        double anchor = this.valueGetter.getFloat(R.id.shinobicharts_fixed_number_of_columns_chart_anchor);
        double skipFrequency = this.valueGetter.getFloat(R.id.shinobicharts_calories_weekly_skip_frequency);
        axis.setTickLabelUpdateStrategy(new TickLabelUpdateStrategy.MapAnchoredTickValueToDefinedStringTickLabelUpdateStrategy(this.tickMarkLabelMap, anchor));
        axis.setTickMarkDrawingStrategy(new TickMarkDrawingStrategy.MultiTickMarkDrawingStrategy(new TickMarkDrawingStrategy.FixedNumberOfColumnWithMappedLabelsTickMarkDrawingStrategy(anchor, skipFrequency), new TickMarkDrawingStrategy.CenterAlignXAxisLabelWithTickLineTickMarkDrawingStrategy()));
        axis.setMajorTickFrequency(Double.valueOf(this.valueGetter.getFloat(R.id.shinobicharts_fixed_number_of_columns_chart_tick_frequency)));
        return axis;
    }

    private Axis<Double, Double> createAndSetupCaloriesYAxis() {
        StyledNumberAxis axis = new StyledNumberAxis(getActivity(), getChartThemeCache());
        axis.setTickMarkClippingModeLow(TickMark.ClippingMode.NEITHER_PERSIST);
        axis.setLabelFormat(new DecimalFormat(getResources().getString(R.string.shinobicharts_calories_weekly_label_format)));
        axis.setTickMarkDrawingStrategy(new TickMarkDrawingStrategy.YAxisLableUnderTickLineTickMarkDrawingStrategy());
        double minValue = this.valueGetter.getFloat(R.id.shinobicharts_calories_weekly_range_min);
        int numberOfIntervals = getResources().getInteger(R.integer.shinobicharts_calories_weekly_number_of_intervals);
        int toTheNearest = getResources().getInteger(R.integer.shinobicharts_calories_weekly_round_to_nearest_value);
        double maxValue = this.weeklyGoals.getAxisMaxBasedOnDailyGoalForWeeklyChartsV1(null, numberOfIntervals, toTheNearest);
        this.caloriesRangeAndFrequencySettingStrategy = new RangeAndFrequencySettingStrategy.FixedNumberOfIntervalsRangeAndFrequencySettingStrategy(axis, minValue, maxValue, numberOfIntervals, toTheNearest, null);
        this.caloriesRangeAndFrequencySettingStrategy.setRangeAndFrequency();
        return axis;
    }

    private void createCaloriesBurnedSeries(ShinobiChart chart) {
        this.belowGoalBurn = SeriesCreator.BELOW_TARGET_COLUMN.create(getResources(), getChartThemeCache());
        this.goalMetBurn = SeriesCreator.ABOVE_TARGET_WEEKLY_COLUMN.create(getResources(), getChartThemeCache());
        this.emptyBurn = SeriesCreator.EMPTY_DATA_COLUMN.create(getResources(), getChartThemeCache());
    }

    private AxisMapping createCaloriesBurnedMapping(ShinobiChart chart) {
        List<Series<?>> seriesList = new ArrayList<>();
        seriesList.add(this.belowGoalBurn);
        seriesList.add(this.goalMetBurn);
        seriesList.add(this.emptyBurn);
        return new AxisMapping(null, seriesList, this.caloriesYAxis);
    }

    @Override // com.shinobicontrols.kcompanionapp.charts.internal.Provideable
    public void reload() {
        if (isReadyForReload()) {
            DataAdapter<Integer, Double> belowGoalBurnData = new SimpleDataAdapter<>();
            DataAdapter<Integer, Double> goalMetBurnData = new SimpleDataAdapter<>();
            DataAdapter<Integer, Double> emptyBurnData = new SimpleDataAdapter<>();
            UserDailySummary[] dailySummariesForWeek = getDataProvider().getDailySummariesForWeek();
            if (dailySummariesForWeek != null && dailySummariesForWeek.length != 0) {
                updateCaloriesAxisRangeAndFrequency(dailySummariesForWeek);
                this.tickMarkLabelMap.clear();
                if (this.caloriesYAxis != null && this.caloriesYAxis.getDefaultRange() != null) {
                    double valueForEmptyData = Utils.getValueForEmptyData(getResources(), this.caloriesYAxis.getDefaultRange().getSpan());
                    for (int i = 0; i < dailySummariesForWeek.length; i++) {
                        UserDailySummary dailySummary = dailySummariesForWeek[i];
                        if (dailySummary != null) {
                            DateTime dateTime = dailySummary.getTimeOfDay();
                            if (dateTime != null) {
                                String label = getLabelForDate(dateTime);
                                this.tickMarkLabelMap.put(i + 1, label);
                            }
                            int caloriesBurned = dailySummary.getCaloriesBurned();
                            if (caloriesBurned == 0) {
                                emptyBurnData.add(new DataPoint<>(Integer.valueOf(i), Double.valueOf(valueForEmptyData)));
                            } else {
                                DataPoint<Integer, Double> dataPoint = new DataPoint<>(Integer.valueOf(i), Double.valueOf(caloriesBurned));
                                if (this.weeklyGoals.getGoal(dateTime) > Constants.SPLITS_ACCURACY && caloriesBurned >= this.weeklyGoals.getGoal(dateTime)) {
                                    goalMetBurnData.add(dataPoint);
                                } else {
                                    belowGoalBurnData.add(dataPoint);
                                }
                            }
                        }
                    }
                    padWithEmptyDataPointsToTheEndOfTheWeek(dailySummariesForWeek, emptyBurnData, valueForEmptyData);
                }
                this.belowGoalBurn.setDataAdapter(belowGoalBurnData);
                this.goalMetBurn.setDataAdapter(goalMetBurnData);
                this.emptyBurn.setDataAdapter(emptyBurnData);
            }
        }
    }

    private void updateCaloriesAxisRangeAndFrequency(UserDailySummary[] dailySummariesForWeek) {
        double minValue = this.valueGetter.getFloat(R.id.shinobicharts_calories_weekly_range_min);
        double maxCalorieBurn = getMaxCalorieBurn(dailySummariesForWeek);
        int numberOfIntervals = getResources().getInteger(R.integer.shinobicharts_calories_weekly_number_of_intervals);
        int toTheNearest = getResources().getInteger(R.integer.shinobicharts_calories_weekly_round_to_nearest_value);
        double maxValue = this.weeklyGoals.getAxisMaxBasedOnDailyGoalForWeeklyChartsV1(Double.valueOf(maxCalorieBurn), numberOfIntervals, toTheNearest);
        this.caloriesRangeAndFrequencySettingStrategy = new RangeAndFrequencySettingStrategy.FixedNumberOfIntervalsRangeAndFrequencySettingStrategy(this.caloriesYAxis, minValue, maxValue, numberOfIntervals, toTheNearest, null);
        this.caloriesRangeAndFrequencySettingStrategy.setRangeAndFrequency();
    }

    private void padWithEmptyDataPointsToTheEndOfTheWeek(UserDailySummary[] dailySummariesForWeek, DataAdapter<Integer, Double> emptyBurnData, double valueForEmptyData) {
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
                emptyBurnData.add(dataPoint);
                indexForNextDataPoint++;
            }
        }
    }

    private int getMaxCalorieBurn(UserDailySummary[] dailySummariesForWeek) {
        int maxCalorieBurn = 0;
        for (UserDailySummary userDailySummary : dailySummariesForWeek) {
            maxCalorieBurn = Math.max(maxCalorieBurn, userDailySummary.getCaloriesBurned());
        }
        return maxCalorieBurn;
    }

    @Override // com.shinobicontrols.kcompanionapp.charts.internal.BaseChartFragment, android.support.v4.app.Fragment
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("WeeklyGoals", this.weeklyGoals.getGoalValues());
    }
}

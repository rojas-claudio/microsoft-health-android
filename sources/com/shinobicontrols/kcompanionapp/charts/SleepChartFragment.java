package com.shinobicontrols.kcompanionapp.charts;

import android.app.Activity;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.microsoft.kapp.R;
import com.microsoft.kapp.comparators.UserActivityComparator;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.kapp.models.TimeSpan;
import com.microsoft.kapp.utils.Constants;
import com.microsoft.krestsdk.models.SequenceType;
import com.microsoft.krestsdk.models.SleepEvent;
import com.microsoft.krestsdk.models.SleepEventSequence;
import com.microsoft.krestsdk.models.SleepTypes;
import com.microsoft.krestsdk.models.UserActivity;
import com.shinobicontrols.charts.AnnotationsManager;
import com.shinobicontrols.charts.Axis;
import com.shinobicontrols.charts.ChartView;
import com.shinobicontrols.charts.DataAdapter;
import com.shinobicontrols.charts.DataPoint;
import com.shinobicontrols.charts.LineSeriesStyle;
import com.shinobicontrols.charts.NumberRange;
import com.shinobicontrols.charts.Series;
import com.shinobicontrols.charts.ShinobiChart;
import com.shinobicontrols.charts.SimpleDataAdapter;
import com.shinobicontrols.charts.TickMark;
import com.shinobicontrols.kcompanionapp.charts.internal.AxisMapping;
import com.shinobicontrols.kcompanionapp.charts.internal.BaseChartFragment;
import com.shinobicontrols.kcompanionapp.charts.internal.ChartManager;
import com.shinobicontrols.kcompanionapp.charts.internal.ChartThemeCache;
import com.shinobicontrols.kcompanionapp.charts.internal.FlagPlacementStrategy;
import com.shinobicontrols.kcompanionapp.charts.internal.FlagPointerDrawingStrategy;
import com.shinobicontrols.kcompanionapp.charts.internal.FlagView;
import com.shinobicontrols.kcompanionapp.charts.internal.FlagViewCreator;
import com.shinobicontrols.kcompanionapp.charts.internal.IconFactory;
import com.shinobicontrols.kcompanionapp.charts.internal.NightTimeBackgroundView;
import com.shinobicontrols.kcompanionapp.charts.internal.PresetTickMarkLabelMaps;
import com.shinobicontrols.kcompanionapp.charts.internal.QuadrantStrategiesSetter;
import com.shinobicontrols.kcompanionapp.charts.internal.RangeAndFrequencySettingStrategy;
import com.shinobicontrols.kcompanionapp.charts.internal.SeriesCreator;
import com.shinobicontrols.kcompanionapp.charts.internal.SleepFlagDateTimeFormatter;
import com.shinobicontrols.kcompanionapp.charts.internal.StyledNumberAxis;
import com.shinobicontrols.kcompanionapp.charts.internal.TickLabelUpdateStrategy;
import com.shinobicontrols.kcompanionapp.charts.internal.TickMarkDrawingStrategy;
import com.shinobicontrols.kcompanionapp.charts.internal.TypedValueGetter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.joda.time.DateTime;
/* loaded from: classes.dex */
public class SleepChartFragment extends BaseChartFragment implements ShinobiChart.OnInternalLayoutListener {
    public static final String ASLEEP_TIME_ARG = "asleepTime";
    private static final String TAG = SleepChartFragment.class.getSimpleName();
    public static final String WOKEUP_TIME_ARG = "wokeupTime";
    private TickMarkDrawingStrategy.AddUnitToUpperMostTickMarkLabel addUnitToUpperMostTickMarkLabel;
    private DateTime asleep;
    private FlagView asleepFlag;
    private SleepFlagDateTimeFormatter asleepFormatter;
    private Series<?> awake;
    private DataProperties dataProperties;
    private boolean hasSleepData;
    private View heartIcon;
    private Series<?> heartRate;
    private RangeAndFrequencySettingStrategy heartRateRangeAndFrequencySettingStrategy;
    private Axis<Double, Double> heartRateYAxis;
    private Series<?> light;
    private NightTimeBackgroundView nightTimeBackgroundView;
    private FlagView peakHeartRateFlag;
    private Series<?> restful;
    private TextView sleepGapWarning;
    private View sleepIcon;
    private RangeAndFrequencySettingStrategy sleepRangeAndFrequencySettingStrategy;
    private Axis<Double, Double> sleepYAxis;
    private TypedValueGetter valueGetter;
    private DateTime wokeUp;
    private FlagView wokeUpFlag;
    private SleepFlagDateTimeFormatter wokeUpFormatter;
    private Axis<Double, Double> xAxis;

    public static SleepChartFragment newInstance() {
        return new SleepChartFragment();
    }

    @Override // com.shinobicontrols.kcompanionapp.charts.internal.BaseChartFragment, android.support.v4.app.Fragment
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.valueGetter = new TypedValueGetter(getResources());
        this.asleepFormatter = new SleepFlagDateTimeFormatter(getResources(), getResources().getString(R.string.shinobicharts_asleep), activity);
        this.wokeUpFormatter = new SleepFlagDateTimeFormatter(getResources(), getResources().getString(R.string.shinobicharts_wokeup), activity);
        this.sleepGapWarning = (TextView) activity.findViewById(R.id.sleep_gap_error);
    }

    @Override // com.shinobicontrols.kcompanionapp.charts.internal.BaseChartFragment, android.support.v4.app.Fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        if (savedInstanceState != null) {
            this.asleep = (DateTime) savedInstanceState.getSerializable(ASLEEP_TIME_ARG);
            this.wokeUp = (DateTime) savedInstanceState.getSerializable(WOKEUP_TIME_ARG);
        }
        ViewGroup iconGroup = (ViewGroup) rootView.findViewById(R.id.icon_group);
        createAndAddIcons(iconGroup);
        ChartView chartView = (ChartView) rootView.findViewById(R.id.chart);
        ShinobiChart chart = chartView.getShinobiChart();
        chart.getStyle().setBackgroundColor(0);
        chart.getStyle().setCanvasBackgroundColor(0);
        chart.getStyle().setPlotAreaBackgroundColor(0);
        this.xAxis = createAndSetupXAxis();
        chart.setXAxis(this.xAxis);
        this.sleepYAxis = createAndSetupSleepYAxis();
        chart.setYAxis(this.sleepYAxis);
        this.heartRateYAxis = createAndSetupHeartRateYAxis();
        chart.addYAxis(this.heartRateYAxis);
        createSleepSeries(chart);
        createHeartRateSeries(chart);
        createChartAnnotations();
        createAndAddAdditionalViews(rootView, chart);
        ChartManager chartManager = getChartManager();
        chartManager.addMapping(createSleepMapping(chart));
        chartManager.addMapping(createHeartRateMapping(chart));
        chart.setOnInternalLayoutListener(this);
        return rootView;
    }

    private void createAndAddIcons(ViewGroup iconGroup) {
        this.sleepIcon = IconFactory.create(IconFactory.SLEEP, getActivity(), getChartThemeCache());
        this.heartIcon = IconFactory.create(IconFactory.HEART_RATE, getActivity(), getChartThemeCache());
        iconGroup.addView(this.sleepIcon);
        iconGroup.addView(this.heartIcon);
    }

    private Axis<Double, Double> createAndSetupXAxis() {
        StyledNumberAxis axis = new StyledNumberAxis(getActivity(), getChartThemeCache());
        axis.setPosition(Axis.Position.REVERSE);
        axis.setExpectedLongestLabel(getResources().getString(R.string.shinobicharts_12pm));
        axis.setTickMarkClippingModeLow(TickMark.ClippingMode.TICKS_AND_LABELS_PERSIST);
        axis.setTickLabelUpdateStrategy(new TickLabelUpdateStrategy.ConvertMillisToHoursTickLabelUpdateStrategy(getResources(), getActivity()));
        axis.setTickMarkDrawingStrategy(new TickMarkDrawingStrategy.XAxisLabelLeftAndTickAcrossCanvasTickMarkDrawingStrategy());
        double minValue = this.valueGetter.getFloat(R.id.shinobicharts_sleep_time_default_min);
        double maxValue = this.valueGetter.getFloat(R.id.shinobicharts_sleep_time_default_max);
        axis.setDefaultRange(new NumberRange(Double.valueOf(minValue), Double.valueOf(maxValue)));
        return axis;
    }

    private Axis<Double, Double> createAndSetupSleepYAxis() {
        StyledNumberAxis axis = new StyledNumberAxis(getActivity(), getChartThemeCache());
        SparseArray<String> tickMarkLabelMap = PresetTickMarkLabelMaps.createSleepTypesMap(getResources());
        axis.setTickLabelUpdateStrategy(new TickLabelUpdateStrategy.MapTickValueToDefinedStringHidingNonMappedTickLabelUpdateStrategy(tickMarkLabelMap));
        axis.setTickMarkDrawingStrategy(new TickMarkDrawingStrategy.YAxisLableAboveOrBelowTickLineDependingOnBaselineTickMarkDrawingStrategy(this.valueGetter.getFloat(R.id.shinobicharts_sleep_baseline)));
        double minValue = this.valueGetter.getFloat(R.id.shinobicharts_sleep_min);
        double maxValue = this.valueGetter.getFloat(R.id.shinobicharts_sleep_max);
        this.sleepRangeAndFrequencySettingStrategy = new RangeAndFrequencySettingStrategy.DefaultRangeAndFrequencySettingStrategy(axis, Double.valueOf(minValue), Double.valueOf(maxValue), null);
        this.sleepRangeAndFrequencySettingStrategy.setRange();
        axis.setMajorTickMarkValues(getKeysAsDoubleList(tickMarkLabelMap));
        return axis;
    }

    private List<Double> getKeysAsDoubleList(SparseArray<String> sparseArray) {
        List<Double> keys = new ArrayList<>();
        for (int i = 0; i < sparseArray.size(); i++) {
            keys.add(Double.valueOf(sparseArray.keyAt(i)));
        }
        return keys;
    }

    private Axis<Double, Double> createAndSetupHeartRateYAxis() {
        StyledNumberAxis axis = new StyledNumberAxis(getActivity(), getChartThemeCache());
        axis.setLabelFormat(new DecimalFormat(getResources().getString(R.string.shinobicharts_sleep_heart_rate_label_format)));
        axis.setTickMarkClippingModeLow(TickMark.ClippingMode.NEITHER_PERSIST);
        this.addUnitToUpperMostTickMarkLabel = new TickMarkDrawingStrategy.AddUnitToUpperMostTickMarkLabel(getResources().getString(R.string.shinobicharts_bpm), getChartThemeCache());
        axis.setTickMarkDrawingStrategy(new TickMarkDrawingStrategy.MultiTickMarkDrawingStrategy(new TickMarkDrawingStrategy.YAxisLableUnderTickLineTickMarkDrawingStrategy(), this.addUnitToUpperMostTickMarkLabel));
        double minValue = this.valueGetter.getFloat(R.id.shinobicharts_sleep_heart_rate_min);
        double maxValue = this.valueGetter.getFloat(R.id.shinobicharts_sleep_heart_rate_default_max);
        axis.setDefaultRange(new NumberRange(Double.valueOf(minValue), Double.valueOf(maxValue)));
        axis.setMajorTickFrequency(Double.valueOf(this.valueGetter.getFloat(R.id.shinobicharts_sleep_heart_rate_default_frequency)));
        return axis;
    }

    private void createSleepSeries(ShinobiChart chart) {
        ChartThemeCache themeCache = getChartThemeCache();
        this.awake = SeriesCreator.STEP_LINE.create(getResources(), themeCache);
        LineSeriesStyle awakeSelectedStyle = (LineSeriesStyle) this.awake.getSelectedStyle();
        awakeSelectedStyle.setAreaColor(themeCache.getAwakeSleepColor());
        this.light = SeriesCreator.STEP_LINE.create(getResources(), themeCache);
        LineSeriesStyle lightSelectedStyle = (LineSeriesStyle) this.light.getSelectedStyle();
        lightSelectedStyle.setAreaColorBelowBaseline(themeCache.getLightSleepColor());
        this.restful = SeriesCreator.STEP_LINE.create(getResources(), themeCache);
        LineSeriesStyle restfulSelectedStyle = (LineSeriesStyle) this.restful.getSelectedStyle();
        restfulSelectedStyle.setAreaColorBelowBaseline(themeCache.getRestfulSleepColor());
    }

    private void createHeartRateSeries(ShinobiChart chart) {
        this.heartRate = SeriesCreator.FILLED_LINE.create(getResources(), getChartThemeCache());
        LineSeriesStyle heartRateStyle = (LineSeriesStyle) this.heartRate.getStyle();
        heartRateStyle.setAreaColor(0);
        heartRateStyle.setAreaLineColor(0);
        heartRateStyle.setLineColor(0);
    }

    private void createChartAnnotations() {
        ChartThemeCache themeCache = getChartThemeCache();
        this.peakHeartRateFlag = FlagViewCreator.createDataPointFlag(getActivity(), IconFactory.HEART_RATE, null, themeCache.getPeakDataPointFlagColor(), themeCache.getPeakDataPointFlagTextColor(), themeCache.getPathToDataPointFlagTypeface(), themeCache.getPathToGlyphTypeface());
        this.peakHeartRateFlag.setVisibility(8);
    }

    private void createAndAddAdditionalViews(View rootView, ShinobiChart chart) {
        ChartThemeCache themeCache = getChartThemeCache();
        FrameLayout chartHolder = (FrameLayout) rootView.findViewById(R.id.chart_holder);
        this.nightTimeBackgroundView = new NightTimeBackgroundView(getActivity(), themeCache);
        this.nightTimeBackgroundView.setVisibility(8);
        chartHolder.addView(this.nightTimeBackgroundView, 0, new ViewGroup.LayoutParams(-1, -1));
        this.asleepFlag = FlagViewCreator.createSleepFlag(getActivity(), themeCache.getFellAsleepFlagColor(), themeCache.getFellAsleepFlagTextColor(), themeCache.getPathToSleepFlagTypeface());
        this.wokeUpFlag = FlagViewCreator.createSleepFlag(getActivity(), themeCache.getWokeUpFlagColor(), themeCache.getWokeUpFlagTextColor(), themeCache.getPathToSleepFlagTypeface());
        ((RelativeLayout) rootView).addView(this.asleepFlag, 0);
        ((RelativeLayout) rootView).addView(this.wokeUpFlag, 0);
    }

    private AxisMapping createSleepMapping(ShinobiChart chart) {
        List<Series<?>> seriesList = new ArrayList<>();
        seriesList.add(this.awake);
        seriesList.add(this.light);
        seriesList.add(this.restful);
        List<View> viewsToShowWhenSelected = new ArrayList<>();
        viewsToShowWhenSelected.add(this.nightTimeBackgroundView);
        viewsToShowWhenSelected.add(this.asleepFlag);
        viewsToShowWhenSelected.add(this.wokeUpFlag);
        return new AxisMapping(this.sleepIcon, seriesList, this.sleepYAxis, viewsToShowWhenSelected);
    }

    private AxisMapping createHeartRateMapping(ShinobiChart chart) {
        List<Series<?>> seriesList = new ArrayList<>();
        seriesList.add(this.heartRate);
        List<View> viewsToShowWhenSelected = new ArrayList<>();
        viewsToShowWhenSelected.add(this.peakHeartRateFlag);
        return new AxisMapping(this.heartIcon, seriesList, this.heartRateYAxis, viewsToShowWhenSelected);
    }

    @Override // com.shinobicontrols.kcompanionapp.charts.internal.Provideable
    public void reload() {
        if (isReadyForReload()) {
            AnnotationsManager annotationsManager = this.heartRate.getChart().getAnnotationsManager();
            annotationsManager.removeAllAnnotations();
            int awakeValue = getResources().getInteger(R.integer.shinobicharts_sleep_awake_value);
            int lightValue = getResources().getInteger(R.integer.shinobicharts_sleep_light_value);
            int restfulValue = getResources().getInteger(R.integer.shinobicharts_sleep_restful_value);
            DataAdapter<Long, Integer> awakeData = new SimpleDataAdapter<>();
            DataAdapter<Long, Integer> lightData = new SimpleDataAdapter<>();
            DataAdapter<Long, Integer> restfulData = new SimpleDataAdapter<>();
            this.hasSleepData = false;
            SleepEvent sleepEvent = getDataProvider().getSleepEvent();
            if (sleepEvent != null && sleepEvent.getSequences() != null && sleepEvent.getSequences().length != 0 && sleepEvent.getInfo() != null) {
                UserActivity[] activities = sleepEvent.getInfo();
                List<UserActivity> activityList = Arrays.asList(activities);
                Collections.sort(activityList, new UserActivityComparator());
                sleepEvent.setInfo((UserActivity[]) activityList.toArray(new UserActivity[activityList.size()]));
                this.dataProperties = getDataProperties(sleepEvent);
                setXAxisRange(sleepEvent);
                long cumulativeDuration = 0;
                if (sleepEvent.getStartTime() != null) {
                    cumulativeDuration = sleepEvent.getStartTime().getMillis();
                }
                this.asleep = null;
                this.wokeUp = null;
                boolean showSleepGapWarning = false;
                SleepEventSequence[] arr$ = sleepEvent.getSequences();
                for (SleepEventSequence sleepSequence : arr$) {
                    if (sleepSequence != null) {
                        TimeSpan duration = TimeSpan.fromSeconds(sleepSequence.getDuration());
                        int awakeYValue = isAwake(sleepSequence) ? awakeValue : 0;
                        int restfulYValue = isRestfulSleep(sleepSequence) ? restfulValue : 0;
                        int restlessYValue = isLightSleep(sleepSequence) ? lightValue : 0;
                        if (!this.hasSleepData && (restfulYValue != 0 || restlessYValue != 0)) {
                            this.hasSleepData = true;
                        }
                        long startValue = cumulativeDuration;
                        long endValue = cumulativeDuration + duration.getMilliseconds();
                        if (!showSleepGapWarning) {
                            showSleepGapWarning = sleepSequence.getSequenceType() == SequenceType.Unknown || sleepSequence.getSequenceType() == SequenceType.NotWorn;
                        }
                        float startFloatValue = (float) startValue;
                        float endFloatValue = (float) endValue;
                        if (endFloatValue - startFloatValue == 0.0f) {
                            endValue = Math.nextUp(endFloatValue);
                        }
                        awakeData.add(new DataPoint<>(Long.valueOf(startValue), Integer.valueOf(awakeYValue)));
                        awakeData.add(new DataPoint<>(Long.valueOf(endValue), Integer.valueOf(awakeYValue)));
                        restfulData.add(new DataPoint<>(Long.valueOf(startValue), Integer.valueOf(restfulYValue)));
                        restfulData.add(new DataPoint<>(Long.valueOf(endValue), Integer.valueOf(restfulYValue)));
                        lightData.add(new DataPoint<>(Long.valueOf(startValue), Integer.valueOf(restlessYValue)));
                        lightData.add(new DataPoint<>(Long.valueOf(endValue), Integer.valueOf(restlessYValue)));
                        cumulativeDuration = endValue;
                    }
                }
                if (this.sleepGapWarning != null) {
                    this.sleepGapWarning.setVisibility(showSleepGapWarning ? 0 : 8);
                }
                this.asleep = sleepEvent.getFallAsleepTime();
                this.wokeUp = sleepEvent.getWakeUpTime();
                this.awake.setDataAdapter(awakeData);
                this.light.setDataAdapter(lightData);
                this.restful.setDataAdapter(restfulData);
                addSleepHeartRateData(sleepEvent);
                if (this.asleep != null) {
                    DateTime sleepDay = this.asleep.minusHours(5);
                    this.asleepFlag.setValue(this.asleepFormatter.format(this.asleep, sleepDay.getDayOfMonth() != sleepEvent.getStartTime().getDayOfMonth()));
                }
                if (this.wokeUp != null) {
                    this.wokeUpFlag.setValue(this.wokeUpFormatter.format(this.wokeUp));
                }
            }
        }
    }

    private boolean isAwake(SleepEventSequence sequence) {
        SequenceType sequenceType = sequence.getSequenceType();
        return sequenceType == SequenceType.Awake || sequenceType == SequenceType.Dozing || sequenceType == SequenceType.Snoozing;
    }

    private boolean isLightSleep(SleepEventSequence sequence) {
        SleepTypes sleepType = sequence.getSleepType();
        SequenceType sequenceType = sequence.getSequenceType();
        return sequenceType == SequenceType.Sleep && sleepType != SleepTypes.RestfulSleep;
    }

    private boolean isRestfulSleep(SleepEventSequence sequence) {
        SleepTypes sleepType = sequence.getSleepType();
        SequenceType sequenceType = sequence.getSequenceType();
        return sequenceType == SequenceType.Sleep && sleepType == SleepTypes.RestfulSleep;
    }

    private void updateHeartRateFlags(int peakHeartRate, long timeAtPeakHeartRate) {
        AnnotationsManager annotationsManager = this.heartRate.getChart().getAnnotationsManager();
        String flagValue = getResources().getString(R.string.shinobicharts_sleep_heart_rate_flag_label_format, Integer.valueOf(peakHeartRate));
        this.peakHeartRateFlag.setValue(flagValue);
        annotationsManager.addViewAnnotation(this.peakHeartRateFlag, Long.valueOf(timeAtPeakHeartRate), Integer.valueOf(peakHeartRate), this.xAxis, this.heartRateYAxis);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class DataProperties {
        final int peakHeartRate;
        final long timeAtPeakHeartRate;

        public DataProperties(int peakHeartRate, long timeAtPeakHeartRate) {
            this.peakHeartRate = peakHeartRate;
            this.timeAtPeakHeartRate = timeAtPeakHeartRate;
        }
    }

    private DataProperties getDataProperties(SleepEvent sleepEvent) {
        int peakHeartRate;
        int peakHeartRate2 = Integer.MIN_VALUE;
        long timeAtpeakHeartRate = 0;
        UserActivity[] arr$ = sleepEvent.getInfo();
        for (UserActivity activity : arr$) {
            if (peakHeartRate2 < activity.getAverageHeartRate()) {
                peakHeartRate2 = activity.getAverageHeartRate();
                timeAtpeakHeartRate = activity.getTimeOfDay().getMillis();
            }
        }
        double minHearRateValue = this.valueGetter.getFloat(R.id.shinobicharts_sleep_heart_rate_min);
        if (peakHeartRate2 > minHearRateValue) {
            peakHeartRate = sleepEvent.getRestingHR();
        } else {
            peakHeartRate = 0;
        }
        return new DataProperties(peakHeartRate, timeAtpeakHeartRate);
    }

    private void setXAxisRange(SleepEvent sleepEvent) {
        DateTime startTime = sleepEvent.getStartTime();
        DateTime rangeMin = startTime.hourOfDay().roundFloorCopy();
        int durationInSeconds = sleepEvent.getDuration();
        DateTime rangeMax = startTime.plusSeconds(durationInSeconds).hourOfDay().roundCeilingCopy();
        this.xAxis.setDefaultRange(new NumberRange(Double.valueOf(rangeMin.getMillis()), Double.valueOf(rangeMax.getMillis())));
        this.xAxis.setMajorTickFrequency(Double.valueOf(this.valueGetter.getFloat(R.id.shinobicharts_sleep_frequency)));
    }

    private void updateHeartRateAxisRangeAndFrequency(double maxAverageHeartRate) {
        double minValue = this.valueGetter.getFloat(R.id.shinobicharts_sleep_heart_rate_min);
        double maxValue = this.valueGetter.getFloat(R.id.shinobicharts_sleep_heart_rate_default_max);
        if (maxAverageHeartRate > maxValue) {
            double additionToMax = this.valueGetter.getFloat(R.id.shinobicharts_sleep_daily_heart_rate_addition_to_max);
            maxValue = maxAverageHeartRate + additionToMax;
        }
        int numberOfIntervals = getResources().getInteger(R.integer.shinobicharts_sleep_heart_rate_number_of_intervals);
        int toTheNearest = getResources().getInteger(R.integer.shinobicharts_sleep_heart_rate_round_to_nearest_value);
        this.heartRateRangeAndFrequencySettingStrategy = new RangeAndFrequencySettingStrategy.FixedNumberOfIntervalsRangeAndRoundedFrequencySettingStrategy(this.heartRateYAxis, minValue - this.valueGetter.getFloat(R.id.shinobicharts_sleep_daily_heart_rate_subtraction_from_min), maxValue, numberOfIntervals, toTheNearest, null, false);
        this.heartRateRangeAndFrequencySettingStrategy.setRangeAndFrequency();
    }

    @Override // com.shinobicontrols.charts.ShinobiChart.OnInternalLayoutListener
    public void onInternalLayout(ShinobiChart chart) {
        double baselineDataValue = this.valueGetter.getFloat(R.id.shinobicharts_sleep_baseline);
        float baselinePixelYValue = this.sleepYAxis.getPixelValueForUserValue(Double.valueOf(baselineDataValue));
        this.nightTimeBackgroundView.setBaselinePixelYValue(baselinePixelYValue);
        int leftMargin = getResources().getDimensionPixelSize(R.dimen.shinobicharts_chart_horizontal_margin);
        NumberRange sleepRange = (NumberRange) this.xAxis.getDefaultRange();
        float min = this.xAxis.getPixelValueForUserValue(sleepRange.getMinimum()) + leftMargin;
        float max = this.xAxis.getPixelValueForUserValue(sleepRange.getMaximum()) + leftMargin;
        float asleepPixel = getSleepFlagXValuePixel(this.asleep);
        float wokeUpPixel = getSleepFlagXValuePixel(this.wokeUp);
        if (this.asleep != null) {
            this.asleepFlag.setFlagPlacementStrategy(new FlagPlacementStrategy.XPositionWithinRangeFlagPlacementStrategy(asleepPixel, min, max, false, wokeUpPixel));
            this.asleepFlag.setFlagPointerDrawingStrategy(new FlagPointerDrawingStrategy.XPositionWithinRangeFlagPointerDrawingStrategy(asleepPixel, min, max));
        }
        if (this.wokeUp != null) {
            this.wokeUpFlag.setFlagPlacementStrategy(new FlagPlacementStrategy.XPositionWithinRangeFlagPlacementStrategy(wokeUpPixel, min, max, true, asleepPixel));
            this.wokeUpFlag.setFlagPointerDrawingStrategy(new FlagPointerDrawingStrategy.XPositionWithinRangeFlagPointerDrawingStrategy(wokeUpPixel, min, max));
        }
        int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, 0);
        int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, 0);
        this.asleepFlag.measure(widthMeasureSpec, heightMeasureSpec);
        this.wokeUpFlag.measure(widthMeasureSpec, heightMeasureSpec);
        if (!this.hasSleepData) {
            this.asleepFlag.setVisibility(8);
            this.wokeUpFlag.setVisibility(8);
        } else if (this.asleep == null) {
            this.asleepFlag.setVisibility(8);
        } else if (this.wokeUp == null) {
            this.wokeUpFlag.setVisibility(8);
        }
        if (this.dataProperties != null) {
            QuadrantStrategiesSetter.setStrategiesBasedOnData(this.peakHeartRateFlag, this.dataProperties.timeAtPeakHeartRate, this.dataProperties.peakHeartRate, this.xAxis, this.heartRateYAxis);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.shinobicontrols.kcompanionapp.charts.internal.BaseChartFragment
    public void applyTheme() {
        super.applyTheme();
        this.addUnitToUpperMostTickMarkLabel.updateWithTheme(getChartThemeCache());
        ChartView chartView = (ChartView) getView().findViewById(R.id.chart);
        chartView.getShinobiChart().redrawChart();
    }

    private void addSleepHeartRateData(SleepEvent sleepEvent) {
        DataAdapter<Long, Integer> heartRateData = new SimpleDataAdapter<>();
        LinkedList<Integer> heartRatePointsForAvgCalculation = new LinkedList<>();
        int movingAvgFactor = getResources().getInteger(R.integer.shinobicharts_moving_average_factor);
        int maxAveragePlotedHeartRate = 0;
        if (sleepEvent != null && sleepEvent.getInfo() != null && sleepEvent.getInfo().length > 0) {
            int InitialHeartRate = sleepEvent.getInfo()[0].getAverageHeartRate();
            UserActivity[] arr$ = sleepEvent.getInfo();
            for (UserActivity userActivity : arr$) {
                if (userActivity != null && userActivity.getTimeOfDay() != null) {
                    if (InitialHeartRate == 0) {
                        InitialHeartRate = userActivity.getAverageHeartRate();
                    } else if (userActivity.getAverageHeartRate() > 0) {
                        if (heartRatePointsForAvgCalculation.size() < movingAvgFactor) {
                            heartRatePointsForAvgCalculation.addFirst(Integer.valueOf(userActivity.getAverageHeartRate()));
                        } else {
                            heartRatePointsForAvgCalculation.removeLast();
                            heartRatePointsForAvgCalculation.addFirst(Integer.valueOf(userActivity.getAverageHeartRate()));
                        }
                        double movingAvgSumForPace = Constants.SPLITS_ACCURACY;
                        Iterator i$ = heartRatePointsForAvgCalculation.iterator();
                        while (i$.hasNext()) {
                            int heartRateValue = i$.next().intValue();
                            movingAvgSumForPace += heartRateValue;
                        }
                        int hrValue = (int) (movingAvgSumForPace / heartRatePointsForAvgCalculation.size());
                        if (maxAveragePlotedHeartRate < hrValue) {
                            maxAveragePlotedHeartRate = hrValue;
                        }
                        heartRateData.add(new DataPoint<>(Long.valueOf(userActivity.getTimeOfDay().getMillis()), Integer.valueOf(hrValue)));
                    }
                }
            }
            this.heartRate.setDataAdapter(heartRateData);
            updateHeartRateAxisRangeAndFrequency(maxAveragePlotedHeartRate);
            double minHearRateValue = this.valueGetter.getFloat(R.id.shinobicharts_sleep_heart_rate_min);
            if (this.dataProperties != null && this.dataProperties.peakHeartRate > 0 && maxAveragePlotedHeartRate > minHearRateValue) {
                updateHeartRateFlags(this.dataProperties.peakHeartRate, this.dataProperties.timeAtPeakHeartRate);
                return;
            }
            return;
        }
        KLog.d(TAG, "Unable to retrieve sleepEvent Info data locally.");
    }

    @Override // com.shinobicontrols.kcompanionapp.charts.internal.BaseChartFragment, android.support.v4.app.Fragment
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(WOKEUP_TIME_ARG, this.wokeUp);
        outState.putSerializable(ASLEEP_TIME_ARG, this.asleep);
    }

    private float getSleepFlagXValuePixel(DateTime time) {
        int leftMargin = getResources().getDimensionPixelSize(R.dimen.shinobicharts_chart_horizontal_margin);
        if (time == null) {
            return -1.0f;
        }
        return this.xAxis.getPixelValueForUserValue(Double.valueOf(time.getMillis())) + leftMargin;
    }
}

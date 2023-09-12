package com.shinobicontrols.kcompanionapp.charts.internal;

import android.content.res.Resources;
import android.os.Bundle;
import com.microsoft.kapp.R;
import com.shinobicontrols.kcompanionapp.charts.ChartTheme;
/* loaded from: classes.dex */
public class ChartThemeCache implements ChartTheme {
    private static final String ABOVE_AVERAGE_PACE_COLOR = "AboveAveragePaceColor";
    private static final String ACTIVITY_GOAL_MET_COLOR = "ActivityGoalMetColor";
    private static final String ANNOTATION_AVERAGE_COLOR = "AnnotationAverageColor";
    private static final String ANNOTATION_LOW_COLOR = "AnnotationLowColor";
    private static final String ANNOTATION_PEAK_COLOR = "AnnotationPeakColor";
    private static final String AVERAGE_DATA_POINT_FLAG_COLOR = "AverageDataPointFlagColor";
    private static final String AVERAGE_DATA_POINT_FLAG_TEXT_COLOR = "AverageDataPointFlagTextColor";
    private static final String AVERAGE_HEART_RATE_LINE_COLOR = "AverageHeartRateLineColor";
    private static final String AVERAGE_MARKER_FILL_COLOR = "AverageMarkerFillColor";
    private static final String AVERAGE_MARKER_STROKE_COLOR = "AverageMarkerStrokeColor";
    private static final String AVERAGE_PACE_LINE_COLOR = "AveragePaceLineColor";
    private static final String AWAKE_SLEEP_COLOR = "AwakeSleepColor";
    private static final String AXIS_TICK_LABEL_COLOR = "AxisTickLabelColor";
    private static final String AXIS_TICK_LINE_COLOR = "AxisTickLineColor";
    private static final String AXIS_ZONE_TICK_LABEL_TITLE_COLOR = "AxisZoneTickLabelTitleColor";
    private static final String AXIS_ZONE_TICK_LINE_COLOR = "AxisZoneTickLineColor";
    private static final String BELOW_AVERAGE_PACE_COLOR = "BelowAveragePaceColor";
    private static final String CHART_BACKGROUND_COLOR = "ChartBackgroundColor";
    private static final String DATA_BAR_FILL_COLOR = "DataBarFillColor";
    private static final String DATA_FILL_COLOR = "DataFillColor";
    private static final String DATA_LINE_COLOR = "DataLineColor";
    private static final String EMPTY_DATA_COLOR = "EmptyDataColor";
    private static final String FELL_ASLEEP_COLOR = "FellAsleepColor";
    private static final String FELL_ASLEEP_TEXT_COLOR = "FellAsleepTextColor";
    private static final String GHOSTED_DATA_BAR_FILL_COLOR = "GhostedDataBarFillColor";
    private static final String GHOSTED_DATA_FILL_COLOR = "GhostedDataFillColor";
    private static final String GHOSTED_DATA_LINE_COLOR = "GhostedDataLineColor";
    private static final String GOAL_MET_DATA_BAR_FILL_COLOR = "GoalMetDataBarFillColor";
    private static final String HIGH_ACTIVITY_DATA_BAR_FILL_COLOR = "HighActivityDataBarFillColor";
    private static final String LIGHT_SLEEP_COLOR = "LightSleepColor";
    private static final String LOWEST_DATA_POINT_FLAG_COLOR = "LowestDataPointFlagColor";
    private static final String LOWEST_DATA_POINT_FLAG_TEXT_COLOR = "LowestDataPointFlagTextColor";
    private static final String LOWEST_MARKER_FILL_COLOR = "LowestMarkerFillColor";
    private static final String LOWEST_MARKER_STROKE_COLOR = "LowestMarkerStrokeColor";
    private static final String MILE_MARKER_COLOR = "MileMarkerColor";
    private static final String PATH_TO_AVERAGE_PACE_LINE_LABEL_TYPEFACE = "PathToAveragePaceLineLabelTypeface";
    private static final String PATH_TO_AXIS_TICK_LABEL_TYPEFACE = "PathToAxisTickLabelTypeface";
    private static final String PATH_TO_DATA_POINT_FLAG_TYPEFACE = "PathToPeakDataPointFlagTypeface";
    private static final String PATH_TO_ELEVATION_ICON_TEXT_TYPEFACE = "PathToElevationIconTextTypeface";
    private static final String PATH_TO_GLYPH_TYPEFACE = "PathToGlyphTypeface";
    private static final String PATH_TO_SLEEP_FLAG_TYPEFACE = "PathToSleepFlagTypeface";
    private static final String PEAK_DATA_POINT_FLAG_COLOR = "PeakDataPointFlagColor";
    private static final String PEAK_DATA_POINT_FLAG_TEXT_COLOR = "PeakDataPointFlagTextColor";
    private static final String PEAK_MARKER_FILL_COLOR = "PeakMarkerFillColor";
    private static final String PEAK_MARKER_STROKE_COLOR = "PeakMarkerStrokeColor";
    private static final String RESTFUL_SLEEP_COLOR = "RestfulSleepColor";
    private static final String SELECTED_ICON_COLOR = "SelectedIconColor";
    private static final String SLEEP_NIGHT_TIME_BACKGROUND_COLOR = "SleepNightTimeBackgroundColor";
    private static final String UNSELECTED_ICON_COLOR = "UnselectedIconColor";
    private static final String WOKE_UP_COLOR = "WokeUpColor";
    private static final String WOKE_UP_TEXT_COLOR = "WokeUpTextColor";
    private final int aboveAveragePaceColor;
    private final int activityGoalMetColor;
    private final int annotationAverageColor;
    private final int annotationLowColor;
    private final int annotationPeakColor;
    private final int averageDataPointFlagColor;
    private final int averageDataPointFlagTextColor;
    private final int averageHeartRateLineColor;
    private final int averageMarkerFillColor;
    private final int averageMarkerStrokeColor;
    private final int averagePaceLineColor;
    private final int awakeSleepColor;
    private final int axisTickLabelColor;
    private final int axisTickLineColor;
    private final int axisZoneTickLabelTitleColor;
    private final int axisZoneTickLineColor;
    private final int belowAveragePaceColor;
    private final int chartBackgroundColor;
    private final int dataBarFillColor;
    private final int dataFillColor;
    private final int dataLineColor;
    private final int emptyDataColor;
    private final int fellAsleepColor;
    private final int fellAsleepTextColor;
    private final int ghostedDataBarFillColor;
    private final int ghostedDataFillColor;
    private final int ghostedDataLineColor;
    private final int goalMetDataBarFillColor;
    private final int highActivityDataBarFillColor;
    private final int lightSleepColor;
    private final int lowestDataPointFlagColor;
    private final int lowestDataPointFlagTextColor;
    private final int lowestMarkerFillColor;
    private final int lowestMarkerStrokeColor;
    private final int mileMarkerColor;
    private final String pathToAveragePaceLineLabelTypeface;
    private final String pathToAxisTickLabelTypeface;
    private final String pathToDataPointFlagTypeface;
    private final String pathToElevationIconTextTypeface;
    private final String pathToGlyphTypeface;
    private final String pathToSleepFlagTypeface;
    private final int peakDataPointFlagColor;
    private final int peakDataPointFlagTextColor;
    private final int peakMarkerFillColor;
    private final int peakMarkerStrokeColor;
    private final int restfulSleepColor;
    private final int selectedIconColor;
    private final int sleepNightTimeBackgroundColor;
    private final int unselectedIconColor;
    private final int wokeUpColor;
    private final int wokeUpTextColor;

    public ChartThemeCache(Resources resources) {
        this.chartBackgroundColor = resources.getColor(R.color.WhiteColor);
        this.selectedIconColor = resources.getColor(R.color.PrimaryColor);
        this.unselectedIconColor = resources.getColor(R.color.IconUnselectedColor);
        this.axisTickLabelColor = resources.getColor(R.color.SecondaryLightTextColor);
        this.axisTickLineColor = resources.getColor(R.color.LightLineColor);
        this.axisZoneTickLineColor = resources.getColor(R.color.LightLineColor);
        this.axisZoneTickLabelTitleColor = resources.getColor(R.color.SecondaryAccentTextColor);
        this.dataLineColor = resources.getColor(R.color.ForegroundLineColor);
        this.dataFillColor = resources.getColor(R.color.PrimaryColor20Opacity);
        this.ghostedDataLineColor = resources.getColor(R.color.BackgroundLineColor);
        this.ghostedDataFillColor = resources.getColor(R.color.BackgroundLineColor);
        this.dataBarFillColor = resources.getColor(R.color.PrimaryMediumColor);
        this.highActivityDataBarFillColor = resources.getColor(R.color.PrimaryLightColor);
        this.goalMetDataBarFillColor = resources.getColor(R.color.BackgroundLineColor);
        this.ghostedDataBarFillColor = resources.getColor(R.color.BackgroundLineColor);
        this.emptyDataColor = resources.getColor(R.color.SecondaryLightTextColor);
        this.annotationPeakColor = resources.getColor(R.color.ForegroundLineColor);
        this.annotationAverageColor = resources.getColor(R.color.ForegroundLineColor);
        this.annotationLowColor = resources.getColor(R.color.ForegroundLineColor);
        this.averagePaceLineColor = resources.getColor(R.color.AverageLineColor);
        this.mileMarkerColor = resources.getColor(R.color.ForegroundLineColor);
        this.activityGoalMetColor = resources.getColor(R.color.ForegroundLineColor);
        this.awakeSleepColor = resources.getColor(R.color.AccentChartColor);
        this.lightSleepColor = resources.getColor(R.color.HighlightChartColor);
        this.restfulSleepColor = resources.getColor(R.color.PrimaryDarkColor);
        this.sleepNightTimeBackgroundColor = resources.getColor(R.color.WhiteColor);
        this.averageHeartRateLineColor = resources.getColor(R.color.AverageLineColor);
        this.fellAsleepColor = resources.getColor(R.color.transparent);
        this.fellAsleepTextColor = resources.getColor(R.color.SecondaryLightTextColor);
        this.wokeUpColor = resources.getColor(R.color.transparent);
        this.wokeUpTextColor = resources.getColor(R.color.SecondaryLightTextColor);
        this.peakDataPointFlagColor = resources.getColor(R.color.ForegroundLineColor);
        this.peakDataPointFlagTextColor = resources.getColor(R.color.WhiteColor);
        this.averageDataPointFlagColor = resources.getColor(R.color.ForegroundLineColor);
        this.averageDataPointFlagTextColor = resources.getColor(R.color.WhiteColor);
        this.lowestDataPointFlagColor = resources.getColor(R.color.ForegroundLineColor);
        this.lowestDataPointFlagTextColor = resources.getColor(R.color.WhiteColor);
        this.peakMarkerStrokeColor = resources.getColor(R.color.ForegroundLineColor);
        this.peakMarkerFillColor = resources.getColor(R.color.WhiteColor);
        this.averageMarkerStrokeColor = resources.getColor(R.color.ForegroundLineColor);
        this.averageMarkerFillColor = resources.getColor(R.color.WhiteColor);
        this.lowestMarkerStrokeColor = resources.getColor(R.color.ForegroundLineColor);
        this.lowestMarkerFillColor = resources.getColor(R.color.WhiteColor);
        this.aboveAveragePaceColor = resources.getColor(R.color.ForegroundLineColor);
        this.belowAveragePaceColor = resources.getColor(R.color.ForegroundLineColor);
        this.pathToGlyphTypeface = resources.getString(R.string.shinobicharts_project_k_symbol_regular_typeface_path);
        this.pathToAxisTickLabelTypeface = resources.getString(R.string.shinobicharts_segoeui_typeface_path);
        this.pathToAveragePaceLineLabelTypeface = resources.getString(R.string.shinobicharts_segoeui_bold_typeface_path);
        this.pathToSleepFlagTypeface = resources.getString(R.string.shinobicharts_segoeui_bold_typeface_path);
        this.pathToDataPointFlagTypeface = resources.getString(R.string.shinobicharts_segoeui_bold_typeface_path);
        this.pathToElevationIconTextTypeface = resources.getString(R.string.shinobicharts_segoeui_typeface_path);
    }

    public ChartThemeCache(ChartTheme theme) {
        this.chartBackgroundColor = theme.getChartBackgroundColor();
        this.selectedIconColor = theme.getSelectedIconColor();
        this.unselectedIconColor = theme.getUnselectedIconColor();
        this.axisTickLabelColor = theme.getAxisTickLabelColor();
        this.axisTickLineColor = theme.getAxisTickLineColor();
        this.axisZoneTickLineColor = theme.getAxisZoneTickLineColor();
        this.axisZoneTickLabelTitleColor = theme.getAxisZoneTickLabelTitleColor();
        this.dataLineColor = theme.getDataLineColor();
        this.dataFillColor = theme.getDataFillColor();
        this.ghostedDataLineColor = theme.getGhostedDataLineColor();
        this.ghostedDataFillColor = theme.getGhostedDataFillColor();
        this.dataBarFillColor = theme.getDataBarFillColor();
        this.highActivityDataBarFillColor = theme.getHighActivityDataBarFillColor();
        this.goalMetDataBarFillColor = theme.getGoalMetDataBarFillColor();
        this.ghostedDataBarFillColor = theme.getGhostedDataBarFillColor();
        this.emptyDataColor = theme.getEmptyDataColor();
        this.annotationPeakColor = theme.getAnnotationPeakColor();
        this.annotationAverageColor = theme.getAnnotationAverageColor();
        this.annotationLowColor = theme.getAnnotationLowColor();
        this.averagePaceLineColor = theme.getAveragePaceLineColor();
        this.mileMarkerColor = theme.getMileMarkerColor();
        this.activityGoalMetColor = theme.getActivityGoalMetColor();
        this.awakeSleepColor = theme.getAwakeSleepColor();
        this.lightSleepColor = theme.getLightSleepColor();
        this.restfulSleepColor = theme.getRestfulSleepColor();
        this.sleepNightTimeBackgroundColor = theme.getSleepNightTimeBackgroundColor();
        this.averageHeartRateLineColor = theme.getAverageHeartRateLineColor();
        this.fellAsleepColor = theme.getFellAsleepFlagColor();
        this.fellAsleepTextColor = theme.getFellAsleepFlagTextColor();
        this.wokeUpColor = theme.getWokeUpFlagColor();
        this.wokeUpTextColor = theme.getWokeUpFlagTextColor();
        this.peakDataPointFlagColor = theme.getPeakDataPointFlagColor();
        this.peakDataPointFlagTextColor = theme.getPeakDataPointFlagTextColor();
        this.averageDataPointFlagColor = theme.getAverageDataPointFlagColor();
        this.averageDataPointFlagTextColor = theme.getAverageDataPointFlagTextColor();
        this.lowestDataPointFlagColor = theme.getLowestDataPointFlagColor();
        this.lowestDataPointFlagTextColor = theme.getLowestDataPointFlagTextColor();
        this.peakMarkerStrokeColor = theme.getPeakMarkerStrokeColor();
        this.peakMarkerFillColor = theme.getPeakMarkerFillColor();
        this.averageMarkerStrokeColor = theme.getAverageMarkerStrokeColor();
        this.averageMarkerFillColor = theme.getAverageMarkerFillColor();
        this.lowestMarkerStrokeColor = theme.getLowestMarkerStrokeColor();
        this.lowestMarkerFillColor = theme.getLowestMarkerFillColor();
        this.aboveAveragePaceColor = theme.getAboveAveragePaceColor();
        this.belowAveragePaceColor = theme.getBelowAveragePaceColor();
        this.pathToGlyphTypeface = theme.getPathToGlyphTypeface();
        this.pathToAxisTickLabelTypeface = theme.getPathToAxisTickLabelTypeface();
        this.pathToAveragePaceLineLabelTypeface = theme.getPathToAveragePaceLineLabelTypeface();
        this.pathToSleepFlagTypeface = theme.getPathToSleepFlagTypeface();
        this.pathToDataPointFlagTypeface = theme.getPathToDataPointFlagTypeface();
        this.pathToElevationIconTextTypeface = theme.getPathToElevationIconTextTypeface();
    }

    public ChartThemeCache(Bundle savedInstanceState) {
        this.chartBackgroundColor = savedInstanceState.getInt(CHART_BACKGROUND_COLOR);
        this.selectedIconColor = savedInstanceState.getInt(SELECTED_ICON_COLOR);
        this.unselectedIconColor = savedInstanceState.getInt(UNSELECTED_ICON_COLOR);
        this.axisTickLabelColor = savedInstanceState.getInt(AXIS_TICK_LABEL_COLOR);
        this.axisTickLineColor = savedInstanceState.getInt(AXIS_TICK_LINE_COLOR);
        this.axisZoneTickLineColor = savedInstanceState.getInt(AXIS_ZONE_TICK_LINE_COLOR);
        this.axisZoneTickLabelTitleColor = savedInstanceState.getInt(AXIS_ZONE_TICK_LABEL_TITLE_COLOR);
        this.dataLineColor = savedInstanceState.getInt(DATA_LINE_COLOR);
        this.dataFillColor = savedInstanceState.getInt(DATA_FILL_COLOR);
        this.ghostedDataLineColor = savedInstanceState.getInt(GHOSTED_DATA_LINE_COLOR);
        this.ghostedDataFillColor = savedInstanceState.getInt(GHOSTED_DATA_FILL_COLOR);
        this.dataBarFillColor = savedInstanceState.getInt(DATA_BAR_FILL_COLOR);
        this.highActivityDataBarFillColor = savedInstanceState.getInt(HIGH_ACTIVITY_DATA_BAR_FILL_COLOR);
        this.goalMetDataBarFillColor = savedInstanceState.getInt(GOAL_MET_DATA_BAR_FILL_COLOR);
        this.ghostedDataBarFillColor = savedInstanceState.getInt(GHOSTED_DATA_BAR_FILL_COLOR);
        this.emptyDataColor = savedInstanceState.getInt(EMPTY_DATA_COLOR);
        this.annotationPeakColor = savedInstanceState.getInt(ANNOTATION_PEAK_COLOR);
        this.annotationAverageColor = savedInstanceState.getInt(ANNOTATION_AVERAGE_COLOR);
        this.annotationLowColor = savedInstanceState.getInt(ANNOTATION_LOW_COLOR);
        this.averagePaceLineColor = savedInstanceState.getInt(AVERAGE_PACE_LINE_COLOR);
        this.mileMarkerColor = savedInstanceState.getInt(MILE_MARKER_COLOR);
        this.activityGoalMetColor = savedInstanceState.getInt(ACTIVITY_GOAL_MET_COLOR);
        this.awakeSleepColor = savedInstanceState.getInt(AWAKE_SLEEP_COLOR);
        this.lightSleepColor = savedInstanceState.getInt(LIGHT_SLEEP_COLOR);
        this.restfulSleepColor = savedInstanceState.getInt(RESTFUL_SLEEP_COLOR);
        this.sleepNightTimeBackgroundColor = savedInstanceState.getInt(SLEEP_NIGHT_TIME_BACKGROUND_COLOR);
        this.averageHeartRateLineColor = savedInstanceState.getInt(AVERAGE_HEART_RATE_LINE_COLOR);
        this.fellAsleepColor = savedInstanceState.getInt(FELL_ASLEEP_COLOR);
        this.fellAsleepTextColor = savedInstanceState.getInt(FELL_ASLEEP_TEXT_COLOR);
        this.wokeUpColor = savedInstanceState.getInt(WOKE_UP_COLOR);
        this.wokeUpTextColor = savedInstanceState.getInt(WOKE_UP_TEXT_COLOR);
        this.peakDataPointFlagColor = savedInstanceState.getInt(PEAK_DATA_POINT_FLAG_COLOR);
        this.peakDataPointFlagTextColor = savedInstanceState.getInt(PEAK_DATA_POINT_FLAG_TEXT_COLOR);
        this.averageDataPointFlagColor = savedInstanceState.getInt(AVERAGE_DATA_POINT_FLAG_COLOR);
        this.averageDataPointFlagTextColor = savedInstanceState.getInt(AVERAGE_DATA_POINT_FLAG_TEXT_COLOR);
        this.lowestDataPointFlagColor = savedInstanceState.getInt(LOWEST_DATA_POINT_FLAG_COLOR);
        this.lowestDataPointFlagTextColor = savedInstanceState.getInt(LOWEST_DATA_POINT_FLAG_TEXT_COLOR);
        this.peakMarkerStrokeColor = savedInstanceState.getInt(PEAK_MARKER_STROKE_COLOR);
        this.peakMarkerFillColor = savedInstanceState.getInt(PEAK_MARKER_FILL_COLOR);
        this.averageMarkerStrokeColor = savedInstanceState.getInt(AVERAGE_MARKER_STROKE_COLOR);
        this.averageMarkerFillColor = savedInstanceState.getInt(AVERAGE_MARKER_FILL_COLOR);
        this.lowestMarkerStrokeColor = savedInstanceState.getInt(LOWEST_MARKER_STROKE_COLOR);
        this.lowestMarkerFillColor = savedInstanceState.getInt(LOWEST_MARKER_FILL_COLOR);
        this.aboveAveragePaceColor = savedInstanceState.getInt(ABOVE_AVERAGE_PACE_COLOR);
        this.belowAveragePaceColor = savedInstanceState.getInt(BELOW_AVERAGE_PACE_COLOR);
        this.pathToGlyphTypeface = savedInstanceState.getString(PATH_TO_GLYPH_TYPEFACE);
        this.pathToAxisTickLabelTypeface = savedInstanceState.getString(PATH_TO_AXIS_TICK_LABEL_TYPEFACE);
        this.pathToAveragePaceLineLabelTypeface = savedInstanceState.getString(PATH_TO_AVERAGE_PACE_LINE_LABEL_TYPEFACE);
        this.pathToSleepFlagTypeface = savedInstanceState.getString(PATH_TO_SLEEP_FLAG_TYPEFACE);
        this.pathToDataPointFlagTypeface = savedInstanceState.getString(PATH_TO_DATA_POINT_FLAG_TYPEFACE);
        this.pathToElevationIconTextTypeface = savedInstanceState.getString(PATH_TO_ELEVATION_ICON_TEXT_TYPEFACE);
    }

    public void onSaveInstaceState(Bundle outState) {
        outState.putInt(CHART_BACKGROUND_COLOR, this.chartBackgroundColor);
        outState.putInt(SELECTED_ICON_COLOR, this.selectedIconColor);
        outState.putInt(UNSELECTED_ICON_COLOR, this.unselectedIconColor);
        outState.putInt(AXIS_TICK_LABEL_COLOR, this.axisTickLabelColor);
        outState.putInt(AXIS_TICK_LINE_COLOR, this.axisTickLineColor);
        outState.putInt(AXIS_ZONE_TICK_LINE_COLOR, this.axisZoneTickLineColor);
        outState.putInt(AXIS_ZONE_TICK_LABEL_TITLE_COLOR, this.axisZoneTickLabelTitleColor);
        outState.putInt(DATA_LINE_COLOR, this.dataLineColor);
        outState.putInt(DATA_FILL_COLOR, this.dataFillColor);
        outState.putInt(GHOSTED_DATA_LINE_COLOR, this.ghostedDataLineColor);
        outState.putInt(GHOSTED_DATA_FILL_COLOR, this.ghostedDataFillColor);
        outState.putInt(DATA_BAR_FILL_COLOR, this.dataBarFillColor);
        outState.putInt(HIGH_ACTIVITY_DATA_BAR_FILL_COLOR, this.highActivityDataBarFillColor);
        outState.putInt(GOAL_MET_DATA_BAR_FILL_COLOR, this.goalMetDataBarFillColor);
        outState.putInt(GHOSTED_DATA_BAR_FILL_COLOR, this.ghostedDataBarFillColor);
        outState.putInt(EMPTY_DATA_COLOR, this.emptyDataColor);
        outState.putInt(ANNOTATION_PEAK_COLOR, this.annotationPeakColor);
        outState.putInt(ANNOTATION_AVERAGE_COLOR, this.annotationAverageColor);
        outState.putInt(ANNOTATION_LOW_COLOR, this.annotationLowColor);
        outState.putInt(AVERAGE_PACE_LINE_COLOR, this.averagePaceLineColor);
        outState.putInt(MILE_MARKER_COLOR, this.mileMarkerColor);
        outState.putInt(ACTIVITY_GOAL_MET_COLOR, this.activityGoalMetColor);
        outState.putInt(AWAKE_SLEEP_COLOR, this.awakeSleepColor);
        outState.putInt(LIGHT_SLEEP_COLOR, this.lightSleepColor);
        outState.putInt(RESTFUL_SLEEP_COLOR, this.restfulSleepColor);
        outState.putInt(SLEEP_NIGHT_TIME_BACKGROUND_COLOR, this.sleepNightTimeBackgroundColor);
        outState.putInt(AVERAGE_HEART_RATE_LINE_COLOR, this.averageHeartRateLineColor);
        outState.putInt(FELL_ASLEEP_COLOR, this.fellAsleepColor);
        outState.putInt(FELL_ASLEEP_TEXT_COLOR, this.fellAsleepTextColor);
        outState.putInt(WOKE_UP_COLOR, this.wokeUpColor);
        outState.putInt(WOKE_UP_TEXT_COLOR, this.wokeUpTextColor);
        outState.putInt(PEAK_DATA_POINT_FLAG_COLOR, this.peakDataPointFlagColor);
        outState.putInt(PEAK_DATA_POINT_FLAG_TEXT_COLOR, this.peakDataPointFlagTextColor);
        outState.putInt(AVERAGE_DATA_POINT_FLAG_COLOR, this.averageDataPointFlagColor);
        outState.putInt(AVERAGE_DATA_POINT_FLAG_TEXT_COLOR, this.averageDataPointFlagTextColor);
        outState.putInt(LOWEST_DATA_POINT_FLAG_COLOR, this.lowestDataPointFlagColor);
        outState.putInt(LOWEST_DATA_POINT_FLAG_TEXT_COLOR, this.lowestDataPointFlagTextColor);
        outState.putInt(PEAK_MARKER_STROKE_COLOR, this.peakMarkerStrokeColor);
        outState.putInt(PEAK_MARKER_FILL_COLOR, this.peakMarkerFillColor);
        outState.putInt(AVERAGE_MARKER_STROKE_COLOR, this.averageMarkerStrokeColor);
        outState.putInt(AVERAGE_MARKER_FILL_COLOR, this.averageMarkerFillColor);
        outState.putInt(LOWEST_MARKER_STROKE_COLOR, this.lowestMarkerStrokeColor);
        outState.putInt(LOWEST_MARKER_FILL_COLOR, this.lowestMarkerFillColor);
        outState.putInt(ABOVE_AVERAGE_PACE_COLOR, this.aboveAveragePaceColor);
        outState.putInt(BELOW_AVERAGE_PACE_COLOR, this.belowAveragePaceColor);
        outState.putString(PATH_TO_GLYPH_TYPEFACE, this.pathToGlyphTypeface);
        outState.putString(PATH_TO_AXIS_TICK_LABEL_TYPEFACE, this.pathToAxisTickLabelTypeface);
        outState.putString(PATH_TO_AVERAGE_PACE_LINE_LABEL_TYPEFACE, this.pathToAveragePaceLineLabelTypeface);
        outState.putString(PATH_TO_SLEEP_FLAG_TYPEFACE, this.pathToSleepFlagTypeface);
        outState.putString(PATH_TO_DATA_POINT_FLAG_TYPEFACE, this.pathToDataPointFlagTypeface);
        outState.putString(PATH_TO_ELEVATION_ICON_TEXT_TYPEFACE, this.pathToElevationIconTextTypeface);
    }

    @Override // com.shinobicontrols.kcompanionapp.charts.ChartTheme
    public int getChartBackgroundColor() {
        return this.chartBackgroundColor;
    }

    @Override // com.shinobicontrols.kcompanionapp.charts.ChartTheme
    public int getSelectedIconColor() {
        return this.selectedIconColor;
    }

    @Override // com.shinobicontrols.kcompanionapp.charts.ChartTheme
    public int getUnselectedIconColor() {
        return this.unselectedIconColor;
    }

    @Override // com.shinobicontrols.kcompanionapp.charts.ChartTheme
    public int getAxisTickLabelColor() {
        return this.axisTickLabelColor;
    }

    @Override // com.shinobicontrols.kcompanionapp.charts.ChartTheme
    public int getAxisTickLineColor() {
        return this.axisTickLineColor;
    }

    @Override // com.shinobicontrols.kcompanionapp.charts.ChartTheme
    public int getAxisZoneTickLineColor() {
        return this.axisZoneTickLineColor;
    }

    @Override // com.shinobicontrols.kcompanionapp.charts.ChartTheme
    public int getAxisZoneTickLabelTitleColor() {
        return this.axisZoneTickLabelTitleColor;
    }

    @Override // com.shinobicontrols.kcompanionapp.charts.ChartTheme
    public int getDataLineColor() {
        return this.dataLineColor;
    }

    @Override // com.shinobicontrols.kcompanionapp.charts.ChartTheme
    public int getDataFillColor() {
        return this.dataFillColor;
    }

    @Override // com.shinobicontrols.kcompanionapp.charts.ChartTheme
    public int getGhostedDataLineColor() {
        return this.ghostedDataLineColor;
    }

    @Override // com.shinobicontrols.kcompanionapp.charts.ChartTheme
    public int getGhostedDataFillColor() {
        return this.ghostedDataFillColor;
    }

    @Override // com.shinobicontrols.kcompanionapp.charts.ChartTheme
    public int getDataBarFillColor() {
        return this.dataBarFillColor;
    }

    @Override // com.shinobicontrols.kcompanionapp.charts.ChartTheme
    public int getHighActivityDataBarFillColor() {
        return this.highActivityDataBarFillColor;
    }

    @Override // com.shinobicontrols.kcompanionapp.charts.ChartTheme
    public int getGoalMetDataBarFillColor() {
        return this.goalMetDataBarFillColor;
    }

    @Override // com.shinobicontrols.kcompanionapp.charts.ChartTheme
    public int getGhostedDataBarFillColor() {
        return this.ghostedDataBarFillColor;
    }

    @Override // com.shinobicontrols.kcompanionapp.charts.ChartTheme
    public int getEmptyDataColor() {
        return this.emptyDataColor;
    }

    @Override // com.shinobicontrols.kcompanionapp.charts.ChartTheme
    public int getAnnotationPeakColor() {
        return this.annotationPeakColor;
    }

    @Override // com.shinobicontrols.kcompanionapp.charts.ChartTheme
    public int getAnnotationAverageColor() {
        return this.annotationAverageColor;
    }

    @Override // com.shinobicontrols.kcompanionapp.charts.ChartTheme
    public int getAnnotationLowColor() {
        return this.annotationLowColor;
    }

    @Override // com.shinobicontrols.kcompanionapp.charts.ChartTheme
    public int getAveragePaceLineColor() {
        return this.averagePaceLineColor;
    }

    @Override // com.shinobicontrols.kcompanionapp.charts.ChartTheme
    public int getMileMarkerColor() {
        return this.mileMarkerColor;
    }

    @Override // com.shinobicontrols.kcompanionapp.charts.ChartTheme
    public int getActivityGoalMetColor() {
        return this.activityGoalMetColor;
    }

    @Override // com.shinobicontrols.kcompanionapp.charts.ChartTheme
    public int getAwakeSleepColor() {
        return this.awakeSleepColor;
    }

    @Override // com.shinobicontrols.kcompanionapp.charts.ChartTheme
    public int getLightSleepColor() {
        return this.lightSleepColor;
    }

    @Override // com.shinobicontrols.kcompanionapp.charts.ChartTheme
    public int getRestfulSleepColor() {
        return this.restfulSleepColor;
    }

    @Override // com.shinobicontrols.kcompanionapp.charts.ChartTheme
    public int getSleepNightTimeBackgroundColor() {
        return this.sleepNightTimeBackgroundColor;
    }

    @Override // com.shinobicontrols.kcompanionapp.charts.ChartTheme
    public int getAverageHeartRateLineColor() {
        return this.averageHeartRateLineColor;
    }

    @Override // com.shinobicontrols.kcompanionapp.charts.ChartTheme
    public int getFellAsleepFlagColor() {
        return this.fellAsleepColor;
    }

    @Override // com.shinobicontrols.kcompanionapp.charts.ChartTheme
    public int getFellAsleepFlagTextColor() {
        return this.fellAsleepTextColor;
    }

    @Override // com.shinobicontrols.kcompanionapp.charts.ChartTheme
    public int getWokeUpFlagColor() {
        return this.wokeUpColor;
    }

    @Override // com.shinobicontrols.kcompanionapp.charts.ChartTheme
    public int getWokeUpFlagTextColor() {
        return this.wokeUpTextColor;
    }

    @Override // com.shinobicontrols.kcompanionapp.charts.ChartTheme
    public int getPeakDataPointFlagColor() {
        return this.peakDataPointFlagColor;
    }

    @Override // com.shinobicontrols.kcompanionapp.charts.ChartTheme
    public int getPeakDataPointFlagTextColor() {
        return this.peakDataPointFlagTextColor;
    }

    @Override // com.shinobicontrols.kcompanionapp.charts.ChartTheme
    public int getAverageDataPointFlagColor() {
        return this.averageDataPointFlagColor;
    }

    @Override // com.shinobicontrols.kcompanionapp.charts.ChartTheme
    public int getAverageDataPointFlagTextColor() {
        return this.averageDataPointFlagTextColor;
    }

    @Override // com.shinobicontrols.kcompanionapp.charts.ChartTheme
    public int getLowestDataPointFlagColor() {
        return this.lowestDataPointFlagColor;
    }

    @Override // com.shinobicontrols.kcompanionapp.charts.ChartTheme
    public int getLowestDataPointFlagTextColor() {
        return this.lowestDataPointFlagTextColor;
    }

    @Override // com.shinobicontrols.kcompanionapp.charts.ChartTheme
    public int getPeakMarkerStrokeColor() {
        return this.peakMarkerStrokeColor;
    }

    @Override // com.shinobicontrols.kcompanionapp.charts.ChartTheme
    public int getPeakMarkerFillColor() {
        return this.peakMarkerFillColor;
    }

    @Override // com.shinobicontrols.kcompanionapp.charts.ChartTheme
    public int getAverageMarkerStrokeColor() {
        return this.averageMarkerStrokeColor;
    }

    @Override // com.shinobicontrols.kcompanionapp.charts.ChartTheme
    public int getAverageMarkerFillColor() {
        return this.averageMarkerFillColor;
    }

    @Override // com.shinobicontrols.kcompanionapp.charts.ChartTheme
    public int getLowestMarkerStrokeColor() {
        return this.lowestMarkerStrokeColor;
    }

    @Override // com.shinobicontrols.kcompanionapp.charts.ChartTheme
    public int getLowestMarkerFillColor() {
        return this.lowestMarkerFillColor;
    }

    @Override // com.shinobicontrols.kcompanionapp.charts.ChartTheme
    public int getAboveAveragePaceColor() {
        return this.aboveAveragePaceColor;
    }

    @Override // com.shinobicontrols.kcompanionapp.charts.ChartTheme
    public int getBelowAveragePaceColor() {
        return this.belowAveragePaceColor;
    }

    @Override // com.shinobicontrols.kcompanionapp.charts.ChartTheme
    public String getPathToGlyphTypeface() {
        return this.pathToGlyphTypeface;
    }

    @Override // com.shinobicontrols.kcompanionapp.charts.ChartTheme
    public String getPathToAxisTickLabelTypeface() {
        return this.pathToAxisTickLabelTypeface;
    }

    @Override // com.shinobicontrols.kcompanionapp.charts.ChartTheme
    public String getPathToAveragePaceLineLabelTypeface() {
        return this.pathToAveragePaceLineLabelTypeface;
    }

    @Override // com.shinobicontrols.kcompanionapp.charts.ChartTheme
    public String getPathToSleepFlagTypeface() {
        return this.pathToSleepFlagTypeface;
    }

    @Override // com.shinobicontrols.kcompanionapp.charts.ChartTheme
    public String getPathToDataPointFlagTypeface() {
        return this.pathToDataPointFlagTypeface;
    }

    @Override // com.shinobicontrols.kcompanionapp.charts.ChartTheme
    public String getPathToElevationIconTextTypeface() {
        return this.pathToElevationIconTextTypeface;
    }
}

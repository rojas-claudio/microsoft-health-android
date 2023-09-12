package com.shinobicontrols.kcompanionapp.charts.internal;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.microsoft.kapp.R;
import com.microsoft.kapp.utils.KAppDateFormatter;
import com.shinobicontrols.charts.Axis;
import com.shinobicontrols.charts.ChartView;
import com.shinobicontrols.charts.ShinobiChart;
import com.shinobicontrols.charts.TickMark;
import com.shinobicontrols.kcompanionapp.charts.ChartTheme;
import com.shinobicontrols.kcompanionapp.charts.DataProvider;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
/* loaded from: classes.dex */
public abstract class BaseChartFragment extends Fragment implements Provideable {
    private static final int DEFAULT_MAPPING_NUMBER_TO_LOAD = 0;
    private static final String SELECTED_MAPPING = "SelectedMapping";
    private ChartManager chartManager;
    private DataProvider dataProvider;
    private ChartView mChartView;
    private boolean setup = false;
    private ChartThemeCache themeCache;

    @Override // android.support.v4.app.Fragment
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override // android.support.v4.app.Fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.shinobicharts_fragment_chart, container, false);
        this.mChartView = (ChartView) rootView.findViewById(R.id.chart);
        ShinobiChart chart = this.mChartView.getShinobiChart();
        this.chartManager = new ChartManager(chart, getResources());
        if (savedInstanceState != null) {
            this.themeCache = new ChartThemeCache(savedInstanceState);
        }
        if (this.themeCache == null) {
            this.themeCache = new ChartThemeCache(getResources());
        }
        chart.getStyle().setBackgroundColor(this.themeCache.getChartBackgroundColor());
        chart.getStyle().setCanvasBackgroundColor(this.themeCache.getChartBackgroundColor());
        chart.getStyle().setPlotAreaBackgroundColor(this.themeCache.getChartBackgroundColor());
        chart.setOnTickMarkUpdateListener(new ShinobiChart.OnTickMarkUpdateListener() { // from class: com.shinobicontrols.kcompanionapp.charts.internal.BaseChartFragment.1
            @Override // com.shinobicontrols.charts.ShinobiChart.OnTickMarkUpdateListener
            public void onUpdateTickMark(TickMark tickMark, Axis<?, ?> axis) {
                if (axis instanceof StyledNumberAxis) {
                    StyledNumberAxis styledAxis = (StyledNumberAxis) axis;
                    styledAxis.getTickLabelUpdateStrategy().onUpdateTickMark(BaseChartFragment.this.getResources(), tickMark, styledAxis);
                }
            }
        });
        chart.setOnTickMarkDrawListener(new ShinobiChart.OnTickMarkDrawListener() { // from class: com.shinobicontrols.kcompanionapp.charts.internal.BaseChartFragment.2
            @Override // com.shinobicontrols.charts.ShinobiChart.OnTickMarkDrawListener
            public void onDrawTickMark(Canvas canvas, TickMark tickMark, Rect labelBackgroundRect, Rect tickMarkRect, Axis<?, ?> axis) {
                if (axis instanceof StyledNumberAxis) {
                    StyledNumberAxis styledAxis = (StyledNumberAxis) axis;
                    styledAxis.getTickMarkDrawingStrategy().onDrawTickMark(BaseChartFragment.this.getResources(), canvas, tickMark, labelBackgroundRect, tickMarkRect, styledAxis);
                }
            }
        });
        rootView.setOnClickListener(new View.OnClickListener() { // from class: com.shinobicontrols.kcompanionapp.charts.internal.BaseChartFragment.3
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                BaseChartFragment.this.onTouched();
            }
        });
        chart.setOnGestureListener(new ShinobiGestureListener(this));
        return rootView;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean isReadyForReload() {
        return this.dataProvider != null && this.setup;
    }

    @Override // android.support.v4.app.Fragment
    public void onViewCreated(View view, Bundle savedInstanceState) {
        int selectedMapping;
        int initialMappingToLoad = 0;
        if (savedInstanceState != null && (selectedMapping = savedInstanceState.getInt(SELECTED_MAPPING, -1)) != -1) {
            initialMappingToLoad = selectedMapping;
        }
        this.chartManager.load(initialMappingToLoad);
        this.setup = true;
        applyTheme();
        reload();
    }

    @Override // android.support.v4.app.Fragment
    public void onPause() {
        super.onPause();
        pauseChartView();
    }

    public void pauseChartView() {
        if (getView() != null) {
            ChartView chartView = (ChartView) getView().findViewById(R.id.chart);
            chartView.onPause();
        }
    }

    @Override // android.support.v4.app.Fragment
    public void onResume() {
        super.onResume();
        resumeChartView();
    }

    public void resumeChartView() {
        if (getView() != null) {
            ChartView chartView = (ChartView) getView().findViewById(R.id.chart);
            chartView.onResume();
        }
    }

    @Override // android.support.v4.app.Fragment
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SELECTED_MAPPING, this.chartManager != null ? this.chartManager.getSelectedMappingPosition() : 0);
        if (this.themeCache != null) {
            this.themeCache.onSaveInstaceState(outState);
        }
    }

    public void onTouched() {
        this.chartManager.selectNext();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public DataProvider getDataProvider() {
        return this.dataProvider;
    }

    @Override // com.shinobicontrols.kcompanionapp.charts.internal.Provideable
    public void setDataProvider(DataProvider dataProvider) {
        this.dataProvider = dataProvider;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public ChartManager getChartManager() {
        return this.chartManager;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public ChartThemeCache getChartThemeCache() {
        return this.themeCache;
    }

    public void applyTheme(ChartTheme theme) {
        if (theme == null) {
            this.themeCache = new ChartThemeCache(getResources());
        } else {
            this.themeCache = new ChartThemeCache(theme);
        }
        applyTheme();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void applyTheme() {
        View rootView = getView();
        if (rootView != null) {
            rootView.setBackgroundColor(this.themeCache.getChartBackgroundColor());
        }
    }

    public void takeSnapShot(ShinobiChart.OnSnapshotDoneListener onSnapshotDoneListener) {
        if (onSnapshotDoneListener != null) {
            ShinobiChart shinobiChart = this.mChartView.getShinobiChart();
            shinobiChart.setOnSnapshotDoneListener(onSnapshotDoneListener);
            shinobiChart.requestSnapshot();
        }
    }

    public ChartView getChart() {
        return this.mChartView;
    }

    public String getLabelForDate(DateTime dateTime) {
        String weekDate = KAppDateFormatter.formatToMonthDay(dateTime);
        DateTimeFormatter weekDayNameFormatter = DateTimeFormat.forPattern(getResources().getString(R.string.shinobicharts_steps_weekly_day_label_format));
        if (isToday(dateTime)) {
            String labelText = getResources().getString(R.string.shinobicharts_steps_weekly_date_header_format, dateTime.toString(weekDayNameFormatter), getResources().getString(R.string.shinobicharts_today));
            return labelText;
        }
        String labelText2 = getResources().getString(R.string.shinobicharts_steps_weekly_date_header_format, dateTime.toString(weekDayNameFormatter), weekDate);
        return labelText2;
    }

    public boolean isToday(DateTime dateTime) {
        DateTime now = DateTime.now();
        LocalDate today = now.toLocalDate();
        LocalDate tomorrow = today.plusDays(1);
        DateTime startOfToday = today.toDateTimeAtStartOfDay(now.getZone());
        DateTime startOfTomorrow = tomorrow.toDateTimeAtStartOfDay(now.getZone());
        if (dateTime.isEqual(startOfToday)) {
            return true;
        }
        return dateTime.isAfter(startOfToday) && dateTime.isBefore(startOfTomorrow);
    }
}

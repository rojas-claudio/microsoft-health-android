package com.shinobicontrols.kcompanionapp.charts;

import android.content.Context;
import android.widget.FrameLayout;
import com.shinobicontrols.charts.ShinobiChart;
import com.shinobicontrols.kcompanionapp.charts.internal.ChartThemeCache;
import com.shinobicontrols.kcompanionapp.charts.internal.FlagView;
import com.shinobicontrols.kcompanionapp.charts.internal.FlagViewCreator;
import com.shinobicontrols.kcompanionapp.charts.internal.PauseLinesView;
import com.shinobicontrols.kcompanionapp.properties.ChartConfig;
import com.shinobicontrols.kcompanionapp.properties.ChartDataProperties;
import com.shinobicontrols.kcompanionapp.providers.YAxisStrategyProvider;
import org.apache.commons.lang3.Validate;
/* loaded from: classes.dex */
public abstract class BaseChart implements IChartView {
    private String mChartTitle;
    private ChartConfig mConfig;
    private final ChartDataProperties mDataProperties;
    protected ChartThemeCache mThemeCache;
    protected YAxisStrategyProvider mYAxisStrategy;

    public BaseChart(ChartConfig config, YAxisStrategyProvider yAxisStrategyProvider, ChartDataProperties dataProperties, String chartTitle) {
        Validate.notNull(config, "config", new Object[0]);
        this.mConfig = config;
        this.mChartTitle = chartTitle;
        this.mYAxisStrategy = yAxisStrategyProvider;
        this.mDataProperties = dataProperties;
    }

    public int getMaxHeartRate() {
        return this.mConfig.getMaxHeartRate();
    }

    public int getAge() {
        return this.mConfig.getAge();
    }

    public Context getContext() {
        return this.mConfig.getContext();
    }

    public String getChartTitle() {
        return this.mChartTitle;
    }

    public boolean isMetric() {
        return this.mConfig.isMetric();
    }

    public ChartDataProperties getChartDataProperties() {
        return this.mDataProperties;
    }

    @Override // com.shinobicontrols.kcompanionapp.charts.IChartView
    public void setChartThemeCache(ChartThemeCache chartThemeCache) {
        Validate.notNull(chartThemeCache, "Chart theme cache", new Object[0]);
        this.mThemeCache = chartThemeCache;
    }

    @Override // com.shinobicontrols.kcompanionapp.charts.IChartView
    public void onChartViewCreated(ShinobiChart chart) {
    }

    public FlagView createFlagView(String icon) {
        FlagView view = FlagViewCreator.createDataPointFlag(getContext(), icon, null, this.mThemeCache.getPeakDataPointFlagColor(), this.mThemeCache.getPeakDataPointFlagTextColor(), this.mThemeCache.getPathToDataPointFlagTypeface(), this.mThemeCache.getPathToGlyphTypeface());
        view.setVisibility(8);
        return view;
    }

    public PauseLinesView createPauseLineView(FrameLayout chartHolder) {
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(-1, -1);
        PauseLinesView view = new PauseLinesView(getContext(), this.mThemeCache);
        view.setBackgroundColor(0);
        view.setVisibility(8);
        chartHolder.addView(view, 0, lp);
        chartHolder.bringChildToFront(view);
        return view;
    }
}

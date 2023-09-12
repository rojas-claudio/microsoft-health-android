package com.shinobicontrols.kcompanionapp.charts.internal;

import android.content.res.Resources;
import android.view.View;
import com.microsoft.kapp.R;
import com.shinobicontrols.charts.Axis;
import com.shinobicontrols.charts.Series;
import com.shinobicontrols.charts.ShinobiChart;
import com.shinobicontrols.charts.TickStyle;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes.dex */
public class ChartManager {
    private static final float HIDDEN_AXIS_WIDTH = 0.0f;
    private static final int HIDE_LAST_INDEX = 2;
    private static final int HIDE_MAPPING_COUNT = 3;
    private static final int HIDE_MAPPING_INDEX = 1;
    private final ShinobiChart chart;
    private final float defaultAxisWidth;
    private OnAxisMappingSelectedListener onSelectedListener;
    private final List<AxisMapping> axisMappings = new ArrayList();
    private AxisMapping selectedMapping = null;

    /* loaded from: classes.dex */
    public interface OnAxisMappingSelectedListener {
        void onAxisMappingSelected(AxisMapping axisMapping);
    }

    public ChartManager(ShinobiChart chart, Resources resources) {
        this.chart = chart;
        this.defaultAxisWidth = resources.getDimensionPixelSize(R.dimen.shinobicharts_axis_width);
    }

    public void selectNext() {
        int next = (this.axisMappings.indexOf(this.selectedMapping) + 1) % this.axisMappings.size();
        select(next);
    }

    public void select(int position) {
        if (this.axisMappings.size() == 3) {
            setAxisHidden(this.axisMappings.get(1), position == 2);
        }
        if (this.selectedMapping != null) {
            deselect(this.selectedMapping);
        }
        if (this.axisMappings.size() != 0) {
            if (position >= this.axisMappings.size()) {
                position = 0;
            }
            this.selectedMapping = this.axisMappings.get(position);
            select(this.selectedMapping);
            if (this.onSelectedListener != null) {
                this.onSelectedListener.onAxisMappingSelected(this.selectedMapping);
            }
            this.chart.redrawChart();
        }
    }

    private void select(AxisMapping axisMapping) {
        showAxis(axisMapping.getAxis(), axisMapping.getAxisWidth());
        for (Series<?> series : axisMapping.getSeriesList()) {
            series.setSelected(true);
            series.setHidden(false);
            this.chart.removeSeries(series);
            this.chart.addSeries(series, this.chart.getXAxis(), axisMapping.getAxis());
        }
        View icon = axisMapping.getIcon();
        if (icon != null) {
            icon.setSelected(true);
        }
        for (View view : axisMapping.getViewToShowWhenSelected()) {
            view.setVisibility(0);
        }
    }

    private void setAxisHidden(AxisMapping axisMapping, boolean hidden) {
        for (Series<?> series : axisMapping.getSeriesList()) {
            series.setHidden(hidden);
        }
    }

    private void deselect(AxisMapping axisMapping) {
        hideAxis(axisMapping.getAxis());
        for (Series<?> series : axisMapping.getSeriesList()) {
            series.setSelected(false);
        }
        View icon = axisMapping.getIcon();
        if (icon != null) {
            icon.setSelected(false);
        }
        for (View view : axisMapping.getViewToShowWhenSelected()) {
            view.setVisibility(8);
        }
    }

    private void showAxis(Axis<?, ?> axis, float axisWidth) {
        if (axisWidth <= 0.0f) {
            axisWidth = this.defaultAxisWidth;
        }
        axis.setWidth(Float.valueOf(axisWidth));
        TickStyle tickStyle = axis.getStyle().getTickStyle();
        tickStyle.setLabelsShown(true);
        tickStyle.setMajorTicksShown(true);
    }

    private void hideAxis(Axis<?, ?> axis) {
        axis.setWidth(Float.valueOf(0.0f));
        TickStyle tickStyle = axis.getStyle().getTickStyle();
        tickStyle.setLabelsShown(false);
        tickStyle.setMajorTicksShown(false);
    }

    public void addMapping(AxisMapping axisMapping) {
        this.axisMappings.add(axisMapping);
    }

    public void load(int initialPosition) {
        for (AxisMapping axisMapping : this.axisMappings) {
            for (Series<?> series : axisMapping.getSeriesList()) {
                this.chart.addSeries(series, this.chart.getXAxis(), axisMapping.getAxis());
            }
            deselect(axisMapping);
        }
        select(initialPosition);
    }

    public int getSelectedMappingPosition() {
        return this.axisMappings.indexOf(this.selectedMapping);
    }

    public void setOnSelectedListener(OnAxisMappingSelectedListener onSelectedListener) {
        this.onSelectedListener = onSelectedListener;
    }
}

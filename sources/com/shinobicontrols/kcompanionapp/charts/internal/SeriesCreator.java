package com.shinobicontrols.kcompanionapp.charts.internal;

import android.content.res.Resources;
import android.util.TypedValue;
import com.microsoft.kapp.R;
import com.shinobicontrols.charts.ColumnSeries;
import com.shinobicontrols.charts.ColumnSeriesStyle;
import com.shinobicontrols.charts.LineSeries;
import com.shinobicontrols.charts.LineSeriesStyle;
import com.shinobicontrols.charts.PointStyle;
import com.shinobicontrols.charts.Series;
import com.shinobicontrols.charts.SeriesStyle;
/* loaded from: classes.dex */
public abstract class SeriesCreator {
    public static final SeriesCreator BELOW_TARGET_COLUMN = new SeriesCreator() { // from class: com.shinobicontrols.kcompanionapp.charts.internal.SeriesCreator.1
        @Override // com.shinobicontrols.kcompanionapp.charts.internal.SeriesCreator
        public Series<?> create(Resources resources, ChartThemeCache themeCache) {
            ColumnSeries columnSeries = new ColumnSeries();
            columnSeries.setStackId(0);
            ColumnSeriesStyle style = (ColumnSeriesStyle) columnSeries.getStyle();
            style.setAreaColor(themeCache.getGhostedDataBarFillColor());
            style.setFillStyle(SeriesStyle.FillStyle.FLAT);
            style.setLineShown(false);
            ColumnSeriesStyle selectedStyle = (ColumnSeriesStyle) columnSeries.getSelectedStyle();
            selectedStyle.setAreaColor(themeCache.getDataBarFillColor());
            selectedStyle.setFillStyle(SeriesStyle.FillStyle.FLAT);
            selectedStyle.setLineShown(false);
            return columnSeries;
        }

        @Override // com.shinobicontrols.kcompanionapp.charts.internal.SeriesCreator
        public void applyTheme(Series<?> series, ChartThemeCache themeCache) {
            ColumnSeries columnSeries = (ColumnSeries) series;
            ColumnSeriesStyle style = (ColumnSeriesStyle) columnSeries.getStyle();
            style.setAreaColor(themeCache.getGhostedDataBarFillColor());
            ColumnSeriesStyle selectedStyle = (ColumnSeriesStyle) columnSeries.getSelectedStyle();
            selectedStyle.setAreaColor(themeCache.getDataBarFillColor());
        }
    };
    public static final SeriesCreator ABOVE_TARGET_DAILY_COLUMN = new SeriesCreator() { // from class: com.shinobicontrols.kcompanionapp.charts.internal.SeriesCreator.2
        @Override // com.shinobicontrols.kcompanionapp.charts.internal.SeriesCreator
        public Series<?> create(Resources resources, ChartThemeCache themeCache) {
            ColumnSeries columnSeries = new ColumnSeries();
            columnSeries.setStackId(0);
            ColumnSeriesStyle style = (ColumnSeriesStyle) columnSeries.getStyle();
            style.setAreaColor(themeCache.getGhostedDataBarFillColor());
            style.setFillStyle(SeriesStyle.FillStyle.FLAT);
            style.setLineShown(false);
            ColumnSeriesStyle selectedStyle = (ColumnSeriesStyle) columnSeries.getSelectedStyle();
            selectedStyle.setAreaColor(themeCache.getHighActivityDataBarFillColor());
            selectedStyle.setFillStyle(SeriesStyle.FillStyle.FLAT);
            selectedStyle.setLineShown(false);
            return columnSeries;
        }

        @Override // com.shinobicontrols.kcompanionapp.charts.internal.SeriesCreator
        public void applyTheme(Series<?> series, ChartThemeCache themeCache) {
            ColumnSeries columnSeries = (ColumnSeries) series;
            ColumnSeriesStyle style = (ColumnSeriesStyle) columnSeries.getStyle();
            style.setAreaColor(themeCache.getGhostedDataBarFillColor());
            ColumnSeriesStyle selectedStyle = (ColumnSeriesStyle) columnSeries.getSelectedStyle();
            selectedStyle.setAreaColor(themeCache.getHighActivityDataBarFillColor());
        }
    };
    public static final SeriesCreator ABOVE_TARGET_WEEKLY_COLUMN = new SeriesCreator() { // from class: com.shinobicontrols.kcompanionapp.charts.internal.SeriesCreator.3
        @Override // com.shinobicontrols.kcompanionapp.charts.internal.SeriesCreator
        public Series<?> create(Resources resources, ChartThemeCache themeCache) {
            ColumnSeries columnSeries = new ColumnSeries();
            columnSeries.setStackId(0);
            ColumnSeriesStyle style = (ColumnSeriesStyle) columnSeries.getStyle();
            style.setAreaColor(themeCache.getGhostedDataBarFillColor());
            style.setFillStyle(SeriesStyle.FillStyle.FLAT);
            style.setLineShown(false);
            ColumnSeriesStyle selectedStyle = (ColumnSeriesStyle) columnSeries.getSelectedStyle();
            selectedStyle.setAreaColor(themeCache.getHighActivityDataBarFillColor());
            selectedStyle.setFillStyle(SeriesStyle.FillStyle.FLAT);
            selectedStyle.setLineShown(false);
            return columnSeries;
        }

        @Override // com.shinobicontrols.kcompanionapp.charts.internal.SeriesCreator
        public void applyTheme(Series<?> series, ChartThemeCache themeCache) {
            ColumnSeries columnSeries = (ColumnSeries) series;
            ColumnSeriesStyle style = (ColumnSeriesStyle) columnSeries.getStyle();
            style.setAreaColor(themeCache.getGhostedDataBarFillColor());
            ColumnSeriesStyle selectedStyle = (ColumnSeriesStyle) columnSeries.getSelectedStyle();
            selectedStyle.setAreaColor(themeCache.getHighActivityDataBarFillColor());
        }
    };
    public static final SeriesCreator SOLID_COLOR_COLUMN = new SeriesCreator() { // from class: com.shinobicontrols.kcompanionapp.charts.internal.SeriesCreator.4
        @Override // com.shinobicontrols.kcompanionapp.charts.internal.SeriesCreator
        public Series<?> create(Resources resources, ChartThemeCache themeCache) {
            ColumnSeries series = new ColumnSeries();
            series.setStackId(1);
            ((ColumnSeriesStyle) series.getStyle()).setAreaColor(themeCache.getGhostedDataBarFillColor());
            ((ColumnSeriesStyle) series.getStyle()).setAreaColorBelowBaseline(themeCache.getGhostedDataBarFillColor());
            ((ColumnSeriesStyle) series.getStyle()).setLineShown(false);
            ((ColumnSeriesStyle) series.getStyle()).setFillStyle(SeriesStyle.FillStyle.FLAT);
            ((ColumnSeriesStyle) series.getSelectedStyle()).setLineShown(false);
            ((ColumnSeriesStyle) series.getSelectedStyle()).setFillStyle(SeriesStyle.FillStyle.FLAT);
            return series;
        }

        @Override // com.shinobicontrols.kcompanionapp.charts.internal.SeriesCreator
        public void applyTheme(Series<?> series, ChartThemeCache themeCache) {
            ColumnSeries columnSeries = (ColumnSeries) series;
            ColumnSeriesStyle style = (ColumnSeriesStyle) columnSeries.getStyle();
            style.setAreaColor(themeCache.getEmptyDataColor());
            ColumnSeriesStyle selectedStyle = (ColumnSeriesStyle) columnSeries.getSelectedStyle();
            selectedStyle.setAreaColor(themeCache.getEmptyDataColor());
        }
    };
    public static final SeriesCreator INVISIBLE_COLUMN = new SeriesCreator() { // from class: com.shinobicontrols.kcompanionapp.charts.internal.SeriesCreator.5
        @Override // com.shinobicontrols.kcompanionapp.charts.internal.SeriesCreator
        public Series<?> create(Resources resources, ChartThemeCache themeCache) {
            ColumnSeries series = new ColumnSeries();
            series.setStackId(1);
            ((ColumnSeriesStyle) series.getStyle()).setLineShown(false);
            ((ColumnSeriesStyle) series.getStyle()).setFillStyle(SeriesStyle.FillStyle.NONE);
            ((ColumnSeriesStyle) series.getSelectedStyle()).setLineShown(false);
            ((ColumnSeriesStyle) series.getSelectedStyle()).setFillStyle(SeriesStyle.FillStyle.NONE);
            return series;
        }

        @Override // com.shinobicontrols.kcompanionapp.charts.internal.SeriesCreator
        public void applyTheme(Series<?> series, ChartThemeCache themeCache) {
        }
    };
    public static final SeriesCreator EMPTY_DATA_COLUMN = new SeriesCreator() { // from class: com.shinobicontrols.kcompanionapp.charts.internal.SeriesCreator.6
        @Override // com.shinobicontrols.kcompanionapp.charts.internal.SeriesCreator
        public Series<?> create(Resources resources, ChartThemeCache themeCache) {
            ColumnSeries columnSeries = new ColumnSeries();
            columnSeries.setStackId(0);
            ColumnSeriesStyle style = (ColumnSeriesStyle) columnSeries.getStyle();
            style.setAreaColor(themeCache.getEmptyDataColor());
            style.setFillStyle(SeriesStyle.FillStyle.FLAT);
            style.setLineShown(false);
            ColumnSeriesStyle selectedStyle = (ColumnSeriesStyle) columnSeries.getSelectedStyle();
            selectedStyle.setAreaColor(themeCache.getEmptyDataColor());
            selectedStyle.setFillStyle(SeriesStyle.FillStyle.FLAT);
            selectedStyle.setLineShown(false);
            return columnSeries;
        }

        @Override // com.shinobicontrols.kcompanionapp.charts.internal.SeriesCreator
        public void applyTheme(Series<?> series, ChartThemeCache themeCache) {
            ColumnSeries columnSeries = (ColumnSeries) series;
            ColumnSeriesStyle style = (ColumnSeriesStyle) columnSeries.getStyle();
            style.setAreaColor(themeCache.getEmptyDataColor());
            ColumnSeriesStyle selectedStyle = (ColumnSeriesStyle) columnSeries.getSelectedStyle();
            selectedStyle.setAreaColor(themeCache.getEmptyDataColor());
        }
    };
    public static final SeriesCreator FILLED_LINE = new SeriesCreator() { // from class: com.shinobicontrols.kcompanionapp.charts.internal.SeriesCreator.7
        @Override // com.shinobicontrols.kcompanionapp.charts.internal.SeriesCreator
        public Series<?> create(Resources resources, ChartThemeCache themeCache) {
            LineSeries lineSeries = new LineSeries();
            LineSeriesStyle style = (LineSeriesStyle) lineSeries.getStyle();
            style.setLineColor(themeCache.getGhostedDataLineColor());
            style.setLineColorBelowBaseline(themeCache.getGhostedDataLineColor());
            float lineWidth = resources.getDimensionPixelSize(R.dimen.LargeLineSize);
            style.setAreaLineWidth(lineWidth / resources.getDisplayMetrics().density);
            style.setLineWidth(lineWidth / resources.getDisplayMetrics().density);
            LineSeriesStyle selectedStyle = (LineSeriesStyle) lineSeries.getSelectedStyle();
            selectedStyle.setLineColor(themeCache.getDataLineColor());
            selectedStyle.setLineColorBelowBaseline(themeCache.getDataLineColor());
            selectedStyle.setAreaLineColor(themeCache.getDataLineColor());
            selectedStyle.setAreaColor(themeCache.getDataFillColor());
            selectedStyle.setFillStyle(SeriesStyle.FillStyle.FLAT);
            float selectedLineWidth = resources.getDimensionPixelSize(R.dimen.LargeLineSize);
            selectedStyle.setAreaLineWidth(selectedLineWidth / resources.getDisplayMetrics().density);
            selectedStyle.setLineWidth(selectedLineWidth / resources.getDisplayMetrics().density);
            return lineSeries;
        }

        @Override // com.shinobicontrols.kcompanionapp.charts.internal.SeriesCreator
        public void applyTheme(Series<?> series, ChartThemeCache themeCache) {
            LineSeries lineSeries = (LineSeries) series;
            LineSeriesStyle style = (LineSeriesStyle) lineSeries.getStyle();
            style.setAreaLineColor(themeCache.getGhostedDataLineColor());
            style.setLineColor(themeCache.getGhostedDataLineColor());
            style.setAreaLineColorBelowBaseline(themeCache.getGhostedDataLineColor());
            style.setLineColorBelowBaseline(themeCache.getGhostedDataLineColor());
            style.setAreaColor(themeCache.getGhostedDataFillColor());
            style.setAreaColorBelowBaseline(themeCache.getGhostedDataFillColor());
            LineSeriesStyle selectedStyle = (LineSeriesStyle) lineSeries.getSelectedStyle();
            selectedStyle.setAreaLineColor(themeCache.getDataLineColor());
            selectedStyle.setAreaColor(themeCache.getDataFillColor());
        }
    };
    public static final SeriesCreator LINE = new SeriesCreator() { // from class: com.shinobicontrols.kcompanionapp.charts.internal.SeriesCreator.8
        @Override // com.shinobicontrols.kcompanionapp.charts.internal.SeriesCreator
        public Series<?> create(Resources resources, ChartThemeCache themeCache) {
            LineSeries lineSeries = new LineSeries();
            LineSeriesStyle style = (LineSeriesStyle) lineSeries.getStyle();
            style.setLineColor(themeCache.getGhostedDataLineColor());
            style.setLineColorBelowBaseline(themeCache.getGhostedDataLineColor());
            float lineWidth = resources.getDimensionPixelSize(R.dimen.LargeLineSize);
            style.setAreaLineWidth(lineWidth / resources.getDisplayMetrics().density);
            style.setLineWidth(lineWidth / resources.getDisplayMetrics().density);
            LineSeriesStyle selectedStyle = (LineSeriesStyle) lineSeries.getSelectedStyle();
            selectedStyle.setLineColor(themeCache.getDataLineColor());
            selectedStyle.setLineColorBelowBaseline(themeCache.getDataLineColor());
            float selectedLineWidth = resources.getDimensionPixelSize(R.dimen.LargeLineSize);
            selectedStyle.setAreaLineWidth(selectedLineWidth / resources.getDisplayMetrics().density);
            selectedStyle.setLineWidth(selectedLineWidth / resources.getDisplayMetrics().density);
            return lineSeries;
        }

        @Override // com.shinobicontrols.kcompanionapp.charts.internal.SeriesCreator
        public void applyTheme(Series<?> series, ChartThemeCache themeCache) {
            LineSeries lineSeries = (LineSeries) series;
            LineSeriesStyle style = (LineSeriesStyle) lineSeries.getStyle();
            style.setAreaLineColor(themeCache.getGhostedDataLineColor());
            style.setLineColor(themeCache.getGhostedDataLineColor());
            style.setAreaLineColorBelowBaseline(themeCache.getGhostedDataLineColor());
            style.setLineColorBelowBaseline(themeCache.getGhostedDataLineColor());
            style.setAreaColor(themeCache.getGhostedDataFillColor());
            style.setAreaColorBelowBaseline(themeCache.getGhostedDataFillColor());
            LineSeriesStyle selectedStyle = (LineSeriesStyle) lineSeries.getSelectedStyle();
            selectedStyle.setAreaLineColor(themeCache.getDataLineColor());
            selectedStyle.setAreaColor(themeCache.getDataFillColor());
        }
    };
    public static final SeriesCreator STEP_LINE = new SeriesCreator() { // from class: com.shinobicontrols.kcompanionapp.charts.internal.SeriesCreator.9
        @Override // com.shinobicontrols.kcompanionapp.charts.internal.SeriesCreator
        public Series<?> create(Resources resources, ChartThemeCache themeCache) {
            LineSeries lineSeries = new LineSeries();
            TypedValue outValue = new TypedValue();
            resources.getValue(R.id.shinobicharts_sleep_baseline, outValue, true);
            lineSeries.setBaseline(Float.valueOf(outValue.getFloat()));
            LineSeriesStyle style = (LineSeriesStyle) lineSeries.getStyle();
            style.setLineShown(false);
            style.setAreaColor(themeCache.getGhostedDataFillColor());
            style.setAreaColorBelowBaseline(themeCache.getGhostedDataFillColor());
            style.setFillStyle(SeriesStyle.FillStyle.FLAT);
            LineSeriesStyle selectedStyle = (LineSeriesStyle) lineSeries.getSelectedStyle();
            selectedStyle.setLineShown(false);
            selectedStyle.setFillStyle(SeriesStyle.FillStyle.FLAT);
            return lineSeries;
        }

        @Override // com.shinobicontrols.kcompanionapp.charts.internal.SeriesCreator
        public void applyTheme(Series<?> series, ChartThemeCache themeCache) {
            LineSeries lineSeries = (LineSeries) series;
            LineSeriesStyle style = (LineSeriesStyle) lineSeries.getStyle();
            style.setAreaColor(themeCache.getGhostedDataFillColor());
            style.setAreaColorBelowBaseline(themeCache.getGhostedDataFillColor());
        }
    };
    public static final SeriesCreator POINT = new SeriesCreator() { // from class: com.shinobicontrols.kcompanionapp.charts.internal.SeriesCreator.10
        @Override // com.shinobicontrols.kcompanionapp.charts.internal.SeriesCreator
        public Series<?> create(Resources resources, ChartThemeCache themeCache) {
            LineSeries lineSeries = new LineSeries();
            TypedValue outValue = new TypedValue();
            resources.getValue(R.id.shinobicharts_sleep_baseline, outValue, true);
            lineSeries.setBaseline(Float.valueOf(outValue.getFloat()));
            LineSeriesStyle style = (LineSeriesStyle) lineSeries.getStyle();
            style.setLineShown(false);
            style.getPointStyle().setPointsShown(false);
            style.setFillStyle(SeriesStyle.FillStyle.NONE);
            LineSeriesStyle selectedStyle = (LineSeriesStyle) lineSeries.getSelectedStyle();
            selectedStyle.setLineShown(false);
            selectedStyle.setFillStyle(SeriesStyle.FillStyle.NONE);
            PointStyle pointStyle = selectedStyle.getPointStyle();
            pointStyle.setPointsShown(true);
            pointStyle.setInnerColor(themeCache.getPeakMarkerFillColor());
            pointStyle.setColor(themeCache.getPeakMarkerStrokeColor());
            float innerRadius = resources.getDimension(R.dimen.shinobicharts_marker_inner_radius);
            pointStyle.setInnerRadius(innerRadius / resources.getDisplayMetrics().density);
            float radius = resources.getDimension(R.dimen.shinobicharts_marker_radius);
            pointStyle.setRadius(radius / resources.getDisplayMetrics().density);
            return lineSeries;
        }

        @Override // com.shinobicontrols.kcompanionapp.charts.internal.SeriesCreator
        public void applyTheme(Series<?> series, ChartThemeCache themeCache) {
            LineSeries lineSeries = (LineSeries) series;
            LineSeriesStyle selectedStyle = (LineSeriesStyle) lineSeries.getSelectedStyle();
            PointStyle pointStyle = selectedStyle.getPointStyle();
            pointStyle.setInnerColor(themeCache.getPeakMarkerFillColor());
            pointStyle.setColor(themeCache.getPeakMarkerStrokeColor());
        }
    };

    public abstract void applyTheme(Series<?> series, ChartThemeCache chartThemeCache);

    public abstract Series<?> create(Resources resources, ChartThemeCache chartThemeCache);
}

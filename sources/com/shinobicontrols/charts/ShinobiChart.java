package com.shinobicontrols.charts;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import java.util.List;
/* loaded from: classes.dex */
public interface ShinobiChart {

    /* loaded from: classes.dex */
    public interface OnAxisMotionStateChangeListener {
        void onAxisMotionStateChange(Axis<?, ?> axis);
    }

    /* loaded from: classes.dex */
    public interface OnAxisRangeChangeListener {
        void onAxisRangeChange(Axis<?, ?> axis);
    }

    /* loaded from: classes.dex */
    public interface OnCrosshairActivationStateChangedListener {
        void onCrosshairActivationStateChanged(ShinobiChart shinobiChart);
    }

    /* loaded from: classes.dex */
    public interface OnCrosshairDrawListener {
        void onDrawCrosshair(ShinobiChart shinobiChart, Canvas canvas, Rect rect, float f, float f2, float f3, Paint paint);
    }

    /* loaded from: classes.dex */
    public interface OnGestureListener {
        void onDoubleTapDown(ShinobiChart shinobiChart, PointF pointF);

        void onDoubleTapUp(ShinobiChart shinobiChart, PointF pointF);

        void onLongTouchDown(ShinobiChart shinobiChart, PointF pointF);

        void onLongTouchUp(ShinobiChart shinobiChart, PointF pointF);

        void onPinch(ShinobiChart shinobiChart, PointF pointF, PointF pointF2, PointF pointF3);

        void onPinchEnd(ShinobiChart shinobiChart, PointF pointF, boolean z, PointF pointF2);

        void onSecondTouchDown(ShinobiChart shinobiChart, PointF pointF, PointF pointF2);

        void onSecondTouchUp(ShinobiChart shinobiChart, PointF pointF, PointF pointF2);

        void onSingleTouchDown(ShinobiChart shinobiChart, PointF pointF);

        void onSingleTouchUp(ShinobiChart shinobiChart, PointF pointF);

        void onSwipe(ShinobiChart shinobiChart, PointF pointF, PointF pointF2);

        void onSwipeEnd(ShinobiChart shinobiChart, PointF pointF, boolean z, PointF pointF2);
    }

    /* loaded from: classes.dex */
    public interface OnInternalLayoutListener {
        void onInternalLayout(ShinobiChart shinobiChart);
    }

    /* loaded from: classes.dex */
    public interface OnPieDonutSliceLabelDrawListener {
        void onDrawLabel(Canvas canvas, PieDonutSlice pieDonutSlice, Rect rect, PieDonutSeries<?> pieDonutSeries);
    }

    /* loaded from: classes.dex */
    public interface OnPieDonutSliceUpdateListener {
        void onUpdateSlice(PieDonutSlice pieDonutSlice, PieDonutSeries<?> pieDonutSeries);
    }

    /* loaded from: classes.dex */
    public interface OnSeriesAnimationListener {
        void onSeriesAnimationFinished(Series<?> series);
    }

    /* loaded from: classes.dex */
    public interface OnSeriesSelectionListener {
        void onPointSelectionStateChanged(Series<?> series, int i);

        void onSeriesSelectionStateChanged(Series<?> series);
    }

    /* loaded from: classes.dex */
    public interface OnSnapshotDoneListener {
        void onSnapshotDone(Bitmap bitmap);
    }

    /* loaded from: classes.dex */
    public interface OnTickMarkDrawListener {
        void onDrawTickMark(Canvas canvas, TickMark tickMark, Rect rect, Rect rect2, Axis<?, ?> axis);
    }

    /* loaded from: classes.dex */
    public interface OnTickMarkUpdateListener {
        void onUpdateTickMark(TickMark tickMark, Axis<?, ?> axis);
    }

    /* loaded from: classes.dex */
    public interface OnTrackingInfoChangedForCrosshairListener {
        void onTrackingInfoChanged(Crosshair crosshair, DataPoint<?, ?> dataPoint, DataPoint<?, ?> dataPoint2, DataPoint<?, ?> dataPoint3);
    }

    /* loaded from: classes.dex */
    public interface OnTrackingInfoChangedForTooltipListener {
        void onTrackingInfoChanged(Tooltip tooltip, DataPoint<?, ?> dataPoint, DataPoint<?, ?> dataPoint2, DataPoint<?, ?> dataPoint3);
    }

    void addSeries(Series<?> series);

    void addSeries(Series<?> series, Axis<?, ?> axis, Axis<?, ?> axis2);

    void addXAxis(Axis<?, ?> axis);

    void addYAxis(Axis<?, ?> axis);

    void applyTheme(int i, boolean z);

    List<Axis<?, ?>> getAllXAxes();

    List<Axis<?, ?>> getAllYAxes();

    AnnotationsManager getAnnotationsManager();

    Rect getCanvasRect();

    Crosshair getCrosshair();

    String getInfo();

    Legend getLegend();

    Rect getPlotAreaRect();

    List<Series<?>> getSeries();

    List<Series<?>> getSeriesForAxis(Axis<?, ?> axis);

    ChartStyle getStyle();

    String getTitle();

    MainTitleStyle getTitleStyle();

    Axis<?, ?> getXAxis();

    Axis<?, ?> getXAxisForSeries(Series<?> series);

    Axis<?, ?> getYAxis();

    Axis<?, ?> getYAxisForSeries(Series<?> series);

    boolean isSeriesSelectionSingle();

    void redrawChart();

    boolean removeSeries(Series<?> series);

    void removeXAxis(Axis<?, ?> axis);

    void removeYAxis(Axis<?, ?> axis);

    void requestSnapshot();

    void setHidden(List<Series<?>> list, boolean z);

    void setLicenseKey(String str);

    void setOnAxisMotionStateChangeListener(OnAxisMotionStateChangeListener onAxisMotionStateChangeListener);

    void setOnAxisRangeChangeListener(OnAxisRangeChangeListener onAxisRangeChangeListener);

    void setOnCrosshairActivationStateChangedListener(OnCrosshairActivationStateChangedListener onCrosshairActivationStateChangedListener);

    void setOnCrosshairDrawListener(OnCrosshairDrawListener onCrosshairDrawListener);

    void setOnGestureListener(OnGestureListener onGestureListener);

    void setOnInternalLayoutListener(OnInternalLayoutListener onInternalLayoutListener);

    void setOnPieDonutSliceLabelDrawListener(OnPieDonutSliceLabelDrawListener onPieDonutSliceLabelDrawListener);

    void setOnPieDonutSliceUpdateListener(OnPieDonutSliceUpdateListener onPieDonutSliceUpdateListener);

    void setOnSeriesAnimationListener(OnSeriesAnimationListener onSeriesAnimationListener);

    void setOnSeriesSelectionListener(OnSeriesSelectionListener onSeriesSelectionListener);

    void setOnSnapshotDoneListener(OnSnapshotDoneListener onSnapshotDoneListener);

    void setOnTickMarkDrawListener(OnTickMarkDrawListener onTickMarkDrawListener);

    void setOnTickMarkUpdateListener(OnTickMarkUpdateListener onTickMarkUpdateListener);

    void setOnTrackingInfoChangedForCrosshairListener(OnTrackingInfoChangedForCrosshairListener onTrackingInfoChangedForCrosshairListener);

    void setOnTrackingInfoChangedForTooltipListener(OnTrackingInfoChangedForTooltipListener onTrackingInfoChangedForTooltipListener);

    void setSeriesSelectionSingle(boolean z);

    void setStyle(ChartStyle chartStyle);

    void setTitle(String str);

    void setTitleStyle(MainTitleStyle mainTitleStyle);

    void setXAxis(Axis<?, ?> axis);

    void setYAxis(Axis<?, ?> axis);
}

package com.shinobicontrols.kcompanionapp.charts.internal;

import android.graphics.PointF;
import com.shinobicontrols.charts.ShinobiChart;
/* loaded from: classes.dex */
public class ShinobiGestureListener implements ShinobiChart.OnGestureListener {
    private final BaseChartFragment chartFragment;

    public ShinobiGestureListener(BaseChartFragment chartFragment) {
        this.chartFragment = chartFragment;
    }

    @Override // com.shinobicontrols.charts.ShinobiChart.OnGestureListener
    public void onDoubleTapDown(ShinobiChart arg0, PointF arg1) {
    }

    @Override // com.shinobicontrols.charts.ShinobiChart.OnGestureListener
    public void onDoubleTapUp(ShinobiChart arg0, PointF arg1) {
        this.chartFragment.onTouched();
    }

    @Override // com.shinobicontrols.charts.ShinobiChart.OnGestureListener
    public void onLongTouchDown(ShinobiChart arg0, PointF arg1) {
    }

    @Override // com.shinobicontrols.charts.ShinobiChart.OnGestureListener
    public void onLongTouchUp(ShinobiChart arg0, PointF arg1) {
    }

    @Override // com.shinobicontrols.charts.ShinobiChart.OnGestureListener
    public void onPinch(ShinobiChart arg0, PointF arg1, PointF arg2, PointF arg3) {
    }

    @Override // com.shinobicontrols.charts.ShinobiChart.OnGestureListener
    public void onPinchEnd(ShinobiChart arg0, PointF arg1, boolean arg2, PointF arg3) {
    }

    @Override // com.shinobicontrols.charts.ShinobiChart.OnGestureListener
    public void onSecondTouchDown(ShinobiChart arg0, PointF arg1, PointF arg2) {
    }

    @Override // com.shinobicontrols.charts.ShinobiChart.OnGestureListener
    public void onSecondTouchUp(ShinobiChart arg0, PointF arg1, PointF arg2) {
    }

    @Override // com.shinobicontrols.charts.ShinobiChart.OnGestureListener
    public void onSingleTouchDown(ShinobiChart arg0, PointF arg1) {
    }

    @Override // com.shinobicontrols.charts.ShinobiChart.OnGestureListener
    public void onSingleTouchUp(ShinobiChart arg0, PointF arg1) {
        this.chartFragment.onTouched();
    }

    @Override // com.shinobicontrols.charts.ShinobiChart.OnGestureListener
    public void onSwipe(ShinobiChart arg0, PointF arg1, PointF arg2) {
    }

    @Override // com.shinobicontrols.charts.ShinobiChart.OnGestureListener
    public void onSwipeEnd(ShinobiChart arg0, PointF arg1, boolean arg2, PointF arg3) {
    }
}

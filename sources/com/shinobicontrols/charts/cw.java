package com.shinobicontrols.charts;

import android.graphics.PointF;
import com.shinobicontrols.charts.Series;
import com.shinobicontrols.charts.ShinobiChart;
import com.shinobicontrols.charts.cn;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class cw implements ShinobiChart.OnGestureListener {
    private final v a;
    private final cn b;

    /* JADX INFO: Access modifiers changed from: package-private */
    public cw(v vVar) {
        this.a = vVar;
        this.b = new cn(vVar);
    }

    @Override // com.shinobicontrols.charts.ShinobiChart.OnGestureListener
    public void onDoubleTapDown(ShinobiChart sender, PointF position) {
    }

    @Override // com.shinobicontrols.charts.ShinobiChart.OnGestureListener
    public void onDoubleTapUp(ShinobiChart sender, PointF position) {
    }

    @Override // com.shinobicontrols.charts.ShinobiChart.OnGestureListener
    public void onLongTouchDown(ShinobiChart sender, PointF position) {
        a(position);
    }

    @Override // com.shinobicontrols.charts.ShinobiChart.OnGestureListener
    public void onLongTouchUp(ShinobiChart sender, PointF position) {
    }

    @Override // com.shinobicontrols.charts.ShinobiChart.OnGestureListener
    public void onSingleTouchDown(ShinobiChart sender, PointF position) {
    }

    @Override // com.shinobicontrols.charts.ShinobiChart.OnGestureListener
    public void onSingleTouchUp(ShinobiChart sender, PointF position) {
        c(position);
    }

    @Override // com.shinobicontrols.charts.ShinobiChart.OnGestureListener
    public void onSwipe(ShinobiChart sender, PointF startPosition, PointF endPosition) {
        b(endPosition);
    }

    @Override // com.shinobicontrols.charts.ShinobiChart.OnGestureListener
    public void onSwipeEnd(ShinobiChart sender, PointF position, boolean flinging, PointF velocity) {
    }

    @Override // com.shinobicontrols.charts.ShinobiChart.OnGestureListener
    public void onSecondTouchDown(ShinobiChart sender, PointF position, PointF position2) {
    }

    @Override // com.shinobicontrols.charts.ShinobiChart.OnGestureListener
    public void onSecondTouchUp(ShinobiChart sender, PointF position, PointF position2) {
    }

    @Override // com.shinobicontrols.charts.ShinobiChart.OnGestureListener
    public void onPinch(ShinobiChart sender, PointF startFocus, PointF endFocus, PointF scaleFactor) {
    }

    @Override // com.shinobicontrols.charts.ShinobiChart.OnGestureListener
    public void onPinchEnd(ShinobiChart sender, PointF focus, boolean flinging, PointF scaleFactor) {
    }

    private void a(PointF pointF) {
        if (this.a.h != null && this.a.r()) {
            Series.a a = this.b.a(pointF, cn.b.CROSSHAIR_ENABLED);
            if (Series.a.b(a)) {
                a(a);
            }
        }
    }

    private void b(PointF pointF) {
        Series.a a;
        Crosshair crosshair = this.a.h;
        if (crosshair != null) {
            switch (crosshair.c) {
                case SINGLE_SERIES:
                    a = this.b.a(crosshair.getTrackedSeries(), pointF, true);
                    break;
                case FLOATING:
                    a = this.b.a(pointF, cn.b.CROSSHAIR_ENABLED);
                    break;
                default:
                    a = null;
                    break;
            }
            if (Series.a.b(a)) {
                a(a);
            }
        }
    }

    private void a(Series.a aVar) {
        CartesianSeries<?> cartesianSeries = (CartesianSeries) aVar.c();
        InternalDataPoint b = aVar.b();
        DataPoint<?, ?> a = ab.a(b, cartesianSeries);
        DataPoint<?, ?> b2 = ab.b(b, cartesianSeries);
        bz d = aVar.d();
        this.a.h.a(cartesianSeries, a, b2, d != null ? ab.a(d, b, cartesianSeries) : null);
    }

    private void c(PointF pointF) {
        Crosshair crosshair = this.a.h;
        if (crosshair != null) {
            crosshair.e();
        }
    }
}

package com.shinobicontrols.charts;

import android.graphics.PointF;
import com.shinobicontrols.charts.Series;
import com.shinobicontrols.charts.ShinobiChart;
import com.shinobicontrols.charts.cn;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class cm implements ShinobiChart.OnGestureListener {
    boolean a;
    private final v b;
    private final cn c;

    /* JADX INFO: Access modifiers changed from: package-private */
    public cm(v vVar) {
        this.b = vVar;
        this.c = new cn(vVar);
    }

    @Override // com.shinobicontrols.charts.ShinobiChart.OnGestureListener
    public void onDoubleTapDown(ShinobiChart sender, PointF position) {
    }

    @Override // com.shinobicontrols.charts.ShinobiChart.OnGestureListener
    public void onDoubleTapUp(ShinobiChart sender, PointF position) {
    }

    @Override // com.shinobicontrols.charts.ShinobiChart.OnGestureListener
    public void onLongTouchDown(ShinobiChart sender, PointF position) {
    }

    @Override // com.shinobicontrols.charts.ShinobiChart.OnGestureListener
    public void onLongTouchUp(ShinobiChart sender, PointF position) {
    }

    @Override // com.shinobicontrols.charts.ShinobiChart.OnGestureListener
    public void onSingleTouchDown(ShinobiChart sender, PointF position) {
    }

    @Override // com.shinobicontrols.charts.ShinobiChart.OnGestureListener
    public void onSingleTouchUp(ShinobiChart sender, PointF position) {
        a(position);
    }

    @Override // com.shinobicontrols.charts.ShinobiChart.OnGestureListener
    public void onSwipe(ShinobiChart sender, PointF startPosition, PointF endPosition) {
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
        if (this.b.l()) {
            c(pointF);
        } else {
            b(pointF);
        }
    }

    private void b(PointF pointF) {
        Series.a a = this.c.a(pointF, cn.b.SELECTION_MODE_NOT_NONE);
        if (Series.a.b(a)) {
            CartesianSeries<?> cartesianSeries = (CartesianSeries) a.c();
            InternalDataPoint b = a.b();
            switch (cartesianSeries.s) {
                case POINT_SINGLE:
                case POINT_MULTIPLE:
                    synchronized (x.a) {
                        if (cartesianSeries.s == Series.SelectionMode.POINT_MULTIPLE || b.h) {
                            cartesianSeries.a(b, b.h ? false : true);
                        } else {
                            int length = cartesianSeries.n.c.length;
                            for (int i = 0; i < length; i++) {
                                InternalDataPoint internalDataPoint = cartesianSeries.n.c[i];
                                if (internalDataPoint != b) {
                                    cartesianSeries.a(internalDataPoint, false);
                                }
                            }
                            cartesianSeries.a(b, true);
                        }
                    }
                    break;
                case SERIES:
                    synchronized (x.a) {
                        if (!this.a) {
                            cartesianSeries.setSelected(cartesianSeries.d ? false : true);
                        } else if (cartesianSeries.d) {
                            cartesianSeries.setSelected(false);
                        } else {
                            for (CartesianSeries<?> cartesianSeries2 : this.b.h()) {
                                if (cartesianSeries2 != cartesianSeries) {
                                    cartesianSeries2.setSelected(false);
                                }
                            }
                            cartesianSeries.setSelected(true);
                        }
                    }
                    break;
            }
            if (cartesianSeries.s != Series.SelectionMode.NONE) {
                this.b.b.e();
                this.b.b.invalidate();
                this.b.redrawChart();
            }
        }
    }

    private void c(PointF pointF) {
        Series.a a = this.c.a(pointF);
        if (Series.a.b(a)) {
            PieDonutSeries<?> pieDonutSeries = (PieDonutSeries) a.c();
            PieDonutSlice pieDonutSlice = (PieDonutSlice) a.b();
            switch (pieDonutSeries.s) {
                case NONE:
                default:
                    return;
                case POINT_SINGLE:
                case POINT_MULTIPLE:
                    a(!pieDonutSlice.h, pieDonutSeries, pieDonutSlice, pieDonutSeries.s, true);
                    return;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a(boolean z, PieDonutSeries<?> pieDonutSeries, PieDonutSlice pieDonutSlice, Series.SelectionMode selectionMode, boolean z2) {
        boolean a;
        boolean z3 = false;
        synchronized (x.a) {
            if (selectionMode == Series.SelectionMode.POINT_SINGLE && !pieDonutSlice.h) {
                int length = pieDonutSeries.n.c.length;
                int i = 0;
                while (i < length) {
                    PieDonutSlice pieDonutSlice2 = (PieDonutSlice) pieDonutSeries.n.c[i];
                    i++;
                    z3 = pieDonutSlice2 != pieDonutSlice ? pieDonutSeries.a((InternalDataPoint) pieDonutSlice2, false) | z3 : z3;
                }
            }
            a = pieDonutSeries.a((InternalDataPoint) pieDonutSlice, z) | z3;
        }
        if (a) {
            pieDonutSeries.a(pieDonutSlice, z2);
        }
    }
}

package com.shinobicontrols.charts;

import android.graphics.PointF;
import android.os.Handler;
import android.os.Looper;
import com.shinobicontrols.charts.ShinobiChart;
import java.util.ArrayList;
import java.util.List;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class bn implements ShinobiChart.OnGestureListener {
    private ShinobiChart.OnGestureListener d;
    private ShinobiChart.OnGestureListener e;
    private final v f;
    private final y h;
    private final a a = new a(this);
    private final List<ShinobiChart.OnGestureListener> c = new ArrayList();
    private b g = b.NO_GESTURE;
    private final Handler b = new Handler(Looper.getMainLooper());

    /* loaded from: classes.dex */
    private enum b {
        NO_GESTURE,
        PANNING,
        ZOOMING
    }

    public bn(v vVar) {
        this.f = vVar;
        this.h = new y(vVar);
    }

    @Override // com.shinobicontrols.charts.ShinobiChart.OnGestureListener
    public void onDoubleTapDown(ShinobiChart sender, PointF position) {
        this.b.removeCallbacks(this.a);
    }

    @Override // com.shinobicontrols.charts.ShinobiChart.OnGestureListener
    public void onDoubleTapUp(ShinobiChart sender, PointF position) {
        for (ShinobiChart.OnGestureListener onGestureListener : this.c) {
            onGestureListener.onDoubleTapUp(sender, position);
        }
    }

    @Override // com.shinobicontrols.charts.ShinobiChart.OnGestureListener
    public void onLongTouchDown(ShinobiChart sender, PointF position) {
        this.d.onLongTouchDown(this.f, position);
    }

    @Override // com.shinobicontrols.charts.ShinobiChart.OnGestureListener
    public void onLongTouchUp(ShinobiChart sender, PointF position) {
    }

    @Override // com.shinobicontrols.charts.ShinobiChart.OnGestureListener
    public void onSingleTouchDown(ShinobiChart sender, PointF position) {
        if (this.f.h == null || !this.f.h.isActive()) {
            for (ShinobiChart.OnGestureListener onGestureListener : this.c) {
                onGestureListener.onSingleTouchDown(sender, position);
            }
        }
    }

    @Override // com.shinobicontrols.charts.ShinobiChart.OnGestureListener
    public void onSingleTouchUp(ShinobiChart sender, PointF position) {
        if (this.f.q()) {
            this.a.b = sender;
            this.a.c = position;
            this.b.postDelayed(this.a, this.h.d);
            return;
        }
        a(sender, position);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(ShinobiChart shinobiChart, PointF pointF) {
        if (this.f.h != null && this.f.h.isActive()) {
            this.d.onSingleTouchUp(shinobiChart, pointF);
        } else {
            this.e.onSingleTouchUp(shinobiChart, pointF);
        }
    }

    @Override // com.shinobicontrols.charts.ShinobiChart.OnGestureListener
    public void onSwipe(ShinobiChart sender, PointF startPosition, PointF endPosition) {
        if (this.f.h != null && this.f.h.isActive()) {
            this.d.onSwipe(sender, startPosition, endPosition);
            return;
        }
        if (this.g != b.PANNING) {
            this.g = b.PANNING;
        }
        for (ShinobiChart.OnGestureListener onGestureListener : this.c) {
            onGestureListener.onSwipe(sender, startPosition, endPosition);
        }
    }

    @Override // com.shinobicontrols.charts.ShinobiChart.OnGestureListener
    public void onSwipeEnd(ShinobiChart sender, PointF position, boolean flinging, PointF velocity) {
        if (this.f.h == null || !this.f.h.isActive()) {
            this.g = b.NO_GESTURE;
            for (ShinobiChart.OnGestureListener onGestureListener : this.c) {
                onGestureListener.onSwipeEnd(sender, position, flinging, velocity);
            }
        }
    }

    @Override // com.shinobicontrols.charts.ShinobiChart.OnGestureListener
    public void onSecondTouchDown(ShinobiChart sender, PointF position, PointF position2) {
    }

    @Override // com.shinobicontrols.charts.ShinobiChart.OnGestureListener
    public void onSecondTouchUp(ShinobiChart sender, PointF position, PointF position2) {
    }

    @Override // com.shinobicontrols.charts.ShinobiChart.OnGestureListener
    public void onPinch(ShinobiChart sender, PointF startFocus, PointF endFocus, PointF scaleFactor) {
        if (this.g != b.ZOOMING) {
            this.g = b.ZOOMING;
        }
        for (ShinobiChart.OnGestureListener onGestureListener : this.c) {
            onGestureListener.onPinch(sender, startFocus, endFocus, scaleFactor);
        }
    }

    @Override // com.shinobicontrols.charts.ShinobiChart.OnGestureListener
    public void onPinchEnd(ShinobiChart sender, PointF focus, boolean flinging, PointF scaleFactor) {
        this.g = b.NO_GESTURE;
        for (ShinobiChart.OnGestureListener onGestureListener : this.c) {
            onGestureListener.onPinchEnd(sender, focus, flinging, scaleFactor);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a(ShinobiChart.OnGestureListener onGestureListener) {
        this.c.add(onGestureListener);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void b(ShinobiChart.OnGestureListener onGestureListener) {
        this.c.remove(onGestureListener);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void c(ShinobiChart.OnGestureListener onGestureListener) {
        this.d = onGestureListener;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void d(ShinobiChart.OnGestureListener onGestureListener) {
        this.e = onGestureListener;
    }

    /* loaded from: classes.dex */
    private static class a implements Runnable {
        final bn a;
        ShinobiChart b;
        PointF c;

        public a(bn bnVar) {
            this.a = bnVar;
        }

        @Override // java.lang.Runnable
        public void run() {
            this.a.a(this.b, this.c);
        }
    }
}

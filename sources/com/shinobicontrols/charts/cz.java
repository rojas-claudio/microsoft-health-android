package com.shinobicontrols.charts;

import android.graphics.PointF;
import android.os.Handler;
import android.os.Looper;
import com.shinobicontrols.charts.ShinobiChart;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
/* loaded from: classes.dex */
class cz {
    private c b;
    private a c;
    private b d;
    private final ShinobiChart.OnGestureListener e;
    private final v g;
    private final y h;
    private final Queue<d> f = new ConcurrentLinkedQueue();
    private boolean i = false;
    private final Handler a = new Handler(Looper.getMainLooper());

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static abstract class d implements Runnable {
        protected final PointF a;
        protected final Queue<d> b;
        protected final ShinobiChart.OnGestureListener c;
        protected final v d;

        protected abstract void a();

        protected abstract void b();

        d(v vVar, PointF pointF, Queue<d> queue, ShinobiChart.OnGestureListener onGestureListener) {
            this.d = vVar;
            this.a = pointF;
            this.b = queue;
            this.c = onGestureListener;
        }

        @Override // java.lang.Runnable
        public void run() {
            a();
            this.b.add(this);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class a extends d {
        a(v vVar, PointF pointF, Queue<d> queue, ShinobiChart.OnGestureListener onGestureListener) {
            super(vVar, pointF, queue, onGestureListener);
        }

        @Override // com.shinobicontrols.charts.cz.d
        protected void a() {
            this.d.a(this.a);
            this.c.onDoubleTapDown(this.d, this.a);
        }

        @Override // com.shinobicontrols.charts.cz.d
        protected void b() {
            this.d.b(this.a);
            this.c.onDoubleTapUp(this.d, this.a);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class b extends d {
        b(v vVar, PointF pointF, Queue<d> queue, ShinobiChart.OnGestureListener onGestureListener) {
            super(vVar, pointF, queue, onGestureListener);
        }

        @Override // com.shinobicontrols.charts.cz.d, java.lang.Runnable
        public void run() {
            super.run();
        }

        @Override // com.shinobicontrols.charts.cz.d
        protected void a() {
            this.d.c(this.a);
            this.c.onLongTouchDown(this.d, this.a);
            this.b.clear();
        }

        @Override // com.shinobicontrols.charts.cz.d
        protected void b() {
            this.d.d(this.a);
            this.c.onLongTouchUp(this.d, this.a);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class c extends d {
        c(v vVar, PointF pointF, Queue<d> queue, ShinobiChart.OnGestureListener onGestureListener) {
            super(vVar, pointF, queue, onGestureListener);
        }

        @Override // com.shinobicontrols.charts.cz.d
        protected void a() {
            this.d.e(this.a);
            this.c.onSingleTouchDown(this.d, this.a);
        }

        @Override // com.shinobicontrols.charts.cz.d
        protected void b() {
            this.d.f(this.a);
            this.c.onSingleTouchUp(this.d, this.a);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public cz(v vVar, ShinobiChart.OnGestureListener onGestureListener, y yVar) {
        this.g = vVar;
        this.e = onGestureListener;
        this.h = yVar;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean a(int i, int i2, cb cbVar) {
        if (i == 0 && i2 == 1) {
            a(cbVar);
            return true;
        } else if (i == 1 && i2 == 1) {
            b(cbVar);
            return true;
        } else if (i == 1 && i2 == 0) {
            c(cbVar);
            return true;
        } else {
            if (i == 1 && i2 == 2) {
                a();
            }
            return false;
        }
    }

    private void a(cb cbVar) {
        long f = cbVar.f();
        if (f > 0 && f < this.h.d) {
            this.c = new a(this.g, cbVar.e().a, this.f, this.e);
            this.c.run();
            return;
        }
        this.b = new c(this.g, cbVar.e().a, this.f, this.e);
        this.b.run();
        this.d = new b(this.g, cbVar.e().a, this.f, this.e);
        this.a.postDelayed(this.d, this.h.e);
    }

    private void b(cb cbVar) {
        boolean z = this.i;
        this.i = a(cbVar.d().a());
        if (this.i) {
            this.a.removeCallbacks(this.d);
            this.f.clear();
            PointF pointF = z ? cbVar.c().a : cbVar.e().a;
            this.g.c(pointF, cbVar.h().a);
            this.e.onSwipe(this.g, pointF, cbVar.h().a);
            cbVar.i();
        }
    }

    private boolean a(float f) {
        return this.i || f > ((float) this.h.a());
    }

    private void c(cb cbVar) {
        this.a.removeCallbacks(this.d);
        this.i = false;
        if (cbVar.a(this.h.a())) {
            VectorF g = cbVar.g();
            boolean z = g.a() > ((float) this.h.c);
            this.g.b(cbVar.h().a, z, g);
            this.e.onSwipeEnd(this.g, cbVar.h().a, z, g);
        }
        b();
        cbVar.a();
    }

    private void a() {
        this.a.removeCallbacks(this.d);
        this.f.clear();
    }

    private void b() {
        while (!this.f.isEmpty()) {
            this.f.poll().b();
        }
    }
}

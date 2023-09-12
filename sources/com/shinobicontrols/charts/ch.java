package com.shinobicontrols.charts;

import android.graphics.PointF;
import com.microsoft.kapp.utils.Constants;
import com.shinobicontrols.charts.Axis;
import com.shinobicontrols.charts.ShinobiChart;
import com.shinobicontrols.charts.b;
import java.util.Locale;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class ch implements ShinobiChart.OnGestureListener {
    private final Axis<?, ?> m;
    private final com.shinobicontrols.charts.b n = new com.shinobicontrols.charts.b();
    private final c o = new c(this);
    private final e p = new e(this);
    private final a q = new a(this);
    private final b r = new b(this);
    boolean a = true;
    boolean b = false;
    boolean c = true;
    boolean d = true;
    private double s = 1.0d;
    boolean e = false;
    boolean f = false;
    boolean g = false;
    boolean h = false;
    float i = 1.2f;
    float j = 0.75f;
    boolean k = true;
    Axis.MotionState l = Axis.MotionState.STOPPED;
    private final NumberRange t = new NumberRange();
    private final NumberRange u = new NumberRange();
    private final EaseOutAnimationCurve v = new EaseOutAnimationCurve();
    private final bm w = new bm();

    public ch(Axis<?, ?> axis) {
        this.m = axis;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static abstract class d implements b.a {
        protected final ch a;
        protected double b;
        protected double c;
        protected double d;
        protected double e;
        protected double f;
        protected double g;
        protected boolean h;

        protected abstract void a(double d);

        d(ch chVar) {
            this.a = chVar;
        }

        @Override // com.shinobicontrols.charts.b.a
        public void a(Animation animation) {
            if (animation instanceof g) {
                a(((g) animation).d());
            }
        }

        protected void a(double d, double d2) {
            this.b = d;
            this.c = d2;
            this.f = this.b;
            this.g = this.c;
        }

        protected void a(double d, double d2, boolean z) {
            this.d = d;
            this.e = d2;
            this.h = z;
        }

        protected void b(double d) {
            this.f = this.b + ((this.d - this.b) * d);
            this.g = this.c + ((this.e - this.c) * d);
            if (Double.isNaN(this.f) || Double.isInfinite(this.f)) {
                this.f = this.a.m.i.a;
            }
            if (Double.isNaN(this.g) || Double.isInfinite(this.g)) {
                this.g = this.a.m.i.b;
            }
            if (this.g < this.f) {
                this.f = this.d;
                this.g = this.e;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class c extends d {
        c(ch chVar) {
            super(chVar);
        }

        @Override // com.shinobicontrols.charts.ch.d
        protected void a(double d) {
            b(d);
            this.a.b(this.f, this.g);
        }

        @Override // com.shinobicontrols.charts.b.a
        public void a() {
        }

        @Override // com.shinobicontrols.charts.b.a
        public void b() {
            this.a.i();
        }

        @Override // com.shinobicontrols.charts.b.a
        public void c() {
            this.a.j();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class e extends d {
        private double i;
        private double j;
        private double k;

        e(ch chVar) {
            super(chVar);
        }

        @Override // com.shinobicontrols.charts.ch.d
        protected void a(double d) {
            this.a.c(this.j - (this.k * d), this.i);
        }

        @Override // com.shinobicontrols.charts.b.a
        public void a() {
        }

        @Override // com.shinobicontrols.charts.b.a
        public void b() {
            this.a.k();
        }

        @Override // com.shinobicontrols.charts.b.a
        public void c() {
            this.a.l();
        }

        void b(double d, double d2) {
            this.i = d;
            this.j = d2;
            this.k = this.j - 1.0d;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class a extends d implements b.a {
        a(ch chVar) {
            super(chVar);
        }

        @Override // com.shinobicontrols.charts.ch.d
        protected void a(double d) {
            b(d);
            this.a.d(this.f, this.g);
        }

        @Override // com.shinobicontrols.charts.b.a
        public void a() {
            this.a.m();
        }

        @Override // com.shinobicontrols.charts.b.a
        public void b() {
            this.a.n();
        }

        @Override // com.shinobicontrols.charts.b.a
        public void c() {
            this.a.o();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class b extends d {
        b(ch chVar) {
            super(chVar);
        }

        @Override // com.shinobicontrols.charts.ch.d
        protected void a(double d) {
            b(d);
            this.a.e(this.f, this.g);
        }

        @Override // com.shinobicontrols.charts.b.a
        public void a() {
            this.a.p();
        }

        @Override // com.shinobicontrols.charts.b.a
        public void b() {
            this.a.q();
        }

        @Override // com.shinobicontrols.charts.b.a
        public void c() {
            this.a.r();
        }
    }

    @Override // com.shinobicontrols.charts.ShinobiChart.OnGestureListener
    public void onDoubleTapDown(ShinobiChart sender, PointF position) {
    }

    @Override // com.shinobicontrols.charts.ShinobiChart.OnGestureListener
    public void onDoubleTapUp(ShinobiChart sender, PointF position) {
        double e2;
        double e3;
        double d2;
        if (!this.m.b.l() && this.k) {
            if (this.m.getDoubleTapBehavior() == Axis.DoubleTapBehavior.RESET_TO_DEFAULT_RANGE && (this.m.isGesturePanningEnabled() || this.m.isGestureZoomingEnabled())) {
                if (this.m.l != null) {
                    e3 = this.m.l.a;
                    d2 = this.m.l.b;
                } else {
                    e3 = this.m.e();
                    d2 = this.m.d();
                }
                this.n.b();
                this.q.a(e3, d2, this.d);
                a(this.q);
            } else if (this.m.isGestureZoomingEnabled()) {
                double b2 = this.m.getCurrentDisplayedRange().b() / 4.0d;
                if (this.m.c.equals(Axis.Orientation.HORIZONTAL)) {
                    e2 = this.m.e(position.x);
                } else {
                    e2 = this.m.e(position.y);
                }
                this.n.b();
                this.q.a(e2 - b2, e2 + b2, this.d);
                a(this.q);
            }
        }
    }

    @Override // com.shinobicontrols.charts.ShinobiChart.OnGestureListener
    public void onLongTouchDown(ShinobiChart sender, PointF position) {
    }

    @Override // com.shinobicontrols.charts.ShinobiChart.OnGestureListener
    public void onLongTouchUp(ShinobiChart sender, PointF position) {
    }

    @Override // com.shinobicontrols.charts.ShinobiChart.OnGestureListener
    public void onSingleTouchDown(ShinobiChart sender, PointF position) {
        this.n.b();
    }

    @Override // com.shinobicontrols.charts.ShinobiChart.OnGestureListener
    public void onSingleTouchUp(ShinobiChart sender, PointF position) {
    }

    @Override // com.shinobicontrols.charts.ShinobiChart.OnGestureListener
    public void onSecondTouchDown(ShinobiChart sender, PointF position, PointF position2) {
    }

    @Override // com.shinobicontrols.charts.ShinobiChart.OnGestureListener
    public void onSecondTouchUp(ShinobiChart sender, PointF position, PointF position2) {
    }

    @Override // com.shinobicontrols.charts.ShinobiChart.OnGestureListener
    public void onSwipe(ShinobiChart sender, PointF startPosition, PointF endPosition) {
        if (this.e && !Range.a(this.m.i)) {
            this.n.b();
            VectorF a2 = VectorF.a(startPosition, endPosition);
            double d2 = this.m.d(a(a2.x, a2.y));
            if (this.m.a()) {
                d2 = -d2;
            }
            a(d2);
        }
    }

    @Override // com.shinobicontrols.charts.ShinobiChart.OnGestureListener
    public void onSwipeEnd(ShinobiChart sender, PointF position, boolean flinging, PointF velocity) {
        if (this.e && !Range.a(this.m.i)) {
            if (flinging) {
                a(velocity);
            } else {
                h();
            }
        }
    }

    @Override // com.shinobicontrols.charts.ShinobiChart.OnGestureListener
    public void onPinch(ShinobiChart sender, PointF startFocus, PointF endFocus, PointF scaleFactor) {
        if (this.f && !Range.a(this.m.i)) {
            this.n.b();
            a(this.m.e(a(startFocus.x, startFocus.y)), this.m.e(a(endFocus.x, endFocus.y)), a(scaleFactor.x, scaleFactor.y));
        }
    }

    @Override // com.shinobicontrols.charts.ShinobiChart.OnGestureListener
    public void onPinchEnd(ShinobiChart sender, PointF focus, boolean flinging, PointF scaleFactor) {
        if (this.f && !Range.a(this.m.i)) {
            if (flinging) {
                a(scaleFactor, focus);
            } else {
                a(flinging);
            }
        }
    }

    public void a() {
        if (!Range.a(this.m.i) && !s()) {
            if (Range.a(this.m.j) && Range.a(this.m.l)) {
                cx.a(this.m.b != null ? this.m.b.getContext().getString(R.string.RangeManagerUnableToSetRange) : "Unable to set axis range as axis has no associated data and no default range set");
                return;
            }
            this.t.a(this.m.i.a, this.m.i.b);
            this.t.b(c(), e());
            this.q.a(this.t.a, this.t.b, this.d);
            if (a(this.q)) {
                a(Axis.MotionState.STOPPED);
            }
        }
    }

    public boolean a(double d2, double d3) {
        return a(d2, d3, this.d, this.c);
    }

    public boolean a(double d2, double d3, boolean z, boolean z2) {
        boolean z3;
        if (!f(d2, d3)) {
            cx.a(this.m.b != null ? this.m.b.getContext().getString(R.string.RangeManagerInvalidRangeRequested) : "Invalid axis range requested");
            return false;
        }
        this.n.b();
        this.t.a(d2, d3);
        this.u.a(d2, d3);
        if (s()) {
            z3 = true;
        } else if (Range.a(this.m.j) && Range.a(this.m.l)) {
            cx.a(this.m.b != null ? this.m.b.getContext().getString(R.string.RangeManagerUnableToSetRange) : "Unable to set axis range as axis has no associated data and no default range set");
            return false;
        } else {
            boolean z4 = d2 >= c() && d3 <= e();
            this.t.b(c(), e());
            this.u.b(b(z2), c(z2));
            z3 = z4;
        }
        this.q.a(this.u.a, this.u.b, z);
        this.r.a(this.t.a, this.t.b, true);
        if (a(this.q) && a(this.r)) {
            a(Axis.MotionState.STOPPED);
        }
        return z3;
    }

    public boolean a(double d2, boolean z, boolean z2) {
        this.n.b();
        double b2 = b(d2);
        this.u.a(this.m.i.a + b2, b2 + this.m.i.b);
        this.u.b(b(z2), c(z2));
        this.o.a(this.u.a, this.u.b, z);
        return a(this.o);
    }

    public boolean b(double d2, double d3, boolean z, boolean z2) {
        if (this.m.j == null) {
            this.s = d2;
            return true;
        }
        this.n.b();
        h(d2, d3);
        this.u.a(this.t.a, this.t.b);
        this.u.b(b(z2), c(z2));
        this.p.a(this.u.a, this.u.b, z);
        return a(this.p);
    }

    public double b() {
        if (Range.a(this.m.i)) {
            return this.s;
        }
        if (Range.a(this.m.l) && Range.a(this.m.j)) {
            return this.s;
        }
        return g() / this.m.i.b();
    }

    private double g() {
        if (this.m.l != null) {
            return this.m.l.b();
        }
        return this.m.d() - this.m.e();
    }

    private void a(double d2) {
        a(Axis.MotionState.GESTURE);
        this.m.a(d2, false, this.c);
    }

    private void h() {
        w();
    }

    private void a(PointF pointF) {
        if (!this.m.isMomentumPanningEnabled() || u() || v()) {
            w();
        } else {
            d(this.m.d(this.m.c == Axis.Orientation.HORIZONTAL ? -pointF.x : pointF.y));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void b(double d2, double d3) {
        this.t.a(d2, d3);
        boolean b2 = this.t.b(c(), e());
        this.m.a(this.t.a, this.t.b);
        if (b2) {
            this.n.b();
        } else {
            a(Axis.MotionState.MOMENTUM);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void i() {
        a(Axis.MotionState.STOPPED);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void j() {
        a(Axis.MotionState.STOPPED);
    }

    private boolean a(double d2, double d3, float f) {
        boolean z;
        if (f > 0.0f) {
            a(Axis.MotionState.GESTURE);
            z = this.m.a(this.m.w() * f, d2, false, this.c);
        } else {
            z = false;
        }
        return (this.e ? this.m.a(d2 - d3, false, this.c) : false) || z;
    }

    private void a(boolean z) {
        w();
    }

    private void a(PointF pointF, PointF pointF2) {
        if (!this.h || u() || v()) {
            w();
            return;
        }
        g(this.m.e(a(pointF2.x, pointF2.y)), a(pointF.x, pointF.y));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void c(double d2, double d3) {
        h(b() * d2, d3);
        boolean b2 = this.t.b(c(), e());
        this.m.a(this.t.a, this.t.b);
        if (b2) {
            this.n.b();
        } else {
            a(Axis.MotionState.MOMENTUM);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void k() {
        a(Axis.MotionState.STOPPED);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void l() {
        a(Axis.MotionState.STOPPED);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void d(double d2, double d3) {
        this.m.a(d2, d3);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void m() {
        a(Axis.MotionState.ANIMATING);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void n() {
        w();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void o() {
        a(Axis.MotionState.STOPPED);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void e(double d2, double d3) {
        this.m.a(d2, d3);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void p() {
        a(Axis.MotionState.BOUNCING);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void q() {
        a(Axis.MotionState.STOPPED);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void r() {
        a(Axis.MotionState.STOPPED);
    }

    private double b(double d2) {
        if ((u() && d2 < Constants.SPLITS_ACCURACY) || (v() && d2 > Constants.SPLITS_ACCURACY)) {
            return d2 / 3.0d;
        }
        return d2;
    }

    private float a(float f, float f2) {
        return this.m.c == Axis.Orientation.HORIZONTAL ? f : f2;
    }

    private boolean a(d dVar) {
        if (f(dVar.d, dVar.e)) {
            if (this.m.i.a == dVar.d && this.m.i.b == dVar.e) {
                return true;
            }
            if (dVar.h) {
                a(0.95f, this.v, dVar);
                return false;
            }
            this.m.a(dVar.d, dVar.e);
            return true;
        }
        return true;
    }

    private void a(float f, AnimationCurve animationCurve, d dVar) {
        dVar.a(this.m.i.a, this.m.i.b);
        g gVar = new g();
        gVar.setDuration(f);
        gVar.a(animationCurve);
        this.n.a(gVar);
        this.n.a(dVar);
        this.n.a();
    }

    private boolean f(double d2, double d3) {
        if (c(d2) || c(d3)) {
            cx.a(this.m.b != null ? this.m.b.getContext().getString(R.string.RangeManagerLimitsCannotBeNaN) : "Range minimum and maximum cannot be infinite or NaNs");
            return false;
        } else if (d3 <= d2) {
            cx.a(String.format(Locale.getDefault(), this.m.b != null ? this.m.b.getContext().getString(R.string.RangeManagerNonPositiveSpan) : "Ignoring range with %f span", Double.valueOf(d3 - d2)));
            return false;
        } else {
            return true;
        }
    }

    private boolean s() {
        return this.a && this.b;
    }

    private double b(boolean z) {
        double c2 = c();
        if (!c(c2) && z) {
            return c2 - t();
        }
        return c2;
    }

    double c() {
        if (s()) {
            return Double.NEGATIVE_INFINITY;
        }
        if (!this.a && this.m.l != null) {
            return this.m.l.a;
        }
        return d();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public double d() {
        return this.m.l != null ? Math.min(this.m.j.a, this.m.l.a) : this.m.e();
    }

    double e() {
        if (s()) {
            return Double.POSITIVE_INFINITY;
        }
        if (!this.a && this.m.l != null) {
            return this.m.l.b;
        }
        return f();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public double f() {
        return this.m.l != null ? Math.max(this.m.j.b, this.m.l.b) : this.m.d();
    }

    private double c(boolean z) {
        double e2 = e();
        if (!c(e2) && z) {
            return e2 + t();
        }
        return e2;
    }

    private boolean c(double d2) {
        return Double.isInfinite(d2) || Double.isNaN(d2);
    }

    private double t() {
        return Range.a(this.m.i) ? Constants.SPLITS_ACCURACY : this.m.i.b() / 4.0d;
    }

    private boolean g(double d2, double d3) {
        if (!this.h) {
            return false;
        }
        this.p.b(d2, d3);
        a(this.j, this.w, this.p);
        return true;
    }

    private boolean d(double d2) {
        if (!this.g) {
            return false;
        }
        double a2 = a(d2, e(this.i), this.i);
        this.o.a(this.m.i.a + a2, a2 + this.m.i.b, true);
        a(this.i, this.w, this.o);
        return true;
    }

    private double e(double d2) {
        return (-Math.log(0.012000000104308128d)) / d2;
    }

    private double a(double d2, double d3, double d4) {
        return ((1.0d - Math.pow(2.718281828459045d, (-d4) * d3)) / d3) * d2;
    }

    private boolean u() {
        return this.m.i.a < c();
    }

    private boolean v() {
        return this.m.i.b > e();
    }

    private void a(Axis.MotionState motionState) {
        boolean z = this.l != motionState;
        this.l = motionState;
        if (z && this.m.b != null) {
            this.m.b.a(this.m);
        }
    }

    private void w() {
        if (!s()) {
            this.u.a(this.m.i.a, this.m.i.b);
            this.u.b(c(), e());
            this.r.a(this.u.a, this.u.b, true);
            if (a(this.r)) {
                a(Axis.MotionState.STOPPED);
                return;
            }
            return;
        }
        a(Axis.MotionState.STOPPED);
    }

    private void h(double d2, double d3) {
        double b2 = (d3 - this.m.i.a) / this.m.i.b();
        double g = g() / d2;
        this.t.a(d3 - (b2 * g), ((1.0d - b2) * g) + d3);
    }
}

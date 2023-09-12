package com.shinobicontrols.charts;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import com.microsoft.kapp.utils.Constants;
import com.shinobicontrols.charts.PropertyChangedEvent;
/* loaded from: classes.dex */
public class Crosshair {
    a a;
    CartesianSeries<?> f;
    Tooltip g;
    private v h;
    private CrosshairStyle i;
    private float l;
    private Data<?, ?> m;
    private final bz k = new bz();
    boolean b = true;
    Mode c = Mode.SINGLE_SERIES;
    OutOfRangeBehavior d = OutOfRangeBehavior.HIDE;
    DrawLinesBehavior e = DrawLinesBehavior.SERIES_DEFAULT;
    private float n = 0.0f;
    private final ci o = new ci();
    private final Rect p = new Rect();
    private final b q = new b(this);
    private boolean r = true;
    private final Paint j = new Paint();

    /* loaded from: classes.dex */
    public enum DrawLinesBehavior {
        ALWAYS,
        NEVER,
        SERIES_DEFAULT
    }

    /* loaded from: classes.dex */
    public enum Mode {
        SINGLE_SERIES,
        FLOATING
    }

    /* loaded from: classes.dex */
    public enum OutOfRangeBehavior {
        KEEP_AT_EDGE,
        HIDE,
        REMOVE
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public enum a {
        SHOWN,
        HIDDEN,
        REMOVED
    }

    public Mode getMode() {
        return this.c;
    }

    public void setMode(Mode mode) {
        this.c = mode;
    }

    public OutOfRangeBehavior getOutOfRangeBehavior() {
        return this.d;
    }

    public void setOutOfRangeBehavior(OutOfRangeBehavior outOfRangeBehavior) {
        this.d = outOfRangeBehavior;
    }

    public DrawLinesBehavior getDrawLinesBehavior() {
        return this.e;
    }

    public void setDrawLinesBehavior(DrawLinesBehavior drawLinesBehavior) {
        this.e = drawLinesBehavior;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Crosshair() {
        this.j.setStyle(Paint.Style.STROKE);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a(v vVar) {
        l();
        this.h = vVar;
        if (vVar != null) {
            this.n = vVar.getResources().getDisplayMetrics().density;
            this.l = at.a(this.n, 2.5f);
            this.g = new Tooltip(vVar.getContext());
            this.g.a(this.q);
            this.g.a(this.i);
            k();
        }
    }

    public CrosshairStyle getStyle() {
        return this.i;
    }

    public void setStyle(CrosshairStyle style) {
        this.i = style;
    }

    public float getPixelXValue() {
        if (this.h == null) {
            return 0.0f;
        }
        return ((float) this.k.b) + this.h.a.left;
    }

    public float getPixelYValue() {
        if (this.h == null) {
            return 0.0f;
        }
        return ((float) this.k.c) + this.h.a.top;
    }

    public Tooltip getTooltip() {
        return this.g;
    }

    public CartesianSeries<?> getTrackedSeries() {
        return this.f;
    }

    public Data<?, ?> getFocus() {
        return this.m;
    }

    public void setFocus(Data<?, ?> focus) {
        this.m = focus;
        if (focus == null || focus.getX() == null || focus.getY() == null) {
            throw new IllegalArgumentException(this.g.getContext().getString(R.string.CrosshairNullXOrYInFocusPoint));
        }
        h();
    }

    public boolean isShown() {
        return this.a == a.SHOWN;
    }

    public void setLineSeriesInterpolationEnabled(boolean enabled) {
        this.b = enabled;
    }

    public boolean isLineSeriesInterpolationEnabled() {
        return this.b;
    }

    public boolean isActive() {
        return this.a == a.SHOWN || this.a == a.HIDDEN;
    }

    void a() {
        this.h.b.d();
    }

    void b() {
        if (this.g != null) {
            this.g.requestLayout();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void c() {
        if (this.h != null) {
            this.h.b.g();
        }
        this.g.forceLayout();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean d() {
        switch (this.e) {
            case ALWAYS:
                return true;
            case NEVER:
                return false;
            case SERIES_DEFAULT:
                return this.f.l.b;
            default:
                throw new UnsupportedOperationException("drawLinesBehavior set incorrectly");
        }
    }

    void a(Canvas canvas, Rect rect) {
        this.p.set(rect);
        if (!this.h.a(canvas, this.p, (float) this.k.b, (float) this.k.c, this.l, this.j)) {
            ChartUtils.drawCrosshair(this.h, canvas, this.p, (float) this.k.b, (float) this.k.c, this.l, this.j);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a(int i, int i2) {
        if (this.g != null) {
            this.g.measure(i, i);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a(int i, int i2, int i3, int i4) {
        if (this.g != null) {
            int measuredWidth = this.g.getMeasuredWidth();
            int measuredHeight = this.g.getMeasuredHeight();
            this.o.a(this.g.a.b, this.g.a.c, this.g.a.b + measuredWidth, this.g.a.c + measuredHeight);
            this.o.a((-measuredWidth) / 2.0d, (-measuredHeight) / 2.0d);
            a(this.o, i, i3);
            c(this.o, i2, i4);
            at.a(this.g, this.o);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @SuppressLint({"WrongCall"})
    public void b(Canvas canvas, Rect rect) {
        a(canvas, rect);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void e() {
        boolean isActive = isActive();
        this.a = a.REMOVED;
        this.f = null;
        this.m = null;
        this.g.b();
        a();
        if (isActive && this.h != null) {
            this.h.m();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a(CartesianSeries<?> cartesianSeries, DataPoint<?, ?> dataPoint, DataPoint<?, ?> dataPoint2, DataPoint<?, ?> dataPoint3) {
        this.f = cartesianSeries;
        if (!this.h.b(cartesianSeries, dataPoint, dataPoint2, dataPoint3)) {
            setFocus(cartesianSeries.l.a(dataPoint, dataPoint2, dataPoint3, this.b));
        }
        b(cartesianSeries, dataPoint, dataPoint2, dataPoint3);
        i();
        a();
        b();
    }

    private void h() {
        if (this.f != null) {
            if (this.m == null) {
                throw new IllegalStateException(this.h != null ? this.h.getContext().getString(R.string.CrosshairNullFocus) : "Unable to determine Crosshair position: must have non-null focus. Have you called setFocus on the crosshair?");
            }
            this.k.b = a(this.m.getX(), this.f.getXAxis(), this.f);
            this.k.c = a(this.m.getY(), this.f.getYAxis(), this.f);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static double a(Object obj, Axis<?, ?> axis, CartesianSeries<?> cartesianSeries) {
        return axis.a(axis.translatePoint(obj), cartesianSeries);
    }

    private void b(CartesianSeries<?> cartesianSeries, DataPoint<?, ?> dataPoint, DataPoint<?, ?> dataPoint2, DataPoint<?, ?> dataPoint3) {
        if (this.g != null) {
            this.g.a(cartesianSeries);
            if (!this.h.a(cartesianSeries, dataPoint, dataPoint2, dataPoint3)) {
                this.g.a(cartesianSeries, dataPoint, dataPoint2, dataPoint3);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void f() {
        if (this.i != null) {
            this.j.setColor(this.i.getLineColor());
            this.j.setStrokeWidth(at.a(this.n, this.i.getLineWidth()));
            if (this.g != null) {
                this.g.a(this.i);
            }
        }
    }

    private void i() {
        if (j()) {
            m();
            return;
        }
        switch (this.d) {
            case HIDE:
                o();
                return;
            case REMOVE:
                e();
                return;
            default:
                return;
        }
    }

    private boolean j() {
        Rect rect = this.h.b.b;
        return !rect.isEmpty() && this.k.b >= ((double) rect.left) && this.k.b <= ((double) rect.right) && this.k.c >= ((double) rect.top) && this.k.c <= ((double) rect.bottom);
    }

    private void k() {
        this.h.b.a(this.g);
    }

    private void l() {
        if (this.h != null) {
            this.h.b.b(this.g);
        }
    }

    private void m() {
        boolean isActive = isActive();
        this.a = a.SHOWN;
        n();
        if (!isActive && this.h != null) {
            this.h.m();
        }
    }

    private void n() {
        if (this.r) {
            this.g.c();
        } else {
            this.g.d();
        }
    }

    private void o() {
        boolean isActive = isActive();
        this.a = a.HIDDEN;
        this.g.d();
        if (!isActive && this.h != null) {
            this.h.m();
        }
    }

    private void a(ci ciVar, int i, int i2) {
        if (ciVar.a() > i2 - i) {
            b(ciVar, i, i2);
            return;
        }
        a(ciVar, i);
        b(ciVar, i2);
    }

    private void b(ci ciVar, int i, int i2) {
        ciVar.b(i - ((ciVar.a() - (i2 - i)) / 2.0d), ciVar.b);
    }

    private void a(ci ciVar, int i) {
        if (ciVar.a < i) {
            ciVar.b(i, ciVar.b);
        }
    }

    private void b(ci ciVar, int i) {
        if (ciVar.c > i) {
            ciVar.b(i - ciVar.a(), ciVar.b);
        }
    }

    private void c(ci ciVar, int i, int i2) {
        if (ciVar.b() > i2 - i) {
            d(ciVar, i, i2);
            return;
        }
        c(ciVar, i);
        d(ciVar, i2);
    }

    private void d(ci ciVar, int i, int i2) {
        ciVar.b(ciVar.a, i - ((ciVar.b() - (i2 - i)) / 2.0d));
    }

    private void c(ci ciVar, int i) {
        if (ciVar.b < i) {
            ciVar.b(ciVar.a, i);
        }
    }

    private void d(ci ciVar, int i) {
        if (ciVar.d > i) {
            ciVar.a(Constants.SPLITS_ACCURACY, -(ciVar.d - i));
            ciVar.b(ciVar.a, i - ciVar.b());
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void g() {
        h();
        if (this.g != null) {
            this.g.a();
        }
        i();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a(Series<?> series) {
        if (this.f == series) {
            e();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void b(Series<?> series) {
        if (this.f == series) {
            e();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class b implements PropertyChangedEvent.Handler {
        private final Crosshair a;

        public b(Crosshair crosshair) {
            this.a = crosshair;
        }

        @Override // com.shinobicontrols.charts.PropertyChangedEvent.Handler
        public void onPropertyChanged() {
            this.a.f();
        }
    }

    public boolean isTooltipEnabled() {
        return this.r;
    }

    public void enableTooltip(boolean tooltipEnabled) {
        this.r = tooltipEnabled;
    }
}

package com.shinobicontrols.charts;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import com.shinobicontrols.charts.Axis;
import com.shinobicontrols.charts.Legend;
import com.shinobicontrols.charts.ShinobiChart;
import com.shinobicontrols.charts.Title;
import com.shinobicontrols.charts.dl;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public abstract class v extends ViewGroup implements ShinobiChart {
    private static final boolean m;
    private final float A;
    private ShinobiChart.OnGestureListener B;
    private ShinobiChart.OnAxisMotionStateChangeListener C;
    private ShinobiChart.OnAxisRangeChangeListener D;
    private ShinobiChart.OnInternalLayoutListener E;
    private ShinobiChart.OnSeriesSelectionListener F;
    private ShinobiChart.OnSeriesAnimationListener G;
    private ShinobiChart.OnCrosshairActivationStateChangedListener H;
    private ShinobiChart.OnPieDonutSliceLabelDrawListener I;
    private ShinobiChart.OnPieDonutSliceUpdateListener J;
    private ShinobiChart.OnTickMarkUpdateListener K;
    private ShinobiChart.OnTickMarkDrawListener L;
    private ShinobiChart.OnCrosshairDrawListener M;
    private ShinobiChart.OnTrackingInfoChangedForTooltipListener N;
    private ShinobiChart.OnTrackingInfoChangedForCrosshairListener O;
    private boolean P;
    final Rect a;
    w b;
    final List<Series<?>> c;
    final cv d;
    final h e;
    final h f;
    cm g;
    Crosshair h;
    final cw i;
    final cp j;
    ShinobiChart.OnSnapshotDoneListener k;
    final AnnotationsManager l;
    private final as n;
    private final bl o;
    private dd p;
    private ChartStyle q;
    private MainTitleStyle r;
    private Title s;
    private Legend t;
    private final au u;
    private final cs v;
    private final dl w;
    private dm x;
    private String y;
    private boolean z;

    abstract dl b();

    static {
        m = Build.VERSION.SDK_INT >= 14;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public List<cr> a() {
        return this.d.a();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public v(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.n = new as();
        this.o = new bl();
        this.a = new Rect();
        this.q = new ChartStyle();
        this.r = new MainTitleStyle();
        this.u = new au(this);
        this.c = new ArrayList();
        this.d = new cv();
        this.v = new cs();
        this.e = new h("x");
        this.f = new h("y");
        this.z = false;
        this.g = new cm(this);
        this.i = new cw(this);
        this.j = new cp(this);
        this.A = getResources().getDisplayMetrics().density;
        this.w = b();
        this.p = de.a(context, attributeSet);
        this.q = this.p.a();
        this.r = this.p.b();
        t();
        b(context);
        c(context);
        d(context);
        u();
        a(context);
        this.b.d(this.g);
        v();
        this.l = new AnnotationsManager(this);
    }

    @Override // com.shinobicontrols.charts.ShinobiChart
    public void setLicenseKey(String licenseKey) {
        this.y = licenseKey;
    }

    private void t() {
        if (this.q != null) {
            if (this.q.getBackgroundColor() == 0) {
                a.a(this, (Drawable) null);
            } else {
                setBackgroundColor(this.q.getBackgroundColor());
            }
        }
    }

    private void a(Context context) {
        if (this.w.a()) {
            this.x = new dm(context);
            addView(this.x);
        }
    }

    private void b(Context context) {
        this.s = new Title(context);
        this.s.setLayoutParams(new ViewGroup.MarginLayoutParams(-2, -2));
        this.s.a(this.r);
        this.s.setVisibility(8);
        addView(this.s);
    }

    private void c(Context context) {
        this.b = new w(context, this);
        this.b.setLayoutParams(new ViewGroup.MarginLayoutParams(-2, -2));
        this.b.a();
        this.b.a(this.q);
        addView(this.b);
    }

    private void d(Context context) {
        this.t = new Legend(context);
        this.t.a(this.u);
        this.t.setVisibility(8);
        ViewGroup.MarginLayoutParams marginLayoutParams = new ViewGroup.MarginLayoutParams(-2, -2);
        int a = at.a(this.A, 10.0f);
        marginLayoutParams.setMargins(a, a, a, a);
        this.t.setLayoutParams(marginLayoutParams);
        this.t.setStyle(this.p.d());
        addView(this.t);
    }

    private void u() {
        this.h = new Crosshair();
        this.h.a(this);
        this.h.setStyle(this.p.c());
        this.b.c(this.i);
    }

    private void v() {
        if (this.w.b() == dl.a.PREMIUM) {
            this.P = true;
        } else if (this.w.b() == dl.a.STANDARD) {
            this.P = false;
        } else if (this.w.b() == dl.a.TRIAL) {
            this.P = true;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void a(Bundle bundle) {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void c() {
        this.b.i();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void d() {
        this.b.j();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void e() {
    }

    @Override // android.view.View
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        this.o.a();
        a(widthMeasureSpec, heightMeasureSpec);
        b(widthMeasureSpec, heightMeasureSpec);
        c(widthMeasureSpec, heightMeasureSpec);
        this.o.b(getPaddingLeft() + getPaddingRight());
        this.o.a(getPaddingTop() + getPaddingBottom());
        setMeasuredDimension(View.resolveSize(this.o.b, widthMeasureSpec), View.resolveSize(this.o.a, heightMeasureSpec));
    }

    private void a(int i, int i2) {
        if (!w()) {
            measureChildWithMargins(this.s, i, this.o.b, i2, this.o.a);
            this.o.a(at.a(this.s));
        }
    }

    private boolean w() {
        return this.s == null || this.s.getVisibility() == 8;
    }

    private void b(int i, int i2) {
        if (!x()) {
            measureChildWithMargins(this.t, i, this.o.b, i2, this.o.a);
            this.o.b(y());
            this.o.a(z());
        }
    }

    private boolean x() {
        return this.t == null || this.t.getVisibility() == 8;
    }

    @Override // com.shinobicontrols.charts.ShinobiChart
    public boolean isSeriesSelectionSingle() {
        return this.g.a;
    }

    @Override // com.shinobicontrols.charts.ShinobiChart
    public void setSeriesSelectionSingle(boolean seriesSelectionIsSingle) {
        this.g.a = seriesSelectionIsSingle;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean f() {
        return this.e.b() && this.f.b();
    }

    private int y() {
        return this.t.a.b(at.b(this.t), this.q.e(), a(j(), (Axis.Position) null), a(j(), 0), (ViewGroup.MarginLayoutParams) this.b.getLayoutParams());
    }

    private Axis.Position a(Axis<?, ?> axis, Axis.Position position) {
        return axis != null ? axis.d : position;
    }

    private int a(Axis<?, ?> axis, int i) {
        return axis != null ? axis.o : i;
    }

    private int z() {
        return this.t.a.a(at.a(this.t), this.q.e(), a(i(), (Axis.Position) null), a(i(), 0), (ViewGroup.MarginLayoutParams) this.b.getLayoutParams());
    }

    private void c(int i, int i2) {
        measureChildWithMargins(this.b, i, this.o.b, i2, this.o.a);
        this.o.b(at.b(this.b));
        this.o.a(at.a(this.b));
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        if (!this.z) {
            this.w.a(this.y);
            this.z = true;
        }
        int paddingRight = (right - getPaddingRight()) - (getPaddingLeft() + left);
        int paddingBottom = (bottom - getPaddingBottom()) - (getPaddingTop() + top);
        this.n.a(0, 0, paddingRight, paddingBottom);
        if (!this.r.getOverlapsChart()) {
            this.n.a(at.a(this.s));
        } else {
            this.s.bringToFront();
        }
        B();
        G();
        this.n.a(0, 0, paddingRight, paddingBottom);
        A();
        D();
        n();
    }

    private void A() {
        if (!w()) {
            this.n.a();
            if (this.r.getCentersOn() != Title.CentersOn.CHART) {
                F();
            }
            if (this.r.getCentersOn() == Title.CentersOn.PLOTTING_AREA) {
                this.n.a.left += this.b.e;
                this.n.a.right -= this.b.f;
            }
            int b = at.b(this.s);
            int a = at.a(this.s);
            Gravity.apply(this.r.getPosition().a() | 48, b, a, this.n.a, this.n.b());
            this.n.c();
            at.a(this.s, this.n.a);
            at.b(this.s, this.n.a);
            this.n.a(a);
        }
    }

    private void B() {
        this.n.a();
        E();
        this.a.set(this.n.a);
        at.b(this.b, this.n.a);
    }

    private void C() {
        boolean z;
        ArrayList arrayList = new ArrayList();
        int size = this.c.size();
        for (int i = 0; i < size; i++) {
            Series<?> series = this.c.get(i);
            if (series instanceof PieDonutSeries) {
                arrayList.add((PieDonutSeries) series);
            }
        }
        int size2 = arrayList.size();
        if (size2 != 0) {
            float f = 0.4f / size2;
            int i2 = 0;
            PieDonutSeries pieDonutSeries = null;
            while (i2 < size2) {
                PieDonutSeries pieDonutSeries2 = (PieDonutSeries) arrayList.get(i2);
                float c = pieDonutSeries != null ? pieDonutSeries.c() : 0.0f;
                if (pieDonutSeries2.a.b) {
                    z = false;
                } else {
                    pieDonutSeries2.a.b(Float.valueOf(pieDonutSeries != null ? c + pieDonutSeries.getOuterRadius() : pieDonutSeries2.b(f)));
                    z = true;
                }
                float c2 = pieDonutSeries2.c();
                if (!pieDonutSeries2.b.b) {
                    pieDonutSeries2.b.b(Float.valueOf((((i2 + 1) * ((0.95f - f) / size2)) + f) - c2));
                    z = true;
                }
                if (z) {
                    pieDonutSeries2.a_();
                }
                i2++;
                pieDonutSeries = pieDonutSeries2;
            }
        }
    }

    private void D() {
        if (this.x != null) {
            Rect plotAreaRect = getPlotAreaRect();
            this.x.measure(View.MeasureSpec.makeMeasureSpec(plotAreaRect.right - plotAreaRect.left, Integer.MIN_VALUE), View.MeasureSpec.makeMeasureSpec(plotAreaRect.bottom - plotAreaRect.top, Integer.MIN_VALUE));
            int measuredWidth = this.x.getMeasuredWidth();
            int measuredHeight = this.x.getMeasuredHeight();
            int i = plotAreaRect.left;
            int i2 = plotAreaRect.top;
            this.x.layout(i, i2, measuredWidth + i, measuredHeight + i2);
        }
    }

    private void E() {
        if (!x()) {
            this.t.a.a(this.n.a, y(), z());
        }
        at.a(this.b, this.n.a);
    }

    private void F() {
        if (!x()) {
            this.t.a.a(this.n.a, y(), 0);
        }
        at.a(this.b, this.n.a);
    }

    private void G() {
        if (!x()) {
            this.n.a();
            if (this.t.getPlacement() == Legend.Placement.INSIDE_PLOT_AREA || this.t.getPlacement() == Legend.Placement.ON_PLOT_AREA_BORDER) {
                E();
            }
            this.t.a.a(this.n.a, this.b.e, this.b.d, this.b.f, this.b.c);
            Gravity.apply(this.t.getPosition().a(), at.b(this.t), at.a(this.t), this.n.a, this.n.b());
            this.n.c();
            at.a(this.t, this.n.a);
            this.t.a.a(this.n.a, (ViewGroup.MarginLayoutParams) this.t.getLayoutParams(), this.t.getMeasuredWidth(), this.t.getMeasuredHeight(), this.q.e());
            at.b(this.t, this.n.a);
        }
    }

    @Override // android.view.ViewGroup
    /* renamed from: a */
    public ViewGroup.MarginLayoutParams generateLayoutParams(AttributeSet attributeSet) {
        return new ViewGroup.MarginLayoutParams(getContext(), attributeSet);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.ViewGroup
    /* renamed from: a */
    public ViewGroup.MarginLayoutParams generateLayoutParams(ViewGroup.LayoutParams layoutParams) {
        return new ViewGroup.MarginLayoutParams(layoutParams);
    }

    @Override // android.view.ViewGroup
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof ViewGroup.MarginLayoutParams;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.ViewGroup
    /* renamed from: g */
    public ViewGroup.MarginLayoutParams generateDefaultLayoutParams() {
        return new ViewGroup.MarginLayoutParams(-2, -2);
    }

    @Override // android.view.View
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (l()) {
            C();
        }
    }

    @Override // com.shinobicontrols.charts.ShinobiChart
    public void addSeries(Series<?> series) {
        addSeries(series, null, null);
    }

    @Override // com.shinobicontrols.charts.ShinobiChart
    public void addSeries(Series<?> series, Axis<?, ?> xAxis, Axis<?, ?> yAxis) {
        if (e(series)) {
            L();
        }
        synchronized (x.a) {
            if (series == null) {
                throw new IllegalArgumentException(getContext().getString(R.string.ChartCannotAddNullSeries));
            }
            if (series instanceof PieDonutSeries) {
                d(series);
            } else {
                a(series, xAxis, yAxis);
            }
        }
    }

    private void d(Series<?> series) {
        if (H()) {
            throw new IllegalStateException(getContext().getString(R.string.ChartPieDonutInCartesian));
        }
        if (this.e.b() || this.f.b()) {
            throw new IllegalStateException(getContext().getString(R.string.ChartPieDonutCannotHaveAxes));
        }
        synchronized (x.a) {
            if (series.x) {
                this.j.a(series, this);
            } else {
                a(series);
            }
        }
    }

    private boolean H() {
        return this.c.size() > 0 && !l();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a(Series<?> series) {
        this.c.add(series);
        if (series instanceof PieDonutSeries) {
            C();
        }
        series.c(this.p, this.v.a(series), false);
        series.a(this);
    }

    private void a(Series<?> series, Axis<?, ?> axis, Axis<?, ?> axis2) {
        if (I()) {
            throw new IllegalStateException(getContext().getString(R.string.ChartCartesianInPieDonut));
        }
        Axis<?, ?> c = c(axis);
        Axis<?, ?> d = d(axis2);
        if (c == null) {
            cx.a(getContext().getString(R.string.ChartNoPrimaryX));
        }
        if (d == null) {
            cx.a(getContext().getString(R.string.ChartNoPrimaryY));
        }
        this.d.a((CartesianSeries) series, c, d);
        synchronized (x.a) {
            if (series.x) {
                this.j.a(series, this);
            } else {
                a(series);
            }
        }
    }

    private boolean I() {
        return this.c.size() > 0 && l();
    }

    private Axis<?, ?> c(Axis<?, ?> axis) {
        if (axis == null) {
            return i();
        }
        if (axis.b != this) {
            addXAxis(axis);
            return axis;
        }
        return axis;
    }

    private Axis<?, ?> d(Axis<?, ?> axis) {
        if (axis == null) {
            return j();
        }
        if (axis.b != this) {
            addYAxis(axis);
            return axis;
        }
        return axis;
    }

    @Override // com.shinobicontrols.charts.ShinobiChart
    public boolean removeSeries(Series<?> s) {
        boolean z;
        synchronized (x.a) {
            if (s.x) {
                this.j.b(s, this);
                z = false;
            } else {
                b(s);
                z = true;
            }
        }
        return z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void b(Series<?> series) {
        Axis<?, ?> axis;
        Axis<?, ?> axis2;
        if (series instanceof PieDonutSeries) {
            axis = null;
            axis2 = null;
        } else {
            axis2 = series.getXAxis();
            axis = series.getYAxis();
        }
        this.c.remove(series);
        this.d.a(series);
        this.v.b(series);
        if (series instanceof PieDonutSeries) {
            C();
        } else {
            if (axis2 != null) {
                axis2.f();
            }
            if (axis != null) {
                axis.f();
            }
        }
        if (this.h != null) {
            this.h.a(series);
        }
        series.a((v) null);
        this.b.e();
        this.b.invalidate();
        redrawChart();
    }

    @Override // com.shinobicontrols.charts.ShinobiChart
    public List<Series<?>> getSeries() {
        return Collections.unmodifiableList(this.c);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public List<CartesianSeries<?>> h() {
        ArrayList arrayList = new ArrayList();
        for (Series<?> series : this.c) {
            if (series instanceof CartesianSeries) {
                arrayList.add((CartesianSeries) series);
            }
        }
        return arrayList;
    }

    @Override // com.shinobicontrols.charts.ShinobiChart
    public Axis<?, ?> getXAxisForSeries(Series<?> s) {
        return this.d.b(s);
    }

    @Override // com.shinobicontrols.charts.ShinobiChart
    public Axis<?, ?> getYAxisForSeries(Series<?> s) {
        return this.d.c(s);
    }

    @Override // com.shinobicontrols.charts.ShinobiChart
    public List<Series<?>> getSeriesForAxis(Axis<?, ?> a) {
        Set<CartesianSeries<?>> e = this.d.e(a);
        return e == null ? new ArrayList() : Arrays.asList(e.toArray(new Series[0]));
    }

    @Override // com.shinobicontrols.charts.ShinobiChart
    public void setHidden(List<Series<?>> seriesList, boolean hidden) {
        if (seriesList == null) {
            cx.b(getResources().getString(R.string.ChartCannotPassInNullSeriesList));
            return;
        }
        synchronized (x.a) {
            ArrayList arrayList = new ArrayList();
            for (Series<?> series : seriesList) {
                if (series.x && series.o != null) {
                    if (hidden) {
                        if (!series.y && series.u != series.w) {
                            arrayList.add(series);
                        }
                    } else if (series.y || series.u != null) {
                        if (series.u != series.v) {
                            arrayList.add(series);
                        }
                    }
                } else {
                    series.a(hidden);
                }
            }
            if (hidden) {
                this.j.a(arrayList);
            } else {
                this.j.b(arrayList);
            }
        }
    }

    @Override // com.shinobicontrols.charts.ShinobiChart
    public List<Axis<?, ?>> getAllXAxes() {
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < this.e.a.length; i++) {
            arrayList.add(this.e.a[i]);
        }
        return Collections.unmodifiableList(arrayList);
    }

    @Override // com.shinobicontrols.charts.ShinobiChart
    public List<Axis<?, ?>> getAllYAxes() {
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < this.f.a.length; i++) {
            arrayList.add(this.f.a[i]);
        }
        return Collections.unmodifiableList(arrayList);
    }

    @Override // com.shinobicontrols.charts.ShinobiChart
    public Rect getPlotAreaRect() {
        Rect rect = new Rect(this.b.b);
        rect.offset(this.a.left, this.a.top);
        return rect;
    }

    @Override // com.shinobicontrols.charts.ShinobiChart
    public Rect getCanvasRect() {
        return this.a;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:53:0x00b8 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Type inference failed for: r3v0, types: [java.lang.ClassLoader] */
    /* JADX WARN: Type inference failed for: r3v1 */
    /* JADX WARN: Type inference failed for: r3v16, types: [java.lang.String] */
    /* JADX WARN: Type inference failed for: r3v6, types: [java.io.InputStream] */
    /* JADX WARN: Type inference failed for: r3v7 */
    @Override // com.shinobicontrols.charts.ShinobiChart
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public java.lang.String getInfo() {
        /*
            r7 = this;
            java.lang.String r0 = "NOT KNOWN"
            java.lang.String r2 = "NOT KNOWN"
            java.lang.String r4 = "NOT KNOWN"
            java.lang.String r0 = "NOT KNOWN"
            java.lang.Thread r1 = java.lang.Thread.currentThread()
            java.lang.ClassLoader r3 = r1.getContextClassLoader()
            r1 = 0
            java.lang.String r5 = "version.properties"
            java.io.InputStream r3 = r3.getResourceAsStream(r5)     // Catch: java.lang.NullPointerException -> L6a java.io.IOException -> L8f java.lang.Throwable -> Lb4
            java.util.Properties r5 = new java.util.Properties     // Catch: java.lang.Throwable -> Lcb java.io.IOException -> Lcd java.lang.NullPointerException -> Ld2
            r5.<init>()     // Catch: java.lang.Throwable -> Lcb java.io.IOException -> Lcd java.lang.NullPointerException -> Ld2
            r5.load(r3)     // Catch: java.lang.Throwable -> Lcb java.io.IOException -> Lcd java.lang.NullPointerException -> Ld2
            java.lang.String r1 = "flavor"
            java.lang.String r6 = "NOT KNOWN"
            java.lang.String r2 = r5.getProperty(r1, r6)     // Catch: java.lang.Throwable -> Lcb java.io.IOException -> Lcd java.lang.NullPointerException -> Ld2
            java.lang.String r1 = "version"
            java.lang.String r6 = "NOT KNOWN"
            java.lang.String r1 = r5.getProperty(r1, r6)     // Catch: java.lang.Throwable -> Lcb java.io.IOException -> Lcd java.lang.NullPointerException -> Ld2
            java.lang.String r4 = "builddate"
            java.lang.String r6 = "NOT KNOWN"
            java.lang.String r0 = r5.getProperty(r4, r6)     // Catch: java.lang.Throwable -> Lcb java.io.IOException -> Ld0 java.lang.NullPointerException -> Ld5
            if (r3 == 0) goto L47
            r3.close()     // Catch: java.io.IOException -> L5b
        L47:
            java.lang.String r3 = "Version: ShinobiCharts for Android %s Version %s, built on %s"
            r4 = 3
            java.lang.Object[] r4 = new java.lang.Object[r4]
            r5 = 0
            r4[r5] = r2
            r2 = 1
            r4[r2] = r1
            r1 = 2
            r4[r1] = r0
            java.lang.String r0 = java.lang.String.format(r3, r4)
            return r0
        L5b:
            r3 = move-exception
            android.content.Context r3 = r7.getContext()
            int r4 = com.shinobicontrols.charts.R.string.ChartFailedToCloseInputStream
            java.lang.String r3 = r3.getString(r4)
            com.shinobicontrols.charts.cx.b(r3)
            goto L47
        L6a:
            r3 = move-exception
            r3 = r1
            r1 = r4
        L6d:
            android.content.Context r4 = r7.getContext()     // Catch: java.lang.Throwable -> Lcb
            int r5 = com.shinobicontrols.charts.R.string.ChartNoVersionProperties     // Catch: java.lang.Throwable -> Lcb
            java.lang.String r4 = r4.getString(r5)     // Catch: java.lang.Throwable -> Lcb
            com.shinobicontrols.charts.cx.b(r4)     // Catch: java.lang.Throwable -> Lcb
            if (r3 == 0) goto L47
            r3.close()     // Catch: java.io.IOException -> L80
            goto L47
        L80:
            r3 = move-exception
            android.content.Context r3 = r7.getContext()
            int r4 = com.shinobicontrols.charts.R.string.ChartFailedToCloseInputStream
            java.lang.String r3 = r3.getString(r4)
            com.shinobicontrols.charts.cx.b(r3)
            goto L47
        L8f:
            r3 = move-exception
            r3 = r1
            r1 = r4
        L92:
            android.content.Context r4 = r7.getContext()     // Catch: java.lang.Throwable -> Lcb
            int r5 = com.shinobicontrols.charts.R.string.ChartNoVersionProperties     // Catch: java.lang.Throwable -> Lcb
            java.lang.String r4 = r4.getString(r5)     // Catch: java.lang.Throwable -> Lcb
            com.shinobicontrols.charts.cx.b(r4)     // Catch: java.lang.Throwable -> Lcb
            if (r3 == 0) goto L47
            r3.close()     // Catch: java.io.IOException -> La5
            goto L47
        La5:
            r3 = move-exception
            android.content.Context r3 = r7.getContext()
            int r4 = com.shinobicontrols.charts.R.string.ChartFailedToCloseInputStream
            java.lang.String r3 = r3.getString(r4)
            com.shinobicontrols.charts.cx.b(r3)
            goto L47
        Lb4:
            r0 = move-exception
            r3 = r1
        Lb6:
            if (r3 == 0) goto Lbb
            r3.close()     // Catch: java.io.IOException -> Lbc
        Lbb:
            throw r0
        Lbc:
            r1 = move-exception
            android.content.Context r1 = r7.getContext()
            int r2 = com.shinobicontrols.charts.R.string.ChartFailedToCloseInputStream
            java.lang.String r1 = r1.getString(r2)
            com.shinobicontrols.charts.cx.b(r1)
            goto Lbb
        Lcb:
            r0 = move-exception
            goto Lb6
        Lcd:
            r1 = move-exception
            r1 = r4
            goto L92
        Ld0:
            r4 = move-exception
            goto L92
        Ld2:
            r1 = move-exception
            r1 = r4
            goto L6d
        Ld5:
            r4 = move-exception
            goto L6d
        */
        throw new UnsupportedOperationException("Method not decompiled: com.shinobicontrols.charts.v.getInfo():java.lang.String");
    }

    @Override // com.shinobicontrols.charts.ShinobiChart
    public Legend getLegend() {
        return this.t;
    }

    @Override // com.shinobicontrols.charts.ShinobiChart
    public Crosshair getCrosshair() {
        return this.h;
    }

    Axis<?, ?> i() {
        return this.e.a();
    }

    Axis<?, ?> j() {
        return this.f.a();
    }

    @Override // com.shinobicontrols.charts.ShinobiChart
    public Axis<?, ?> getXAxis() {
        return i();
    }

    @Override // com.shinobicontrols.charts.ShinobiChart
    public void setXAxis(Axis<?, ?> axis) {
        a(axis, this.e);
        e(axis);
    }

    private void a(Axis<?, ?> axis, h hVar) {
        if (axis == null) {
            throw new IllegalArgumentException(getContext().getString(R.string.ChartCannotAddNullAxis));
        }
        if (b(hVar)) {
            L();
        }
        if (l()) {
            throw new IllegalStateException(getContext().getString(R.string.ChartPieDonutCannotHaveAxes));
        }
        if (axis.b == this) {
            throw new IllegalStateException(getContext().getString(R.string.ChartAlreadyHasThisAxis));
        }
        if (axis.b != null) {
            throw new IllegalArgumentException(getContext().getString(R.string.ChartAxisBelongsToAnotherChart));
        }
    }

    private void e(Axis<?, ?> axis) {
        Axis<?, ?> i = i();
        if (i != null) {
            removeXAxis(i);
        }
        a(axis, Axis.Orientation.HORIZONTAL, this.p.e());
        this.e.a(axis);
        this.d.a(axis);
    }

    @Override // com.shinobicontrols.charts.ShinobiChart
    public Axis<?, ?> getYAxis() {
        return j();
    }

    @Override // com.shinobicontrols.charts.ShinobiChart
    public void setYAxis(Axis<?, ?> axis) {
        a(axis, this.f);
        f(axis);
    }

    private void f(Axis<?, ?> axis) {
        Axis<?, ?> j = j();
        if (j != null) {
            removeYAxis(j);
        }
        a(axis, Axis.Orientation.VERTICAL, this.p.f());
        this.f.a(axis);
        this.d.b(axis);
    }

    @Override // com.shinobicontrols.charts.ShinobiChart
    public void applyTheme(int themeResID, boolean overwrite) {
        if (overwrite) {
            this.p = de.a(getContext(), themeResID);
        } else {
            this.p = de.a(getContext(), this.p, themeResID);
        }
        a(overwrite);
    }

    @Override // com.shinobicontrols.charts.ShinobiChart
    public String getTitle() {
        if (this.s == null) {
            return null;
        }
        return this.s.getText().toString();
    }

    @Override // com.shinobicontrols.charts.ShinobiChart
    public void setTitle(String title) {
        if (this.s == null) {
            b(getContext());
        }
        this.s.setText(title);
        if (title != null) {
            this.s.setVisibility(0);
        } else {
            this.s.setVisibility(8);
        }
    }

    @Override // com.shinobicontrols.charts.ShinobiChart
    public void addXAxis(Axis<?, ?> axis) {
        a(axis, this.e);
        if (this.e.b()) {
            g(axis);
        } else {
            e(axis);
        }
    }

    private void g(Axis<?, ?> axis) {
        this.e.b(axis);
        a(axis, Axis.Orientation.HORIZONTAL, this.p.e());
    }

    @Override // com.shinobicontrols.charts.ShinobiChart
    public void addYAxis(Axis<?, ?> axis) {
        a(axis, this.f);
        if (this.f.b()) {
            h(axis);
        } else {
            f(axis);
        }
    }

    private void h(Axis<?, ?> axis) {
        this.f.b(axis);
        a(axis, Axis.Orientation.VERTICAL, this.p.f());
    }

    private void a(Axis<?, ?> axis, Axis.Orientation orientation, AxisStyle axisStyle) {
        axis.a(orientation);
        AxisStyle axisStyle2 = axis.getStyle() == null ? new AxisStyle() : axis.getStyle();
        axisStyle2.a(axisStyle);
        axis.setStyle(axisStyle2);
        axis.a(this);
        this.b.a(axis.z());
        this.b.addView(axis.t());
    }

    @Override // com.shinobicontrols.charts.ShinobiChart
    public ChartStyle getStyle() {
        return this.q;
    }

    @Override // com.shinobicontrols.charts.ShinobiChart
    public void setStyle(ChartStyle style) {
        this.q = style;
    }

    @Override // com.shinobicontrols.charts.ShinobiChart
    public MainTitleStyle getTitleStyle() {
        return this.r;
    }

    @Override // com.shinobicontrols.charts.ShinobiChart
    public void setTitleStyle(MainTitleStyle titleStyle) {
        this.r = titleStyle;
    }

    void a(boolean z) {
        if (this.p != null) {
            if (z || this.q == null) {
                this.q = new ChartStyle();
            }
            this.q.a(this.p.a());
            if (z || this.r == null) {
                this.r = new MainTitleStyle();
            }
            this.r.a(this.p.b());
            for (int i = 0; i < this.e.a.length; i++) {
                Axis<?, ?> axis = this.e.a[i];
                AxisStyle axisStyle = (z || axis.getStyle() == null) ? new AxisStyle() : axis.getStyle();
                axisStyle.a(this.p.e());
                axis.setStyle(axisStyle);
            }
            for (int i2 = 0; i2 < this.f.a.length; i2++) {
                Axis<?, ?> axis2 = this.f.a[i2];
                AxisStyle axisStyle2 = (z || axis2.getStyle() == null) ? new AxisStyle() : axis2.getStyle();
                axisStyle2.a(this.p.f());
                axis2.setStyle(axisStyle2);
            }
            if (this.t != null) {
                if (z || this.t.getStyle() == null) {
                    this.t.setStyle(new LegendStyle());
                }
                this.t.getStyle().a(this.p.d());
            }
            if (this.h != null) {
                if (z || this.h.getStyle() == null) {
                    this.h.setStyle(new CrosshairStyle());
                }
                this.h.getStyle().a(this.p.c());
            }
            for (Series<?> series : this.c) {
                series.c(this.p, this.v.c(series), z);
            }
            for (Annotation annotation : this.l.getAnnotations()) {
                if (z || annotation.getStyle() == null) {
                    annotation.setStyle(new AnnotationStyle());
                }
                annotation.getStyle().a(this.p.g());
            }
        }
    }

    @Override // com.shinobicontrols.charts.ShinobiChart
    public void redrawChart() {
        if (this.t != null) {
            this.t.a();
        }
        t();
        this.b.a(this.q);
        this.s.a(this.r);
        if (this.h != null) {
            this.h.f();
        }
        this.l.b();
        J();
    }

    private void J() {
        invalidate();
        requestLayout();
        this.b.invalidate();
        this.b.requestLayout();
        for (int i = 0; i < this.e.a.length; i++) {
            this.e.a[i].u.c();
        }
        for (int i2 = 0; i2 < this.f.a.length; i2++) {
            this.f.a[i2].u.c();
        }
    }

    @Override // com.shinobicontrols.charts.ShinobiChart
    public void removeXAxis(Axis<?, ?> axis) {
        this.d.c(axis);
        b(axis, this.e);
    }

    @Override // com.shinobicontrols.charts.ShinobiChart
    public void removeYAxis(Axis<?, ?> axis) {
        this.d.d(axis);
        b(axis, this.f);
    }

    private void b(Axis<?, ?> axis, h hVar) {
        hVar.c(axis);
        this.b.b(axis.z());
        this.b.removeView(axis.t());
        axis.h();
        this.l.removeAllAnnotations(axis);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public w k() {
        return this.b;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean l() {
        for (Object obj : this.c.toArray()) {
            if (((Series) obj) instanceof PieDonutSeries) {
                return true;
            }
        }
        return false;
    }

    @Override // android.view.ViewGroup
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (f()) {
            switch (ev.getActionMasked()) {
                case 0:
                case 2:
                    if (K()) {
                        getParent().requestDisallowInterceptTouchEvent(true);
                        break;
                    }
                    break;
                case 1:
                case 3:
                    if (K()) {
                        getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                    }
                    break;
            }
        }
        return super.onInterceptTouchEvent(ev);
    }

    private boolean K() {
        return (this.h != null && this.h.isActive()) || a(this.e) || a(this.f);
    }

    private boolean a(h hVar) {
        int length = hVar.a.length;
        for (int i = 0; i < length; i++) {
            Axis<?, ?> axis = hVar.a[i];
            if (axis.isGesturePanningEnabled() || axis.isGestureZoomingEnabled()) {
                return true;
            }
        }
        return false;
    }

    @Override // com.shinobicontrols.charts.ShinobiChart
    public void setOnGestureListener(ShinobiChart.OnGestureListener listener) {
        this.B = listener;
    }

    @Override // com.shinobicontrols.charts.ShinobiChart
    public void setOnAxisMotionStateChangeListener(ShinobiChart.OnAxisMotionStateChangeListener listener) {
        this.C = listener;
    }

    @Override // com.shinobicontrols.charts.ShinobiChart
    public void setOnAxisRangeChangeListener(ShinobiChart.OnAxisRangeChangeListener listener) {
        this.D = listener;
    }

    @Override // com.shinobicontrols.charts.ShinobiChart
    public void setOnInternalLayoutListener(ShinobiChart.OnInternalLayoutListener listener) {
        this.E = listener;
    }

    @Override // com.shinobicontrols.charts.ShinobiChart
    public void setOnSeriesSelectionListener(ShinobiChart.OnSeriesSelectionListener listener) {
        this.F = listener;
    }

    @Override // com.shinobicontrols.charts.ShinobiChart
    public void setOnSnapshotDoneListener(ShinobiChart.OnSnapshotDoneListener listener) {
        this.k = listener;
    }

    @Override // com.shinobicontrols.charts.ShinobiChart
    public void setOnSeriesAnimationListener(ShinobiChart.OnSeriesAnimationListener listener) {
        this.G = listener;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a(Axis<?, ?> axis) {
        if (this.C != null) {
            this.C.onAxisMotionStateChange(axis);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void b(Axis<?, ?> axis) {
        if (this.D != null) {
            this.D.onAxisRangeChange(axis);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void m() {
        if (this.H != null) {
            this.H.onCrosshairActivationStateChanged(this);
        }
    }

    void n() {
        if (this.E != null) {
            this.E.onInternalLayout(this);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a(PointF pointF) {
        if (this.B != null) {
            this.B.onDoubleTapDown(this, g(pointF));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void b(PointF pointF) {
        if (this.B != null) {
            this.B.onDoubleTapUp(this, g(pointF));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void c(PointF pointF) {
        if (this.B != null) {
            this.B.onLongTouchDown(this, g(pointF));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void d(PointF pointF) {
        if (this.B != null) {
            this.B.onLongTouchUp(this, g(pointF));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a(PointF pointF, PointF pointF2, PointF pointF3) {
        if (this.B != null) {
            this.B.onPinch(this, g(pointF), g(pointF2), pointF3);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a(PointF pointF, boolean z, PointF pointF2) {
        if (this.B != null) {
            this.B.onPinchEnd(this, g(pointF), z, pointF2);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a(PointF pointF, PointF pointF2) {
        if (this.B != null) {
            this.B.onSecondTouchDown(this, g(pointF), g(pointF2));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void b(PointF pointF, PointF pointF2) {
        if (this.B != null) {
            this.B.onSecondTouchUp(this, g(pointF), g(pointF2));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void e(PointF pointF) {
        if (this.B != null) {
            this.B.onSingleTouchDown(this, g(pointF));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void f(PointF pointF) {
        if (this.B != null) {
            this.B.onSingleTouchUp(this, g(pointF));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void c(PointF pointF, PointF pointF2) {
        if (this.B != null) {
            this.B.onSwipe(this, g(pointF), g(pointF2));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void b(PointF pointF, boolean z, PointF pointF2) {
        if (this.B != null) {
            this.B.onSwipeEnd(this, g(pointF), z, pointF2);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a(CartesianSeries<?> cartesianSeries) {
        if (this.F != null) {
            this.F.onSeriesSelectionStateChanged(cartesianSeries);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a(Series<?> series, int i) {
        if (this.F != null) {
            this.F.onPointSelectionStateChanged(series, i);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void c(Series<?> series) {
        if (this.G != null) {
            this.G.onSeriesAnimationFinished(series);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a(PieDonutSlice pieDonutSlice, PieDonutSeries<?> pieDonutSeries) {
        if (this.J != null) {
            this.J.onUpdateSlice(pieDonutSlice, pieDonutSeries);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a(TickMark tickMark, Axis<?, ?> axis) {
        if (this.K != null) {
            this.K.onUpdateTickMark(tickMark, axis);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean a(Canvas canvas, TickMark tickMark, Rect rect, Rect rect2, Axis<?, ?> axis) {
        boolean z = this.L != null;
        if (z) {
            this.L.onDrawTickMark(canvas, tickMark, rect, rect2, axis);
        }
        return z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean a(Canvas canvas, PieDonutSlice pieDonutSlice, Rect rect, PieDonutSeries<?> pieDonutSeries) {
        boolean z = this.I != null;
        if (z) {
            this.I.onDrawLabel(canvas, pieDonutSlice, rect, pieDonutSeries);
        }
        return z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean a(Canvas canvas, Rect rect, float f, float f2, float f3, Paint paint) {
        boolean z = this.M != null;
        if (z) {
            this.M.onDrawCrosshair(this, canvas, rect, f, f2, f3, paint);
        }
        return z;
    }

    private PointF g(PointF pointF) {
        PointF pointF2 = new PointF(pointF.x, pointF.y);
        pointF2.offset(this.a.left + this.b.b.left, this.a.top + this.b.b.top);
        return pointF2;
    }

    private void L() {
        if (!this.P) {
            throw new UnsupportedOperationException(getContext().getString(R.string.ChartPremiumOnly));
        }
    }

    private boolean e(Series<?> series) {
        return (series instanceof BandSeries) || (series instanceof OHLCSeries) || (series instanceof CandlestickSeries);
    }

    private boolean b(h hVar) {
        return hVar.b();
    }

    @Override // com.shinobicontrols.charts.ShinobiChart
    public void setOnPieDonutSliceLabelDrawListener(ShinobiChart.OnPieDonutSliceLabelDrawListener pieDonutSliceLabelListener) {
        this.I = pieDonutSliceLabelListener;
    }

    @Override // com.shinobicontrols.charts.ShinobiChart
    public void setOnPieDonutSliceUpdateListener(ShinobiChart.OnPieDonutSliceUpdateListener onPieDonutSliceUpdateListener) {
        this.J = onPieDonutSliceUpdateListener;
    }

    @Override // com.shinobicontrols.charts.ShinobiChart
    public void setOnTickMarkUpdateListener(ShinobiChart.OnTickMarkUpdateListener onTickMarkUpdateListener) {
        for (Axis<?, ?> axis : this.e.a) {
            axis.u.a = true;
        }
        for (Axis<?, ?> axis2 : this.f.a) {
            axis2.u.a = true;
        }
        this.K = onTickMarkUpdateListener;
    }

    @Override // com.shinobicontrols.charts.ShinobiChart
    public void setOnTickMarkDrawListener(ShinobiChart.OnTickMarkDrawListener onTickMarkDrawListener) {
        for (Axis<?, ?> axis : this.e.a) {
            axis.u.a = true;
        }
        for (Axis<?, ?> axis2 : this.f.a) {
            axis2.u.a = true;
        }
        this.L = onTickMarkDrawListener;
    }

    @Override // com.shinobicontrols.charts.ShinobiChart
    public AnnotationsManager getAnnotationsManager() {
        return this.l;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void o() {
        L();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a(final Bitmap bitmap) {
        if (this.k != null) {
            if (m) {
                this.b.a(bitmap);
            }
            post(new Runnable() { // from class: com.shinobicontrols.charts.v.1
                @Override // java.lang.Runnable
                public void run() {
                    v.this.k.onSnapshotDone(v.this.b(bitmap));
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Bitmap b(Bitmap bitmap) {
        Bitmap createBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        draw(canvas);
        if (!m) {
            BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(), bitmap);
            bitmapDrawable.setBounds(getPlotAreaRect());
            bitmapDrawable.draw(canvas);
        }
        BitmapDrawable bitmapDrawable2 = new BitmapDrawable(getResources(), createBitmap);
        bitmapDrawable2.setAntiAlias(true);
        return bitmapDrawable2.getBitmap();
    }

    @Override // com.shinobicontrols.charts.ShinobiChart
    public void requestSnapshot() {
        this.b.l();
        redrawChart();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public dd p() {
        return this.p;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean q() {
        for (Axis<?, ?> axis : this.e.a) {
            if (axis.isDoubleTapEnabled()) {
                return true;
            }
        }
        for (Axis<?, ?> axis2 : this.f.a) {
            if (axis2.isDoubleTapEnabled()) {
                return true;
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean r() {
        for (CartesianSeries<?> cartesianSeries : h()) {
            if (cartesianSeries.l.a) {
                return true;
            }
        }
        return false;
    }

    @Override // com.shinobicontrols.charts.ShinobiChart
    public void setOnCrosshairDrawListener(ShinobiChart.OnCrosshairDrawListener onCrosshairDrawListener) {
        this.M = onCrosshairDrawListener;
    }

    @Override // com.shinobicontrols.charts.ShinobiChart
    public void setOnTrackingInfoChangedForTooltipListener(ShinobiChart.OnTrackingInfoChangedForTooltipListener listener) {
        this.N = listener;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean a(CartesianSeries<?> cartesianSeries, DataPoint<?, ?> dataPoint, DataPoint<?, ?> dataPoint2, DataPoint<?, ?> dataPoint3) {
        boolean z = this.N != null;
        if (z) {
            this.N.onTrackingInfoChanged(this.h.g, dataPoint, dataPoint2, dataPoint3);
        }
        return z;
    }

    @Override // com.shinobicontrols.charts.ShinobiChart
    public void setOnTrackingInfoChangedForCrosshairListener(ShinobiChart.OnTrackingInfoChangedForCrosshairListener listener) {
        this.O = listener;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean b(CartesianSeries<?> cartesianSeries, DataPoint<?, ?> dataPoint, DataPoint<?, ?> dataPoint2, DataPoint<?, ?> dataPoint3) {
        boolean z = this.O != null;
        if (z) {
            this.O.onTrackingInfoChanged(this.h, dataPoint, dataPoint2, dataPoint3);
        }
        return z;
    }

    @Override // com.shinobicontrols.charts.ShinobiChart
    public void setOnCrosshairActivationStateChangedListener(ShinobiChart.OnCrosshairActivationStateChangedListener listener) {
        this.H = listener;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void s() {
        L();
    }
}

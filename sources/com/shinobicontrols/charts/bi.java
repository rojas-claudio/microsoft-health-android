package com.shinobicontrols.charts;

import com.microsoft.kapp.utils.Constants;
import com.shinobicontrols.charts.SeriesStyle;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class bi extends t<LineSeries> {
    private static float[] P = new float[0];
    private static int[] Q = new int[0];
    private int A;
    private int B;
    private int C;
    private int D;
    private int E;
    private int F;
    private int G;
    private float H;
    private SeriesStyle.FillStyle I;
    private boolean J;
    private boolean K;
    private float L;
    private int[] M;
    private float[] N;
    private float[] O;
    int a;
    int b;
    float c;
    float d;
    boolean e;
    float[] f;
    float[] g;
    int h;
    int i;
    private float n;
    private float o;
    private float p;
    private float q;
    private float r;
    private float s;
    private int t;
    private int u;
    private int v;
    private int w;
    private int x;
    private int y;
    private int z;

    /* JADX INFO: Access modifiers changed from: package-private */
    public bi(LineSeries lineSeries) {
        super(lineSeries);
        this.b = 0;
        this.e = false;
    }

    @Override // com.shinobicontrols.charts.t
    void a(SChartGLDrawer sChartGLDrawer, float[] fArr, float f) {
        if (((LineSeries) this.k).s()) {
            a((LineSeries) this.k, f);
            this.L = f;
            a(sChartGLDrawer, this.b);
            b(sChartGLDrawer, this.b);
            a(sChartGLDrawer);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a(int i) {
        if (this.f == null || this.f.length != i) {
            this.f = new float[i];
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void b(int i) {
        if (this.g == null || this.g.length != i) {
            this.g = new float[i];
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void c(int i) {
        if (this.N == null || this.N.length != i) {
            this.N = new float[i];
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void d(int i) {
        if (this.O == null || this.O.length != i) {
            this.O = new float[i];
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void e(int i) {
        if (this.M == null || this.M.length != i) {
            this.M = new int[i];
        }
    }

    private void a(SChartGLDrawer sChartGLDrawer, int i) {
        if (this.I != SeriesStyle.FillStyle.NONE) {
            if (this.K) {
                sChartGLDrawer.drawBandSeriesFill(this.f, this.k, this.g, i, this.B, this.E, ((LineSeries) this.k).j.a(), this.a, this.j);
            } else {
                sChartGLDrawer.drawHorizontalFill(this.f, this.k, i, this.B, this.E, this.C, this.F, f(this.C), f(this.F), this.m, this.c, this.d, 1 - ((LineSeries) this.k).j.a(), this.I == SeriesStyle.FillStyle.GRADIENT, this.j);
            }
        }
    }

    private void b(SChartGLDrawer sChartGLDrawer, int i) {
        if (f(this.D) && this.J) {
            sChartGLDrawer.drawLineStrip(this.f, this.k, i, this.D, this.G, this.H, this.m, 1 - ((LineSeries) this.k).j.a(), this.j);
        }
    }

    private void a(SChartGLDrawer sChartGLDrawer) {
        if (sChartGLDrawer.getPerformCalculations()) {
            if (this.e) {
                if (e()) {
                    int i = 0;
                    for (int i2 = 0; i2 < this.l.c.length; i2++) {
                        InternalDataPoint internalDataPoint = this.l.c[i2];
                        if (!internalDataPoint.h) {
                            this.f[i] = (float) internalDataPoint.c;
                            this.f[i + 1] = (float) internalDataPoint.d;
                            a(internalDataPoint, i);
                            i += 2;
                        }
                    }
                    boolean z = this.o > 0.0f && this.u != 0;
                    sChartGLDrawer.drawDataPoints(this.f, this.k, this.M, i, this.t, this.v, this.n, this.N, this.r, this.m, 1 - ((LineSeries) this.k).j.a(), this.j, (z || f()) ? false : true);
                    if (z) {
                        sChartGLDrawer.drawDataPoints(this.f, this.k, this.M, i, this.u, this.w, this.n, this.O, 0.0f, this.m, 1 - ((LineSeries) this.k).j.a(), this.j, !f());
                    }
                }
                if (f()) {
                    int i3 = 0;
                    for (int i4 = 0; i4 < this.l.c.length; i4++) {
                        InternalDataPoint internalDataPoint2 = this.l.c[i4];
                        if (internalDataPoint2.h) {
                            this.f[i3] = (float) internalDataPoint2.c;
                            this.f[i3 + 1] = (float) internalDataPoint2.d;
                            a(internalDataPoint2, i3);
                            i3 += 2;
                        }
                    }
                    boolean z2 = this.q > 0.0f && this.y != 0;
                    sChartGLDrawer.drawDataPoints(this.f, this.k, this.M, i3, this.x, this.z, this.p, this.N, this.s, this.m, 1 - ((LineSeries) this.k).j.a(), this.j, !z2);
                    if (z2) {
                        sChartGLDrawer.drawDataPoints(this.f, this.k, this.M, i3, this.y, this.A, this.p, this.O, 0.0f, this.m, 1 - ((LineSeries) this.k).j.a(), this.j, true);
                    }
                }
            } else if (e()) {
                float f = this.n;
                int i5 = 0;
                for (int i6 = 0; i6 < this.l.c.length; i6++) {
                    InternalDataPoint internalDataPoint3 = this.l.c[i6];
                    if (!internalDataPoint3.h) {
                        this.f[i5] = (float) internalDataPoint3.c;
                        this.f[i5 + 1] = (float) internalDataPoint3.d;
                        a(internalDataPoint3, i5);
                        i5 += 2;
                    }
                }
                boolean z3 = this.o > 0.0f && this.u != 0;
                sChartGLDrawer.drawDataPoints(this.f, this.k, this.M, i5, this.t, this.v, f, this.N, this.r, this.m, 1 - ((LineSeries) this.k).j.a(), this.j, !z3);
                if (z3) {
                    sChartGLDrawer.drawDataPoints(this.f, this.k, this.M, i5, this.u, this.w, f, this.O, 0.0f, this.m, 1 - ((LineSeries) this.k).j.a(), this.j, true);
                }
            }
        } else if (d()) {
            sChartGLDrawer.drawDataPoints(P, null, Q, 0, this.t, this.v, 0.0f, P, this.r, 0.0f, 0, this.j, true);
        }
    }

    private void a(LineSeries lineSeries, double d) {
        LineSeriesStyle lineSeriesStyle = lineSeries.d ? (LineSeriesStyle) lineSeries.r : (LineSeriesStyle) lineSeries.q;
        this.K = lineSeries.l();
        this.l = lineSeries.n;
        this.J = lineSeriesStyle.isLineShown();
        this.I = lineSeriesStyle.getFillStyle();
        this.B = lineSeriesStyle.getAreaColor();
        this.C = lineSeriesStyle.getAreaColorGradient();
        this.D = lineSeriesStyle.getLineColor();
        this.H = lineSeriesStyle.getLineWidth();
        this.E = lineSeriesStyle.getAreaColorBelowBaseline();
        this.F = lineSeriesStyle.getAreaColorGradientBelowBaseline();
        this.G = lineSeriesStyle.getLineColorBelowBaseline();
        int areaLineColor = lineSeriesStyle.getAreaLineColor();
        float areaLineWidth = lineSeriesStyle.getAreaLineWidth();
        int areaLineColorBelowBaseline = lineSeriesStyle.getAreaLineColorBelowBaseline();
        PointStyle pointStyle = lineSeriesStyle.getPointStyle();
        PointStyle selectedPointStyle = lineSeriesStyle.getSelectedPointStyle();
        this.n = pointStyle.getRadius();
        this.o = pointStyle.getInnerRadius();
        this.t = pointStyle.getColor();
        this.u = pointStyle.getInnerColor();
        this.v = pointStyle.getColorBelowBaseline();
        this.w = pointStyle.getInnerColorBelowBaseline();
        this.r = pointStyle.a();
        this.p = selectedPointStyle.getRadius();
        this.q = selectedPointStyle.getInnerRadius();
        this.x = selectedPointStyle.getColor();
        this.y = selectedPointStyle.getInnerColor();
        this.z = selectedPointStyle.getColorBelowBaseline();
        this.A = selectedPointStyle.getInnerColorBelowBaseline();
        this.s = selectedPointStyle.a();
        if (this.I != SeriesStyle.FillStyle.NONE && f(this.B)) {
            this.D = areaLineColor;
            this.G = areaLineColorBelowBaseline;
            this.H = areaLineWidth;
        }
        if (this.I != SeriesStyle.FillStyle.NONE && f(this.E)) {
            this.G = areaLineColorBelowBaseline;
        }
        this.H = (float) (this.H * d);
        this.n = (float) (this.n * d);
        this.o = (float) (this.o * d);
        this.p = (float) (this.p * d);
        this.q = (float) (this.q * d);
    }

    private boolean f(int i) {
        return i != 0;
    }

    private void a(InternalDataPoint internalDataPoint, int i) {
        int i2 = i / 2;
        double d = internalDataPoint.f * this.L;
        double d2 = internalDataPoint.g * this.L;
        if (internalDataPoint.e != -1) {
            this.M[i2] = internalDataPoint.e;
        }
        if (this.N.length > 0) {
            if (d > Constants.SPLITS_ACCURACY) {
                this.N[i2] = (float) d;
            } else if (internalDataPoint.h) {
                this.N[i2] = this.p;
            } else {
                this.N[i2] = this.n;
            }
        }
        if (this.O.length > 0) {
            if (d2 > Constants.SPLITS_ACCURACY && d2 < d) {
                this.O[i2] = (float) d2;
            } else if (internalDataPoint.h) {
                this.O[i2] = this.q;
            } else {
                this.O[i2] = this.o;
            }
        }
    }

    private boolean e() {
        return (((LineSeries) this.k).d ? (LineSeriesStyle) ((LineSeries) this.k).r : (LineSeriesStyle) ((LineSeries) this.k).q).getPointStyle().arePointsShown() && this.i > 0;
    }

    private boolean f() {
        return (((LineSeries) this.k).d ? (LineSeriesStyle) ((LineSeries) this.k).r : (LineSeriesStyle) ((LineSeries) this.k).q).getSelectedPointStyle().arePointsShown() && this.h > 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean d() {
        return e() || f();
    }
}

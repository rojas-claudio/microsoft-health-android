package com.shinobicontrols.charts;

import com.microsoft.kapp.utils.Constants;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class k extends t<BandSeries> {
    private static float[] I = new float[0];
    private static int[] J = new int[0];
    private int A;
    private float B;
    private float C;
    private float D;
    private float E;
    private float F;
    private boolean G;
    private boolean H;
    float[] a;
    float[] b;
    float[] c;
    float[] d;
    float[] e;
    float[] f;
    int g;
    int h;
    int i;
    private float[] n;
    private float[] o;
    private float[] p;
    private int[] q;
    private int[] r;
    private int[] s;
    private int t;
    private int u;
    private int v;
    private int w;
    private int x;
    private int y;
    private int z;

    public k(BandSeries bandSeries) {
        super(bandSeries);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a(int i) {
        if (this.a == null || this.a.length != i * 2) {
            this.a = new float[i * 2];
        }
        if (this.d == null || this.d.length != i * 2) {
            this.d = new float[i * 2];
        }
        if (this.n == null || this.n.length != i) {
            this.n = new float[i];
        }
        if (this.q == null || this.q.length != i) {
            this.q = new int[i];
        }
        if (this.b == null || this.b.length != i * 2) {
            this.b = new float[i * 2];
        }
        if (this.e == null || this.e.length != i * 2) {
            this.e = new float[i * 2];
        }
        if (this.o == null || this.o.length != i) {
            this.o = new float[i];
        }
        if (this.r == null || this.r.length != i) {
            this.r = new int[i];
        }
        if (this.c == null || this.c.length != i * 2) {
            this.c = new float[i * 2];
        }
        if (this.f == null || this.f.length != i * 2) {
            this.f = new float[i * 2];
        }
        if (this.p == null || this.p.length != i) {
            this.p = new float[i];
        }
        if (this.s == null || this.s.length != i) {
            this.s = new int[i];
        }
    }

    @Override // com.shinobicontrols.charts.t
    void a(SChartGLDrawer sChartGLDrawer, float[] fArr, float f) {
        if (((BandSeries) this.k).s()) {
            a((BandSeries) this.k, f);
            sChartGLDrawer.drawBandSeriesFill(this.a, this.k, this.d, this.l.c.length * 2, this.z, this.A, ((BandSeries) this.k).j.a(), this.i, fArr);
            sChartGLDrawer.drawLineStrip(this.d, this.k, this.l.c.length * 2, this.u, this.u, this.B, this.m, ((BandSeries) this.k).j.a(), fArr);
            sChartGLDrawer.drawLineStrip(this.a, this.k, this.l.c.length * 2, this.t, this.t, this.B, this.m, ((BandSeries) this.k).j.a(), fArr);
            if (this.g > 0) {
                if (!sChartGLDrawer.getPerformCalculations()) {
                    if (d() || e()) {
                        sChartGLDrawer.drawDataPoints(I, null, J, 0, this.w, this.w, 0.0f, I, this.D, 0.0f, ((BandSeries) this.k).j.a(), fArr, false);
                        return;
                    }
                    return;
                }
                if (d()) {
                    int i = 0;
                    for (int i2 = 0; i2 < this.l.c.length; i2++) {
                        InternalDataPoint internalDataPoint = this.l.c[i2];
                        if (!internalDataPoint.h) {
                            a(i, internalDataPoint, this.r);
                            a(i, internalDataPoint, this.o);
                            i++;
                        }
                    }
                    sChartGLDrawer.drawDataPoints(this.e, this.k, this.r, this.h * 2, this.w, this.w, this.C, this.o, this.D, this.m, ((BandSeries) this.k).j.a(), fArr, false);
                    sChartGLDrawer.drawDataPoints(this.b, this.k, this.r, this.h * 2, this.v, this.v, this.C, this.o, this.D, this.m, ((BandSeries) this.k).j.a(), fArr, !e());
                }
                if (e()) {
                    int i3 = 0;
                    for (int i4 = 0; i4 < this.l.c.length; i4++) {
                        InternalDataPoint internalDataPoint2 = this.l.c[i4];
                        if (internalDataPoint2.h) {
                            a(i3, internalDataPoint2, this.s);
                            a(i3, internalDataPoint2, this.p);
                            i3++;
                        }
                    }
                    sChartGLDrawer.drawDataPoints(this.f, this.k, this.s, this.g * 2, this.y, this.y, this.E, this.p, this.F, this.m, ((BandSeries) this.k).j.a(), fArr, false);
                    sChartGLDrawer.drawDataPoints(this.c, this.k, this.s, this.g * 2, this.x, this.x, this.E, this.p, this.F, this.m, ((BandSeries) this.k).j.a(), fArr, true);
                }
            } else if (d()) {
                if (sChartGLDrawer.getPerformCalculations()) {
                    int i5 = 0;
                    for (int i6 = 0; i6 < this.l.c.length; i6++) {
                        InternalDataPoint internalDataPoint3 = this.l.c[i6];
                        a(i5, internalDataPoint3, this.q);
                        a(i5, internalDataPoint3, this.n);
                        i5++;
                    }
                }
                sChartGLDrawer.drawDataPoints(this.d, this.k, this.q, this.l.c.length * 2, this.w, this.w, this.C, this.n, this.D, this.m, ((BandSeries) this.k).j.a(), fArr, true);
                sChartGLDrawer.drawDataPoints(this.a, this.k, this.q, this.l.c.length * 2, this.v, this.v, this.C, this.n, this.D, this.m, ((BandSeries) this.k).j.a(), fArr, true);
            }
        }
    }

    private void a(int i, InternalDataPoint internalDataPoint, float[] fArr) {
        if (fArr != null) {
            if (internalDataPoint.f > Constants.SPLITS_ACCURACY) {
                fArr[i] = (float) internalDataPoint.f;
            } else {
                fArr[i] = internalDataPoint.h ? this.E : this.C;
            }
        }
    }

    private void a(int i, InternalDataPoint internalDataPoint, int[] iArr) {
        if (internalDataPoint.e != -1) {
            iArr[i] = internalDataPoint.e;
        }
    }

    private boolean d() {
        return this.G && this.h > 0;
    }

    private boolean e() {
        return this.H && this.g > 0;
    }

    private void a(BandSeries bandSeries, double d) {
        BandSeriesStyle bandSeriesStyle = bandSeries.d ? (BandSeriesStyle) bandSeries.r : (BandSeriesStyle) bandSeries.q;
        this.l = bandSeries.n;
        this.G = bandSeriesStyle.getPointStyle().arePointsShown();
        this.H = bandSeriesStyle.getSelectedPointStyle().arePointsShown();
        this.t = bandSeriesStyle.getLineColorHigh();
        this.u = bandSeriesStyle.getLineColorLow();
        this.z = bandSeriesStyle.isFillShown() ? bandSeriesStyle.getAreaColorNormal() : 0;
        this.A = bandSeriesStyle.isFillShown() ? bandSeriesStyle.getAreaColorInverted() : 0;
        this.B = bandSeriesStyle.getLineWidth() * ((float) d);
        this.v = bandSeriesStyle.getPointStyle().arePointsShown() ? bandSeriesStyle.getPointStyle().getColor() : 0;
        this.w = bandSeriesStyle.getPointStyle().arePointsShown() ? bandSeriesStyle.getPointStyle().getColorBelowBaseline() : 0;
        this.C = bandSeriesStyle.getPointStyle().getRadius() * ((float) d);
        this.D = bandSeriesStyle.getPointStyle().a();
        this.x = bandSeriesStyle.getSelectedPointStyle().arePointsShown() ? bandSeriesStyle.getSelectedPointStyle().getColor() : 0;
        this.y = bandSeriesStyle.getSelectedPointStyle().arePointsShown() ? bandSeriesStyle.getSelectedPointStyle().getColorBelowBaseline() : 0;
        this.E = bandSeriesStyle.getSelectedPointStyle().getRadius() * ((float) d);
        this.F = bandSeriesStyle.getSelectedPointStyle().a();
    }
}

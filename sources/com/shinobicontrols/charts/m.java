package com.shinobicontrols.charts;

import com.shinobicontrols.charts.SeriesStyle;
import java.util.Arrays;
/* loaded from: classes.dex */
class m extends t<BarColumnSeries<?>> {
    float[] a;
    float[] b;
    int c;
    private int d;
    private int e;
    private int f;
    private int g;
    private int h;
    private int i;
    private float n;
    private boolean o;
    private SeriesStyle.FillStyle p;
    private float q;
    private final int r;

    /* JADX INFO: Access modifiers changed from: package-private */
    public m(BarColumnSeries<?> barColumnSeries) {
        super(barColumnSeries);
        this.c = 0;
        this.r = barColumnSeries.j.a();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a(int i) {
        if (this.a == null || this.a.length != i) {
            this.a = new float[i];
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void b(int i) {
        if (this.b == null || this.b.length != i) {
            this.b = new float[i];
        }
    }

    @Override // com.shinobicontrols.charts.t
    void a(SChartGLDrawer sChartGLDrawer, float[] fArr, float f) {
        if (((BarColumnSeries) this.k).s()) {
            a((BarColumnSeries) this.k, f);
            boolean z = !(this.d == 0 && this.g == 0) && this.o;
            boolean z2 = this.p != SeriesStyle.FillStyle.NONE;
            if (this.p != SeriesStyle.FillStyle.GRADIENT) {
                this.f = this.e;
                this.i = this.h;
            }
            if (z && !z2) {
                this.e = 0;
                this.h = this.e;
                this.f = this.e;
                this.i = this.e;
                z2 = true;
            }
            if (z2) {
                sChartGLDrawer.drawBarColumnFill(this.a, this.k, this.b, this.c, this.e, this.h, this.f, this.i, this.q, this.m, this.r, z, fArr);
            }
            if (z) {
                sChartGLDrawer.drawBarColumnLine(this.a, this.k, this.b, this.c, this.d, this.g, this.d != 0, this.g != 0, this.q, this.n, this.m, this.r, fArr);
            }
            if (this.b != null) {
                Arrays.fill(this.b, 0.0f);
            }
        }
    }

    private void a(BarColumnSeries<?> barColumnSeries, double d) {
        BarColumnSeriesStyle barColumnSeriesStyle = (BarColumnSeriesStyle) (barColumnSeries.d ? barColumnSeries.r : barColumnSeries.q);
        this.q = barColumnSeries.a();
        this.l = barColumnSeries.n;
        this.d = barColumnSeriesStyle.getLineColor();
        this.n = barColumnSeriesStyle.getLineWidth();
        this.e = barColumnSeriesStyle.getAreaColor();
        this.f = barColumnSeriesStyle.getAreaColorGradient();
        this.g = barColumnSeriesStyle.getLineColorBelowBaseline();
        this.h = barColumnSeriesStyle.getAreaColorBelowBaseline();
        this.i = barColumnSeriesStyle.getAreaColorGradientBelowBaseline();
        this.o = barColumnSeriesStyle.isLineShown();
        this.p = barColumnSeriesStyle.getFillStyle();
        this.n = (float) (this.n * d);
    }
}

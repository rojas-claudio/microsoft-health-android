package com.shinobicontrols.charts;
/* loaded from: classes.dex */
class p extends t<CandlestickSeries> {
    float[] a;
    private int[] b;
    private int[] c;
    private final int d;
    private float e;
    private float f;
    private int g;
    private int h;
    private int i;
    private int n;
    private int o;
    private int p;
    private int q;
    private int r;
    private int s;
    private int t;
    private boolean u;
    private boolean v;
    private float w;

    public p(CandlestickSeries candlestickSeries) {
        super(candlestickSeries);
        this.d = candlestickSeries.j.a();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a(int i) {
        if (this.a == null || this.a.length != i * 5) {
            this.a = new float[i * 5];
        }
        if (this.b == null || this.b.length != i) {
            this.b = new int[i];
        }
        if (this.c == null || this.c.length != i) {
            this.c = new int[i];
        }
    }

    @Override // com.shinobicontrols.charts.t
    void a(SChartGLDrawer sChartGLDrawer, float[] fArr, float f) {
        if (((CandlestickSeries) this.k).s()) {
            a((CandlestickSeries) this.k, f);
            for (int i = 0; i < this.l.c.length; i++) {
                InternalDataPoint internalDataPoint = this.l.c[i];
                if (internalDataPoint.a()) {
                    if (internalDataPoint.h) {
                        this.b[i] = this.s;
                        this.c[i] = this.t;
                    } else {
                        this.b[i] = this.o;
                        this.c[i] = this.p;
                    }
                } else if (internalDataPoint.h) {
                    this.b[i] = this.q;
                    this.c[i] = this.r;
                } else {
                    this.b[i] = this.i;
                    this.c[i] = this.n;
                }
            }
            sChartGLDrawer.drawCandlesticks(this.a, this.k, this.l.c.length, this.b, this.c, this.g, this.h, this.u, this.v, this.w, this.e, this.f, this.d, f, fArr);
        }
    }

    private void a(CandlestickSeries candlestickSeries, double d) {
        boolean z = true;
        CandlestickSeriesStyle candlestickSeriesStyle = candlestickSeries.d ? (CandlestickSeriesStyle) candlestickSeries.r : (CandlestickSeriesStyle) candlestickSeries.q;
        this.w = candlestickSeries.a();
        this.l = candlestickSeries.n;
        this.e = (float) (candlestickSeriesStyle.getOutlineWidth() * d);
        this.f = (float) (candlestickSeriesStyle.getStickWidth() * d);
        this.g = candlestickSeriesStyle.getOutlineColor();
        this.h = candlestickSeriesStyle.getStickColor();
        this.i = ((CandlestickSeriesStyle) candlestickSeries.q).getFallingColor();
        this.n = ((CandlestickSeriesStyle) candlestickSeries.q).getFallingColorGradient();
        this.o = ((CandlestickSeriesStyle) candlestickSeries.q).getRisingColor();
        this.p = ((CandlestickSeriesStyle) candlestickSeries.q).getRisingColorGradient();
        this.q = ((CandlestickSeriesStyle) candlestickSeries.r).getFallingColor();
        this.r = ((CandlestickSeriesStyle) candlestickSeries.r).getFallingColorGradient();
        this.s = ((CandlestickSeriesStyle) candlestickSeries.r).getRisingColor();
        this.t = ((CandlestickSeriesStyle) candlestickSeries.r).getRisingColorGradient();
        this.u = this.g != 0 && this.e > 0.0f;
        if (this.h == 0 || this.f <= 0.0f) {
            z = false;
        }
        this.v = z;
    }
}

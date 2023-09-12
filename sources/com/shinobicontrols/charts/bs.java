package com.shinobicontrols.charts;
/* loaded from: classes.dex */
class bs extends t<OHLCSeries> {
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
    private float s;

    public bs(OHLCSeries oHLCSeries) {
        super(oHLCSeries);
        this.d = oHLCSeries.j.a();
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
        if (((OHLCSeries) this.k).s()) {
            a((OHLCSeries) this.k, f);
            for (int i = 0; i < this.l.c.length; i++) {
                InternalDataPoint internalDataPoint = this.l.c[i];
                if (internalDataPoint.a()) {
                    if (internalDataPoint.h) {
                        this.b[i] = this.q;
                        this.c[i] = this.r;
                    } else {
                        this.b[i] = this.i;
                        this.c[i] = this.n;
                    }
                } else if (internalDataPoint.h) {
                    this.b[i] = this.o;
                    this.c[i] = this.p;
                } else {
                    this.b[i] = this.g;
                    this.c[i] = this.h;
                }
            }
            sChartGLDrawer.drawOHLCPoints(this.a, this.k, this.l.c.length, this.b, this.c, this.d, this.s, this.f, this.e, fArr);
        }
    }

    private void a(OHLCSeries oHLCSeries, double d) {
        OHLCSeriesStyle oHLCSeriesStyle = oHLCSeries.d ? (OHLCSeriesStyle) oHLCSeries.r : (OHLCSeriesStyle) oHLCSeries.q;
        this.s = oHLCSeries.a();
        this.l = oHLCSeries.n;
        this.e = (float) (oHLCSeriesStyle.getArmWidth() * d);
        this.f = (float) (oHLCSeriesStyle.getTrunkWidth() * d);
        this.g = ((OHLCSeriesStyle) oHLCSeries.q).getFallingColor();
        this.h = ((OHLCSeriesStyle) oHLCSeries.q).getFallingColorGradient();
        this.i = ((OHLCSeriesStyle) oHLCSeries.q).getRisingColor();
        this.n = ((OHLCSeriesStyle) oHLCSeries.q).getRisingColorGradient();
        this.o = ((OHLCSeriesStyle) oHLCSeries.r).getFallingColor();
        this.p = ((OHLCSeriesStyle) oHLCSeries.r).getFallingColorGradient();
        this.q = ((OHLCSeriesStyle) oHLCSeries.r).getRisingColor();
        this.r = ((OHLCSeriesStyle) oHLCSeries.r).getRisingColorGradient();
    }
}

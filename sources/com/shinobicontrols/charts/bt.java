package com.shinobicontrols.charts;

import com.shinobicontrols.charts.PieDonutSeries;
/* loaded from: classes.dex */
class bt extends n<PieDonutSeries<?>> {
    private final a a;
    private final a b;
    private float c;
    private float d;
    private PieDonutSeries.DrawDirection e;
    private float f;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class a {
        boolean a;
        boolean b;
        Integer[] c;
        Integer[] d;
        PieDonutSeries.RadialEffect e;
        float f;

        private a() {
        }

        int a(int i) {
            return this.c[i % this.c.length].intValue();
        }

        int b(int i) {
            return this.d[i % this.d.length].intValue();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public bt(PieDonutSeries<?> pieDonutSeries) {
        super(pieDonutSeries);
        this.a = new a();
        this.b = new a();
    }

    @Override // com.shinobicontrols.charts.cu
    public void a(ai aiVar, SChartGLDrawer sChartGLDrawer) {
        double d;
        double d2;
        a((PieDonutSeries) this.k);
        float f = aiVar.c().density;
        float max = 1.0f / Math.max(aiVar.a(), aiVar.b());
        int length = this.l.c.length;
        for (int i = 0; i < length; i++) {
            PieDonutSlice pieDonutSlice = (PieDonutSlice) this.l.c[i];
            a aVar = pieDonutSlice.h ? this.b : this.a;
            if (this.e == PieDonutSeries.DrawDirection.ANTICLOCKWISE) {
                d = this.f + pieDonutSlice.n;
                d2 = this.f + pieDonutSlice.o;
            } else {
                d = this.f - pieDonutSlice.n;
                d2 = this.f - pieDonutSlice.o;
            }
            sChartGLDrawer.drawRadialSlice(i, this.k, (float) d, (float) d2, this.d, this.c, pieDonutSlice.q, aVar.a ? aVar.a(i) : 0, aVar.b ? aVar.b(i) : 0, aVar.b, aVar.f * f, (aVar.a ? aVar.e : PieDonutSeries.RadialEffect.FLAT).getXmlValue(), max);
        }
    }

    private void a(PieDonutSeries<?> pieDonutSeries) {
        PieDonutSeriesStyle pieDonutSeriesStyle = (PieDonutSeriesStyle) pieDonutSeries.q;
        PieDonutSeriesStyle pieDonutSeriesStyle2 = (PieDonutSeriesStyle) pieDonutSeries.r;
        this.c = pieDonutSeries.getOuterRadius();
        this.d = pieDonutSeries.getInnerRadius();
        this.e = pieDonutSeries.getDrawDirection();
        this.f = pieDonutSeries.getRotation();
        this.l = pieDonutSeries.n;
        this.a.a = pieDonutSeriesStyle.isFlavorShown();
        this.a.b = pieDonutSeriesStyle.isCrustShown();
        this.a.c = pieDonutSeriesStyle.b();
        this.a.d = pieDonutSeriesStyle.a();
        this.a.e = pieDonutSeriesStyle.getRadialEffect();
        this.a.f = pieDonutSeriesStyle.getCrustThickness();
        this.b.a = pieDonutSeriesStyle2.isFlavorShown();
        this.b.b = pieDonutSeriesStyle2.isCrustShown();
        this.b.c = pieDonutSeriesStyle2.b();
        this.b.d = pieDonutSeriesStyle2.a();
        this.b.e = pieDonutSeriesStyle2.getRadialEffect();
        this.b.f = pieDonutSeriesStyle2.getCrustThickness();
    }
}

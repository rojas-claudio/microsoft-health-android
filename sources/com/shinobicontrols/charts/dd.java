package com.shinobicontrols.charts;

import java.util.ArrayList;
import java.util.List;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class dd {
    final List<LineSeriesStyle> a = new ArrayList();
    final List<LineSeriesStyle> b = new ArrayList();
    final List<ColumnSeriesStyle> c = new ArrayList();
    final List<ColumnSeriesStyle> d = new ArrayList();
    final List<BarSeriesStyle> e = new ArrayList();
    final List<BarSeriesStyle> f = new ArrayList();
    final List<CandlestickSeriesStyle> g = new ArrayList();
    final List<CandlestickSeriesStyle> h = new ArrayList();
    final List<OHLCSeriesStyle> i = new ArrayList();
    final List<OHLCSeriesStyle> j = new ArrayList();
    final List<BandSeriesStyle> k = new ArrayList();
    final List<BandSeriesStyle> l = new ArrayList();
    final List<PieSeriesStyle> m = new ArrayList();
    final List<PieSeriesStyle> n = new ArrayList();
    final List<DonutSeriesStyle> o = new ArrayList();
    final List<DonutSeriesStyle> p = new ArrayList();
    private ChartStyle q;
    private MainTitleStyle r;
    private CrosshairStyle s;
    private LegendStyle t;
    private AxisStyle u;
    private AxisStyle v;
    private AnnotationStyle w;

    public void a(ColumnSeriesStyle columnSeriesStyle, boolean z) {
        if (z) {
            this.d.add(columnSeriesStyle);
        } else {
            this.c.add(columnSeriesStyle);
        }
    }

    public void a(BarSeriesStyle barSeriesStyle, boolean z) {
        if (z) {
            this.f.add(barSeriesStyle);
        } else {
            this.e.add(barSeriesStyle);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a(LineSeriesStyle lineSeriesStyle, boolean z) {
        if (z) {
            this.b.add(lineSeriesStyle);
        } else {
            this.a.add(lineSeriesStyle);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a(PieSeriesStyle pieSeriesStyle, boolean z) {
        if (z) {
            this.n.add(pieSeriesStyle);
        } else {
            this.m.add(pieSeriesStyle);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a(DonutSeriesStyle donutSeriesStyle, boolean z) {
        if (z) {
            this.p.add(donutSeriesStyle);
        } else {
            this.o.add(donutSeriesStyle);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a(BandSeriesStyle bandSeriesStyle, boolean z) {
        if (z) {
            this.l.add(bandSeriesStyle);
        } else {
            this.k.add(bandSeriesStyle);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a(CandlestickSeriesStyle candlestickSeriesStyle, boolean z) {
        if (z) {
            this.h.add(candlestickSeriesStyle);
        } else {
            this.g.add(candlestickSeriesStyle);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a(OHLCSeriesStyle oHLCSeriesStyle, boolean z) {
        if (z) {
            this.j.add(oHLCSeriesStyle);
        } else {
            this.i.add(oHLCSeriesStyle);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public BandSeriesStyle a(int i, boolean z) {
        return (BandSeriesStyle) a(z ? this.l : this.k, i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public CandlestickSeriesStyle b(int i, boolean z) {
        return (CandlestickSeriesStyle) a(z ? this.h : this.g, i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public OHLCSeriesStyle c(int i, boolean z) {
        return (OHLCSeriesStyle) a(z ? this.j : this.i, i);
    }

    public ColumnSeriesStyle d(int i, boolean z) {
        return (ColumnSeriesStyle) a(z ? this.d : this.c, i);
    }

    public BarSeriesStyle e(int i, boolean z) {
        return (BarSeriesStyle) a(z ? this.f : this.e, i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public LineSeriesStyle f(int i, boolean z) {
        return (LineSeriesStyle) a(z ? this.b : this.a, i);
    }

    private <T extends SeriesStyle> T a(List<T> list, int i) {
        return list.get(i % list.size());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public PieSeriesStyle g(int i, boolean z) {
        return (PieSeriesStyle) a(z ? this.n : this.m, i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public DonutSeriesStyle h(int i, boolean z) {
        return (DonutSeriesStyle) a(z ? this.p : this.o, i);
    }

    public ChartStyle a() {
        return this.q;
    }

    public void a(ChartStyle chartStyle) {
        this.q = chartStyle;
    }

    public MainTitleStyle b() {
        return this.r;
    }

    public void a(MainTitleStyle mainTitleStyle) {
        this.r = mainTitleStyle;
    }

    public CrosshairStyle c() {
        return this.s;
    }

    public void a(CrosshairStyle crosshairStyle) {
        this.s = crosshairStyle;
    }

    public LegendStyle d() {
        return this.t;
    }

    public void a(LegendStyle legendStyle) {
        this.t = legendStyle;
    }

    public AxisStyle e() {
        return this.u;
    }

    public void a(AxisStyle axisStyle) {
        this.u = axisStyle;
    }

    public AxisStyle f() {
        return this.v;
    }

    public void b(AxisStyle axisStyle) {
        this.v = axisStyle;
    }

    public AnnotationStyle g() {
        return this.w;
    }

    public void a(AnnotationStyle annotationStyle) {
        this.w = annotationStyle;
    }
}

package com.shinobicontrols.charts;

import com.shinobicontrols.charts.SeriesStyle;
/* loaded from: classes.dex */
public final class LineSeriesStyle extends SeriesStyle {
    final dj<Integer> a = new dj<>(0);
    final dj<Integer> b = new dj<>(0);
    final dj<Integer> c = new dj<>(0);
    final dj<Integer> d = new dj<>(0);
    final dj<Integer> e = new dj<>(-16777216);
    final dj<Integer> f = new dj<>(0);
    final dj<Float> g = new dj<>(Float.valueOf(1.0f));
    final dj<Integer> h = new dj<>(-16777216);
    final dj<Integer> i = new dj<>(-16777216);
    final dj<Float> j = new dj<>(Float.valueOf(1.0f));
    private PointStyle o = new PointStyle(this);
    private PointStyle p = new PointStyle(this);
    final dj<Boolean> k = new dj<>(true);
    final dj<SeriesStyle.FillStyle> l = new dj<>(SeriesStyle.FillStyle.NONE);
    dj<a> m = new dj<>(a.HORIZONTAL);

    /* loaded from: classes.dex */
    enum a {
        HORIZONTAL,
        VERTICAL
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.shinobicontrols.charts.SeriesStyle
    public void a(SeriesStyle seriesStyle) {
        synchronized (x.a) {
            super.a(seriesStyle);
            LineSeriesStyle lineSeriesStyle = (LineSeriesStyle) seriesStyle;
            this.a.b(Integer.valueOf(lineSeriesStyle.getAreaColor()));
            this.b.b(Integer.valueOf(lineSeriesStyle.getAreaColorBelowBaseline()));
            this.c.b(Integer.valueOf(lineSeriesStyle.getAreaColorGradientBelowBaseline()));
            this.d.b(Integer.valueOf(lineSeriesStyle.getAreaColorGradient()));
            this.e.b(Integer.valueOf(lineSeriesStyle.getAreaLineColor()));
            this.f.b(Integer.valueOf(lineSeriesStyle.getAreaLineColorBelowBaseline()));
            this.l.b(lineSeriesStyle.getFillStyle());
            this.h.b(Integer.valueOf(lineSeriesStyle.getLineColor()));
            this.i.b(Integer.valueOf(lineSeriesStyle.getLineColorBelowBaseline()));
            this.j.b(Float.valueOf(lineSeriesStyle.getLineWidth()));
            this.k.b(Boolean.valueOf(lineSeriesStyle.isLineShown()));
            this.o.a(lineSeriesStyle.getPointStyle());
            this.p.a(lineSeriesStyle.getSelectedPointStyle());
        }
    }

    public int getAreaColor() {
        return this.a.a.intValue();
    }

    public void setAreaColor(int areaColor) {
        synchronized (x.a) {
            this.a.a(Integer.valueOf(areaColor));
            d();
        }
    }

    public int getAreaColorBelowBaseline() {
        return this.b.a.intValue();
    }

    public void setAreaColorBelowBaseline(int areaColorBelowBaseline) {
        synchronized (x.a) {
            this.b.a(Integer.valueOf(areaColorBelowBaseline));
            d();
        }
    }

    public int getAreaColorGradient() {
        return this.d.a.intValue();
    }

    public void setAreaColorGradient(int areaColorGradient) {
        synchronized (x.a) {
            this.d.a(Integer.valueOf(areaColorGradient));
            d();
        }
    }

    public int getAreaColorGradientBelowBaseline() {
        return this.c.a.intValue();
    }

    public void setAreaColorGradientBelowBaseline(int areaColorGradientBelowBaseline) {
        synchronized (x.a) {
            this.c.a(Integer.valueOf(areaColorGradientBelowBaseline));
            d();
        }
    }

    public int getAreaLineColor() {
        return this.e.a.intValue();
    }

    public void setAreaLineColor(int areaLineColor) {
        synchronized (x.a) {
            this.e.a(Integer.valueOf(areaLineColor));
            d();
        }
    }

    public int getAreaLineColorBelowBaseline() {
        return this.f.a.intValue();
    }

    public void setAreaLineColorBelowBaseline(int areaLineColorBelowBaseline) {
        synchronized (x.a) {
            this.f.a(Integer.valueOf(areaLineColorBelowBaseline));
            d();
        }
    }

    public float getAreaLineWidth() {
        return this.g.a.floatValue();
    }

    public void setAreaLineWidth(float areaLineWidth) {
        synchronized (x.a) {
            this.g.a(Float.valueOf(areaLineWidth));
            d();
        }
    }

    public SeriesStyle.FillStyle getFillStyle() {
        return this.l.a;
    }

    public void setFillStyle(SeriesStyle.FillStyle fillStyle) {
        synchronized (x.a) {
            this.l.a(fillStyle);
            d();
        }
    }

    public int getLineColor() {
        return this.h.a.intValue();
    }

    public void setLineColor(int lineColor) {
        synchronized (x.a) {
            this.h.a(Integer.valueOf(lineColor));
            d();
        }
    }

    public int getLineColorBelowBaseline() {
        return this.i.a.intValue();
    }

    public void setLineColorBelowBaseline(int lineColorBelowBaseline) {
        synchronized (x.a) {
            this.i.a(Integer.valueOf(lineColorBelowBaseline));
            d();
        }
    }

    public float getLineWidth() {
        return this.j.a.floatValue();
    }

    public void setLineWidth(float lineWidth) {
        synchronized (x.a) {
            this.j.a(Float.valueOf(lineWidth));
            d();
        }
    }

    public PointStyle getPointStyle() {
        return this.o;
    }

    public void setPointStyle(PointStyle style) {
        if (style == null) {
            throw new IllegalArgumentException("Styles may not be null");
        }
        synchronized (x.a) {
            this.o.j = null;
            this.o = style;
            this.o.j = this;
            d();
        }
    }

    public PointStyle getSelectedPointStyle() {
        return this.p;
    }

    public void setSelectedPointStyle(PointStyle style) {
        if (style == null) {
            throw new IllegalArgumentException("Styles may not be null");
        }
        synchronized (x.a) {
            this.p.j = null;
            this.p = style;
            this.p.j = this;
            d();
        }
    }

    public boolean isLineShown() {
        return this.k.a.booleanValue();
    }

    public void setLineShown(boolean lineShown) {
        synchronized (x.a) {
            this.k.a(Boolean.valueOf(lineShown));
            d();
        }
    }
}

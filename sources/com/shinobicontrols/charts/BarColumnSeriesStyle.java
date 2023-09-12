package com.shinobicontrols.charts;

import com.shinobicontrols.charts.SeriesStyle;
/* loaded from: classes.dex */
abstract class BarColumnSeriesStyle extends SeriesStyle {
    final dj<Integer> a = new dj<>(-16777216);
    final dj<Integer> b = new dj<>(-16777216);
    final dj<Integer> c = new dj<>(-16777216);
    final dj<Integer> d = new dj<>(-16777216);
    final dj<Integer> e = new dj<>(-16777216);
    final dj<Integer> f = new dj<>(-16777216);
    final dj<Float> g = new dj<>(Float.valueOf(1.0f));
    final dj<Boolean> h = new dj<>(false);
    final dj<SeriesStyle.FillStyle> i = new dj<>(SeriesStyle.FillStyle.GRADIENT);

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.shinobicontrols.charts.SeriesStyle
    public void a(SeriesStyle seriesStyle) {
        synchronized (x.a) {
            super.a(seriesStyle);
            BarColumnSeriesStyle barColumnSeriesStyle = (BarColumnSeriesStyle) seriesStyle;
            this.a.b(Integer.valueOf(barColumnSeriesStyle.getAreaColor()));
            this.b.b(Integer.valueOf(barColumnSeriesStyle.getAreaColorBelowBaseline()));
            this.c.b(Integer.valueOf(barColumnSeriesStyle.getAreaColorGradientBelowBaseline()));
            this.d.b(Integer.valueOf(barColumnSeriesStyle.getAreaColorGradient()));
            this.e.b(Integer.valueOf(barColumnSeriesStyle.getLineColor()));
            this.f.b(Integer.valueOf(barColumnSeriesStyle.getLineColorBelowBaseline()));
            this.g.b(Float.valueOf(barColumnSeriesStyle.getLineWidth()));
            this.i.b(barColumnSeriesStyle.getFillStyle());
            this.h.b(Boolean.valueOf(barColumnSeriesStyle.isLineShown()));
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

    public int getLineColor() {
        return this.e.a.intValue();
    }

    public void setLineColor(int lineColor) {
        synchronized (x.a) {
            this.e.a(Integer.valueOf(lineColor));
            d();
        }
    }

    public int getLineColorBelowBaseline() {
        return this.f.a.intValue();
    }

    public void setLineColorBelowBaseline(int lineColorBelowBaseline) {
        synchronized (x.a) {
            this.f.a(Integer.valueOf(lineColorBelowBaseline));
            d();
        }
    }

    public float getLineWidth() {
        return this.g.a.floatValue();
    }

    public void setLineWidth(float lineWidth) {
        synchronized (x.a) {
            this.g.a(Float.valueOf(lineWidth));
            d();
        }
    }

    public boolean isLineShown() {
        return this.h.a.booleanValue();
    }

    public void setLineShown(boolean showLine) {
        synchronized (x.a) {
            this.h.a(Boolean.valueOf(showLine));
            d();
        }
    }

    public SeriesStyle.FillStyle getFillStyle() {
        return this.i.a;
    }

    public void setFillStyle(SeriesStyle.FillStyle fillStyle) {
        synchronized (x.a) {
            this.i.a(fillStyle);
            d();
        }
    }
}

package com.shinobicontrols.charts;
/* loaded from: classes.dex */
public class CandlestickSeriesStyle extends SeriesStyle {
    final dj<Integer> a = new dj<>(-16777216);
    final dj<Integer> b = new dj<>(0);
    final dj<Integer> c = new dj<>(-16777216);
    final dj<Integer> d = new dj<>(0);
    final dj<Integer> e = new dj<>(-16777216);
    final dj<Integer> f = new dj<>(-16777216);
    final dj<Float> g = new dj<>(Float.valueOf(2.0f));
    final dj<Float> h = new dj<>(Float.valueOf(2.0f));

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.shinobicontrols.charts.SeriesStyle
    public void a(SeriesStyle seriesStyle) {
        synchronized (x.a) {
            super.a(seriesStyle);
            CandlestickSeriesStyle candlestickSeriesStyle = (CandlestickSeriesStyle) seriesStyle;
            this.c.b(Integer.valueOf(candlestickSeriesStyle.getFallingColor()));
            this.d.b(Integer.valueOf(candlestickSeriesStyle.getFallingColorGradient()));
            this.a.b(Integer.valueOf(candlestickSeriesStyle.getRisingColor()));
            this.b.b(Integer.valueOf(candlestickSeriesStyle.getRisingColorGradient()));
            this.e.b(Integer.valueOf(candlestickSeriesStyle.getOutlineColor()));
            this.h.b(Float.valueOf(candlestickSeriesStyle.getOutlineWidth()));
            this.f.b(Integer.valueOf(candlestickSeriesStyle.getStickColor()));
            this.g.b(Float.valueOf(candlestickSeriesStyle.getStickWidth()));
        }
    }

    public int getRisingColor() {
        return this.a.a.intValue();
    }

    public void setRisingColor(int color) {
        synchronized (x.a) {
            this.a.a(Integer.valueOf(color));
            d();
        }
    }

    public int getRisingColorGradient() {
        return this.b.a.intValue();
    }

    public void setRisingColorGradient(int color) {
        synchronized (x.a) {
            this.b.a(Integer.valueOf(color));
            d();
        }
    }

    public int getFallingColor() {
        return this.c.a.intValue();
    }

    public void setFallingColor(int color) {
        synchronized (x.a) {
            this.c.a(Integer.valueOf(color));
            d();
        }
    }

    public int getFallingColorGradient() {
        return this.d.a.intValue();
    }

    public void setFallingColorGradient(int color) {
        synchronized (x.a) {
            this.d.a(Integer.valueOf(color));
            d();
        }
    }

    public int getOutlineColor() {
        return this.e.a.intValue();
    }

    public void setOutlineColor(int color) {
        synchronized (x.a) {
            this.e.a(Integer.valueOf(color));
            d();
        }
    }

    public int getStickColor() {
        return this.f.a.intValue();
    }

    public void setStickColor(int color) {
        synchronized (x.a) {
            this.f.a(Integer.valueOf(color));
            d();
        }
    }

    public float getStickWidth() {
        return this.g.a.floatValue();
    }

    public void setStickWidth(float width) {
        synchronized (x.a) {
            this.g.a(Float.valueOf(width));
            d();
        }
    }

    public float getOutlineWidth() {
        return this.h.a.floatValue();
    }

    public void setOutlineWidth(float width) {
        synchronized (x.a) {
            this.h.a(Float.valueOf(width));
            d();
        }
    }
}

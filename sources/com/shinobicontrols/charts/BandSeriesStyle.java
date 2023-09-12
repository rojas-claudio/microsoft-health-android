package com.shinobicontrols.charts;
/* loaded from: classes.dex */
public class BandSeriesStyle extends SeriesStyle {
    final dj<Boolean> a = new dj<>(true);
    final dj<Integer> b = new dj<>(-16777216);
    final dj<Integer> c = new dj<>(-16777216);
    final dj<Float> d = new dj<>(Float.valueOf(2.0f));
    final dj<Integer> e = new dj<>(-16776961);
    final dj<Integer> f = new dj<>(0);
    PointStyle g = new PointStyle(this);
    PointStyle h = new PointStyle(this);

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.shinobicontrols.charts.SeriesStyle
    public void a(SeriesStyle seriesStyle) {
        synchronized (x.a) {
            super.a(seriesStyle);
            BandSeriesStyle bandSeriesStyle = (BandSeriesStyle) seriesStyle;
            this.f.b(Integer.valueOf(bandSeriesStyle.getAreaColorInverted()));
            this.e.b(Integer.valueOf(bandSeriesStyle.getAreaColorNormal()));
            this.b.b(Integer.valueOf(bandSeriesStyle.getLineColorHigh()));
            this.c.b(Integer.valueOf(bandSeriesStyle.getLineColorLow()));
            this.d.b(Float.valueOf(bandSeriesStyle.getLineWidth()));
            this.a.b(Boolean.valueOf(bandSeriesStyle.isFillShown()));
            this.g.a(bandSeriesStyle.getPointStyle());
            this.h.a(bandSeriesStyle.getSelectedPointStyle());
        }
    }

    public boolean isFillShown() {
        return this.a.a.booleanValue();
    }

    public void setFillShown(boolean shown) {
        synchronized (x.a) {
            this.a.a(Boolean.valueOf(shown));
            d();
        }
    }

    public int getLineColorHigh() {
        return this.b.a.intValue();
    }

    public void setLineColorHigh(int color) {
        synchronized (x.a) {
            this.b.a(Integer.valueOf(color));
            d();
        }
    }

    public int getLineColorLow() {
        return this.c.a.intValue();
    }

    public void setLineColorLow(int color) {
        synchronized (x.a) {
            this.c.a(Integer.valueOf(color));
            d();
        }
    }

    public float getLineWidth() {
        return this.d.a.floatValue();
    }

    public void setLineWidth(float width) {
        synchronized (x.a) {
            this.d.a(Float.valueOf(width));
            d();
        }
    }

    public int getAreaColorNormal() {
        return this.e.a.intValue();
    }

    public void setAreaColorNormal(int color) {
        synchronized (x.a) {
            this.e.a(Integer.valueOf(color));
            d();
        }
    }

    public int getAreaColorInverted() {
        return this.f.a.intValue();
    }

    public void setAreaColorInverted(int color) {
        synchronized (x.a) {
            this.f.a(Integer.valueOf(color));
            d();
        }
    }

    public PointStyle getPointStyle() {
        return this.g;
    }

    public void setPointStyle(PointStyle style) {
        if (style == null) {
            throw new IllegalArgumentException("Styles may not be null");
        }
        synchronized (x.a) {
            this.g.j = null;
            this.g = style;
            this.g.j = this;
            d();
        }
    }

    public PointStyle getSelectedPointStyle() {
        return this.h;
    }

    public void setSelectedPointStyle(PointStyle style) {
        if (style == null) {
            throw new IllegalArgumentException("Styles may not be null");
        }
        synchronized (x.a) {
            this.h.j = null;
            this.h = style;
            this.h.j = this;
            d();
        }
    }
}

package com.shinobicontrols.charts;
/* loaded from: classes.dex */
public class OHLCSeriesStyle extends SeriesStyle {
    final dj<Integer> a = new dj<>(-16777216);
    final dj<Integer> b = new dj<>(0);
    final dj<Integer> c = new dj<>(-16777216);
    final dj<Integer> d = new dj<>(0);
    final dj<Float> e = new dj<>(Float.valueOf(2.0f));
    final dj<Float> f = new dj<>(Float.valueOf(2.0f));

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.shinobicontrols.charts.SeriesStyle
    public void a(SeriesStyle seriesStyle) {
        synchronized (x.a) {
            super.a(seriesStyle);
            OHLCSeriesStyle oHLCSeriesStyle = (OHLCSeriesStyle) seriesStyle;
            this.f.b(Float.valueOf(oHLCSeriesStyle.getArmWidth()));
            this.c.b(Integer.valueOf(oHLCSeriesStyle.getFallingColor()));
            this.d.b(Integer.valueOf(oHLCSeriesStyle.getFallingColorGradient()));
            this.a.b(Integer.valueOf(oHLCSeriesStyle.getRisingColor()));
            this.b.b(Integer.valueOf(oHLCSeriesStyle.getRisingColorGradient()));
            this.e.b(Float.valueOf(oHLCSeriesStyle.getTrunkWidth()));
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

    public float getTrunkWidth() {
        return this.e.a.floatValue();
    }

    public void setTrunkWidth(float width) {
        synchronized (x.a) {
            this.e.a(Float.valueOf(width));
            d();
        }
    }

    public float getArmWidth() {
        return this.f.a.floatValue();
    }

    public void setArmWidth(float width) {
        synchronized (x.a) {
            this.f.a(Float.valueOf(width));
            d();
        }
    }
}

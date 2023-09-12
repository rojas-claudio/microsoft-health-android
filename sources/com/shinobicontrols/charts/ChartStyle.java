package com.shinobicontrols.charts;
/* loaded from: classes.dex */
public final class ChartStyle {
    final dj<Integer> a = new dj<>(-1);
    final dj<Integer> b = new dj<>(-1);
    final dj<Integer> c = new dj<>(-1);
    final dj<Float> d = new dj<>(Float.valueOf(0.0f));
    final dj<Integer> e = new dj<>(-1);
    final dj<Integer> f = new dj<>(-1);
    final dj<Integer> g = new dj<>(-1);
    final dj<Float> h = new dj<>(Float.valueOf(0.0f));

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a(ChartStyle chartStyle) {
        if (chartStyle != null) {
            this.a.b(Integer.valueOf(chartStyle.getBackgroundColor()));
            this.b.b(Integer.valueOf(chartStyle.a()));
            this.c.b(Integer.valueOf(chartStyle.b()));
            this.d.b(Float.valueOf(chartStyle.c()));
            this.e.b(Integer.valueOf(chartStyle.getCanvasBackgroundColor()));
            this.f.b(Integer.valueOf(chartStyle.getPlotAreaBackgroundColor()));
            this.g.b(Integer.valueOf(chartStyle.d()));
            this.h.b(Float.valueOf(chartStyle.e()));
        }
    }

    public int getBackgroundColor() {
        return this.a.a.intValue();
    }

    public void setBackgroundColor(int backgroundColor) {
        this.a.a(Integer.valueOf(backgroundColor));
    }

    int a() {
        return this.b.a.intValue();
    }

    int b() {
        return this.c.a.intValue();
    }

    float c() {
        return this.d.a.floatValue();
    }

    public int getCanvasBackgroundColor() {
        return this.e.a.intValue();
    }

    public void setCanvasBackgroundColor(int canvasBackgroundColor) {
        this.e.a(Integer.valueOf(canvasBackgroundColor));
    }

    public int getPlotAreaBackgroundColor() {
        return this.f.a.intValue();
    }

    public void setPlotAreaBackgroundColor(int plotAreaBackgroundColor) {
        this.f.a(Integer.valueOf(plotAreaBackgroundColor));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int d() {
        return this.g.a.intValue();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public float e() {
        return this.h.a.floatValue();
    }
}

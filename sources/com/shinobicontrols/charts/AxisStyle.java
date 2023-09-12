package com.shinobicontrols.charts;

import com.shinobicontrols.charts.PropertyChangedEvent;
/* loaded from: classes.dex */
public final class AxisStyle {
    final dj<Float> a = new dj<>(Float.valueOf(0.0f));
    final dj<Float> b = new dj<>(Float.valueOf(0.0f));
    final dj<Integer> c = new dj<>(-16777216);
    final dj<Float> d = new dj<>(Float.valueOf(1.0f));
    GridStripeStyle e = new GridStripeStyle();
    GridlineStyle f = new GridlineStyle();
    TickStyle g = new TickStyle();
    AxisTitleStyle h = new AxisTitleStyle();
    private final al i = new al();

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a(AxisStyle axisStyle) {
        if (axisStyle != null) {
            this.a.b(Float.valueOf(axisStyle.getInterSeriesPadding()));
            this.b.b(Float.valueOf(axisStyle.getInterSeriesSetPadding()));
            this.c.b(Integer.valueOf(axisStyle.getLineColor()));
            this.d.b(Float.valueOf(axisStyle.getLineWidth()));
            this.e.a(axisStyle.e);
            this.f.a(axisStyle.f);
            this.g.a(axisStyle.g);
            this.h.a(axisStyle.h);
        }
    }

    public GridStripeStyle getGridStripeStyle() {
        return this.e;
    }

    public void setGridStripeStyle(GridStripeStyle gridStripeStyle) {
        this.e = gridStripeStyle;
    }

    public float getInterSeriesPadding() {
        return this.a.a.floatValue();
    }

    public void setInterSeriesPadding(float interSeriesPadding) {
        interSeriesPadding = (interSeriesPadding < 0.0f || interSeriesPadding >= 1.0f) ? 0.0f : 0.0f;
        synchronized (x.a) {
            this.a.a(Float.valueOf(interSeriesPadding));
            a();
        }
    }

    public float getInterSeriesSetPadding() {
        return this.b.a.floatValue();
    }

    public void setInterSeriesSetPadding(float interSeriesSetPadding) {
        interSeriesSetPadding = (interSeriesSetPadding < 0.0f || interSeriesSetPadding >= 1.0f) ? 0.0f : 0.0f;
        synchronized (x.a) {
            this.b.a(Float.valueOf(interSeriesSetPadding));
            a();
        }
    }

    public int getLineColor() {
        return this.c.a.intValue();
    }

    public void setLineColor(int lineColor) {
        this.c.a(Integer.valueOf(lineColor));
    }

    public float getLineWidth() {
        return this.d.a.floatValue();
    }

    public void setLineWidth(float lineWidth) {
        this.d.a(Float.valueOf(lineWidth));
    }

    public GridlineStyle getGridlineStyle() {
        return this.f;
    }

    public void setGridlineStyle(GridlineStyle gridlineStyle) {
        this.f = gridlineStyle;
    }

    public TickStyle getTickStyle() {
        return this.g;
    }

    public void setTickStyle(TickStyle tickStyle) {
        this.g = tickStyle;
    }

    public AxisTitleStyle getTitleStyle() {
        return this.h;
    }

    public void setTitleStyle(AxisTitleStyle titleStyle) {
        this.h = titleStyle;
    }

    private final void a() {
        this.i.a(new PropertyChangedEvent());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public am a(PropertyChangedEvent.Handler handler) {
        return this.i.a(PropertyChangedEvent.a, handler);
    }
}

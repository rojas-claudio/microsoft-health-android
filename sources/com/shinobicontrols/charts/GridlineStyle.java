package com.shinobicontrols.charts;
/* loaded from: classes.dex */
public class GridlineStyle {
    final dj<Boolean> a = new dj<>(false);
    final dj<Boolean> b = new dj<>(false);
    final dj<Integer> c = new dj<>(-12303292);
    private final dj<Float> e = new dj<>(Float.valueOf(1.0f));
    final dj<float[]> d = new dj<>(new float[]{1.0f, 1.0f});

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a(GridlineStyle gridlineStyle) {
        if (gridlineStyle != null) {
            this.a.b(Boolean.valueOf(gridlineStyle.areGridlinesShown()));
            this.b.b(Boolean.valueOf(gridlineStyle.areGridlinesDashed()));
            this.c.b(Integer.valueOf(gridlineStyle.getLineColor()));
            this.e.b(Float.valueOf(gridlineStyle.getLineWidth()));
            this.d.b(gridlineStyle.getDashStyle());
        }
    }

    public boolean areGridlinesShown() {
        return this.a.a.booleanValue();
    }

    public void setGridlinesShown(boolean showGridlines) {
        this.a.a(Boolean.valueOf(showGridlines));
    }

    public boolean areGridlinesDashed() {
        return this.b.a.booleanValue();
    }

    public void setGridlinesDashed(boolean dashedGridlines) {
        this.b.a(Boolean.valueOf(dashedGridlines));
    }

    public int getLineColor() {
        return this.c.a.intValue();
    }

    public void setLineColor(int lineColor) {
        this.c.a(Integer.valueOf(lineColor));
    }

    public float getLineWidth() {
        return this.e.a.floatValue();
    }

    public void setLineWidth(float lineWidth) {
        this.e.a(Float.valueOf(lineWidth));
    }

    public float[] getDashStyle() {
        return this.d.a;
    }

    public void setDashStyle(float[] dashStyle) {
        this.d.a(dashStyle);
    }
}

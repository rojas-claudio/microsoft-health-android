package com.shinobicontrols.charts;

import android.graphics.Typeface;
import com.shinobicontrols.charts.TickMark;
/* loaded from: classes.dex */
public final class TickStyle {
    final dj<Integer> a = new dj<>(-16777216);
    final dj<Typeface> b = new dj<>(Typeface.DEFAULT);
    final dj<Float> c = new dj<>(Float.valueOf(10.0f));
    final dj<Integer> d = new dj<>(-1);
    final dj<Integer> e = new dj<>(-16777216);
    final dj<Float> f = new dj<>(Float.valueOf(1.0f));
    final dj<Float> g = new dj<>(Float.valueOf(1.0f));
    final dj<Boolean> h = new dj<>(true);
    final dj<Boolean> i = new dj<>(true);
    final dj<Boolean> j = new dj<>(false);
    final dj<Float> k = new dj<>(Float.valueOf(1.0f));
    final dj<TickMark.Orientation> l = new dj<>(TickMark.Orientation.HORIZONTAL);

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a(TickStyle tickStyle) {
        if (tickStyle != null) {
            this.a.b(tickStyle.a.a);
            this.b.b(tickStyle.b.a);
            this.c.b(tickStyle.c.a);
            this.d.b(tickStyle.d.a);
            this.e.b(tickStyle.e.a);
            this.f.b(tickStyle.f.a);
            this.g.b(tickStyle.g.a);
            this.h.b(tickStyle.h.a);
            this.i.b(tickStyle.i.a);
            this.j.b(tickStyle.j.a);
            this.k.b(tickStyle.k.a);
            this.l.b(tickStyle.l.a);
        }
    }

    public int getLabelColor() {
        return this.a.a.intValue();
    }

    public Typeface getLabelTypeface() {
        return this.b.a;
    }

    public float getLabelTextSize() {
        return this.c.a.floatValue();
    }

    public int getLabelTextShadowColor() {
        return this.d.a.intValue();
    }

    public int getLineColor() {
        return this.e.a.intValue();
    }

    public float getLineLength() {
        return this.f.a.floatValue();
    }

    public float getLineWidth() {
        return this.g.a.floatValue();
    }

    public float getTickGap() {
        return this.k.a.floatValue();
    }

    public TickMark.Orientation getLabelOrientation() {
        return this.l.a;
    }

    public boolean areLabelsShown() {
        return this.h.a.booleanValue();
    }

    public boolean areMajorTicksShown() {
        return this.i.a.booleanValue();
    }

    public boolean areMinorTicksShown() {
        return this.j.a.booleanValue();
    }

    public void setLabelColor(int labelColor) {
        this.a.a(Integer.valueOf(labelColor));
    }

    public void setLabelTypeface(Typeface labelTypeface) {
        this.b.a(labelTypeface);
    }

    public void setLabelTextSize(float labelTextSize) {
        this.c.a(Float.valueOf(labelTextSize));
    }

    public void setLabelTextShadowColor(int labelTextShadowColor) {
        this.d.a(Integer.valueOf(labelTextShadowColor));
    }

    public void setLineColor(int lineColor) {
        this.e.a(Integer.valueOf(lineColor));
    }

    public void setLineLength(float lineLength) {
        this.f.a(Float.valueOf(lineLength));
    }

    public void setLineWidth(float lineWidth) {
        this.g.a(Float.valueOf(lineWidth));
    }

    public void setLabelsShown(boolean showLabels) {
        this.h.a(Boolean.valueOf(showLabels));
    }

    public void setMajorTicksShown(boolean showTicks) {
        this.i.a(Boolean.valueOf(showTicks));
    }

    public void setMinorTicksShown(boolean showTicks) {
        this.j.a(Boolean.valueOf(showTicks));
    }

    public void setTickGap(float tickGap) {
        this.k.a(Float.valueOf(tickGap));
    }

    public void setLabelOrientation(TickMark.Orientation tickLabelOrientation) {
        this.l.a(tickLabelOrientation);
    }
}

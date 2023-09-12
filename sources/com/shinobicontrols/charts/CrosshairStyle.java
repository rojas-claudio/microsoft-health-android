package com.shinobicontrols.charts;

import android.graphics.Typeface;
/* loaded from: classes.dex */
public class CrosshairStyle {
    final dj<Float> a = new dj<>(Float.valueOf(0.0f));
    final dj<Integer> b = new dj<>(-16777216);
    final dj<Float> c = new dj<>(Float.valueOf(0.0f));
    final dj<Typeface> d = new dj<>(null);
    final dj<Float> e = new dj<>(Float.valueOf(12.0f));
    final dj<Integer> f = new dj<>(-16777216);
    final dj<Integer> g = new dj<>(0);
    final dj<Integer> h = new dj<>(0);
    final dj<Float> i = new dj<>(Float.valueOf(0.0f));
    final dj<Float> j = new dj<>(Float.valueOf(0.0f));
    final dj<Integer> k = new dj<>(0);

    public float getLineWidth() {
        return this.a.a.floatValue();
    }

    public void setLineWidth(float lineWidth) {
        this.a.a(Float.valueOf(lineWidth));
    }

    public int getLineColor() {
        return this.b.a.intValue();
    }

    public void setLineColor(int lineColor) {
        this.b.a(Integer.valueOf(lineColor));
    }

    public float getTooltipPadding() {
        return this.c.a.floatValue();
    }

    public void setTooltipPadding(float tooltipPadding) {
        this.c.a(Float.valueOf(tooltipPadding));
    }

    public Typeface getTooltipTypeface() {
        return this.d.a;
    }

    public void setTooltipTypeface(Typeface tooltipTypeface) {
        this.d.a(tooltipTypeface);
    }

    public float getTooltipTextSize() {
        return this.e.a.floatValue();
    }

    public void setTooltipTextSize(float tooltipTextSize) {
        this.e.a(Float.valueOf(tooltipTextSize));
    }

    public int getTooltipTextColor() {
        return this.f.a.intValue();
    }

    public void setTooltipTextColor(int tooltipTextColor) {
        this.f.a(Integer.valueOf(tooltipTextColor));
    }

    public int getTooltipLabelBackgroundColor() {
        return this.g.a.intValue();
    }

    public void setTooltipLabelBackgroundColor(int tooltipLabelBackgroundColor) {
        this.g.a(Integer.valueOf(tooltipLabelBackgroundColor));
    }

    public int getTooltipBackgroundColor() {
        return this.h.a.intValue();
    }

    public void setTooltipBackgroundColor(int tooltipBackgroundColor) {
        this.h.a(Integer.valueOf(tooltipBackgroundColor));
    }

    public float getTooltipCornerRadius() {
        return this.i.a.floatValue();
    }

    public void setTooltipCornerRadius(float tooltipCornerRadius) {
        this.i.a(Float.valueOf(tooltipCornerRadius));
    }

    public float getTooltipBorderWidth() {
        return this.j.a.floatValue();
    }

    public void setTooltipBorderWidth(float tooltipBorderWidth) {
        this.j.a(Float.valueOf(tooltipBorderWidth));
    }

    public int getTooltipBorderColor() {
        return this.k.a.intValue();
    }

    public void setTooltipBorderColor(int tooltipBorderColor) {
        this.k.a(Integer.valueOf(tooltipBorderColor));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a(CrosshairStyle crosshairStyle) {
        if (crosshairStyle != null) {
            this.a.b(crosshairStyle.a.a);
            this.b.b(crosshairStyle.b.a);
            this.c.b(crosshairStyle.c.a);
            this.d.b(crosshairStyle.d.a);
            this.e.b(crosshairStyle.e.a);
            this.f.b(crosshairStyle.f.a);
            this.g.b(crosshairStyle.g.a);
            this.h.b(crosshairStyle.h.a);
            this.i.b(crosshairStyle.i.a);
            this.j.b(crosshairStyle.j.a);
            this.k.b(crosshairStyle.k.a);
        }
    }
}

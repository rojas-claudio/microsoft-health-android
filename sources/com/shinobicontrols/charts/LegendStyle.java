package com.shinobicontrols.charts;

import android.graphics.Typeface;
import com.shinobicontrols.charts.Legend;
/* loaded from: classes.dex */
public final class LegendStyle {
    final dj<Integer> a = new dj<>(0);
    final dj<Integer> b = new dj<>(0);
    final dj<Float> c = new dj<>(Float.valueOf(0.0f));
    final dj<Float> d = new dj<>(Float.valueOf(0.0f));
    final dj<Typeface> e = new dj<>(null);
    final dj<Integer> f = new dj<>(-16777216);
    final dj<Float> g = new dj<>(Float.valueOf(10.0f));
    final dj<Float> h = new dj<>(Float.valueOf(4.0f));
    final dj<Float> i = new dj<>(Float.valueOf(4.0f));
    final dj<Boolean> j = new dj<>(true);
    final dj<Legend.SymbolAlignment> k = new dj<>(Legend.SymbolAlignment.LEFT);
    final dj<Float> l = new dj<>(Float.valueOf(0.0f));
    final dj<Float> m = new dj<>(Float.valueOf(32.0f));
    final dj<Integer> n = new dj<>(0);
    final dj<Typeface> o = new dj<>(null);
    final dj<Integer> p = new dj<>(-16777216);
    final dj<Float> q = new dj<>(Float.valueOf(12.0f));
    final dj<Float> r = new dj<>(Float.valueOf(4.0f));
    final bf s = new bf();

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a(LegendStyle legendStyle) {
        if (legendStyle != null) {
            this.a.b(Integer.valueOf(legendStyle.getBackgroundColor()));
            this.b.b(Integer.valueOf(legendStyle.getBorderColor()));
            this.c.b(Float.valueOf(legendStyle.getBorderWidth()));
            this.d.b(Float.valueOf(legendStyle.getCornerRadius()));
            this.e.b(legendStyle.getTypeface());
            this.f.b(Integer.valueOf(legendStyle.getTextColor()));
            this.g.b(Float.valueOf(legendStyle.getTextSize()));
            this.h.b(Float.valueOf(legendStyle.getSymbolLabelGap()));
            this.i.b(Float.valueOf(legendStyle.getPadding()));
            this.j.b(Boolean.valueOf(legendStyle.areSymbolsShown()));
            this.k.b(legendStyle.getSymbolAlignment());
            this.l.b(Float.valueOf(legendStyle.getSymbolCornerRadius()));
            this.m.b(Float.valueOf(legendStyle.getSymbolWidth()));
            this.n.b(Integer.valueOf(legendStyle.getTextAlignment()));
            this.o.b(legendStyle.getTitleTypeface());
            this.p.b(Integer.valueOf(legendStyle.getTitleTextColor()));
            this.q.b(Float.valueOf(legendStyle.getTitleTextSize()));
            this.r.b(Float.valueOf(legendStyle.getRowVerticalMargin()));
        }
    }

    public float getTitlePadding() {
        return this.s.getPadding();
    }

    public void setTitlePadding(float padding) {
        this.s.setPadding(padding);
    }

    public float getTitleMargin() {
        return this.s.getMargin();
    }

    public void setTitleMargin(float margin) {
        this.s.setMargin(margin);
    }

    public int getBackgroundColor() {
        return this.a.a.intValue();
    }

    public int getBorderColor() {
        return this.b.a.intValue();
    }

    public float getBorderWidth() {
        return this.c.a.floatValue();
    }

    public float getCornerRadius() {
        return this.d.a.floatValue();
    }

    public Typeface getTypeface() {
        return this.e.a;
    }

    public int getTextColor() {
        return this.f.a.intValue();
    }

    public float getTextSize() {
        return this.g.a.floatValue();
    }

    public float getSymbolLabelGap() {
        return this.h.a.floatValue();
    }

    public float getPadding() {
        return this.i.a.floatValue();
    }

    public Legend.SymbolAlignment getSymbolAlignment() {
        return this.k.a;
    }

    public float getSymbolCornerRadius() {
        return this.l.a.floatValue();
    }

    public float getSymbolWidth() {
        return this.m.a.floatValue();
    }

    public int getTextAlignment() {
        return this.n.a.intValue();
    }

    public Typeface getTitleTypeface() {
        return this.s.getTypeface();
    }

    public int getTitleTextColor() {
        return this.s.getTextColor();
    }

    public float getTitleTextSize() {
        return this.s.getTextSize();
    }

    public float getRowVerticalMargin() {
        return this.r.a.floatValue();
    }

    public boolean areSymbolsShown() {
        return this.j.a.booleanValue();
    }

    public void setBackgroundColor(int backgroundColor) {
        this.a.a(Integer.valueOf(backgroundColor));
    }

    public void setBorderColor(int borderColor) {
        this.b.a(Integer.valueOf(borderColor));
    }

    public void setBorderWidth(float borderWidth) {
        this.c.a(Float.valueOf(borderWidth));
    }

    public void setCornerRadius(float cornerRadius) {
        this.d.a(Float.valueOf(cornerRadius));
    }

    public void setTypeface(Typeface typeface) {
        this.e.a(typeface);
    }

    public void setTextColor(int textColor) {
        this.f.a(Integer.valueOf(textColor));
    }

    public void setTextSize(float textSize) {
        this.g.a(Float.valueOf(textSize));
    }

    public void setSymbolLabelGap(float symbolLabelGap) {
        this.h.a(Float.valueOf(symbolLabelGap));
    }

    public void setPadding(float padding) {
        this.i.a(Float.valueOf(padding));
    }

    public void setSymbolsShown(boolean showSymbols) {
        this.j.a(Boolean.valueOf(showSymbols));
    }

    public void setSymbolAlignment(Legend.SymbolAlignment symbolAlignment) {
        this.k.a(symbolAlignment);
    }

    public void setSymbolCornerRadius(float symbolCornerRadius) {
        this.l.a(Float.valueOf(symbolCornerRadius));
    }

    public void setSymbolWidth(float symbolWidth) {
        this.m.a(Float.valueOf(symbolWidth));
    }

    public void setTextAlignment(int textAlignment) {
        this.n.a(Integer.valueOf(textAlignment));
    }

    public void setTitleTypeface(Typeface titleTypeface) {
        this.s.d.a(titleTypeface);
    }

    public void setTitleTextColor(int titleTextColor) {
        this.s.h.a(Integer.valueOf(titleTextColor));
    }

    public void setTitleTextSize(float titleTextSize) {
        this.s.e.a(Float.valueOf(titleTextSize));
    }

    public void setRowVerticalMargin(float rowVerticalMargin) {
        this.r.a(Float.valueOf(rowVerticalMargin));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public bf a() {
        return this.s;
    }
}

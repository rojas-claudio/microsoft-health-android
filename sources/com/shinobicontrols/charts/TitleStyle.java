package com.shinobicontrols.charts;

import android.graphics.Typeface;
import com.shinobicontrols.charts.Title;
/* loaded from: classes.dex */
public abstract class TitleStyle {
    final dj<Integer> c = new dj<>(-1);
    final dj<Typeface> d = new dj<>(null);
    final dj<Float> e = new dj<>(Float.valueOf(12.0f));
    final dj<Float> f = new dj<>(Float.valueOf(12.0f));
    final dj<Title.Position> g = new dj<>(Title.Position.CENTER);
    final dj<Integer> h = new dj<>(-16777216);
    final dj<Float> i = new dj<>(Float.valueOf(6.0f));
    final dj<Float> j = new dj<>(Float.valueOf(10.0f));

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a(TitleStyle titleStyle) {
        if (titleStyle != null) {
            this.c.b(Integer.valueOf(titleStyle.getBackgroundColor()));
            this.d.b(titleStyle.getTypeface());
            this.e.b(Float.valueOf(titleStyle.getTextSize()));
            this.f.b(Float.valueOf(titleStyle.a()));
            this.g.b(titleStyle.getPosition());
            this.h.b(Integer.valueOf(titleStyle.getTextColor()));
        }
    }

    public float getPadding() {
        return this.i.a.floatValue();
    }

    public void setPadding(float padding) {
        this.i.a(Float.valueOf(padding));
    }

    public float getMargin() {
        return this.j.a.floatValue();
    }

    /* JADX WARN: Type inference failed for: r1v0, types: [java.lang.Float, T] */
    public void setMargin(float margin) {
        this.j.a = Float.valueOf(margin);
    }

    public int getBackgroundColor() {
        return this.c.a.intValue();
    }

    public void setBackgroundColor(int backgroundColor) {
        this.c.a(Integer.valueOf(backgroundColor));
    }

    public Typeface getTypeface() {
        return this.d.a;
    }

    public void setTypeface(Typeface typeface) {
        this.d.a(typeface);
    }

    public float getTextSize() {
        return this.e.a.floatValue();
    }

    public void setTextSize(float textSize) {
        this.e.a(Float.valueOf(textSize));
    }

    float a() {
        return this.f.a.floatValue();
    }

    public Title.Position getPosition() {
        return this.g.a;
    }

    public void setPosition(Title.Position position) {
        this.g.a(position);
    }

    public int getTextColor() {
        return this.h.a.intValue();
    }

    public void setTextColor(int textColor) {
        this.h.a(Integer.valueOf(textColor));
    }
}

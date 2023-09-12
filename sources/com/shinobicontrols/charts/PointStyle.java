package com.shinobicontrols.charts;

import android.graphics.drawable.Drawable;
/* loaded from: classes.dex */
public final class PointStyle {
    final dj<Integer> a;
    final dj<Integer> b;
    final dj<Float> c;
    final dj<Integer> d;
    final dj<Integer> e;
    final dj<Float> f;
    final dj<Float> g;
    final dj<Boolean> h;
    final dj<Drawable> i;
    SeriesStyle j;

    public PointStyle() {
        this.a = new dj<>(-16777216);
        this.b = new dj<>(-16777216);
        this.c = new dj<>(Float.valueOf(0.0f));
        this.d = new dj<>(-16777216);
        this.e = new dj<>(-16777216);
        this.f = new dj<>(Float.valueOf(3.0f));
        this.g = new dj<>(Float.valueOf(5.0f));
        this.h = new dj<>(false);
        this.i = new dj<>(null);
        this.j = null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public PointStyle(SeriesStyle parentStyle) {
        this.a = new dj<>(-16777216);
        this.b = new dj<>(-16777216);
        this.c = new dj<>(Float.valueOf(0.0f));
        this.d = new dj<>(-16777216);
        this.e = new dj<>(-16777216);
        this.f = new dj<>(Float.valueOf(3.0f));
        this.g = new dj<>(Float.valueOf(5.0f));
        this.h = new dj<>(false);
        this.i = new dj<>(null);
        this.j = null;
        this.j = parentStyle;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a(PointStyle pointStyle) {
        synchronized (x.a) {
            if (pointStyle != null) {
                this.a.b(Integer.valueOf(pointStyle.getColor()));
                this.b.b(Integer.valueOf(pointStyle.getColorBelowBaseline()));
                this.c.b(Float.valueOf(pointStyle.a()));
                this.d.b(Integer.valueOf(pointStyle.getInnerColor()));
                this.e.b(Integer.valueOf(pointStyle.getInnerColorBelowBaseline()));
                this.f.b(Float.valueOf(pointStyle.getInnerRadius()));
                this.g.b(Float.valueOf(pointStyle.getRadius()));
                this.h.b(Boolean.valueOf(pointStyle.arePointsShown()));
                this.i.b(pointStyle.b());
            }
        }
    }

    public int getColor() {
        return this.a.a.intValue();
    }

    public void setColor(int color) {
        synchronized (x.a) {
            this.a.a(Integer.valueOf(color));
            c();
        }
    }

    public int getColorBelowBaseline() {
        return this.b.a.intValue();
    }

    public void setColorBelowBaseline(int colorBelowBaseline) {
        synchronized (x.a) {
            this.b.a(Integer.valueOf(colorBelowBaseline));
            c();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public float a() {
        return this.c.a.floatValue();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a(float f) {
        synchronized (x.a) {
            this.c.a(Float.valueOf(f));
            c();
        }
    }

    public int getInnerColor() {
        return this.d.a.intValue();
    }

    public void setInnerColor(int innerColor) {
        synchronized (x.a) {
            this.d.a(Integer.valueOf(innerColor));
            c();
        }
    }

    public int getInnerColorBelowBaseline() {
        return this.e.a.intValue();
    }

    public void setInnerColorBelowBaseline(int innerColorBelowBaseline) {
        synchronized (x.a) {
            this.e.a(Integer.valueOf(innerColorBelowBaseline));
            c();
        }
    }

    public float getInnerRadius() {
        return this.f.a.floatValue();
    }

    public void setInnerRadius(float innerRadius) {
        synchronized (x.a) {
            this.f.a(Float.valueOf(innerRadius));
            c();
        }
    }

    public float getRadius() {
        return this.g.a.floatValue();
    }

    public void setRadius(float radius) {
        synchronized (x.a) {
            this.g.a(Float.valueOf(radius));
            c();
        }
    }

    public boolean arePointsShown() {
        return this.h.a.booleanValue();
    }

    public void setPointsShown(boolean showPoints) {
        synchronized (x.a) {
            this.h.a(Boolean.valueOf(showPoints));
            c();
        }
    }

    Drawable b() {
        return this.i.a;
    }

    final void c() {
        if (this.j != null) {
            this.j.d();
        }
    }
}

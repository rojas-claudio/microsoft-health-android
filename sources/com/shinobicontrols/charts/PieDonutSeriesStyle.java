package com.shinobicontrols.charts;

import android.graphics.Typeface;
import com.shinobicontrols.charts.PieDonutSeries;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public abstract class PieDonutSeriesStyle extends SeriesStyle {
    final dj<Boolean> a = new dj<>(true);
    final dj<Boolean> b = new dj<>(true);
    final dj<Boolean> c = new dj<>(true);
    final dj<PieDonutSeries.RadialEffect> d = new dj<>(PieDonutSeries.RadialEffect.DEFAULT);
    final dj<Float> e = new dj<>(Float.valueOf(0.0f));
    final dj<Integer[]> f = new dj<>(new Integer[]{-16777216, -1});
    final dj<Float> g = new dj<>(Float.valueOf(0.0f));
    final dj<Integer[]> h = new dj<>(new Integer[]{-16777216, -1});
    final dj<Float> i = new dj<>(Float.valueOf(0.0f));
    final dj<Typeface> j = new dj<>(null);
    final dj<Float> k = new dj<>(Float.valueOf(10.0f));
    final dj<Integer> l = new dj<>(-16777216);
    final dj<Integer> m = new dj<>(0);

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.shinobicontrols.charts.SeriesStyle
    public void a(SeriesStyle seriesStyle) {
        synchronized (x.a) {
            super.a(seriesStyle);
            PieDonutSeriesStyle pieDonutSeriesStyle = (PieDonutSeriesStyle) seriesStyle;
            this.a.b(Boolean.valueOf(pieDonutSeriesStyle.isCrustShown()));
            this.b.b(Boolean.valueOf(pieDonutSeriesStyle.isFlavorShown()));
            this.c.b(Boolean.valueOf(pieDonutSeriesStyle.areLabelsShown()));
            this.d.b(pieDonutSeriesStyle.getRadialEffect());
            this.e.b(Float.valueOf(pieDonutSeriesStyle.getInitialRotation()));
            this.f.b(pieDonutSeriesStyle.f.a);
            this.g.b(Float.valueOf(pieDonutSeriesStyle.getCrustThickness()));
            this.h.b(pieDonutSeriesStyle.h.a);
            this.i.b(Float.valueOf(pieDonutSeriesStyle.getProtrusion()));
            this.j.b(pieDonutSeriesStyle.getLabelTypeface());
            this.k.b(Float.valueOf(pieDonutSeriesStyle.getLabelTextSize()));
            this.l.b(Integer.valueOf(pieDonutSeriesStyle.getLabelTextColor()));
            this.m.b(Integer.valueOf(pieDonutSeriesStyle.getLabelBackgroundColor()));
        }
    }

    public boolean isCrustShown() {
        return this.a.a.booleanValue();
    }

    public void setCrustShown(boolean crustShown) {
        synchronized (x.a) {
            this.a.a(Boolean.valueOf(crustShown));
            d();
        }
    }

    public boolean isFlavorShown() {
        return this.b.a.booleanValue();
    }

    public void setFlavorShown(boolean flavorShown) {
        synchronized (x.a) {
            this.b.a(Boolean.valueOf(flavorShown));
            d();
        }
    }

    public boolean areLabelsShown() {
        return this.c.a.booleanValue();
    }

    public void setLabelsShown(boolean labelsShown) {
        synchronized (x.a) {
            this.c.a(Boolean.valueOf(labelsShown));
            d();
        }
    }

    public PieDonutSeries.RadialEffect getRadialEffect() {
        return this.d.a;
    }

    public void setRadialEffect(PieDonutSeries.RadialEffect radialEffect) {
        synchronized (x.a) {
            this.d.a(radialEffect);
            d();
        }
    }

    public float getInitialRotation() {
        return this.e.a.floatValue();
    }

    public void setInitialRotation(float initialRotation) {
        synchronized (x.a) {
            this.e.a(Float.valueOf(initialRotation));
            d();
        }
    }

    public int crustColorAtIndex(int index) {
        Integer[] numArr = this.f.a;
        return numArr[index % numArr.length].intValue();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Integer[] a() {
        return this.f.a;
    }

    public void setCrustColors(int[] crustColors) {
        synchronized (x.a) {
            if (crustColors != null) {
                Integer[] numArr = new Integer[crustColors.length];
                for (int i = 0; i < crustColors.length; i++) {
                    numArr[i] = Integer.valueOf(crustColors[i]);
                }
                this.f.a(numArr);
                d();
            }
        }
    }

    public float getCrustThickness() {
        return this.g.a.floatValue();
    }

    public void setCrustThickness(float crustThickness) {
        synchronized (x.a) {
            if (crustThickness < 1.0f) {
                cx.b("Ignoring setting of crustThickness: cannot have a crustThickness of less than 1");
            } else {
                this.g.a(Float.valueOf(crustThickness));
                d();
            }
        }
    }

    public int flavorColorAtIndex(int index) {
        Integer[] numArr = this.h.a;
        return numArr[index % numArr.length].intValue();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Integer[] b() {
        return this.h.a;
    }

    public void setFlavorColors(int[] flavorColors) {
        synchronized (x.a) {
            if (flavorColors != null) {
                Integer[] numArr = new Integer[flavorColors.length];
                for (int i = 0; i < flavorColors.length; i++) {
                    numArr[i] = Integer.valueOf(flavorColors[i]);
                }
                this.h.a(numArr);
                d();
            }
        }
    }

    public float getProtrusion() {
        return this.i.a.floatValue();
    }

    public void setProtrusion(float protrusion) {
        synchronized (x.a) {
            if (protrusion < 0.0f) {
                throw new IllegalArgumentException("Protrusion must be positive");
            }
            this.i.a(Float.valueOf(protrusion));
            d();
        }
    }

    public Typeface getLabelTypeface() {
        return this.j.a;
    }

    public void setLabelTypeface(Typeface labelTypeface) {
        this.j.a(labelTypeface);
        d();
    }

    public int getLabelTextColor() {
        return this.l.a.intValue();
    }

    public void setLabelTextColor(int labelTextColor) {
        this.l.a(Integer.valueOf(labelTextColor));
        d();
    }

    public int getLabelBackgroundColor() {
        return this.m.a.intValue();
    }

    public void setLabelBackgroundColor(int labelBackgroundColor) {
        this.m.a(Integer.valueOf(labelBackgroundColor));
        d();
    }

    public float getLabelTextSize() {
        return this.k.a.floatValue();
    }

    public void setLabelTextSize(float labelTextSize) {
        this.k.a(Float.valueOf(labelTextSize));
        d();
    }
}

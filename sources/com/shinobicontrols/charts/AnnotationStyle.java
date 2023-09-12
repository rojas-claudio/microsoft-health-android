package com.shinobicontrols.charts;

import android.graphics.Typeface;
/* loaded from: classes.dex */
public class AnnotationStyle {
    final dj<Integer> a = new dj<>(-16777216);
    final dj<Float> b = new dj<>(Float.valueOf(12.0f));
    final dj<Typeface> c = new dj<>(null);
    final dj<Integer> d = new dj<>(0);

    public int getTextColor() {
        return this.a.a.intValue();
    }

    public void setTextColor(int textColor) {
        this.a.a(Integer.valueOf(textColor));
    }

    public float getTextSize() {
        return this.b.a.floatValue();
    }

    public void setTextSize(float textSize) {
        this.b.a(Float.valueOf(textSize));
    }

    public Typeface getTypeface() {
        return this.c.a;
    }

    public void setTypeface(Typeface typeface) {
        this.c.a(typeface);
    }

    public int getBackgroundColor() {
        return this.d.a.intValue();
    }

    public void setBackgroundColor(int backgroundColor) {
        this.d.a(Integer.valueOf(backgroundColor));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a(AnnotationStyle annotationStyle) {
        if (annotationStyle != null) {
            this.d.b(Integer.valueOf(annotationStyle.getBackgroundColor()));
            this.a.b(Integer.valueOf(annotationStyle.getTextColor()));
            this.b.b(Float.valueOf(annotationStyle.getTextSize()));
            this.c.b(annotationStyle.getTypeface());
        }
    }
}

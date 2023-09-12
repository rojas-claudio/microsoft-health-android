package com.shinobicontrols.charts;

import android.view.View;
import android.view.ViewGroup;
import com.shinobicontrols.charts.d;
/* loaded from: classes.dex */
public class Annotation {
    private Object a;
    private Object b;
    private final Axis<?, ?> c;
    private final Axis<?, ?> d;
    private Range<?> e;
    private Range<?> f;
    private final View g;
    private AnnotationStyle i;
    private final e k;
    private Position h = Position.IN_FRONT_OF_DATA;
    private final al j = new al();

    /* loaded from: classes.dex */
    public enum Position {
        IN_FRONT_OF_DATA,
        BEHIND_DATA
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Annotation(View view, Axis<?, ?> xAxis, Axis<?, ?> yAxis, e styleApplier) {
        if (view == null) {
            throw new IllegalArgumentException("Annotation cannot have a null View.");
        }
        if (xAxis == null) {
            throw new IllegalArgumentException(view.getContext().getString(R.string.AnnotationCannotHaveNullX));
        }
        if (yAxis == null) {
            throw new IllegalArgumentException(view.getContext().getString(R.string.AnnotationCannotHaveNullY));
        }
        if (xAxis == yAxis) {
            throw new IllegalArgumentException(view.getContext().getString(R.string.AnnotationCannotHaveSameXY));
        }
        this.g = view;
        this.c = xAxis;
        this.d = yAxis;
        this.k = styleApplier;
    }

    public Object getXValue() {
        return this.a;
    }

    public void setXValue(Object value) {
        this.a = value;
    }

    public Object getYValue() {
        return this.b;
    }

    public void setYValue(Object value) {
        this.b = value;
    }

    public Axis<?, ?> getXAxis() {
        return this.c;
    }

    public Axis<?, ?> getYAxis() {
        return this.d;
    }

    public Range<?> getXRange() {
        return this.e;
    }

    public void setXRange(Range<?> range) {
        this.e = range;
    }

    public Range<?> getYRange() {
        return this.f;
    }

    public void setYRange(Range<?> range) {
        this.f = range;
    }

    public View getView() {
        return this.g;
    }

    public Position getPosition() {
        return this.h;
    }

    public void setPosition(Position position) {
        this.h = position;
        a();
    }

    public AnnotationStyle getStyle() {
        return this.i;
    }

    public void setStyle(AnnotationStyle style) {
        this.i = style;
    }

    final void a() {
        this.j.a(new d(this));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public am a(d.a aVar) {
        return this.j.a(d.a, aVar);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a(int i, int i2) {
        this.g.measure(i, i2);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a(int i, int i2, int i3, int i4) {
        ViewGroup.LayoutParams layoutParams = this.g.getLayoutParams();
        if (layoutParams.width != -1) {
            if (layoutParams.width == 0 && this.e != null) {
                i = ((int) this.c.f(this.c.translatePoint(this.e.getMinimum()))) - this.c.b.a.left;
                i3 = ((int) this.c.f(this.c.translatePoint(this.e.getMaximum()))) - this.c.b.a.left;
            } else if (this.a == null) {
                throw new NullPointerException(this.g.getContext().getString(R.string.AnnotationCannotConvertNullX));
            } else {
                double f = this.c.f(this.c.translatePoint(this.a)) - this.c.b.a.left;
                int measuredWidth = this.g.getMeasuredWidth();
                i = (int) (f - (measuredWidth / 2.0d));
                i3 = i + measuredWidth;
            }
        }
        if (layoutParams.height != -1) {
            if (layoutParams.height == 0 && this.f != null) {
                i2 = ((int) this.d.f(this.d.translatePoint(this.f.getMaximum()))) - this.d.b.a.top;
                i4 = ((int) this.d.f(this.d.translatePoint(this.f.getMinimum()))) - this.d.b.a.top;
            } else if (this.b == null) {
                throw new NullPointerException(this.g.getContext().getString(R.string.AnnotationCannotConvertNullY));
            } else {
                double f2 = this.d.f(this.d.translatePoint(this.b)) - this.d.b.a.top;
                int measuredHeight = this.g.getMeasuredHeight();
                i2 = (int) (f2 - (measuredHeight / 2.0d));
                i4 = i2 + measuredHeight;
            }
        }
        this.g.layout(i, i2, i3, i4);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void b() {
        this.k.a(this);
    }
}

package com.shinobicontrols.charts;

import android.graphics.drawable.Drawable;
import com.microsoft.kapp.utils.Constants;
import com.shinobicontrols.charts.Axis;
import com.shinobicontrols.charts.SeriesStyle;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public abstract class BarColumnSeries<T extends SeriesStyle> extends CartesianSeries<T> {
    float a = 0.8f;

    /* JADX INFO: Access modifiers changed from: package-private */
    public float a() {
        return this.a;
    }

    void b(float f) {
        synchronized (x.a) {
            this.a = f;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.shinobicontrols.charts.Series
    public void a(Axis<?, ?> axis) {
        if (axis.a(this.j)) {
            int length = this.n.c.length;
            for (int i = 0; i < length; i++) {
                a(this.n.c[i], axis);
            }
        }
    }

    private void a(InternalDataPoint internalDataPoint, Axis<?, ?> axis) {
        if (axis.c == Axis.Orientation.HORIZONTAL) {
            internalDataPoint.c = a(internalDataPoint.a, axis);
            internalDataPoint.d = internalDataPoint.b;
            return;
        }
        internalDataPoint.d = a(internalDataPoint.b, axis);
        internalDataPoint.c = internalDataPoint.a;
    }

    private double a(double d, Axis<?, ?> axis) {
        int c = this.t.c();
        int i = i();
        double b = axis.j.b();
        if (b == Constants.SPLITS_ACCURACY) {
            b = 1.0d;
        }
        double d2 = axis.j.a;
        double d3 = d2 + (((axis.e - d2) * b) / b);
        double floatValue = axis.f * (1.0f - axis.g.b.a.floatValue());
        double floatValue2 = (1.0f - axis.g.a.a.floatValue()) * (floatValue / c);
        b((float) floatValue2);
        return ((axis.g.a.a.floatValue() * floatValue) / (c * 2)) + (((floatValue2 / 2.0d) + (d3 + (d - axis.e))) - (floatValue / 2.0d)) + ((i * floatValue) / c);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public NumberRange a(double d, NumberRange numberRange) {
        return Range.a(numberRange) ? numberRange : new NumberRange(Double.valueOf(numberRange.a - (0.5d * d)), Double.valueOf(numberRange.b + (0.5d * d)));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public NumberRange a(NumberRange numberRange) {
        if (!Range.a(numberRange)) {
            NumberRange numberRange2 = new NumberRange(Double.valueOf(numberRange.a * 1.01d), Double.valueOf(numberRange.b * 1.01d));
            numberRange2.a(this.t.b((CartesianSeries<?>) this));
            return numberRange2;
        }
        return numberRange;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Drawable c(float f) {
        BarColumnSeriesStyle barColumnSeriesStyle = (BarColumnSeriesStyle) ((!isSelected() || this.r == null) ? this.q : this.r);
        return new ba(barColumnSeriesStyle.getFillStyle() == SeriesStyle.FillStyle.NONE ? 0 : barColumnSeriesStyle.getAreaColor(), barColumnSeriesStyle.isLineShown() ? barColumnSeriesStyle.getLineColor() : 0, f);
    }
}

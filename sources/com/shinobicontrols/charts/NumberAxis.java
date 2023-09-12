package com.shinobicontrols.charts;

import android.graphics.PointF;
import com.microsoft.kapp.utils.Constants;
import java.text.DecimalFormat;
/* loaded from: classes.dex */
public class NumberAxis extends Axis<Double, Double> {
    private int A;
    private DecimalFormat D;
    private int B = -1;
    private final bq C = new bq();
    private final PointF E = new PointF(1.2f, 1.2f);

    public NumberAxis() {
    }

    public NumberAxis(NumberRange defaultRange) {
        setDefaultRange(defaultRange);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.shinobicontrols.charts.Axis
    public boolean isDataValid(Object point) {
        return point instanceof Number;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.shinobicontrols.charts.Axis
    public double convertPoint(Object userData) {
        return translatePoint(userData);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.shinobicontrols.charts.Axis
    public double translatePoint(Object userData) {
        validateUserData(userData);
        return transformUserValueToInternal(Double.valueOf(convertUserValueTypeToInternalDataType(userData)));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.shinobicontrols.charts.Axis
    public double transformExternalValueToInternal(Double externalValue) {
        return externalValue.doubleValue();
    }

    @Override // com.shinobicontrols.charts.Axis
    double convertUserValueTypeToInternalDataType(Object rawUserValue) {
        return Double.valueOf(((Number) rawUserValue).doubleValue()).doubleValue();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.shinobicontrols.charts.Axis
    public Double transformInternalValueToExternal(double internalValue) {
        return Double.valueOf(internalValue);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.shinobicontrols.charts.Axis
    public Double transformUserValueToChartValue(Double userValue) {
        return userValue;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.shinobicontrols.charts.Axis
    public Double transformChartValueToUserValue(Double chartValue) {
        return chartValue;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.shinobicontrols.charts.Axis
    public Range<Double> createRange(Double min, Double max) {
        return new NumberRange(min, max);
    }

    public DecimalFormat getLabelFormat() {
        return this.D;
    }

    public void setLabelFormat(DecimalFormat labelFormat) {
        this.D = labelFormat;
    }

    @Override // com.shinobicontrols.charts.Axis
    String i() {
        if (m()) {
            H();
        }
        if (k()) {
            return this.x;
        }
        if (I()) {
            String format = this.D.format(this.i.a);
            String format2 = this.D.format(this.i.b);
            if (format2.length() <= format.length()) {
                format2 = format;
            }
            this.w = format2;
        } else {
            this.C.a(this.A);
            if (this.C.b(this.B)) {
                this.u.c();
            }
            this.w = this.C.a();
        }
        return this.w;
    }

    private void H() {
        int h = h(((Double) this.r).doubleValue());
        int h2 = h(G());
        this.A = g(Math.max(Math.abs(this.i.a), Math.abs(this.i.b)));
        this.B = Math.max(h2, h);
    }

    private static int g(double d) {
        double log10 = Math.log10(d);
        if (log10 >= 1.0d) {
            return 1 + ((int) Math.floor(log10));
        }
        return 1;
    }

    private static int h(double d) {
        if (d == Constants.SPLITS_ACCURACY) {
            return 0;
        }
        double log10 = Math.log10(d);
        if (log10 < Constants.SPLITS_ACCURACY) {
            return (int) Math.ceil(-log10);
        }
        return 0;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.shinobicontrols.charts.Axis
    public String getFormattedString(Double value) {
        return I() ? this.D.format(value) : this.C.format(value);
    }

    private boolean I() {
        return this.D != null;
    }

    /* JADX WARN: Type inference failed for: r0v19, types: [java.lang.Double, U] */
    /* JADX WARN: Type inference failed for: r0v20, types: [java.lang.Double, U] */
    /* JADX WARN: Type inference failed for: r0v21, types: [java.lang.Double, U] */
    /* JADX WARN: Type inference failed for: r0v22, types: [java.lang.Double, U] */
    /* JADX WARN: Type inference failed for: r0v28, types: [java.lang.Double, U] */
    /* JADX WARN: Type inference failed for: r0v29, types: [java.lang.Double, U] */
    @Override // com.shinobicontrols.charts.Axis
    void c(int i) {
        setCurrentMajorTickFrequency(Double.valueOf(this.i.b() / 20.0d));
        double pow = Math.pow(10.0d, Math.floor(Math.log10(((Double) this.r).doubleValue())));
        do {
            if (pow > ((Double) this.r).doubleValue()) {
                this.r = Double.valueOf(pow);
                this.s = Double.valueOf(pow / 2.0d);
            } else if (5.0d * pow > ((Double) this.r).doubleValue()) {
                this.r = Double.valueOf(5.0d * pow);
                this.s = Double.valueOf(pow);
            } else if (10.0d * pow > ((Double) this.r).doubleValue()) {
                this.r = Double.valueOf(10.0d * pow);
                this.s = Double.valueOf(5.0d * pow);
            } else {
                pow *= 10.0d;
            }
            r();
        } while (!a((int) Math.floor(this.i.b() / ((Double) this.r).doubleValue()), i, this.E));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.shinobicontrols.charts.Axis
    public double a(int i) {
        double d = this.i.a;
        double G = G();
        double floor = G + (Math.floor((d - G) / ((Double) this.r).doubleValue()) * ((Double) this.r).doubleValue());
        while (floor < d) {
            floor = a(floor, true);
        }
        while (!a(floor, i, this.i.b())) {
            floor += ((Double) this.r).doubleValue();
        }
        return floor;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.shinobicontrols.charts.Axis
    public double b(int i) {
        if (!o()) {
            return Double.NaN;
        }
        double a = a(i);
        double b = b(a, false);
        while (b >= this.i.a) {
            a = b;
            b = b(b, false);
        }
        return a;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.shinobicontrols.charts.Axis
    public boolean b(double d) {
        double IEEEremainder = Math.IEEEremainder((d - G()) / ((Double) this.r).doubleValue(), 2.0d);
        return (IEEEremainder < -0.5d && IEEEremainder > -1.5d) || (IEEEremainder > 0.5d && IEEEremainder < 1.5d);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.shinobicontrols.charts.Axis
    public double a(double d, boolean z) {
        double doubleValue = (z ? (Double) this.r : getCurrentMinorTickFrequency()).doubleValue() + d;
        return (doubleValue >= Constants.SPLITS_ACCURACY || (0.9998999834060669d * Math.pow(10.0d, (double) (-(this.B + 1)))) + doubleValue <= Constants.SPLITS_ACCURACY) ? doubleValue : Constants.SPLITS_ACCURACY;
    }

    double b(double d, boolean z) {
        double doubleValue = d - (z ? (Double) this.r : getCurrentMinorTickFrequency()).doubleValue();
        return (doubleValue >= Constants.SPLITS_ACCURACY || (0.9998999834060669d * Math.pow(10.0d, (double) (-(this.B + 1)))) + doubleValue <= Constants.SPLITS_ACCURACY) ? doubleValue : Constants.SPLITS_ACCURACY;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.shinobicontrols.charts.Axis
    public double transformExternalFrequencyToInternal(Double externalValue) {
        return externalValue.doubleValue();
    }

    @Override // com.shinobicontrols.charts.Axis
    double x() {
        return 1.0d;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.shinobicontrols.charts.Axis
    public void setMajorTickFrequencyInternal(Double frequency) {
        if (frequency == 0) {
            this.p = null;
        } else if (frequency.doubleValue() > Constants.SPLITS_ACCURACY) {
            this.p = frequency;
        } else {
            cx.b(this.b != null ? this.b.getContext().getString(R.string.NumberAxisInvalidFrequency) : "The frequency is invalid and will be ignored");
            this.p = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.shinobicontrols.charts.Axis
    public void setMinorTickFrequencyInternal(Double frequency) {
        if (frequency == 0) {
            this.q = null;
        } else if (frequency.doubleValue() > Constants.SPLITS_ACCURACY) {
            this.q = frequency;
        } else {
            cx.b(this.b != null ? this.b.getContext().getString(R.string.NumberAxisInvalidFrequency) : "The frequency is invalid and will be ignored");
            this.q = null;
        }
    }

    /* JADX WARN: Type inference failed for: r0v4, types: [java.lang.Double, U] */
    @Override // com.shinobicontrols.charts.Axis
    void p() {
        if (this.p != 0) {
            this.r = Double.valueOf(((Double) this.p).doubleValue());
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Type inference failed for: r0v5, types: [java.lang.Double, U] */
    @Override // com.shinobicontrols.charts.Axis
    public void q() {
        if (l() && this.q != 0) {
            this.s = Double.valueOf(((Double) this.q).doubleValue());
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.shinobicontrols.charts.Axis
    public Double getDefaultBaseline() {
        return Double.valueOf((double) Constants.SPLITS_ACCURACY);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.shinobicontrols.charts.Axis
    public Double applyMappingForSkipRangesToUserValue(Double userValue) {
        return Double.valueOf(this.y.a(userValue.doubleValue()));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.shinobicontrols.charts.Axis
    public Double removeMappingForSkipRangesFromChartValue(Double chartValue) {
        return Double.valueOf(this.z.a(chartValue.doubleValue()));
    }

    @Override // com.shinobicontrols.charts.Axis
    void g() {
    }

    double G() {
        return Constants.SPLITS_ACCURACY;
    }
}

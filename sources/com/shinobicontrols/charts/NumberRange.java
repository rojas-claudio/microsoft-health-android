package com.shinobicontrols.charts;
/* loaded from: classes.dex */
public class NumberRange extends Range<Double> {
    /* JADX INFO: Access modifiers changed from: package-private */
    public NumberRange() {
        super(Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY);
    }

    public NumberRange(Double min, Double max) {
        super(min.doubleValue(), max.doubleValue());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a(double d, double d2) {
        this.a = d;
        this.b = d2;
    }

    @Override // com.shinobicontrols.charts.Range
    public Double getMinimum() {
        return Double.valueOf(this.a);
    }

    @Override // com.shinobicontrols.charts.Range
    public Double getMaximum() {
        return Double.valueOf(this.b);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Range<Double> a() {
        return new NumberRange(Double.valueOf(this.a), Double.valueOf(this.b));
    }
}

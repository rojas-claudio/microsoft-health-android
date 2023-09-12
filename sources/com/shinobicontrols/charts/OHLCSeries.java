package com.shinobicontrols.charts;

import android.graphics.drawable.Drawable;
import com.shinobicontrols.charts.CartesianSeries;
import com.shinobicontrols.charts.aa;
/* loaded from: classes.dex */
public class OHLCSeries extends BarColumnSeries<OHLCSeriesStyle> {
    public OHLCSeries() {
        this.p = new bs(this);
        this.k = new br(this);
        setStyle(d());
        setSelectedStyle(d());
        this.v = SeriesAnimation.createGrowVerticalAnimation();
        this.w = SeriesAnimation.createGrowVerticalAnimation();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.shinobicontrols.charts.Series
    /* renamed from: e */
    public OHLCSeriesStyle d() {
        return new OHLCSeriesStyle();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.shinobicontrols.charts.Series
    /* renamed from: a */
    public OHLCSeriesStyle b(dd ddVar, int i, boolean z) {
        return ddVar.c(i, z);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.shinobicontrols.charts.Series
    public Drawable a(float f) {
        OHLCSeriesStyle oHLCSeriesStyle = (!isSelected() || this.r == 0) ? (OHLCSeriesStyle) this.q : (OHLCSeriesStyle) this.r;
        if (oHLCSeriesStyle.c()) {
            return new bd();
        }
        return new ba(oHLCSeriesStyle.getRisingColor(), oHLCSeriesStyle.getRisingColor(), f);
    }

    @Override // com.shinobicontrols.charts.CartesianSeries
    InternalDataPoint a(Data<?, ?> data, Axis<?, ?> axis, Axis<?, ?> axis2) {
        return b(data, axis, axis2);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static InternalDataPoint b(Data<?, ?> data, Axis<?, ?> axis, Axis<?, ?> axis2) {
        InternalDataPoint internalDataPoint = new InternalDataPoint();
        double convertPoint = axis.convertPoint(data.getX());
        internalDataPoint.a = convertPoint;
        internalDataPoint.c = convertPoint;
        MultiValueData multiValueData = (MultiValueData) data;
        internalDataPoint.a(axis2.convertPoint(multiValueData.getOpen()), axis2.convertPoint(multiValueData.getHigh()), axis2.convertPoint(multiValueData.getLow()), axis2.convertPoint(multiValueData.getClose()));
        if (data instanceof SelectableData) {
            internalDataPoint.h = ((SelectableData) data).getSelected();
        }
        return internalDataPoint;
    }

    @Override // com.shinobicontrols.charts.CartesianSeries
    void a(ao aoVar, InternalDataPoint internalDataPoint) {
        b(aoVar, internalDataPoint);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void b(ao aoVar, InternalDataPoint internalDataPoint) {
        aoVar.a.a(internalDataPoint.a);
        aoVar.b.a(internalDataPoint.j.get("Low").doubleValue());
        aoVar.b.a(internalDataPoint.j.get("High").doubleValue());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.shinobicontrols.charts.CartesianSeries
    public void a(Axis<?, ?> axis, Axis<?, ?> axis2) {
        super.a(axis, axis2);
        super.n();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.shinobicontrols.charts.CartesianSeries
    public NumberRange b(Axis<?, ?> axis) {
        return axis.a() ? a(axis.c(), o()) : a(p());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.shinobicontrols.charts.BarColumnSeries
    public NumberRange a(NumberRange numberRange) {
        if (!Range.a(numberRange)) {
            return new NumberRange(Double.valueOf(numberRange.a * 1.01d), Double.valueOf(numberRange.b * 1.01d));
        }
        return numberRange;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.shinobicontrols.charts.CartesianSeries
    public CartesianSeries.a f() {
        return CartesianSeries.a.HORIZONTAL;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.shinobicontrols.charts.CartesianSeries
    public CartesianSeries.a g() {
        return CartesianSeries.a.HORIZONTAL;
    }

    @Override // com.shinobicontrols.charts.CartesianSeries
    public void setBaseline(Object baseline) {
        cx.b(this.o != null ? this.o.getContext().getString(R.string.OHLCSeriesBaselineNotApplicable) : "Baseline not applicable for OHLCSeries.");
    }

    @Override // com.shinobicontrols.charts.CartesianSeries
    aa c() {
        return new aa.d();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.shinobicontrols.charts.CartesianSeries, com.shinobicontrols.charts.Series
    public double b() {
        return this.u.b != null ? (getYAxis().i.a * (1.0d - this.u.b.floatValue())) + (getYAxis().i.b * this.u.b.floatValue()) : this.t.b((CartesianSeries<?>) this);
    }

    @Override // com.shinobicontrols.charts.CartesianSeries
    boolean a(Axis<?, ?> axis, Axis<?, ?> axis2, Data<?, ?> data) {
        return b(axis, axis2, data);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean b(Axis<?, ?> axis, Axis<?, ?> axis2, Data<?, ?> data) {
        return a(axis, data) || b(axis2, data);
    }

    private static boolean a(Axis<?, ?> axis, Data<?, ?> data) {
        return axis.isUserDataPointWithinASkipRange(data.getX());
    }

    private static boolean b(Axis<?, ?> axis, Data<?, ?> data) {
        return axis.isUserDataPointWithinASkipRange(((MultiValueData) data).getOpen()) || axis.isUserDataPointWithinASkipRange(((MultiValueData) data).getHigh()) || axis.isUserDataPointWithinASkipRange(((MultiValueData) data).getLow()) || axis.isUserDataPointWithinASkipRange(((MultiValueData) data).getClose());
    }
}

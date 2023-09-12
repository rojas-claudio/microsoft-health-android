package com.shinobicontrols.charts;

import android.graphics.drawable.Drawable;
import com.shinobicontrols.charts.CartesianSeries;
import com.shinobicontrols.charts.aa;
/* loaded from: classes.dex */
public class CandlestickSeries extends BarColumnSeries<CandlestickSeriesStyle> {
    public CandlestickSeries() {
        this.p = new p(this);
        this.k = new o(this);
        setStyle(d());
        setSelectedStyle(d());
        this.v = SeriesAnimation.createGrowVerticalAnimation();
        this.w = SeriesAnimation.createGrowVerticalAnimation();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.shinobicontrols.charts.Series
    /* renamed from: e */
    public CandlestickSeriesStyle d() {
        return new CandlestickSeriesStyle();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.shinobicontrols.charts.Series
    /* renamed from: a */
    public CandlestickSeriesStyle b(dd ddVar, int i, boolean z) {
        return ddVar.b(i, z);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.shinobicontrols.charts.Series
    public Drawable a(float f) {
        CandlestickSeriesStyle candlestickSeriesStyle = (!isSelected() || this.r == 0) ? (CandlestickSeriesStyle) this.q : (CandlestickSeriesStyle) this.r;
        if (candlestickSeriesStyle.c()) {
            return new bb();
        }
        return new ba(candlestickSeriesStyle.getRisingColor(), candlestickSeriesStyle.getOutlineColor(), f);
    }

    @Override // com.shinobicontrols.charts.CartesianSeries
    InternalDataPoint a(Data<?, ?> data, Axis<?, ?> axis, Axis<?, ?> axis2) {
        return OHLCSeries.b(data, axis, axis2);
    }

    @Override // com.shinobicontrols.charts.CartesianSeries
    void a(ao aoVar, InternalDataPoint internalDataPoint) {
        OHLCSeries.b(aoVar, internalDataPoint);
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
        cx.b(this.o != null ? this.o.getContext().getString(R.string.CandlestickSeriesBaselineNotApplicable) : "Baseline not applicable for CandlestickSeries.");
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
        return OHLCSeries.b(axis, axis2, data);
    }
}

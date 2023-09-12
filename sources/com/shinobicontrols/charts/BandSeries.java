package com.shinobicontrols.charts;

import android.graphics.drawable.Drawable;
import com.shinobicontrols.charts.Series;
import com.shinobicontrols.charts.aa;
/* loaded from: classes.dex */
public class BandSeries extends CartesianSeries<BandSeriesStyle> {
    public BandSeries() {
        this(Series.Orientation.HORIZONTAL);
    }

    public BandSeries(Series.Orientation orientation) {
        this.j = orientation;
        this.p = new k(this);
        this.k = new j(this);
        setStyle(d());
        setSelectedStyle(d());
        this.v = SeriesAnimation.createTelevisionAnimation();
        this.w = SeriesAnimation.createTelevisionAnimation();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.shinobicontrols.charts.Series
    /* renamed from: a */
    public BandSeriesStyle d() {
        return new BandSeriesStyle();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.shinobicontrols.charts.Series
    /* renamed from: a */
    public BandSeriesStyle b(dd ddVar, int i, boolean z) {
        return ddVar.a(i, z);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.shinobicontrols.charts.Series
    public Drawable a(float f) {
        BandSeriesStyle bandSeriesStyle = (!isSelected() || this.r == 0) ? (BandSeriesStyle) this.q : (BandSeriesStyle) this.r;
        if (bandSeriesStyle.c()) {
            return new az();
        }
        return new ba(bandSeriesStyle.getAreaColorNormal(), bandSeriesStyle.getLineColorHigh(), f);
    }

    @Override // com.shinobicontrols.charts.CartesianSeries
    InternalDataPoint a(Data<?, ?> data, Axis<?, ?> axis, Axis<?, ?> axis2) {
        double convertPoint;
        double convertPoint2;
        MultiValueData multiValueData = (MultiValueData) data;
        InternalDataPoint internalDataPoint = new InternalDataPoint();
        if (this.j == Series.Orientation.HORIZONTAL) {
            double convertPoint3 = axis.convertPoint(data.getX());
            internalDataPoint.a = convertPoint3;
            internalDataPoint.c = convertPoint3;
            convertPoint = axis2.convertPoint(multiValueData.getHigh());
            convertPoint2 = axis2.convertPoint(multiValueData.getLow());
        } else {
            double convertPoint4 = axis2.convertPoint(data.getX());
            internalDataPoint.a = convertPoint4;
            internalDataPoint.c = convertPoint4;
            convertPoint = axis.convertPoint(multiValueData.getHigh());
            convertPoint2 = axis.convertPoint(multiValueData.getLow());
        }
        internalDataPoint.a(convertPoint2, convertPoint);
        if (data instanceof SelectableData) {
            internalDataPoint.h = ((SelectableData) data).getSelected();
        }
        return internalDataPoint;
    }

    @Override // com.shinobicontrols.charts.CartesianSeries
    void a(ao aoVar, InternalDataPoint internalDataPoint) {
        if (this.j == Series.Orientation.HORIZONTAL) {
            aoVar.a.a(internalDataPoint.a);
            aoVar.b.a(internalDataPoint.j.get("Low").doubleValue());
            aoVar.b.a(internalDataPoint.j.get("High").doubleValue());
            return;
        }
        aoVar.b.a(internalDataPoint.a);
        aoVar.a.a(internalDataPoint.j.get("Low").doubleValue());
        aoVar.a.a(internalDataPoint.j.get("High").doubleValue());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.shinobicontrols.charts.CartesianSeries
    public void a(Axis<?, ?> axis, Axis<?, ?> axis2) {
        super.a(axis, axis2);
        super.n();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.shinobicontrols.charts.CartesianSeries, com.shinobicontrols.charts.Series
    public double b() {
        return this.u.b != null ? (getYAxis().i.a * (1.0d - this.u.b.floatValue())) + (getYAxis().i.b * this.u.b.floatValue()) : this.t.b((CartesianSeries<?>) this);
    }

    @Override // com.shinobicontrols.charts.CartesianSeries
    public void setBaseline(Object baseline) {
        cx.b(this.o != null ? this.o.getContext().getString(R.string.BandSeriesBaselineNotApplicable) : "Baseline not applicable for BandSeries.");
    }

    @Override // com.shinobicontrols.charts.CartesianSeries
    aa c() {
        return new aa.d();
    }

    @Override // com.shinobicontrols.charts.CartesianSeries
    boolean a(Axis<?, ?> axis, Axis<?, ?> axis2, Data<?, ?> data) {
        return (this.j == Series.Orientation.HORIZONTAL ? a(axis, data) : a(axis2, data)) || (this.j == Series.Orientation.HORIZONTAL ? b(axis2, data) : b(axis, data));
    }

    private boolean a(Axis<?, ?> axis, Data<?, ?> data) {
        return axis.isUserDataPointWithinASkipRange(data.getX());
    }

    private boolean b(Axis<?, ?> axis, Data<?, ?> data) {
        return axis.isUserDataPointWithinASkipRange(((MultiValueData) data).getHigh()) || axis.isUserDataPointWithinASkipRange(((MultiValueData) data).getLow());
    }
}

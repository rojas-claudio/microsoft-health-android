package com.shinobicontrols.charts;

import android.graphics.drawable.Drawable;
import com.shinobicontrols.charts.CartesianSeries;
import com.shinobicontrols.charts.LineSeriesStyle;
import com.shinobicontrols.charts.Series;
import com.shinobicontrols.charts.SeriesStyle;
import com.shinobicontrols.charts.aa;
/* loaded from: classes.dex */
public final class LineSeries extends CartesianSeries<LineSeriesStyle> {
    private final ca a = new ca();

    public LineSeries() {
        this.p = new bi(this);
        this.k = new bh(this);
        setStyle(d());
        setSelectedStyle(d());
        this.v = SeriesAnimation.createTelevisionAnimation();
        this.w = SeriesAnimation.createTelevisionAnimation();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.shinobicontrols.charts.Series
    /* renamed from: a */
    public LineSeriesStyle d() {
        return new LineSeriesStyle();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.shinobicontrols.charts.Series
    /* renamed from: a */
    public LineSeriesStyle b(dd ddVar, int i, boolean z) {
        return ddVar.f(i, z);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.shinobicontrols.charts.Series
    public Drawable a(float f) {
        LineSeriesStyle lineSeriesStyle = (!isSelected() || this.r == 0) ? (LineSeriesStyle) this.q : (LineSeriesStyle) this.r;
        if (lineSeriesStyle.c()) {
            return new bc();
        }
        return new ba(lineSeriesStyle.getFillStyle() == SeriesStyle.FillStyle.NONE ? lineSeriesStyle.getLineColor() : lineSeriesStyle.getAreaColor(), lineSeriesStyle.getFillStyle() == SeriesStyle.FillStyle.NONE ? lineSeriesStyle.getLineColor() : lineSeriesStyle.getAreaLineColor(), f);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.shinobicontrols.charts.CartesianSeries
    public void a(Series.a aVar, bz bzVar, boolean z) {
        cn.a(this, aVar, bzVar, z);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.shinobicontrols.charts.CartesianSeries
    public void a(Series.a aVar, bz bzVar) {
        if (this.o.h != null && this.o.h.b) {
            bz a = a(aVar.b(), bzVar);
            aVar.a(new bz(a.b, a.c));
            return;
        }
        super.a(aVar, bzVar);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.shinobicontrols.charts.CartesianSeries
    public CartesianSeries.a g() {
        switch (((LineSeriesStyle) this.q).m.a) {
            case HORIZONTAL:
                return CartesianSeries.a.HORIZONTAL;
            case VERTICAL:
                return CartesianSeries.a.VERTICAL;
            default:
                return CartesianSeries.a.HORIZONTAL;
        }
    }

    private bz a(InternalDataPoint internalDataPoint, bz bzVar) {
        return this.a.a(internalDataPoint, bzVar, this.n.c, ((LineSeriesStyle) this.q).m.a == LineSeriesStyle.a.HORIZONTAL);
    }

    @Override // com.shinobicontrols.charts.CartesianSeries
    aa c() {
        return new aa.c();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.shinobicontrols.charts.CartesianSeries, com.shinobicontrols.charts.Series
    public double b() {
        return this.u.b != null ? (getYAxis().i.a * (1.0d - this.u.b.floatValue())) + (getYAxis().i.b * this.u.b.floatValue()) : this.t.b((CartesianSeries<?>) this);
    }
}

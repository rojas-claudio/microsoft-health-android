package com.shinobicontrols.charts;

import android.graphics.drawable.Drawable;
import com.shinobicontrols.charts.CartesianSeries;
import com.shinobicontrols.charts.Series;
import com.shinobicontrols.charts.aa;
/* loaded from: classes.dex */
public final class ColumnSeries extends BarColumnSeries<ColumnSeriesStyle> {
    public ColumnSeries() {
        this.j = Series.Orientation.HORIZONTAL;
        this.i = true;
        this.p = new m(this);
        this.k = new l(this);
        setStyle(d());
        setSelectedStyle(d());
        this.v = SeriesAnimation.createGrowVerticalAnimation();
        this.w = SeriesAnimation.createGrowVerticalAnimation();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.shinobicontrols.charts.Series
    /* renamed from: e */
    public ColumnSeriesStyle d() {
        return new ColumnSeriesStyle();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.shinobicontrols.charts.Series
    /* renamed from: a */
    public ColumnSeriesStyle b(dd ddVar, int i, boolean z) {
        return ddVar.d(i, z);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.shinobicontrols.charts.CartesianSeries
    public NumberRange b(Axis<?, ?> axis) {
        return axis.a() ? a(axis.c(), o()) : a(p());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.shinobicontrols.charts.Series
    public Drawable a(float f) {
        if (((ColumnSeriesStyle) this.q).c()) {
            return null;
        }
        return c(f);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.shinobicontrols.charts.CartesianSeries
    public void a(Series.a aVar, bz bzVar, boolean z) {
        cn.b((BarColumnSeries<?>) this, aVar, bzVar, z);
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

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.shinobicontrols.charts.CartesianSeries, com.shinobicontrols.charts.Series
    public double b() {
        return this.u.b != null ? (getYAxis().i.a * (1.0d - this.u.b.floatValue())) + (getYAxis().i.b * this.u.b.floatValue()) : this.t.b((CartesianSeries<?>) this);
    }

    @Override // com.shinobicontrols.charts.CartesianSeries
    aa c() {
        return new aa.b();
    }
}

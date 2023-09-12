package com.shinobicontrols.charts;

import android.graphics.drawable.Drawable;
import com.shinobicontrols.charts.CartesianSeries;
import com.shinobicontrols.charts.Series;
import com.shinobicontrols.charts.aa;
/* loaded from: classes.dex */
public final class BarSeries extends BarColumnSeries<BarSeriesStyle> {
    public BarSeries() {
        this.j = Series.Orientation.VERTICAL;
        this.i = true;
        this.p = new m(this);
        this.k = new l(this);
        setStyle(d());
        setSelectedStyle(d());
        this.v = SeriesAnimation.createGrowHorizontalAnimation();
        this.w = SeriesAnimation.createGrowHorizontalAnimation();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.shinobicontrols.charts.Series
    /* renamed from: e */
    public BarSeriesStyle d() {
        return new BarSeriesStyle();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.shinobicontrols.charts.Series
    /* renamed from: a */
    public BarSeriesStyle b(dd ddVar, int i, boolean z) {
        return ddVar.e(i, z);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.shinobicontrols.charts.CartesianSeries
    public NumberRange b(Axis<?, ?> axis) {
        return axis.a() ? a(o()) : a(axis.c(), p());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.shinobicontrols.charts.Series
    public Drawable a(float f) {
        if (((BarSeriesStyle) this.q).c()) {
            return null;
        }
        return c(f);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.shinobicontrols.charts.CartesianSeries
    public void a(Series.a aVar, bz bzVar, boolean z) {
        cn.a((BarColumnSeries<?>) this, aVar, bzVar, z);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.shinobicontrols.charts.CartesianSeries
    public CartesianSeries.a f() {
        return CartesianSeries.a.VERTICAL;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.shinobicontrols.charts.CartesianSeries
    public CartesianSeries.a g() {
        return CartesianSeries.a.VERTICAL;
    }

    @Override // com.shinobicontrols.charts.CartesianSeries
    aa c() {
        return new aa.a();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.shinobicontrols.charts.CartesianSeries, com.shinobicontrols.charts.Series
    public double h() {
        return this.u.a != null ? (getXAxis().i.a * (1.0d - this.u.a.floatValue())) + (getXAxis().i.b * this.u.a.floatValue()) : this.t.b((CartesianSeries<?>) this);
    }
}

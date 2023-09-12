package com.shinobicontrols.charts;
/* loaded from: classes.dex */
public class DonutSeries extends PieDonutSeries<DonutSeriesStyle> {
    public DonutSeries() {
        this.p = new bt(this);
        setStyle(d());
        setSelectedStyle(d());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.shinobicontrols.charts.Series
    /* renamed from: a */
    public DonutSeriesStyle d() {
        return new DonutSeriesStyle();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.shinobicontrols.charts.Series
    /* renamed from: a */
    public DonutSeriesStyle b(dd ddVar, int i, boolean z) {
        return ddVar.h(i, z);
    }

    @Override // com.shinobicontrols.charts.PieDonutSeries
    public float getInnerRadius() {
        return super.getInnerRadius();
    }

    public void setInnerRadius(float innerRadius) {
        synchronized (x.a) {
            this.a.a(Float.valueOf(innerRadius));
            a_();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.shinobicontrols.charts.PieDonutSeries
    public float b(float f) {
        return f;
    }
}

package com.shinobicontrols.charts;
/* loaded from: classes.dex */
public class PieSeries extends PieDonutSeries<PieSeriesStyle> {
    public PieSeries() {
        this.p = new bt(this);
        setStyle(d());
        setSelectedStyle(d());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.shinobicontrols.charts.Series
    /* renamed from: a */
    public PieSeriesStyle d() {
        return new PieSeriesStyle();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.shinobicontrols.charts.Series
    /* renamed from: a */
    public PieSeriesStyle b(dd ddVar, int i, boolean z) {
        return ddVar.g(i, z);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.shinobicontrols.charts.PieDonutSeries
    public float b(float f) {
        return 0.0f;
    }
}

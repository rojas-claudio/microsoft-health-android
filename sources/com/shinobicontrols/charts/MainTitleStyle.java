package com.shinobicontrols.charts;

import com.shinobicontrols.charts.Title;
/* loaded from: classes.dex */
public final class MainTitleStyle extends TitleStyle {
    final dj<Boolean> a = new dj<>(false);
    final dj<Title.CentersOn> b = new dj<>(Title.CentersOn.PLOTTING_AREA);

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a(MainTitleStyle mainTitleStyle) {
        super.a((TitleStyle) mainTitleStyle);
        this.a.b(Boolean.valueOf(mainTitleStyle.getOverlapsChart()));
        this.b.b(mainTitleStyle.getCentersOn());
    }

    public Title.CentersOn getCentersOn() {
        return this.b.a;
    }

    public void setCentersOn(Title.CentersOn titleCentersOn) {
        this.b.a(titleCentersOn);
    }

    public boolean getOverlapsChart() {
        return this.a.a.booleanValue();
    }

    public void setOverlapsChart(boolean overlapChartTitle) {
        this.a.a(Boolean.valueOf(overlapChartTitle));
    }
}

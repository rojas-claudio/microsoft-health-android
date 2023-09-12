package com.shinobicontrols.charts;

import com.shinobicontrols.charts.Title;
/* loaded from: classes.dex */
public final class AxisTitleStyle extends TitleStyle {
    final dj<Title.Orientation> a = new dj<>(Title.Orientation.HORIZONTAL);

    public Title.Orientation getOrientation() {
        return this.a.a;
    }

    public void setOrientation(Title.Orientation orientation) {
        this.a.a(orientation);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a(AxisTitleStyle axisTitleStyle) {
        super.a((TitleStyle) axisTitleStyle);
        this.a.b(axisTitleStyle.getOrientation());
    }
}

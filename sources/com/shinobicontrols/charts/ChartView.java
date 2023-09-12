package com.shinobicontrols.charts;

import android.content.Context;
import android.util.AttributeSet;
/* loaded from: classes.dex */
public class ChartView extends ChartViewBase {
    @Override // com.shinobicontrols.charts.ChartViewBase
    public /* bridge */ /* synthetic */ void onPause() {
        super.onPause();
    }

    @Override // com.shinobicontrols.charts.ChartViewBase
    public /* bridge */ /* synthetic */ void onResume() {
        super.onResume();
    }

    public ChartView(Context context) {
        this(context, null);
    }

    public ChartView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ChartView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override // com.shinobicontrols.charts.ChartViewBase
    v a(Context context, AttributeSet attributeSet, int i) {
        return new ce(context, attributeSet, i);
    }
}

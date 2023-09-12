package com.shinobicontrols.charts;

import com.shinobicontrols.charts.CartesianSeries;
/* loaded from: classes.dex */
abstract class t<T extends CartesianSeries<?>> extends n<T> {
    float m;

    abstract void a(SChartGLDrawer sChartGLDrawer, float[] fArr, float f);

    /* JADX INFO: Access modifiers changed from: package-private */
    public t(T t) {
        super(t);
    }

    @Override // com.shinobicontrols.charts.cu
    public void a(ai aiVar, SChartGLDrawer sChartGLDrawer) {
        Axis<?, ?> xAxis = ((CartesianSeries) this.k).getXAxis();
        Axis<?, ?> yAxis = ((CartesianSeries) this.k).getYAxis();
        if (xAxis == null || yAxis == null) {
            throw new IllegalStateException("Chart must have an X Axis and a Y Axis to draw a Series.");
        }
        NumberRange numberRange = xAxis.i;
        NumberRange numberRange2 = yAxis.i;
        if (!Range.a(numberRange) && !Range.a(numberRange2)) {
            this.j[0] = ((float) numberRange.b()) / 2.0f;
            this.j[1] = ((float) numberRange2.b()) / 2.0f;
            this.j[2] = (float) numberRange.a;
            this.j[3] = (float) numberRange2.a;
            a(sChartGLDrawer, this.j, aiVar.c().density);
        }
    }
}

package com.shinobicontrols.charts;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class ao {
    NumberRange a = new NumberRange();
    NumberRange b = new NumberRange();
    InternalDataPoint[] c = new InternalDataPoint[0];

    /* JADX INFO: Access modifiers changed from: package-private */
    public Object[] a(DataAdapter<?, ?> dataAdapter) {
        Object[] array = dataAdapter.getDataPointsForDisplay().toArray();
        this.a = new NumberRange();
        this.b = new NumberRange();
        return array;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a(int i) {
        if (this.c == null || this.c.length != i) {
            this.c = new InternalDataPoint[i];
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int a() {
        return this.c.length;
    }
}

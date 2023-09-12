package com.shinobicontrols.charts;

import com.shinobicontrols.charts.Series;
/* loaded from: classes.dex */
abstract class n<T extends Series<?>> implements cu {
    protected final T k;
    protected ao l;
    protected final float[] j = {1.0f, 1.0f, 0.0f, 0.0f};
    private boolean a = true;

    /* JADX INFO: Access modifiers changed from: package-private */
    public n(T t) {
        this.k = t;
    }

    @Override // com.shinobicontrols.charts.cu
    public void a() {
        this.a = true;
    }

    @Override // com.shinobicontrols.charts.cu
    public boolean b() {
        return this.a;
    }

    @Override // com.shinobicontrols.charts.cu
    public void c() {
        this.a = false;
    }
}

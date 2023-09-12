package com.shinobicontrols.charts;

import android.util.DisplayMetrics;
/* loaded from: classes.dex */
class ai extends aj<an> implements an {
    private float a = 1.0f;
    private float b = 1.0f;
    private DisplayMetrics c;

    /* JADX INFO: Access modifiers changed from: package-private */
    public float a() {
        return this.a;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public float b() {
        return this.b;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public DisplayMetrics c() {
        return this.c;
    }

    @Override // com.shinobicontrols.charts.an
    public void a(float f, float f2, DisplayMetrics displayMetrics) {
        this.b = f;
        this.a = f2;
        this.c = displayMetrics;
        d();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.shinobicontrols.charts.aj
    public void a(an anVar) {
        anVar.a(this.b, this.a, this.c);
    }
}

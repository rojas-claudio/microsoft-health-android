package com.shinobicontrols.charts;

import android.graphics.Rect;
/* loaded from: classes.dex */
class as {
    final Rect a = new Rect();
    final Rect b = new Rect();
    private final Rect c = new Rect();

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a() {
        this.a.set(this.c);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Rect b() {
        this.b.setEmpty();
        return this.b;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void c() {
        this.a.set(this.b);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a(int i, int i2, int i3, int i4) {
        this.c.set(i, i2, i3, i4);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a(int i) {
        this.c.top += i;
    }
}

package com.shinobicontrols.charts;

import android.view.ViewConfiguration;
/* loaded from: classes.dex */
class y {
    final int a;
    final int b;
    final int c;
    final int d;
    final int e;
    final int f;
    private final ViewConfiguration g;
    private final v h;

    /* JADX INFO: Access modifiers changed from: package-private */
    public y(v vVar) {
        this.h = vVar;
        this.g = ViewConfiguration.get(vVar.getContext());
        float f = vVar.getResources().getDisplayMetrics().density;
        this.a = this.g.getScaledTouchSlop();
        this.b = at.a(f, 5.0f);
        this.c = this.g.getScaledMinimumFlingVelocity();
        this.d = ViewConfiguration.getDoubleTapTimeout();
        this.e = ViewConfiguration.getLongPressTimeout();
        this.f = at.a(f, 20.0f);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int a() {
        return this.h.r() ? this.a : this.b;
    }
}

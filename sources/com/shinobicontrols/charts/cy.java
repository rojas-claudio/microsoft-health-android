package com.shinobicontrols.charts;

import android.view.MotionEvent;
import com.shinobicontrols.charts.ShinobiChart;
/* loaded from: classes.dex */
class cy {
    private final dk a = new dk();
    private final cd b = new cd(2, this.a);
    private final cz c;
    private final af d;
    private final y e;

    /* JADX INFO: Access modifiers changed from: package-private */
    public cy(v vVar, ShinobiChart.OnGestureListener onGestureListener) {
        this.e = new y(vVar);
        this.c = new cz(vVar, onGestureListener, this.e);
        this.d = new af(vVar, onGestureListener, this.e);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean a(MotionEvent motionEvent) {
        boolean z = true;
        if (motionEvent.getActionMasked() == 3) {
            this.b.a();
        } else {
            this.a.a().addMovement(motionEvent);
            cb a = this.b.a(0);
            cb a2 = this.b.a(1);
            int b = this.b.b();
            this.b.a(motionEvent);
            if (a == null) {
                a = this.b.a(0);
            }
            if (a2 == null) {
                a2 = this.b.a(1);
            }
            int b2 = this.b.b();
            z = this.c.a(b, b2, a) || this.d.a(b, b2, a, a2);
        }
        if (this.b.b() == 0) {
            this.a.c();
        }
        return z;
    }
}

package com.shinobicontrols.charts;

import android.graphics.PointF;
import android.os.SystemClock;
/* loaded from: classes.dex */
class cc {
    final PointF a;
    final long b = SystemClock.uptimeMillis();

    /* JADX INFO: Access modifiers changed from: package-private */
    public cc(float f, float f2) {
        this.a = new PointF(f, f2);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o instanceof cc) {
            cc ccVar = (cc) o;
            return this.b == ccVar.b && this.a.equals(ccVar);
        }
        return false;
    }

    public int hashCode() {
        return ((((int) (this.b ^ (this.b >>> 32))) + 527) * 31) + this.a.hashCode();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public long a(cc ccVar) {
        return ccVar.b - this.b;
    }
}

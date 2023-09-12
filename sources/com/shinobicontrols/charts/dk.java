package com.shinobicontrols.charts;

import android.view.VelocityTracker;
/* loaded from: classes.dex */
class dk {
    private VelocityTracker a;

    /* JADX INFO: Access modifiers changed from: package-private */
    public VelocityTracker a() {
        if (this.a == null) {
            this.a = VelocityTracker.obtain();
        }
        return this.a;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public VelocityTracker b() {
        return this.a;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void c() {
        if (this.a != null) {
            this.a.recycle();
            this.a = null;
        }
    }
}

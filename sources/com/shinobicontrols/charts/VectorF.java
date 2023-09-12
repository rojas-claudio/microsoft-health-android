package com.shinobicontrols.charts;

import android.graphics.PointF;
/* loaded from: classes.dex */
class VectorF extends PointF {
    /* JADX INFO: Access modifiers changed from: package-private */
    public VectorF(float x, float y) {
        super(x, y);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static VectorF a(PointF pointF, PointF pointF2) {
        return new VectorF(pointF2.x - pointF.x, pointF2.y - pointF.y);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public float a() {
        return Math.max(Math.abs(this.x), Math.abs(this.y));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public VectorF b() {
        return new VectorF(Math.abs(this.x), Math.abs(this.y));
    }
}

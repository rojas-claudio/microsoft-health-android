package com.shinobicontrols.charts;

import android.graphics.PointF;
import com.shinobicontrols.charts.ShinobiChart;
/* loaded from: classes.dex */
class af {
    private final ShinobiChart.OnGestureListener a;
    private final v b;
    private final y c;
    private VectorF d = new VectorF(1.0f, 1.0f);
    private boolean e = false;

    /* JADX INFO: Access modifiers changed from: package-private */
    public af(v vVar, ShinobiChart.OnGestureListener onGestureListener, y yVar) {
        this.b = vVar;
        this.a = onGestureListener;
        this.c = yVar;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean a(int i, int i2, cb cbVar, cb cbVar2) {
        if (i == 1 && i2 == 2) {
            a(cbVar, cbVar2);
            return true;
        } else if (i == 2 && i2 == 2) {
            b(cbVar, cbVar2);
            return true;
        } else if (i == 2 && i2 == 1) {
            c(cbVar, cbVar2);
            return true;
        } else {
            return false;
        }
    }

    private void a(cb cbVar, cb cbVar2) {
        this.b.a(cbVar.e().a, cbVar2.e().a);
        this.a.onSecondTouchDown(this.b, cbVar.e().a, cbVar2.e().a);
    }

    private void b(cb cbVar, cb cbVar2) {
        boolean z = this.e;
        this.e = a(cbVar.d().a(), cbVar2.d().a());
        if (this.e) {
            PointF pointF = z ? cbVar.c().a : cbVar.e().a;
            PointF pointF2 = z ? cbVar2.c().a : cbVar2.e().a;
            PointF a = a(pointF, pointF2);
            cbVar.i();
            cbVar2.i();
            PointF a2 = a(cbVar.h().a, cbVar2.h().a);
            this.d = a(VectorF.a(pointF, pointF2).b(), VectorF.a(cbVar.h().a, cbVar2.h().a).b());
            this.b.a(a, a2, this.d);
            this.a.onPinch(this.b, a, a2, this.d);
        }
    }

    private boolean a(float f, float f2) {
        return this.e || f > ((float) this.c.b) || f2 > ((float) this.c.b);
    }

    private PointF a(PointF pointF, PointF pointF2) {
        return new PointF((pointF.x + pointF2.x) / 2.0f, (pointF.y + pointF2.y) / 2.0f);
    }

    private VectorF a(VectorF vectorF, VectorF vectorF2) {
        float f = 1.0f;
        float f2 = (vectorF.x <= ((float) this.c.f) || vectorF2.x <= ((float) this.c.f)) ? 1.0f : vectorF2.x / vectorF.x;
        if (vectorF.y > this.c.f && vectorF2.y > this.c.f) {
            f = vectorF2.y / vectorF.y;
        }
        return new VectorF(f2, f);
    }

    private void c(cb cbVar, cb cbVar2) {
        boolean z = false;
        this.e = false;
        int i = this.c.b;
        if (cbVar.a(i) || cbVar2.a(i)) {
            z = (cbVar.g().a() > ((float) this.c.c) || cbVar2.g().a() > ((float) this.c.c)) ? true : true;
            PointF a = a(cbVar.h().a, cbVar2.h().a);
            this.b.a(a, z, this.d);
            this.a.onPinchEnd(this.b, a, z, this.d);
        } else {
            this.b.b(cbVar.h().a, cbVar2.h().a);
            this.a.onSecondTouchUp(this.b, cbVar.h().a, cbVar2.h().a);
        }
        cbVar.a();
        cbVar2.a();
    }
}

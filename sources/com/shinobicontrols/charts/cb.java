package com.shinobicontrols.charts;

import android.view.VelocityTracker;
import java.util.ArrayList;
/* loaded from: classes.dex */
class cb {
    private final int a;
    private int b;
    private final dk c;
    private final ArrayList<cc> d = new ArrayList<>();
    private boolean e;
    private cc f;

    /* JADX INFO: Access modifiers changed from: package-private */
    public cb(int i, dk dkVar) {
        this.a = i;
        this.c = dkVar;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a() {
        this.b = 0;
        this.d.clear();
        if (this.c.b() != null) {
            this.c.b().clear();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void b() {
        this.f = null;
        a();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public cc c() {
        return a(this.b);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public VectorF d() {
        return a(this.b, this.d.size() - 1);
    }

    VectorF a(int i, int i2) {
        return (i >= i2 || i < 0 || this.d.size() <= i2) ? new VectorF(0.0f, 0.0f) : VectorF.a(this.d.get(i).a, this.d.get(i2).a);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public cc e() {
        return a(0);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public long f() {
        if (this.f == null) {
            return 0L;
        }
        return this.f.a(e());
    }

    cc a(int i) {
        return this.d.get(i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public VectorF g() {
        float f;
        float f2 = 0.0f;
        VelocityTracker b = this.c.b();
        if (b != null) {
            b.computeCurrentVelocity(1000);
            f = b.getXVelocity(this.a);
            f2 = b.getYVelocity(this.a);
        } else {
            f = 0.0f;
        }
        return new VectorF(f, f2);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void b(int i) {
        switch (i) {
            case 0:
            case 5:
                this.e = true;
                return;
            case 1:
            case 3:
            case 6:
                this.f = h();
                this.e = false;
                return;
            case 2:
            case 4:
            default:
                return;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean a(cc ccVar) {
        if (this.d.isEmpty() || !ccVar.equals(h())) {
            this.d.add(ccVar);
            return true;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean a(float f) {
        float f2 = 0.0f;
        float f3 = 0.0f;
        for (int i = 0; i < this.d.size() - 2; i++) {
            VectorF b = a(i, i + 1).b();
            f3 += b.x;
            f2 += b.y;
            if (f3 > f || f2 > f) {
                return true;
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public cc h() {
        if (this.d.isEmpty()) {
            return null;
        }
        return a(this.d.size() - 1);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void i() {
        this.b = this.d.size() - 1;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean j() {
        return this.e;
    }
}

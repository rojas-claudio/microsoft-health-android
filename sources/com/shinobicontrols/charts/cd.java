package com.shinobicontrols.charts;

import android.util.SparseArray;
import android.view.MotionEvent;
import java.util.ArrayList;
/* loaded from: classes.dex */
class cd {
    private final SparseArray<cb> a;
    private final ArrayList<cb> b;
    private final dk c;

    /* JADX INFO: Access modifiers changed from: package-private */
    public cd(int i, dk dkVar) {
        this.a = new SparseArray<>(i);
        this.b = new ArrayList<>(i);
        this.c = dkVar;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public cb a(int i) {
        if (i < 0 || i >= this.b.size()) {
            return null;
        }
        return this.b.get(i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a(MotionEvent motionEvent) {
        b(motionEvent);
        c(motionEvent);
    }

    private void b(MotionEvent motionEvent) {
        int pointerCount = motionEvent.getPointerCount();
        for (int i = 0; i < pointerCount; i++) {
            cb a = a(i, motionEvent);
            a.a(new cc(motionEvent.getX(i), motionEvent.getY(i)));
            if (!this.b.contains(a)) {
                this.b.add(a);
            }
        }
    }

    private cb a(int i, MotionEvent motionEvent) {
        int pointerId = motionEvent.getPointerId(i);
        cb cbVar = this.a.get(pointerId);
        if (cbVar == null) {
            cb cbVar2 = new cb(pointerId, this.c);
            this.a.put(pointerId, cbVar2);
            return cbVar2;
        }
        return cbVar;
    }

    private void c(MotionEvent motionEvent) {
        cb cbVar = this.a.get(motionEvent.getPointerId(motionEvent.getActionIndex()));
        cbVar.b(motionEvent.getActionMasked());
        if (!cbVar.j()) {
            this.b.remove(cbVar);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a() {
        int size = this.a.size();
        for (int i = 0; i < size; i++) {
            cb valueAt = this.a.valueAt(i);
            if (valueAt != null) {
                valueAt.b();
            }
        }
        this.b.clear();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int b() {
        return this.b.size();
    }
}

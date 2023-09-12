package com.shinobicontrols.charts;

import java.util.ArrayList;
import java.util.List;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class h {
    Axis<?, ?>[] a = new Axis[0];
    private final String b;

    /* JADX INFO: Access modifiers changed from: package-private */
    public h(String str) {
        this.b = str;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Axis<?, ?> a() {
        if (this.a.length == 0) {
            return null;
        }
        return this.a[0];
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean b() {
        return this.a.length > 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a(Axis<?, ?> axis) {
        ArrayList arrayList = new ArrayList();
        arrayList.add(axis);
        a(arrayList, axis);
        this.a = new Axis[0];
        this.a = (Axis[]) arrayList.toArray(this.a);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void b(Axis<?, ?> axis) {
        ArrayList arrayList = new ArrayList();
        a(arrayList, axis);
        arrayList.add(axis);
        this.a = new Axis[0];
        this.a = (Axis[]) arrayList.toArray(this.a);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void c(Axis<?, ?> axis) {
        ArrayList arrayList = new ArrayList();
        a(arrayList, axis);
        this.a = new Axis[0];
        this.a = (Axis[]) arrayList.toArray(this.a);
    }

    private void a(List<Axis<?, ?>> list, Axis<?, ?> axis) {
        for (int i = 0; i < this.a.length; i++) {
            if (axis != this.a[i]) {
                list.add(this.a[i]);
            }
        }
    }
}

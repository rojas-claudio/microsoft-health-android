package com.shinobicontrols.charts;
/* loaded from: classes.dex */
class dj<T> {
    T a;
    boolean b;

    /* JADX INFO: Access modifiers changed from: package-private */
    public dj(T t) {
        this.a = t;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a(T t) {
        this.a = t;
        this.b = true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void b(T t) {
        if (!this.b) {
            this.a = t;
        }
    }
}

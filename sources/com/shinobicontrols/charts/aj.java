package com.shinobicontrols.charts;

import java.util.ArrayList;
import java.util.List;
/* loaded from: classes.dex */
abstract class aj<T> {
    private final List<T> a = new ArrayList();

    abstract void a(T t);

    /* JADX INFO: Access modifiers changed from: package-private */
    public void d() {
        for (T t : this.a) {
            a(t);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void e() {
        this.a.clear();
    }
}

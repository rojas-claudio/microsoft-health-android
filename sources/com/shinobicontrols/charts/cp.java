package com.shinobicontrols.charts;

import java.util.ArrayList;
import java.util.List;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class cp {
    private final v a;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public enum a {
        SHOW,
        HIDE,
        REMOVE,
        ADD,
        STACK_ID
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public cp(v vVar) {
        this.a = vVar;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a(Series<?> series) {
        ArrayList arrayList = new ArrayList();
        arrayList.add(series);
        a(arrayList);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a(List<Series<?>> list) {
        a(list, a.HIDE);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void b(Series<?> series) {
        ArrayList arrayList = new ArrayList();
        arrayList.add(series);
        b(arrayList);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void b(List<Series<?>> list) {
        a(list, a.SHOW);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a(Series<?> series, v vVar) {
        ArrayList arrayList = new ArrayList();
        arrayList.add(series);
        a(arrayList, a.ADD);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void b(Series<?> series, v vVar) {
        ArrayList arrayList = new ArrayList();
        arrayList.add(series);
        a(arrayList, a.REMOVE);
    }

    private void a(List<Series<?>> list, a aVar) {
        ArrayList arrayList = new ArrayList();
        ArrayList<co> arrayList2 = new ArrayList();
        for (Series<?> series : list) {
            if (!series.isAnimating() && !arrayList.contains(series)) {
                co a2 = co.a(series, list, this.a, aVar);
                if (!a2.d()) {
                    a2.a();
                    arrayList2.add(a2);
                }
                arrayList.addAll(a2.c());
            }
        }
        for (co coVar : arrayList2) {
            coVar.b();
        }
    }
}

package com.shinobicontrols.charts;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public abstract class cj {
    private static cj a = new cj() { // from class: com.shinobicontrols.charts.cj.1
        @Override // com.shinobicontrols.charts.cj
        List<Series<?>> c(Series<?> series) {
            ArrayList arrayList = new ArrayList();
            arrayList.add(series);
            return arrayList;
        }
    };
    private static cj b = new cj() { // from class: com.shinobicontrols.charts.cj.2
        @Override // com.shinobicontrols.charts.cj
        List<Series<?>> c(Series<?> series) {
            ArrayList arrayList = new ArrayList();
            for (Series<?> series2 : series.t.c(series)) {
                arrayList.add(series2);
            }
            return arrayList;
        }
    };

    abstract List<Series<?>> c(Series<?> series);

    /* JADX INFO: Access modifiers changed from: package-private */
    public static List<Series<?>> a(Series<?> series) {
        return (series.t == null || !d(series)) ? a.c(series) : b.c(series);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static List<Series<?>> b(Series<?> series) {
        return a(a(series));
    }

    private cj() {
    }

    private static boolean d(Series<?> series) {
        return (series instanceof CartesianSeries) && ((CartesianSeries) series).c != null;
    }

    private static List<Series<?>> a(List<Series<?>> list) {
        Iterator<Series<?>> it = list.iterator();
        while (it.hasNext()) {
            if (e(it.next())) {
                it.remove();
            }
        }
        return list;
    }

    private static boolean e(Series<?> series) {
        return series.o == null || series.y || series.isAnimating();
    }
}

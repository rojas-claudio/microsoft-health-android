package com.shinobicontrols.charts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class cs {
    Map<Class<? extends Series>, a> a = new HashMap();

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Multi-variable type inference failed */
    public int a(Series<?> series) {
        a aVar;
        Class<?> cls = series.getClass();
        if (this.a.containsKey(cls)) {
            aVar = this.a.get(cls);
            if (aVar.a()) {
                b(aVar, series);
            } else {
                a(aVar, series);
            }
        } else {
            aVar = new a();
            this.a.put(cls, aVar);
            a(aVar, series);
        }
        return c(aVar, series);
    }

    private void a(a aVar, Series<?> series) {
        aVar.add(series);
    }

    private void b(a aVar, Series<?> series) {
        aVar.a(null, series);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void b(Series<?> series) {
        Class<?> cls = series.getClass();
        if (this.a.containsKey(cls)) {
            a aVar = this.a.get(cls);
            if (aVar.contains(series)) {
                aVar.a(series);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int c(Series<?> series) {
        Class<?> cls = series.getClass();
        if (this.a.containsKey(cls)) {
            return c(this.a.get(cls), series);
        }
        return -1;
    }

    private int c(a aVar, Series<?> series) {
        return aVar.indexOf(series);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class a extends ArrayList<Series<?>> {
        private static final long serialVersionUID = -6971678076805971571L;

        a() {
        }

        void a(Series<?> series, Series<?> series2) {
            int indexOf = indexOf(series);
            if (indexOf != -1) {
                remove(series);
                add(indexOf, series2);
            }
        }

        void a(Series<?> series) {
            a(series, null);
        }

        boolean a() {
            return indexOf(null) != -1;
        }
    }
}

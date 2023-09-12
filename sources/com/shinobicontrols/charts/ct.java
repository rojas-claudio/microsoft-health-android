package com.shinobicontrols.charts;

import com.shinobicontrols.charts.cp;
import java.util.List;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public abstract class ct {
    List<Series<?>> a;
    List<Series<?>> b;

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract void a();

    /* JADX INFO: Access modifiers changed from: package-private */
    public static ct a(List<Series<?>> list, List<Series<?>> list2, v vVar, cp.a aVar) {
        switch (aVar) {
            case HIDE:
                return new b(list, list2);
            case SHOW:
                return new d(list, list2);
            case ADD:
                return new a(list, list2, vVar);
            case REMOVE:
                return new c(list, list2, vVar);
            default:
                return null;
        }
    }

    public ct(List<Series<?>> list, List<Series<?>> list2) {
        this.a = list;
        this.b = list2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public List<Series<?>> b() {
        return this.b;
    }

    /* loaded from: classes.dex */
    private static class b extends ct {
        public b(List<Series<?>> list, List<Series<?>> list2) {
            super(list, list2);
        }

        @Override // com.shinobicontrols.charts.ct
        void a() {
            this.b.removeAll(this.a);
        }
    }

    /* loaded from: classes.dex */
    private static class d extends ct {
        public d(List<Series<?>> list, List<Series<?>> list2) {
            super(list, list2);
        }

        @Override // com.shinobicontrols.charts.ct
        void a() {
            this.b.addAll(this.a);
        }
    }

    /* loaded from: classes.dex */
    private static class c extends ct {
        private final v c;

        public c(List<Series<?>> list, List<Series<?>> list2, v vVar) {
            super(list, list2);
            this.c = vVar;
        }

        @Override // com.shinobicontrols.charts.ct
        void a() {
            this.b.removeAll(this.a);
            synchronized (x.a) {
                for (Series<?> series : this.a) {
                    this.c.b(series);
                }
            }
        }
    }

    /* loaded from: classes.dex */
    private static class a extends ct {
        private final v c;

        public a(List<Series<?>> list, List<Series<?>> list2, v vVar) {
            super(list, list2);
            this.c = vVar;
        }

        @Override // com.shinobicontrols.charts.ct
        void a() {
            synchronized (x.a) {
                for (Series<?> series : this.a) {
                    if (!series.y) {
                        this.b.add(series);
                    }
                    this.c.a(series);
                }
            }
        }
    }
}

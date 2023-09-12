package com.shinobicontrols.charts;

import com.shinobicontrols.charts.b;
import com.shinobicontrols.charts.cp;
import java.util.ArrayList;
import java.util.List;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class co {
    private final com.shinobicontrols.charts.b a = new com.shinobicontrols.charts.b();
    private final com.shinobicontrols.charts.c b = new com.shinobicontrols.charts.c();
    private final List<Series<?>> c;
    private final List<Series<?>> d;
    private final List<Series<?>> e;
    private final ct f;
    private final v g;

    /* JADX INFO: Access modifiers changed from: package-private */
    public static co a(Series<?> series, List<Series<?>> list, v vVar, cp.a aVar) {
        List<Series<?>> a2 = cj.a(series);
        List<Series<?>> a3 = a(a2, list);
        List<Series<?>> b2 = cj.b(series);
        return new co(a2, a3, b2, ct.a(a3, b2, vVar, aVar), vVar);
    }

    private static List<Series<?>> a(List<Series<?>> list, List<Series<?>> list2) {
        ArrayList arrayList = new ArrayList(list2);
        arrayList.retainAll(list);
        return arrayList;
    }

    private co(List<Series<?>> list, List<Series<?>> list2, List<Series<?>> list3, ct ctVar, v vVar) {
        this.c = list;
        this.d = list2;
        this.e = list3;
        this.f = ctVar;
        this.g = vVar;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a() {
        for (Series<?> series : this.e) {
            series.w.a(true);
            series.u = series.w;
            series.p.a();
            this.b.a(series.u);
        }
        this.a.a(this.b);
        this.a.a(new a(this.g, this.d, this.f));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void b() {
        this.a.a();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public List<Series<?>> c() {
        return this.d;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean d() {
        for (Series<?> series : this.c) {
            if (series.isAnimating()) {
                return true;
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static abstract class b implements b.a {
        final v a;

        public b(v vVar) {
            this.a = vVar;
        }

        @Override // com.shinobicontrols.charts.b.a
        public void a() {
        }

        @Override // com.shinobicontrols.charts.b.a
        public void a(Animation animation) {
            synchronized (x.a) {
                this.a.b.f();
            }
        }

        @Override // com.shinobicontrols.charts.b.a
        public void b() {
        }

        @Override // com.shinobicontrols.charts.b.a
        public void c() {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class a extends b {
        private final List<Series<?>> b;
        private final ct c;

        public a(v vVar, List<Series<?>> list, ct ctVar) {
            super(vVar);
            this.b = list;
            this.c = ctVar;
        }

        @Override // com.shinobicontrols.charts.co.b, com.shinobicontrols.charts.b.a
        public void a() {
            for (Series<?> series : this.c.b()) {
                series.z.a();
            }
        }

        @Override // com.shinobicontrols.charts.co.b, com.shinobicontrols.charts.b.a
        public void b() {
            synchronized (x.a) {
                for (Series<?> series : this.c.b()) {
                    series.u = null;
                    series.a(true);
                }
            }
            this.c.a();
            d();
        }

        private void d() {
            com.shinobicontrols.charts.b bVar = new com.shinobicontrols.charts.b();
            com.shinobicontrols.charts.c cVar = new com.shinobicontrols.charts.c();
            List<Series<?>> b = this.c.b();
            for (Series<?> series : b) {
                series.v.a(false);
                series.u = series.v;
                synchronized (x.a) {
                    series.a(false);
                }
                cVar.a(series.u);
            }
            bVar.a(cVar);
            bVar.a(new c(this.a, this.b, b));
            bVar.a();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class c extends b {
        private final List<Series<?>> b;
        private final List<Series<?>> c;

        public c(v vVar, List<Series<?>> list, List<Series<?>> list2) {
            super(vVar);
            this.b = list;
            this.c = list2;
        }

        @Override // com.shinobicontrols.charts.co.b, com.shinobicontrols.charts.b.a
        public void b() {
            synchronized (x.a) {
                for (Series<?> series : this.c) {
                    series.u = null;
                    series.p.a();
                    series.z.a();
                }
            }
            for (Series<?> series2 : this.b) {
                this.a.c(series2);
            }
        }
    }
}

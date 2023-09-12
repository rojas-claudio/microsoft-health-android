package com.shinobicontrols.charts;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public abstract class ae {
    final Series<?> a;

    public abstract void a();

    public static ae a(Series<?> series) {
        return series instanceof PieDonutSeries ? new b(series) : new a(series);
    }

    public ae(Series<?> series) {
        this.a = series;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class a extends ae {
        @Override // com.shinobicontrols.charts.ae
        public void a() {
        }

        public a(Series<?> series) {
            super(series);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class b extends ae {
        @Override // com.shinobicontrols.charts.ae
        public void a() {
            this.a.o.b.invalidate();
        }

        public b(Series<?> series) {
            super(series);
        }
    }
}

package com.shinobicontrols.charts;
/* loaded from: classes.dex */
abstract class aa {
    boolean a;
    final boolean b;

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract DataPoint<?, ?> a(DataPoint<?, ?> dataPoint, DataPoint<?, ?> dataPoint2, DataPoint<?, ?> dataPoint3, boolean z);

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract DataPoint<?, ?> b(DataPoint<?, ?> dataPoint, DataPoint<?, ?> dataPoint2, DataPoint<?, ?> dataPoint3, boolean z);

    private aa(boolean z) {
        this.a = false;
        this.b = z;
    }

    /* loaded from: classes.dex */
    static class c extends aa {
        /* JADX INFO: Access modifiers changed from: package-private */
        public c() {
            super(true);
        }

        @Override // com.shinobicontrols.charts.aa
        DataPoint<?, ?> a(DataPoint<?, ?> dataPoint, DataPoint<?, ?> dataPoint2, DataPoint<?, ?> dataPoint3, boolean z) {
            return z ? dataPoint3 : dataPoint2;
        }

        @Override // com.shinobicontrols.charts.aa
        DataPoint<?, ?> b(DataPoint<?, ?> dataPoint, DataPoint<?, ?> dataPoint2, DataPoint<?, ?> dataPoint3, boolean z) {
            return z ? dataPoint3 : dataPoint2;
        }
    }

    /* loaded from: classes.dex */
    static class a extends aa {
        /* JADX INFO: Access modifiers changed from: package-private */
        public a() {
            super(false);
        }

        @Override // com.shinobicontrols.charts.aa
        DataPoint<?, ?> a(DataPoint<?, ?> dataPoint, DataPoint<?, ?> dataPoint2, DataPoint<?, ?> dataPoint3, boolean z) {
            return dataPoint2;
        }

        @Override // com.shinobicontrols.charts.aa
        DataPoint<?, ?> b(DataPoint<?, ?> dataPoint, DataPoint<?, ?> dataPoint2, DataPoint<?, ?> dataPoint3, boolean z) {
            return new DataPoint<>(dataPoint2.getX(), dataPoint.getY());
        }
    }

    /* loaded from: classes.dex */
    static class b extends aa {
        /* JADX INFO: Access modifiers changed from: package-private */
        public b() {
            super(false);
        }

        @Override // com.shinobicontrols.charts.aa
        DataPoint<?, ?> a(DataPoint<?, ?> dataPoint, DataPoint<?, ?> dataPoint2, DataPoint<?, ?> dataPoint3, boolean z) {
            return dataPoint2;
        }

        @Override // com.shinobicontrols.charts.aa
        DataPoint<?, ?> b(DataPoint<?, ?> dataPoint, DataPoint<?, ?> dataPoint2, DataPoint<?, ?> dataPoint3, boolean z) {
            return new DataPoint<>(dataPoint.getX(), dataPoint2.getY());
        }
    }

    /* loaded from: classes.dex */
    static class d extends aa {
        /* JADX INFO: Access modifiers changed from: package-private */
        public d() {
            super(false);
        }

        @Override // com.shinobicontrols.charts.aa
        DataPoint<?, ?> a(DataPoint<?, ?> dataPoint, DataPoint<?, ?> dataPoint2, DataPoint<?, ?> dataPoint3, boolean z) {
            return dataPoint2;
        }

        @Override // com.shinobicontrols.charts.aa
        DataPoint<?, ?> b(DataPoint<?, ?> dataPoint, DataPoint<?, ?> dataPoint2, DataPoint<?, ?> dataPoint3, boolean z) {
            return dataPoint;
        }
    }
}

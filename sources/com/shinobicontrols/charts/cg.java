package com.shinobicontrols.charts;

import com.shinobicontrols.charts.PieDonutSeries;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public abstract class cg {
    static cg a = new cg() { // from class: com.shinobicontrols.charts.cg.1
        @Override // com.shinobicontrols.charts.cg
        float a(float f, float f2) {
            return bk.a(f + f2);
        }

        @Override // com.shinobicontrols.charts.cg
        float b(float f, float f2) {
            return bk.a(f + f2);
        }

        @Override // com.shinobicontrols.charts.cg
        float a(double d, double d2) {
            return b(d, d2);
        }

        @Override // com.shinobicontrols.charts.cg
        float a(float f) {
            return f;
        }
    };
    static cg b = new cg() { // from class: com.shinobicontrols.charts.cg.2
        @Override // com.shinobicontrols.charts.cg
        float a(float f, float f2) {
            return bk.a(f2 - f);
        }

        @Override // com.shinobicontrols.charts.cg
        float b(float f, float f2) {
            return bk.a(f2 - f);
        }

        @Override // com.shinobicontrols.charts.cg
        float a(double d, double d2) {
            return 6.2831855f - b(d, d2);
        }

        @Override // com.shinobicontrols.charts.cg
        float a(float f) {
            return (float) (6.283185307179586d - f);
        }
    };

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract float a(double d, double d2);

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract float a(float f);

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract float a(float f, float f2);

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract float b(float f, float f2);

    cg() {
    }

    float b(double d, double d2) {
        return bk.a((float) (Math.atan2(d2, d) + 1.5707963267948966d));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static cg a(PieDonutSeries.DrawDirection drawDirection) {
        return drawDirection == PieDonutSeries.DrawDirection.ANTICLOCKWISE ? a : b;
    }
}

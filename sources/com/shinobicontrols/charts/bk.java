package com.shinobicontrols.charts;
/* loaded from: classes.dex */
final class bk {
    static float a(float f, float f2, float f3) {
        float f4 = f;
        while (f4 < f2) {
            f4 += 6.2831855f;
        }
        while (f4 > f3) {
            f4 -= 6.2831855f;
        }
        return f4;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static float a(float f) {
        return a(f, 0.0f, 6.2831855f);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static float b(float f) {
        return a(f, -3.1415927f, 3.1415927f);
    }
}

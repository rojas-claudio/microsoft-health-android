package com.shinobicontrols.charts;

import com.shinobicontrols.charts.Axis;
import com.shinobicontrols.charts.TickMark;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class df {
    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean a(TickMark tickMark, Axis.c cVar, Axis<?, ?> axis, double d) {
        switch (cVar.F) {
            case TICKS_AND_LABELS_PERSIST:
                return d > ((double) cVar.C);
            case NEITHER_PERSIST:
            case TICKS_PERSIST:
                return (0.5d * ((double) cVar.E)) + d > ((double) cVar.C);
            default:
                return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean b(TickMark tickMark, Axis.c cVar, Axis<?, ?> axis, double d) {
        switch (cVar.G) {
            case TICKS_AND_LABELS_PERSIST:
                return d < ((double) cVar.D);
            case NEITHER_PERSIST:
            case TICKS_PERSIST:
                return d - (0.5d * ((double) cVar.E)) < ((double) cVar.D);
            default:
                return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean a(TickMark tickMark, Axis.c cVar, Axis<?, ?> axis, boolean z, boolean z2) {
        if (z2 && !z) {
            return cVar.G != TickMark.ClippingMode.TICKS_PERSIST;
        } else if (!z || z2) {
            return false;
        } else {
            return cVar.F != TickMark.ClippingMode.TICKS_PERSIST;
        }
    }
}

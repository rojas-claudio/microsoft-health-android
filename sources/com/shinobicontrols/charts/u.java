package com.shinobicontrols.charts;

import com.microsoft.kapp.utils.Constants;
import com.shinobicontrols.charts.Axis;
/* loaded from: classes.dex */
class u extends df {
    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.shinobicontrols.charts.df
    public boolean a(TickMark tickMark, Axis.c cVar, Axis<?, ?> axis, double d) {
        return a(tickMark.a, axis.j.b, Constants.SPLITS_ACCURACY) || super.a(tickMark, cVar, axis, d);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.shinobicontrols.charts.df
    public boolean b(TickMark tickMark, Axis.c cVar, Axis<?, ?> axis, double d) {
        return a(tickMark.a, axis.j.b, Constants.SPLITS_ACCURACY) || super.b(tickMark, cVar, axis, d);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.shinobicontrols.charts.df
    public boolean a(TickMark tickMark, Axis.c cVar, Axis<?, ?> axis, boolean z, boolean z2) {
        return a(tickMark.a, axis.j.b, Constants.SPLITS_ACCURACY) || super.a(tickMark, cVar, axis, z, z2);
    }

    private boolean a(double d, double d2, double d3) {
        return d > d2 + d3;
    }
}

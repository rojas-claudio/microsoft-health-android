package com.shinobicontrols.charts;

import com.microsoft.kapp.diagnostics.TelemetryConstants;
/* loaded from: classes.dex */
class br extends cq {
    /* JADX INFO: Access modifiers changed from: package-private */
    public br(OHLCSeries oHLCSeries) {
        super(false, oHLCSeries);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.shinobicontrols.charts.cq
    public void a() {
        int i = 0;
        bs bsVar = (bs) this.a.p;
        bsVar.a(this.a.n.a());
        int i2 = 0;
        while (i2 < this.a.n.c.length) {
            InternalDataPoint internalDataPoint = this.a.n.c[i2];
            int i3 = i + 1;
            bsVar.a[i] = (float) internalDataPoint.c;
            int i4 = i3 + 1;
            bsVar.a[i3] = internalDataPoint.j.get("High").floatValue();
            int i5 = i4 + 1;
            bsVar.a[i4] = internalDataPoint.j.get(TelemetryConstants.Events.LogHomeTileTap.Dimensions.ACTION_OPEN).floatValue();
            int i6 = i5 + 1;
            bsVar.a[i5] = internalDataPoint.j.get(TelemetryConstants.Events.LogHomeTileTap.Dimensions.ACTION_CLOSE).floatValue();
            bsVar.a[i6] = internalDataPoint.j.get("Low").floatValue();
            internalDataPoint.d = (internalDataPoint.j.get("High").doubleValue() + internalDataPoint.j.get("Low").floatValue()) / 2.0d;
            i2++;
            i = i6 + 1;
        }
    }
}

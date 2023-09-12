package com.shinobicontrols.charts;

import com.microsoft.kapp.diagnostics.TelemetryConstants;
import com.microsoft.kapp.utils.Constants;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class InternalDataPoint {
    static a k = new a();
    static b l = new b();
    double a;
    double b;
    double c;
    double d;
    int e = 0;
    double f = Constants.SPLITS_ACCURACY;
    double g = Constants.SPLITS_ACCURACY;
    boolean h = false;
    int i;
    Map<String, Double> j;

    /* JADX INFO: Access modifiers changed from: package-private */
    public InternalDataPoint() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public InternalDataPoint(double x, double y) {
        this.a = x;
        this.b = y;
        this.c = x;
        this.d = y;
    }

    /* loaded from: classes.dex */
    static class a implements Comparator<InternalDataPoint> {
        a() {
        }

        @Override // java.util.Comparator
        /* renamed from: a */
        public int compare(InternalDataPoint internalDataPoint, InternalDataPoint internalDataPoint2) {
            return Double.compare(internalDataPoint.a, internalDataPoint2.a);
        }
    }

    /* loaded from: classes.dex */
    static class b implements Comparator<InternalDataPoint> {
        b() {
        }

        @Override // java.util.Comparator
        /* renamed from: a */
        public int compare(InternalDataPoint internalDataPoint, InternalDataPoint internalDataPoint2) {
            return Double.compare(internalDataPoint.b, internalDataPoint2.b);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a(double d, double d2, double d3, double d4) {
        a(d3, d2);
        this.j.put(TelemetryConstants.Events.LogHomeTileTap.Dimensions.ACTION_OPEN, Double.valueOf(d));
        this.j.put(TelemetryConstants.Events.LogHomeTileTap.Dimensions.ACTION_CLOSE, Double.valueOf(d4));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a(double d, double d2) {
        if (this.j == null) {
            this.j = new HashMap();
        }
        this.j.put("Low", Double.valueOf(d));
        this.j.put("High", Double.valueOf(d2));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean a() {
        return this.j.get(TelemetryConstants.Events.LogHomeTileTap.Dimensions.ACTION_CLOSE).doubleValue() > this.j.get(TelemetryConstants.Events.LogHomeTileTap.Dimensions.ACTION_OPEN).doubleValue();
    }
}

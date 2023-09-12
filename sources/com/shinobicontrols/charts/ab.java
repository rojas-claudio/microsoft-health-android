package com.shinobicontrols.charts;

import com.microsoft.kapp.diagnostics.TelemetryConstants;
import com.shinobicontrols.charts.Series;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class ab {
    /* JADX INFO: Access modifiers changed from: package-private */
    public static DataPoint<?, ?> a(InternalDataPoint internalDataPoint, CartesianSeries<?> cartesianSeries) {
        return c(internalDataPoint, cartesianSeries) ? a(internalDataPoint.a, internalDataPoint, cartesianSeries) : a(internalDataPoint.a, internalDataPoint.b, internalDataPoint.h, cartesianSeries);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static DataPoint<?, ?> b(InternalDataPoint internalDataPoint, CartesianSeries<?> cartesianSeries) {
        return a(internalDataPoint.c, internalDataPoint.d, internalDataPoint.h, cartesianSeries);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static DataPoint<?, ?> a(bz bzVar, InternalDataPoint internalDataPoint, CartesianSeries<?> cartesianSeries) {
        return a(bzVar.b, bzVar.c, internalDataPoint.h, cartesianSeries);
    }

    private static boolean c(InternalDataPoint internalDataPoint, CartesianSeries<?> cartesianSeries) {
        return cartesianSeries.getDataAdapter().get(internalDataPoint.i) instanceof MultiValueData;
    }

    private static DataPoint<?, ?> a(double d, double d2, boolean z, CartesianSeries<?> cartesianSeries) {
        return new DataPoint<>(cartesianSeries.getXAxis().transformInternalValueToUser(d), cartesianSeries.getYAxis().transformInternalValueToUser(d2), z);
    }

    private static DataPoint<?, ?> a(double d, InternalDataPoint internalDataPoint, CartesianSeries<?> cartesianSeries) {
        Axis<?, ?> a = a(cartesianSeries);
        Axis<?, ?> b = b(cartesianSeries);
        Object transformInternalValueToUser = a.transformInternalValueToUser(d);
        Object transformInternalValueToUser2 = b.transformInternalValueToUser(internalDataPoint.j.get("Low").doubleValue());
        Object transformInternalValueToUser3 = b.transformInternalValueToUser(internalDataPoint.j.get("High").doubleValue());
        Double d2 = internalDataPoint.j.get(TelemetryConstants.Events.LogHomeTileTap.Dimensions.ACTION_OPEN);
        Double d3 = internalDataPoint.j.get(TelemetryConstants.Events.LogHomeTileTap.Dimensions.ACTION_CLOSE);
        return (d2 == null || d3 == null) ? new MultiValueDataPoint(transformInternalValueToUser, transformInternalValueToUser2, transformInternalValueToUser3, internalDataPoint.h) : new MultiValueDataPoint(transformInternalValueToUser, transformInternalValueToUser2, transformInternalValueToUser3, b.transformInternalValueToUser(d2.doubleValue()), b.transformInternalValueToUser(d3.doubleValue()), internalDataPoint.h);
    }

    private static Axis<?, ?> a(CartesianSeries<?> cartesianSeries) {
        return c(cartesianSeries) ? cartesianSeries.getYAxis() : cartesianSeries.getXAxis();
    }

    private static Axis<?, ?> b(CartesianSeries<?> cartesianSeries) {
        return c(cartesianSeries) ? cartesianSeries.getXAxis() : cartesianSeries.getYAxis();
    }

    private static boolean c(CartesianSeries<?> cartesianSeries) {
        return cartesianSeries.j == Series.Orientation.VERTICAL;
    }
}

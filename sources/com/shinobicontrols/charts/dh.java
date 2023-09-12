package com.shinobicontrols.charts;

import android.graphics.PointF;
import android.view.View;
import android.widget.TextView;
import com.shinobicontrols.charts.Series;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public abstract class dh {
    static dh f = new dh() { // from class: com.shinobicontrols.charts.dh.1
        @Override // com.shinobicontrols.charts.dh
        void a(Tooltip tooltip, DataPoint<?, ?> dataPoint) {
            DefaultTooltipView defaultTooltipView = (DefaultTooltipView) tooltip.getView();
            this.c = defaultTooltipView.getChildCount();
            CartesianSeries<?> trackedSeries = tooltip.getTrackedSeries();
            Axis<?, ?> xAxis = trackedSeries.getXAxis();
            Axis<?, ?> yAxis = trackedSeries.getYAxis();
            this.a.setLength(0);
            this.a.append(xAxis.a(xAxis.translatePoint(dataPoint.getX()))).append(", ").append(yAxis.a(yAxis.translatePoint(dataPoint.getY())));
            defaultTooltipView.a.setText(this.a.toString());
            defaultTooltipView.a.setVisibility(0);
            defaultTooltipView.b.setVisibility(8);
            defaultTooltipView.c.setVisibility(8);
            defaultTooltipView.d.setVisibility(8);
            defaultTooltipView.e.setVisibility(8);
            defaultTooltipView.f.setVisibility(8);
            defaultTooltipView.g.setVisibility(8);
            defaultTooltipView.h.setVisibility(8);
            defaultTooltipView.i.setVisibility(8);
        }
    };
    static dh g = new dh() { // from class: com.shinobicontrols.charts.dh.2
        @Override // com.shinobicontrols.charts.dh
        void a(Tooltip tooltip, DataPoint<?, ?> dataPoint) {
            DefaultTooltipView defaultTooltipView = (DefaultTooltipView) tooltip.getView();
            this.c = defaultTooltipView.getChildCount();
            a(new TextView[]{defaultTooltipView.a, defaultTooltipView.b, defaultTooltipView.f, defaultTooltipView.c, defaultTooltipView.g}, dataPoint, tooltip);
            defaultTooltipView.d.setVisibility(8);
            defaultTooltipView.e.setVisibility(8);
            defaultTooltipView.h.setVisibility(8);
            defaultTooltipView.i.setVisibility(8);
            a(defaultTooltipView, tooltip);
        }
    };
    static dh h = new dh() { // from class: com.shinobicontrols.charts.dh.3
        @Override // com.shinobicontrols.charts.dh
        void a(Tooltip tooltip, DataPoint<?, ?> dataPoint) {
            DefaultTooltipView defaultTooltipView = (DefaultTooltipView) tooltip.getView();
            this.c = defaultTooltipView.getChildCount();
            Axis c = dh.c(tooltip.getTrackedSeries());
            a(new TextView[]{defaultTooltipView.a, defaultTooltipView.c, defaultTooltipView.g, defaultTooltipView.d, defaultTooltipView.h}, dataPoint, tooltip);
            MultiValueData multiValueData = (MultiValueData) dataPoint;
            defaultTooltipView.b.setText("open : ");
            defaultTooltipView.f.setText(c.a(c.translatePoint(multiValueData.getOpen())));
            defaultTooltipView.b.setVisibility(0);
            defaultTooltipView.f.setVisibility(0);
            defaultTooltipView.e.setText("  close : ");
            defaultTooltipView.i.setText(c.a(c.translatePoint(multiValueData.getClose())));
            defaultTooltipView.e.setVisibility(0);
            defaultTooltipView.i.setVisibility(0);
            a(defaultTooltipView, tooltip);
        }
    };
    static dh i = new dh() { // from class: com.shinobicontrols.charts.dh.4
        @Override // com.shinobicontrols.charts.dh
        void a(Tooltip tooltip, DataPoint<?, ?> dataPoint) {
        }
    };
    final StringBuilder a = new StringBuilder();
    ar b = new ar();
    int c;
    int d;
    int e;

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract void a(Tooltip tooltip, DataPoint<?, ?> dataPoint);

    dh() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static dh a(Tooltip tooltip) {
        View view = tooltip.getView();
        CartesianSeries<?> trackedSeries = tooltip.getTrackedSeries();
        if (trackedSeries == null || !(view instanceof DefaultTooltipView)) {
            return i;
        }
        if ((trackedSeries instanceof CandlestickSeries) || (trackedSeries instanceof OHLCSeries)) {
            return h;
        }
        if (trackedSeries instanceof BandSeries) {
            return g;
        }
        return f;
    }

    private static Axis<?, ?> b(CartesianSeries<?> cartesianSeries) {
        return d(cartesianSeries) ? cartesianSeries.getYAxis() : cartesianSeries.getXAxis();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static Axis<?, ?> c(CartesianSeries<?> cartesianSeries) {
        return d(cartesianSeries) ? cartesianSeries.getXAxis() : cartesianSeries.getYAxis();
    }

    private static boolean d(CartesianSeries<?> cartesianSeries) {
        return cartesianSeries.j == Series.Orientation.VERTICAL;
    }

    void a(TextView[] textViewArr, DataPoint<?, ?> dataPoint, Tooltip tooltip) {
        CartesianSeries<?> trackedSeries = tooltip.getTrackedSeries();
        Axis<?, ?> c = c(trackedSeries);
        Axis<?, ?> b = b(trackedSeries);
        this.a.setLength(0);
        this.a.append("x : ").append(b.a(b.translatePoint(dataPoint.getX())));
        textViewArr[0].setText(this.a.toString());
        textViewArr[0].setVisibility(0);
        MultiValueData multiValueData = (MultiValueData) dataPoint;
        textViewArr[1].setText("high : ");
        textViewArr[2].setText(c.a(c.translatePoint(multiValueData.getHigh())));
        textViewArr[1].setVisibility(0);
        textViewArr[2].setVisibility(0);
        textViewArr[3].setText("low : ");
        textViewArr[4].setText(c.a(c.translatePoint(multiValueData.getLow())));
        textViewArr[3].setVisibility(0);
        textViewArr[4].setVisibility(0);
    }

    void a(DefaultTooltipView defaultTooltipView, Tooltip tooltip) {
        ShinobiChart chart = tooltip.getTrackedSeries().getChart();
        b(defaultTooltipView, a(chart.getCrosshair().getStyle(), chart));
        a(defaultTooltipView, a(defaultTooltipView, chart.getCrosshair().getStyle(), chart));
    }

    int a(CrosshairStyle crosshairStyle, ShinobiChart shinobiChart) {
        PointF pointF = new PointF(0.0f, 0.0f);
        this.b.a(pointF, "  close : ", crosshairStyle.getTooltipTextSize(), crosshairStyle.getTooltipTypeface(), (v) shinobiChart);
        this.d = (int) pointF.x;
        return this.d;
    }

    int a(DefaultTooltipView defaultTooltipView, CrosshairStyle crosshairStyle, ShinobiChart shinobiChart) {
        float f2 = 0.0f;
        PointF pointF = new PointF(0.0f, 0.0f);
        int i2 = 5;
        while (true) {
            int i3 = i2;
            float f3 = f2;
            if (i3 < this.c) {
                this.b.a(pointF, (String) ((TextView) defaultTooltipView.getChildAt(i3)).getText(), crosshairStyle.getTooltipTextSize(), crosshairStyle.getTooltipTypeface(), (v) shinobiChart);
                f2 = pointF.x > f3 ? pointF.x : f3;
                i2 = i3 + 1;
            } else {
                this.e = (int) f3;
                return this.e;
            }
        }
    }

    void a(DefaultTooltipView defaultTooltipView, int i2) {
        int i3 = 5;
        while (true) {
            int i4 = i3;
            if (i4 < this.c) {
                ((TextView) defaultTooltipView.getChildAt(i4)).setWidth(i2);
                i3 = i4 + 1;
            } else {
                return;
            }
        }
    }

    void b(DefaultTooltipView defaultTooltipView, int i2) {
        int i3 = 1;
        while (true) {
            int i4 = i3;
            if (i4 < 5) {
                ((TextView) defaultTooltipView.getChildAt(i4)).setWidth(i2);
                i3 = i4 + 1;
            } else {
                return;
            }
        }
    }
}

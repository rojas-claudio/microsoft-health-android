package com.shinobicontrols.charts;

import android.annotation.SuppressLint;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class au {
    private final v a;
    private final float b;

    /* JADX INFO: Access modifiers changed from: package-private */
    public au(ShinobiChart shinobiChart) {
        if (!(shinobiChart instanceof v)) {
            throw new IllegalStateException("Unable to retrieve LegendItems from Chart");
        }
        this.a = (v) shinobiChart;
        this.b = this.a.getResources().getDisplayMetrics().density;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public List<av> a(LegendStyle legendStyle) {
        return this.a.l() ? b(legendStyle) : c(legendStyle);
    }

    private List<av> b(LegendStyle legendStyle) {
        ArrayList arrayList = new ArrayList();
        if (this.a.getSeries().size() > 0) {
            Series<?> series = this.a.getSeries().get(0);
            if (series.isShownInLegend() && (series instanceof PieDonutSeries)) {
                PieDonutSeries pieDonutSeries = (PieDonutSeries) series;
                int length = pieDonutSeries.n.c.length;
                for (int i = 0; i < length; i++) {
                    arrayList.add(new av(a(pieDonutSeries.a(i), b(i)), new ay(this.a.getContext(), pieDonutSeries.a(i, this.b)), pieDonutSeries, i));
                }
            }
        }
        return Collections.unmodifiableList(arrayList);
    }

    private List<av> c(LegendStyle legendStyle) {
        ArrayList arrayList = new ArrayList();
        List<Series<?>> series = this.a.getSeries();
        int i = 0;
        while (true) {
            int i2 = i;
            if (i2 < series.size()) {
                Series<?> series2 = series.get(i2);
                if (series2.isShownInLegend()) {
                    arrayList.add(new av(a(series2.getTitle(), a(i2)), new ay(this.a.getContext(), series2.a(this.b)), series2, -1));
                }
                i = i2 + 1;
            } else {
                return Collections.unmodifiableList(arrayList);
            }
        }
    }

    @SuppressLint({"DefaultLocale"})
    private String a(int i) {
        return String.format("%s %d", "Series", Integer.valueOf(i + 1));
    }

    @SuppressLint({"DefaultLocale"})
    private String b(int i) {
        return String.format("%s %d", "Slice", Integer.valueOf(i + 1));
    }

    private TextView a(String str, String str2) {
        TextView textView = new TextView(this.a.getContext());
        textView.setText(b(str, str2));
        int a = at.a(this.b, 5.0f);
        textView.setPadding(a, a, a, a);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-2, -2);
        layoutParams.gravity = 1;
        textView.setLayoutParams(layoutParams);
        return textView;
    }

    private String b(String str, String str2) {
        return Axis.a(str) ? str2 : str;
    }
}

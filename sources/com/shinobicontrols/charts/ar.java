package com.shinobicontrols.charts;

import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Typeface;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class ar {
    private final Paint a = new Paint();
    private float b;
    private float c;
    private String d;
    private float e;
    private Typeface f;

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a(PointF pointF, String str, float f, Typeface typeface, v vVar) {
        if (str.equals(this.d) && f == this.e && typeface.equals(this.f)) {
            pointF.x = this.b;
            pointF.y = this.c;
            return;
        }
        this.a.setTextSize(vVar.getResources().getDisplayMetrics().scaledDensity * f);
        this.a.setTypeface(typeface);
        Paint.FontMetrics fontMetrics = this.a.getFontMetrics();
        pointF.x = a(str, this.a);
        pointF.y = (float) Math.ceil(fontMetrics.bottom - fontMetrics.top);
        this.d = str;
        this.e = f;
        this.f = typeface;
        this.b = pointF.x;
        this.c = pointF.y;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void b(PointF pointF, String str, float f, Typeface typeface, v vVar) {
        a(pointF, str, f, typeface, vVar);
        pointF.y *= a(str);
    }

    private float a(String str, Paint paint) {
        float measureText;
        String[] split;
        if (str.contains("\n")) {
            measureText = 0.0f;
            for (String str2 : str.split("\n")) {
                if (paint.measureText(str2) > measureText) {
                    measureText = paint.measureText(str2);
                }
            }
        } else {
            measureText = paint.measureText(str);
        }
        return (float) Math.ceil(measureText + 2.0f);
    }

    private int a(String str) {
        if (str == null || str.length() <= 0) {
            return 0;
        }
        return str.split("\n", -1).length;
    }
}

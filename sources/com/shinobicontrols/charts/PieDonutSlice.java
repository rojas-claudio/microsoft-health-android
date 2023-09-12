package com.shinobicontrols.charts;

import android.graphics.Paint;
import android.graphics.Point;
/* loaded from: classes.dex */
public final class PieDonutSlice extends InternalDataPoint {
    String m;
    float n;
    float o;
    float p;
    float q;
    Point r;
    Point s;
    Paint t;
    Paint u;

    /* JADX INFO: Access modifiers changed from: package-private */
    public PieDonutSlice(double x, double y) {
        super(x, y);
        this.t = new Paint();
        this.u = new Paint();
    }

    public float getCenterAngle() {
        return (this.n + this.o) / 2.0f;
    }

    public Point getLabelCenter() {
        return this.s;
    }

    public String getLabelText() {
        return this.m;
    }

    public void setLabelText(String labelText) {
        this.m = labelText;
    }

    public double getY() {
        return this.b;
    }

    public int getCenterX() {
        return this.r.x;
    }

    public int getCenterY() {
        return this.r.y;
    }

    public Paint getLabelPaint() {
        return this.t;
    }

    public Paint getLabelBackgroundPaint() {
        return this.u;
    }
}

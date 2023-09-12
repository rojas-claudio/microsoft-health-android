package com.shinobicontrols.charts;
/* loaded from: classes.dex */
class bm extends AnimationCurve {
    private static final float c = -((float) Math.log(0.012000000104308128d));
    private final float d = a(1.0d);

    private float a(double d) {
        return (1.0f - ((float) Math.pow(2.718281828459045d, (-d) * c))) / c;
    }

    @Override // com.shinobicontrols.charts.AnimationCurve
    public float valueAtTime(float time) {
        return a(time) / this.d;
    }
}

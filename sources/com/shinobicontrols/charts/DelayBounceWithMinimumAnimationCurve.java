package com.shinobicontrols.charts;
/* loaded from: classes.dex */
public class DelayBounceWithMinimumAnimationCurve extends AnimationCurve {
    @Override // com.shinobicontrols.charts.AnimationCurve
    public float valueAtTime(float time) {
        float time2;
        if (time < 0.5f) {
            time2 = 0.0f;
        } else {
            time2 = (time - 0.5f) * 2.0f;
        }
        float a = a(time2);
        if (a < 0.05f) {
            return 0.05f;
        }
        return a;
    }
}

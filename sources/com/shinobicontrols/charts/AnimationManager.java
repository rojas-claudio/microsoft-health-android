package com.shinobicontrols.charts;

import com.microsoft.kapp.utils.Constants;
/* loaded from: classes.dex */
class AnimationManager {
    private final long nativeHandle = 0;

    private native void setCurrentState(float f, float f2, float f3, boolean z, boolean z2, boolean z3, double d, double d2, double d3);

    static {
        System.loadLibrary("shinobicharts-android");
    }

    void update(Series<?> series) {
        if (series.u != null) {
            SeriesAnimation seriesAnimation = series.u;
            setCurrentState(seriesAnimation.d(), seriesAnimation.e(), seriesAnimation.f(), true, true, true, series.h(), series.b(), seriesAnimation.getDuration());
            return;
        }
        setCurrentState(1.0f, 1.0f, 1.0f, false, true, false, Constants.SPLITS_ACCURACY, Constants.SPLITS_ACCURACY, Constants.SPLITS_ACCURACY);
    }
}

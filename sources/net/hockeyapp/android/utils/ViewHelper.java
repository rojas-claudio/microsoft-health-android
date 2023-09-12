package net.hockeyapp.android.utils;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
/* loaded from: classes.dex */
public class ViewHelper {
    public static Drawable getGradient() {
        int[] colors = {-16777216, 0};
        GradientDrawable gradient = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, colors);
        return gradient;
    }
}

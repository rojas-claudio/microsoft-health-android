package com.shinobicontrols.kcompanionapp.charts.internal;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.view.View;
import android.widget.TextView;
import com.microsoft.kapp.R;
/* loaded from: classes.dex */
public class IconFactory {
    public static final String BIKE = "\ue133";
    public static final String CALORIE_BURN = "\ue009";
    public static final String ELEVATION = "\ue103";
    public static final String HEART_RATE = "\ue006";
    public static final String PAR = "\ue158";
    public static final String RUN = "\ue004";
    public static final String SLEEP = "\ue005";
    public static final String SPEED = "\ue137";
    public static final String STEPS = "\ue008";
    public static final int[][] states = {new int[]{16842913}, new int[]{-16842913}};

    public static View create(String iconText, Context context, ChartThemeCache themeCache) {
        String typefaceAssetPath = themeCache.getPathToGlyphTypeface();
        Typeface typeface = Typeface.createFromAsset(context.getResources().getAssets(), typefaceAssetPath);
        TextView icon = new TextView(context);
        icon.setTypeface(typeface);
        ColorStateList colorStateList = new ColorStateList(states, new int[]{themeCache.getSelectedIconColor(), themeCache.getUnselectedIconColor()});
        icon.setTextColor(colorStateList);
        icon.setText(iconText);
        icon.setTextSize(0, context.getResources().getDimension(R.dimen.MediumIconSize));
        return icon;
    }
}

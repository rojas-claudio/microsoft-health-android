package com.shinobicontrols.kcompanionapp.charts.internal;

import android.content.Context;
import android.graphics.Typeface;
import android.view.ViewGroup;
import com.microsoft.kapp.R;
/* loaded from: classes.dex */
public class FlagViewCreator {
    public static FlagView createDataPointFlag(Context context, String icon, String title, int flagColor, int textColor, String pathToTypeface, String pathToGlyphTypeface) {
        FlagView flagView = new FlagView(context);
        flagView.setFlagColor(flagColor);
        flagView.setTitleColor(textColor);
        flagView.setSymbolColor(textColor);
        flagView.setValueColor(textColor);
        flagView.setSymbolTypeface(Typeface.createFromAsset(context.getAssets(), pathToGlyphTypeface));
        Typeface textTypeface = Typeface.createFromAsset(context.getAssets(), pathToTypeface);
        flagView.setTitleTypeface(textTypeface);
        flagView.setValueTypeface(textTypeface);
        float textSizeInSP = context.getResources().getDimensionPixelSize(R.dimen.shinobicharts_data_point_flag_text_size) / context.getResources().getDisplayMetrics().scaledDensity;
        flagView.setValueTextSize(textSizeInSP);
        flagView.setSymbolTextSize(textSizeInSP);
        float titleTextSizeInSP = context.getResources().getDimensionPixelSize(R.dimen.shinobicharts_data_point_flag_title_text_size) / context.getResources().getDisplayMetrics().scaledDensity;
        flagView.setTitleTextSize(titleTextSizeInSP);
        flagView.setLayoutParams(new ViewGroup.LayoutParams(-2, -2));
        flagView.setSymbol(icon);
        flagView.setTitle(title);
        flagView.setFlagSizingStrategy(FlagMeasuringStrategy.FOUR_TIMES_FLAG_TEXT_PLUS_POINTER_OFFSET);
        flagView.setFlagPlacementStrategy(FlagPlacementStrategy.TOP_RIGHT_QUADRANT_FLAG_PLACEMENT_STRATEGY);
        flagView.setFlagPointerDrawingStrategy(FlagPointerDrawingStrategy.TOP_RIGHT_QUADRANT_FLAG_POINTER_DRAWING_STRATEGY);
        return flagView;
    }

    public static FlagView createLowFlag(Context context, String icon, String value, String title, ChartThemeCache themeCache) {
        return null;
    }

    public static FlagView createSleepFlag(Context context, int flagColor, int textColor, String pathToTypeface) {
        FlagView flagView = new FlagView(context);
        flagView.setFlagColor(flagColor);
        flagView.setValueColor(textColor);
        Typeface textTypeface = Typeface.createFromAsset(context.getAssets(), pathToTypeface);
        flagView.setValueTypeface(textTypeface);
        float textSize = context.getResources().getDimensionPixelSize(R.dimen.shinobicharts_sleep_flag_text_size);
        flagView.setValueTextSize(textSize / context.getResources().getDisplayMetrics().scaledDensity);
        flagView.setCornerRadius(context.getResources().getDimensionPixelSize(R.dimen.shinobicharts_sleep_flag_corner_radius));
        flagView.setTitle(null);
        flagView.setSymbol(null);
        flagView.setLayoutParams(new ViewGroup.LayoutParams(-2, -2));
        flagView.setFlagSizingStrategy(FlagMeasuringStrategy.MATCH_PARENT);
        flagView.setFlagPointerDrawingStrategy(FlagPointerDrawingStrategy.NULL);
        flagView.setFlagPlacementStrategy(FlagPlacementStrategy.NULL);
        return flagView;
    }
}

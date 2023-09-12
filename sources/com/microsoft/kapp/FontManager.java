package com.microsoft.kapp;

import android.content.Context;
import android.graphics.Typeface;
import com.microsoft.kapp.logging.KLog;
/* loaded from: classes.dex */
public final class FontManager {
    public static final int FF_ID_TYPEFACE_BOLD = 1;
    public static final int FF_ID_TYPEFACE_LIGHT = 3;
    public static final int FF_ID_TYPEFACE_NORMAL = 0;
    public static final int FF_ID_TYPEFACE_SEMI_BOLD = 2;
    private static final String SEGOE_UI_BOLD_FONT_FILE = "fonts/segoeuib.ttf";
    private static final String SEGOE_UI_LIGHT_FONT_FILE = "fonts/segoeuil.ttf";
    private static final String SEGOE_UI_NORMAL_FONT_FILE = "fonts/segoeui.ttf";
    private static final String SEGOE_UI_SEMIBOLD_FONT_FILE = "fonts/segoeuisb.ttf";
    public static final String SYMBOLS_FONT_FILE = "fonts/ProjectKSymbolRegular.ttf";
    private static Typeface mGlyphFontFace = null;
    private static Typeface mTypefaceNormal = null;
    private static Typeface mTypefaceBold = null;
    private static Typeface mTypefaceSemiBold = null;
    private static Typeface mTypefaceLight = null;
    private static final String TAG = FontManager.class.getSimpleName();

    public FontManager(Context context) {
        try {
            mTypefaceNormal = Typeface.createFromAsset(context.getAssets(), SEGOE_UI_NORMAL_FONT_FILE);
            mTypefaceBold = Typeface.createFromAsset(context.getAssets(), SEGOE_UI_BOLD_FONT_FILE);
            mTypefaceSemiBold = Typeface.createFromAsset(context.getAssets(), SEGOE_UI_SEMIBOLD_FONT_FILE);
            mTypefaceLight = Typeface.createFromAsset(context.getAssets(), SEGOE_UI_LIGHT_FONT_FILE);
            mGlyphFontFace = Typeface.createFromAsset(context.getAssets(), SYMBOLS_FONT_FILE);
        } catch (Exception e) {
            KLog.e(TAG, "FontManager initialization failed.", e);
        }
    }

    public Typeface getFontFace(int fontFamilyId) {
        switch (fontFamilyId) {
            case 0:
                return mTypefaceNormal;
            case 1:
                return mTypefaceBold;
            case 2:
                return mTypefaceSemiBold;
            case 3:
                return mTypefaceLight;
            default:
                return mTypefaceNormal;
        }
    }

    public Typeface getGlyphFontFace() {
        return mGlyphFontFace;
    }
}

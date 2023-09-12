package com.microsoft.kapp.style.text;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import com.facebook.internal.AnalyticsEvents;
import com.microsoft.kapp.FontManager;
import com.microsoft.kapp.KApplicationGraph;
import com.microsoft.kapp.R;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.kapp.utils.StrappConstants;
import com.microsoft.kapp.utils.ViewUtils;
import java.util.HashMap;
import java.util.List;
import javax.inject.Inject;
/* loaded from: classes.dex */
public class CustomStyleToSpan extends StyleToSpan {
    @Inject
    FontManager mFontManager;
    static int ATTR_FONT_FAMILY = R.attr.custom_font_family;
    static int ATTR_SPAN_DIMENSION = R.attr.spanDimension;
    static int ATTR_TEXT_COLOR = 16842904;
    static int ATTR_TEXT_SIZE = 16842901;
    static int[] CUSTOM_ATTRS = {ATTR_FONT_FAMILY, ATTR_SPAN_DIMENSION, ATTR_TEXT_COLOR, ATTR_TEXT_SIZE};
    static HashMap<Integer, Integer> styleAttributesIndex = ViewUtils.getStyleAttributesWithIndex(CUSTOM_ATTRS);
    static int ATTR_FONT_FAMILY_INDEX = styleAttributesIndex.get(Integer.valueOf(ATTR_FONT_FAMILY)).intValue();
    static int ATTR_SPAN_DIMENSION_INDEX = styleAttributesIndex.get(Integer.valueOf(ATTR_SPAN_DIMENSION)).intValue();
    static int ATTR_TEXT_COLOR_INDEX = styleAttributesIndex.get(Integer.valueOf(ATTR_TEXT_COLOR)).intValue();
    static int ATTR_TEXT_SIZE_INDEX = styleAttributesIndex.get(Integer.valueOf(ATTR_TEXT_SIZE)).intValue();
    static String DEF_TYPE = AnalyticsEvents.PARAMETER_LIKE_VIEW_STYLE;
    static String DEF_PACKAGE = StrappConstants.NOTIFICATION_SERVICE_MICROSOFT_HEALTH;

    public CustomStyleToSpan() {
        KApplicationGraph.getApplicationGraph().inject(this);
    }

    public List<CharacterStyle> getSpans(Context context, String styleName) {
        List<CharacterStyle> result = super.getSpans(context, styleName, DEF_TYPE, DEF_PACKAGE);
        int styleId = context.getResources().getIdentifier(styleName, DEF_TYPE, DEF_PACKAGE);
        TypedArray styleAttributes = null;
        if (styleId != 0) {
            try {
                try {
                    styleAttributes = context.obtainStyledAttributes(styleId, CUSTOM_ATTRS);
                    int fontFamilyId = styleAttributes.getInt(ATTR_FONT_FAMILY_INDEX, DEFAULT_VALUE);
                    float relativeSize = styleAttributes.getFloat(ATTR_SPAN_DIMENSION_INDEX, DEFAULT_VALUE);
                    int textColor = styleAttributes.getColor(ATTR_TEXT_COLOR_INDEX, DEFAULT_VALUE);
                    int textSize = styleAttributes.getDimensionPixelOffset(ATTR_TEXT_SIZE_INDEX, DEFAULT_VALUE);
                    if (fontFamilyId != DEFAULT_VALUE) {
                        Typeface typeface = this.mFontManager.getFontFace(fontFamilyId);
                        result.add(new com.microsoft.kapp.views.CustomTypefaceSpan(typeface));
                    }
                    if (relativeSize != DEFAULT_VALUE) {
                        result.add(new RelativeSizeSpan(relativeSize));
                    }
                    if (textColor != DEFAULT_VALUE) {
                        result.add(new ForegroundColorSpan(textColor));
                    }
                    if (textSize != DEFAULT_VALUE) {
                        result.add(new AbsoluteSizeSpan(textSize));
                    }
                } catch (Resources.NotFoundException e) {
                    KLog.e(StyleToSpan.class.getName(), String.format("style not found {0}", styleName));
                    if (styleAttributes != null) {
                        styleAttributes.recycle();
                    }
                }
            } finally {
                if (styleAttributes != null) {
                    styleAttributes.recycle();
                }
            }
        }
        return result;
    }
}

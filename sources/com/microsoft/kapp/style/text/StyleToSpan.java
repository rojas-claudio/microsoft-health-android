package com.microsoft.kapp.style.text;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;
import com.microsoft.kapp.diagnostics.Validate;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.kapp.utils.Constants;
import java.util.LinkedList;
import java.util.List;
/* loaded from: classes.dex */
public class StyleToSpan {
    static int DEFAULT_VALUE = -1;
    static int[] attrs = {16842901, 16842904};

    public List<CharacterStyle> getSpans(Context context, String styleName, String defType, String defPackage) {
        Validate.notNull(context, Constants.FRE_INTENT_EXTRA_INFO);
        Validate.notNullOrEmpty(styleName, "styleName");
        List<CharacterStyle> result = new LinkedList<>();
        int styleId = context.getResources().getIdentifier(styleName, defType, defPackage);
        TypedArray styleAttributes = null;
        if (styleId != 0) {
            try {
                try {
                    styleAttributes = context.obtainStyledAttributes(styleId, attrs);
                    int mTextSize = styleAttributes.getDimensionPixelSize(0, -1);
                    int mTextColor = styleAttributes.getColor(1, DEFAULT_VALUE);
                    if (mTextSize != DEFAULT_VALUE) {
                        result.add(new AbsoluteSizeSpan(mTextSize));
                    }
                    if (mTextColor != DEFAULT_VALUE) {
                        result.add(new ForegroundColorSpan(mTextColor));
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

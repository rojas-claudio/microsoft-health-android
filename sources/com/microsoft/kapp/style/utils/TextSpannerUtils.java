package com.microsoft.kapp.style.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import com.microsoft.kapp.style.text.CustomTypefaceSpan;
/* loaded from: classes.dex */
public class TextSpannerUtils {
    private static final int INVALID_RESOURCE_ID = -1;
    public static final int SKIP_THIS_SPAN_PARAM = -1;

    private static boolean validateParams(CharSequence text, int startIndex, int endIndex) {
        int textLength;
        return text != null && text.length() != 0 && startIndex >= 0 && endIndex >= 0 && startIndex <= (textLength = text.length()) && endIndex <= textLength && startIndex <= endIndex;
    }

    public static CharSequence spanTextWithCustomTypeface(CharSequence text, Typeface typeface, int startIndex, int endIndex) {
        if (validateParams(text, startIndex, endIndex) && typeface != null) {
            Spannable spannableText = new SpannableString(text);
            spannableText.setSpan(new CustomTypefaceSpan("", typeface), startIndex, endIndex, 34);
            return spannableText;
        }
        return text;
    }

    public static CharSequence spanTextWithSize(Context context, CharSequence text, int textSizeResId, int startIndex, int endIndex) {
        if (validateParams(text, startIndex, endIndex) && textSizeResId != -1) {
            Spannable spannableText = new SpannableString(text);
            int textSizeInPixels = context.getResources().getDimensionPixelSize(textSizeResId);
            spannableText.setSpan(new AbsoluteSizeSpan(textSizeInPixels), startIndex, endIndex, 34);
            return spannableText;
        }
        return text;
    }

    public static CharSequence spanTextWithColor(Context context, CharSequence text, int textColorResId, int startIndex, int endIndex) {
        if (validateParams(text, startIndex, endIndex) && textColorResId != -1) {
            Spannable spannableText = new SpannableString(text);
            int textColor = context.getResources().getColor(textColorResId);
            spannableText.setSpan(new ForegroundColorSpan(textColor), startIndex, endIndex, 34);
            return spannableText;
        }
        return text;
    }

    public static CharSequence multiSpanText(Context context, CharSequence text, Typeface typeface, int textsizeResId, int textColorResId, int startIndex, int endIndex) {
        CharSequence spannedText = text;
        if (typeface != null) {
            spannedText = spanTextWithCustomTypeface(spannedText, typeface, startIndex, endIndex);
        }
        if (textsizeResId != -1) {
            spannedText = spanTextWithSize(context, spannedText, textsizeResId, startIndex, endIndex);
        }
        if (textsizeResId != -1) {
            return spanTextWithColor(context, spannedText, textColorResId, startIndex, endIndex);
        }
        return spannedText;
    }
}

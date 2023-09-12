package com.microsoft.kapp.style.text;

import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.text.style.TypefaceSpan;
/* loaded from: classes.dex */
public class CustomTypefaceSpan extends TypefaceSpan {
    private final Typeface mCustomTypeface;

    public CustomTypefaceSpan(String family, Typeface customTypeface) {
        super(family);
        this.mCustomTypeface = customTypeface;
    }

    @Override // android.text.style.TypefaceSpan, android.text.style.CharacterStyle
    public void updateDrawState(TextPaint textPaint) {
        applyNewTypeface(textPaint, this.mCustomTypeface);
    }

    @Override // android.text.style.TypefaceSpan, android.text.style.MetricAffectingSpan
    public void updateMeasureState(TextPaint paint) {
        applyNewTypeface(paint, this.mCustomTypeface);
    }

    private static void applyNewTypeface(Paint paint, Typeface newTypeface) {
        int oldFontStyle;
        Typeface oldTypeface = paint.getTypeface();
        if (oldTypeface == null) {
            oldFontStyle = 0;
        } else {
            oldFontStyle = oldTypeface.getStyle();
        }
        int fakeStyle = oldFontStyle & (newTypeface.getStyle() ^ (-1));
        if ((fakeStyle & 1) != 0) {
            paint.setFakeBoldText(true);
        }
        if ((fakeStyle & 2) != 0) {
            paint.setTextSkewX(-0.25f);
        }
        paint.setTypeface(newTypeface);
    }
}

package com.microsoft.kapp.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.TextView;
import com.microsoft.kapp.FontManager;
import com.microsoft.kapp.KApplicationGraph;
import com.microsoft.kapp.R;
import com.microsoft.kapp.style.utils.MetricSpannerUtils;
import javax.inject.Inject;
/* loaded from: classes.dex */
public class CustomFontTextGlyphView extends TextView {
    @Inject
    FontManager mFontManager;

    public CustomFontTextGlyphView(Context context) {
        this(context, null);
    }

    public CustomFontTextGlyphView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context, attrs);
    }

    public CustomFontTextGlyphView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initialize(context, attrs);
    }

    private void initialize(Context context, AttributeSet attrs) {
        KApplicationGraph.getApplicationGraph().inject(this);
        TypedArray typedAttributes = context.obtainStyledAttributes(attrs, R.styleable.TextWithGlyph);
        TypedArray customFontAttributes = context.obtainStyledAttributes(attrs, R.styleable.CustomFontTextView);
        try {
            int fontFamilyId = customFontAttributes.getInt(0, 0);
            Typeface textFont = this.mFontManager.getFontFace(fontFamilyId);
            Typeface glyphFont = this.mFontManager.getGlyphFontFace();
            CharSequence text = getText();
            CharSequence glyph = typedAttributes.getText(0);
            CharSequence combined = text;
            if (!TextUtils.isEmpty(glyph)) {
                combined = TextUtils.concat(text, MetricSpannerUtils.METRIC_UNITS_SEPARATOR_SPACE, glyph);
            }
            SpannableStringBuilder sb = new SpannableStringBuilder(combined);
            sb.setSpan(new CustomTypefaceSpan(textFont), 0, text.length(), 34);
            if (!TextUtils.isEmpty(glyph)) {
                sb.setSpan(new CustomTypefaceSpan(glyphFont), text.length(), combined.length(), 34);
            }
            setText(sb);
        } finally {
            typedAttributes.recycle();
            customFontAttributes.recycle();
        }
    }
}

package com.microsoft.kapp.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;
import com.microsoft.kapp.FontManager;
import com.microsoft.kapp.KApplicationGraph;
import javax.inject.Inject;
/* loaded from: classes.dex */
public class CustomGlyphView extends TextView {
    @Inject
    FontManager mFontManager;

    public CustomGlyphView(Context context) {
        super(context);
        init();
        initTypeface();
    }

    public CustomGlyphView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
        initTypeface();
    }

    public CustomGlyphView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
        initTypeface();
    }

    private void init() {
        KApplicationGraph.getApplicationGraph().inject(this);
    }

    private void initTypeface() {
        setTypeface(this.mFontManager.getGlyphFontFace());
    }

    public void setGlyph(int glyphResId) {
        setText(getContext().getString(glyphResId));
    }
}

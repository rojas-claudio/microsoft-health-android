package com.microsoft.kapp.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;
import com.microsoft.kapp.FontManager;
import com.microsoft.kapp.KApplicationGraph;
import com.microsoft.kapp.utils.CommonUtils;
import javax.inject.Inject;
/* loaded from: classes.dex */
public class CustomFontTextView extends TextView {
    @Inject
    FontManager mFontManager;

    public CustomFontTextView(Context context) {
        this(context, null);
    }

    public CustomFontTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
        CommonUtils.applyCommonStyles(this, this.mFontManager, context, attrs);
    }

    public CustomFontTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
        CommonUtils.applyCommonStyles(this, this.mFontManager, context, attrs);
    }

    private void init() {
        KApplicationGraph.getApplicationGraph().inject(this);
    }
}

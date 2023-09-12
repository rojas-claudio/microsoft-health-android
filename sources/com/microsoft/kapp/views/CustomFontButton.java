package com.microsoft.kapp.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;
import com.microsoft.kapp.FontManager;
import com.microsoft.kapp.KApplicationGraph;
import com.microsoft.kapp.utils.CommonUtils;
import javax.inject.Inject;
/* loaded from: classes.dex */
public class CustomFontButton extends Button {
    @Inject
    FontManager mFontManager;

    public CustomFontButton(Context context) {
        this(context, null);
    }

    public CustomFontButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
        CommonUtils.applyCommonStyles(this, this.mFontManager, context, attrs);
    }

    public CustomFontButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
        CommonUtils.applyCommonStyles(this, this.mFontManager, context, attrs);
    }

    private void init() {
        KApplicationGraph.getApplicationGraph().inject(this);
    }
}

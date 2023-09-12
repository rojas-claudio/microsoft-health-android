package com.microsoft.kapp.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.microsoft.kapp.R;
import com.microsoft.kapp.style.utils.MetricSpannerUtils;
import com.microsoft.kapp.utils.ViewUtils;
/* loaded from: classes.dex */
public abstract class BaseTrackerStat extends LinearLayout {
    private CustomGlyphView mGlyphView;
    private TextView mTitleView;

    public BaseTrackerStat(Context context) {
        super(context);
    }

    public BaseTrackerStat(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void initialize() {
        this.mTitleView = (TextView) ViewUtils.getValidView(this, R.id.txt_stat_title, TextView.class);
        this.mGlyphView = (CustomGlyphView) ViewUtils.getValidView(this, R.id.txt_stat_title_symbol, CustomGlyphView.class);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setTitleSymbolVisibility(int visibility) {
        this.mGlyphView.setVisibility(visibility);
    }

    public void setTitle(String title) {
        this.mTitleView.setText(title);
    }

    public void setTitle(int titleResId) {
        this.mTitleView.setText(titleResId);
    }

    public void setTitleSymbol(int symbolId) {
        this.mGlyphView.setGlyph(symbolId);
    }

    public String getTitle() {
        return this.mTitleView.getText().toString();
    }

    public String getGlyph() {
        return this.mGlyphView.getText().toString();
    }

    public void setTitleContentDesc(String contentDesc) {
        this.mTitleView.setContentDescription(contentDesc);
    }

    public void setTitleSymbolContentDesc(String contentDesc) {
        this.mGlyphView.setContentDescription(contentDesc);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public String getContentDescBase(String title) {
        String modifiedTitleForContentDesc = title.replace(MetricSpannerUtils.METRIC_UNITS_SEPARATOR_SPACE, "_");
        String contentDescBase = getResources().getString(R.string.tracker_stat_content_desc_base) + "_" + modifiedTitleForContentDesc;
        return contentDescBase;
    }
}

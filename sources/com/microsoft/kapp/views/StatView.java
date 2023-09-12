package com.microsoft.kapp.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.microsoft.kapp.R;
import com.microsoft.kapp.utils.ViewUtils;
/* loaded from: classes.dex */
public class StatView extends LinearLayout {
    private TextView mLeftSubTitle;
    private TextView mLeftSubValue;
    private TextView mRightSubTitle;
    private TextView mRightSubValue;
    private CustomGlyphView mStatGlyph;
    private TextView mStatTitle;
    private TextView mStatValue;

    public StatView(Context context) {
        super(context);
        init();
    }

    public StatView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public StatView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.stat_layout, this);
        this.mStatGlyph = (CustomGlyphView) ViewUtils.getValidView(this, R.id.stat_icon, CustomGlyphView.class);
        this.mStatTitle = (TextView) ViewUtils.getValidView(this, R.id.stat_title, TextView.class);
        this.mStatValue = (TextView) ViewUtils.getValidView(this, R.id.stat_value, TextView.class);
        this.mLeftSubTitle = (TextView) ViewUtils.getValidView(this, R.id.left_sub_stat_title, TextView.class);
        this.mLeftSubValue = (TextView) ViewUtils.getValidView(this, R.id.left_sub_stat_value, TextView.class);
        this.mRightSubTitle = (TextView) ViewUtils.getValidView(this, R.id.right_sub_stat_title, TextView.class);
        this.mRightSubValue = (TextView) ViewUtils.getValidView(this, R.id.right_sub_stat_value, TextView.class);
    }

    public void setStatGlyph(int glyphResId) {
        this.mStatGlyph.setText(glyphResId);
    }

    public void setStat(String statTitle, String statValue) {
        this.mStatTitle.setText(statTitle);
        this.mStatValue.setText(statValue);
    }

    public void setLeftSubStat(String statTitle, String statValue) {
        this.mLeftSubTitle.setText(statTitle);
        this.mLeftSubValue.setText(statValue);
        this.mLeftSubTitle.setVisibility(0);
        this.mLeftSubValue.setVisibility(0);
    }

    public void setRightSubStat(String statTitle, String statValue) {
        this.mRightSubTitle.setText(statTitle);
        this.mRightSubValue.setText(statValue);
        this.mRightSubTitle.setVisibility(0);
        this.mRightSubValue.setVisibility(0);
    }

    public void setStatTitleSize(float statTitleTextSize) {
        this.mStatGlyph.setTextSize(statTitleTextSize);
        this.mStatTitle.setTextSize(statTitleTextSize);
        this.mLeftSubTitle.setTextSize(statTitleTextSize);
        this.mLeftSubValue.setTextSize(statTitleTextSize);
        this.mRightSubTitle.setTextSize(statTitleTextSize);
        this.mRightSubValue.setTextSize(statTitleTextSize);
    }

    public void setStatValueSize(float statValueTextSize) {
        this.mStatValue.setTextSize(statValueTextSize);
    }
}

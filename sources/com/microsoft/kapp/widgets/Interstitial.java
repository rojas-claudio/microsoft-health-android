package com.microsoft.kapp.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import com.microsoft.kapp.R;
import com.microsoft.kapp.utils.ViewUtils;
import com.microsoft.kapp.views.CustomFontTextView;
/* loaded from: classes.dex */
public class Interstitial extends FrameLayout {
    public static final int SLIDE_ADDING_WEBTILE = 6010;
    public static final int SLIDE_GETTING_ACCOUNT_INFO = 5010;
    public static final int SLIDE_GETTING_BAND_INFO = 5040;
    public static final int SLIDE_GETTING_YOU_SETUP = 5020;
    public static final int SLIDE_GONE = 4090;
    public static final int SLIDE_PROCESSING_WEBTILE = 6000;
    public static final int SLIDE_SPINNER_ONLY = 5000;
    public static final int SLIDE_UPDATING = 5030;
    public static final int SLIDE_UPDATING_BAND_INFO = 5060;
    private CustomFontTextView mSubtitle;
    private CustomFontTextView mTitle;

    public Interstitial(Context context) {
        super(context);
        initializeViews(context, null, R.style.InterstitialDefault);
    }

    public Interstitial(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializeViews(context, attrs, R.style.InterstitialDefault);
    }

    public Interstitial(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initializeViews(context, attrs, defStyle);
    }

    private void initializeViews(Context context, AttributeSet attrs, int defStyle) {
        inflate(context, R.layout.widget_interstitial, this);
        this.mTitle = (CustomFontTextView) ViewUtils.getValidView(this, R.id.waiting_title, CustomFontTextView.class);
        this.mSubtitle = (CustomFontTextView) ViewUtils.getValidView(this, R.id.waiting_subtitle, CustomFontTextView.class);
        setOnClickListener(null);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.Interstitial, defStyle, 0);
        try {
            int titleColor = typedArray.getColor(2, getResources().getColor(R.color.WhiteColor));
            int subtitleColor = typedArray.getColor(3, getResources().getColor(R.color.WhiteColor));
            int titleSize = typedArray.getDimensionPixelSize(0, getResources().getDimensionPixelSize(R.dimen.font_size_title));
            int subtitleSize = typedArray.getDimensionPixelSize(1, getResources().getDimensionPixelSize(R.dimen.font_size_smallBody));
            this.mTitle.setTextSize(0, titleSize);
            this.mSubtitle.setTextSize(0, subtitleSize);
            this.mTitle.setTextColor(titleColor);
            this.mSubtitle.setTextColor(subtitleColor);
        } finally {
            typedArray.recycle();
        }
    }

    public boolean setSlide(int slide) {
        setVisibility(0);
        this.mTitle.setVisibility(0);
        this.mSubtitle.setVisibility(0);
        switch (slide) {
            case 5000:
                this.mTitle.setVisibility(8);
                this.mSubtitle.setVisibility(8);
                break;
            case SLIDE_GETTING_ACCOUNT_INFO /* 5010 */:
                this.mTitle.setText(R.string.slide_just_a_few_seconds);
                this.mSubtitle.setText(R.string.slide_getting_your_account_info);
                break;
            case SLIDE_GETTING_YOU_SETUP /* 5020 */:
                this.mTitle.setText(R.string.slide_just_a_few_seconds);
                this.mSubtitle.setText(R.string.slide_getting_you_all_setup);
                break;
            case SLIDE_UPDATING /* 5030 */:
                this.mTitle.setText(R.string.slide_updating);
                this.mSubtitle.setVisibility(8);
                break;
            case SLIDE_GETTING_BAND_INFO /* 5040 */:
                this.mTitle.setText(R.string.slide_just_a_few_seconds);
                this.mSubtitle.setText(R.string.slide_getting_band_info1);
                break;
            case SLIDE_UPDATING_BAND_INFO /* 5060 */:
                this.mTitle.setText(R.string.slide_just_a_few_seconds);
                this.mSubtitle.setText(R.string.slide_band_is_getting_updated);
                break;
            case 6000:
                this.mTitle.setText(R.string.webtile_please_wait);
                this.mSubtitle.setText(R.string.webtile_few_moments);
                break;
            case SLIDE_ADDING_WEBTILE /* 6010 */:
                this.mTitle.setText(R.string.webtile_please_wait);
                this.mSubtitle.setText(R.string.webtile_add_tile_few_moments);
                break;
            default:
                setVisibility(8);
                return false;
        }
        return true;
    }

    public void setSlide(String title, String subtitle) {
        setVisibility(0);
        if (title == null) {
            this.mTitle.setVisibility(8);
        } else {
            this.mTitle.setVisibility(0);
            this.mTitle.setText(title);
        }
        if (subtitle == null) {
            this.mSubtitle.setVisibility(8);
            return;
        }
        this.mSubtitle.setVisibility(0);
        this.mSubtitle.setText(subtitle);
    }
}

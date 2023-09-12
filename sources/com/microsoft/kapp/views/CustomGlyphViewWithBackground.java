package com.microsoft.kapp.views;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import com.microsoft.kapp.R;
import com.microsoft.kapp.utils.ViewUtils;
/* loaded from: classes.dex */
public class CustomGlyphViewWithBackground extends RelativeLayout {
    private CustomGlyphView mBackground;
    private PicassoImageView mBackgroundImage;
    private CustomGlyphView mForeground;

    public CustomGlyphViewWithBackground(Context context) {
        super(context);
        init(context);
    }

    public CustomGlyphViewWithBackground(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CustomGlyphViewWithBackground(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public void setForegoundGlyph(int glyphResId) {
        this.mForeground.setGlyph(glyphResId);
    }

    public void setBackgroundGlyph(int glyphResId) {
        this.mBackground.setGlyph(glyphResId);
    }

    public void setBackgroundText(String text) {
        this.mBackground.setText(text);
    }

    public void setBackgroundImage(Drawable image) {
        this.mBackgroundImage.setBackground(image);
    }

    public void setBackgroundVisibility(int visibility) {
        this.mBackgroundImage.setVisibility(visibility);
    }

    public void setTextSize(float size) {
        this.mForeground.setTextSize(size);
        this.mBackground.setTextSize(size);
    }

    private void init(Context context) {
        inflate(context, R.layout.custom_glyph_view_with_background, this);
        this.mForeground = (CustomGlyphView) ViewUtils.getValidView(this, R.id.front_glyph, CustomGlyphView.class);
        this.mBackground = (CustomGlyphView) ViewUtils.getValidView(this, R.id.background_glyph, CustomGlyphView.class);
        this.mBackgroundImage = (PicassoImageView) ViewUtils.getValidView(this, R.id.background_image, PicassoImageView.class);
        setLongClickable(true);
    }

    public void setTextColor(int color) {
        this.mForeground.setTextColor(color);
    }

    public void setBackColor(int color) {
        this.mBackground.setTextColor(color);
    }

    public void setText(String text) {
        this.mForeground.setText(text);
    }
}

package com.microsoft.kapp.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.microsoft.kapp.R;
import com.microsoft.kapp.utils.ViewUtils;
import com.microsoft.kapp.views.CustomGlyphView;
/* loaded from: classes.dex */
public class ConfirmationBar extends ViewGroup {
    private final CustomGlyphView mCancelImageButton;
    private final CustomGlyphView mConfirmImageButton;
    private final View mConfirmationBarView;

    public ConfirmationBar(Context context) {
        this(context, null);
    }

    public ConfirmationBar(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.confirmationBarStyle);
    }

    public ConfirmationBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        LayoutInflater inflater = LayoutInflater.from(context);
        this.mConfirmationBarView = inflater.inflate(R.layout.confirmation_bar, (ViewGroup) this, false);
        addView(this.mConfirmationBarView);
        boolean isInEditMode = isInEditMode();
        this.mConfirmImageButton = isInEditMode ? null : (CustomGlyphView) ViewUtils.getValidView(this.mConfirmationBarView, R.id.confirmation_bar_save, CustomGlyphView.class);
        this.mCancelImageButton = isInEditMode ? null : (CustomGlyphView) ViewUtils.getValidView(this.mConfirmationBarView, R.id.confirmation_bar_cancel, CustomGlyphView.class);
        TypedArray attrArray = context.obtainStyledAttributes(attrs, R.styleable.ConfirmationBar);
        try {
            boolean useDiskSaveIcon = attrArray.getBoolean(1, false);
            if (useDiskSaveIcon) {
                this.mConfirmImageButton.setGlyph(R.string.glyph_save);
            }
        } finally {
            attrArray.recycle();
        }
    }

    public void setOnConfirmButtonClickListener(View.OnClickListener listener) {
        this.mConfirmImageButton.setOnClickListener(listener);
    }

    public void setOnCancelButtonClickListener(View.OnClickListener listener) {
        this.mCancelImageButton.setOnClickListener(listener);
    }

    @Override // android.view.View
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        measureChild(this.mConfirmationBarView, widthMeasureSpec, heightMeasureSpec);
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        this.mConfirmationBarView.layout(0, 0, r, b - t);
    }
}

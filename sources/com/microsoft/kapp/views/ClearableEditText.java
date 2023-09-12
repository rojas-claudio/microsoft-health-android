package com.microsoft.kapp.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import com.microsoft.kapp.R;
/* loaded from: classes.dex */
public class ClearableEditText extends CustomFontEditText {
    private static final int BOTTOM_DRAWABLE = 3;
    private static final int END_DRAWABLE = 2;
    private static final int START_DRAWABLE = 0;
    private static final int TOP_DRAWABLE = 1;
    private Drawable mClearButton;

    public ClearableEditText(Context context) {
        this(context, null);
    }

    public ClearableEditText(Context context, AttributeSet attrs) {
        this(context, attrs, 16842862);
    }

    public ClearableEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        Drawable[] compoundDrawables = getCompoundDrawablesRelative();
        this.mClearButton = compoundDrawables[2];
        if (this.mClearButton == null) {
            this.mClearButton = getResources().getDrawable(R.drawable.ic_navigation_close);
        }
        this.mClearButton.setBounds(0, 0, this.mClearButton.getIntrinsicWidth(), this.mClearButton.getIntrinsicHeight());
        setClearIconVisible(false);
    }

    @Override // android.widget.TextView, android.view.View
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        setClearIconVisible(focused && !getText().toString().isEmpty());
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
    }

    @Override // android.widget.TextView
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        setClearIconVisible(isFocused() && text.length() + (lengthAfter - lengthBefore) > 0);
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
    }

    @Override // android.widget.TextView, android.view.View
    @SuppressLint({"ClickableViewAccessibility"})
    public boolean onTouchEvent(MotionEvent event) {
        Drawable clearIcon = getCompoundDrawables()[2];
        if (clearIcon != null && event.getAction() == 1) {
            boolean ltr = getLayoutDirection() == 0;
            float x = event.getX();
            int viewWidth = getWidth();
            int iconWidth = clearIcon.getIntrinsicWidth();
            if ((ltr && x < viewWidth && x > (viewWidth - getPaddingEnd()) - iconWidth) || (!ltr && x > 0.0f && x < iconWidth)) {
                setText("");
            }
        }
        return super.onTouchEvent(event);
    }

    private void setClearIconVisible(boolean visible) {
        Drawable[] compoundDrawables = getCompoundDrawablesRelative();
        setCompoundDrawablesRelative(compoundDrawables[0], compoundDrawables[1], visible ? this.mClearButton : null, compoundDrawables[3]);
    }
}

package com.microsoft.kapp.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;
/* loaded from: classes.dex */
public class BlockableScrollView extends ScrollView {
    private boolean mIsScrollEnabled;

    public BlockableScrollView(Context context) {
        super(context);
        this.mIsScrollEnabled = true;
    }

    public BlockableScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mIsScrollEnabled = true;
    }

    public BlockableScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mIsScrollEnabled = true;
    }

    public void setIsScrollEnabled(boolean enabled) {
        this.mIsScrollEnabled = enabled;
    }

    public boolean isScrollEnabled() {
        return this.mIsScrollEnabled;
    }

    @Override // android.widget.ScrollView, android.view.View
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == 0) {
            if (this.mIsScrollEnabled) {
                return super.onTouchEvent(event);
            }
            return this.mIsScrollEnabled;
        }
        return super.onTouchEvent(event);
    }

    @Override // android.widget.ScrollView, android.view.ViewGroup
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (this.mIsScrollEnabled) {
            return super.onInterceptTouchEvent(event);
        }
        return false;
    }
}

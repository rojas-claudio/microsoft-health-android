package com.microsoft.kapp.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ScrollView;
/* loaded from: classes.dex */
public class TrackableScrollView extends ScrollView {
    private boolean atBottom;
    private OnHitBottomListener mOnHitBottomListener;
    private OnScrollChangedListener mOnScrollChangedListener;

    /* loaded from: classes.dex */
    public interface OnHitBottomListener {
        void onHitBottom();
    }

    /* loaded from: classes.dex */
    public interface OnScrollChangedListener {
        void onScrollChanged(TrackableScrollView trackableScrollView, int i, int i2, int i3, int i4);
    }

    public TrackableScrollView(Context context) {
        super(context);
    }

    public TrackableScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TrackableScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setOnScrollChangedListener(OnScrollChangedListener listener) {
        this.mOnScrollChangedListener = listener;
    }

    public void setOnHitBottomListener(OnHitBottomListener listener) {
        this.mOnHitBottomListener = listener;
    }

    @Override // android.view.View
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        if (l != oldl || t != oldt) {
            if (this.mOnScrollChangedListener != null) {
                this.mOnScrollChangedListener.onScrollChanged(this, l, t, oldl, oldt);
            }
            View lastChild = getChildAt(getChildCount() - 1);
            if (lastChild != null) {
                int distanceFromBottom = (lastChild.getBottom() - getHeight()) - getScrollY();
                if (distanceFromBottom <= 0) {
                    if (!this.atBottom && this.mOnHitBottomListener != null) {
                        this.mOnHitBottomListener.onHitBottom();
                    }
                    this.atBottom = true;
                } else {
                    this.atBottom = false;
                }
            }
        }
        super.onScrollChanged(l, t, oldl, oldt);
    }
}

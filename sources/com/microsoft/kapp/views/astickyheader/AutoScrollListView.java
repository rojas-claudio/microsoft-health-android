package com.microsoft.kapp.views.astickyheader;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListAdapter;
import android.widget.ListView;
/* loaded from: classes.dex */
public class AutoScrollListView extends ListView {
    private static final float PREFERRED_SELECTION_OFFSET_FROM_TOP = 0.33f;
    private boolean mIsFastScrollEnabled;
    private int mRequestedScrollPosition;
    private ScrollBar mScrollBar;
    private boolean mSmoothScrollRequested;

    public AutoScrollListView(Context context) {
        super(context);
        this.mIsFastScrollEnabled = false;
        this.mScrollBar = null;
        this.mRequestedScrollPosition = -1;
    }

    public AutoScrollListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mIsFastScrollEnabled = false;
        this.mScrollBar = null;
        this.mRequestedScrollPosition = -1;
    }

    public AutoScrollListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mIsFastScrollEnabled = false;
        this.mScrollBar = null;
        this.mRequestedScrollPosition = -1;
    }

    public void requestPositionToScreen(int position, boolean smoothScroll) {
        this.mRequestedScrollPosition = position;
        this.mSmoothScrollRequested = smoothScroll;
        requestLayout();
    }

    public boolean isScrollBarEnabled() {
        return this.mIsFastScrollEnabled;
    }

    public void setIsScrollBarEnabled(boolean enabled) {
        this.mIsFastScrollEnabled = enabled;
        if (this.mIsFastScrollEnabled) {
            if (this.mScrollBar == null) {
                this.mScrollBar = new ScrollBar(getContext(), this);
            }
            this.mScrollBar.show();
        } else if (this.mScrollBar != null) {
            this.mScrollBar.hide();
            this.mScrollBar = null;
        }
    }

    @Override // android.widget.AbsListView, android.view.View
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (this.mScrollBar != null) {
            this.mScrollBar.draw(canvas);
        }
    }

    @Override // android.widget.AdapterView
    public void setAdapter(ListAdapter adapter) {
        super.setAdapter(adapter);
        if (this.mScrollBar != null) {
            this.mScrollBar.setAdapter(adapter);
        }
    }

    @Override // android.widget.ListView, android.widget.AbsListView, android.view.View
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (this.mScrollBar != null) {
            this.mScrollBar.onSizeChanged(w, h);
        }
    }

    @Override // android.widget.AbsListView, android.view.View
    public boolean onTouchEvent(MotionEvent ev) {
        if (this.mScrollBar == null || !this.mScrollBar.onTouchEvent(ev)) {
            return super.onTouchEvent(ev);
        }
        return true;
    }

    @Override // android.widget.ListView, android.widget.AbsListView
    protected void layoutChildren() {
        super.layoutChildren();
        if (this.mRequestedScrollPosition != -1) {
            int position = this.mRequestedScrollPosition;
            this.mRequestedScrollPosition = -1;
            int firstPosition = getFirstVisiblePosition() + 1;
            int lastPosition = getLastVisiblePosition();
            if (position < firstPosition || position > lastPosition) {
                int offset = (int) (getHeight() * PREFERRED_SELECTION_OFFSET_FROM_TOP);
                if (!this.mSmoothScrollRequested) {
                    setSelectionFromTop(position, offset);
                    super.layoutChildren();
                    return;
                }
                int twoScreens = (lastPosition - firstPosition) * 2;
                if (position < firstPosition) {
                    int preliminaryPosition = position + twoScreens;
                    if (preliminaryPosition >= getCount()) {
                        preliminaryPosition = getCount() - 1;
                    }
                    if (preliminaryPosition < firstPosition) {
                        setSelection(preliminaryPosition);
                        super.layoutChildren();
                    }
                } else {
                    int preliminaryPosition2 = position - twoScreens;
                    if (preliminaryPosition2 < 0) {
                        preliminaryPosition2 = 0;
                    }
                    if (preliminaryPosition2 > lastPosition) {
                        setSelection(preliminaryPosition2);
                        super.layoutChildren();
                    }
                }
                if (Build.VERSION.SDK_INT >= 11) {
                    smoothScrollToPositionFromTop(position, offset);
                } else {
                    smoothScrollToPosition(position);
                }
            }
        }
    }
}

package com.unnamed.b.atv.view;

import android.content.Context;
import android.graphics.Rect;
import android.support.v4.media.TransportMediator;
import android.util.AttributeSet;
import android.view.FocusFinder;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.Scroller;
import java.util.List;
/* loaded from: classes.dex */
public class TwoDScrollView extends FrameLayout {
    static final int ANIMATED_SCROLL_GAP = 250;
    static final float MAX_SCROLL_FACTOR = 0.5f;
    private View mChildToScrollTo;
    private boolean mIsBeingDragged;
    private boolean mIsLayoutDirty;
    private float mLastMotionX;
    private float mLastMotionY;
    private long mLastScroll;
    private int mMaximumVelocity;
    private int mMinimumVelocity;
    private Scroller mScroller;
    private final Rect mTempRect;
    private int mTouchSlop;
    private boolean mTwoDScrollViewMovedFocus;
    private VelocityTracker mVelocityTracker;

    public TwoDScrollView(Context context) {
        super(context);
        this.mTempRect = new Rect();
        this.mIsLayoutDirty = true;
        this.mChildToScrollTo = null;
        this.mIsBeingDragged = false;
        initTwoDScrollView();
    }

    public TwoDScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mTempRect = new Rect();
        this.mIsLayoutDirty = true;
        this.mChildToScrollTo = null;
        this.mIsBeingDragged = false;
        initTwoDScrollView();
    }

    public TwoDScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mTempRect = new Rect();
        this.mIsLayoutDirty = true;
        this.mChildToScrollTo = null;
        this.mIsBeingDragged = false;
        initTwoDScrollView();
    }

    @Override // android.view.View
    protected float getTopFadingEdgeStrength() {
        if (getChildCount() == 0) {
            return 0.0f;
        }
        int length = getVerticalFadingEdgeLength();
        if (getScrollY() < length) {
            return getScrollY() / length;
        }
        return 1.0f;
    }

    @Override // android.view.View
    protected float getBottomFadingEdgeStrength() {
        if (getChildCount() == 0) {
            return 0.0f;
        }
        int length = getVerticalFadingEdgeLength();
        int bottomEdge = getHeight() - getPaddingBottom();
        int span = (getChildAt(0).getBottom() - getScrollY()) - bottomEdge;
        if (span < length) {
            return span / length;
        }
        return 1.0f;
    }

    @Override // android.view.View
    protected float getLeftFadingEdgeStrength() {
        if (getChildCount() == 0) {
            return 0.0f;
        }
        int length = getHorizontalFadingEdgeLength();
        if (getScrollX() < length) {
            return getScrollX() / length;
        }
        return 1.0f;
    }

    @Override // android.view.View
    protected float getRightFadingEdgeStrength() {
        if (getChildCount() == 0) {
            return 0.0f;
        }
        int length = getHorizontalFadingEdgeLength();
        int rightEdge = getWidth() - getPaddingRight();
        int span = (getChildAt(0).getRight() - getScrollX()) - rightEdge;
        if (span < length) {
            return span / length;
        }
        return 1.0f;
    }

    public int getMaxScrollAmountVertical() {
        return (int) (MAX_SCROLL_FACTOR * getHeight());
    }

    public int getMaxScrollAmountHorizontal() {
        return (int) (MAX_SCROLL_FACTOR * getWidth());
    }

    private void initTwoDScrollView() {
        this.mScroller = new Scroller(getContext());
        setFocusable(true);
        setDescendantFocusability(262144);
        setWillNotDraw(false);
        ViewConfiguration configuration = ViewConfiguration.get(getContext());
        this.mTouchSlop = configuration.getScaledTouchSlop();
        this.mMinimumVelocity = configuration.getScaledMinimumFlingVelocity();
        this.mMaximumVelocity = configuration.getScaledMaximumFlingVelocity();
    }

    @Override // android.view.ViewGroup
    public void addView(View child) {
        if (getChildCount() > 0) {
            throw new IllegalStateException("TwoDScrollView can host only one direct child");
        }
        super.addView(child);
    }

    @Override // android.view.ViewGroup
    public void addView(View child, int index) {
        if (getChildCount() > 0) {
            throw new IllegalStateException("TwoDScrollView can host only one direct child");
        }
        super.addView(child, index);
    }

    @Override // android.view.ViewGroup, android.view.ViewManager
    public void addView(View child, ViewGroup.LayoutParams params) {
        if (getChildCount() > 0) {
            throw new IllegalStateException("TwoDScrollView can host only one direct child");
        }
        super.addView(child, params);
    }

    @Override // android.view.ViewGroup
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        if (getChildCount() > 0) {
            throw new IllegalStateException("TwoDScrollView can host only one direct child");
        }
        super.addView(child, index, params);
    }

    private boolean canScroll() {
        View child = getChildAt(0);
        if (child != null) {
            int childHeight = child.getHeight();
            int childWidth = child.getWidth();
            return getHeight() < (getPaddingTop() + childHeight) + getPaddingBottom() || getWidth() < (getPaddingLeft() + childWidth) + getPaddingRight();
        }
        return false;
    }

    @Override // android.view.ViewGroup, android.view.View
    public boolean dispatchKeyEvent(KeyEvent event) {
        boolean handled = super.dispatchKeyEvent(event);
        if (handled) {
            return true;
        }
        return executeKeyEvent(event);
    }

    public boolean executeKeyEvent(KeyEvent event) {
        this.mTempRect.setEmpty();
        if (!canScroll()) {
            if (isFocused()) {
                View currentFocused = findFocus();
                if (currentFocused == this) {
                    currentFocused = null;
                }
                View nextFocused = FocusFinder.getInstance().findNextFocus(this, currentFocused, TransportMediator.KEYCODE_MEDIA_RECORD);
                return (nextFocused == null || nextFocused == this || !nextFocused.requestFocus(TransportMediator.KEYCODE_MEDIA_RECORD)) ? false : true;
            }
            return false;
        }
        boolean handled = false;
        if (event.getAction() == 0) {
            switch (event.getKeyCode()) {
                case 19:
                    if (!event.isAltPressed()) {
                        handled = arrowScroll(33, false);
                        break;
                    } else {
                        handled = fullScroll(33, false);
                        break;
                    }
                case 20:
                    if (!event.isAltPressed()) {
                        handled = arrowScroll(TransportMediator.KEYCODE_MEDIA_RECORD, false);
                        break;
                    } else {
                        handled = fullScroll(TransportMediator.KEYCODE_MEDIA_RECORD, false);
                        break;
                    }
                case 21:
                    if (!event.isAltPressed()) {
                        handled = arrowScroll(17, true);
                        break;
                    } else {
                        handled = fullScroll(17, true);
                        break;
                    }
                case 22:
                    if (!event.isAltPressed()) {
                        handled = arrowScroll(66, true);
                        break;
                    } else {
                        handled = fullScroll(66, true);
                        break;
                    }
            }
        }
        return handled;
    }

    @Override // android.view.ViewGroup
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        if (action == 2 && this.mIsBeingDragged) {
            return true;
        }
        if (!canScroll()) {
            this.mIsBeingDragged = false;
            return false;
        }
        float y = ev.getY();
        float x = ev.getX();
        switch (action) {
            case 0:
                this.mLastMotionY = y;
                this.mLastMotionX = x;
                this.mIsBeingDragged = this.mScroller.isFinished() ? false : true;
                break;
            case 1:
            case 3:
                this.mIsBeingDragged = false;
                break;
            case 2:
                int yDiff = (int) Math.abs(y - this.mLastMotionY);
                int xDiff = (int) Math.abs(x - this.mLastMotionX);
                if (yDiff > this.mTouchSlop || xDiff > this.mTouchSlop) {
                    this.mIsBeingDragged = true;
                    break;
                }
                break;
        }
        return this.mIsBeingDragged;
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent ev) {
        if ((ev.getAction() != 0 || ev.getEdgeFlags() == 0) && canScroll()) {
            if (this.mVelocityTracker == null) {
                this.mVelocityTracker = VelocityTracker.obtain();
            }
            this.mVelocityTracker.addMovement(ev);
            int action = ev.getAction();
            float y = ev.getY();
            float x = ev.getX();
            switch (action) {
                case 0:
                    if (!this.mScroller.isFinished()) {
                        this.mScroller.abortAnimation();
                    }
                    this.mLastMotionY = y;
                    this.mLastMotionX = x;
                    break;
                case 1:
                    VelocityTracker velocityTracker = this.mVelocityTracker;
                    velocityTracker.computeCurrentVelocity(1000, this.mMaximumVelocity);
                    int initialXVelocity = (int) velocityTracker.getXVelocity();
                    int initialYVelocity = (int) velocityTracker.getYVelocity();
                    if (Math.abs(initialXVelocity) + Math.abs(initialYVelocity) > this.mMinimumVelocity && getChildCount() > 0) {
                        fling(-initialXVelocity, -initialYVelocity);
                    }
                    if (this.mVelocityTracker != null) {
                        this.mVelocityTracker.recycle();
                        this.mVelocityTracker = null;
                        break;
                    }
                    break;
                case 2:
                    int deltaX = (int) (this.mLastMotionX - x);
                    int deltaY = (int) (this.mLastMotionY - y);
                    this.mLastMotionX = x;
                    this.mLastMotionY = y;
                    if (deltaX < 0) {
                        if (getScrollX() < 0) {
                            deltaX = 0;
                        }
                    } else if (deltaX > 0) {
                        int rightEdge = getWidth() - getPaddingRight();
                        int availableToScroll = (getChildAt(0).getRight() - getScrollX()) - rightEdge;
                        deltaX = availableToScroll > 0 ? Math.min(availableToScroll, deltaX) : 0;
                    }
                    if (deltaY < 0) {
                        if (getScrollY() < 0) {
                            deltaY = 0;
                        }
                    } else if (deltaY > 0) {
                        int bottomEdge = getHeight() - getPaddingBottom();
                        int availableToScroll2 = (getChildAt(0).getBottom() - getScrollY()) - bottomEdge;
                        deltaY = availableToScroll2 > 0 ? Math.min(availableToScroll2, deltaY) : 0;
                    }
                    if (deltaY != 0 || deltaX != 0) {
                        scrollBy(deltaX, deltaY);
                        break;
                    }
            }
            return true;
        }
        return false;
    }

    private View findFocusableViewInMyBounds(boolean topFocus, int top, boolean leftFocus, int left, View preferredFocusable) {
        int verticalFadingEdgeLength = getVerticalFadingEdgeLength() / 2;
        int topWithoutFadingEdge = top + verticalFadingEdgeLength;
        int bottomWithoutFadingEdge = (getHeight() + top) - verticalFadingEdgeLength;
        int horizontalFadingEdgeLength = getHorizontalFadingEdgeLength() / 2;
        int leftWithoutFadingEdge = left + horizontalFadingEdgeLength;
        int rightWithoutFadingEdge = (getWidth() + left) - horizontalFadingEdgeLength;
        return (preferredFocusable == null || preferredFocusable.getTop() >= bottomWithoutFadingEdge || preferredFocusable.getBottom() <= topWithoutFadingEdge || preferredFocusable.getLeft() >= rightWithoutFadingEdge || preferredFocusable.getRight() <= leftWithoutFadingEdge) ? findFocusableViewInBounds(topFocus, topWithoutFadingEdge, bottomWithoutFadingEdge, leftFocus, leftWithoutFadingEdge, rightWithoutFadingEdge) : preferredFocusable;
    }

    private View findFocusableViewInBounds(boolean topFocus, int top, int bottom, boolean leftFocus, int left, int right) {
        List<View> focusables = getFocusables(2);
        View focusCandidate = null;
        boolean foundFullyContainedFocusable = false;
        int count = focusables.size();
        for (int i = 0; i < count; i++) {
            View view = focusables.get(i);
            int viewTop = view.getTop();
            int viewBottom = view.getBottom();
            int viewLeft = view.getLeft();
            int viewRight = view.getRight();
            if (top < viewBottom && viewTop < bottom && left < viewRight && viewLeft < right) {
                boolean viewIsFullyContained = top < viewTop && viewBottom < bottom && left < viewLeft && viewRight < right;
                if (focusCandidate == null) {
                    focusCandidate = view;
                    foundFullyContainedFocusable = viewIsFullyContained;
                } else {
                    boolean viewIsCloserToVerticalBoundary = (topFocus && viewTop < focusCandidate.getTop()) || (!topFocus && viewBottom > focusCandidate.getBottom());
                    boolean viewIsCloserToHorizontalBoundary = (leftFocus && viewLeft < focusCandidate.getLeft()) || (!leftFocus && viewRight > focusCandidate.getRight());
                    if (foundFullyContainedFocusable) {
                        if (viewIsFullyContained && viewIsCloserToVerticalBoundary && viewIsCloserToHorizontalBoundary) {
                            focusCandidate = view;
                        }
                    } else if (viewIsFullyContained) {
                        focusCandidate = view;
                        foundFullyContainedFocusable = true;
                    } else if (viewIsCloserToVerticalBoundary && viewIsCloserToHorizontalBoundary) {
                        focusCandidate = view;
                    }
                }
            }
        }
        return focusCandidate;
    }

    public boolean fullScroll(int direction, boolean horizontal) {
        int count;
        int count2;
        if (!horizontal) {
            boolean down = direction == 130;
            int height = getHeight();
            this.mTempRect.top = 0;
            this.mTempRect.bottom = height;
            if (down && (count2 = getChildCount()) > 0) {
                View view = getChildAt(count2 - 1);
                this.mTempRect.bottom = view.getBottom();
                this.mTempRect.top = this.mTempRect.bottom - height;
            }
            return scrollAndFocus(direction, this.mTempRect.top, this.mTempRect.bottom, 0, 0, 0);
        }
        boolean right = direction == 130;
        int width = getWidth();
        this.mTempRect.left = 0;
        this.mTempRect.right = width;
        if (right && (count = getChildCount()) > 0) {
            View view2 = getChildAt(count - 1);
            this.mTempRect.right = view2.getBottom();
            this.mTempRect.left = this.mTempRect.right - width;
        }
        return scrollAndFocus(0, 0, 0, direction, this.mTempRect.top, this.mTempRect.bottom);
    }

    private boolean scrollAndFocus(int directionY, int top, int bottom, int directionX, int left, int right) {
        boolean handled = true;
        int height = getHeight();
        int containerTop = getScrollY();
        int containerBottom = containerTop + height;
        boolean up = directionY == 33;
        int width = getWidth();
        int containerLeft = getScrollX();
        int containerRight = containerLeft + width;
        boolean leftwards = directionX == 33;
        View newFocused = findFocusableViewInBounds(up, top, bottom, leftwards, left, right);
        if (newFocused == null) {
            newFocused = this;
        }
        if ((top >= containerTop && bottom <= containerBottom) || (left >= containerLeft && right <= containerRight)) {
            handled = false;
        } else {
            int deltaY = up ? top - containerTop : bottom - containerBottom;
            int deltaX = leftwards ? left - containerLeft : right - containerRight;
            doScroll(deltaX, deltaY);
        }
        if (newFocused != findFocus() && newFocused.requestFocus(directionY)) {
            this.mTwoDScrollViewMovedFocus = true;
            this.mTwoDScrollViewMovedFocus = false;
        }
        return handled;
    }

    public boolean arrowScroll(int direction, boolean horizontal) {
        View currentFocused = findFocus();
        if (currentFocused == this) {
            currentFocused = null;
        }
        View nextFocused = FocusFinder.getInstance().findNextFocus(this, currentFocused, direction);
        int maxJump = horizontal ? getMaxScrollAmountHorizontal() : getMaxScrollAmountVertical();
        if (!horizontal) {
            if (nextFocused != null) {
                nextFocused.getDrawingRect(this.mTempRect);
                offsetDescendantRectToMyCoords(nextFocused, this.mTempRect);
                doScroll(0, computeScrollDeltaToGetChildRectOnScreen(this.mTempRect));
                nextFocused.requestFocus(direction);
            } else {
                int scrollDelta = maxJump;
                if (direction == 33 && getScrollY() < scrollDelta) {
                    scrollDelta = getScrollY();
                } else if (direction == 130 && getChildCount() > 0) {
                    int daBottom = getChildAt(0).getBottom();
                    int screenBottom = getScrollY() + getHeight();
                    if (daBottom - screenBottom < maxJump) {
                        scrollDelta = daBottom - screenBottom;
                    }
                }
                if (scrollDelta == 0) {
                    return false;
                }
                doScroll(0, direction == 130 ? scrollDelta : -scrollDelta);
            }
        } else if (nextFocused != null) {
            nextFocused.getDrawingRect(this.mTempRect);
            offsetDescendantRectToMyCoords(nextFocused, this.mTempRect);
            doScroll(computeScrollDeltaToGetChildRectOnScreen(this.mTempRect), 0);
            nextFocused.requestFocus(direction);
        } else {
            int scrollDelta2 = maxJump;
            if (direction == 33 && getScrollY() < scrollDelta2) {
                scrollDelta2 = getScrollY();
            } else if (direction == 130 && getChildCount() > 0) {
                int daBottom2 = getChildAt(0).getBottom();
                int screenBottom2 = getScrollY() + getHeight();
                if (daBottom2 - screenBottom2 < maxJump) {
                    scrollDelta2 = daBottom2 - screenBottom2;
                }
            }
            if (scrollDelta2 == 0) {
                return false;
            }
            doScroll(direction == 130 ? scrollDelta2 : -scrollDelta2, 0);
        }
        return true;
    }

    private void doScroll(int deltaX, int deltaY) {
        if (deltaX != 0 || deltaY != 0) {
            smoothScrollBy(deltaX, deltaY);
        }
    }

    public final void smoothScrollBy(int dx, int dy) {
        long duration = AnimationUtils.currentAnimationTimeMillis() - this.mLastScroll;
        if (duration > 250) {
            this.mScroller.startScroll(getScrollX(), getScrollY(), dx, dy);
            awakenScrollBars(this.mScroller.getDuration());
            invalidate();
        } else {
            if (!this.mScroller.isFinished()) {
                this.mScroller.abortAnimation();
            }
            scrollBy(dx, dy);
        }
        this.mLastScroll = AnimationUtils.currentAnimationTimeMillis();
    }

    public final void smoothScrollTo(int x, int y) {
        smoothScrollBy(x - getScrollX(), y - getScrollY());
    }

    @Override // android.view.View
    protected int computeVerticalScrollRange() {
        int count = getChildCount();
        return count == 0 ? getHeight() : getChildAt(0).getBottom();
    }

    @Override // android.view.View
    protected int computeHorizontalScrollRange() {
        int count = getChildCount();
        return count == 0 ? getWidth() : getChildAt(0).getRight();
    }

    @Override // android.view.ViewGroup
    protected void measureChild(View child, int parentWidthMeasureSpec, int parentHeightMeasureSpec) {
        ViewGroup.LayoutParams lp = child.getLayoutParams();
        int childWidthMeasureSpec = getChildMeasureSpec(parentWidthMeasureSpec, getPaddingLeft() + getPaddingRight(), lp.width);
        int childHeightMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, 0);
        child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
    }

    @Override // android.view.ViewGroup
    protected void measureChildWithMargins(View child, int parentWidthMeasureSpec, int widthUsed, int parentHeightMeasureSpec, int heightUsed) {
        ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) child.getLayoutParams();
        int childWidthMeasureSpec = View.MeasureSpec.makeMeasureSpec(lp.leftMargin + lp.rightMargin, 0);
        int childHeightMeasureSpec = View.MeasureSpec.makeMeasureSpec(lp.topMargin + lp.bottomMargin, 0);
        child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
    }

    @Override // android.view.View
    public void computeScroll() {
        if (this.mScroller.computeScrollOffset()) {
            int oldX = getScrollX();
            int oldY = getScrollY();
            int x = this.mScroller.getCurrX();
            int y = this.mScroller.getCurrY();
            if (getChildCount() > 0) {
                View child = getChildAt(0);
                scrollTo(clamp(x, (getWidth() - getPaddingRight()) - getPaddingLeft(), child.getWidth()), clamp(y, (getHeight() - getPaddingBottom()) - getPaddingTop(), child.getHeight()));
            } else {
                scrollTo(x, y);
            }
            if (oldX != getScrollX() || oldY != getScrollY()) {
                onScrollChanged(getScrollX(), getScrollY(), oldX, oldY);
            }
            postInvalidate();
        }
    }

    private void scrollToChild(View child) {
        child.getDrawingRect(this.mTempRect);
        offsetDescendantRectToMyCoords(child, this.mTempRect);
        int scrollDelta = computeScrollDeltaToGetChildRectOnScreen(this.mTempRect);
        if (scrollDelta != 0) {
            scrollBy(0, scrollDelta);
        }
    }

    private boolean scrollToChildRect(Rect rect, boolean immediate) {
        int delta = computeScrollDeltaToGetChildRectOnScreen(rect);
        boolean scroll = delta != 0;
        if (scroll) {
            if (immediate) {
                scrollBy(0, delta);
            } else {
                smoothScrollBy(0, delta);
            }
        }
        return scroll;
    }

    protected int computeScrollDeltaToGetChildRectOnScreen(Rect rect) {
        if (getChildCount() == 0) {
            return 0;
        }
        int height = getHeight();
        int screenTop = getScrollY();
        int screenBottom = screenTop + height;
        int fadingEdge = getVerticalFadingEdgeLength();
        if (rect.top > 0) {
            screenTop += fadingEdge;
        }
        if (rect.bottom < getChildAt(0).getHeight()) {
            screenBottom -= fadingEdge;
        }
        if (rect.bottom > screenBottom && rect.top > screenTop) {
            int scrollYDelta = rect.height() > height ? 0 + (rect.top - screenTop) : 0 + (rect.bottom - screenBottom);
            int bottom = getChildAt(0).getBottom();
            int distanceToBottom = bottom - screenBottom;
            return Math.min(scrollYDelta, distanceToBottom);
        } else if (rect.top >= screenTop || rect.bottom >= screenBottom) {
            return 0;
        } else {
            int scrollYDelta2 = rect.height() > height ? 0 - (screenBottom - rect.bottom) : 0 - (screenTop - rect.top);
            return Math.max(scrollYDelta2, -getScrollY());
        }
    }

    @Override // android.view.ViewGroup, android.view.ViewParent
    public void requestChildFocus(View child, View focused) {
        if (!this.mTwoDScrollViewMovedFocus) {
            if (!this.mIsLayoutDirty) {
                scrollToChild(focused);
            } else {
                this.mChildToScrollTo = focused;
            }
        }
        super.requestChildFocus(child, focused);
    }

    @Override // android.view.ViewGroup
    protected boolean onRequestFocusInDescendants(int direction, Rect previouslyFocusedRect) {
        if (direction == 2) {
            direction = TransportMediator.KEYCODE_MEDIA_RECORD;
        } else if (direction == 1) {
            direction = 33;
        }
        View nextFocus = previouslyFocusedRect == null ? FocusFinder.getInstance().findNextFocus(this, null, direction) : FocusFinder.getInstance().findNextFocusFromRect(this, previouslyFocusedRect, direction);
        if (nextFocus == null) {
            return false;
        }
        return nextFocus.requestFocus(direction, previouslyFocusedRect);
    }

    @Override // android.view.ViewGroup, android.view.ViewParent
    public boolean requestChildRectangleOnScreen(View child, Rect rectangle, boolean immediate) {
        rectangle.offset(child.getLeft() - child.getScrollX(), child.getTop() - child.getScrollY());
        return scrollToChildRect(rectangle, immediate);
    }

    @Override // android.view.View, android.view.ViewParent
    public void requestLayout() {
        this.mIsLayoutDirty = true;
        super.requestLayout();
    }

    @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        this.mIsLayoutDirty = false;
        if (this.mChildToScrollTo != null && isViewDescendantOf(this.mChildToScrollTo, this)) {
            scrollToChild(this.mChildToScrollTo);
        }
        this.mChildToScrollTo = null;
        scrollTo(getScrollX(), getScrollY());
    }

    @Override // android.view.View
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        View currentFocused = findFocus();
        if (currentFocused != null && this != currentFocused) {
            currentFocused.getDrawingRect(this.mTempRect);
            offsetDescendantRectToMyCoords(currentFocused, this.mTempRect);
            int scrollDeltaX = computeScrollDeltaToGetChildRectOnScreen(this.mTempRect);
            int scrollDeltaY = computeScrollDeltaToGetChildRectOnScreen(this.mTempRect);
            doScroll(scrollDeltaX, scrollDeltaY);
        }
    }

    private boolean isViewDescendantOf(View child, View parent) {
        if (child == parent) {
            return true;
        }
        ViewParent theParent = child.getParent();
        return (theParent instanceof ViewGroup) && isViewDescendantOf((View) theParent, parent);
    }

    public void fling(int velocityX, int velocityY) {
        if (getChildCount() > 0) {
            int height = (getHeight() - getPaddingBottom()) - getPaddingTop();
            int bottom = getChildAt(0).getHeight();
            int width = (getWidth() - getPaddingRight()) - getPaddingLeft();
            int right = getChildAt(0).getWidth();
            this.mScroller.fling(getScrollX(), getScrollY(), velocityX, velocityY, 0, right - width, 0, bottom - height);
            boolean movingDown = velocityY > 0;
            boolean movingRight = velocityX > 0;
            View newFocused = findFocusableViewInMyBounds(movingRight, this.mScroller.getFinalX(), movingDown, this.mScroller.getFinalY(), findFocus());
            if (newFocused == null) {
                newFocused = this;
            }
            if (newFocused != findFocus()) {
                if (newFocused.requestFocus(movingDown ? TransportMediator.KEYCODE_MEDIA_RECORD : 33)) {
                    this.mTwoDScrollViewMovedFocus = true;
                    this.mTwoDScrollViewMovedFocus = false;
                }
            }
            awakenScrollBars(this.mScroller.getDuration());
            invalidate();
        }
    }

    @Override // android.view.View
    public void scrollTo(int x, int y) {
        if (getChildCount() > 0) {
            View child = getChildAt(0);
            int x2 = clamp(x, (getWidth() - getPaddingRight()) - getPaddingLeft(), child.getWidth());
            int y2 = clamp(y, (getHeight() - getPaddingBottom()) - getPaddingTop(), child.getHeight());
            if (x2 != getScrollX() || y2 != getScrollY()) {
                super.scrollTo(x2, y2);
            }
        }
    }

    private int clamp(int n, int my, int child) {
        if (my >= child || n < 0) {
            return 0;
        }
        if (my + n > child) {
            return child - my;
        }
        return n;
    }
}

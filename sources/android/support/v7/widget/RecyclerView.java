package android.support.v7.widget;

import android.content.Context;
import android.database.Observable;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.util.ArrayMap;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.VelocityTrackerCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewConfigurationCompat;
import android.support.v4.view.accessibility.AccessibilityEventCompat;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.support.v4.view.accessibility.AccessibilityRecordCompat;
import android.support.v4.widget.EdgeEffectCompat;
import android.support.v4.widget.ScrollerCompat;
import android.support.v7.widget.AdapterHelper;
import android.support.v7.widget.ChildHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.FocusFinder;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.view.animation.Interpolator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
/* loaded from: classes.dex */
public class RecyclerView extends ViewGroup {
    private static final boolean DEBUG = false;
    private static final boolean DISPATCH_TEMP_DETACH = false;
    private static final boolean FORCE_INVALIDATE_DISPLAY_LIST;
    public static final int HORIZONTAL = 0;
    private static final int INVALID_POINTER = -1;
    public static final int INVALID_TYPE = -1;
    private static final int MAX_SCROLL_DURATION = 2000;
    public static final long NO_ID = -1;
    public static final int NO_POSITION = -1;
    public static final int SCROLL_STATE_DRAGGING = 1;
    public static final int SCROLL_STATE_IDLE = 0;
    public static final int SCROLL_STATE_SETTLING = 2;
    private static final String TAG = "RecyclerView";
    public static final int TOUCH_SLOP_DEFAULT = 0;
    public static final int TOUCH_SLOP_PAGING = 1;
    public static final int VERTICAL = 1;
    private static final Interpolator sQuinticInterpolator;
    private RecyclerViewAccessibilityDelegate mAccessibilityDelegate;
    private final AccessibilityManager mAccessibilityManager;
    private OnItemTouchListener mActiveOnItemTouchListener;
    private Adapter mAdapter;
    AdapterHelper mAdapterHelper;
    private boolean mAdapterUpdateDuringMeasure;
    private EdgeEffectCompat mBottomGlow;
    ChildHelper mChildHelper;
    private boolean mClipToPadding;
    private boolean mDataSetHasChangedAfterLayout;
    final List<View> mDisappearingViewsInLayoutPass;
    private boolean mEatRequestLayout;
    private boolean mFirstLayoutComplete;
    private boolean mHasFixedSize;
    private int mInitialTouchX;
    private int mInitialTouchY;
    private boolean mIsAttached;
    ItemAnimator mItemAnimator;
    private ItemAnimator.ItemAnimatorListener mItemAnimatorListener;
    private Runnable mItemAnimatorRunner;
    private final ArrayList<ItemDecoration> mItemDecorations;
    boolean mItemsAddedOrRemoved;
    boolean mItemsChanged;
    private int mLastTouchX;
    private int mLastTouchY;
    private LayoutManager mLayout;
    private boolean mLayoutRequestEaten;
    private EdgeEffectCompat mLeftGlow;
    private final int mMaxFlingVelocity;
    private final int mMinFlingVelocity;
    private final RecyclerViewDataObserver mObserver;
    private final ArrayList<OnItemTouchListener> mOnItemTouchListeners;
    private SavedState mPendingSavedState;
    private final boolean mPostUpdatesOnAnimation;
    private boolean mPostedAnimatorRunner;
    final Recycler mRecycler;
    private RecyclerListener mRecyclerListener;
    private EdgeEffectCompat mRightGlow;
    private boolean mRunningLayoutOrScroll;
    private OnScrollListener mScrollListener;
    private int mScrollPointerId;
    private int mScrollState;
    final State mState;
    private final Rect mTempRect;
    private EdgeEffectCompat mTopGlow;
    private int mTouchSlop;
    private final Runnable mUpdateChildViewsRunnable;
    private VelocityTracker mVelocityTracker;
    private final ViewFlinger mViewFlinger;

    /* loaded from: classes.dex */
    public interface OnItemTouchListener {
        boolean onInterceptTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent);

        void onTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent);
    }

    /* loaded from: classes.dex */
    public interface RecyclerListener {
        void onViewRecycled(ViewHolder viewHolder);
    }

    /* loaded from: classes.dex */
    public static abstract class ViewCacheExtension {
        public abstract View getViewForPositionAndType(Recycler recycler, int i, int i2);
    }

    static {
        FORCE_INVALIDATE_DISPLAY_LIST = Build.VERSION.SDK_INT == 19 || Build.VERSION.SDK_INT == 20;
        sQuinticInterpolator = new Interpolator() { // from class: android.support.v7.widget.RecyclerView.3
            @Override // android.animation.TimeInterpolator
            public float getInterpolation(float t) {
                float t2 = t - 1.0f;
                return (t2 * t2 * t2 * t2 * t2) + 1.0f;
            }
        };
    }

    public RecyclerView(Context context) {
        this(context, null);
    }

    public RecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mObserver = new RecyclerViewDataObserver();
        this.mRecycler = new Recycler();
        this.mDisappearingViewsInLayoutPass = new ArrayList();
        this.mUpdateChildViewsRunnable = new Runnable() { // from class: android.support.v7.widget.RecyclerView.1
            @Override // java.lang.Runnable
            public void run() {
                if (RecyclerView.this.mFirstLayoutComplete) {
                    if (RecyclerView.this.mDataSetHasChangedAfterLayout) {
                        RecyclerView.this.dispatchLayout();
                    } else if (RecyclerView.this.mAdapterHelper.hasPendingUpdates()) {
                        RecyclerView.this.eatRequestLayout();
                        RecyclerView.this.mAdapterHelper.preProcess();
                        if (!RecyclerView.this.mLayoutRequestEaten) {
                            RecyclerView.this.rebindUpdatedViewHolders();
                        }
                        RecyclerView.this.resumeRequestLayout(true);
                    }
                }
            }
        };
        this.mTempRect = new Rect();
        this.mItemDecorations = new ArrayList<>();
        this.mOnItemTouchListeners = new ArrayList<>();
        this.mDataSetHasChangedAfterLayout = false;
        this.mRunningLayoutOrScroll = false;
        this.mItemAnimator = new DefaultItemAnimator();
        this.mScrollState = 0;
        this.mScrollPointerId = -1;
        this.mViewFlinger = new ViewFlinger();
        this.mState = new State();
        this.mItemsAddedOrRemoved = false;
        this.mItemsChanged = false;
        this.mItemAnimatorListener = new ItemAnimatorRestoreListener();
        this.mPostedAnimatorRunner = false;
        this.mItemAnimatorRunner = new Runnable() { // from class: android.support.v7.widget.RecyclerView.2
            @Override // java.lang.Runnable
            public void run() {
                if (RecyclerView.this.mItemAnimator != null) {
                    RecyclerView.this.mItemAnimator.runPendingAnimations();
                }
                RecyclerView.this.mPostedAnimatorRunner = false;
            }
        };
        int version = Build.VERSION.SDK_INT;
        this.mPostUpdatesOnAnimation = version >= 16;
        ViewConfiguration vc = ViewConfiguration.get(context);
        this.mTouchSlop = vc.getScaledTouchSlop();
        this.mMinFlingVelocity = vc.getScaledMinimumFlingVelocity();
        this.mMaxFlingVelocity = vc.getScaledMaximumFlingVelocity();
        setWillNotDraw(ViewCompat.getOverScrollMode(this) == 2);
        this.mItemAnimator.setListener(this.mItemAnimatorListener);
        initAdapterManager();
        initChildrenHelper();
        if (ViewCompat.getImportantForAccessibility(this) == 0) {
            ViewCompat.setImportantForAccessibility(this, 1);
        }
        this.mAccessibilityManager = (AccessibilityManager) getContext().getSystemService("accessibility");
        setAccessibilityDelegateCompat(new RecyclerViewAccessibilityDelegate(this));
    }

    public RecyclerViewAccessibilityDelegate getCompatAccessibilityDelegate() {
        return this.mAccessibilityDelegate;
    }

    public void setAccessibilityDelegateCompat(RecyclerViewAccessibilityDelegate accessibilityDelegate) {
        this.mAccessibilityDelegate = accessibilityDelegate;
        ViewCompat.setAccessibilityDelegate(this, this.mAccessibilityDelegate);
    }

    private void initChildrenHelper() {
        this.mChildHelper = new ChildHelper(new ChildHelper.Callback() { // from class: android.support.v7.widget.RecyclerView.4
            @Override // android.support.v7.widget.ChildHelper.Callback
            public int getChildCount() {
                return RecyclerView.this.getChildCount();
            }

            @Override // android.support.v7.widget.ChildHelper.Callback
            public void addView(View child, int index) {
                RecyclerView.this.addView(child, index);
                RecyclerView.this.dispatchChildAttached(child);
            }

            @Override // android.support.v7.widget.ChildHelper.Callback
            public int indexOfChild(View view) {
                return RecyclerView.this.indexOfChild(view);
            }

            @Override // android.support.v7.widget.ChildHelper.Callback
            public void removeViewAt(int index) {
                View child = RecyclerView.this.getChildAt(index);
                if (child != null) {
                    RecyclerView.this.dispatchChildDetached(child);
                }
                RecyclerView.this.removeViewAt(index);
            }

            @Override // android.support.v7.widget.ChildHelper.Callback
            public View getChildAt(int offset) {
                return RecyclerView.this.getChildAt(offset);
            }

            @Override // android.support.v7.widget.ChildHelper.Callback
            public void removeAllViews() {
                int count = getChildCount();
                for (int i = 0; i < count; i++) {
                    RecyclerView.this.dispatchChildDetached(getChildAt(i));
                }
                RecyclerView.this.removeAllViews();
            }

            @Override // android.support.v7.widget.ChildHelper.Callback
            public ViewHolder getChildViewHolder(View view) {
                return RecyclerView.getChildViewHolderInt(view);
            }

            @Override // android.support.v7.widget.ChildHelper.Callback
            public void attachViewToParent(View child, int index, ViewGroup.LayoutParams layoutParams) {
                ViewHolder vh = RecyclerView.getChildViewHolderInt(child);
                if (vh != null) {
                    if (!vh.isTmpDetached() && !vh.shouldIgnore()) {
                        throw new IllegalArgumentException("Called attach on a child which is not detached: " + vh);
                    }
                    vh.clearTmpDetachFlag();
                }
                RecyclerView.this.attachViewToParent(child, index, layoutParams);
            }

            @Override // android.support.v7.widget.ChildHelper.Callback
            public void detachViewFromParent(int offset) {
                ViewHolder vh;
                View view = getChildAt(offset);
                if (view != null && (vh = RecyclerView.getChildViewHolderInt(view)) != null) {
                    if (vh.isTmpDetached() && !vh.shouldIgnore()) {
                        throw new IllegalArgumentException("called detach on an already detached child " + vh);
                    }
                    vh.addFlags(256);
                }
                RecyclerView.this.detachViewFromParent(offset);
            }
        });
    }

    void initAdapterManager() {
        this.mAdapterHelper = new AdapterHelper(new AdapterHelper.Callback() { // from class: android.support.v7.widget.RecyclerView.5
            @Override // android.support.v7.widget.AdapterHelper.Callback
            public ViewHolder findViewHolder(int position) {
                return RecyclerView.this.findViewHolderForPosition(position, true);
            }

            @Override // android.support.v7.widget.AdapterHelper.Callback
            public void offsetPositionsForRemovingInvisible(int start, int count) {
                RecyclerView.this.offsetPositionRecordsForRemove(start, count, true);
                RecyclerView.this.mItemsAddedOrRemoved = true;
                State.access$1012(RecyclerView.this.mState, count);
            }

            @Override // android.support.v7.widget.AdapterHelper.Callback
            public void offsetPositionsForRemovingLaidOutOrNewView(int positionStart, int itemCount) {
                RecyclerView.this.offsetPositionRecordsForRemove(positionStart, itemCount, false);
                RecyclerView.this.mItemsAddedOrRemoved = true;
            }

            @Override // android.support.v7.widget.AdapterHelper.Callback
            public void markViewHoldersUpdated(int positionStart, int itemCount) {
                RecyclerView.this.viewRangeUpdate(positionStart, itemCount);
                RecyclerView.this.mItemsChanged = true;
            }

            @Override // android.support.v7.widget.AdapterHelper.Callback
            public void onDispatchFirstPass(AdapterHelper.UpdateOp op) {
                dispatchUpdate(op);
            }

            void dispatchUpdate(AdapterHelper.UpdateOp op) {
                switch (op.cmd) {
                    case 0:
                        RecyclerView.this.mLayout.onItemsAdded(RecyclerView.this, op.positionStart, op.itemCount);
                        return;
                    case 1:
                        RecyclerView.this.mLayout.onItemsRemoved(RecyclerView.this, op.positionStart, op.itemCount);
                        return;
                    case 2:
                        RecyclerView.this.mLayout.onItemsUpdated(RecyclerView.this, op.positionStart, op.itemCount);
                        return;
                    case 3:
                        RecyclerView.this.mLayout.onItemsMoved(RecyclerView.this, op.positionStart, op.itemCount, 1);
                        return;
                    default:
                        return;
                }
            }

            @Override // android.support.v7.widget.AdapterHelper.Callback
            public void onDispatchSecondPass(AdapterHelper.UpdateOp op) {
                dispatchUpdate(op);
            }

            @Override // android.support.v7.widget.AdapterHelper.Callback
            public void offsetPositionsForAdd(int positionStart, int itemCount) {
                RecyclerView.this.offsetPositionRecordsForInsert(positionStart, itemCount);
                RecyclerView.this.mItemsAddedOrRemoved = true;
            }

            @Override // android.support.v7.widget.AdapterHelper.Callback
            public void offsetPositionsForMove(int from, int to) {
                RecyclerView.this.offsetPositionRecordsForMove(from, to);
                RecyclerView.this.mItemsAddedOrRemoved = true;
            }
        });
    }

    public void setHasFixedSize(boolean hasFixedSize) {
        this.mHasFixedSize = hasFixedSize;
    }

    public boolean hasFixedSize() {
        return this.mHasFixedSize;
    }

    @Override // android.view.ViewGroup
    public void setClipToPadding(boolean clipToPadding) {
        if (clipToPadding != this.mClipToPadding) {
            invalidateGlows();
        }
        this.mClipToPadding = clipToPadding;
        super.setClipToPadding(clipToPadding);
        if (this.mFirstLayoutComplete) {
            requestLayout();
        }
    }

    public void setScrollingTouchSlop(int slopConstant) {
        ViewConfiguration vc = ViewConfiguration.get(getContext());
        switch (slopConstant) {
            case 0:
                break;
            case 1:
                this.mTouchSlop = ViewConfigurationCompat.getScaledPagingTouchSlop(vc);
                return;
            default:
                Log.w(TAG, "setScrollingTouchSlop(): bad argument constant " + slopConstant + "; using default value");
                break;
        }
        this.mTouchSlop = vc.getScaledTouchSlop();
    }

    public void swapAdapter(Adapter adapter, boolean removeAndRecycleExistingViews) {
        setAdapterInternal(adapter, true, removeAndRecycleExistingViews);
        this.mDataSetHasChangedAfterLayout = true;
        requestLayout();
    }

    public void setAdapter(Adapter adapter) {
        setAdapterInternal(adapter, false, true);
        requestLayout();
    }

    private void setAdapterInternal(Adapter adapter, boolean compatibleWithPrevious, boolean removeAndRecycleViews) {
        if (this.mAdapter != null) {
            this.mAdapter.unregisterAdapterDataObserver(this.mObserver);
            this.mAdapter.onDetachedFromRecyclerView(this);
        }
        if (!compatibleWithPrevious || removeAndRecycleViews) {
            if (this.mItemAnimator != null) {
                this.mItemAnimator.endAnimations();
            }
            if (this.mLayout != null) {
                this.mLayout.removeAndRecycleAllViews(this.mRecycler);
                this.mLayout.removeAndRecycleScrapInt(this.mRecycler);
            }
        }
        this.mAdapterHelper.reset();
        Adapter oldAdapter = this.mAdapter;
        this.mAdapter = adapter;
        if (adapter != null) {
            adapter.registerAdapterDataObserver(this.mObserver);
            adapter.onAttachedToRecyclerView(this);
        }
        if (this.mLayout != null) {
            this.mLayout.onAdapterChanged(oldAdapter, this.mAdapter);
        }
        this.mRecycler.onAdapterChanged(oldAdapter, this.mAdapter, compatibleWithPrevious);
        this.mState.mStructureChanged = true;
        markKnownViewsInvalid();
    }

    public Adapter getAdapter() {
        return this.mAdapter;
    }

    public void setRecyclerListener(RecyclerListener listener) {
        this.mRecyclerListener = listener;
    }

    public void setLayoutManager(LayoutManager layout) {
        if (layout != this.mLayout) {
            if (this.mLayout != null) {
                if (this.mIsAttached) {
                    this.mLayout.onDetachedFromWindow(this, this.mRecycler);
                }
                this.mLayout.setRecyclerView(null);
            }
            this.mRecycler.clear();
            this.mChildHelper.removeAllViewsUnfiltered();
            this.mLayout = layout;
            if (layout != null) {
                if (layout.mRecyclerView != null) {
                    throw new IllegalArgumentException("LayoutManager " + layout + " is already attached to a RecyclerView: " + layout.mRecyclerView);
                }
                this.mLayout.setRecyclerView(this);
                if (this.mIsAttached) {
                    this.mLayout.onAttachedToWindow(this);
                }
            }
            requestLayout();
        }
    }

    @Override // android.view.View
    protected Parcelable onSaveInstanceState() {
        SavedState state = new SavedState(super.onSaveInstanceState());
        if (this.mPendingSavedState == null) {
            if (this.mLayout != null) {
                state.mLayoutState = this.mLayout.onSaveInstanceState();
            } else {
                state.mLayoutState = null;
            }
        } else {
            state.copyFrom(this.mPendingSavedState);
        }
        return state;
    }

    @Override // android.view.View
    protected void onRestoreInstanceState(Parcelable state) {
        this.mPendingSavedState = (SavedState) state;
        super.onRestoreInstanceState(this.mPendingSavedState.getSuperState());
        if (this.mLayout != null && this.mPendingSavedState.mLayoutState != null) {
            this.mLayout.onRestoreInstanceState(this.mPendingSavedState.mLayoutState);
        }
    }

    private void addAnimatingView(ViewHolder viewHolder) {
        View view = viewHolder.itemView;
        boolean alreadyParented = view.getParent() == this;
        this.mRecycler.unscrapView(getChildViewHolder(view));
        if (viewHolder.isTmpDetached()) {
            this.mChildHelper.attachViewToParent(view, -1, view.getLayoutParams(), true);
        } else if (!alreadyParented) {
            this.mChildHelper.addView(view, true);
        } else {
            this.mChildHelper.hide(view);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean removeAnimatingView(View view) {
        eatRequestLayout();
        boolean removed = this.mChildHelper.removeViewIfHidden(view);
        if (removed) {
            ViewHolder viewHolder = getChildViewHolderInt(view);
            this.mRecycler.unscrapView(viewHolder);
            this.mRecycler.recycleViewHolderInternal(viewHolder);
        }
        resumeRequestLayout(false);
        return removed;
    }

    public LayoutManager getLayoutManager() {
        return this.mLayout;
    }

    public RecycledViewPool getRecycledViewPool() {
        return this.mRecycler.getRecycledViewPool();
    }

    public void setRecycledViewPool(RecycledViewPool pool) {
        this.mRecycler.setRecycledViewPool(pool);
    }

    public void setViewCacheExtension(ViewCacheExtension extension) {
        this.mRecycler.setViewCacheExtension(extension);
    }

    public void setItemViewCacheSize(int size) {
        this.mRecycler.setViewCacheSize(size);
    }

    public int getScrollState() {
        return this.mScrollState;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setScrollState(int state) {
        if (state != this.mScrollState) {
            this.mScrollState = state;
            if (state != 2) {
                stopScrollersInternal();
            }
            if (this.mScrollListener != null) {
                this.mScrollListener.onScrollStateChanged(this, state);
            }
            this.mLayout.onScrollStateChanged(state);
        }
    }

    public void addItemDecoration(ItemDecoration decor, int index) {
        if (this.mLayout != null) {
            this.mLayout.assertNotInLayoutOrScroll("Cannot add item decoration during a scroll  or layout");
        }
        if (this.mItemDecorations.isEmpty()) {
            setWillNotDraw(false);
        }
        if (index < 0) {
            this.mItemDecorations.add(decor);
        } else {
            this.mItemDecorations.add(index, decor);
        }
        markItemDecorInsetsDirty();
        requestLayout();
    }

    public void addItemDecoration(ItemDecoration decor) {
        addItemDecoration(decor, -1);
    }

    public void removeItemDecoration(ItemDecoration decor) {
        if (this.mLayout != null) {
            this.mLayout.assertNotInLayoutOrScroll("Cannot remove item decoration during a scroll  or layout");
        }
        this.mItemDecorations.remove(decor);
        if (this.mItemDecorations.isEmpty()) {
            setWillNotDraw(ViewCompat.getOverScrollMode(this) == 2);
        }
        markItemDecorInsetsDirty();
        requestLayout();
    }

    public void setOnScrollListener(OnScrollListener listener) {
        this.mScrollListener = listener;
    }

    public void scrollToPosition(int position) {
        stopScroll();
        this.mLayout.scrollToPosition(position);
        awakenScrollBars();
    }

    public void smoothScrollToPosition(int position) {
        this.mLayout.smoothScrollToPosition(this, this.mState, position);
    }

    @Override // android.view.View
    public void scrollTo(int x, int y) {
        throw new UnsupportedOperationException("RecyclerView does not support scrolling to an absolute position.");
    }

    @Override // android.view.View
    public void scrollBy(int x, int y) {
        if (this.mLayout == null) {
            throw new IllegalStateException("Cannot scroll without a LayoutManager set. Call setLayoutManager with a non-null argument.");
        }
        boolean canScrollHorizontal = this.mLayout.canScrollHorizontally();
        boolean canScrollVertical = this.mLayout.canScrollVertically();
        if (canScrollHorizontal || canScrollVertical) {
            if (!canScrollHorizontal) {
                x = 0;
            }
            if (!canScrollVertical) {
                y = 0;
            }
            scrollByInternal(x, y);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void consumePendingUpdateOperations() {
        this.mUpdateChildViewsRunnable.run();
    }

    void scrollByInternal(int x, int y) {
        int overscrollX = 0;
        int overscrollY = 0;
        int hresult = 0;
        int vresult = 0;
        consumePendingUpdateOperations();
        if (this.mAdapter != null) {
            eatRequestLayout();
            this.mRunningLayoutOrScroll = true;
            if (x != 0) {
                hresult = this.mLayout.scrollHorizontallyBy(x, this.mRecycler, this.mState);
                overscrollX = x - hresult;
            }
            if (y != 0) {
                vresult = this.mLayout.scrollVerticallyBy(y, this.mRecycler, this.mState);
                overscrollY = y - vresult;
            }
            if (supportsChangeAnimations()) {
                int count = this.mChildHelper.getChildCount();
                for (int i = 0; i < count; i++) {
                    View view = this.mChildHelper.getChildAt(i);
                    ViewHolder holder = getChildViewHolder(view);
                    if (holder != null && holder.mShadowingHolder != null) {
                        ViewHolder shadowingHolder = holder.mShadowingHolder;
                        View shadowingView = shadowingHolder != null ? shadowingHolder.itemView : null;
                        if (shadowingView != null) {
                            int left = view.getLeft();
                            int top = view.getTop();
                            if (left != shadowingView.getLeft() || top != shadowingView.getTop()) {
                                shadowingView.layout(left, top, shadowingView.getWidth() + left, shadowingView.getHeight() + top);
                            }
                        }
                    }
                }
            }
            this.mRunningLayoutOrScroll = false;
            resumeRequestLayout(false);
        }
        if (!this.mItemDecorations.isEmpty()) {
            invalidate();
        }
        if (ViewCompat.getOverScrollMode(this) != 2) {
            considerReleasingGlowsOnScroll(x, y);
            pullGlows(overscrollX, overscrollY);
        }
        if (hresult != 0 || vresult != 0) {
            onScrollChanged(0, 0, 0, 0);
            if (this.mScrollListener != null) {
                this.mScrollListener.onScrolled(this, hresult, vresult);
            }
        }
        if (!awakenScrollBars()) {
            invalidate();
        }
    }

    @Override // android.view.View
    protected int computeHorizontalScrollOffset() {
        if (this.mLayout.canScrollHorizontally()) {
            return this.mLayout.computeHorizontalScrollOffset(this.mState);
        }
        return 0;
    }

    @Override // android.view.View
    protected int computeHorizontalScrollExtent() {
        if (this.mLayout.canScrollHorizontally()) {
            return this.mLayout.computeHorizontalScrollExtent(this.mState);
        }
        return 0;
    }

    @Override // android.view.View
    protected int computeHorizontalScrollRange() {
        if (this.mLayout.canScrollHorizontally()) {
            return this.mLayout.computeHorizontalScrollRange(this.mState);
        }
        return 0;
    }

    @Override // android.view.View
    protected int computeVerticalScrollOffset() {
        if (this.mLayout.canScrollVertically()) {
            return this.mLayout.computeVerticalScrollOffset(this.mState);
        }
        return 0;
    }

    @Override // android.view.View
    protected int computeVerticalScrollExtent() {
        if (this.mLayout.canScrollVertically()) {
            return this.mLayout.computeVerticalScrollExtent(this.mState);
        }
        return 0;
    }

    @Override // android.view.View
    protected int computeVerticalScrollRange() {
        if (this.mLayout.canScrollVertically()) {
            return this.mLayout.computeVerticalScrollRange(this.mState);
        }
        return 0;
    }

    void eatRequestLayout() {
        if (!this.mEatRequestLayout) {
            this.mEatRequestLayout = true;
            this.mLayoutRequestEaten = false;
        }
    }

    void resumeRequestLayout(boolean performLayoutChildren) {
        if (this.mEatRequestLayout) {
            if (performLayoutChildren && this.mLayoutRequestEaten && this.mLayout != null && this.mAdapter != null) {
                dispatchLayout();
            }
            this.mEatRequestLayout = false;
            this.mLayoutRequestEaten = false;
        }
    }

    public void smoothScrollBy(int dx, int dy) {
        if (dx != 0 || dy != 0) {
            this.mViewFlinger.smoothScrollBy(dx, dy);
        }
    }

    public boolean fling(int velocityX, int velocityY) {
        if (Math.abs(velocityX) < this.mMinFlingVelocity) {
            velocityX = 0;
        }
        if (Math.abs(velocityY) < this.mMinFlingVelocity) {
            velocityY = 0;
        }
        int velocityX2 = Math.max(-this.mMaxFlingVelocity, Math.min(velocityX, this.mMaxFlingVelocity));
        int velocityY2 = Math.max(-this.mMaxFlingVelocity, Math.min(velocityY, this.mMaxFlingVelocity));
        if (velocityX2 == 0 && velocityY2 == 0) {
            return false;
        }
        this.mViewFlinger.fling(velocityX2, velocityY2);
        return true;
    }

    public void stopScroll() {
        setScrollState(0);
        stopScrollersInternal();
    }

    private void stopScrollersInternal() {
        this.mViewFlinger.stop();
        this.mLayout.stopSmoothScroller();
    }

    private void pullGlows(int overscrollX, int overscrollY) {
        if (overscrollX < 0) {
            ensureLeftGlow();
            this.mLeftGlow.onPull((-overscrollX) / getWidth());
        } else if (overscrollX > 0) {
            ensureRightGlow();
            this.mRightGlow.onPull(overscrollX / getWidth());
        }
        if (overscrollY < 0) {
            ensureTopGlow();
            this.mTopGlow.onPull((-overscrollY) / getHeight());
        } else if (overscrollY > 0) {
            ensureBottomGlow();
            this.mBottomGlow.onPull(overscrollY / getHeight());
        }
        if (overscrollX != 0 || overscrollY != 0) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    private void releaseGlows() {
        boolean needsInvalidate = this.mLeftGlow != null ? this.mLeftGlow.onRelease() : false;
        if (this.mTopGlow != null) {
            needsInvalidate |= this.mTopGlow.onRelease();
        }
        if (this.mRightGlow != null) {
            needsInvalidate |= this.mRightGlow.onRelease();
        }
        if (this.mBottomGlow != null) {
            needsInvalidate |= this.mBottomGlow.onRelease();
        }
        if (needsInvalidate) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void considerReleasingGlowsOnScroll(int dx, int dy) {
        boolean needsInvalidate = false;
        if (this.mLeftGlow != null && !this.mLeftGlow.isFinished() && dx > 0) {
            needsInvalidate = this.mLeftGlow.onRelease();
        }
        if (this.mRightGlow != null && !this.mRightGlow.isFinished() && dx < 0) {
            needsInvalidate |= this.mRightGlow.onRelease();
        }
        if (this.mTopGlow != null && !this.mTopGlow.isFinished() && dy > 0) {
            needsInvalidate |= this.mTopGlow.onRelease();
        }
        if (this.mBottomGlow != null && !this.mBottomGlow.isFinished() && dy < 0) {
            needsInvalidate |= this.mBottomGlow.onRelease();
        }
        if (needsInvalidate) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    void absorbGlows(int velocityX, int velocityY) {
        if (velocityX < 0) {
            ensureLeftGlow();
            this.mLeftGlow.onAbsorb(-velocityX);
        } else if (velocityX > 0) {
            ensureRightGlow();
            this.mRightGlow.onAbsorb(velocityX);
        }
        if (velocityY < 0) {
            ensureTopGlow();
            this.mTopGlow.onAbsorb(-velocityY);
        } else if (velocityY > 0) {
            ensureBottomGlow();
            this.mBottomGlow.onAbsorb(velocityY);
        }
        if (velocityX != 0 || velocityY != 0) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    void ensureLeftGlow() {
        if (this.mLeftGlow == null) {
            this.mLeftGlow = new EdgeEffectCompat(getContext());
            if (this.mClipToPadding) {
                this.mLeftGlow.setSize((getMeasuredHeight() - getPaddingTop()) - getPaddingBottom(), (getMeasuredWidth() - getPaddingLeft()) - getPaddingRight());
            } else {
                this.mLeftGlow.setSize(getMeasuredHeight(), getMeasuredWidth());
            }
        }
    }

    void ensureRightGlow() {
        if (this.mRightGlow == null) {
            this.mRightGlow = new EdgeEffectCompat(getContext());
            if (this.mClipToPadding) {
                this.mRightGlow.setSize((getMeasuredHeight() - getPaddingTop()) - getPaddingBottom(), (getMeasuredWidth() - getPaddingLeft()) - getPaddingRight());
            } else {
                this.mRightGlow.setSize(getMeasuredHeight(), getMeasuredWidth());
            }
        }
    }

    void ensureTopGlow() {
        if (this.mTopGlow == null) {
            this.mTopGlow = new EdgeEffectCompat(getContext());
            if (this.mClipToPadding) {
                this.mTopGlow.setSize((getMeasuredWidth() - getPaddingLeft()) - getPaddingRight(), (getMeasuredHeight() - getPaddingTop()) - getPaddingBottom());
            } else {
                this.mTopGlow.setSize(getMeasuredWidth(), getMeasuredHeight());
            }
        }
    }

    void ensureBottomGlow() {
        if (this.mBottomGlow == null) {
            this.mBottomGlow = new EdgeEffectCompat(getContext());
            if (this.mClipToPadding) {
                this.mBottomGlow.setSize((getMeasuredWidth() - getPaddingLeft()) - getPaddingRight(), (getMeasuredHeight() - getPaddingTop()) - getPaddingBottom());
            } else {
                this.mBottomGlow.setSize(getMeasuredWidth(), getMeasuredHeight());
            }
        }
    }

    void invalidateGlows() {
        this.mBottomGlow = null;
        this.mTopGlow = null;
        this.mRightGlow = null;
        this.mLeftGlow = null;
    }

    @Override // android.view.ViewGroup, android.view.ViewParent
    public View focusSearch(View focused, int direction) {
        View result = this.mLayout.onInterceptFocusSearch(focused, direction);
        if (result != null) {
            return result;
        }
        FocusFinder ff = FocusFinder.getInstance();
        View result2 = ff.findNextFocus(this, focused, direction);
        if (result2 == null && this.mAdapter != null) {
            eatRequestLayout();
            result2 = this.mLayout.onFocusSearchFailed(focused, direction, this.mRecycler, this.mState);
            resumeRequestLayout(false);
        }
        return result2 != null ? result2 : super.focusSearch(focused, direction);
    }

    @Override // android.view.ViewGroup, android.view.ViewParent
    public void requestChildFocus(View child, View focused) {
        if (!this.mLayout.onRequestChildFocus(this, this.mState, child, focused) && focused != null) {
            this.mTempRect.set(0, 0, focused.getWidth(), focused.getHeight());
            offsetDescendantRectToMyCoords(focused, this.mTempRect);
            offsetRectIntoDescendantCoords(child, this.mTempRect);
            requestChildRectangleOnScreen(child, this.mTempRect, this.mFirstLayoutComplete ? false : true);
        }
        super.requestChildFocus(child, focused);
    }

    @Override // android.view.ViewGroup, android.view.ViewParent
    public boolean requestChildRectangleOnScreen(View child, Rect rect, boolean immediate) {
        return this.mLayout.requestChildRectangleOnScreen(this, child, rect, immediate);
    }

    @Override // android.view.ViewGroup, android.view.View
    public void addFocusables(ArrayList<View> views, int direction, int focusableMode) {
        if (!this.mLayout.onAddFocusables(this, views, direction, focusableMode)) {
            super.addFocusables(views, direction, focusableMode);
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.mIsAttached = true;
        this.mFirstLayoutComplete = false;
        if (this.mLayout != null) {
            this.mLayout.onAttachedToWindow(this);
        }
        this.mPostedAnimatorRunner = false;
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (this.mItemAnimator != null) {
            this.mItemAnimator.endAnimations();
        }
        this.mFirstLayoutComplete = false;
        stopScroll();
        this.mIsAttached = false;
        if (this.mLayout != null) {
            this.mLayout.onDetachedFromWindow(this, this.mRecycler);
        }
        removeCallbacks(this.mItemAnimatorRunner);
    }

    void assertInLayoutOrScroll(String message) {
        if (!this.mRunningLayoutOrScroll) {
            if (message == null) {
                throw new IllegalStateException("Cannot call this method unless RecyclerView is computing a layout or scrolling");
            }
            throw new IllegalStateException(message);
        }
    }

    void assertNotInLayoutOrScroll(String message) {
        if (this.mRunningLayoutOrScroll) {
            if (message == null) {
                throw new IllegalStateException("Cannot call this method while RecyclerView is computing a layout or scrolling");
            }
            throw new IllegalStateException(message);
        }
    }

    public void addOnItemTouchListener(OnItemTouchListener listener) {
        this.mOnItemTouchListeners.add(listener);
    }

    public void removeOnItemTouchListener(OnItemTouchListener listener) {
        this.mOnItemTouchListeners.remove(listener);
        if (this.mActiveOnItemTouchListener == listener) {
            this.mActiveOnItemTouchListener = null;
        }
    }

    private boolean dispatchOnItemTouchIntercept(MotionEvent e) {
        int action = e.getAction();
        if (action == 3 || action == 0) {
            this.mActiveOnItemTouchListener = null;
        }
        int listenerCount = this.mOnItemTouchListeners.size();
        for (int i = 0; i < listenerCount; i++) {
            OnItemTouchListener listener = this.mOnItemTouchListeners.get(i);
            if (listener.onInterceptTouchEvent(this, e) && action != 3) {
                this.mActiveOnItemTouchListener = listener;
                return true;
            }
        }
        return false;
    }

    private boolean dispatchOnItemTouch(MotionEvent e) {
        int action = e.getAction();
        if (this.mActiveOnItemTouchListener != null) {
            if (action == 0) {
                this.mActiveOnItemTouchListener = null;
            } else {
                this.mActiveOnItemTouchListener.onTouchEvent(this, e);
                if (action == 3 || action == 1) {
                    this.mActiveOnItemTouchListener = null;
                    return true;
                }
                return true;
            }
        }
        if (action != 0) {
            int listenerCount = this.mOnItemTouchListeners.size();
            for (int i = 0; i < listenerCount; i++) {
                OnItemTouchListener listener = this.mOnItemTouchListeners.get(i);
                if (listener.onInterceptTouchEvent(this, e)) {
                    this.mActiveOnItemTouchListener = listener;
                    return true;
                }
            }
        }
        return false;
    }

    @Override // android.view.ViewGroup
    public boolean onInterceptTouchEvent(MotionEvent e) {
        if (dispatchOnItemTouchIntercept(e)) {
            cancelTouch();
            return true;
        }
        boolean canScrollHorizontally = this.mLayout.canScrollHorizontally();
        boolean canScrollVertically = this.mLayout.canScrollVertically();
        if (this.mVelocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
        }
        this.mVelocityTracker.addMovement(e);
        int action = MotionEventCompat.getActionMasked(e);
        int actionIndex = MotionEventCompat.getActionIndex(e);
        switch (action) {
            case 0:
                this.mScrollPointerId = MotionEventCompat.getPointerId(e, 0);
                int x = (int) (e.getX() + 0.5f);
                this.mLastTouchX = x;
                this.mInitialTouchX = x;
                int y = (int) (e.getY() + 0.5f);
                this.mLastTouchY = y;
                this.mInitialTouchY = y;
                if (this.mScrollState == 2) {
                    getParent().requestDisallowInterceptTouchEvent(true);
                    setScrollState(1);
                    break;
                }
                break;
            case 1:
                this.mVelocityTracker.clear();
                break;
            case 2:
                int index = MotionEventCompat.findPointerIndex(e, this.mScrollPointerId);
                if (index < 0) {
                    Log.e(TAG, "Error processing scroll; pointer index for id " + this.mScrollPointerId + " not found. Did any MotionEvents get skipped?");
                    return false;
                }
                int x2 = (int) (MotionEventCompat.getX(e, index) + 0.5f);
                int y2 = (int) (MotionEventCompat.getY(e, index) + 0.5f);
                if (this.mScrollState != 1) {
                    int dx = x2 - this.mInitialTouchX;
                    int dy = y2 - this.mInitialTouchY;
                    boolean startScroll = false;
                    if (canScrollHorizontally && Math.abs(dx) > this.mTouchSlop) {
                        this.mLastTouchX = ((dx < 0 ? -1 : 1) * this.mTouchSlop) + this.mInitialTouchX;
                        startScroll = true;
                    }
                    if (canScrollVertically && Math.abs(dy) > this.mTouchSlop) {
                        this.mLastTouchY = ((dy < 0 ? -1 : 1) * this.mTouchSlop) + this.mInitialTouchY;
                        startScroll = true;
                    }
                    if (startScroll) {
                        getParent().requestDisallowInterceptTouchEvent(true);
                        setScrollState(1);
                        break;
                    }
                }
                break;
            case 3:
                cancelTouch();
                break;
            case 5:
                this.mScrollPointerId = MotionEventCompat.getPointerId(e, actionIndex);
                int x3 = (int) (MotionEventCompat.getX(e, actionIndex) + 0.5f);
                this.mLastTouchX = x3;
                this.mInitialTouchX = x3;
                int y3 = (int) (MotionEventCompat.getY(e, actionIndex) + 0.5f);
                this.mLastTouchY = y3;
                this.mInitialTouchY = y3;
                break;
            case 6:
                onPointerUp(e);
                break;
        }
        return this.mScrollState == 1;
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent e) {
        if (dispatchOnItemTouch(e)) {
            cancelTouch();
            return true;
        }
        boolean canScrollHorizontally = this.mLayout.canScrollHorizontally();
        boolean canScrollVertically = this.mLayout.canScrollVertically();
        if (this.mVelocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
        }
        this.mVelocityTracker.addMovement(e);
        int action = MotionEventCompat.getActionMasked(e);
        int actionIndex = MotionEventCompat.getActionIndex(e);
        switch (action) {
            case 0:
                this.mScrollPointerId = MotionEventCompat.getPointerId(e, 0);
                int x = (int) (e.getX() + 0.5f);
                this.mLastTouchX = x;
                this.mInitialTouchX = x;
                int y = (int) (e.getY() + 0.5f);
                this.mLastTouchY = y;
                this.mInitialTouchY = y;
                break;
            case 1:
                this.mVelocityTracker.computeCurrentVelocity(1000, this.mMaxFlingVelocity);
                float xvel = canScrollHorizontally ? -VelocityTrackerCompat.getXVelocity(this.mVelocityTracker, this.mScrollPointerId) : 0.0f;
                float yvel = canScrollVertically ? -VelocityTrackerCompat.getYVelocity(this.mVelocityTracker, this.mScrollPointerId) : 0.0f;
                if ((xvel == 0.0f && yvel == 0.0f) || !fling((int) xvel, (int) yvel)) {
                    setScrollState(0);
                }
                this.mVelocityTracker.clear();
                releaseGlows();
                break;
            case 2:
                int index = MotionEventCompat.findPointerIndex(e, this.mScrollPointerId);
                if (index < 0) {
                    Log.e(TAG, "Error processing scroll; pointer index for id " + this.mScrollPointerId + " not found. Did any MotionEvents get skipped?");
                    return false;
                }
                int x2 = (int) (MotionEventCompat.getX(e, index) + 0.5f);
                int y2 = (int) (MotionEventCompat.getY(e, index) + 0.5f);
                if (this.mScrollState != 1) {
                    int dx = x2 - this.mInitialTouchX;
                    int dy = y2 - this.mInitialTouchY;
                    boolean startScroll = false;
                    if (canScrollHorizontally && Math.abs(dx) > this.mTouchSlop) {
                        this.mLastTouchX = ((dx < 0 ? -1 : 1) * this.mTouchSlop) + this.mInitialTouchX;
                        startScroll = true;
                    }
                    if (canScrollVertically && Math.abs(dy) > this.mTouchSlop) {
                        this.mLastTouchY = ((dy < 0 ? -1 : 1) * this.mTouchSlop) + this.mInitialTouchY;
                        startScroll = true;
                    }
                    if (startScroll) {
                        getParent().requestDisallowInterceptTouchEvent(true);
                        setScrollState(1);
                    }
                }
                if (this.mScrollState == 1) {
                    scrollByInternal(canScrollHorizontally ? -(x2 - this.mLastTouchX) : 0, canScrollVertically ? -(y2 - this.mLastTouchY) : 0);
                }
                this.mLastTouchX = x2;
                this.mLastTouchY = y2;
                break;
                break;
            case 3:
                cancelTouch();
                break;
            case 5:
                this.mScrollPointerId = MotionEventCompat.getPointerId(e, actionIndex);
                int x3 = (int) (MotionEventCompat.getX(e, actionIndex) + 0.5f);
                this.mLastTouchX = x3;
                this.mInitialTouchX = x3;
                int y3 = (int) (MotionEventCompat.getY(e, actionIndex) + 0.5f);
                this.mLastTouchY = y3;
                this.mInitialTouchY = y3;
                break;
            case 6:
                onPointerUp(e);
                break;
        }
        return true;
    }

    private void cancelTouch() {
        if (this.mVelocityTracker != null) {
            this.mVelocityTracker.clear();
        }
        releaseGlows();
        setScrollState(0);
    }

    private void onPointerUp(MotionEvent e) {
        int actionIndex = MotionEventCompat.getActionIndex(e);
        if (MotionEventCompat.getPointerId(e, actionIndex) == this.mScrollPointerId) {
            int newIndex = actionIndex == 0 ? 1 : 0;
            this.mScrollPointerId = MotionEventCompat.getPointerId(e, newIndex);
            int x = (int) (MotionEventCompat.getX(e, newIndex) + 0.5f);
            this.mLastTouchX = x;
            this.mInitialTouchX = x;
            int y = (int) (MotionEventCompat.getY(e, newIndex) + 0.5f);
            this.mLastTouchY = y;
            this.mInitialTouchY = y;
        }
    }

    @Override // android.view.View
    protected void onMeasure(int widthSpec, int heightSpec) {
        if (this.mAdapterUpdateDuringMeasure) {
            eatRequestLayout();
            processAdapterUpdatesAndSetAnimationFlags();
            if (!this.mState.mRunPredictiveAnimations) {
                this.mAdapterHelper.consumeUpdatesInOnePass();
                this.mState.mInPreLayout = false;
            } else {
                this.mState.mInPreLayout = true;
            }
            this.mAdapterUpdateDuringMeasure = false;
            resumeRequestLayout(false);
        }
        if (this.mAdapter != null) {
            this.mState.mItemCount = this.mAdapter.getItemCount();
        } else {
            this.mState.mItemCount = 0;
        }
        this.mLayout.onMeasure(this.mRecycler, this.mState, widthSpec, heightSpec);
        this.mState.mInPreLayout = false;
    }

    @Override // android.view.View
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (w != oldw || h != oldh) {
            invalidateGlows();
        }
    }

    public void setItemAnimator(ItemAnimator animator) {
        if (this.mItemAnimator != null) {
            this.mItemAnimator.endAnimations();
            this.mItemAnimator.setListener(null);
        }
        this.mItemAnimator = animator;
        if (this.mItemAnimator != null) {
            this.mItemAnimator.setListener(this.mItemAnimatorListener);
        }
    }

    public ItemAnimator getItemAnimator() {
        return this.mItemAnimator;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean supportsChangeAnimations() {
        return this.mItemAnimator != null && this.mItemAnimator.getSupportsChangeAnimations();
    }

    private void postAnimationRunner() {
        if (!this.mPostedAnimatorRunner && this.mIsAttached) {
            ViewCompat.postOnAnimation(this, this.mItemAnimatorRunner);
            this.mPostedAnimatorRunner = true;
        }
    }

    private boolean predictiveItemAnimationsEnabled() {
        return this.mItemAnimator != null && this.mLayout.supportsPredictiveItemAnimations();
    }

    private void processAdapterUpdatesAndSetAnimationFlags() {
        boolean z = true;
        if (this.mDataSetHasChangedAfterLayout) {
            this.mAdapterHelper.reset();
            markKnownViewsInvalid();
            this.mLayout.onItemsChanged(this);
        }
        if (this.mItemAnimator != null && this.mLayout.supportsPredictiveItemAnimations()) {
            this.mAdapterHelper.preProcess();
        } else {
            this.mAdapterHelper.consumeUpdatesInOnePass();
        }
        boolean animationTypeSupported = (this.mItemsAddedOrRemoved && !this.mItemsChanged) || this.mItemsAddedOrRemoved || (this.mItemsChanged && supportsChangeAnimations());
        this.mState.mRunSimpleAnimations = this.mFirstLayoutComplete && this.mItemAnimator != null && (this.mDataSetHasChangedAfterLayout || animationTypeSupported || this.mLayout.mRequestedSimpleAnimations) && (!this.mDataSetHasChangedAfterLayout || this.mAdapter.hasStableIds());
        State state = this.mState;
        if (!this.mState.mRunSimpleAnimations || !animationTypeSupported || this.mDataSetHasChangedAfterLayout || !predictiveItemAnimationsEnabled()) {
            z = false;
        }
        state.mRunPredictiveAnimations = z;
    }

    void dispatchLayout() {
        if (this.mAdapter == null) {
            Log.e(TAG, "No adapter attached; skipping layout");
            return;
        }
        this.mDisappearingViewsInLayoutPass.clear();
        eatRequestLayout();
        this.mRunningLayoutOrScroll = true;
        processAdapterUpdatesAndSetAnimationFlags();
        this.mState.mOldChangedHolders = (this.mState.mRunSimpleAnimations && this.mItemsChanged && supportsChangeAnimations()) ? new ArrayMap<>() : null;
        this.mItemsChanged = false;
        this.mItemsAddedOrRemoved = false;
        ArrayMap<View, Rect> appearingViewInitialBounds = null;
        this.mState.mInPreLayout = this.mState.mRunPredictiveAnimations;
        this.mState.mItemCount = this.mAdapter.getItemCount();
        if (this.mState.mRunSimpleAnimations) {
            this.mState.mPreLayoutHolderMap.clear();
            this.mState.mPostLayoutHolderMap.clear();
            int count = this.mChildHelper.getChildCount();
            for (int i = 0; i < count; i++) {
                ViewHolder holder = getChildViewHolderInt(this.mChildHelper.getChildAt(i));
                if (!holder.shouldIgnore() && (!holder.isInvalid() || this.mAdapter.hasStableIds())) {
                    View view = holder.itemView;
                    this.mState.mPreLayoutHolderMap.put(holder, new ItemHolderInfo(holder, view.getLeft(), view.getTop(), view.getRight(), view.getBottom()));
                }
            }
        }
        if (this.mState.mRunPredictiveAnimations) {
            saveOldPositions();
            if (this.mState.mOldChangedHolders != null) {
                int count2 = this.mChildHelper.getChildCount();
                for (int i2 = 0; i2 < count2; i2++) {
                    ViewHolder holder2 = getChildViewHolderInt(this.mChildHelper.getChildAt(i2));
                    if (holder2.isChanged() && !holder2.isRemoved() && !holder2.shouldIgnore()) {
                        this.mState.mOldChangedHolders.put(Long.valueOf(getChangedHolderKey(holder2)), holder2);
                        this.mState.mPreLayoutHolderMap.remove(holder2);
                    }
                }
            }
            boolean didStructureChange = this.mState.mStructureChanged;
            this.mState.mStructureChanged = false;
            this.mLayout.onLayoutChildren(this.mRecycler, this.mState);
            this.mState.mStructureChanged = didStructureChange;
            appearingViewInitialBounds = new ArrayMap<>();
            for (int i3 = 0; i3 < this.mChildHelper.getChildCount(); i3++) {
                boolean found = false;
                View child = this.mChildHelper.getChildAt(i3);
                if (!getChildViewHolderInt(child).shouldIgnore()) {
                    int j = 0;
                    while (true) {
                        if (j < this.mState.mPreLayoutHolderMap.size()) {
                            if (this.mState.mPreLayoutHolderMap.keyAt(j).itemView != child) {
                                j++;
                            } else {
                                found = true;
                                break;
                            }
                        } else {
                            break;
                        }
                    }
                    if (!found) {
                        appearingViewInitialBounds.put(child, new Rect(child.getLeft(), child.getTop(), child.getRight(), child.getBottom()));
                    }
                }
            }
            clearOldPositions();
            this.mAdapterHelper.consumePostponedUpdates();
        } else {
            clearOldPositions();
            this.mAdapterHelper.consumeUpdatesInOnePass();
            if (this.mState.mOldChangedHolders != null) {
                int count3 = this.mChildHelper.getChildCount();
                for (int i4 = 0; i4 < count3; i4++) {
                    ViewHolder holder3 = getChildViewHolderInt(this.mChildHelper.getChildAt(i4));
                    if (holder3.isChanged() && !holder3.isRemoved() && !holder3.shouldIgnore()) {
                        this.mState.mOldChangedHolders.put(Long.valueOf(getChangedHolderKey(holder3)), holder3);
                        this.mState.mPreLayoutHolderMap.remove(holder3);
                    }
                }
            }
        }
        this.mState.mItemCount = this.mAdapter.getItemCount();
        this.mState.mDeletedInvisibleItemCountSincePreviousLayout = 0;
        this.mState.mInPreLayout = false;
        this.mLayout.onLayoutChildren(this.mRecycler, this.mState);
        this.mState.mStructureChanged = false;
        this.mPendingSavedState = null;
        this.mState.mRunSimpleAnimations = this.mState.mRunSimpleAnimations && this.mItemAnimator != null;
        if (this.mState.mRunSimpleAnimations) {
            ArrayMap<Long, ViewHolder> newChangedHolders = this.mState.mOldChangedHolders != null ? new ArrayMap<>() : null;
            int count4 = this.mChildHelper.getChildCount();
            for (int i5 = 0; i5 < count4; i5++) {
                ViewHolder holder4 = getChildViewHolderInt(this.mChildHelper.getChildAt(i5));
                if (!holder4.shouldIgnore()) {
                    View view2 = holder4.itemView;
                    long key = getChangedHolderKey(holder4);
                    if (newChangedHolders != null && this.mState.mOldChangedHolders.get(Long.valueOf(key)) != null) {
                        newChangedHolders.put(Long.valueOf(key), holder4);
                    } else {
                        this.mState.mPostLayoutHolderMap.put(holder4, new ItemHolderInfo(holder4, view2.getLeft(), view2.getTop(), view2.getRight(), view2.getBottom()));
                    }
                }
            }
            processDisappearingList(appearingViewInitialBounds);
            int preLayoutCount = this.mState.mPreLayoutHolderMap.size();
            for (int i6 = preLayoutCount - 1; i6 >= 0; i6--) {
                if (!this.mState.mPostLayoutHolderMap.containsKey(this.mState.mPreLayoutHolderMap.keyAt(i6))) {
                    ItemHolderInfo disappearingItem = this.mState.mPreLayoutHolderMap.valueAt(i6);
                    this.mState.mPreLayoutHolderMap.removeAt(i6);
                    View view3 = disappearingItem.holder.itemView;
                    this.mRecycler.unscrapView(disappearingItem.holder);
                    animateDisappearance(disappearingItem);
                }
            }
            int postLayoutCount = this.mState.mPostLayoutHolderMap.size();
            if (postLayoutCount > 0) {
                for (int i7 = postLayoutCount - 1; i7 >= 0; i7--) {
                    ViewHolder itemHolder = this.mState.mPostLayoutHolderMap.keyAt(i7);
                    ItemHolderInfo info = this.mState.mPostLayoutHolderMap.valueAt(i7);
                    if (this.mState.mPreLayoutHolderMap.isEmpty() || !this.mState.mPreLayoutHolderMap.containsKey(itemHolder)) {
                        this.mState.mPostLayoutHolderMap.removeAt(i7);
                        Rect initialBounds = appearingViewInitialBounds != null ? appearingViewInitialBounds.get(itemHolder.itemView) : null;
                        animateAppearance(itemHolder, initialBounds, info.left, info.top);
                    }
                }
            }
            int count5 = this.mState.mPostLayoutHolderMap.size();
            for (int i8 = 0; i8 < count5; i8++) {
                ViewHolder postHolder = this.mState.mPostLayoutHolderMap.keyAt(i8);
                ItemHolderInfo postInfo = this.mState.mPostLayoutHolderMap.valueAt(i8);
                ItemHolderInfo preInfo = this.mState.mPreLayoutHolderMap.get(postHolder);
                if (preInfo != null && postInfo != null && (preInfo.left != postInfo.left || preInfo.top != postInfo.top)) {
                    postHolder.setIsRecyclable(false);
                    if (this.mItemAnimator.animateMove(postHolder, preInfo.left, preInfo.top, postInfo.left, postInfo.top)) {
                        postAnimationRunner();
                    }
                }
            }
            int count6 = this.mState.mOldChangedHolders != null ? this.mState.mOldChangedHolders.size() : 0;
            for (int i9 = count6 - 1; i9 >= 0; i9--) {
                long key2 = this.mState.mOldChangedHolders.keyAt(i9).longValue();
                ViewHolder oldHolder = this.mState.mOldChangedHolders.get(Long.valueOf(key2));
                View view4 = oldHolder.itemView;
                if (!oldHolder.shouldIgnore() && this.mRecycler.mChangedScrap != null && this.mRecycler.mChangedScrap.contains(oldHolder)) {
                    animateChange(oldHolder, newChangedHolders.get(Long.valueOf(key2)));
                }
            }
        }
        resumeRequestLayout(false);
        this.mLayout.removeAndRecycleScrapInt(this.mRecycler);
        this.mState.mPreviousLayoutItemCount = this.mState.mItemCount;
        this.mDataSetHasChangedAfterLayout = false;
        this.mState.mRunSimpleAnimations = false;
        this.mState.mRunPredictiveAnimations = false;
        this.mRunningLayoutOrScroll = false;
        this.mLayout.mRequestedSimpleAnimations = false;
        if (this.mRecycler.mChangedScrap != null) {
            this.mRecycler.mChangedScrap.clear();
        }
        this.mState.mOldChangedHolders = null;
    }

    @Override // android.view.ViewGroup
    protected void removeDetachedView(View child, boolean animate) {
        ViewHolder vh = getChildViewHolderInt(child);
        if (vh != null) {
            if (vh.isTmpDetached()) {
                vh.clearTmpDetachFlag();
            } else if (!vh.shouldIgnore()) {
                throw new IllegalArgumentException("Called removeDetachedView with a view which is not flagged as tmp detached." + vh);
            }
        }
        dispatchChildDetached(child);
        super.removeDetachedView(child, animate);
    }

    long getChangedHolderKey(ViewHolder holder) {
        return this.mAdapter.hasStableIds() ? holder.getItemId() : holder.mPosition;
    }

    private void processDisappearingList(ArrayMap<View, Rect> appearingViews) {
        int count = this.mDisappearingViewsInLayoutPass.size();
        for (int i = 0; i < count; i++) {
            View view = this.mDisappearingViewsInLayoutPass.get(i);
            ViewHolder vh = getChildViewHolderInt(view);
            ItemHolderInfo info = this.mState.mPreLayoutHolderMap.remove(vh);
            if (!this.mState.isPreLayout()) {
                this.mState.mPostLayoutHolderMap.remove(vh);
            }
            if (appearingViews.remove(view) != null) {
                this.mLayout.removeAndRecycleView(view, this.mRecycler);
            } else if (info != null) {
                animateDisappearance(info);
            } else {
                animateDisappearance(new ItemHolderInfo(vh, view.getLeft(), view.getTop(), view.getRight(), view.getBottom()));
            }
        }
        this.mDisappearingViewsInLayoutPass.clear();
    }

    private void animateAppearance(ViewHolder itemHolder, Rect beforeBounds, int afterLeft, int afterTop) {
        View view = itemHolder.itemView;
        if (beforeBounds != null && (beforeBounds.left != afterLeft || beforeBounds.top != afterTop)) {
            itemHolder.setIsRecyclable(false);
            if (this.mItemAnimator.animateMove(itemHolder, beforeBounds.left, beforeBounds.top, afterLeft, afterTop)) {
                postAnimationRunner();
                return;
            }
            return;
        }
        itemHolder.setIsRecyclable(false);
        if (this.mItemAnimator.animateAdd(itemHolder)) {
            postAnimationRunner();
        }
    }

    private void animateDisappearance(ItemHolderInfo disappearingItem) {
        View disappearingItemView = disappearingItem.holder.itemView;
        addAnimatingView(disappearingItem.holder);
        int oldLeft = disappearingItem.left;
        int oldTop = disappearingItem.top;
        int newLeft = disappearingItemView.getLeft();
        int newTop = disappearingItemView.getTop();
        if (oldLeft != newLeft || oldTop != newTop) {
            disappearingItem.holder.setIsRecyclable(false);
            disappearingItemView.layout(newLeft, newTop, disappearingItemView.getWidth() + newLeft, disappearingItemView.getHeight() + newTop);
            if (this.mItemAnimator.animateMove(disappearingItem.holder, oldLeft, oldTop, newLeft, newTop)) {
                postAnimationRunner();
                return;
            }
            return;
        }
        disappearingItem.holder.setIsRecyclable(false);
        if (this.mItemAnimator.animateRemove(disappearingItem.holder)) {
            postAnimationRunner();
        }
    }

    private void animateChange(ViewHolder oldHolder, ViewHolder newHolder) {
        int toLeft;
        int toTop;
        oldHolder.setIsRecyclable(false);
        addAnimatingView(oldHolder);
        oldHolder.mShadowedHolder = newHolder;
        this.mRecycler.unscrapView(oldHolder);
        int fromLeft = oldHolder.itemView.getLeft();
        int fromTop = oldHolder.itemView.getTop();
        if (newHolder == null || newHolder.shouldIgnore()) {
            toLeft = fromLeft;
            toTop = fromTop;
        } else {
            toLeft = newHolder.itemView.getLeft();
            toTop = newHolder.itemView.getTop();
            newHolder.setIsRecyclable(false);
            newHolder.mShadowingHolder = oldHolder;
        }
        if (this.mItemAnimator.animateChange(oldHolder, newHolder, fromLeft, fromTop, toLeft, toTop)) {
            postAnimationRunner();
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        eatRequestLayout();
        dispatchLayout();
        resumeRequestLayout(false);
        this.mFirstLayoutComplete = true;
    }

    @Override // android.view.View, android.view.ViewParent
    public void requestLayout() {
        if (!this.mEatRequestLayout) {
            super.requestLayout();
        } else {
            this.mLayoutRequestEaten = true;
        }
    }

    void markItemDecorInsetsDirty() {
        int childCount = this.mChildHelper.getUnfilteredChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = this.mChildHelper.getUnfilteredChildAt(i);
            ((LayoutParams) child.getLayoutParams()).mInsetsDirty = true;
        }
        this.mRecycler.markItemDecorInsetsDirty();
    }

    @Override // android.view.View
    public void draw(Canvas c) {
        boolean z = true;
        super.draw(c);
        int count = this.mItemDecorations.size();
        for (int i = 0; i < count; i++) {
            this.mItemDecorations.get(i).onDrawOver(c, this, this.mState);
        }
        boolean needsInvalidate = false;
        if (this.mLeftGlow != null && !this.mLeftGlow.isFinished()) {
            int restore = c.save();
            int padding = this.mClipToPadding ? getPaddingBottom() : 0;
            c.rotate(270.0f);
            c.translate((-getHeight()) + padding, 0.0f);
            needsInvalidate = this.mLeftGlow != null && this.mLeftGlow.draw(c);
            c.restoreToCount(restore);
        }
        if (this.mTopGlow != null && !this.mTopGlow.isFinished()) {
            int restore2 = c.save();
            if (this.mClipToPadding) {
                c.translate(getPaddingLeft(), getPaddingTop());
            }
            needsInvalidate |= this.mTopGlow != null && this.mTopGlow.draw(c);
            c.restoreToCount(restore2);
        }
        if (this.mRightGlow != null && !this.mRightGlow.isFinished()) {
            int restore3 = c.save();
            int width = getWidth();
            int padding2 = this.mClipToPadding ? getPaddingTop() : 0;
            c.rotate(90.0f);
            c.translate(-padding2, -width);
            needsInvalidate |= this.mRightGlow != null && this.mRightGlow.draw(c);
            c.restoreToCount(restore3);
        }
        if (this.mBottomGlow != null && !this.mBottomGlow.isFinished()) {
            int restore4 = c.save();
            c.rotate(180.0f);
            if (this.mClipToPadding) {
                c.translate((-getWidth()) + getPaddingRight(), (-getHeight()) + getPaddingBottom());
            } else {
                c.translate(-getWidth(), -getHeight());
            }
            needsInvalidate |= (this.mBottomGlow == null || !this.mBottomGlow.draw(c)) ? false : false;
            c.restoreToCount(restore4);
        }
        if (!needsInvalidate && this.mItemAnimator != null && this.mItemDecorations.size() > 0 && this.mItemAnimator.isRunning()) {
            needsInvalidate = true;
        }
        if (needsInvalidate) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    @Override // android.view.View
    public void onDraw(Canvas c) {
        super.onDraw(c);
        int count = this.mItemDecorations.size();
        for (int i = 0; i < count; i++) {
            this.mItemDecorations.get(i).onDraw(c, this, this.mState);
        }
    }

    @Override // android.view.ViewGroup
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return (p instanceof LayoutParams) && this.mLayout.checkLayoutParams((LayoutParams) p);
    }

    @Override // android.view.ViewGroup
    protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
        if (this.mLayout == null) {
            throw new IllegalStateException("RecyclerView has no LayoutManager");
        }
        return this.mLayout.generateDefaultLayoutParams();
    }

    @Override // android.view.ViewGroup
    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
        if (this.mLayout == null) {
            throw new IllegalStateException("RecyclerView has no LayoutManager");
        }
        return this.mLayout.generateLayoutParams(getContext(), attrs);
    }

    @Override // android.view.ViewGroup
    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        if (this.mLayout == null) {
            throw new IllegalStateException("RecyclerView has no LayoutManager");
        }
        return this.mLayout.generateLayoutParams(p);
    }

    void saveOldPositions() {
        int childCount = this.mChildHelper.getUnfilteredChildCount();
        for (int i = 0; i < childCount; i++) {
            ViewHolder holder = getChildViewHolderInt(this.mChildHelper.getUnfilteredChildAt(i));
            if (!holder.shouldIgnore()) {
                holder.saveOldPosition();
            }
        }
    }

    void clearOldPositions() {
        int childCount = this.mChildHelper.getUnfilteredChildCount();
        for (int i = 0; i < childCount; i++) {
            ViewHolder holder = getChildViewHolderInt(this.mChildHelper.getUnfilteredChildAt(i));
            if (!holder.shouldIgnore()) {
                holder.clearOldPosition();
            }
        }
        this.mRecycler.clearOldPositions();
    }

    void offsetPositionRecordsForMove(int from, int to) {
        int start;
        int end;
        int inBetweenOffset;
        int childCount = this.mChildHelper.getUnfilteredChildCount();
        if (from < to) {
            start = from;
            end = to;
            inBetweenOffset = -1;
        } else {
            start = to;
            end = from;
            inBetweenOffset = 1;
        }
        for (int i = 0; i < childCount; i++) {
            ViewHolder holder = getChildViewHolderInt(this.mChildHelper.getUnfilteredChildAt(i));
            if (holder != null && holder.mPosition >= start && holder.mPosition <= end) {
                if (holder.mPosition == from) {
                    holder.offsetPosition(to - from, false);
                } else {
                    holder.offsetPosition(inBetweenOffset, false);
                }
                this.mState.mStructureChanged = true;
            }
        }
        this.mRecycler.offsetPositionRecordsForMove(from, to);
        requestLayout();
    }

    void offsetPositionRecordsForInsert(int positionStart, int itemCount) {
        int childCount = this.mChildHelper.getUnfilteredChildCount();
        for (int i = 0; i < childCount; i++) {
            ViewHolder holder = getChildViewHolderInt(this.mChildHelper.getUnfilteredChildAt(i));
            if (holder != null && !holder.shouldIgnore() && holder.mPosition >= positionStart) {
                holder.offsetPosition(itemCount, false);
                this.mState.mStructureChanged = true;
            }
        }
        this.mRecycler.offsetPositionRecordsForInsert(positionStart, itemCount);
        requestLayout();
    }

    void offsetPositionRecordsForRemove(int positionStart, int itemCount, boolean applyToPreLayout) {
        int positionEnd = positionStart + itemCount;
        int childCount = this.mChildHelper.getUnfilteredChildCount();
        for (int i = 0; i < childCount; i++) {
            ViewHolder holder = getChildViewHolderInt(this.mChildHelper.getUnfilteredChildAt(i));
            if (holder != null && !holder.shouldIgnore()) {
                if (holder.mPosition >= positionEnd) {
                    holder.offsetPosition(-itemCount, applyToPreLayout);
                    this.mState.mStructureChanged = true;
                } else if (holder.mPosition >= positionStart) {
                    holder.flagRemovedAndOffsetPosition(positionStart - 1, -itemCount, applyToPreLayout);
                    this.mState.mStructureChanged = true;
                }
            }
        }
        this.mRecycler.offsetPositionRecordsForRemove(positionStart, itemCount, applyToPreLayout);
        requestLayout();
    }

    void viewRangeUpdate(int positionStart, int itemCount) {
        int childCount = this.mChildHelper.getUnfilteredChildCount();
        int positionEnd = positionStart + itemCount;
        for (int i = 0; i < childCount; i++) {
            View child = this.mChildHelper.getUnfilteredChildAt(i);
            ViewHolder holder = getChildViewHolderInt(child);
            if (holder != null && !holder.shouldIgnore() && holder.mPosition >= positionStart && holder.mPosition < positionEnd) {
                holder.addFlags(2);
                if (supportsChangeAnimations()) {
                    holder.addFlags(64);
                }
                ((LayoutParams) child.getLayoutParams()).mInsetsDirty = true;
            }
        }
        this.mRecycler.viewRangeUpdate(positionStart, itemCount);
    }

    void rebindUpdatedViewHolders() {
        int childCount = this.mChildHelper.getChildCount();
        for (int i = 0; i < childCount; i++) {
            ViewHolder holder = getChildViewHolderInt(this.mChildHelper.getChildAt(i));
            if (holder != null && !holder.shouldIgnore()) {
                if (holder.isRemoved() || holder.isInvalid()) {
                    requestLayout();
                } else if (holder.needsUpdate()) {
                    int type = this.mAdapter.getItemViewType(holder.mPosition);
                    if (holder.getItemViewType() == type) {
                        if (!holder.isChanged() || !supportsChangeAnimations()) {
                            this.mAdapter.bindViewHolder(holder, holder.mPosition);
                        } else {
                            requestLayout();
                        }
                    } else {
                        holder.addFlags(4);
                        requestLayout();
                    }
                }
            }
        }
    }

    void markKnownViewsInvalid() {
        int childCount = this.mChildHelper.getUnfilteredChildCount();
        for (int i = 0; i < childCount; i++) {
            ViewHolder holder = getChildViewHolderInt(this.mChildHelper.getUnfilteredChildAt(i));
            if (holder != null && !holder.shouldIgnore()) {
                holder.addFlags(6);
            }
        }
        markItemDecorInsetsDirty();
        this.mRecycler.markKnownViewsInvalid();
    }

    public void invalidateItemDecorations() {
        if (this.mItemDecorations.size() != 0) {
            if (this.mLayout != null) {
                this.mLayout.assertNotInLayoutOrScroll("Cannot invalidate item decorations during a scroll or layout");
            }
            markItemDecorInsetsDirty();
            requestLayout();
        }
    }

    public ViewHolder getChildViewHolder(View child) {
        ViewParent parent = child.getParent();
        if (parent != null && parent != this) {
            throw new IllegalArgumentException("View " + child + " is not a direct child of " + this);
        }
        return getChildViewHolderInt(child);
    }

    static ViewHolder getChildViewHolderInt(View child) {
        if (child == null) {
            return null;
        }
        return ((LayoutParams) child.getLayoutParams()).mViewHolder;
    }

    public int getChildPosition(View child) {
        ViewHolder holder = getChildViewHolderInt(child);
        if (holder != null) {
            return holder.getPosition();
        }
        return -1;
    }

    public long getChildItemId(View child) {
        ViewHolder holder;
        if (this.mAdapter == null || !this.mAdapter.hasStableIds() || (holder = getChildViewHolderInt(child)) == null) {
            return -1L;
        }
        return holder.getItemId();
    }

    public ViewHolder findViewHolderForPosition(int position) {
        return findViewHolderForPosition(position, false);
    }

    ViewHolder findViewHolderForPosition(int position, boolean checkNewPosition) {
        int childCount = this.mChildHelper.getUnfilteredChildCount();
        for (int i = 0; i < childCount; i++) {
            ViewHolder holder = getChildViewHolderInt(this.mChildHelper.getUnfilteredChildAt(i));
            if (holder != null && !holder.isRemoved()) {
                if (checkNewPosition) {
                    if (holder.mPosition == position) {
                        return holder;
                    }
                } else if (holder.getPosition() == position) {
                    return holder;
                }
            }
        }
        return null;
    }

    public ViewHolder findViewHolderForItemId(long id) {
        int childCount = this.mChildHelper.getUnfilteredChildCount();
        for (int i = 0; i < childCount; i++) {
            ViewHolder holder = getChildViewHolderInt(this.mChildHelper.getUnfilteredChildAt(i));
            if (holder != null && holder.getItemId() == id) {
                return holder;
            }
        }
        return null;
    }

    public View findChildViewUnder(float x, float y) {
        int count = this.mChildHelper.getChildCount();
        for (int i = count - 1; i >= 0; i--) {
            View child = this.mChildHelper.getChildAt(i);
            float translationX = ViewCompat.getTranslationX(child);
            float translationY = ViewCompat.getTranslationY(child);
            if (x >= child.getLeft() + translationX && x <= child.getRight() + translationX && y >= child.getTop() + translationY && y <= child.getBottom() + translationY) {
                return child;
            }
        }
        return null;
    }

    public void offsetChildrenVertical(int dy) {
        int childCount = this.mChildHelper.getChildCount();
        for (int i = 0; i < childCount; i++) {
            this.mChildHelper.getChildAt(i).offsetTopAndBottom(dy);
        }
    }

    public void onChildAttachedToWindow(View child) {
    }

    public void onChildDetachedFromWindow(View child) {
    }

    public void offsetChildrenHorizontal(int dx) {
        int childCount = this.mChildHelper.getChildCount();
        for (int i = 0; i < childCount; i++) {
            this.mChildHelper.getChildAt(i).offsetLeftAndRight(dx);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Rect getItemDecorInsetsForChild(View child) {
        LayoutParams lp = (LayoutParams) child.getLayoutParams();
        if (!lp.mInsetsDirty) {
            return lp.mDecorInsets;
        }
        Rect insets = lp.mDecorInsets;
        insets.set(0, 0, 0, 0);
        int decorCount = this.mItemDecorations.size();
        for (int i = 0; i < decorCount; i++) {
            this.mTempRect.set(0, 0, 0, 0);
            this.mItemDecorations.get(i).getItemOffsets(this.mTempRect, child, this, this.mState);
            insets.left += this.mTempRect.left;
            insets.top += this.mTempRect.top;
            insets.right += this.mTempRect.right;
            insets.bottom += this.mTempRect.bottom;
        }
        lp.mInsetsDirty = false;
        return insets;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class ViewFlinger implements Runnable {
        private int mLastFlingX;
        private int mLastFlingY;
        private ScrollerCompat mScroller;
        private Interpolator mInterpolator = RecyclerView.sQuinticInterpolator;
        private boolean mEatRunOnAnimationRequest = false;
        private boolean mReSchedulePostAnimationCallback = false;

        public ViewFlinger() {
            this.mScroller = ScrollerCompat.create(RecyclerView.this.getContext(), RecyclerView.sQuinticInterpolator);
        }

        @Override // java.lang.Runnable
        public void run() {
            disableRunOnAnimationRequests();
            RecyclerView.this.consumePendingUpdateOperations();
            ScrollerCompat scroller = this.mScroller;
            SmoothScroller smoothScroller = RecyclerView.this.mLayout.mSmoothScroller;
            if (scroller.computeScrollOffset()) {
                int x = scroller.getCurrX();
                int y = scroller.getCurrY();
                int dx = x - this.mLastFlingX;
                int dy = y - this.mLastFlingY;
                int hresult = 0;
                int vresult = 0;
                this.mLastFlingX = x;
                this.mLastFlingY = y;
                int overscrollX = 0;
                int overscrollY = 0;
                if (RecyclerView.this.mAdapter != null) {
                    RecyclerView.this.eatRequestLayout();
                    RecyclerView.this.mRunningLayoutOrScroll = true;
                    if (dx != 0) {
                        hresult = RecyclerView.this.mLayout.scrollHorizontallyBy(dx, RecyclerView.this.mRecycler, RecyclerView.this.mState);
                        overscrollX = dx - hresult;
                    }
                    if (dy != 0) {
                        vresult = RecyclerView.this.mLayout.scrollVerticallyBy(dy, RecyclerView.this.mRecycler, RecyclerView.this.mState);
                        overscrollY = dy - vresult;
                    }
                    if (RecyclerView.this.supportsChangeAnimations()) {
                        int count = RecyclerView.this.mChildHelper.getChildCount();
                        for (int i = 0; i < count; i++) {
                            View view = RecyclerView.this.mChildHelper.getChildAt(i);
                            ViewHolder holder = RecyclerView.this.getChildViewHolder(view);
                            if (holder != null && holder.mShadowingHolder != null) {
                                View shadowingView = holder.mShadowingHolder != null ? holder.mShadowingHolder.itemView : null;
                                if (shadowingView != null) {
                                    int left = view.getLeft();
                                    int top = view.getTop();
                                    if (left != shadowingView.getLeft() || top != shadowingView.getTop()) {
                                        shadowingView.layout(left, top, shadowingView.getWidth() + left, shadowingView.getHeight() + top);
                                    }
                                }
                            }
                        }
                    }
                    if (smoothScroller != null && !smoothScroller.isPendingInitialRun() && smoothScroller.isRunning()) {
                        int adapterSize = RecyclerView.this.mState.getItemCount();
                        if (adapterSize == 0) {
                            smoothScroller.stop();
                        } else if (smoothScroller.getTargetPosition() >= adapterSize) {
                            smoothScroller.setTargetPosition(adapterSize - 1);
                            smoothScroller.onAnimation(dx - overscrollX, dy - overscrollY);
                        } else {
                            smoothScroller.onAnimation(dx - overscrollX, dy - overscrollY);
                        }
                    }
                    RecyclerView.this.mRunningLayoutOrScroll = false;
                    RecyclerView.this.resumeRequestLayout(false);
                }
                boolean fullyConsumedScroll = dx == hresult && dy == vresult;
                if (!RecyclerView.this.mItemDecorations.isEmpty()) {
                    RecyclerView.this.invalidate();
                }
                if (ViewCompat.getOverScrollMode(RecyclerView.this) != 2) {
                    RecyclerView.this.considerReleasingGlowsOnScroll(dx, dy);
                }
                if (overscrollX != 0 || overscrollY != 0) {
                    int vel = (int) scroller.getCurrVelocity();
                    int velX = 0;
                    if (overscrollX != x) {
                        if (overscrollX < 0) {
                            velX = -vel;
                        } else {
                            velX = overscrollX > 0 ? vel : 0;
                        }
                    }
                    int velY = 0;
                    if (overscrollY != y) {
                        if (overscrollY < 0) {
                            velY = -vel;
                        } else {
                            velY = overscrollY > 0 ? vel : 0;
                        }
                    }
                    if (ViewCompat.getOverScrollMode(RecyclerView.this) != 2) {
                        RecyclerView.this.absorbGlows(velX, velY);
                    }
                    if ((velX != 0 || overscrollX == x || scroller.getFinalX() == 0) && (velY != 0 || overscrollY == y || scroller.getFinalY() == 0)) {
                        scroller.abortAnimation();
                    }
                }
                if (hresult != 0 || vresult != 0) {
                    RecyclerView.this.onScrollChanged(0, 0, 0, 0);
                    if (RecyclerView.this.mScrollListener != null) {
                        RecyclerView.this.mScrollListener.onScrolled(RecyclerView.this, hresult, vresult);
                    }
                }
                if (!RecyclerView.this.awakenScrollBars()) {
                    RecyclerView.this.invalidate();
                }
                if (scroller.isFinished() || !fullyConsumedScroll) {
                    RecyclerView.this.setScrollState(0);
                } else {
                    postOnAnimation();
                }
            }
            if (smoothScroller != null && smoothScroller.isPendingInitialRun()) {
                smoothScroller.onAnimation(0, 0);
            }
            enableRunOnAnimationRequests();
        }

        private void disableRunOnAnimationRequests() {
            this.mReSchedulePostAnimationCallback = false;
            this.mEatRunOnAnimationRequest = true;
        }

        private void enableRunOnAnimationRequests() {
            this.mEatRunOnAnimationRequest = false;
            if (this.mReSchedulePostAnimationCallback) {
                postOnAnimation();
            }
        }

        void postOnAnimation() {
            if (this.mEatRunOnAnimationRequest) {
                this.mReSchedulePostAnimationCallback = true;
            } else {
                ViewCompat.postOnAnimation(RecyclerView.this, this);
            }
        }

        public void fling(int velocityX, int velocityY) {
            RecyclerView.this.setScrollState(2);
            this.mLastFlingY = 0;
            this.mLastFlingX = 0;
            this.mScroller.fling(0, 0, velocityX, velocityY, Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE);
            postOnAnimation();
        }

        public void smoothScrollBy(int dx, int dy) {
            smoothScrollBy(dx, dy, 0, 0);
        }

        public void smoothScrollBy(int dx, int dy, int vx, int vy) {
            smoothScrollBy(dx, dy, computeScrollDuration(dx, dy, vx, vy));
        }

        private float distanceInfluenceForSnapDuration(float f) {
            return (float) Math.sin((float) ((f - 0.5f) * 0.4712389167638204d));
        }

        private int computeScrollDuration(int dx, int dy, int vx, int vy) {
            int duration;
            int absDx = Math.abs(dx);
            int absDy = Math.abs(dy);
            boolean horizontal = absDx > absDy;
            int velocity = (int) Math.sqrt((vx * vx) + (vy * vy));
            int delta = (int) Math.sqrt((dx * dx) + (dy * dy));
            int containerSize = horizontal ? RecyclerView.this.getWidth() : RecyclerView.this.getHeight();
            int halfContainerSize = containerSize / 2;
            float distanceRatio = Math.min(1.0f, (1.0f * delta) / containerSize);
            float distance = halfContainerSize + (halfContainerSize * distanceInfluenceForSnapDuration(distanceRatio));
            if (velocity > 0) {
                duration = Math.round(1000.0f * Math.abs(distance / velocity)) * 4;
            } else {
                if (!horizontal) {
                    absDx = absDy;
                }
                float absDelta = absDx;
                duration = (int) (((absDelta / containerSize) + 1.0f) * 300.0f);
            }
            return Math.min(duration, 2000);
        }

        public void smoothScrollBy(int dx, int dy, int duration) {
            smoothScrollBy(dx, dy, duration, RecyclerView.sQuinticInterpolator);
        }

        public void smoothScrollBy(int dx, int dy, int duration, Interpolator interpolator) {
            if (this.mInterpolator != interpolator) {
                this.mInterpolator = interpolator;
                this.mScroller = ScrollerCompat.create(RecyclerView.this.getContext(), interpolator);
            }
            RecyclerView.this.setScrollState(2);
            this.mLastFlingY = 0;
            this.mLastFlingX = 0;
            this.mScroller.startScroll(0, 0, dx, dy, duration);
            postOnAnimation();
        }

        public void stop() {
            RecyclerView.this.removeCallbacks(this);
            this.mScroller.abortAnimation();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class RecyclerViewDataObserver extends AdapterDataObserver {
        private RecyclerViewDataObserver() {
        }

        @Override // android.support.v7.widget.RecyclerView.AdapterDataObserver
        public void onChanged() {
            RecyclerView.this.assertNotInLayoutOrScroll(null);
            if (RecyclerView.this.mAdapter.hasStableIds()) {
                RecyclerView.this.mState.mStructureChanged = true;
                RecyclerView.this.mDataSetHasChangedAfterLayout = true;
            } else {
                RecyclerView.this.mState.mStructureChanged = true;
                RecyclerView.this.mDataSetHasChangedAfterLayout = true;
            }
            if (!RecyclerView.this.mAdapterHelper.hasPendingUpdates()) {
                RecyclerView.this.requestLayout();
            }
        }

        @Override // android.support.v7.widget.RecyclerView.AdapterDataObserver
        public void onItemRangeChanged(int positionStart, int itemCount) {
            RecyclerView.this.assertNotInLayoutOrScroll(null);
            if (RecyclerView.this.mAdapterHelper.onItemRangeChanged(positionStart, itemCount)) {
                triggerUpdateProcessor();
            }
        }

        @Override // android.support.v7.widget.RecyclerView.AdapterDataObserver
        public void onItemRangeInserted(int positionStart, int itemCount) {
            RecyclerView.this.assertNotInLayoutOrScroll(null);
            if (RecyclerView.this.mAdapterHelper.onItemRangeInserted(positionStart, itemCount)) {
                triggerUpdateProcessor();
            }
        }

        @Override // android.support.v7.widget.RecyclerView.AdapterDataObserver
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            RecyclerView.this.assertNotInLayoutOrScroll(null);
            if (RecyclerView.this.mAdapterHelper.onItemRangeRemoved(positionStart, itemCount)) {
                triggerUpdateProcessor();
            }
        }

        @Override // android.support.v7.widget.RecyclerView.AdapterDataObserver
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            RecyclerView.this.assertNotInLayoutOrScroll(null);
            if (RecyclerView.this.mAdapterHelper.onItemRangeMoved(fromPosition, toPosition, itemCount)) {
                triggerUpdateProcessor();
            }
        }

        void triggerUpdateProcessor() {
            if (!RecyclerView.this.mPostUpdatesOnAnimation || !RecyclerView.this.mHasFixedSize || !RecyclerView.this.mIsAttached) {
                RecyclerView.this.mAdapterUpdateDuringMeasure = true;
                RecyclerView.this.requestLayout();
                return;
            }
            ViewCompat.postOnAnimation(RecyclerView.this, RecyclerView.this.mUpdateChildViewsRunnable);
        }
    }

    /* loaded from: classes.dex */
    public static class RecycledViewPool {
        private static final int DEFAULT_MAX_SCRAP = 5;
        private SparseArray<ArrayList<ViewHolder>> mScrap = new SparseArray<>();
        private SparseIntArray mMaxScrap = new SparseIntArray();
        private int mAttachCount = 0;

        public void clear() {
            this.mScrap.clear();
        }

        public void setMaxRecycledViews(int viewType, int max) {
            this.mMaxScrap.put(viewType, max);
            ArrayList<ViewHolder> scrapHeap = this.mScrap.get(viewType);
            if (scrapHeap != null) {
                while (scrapHeap.size() > max) {
                    scrapHeap.remove(scrapHeap.size() - 1);
                }
            }
        }

        public ViewHolder getRecycledView(int viewType) {
            ArrayList<ViewHolder> scrapHeap = this.mScrap.get(viewType);
            if (scrapHeap == null || scrapHeap.isEmpty()) {
                return null;
            }
            int index = scrapHeap.size() - 1;
            ViewHolder scrap = scrapHeap.get(index);
            scrapHeap.remove(index);
            return scrap;
        }

        int size() {
            int count = 0;
            for (int i = 0; i < this.mScrap.size(); i++) {
                ArrayList<ViewHolder> viewHolders = this.mScrap.valueAt(i);
                if (viewHolders != null) {
                    count += viewHolders.size();
                }
            }
            return count;
        }

        public void putRecycledView(ViewHolder scrap) {
            int viewType = scrap.getItemViewType();
            ArrayList scrapHeap = getScrapHeapForType(viewType);
            if (this.mMaxScrap.get(viewType) > scrapHeap.size()) {
                scrap.resetInternal();
                scrapHeap.add(scrap);
            }
        }

        void attach(Adapter adapter) {
            this.mAttachCount++;
        }

        void detach() {
            this.mAttachCount--;
        }

        void onAdapterChanged(Adapter oldAdapter, Adapter newAdapter, boolean compatibleWithPrevious) {
            if (oldAdapter != null) {
                detach();
            }
            if (!compatibleWithPrevious && this.mAttachCount == 0) {
                clear();
            }
            if (newAdapter != null) {
                attach(newAdapter);
            }
        }

        private ArrayList<ViewHolder> getScrapHeapForType(int viewType) {
            ArrayList<ViewHolder> scrap = this.mScrap.get(viewType);
            if (scrap == null) {
                scrap = new ArrayList<>();
                this.mScrap.put(viewType, scrap);
                if (this.mMaxScrap.indexOfKey(viewType) < 0) {
                    this.mMaxScrap.put(viewType, 5);
                }
            }
            return scrap;
        }
    }

    /* loaded from: classes.dex */
    public final class Recycler {
        private static final int DEFAULT_CACHE_SIZE = 2;
        private RecycledViewPool mRecyclerPool;
        private ViewCacheExtension mViewCacheExtension;
        final ArrayList<ViewHolder> mAttachedScrap = new ArrayList<>();
        private ArrayList<ViewHolder> mChangedScrap = null;
        final ArrayList<ViewHolder> mCachedViews = new ArrayList<>();
        private final List<ViewHolder> mUnmodifiableAttachedScrap = Collections.unmodifiableList(this.mAttachedScrap);
        private int mViewCacheMax = 2;

        public Recycler() {
        }

        public void clear() {
            this.mAttachedScrap.clear();
            recycleAndClearCachedViews();
        }

        public void setViewCacheSize(int viewCount) {
            this.mViewCacheMax = viewCount;
            for (int i = this.mCachedViews.size() - 1; i >= 0 && this.mCachedViews.size() > viewCount; i--) {
                tryToRecycleCachedViewAt(i);
            }
            while (this.mCachedViews.size() > viewCount) {
                this.mCachedViews.remove(this.mCachedViews.size() - 1);
            }
        }

        public List<ViewHolder> getScrapList() {
            return this.mUnmodifiableAttachedScrap;
        }

        boolean validateViewHolderForOffsetPosition(ViewHolder holder) {
            if (holder.isRemoved()) {
                return true;
            }
            if (holder.mPosition < 0 || holder.mPosition >= RecyclerView.this.mAdapter.getItemCount()) {
                throw new IndexOutOfBoundsException("Inconsistency detected. Invalid view holder adapter position" + holder);
            }
            if (!RecyclerView.this.mState.isPreLayout()) {
                int type = RecyclerView.this.mAdapter.getItemViewType(holder.mPosition);
                if (type != holder.getItemViewType()) {
                    return false;
                }
            }
            return !RecyclerView.this.mAdapter.hasStableIds() || holder.getItemId() == RecyclerView.this.mAdapter.getItemId(holder.mPosition);
        }

        public void bindViewToPosition(View view, int position) {
            LayoutParams rvLayoutParams;
            ViewHolder holder = RecyclerView.getChildViewHolderInt(view);
            if (holder == null) {
                throw new IllegalArgumentException("The view does not have a ViewHolder. You cannot pass arbitrary views to this method, they should be created by the Adapter");
            }
            int offsetPosition = RecyclerView.this.mAdapterHelper.findPositionOffset(position);
            if (offsetPosition >= 0 && offsetPosition < RecyclerView.this.mAdapter.getItemCount()) {
                RecyclerView.this.mAdapter.bindViewHolder(holder, offsetPosition);
                attachAccessibilityDelegate(view);
                if (RecyclerView.this.mState.isPreLayout()) {
                    holder.mPreLayoutPosition = position;
                }
                ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
                if (lp == null) {
                    rvLayoutParams = (LayoutParams) RecyclerView.this.generateDefaultLayoutParams();
                    holder.itemView.setLayoutParams(rvLayoutParams);
                } else if (!RecyclerView.this.checkLayoutParams(lp)) {
                    rvLayoutParams = (LayoutParams) RecyclerView.this.generateLayoutParams(lp);
                    holder.itemView.setLayoutParams(rvLayoutParams);
                } else {
                    rvLayoutParams = (LayoutParams) lp;
                }
                rvLayoutParams.mInsetsDirty = true;
                rvLayoutParams.mViewHolder = holder;
                rvLayoutParams.mPendingInvalidate = holder.itemView.getParent() == null;
                return;
            }
            throw new IndexOutOfBoundsException("Inconsistency detected. Invalid item position " + position + "(offset:" + offsetPosition + ").state:" + RecyclerView.this.mState.getItemCount());
        }

        public int convertPreLayoutPositionToPostLayout(int position) {
            if (position < 0 || position >= RecyclerView.this.mState.getItemCount()) {
                throw new IndexOutOfBoundsException("invalid position " + position + ". State item count is " + RecyclerView.this.mState.getItemCount());
            }
            return !RecyclerView.this.mState.isPreLayout() ? position : RecyclerView.this.mAdapterHelper.findPositionOffset(position);
        }

        public View getViewForPosition(int position) {
            return getViewForPosition(position, false);
        }

        View getViewForPosition(int position, boolean dryRun) {
            LayoutParams rvLayoutParams;
            View view;
            boolean z = true;
            if (position < 0 || position >= RecyclerView.this.mState.getItemCount()) {
                throw new IndexOutOfBoundsException("Invalid item position " + position + "(" + position + "). Item count:" + RecyclerView.this.mState.getItemCount());
            }
            boolean fromScrap = false;
            ViewHolder holder = null;
            if (RecyclerView.this.mState.isPreLayout()) {
                holder = getChangedScrapViewForPosition(position);
                fromScrap = holder != null;
            }
            if (holder == null && (holder = getScrapViewForPosition(position, -1, dryRun)) != null) {
                if (!validateViewHolderForOffsetPosition(holder)) {
                    if (!dryRun) {
                        holder.addFlags(4);
                        if (holder.isScrap()) {
                            RecyclerView.this.removeDetachedView(holder.itemView, false);
                            holder.unScrap();
                        } else if (holder.wasReturnedFromScrap()) {
                            holder.clearReturnedFromScrapFlag();
                        }
                        recycleViewHolderInternal(holder);
                    }
                    holder = null;
                } else {
                    fromScrap = true;
                }
            }
            if (holder == null) {
                int offsetPosition = RecyclerView.this.mAdapterHelper.findPositionOffset(position);
                if (offsetPosition >= 0 && offsetPosition < RecyclerView.this.mAdapter.getItemCount()) {
                    int type = RecyclerView.this.mAdapter.getItemViewType(offsetPosition);
                    if (RecyclerView.this.mAdapter.hasStableIds() && (holder = getScrapViewForId(RecyclerView.this.mAdapter.getItemId(offsetPosition), type, dryRun)) != null) {
                        holder.mPosition = offsetPosition;
                        fromScrap = true;
                    }
                    if (holder == null && this.mViewCacheExtension != null && (view = this.mViewCacheExtension.getViewForPositionAndType(this, position, type)) != null) {
                        holder = RecyclerView.this.getChildViewHolder(view);
                        if (holder == null) {
                            throw new IllegalArgumentException("getViewForPositionAndType returned a view which does not have a ViewHolder");
                        }
                        if (holder.shouldIgnore()) {
                            throw new IllegalArgumentException("getViewForPositionAndType returned a view that is ignored. You must call stopIgnoring before returning this view.");
                        }
                    }
                    if (holder == null && (holder = getRecycledViewPool().getRecycledView(RecyclerView.this.mAdapter.getItemViewType(offsetPosition))) != null) {
                        holder.resetInternal();
                        if (RecyclerView.FORCE_INVALIDATE_DISPLAY_LIST) {
                            invalidateDisplayListInt(holder);
                        }
                    }
                    if (holder == null) {
                        holder = RecyclerView.this.mAdapter.createViewHolder(RecyclerView.this, RecyclerView.this.mAdapter.getItemViewType(offsetPosition));
                    }
                } else {
                    throw new IndexOutOfBoundsException("Inconsistency detected. Invalid item position " + position + "(offset:" + offsetPosition + ").state:" + RecyclerView.this.mState.getItemCount());
                }
            }
            boolean bound = false;
            if (RecyclerView.this.mState.isPreLayout() && holder.isBound()) {
                holder.mPreLayoutPosition = position;
            } else if (!holder.isBound() || holder.needsUpdate() || holder.isInvalid()) {
                RecyclerView.this.mAdapter.bindViewHolder(holder, RecyclerView.this.mAdapterHelper.findPositionOffset(position));
                attachAccessibilityDelegate(holder.itemView);
                bound = true;
                if (RecyclerView.this.mState.isPreLayout()) {
                    holder.mPreLayoutPosition = position;
                }
            }
            ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
            if (lp == null) {
                rvLayoutParams = (LayoutParams) RecyclerView.this.generateDefaultLayoutParams();
                holder.itemView.setLayoutParams(rvLayoutParams);
            } else if (!RecyclerView.this.checkLayoutParams(lp)) {
                rvLayoutParams = (LayoutParams) RecyclerView.this.generateLayoutParams(lp);
                holder.itemView.setLayoutParams(rvLayoutParams);
            } else {
                rvLayoutParams = (LayoutParams) lp;
            }
            rvLayoutParams.mViewHolder = holder;
            if (!fromScrap || !bound) {
                z = false;
            }
            rvLayoutParams.mPendingInvalidate = z;
            return holder.itemView;
        }

        private void attachAccessibilityDelegate(View itemView) {
            if (RecyclerView.this.mAccessibilityManager != null && RecyclerView.this.mAccessibilityManager.isEnabled()) {
                if (ViewCompat.getImportantForAccessibility(itemView) == 0) {
                    ViewCompat.setImportantForAccessibility(itemView, 1);
                }
                if (!ViewCompat.hasAccessibilityDelegate(itemView)) {
                    ViewCompat.setAccessibilityDelegate(itemView, RecyclerView.this.mAccessibilityDelegate.getItemDelegate());
                }
            }
        }

        private void invalidateDisplayListInt(ViewHolder holder) {
            if (holder.itemView instanceof ViewGroup) {
                invalidateDisplayListInt((ViewGroup) holder.itemView, false);
            }
        }

        private void invalidateDisplayListInt(ViewGroup viewGroup, boolean invalidateThis) {
            for (int i = viewGroup.getChildCount() - 1; i >= 0; i--) {
                View view = viewGroup.getChildAt(i);
                if (view instanceof ViewGroup) {
                    invalidateDisplayListInt((ViewGroup) view, true);
                }
            }
            if (invalidateThis) {
                if (viewGroup.getVisibility() == 4) {
                    viewGroup.setVisibility(0);
                    viewGroup.setVisibility(4);
                    return;
                }
                int visibility = viewGroup.getVisibility();
                viewGroup.setVisibility(4);
                viewGroup.setVisibility(visibility);
            }
        }

        public void recycleView(View view) {
            ViewHolder holder = RecyclerView.getChildViewHolderInt(view);
            if (holder.isTmpDetached()) {
                RecyclerView.this.removeDetachedView(view, false);
            }
            if (holder.isScrap()) {
                holder.unScrap();
            } else if (holder.wasReturnedFromScrap()) {
                holder.clearReturnedFromScrapFlag();
            }
            recycleViewHolderInternal(holder);
        }

        void recycleViewInternal(View view) {
            recycleViewHolderInternal(RecyclerView.getChildViewHolderInt(view));
        }

        void recycleAndClearCachedViews() {
            int count = this.mCachedViews.size();
            for (int i = count - 1; i >= 0; i--) {
                tryToRecycleCachedViewAt(i);
            }
            this.mCachedViews.clear();
        }

        boolean tryToRecycleCachedViewAt(int cachedViewIndex) {
            ViewHolder viewHolder = this.mCachedViews.get(cachedViewIndex);
            if (viewHolder.isRecyclable()) {
                getRecycledViewPool().putRecycledView(viewHolder);
                dispatchViewRecycled(viewHolder);
                this.mCachedViews.remove(cachedViewIndex);
                return true;
            }
            return false;
        }

        void recycleViewHolderInternal(ViewHolder holder) {
            if (holder.isScrap() || holder.itemView.getParent() != null) {
                throw new IllegalArgumentException("Scrapped or attached views may not be recycled. isScrap:" + holder.isScrap() + " isAttached:" + (holder.itemView.getParent() != null));
            } else if (holder.isTmpDetached()) {
                throw new IllegalArgumentException("Tmp detached view should be removed from RecyclerView before it can be recycled: " + holder);
            } else {
                if (holder.shouldIgnore()) {
                    throw new IllegalArgumentException("Trying to recycle an ignored view holder. You should first call stopIgnoringView(view) before calling recycle.");
                }
                if (holder.isRecyclable()) {
                    boolean cached = false;
                    if (!holder.isInvalid() && ((RecyclerView.this.mState.mInPreLayout || !holder.isRemoved()) && !holder.isChanged())) {
                        if (this.mCachedViews.size() == this.mViewCacheMax && !this.mCachedViews.isEmpty()) {
                            for (int i = 0; i < this.mCachedViews.size() && !tryToRecycleCachedViewAt(i); i++) {
                            }
                        }
                        if (this.mCachedViews.size() < this.mViewCacheMax) {
                            this.mCachedViews.add(holder);
                            cached = true;
                        }
                    }
                    if (!cached) {
                        getRecycledViewPool().putRecycledView(holder);
                        dispatchViewRecycled(holder);
                    }
                }
                RecyclerView.this.mState.onViewRecycled(holder);
            }
        }

        void quickRecycleScrapView(View view) {
            ViewHolder holder = RecyclerView.getChildViewHolderInt(view);
            holder.mScrapContainer = null;
            holder.clearReturnedFromScrapFlag();
            recycleViewHolderInternal(holder);
        }

        void scrapView(View view) {
            ViewHolder holder = RecyclerView.getChildViewHolderInt(view);
            holder.setScrapContainer(this);
            if (!holder.isChanged() || !RecyclerView.this.supportsChangeAnimations()) {
                if (holder.isInvalid() && !holder.isRemoved() && !RecyclerView.this.mAdapter.hasStableIds()) {
                    throw new IllegalArgumentException("Called scrap view with an invalid view. Invalid views cannot be reused from scrap, they should rebound from recycler pool.");
                }
                this.mAttachedScrap.add(holder);
                return;
            }
            if (this.mChangedScrap == null) {
                this.mChangedScrap = new ArrayList<>();
            }
            this.mChangedScrap.add(holder);
        }

        void unscrapView(ViewHolder holder) {
            if (!holder.isChanged() || !RecyclerView.this.supportsChangeAnimations() || this.mChangedScrap == null) {
                this.mAttachedScrap.remove(holder);
            } else {
                this.mChangedScrap.remove(holder);
            }
            holder.mScrapContainer = null;
            holder.clearReturnedFromScrapFlag();
        }

        int getScrapCount() {
            return this.mAttachedScrap.size();
        }

        View getScrapViewAt(int index) {
            return this.mAttachedScrap.get(index).itemView;
        }

        void clearScrap() {
            this.mAttachedScrap.clear();
        }

        ViewHolder getChangedScrapViewForPosition(int position) {
            int changedScrapSize;
            int offsetPosition;
            if (this.mChangedScrap == null || (changedScrapSize = this.mChangedScrap.size()) == 0) {
                return null;
            }
            for (int i = 0; i < changedScrapSize; i++) {
                ViewHolder holder = this.mChangedScrap.get(i);
                if (!holder.wasReturnedFromScrap() && holder.getPosition() == position) {
                    holder.addFlags(32);
                    return holder;
                }
            }
            if (RecyclerView.this.mAdapter.hasStableIds() && (offsetPosition = RecyclerView.this.mAdapterHelper.findPositionOffset(position)) > 0 && offsetPosition < RecyclerView.this.mAdapter.getItemCount()) {
                long id = RecyclerView.this.mAdapter.getItemId(offsetPosition);
                for (int i2 = 0; i2 < changedScrapSize; i2++) {
                    ViewHolder holder2 = this.mChangedScrap.get(i2);
                    if (!holder2.wasReturnedFromScrap() && holder2.getItemId() == id) {
                        holder2.addFlags(32);
                        return holder2;
                    }
                }
            }
            return null;
        }

        /* JADX WARN: Removed duplicated region for block: B:25:0x00a0  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        android.support.v7.widget.RecyclerView.ViewHolder getScrapViewForPosition(int r9, int r10, boolean r11) {
            /*
                r8 = this;
                java.util.ArrayList<android.support.v7.widget.RecyclerView$ViewHolder> r5 = r8.mAttachedScrap
                int r3 = r5.size()
                r2 = 0
            L7:
                if (r2 >= r3) goto L7e
                java.util.ArrayList<android.support.v7.widget.RecyclerView$ViewHolder> r5 = r8.mAttachedScrap
                java.lang.Object r1 = r5.get(r2)
                android.support.v7.widget.RecyclerView$ViewHolder r1 = (android.support.v7.widget.RecyclerView.ViewHolder) r1
                boolean r5 = r1.wasReturnedFromScrap()
                if (r5 != 0) goto Lc2
                int r5 = r1.getPosition()
                if (r5 != r9) goto Lc2
                boolean r5 = r1.isInvalid()
                if (r5 != 0) goto Lc2
                android.support.v7.widget.RecyclerView r5 = android.support.v7.widget.RecyclerView.this
                android.support.v7.widget.RecyclerView$State r5 = r5.mState
                boolean r5 = android.support.v7.widget.RecyclerView.State.access$1500(r5)
                if (r5 != 0) goto L33
                boolean r5 = r1.isRemoved()
                if (r5 != 0) goto Lc2
            L33:
                r5 = -1
                if (r10 == r5) goto Lbc
                int r5 = r1.getItemViewType()
                if (r5 == r10) goto Lbc
                java.lang.String r5 = "RecyclerView"
                java.lang.StringBuilder r6 = new java.lang.StringBuilder
                r6.<init>()
                java.lang.String r7 = "Scrap view for position "
                java.lang.StringBuilder r6 = r6.append(r7)
                java.lang.StringBuilder r6 = r6.append(r9)
                java.lang.String r7 = " isn't dirty but has"
                java.lang.StringBuilder r6 = r6.append(r7)
                java.lang.String r7 = " wrong view type! (found "
                java.lang.StringBuilder r6 = r6.append(r7)
                int r7 = r1.getItemViewType()
                java.lang.StringBuilder r6 = r6.append(r7)
                java.lang.String r7 = " but expected "
                java.lang.StringBuilder r6 = r6.append(r7)
                java.lang.StringBuilder r6 = r6.append(r10)
                java.lang.String r7 = ")"
                java.lang.StringBuilder r6 = r6.append(r7)
                java.lang.String r6 = r6.toString()
                android.util.Log.e(r5, r6)
            L7e:
                if (r11 != 0) goto L97
                android.support.v7.widget.RecyclerView r5 = android.support.v7.widget.RecyclerView.this
                android.support.v7.widget.ChildHelper r5 = r5.mChildHelper
                android.view.View r4 = r5.findHiddenNonRemovedView(r9, r10)
                if (r4 == 0) goto L97
                android.support.v7.widget.RecyclerView r5 = android.support.v7.widget.RecyclerView.this
                android.support.v7.widget.RecyclerView$ItemAnimator r5 = r5.mItemAnimator
                android.support.v7.widget.RecyclerView r6 = android.support.v7.widget.RecyclerView.this
                android.support.v7.widget.RecyclerView$ViewHolder r6 = r6.getChildViewHolder(r4)
                r5.endAnimation(r6)
            L97:
                java.util.ArrayList<android.support.v7.widget.RecyclerView$ViewHolder> r5 = r8.mCachedViews
                int r0 = r5.size()
                r2 = 0
            L9e:
                if (r2 >= r0) goto Lc9
                java.util.ArrayList<android.support.v7.widget.RecyclerView$ViewHolder> r5 = r8.mCachedViews
                java.lang.Object r1 = r5.get(r2)
                android.support.v7.widget.RecyclerView$ViewHolder r1 = (android.support.v7.widget.RecyclerView.ViewHolder) r1
                boolean r5 = r1.isInvalid()
                if (r5 != 0) goto Lc6
                int r5 = r1.getPosition()
                if (r5 != r9) goto Lc6
                if (r11 != 0) goto Lbb
                java.util.ArrayList<android.support.v7.widget.RecyclerView$ViewHolder> r5 = r8.mCachedViews
                r5.remove(r2)
            Lbb:
                return r1
            Lbc:
                r5 = 32
                r1.addFlags(r5)
                goto Lbb
            Lc2:
                int r2 = r2 + 1
                goto L7
            Lc6:
                int r2 = r2 + 1
                goto L9e
            Lc9:
                r1 = 0
                goto Lbb
            */
            throw new UnsupportedOperationException("Method not decompiled: android.support.v7.widget.RecyclerView.Recycler.getScrapViewForPosition(int, int, boolean):android.support.v7.widget.RecyclerView$ViewHolder");
        }

        ViewHolder getScrapViewForId(long id, int type, boolean dryRun) {
            int count = this.mAttachedScrap.size();
            for (int i = count - 1; i >= 0; i--) {
                ViewHolder holder = this.mAttachedScrap.get(i);
                if (holder.getItemId() == id && !holder.wasReturnedFromScrap()) {
                    if (type == holder.getItemViewType()) {
                        holder.addFlags(32);
                        if (holder.isRemoved() && !RecyclerView.this.mState.isPreLayout()) {
                            holder.setFlags(2, 14);
                            return holder;
                        }
                        return holder;
                    } else if (!dryRun) {
                        this.mAttachedScrap.remove(i);
                        RecyclerView.this.removeDetachedView(holder.itemView, false);
                        quickRecycleScrapView(holder.itemView);
                    }
                }
            }
            int cacheSize = this.mCachedViews.size();
            for (int i2 = cacheSize - 1; i2 >= 0; i2--) {
                ViewHolder holder2 = this.mCachedViews.get(i2);
                if (holder2.getItemId() == id) {
                    if (type == holder2.getItemViewType()) {
                        if (!dryRun) {
                            this.mCachedViews.remove(i2);
                            return holder2;
                        }
                        return holder2;
                    } else if (!dryRun) {
                        tryToRecycleCachedViewAt(i2);
                    }
                }
            }
            return null;
        }

        void dispatchViewRecycled(ViewHolder holder) {
            if (RecyclerView.this.mRecyclerListener != null) {
                RecyclerView.this.mRecyclerListener.onViewRecycled(holder);
            }
            if (RecyclerView.this.mAdapter != null) {
                RecyclerView.this.mAdapter.onViewRecycled(holder);
            }
            if (RecyclerView.this.mState != null) {
                RecyclerView.this.mState.onViewRecycled(holder);
            }
        }

        void onAdapterChanged(Adapter oldAdapter, Adapter newAdapter, boolean compatibleWithPrevious) {
            clear();
            getRecycledViewPool().onAdapterChanged(oldAdapter, newAdapter, compatibleWithPrevious);
        }

        void offsetPositionRecordsForMove(int from, int to) {
            int start;
            int end;
            int inBetweenOffset;
            if (from < to) {
                start = from;
                end = to;
                inBetweenOffset = -1;
            } else {
                start = to;
                end = from;
                inBetweenOffset = 1;
            }
            int cachedCount = this.mCachedViews.size();
            for (int i = 0; i < cachedCount; i++) {
                ViewHolder holder = this.mCachedViews.get(i);
                if (holder != null && holder.mPosition >= start && holder.mPosition <= end) {
                    if (holder.mPosition == from) {
                        holder.offsetPosition(to - from, false);
                    } else {
                        holder.offsetPosition(inBetweenOffset, false);
                    }
                }
            }
        }

        void offsetPositionRecordsForInsert(int insertedAt, int count) {
            int cachedCount = this.mCachedViews.size();
            for (int i = 0; i < cachedCount; i++) {
                ViewHolder holder = this.mCachedViews.get(i);
                if (holder != null && holder.getPosition() >= insertedAt) {
                    holder.offsetPosition(count, true);
                }
            }
        }

        void offsetPositionRecordsForRemove(int removedFrom, int count, boolean applyToPreLayout) {
            int removedEnd = removedFrom + count;
            int cachedCount = this.mCachedViews.size();
            for (int i = cachedCount - 1; i >= 0; i--) {
                ViewHolder holder = this.mCachedViews.get(i);
                if (holder != null) {
                    if (holder.getPosition() >= removedEnd) {
                        holder.offsetPosition(-count, applyToPreLayout);
                    } else if (holder.getPosition() >= removedFrom && !tryToRecycleCachedViewAt(i)) {
                        holder.addFlags(4);
                    }
                }
            }
        }

        void setViewCacheExtension(ViewCacheExtension extension) {
            this.mViewCacheExtension = extension;
        }

        void setRecycledViewPool(RecycledViewPool pool) {
            if (this.mRecyclerPool != null) {
                this.mRecyclerPool.detach();
            }
            this.mRecyclerPool = pool;
            if (pool != null) {
                this.mRecyclerPool.attach(RecyclerView.this.getAdapter());
            }
        }

        RecycledViewPool getRecycledViewPool() {
            if (this.mRecyclerPool == null) {
                this.mRecyclerPool = new RecycledViewPool();
            }
            return this.mRecyclerPool;
        }

        void viewRangeUpdate(int positionStart, int itemCount) {
            int pos;
            int positionEnd = positionStart + itemCount;
            int cachedCount = this.mCachedViews.size();
            for (int i = 0; i < cachedCount; i++) {
                ViewHolder holder = this.mCachedViews.get(i);
                if (holder != null && (pos = holder.getPosition()) >= positionStart && pos < positionEnd) {
                    holder.addFlags(2);
                }
            }
        }

        void markKnownViewsInvalid() {
            if (RecyclerView.this.mAdapter != null && RecyclerView.this.mAdapter.hasStableIds()) {
                int cachedCount = this.mCachedViews.size();
                for (int i = 0; i < cachedCount; i++) {
                    ViewHolder holder = this.mCachedViews.get(i);
                    if (holder != null) {
                        holder.addFlags(6);
                    }
                }
                return;
            }
            for (int i2 = this.mCachedViews.size() - 1; i2 >= 0; i2--) {
                if (!tryToRecycleCachedViewAt(i2)) {
                    this.mCachedViews.get(i2).addFlags(6);
                }
            }
        }

        void clearOldPositions() {
            int cachedCount = this.mCachedViews.size();
            for (int i = 0; i < cachedCount; i++) {
                ViewHolder holder = this.mCachedViews.get(i);
                holder.clearOldPosition();
            }
            int scrapCount = this.mAttachedScrap.size();
            for (int i2 = 0; i2 < scrapCount; i2++) {
                this.mAttachedScrap.get(i2).clearOldPosition();
            }
            if (this.mChangedScrap != null) {
                int changedScrapCount = this.mChangedScrap.size();
                for (int i3 = 0; i3 < changedScrapCount; i3++) {
                    this.mChangedScrap.get(i3).clearOldPosition();
                }
            }
        }

        void markItemDecorInsetsDirty() {
            int cachedCount = this.mCachedViews.size();
            for (int i = 0; i < cachedCount; i++) {
                ViewHolder holder = this.mCachedViews.get(i);
                LayoutParams layoutParams = (LayoutParams) holder.itemView.getLayoutParams();
                if (layoutParams != null) {
                    layoutParams.mInsetsDirty = true;
                }
            }
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Adapter<VH extends ViewHolder> {
        private final AdapterDataObservable mObservable = new AdapterDataObservable();
        private boolean mHasStableIds = false;

        public abstract int getItemCount();

        public abstract void onBindViewHolder(VH vh, int i);

        public abstract VH onCreateViewHolder(ViewGroup viewGroup, int i);

        public final VH createViewHolder(ViewGroup parent, int viewType) {
            VH holder = onCreateViewHolder(parent, viewType);
            holder.mItemViewType = viewType;
            return holder;
        }

        public final void bindViewHolder(VH holder, int position) {
            holder.mPosition = position;
            if (hasStableIds()) {
                holder.mItemId = getItemId(position);
            }
            onBindViewHolder(holder, position);
            holder.setFlags(1, 7);
        }

        public int getItemViewType(int position) {
            return 0;
        }

        public void setHasStableIds(boolean hasStableIds) {
            if (hasObservers()) {
                throw new IllegalStateException("Cannot change whether this adapter has stable IDs while the adapter has registered observers.");
            }
            this.mHasStableIds = hasStableIds;
        }

        public long getItemId(int position) {
            return -1L;
        }

        public final boolean hasStableIds() {
            return this.mHasStableIds;
        }

        public void onViewRecycled(VH holder) {
        }

        public void onViewAttachedToWindow(VH holder) {
        }

        public void onViewDetachedFromWindow(VH holder) {
        }

        public final boolean hasObservers() {
            return this.mObservable.hasObservers();
        }

        public void registerAdapterDataObserver(AdapterDataObserver observer) {
            this.mObservable.registerObserver(observer);
        }

        public void unregisterAdapterDataObserver(AdapterDataObserver observer) {
            this.mObservable.unregisterObserver(observer);
        }

        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        }

        public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        }

        public final void notifyDataSetChanged() {
            this.mObservable.notifyChanged();
        }

        public final void notifyItemChanged(int position) {
            this.mObservable.notifyItemRangeChanged(position, 1);
        }

        public final void notifyItemRangeChanged(int positionStart, int itemCount) {
            this.mObservable.notifyItemRangeChanged(positionStart, itemCount);
        }

        public final void notifyItemInserted(int position) {
            this.mObservable.notifyItemRangeInserted(position, 1);
        }

        public final void notifyItemMoved(int fromPosition, int toPosition) {
            this.mObservable.notifyItemMoved(fromPosition, toPosition);
        }

        public final void notifyItemRangeInserted(int positionStart, int itemCount) {
            this.mObservable.notifyItemRangeInserted(positionStart, itemCount);
        }

        public final void notifyItemRemoved(int position) {
            this.mObservable.notifyItemRangeRemoved(position, 1);
        }

        public final void notifyItemRangeRemoved(int positionStart, int itemCount) {
            this.mObservable.notifyItemRangeRemoved(positionStart, itemCount);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dispatchChildDetached(View child) {
        if (this.mAdapter != null) {
            this.mAdapter.onViewDetachedFromWindow(getChildViewHolderInt(child));
        }
        onChildDetachedFromWindow(child);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dispatchChildAttached(View child) {
        if (this.mAdapter != null) {
            this.mAdapter.onViewAttachedToWindow(getChildViewHolderInt(child));
        }
        onChildAttachedToWindow(child);
    }

    /* loaded from: classes.dex */
    public static abstract class LayoutManager {
        ChildHelper mChildHelper;
        RecyclerView mRecyclerView;
        private boolean mRequestedSimpleAnimations = false;
        @Nullable
        SmoothScroller mSmoothScroller;

        public abstract LayoutParams generateDefaultLayoutParams();

        void setRecyclerView(RecyclerView recyclerView) {
            if (recyclerView == null) {
                this.mRecyclerView = null;
                this.mChildHelper = null;
                return;
            }
            this.mRecyclerView = recyclerView;
            this.mChildHelper = recyclerView.mChildHelper;
        }

        public void requestLayout() {
            if (this.mRecyclerView != null) {
                this.mRecyclerView.requestLayout();
            }
        }

        public void assertInLayoutOrScroll(String message) {
            if (this.mRecyclerView != null) {
                this.mRecyclerView.assertInLayoutOrScroll(message);
            }
        }

        public void assertNotInLayoutOrScroll(String message) {
            if (this.mRecyclerView != null) {
                this.mRecyclerView.assertNotInLayoutOrScroll(message);
            }
        }

        public boolean supportsPredictiveItemAnimations() {
            return false;
        }

        public void onAttachedToWindow(RecyclerView view) {
        }

        @Deprecated
        public void onDetachedFromWindow(RecyclerView view) {
        }

        public void onDetachedFromWindow(RecyclerView view, Recycler recycler) {
            onDetachedFromWindow(view);
        }

        public boolean getClipToPadding() {
            return this.mRecyclerView != null && this.mRecyclerView.mClipToPadding;
        }

        public void onLayoutChildren(Recycler recycler, State state) {
            Log.e(RecyclerView.TAG, "You must override onLayoutChildren(Recycler recycler, State state) ");
        }

        public boolean checkLayoutParams(LayoutParams lp) {
            return lp != null;
        }

        public LayoutParams generateLayoutParams(ViewGroup.LayoutParams lp) {
            if (lp instanceof LayoutParams) {
                return new LayoutParams((LayoutParams) lp);
            }
            if (lp instanceof ViewGroup.MarginLayoutParams) {
                return new LayoutParams((ViewGroup.MarginLayoutParams) lp);
            }
            return new LayoutParams(lp);
        }

        public LayoutParams generateLayoutParams(Context c, AttributeSet attrs) {
            return new LayoutParams(c, attrs);
        }

        public int scrollHorizontallyBy(int dx, Recycler recycler, State state) {
            return 0;
        }

        public int scrollVerticallyBy(int dy, Recycler recycler, State state) {
            return 0;
        }

        public boolean canScrollHorizontally() {
            return false;
        }

        public boolean canScrollVertically() {
            return false;
        }

        public void scrollToPosition(int position) {
        }

        public void smoothScrollToPosition(RecyclerView recyclerView, State state, int position) {
            Log.e(RecyclerView.TAG, "You must override smoothScrollToPosition to support smooth scrolling");
        }

        public void startSmoothScroll(SmoothScroller smoothScroller) {
            if (this.mSmoothScroller != null && smoothScroller != this.mSmoothScroller && this.mSmoothScroller.isRunning()) {
                this.mSmoothScroller.stop();
            }
            this.mSmoothScroller = smoothScroller;
            this.mSmoothScroller.start(this.mRecyclerView, this);
        }

        public boolean isSmoothScrolling() {
            return this.mSmoothScroller != null && this.mSmoothScroller.isRunning();
        }

        public int getLayoutDirection() {
            return ViewCompat.getLayoutDirection(this.mRecyclerView);
        }

        public void endAnimation(View view) {
            if (this.mRecyclerView.mItemAnimator != null) {
                this.mRecyclerView.mItemAnimator.endAnimation(RecyclerView.getChildViewHolderInt(view));
            }
        }

        public void addDisappearingView(View child) {
            addDisappearingView(child, -1);
        }

        public void addDisappearingView(View child, int index) {
            addViewInt(child, index, true);
        }

        public void addView(View child) {
            addView(child, -1);
        }

        public void addView(View child, int index) {
            addViewInt(child, index, false);
        }

        private void addViewInt(View child, int index, boolean disappearing) {
            ViewHolder holder = RecyclerView.getChildViewHolderInt(child);
            if (disappearing || holder.isRemoved()) {
                this.mRecyclerView.addToDisappearingList(child);
            } else {
                this.mRecyclerView.removeFromDisappearingList(child);
            }
            LayoutParams lp = (LayoutParams) child.getLayoutParams();
            if (holder.wasReturnedFromScrap() || holder.isScrap()) {
                if (holder.isScrap()) {
                    holder.unScrap();
                } else {
                    holder.clearReturnedFromScrapFlag();
                }
                this.mChildHelper.attachViewToParent(child, index, child.getLayoutParams(), false);
            } else if (child.getParent() == this.mRecyclerView) {
                int currentIndex = this.mChildHelper.indexOfChild(child);
                if (index == -1) {
                    index = this.mChildHelper.getChildCount();
                }
                if (currentIndex == -1) {
                    throw new IllegalStateException("Added View has RecyclerView as parent but view is not a real child. Unfiltered index:" + this.mRecyclerView.indexOfChild(child));
                }
                if (currentIndex != index) {
                    this.mRecyclerView.mLayout.moveView(currentIndex, index);
                }
            } else {
                this.mChildHelper.addView(child, index, false);
                lp.mInsetsDirty = true;
                if (this.mSmoothScroller != null && this.mSmoothScroller.isRunning()) {
                    this.mSmoothScroller.onChildAttachedToWindow(child);
                }
            }
            if (lp.mPendingInvalidate) {
                holder.itemView.invalidate();
                lp.mPendingInvalidate = false;
            }
        }

        public void removeView(View child) {
            this.mChildHelper.removeView(child);
        }

        public void removeViewAt(int index) {
            View child = getChildAt(index);
            if (child != null) {
                this.mChildHelper.removeViewAt(index);
            }
        }

        public void removeAllViews() {
            int childCount = getChildCount();
            for (int i = childCount - 1; i >= 0; i--) {
                getChildAt(i);
                this.mChildHelper.removeViewAt(i);
            }
        }

        public int getPosition(View view) {
            return ((LayoutParams) view.getLayoutParams()).getViewPosition();
        }

        public int getItemViewType(View view) {
            return RecyclerView.getChildViewHolderInt(view).getItemViewType();
        }

        public View findViewByPosition(int position) {
            int childCount = getChildCount();
            for (int i = 0; i < childCount; i++) {
                View child = getChildAt(i);
                ViewHolder vh = RecyclerView.getChildViewHolderInt(child);
                if (vh != null && vh.getPosition() == position && !vh.shouldIgnore() && (this.mRecyclerView.mState.isPreLayout() || !vh.isRemoved())) {
                    return child;
                }
            }
            return null;
        }

        public void detachView(View child) {
            int ind = this.mChildHelper.indexOfChild(child);
            if (ind >= 0) {
                detachViewInternal(ind, child);
            }
        }

        public void detachViewAt(int index) {
            detachViewInternal(index, getChildAt(index));
        }

        private void detachViewInternal(int index, View view) {
            this.mChildHelper.detachViewFromParent(index);
        }

        public void attachView(View child, int index, LayoutParams lp) {
            ViewHolder vh = RecyclerView.getChildViewHolderInt(child);
            if (vh.isRemoved()) {
                this.mRecyclerView.addToDisappearingList(child);
            } else {
                this.mRecyclerView.removeFromDisappearingList(child);
            }
            this.mChildHelper.attachViewToParent(child, index, lp, vh.isRemoved());
        }

        public void attachView(View child, int index) {
            attachView(child, index, (LayoutParams) child.getLayoutParams());
        }

        public void attachView(View child) {
            attachView(child, -1);
        }

        public void removeDetachedView(View child) {
            this.mRecyclerView.removeDetachedView(child, false);
        }

        public void moveView(int fromIndex, int toIndex) {
            View view = getChildAt(fromIndex);
            if (view == null) {
                throw new IllegalArgumentException("Cannot move a child from non-existing index:" + fromIndex);
            }
            detachViewAt(fromIndex);
            attachView(view, toIndex);
        }

        public void detachAndScrapView(View child, Recycler recycler) {
            int index = this.mChildHelper.indexOfChild(child);
            scrapOrRecycleView(recycler, index, child);
        }

        public void detachAndScrapViewAt(int index, Recycler recycler) {
            View child = getChildAt(index);
            scrapOrRecycleView(recycler, index, child);
        }

        public void removeAndRecycleView(View child, Recycler recycler) {
            removeView(child);
            recycler.recycleView(child);
        }

        public void removeAndRecycleViewAt(int index, Recycler recycler) {
            View view = getChildAt(index);
            removeViewAt(index);
            recycler.recycleView(view);
        }

        public int getChildCount() {
            if (this.mChildHelper != null) {
                return this.mChildHelper.getChildCount();
            }
            return 0;
        }

        public View getChildAt(int index) {
            if (this.mChildHelper != null) {
                return this.mChildHelper.getChildAt(index);
            }
            return null;
        }

        public int getWidth() {
            if (this.mRecyclerView != null) {
                return this.mRecyclerView.getWidth();
            }
            return 0;
        }

        public int getHeight() {
            if (this.mRecyclerView != null) {
                return this.mRecyclerView.getHeight();
            }
            return 0;
        }

        public int getPaddingLeft() {
            if (this.mRecyclerView != null) {
                return this.mRecyclerView.getPaddingLeft();
            }
            return 0;
        }

        public int getPaddingTop() {
            if (this.mRecyclerView != null) {
                return this.mRecyclerView.getPaddingTop();
            }
            return 0;
        }

        public int getPaddingRight() {
            if (this.mRecyclerView != null) {
                return this.mRecyclerView.getPaddingRight();
            }
            return 0;
        }

        public int getPaddingBottom() {
            if (this.mRecyclerView != null) {
                return this.mRecyclerView.getPaddingBottom();
            }
            return 0;
        }

        public int getPaddingStart() {
            if (this.mRecyclerView != null) {
                return ViewCompat.getPaddingStart(this.mRecyclerView);
            }
            return 0;
        }

        public int getPaddingEnd() {
            if (this.mRecyclerView != null) {
                return ViewCompat.getPaddingEnd(this.mRecyclerView);
            }
            return 0;
        }

        public boolean isFocused() {
            return this.mRecyclerView != null && this.mRecyclerView.isFocused();
        }

        public boolean hasFocus() {
            return this.mRecyclerView != null && this.mRecyclerView.hasFocus();
        }

        public View getFocusedChild() {
            if (this.mRecyclerView == null) {
                return null;
            }
            View focused = this.mRecyclerView.getFocusedChild();
            if (focused == null || this.mChildHelper.isHidden(focused)) {
                return null;
            }
            return focused;
        }

        public int getItemCount() {
            Adapter a = this.mRecyclerView != null ? this.mRecyclerView.getAdapter() : null;
            if (a != null) {
                return a.getItemCount();
            }
            return 0;
        }

        public void offsetChildrenHorizontal(int dx) {
            if (this.mRecyclerView != null) {
                this.mRecyclerView.offsetChildrenHorizontal(dx);
            }
        }

        public void offsetChildrenVertical(int dy) {
            if (this.mRecyclerView != null) {
                this.mRecyclerView.offsetChildrenVertical(dy);
            }
        }

        public void ignoreView(View view) {
            if (view.getParent() != this.mRecyclerView || this.mRecyclerView.indexOfChild(view) == -1) {
                throw new IllegalArgumentException("View should be fully attached to be ignored");
            }
            ViewHolder vh = RecyclerView.getChildViewHolderInt(view);
            vh.addFlags(128);
            this.mRecyclerView.mState.onViewIgnored(vh);
        }

        public void stopIgnoringView(View view) {
            ViewHolder vh = RecyclerView.getChildViewHolderInt(view);
            vh.stopIgnoring();
            vh.resetInternal();
            vh.addFlags(4);
        }

        public void detachAndScrapAttachedViews(Recycler recycler) {
            int childCount = getChildCount();
            for (int i = childCount - 1; i >= 0; i--) {
                View v = getChildAt(i);
                scrapOrRecycleView(recycler, i, v);
            }
        }

        private void scrapOrRecycleView(Recycler recycler, int index, View view) {
            ViewHolder viewHolder = RecyclerView.getChildViewHolderInt(view);
            if (!viewHolder.shouldIgnore()) {
                if (viewHolder.isInvalid() && !viewHolder.isRemoved() && !viewHolder.isChanged() && !this.mRecyclerView.mAdapter.hasStableIds()) {
                    removeViewAt(index);
                    recycler.recycleViewHolderInternal(viewHolder);
                    return;
                }
                detachViewAt(index);
                recycler.scrapView(view);
            }
        }

        void removeAndRecycleScrapInt(Recycler recycler) {
            int scrapCount = recycler.getScrapCount();
            for (int i = 0; i < scrapCount; i++) {
                View scrap = recycler.getScrapViewAt(i);
                ViewHolder vh = RecyclerView.getChildViewHolderInt(scrap);
                if (!vh.shouldIgnore()) {
                    if (vh.isTmpDetached()) {
                        this.mRecyclerView.removeDetachedView(scrap, false);
                    }
                    recycler.quickRecycleScrapView(scrap);
                }
            }
            recycler.clearScrap();
            if (scrapCount > 0) {
                this.mRecyclerView.invalidate();
            }
        }

        public void measureChild(View child, int widthUsed, int heightUsed) {
            LayoutParams lp = (LayoutParams) child.getLayoutParams();
            Rect insets = this.mRecyclerView.getItemDecorInsetsForChild(child);
            int widthUsed2 = widthUsed + insets.left + insets.right;
            int heightUsed2 = heightUsed + insets.top + insets.bottom;
            int widthSpec = getChildMeasureSpec(getWidth(), getPaddingLeft() + getPaddingRight() + widthUsed2, lp.width, canScrollHorizontally());
            int heightSpec = getChildMeasureSpec(getHeight(), getPaddingTop() + getPaddingBottom() + heightUsed2, lp.height, canScrollVertically());
            child.measure(widthSpec, heightSpec);
        }

        public void measureChildWithMargins(View child, int widthUsed, int heightUsed) {
            LayoutParams lp = (LayoutParams) child.getLayoutParams();
            Rect insets = this.mRecyclerView.getItemDecorInsetsForChild(child);
            int widthUsed2 = widthUsed + insets.left + insets.right;
            int heightUsed2 = heightUsed + insets.top + insets.bottom;
            int widthSpec = getChildMeasureSpec(getWidth(), getPaddingLeft() + getPaddingRight() + lp.leftMargin + lp.rightMargin + widthUsed2, lp.width, canScrollHorizontally());
            int heightSpec = getChildMeasureSpec(getHeight(), getPaddingTop() + getPaddingBottom() + lp.topMargin + lp.bottomMargin + heightUsed2, lp.height, canScrollVertically());
            child.measure(widthSpec, heightSpec);
        }

        public static int getChildMeasureSpec(int parentSize, int padding, int childDimension, boolean canScroll) {
            int size = Math.max(0, parentSize - padding);
            int resultSize = 0;
            int resultMode = 0;
            if (canScroll) {
                if (childDimension >= 0) {
                    resultSize = childDimension;
                    resultMode = 1073741824;
                } else {
                    resultSize = 0;
                    resultMode = 0;
                }
            } else if (childDimension >= 0) {
                resultSize = childDimension;
                resultMode = 1073741824;
            } else if (childDimension == -1) {
                resultSize = size;
                resultMode = 1073741824;
            } else if (childDimension == -2) {
                resultSize = size;
                resultMode = Integer.MIN_VALUE;
            }
            return View.MeasureSpec.makeMeasureSpec(resultSize, resultMode);
        }

        public int getDecoratedMeasuredWidth(View child) {
            Rect insets = ((LayoutParams) child.getLayoutParams()).mDecorInsets;
            return child.getMeasuredWidth() + insets.left + insets.right;
        }

        public int getDecoratedMeasuredHeight(View child) {
            Rect insets = ((LayoutParams) child.getLayoutParams()).mDecorInsets;
            return child.getMeasuredHeight() + insets.top + insets.bottom;
        }

        public void layoutDecorated(View child, int left, int top, int right, int bottom) {
            Rect insets = ((LayoutParams) child.getLayoutParams()).mDecorInsets;
            child.layout(insets.left + left, insets.top + top, right - insets.right, bottom - insets.bottom);
        }

        public int getDecoratedLeft(View child) {
            return child.getLeft() - getLeftDecorationWidth(child);
        }

        public int getDecoratedTop(View child) {
            return child.getTop() - getTopDecorationHeight(child);
        }

        public int getDecoratedRight(View child) {
            return child.getRight() + getRightDecorationWidth(child);
        }

        public int getDecoratedBottom(View child) {
            return child.getBottom() + getBottomDecorationHeight(child);
        }

        public void calculateItemDecorationsForChild(View child, Rect outRect) {
            if (this.mRecyclerView == null) {
                outRect.set(0, 0, 0, 0);
                return;
            }
            Rect insets = this.mRecyclerView.getItemDecorInsetsForChild(child);
            outRect.set(insets);
        }

        public int getTopDecorationHeight(View child) {
            return ((LayoutParams) child.getLayoutParams()).mDecorInsets.top;
        }

        public int getBottomDecorationHeight(View child) {
            return ((LayoutParams) child.getLayoutParams()).mDecorInsets.bottom;
        }

        public int getLeftDecorationWidth(View child) {
            return ((LayoutParams) child.getLayoutParams()).mDecorInsets.left;
        }

        public int getRightDecorationWidth(View child) {
            return ((LayoutParams) child.getLayoutParams()).mDecorInsets.right;
        }

        public View onFocusSearchFailed(View focused, int direction, Recycler recycler, State state) {
            return null;
        }

        public View onInterceptFocusSearch(View focused, int direction) {
            return null;
        }

        public boolean requestChildRectangleOnScreen(RecyclerView parent, View child, Rect rect, boolean immediate) {
            int dx;
            int parentLeft = getPaddingLeft();
            int parentTop = getPaddingTop();
            int parentRight = getWidth() - getPaddingRight();
            int parentBottom = getHeight() - getPaddingBottom();
            int childLeft = child.getLeft() + rect.left;
            int childTop = child.getTop() + rect.top;
            int childRight = childLeft + rect.width();
            int childBottom = childTop + rect.height();
            int offScreenLeft = Math.min(0, childLeft - parentLeft);
            int offScreenTop = Math.min(0, childTop - parentTop);
            int offScreenRight = Math.max(0, childRight - parentRight);
            int offScreenBottom = Math.max(0, childBottom - parentBottom);
            if (ViewCompat.getLayoutDirection(parent) == 1) {
                dx = offScreenRight != 0 ? offScreenRight : offScreenLeft;
            } else {
                dx = offScreenLeft != 0 ? offScreenLeft : offScreenRight;
            }
            int dy = offScreenTop != 0 ? offScreenTop : offScreenBottom;
            if (dx != 0 || dy != 0) {
                if (immediate) {
                    parent.scrollBy(dx, dy);
                } else {
                    parent.smoothScrollBy(dx, dy);
                }
                return true;
            }
            return false;
        }

        @Deprecated
        public boolean onRequestChildFocus(RecyclerView parent, View child, View focused) {
            return isSmoothScrolling() || parent.mRunningLayoutOrScroll;
        }

        public boolean onRequestChildFocus(RecyclerView parent, State state, View child, View focused) {
            return onRequestChildFocus(parent, child, focused);
        }

        public void onAdapterChanged(Adapter oldAdapter, Adapter newAdapter) {
        }

        public boolean onAddFocusables(RecyclerView recyclerView, ArrayList<View> views, int direction, int focusableMode) {
            return false;
        }

        public void onItemsChanged(RecyclerView recyclerView) {
        }

        public void onItemsAdded(RecyclerView recyclerView, int positionStart, int itemCount) {
        }

        public void onItemsRemoved(RecyclerView recyclerView, int positionStart, int itemCount) {
        }

        public void onItemsUpdated(RecyclerView recyclerView, int positionStart, int itemCount) {
        }

        public void onItemsMoved(RecyclerView recyclerView, int from, int to, int itemCount) {
        }

        public int computeHorizontalScrollExtent(State state) {
            return 0;
        }

        public int computeHorizontalScrollOffset(State state) {
            return 0;
        }

        public int computeHorizontalScrollRange(State state) {
            return 0;
        }

        public int computeVerticalScrollExtent(State state) {
            return 0;
        }

        public int computeVerticalScrollOffset(State state) {
            return 0;
        }

        public int computeVerticalScrollRange(State state) {
            return 0;
        }

        public void onMeasure(Recycler recycler, State state, int widthSpec, int heightSpec) {
            int width;
            int height;
            int widthMode = View.MeasureSpec.getMode(widthSpec);
            int heightMode = View.MeasureSpec.getMode(heightSpec);
            int widthSize = View.MeasureSpec.getSize(widthSpec);
            int heightSize = View.MeasureSpec.getSize(heightSpec);
            switch (widthMode) {
                case Integer.MIN_VALUE:
                case 1073741824:
                    width = widthSize;
                    break;
                default:
                    width = getMinimumWidth();
                    break;
            }
            switch (heightMode) {
                case Integer.MIN_VALUE:
                case 1073741824:
                    height = heightSize;
                    break;
                default:
                    height = getMinimumHeight();
                    break;
            }
            setMeasuredDimension(width, height);
        }

        public void setMeasuredDimension(int widthSize, int heightSize) {
            this.mRecyclerView.setMeasuredDimension(widthSize, heightSize);
        }

        public int getMinimumWidth() {
            return ViewCompat.getMinimumWidth(this.mRecyclerView);
        }

        public int getMinimumHeight() {
            return ViewCompat.getMinimumHeight(this.mRecyclerView);
        }

        public Parcelable onSaveInstanceState() {
            return null;
        }

        public void onRestoreInstanceState(Parcelable state) {
        }

        void stopSmoothScroller() {
            if (this.mSmoothScroller != null) {
                this.mSmoothScroller.stop();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void onSmoothScrollerStopped(SmoothScroller smoothScroller) {
            if (this.mSmoothScroller == smoothScroller) {
                this.mSmoothScroller = null;
            }
        }

        public void onScrollStateChanged(int state) {
        }

        public void removeAndRecycleAllViews(Recycler recycler) {
            for (int i = getChildCount() - 1; i >= 0; i--) {
                View view = getChildAt(i);
                if (!RecyclerView.getChildViewHolderInt(view).shouldIgnore()) {
                    removeAndRecycleViewAt(i, recycler);
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfoCompat info) {
            onInitializeAccessibilityNodeInfo(this.mRecyclerView.mRecycler, this.mRecyclerView.mState, info);
        }

        public void onInitializeAccessibilityNodeInfo(Recycler recycler, State state, AccessibilityNodeInfoCompat info) {
            info.setClassName(RecyclerView.class.getName());
            if (ViewCompat.canScrollVertically(this.mRecyclerView, -1) || ViewCompat.canScrollHorizontally(this.mRecyclerView, -1)) {
                info.addAction(8192);
                info.setScrollable(true);
            }
            if (ViewCompat.canScrollVertically(this.mRecyclerView, 1) || ViewCompat.canScrollHorizontally(this.mRecyclerView, 1)) {
                info.addAction(4096);
                info.setScrollable(true);
            }
            AccessibilityNodeInfoCompat.CollectionInfoCompat collectionInfo = AccessibilityNodeInfoCompat.CollectionInfoCompat.obtain(getRowCountForAccessibility(recycler, state), getColumnCountForAccessibility(recycler, state), isLayoutHierarchical(recycler, state), getSelectionModeForAccessibility(recycler, state));
            info.setCollectionInfo(collectionInfo);
        }

        public void onInitializeAccessibilityEvent(AccessibilityEvent event) {
            onInitializeAccessibilityEvent(this.mRecyclerView.mRecycler, this.mRecyclerView.mState, event);
        }

        public void onInitializeAccessibilityEvent(Recycler recycler, State state, AccessibilityEvent event) {
            boolean z = true;
            AccessibilityRecordCompat record = AccessibilityEventCompat.asRecord(event);
            if (this.mRecyclerView != null && record != null) {
                if (!ViewCompat.canScrollVertically(this.mRecyclerView, 1) && !ViewCompat.canScrollVertically(this.mRecyclerView, -1) && !ViewCompat.canScrollHorizontally(this.mRecyclerView, -1) && !ViewCompat.canScrollHorizontally(this.mRecyclerView, 1)) {
                    z = false;
                }
                record.setScrollable(z);
                if (this.mRecyclerView.mAdapter != null) {
                    record.setItemCount(this.mRecyclerView.mAdapter.getItemCount());
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public void onInitializeAccessibilityNodeInfoForItem(View host, AccessibilityNodeInfoCompat info) {
            ViewHolder vh = RecyclerView.getChildViewHolderInt(host);
            if (vh != null && !vh.isRemoved()) {
                onInitializeAccessibilityNodeInfoForItem(this.mRecyclerView.mRecycler, this.mRecyclerView.mState, host, info);
            }
        }

        public void onInitializeAccessibilityNodeInfoForItem(Recycler recycler, State state, View host, AccessibilityNodeInfoCompat info) {
            int rowIndexGuess = canScrollVertically() ? getPosition(host) : 0;
            int columnIndexGuess = canScrollHorizontally() ? getPosition(host) : 0;
            AccessibilityNodeInfoCompat.CollectionItemInfoCompat itemInfo = AccessibilityNodeInfoCompat.CollectionItemInfoCompat.obtain(rowIndexGuess, 1, columnIndexGuess, 1, false, false);
            info.setCollectionItemInfo(itemInfo);
        }

        public void requestSimpleAnimationsInNextLayout() {
            this.mRequestedSimpleAnimations = true;
        }

        public int getSelectionModeForAccessibility(Recycler recycler, State state) {
            return 0;
        }

        public int getRowCountForAccessibility(Recycler recycler, State state) {
            if (this.mRecyclerView == null || this.mRecyclerView.mAdapter == null || !canScrollVertically()) {
                return 1;
            }
            return this.mRecyclerView.mAdapter.getItemCount();
        }

        public int getColumnCountForAccessibility(Recycler recycler, State state) {
            if (this.mRecyclerView == null || this.mRecyclerView.mAdapter == null || !canScrollHorizontally()) {
                return 1;
            }
            return this.mRecyclerView.mAdapter.getItemCount();
        }

        public boolean isLayoutHierarchical(Recycler recycler, State state) {
            return false;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public boolean performAccessibilityAction(int action, Bundle args) {
            return performAccessibilityAction(this.mRecyclerView.mRecycler, this.mRecyclerView.mState, action, args);
        }

        public boolean performAccessibilityAction(Recycler recycler, State state, int action, Bundle args) {
            if (this.mRecyclerView == null) {
                return false;
            }
            int vScroll = 0;
            int hScroll = 0;
            switch (action) {
                case 4096:
                    if (ViewCompat.canScrollVertically(this.mRecyclerView, 1)) {
                        vScroll = (getHeight() - getPaddingTop()) - getPaddingBottom();
                    }
                    if (ViewCompat.canScrollHorizontally(this.mRecyclerView, 1)) {
                        hScroll = (getWidth() - getPaddingLeft()) - getPaddingRight();
                        break;
                    }
                    break;
                case 8192:
                    if (ViewCompat.canScrollVertically(this.mRecyclerView, -1)) {
                        vScroll = -((getHeight() - getPaddingTop()) - getPaddingBottom());
                    }
                    if (ViewCompat.canScrollHorizontally(this.mRecyclerView, -1)) {
                        hScroll = -((getWidth() - getPaddingLeft()) - getPaddingRight());
                        break;
                    }
                    break;
            }
            if (vScroll == 0 && hScroll == 0) {
                return false;
            }
            this.mRecyclerView.scrollBy(hScroll, vScroll);
            return true;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public boolean performAccessibilityActionForItem(View view, int action, Bundle args) {
            return performAccessibilityActionForItem(this.mRecyclerView.mRecycler, this.mRecyclerView.mState, view, action, args);
        }

        public boolean performAccessibilityActionForItem(Recycler recycler, State state, View view, int action, Bundle args) {
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void removeFromDisappearingList(View child) {
        this.mDisappearingViewsInLayoutPass.remove(child);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void addToDisappearingList(View child) {
        if (!this.mDisappearingViewsInLayoutPass.contains(child)) {
            this.mDisappearingViewsInLayoutPass.add(child);
        }
    }

    /* loaded from: classes.dex */
    public static abstract class ItemDecoration {
        public void onDraw(Canvas c, RecyclerView parent, State state) {
            onDraw(c, parent);
        }

        @Deprecated
        public void onDraw(Canvas c, RecyclerView parent) {
        }

        public void onDrawOver(Canvas c, RecyclerView parent, State state) {
            onDrawOver(c, parent);
        }

        @Deprecated
        public void onDrawOver(Canvas c, RecyclerView parent) {
        }

        @Deprecated
        public void getItemOffsets(Rect outRect, int itemPosition, RecyclerView parent) {
            outRect.set(0, 0, 0, 0);
        }

        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, State state) {
            getItemOffsets(outRect, ((LayoutParams) view.getLayoutParams()).getViewPosition(), parent);
        }
    }

    /* loaded from: classes.dex */
    public static abstract class OnScrollListener {
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        }

        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        }
    }

    /* loaded from: classes.dex */
    public static abstract class ViewHolder {
        static final int FLAG_BOUND = 1;
        static final int FLAG_CHANGED = 64;
        static final int FLAG_IGNORE = 128;
        static final int FLAG_INVALID = 4;
        static final int FLAG_NOT_RECYCLABLE = 16;
        static final int FLAG_REMOVED = 8;
        static final int FLAG_RETURNED_FROM_SCRAP = 32;
        static final int FLAG_TMP_DETACHED = 256;
        static final int FLAG_UPDATE = 2;
        public final View itemView;
        private int mFlags;
        int mPosition = -1;
        int mOldPosition = -1;
        long mItemId = -1;
        int mItemViewType = -1;
        int mPreLayoutPosition = -1;
        ViewHolder mShadowedHolder = null;
        ViewHolder mShadowingHolder = null;
        private int mIsRecyclableCount = 0;
        private Recycler mScrapContainer = null;

        public ViewHolder(View itemView) {
            if (itemView == null) {
                throw new IllegalArgumentException("itemView may not be null");
            }
            this.itemView = itemView;
        }

        void flagRemovedAndOffsetPosition(int mNewPosition, int offset, boolean applyToPreLayout) {
            addFlags(8);
            offsetPosition(offset, applyToPreLayout);
            this.mPosition = mNewPosition;
        }

        void offsetPosition(int offset, boolean applyToPreLayout) {
            if (this.mOldPosition == -1) {
                this.mOldPosition = this.mPosition;
            }
            if (this.mPreLayoutPosition == -1) {
                this.mPreLayoutPosition = this.mPosition;
            }
            if (applyToPreLayout) {
                this.mPreLayoutPosition += offset;
            }
            this.mPosition += offset;
            if (this.itemView.getLayoutParams() != null) {
                ((LayoutParams) this.itemView.getLayoutParams()).mInsetsDirty = true;
            }
        }

        void clearOldPosition() {
            this.mOldPosition = -1;
            this.mPreLayoutPosition = -1;
        }

        void saveOldPosition() {
            if (this.mOldPosition == -1) {
                this.mOldPosition = this.mPosition;
            }
        }

        boolean shouldIgnore() {
            return (this.mFlags & 128) != 0;
        }

        public final int getPosition() {
            return this.mPreLayoutPosition == -1 ? this.mPosition : this.mPreLayoutPosition;
        }

        public final int getOldPosition() {
            return this.mOldPosition;
        }

        public final long getItemId() {
            return this.mItemId;
        }

        public final int getItemViewType() {
            return this.mItemViewType;
        }

        boolean isScrap() {
            return this.mScrapContainer != null;
        }

        void unScrap() {
            this.mScrapContainer.unscrapView(this);
        }

        boolean wasReturnedFromScrap() {
            return (this.mFlags & 32) != 0;
        }

        void clearReturnedFromScrapFlag() {
            this.mFlags &= -33;
        }

        void clearTmpDetachFlag() {
            this.mFlags &= -257;
        }

        void stopIgnoring() {
            this.mFlags &= -129;
        }

        void setScrapContainer(Recycler recycler) {
            this.mScrapContainer = recycler;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public boolean isInvalid() {
            return (this.mFlags & 4) != 0;
        }

        boolean needsUpdate() {
            return (this.mFlags & 2) != 0;
        }

        boolean isChanged() {
            return (this.mFlags & 64) != 0;
        }

        boolean isBound() {
            return (this.mFlags & 1) != 0;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public boolean isRemoved() {
            return (this.mFlags & 8) != 0;
        }

        boolean isTmpDetached() {
            return (this.mFlags & 256) != 0;
        }

        void setFlags(int flags, int mask) {
            this.mFlags = (this.mFlags & (mask ^ (-1))) | (flags & mask);
        }

        void addFlags(int flags) {
            this.mFlags |= flags;
        }

        void resetInternal() {
            this.mFlags = 0;
            this.mPosition = -1;
            this.mOldPosition = -1;
            this.mItemId = -1L;
            this.mPreLayoutPosition = -1;
            this.mIsRecyclableCount = 0;
            this.mShadowedHolder = null;
            this.mShadowingHolder = null;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder("ViewHolder{" + Integer.toHexString(hashCode()) + " position=" + this.mPosition + " id=" + this.mItemId + ", oldPos=" + this.mOldPosition + ", pLpos:" + this.mPreLayoutPosition);
            if (isScrap()) {
                sb.append(" scrap");
            }
            if (isInvalid()) {
                sb.append(" invalid");
            }
            if (!isBound()) {
                sb.append(" unbound");
            }
            if (needsUpdate()) {
                sb.append(" update");
            }
            if (isRemoved()) {
                sb.append(" removed");
            }
            if (shouldIgnore()) {
                sb.append(" ignored");
            }
            if (isChanged()) {
                sb.append(" changed");
            }
            if (isTmpDetached()) {
                sb.append(" tmpDetached");
            }
            if (!isRecyclable()) {
                sb.append(" not recyclable(" + this.mIsRecyclableCount + ")");
            }
            if (this.itemView.getParent() == null) {
                sb.append(" no parent");
            }
            sb.append("}");
            return sb.toString();
        }

        public final void setIsRecyclable(boolean recyclable) {
            this.mIsRecyclableCount = recyclable ? this.mIsRecyclableCount - 1 : this.mIsRecyclableCount + 1;
            if (this.mIsRecyclableCount < 0) {
                this.mIsRecyclableCount = 0;
                Log.e("View", "isRecyclable decremented below 0: unmatched pair of setIsRecyable() calls for " + this);
            } else if (!recyclable && this.mIsRecyclableCount == 1) {
                this.mFlags |= 16;
            } else if (recyclable && this.mIsRecyclableCount == 0) {
                this.mFlags &= -17;
            }
        }

        public final boolean isRecyclable() {
            return (this.mFlags & 16) == 0 && !ViewCompat.hasTransientState(this.itemView);
        }
    }

    /* loaded from: classes.dex */
    public static class LayoutParams extends ViewGroup.MarginLayoutParams {
        final Rect mDecorInsets;
        boolean mInsetsDirty;
        boolean mPendingInvalidate;
        ViewHolder mViewHolder;

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
            this.mDecorInsets = new Rect();
            this.mInsetsDirty = true;
            this.mPendingInvalidate = false;
        }

        public LayoutParams(int width, int height) {
            super(width, height);
            this.mDecorInsets = new Rect();
            this.mInsetsDirty = true;
            this.mPendingInvalidate = false;
        }

        public LayoutParams(ViewGroup.MarginLayoutParams source) {
            super(source);
            this.mDecorInsets = new Rect();
            this.mInsetsDirty = true;
            this.mPendingInvalidate = false;
        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
            this.mDecorInsets = new Rect();
            this.mInsetsDirty = true;
            this.mPendingInvalidate = false;
        }

        public LayoutParams(LayoutParams source) {
            super((ViewGroup.LayoutParams) source);
            this.mDecorInsets = new Rect();
            this.mInsetsDirty = true;
            this.mPendingInvalidate = false;
        }

        public boolean viewNeedsUpdate() {
            return this.mViewHolder.needsUpdate();
        }

        public boolean isViewInvalid() {
            return this.mViewHolder.isInvalid();
        }

        public boolean isItemRemoved() {
            return this.mViewHolder.isRemoved();
        }

        public boolean isItemChanged() {
            return this.mViewHolder.isChanged();
        }

        public int getViewPosition() {
            return this.mViewHolder.getPosition();
        }
    }

    /* loaded from: classes.dex */
    public static abstract class AdapterDataObserver {
        public void onChanged() {
        }

        public void onItemRangeChanged(int positionStart, int itemCount) {
        }

        public void onItemRangeInserted(int positionStart, int itemCount) {
        }

        public void onItemRangeRemoved(int positionStart, int itemCount) {
        }

        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
        }
    }

    /* loaded from: classes.dex */
    public static abstract class SmoothScroller {
        private LayoutManager mLayoutManager;
        private boolean mPendingInitialRun;
        private RecyclerView mRecyclerView;
        private boolean mRunning;
        private View mTargetView;
        private int mTargetPosition = -1;
        private final Action mRecyclingAction = new Action(0, 0);

        protected abstract void onSeekTargetStep(int i, int i2, State state, Action action);

        protected abstract void onStart();

        protected abstract void onStop();

        protected abstract void onTargetFound(View view, State state, Action action);

        void start(RecyclerView recyclerView, LayoutManager layoutManager) {
            this.mRecyclerView = recyclerView;
            this.mLayoutManager = layoutManager;
            if (this.mTargetPosition == -1) {
                throw new IllegalArgumentException("Invalid target position");
            }
            this.mRecyclerView.mState.mTargetPosition = this.mTargetPosition;
            this.mRunning = true;
            this.mPendingInitialRun = true;
            this.mTargetView = findViewByPosition(getTargetPosition());
            onStart();
            this.mRecyclerView.mViewFlinger.postOnAnimation();
        }

        public void setTargetPosition(int targetPosition) {
            this.mTargetPosition = targetPosition;
        }

        public LayoutManager getLayoutManager() {
            return this.mLayoutManager;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        public final void stop() {
            if (this.mRunning) {
                onStop();
                this.mRecyclerView.mState.mTargetPosition = -1;
                this.mTargetView = null;
                this.mTargetPosition = -1;
                this.mPendingInitialRun = false;
                this.mRunning = false;
                this.mLayoutManager.onSmoothScrollerStopped(this);
                this.mLayoutManager = null;
                this.mRecyclerView = null;
            }
        }

        public boolean isPendingInitialRun() {
            return this.mPendingInitialRun;
        }

        public boolean isRunning() {
            return this.mRunning;
        }

        public int getTargetPosition() {
            return this.mTargetPosition;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void onAnimation(int dx, int dy) {
            if (!this.mRunning || this.mTargetPosition == -1) {
                stop();
            }
            this.mPendingInitialRun = false;
            if (this.mTargetView != null) {
                if (getChildPosition(this.mTargetView) == this.mTargetPosition) {
                    onTargetFound(this.mTargetView, this.mRecyclerView.mState, this.mRecyclingAction);
                    this.mRecyclingAction.runIfNecessary(this.mRecyclerView);
                    stop();
                } else {
                    Log.e(RecyclerView.TAG, "Passed over target position while smooth scrolling.");
                    this.mTargetView = null;
                }
            }
            if (this.mRunning) {
                onSeekTargetStep(dx, dy, this.mRecyclerView.mState, this.mRecyclingAction);
                this.mRecyclingAction.runIfNecessary(this.mRecyclerView);
            }
        }

        public int getChildPosition(View view) {
            return this.mRecyclerView.getChildPosition(view);
        }

        public int getChildCount() {
            return this.mRecyclerView.mLayout.getChildCount();
        }

        public View findViewByPosition(int position) {
            return this.mRecyclerView.mLayout.findViewByPosition(position);
        }

        public void instantScrollToPosition(int position) {
            this.mRecyclerView.scrollToPosition(position);
        }

        protected void onChildAttachedToWindow(View child) {
            if (getChildPosition(child) == getTargetPosition()) {
                this.mTargetView = child;
            }
        }

        /* JADX INFO: Access modifiers changed from: protected */
        public void normalize(PointF scrollVector) {
            double magnitute = Math.sqrt((scrollVector.x * scrollVector.x) + (scrollVector.y * scrollVector.y));
            scrollVector.x = (float) (scrollVector.x / magnitute);
            scrollVector.y = (float) (scrollVector.y / magnitute);
        }

        /* loaded from: classes.dex */
        public static class Action {
            public static final int UNDEFINED_DURATION = Integer.MIN_VALUE;
            private boolean changed;
            private int consecutiveUpdates;
            private int mDuration;
            private int mDx;
            private int mDy;
            private Interpolator mInterpolator;

            public Action(int dx, int dy) {
                this(dx, dy, Integer.MIN_VALUE, null);
            }

            public Action(int dx, int dy, int duration) {
                this(dx, dy, duration, null);
            }

            public Action(int dx, int dy, int duration, Interpolator interpolator) {
                this.changed = false;
                this.consecutiveUpdates = 0;
                this.mDx = dx;
                this.mDy = dy;
                this.mDuration = duration;
                this.mInterpolator = interpolator;
            }

            /* JADX INFO: Access modifiers changed from: private */
            public void runIfNecessary(RecyclerView recyclerView) {
                if (this.changed) {
                    validate();
                    if (this.mInterpolator != null) {
                        recyclerView.mViewFlinger.smoothScrollBy(this.mDx, this.mDy, this.mDuration, this.mInterpolator);
                    } else if (this.mDuration == Integer.MIN_VALUE) {
                        recyclerView.mViewFlinger.smoothScrollBy(this.mDx, this.mDy);
                    } else {
                        recyclerView.mViewFlinger.smoothScrollBy(this.mDx, this.mDy, this.mDuration);
                    }
                    this.consecutiveUpdates++;
                    if (this.consecutiveUpdates > 10) {
                        Log.e(RecyclerView.TAG, "Smooth Scroll action is being updated too frequently. Make sure you are not changing it unless necessary");
                    }
                    this.changed = false;
                    return;
                }
                this.consecutiveUpdates = 0;
            }

            private void validate() {
                if (this.mInterpolator != null && this.mDuration < 1) {
                    throw new IllegalStateException("If you provide an interpolator, you must set a positive duration");
                }
                if (this.mDuration < 1) {
                    throw new IllegalStateException("Scroll duration must be a positive number");
                }
            }

            public int getDx() {
                return this.mDx;
            }

            public void setDx(int dx) {
                this.changed = true;
                this.mDx = dx;
            }

            public int getDy() {
                return this.mDy;
            }

            public void setDy(int dy) {
                this.changed = true;
                this.mDy = dy;
            }

            public int getDuration() {
                return this.mDuration;
            }

            public void setDuration(int duration) {
                this.changed = true;
                this.mDuration = duration;
            }

            public Interpolator getInterpolator() {
                return this.mInterpolator;
            }

            public void setInterpolator(Interpolator interpolator) {
                this.changed = true;
                this.mInterpolator = interpolator;
            }

            public void update(int dx, int dy, int duration, Interpolator interpolator) {
                this.mDx = dx;
                this.mDy = dy;
                this.mDuration = duration;
                this.mInterpolator = interpolator;
                this.changed = true;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class AdapterDataObservable extends Observable<AdapterDataObserver> {
        AdapterDataObservable() {
        }

        public boolean hasObservers() {
            return !this.mObservers.isEmpty();
        }

        public void notifyChanged() {
            for (int i = this.mObservers.size() - 1; i >= 0; i--) {
                ((AdapterDataObserver) this.mObservers.get(i)).onChanged();
            }
        }

        public void notifyItemRangeChanged(int positionStart, int itemCount) {
            for (int i = this.mObservers.size() - 1; i >= 0; i--) {
                ((AdapterDataObserver) this.mObservers.get(i)).onItemRangeChanged(positionStart, itemCount);
            }
        }

        public void notifyItemRangeInserted(int positionStart, int itemCount) {
            for (int i = this.mObservers.size() - 1; i >= 0; i--) {
                ((AdapterDataObserver) this.mObservers.get(i)).onItemRangeInserted(positionStart, itemCount);
            }
        }

        public void notifyItemRangeRemoved(int positionStart, int itemCount) {
            for (int i = this.mObservers.size() - 1; i >= 0; i--) {
                ((AdapterDataObserver) this.mObservers.get(i)).onItemRangeRemoved(positionStart, itemCount);
            }
        }

        public void notifyItemMoved(int fromPosition, int toPosition) {
            for (int i = this.mObservers.size() - 1; i >= 0; i--) {
                ((AdapterDataObserver) this.mObservers.get(i)).onItemRangeMoved(fromPosition, toPosition, 1);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class SavedState extends View.BaseSavedState {
        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() { // from class: android.support.v7.widget.RecyclerView.SavedState.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
        Parcelable mLayoutState;

        SavedState(Parcel in) {
            super(in);
            this.mLayoutState = in.readParcelable(LayoutManager.class.getClassLoader());
        }

        SavedState(Parcelable superState) {
            super(superState);
        }

        @Override // android.view.View.BaseSavedState, android.view.AbsSavedState, android.os.Parcelable
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeParcelable(this.mLayoutState, 0);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void copyFrom(SavedState other) {
            this.mLayoutState = other.mLayoutState;
        }
    }

    /* loaded from: classes.dex */
    public static class State {
        private SparseArray<Object> mData;
        private int mTargetPosition = -1;
        ArrayMap<ViewHolder, ItemHolderInfo> mPreLayoutHolderMap = new ArrayMap<>();
        ArrayMap<ViewHolder, ItemHolderInfo> mPostLayoutHolderMap = new ArrayMap<>();
        ArrayMap<Long, ViewHolder> mOldChangedHolders = new ArrayMap<>();
        int mItemCount = 0;
        private int mPreviousLayoutItemCount = 0;
        private int mDeletedInvisibleItemCountSincePreviousLayout = 0;
        private boolean mStructureChanged = false;
        private boolean mInPreLayout = false;
        private boolean mRunSimpleAnimations = false;
        private boolean mRunPredictiveAnimations = false;

        static /* synthetic */ int access$1012(State x0, int x1) {
            int i = x0.mDeletedInvisibleItemCountSincePreviousLayout + x1;
            x0.mDeletedInvisibleItemCountSincePreviousLayout = i;
            return i;
        }

        State reset() {
            this.mTargetPosition = -1;
            if (this.mData != null) {
                this.mData.clear();
            }
            this.mItemCount = 0;
            this.mStructureChanged = false;
            return this;
        }

        public boolean isPreLayout() {
            return this.mInPreLayout;
        }

        public boolean willRunPredictiveAnimations() {
            return this.mRunPredictiveAnimations;
        }

        public boolean willRunSimpleAnimations() {
            return this.mRunSimpleAnimations;
        }

        public void remove(int resourceId) {
            if (this.mData != null) {
                this.mData.remove(resourceId);
            }
        }

        public <T> T get(int resourceId) {
            if (this.mData == null) {
                return null;
            }
            return (T) this.mData.get(resourceId);
        }

        public void put(int resourceId, Object data) {
            if (this.mData == null) {
                this.mData = new SparseArray<>();
            }
            this.mData.put(resourceId, data);
        }

        public int getTargetScrollPosition() {
            return this.mTargetPosition;
        }

        public boolean hasTargetScrollPosition() {
            return this.mTargetPosition != -1;
        }

        public boolean didStructureChange() {
            return this.mStructureChanged;
        }

        public int getItemCount() {
            return this.mInPreLayout ? this.mPreviousLayoutItemCount - this.mDeletedInvisibleItemCountSincePreviousLayout : this.mItemCount;
        }

        public void onViewRecycled(ViewHolder holder) {
            this.mPreLayoutHolderMap.remove(holder);
            this.mPostLayoutHolderMap.remove(holder);
            if (this.mOldChangedHolders != null) {
                removeFrom(this.mOldChangedHolders, holder);
            }
        }

        public void onViewIgnored(ViewHolder holder) {
            onViewRecycled(holder);
        }

        private void removeFrom(ArrayMap<Long, ViewHolder> holderMap, ViewHolder holder) {
            for (int i = holderMap.size() - 1; i >= 0; i--) {
                if (holder == holderMap.valueAt(i)) {
                    holderMap.removeAt(i);
                    return;
                }
            }
        }

        public String toString() {
            return "State{mTargetPosition=" + this.mTargetPosition + ", mPreLayoutHolderMap=" + this.mPreLayoutHolderMap + ", mPostLayoutHolderMap=" + this.mPostLayoutHolderMap + ", mData=" + this.mData + ", mItemCount=" + this.mItemCount + ", mPreviousLayoutItemCount=" + this.mPreviousLayoutItemCount + ", mDeletedInvisibleItemCountSincePreviousLayout=" + this.mDeletedInvisibleItemCountSincePreviousLayout + ", mStructureChanged=" + this.mStructureChanged + ", mInPreLayout=" + this.mInPreLayout + ", mRunSimpleAnimations=" + this.mRunSimpleAnimations + ", mRunPredictiveAnimations=" + this.mRunPredictiveAnimations + '}';
        }
    }

    /* loaded from: classes.dex */
    private class ItemAnimatorRestoreListener implements ItemAnimator.ItemAnimatorListener {
        private ItemAnimatorRestoreListener() {
        }

        @Override // android.support.v7.widget.RecyclerView.ItemAnimator.ItemAnimatorListener
        public void onRemoveFinished(ViewHolder item) {
            item.setIsRecyclable(true);
            if (!RecyclerView.this.removeAnimatingView(item.itemView) && item.isTmpDetached()) {
                RecyclerView.this.removeDetachedView(item.itemView, false);
            }
        }

        @Override // android.support.v7.widget.RecyclerView.ItemAnimator.ItemAnimatorListener
        public void onAddFinished(ViewHolder item) {
            item.setIsRecyclable(true);
            if (item.isRecyclable()) {
                RecyclerView.this.removeAnimatingView(item.itemView);
            }
        }

        @Override // android.support.v7.widget.RecyclerView.ItemAnimator.ItemAnimatorListener
        public void onMoveFinished(ViewHolder item) {
            item.setIsRecyclable(true);
            if (item.isRecyclable()) {
                RecyclerView.this.removeAnimatingView(item.itemView);
            }
        }

        @Override // android.support.v7.widget.RecyclerView.ItemAnimator.ItemAnimatorListener
        public void onChangeFinished(ViewHolder item) {
            item.setIsRecyclable(true);
            if (item.mShadowedHolder != null && item.mShadowingHolder == null) {
                item.mShadowedHolder = null;
                item.setFlags(-65, item.mFlags);
            }
            item.mShadowingHolder = null;
            if (item.isRecyclable()) {
                RecyclerView.this.removeAnimatingView(item.itemView);
            }
        }
    }

    /* loaded from: classes.dex */
    public static abstract class ItemAnimator {
        private ItemAnimatorListener mListener = null;
        private ArrayList<ItemAnimatorFinishedListener> mFinishedListeners = new ArrayList<>();
        private long mAddDuration = 120;
        private long mRemoveDuration = 120;
        private long mMoveDuration = 250;
        private long mChangeDuration = 250;
        private boolean mSupportsChangeAnimations = false;

        /* loaded from: classes.dex */
        public interface ItemAnimatorFinishedListener {
            void onAnimationsFinished();
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* loaded from: classes.dex */
        public interface ItemAnimatorListener {
            void onAddFinished(ViewHolder viewHolder);

            void onChangeFinished(ViewHolder viewHolder);

            void onMoveFinished(ViewHolder viewHolder);

            void onRemoveFinished(ViewHolder viewHolder);
        }

        public abstract boolean animateAdd(ViewHolder viewHolder);

        public abstract boolean animateChange(ViewHolder viewHolder, ViewHolder viewHolder2, int i, int i2, int i3, int i4);

        public abstract boolean animateMove(ViewHolder viewHolder, int i, int i2, int i3, int i4);

        public abstract boolean animateRemove(ViewHolder viewHolder);

        public abstract void endAnimation(ViewHolder viewHolder);

        public abstract void endAnimations();

        public abstract boolean isRunning();

        public abstract void runPendingAnimations();

        public long getMoveDuration() {
            return this.mMoveDuration;
        }

        public void setMoveDuration(long moveDuration) {
            this.mMoveDuration = moveDuration;
        }

        public long getAddDuration() {
            return this.mAddDuration;
        }

        public void setAddDuration(long addDuration) {
            this.mAddDuration = addDuration;
        }

        public long getRemoveDuration() {
            return this.mRemoveDuration;
        }

        public void setRemoveDuration(long removeDuration) {
            this.mRemoveDuration = removeDuration;
        }

        public long getChangeDuration() {
            return this.mChangeDuration;
        }

        public void setChangeDuration(long changeDuration) {
            this.mChangeDuration = changeDuration;
        }

        public boolean getSupportsChangeAnimations() {
            return this.mSupportsChangeAnimations;
        }

        public void setSupportsChangeAnimations(boolean supportsChangeAnimations) {
            this.mSupportsChangeAnimations = supportsChangeAnimations;
        }

        void setListener(ItemAnimatorListener listener) {
            this.mListener = listener;
        }

        public final void dispatchRemoveFinished(ViewHolder item) {
            onRemoveFinished(item);
            if (this.mListener != null) {
                this.mListener.onRemoveFinished(item);
            }
        }

        public final void dispatchMoveFinished(ViewHolder item) {
            onMoveFinished(item);
            if (this.mListener != null) {
                this.mListener.onMoveFinished(item);
            }
        }

        public final void dispatchAddFinished(ViewHolder item) {
            onAddFinished(item);
            if (this.mListener != null) {
                this.mListener.onAddFinished(item);
            }
        }

        public final void dispatchChangeFinished(ViewHolder item, boolean oldItem) {
            onChangeFinished(item, oldItem);
            if (this.mListener != null) {
                this.mListener.onChangeFinished(item);
            }
        }

        public final void dispatchRemoveStarting(ViewHolder item) {
            onRemoveStarting(item);
        }

        public final void dispatchMoveStarting(ViewHolder item) {
            onMoveStarting(item);
        }

        public final void dispatchAddStarting(ViewHolder item) {
            onAddStarting(item);
        }

        public final void dispatchChangeStarting(ViewHolder item, boolean oldItem) {
            onChangeStarting(item, oldItem);
        }

        public final boolean isRunning(ItemAnimatorFinishedListener listener) {
            boolean running = isRunning();
            if (listener != null) {
                if (!running) {
                    listener.onAnimationsFinished();
                } else {
                    this.mFinishedListeners.add(listener);
                }
            }
            return running;
        }

        public final void dispatchAnimationsFinished() {
            int count = this.mFinishedListeners.size();
            for (int i = 0; i < count; i++) {
                this.mFinishedListeners.get(i).onAnimationsFinished();
            }
            this.mFinishedListeners.clear();
        }

        public void onRemoveStarting(ViewHolder item) {
        }

        public void onRemoveFinished(ViewHolder item) {
        }

        public void onAddStarting(ViewHolder item) {
        }

        public void onAddFinished(ViewHolder item) {
        }

        public void onMoveStarting(ViewHolder item) {
        }

        public void onMoveFinished(ViewHolder item) {
        }

        public void onChangeStarting(ViewHolder item, boolean oldItem) {
        }

        public void onChangeFinished(ViewHolder item, boolean oldItem) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class ItemHolderInfo {
        int bottom;
        ViewHolder holder;
        int left;
        int right;
        int top;

        ItemHolderInfo(ViewHolder holder, int left, int top, int right, int bottom) {
            this.holder = holder;
            this.left = left;
            this.top = top;
            this.right = right;
            this.bottom = bottom;
        }
    }
}

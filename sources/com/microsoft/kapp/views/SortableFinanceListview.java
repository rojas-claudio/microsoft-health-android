package com.microsoft.kapp.views;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.WrapperListAdapter;
import com.microsoft.kapp.adapters.FinanceCompanyReorderAdapter;
import com.microsoft.kapp.services.finance.StockCompanyInformation;
import java.util.List;
/* loaded from: classes.dex */
public class SortableFinanceListview extends ListView {
    private static final TypeEvaluator<Rect> sBoundEvaluator = new TypeEvaluator<Rect>() { // from class: com.microsoft.kapp.views.SortableFinanceListview.5
        @Override // android.animation.TypeEvaluator
        public Rect evaluate(float fraction, Rect startValue, Rect endValue) {
            return new Rect(interpolate(startValue.left, endValue.left, fraction), interpolate(startValue.top, endValue.top, fraction), interpolate(startValue.right, endValue.right, fraction), interpolate(startValue.bottom, endValue.bottom, fraction));
        }

        public int interpolate(int start, int end, float fraction) {
            return (int) (start + ((end - start) * fraction));
        }
    };
    private final int INVALID_ID;
    private final int INVALID_POINTER_ID;
    private final int LINE_THICKNESS;
    private final int MOVE_DURATION;
    private long mAboveItemId;
    private int mActivePointerId;
    private long mBelowItemId;
    private boolean mCellIsMoving;
    private int mDownX;
    private int mDownY;
    private BitmapDrawable mHoverCell;
    private Rect mHoverCellCurrentBounds;
    private Rect mHoverCellOriginalBounds;
    private int mLastEventY;
    private long mMobileItemId;
    private AdapterView.OnItemLongClickListener mOnItemLongClickListener;
    private OnReorderListener mOnReorderListener;
    public List<StockCompanyInformation> mStockCompanyList;
    private int mTotalOffset;

    /* loaded from: classes.dex */
    public interface OnReorderListener {
        void onReorder(List<StockCompanyInformation> list);
    }

    static /* synthetic */ int access$012(SortableFinanceListview x0, int x1) {
        int i = x0.mTotalOffset + x1;
        x0.mTotalOffset = i;
        return i;
    }

    public SortableFinanceListview(Context context) {
        super(context);
        this.MOVE_DURATION = 150;
        this.LINE_THICKNESS = 3;
        this.mLastEventY = -1;
        this.mDownY = -1;
        this.mDownX = -1;
        this.mTotalOffset = 0;
        this.INVALID_ID = -1;
        this.mAboveItemId = -1L;
        this.mMobileItemId = -1L;
        this.mBelowItemId = -1L;
        this.INVALID_POINTER_ID = -1;
        this.mActivePointerId = -1;
        this.mOnItemLongClickListener = new AdapterView.OnItemLongClickListener() { // from class: com.microsoft.kapp.views.SortableFinanceListview.1
            @Override // android.widget.AdapterView.OnItemLongClickListener
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int pos, long id) {
                SortableFinanceListview.this.mTotalOffset = 0;
                int position = SortableFinanceListview.this.pointToPosition(SortableFinanceListview.this.mDownX, SortableFinanceListview.this.mDownY);
                int itemNum = position - SortableFinanceListview.this.getFirstVisiblePosition();
                View selectedView = SortableFinanceListview.this.getChildAt(itemNum);
                SortableFinanceListview.this.mMobileItemId = SortableFinanceListview.this.getAdapter().getItemId(position);
                SortableFinanceListview.this.mHoverCell = SortableFinanceListview.this.getAndAddHoverView(selectedView);
                selectedView.setVisibility(4);
                SortableFinanceListview.this.mCellIsMoving = true;
                SortableFinanceListview.this.updateNeighborViewsForID(SortableFinanceListview.this.mMobileItemId);
                return true;
            }
        };
        setOnItemLongClickListener(this.mOnItemLongClickListener);
    }

    public SortableFinanceListview(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.MOVE_DURATION = 150;
        this.LINE_THICKNESS = 3;
        this.mLastEventY = -1;
        this.mDownY = -1;
        this.mDownX = -1;
        this.mTotalOffset = 0;
        this.INVALID_ID = -1;
        this.mAboveItemId = -1L;
        this.mMobileItemId = -1L;
        this.mBelowItemId = -1L;
        this.INVALID_POINTER_ID = -1;
        this.mActivePointerId = -1;
        this.mOnItemLongClickListener = new AdapterView.OnItemLongClickListener() { // from class: com.microsoft.kapp.views.SortableFinanceListview.1
            @Override // android.widget.AdapterView.OnItemLongClickListener
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int pos, long id) {
                SortableFinanceListview.this.mTotalOffset = 0;
                int position = SortableFinanceListview.this.pointToPosition(SortableFinanceListview.this.mDownX, SortableFinanceListview.this.mDownY);
                int itemNum = position - SortableFinanceListview.this.getFirstVisiblePosition();
                View selectedView = SortableFinanceListview.this.getChildAt(itemNum);
                SortableFinanceListview.this.mMobileItemId = SortableFinanceListview.this.getAdapter().getItemId(position);
                SortableFinanceListview.this.mHoverCell = SortableFinanceListview.this.getAndAddHoverView(selectedView);
                selectedView.setVisibility(4);
                SortableFinanceListview.this.mCellIsMoving = true;
                SortableFinanceListview.this.updateNeighborViewsForID(SortableFinanceListview.this.mMobileItemId);
                return true;
            }
        };
        setOnItemLongClickListener(this.mOnItemLongClickListener);
    }

    public SortableFinanceListview(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.MOVE_DURATION = 150;
        this.LINE_THICKNESS = 3;
        this.mLastEventY = -1;
        this.mDownY = -1;
        this.mDownX = -1;
        this.mTotalOffset = 0;
        this.INVALID_ID = -1;
        this.mAboveItemId = -1L;
        this.mMobileItemId = -1L;
        this.mBelowItemId = -1L;
        this.INVALID_POINTER_ID = -1;
        this.mActivePointerId = -1;
        this.mOnItemLongClickListener = new AdapterView.OnItemLongClickListener() { // from class: com.microsoft.kapp.views.SortableFinanceListview.1
            @Override // android.widget.AdapterView.OnItemLongClickListener
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int pos, long id) {
                SortableFinanceListview.this.mTotalOffset = 0;
                int position = SortableFinanceListview.this.pointToPosition(SortableFinanceListview.this.mDownX, SortableFinanceListview.this.mDownY);
                int itemNum = position - SortableFinanceListview.this.getFirstVisiblePosition();
                View selectedView = SortableFinanceListview.this.getChildAt(itemNum);
                SortableFinanceListview.this.mMobileItemId = SortableFinanceListview.this.getAdapter().getItemId(position);
                SortableFinanceListview.this.mHoverCell = SortableFinanceListview.this.getAndAddHoverView(selectedView);
                selectedView.setVisibility(4);
                SortableFinanceListview.this.mCellIsMoving = true;
                SortableFinanceListview.this.updateNeighborViewsForID(SortableFinanceListview.this.mMobileItemId);
                return true;
            }
        };
        setOnItemLongClickListener(this.mOnItemLongClickListener);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public BitmapDrawable getAndAddHoverView(View view) {
        int w = view.getWidth();
        int h = view.getHeight();
        int top = view.getTop();
        int left = view.getLeft();
        Bitmap b = getBitmapWithBorder(view);
        BitmapDrawable drawable = new BitmapDrawable(getResources(), b);
        this.mHoverCellOriginalBounds = new Rect(left, top, left + w, top + h);
        this.mHoverCellCurrentBounds = new Rect(this.mHoverCellOriginalBounds);
        drawable.setBounds(this.mHoverCellCurrentBounds);
        return drawable;
    }

    private Bitmap getBitmapWithBorder(View v) {
        Bitmap bitmap = getBitmapFromView(v);
        Canvas can = new Canvas(bitmap);
        Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(3.0f);
        paint.setColor(-16777216);
        can.drawBitmap(bitmap, 0.0f, 0.0f, (Paint) null);
        can.drawRect(rect, paint);
        return bitmap;
    }

    private Bitmap getBitmapFromView(View v) {
        Bitmap bitmap = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        v.draw(canvas);
        return bitmap;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateNeighborViewsForID(long itemID) {
        int position = getPositionForID(itemID);
        FinanceCompanyReorderAdapter adapter = (FinanceCompanyReorderAdapter) getAdapter();
        this.mAboveItemId = adapter.getItemId(position - 1);
        this.mBelowItemId = adapter.getItemId(position + 1);
    }

    public View getViewForID(long itemID) {
        int firstVisiblePosition = getFirstVisiblePosition();
        FinanceCompanyReorderAdapter adapter = (FinanceCompanyReorderAdapter) getAdapter();
        for (int i = 0; i < getChildCount(); i++) {
            View v = getChildAt(i);
            int position = firstVisiblePosition + i;
            long id = adapter.getItemId(position);
            if (id == itemID) {
                return v;
            }
        }
        return null;
    }

    public int getPositionForID(long itemID) {
        View v = getViewForID(itemID);
        if (v == null) {
            return -1;
        }
        return getPositionForView(v);
    }

    @Override // android.widget.ListView, android.widget.AbsListView, android.view.ViewGroup, android.view.View
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (this.mHoverCell != null) {
            this.mHoverCell.draw(canvas);
        }
    }

    @Override // android.widget.AbsListView, android.view.View
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction() & 255) {
            case 0:
                this.mDownX = (int) event.getX();
                this.mDownY = (int) event.getY();
                this.mActivePointerId = event.getPointerId(0);
                break;
            case 1:
                touchEventsEnded();
                if (this.mOnReorderListener != null) {
                    this.mOnReorderListener.onReorder(this.mStockCompanyList);
                    break;
                }
                break;
            case 2:
                if (this.mActivePointerId != -1) {
                    int pointerIndex = event.findPointerIndex(this.mActivePointerId);
                    this.mLastEventY = (int) event.getY(pointerIndex);
                    int deltaY = this.mLastEventY - this.mDownY;
                    if (this.mCellIsMoving) {
                        this.mHoverCellCurrentBounds.offsetTo(this.mHoverCellOriginalBounds.left, this.mHoverCellOriginalBounds.top + deltaY + this.mTotalOffset);
                        this.mHoverCell.setBounds(this.mHoverCellCurrentBounds);
                        invalidate();
                        handleCellSwitch();
                        return false;
                    }
                }
                break;
            case 3:
                touchEventsCancelled();
                break;
            case 6:
                int pointerIndex2 = (event.getAction() & MotionEventCompat.ACTION_POINTER_INDEX_MASK) >> 8;
                int pointerId = event.getPointerId(pointerIndex2);
                if (pointerId == this.mActivePointerId) {
                    touchEventsEnded();
                    break;
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    private void handleCellSwitch() {
        final int deltaY = this.mLastEventY - this.mDownY;
        int deltaYTotal = this.mHoverCellOriginalBounds.top + this.mTotalOffset + deltaY;
        View belowView = getViewForID(this.mBelowItemId);
        View mobileView = getViewForID(this.mMobileItemId);
        View aboveView = getViewForID(this.mAboveItemId);
        boolean isBelow = (belowView == null || this.mBelowItemId == -1 || deltaYTotal <= belowView.getTop()) ? false : true;
        boolean isAbove = (aboveView == null || this.mAboveItemId == -1 || deltaYTotal >= aboveView.getTop()) ? false : true;
        if (isBelow || isAbove) {
            final long switchItemID = isBelow ? this.mBelowItemId : this.mAboveItemId;
            View switchView = isBelow ? belowView : aboveView;
            int originalItem = getPositionForView(mobileView);
            if (switchView == null) {
                updateNeighborViewsForID(this.mMobileItemId);
                return;
            }
            swapElements(this.mStockCompanyList, originalItem, getPositionForView(switchView));
            ((BaseAdapter) getAdapter()).notifyDataSetChanged();
            this.mDownY = this.mLastEventY;
            final int switchViewStartTop = switchView.getTop();
            mobileView.setVisibility(0);
            if (Build.VERSION.SDK_INT <= 19) {
                switchView.setVisibility(4);
            }
            updateNeighborViewsForID(this.mMobileItemId);
            final ViewTreeObserver observer = getViewTreeObserver();
            observer.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() { // from class: com.microsoft.kapp.views.SortableFinanceListview.2
                @Override // android.view.ViewTreeObserver.OnPreDrawListener
                public boolean onPreDraw() {
                    observer.removeOnPreDrawListener(this);
                    View switchView1 = SortableFinanceListview.this.getViewForID(switchItemID);
                    SortableFinanceListview.access$012(SortableFinanceListview.this, deltaY);
                    int switchViewNewTop = switchView1.getTop();
                    int delta = switchViewStartTop - switchViewNewTop;
                    switchView1.setTranslationY(delta);
                    ObjectAnimator animator = ObjectAnimator.ofFloat(switchView1, View.TRANSLATION_Y, 0.0f);
                    animator.setDuration(150L);
                    animator.start();
                    return true;
                }
            });
        }
    }

    private void swapElements(List<StockCompanyInformation> arrayList, int indexOne, int indexTwo) {
        if (indexTwo < arrayList.size() && indexOne != -1 && indexTwo != -1) {
            StockCompanyInformation temp = arrayList.get(indexOne);
            arrayList.set(indexOne, arrayList.get(indexTwo));
            arrayList.set(indexTwo, temp);
        }
    }

    private void touchEventsEnded() {
        final View mobileView = getViewForID(this.mMobileItemId);
        if (this.mCellIsMoving) {
            this.mCellIsMoving = false;
            this.mActivePointerId = -1;
            this.mHoverCellCurrentBounds.offsetTo(this.mHoverCellOriginalBounds.left, mobileView.getTop());
            ObjectAnimator hoverViewAnimator = ObjectAnimator.ofObject(this.mHoverCell, "bounds", sBoundEvaluator, this.mHoverCellCurrentBounds);
            hoverViewAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.microsoft.kapp.views.SortableFinanceListview.3
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    SortableFinanceListview.this.invalidate();
                }
            });
            hoverViewAnimator.addListener(new AnimatorListenerAdapter() { // from class: com.microsoft.kapp.views.SortableFinanceListview.4
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationStart(Animator animation) {
                    SortableFinanceListview.this.setEnabled(false);
                }

                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animation) {
                    SortableFinanceListview.this.mAboveItemId = -1L;
                    SortableFinanceListview.this.mMobileItemId = -1L;
                    SortableFinanceListview.this.mBelowItemId = -1L;
                    mobileView.setVisibility(0);
                    SortableFinanceListview.this.mHoverCell = null;
                    SortableFinanceListview.this.setEnabled(true);
                    SortableFinanceListview.this.invalidate();
                }
            });
            hoverViewAnimator.start();
            return;
        }
        touchEventsCancelled();
    }

    private void touchEventsCancelled() {
        View mobileView = getViewForID(this.mMobileItemId);
        if (this.mCellIsMoving) {
            this.mAboveItemId = -1L;
            this.mMobileItemId = -1L;
            this.mBelowItemId = -1L;
            mobileView.setVisibility(0);
            this.mHoverCell = null;
            invalidate();
        }
        this.mCellIsMoving = false;
        this.mActivePointerId = -1;
    }

    public void setStockCompaniesDisplayed(List<StockCompanyInformation> stockCompanyList) {
        this.mStockCompanyList = stockCompanyList;
    }

    public void setOnReorderListener(OnReorderListener onReorderListener) {
        this.mOnReorderListener = onReorderListener;
    }

    @Override // android.widget.ListView, android.widget.AdapterView
    public ListAdapter getAdapter() {
        ListAdapter adapter = super.getAdapter();
        if (adapter instanceof WrapperListAdapter) {
            return ((WrapperListAdapter) adapter).getWrappedAdapter();
        }
        return adapter;
    }
}

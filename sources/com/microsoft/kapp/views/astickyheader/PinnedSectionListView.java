package com.microsoft.kapp.views.astickyheader;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.HeaderViewListAdapter;
import android.widget.ListAdapter;
import android.widget.SectionIndexer;
import android.widget.WrapperListAdapter;
/* loaded from: classes.dex */
public class PinnedSectionListView extends AutoScrollListView {
    private final DataSetObserver mDataSetObserver;
    AbsListView.OnScrollListener mDelegateOnScrollListener;
    private MotionEvent mDownEvent;
    private final AbsListView.OnScrollListener mOnScrollListener;
    PinnedSection mPinnedSection;
    PinnedSection mRecycleSection;
    private int mSectionsDistanceY;
    private GradientDrawable mShadowDrawable;
    private int mShadowHeight;
    private final PointF mTouchPoint;
    private final Rect mTouchRect;
    private int mTouchSlop;
    private View mTouchTarget;
    int mTranslateY;

    /* loaded from: classes.dex */
    public interface PinnedSectionListAdapter extends ListAdapter {
        int getPositionForSection(int i);

        String[] getSections();

        boolean isItemViewTypePinned(int i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class PinnedSection {
        public long id;
        public int position;
        public View view;

        PinnedSection() {
        }
    }

    public PinnedSectionListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mTouchRect = new Rect();
        this.mTouchPoint = new PointF();
        this.mOnScrollListener = new AbsListView.OnScrollListener() { // from class: com.microsoft.kapp.views.astickyheader.PinnedSectionListView.1
            @Override // android.widget.AbsListView.OnScrollListener
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (PinnedSectionListView.this.mDelegateOnScrollListener != null) {
                    PinnedSectionListView.this.mDelegateOnScrollListener.onScrollStateChanged(view, scrollState);
                }
            }

            @Override // android.widget.AbsListView.OnScrollListener
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (PinnedSectionListView.this.mDelegateOnScrollListener != null) {
                    PinnedSectionListView.this.mDelegateOnScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
                }
                PinnedSectionListAdapter adapter = PinnedSectionListView.this.getPinnedAdapter();
                if (adapter != null && visibleItemCount != 0) {
                    boolean isFirstVisibleItemSection = PinnedSectionListView.isItemViewTypePinned(adapter, firstVisibleItem);
                    if (isFirstVisibleItemSection) {
                        View sectionView = PinnedSectionListView.this.getChildAt(0);
                        if (sectionView.getTop() == PinnedSectionListView.this.getPaddingTop()) {
                            PinnedSectionListView.this.destroyPinnedShadow();
                            return;
                        } else {
                            PinnedSectionListView.this.ensureShadowForPosition(firstVisibleItem, firstVisibleItem, visibleItemCount);
                            return;
                        }
                    }
                    int sectionPosition = PinnedSectionListView.this.findCurrentSectionPosition(firstVisibleItem);
                    if (sectionPosition > -1) {
                        PinnedSectionListView.this.ensureShadowForPosition(sectionPosition, firstVisibleItem, visibleItemCount);
                    } else {
                        PinnedSectionListView.this.destroyPinnedShadow();
                    }
                }
            }
        };
        this.mDataSetObserver = new DataSetObserver() { // from class: com.microsoft.kapp.views.astickyheader.PinnedSectionListView.2
            @Override // android.database.DataSetObserver
            public void onChanged() {
                PinnedSectionListView.this.recreatePinnedShadow();
            }

            @Override // android.database.DataSetObserver
            public void onInvalidated() {
                PinnedSectionListView.this.recreatePinnedShadow();
            }
        };
        initView();
    }

    public PinnedSectionListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mTouchRect = new Rect();
        this.mTouchPoint = new PointF();
        this.mOnScrollListener = new AbsListView.OnScrollListener() { // from class: com.microsoft.kapp.views.astickyheader.PinnedSectionListView.1
            @Override // android.widget.AbsListView.OnScrollListener
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (PinnedSectionListView.this.mDelegateOnScrollListener != null) {
                    PinnedSectionListView.this.mDelegateOnScrollListener.onScrollStateChanged(view, scrollState);
                }
            }

            @Override // android.widget.AbsListView.OnScrollListener
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (PinnedSectionListView.this.mDelegateOnScrollListener != null) {
                    PinnedSectionListView.this.mDelegateOnScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
                }
                PinnedSectionListAdapter adapter = PinnedSectionListView.this.getPinnedAdapter();
                if (adapter != null && visibleItemCount != 0) {
                    boolean isFirstVisibleItemSection = PinnedSectionListView.isItemViewTypePinned(adapter, firstVisibleItem);
                    if (isFirstVisibleItemSection) {
                        View sectionView = PinnedSectionListView.this.getChildAt(0);
                        if (sectionView.getTop() == PinnedSectionListView.this.getPaddingTop()) {
                            PinnedSectionListView.this.destroyPinnedShadow();
                            return;
                        } else {
                            PinnedSectionListView.this.ensureShadowForPosition(firstVisibleItem, firstVisibleItem, visibleItemCount);
                            return;
                        }
                    }
                    int sectionPosition = PinnedSectionListView.this.findCurrentSectionPosition(firstVisibleItem);
                    if (sectionPosition > -1) {
                        PinnedSectionListView.this.ensureShadowForPosition(sectionPosition, firstVisibleItem, visibleItemCount);
                    } else {
                        PinnedSectionListView.this.destroyPinnedShadow();
                    }
                }
            }
        };
        this.mDataSetObserver = new DataSetObserver() { // from class: com.microsoft.kapp.views.astickyheader.PinnedSectionListView.2
            @Override // android.database.DataSetObserver
            public void onChanged() {
                PinnedSectionListView.this.recreatePinnedShadow();
            }

            @Override // android.database.DataSetObserver
            public void onInvalidated() {
                PinnedSectionListView.this.recreatePinnedShadow();
            }
        };
        initView();
    }

    private void initView() {
        setOnScrollListener(this.mOnScrollListener);
        this.mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
        initShadow(false);
    }

    public void setShadowVisible(boolean visible) {
        initShadow(visible);
        if (this.mPinnedSection != null) {
            View v = this.mPinnedSection.view;
            invalidate(v.getLeft(), v.getTop(), v.getRight(), v.getBottom() + this.mShadowHeight);
        }
    }

    public void initShadow(boolean visible) {
        if (visible) {
            if (this.mShadowDrawable == null) {
                this.mShadowDrawable = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, new int[]{Color.parseColor("#ffa0a0a0"), Color.parseColor("#50a0a0a0"), Color.parseColor("#00a0a0a0")});
                this.mShadowHeight = (int) (12.0f * getResources().getDisplayMetrics().density);
            }
        } else if (this.mShadowDrawable != null) {
            this.mShadowDrawable = null;
            this.mShadowHeight = 0;
        }
    }

    void createPinnedShadow(int position) {
        PinnedSection pinnedShadow = this.mRecycleSection;
        this.mRecycleSection = null;
        if (pinnedShadow == null) {
            pinnedShadow = new PinnedSection();
        }
        View pinnedView = getAdapter().getView(position, pinnedShadow.view, this);
        AbsListView.LayoutParams layoutParams = (AbsListView.LayoutParams) pinnedView.getLayoutParams();
        if (layoutParams == null) {
            layoutParams = new AbsListView.LayoutParams(-1, -2);
        }
        int heightMode = View.MeasureSpec.getMode(layoutParams.height);
        int heightSize = View.MeasureSpec.getSize(layoutParams.height);
        if (heightMode == 0) {
            heightMode = 1073741824;
        }
        int maxHeight = (getHeight() - getListPaddingTop()) - getListPaddingBottom();
        if (heightSize > maxHeight) {
            heightSize = maxHeight;
        }
        int ws = View.MeasureSpec.makeMeasureSpec((getWidth() - getListPaddingLeft()) - getListPaddingRight(), 1073741824);
        int hs = View.MeasureSpec.makeMeasureSpec(heightSize, heightMode);
        pinnedView.measure(ws, hs);
        pinnedView.layout(0, 0, pinnedView.getMeasuredWidth(), pinnedView.getMeasuredHeight());
        this.mTranslateY = 0;
        pinnedShadow.view = pinnedView;
        pinnedShadow.position = position;
        pinnedShadow.id = getAdapter().getItemId(position);
        this.mPinnedSection = pinnedShadow;
    }

    void destroyPinnedShadow() {
        if (this.mPinnedSection != null) {
            this.mRecycleSection = this.mPinnedSection;
            this.mPinnedSection = null;
        }
    }

    void ensureShadowForPosition(int sectionPosition, int firstVisibleItem, int visibleItemCount) {
        if (visibleItemCount < 2) {
            destroyPinnedShadow();
            return;
        }
        if (this.mPinnedSection != null && this.mPinnedSection.position != sectionPosition) {
            destroyPinnedShadow();
        }
        if (this.mPinnedSection == null) {
            createPinnedShadow(sectionPosition);
        }
        int nextPosition = sectionPosition + 1;
        if (nextPosition < getCount()) {
            int nextSectionPosition = findFirstVisibleSectionPosition(nextPosition, visibleItemCount - (nextPosition - firstVisibleItem));
            if (nextSectionPosition > -1) {
                View nextSectionView = getChildAt(nextSectionPosition - firstVisibleItem);
                int bottom = this.mPinnedSection.view.getBottom() + getPaddingTop();
                this.mSectionsDistanceY = nextSectionView.getTop() - bottom;
                if (this.mSectionsDistanceY < 0) {
                    this.mTranslateY = this.mSectionsDistanceY;
                    return;
                } else {
                    this.mTranslateY = 0;
                    return;
                }
            }
            this.mTranslateY = 0;
            this.mSectionsDistanceY = Integer.MAX_VALUE;
        }
    }

    int findFirstVisibleSectionPosition(int firstVisibleItem, int visibleItemCount) {
        PinnedSectionListAdapter adapter = getPinnedAdapter();
        for (int childIndex = 0; childIndex < visibleItemCount; childIndex++) {
            int position = firstVisibleItem + childIndex;
            if (isItemViewTypePinned(adapter, position)) {
                return position;
            }
        }
        return -1;
    }

    int findCurrentSectionPosition(int fromPosition) {
        PinnedSectionListAdapter adapter = getPinnedAdapter();
        if (adapter instanceof SectionIndexer) {
            SectionIndexer indexer = (SectionIndexer) adapter;
            int sectionPosition = indexer.getSectionForPosition(fromPosition);
            int itemPosition = indexer.getPositionForSection(sectionPosition);
            if (isItemViewTypePinned(adapter, itemPosition)) {
                return itemPosition;
            }
        }
        for (int position = fromPosition; position >= 0; position--) {
            if (isItemViewTypePinned(adapter, position)) {
                return position;
            }
        }
        return -1;
    }

    void recreatePinnedShadow() {
        int firstVisiblePosition;
        int sectionPosition;
        destroyPinnedShadow();
        ListAdapter adapter = getAdapter();
        if (adapter != null && adapter.getCount() > 0 && (sectionPosition = findCurrentSectionPosition((firstVisiblePosition = getFirstVisiblePosition()))) != -1) {
            ensureShadowForPosition(sectionPosition, firstVisiblePosition, getLastVisiblePosition() - firstVisiblePosition);
        }
    }

    @Override // android.widget.AbsListView
    public void setOnScrollListener(AbsListView.OnScrollListener listener) {
        if (listener == this.mOnScrollListener) {
            super.setOnScrollListener(listener);
        } else {
            this.mDelegateOnScrollListener = listener;
        }
    }

    @Override // android.widget.AbsListView, android.view.View
    public void onRestoreInstanceState(Parcelable state) {
        super.onRestoreInstanceState(state);
        post(new Runnable() { // from class: com.microsoft.kapp.views.astickyheader.PinnedSectionListView.3
            @Override // java.lang.Runnable
            public void run() {
                PinnedSectionListView.this.recreatePinnedShadow();
            }
        });
    }

    @Override // com.microsoft.kapp.views.astickyheader.AutoScrollListView, android.widget.AdapterView
    public void setAdapter(ListAdapter adapter) {
        if (adapter != null) {
            if (!(adapter instanceof PinnedSectionListAdapter)) {
                throw new IllegalArgumentException("Does your adapter implement PinnedSectionListAdapter?");
            }
            if (adapter.getViewTypeCount() < 2) {
                throw new IllegalArgumentException("Does your adapter handle at least two types of views in getViewTypeCount() method: items and sections?");
            }
        }
        ListAdapter oldAdapter = getAdapter();
        if (oldAdapter != null) {
            oldAdapter.unregisterDataSetObserver(this.mDataSetObserver);
        }
        if (adapter != null) {
            adapter.registerDataSetObserver(this.mDataSetObserver);
        }
        if (oldAdapter != adapter) {
            destroyPinnedShadow();
        }
        super.setAdapter(adapter);
    }

    @Override // android.widget.AbsListView, android.widget.AdapterView, android.view.ViewGroup, android.view.View
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (this.mPinnedSection != null) {
            int parentWidth = ((r - l) - getPaddingLeft()) - getPaddingRight();
            int shadowWidth = this.mPinnedSection.view.getWidth();
            if (parentWidth != shadowWidth) {
                recreatePinnedShadow();
            }
        }
    }

    @Override // android.widget.ListView, android.widget.AbsListView, android.view.ViewGroup, android.view.View
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (this.mPinnedSection != null) {
            int pLeft = getListPaddingLeft();
            int pTop = getListPaddingTop();
            View view = this.mPinnedSection.view;
            canvas.save();
            int clipHeight = view.getHeight() + (this.mShadowDrawable == null ? 0 : Math.min(this.mShadowHeight, this.mSectionsDistanceY));
            canvas.clipRect(pLeft, pTop, view.getWidth() + pLeft, pTop + clipHeight);
            canvas.translate(pLeft, this.mTranslateY + pTop);
            drawChild(canvas, this.mPinnedSection.view, getDrawingTime());
            if (this.mShadowDrawable != null && this.mSectionsDistanceY > 0) {
                this.mShadowDrawable.setBounds(this.mPinnedSection.view.getLeft(), this.mPinnedSection.view.getBottom(), this.mPinnedSection.view.getRight(), this.mPinnedSection.view.getBottom() + this.mShadowHeight);
                this.mShadowDrawable.draw(canvas);
            }
            canvas.restore();
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    public boolean dispatchTouchEvent(MotionEvent ev) {
        float x = ev.getX();
        float y = ev.getY();
        int action = ev.getAction();
        if (action == 0 && this.mTouchTarget == null && this.mPinnedSection != null && isPinnedViewTouched(this.mPinnedSection.view, x, y)) {
            this.mTouchTarget = this.mPinnedSection.view;
            this.mTouchPoint.x = x;
            this.mTouchPoint.y = y;
            this.mDownEvent = MotionEvent.obtain(ev);
        }
        if (this.mTouchTarget != null) {
            if (isPinnedViewTouched(this.mTouchTarget, x, y)) {
                this.mTouchTarget.dispatchTouchEvent(ev);
            }
            if (action == 1) {
                super.dispatchTouchEvent(ev);
                performPinnedItemClick();
                clearTouchTarget();
                return true;
            } else if (action == 3) {
                clearTouchTarget();
                return true;
            } else if (action != 2 || Math.abs(y - this.mTouchPoint.y) <= this.mTouchSlop) {
                return true;
            } else {
                MotionEvent event = MotionEvent.obtain(ev);
                event.setAction(3);
                this.mTouchTarget.dispatchTouchEvent(event);
                event.recycle();
                super.dispatchTouchEvent(this.mDownEvent);
                super.dispatchTouchEvent(ev);
                clearTouchTarget();
                return true;
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    private boolean isPinnedViewTouched(View view, float x, float y) {
        view.getHitRect(this.mTouchRect);
        this.mTouchRect.top += this.mTranslateY;
        this.mTouchRect.bottom += this.mTranslateY + getPaddingTop();
        this.mTouchRect.left += getPaddingLeft();
        this.mTouchRect.right -= getPaddingRight();
        return this.mTouchRect.contains((int) x, (int) y);
    }

    private void clearTouchTarget() {
        this.mTouchTarget = null;
        if (this.mDownEvent != null) {
            this.mDownEvent.recycle();
            this.mDownEvent = null;
        }
    }

    private boolean performPinnedItemClick() {
        AdapterView.OnItemClickListener listener;
        if (this.mPinnedSection == null || (listener = getOnItemClickListener()) == null) {
            return false;
        }
        View view = this.mPinnedSection.view;
        playSoundEffect(0);
        if (view != null) {
            view.sendAccessibilityEvent(1);
        }
        listener.onItemClick(this, view, this.mPinnedSection.position, this.mPinnedSection.id);
        return true;
    }

    public static boolean isItemViewTypePinned(ListAdapter adapter, int position) {
        if (adapter instanceof HeaderViewListAdapter) {
            adapter = ((HeaderViewListAdapter) adapter).getWrappedAdapter();
        }
        return ((PinnedSectionListAdapter) adapter).isItemViewTypePinned(position);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public PinnedSectionListAdapter getPinnedAdapter() {
        if (getAdapter() instanceof WrapperListAdapter) {
            PinnedSectionListAdapter adapter = (PinnedSectionListAdapter) ((WrapperListAdapter) getAdapter()).getWrappedAdapter();
            return adapter;
        }
        PinnedSectionListAdapter adapter2 = (PinnedSectionListAdapter) getAdapter();
        return adapter2;
    }
}

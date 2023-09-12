package com.microsoft.kapp.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.microsoft.kapp.R;
import com.microsoft.kapp.adapters.IconPagerAdapter;
import com.microsoft.kapp.diagnostics.Validate;
/* loaded from: classes.dex */
public class PagerIconStrip extends HorizontalScrollView implements ViewPager.OnPageChangeListener {
    private final float mIconHeight;
    private IconPagerAdapter mIconPagerAdapter;
    private final float mIconWidth;
    private final LinearLayout mIconsLayout;
    private ViewPager.OnPageChangeListener mOnPageChangeListener;
    private int mSpaceWidth;
    private final View.OnClickListener mTabClickListener;
    private ViewPager mViewPager;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class IconView extends ImageView {
        private final int mIndex;

        public IconView(Context context, int index) {
            super(context);
            Validate.isTrue(index >= 0, "index must be greater than or equal to zero.");
            this.mIndex = index;
        }

        public int getIndex() {
            return this.mIndex;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class SpaceView extends View {
        public SpaceView(Context context) {
            super(context);
        }

        @Override // android.view.View
        public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            super.onMeasure(View.MeasureSpec.makeMeasureSpec(PagerIconStrip.this.mSpaceWidth, 1073741824), heightMeasureSpec);
        }
    }

    public PagerIconStrip(Context context) {
        this(context, null);
    }

    public PagerIconStrip(Context context, AttributeSet attr) {
        super(context, attr);
        this.mTabClickListener = new View.OnClickListener() { // from class: com.microsoft.kapp.widgets.PagerIconStrip.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                IconView tabView = (IconView) view;
                int newSelected = tabView.getIndex();
                PagerIconStrip.this.mViewPager.setCurrentItem(newSelected);
            }
        };
        setHorizontalScrollBarEnabled(false);
        this.mIconsLayout = new LinearLayout(context, attr, R.attr.pagerIconStripStyle);
        ViewGroup.LayoutParams params = new FrameLayout.LayoutParams(-2, -1, 17);
        addView(this.mIconsLayout, params);
        TypedArray typedArray = getContext().obtainStyledAttributes(attr, R.styleable.PagerIconStrip);
        try {
            this.mIconWidth = typedArray.getDimension(0, 0.0f);
            this.mIconHeight = typedArray.getDimension(1, 0.0f);
        } finally {
            typedArray.recycle();
        }
    }

    @Override // android.widget.HorizontalScrollView, android.widget.FrameLayout, android.view.View
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = View.MeasureSpec.getMode(widthMeasureSpec);
        boolean lockedExpanded = widthMode == 1073741824;
        setFillViewport(lockedExpanded);
        int childCount = this.mIconPagerAdapter.getCount();
        if (childCount > 1) {
            int width = View.MeasureSpec.getSize(widthMeasureSpec);
            float totalIconWidth = childCount * this.mIconWidth;
            this.mSpaceWidth = ((int) (width - totalIconWidth)) / (childCount - 1);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override // android.support.v4.view.ViewPager.OnPageChangeListener
    public void onPageScrollStateChanged(int state) {
        if (this.mOnPageChangeListener != null) {
            this.mOnPageChangeListener.onPageScrollStateChanged(state);
        }
    }

    @Override // android.support.v4.view.ViewPager.OnPageChangeListener
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (this.mOnPageChangeListener != null) {
            this.mOnPageChangeListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
        }
    }

    @Override // android.support.v4.view.ViewPager.OnPageChangeListener
    public void onPageSelected(int position) {
        setCurrentItem(position);
        if (this.mOnPageChangeListener != null) {
            this.mOnPageChangeListener.onPageSelected(position);
        }
    }

    public void setViewPager(ViewPager viewPager) {
        Validate.notNull(viewPager, "viewPager");
        if (this.mViewPager != null) {
            this.mViewPager.setOnPageChangeListener(null);
        }
        PagerAdapter adapter = viewPager.getAdapter();
        if (adapter == null) {
            throw new IllegalStateException("The ViewPager does not have adapter assigned.");
        }
        if (!(adapter instanceof IconPagerAdapter)) {
            throw new IllegalStateException("The adapter assigned to the ViewPager is not an instance of IconPagerAdapter");
        }
        this.mViewPager = viewPager;
        this.mViewPager.setOnPageChangeListener(this);
        this.mIconPagerAdapter = (IconPagerAdapter) adapter;
        onViewPagerChanged();
    }

    public void setViewPager(ViewPager viewPager, int initialPosition) {
        setViewPager(viewPager);
        setCurrentItem(initialPosition);
    }

    public void setCurrentItem(int position) {
        if (this.mViewPager == null) {
            throw new IllegalStateException("The ViewPager has not been set.");
        }
        this.mViewPager.setCurrentItem(position);
        int selectedTabIndex = getSelectedTabIndex(position);
        int tabCount = this.mIconsLayout.getChildCount();
        int i = 0;
        while (i < tabCount) {
            View child = this.mIconsLayout.getChildAt(i);
            boolean isSelected = i == selectedTabIndex;
            child.setSelected(isSelected);
            i++;
        }
    }

    public void setOnPageChangeListener(ViewPager.OnPageChangeListener listener) {
        this.mOnPageChangeListener = listener;
    }

    private View[] createIconViews(int position) {
        Context context = getContext();
        IconView view = new IconView(context, position);
        view.setImageResource(this.mIconPagerAdapter.getIconResourceId(position));
        view.setFocusable(true);
        view.setOnClickListener(this.mTabClickListener);
        view.setContentDescription(this.mIconPagerAdapter.getContentDescription(position));
        if (this.mIconWidth != 0.0f && this.mIconHeight != 0.0f) {
            float convertedWidth = this.mIconWidth;
            float convertedHeight = this.mIconHeight;
            view.setLayoutParams(new LinearLayout.LayoutParams((int) convertedWidth, (int) convertedHeight));
        }
        SpaceView space = null;
        if (position != this.mViewPager.getAdapter().getCount() - 1) {
            space = new SpaceView(context);
        }
        return space == null ? new View[]{view} : new View[]{view, space};
    }

    private int getSelectedTabIndex(int position) {
        return position * 2;
    }

    private void onViewPagerChanged() {
        this.mIconsLayout.removeAllViews();
        int count = this.mIconPagerAdapter.getCount();
        for (int i = 0; i < count; i++) {
            View[] views = createIconViews(i);
            if (views != null) {
                for (View view : views) {
                    if (view != null) {
                        this.mIconsLayout.addView(view);
                    }
                }
            }
        }
        setCurrentItem(0);
        requestLayout();
    }
}

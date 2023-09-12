package com.microsoft.kapp.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.RotateDrawable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import com.microsoft.kapp.FontManager;
import com.microsoft.kapp.KApplicationGraph;
import com.microsoft.kapp.R;
import com.microsoft.kapp.adapters.TitlePagerAdapter;
import com.microsoft.kapp.diagnostics.Validate;
import com.microsoft.kapp.utils.ViewUtils;
import com.microsoft.kapp.views.CustomFontTextView;
import java.util.HashMap;
import javax.inject.Inject;
/* loaded from: classes.dex */
public class PagerTitleStrip extends FrameLayout implements ViewPager.OnPageChangeListener {
    private View mArrow;
    private int mArrowAnimationDuration;
    @Inject
    FontManager mFontManager;
    private ViewPager.OnPageChangeListener mOnPageChangeListener;
    private final View.OnClickListener mTabClickListener;
    private int mTextFontFamilyId;
    private int mTextSelectedColor;
    private int mTextSize;
    private int mTextUnSelectedColor;
    private TitlePagerAdapter mTitlePagerAdapter;
    private final LinearLayout mTitlesLayout;
    private ViewPager mViewPager;
    static int ATTR_FONT_FAMILY = R.attr.custom_font_family;
    static int ATTR_TEXT_COLOR = 16842904;
    static int ATTR_TEXT_SIZE = 16842901;
    static int[] CUSTOM_ATTRS = {ATTR_FONT_FAMILY, ATTR_TEXT_COLOR, ATTR_TEXT_SIZE};
    static HashMap<Integer, Integer> styleAttributesIndex = ViewUtils.getStyleAttributesWithIndex(CUSTOM_ATTRS);
    static int ATTR_FONT_FAMILY_INDEX = styleAttributesIndex.get(Integer.valueOf(ATTR_FONT_FAMILY)).intValue();
    static int ATTR_TEXT_COLOR_INDEX = styleAttributesIndex.get(Integer.valueOf(ATTR_TEXT_COLOR)).intValue();
    static int ATTR_TEXT_SIZE_INDEX = styleAttributesIndex.get(Integer.valueOf(ATTR_TEXT_SIZE)).intValue();

    /* loaded from: classes.dex */
    public static class CustomTitleTextView extends CustomFontTextView {
        private final int mIndex;

        public CustomTitleTextView(Context context, int index) {
            super(context);
            Validate.isTrue(index >= 0, "index must be greater than or equal to zero.");
            this.mIndex = index;
        }

        public int getIndex() {
            return this.mIndex;
        }
    }

    public PagerTitleStrip(Context context) {
        this(context, null);
        init();
    }

    public PagerTitleStrip(Context context, AttributeSet attr) {
        super(context, attr);
        this.mTabClickListener = new View.OnClickListener() { // from class: com.microsoft.kapp.widgets.PagerTitleStrip.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                CustomTitleTextView tabView = (CustomTitleTextView) view;
                int newSelected = tabView.getIndex();
                PagerTitleStrip.this.mViewPager.setCurrentItem(newSelected);
            }
        };
        init();
        setHorizontalScrollBarEnabled(false);
        LayoutInflater inflater = LayoutInflater.from(context);
        View rootView = inflater.inflate(R.layout.pager_title_strip, (ViewGroup) this, false);
        this.mTitlesLayout = (LinearLayout) ViewUtils.getValidView(rootView, R.id.titles_container, LinearLayout.class);
        this.mArrow = (View) ViewUtils.getValidView(rootView, R.id.pager_title_arrow, View.class);
        ViewGroup.LayoutParams params = new FrameLayout.LayoutParams(-1, -2, 3);
        addView(rootView, params);
        TypedArray typedArray = getContext().obtainStyledAttributes(attr, R.styleable.PagerTitleStrip);
        TypedArray selectedArray = null;
        TypedArray unSelectedArray = null;
        try {
            int styleId = typedArray.getResourceId(0, 0);
            selectedArray = getContext().obtainStyledAttributes(styleId, CUSTOM_ATTRS);
            int unSelectedStyleId = typedArray.getResourceId(1, 0);
            unSelectedArray = getContext().obtainStyledAttributes(unSelectedStyleId, CUSTOM_ATTRS);
            this.mTextFontFamilyId = selectedArray.getInt(ATTR_FONT_FAMILY_INDEX, 0);
            this.mTextSelectedColor = selectedArray.getColor(ATTR_TEXT_COLOR_INDEX, 0);
            this.mTextSize = selectedArray.getDimensionPixelOffset(ATTR_TEXT_SIZE_INDEX, 0);
            this.mTextUnSelectedColor = unSelectedArray.getColor(ATTR_TEXT_COLOR_INDEX, 0);
            this.mArrowAnimationDuration = typedArray.getInt(2, 0);
        } finally {
            typedArray.recycle();
            if (selectedArray != null) {
                selectedArray.recycle();
            }
            if (unSelectedArray != null) {
                unSelectedArray.recycle();
            }
        }
    }

    private void init() {
        KApplicationGraph.getApplicationGraph().inject(this);
    }

    @Override // android.view.View
    public void setBackgroundColor(int color) {
        this.mTitlesLayout.setBackgroundColor(color);
        RotateDrawable rotateDrawable = (RotateDrawable) this.mArrow.getBackground();
        GradientDrawable gradientDrawable = (GradientDrawable) rotateDrawable.getDrawable();
        gradientDrawable.setColor(color);
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
        if (!(adapter instanceof TitlePagerAdapter)) {
            throw new IllegalStateException("The adapter assigned to the ViewPager is not an instance of TitlePagerAdapter");
        }
        this.mViewPager = viewPager;
        this.mViewPager.setOnPageChangeListener(this);
        this.mViewPager.setOverScrollMode(2);
        this.mTitlePagerAdapter = (TitlePagerAdapter) adapter;
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
        int tabCount = this.mTitlesLayout.getChildCount();
        float arrowX = 0.0f;
        float arrowMarginLeft = getResources().getDimension(R.dimen.tile_pivot_header_arrow_margin_left);
        int i = 0;
        while (i < tabCount) {
            CustomTitleTextView child = (CustomTitleTextView) this.mTitlesLayout.getChildAt(i);
            boolean isSelected = i == position;
            child.setSelected(isSelected);
            child.setTextColor(isSelected ? this.mTextSelectedColor : this.mTextUnSelectedColor);
            if (isSelected) {
                arrowX = child.getX() - arrowMarginLeft;
            }
            i++;
        }
        if (position == 0) {
            arrowX = arrowMarginLeft;
        }
        this.mArrow.animate().x(arrowX).setDuration(this.mArrowAnimationDuration).start();
    }

    public void setOnPageChangeListener(ViewPager.OnPageChangeListener listener) {
        this.mOnPageChangeListener = listener;
    }

    private View createTitleTextView(int position) {
        Context context = getContext();
        CustomTitleTextView titleTextView = new CustomTitleTextView(context, position);
        titleTextView.setFocusable(true);
        titleTextView.setOnClickListener(this.mTabClickListener);
        titleTextView.setText(this.mTitlePagerAdapter.getPageTitle(position));
        titleTextView.setTextSize(0, this.mTextSize);
        Typeface typeface = this.mFontManager.getFontFace(this.mTextFontFamilyId);
        titleTextView.setTypeface(typeface);
        return titleTextView;
    }

    private void onViewPagerChanged() {
        this.mTitlesLayout.removeAllViews();
        int count = this.mTitlePagerAdapter.getCount();
        for (int i = 0; i < count; i++) {
            View view = createTitleTextView(i);
            if (view != null) {
                this.mTitlesLayout.addView(view);
            }
        }
        setCurrentItem(0);
        requestLayout();
    }
}

package com.microsoft.kapp.views;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.microsoft.kapp.R;
import com.microsoft.kapp.logging.KLog;
import java.util.ArrayList;
/* loaded from: classes.dex */
public class PageIndicatorView extends LinearLayout {
    private static final String TAG = PageIndicatorView.class.getName();
    private Drawable mActiveDrawable;
    private Context mContext;
    private Drawable mInactiveDrawable;
    private int mIndicatorSize;
    private ArrayList<ImageView> mIndicators;
    private int mPageCount;

    public PageIndicatorView(Context context) {
        super(context);
        this.mIndicatorSize = (int) getResources().getDimension(R.dimen.page_indicator_oval_spacing);
        this.mContext = context;
    }

    public PageIndicatorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mIndicatorSize = (int) getResources().getDimension(R.dimen.page_indicator_oval_spacing);
        this.mContext = context;
    }

    @Override // android.view.View
    protected void onFinishInflate() {
        initPageControl();
    }

    private void initPageControl() {
        setLayoutParams(new LinearLayout.LayoutParams(-2, -2));
        this.mIndicators = new ArrayList<>();
        Resources res = getResources();
        try {
            this.mActiveDrawable = Drawable.createFromXml(res, res.getXml(R.drawable.page_indicator));
            this.mInactiveDrawable = Drawable.createFromXml(res, res.getXml(R.drawable.page_indicator));
            this.mActiveDrawable.setBounds(0, 0, R.dimen.page_indicator_oval_size, R.dimen.page_indicator_oval_size);
            this.mInactiveDrawable.setBounds(0, 0, R.dimen.page_indicator_oval_size, R.dimen.page_indicator_oval_size);
            ((GradientDrawable) this.mActiveDrawable).setColor(getResources().getColor(R.color.GreyDarkColor));
            ((GradientDrawable) this.mInactiveDrawable).setColor(getResources().getColor(R.color.GreyColor));
        } catch (Exception e) {
            KLog.e(TAG, e.getMessage(), e);
        }
    }

    public void setPageCount(int pageCount) {
        this.mPageCount = pageCount;
        if (pageCount <= 1) {
            setVisibility(4);
            return;
        }
        setVisibility(0);
        for (int i = 0; i < pageCount; i++) {
            ImageView imageView = new ImageView(this.mContext);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(this.mIndicatorSize, this.mIndicatorSize);
            params.setMargins(this.mIndicatorSize / 2, this.mIndicatorSize, this.mIndicatorSize / 2, this.mIndicatorSize);
            imageView.setLayoutParams(params);
            imageView.setBackground(this.mInactiveDrawable);
            this.mIndicators.add(imageView);
            addView(imageView);
        }
        setCurrentPage(0);
    }

    public void setCurrentPage(int currentPage) {
        if (currentPage < this.mPageCount) {
            int i = 0;
            while (i < this.mIndicators.size()) {
                this.mIndicators.get(i).setBackground(i == currentPage ? this.mActiveDrawable : this.mInactiveDrawable);
                i++;
            }
        }
    }
}

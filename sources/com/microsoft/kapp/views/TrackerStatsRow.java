package com.microsoft.kapp.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.microsoft.kapp.R;
import com.microsoft.kapp.utils.ViewUtils;
/* loaded from: classes.dex */
public class TrackerStatsRow extends LinearLayout {
    private ViewGroup mLeft;
    private ViewGroup mRight;

    public TrackerStatsRow(Context context) {
        super(context);
        initialize(context);
    }

    public TrackerStatsRow(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context);
    }

    public TrackerStatsRow(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initialize(context);
    }

    private void initialize(Context context) {
        LayoutInflater.from(context).inflate(R.layout.tracker_stats_row, this);
        this.mLeft = (ViewGroup) ViewUtils.getValidView(this, R.id.left_child, ViewGroup.class);
        this.mRight = (ViewGroup) ViewUtils.getValidView(this, R.id.right_child, ViewGroup.class);
    }

    public void setLeftTracker(BaseTrackerStat stat) {
        this.mLeft.removeAllViews();
        this.mLeft.addView(stat);
    }

    public void setRightTracker(BaseTrackerStat stat) {
        this.mRight.removeAllViews();
        this.mRight.addView(stat);
    }

    public BaseTrackerStat getLeftChild() {
        return (BaseTrackerStat) this.mLeft.getChildAt(0);
    }

    public BaseTrackerStat getRightChild() {
        return (BaseTrackerStat) this.mRight.getChildAt(0);
    }
}

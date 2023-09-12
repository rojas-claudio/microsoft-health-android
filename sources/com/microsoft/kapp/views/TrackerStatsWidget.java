package com.microsoft.kapp.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import com.microsoft.kapp.R;
import java.util.List;
/* loaded from: classes.dex */
public class TrackerStatsWidget extends LinearLayout {
    private int mStatStyleResourceId;
    private int mTotalStats;

    public TrackerStatsWidget(Context context) {
        super(context);
        this.mStatStyleResourceId = 0;
        this.mTotalStats = 0;
        initialize(context, null);
    }

    public TrackerStatsWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mStatStyleResourceId = 0;
        this.mTotalStats = 0;
        initialize(context, attrs);
    }

    public TrackerStatsWidget(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mStatStyleResourceId = 0;
        this.mTotalStats = 0;
        initialize(context, attrs);
    }

    public int getStatStyleResourceId() {
        return this.mStatStyleResourceId;
    }

    public void setStatStyleResourceId(int mStatStyleResourceId) {
        this.mStatStyleResourceId = mStatStyleResourceId;
    }

    private void initialize(Context context, AttributeSet attrs) {
        setOrientation(1);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TrackerStatsWidget);
        try {
            setStatStyleResourceId(a.getResourceId(0, 0));
        } finally {
            a.recycle();
        }
    }

    public void addStat(BaseTrackerStat stat) {
        if (stat != null) {
            if ((this.mTotalStats & 1) == 0) {
                TrackerStatsRow row = new TrackerStatsRow(getContext());
                row.setLeftTracker(stat);
                addView(row);
                this.mTotalStats++;
                return;
            }
            TrackerStatsRow row2 = getRow(this.mTotalStats);
            if (row2 != null) {
                row2.setRightTracker(stat);
                this.mTotalStats++;
            }
        }
    }

    public BaseTrackerStat getStat(int statIndex) {
        TrackerStatsRow row = getRow(statIndex);
        if (row != null) {
            return (statIndex & 1) == 0 ? row.getLeftChild() : row.getRightChild();
        }
        return null;
    }

    public void setStats(List<BaseTrackerStat> stats) {
        clearStats();
        addStats(stats);
    }

    public void addStats(List<BaseTrackerStat> stats) {
        if (stats != null) {
            for (BaseTrackerStat stat : stats) {
                addStat(stat);
            }
        }
    }

    private TrackerStatsRow getRow(int statIndex) {
        return (TrackerStatsRow) getChildAt(statIndex >> 1);
    }

    public void clearStats() {
        removeAllViews();
        this.mTotalStats = 0;
    }
}

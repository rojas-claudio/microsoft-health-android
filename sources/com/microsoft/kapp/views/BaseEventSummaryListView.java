package com.microsoft.kapp.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import com.microsoft.kapp.R;
import com.microsoft.kapp.adapters.BaseUserEventSummaryAdapter;
import com.microsoft.kapp.diagnostics.Validate;
import com.microsoft.kapp.models.BaseUserEventSummary;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
/* loaded from: classes.dex */
public class BaseEventSummaryListView extends ListView {
    private int mBackgroundResource;
    private Context mContext;
    private int mDetailsTextColor;
    private int mHeaderTextColor;
    private HashSet<String> mIdSet;
    private List<BaseUserEventSummary> mItems;

    public BaseEventSummaryListView(Context context) {
        super(context);
        initialize(context, null);
    }

    public BaseEventSummaryListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context, attrs);
    }

    public BaseEventSummaryListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initialize(context, attrs);
    }

    public void setItems(List<BaseUserEventSummary> items) {
        if (this.mItems == null) {
            this.mItems = new ArrayList(items);
        } else {
            this.mItems.clear();
            this.mItems.addAll(items);
        }
        this.mIdSet = new HashSet<>();
        for (BaseUserEventSummary summary : items) {
            String eventId = summary.getEventId();
            if (!this.mIdSet.contains(eventId)) {
                this.mIdSet.add(eventId);
            }
        }
        applyItemsToList();
    }

    public void addItems(List<BaseUserEventSummary> items) {
        Validate.notNull(items, "items");
        if (this.mIdSet == null) {
            this.mIdSet = new HashSet<>();
        }
        if (this.mItems == null) {
            this.mItems = new ArrayList();
        }
        for (BaseUserEventSummary summary : items) {
            String eventId = summary.getEventId();
            if (!this.mIdSet.contains(eventId)) {
                this.mItems.add(summary);
                this.mIdSet.add(eventId);
            }
        }
        applyItemsToList();
    }

    public void removeItem(int index) {
        if (index >= 0 && index < this.mItems.size()) {
            BaseUserEventSummary summary = this.mItems.get(index);
            String id = summary.getEventId();
            this.mIdSet.remove(id);
            this.mItems.remove(index);
            applyItemsToList();
        }
    }

    public BaseUserEventSummary getLastItem() {
        if (this.mItems == null || this.mItems.isEmpty()) {
            return null;
        }
        return this.mItems.get(this.mItems.size() - 1);
    }

    private void applyItemsToList() {
        BaseUserEventSummaryAdapter adapter = (BaseUserEventSummaryAdapter) getAdapter();
        if (adapter == null) {
            setAdapter((ListAdapter) new BaseUserEventSummaryAdapter(this.mContext, this.mItems, this.mHeaderTextColor, this.mDetailsTextColor, this.mBackgroundResource));
        } else {
            adapter.notifyDataSetChanged();
        }
        setListViewHeightBasedOnChildren();
    }

    private void setListViewHeightBasedOnChildren() {
        ListAdapter listAdapter = getAdapter();
        if (listAdapter != null) {
            int desiredWidth = View.MeasureSpec.makeMeasureSpec(getWidth(), 0);
            int totalHeight = 0;
            int count = listAdapter.getCount();
            View view = null;
            for (int i = 0; i < count; i++) {
                view = listAdapter.getView(i, view, this);
                if (view != null) {
                    if (i == 0) {
                        view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, -2));
                    }
                    view.measure(desiredWidth, 0);
                    totalHeight += view.getMeasuredHeight();
                }
            }
            ViewGroup.LayoutParams layoutParams = getLayoutParams();
            if (count > 1) {
                totalHeight += getDividerHeight() * (count - 1);
            }
            layoutParams.height = totalHeight;
            setLayoutParams(layoutParams);
            requestLayout();
        }
    }

    private void initialize(Context context, AttributeSet attrs) {
        this.mContext = context;
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.BaseEventSummaryListView);
            this.mHeaderTextColor = a.getColor(0, getResources().getColor(R.color.exercise_summary_header_text));
            this.mDetailsTextColor = a.getColor(1, getResources().getColor(R.color.exercise_text));
            this.mBackgroundResource = a.getResourceId(2, R.drawable.exercise_summary_background);
            a.recycle();
        }
        Drawable transparentDrawable = new ColorDrawable(0);
        setDivider(transparentDrawable);
        setSelector(transparentDrawable);
    }
}

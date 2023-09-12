package com.microsoft.kapp.views;

import android.content.Context;
import android.util.AttributeSet;
import com.microsoft.kapp.adapters.UserEventSummaryAdapter;
import com.microsoft.kapp.diagnostics.Validate;
import com.microsoft.kapp.models.UserEventSummary;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
/* loaded from: classes.dex */
public class UserEventSummaryListView extends LinearListView {
    private Context mContext;
    private HashSet<String> mIdSet;
    private List<UserEventSummary> mItems;

    public UserEventSummaryListView(Context context) {
        super(context);
        initialize(context, null);
    }

    public UserEventSummaryListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context, attrs);
    }

    public UserEventSummaryListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initialize(context, attrs);
    }

    public void setItems(List<UserEventSummary> items) {
        Validate.notNull(items, "items");
        if (this.mItems == null) {
            this.mItems = new ArrayList(items);
        } else {
            this.mItems.clear();
            this.mItems.addAll(items);
        }
        this.mIdSet = new HashSet<>();
        for (UserEventSummary summary : items) {
            String eventId = summary.getEventId();
            if (!this.mIdSet.contains(eventId)) {
                this.mIdSet.add(eventId);
            }
        }
        applyItemsToList();
    }

    public void addItems(List<UserEventSummary> items) {
        Validate.notNull(items, "items");
        if (this.mIdSet == null) {
            this.mIdSet = new HashSet<>();
        }
        if (this.mItems == null) {
            this.mItems = new ArrayList();
        }
        for (UserEventSummary summary : items) {
            String eventId = summary.getEventId();
            if (!this.mIdSet.contains(eventId)) {
                this.mItems.add(summary);
                this.mIdSet.add(eventId);
            }
        }
        applyItemsToList();
    }

    public UserEventSummary getLastItem() {
        if (this.mItems == null || this.mItems.isEmpty()) {
            return null;
        }
        return this.mItems.get(this.mItems.size() - 1);
    }

    public void removeItem(int index) {
        if (index >= 0 && index < this.mItems.size()) {
            UserEventSummary summary = this.mItems.get(index);
            String id = summary.getEventId();
            this.mIdSet.remove(id);
            this.mItems.remove(index);
            applyItemsToList();
        }
    }

    private void applyItemsToList() {
        UserEventSummaryAdapter adapter = (UserEventSummaryAdapter) getAdapter();
        if (adapter == null) {
            setAdapter(new UserEventSummaryAdapter(this.mContext, this.mItems));
        } else {
            adapter.notifyDataSetChanged();
        }
    }

    private void initialize(Context context, AttributeSet attrs) {
        this.mContext = context;
    }
}

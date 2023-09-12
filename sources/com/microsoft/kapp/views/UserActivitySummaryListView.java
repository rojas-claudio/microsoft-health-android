package com.microsoft.kapp.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.microsoft.kapp.R;
import com.microsoft.kapp.adapters.UserEventSummaryAdapter;
import com.microsoft.kapp.diagnostics.Validate;
import com.microsoft.kapp.models.UserActivitySummary;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes.dex */
public class UserActivitySummaryListView extends LinearListView {
    private Context mContext;
    private List<UserActivitySummary> mItems;

    public UserActivitySummaryListView(Context context) {
        super(context);
        initialize(context);
    }

    public UserActivitySummaryListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context);
    }

    public UserActivitySummaryListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initialize(context);
    }

    public void setItems(List<UserActivitySummary> items) {
        if (this.mItems == null) {
            this.mItems = new ArrayList(items);
        } else {
            this.mItems.clear();
            this.mItems.addAll(items);
        }
        applyItemsToList();
    }

    public void addItems(List<UserActivitySummary> items) {
        Validate.notNull(items, "items");
        this.mItems.addAll(items);
        applyItemsToList();
    }

    public void removeItem(int index) {
        if (index >= 0 && index < this.mItems.size()) {
            this.mItems.remove(index);
            applyItemsToList();
        }
    }

    public UserActivitySummary getLastItem() {
        if (this.mItems == null || this.mItems.isEmpty()) {
            return null;
        }
        return this.mItems.get(this.mItems.size() - 1);
    }

    public int getItemCount() {
        return this.mItems.size();
    }

    private void applyItemsToList() {
        UserActivitySummaryAdapter adapter = (UserActivitySummaryAdapter) getAdapter();
        if (adapter == null) {
            setAdapter(new UserActivitySummaryAdapter(this.mContext, this.mItems));
            ViewTreeObserver viewTreeObserver = getViewTreeObserver();
            if (viewTreeObserver != null && viewTreeObserver.isAlive()) {
                viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() { // from class: com.microsoft.kapp.views.UserActivitySummaryListView.1
                    @Override // android.view.ViewTreeObserver.OnGlobalLayoutListener
                    public void onGlobalLayout() {
                        UserActivitySummaryListView.this.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                });
                return;
            }
            return;
        }
        adapter.notifyDataSetChanged();
    }

    private void initialize(Context context) {
        this.mContext = context;
    }

    public void refresh() {
        UserEventSummaryAdapter adapter = (UserEventSummaryAdapter) getAdapter();
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class UserActivitySummaryAdapter extends ArrayAdapter<UserActivitySummary> {
        private static final int RESOURCE_ID = 2130903081;

        /* loaded from: classes.dex */
        private class ViewHolder {
            TextView dateText;
            TextView valueText;

            private ViewHolder() {
            }
        }

        public UserActivitySummaryAdapter(Context context, List<UserActivitySummary> summaries) {
            super(context, (int) R.layout.adapter_user_activity_summary, summaries);
        }

        @Override // android.widget.ArrayAdapter, android.widget.Adapter
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                LayoutInflater inflater = (LayoutInflater) getContext().getSystemService("layout_inflater");
                convertView = inflater.inflate(R.layout.adapter_user_activity_summary, (ViewGroup) null);
                holder.dateText = (TextView) convertView.findViewById(R.id.activity_date_name);
                holder.valueText = (TextView) convertView.findViewById(R.id.activity_value);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            UserActivitySummary summary = getItem(position);
            String name = summary.getName(UserActivitySummaryListView.this.mContext);
            holder.dateText.setText(name);
            holder.valueText.setText(summary.getValue(UserActivitySummaryListView.this.mContext));
            return convertView;
        }
    }
}

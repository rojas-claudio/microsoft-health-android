package com.microsoft.kapp.views;

import android.content.Context;
import android.database.DataSetObserver;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
/* loaded from: classes.dex */
public class LinearListView extends LinearLayout {
    private ListAdapter mAdapter;
    private DataSetObserver mDataObserver;
    private OnItemClickListener mOnItemClickListener;

    /* loaded from: classes.dex */
    public interface OnItemClickListener {
        void onItemClick(LinearListView linearListView, View view, int i, long j);
    }

    public LinearListView(Context context) {
        super(context);
        this.mDataObserver = new DataSetObserver() { // from class: com.microsoft.kapp.views.LinearListView.1
            @Override // android.database.DataSetObserver
            public void onChanged() {
                LinearListView.this.setupChildren();
            }

            @Override // android.database.DataSetObserver
            public void onInvalidated() {
                LinearListView.this.setupChildren();
            }
        };
        init();
    }

    public LinearListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mDataObserver = new DataSetObserver() { // from class: com.microsoft.kapp.views.LinearListView.1
            @Override // android.database.DataSetObserver
            public void onChanged() {
                LinearListView.this.setupChildren();
            }

            @Override // android.database.DataSetObserver
            public void onInvalidated() {
                LinearListView.this.setupChildren();
            }
        };
        init();
    }

    public LinearListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mDataObserver = new DataSetObserver() { // from class: com.microsoft.kapp.views.LinearListView.1
            @Override // android.database.DataSetObserver
            public void onChanged() {
                LinearListView.this.setupChildren();
            }

            @Override // android.database.DataSetObserver
            public void onInvalidated() {
                LinearListView.this.setupChildren();
            }
        };
        init();
    }

    public void setAdapter(ListAdapter adapter) {
        if (this.mAdapter != null) {
            this.mAdapter.unregisterDataSetObserver(this.mDataObserver);
        }
        this.mAdapter = adapter;
        if (this.mAdapter != null) {
            this.mAdapter.registerDataSetObserver(this.mDataObserver);
        }
        setupChildren();
    }

    public ListAdapter getAdapter() {
        return this.mAdapter;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public boolean performItemClick(View view, int position, long id) {
        if (this.mOnItemClickListener != null) {
            this.mOnItemClickListener.onItemClick(this, view, position, id);
            return true;
        }
        return false;
    }

    public int getCount() {
        return this.mAdapter.getCount();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setupChildren() {
        removeAllViews();
        if (this.mAdapter != null && !this.mAdapter.isEmpty()) {
            for (int i = 0; i < this.mAdapter.getCount(); i++) {
                View child = this.mAdapter.getView(i, null, this);
                child.setOnClickListener(new InternalOnClickListener(i));
                addView(child);
            }
        }
    }

    private void init() {
        setOrientation(1);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class InternalOnClickListener implements View.OnClickListener {
        int mPosition;

        public InternalOnClickListener(int position) {
            this.mPosition = position;
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View v) {
            if (LinearListView.this.mOnItemClickListener != null && LinearListView.this.mAdapter != null) {
                LinearListView.this.mOnItemClickListener.onItemClick(LinearListView.this, v, this.mPosition, LinearListView.this.mAdapter.getItemId(this.mPosition));
            }
        }
    }
}

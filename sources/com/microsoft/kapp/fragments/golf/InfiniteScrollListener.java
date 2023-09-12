package com.microsoft.kapp.fragments.golf;

import android.widget.AbsListView;
/* loaded from: classes.dex */
public abstract class InfiniteScrollListener implements AbsListView.OnScrollListener {
    private int visibleThreshold;
    private int currentPage = 0;
    private int previousTotal = 0;
    private boolean loading = false;

    public abstract boolean hasData();

    public abstract void loadMore(int i, int i2);

    public InfiniteScrollListener(int visibleThreshold) {
        this.visibleThreshold = 5;
        this.visibleThreshold = visibleThreshold;
    }

    @Override // android.widget.AbsListView.OnScrollListener
    public void onScrollStateChanged(AbsListView view, int scrollState) {
    }

    @Override // android.widget.AbsListView.OnScrollListener
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (hasData()) {
            if (this.loading && totalItemCount > this.previousTotal) {
                this.loading = false;
                this.previousTotal = totalItemCount;
                this.currentPage++;
            }
            if (!this.loading && totalItemCount - visibleItemCount <= this.visibleThreshold + firstVisibleItem) {
                loadMore(this.currentPage + 1, totalItemCount);
                this.loading = true;
            }
        }
    }
}

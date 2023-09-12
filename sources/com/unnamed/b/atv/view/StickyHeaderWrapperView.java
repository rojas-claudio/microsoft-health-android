package com.unnamed.b.atv.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import com.unnamed.b.atv.R;
/* loaded from: classes.dex */
public class StickyHeaderWrapperView extends RelativeLayout {
    private RelativeLayout mHeaderView;
    private StickyHeaderScrollView mScrollView;
    private AndroidTreeView mTreeView;

    public StickyHeaderWrapperView(Context context) {
        super(context);
    }

    public StickyHeaderWrapperView(Context context, AndroidTreeView treeView) {
        super(context);
        this.mTreeView = treeView;
        initialzeViews();
    }

    @Override // android.view.ViewGroup
    public void addView(View view) {
        if (this.mScrollView != null) {
            this.mScrollView.addView(view);
        }
    }

    private void initialzeViews() {
        LayoutInflater mInflater = LayoutInflater.from(getContext());
        View viewRoot = mInflater.inflate(R.layout.sticky_header_view, (ViewGroup) this, true);
        this.mScrollView = (StickyHeaderScrollView) viewRoot.findViewById(R.id.sticky_header_view_scroll_view);
        this.mHeaderView = (RelativeLayout) viewRoot.findViewById(R.id.sticky_header_view_header);
        this.mScrollView.setTreeView(this.mTreeView);
        this.mScrollView.setHeaderView(this.mHeaderView);
    }

    public StickyHeaderScrollView getScrollView() {
        return this.mScrollView;
    }
}

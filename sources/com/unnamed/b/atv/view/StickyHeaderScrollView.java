package com.unnamed.b.atv.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import com.unnamed.b.atv.model.TreeNode;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes.dex */
public class StickyHeaderScrollView extends ScrollView {
    private TreeNode mHeaderNode;
    private RelativeLayout mHeaderView;
    private AndroidTreeView mTreeView;
    private List<ViewPositionItem> mViewPositionList;

    public StickyHeaderScrollView(Context context) {
        super(context);
    }

    public StickyHeaderScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public StickyHeaderScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public StickyHeaderScrollView(Context context, AndroidTreeView treeView, RelativeLayout header) {
        super(context);
        this.mTreeView = treeView;
        this.mHeaderView = header;
    }

    public void setTreeView(AndroidTreeView treeView) {
        this.mTreeView = treeView;
    }

    public void setHeaderView(RelativeLayout header) {
        this.mHeaderView = header;
    }

    @Override // android.view.View
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        if (this.mHeaderView != null) {
            List<Integer> steps = getStepsFromViewPositionList(t);
            if (steps != null) {
                TreeNode node = this.mTreeView.getTreeRoot();
                for (int i = 0; i < steps.size(); i++) {
                    node = node.getChildren().get(steps.get(i).intValue());
                }
                showHeader(node);
                return;
            }
            hideHeader();
        }
    }

    private void showHeader(TreeNode node) {
        if (this.mHeaderView != null && node != null) {
            this.mHeaderView.setVisibility(0);
            if (!node.isValid() || !node.equals(this.mHeaderNode)) {
                this.mHeaderNode = node;
                this.mHeaderView.removeAllViews();
                this.mHeaderView.addView(node.getViewHolder().getAlternativeView());
                node.setValid(true);
            }
        }
    }

    private void hideHeader() {
        this.mHeaderView.setVisibility(8);
    }

    @Override // android.widget.ScrollView, android.widget.FrameLayout, android.view.ViewGroup, android.view.View
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, right);
        constructViewPositionList();
    }

    public void constructViewPositionList() {
        if (getChildCount() > 0) {
            LinearLayout treeContainer = (LinearLayout) getChildAt(0);
            this.mViewPositionList = constructViewPositionList(treeContainer);
        }
    }

    private List<ViewPositionItem> constructViewPositionList(LinearLayout treeContainer) {
        List<ViewPositionItem> list = new ArrayList<>();
        int totalHeightFromTop = 0;
        if (treeContainer != null) {
            for (int i = 0; i < treeContainer.getChildCount(); i++) {
                LinearLayout item = (LinearLayout) treeContainer.getChildAt(i);
                if (item.getChildCount() > 1) {
                    totalHeightFromTop += item.getChildAt(0).getHeight();
                    LinearLayout childLists = (LinearLayout) item.getChildAt(1);
                    if (childLists.getChildCount() > 0) {
                        for (int j = 0; j < childLists.getChildCount(); j++) {
                            List<Integer> steps = new ArrayList<>();
                            View childItem = childLists.getChildAt(j);
                            steps.add(Integer.valueOf(i));
                            steps.add(Integer.valueOf(j));
                            list.add(new ViewPositionItem(totalHeightFromTop, steps));
                            totalHeightFromTop += childItem.getHeight();
                        }
                    }
                }
            }
        }
        return list;
    }

    private List<Integer> getStepsFromViewPositionList(int t) {
        if (this.mViewPositionList == null || this.mViewPositionList.size() == 0) {
            return null;
        }
        int left = 0;
        int right = this.mViewPositionList.size() - 1;
        int resultIndex = -1;
        while (left <= right) {
            int mid = (right + left) / 2;
            ViewPositionItem midItem = this.mViewPositionList.get(mid);
            if (midItem.distanceFromRoot == t) {
                return midItem.stepsOnTree;
            }
            if (midItem.distanceFromRoot > t) {
                right = mid - 1;
            } else {
                resultIndex = mid;
                left = mid + 1;
            }
        }
        if (resultIndex >= 0) {
            return this.mViewPositionList.get(resultIndex).stepsOnTree;
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class ViewPositionItem {
        public int distanceFromRoot;
        public List<Integer> stepsOnTree;

        public ViewPositionItem(int distanceFromRoot, List<Integer> steps) {
            this.distanceFromRoot = distanceFromRoot;
            this.stepsOnTree = steps;
        }
    }
}

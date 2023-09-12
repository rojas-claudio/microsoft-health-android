package com.unnamed.b.atv.view;

import android.content.Context;
import android.text.TextUtils;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.LinearLayout;
import com.unnamed.b.atv.R;
import com.unnamed.b.atv.holder.SimpleViewHolder;
import com.unnamed.b.atv.model.TreeNode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
/* loaded from: classes.dex */
public class AndroidTreeView {
    private static final String NODES_PATH_SEPARATOR = ";";
    private boolean applyForRoot;
    private Context mContext;
    private TreeNode mRoot;
    private boolean mSelectionModeEnabled;
    private TreeNode.TreeNodeClickListener nodeClickListener;
    private int containerStyle = 0;
    private Class<? extends TreeNode.BaseNodeViewHolder> defaultViewHolderClass = SimpleViewHolder.class;
    private boolean mUseDefaultAnimation = false;
    private boolean use2dScroll = false;

    public AndroidTreeView(Context context, TreeNode root) {
        this.mRoot = root;
        this.mContext = context;
    }

    public void setDefaultAnimation(boolean defaultAnimation) {
        this.mUseDefaultAnimation = defaultAnimation;
    }

    public void setDefaultContainerStyle(int style) {
        setDefaultContainerStyle(style, false);
    }

    public void setDefaultContainerStyle(int style, boolean applyForRoot) {
        this.containerStyle = style;
        this.applyForRoot = applyForRoot;
    }

    public void setUse2dScroll(boolean use2dScroll) {
        this.use2dScroll = use2dScroll;
    }

    public boolean is2dScrollEnabled() {
        return this.use2dScroll;
    }

    public void setDefaultViewHolder(Class<? extends TreeNode.BaseNodeViewHolder> viewHolder) {
        this.defaultViewHolderClass = viewHolder;
    }

    public void setDefaultNodeClickListener(TreeNode.TreeNodeClickListener listener) {
        this.nodeClickListener = listener;
    }

    public void expandAll() {
        expandNode(this.mRoot, true);
    }

    public void collapseAll() {
        for (TreeNode n : this.mRoot.getChildren()) {
            collapseNode(n, true);
        }
    }

    public TreeNode getTreeRoot() {
        return this.mRoot;
    }

    public View getView(int style) {
        ViewGroup view;
        if (style > 0) {
            ContextThemeWrapper newContext = new ContextThemeWrapper(this.mContext, style);
            view = this.use2dScroll ? new TwoDScrollView(newContext) : new StickyHeaderWrapperView(newContext, this);
        } else {
            view = this.use2dScroll ? new TwoDScrollView(this.mContext) : new StickyHeaderWrapperView(this.mContext, this);
        }
        view.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
        Context containerContext = this.mContext;
        if (this.containerStyle != 0 && this.applyForRoot) {
            containerContext = new ContextThemeWrapper(this.mContext, this.containerStyle);
        }
        final LinearLayout viewTreeItems = new LinearLayout(containerContext, null, this.containerStyle);
        viewTreeItems.setId(R.id.tree_items);
        viewTreeItems.setOrientation(1);
        view.addView(viewTreeItems);
        this.mRoot.setViewHolder(new TreeNode.BaseNodeViewHolder(this.mContext) { // from class: com.unnamed.b.atv.view.AndroidTreeView.1
            @Override // com.unnamed.b.atv.model.TreeNode.BaseNodeViewHolder
            public View createNodeView(TreeNode node, ViewGroup container, Object value) {
                return null;
            }

            @Override // com.unnamed.b.atv.model.TreeNode.BaseNodeViewHolder
            public ViewGroup getNodeItemsView() {
                return viewTreeItems;
            }
        });
        expandNode(this.mRoot, false);
        return view;
    }

    public View getView() {
        return getView(-1);
    }

    public void expandLevel(int level) {
        for (TreeNode n : this.mRoot.getChildren()) {
            expandLevel(n, level);
        }
    }

    private void expandLevel(TreeNode node, int level) {
        if (node.getLevel() <= level) {
            expandNode(node, false);
        }
        for (TreeNode n : node.getChildren()) {
            expandLevel(n, level);
        }
    }

    public void expandNode(TreeNode node) {
        expandNode(node, false);
    }

    public void collapseNode(TreeNode node) {
        collapseNode(node, false);
    }

    public String getSaveState() {
        StringBuilder builder = new StringBuilder();
        getSaveState(this.mRoot, builder);
        if (builder.length() > 0) {
            builder.setLength(builder.length() - 1);
        }
        return builder.toString();
    }

    public void restoreState(String saveState) {
        if (!TextUtils.isEmpty(saveState)) {
            collapseAll();
            String[] openNodesArray = saveState.split(NODES_PATH_SEPARATOR);
            Set<String> openNodes = new HashSet<>(Arrays.asList(openNodesArray));
            restoreNodeState(this.mRoot, openNodes);
        }
    }

    private void restoreNodeState(TreeNode node, Set<String> openNodes) {
        for (TreeNode n : node.getChildren()) {
            if (openNodes.contains(n.getPath())) {
                expandNode(n);
                restoreNodeState(n, openNodes);
            }
        }
    }

    private void getSaveState(TreeNode root, StringBuilder sBuilder) {
        for (TreeNode node : root.getChildren()) {
            if (node.isExpanded()) {
                sBuilder.append(node.getPath());
                sBuilder.append(NODES_PATH_SEPARATOR);
                getSaveState(node, sBuilder);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void toggleNode(TreeNode node) {
        if (node.isExpanded()) {
            collapseNode(node, false);
        } else {
            expandNode(node, false);
        }
    }

    private void collapseNode(TreeNode node, boolean includeSubnodes) {
        node.setExpanded(false);
        TreeNode.BaseNodeViewHolder nodeViewHolder = getViewHolderForNode(node);
        if (this.mUseDefaultAnimation) {
            collapse(nodeViewHolder.getNodeItemsView());
        } else {
            nodeViewHolder.getNodeItemsView().setVisibility(8);
        }
        nodeViewHolder.toggle(false);
        if (includeSubnodes) {
            for (TreeNode n : node.getChildren()) {
                collapseNode(n, includeSubnodes);
            }
        }
    }

    private void expandNode(TreeNode node, boolean includeSubnodes) {
        node.setExpanded(true);
        TreeNode.BaseNodeViewHolder parentViewHolder = getViewHolderForNode(node);
        parentViewHolder.getNodeItemsView().removeAllViews();
        parentViewHolder.toggle(true);
        for (TreeNode n : node.getChildren()) {
            addNode(parentViewHolder.getNodeItemsView(), n);
            if (n.isExpanded() || includeSubnodes) {
                expandNode(n, includeSubnodes);
            }
        }
        if (this.mUseDefaultAnimation) {
            expand(parentViewHolder.getNodeItemsView());
        } else {
            parentViewHolder.getNodeItemsView().setVisibility(0);
        }
    }

    private void addNode(ViewGroup container, final TreeNode n) {
        TreeNode.BaseNodeViewHolder viewHolder = getViewHolderForNode(n);
        View nodeView = viewHolder.getView(container);
        container.addView(nodeView);
        if (this.mSelectionModeEnabled) {
            viewHolder.toggleSelectionMode(this.mSelectionModeEnabled);
        }
        nodeView.setOnClickListener(new View.OnClickListener() { // from class: com.unnamed.b.atv.view.AndroidTreeView.2
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                if (n.getClickListener() == null) {
                    if (AndroidTreeView.this.nodeClickListener != null) {
                        AndroidTreeView.this.nodeClickListener.onClick(n, n.getValue());
                    }
                } else {
                    n.getClickListener().onClick(n, n.getValue());
                }
                AndroidTreeView.this.toggleNode(n);
            }
        });
    }

    public void setSelectionModeEnabled(boolean selectionModeEnabled) {
        if (!selectionModeEnabled) {
            deselectAll();
        }
        this.mSelectionModeEnabled = selectionModeEnabled;
        for (TreeNode node : this.mRoot.getChildren()) {
            toggleSelectionMode(node, selectionModeEnabled);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public <E> List<E> getSelectedValues(Class<E> clazz) {
        ArrayList arrayList = new ArrayList();
        List<TreeNode> selected = getSelected();
        for (TreeNode n : selected) {
            Object value = n.getValue();
            if (value != null && value.getClass().equals(clazz)) {
                arrayList.add(value);
            }
        }
        return arrayList;
    }

    public boolean isSelectionModeEnabled() {
        return this.mSelectionModeEnabled;
    }

    private void toggleSelectionMode(TreeNode parent, boolean mSelectionModeEnabled) {
        toogleSelectionForNode(parent, mSelectionModeEnabled);
        if (parent.isExpanded()) {
            for (TreeNode node : parent.getChildren()) {
                toggleSelectionMode(node, mSelectionModeEnabled);
            }
        }
    }

    public List<TreeNode> getSelected() {
        return this.mSelectionModeEnabled ? getSelected(this.mRoot) : new ArrayList();
    }

    private List<TreeNode> getSelected(TreeNode parent) {
        List<TreeNode> result = new ArrayList<>();
        for (TreeNode n : parent.getChildren()) {
            if (n.isSelected()) {
                result.add(n);
            }
            result.addAll(getSelected(n));
        }
        return result;
    }

    public void selectAll(boolean skipCollapsed) {
        makeAllSelection(true, skipCollapsed);
    }

    public void deselectAll() {
        makeAllSelection(false, false);
    }

    private void makeAllSelection(boolean selected, boolean skipCollapsed) {
        if (this.mSelectionModeEnabled) {
            for (TreeNode node : this.mRoot.getChildren()) {
                selectNode(node, selected, skipCollapsed);
            }
        }
    }

    public void selectNode(TreeNode node, boolean selected) {
        if (this.mSelectionModeEnabled) {
            node.setSelected(selected);
            toogleSelectionForNode(node, true);
        }
    }

    private void selectNode(TreeNode parent, boolean selected, boolean skipCollapsed) {
        parent.setSelected(selected);
        toogleSelectionForNode(parent, true);
        boolean toContinue = skipCollapsed ? parent.isExpanded() : true;
        if (toContinue) {
            for (TreeNode node : parent.getChildren()) {
                selectNode(node, selected, skipCollapsed);
            }
        }
    }

    private void toogleSelectionForNode(TreeNode node, boolean makeSelectable) {
        TreeNode.BaseNodeViewHolder holder = getViewHolderForNode(node);
        if (holder.isInitialized()) {
            getViewHolderForNode(node).toggleSelectionMode(makeSelectable);
        }
    }

    private TreeNode.BaseNodeViewHolder getViewHolderForNode(TreeNode node) {
        TreeNode.BaseNodeViewHolder viewHolder = node.getViewHolder();
        if (viewHolder == null) {
            try {
                Object object = this.defaultViewHolderClass.getConstructor(Context.class).newInstance(this.mContext);
                viewHolder = (TreeNode.BaseNodeViewHolder) object;
                node.setViewHolder(viewHolder);
            } catch (Exception e) {
                throw new RuntimeException("Could not instantiate class " + this.defaultViewHolderClass);
            }
        }
        if (viewHolder.getContainerStyle() <= 0) {
            viewHolder.setContainerStyle(this.containerStyle);
        }
        if (viewHolder.getTreeView() == null) {
            viewHolder.setTreeViev(this);
        }
        return viewHolder;
    }

    private static void expand(final View v) {
        v.measure(-1, -2);
        final int targetHeight = v.getMeasuredHeight();
        v.getLayoutParams().height = 0;
        v.setVisibility(0);
        Animation a = new Animation() { // from class: com.unnamed.b.atv.view.AndroidTreeView.3
            @Override // android.view.animation.Animation
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1.0f ? -2 : (int) (targetHeight * interpolatedTime);
                v.requestLayout();
            }

            @Override // android.view.animation.Animation
            public boolean willChangeBounds() {
                return true;
            }
        };
        a.setDuration((int) (targetHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

    private static void collapse(final View v) {
        final int initialHeight = v.getMeasuredHeight();
        Animation a = new Animation() { // from class: com.unnamed.b.atv.view.AndroidTreeView.4
            @Override // android.view.animation.Animation
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if (interpolatedTime == 1.0f) {
                    v.setVisibility(8);
                    return;
                }
                v.getLayoutParams().height = initialHeight - ((int) (initialHeight * interpolatedTime));
                v.requestLayout();
            }

            @Override // android.view.animation.Animation
            public boolean willChangeBounds() {
                return true;
            }
        };
        a.setDuration((int) (initialHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

    public void addNode(TreeNode parent, TreeNode nodeToAdd) {
        parent.addChild(nodeToAdd);
        if (parent.isExpanded()) {
            TreeNode.BaseNodeViewHolder parentViewHolder = getViewHolderForNode(parent);
            addNode(parentViewHolder.getNodeItemsView(), nodeToAdd);
        }
    }

    public void removeNode(TreeNode node) {
        if (node.getParent() != null) {
            TreeNode parent = node.getParent();
            int index = parent.deleteChild(node);
            if (parent.isExpanded() && index >= 0) {
                TreeNode.BaseNodeViewHolder parentViewHolder = getViewHolderForNode(parent);
                parentViewHolder.getNodeItemsView().removeViewAt(index);
            }
        }
    }
}

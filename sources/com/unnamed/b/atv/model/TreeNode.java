package com.unnamed.b.atv.model;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import com.unnamed.b.atv.R;
import com.unnamed.b.atv.view.AndroidTreeView;
import com.unnamed.b.atv.view.TreeNodeWrapperView;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
/* loaded from: classes.dex */
public class TreeNode {
    public static final String NODES_ID_SEPARATOR = ":";
    private boolean isValid;
    private boolean mExpanded;
    private int mId;
    private TreeNodeClickListener mListener;
    private TreeNode mParent;
    private boolean mSelected;
    private Object mValue;
    private BaseNodeViewHolder mViewHolder;
    private boolean mSelectable = true;
    private final List<TreeNode> children = new ArrayList();

    /* loaded from: classes.dex */
    public interface TreeNodeClickListener {
        void onClick(TreeNode treeNode, Object obj);
    }

    public static TreeNode root() {
        TreeNode root = new TreeNode(null);
        root.setSelectable(false);
        return root;
    }

    public TreeNode(Object value) {
        this.mValue = value;
    }

    public TreeNode addChild(TreeNode childNode) {
        childNode.mParent = this;
        childNode.mId = size();
        this.children.add(childNode);
        return this;
    }

    public TreeNode addChildren(TreeNode... nodes) {
        for (TreeNode n : nodes) {
            addChild(n);
        }
        return this;
    }

    public TreeNode addChildren(Collection<TreeNode> nodes) {
        for (TreeNode n : nodes) {
            addChild(n);
        }
        return this;
    }

    public int deleteChild(TreeNode child) {
        for (int i = 0; i < this.children.size(); i++) {
            if (child.mId == this.children.get(i).mId) {
                this.children.remove(i);
                return i;
            }
        }
        return -1;
    }

    public List<TreeNode> getChildren() {
        return Collections.unmodifiableList(this.children);
    }

    public int size() {
        return this.children.size();
    }

    public TreeNode getParent() {
        return this.mParent;
    }

    public int getId() {
        return this.mId;
    }

    public boolean isLeaf() {
        return size() == 0;
    }

    public Object getValue() {
        return this.mValue;
    }

    public boolean isExpanded() {
        return this.mExpanded;
    }

    public TreeNode setExpanded(boolean expanded) {
        this.mExpanded = expanded;
        return this;
    }

    public void setSelected(boolean selected) {
        this.mSelected = selected;
    }

    public boolean isSelected() {
        if (this.mSelectable) {
            return this.mSelected;
        }
        return false;
    }

    public void setSelectable(boolean selectable) {
        this.mSelectable = selectable;
    }

    public boolean isSelectable() {
        return this.mSelectable;
    }

    public boolean isValid() {
        return this.isValid;
    }

    public void setValid(boolean isValid) {
        this.isValid = isValid;
    }

    public void invalidate() {
        this.isValid = false;
    }

    public String getPath() {
        StringBuilder path = new StringBuilder();
        TreeNode node = this;
        while (node.mParent != null) {
            path.append(node.getId());
            node = node.mParent;
            if (node.mParent != null) {
                path.append(NODES_ID_SEPARATOR);
            }
        }
        return path.toString();
    }

    public int getLevel() {
        int level = 0;
        TreeNode root = this;
        while (root.mParent != null) {
            root = root.mParent;
            level++;
        }
        return level;
    }

    public boolean isLastChild() {
        int parentSize;
        if (isRoot() || (parentSize = this.mParent.children.size()) <= 0) {
            return false;
        }
        List<TreeNode> parentChildren = this.mParent.children;
        return parentChildren.get(parentSize + (-1)).mId == this.mId;
    }

    public TreeNode setViewHolder(BaseNodeViewHolder viewHolder) {
        this.mViewHolder = viewHolder;
        if (viewHolder != null) {
            viewHolder.mNode = this;
        }
        return this;
    }

    public TreeNode setClickListener(TreeNodeClickListener listener) {
        this.mListener = listener;
        return this;
    }

    public TreeNodeClickListener getClickListener() {
        return this.mListener;
    }

    public BaseNodeViewHolder getViewHolder() {
        return this.mViewHolder;
    }

    public boolean isFirstChild() {
        if (isRoot()) {
            return false;
        }
        List<TreeNode> parentChildren = this.mParent.children;
        return parentChildren.get(0).mId == this.mId;
    }

    public boolean isRoot() {
        return this.mParent == null;
    }

    public TreeNode getRoot() {
        TreeNode root = this;
        while (root.mParent != null) {
            root = root.mParent;
        }
        return root;
    }

    /* loaded from: classes.dex */
    public static abstract class BaseNodeViewHolder<E> {
        protected View alternativeView;
        protected int containerStyle;
        protected Context context;
        protected boolean isExpandAble = true;
        protected TreeNode mNode;
        protected E mValue;
        private View mView;
        protected AndroidTreeView tView;

        public abstract View createNodeView(TreeNode treeNode, ViewGroup viewGroup, E e);

        public BaseNodeViewHolder(Context context) {
            this.context = context;
        }

        public View getView(ViewGroup container) {
            if (this.mView != null) {
                return this.mView;
            }
            View nodeView = getNodeView(container);
            TreeNodeWrapperView nodeWrapperView = new TreeNodeWrapperView(nodeView.getContext(), getContainerStyle());
            nodeWrapperView.insertNodeView(nodeView);
            this.mView = nodeWrapperView;
            return this.mView;
        }

        public void setTreeViev(AndroidTreeView treeViev) {
            this.tView = treeViev;
        }

        public AndroidTreeView getTreeView() {
            return this.tView;
        }

        public void setContainerStyle(int style) {
            this.containerStyle = style;
        }

        /* JADX WARN: Multi-variable type inference failed */
        public View getNodeView(ViewGroup container) {
            return createNodeView(this.mNode, container, this.mNode.getValue());
        }

        public ViewGroup getNodeItemsView() {
            return (ViewGroup) getView(null).findViewById(R.id.node_items);
        }

        public boolean isInitialized() {
            return this.mView != null;
        }

        public int getContainerStyle() {
            return this.containerStyle;
        }

        public View getAlternativeView() {
            return this.alternativeView;
        }

        public boolean isExpandAble() {
            return this.isExpandAble;
        }

        public void setExpandAble(boolean isExpandAble) {
            this.isExpandAble = isExpandAble;
        }

        public void toggle(boolean active) {
        }

        public void toggleSelectionMode(boolean editModeEnabled) {
        }
    }
}

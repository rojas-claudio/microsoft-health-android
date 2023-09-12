package com.unnamed.b.atv.holder;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.unnamed.b.atv.model.TreeNode;
/* loaded from: classes.dex */
public class SimpleViewHolder extends TreeNode.BaseNodeViewHolder<Object> {
    public SimpleViewHolder(Context context) {
        super(context);
    }

    @Override // com.unnamed.b.atv.model.TreeNode.BaseNodeViewHolder
    public View createNodeView(TreeNode node, ViewGroup container, Object value) {
        TextView tv = new TextView(this.context);
        tv.setText(String.valueOf(value));
        return tv;
    }

    @Override // com.unnamed.b.atv.model.TreeNode.BaseNodeViewHolder
    public void toggle(boolean active) {
    }
}

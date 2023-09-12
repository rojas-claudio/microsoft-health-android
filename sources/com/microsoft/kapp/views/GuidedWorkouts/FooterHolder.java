package com.microsoft.kapp.views.GuidedWorkouts;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.microsoft.kapp.R;
import com.unnamed.b.atv.model.TreeNode;
/* loaded from: classes.dex */
public class FooterHolder extends TreeNode.BaseNodeViewHolder<Integer> {
    public FooterHolder(Context context) {
        super(context);
    }

    @Override // com.unnamed.b.atv.model.TreeNode.BaseNodeViewHolder
    public View createNodeView(TreeNode node, ViewGroup container, Integer value) {
        View footerView = LayoutInflater.from(this.context).inflate(R.layout.guided_workout_next_fragment_bottom_view, (ViewGroup) null, false);
        return footerView;
    }
}

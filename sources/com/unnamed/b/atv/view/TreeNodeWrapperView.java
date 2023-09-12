package com.unnamed.b.atv.view;

import android.content.Context;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import com.unnamed.b.atv.R;
/* loaded from: classes.dex */
public class TreeNodeWrapperView extends LinearLayout {
    private final int containerStyle;
    private ViewGroup nodeContainer;
    private LinearLayout nodeItemsContainer;

    public TreeNodeWrapperView(Context context, int containerStyle) {
        super(context);
        this.containerStyle = containerStyle;
        init();
    }

    private void init() {
        setOrientation(1);
        this.nodeContainer = new RelativeLayout(getContext());
        this.nodeContainer.setLayoutParams(new LinearLayout.LayoutParams(-1, -2));
        this.nodeContainer.setId(R.id.node_header);
        ContextThemeWrapper newContext = new ContextThemeWrapper(getContext(), this.containerStyle);
        this.nodeItemsContainer = new LinearLayout(newContext, null, this.containerStyle);
        this.nodeItemsContainer.setLayoutParams(new LinearLayout.LayoutParams(-1, -2));
        this.nodeItemsContainer.setId(R.id.node_items);
        this.nodeItemsContainer.setOrientation(1);
        this.nodeItemsContainer.setVisibility(8);
        addView(this.nodeContainer);
        addView(this.nodeItemsContainer);
    }

    public void insertNodeView(View nodeView) {
        this.nodeContainer.addView(nodeView);
    }
}

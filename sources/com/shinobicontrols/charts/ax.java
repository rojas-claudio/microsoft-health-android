package com.shinobicontrols.charts;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import java.util.List;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class ax extends LinearLayout {
    private final float a;

    public ax(Context context) {
        super(context);
        this.a = getResources().getDisplayMetrics().density;
        setOrientation(0);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a(List<av> list, int i) {
        removeAllViews();
        if (i != 0) {
            int min = Math.min(list.size(), i);
            a(min);
            int i2 = 0;
            while (true) {
                int i3 = i2;
                if (i3 < list.size()) {
                    av avVar = list.get(i3);
                    View childAt = getChildAt(i3 % min);
                    if (childAt instanceof aw) {
                        ((aw) childAt).a(avVar);
                    }
                    i2 = i3 + 1;
                } else {
                    return;
                }
            }
        }
    }

    private void a(int i) {
        for (int i2 = 0; i2 < i; i2++) {
            aw awVar = new aw(getContext());
            awVar.setLayoutParams(new LinearLayout.LayoutParams(-2, -2));
            addView(awVar);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a(LegendStyle legendStyle) {
        int a = at.a(this.a, legendStyle.getSymbolLabelGap() / 2.0f);
        int i = 0;
        while (i < getChildCount()) {
            View childAt = getChildAt(i);
            if (childAt instanceof aw) {
                aw awVar = (aw) childAt;
                awVar.a(legendStyle);
                a(awVar, i > 0 ? a : 0, i < getChildCount() + (-1) ? a : 0);
            }
            i++;
        }
    }

    private void a(aw awVar, int i, int i2) {
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) awVar.getLayoutParams();
        layoutParams.leftMargin = i;
        layoutParams.rightMargin = i2;
    }
}

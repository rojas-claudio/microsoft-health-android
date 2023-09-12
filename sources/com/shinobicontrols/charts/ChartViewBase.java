package com.shinobicontrols.charts;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.widget.FrameLayout;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public abstract class ChartViewBase extends FrameLayout {
    private final v a;

    abstract v a(Context context, AttributeSet attributeSet, int i);

    /* JADX INFO: Access modifiers changed from: package-private */
    public ChartViewBase(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.a = a(getContext(), attrs, defStyle);
        addView(this.a);
    }

    public final ShinobiChart getShinobiChart() {
        return this.a;
    }

    public final void onCreate(Bundle savedInstanceState) {
        this.a.a(savedInstanceState);
    }

    public void onResume() {
        this.a.c();
    }

    public void onPause() {
        this.a.d();
    }

    public final void onDestroy() {
        this.a.e();
    }
}

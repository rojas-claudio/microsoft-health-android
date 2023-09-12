package com.google.android.gms.internal;

import android.app.Activity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
/* loaded from: classes.dex */
public final class bk extends FrameLayout implements View.OnClickListener {
    private final Activity fD;
    private final ImageButton gk;

    public bk(Activity activity, int i) {
        super(activity);
        this.fD = activity;
        setOnClickListener(this);
        this.gk = new ImageButton(activity);
        this.gk.setImageResource(17301527);
        this.gk.setBackgroundColor(0);
        this.gk.setOnClickListener(this);
        this.gk.setPadding(0, 0, 0, 0);
        int a = cm.a(activity, i);
        addView(this.gk, new FrameLayout.LayoutParams(a, a, 17));
    }

    public void d(boolean z) {
        this.gk.setVisibility(z ? 4 : 0);
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        this.fD.finish();
    }
}

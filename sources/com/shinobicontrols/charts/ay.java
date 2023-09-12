package com.shinobicontrols.charts;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;
/* loaded from: classes.dex */
class ay extends View {
    private final Drawable a;

    @SuppressLint({"NewApi", "ViewConstructor"})
    public ay(Context context, Drawable drawable) {
        super(context);
        this.a = drawable;
        if (Build.VERSION.SDK_INT >= 16) {
            setBackground(this.a);
        } else {
            setBackgroundDrawable(this.a);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Drawable a() {
        return this.a;
    }
}

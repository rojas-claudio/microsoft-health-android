package com.microsoft.kapp.style.view;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.view.View;
/* loaded from: classes.dex */
public class ViewStyleUtils {
    public static void setViewDrawableColor(View view, int drawableColorResId) {
        Drawable bgDrawable;
        if (view != null && drawableColorResId > 0 && (bgDrawable = view.getBackground()) != null) {
            Context context = view.getContext();
            int drawableColor = context.getResources().getColor(drawableColorResId);
            bgDrawable.setColorFilter(drawableColor, PorterDuff.Mode.SRC);
        }
    }
}

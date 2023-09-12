package com.microsoft.kapp.views;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;
/* loaded from: classes.dex */
public class AspectImageView extends ImageView {
    private static final double RATIO = 0.5625d;

    public AspectImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public int getEstimatedWidth(Activity context) {
        Display display = context.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size.x;
    }

    public int getEstimatedHeight(Activity context) {
        Display display = context.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return (int) (size.x * RATIO);
    }

    @Override // android.widget.ImageView, android.view.View
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = View.MeasureSpec.getSize(widthMeasureSpec);
        int height = (int) (width * RATIO);
        setMeasuredDimension(width, height);
    }
}

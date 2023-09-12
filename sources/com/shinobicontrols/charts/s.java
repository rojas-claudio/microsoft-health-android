package com.shinobicontrols.charts;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;
import com.shinobicontrols.charts.Annotation;
/* JADX INFO: Access modifiers changed from: package-private */
@SuppressLint({"ViewConstructor"})
/* loaded from: classes.dex */
public class s extends ViewGroup {
    final w a;
    private final Paint b;
    private Bitmap c;

    /* JADX INFO: Access modifiers changed from: package-private */
    public s(Context context, w wVar) {
        super(context);
        this.b = new Paint();
        setWillNotDraw(false);
        this.a = wVar;
    }

    @Override // android.view.View
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int size = View.MeasureSpec.getSize(widthMeasureSpec);
        int size2 = View.MeasureSpec.getSize(heightMeasureSpec);
        this.a.a.l.a(View.MeasureSpec.makeMeasureSpec(size, Integer.MIN_VALUE), View.MeasureSpec.makeMeasureSpec(size2, Integer.MIN_VALUE), Annotation.Position.BEHIND_DATA);
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        this.a.a.l.a(this.a.b.left, this.a.b.top, this.a.b.right, this.a.b.bottom, Annotation.Position.BEHIND_DATA);
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        this.b.setColor(this.a.a.getStyle().getPlotAreaBackgroundColor());
        canvas.drawRect(this.a.b, this.b);
        this.a.a(canvas);
        canvas.clipRect(this.a.b);
    }

    @Override // android.view.View
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (this.c != null) {
            canvas.drawBitmap(this.c, (Rect) null, this.a.b, this.b);
            this.c = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a(Bitmap bitmap) {
        this.c = bitmap;
    }
}

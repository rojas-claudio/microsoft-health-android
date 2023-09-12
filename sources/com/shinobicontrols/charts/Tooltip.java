package com.shinobicontrols.charts;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.shinobicontrols.charts.PropertyChangedEvent;
@SuppressLint({"ViewConstructor"})
/* loaded from: classes.dex */
public class Tooltip extends FrameLayout {
    final bz a;
    dh b;
    private View c;
    private CartesianSeries<?> d;
    private Data<?, ?> e;
    private final al f;
    private final GradientDrawable g;

    /* JADX INFO: Access modifiers changed from: package-private */
    public Tooltip(Context context) {
        super(context);
        this.a = new bz();
        this.f = new al();
        this.g = new GradientDrawable();
        this.c = new DefaultTooltipView(context);
        addView(this.c, new FrameLayout.LayoutParams(-2, -2, 17));
        setVisibility(8);
        this.b = dh.a(this);
    }

    public CartesianSeries<?> getTrackedSeries() {
        return this.d;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a(CartesianSeries<?> cartesianSeries) {
        this.d = cartesianSeries;
        this.b = dh.a(this);
    }

    public Data<?, ?> getCenter() {
        return this.e;
    }

    public void setCenter(Data<?, ?> center) {
        this.e = center;
        if (center == null || center.getX() == null || center.getY() == null) {
            throw new IllegalArgumentException(getContext().getString(R.string.TooltipNullXOrYInCenterPoint));
        }
        e();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a(CrosshairStyle crosshairStyle) {
        if (crosshairStyle != null && (this.c instanceof DefaultTooltipView)) {
            a(this, crosshairStyle);
            a(this.c, crosshairStyle, 0, 9);
        }
    }

    @Override // android.view.View
    public void forceLayout() {
        super.forceLayout();
        this.c.forceLayout();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a(CartesianSeries<?> cartesianSeries, DataPoint<?, ?> dataPoint, DataPoint<?, ?> dataPoint2, DataPoint<?, ?> dataPoint3) {
        setCenter(cartesianSeries.l.a(dataPoint, dataPoint2, dataPoint3, cartesianSeries.getChart().getCrosshair().b));
        ChartUtils.updateTooltipContent(this, cartesianSeries.l.b(dataPoint, dataPoint2, dataPoint3, cartesianSeries.getChart().getCrosshair().b));
    }

    private void e() {
        if (this.d != null) {
            if (this.e == null) {
                throw new IllegalStateException(getContext().getString(R.string.TooltipNullCenter));
            }
            this.a.b = Crosshair.a(this.e.getX(), this.d.getXAxis(), this.d);
            this.a.c = Crosshair.a(this.e.getY(), this.d.getYAxis(), this.d);
        }
    }

    public View getView() {
        return this.c;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a() {
        e();
    }

    private void a(View view, CrosshairStyle crosshairStyle, int i, int i2) {
        while (i < i2) {
            a((TextView) ((DefaultTooltipView) view).getChildAt(i), crosshairStyle);
            i++;
        }
    }

    private void a(TextView textView, CrosshairStyle crosshairStyle) {
        textView.setTextColor(crosshairStyle.getTooltipTextColor());
        textView.setTypeface(crosshairStyle.getTooltipTypeface());
        textView.setTextSize(2, crosshairStyle.getTooltipTextSize());
        textView.setBackgroundColor(crosshairStyle.getTooltipLabelBackgroundColor());
    }

    private void a(View view, CrosshairStyle crosshairStyle) {
        float f = view.getResources().getDisplayMetrics().density;
        int a = at.a(f, crosshairStyle.getTooltipPadding());
        view.setPadding(a, a, a, a);
        this.g.setColor(crosshairStyle.getTooltipBackgroundColor());
        this.g.setStroke(at.a(f, crosshairStyle.getTooltipBorderWidth()), crosshairStyle.getTooltipBorderColor());
        this.g.setCornerRadius(at.a(f, crosshairStyle.getTooltipCornerRadius()));
        a.a(view, this.g);
    }

    public void setView(View view) {
        if (view == null) {
            throw new IllegalArgumentException(getContext().getString(R.string.TooltipNullView));
        }
        removeView(this.c);
        this.c = view;
        addView(view);
        this.f.a(new PropertyChangedEvent());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public am a(PropertyChangedEvent.Handler handler) {
        return this.f.a(PropertyChangedEvent.a, handler);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void b() {
        setVisibility(8);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void c() {
        setVisibility(0);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void d() {
        setVisibility(8);
    }
}

package com.shinobicontrols.charts;

import android.content.Context;
import android.widget.RelativeLayout;
import android.widget.TextView;
/* loaded from: classes.dex */
public class DefaultTooltipView extends RelativeLayout {
    TextView a;
    TextView b;
    TextView c;
    TextView d;
    TextView e;
    TextView f;
    TextView g;
    TextView h;
    TextView i;

    public DefaultTooltipView(Context context) {
        super(context);
        a(context);
        b();
    }

    public final void setText(CharSequence text) {
        this.a.setText(text);
        a();
    }

    public final void setText(int resid) {
        this.a.setText(resid);
        a();
    }

    private void a() {
        for (int i = 1; i < 9; i++) {
            getChildAt(i).setVisibility(8);
        }
    }

    private void a(Context context) {
        this.a = new TextView(context);
        this.a.setId(1);
        this.b = new TextView(context);
        this.b.setId(2);
        this.c = new TextView(context);
        this.c.setId(3);
        this.d = new TextView(context);
        this.d.setId(4);
        this.e = new TextView(context);
        this.e.setId(5);
        this.f = new TextView(context);
        this.f.setId(6);
        this.g = new TextView(context);
        this.g.setId(7);
        this.h = new TextView(context);
        this.h.setId(8);
        this.i = new TextView(context);
        this.i.setId(9);
        addView(this.a);
        addView(this.b);
        addView(this.c);
        addView(this.d);
        addView(this.e);
        addView(this.f);
        addView(this.g);
        addView(this.h);
        addView(this.i);
        for (int i = 0; i < 5; i++) {
            ((TextView) getChildAt(i)).setGravity(5);
        }
        for (int i2 = 5; i2 < 9; i2++) {
            ((TextView) getChildAt(i2)).setGravity(3);
        }
        ((TextView) getChildAt(0)).setGravity(1);
    }

    private void b() {
        c();
        d();
        e();
        f();
        g();
    }

    private void c() {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) this.a.getLayoutParams();
        layoutParams.addRule(10);
        layoutParams.addRule(14);
    }

    private void d() {
        ((RelativeLayout.LayoutParams) this.b.getLayoutParams()).addRule(3, 1);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) this.f.getLayoutParams();
        layoutParams.addRule(3, 1);
        layoutParams.addRule(1, 2);
    }

    private void e() {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) this.c.getLayoutParams();
        layoutParams.addRule(3, 1);
        layoutParams.addRule(1, 6);
        RelativeLayout.LayoutParams layoutParams2 = (RelativeLayout.LayoutParams) this.g.getLayoutParams();
        layoutParams2.addRule(3, 1);
        layoutParams2.addRule(1, 3);
    }

    private void f() {
        ((RelativeLayout.LayoutParams) this.d.getLayoutParams()).addRule(3, 2);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) this.h.getLayoutParams();
        layoutParams.addRule(3, 6);
        layoutParams.addRule(1, 4);
    }

    private void g() {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) this.e.getLayoutParams();
        layoutParams.addRule(3, 3);
        layoutParams.addRule(1, 8);
        RelativeLayout.LayoutParams layoutParams2 = (RelativeLayout.LayoutParams) this.i.getLayoutParams();
        layoutParams2.addRule(3, 7);
        layoutParams2.addRule(1, 5);
    }
}

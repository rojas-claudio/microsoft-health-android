package com.shinobicontrols.charts;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.widget.LinearLayout;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes.dex */
public class Legend extends LinearLayout {
    public static final int ALL = -1;
    public static final int VARIABLE = -2;
    bg a;
    private final List<av> b;
    private final ax c;
    private Placement d;
    private Position e;
    private LegendStyle f;
    private final Title g;
    private int h;
    private au i;
    private int j;
    private final float k;

    /* loaded from: classes.dex */
    public enum Placement {
        INSIDE_PLOT_AREA,
        ON_PLOT_AREA_BORDER,
        OUTSIDE_PLOT_AREA
    }

    /* loaded from: classes.dex */
    public enum SymbolAlignment {
        LEFT,
        RIGHT
    }

    /* loaded from: classes.dex */
    public enum Position {
        BOTTOM_CENTER(81),
        BOTTOM_LEFT(83),
        BOTTOM_RIGHT(85),
        MIDDLE_LEFT(19),
        MIDDLE_RIGHT(21),
        TOP_CENTER(49),
        TOP_LEFT(51),
        TOP_RIGHT(53);
        
        private final int a;

        Position(int gravity) {
            this.a = gravity;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public int a() {
            return this.a;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Legend(Context context) {
        super(context);
        this.d = Placement.OUTSIDE_PLOT_AREA;
        this.e = Position.TOP_RIGHT;
        this.j = -2;
        this.b = new ArrayList();
        this.a = bg.a(this);
        this.k = getResources().getDisplayMetrics().density;
        setOrientation(1);
        this.g = a(context);
        this.c = b(context);
        addView(this.g);
        addView(this.c);
    }

    private Title a(Context context) {
        Title title = new Title(context);
        title.setVisibility(8);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-2, -2);
        layoutParams.gravity = 1;
        title.setLayoutParams(layoutParams);
        return title;
    }

    private ax b(Context context) {
        ax axVar = new ax(context);
        axVar.setLayoutParams(new LinearLayout.LayoutParams(-2, -2));
        return axVar;
    }

    public int getMaxSeriesPerRow() {
        return this.j;
    }

    public Placement getPlacement() {
        return this.d;
    }

    public Position getPosition() {
        return this.e;
    }

    public LegendStyle getStyle() {
        return this.f;
    }

    public String getTitle() {
        return this.g.getText().toString();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a() {
        this.b.clear();
        this.b.addAll(this.i.a(this.f));
        this.c.a(this.b, a(this.j, this.b.size()));
        super.setVisibility(d());
        b();
        invalidate();
        requestLayout();
    }

    private int a(int i, int i2) {
        if (i < 0) {
            if (i != -1 && this.e != Position.TOP_CENTER && this.e != Position.BOTTOM_CENTER) {
                return 1;
            }
            return i2;
        }
        return i;
    }

    private void b() {
        c();
        this.g.a(this.f.a());
        this.c.a(this.f);
        int a = at.a(this.k, this.f.getPadding());
        setPadding(a, a, a, a);
        a(this.f.getRowVerticalMargin());
    }

    @SuppressLint({"NewApi"})
    private void c() {
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setColor(this.f.getBackgroundColor());
        gradientDrawable.setStroke(at.a(this.k, this.f.getBorderWidth()), this.f.getBorderColor());
        gradientDrawable.setCornerRadius(this.f.getCornerRadius());
        if (Build.VERSION.SDK_INT >= 16) {
            setBackground(gradientDrawable);
        } else {
            setBackgroundDrawable(gradientDrawable);
        }
    }

    private void a(float f) {
        int a = at.a(this.k, f / 2.0f);
        if (this.c.getChildCount() > 0 && this.g != null && this.g.getVisibility() != 8) {
            ((LinearLayout.LayoutParams) this.g.getLayoutParams()).bottomMargin += a;
            ((LinearLayout.LayoutParams) this.c.getLayoutParams()).topMargin = a;
        }
    }

    private int d() {
        if (e()) {
            return 8;
        }
        return this.h;
    }

    private boolean e() {
        return this.b.isEmpty() && this.g.getVisibility() == 8;
    }

    public void setMaxSeriesPerRow(int maxSeriesPerRow) {
        this.j = maxSeriesPerRow;
        a();
    }

    public void setPlacement(Placement placement) {
        this.d = placement;
        this.a = bg.a(this);
        a();
    }

    public void setPosition(Position position) {
        this.e = position;
        this.a = bg.a(this);
        a();
    }

    public void setStyle(LegendStyle style) {
        this.f = style;
    }

    public void setTitle(String title) {
        this.g.setText(title);
        if (Axis.a(title)) {
            this.g.setVisibility(8);
        } else {
            this.g.setVisibility(0);
        }
        a();
    }

    @Override // android.view.View
    public void setVisibility(int visibility) {
        this.h = visibility;
        super.setVisibility(d());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a(au auVar) {
        this.i = auVar;
    }
}

package com.shinobicontrols.charts;
/* loaded from: classes.dex */
public final class GridStripeStyle {
    final dj<Integer> a = new dj<>(-3355444);
    final dj<Integer> b = new dj<>(-7829368);
    final dj<Boolean> c = new dj<>(false);

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a(GridStripeStyle gridStripeStyle) {
        if (gridStripeStyle != null) {
            this.a.b(Integer.valueOf(gridStripeStyle.getStripeColor()));
            this.b.b(Integer.valueOf(gridStripeStyle.getAlternateStripeColor()));
            this.c.b(Boolean.valueOf(gridStripeStyle.areGridStripesShown()));
        }
    }

    public int getStripeColor() {
        return this.a.a.intValue();
    }

    public void setStripeColor(int stripeColor) {
        this.a.a(Integer.valueOf(stripeColor));
    }

    public int getAlternateStripeColor() {
        return this.b.a.intValue();
    }

    public void setAlternateStripeColor(int alternateStripeColor) {
        this.b.a(Integer.valueOf(alternateStripeColor));
    }

    public boolean areGridStripesShown() {
        return this.c.a.booleanValue();
    }

    public void setGridStripesShown(boolean showGridStripes) {
        this.c.a(Boolean.valueOf(showGridStripes));
    }
}

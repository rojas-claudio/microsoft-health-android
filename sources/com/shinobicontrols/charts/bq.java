package com.shinobicontrols.charts;

import java.text.DecimalFormat;
/* loaded from: classes.dex */
class bq extends DecimalFormat {
    private static final long serialVersionUID = 5745069603170376668L;
    private String a;
    private int b;
    private int d;
    private double f;
    private int c = -1;
    private double e = -1.0d;

    /* JADX INFO: Access modifiers changed from: package-private */
    public String a() {
        if (this.e != this.f || this.c != this.d) {
            this.a = super.format(this.e);
            this.f = this.e;
            this.d = this.c;
        }
        return this.a;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a(int i) {
        if (i != this.b) {
            this.b = i;
            this.e = -Math.pow(10.0d, this.b);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean b(int i) {
        if (i == this.c) {
            return false;
        }
        this.c = i;
        super.setMinimumFractionDigits(this.c);
        super.setMaximumFractionDigits(this.c + 2);
        return true;
    }
}

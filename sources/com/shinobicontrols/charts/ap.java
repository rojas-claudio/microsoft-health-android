package com.shinobicontrols.charts;

import java.util.Comparator;
import java.util.Locale;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class ap {
    static final Comparator<ap> a = new Comparator<ap>() { // from class: com.shinobicontrols.charts.ap.1
        @Override // java.util.Comparator
        /* renamed from: a */
        public int compare(ap apVar, ap apVar2) {
            if (apVar.b < apVar2.b) {
                return -1;
            }
            if (apVar.b > apVar2.b) {
                return 1;
            }
            return 0;
        }
    };
    final double b;
    final bj c;

    /* JADX INFO: Access modifiers changed from: package-private */
    public ap(double d, bj bjVar) {
        this.b = d;
        this.c = bjVar;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        return (o instanceof ap) && ((ap) o).b == this.b;
    }

    public int hashCode() {
        long doubleToLongBits = Double.doubleToLongBits(this.b);
        return ((int) (doubleToLongBits ^ (doubleToLongBits >>> 32))) + 527;
    }

    public String toString() {
        return String.format(Locale.US, "%s, from %f", this.c.toString(), Double.valueOf(this.b));
    }
}

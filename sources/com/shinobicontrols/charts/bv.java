package com.shinobicontrols.charts;

import com.microsoft.kapp.utils.Constants;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
/* loaded from: classes.dex */
class bv {
    private final List<ap> a = new ArrayList();

    bv() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static bv a() {
        bv bvVar = new bv();
        ap apVar = new ap(Double.NEGATIVE_INFINITY, new bj(1.0d, Constants.SPLITS_ACCURACY));
        HashSet hashSet = new HashSet();
        hashSet.add(apVar);
        bvVar.a(hashSet);
        return bvVar;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public double a(double d) {
        ap b = b(d);
        if (b == null) {
            return Double.NaN;
        }
        return b.c.a(d);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ap b(double d) {
        if (this.a.isEmpty() || d < this.a.get(0).b) {
            return null;
        }
        for (int i = 0; i < this.a.size() - 1; i++) {
            ap apVar = this.a.get(i);
            ap apVar2 = this.a.get(i + 1);
            if (d >= apVar.b && d < apVar2.b) {
                return apVar;
            }
        }
        return this.a.get(this.a.size() - 1);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a(Set<ap> set) {
        this.a.clear();
        this.a.addAll(set);
        Collections.sort(this.a, ap.a);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        int i = 0;
        while (true) {
            int i2 = i;
            if (i2 < this.a.size()) {
                sb.append(this.a.get(i2).toString());
                if (i2 != this.a.size() - 1) {
                    sb.append("\n");
                }
                i = i2 + 1;
            } else {
                return sb.toString();
            }
        }
    }
}

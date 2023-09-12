package com.shinobicontrols.charts;

import com.microsoft.kapp.utils.Constants;
import java.lang.Comparable;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
/* loaded from: classes.dex */
class ck<T extends Comparable<T>> implements aq<T> {
    @Override // com.shinobicontrols.charts.aq
    public Set<ap> a(List<Range<T>> list) {
        HashSet hashSet = new HashSet();
        double d = Constants.SPLITS_ACCURACY;
        hashSet.add(new ap(Double.NEGATIVE_INFINITY, new bj(1.0d, Constants.SPLITS_ACCURACY)));
        if (list.isEmpty()) {
            return hashSet;
        }
        Iterator<Range<T>> it = list.iterator();
        while (true) {
            double d2 = d;
            if (!it.hasNext()) {
                return hashSet;
            }
            Range<T> next = it.next();
            hashSet.add(new ap(next.a - d2, new bj(1.0d, next.b() + d2)));
            d = d2 + next.b();
        }
    }
}

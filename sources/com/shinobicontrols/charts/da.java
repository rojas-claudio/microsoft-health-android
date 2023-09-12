package com.shinobicontrols.charts;

import java.lang.Comparable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class da<T extends Comparable<T>, U> {
    private final Axis<T, U> a;
    private final Comparator<Range<T>> b = (Comparator<Range<T>>) new Comparator<Range<T>>() { // from class: com.shinobicontrols.charts.da.1
        @Override // java.util.Comparator
        /* renamed from: a */
        public int compare(Range<T> range, Range<T> range2) {
            if (range.a < range2.a) {
                return -1;
            }
            if (range.a > range2.a) {
                return 1;
            }
            if (range.b >= range2.b) {
                return range.b > range2.b ? 1 : 0;
            }
            return -1;
        }
    };

    /* JADX INFO: Access modifiers changed from: package-private */
    public da(Axis<T, U> axis) {
        this.a = axis;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public List<Range<T>> a(List<Range<T>> list) {
        ArrayList arrayList = new ArrayList(list);
        Collections.sort(arrayList, this.b);
        return arrayList;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public List<Range<T>> b(List<Range<T>> list) {
        T t;
        Range<T> range;
        ArrayList arrayList = new ArrayList();
        int i = 0;
        while (i < list.size()) {
            Range<T> range2 = list.get(i);
            T minimum = range2.getMinimum();
            T maximum = range2.getMaximum();
            int i2 = i + 1;
            while (true) {
                t = maximum;
                range = range2;
                if (i2 >= list.size()) {
                    break;
                }
                Range<T> range3 = list.get(i2);
                if (!range.a(range3, true)) {
                    break;
                }
                range2 = a(range, range3);
                maximum = range2.getMaximum();
                i2++;
            }
            if (i2 - i != 1) {
                range = this.a.createRange(minimum, t);
            }
            arrayList.add(range);
            i = i2;
        }
        return arrayList;
    }

    private Range<T> a(Range<T> range, Range<T> range2) {
        return range.getMaximum().compareTo(range2.getMaximum()) >= 0 ? range : range2;
    }
}

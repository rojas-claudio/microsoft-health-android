package com.google.android.gms.internal;

import com.j256.ormlite.stmt.query.SimpleComparison;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
/* loaded from: classes.dex */
public final class dl {

    /* loaded from: classes.dex */
    public static final class a {
        private final List<String> lj;
        private final Object lk;

        private a(Object obj) {
            this.lk = dm.e(obj);
            this.lj = new ArrayList();
        }

        public a a(String str, Object obj) {
            this.lj.add(((String) dm.e(str)) + SimpleComparison.EQUAL_TO_OPERATION + String.valueOf(obj));
            return this;
        }

        public String toString() {
            StringBuilder append = new StringBuilder(100).append(this.lk.getClass().getSimpleName()).append('{');
            int size = this.lj.size();
            for (int i = 0; i < size; i++) {
                append.append(this.lj.get(i));
                if (i < size - 1) {
                    append.append(", ");
                }
            }
            return append.append('}').toString();
        }
    }

    public static a d(Object obj) {
        return new a(obj);
    }

    public static boolean equal(Object a2, Object b) {
        return a2 == b || (a2 != null && a2.equals(b));
    }

    public static int hashCode(Object... objects) {
        return Arrays.hashCode(objects);
    }
}

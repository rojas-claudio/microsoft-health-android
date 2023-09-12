package com.google.android.gms.internal;

import com.j256.ormlite.stmt.query.SimpleComparison;
import java.util.LinkedHashMap;
/* loaded from: classes.dex */
public class dq<K, V> {
    private final LinkedHashMap<K, V> lm;
    private int ln;
    private int lo;
    private int lp;
    private int lq;
    private int lr;
    private int ls;
    private int size;

    public dq(int i) {
        if (i <= 0) {
            throw new IllegalArgumentException("maxSize <= 0");
        }
        this.ln = i;
        this.lm = new LinkedHashMap<>(0, 0.75f, true);
    }

    private int b(K k, V v) {
        int sizeOf = sizeOf(k, v);
        if (sizeOf < 0) {
            throw new IllegalStateException("Negative size: " + k + SimpleComparison.EQUAL_TO_OPERATION + v);
        }
        return sizeOf;
    }

    protected V create(K key) {
        return null;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void entryRemoved(boolean evicted, K key, V oldValue, V newValue) {
    }

    public final void evictAll() {
        trimToSize(-1);
    }

    public final V get(K key) {
        V put;
        if (key == null) {
            throw new NullPointerException("key == null");
        }
        synchronized (this) {
            V v = this.lm.get(key);
            if (v != null) {
                this.lr++;
                return v;
            }
            this.ls++;
            V create = create(key);
            if (create == null) {
                return null;
            }
            synchronized (this) {
                this.lp++;
                put = this.lm.put(key, create);
                if (put != null) {
                    this.lm.put(key, put);
                } else {
                    this.size += b(key, create);
                }
            }
            if (put != null) {
                entryRemoved(false, key, create, put);
                return put;
            }
            trimToSize(this.ln);
            return create;
        }
    }

    public final V put(K key, V value) {
        V put;
        if (key == null || value == null) {
            throw new NullPointerException("key == null || value == null");
        }
        synchronized (this) {
            this.lo++;
            this.size += b(key, value);
            put = this.lm.put(key, value);
            if (put != null) {
                this.size -= b(key, put);
            }
        }
        if (put != null) {
            entryRemoved(false, key, put, value);
        }
        trimToSize(this.ln);
        return put;
    }

    public final synchronized int size() {
        return this.size;
    }

    protected int sizeOf(K key, V value) {
        return 1;
    }

    public final synchronized String toString() {
        String format;
        synchronized (this) {
            int i = this.lr + this.ls;
            format = String.format("LruCache[maxSize=%d,hits=%d,misses=%d,hitRate=%d%%]", Integer.valueOf(this.ln), Integer.valueOf(this.lr), Integer.valueOf(this.ls), Integer.valueOf(i != 0 ? (this.lr * 100) / i : 0));
        }
        return format;
    }

    /* JADX WARN: Code restructure failed: missing block: B:10:0x0032, code lost:
        throw new java.lang.IllegalStateException(getClass().getName() + ".sizeOf() is reporting inconsistent results!");
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void trimToSize(int r5) {
        /*
            r4 = this;
        L0:
            monitor-enter(r4)
            int r0 = r4.size     // Catch: java.lang.Throwable -> L33
            if (r0 < 0) goto L11
            java.util.LinkedHashMap<K, V> r0 = r4.lm     // Catch: java.lang.Throwable -> L33
            boolean r0 = r0.isEmpty()     // Catch: java.lang.Throwable -> L33
            if (r0 == 0) goto L36
            int r0 = r4.size     // Catch: java.lang.Throwable -> L33
            if (r0 == 0) goto L36
        L11:
            java.lang.IllegalStateException r0 = new java.lang.IllegalStateException     // Catch: java.lang.Throwable -> L33
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> L33
            r1.<init>()     // Catch: java.lang.Throwable -> L33
            java.lang.Class r2 = r4.getClass()     // Catch: java.lang.Throwable -> L33
            java.lang.String r2 = r2.getName()     // Catch: java.lang.Throwable -> L33
            java.lang.StringBuilder r1 = r1.append(r2)     // Catch: java.lang.Throwable -> L33
            java.lang.String r2 = ".sizeOf() is reporting inconsistent results!"
            java.lang.StringBuilder r1 = r1.append(r2)     // Catch: java.lang.Throwable -> L33
            java.lang.String r1 = r1.toString()     // Catch: java.lang.Throwable -> L33
            r0.<init>(r1)     // Catch: java.lang.Throwable -> L33
            throw r0     // Catch: java.lang.Throwable -> L33
        L33:
            r0 = move-exception
            monitor-exit(r4)     // Catch: java.lang.Throwable -> L33
            throw r0
        L36:
            int r0 = r4.size     // Catch: java.lang.Throwable -> L33
            if (r0 <= r5) goto L42
            java.util.LinkedHashMap<K, V> r0 = r4.lm     // Catch: java.lang.Throwable -> L33
            boolean r0 = r0.isEmpty()     // Catch: java.lang.Throwable -> L33
            if (r0 == 0) goto L44
        L42:
            monitor-exit(r4)     // Catch: java.lang.Throwable -> L33
            return
        L44:
            java.util.LinkedHashMap<K, V> r0 = r4.lm     // Catch: java.lang.Throwable -> L33
            java.util.Set r0 = r0.entrySet()     // Catch: java.lang.Throwable -> L33
            java.util.Iterator r0 = r0.iterator()     // Catch: java.lang.Throwable -> L33
            java.lang.Object r0 = r0.next()     // Catch: java.lang.Throwable -> L33
            java.util.Map$Entry r0 = (java.util.Map.Entry) r0     // Catch: java.lang.Throwable -> L33
            java.lang.Object r1 = r0.getKey()     // Catch: java.lang.Throwable -> L33
            java.lang.Object r0 = r0.getValue()     // Catch: java.lang.Throwable -> L33
            java.util.LinkedHashMap<K, V> r2 = r4.lm     // Catch: java.lang.Throwable -> L33
            r2.remove(r1)     // Catch: java.lang.Throwable -> L33
            int r2 = r4.size     // Catch: java.lang.Throwable -> L33
            int r3 = r4.b(r1, r0)     // Catch: java.lang.Throwable -> L33
            int r2 = r2 - r3
            r4.size = r2     // Catch: java.lang.Throwable -> L33
            int r2 = r4.lq     // Catch: java.lang.Throwable -> L33
            int r2 = r2 + 1
            r4.lq = r2     // Catch: java.lang.Throwable -> L33
            monitor-exit(r4)     // Catch: java.lang.Throwable -> L33
            r2 = 1
            r3 = 0
            r4.entryRemoved(r2, r1, r0, r3)
            goto L0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.dq.trimToSize(int):void");
    }
}

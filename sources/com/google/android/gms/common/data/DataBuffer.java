package com.google.android.gms.common.data;

import java.util.Iterator;
/* loaded from: classes.dex */
public abstract class DataBuffer<T> implements Iterable<T> {
    protected final d jf;

    /* JADX INFO: Access modifiers changed from: protected */
    public DataBuffer(d dataHolder) {
        this.jf = dataHolder;
        if (this.jf != null) {
            this.jf.b(this);
        }
    }

    public void close() {
        this.jf.close();
    }

    public int describeContents() {
        return 0;
    }

    public abstract T get(int i);

    public int getCount() {
        return this.jf.getCount();
    }

    public boolean isClosed() {
        return this.jf.isClosed();
    }

    @Override // java.lang.Iterable
    public Iterator<T> iterator() {
        return new a(this);
    }
}

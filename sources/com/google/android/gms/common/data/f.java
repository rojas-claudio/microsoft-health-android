package com.google.android.gms.common.data;

import java.util.ArrayList;
/* loaded from: classes.dex */
public abstract class f<T> extends DataBuffer<T> {
    private boolean jA;
    private ArrayList<Integer> jB;

    /* JADX INFO: Access modifiers changed from: protected */
    public f(d dVar) {
        super(dVar);
        this.jA = false;
    }

    private void aN() {
        synchronized (this) {
            if (!this.jA) {
                int count = this.jf.getCount();
                this.jB = new ArrayList<>();
                if (count > 0) {
                    this.jB.add(0);
                    String primaryDataMarkerColumn = getPrimaryDataMarkerColumn();
                    String c = this.jf.c(primaryDataMarkerColumn, 0, this.jf.q(0));
                    int i = 1;
                    while (i < count) {
                        String c2 = this.jf.c(primaryDataMarkerColumn, i, this.jf.q(i));
                        if (c2.equals(c)) {
                            c2 = c;
                        } else {
                            this.jB.add(Integer.valueOf(i));
                        }
                        i++;
                        c = c2;
                    }
                }
                this.jA = true;
            }
        }
    }

    private int u(int i) {
        if (i < 0 || i == this.jB.size()) {
            return 0;
        }
        return i == this.jB.size() + (-1) ? this.jf.getCount() - this.jB.get(i).intValue() : this.jB.get(i + 1).intValue() - this.jB.get(i).intValue();
    }

    protected abstract T a(int i, int i2);

    @Override // com.google.android.gms.common.data.DataBuffer
    public final T get(int position) {
        aN();
        return a(t(position), u(position));
    }

    @Override // com.google.android.gms.common.data.DataBuffer
    public int getCount() {
        aN();
        return this.jB.size();
    }

    protected abstract String getPrimaryDataMarkerColumn();

    int t(int i) {
        if (i < 0 || i >= this.jB.size()) {
            throw new IllegalArgumentException("Position " + i + " is out of bounds for this buffer");
        }
        return this.jB.get(i).intValue();
    }
}

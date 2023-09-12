package com.google.android.gms.common.data;

import android.database.CharArrayBuffer;
import android.net.Uri;
import com.google.android.gms.internal.dl;
import com.google.android.gms.internal.dm;
/* loaded from: classes.dex */
public abstract class b {
    protected final d jf;
    protected final int ji;
    private final int jj;

    public b(d dVar, int i) {
        this.jf = (d) dm.e(dVar);
        dm.k(i >= 0 && i < dVar.getCount());
        this.ji = i;
        this.jj = dVar.q(this.ji);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void a(String str, CharArrayBuffer charArrayBuffer) {
        this.jf.a(str, this.ji, this.jj, charArrayBuffer);
    }

    public boolean equals(Object obj) {
        if (obj instanceof b) {
            b bVar = (b) obj;
            return dl.equal(Integer.valueOf(bVar.ji), Integer.valueOf(this.ji)) && dl.equal(Integer.valueOf(bVar.jj), Integer.valueOf(this.jj)) && bVar.jf == this.jf;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean getBoolean(String column) {
        return this.jf.d(column, this.ji, this.jj);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public byte[] getByteArray(String column) {
        return this.jf.e(column, this.ji, this.jj);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public int getInteger(String column) {
        return this.jf.b(column, this.ji, this.jj);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public long getLong(String column) {
        return this.jf.a(column, this.ji, this.jj);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public String getString(String column) {
        return this.jf.c(column, this.ji, this.jj);
    }

    public int hashCode() {
        return dl.hashCode(Integer.valueOf(this.ji), Integer.valueOf(this.jj), this.jf);
    }

    public boolean isDataValid() {
        return !this.jf.isClosed();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public Uri u(String str) {
        return this.jf.f(str, this.ji, this.jj);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean v(String str) {
        return this.jf.g(str, this.ji, this.jj);
    }
}

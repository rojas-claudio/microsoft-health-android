package com.google.android.gms.internal;

import android.support.v4.media.TransportMediator;
import java.io.IOException;
/* loaded from: classes.dex */
public final class gl {
    private final byte[] buffer;
    private int position;
    private final int ux;

    /* loaded from: classes.dex */
    public static class a extends IOException {
        a(int i, int i2) {
            super("CodedOutputStream was writing to a flat byte array and ran out of space (pos " + i + " limit " + i2 + ").");
        }
    }

    private gl(byte[] bArr, int i, int i2) {
        this.buffer = bArr;
        this.position = i;
        this.ux = i + i2;
    }

    public static gl a(byte[] bArr, int i, int i2) {
        return new gl(bArr, i, i2);
    }

    public static gl g(byte[] bArr) {
        return a(bArr, 0, bArr.length);
    }

    public void aD(int i) throws IOException {
        b((byte) i);
    }

    public void aE(int i) throws IOException {
        while ((i & (-128)) != 0) {
            aD((i & TransportMediator.KEYCODE_MEDIA_PAUSE) | 128);
            i >>>= 7;
        }
        aD(i);
    }

    public void ab(String str) throws IOException {
        byte[] bytes = str.getBytes("UTF-8");
        aE(bytes.length);
        h(bytes);
    }

    public void b(byte b) throws IOException {
        if (this.position == this.ux) {
            throw new a(this.position, this.ux);
        }
        byte[] bArr = this.buffer;
        int i = this.position;
        this.position = i + 1;
        bArr[i] = b;
    }

    public void b(int i, long j) throws IOException {
        d(i, 0);
        i(j);
    }

    public void b(int i, String str) throws IOException {
        d(i, 2);
        ab(str);
    }

    public void b(byte[] bArr, int i, int i2) throws IOException {
        if (this.ux - this.position < i2) {
            throw new a(this.position, this.ux);
        }
        System.arraycopy(bArr, i, this.buffer, this.position, i2);
        this.position += i2;
    }

    public void d(int i, int i2) throws IOException {
        aE(gm.e(i, i2));
    }

    public int ec() {
        return this.ux - this.position;
    }

    public void h(byte[] bArr) throws IOException {
        b(bArr, 0, bArr.length);
    }

    public void i(long j) throws IOException {
        j(j);
    }

    public void j(long j) throws IOException {
        while (((-128) & j) != 0) {
            aD((((int) j) & TransportMediator.KEYCODE_MEDIA_PAUSE) | 128);
            j >>>= 7;
        }
        aD((int) j);
    }
}

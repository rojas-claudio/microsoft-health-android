package com.google.android.gms.internal;
/* loaded from: classes.dex */
public class gk {
    private final byte[] uu = new byte[256];
    private int uv;
    private int uw;

    public gk(byte[] bArr) {
        for (int i = 0; i < 256; i++) {
            this.uu[i] = (byte) i;
        }
        int i2 = 0;
        for (int i3 = 0; i3 < 256; i3++) {
            i2 = (i2 + this.uu[i3] + bArr[i3 % bArr.length]) & 255;
            byte b = this.uu[i3];
            this.uu[i3] = this.uu[i2];
            this.uu[i2] = b;
        }
        this.uv = 0;
        this.uw = 0;
    }

    public void f(byte[] bArr) {
        int i = this.uv;
        int i2 = this.uw;
        for (int i3 = 0; i3 < bArr.length; i3++) {
            i = (i + 1) & 255;
            i2 = (i2 + this.uu[i]) & 255;
            byte b = this.uu[i];
            this.uu[i] = this.uu[i2];
            this.uu[i2] = b;
            bArr[i3] = (byte) (bArr[i3] ^ this.uu[(this.uu[i] + this.uu[i2]) & 255]);
        }
        this.uv = i;
        this.uw = i2;
    }
}

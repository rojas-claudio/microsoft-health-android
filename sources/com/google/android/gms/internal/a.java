package com.google.android.gms.internal;

import android.util.Base64;
/* loaded from: classes.dex */
class a implements j {
    @Override // com.google.android.gms.internal.j
    public String a(byte[] bArr, boolean z) {
        return Base64.encodeToString(bArr, z ? 11 : 2);
    }

    @Override // com.google.android.gms.internal.j
    public byte[] a(String str, boolean z) throws IllegalArgumentException {
        return Base64.decode(str, z ? 11 : 2);
    }
}

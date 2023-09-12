package com.microsoft.kapp.logging.http;

import com.microsoft.kapp.diagnostics.Compatibility;
/* loaded from: classes.dex */
public class HttpTransactionFactory {
    public static HttpTransaction createTransaction() {
        return !Compatibility.isPublicRelease() ? new HttpTransactionImpl() : new MockHttpTransactionImpl();
    }
}

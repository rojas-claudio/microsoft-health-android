package com.facebook.internal;
/* loaded from: classes.dex */
public enum SessionAuthorizationType {
    READ,
    PUBLISH;

    /* renamed from: values  reason: to resolve conflict with enum method */
    public static SessionAuthorizationType[] valuesCustom() {
        SessionAuthorizationType[] valuesCustom = values();
        int length = valuesCustom.length;
        SessionAuthorizationType[] sessionAuthorizationTypeArr = new SessionAuthorizationType[length];
        System.arraycopy(valuesCustom, 0, sessionAuthorizationTypeArr, 0, length);
        return sessionAuthorizationTypeArr;
    }
}

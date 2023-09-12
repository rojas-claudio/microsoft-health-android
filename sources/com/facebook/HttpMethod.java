package com.facebook;
/* loaded from: classes.dex */
public enum HttpMethod {
    GET,
    POST,
    DELETE;

    /* renamed from: values  reason: to resolve conflict with enum method */
    public static HttpMethod[] valuesCustom() {
        HttpMethod[] valuesCustom = values();
        int length = valuesCustom.length;
        HttpMethod[] httpMethodArr = new HttpMethod[length];
        System.arraycopy(valuesCustom, 0, httpMethodArr, 0, length);
        return httpMethodArr;
    }
}

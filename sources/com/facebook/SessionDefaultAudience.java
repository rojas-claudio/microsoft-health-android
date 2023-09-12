package com.facebook;

import com.facebook.internal.NativeProtocol;
/* loaded from: classes.dex */
public enum SessionDefaultAudience {
    NONE(null),
    ONLY_ME(NativeProtocol.AUDIENCE_ME),
    FRIENDS(NativeProtocol.AUDIENCE_FRIENDS),
    EVERYONE(NativeProtocol.AUDIENCE_EVERYONE);
    
    private final String nativeProtocolAudience;

    /* renamed from: values  reason: to resolve conflict with enum method */
    public static SessionDefaultAudience[] valuesCustom() {
        SessionDefaultAudience[] valuesCustom = values();
        int length = valuesCustom.length;
        SessionDefaultAudience[] sessionDefaultAudienceArr = new SessionDefaultAudience[length];
        System.arraycopy(valuesCustom, 0, sessionDefaultAudienceArr, 0, length);
        return sessionDefaultAudienceArr;
    }

    SessionDefaultAudience(String protocol) {
        this.nativeProtocolAudience = protocol;
    }

    public String getNativeProtocolAudience() {
        return this.nativeProtocolAudience;
    }
}

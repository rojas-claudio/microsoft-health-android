package com.facebook;
/* loaded from: classes.dex */
public enum SessionLoginBehavior {
    SSO_WITH_FALLBACK(true, true),
    SSO_ONLY(true, false),
    SUPPRESS_SSO(false, true);
    
    private final boolean allowsKatanaAuth;
    private final boolean allowsWebViewAuth;

    /* renamed from: values  reason: to resolve conflict with enum method */
    public static SessionLoginBehavior[] valuesCustom() {
        SessionLoginBehavior[] valuesCustom = values();
        int length = valuesCustom.length;
        SessionLoginBehavior[] sessionLoginBehaviorArr = new SessionLoginBehavior[length];
        System.arraycopy(valuesCustom, 0, sessionLoginBehaviorArr, 0, length);
        return sessionLoginBehaviorArr;
    }

    SessionLoginBehavior(boolean allowsKatanaAuth, boolean allowsWebViewAuth) {
        this.allowsKatanaAuth = allowsKatanaAuth;
        this.allowsWebViewAuth = allowsWebViewAuth;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean allowsKatanaAuth() {
        return this.allowsKatanaAuth;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean allowsWebViewAuth() {
        return this.allowsWebViewAuth;
    }
}

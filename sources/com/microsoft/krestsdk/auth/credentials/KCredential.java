package com.microsoft.krestsdk.auth.credentials;
/* loaded from: classes.dex */
public class KCredential {
    private AccountMetadata mAccountMetadata;
    private KdsCredential mKdsCredential;
    private MsaCredential mMsaCredential;

    public KCredential(MsaCredential msaCredential, KdsCredential kdsCredential) {
        this.mMsaCredential = msaCredential;
        this.mKdsCredential = kdsCredential;
        this.mAccountMetadata = new AccountMetadata(kdsCredential.getUserId(), kdsCredential.getProfileCreationDate());
    }

    public MsaCredential getMsaCredential() {
        return this.mMsaCredential;
    }

    public KdsCredential getKdsCredential() {
        return this.mKdsCredential;
    }

    public AccountMetadata getAccountMetadata() {
        return this.mAccountMetadata;
    }

    public String getAccessToken() {
        return this.mKdsCredential != null ? this.mKdsCredential.getAccessToken() : "";
    }

    public boolean isTokenExpired() {
        return this.mMsaCredential == null || this.mKdsCredential == null || this.mMsaCredential.isTokenExpired() || this.mKdsCredential.isTokenExpired();
    }

    public String getEndPoint() {
        return this.mKdsCredential != null ? this.mKdsCredential.getEndPoint() : "";
    }

    public String getFUSEndPoint() {
        return this.mKdsCredential != null ? this.mKdsCredential.getFUSEndPoint() : "";
    }

    public String getHnFEndPoint() {
        return this.mKdsCredential != null ? this.mKdsCredential.getHnFEndPoint() : "";
    }

    public String getHnFQueryParameters() {
        return this.mKdsCredential != null ? this.mKdsCredential.getHnFQueryParameters() : "";
    }
}

package com.google.android.gms.wallet;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
/* loaded from: classes.dex */
public final class FullWallet implements SafeParcelable {
    public static final Parcelable.Creator<FullWallet> CREATOR = new c();
    private final int iM;
    String tH;
    String tI;
    ProxyCard tJ;
    String tK;
    Address tL;
    Address tM;
    String[] tN;

    public FullWallet() {
        this.iM = 1;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public FullWallet(int versionCode, String googleTransactionId, String merchantTransactionId, ProxyCard proxyCard, String email, Address billingAddress, Address shippingAddress, String[] paymentDescriptions) {
        this.iM = versionCode;
        this.tH = googleTransactionId;
        this.tI = merchantTransactionId;
        this.tJ = proxyCard;
        this.tK = email;
        this.tL = billingAddress;
        this.tM = shippingAddress;
        this.tN = paymentDescriptions;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public Address getBillingAddress() {
        return this.tL;
    }

    public String getEmail() {
        return this.tK;
    }

    public String getGoogleTransactionId() {
        return this.tH;
    }

    public String getMerchantTransactionId() {
        return this.tI;
    }

    public String[] getPaymentDescriptions() {
        return this.tN;
    }

    public ProxyCard getProxyCard() {
        return this.tJ;
    }

    public Address getShippingAddress() {
        return this.tM;
    }

    public int getVersionCode() {
        return this.iM;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        c.a(this, out, flags);
    }
}

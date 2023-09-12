package com.google.android.gms.wallet;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
/* loaded from: classes.dex */
public final class MaskedWallet implements SafeParcelable {
    public static final Parcelable.Creator<MaskedWallet> CREATOR = new g();
    private final int iM;
    String tH;
    String tI;
    String tK;
    Address tL;
    Address tM;
    String[] tN;
    LoyaltyWalletObject[] tZ;
    OfferWalletObject[] ua;

    public MaskedWallet() {
        this.iM = 2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public MaskedWallet(int versionCode, String googleTransactionId, String merchantTransactionId, String[] paymentDescriptions, String email, Address billingAddress, Address shippingAddress, LoyaltyWalletObject[] loyaltyWalletObjects, OfferWalletObject[] offerWalletObjects) {
        this.iM = versionCode;
        this.tH = googleTransactionId;
        this.tI = merchantTransactionId;
        this.tN = paymentDescriptions;
        this.tK = email;
        this.tL = billingAddress;
        this.tM = shippingAddress;
        this.tZ = loyaltyWalletObjects;
        this.ua = offerWalletObjects;
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

    public LoyaltyWalletObject[] getLoyaltyWalletObjects() {
        return this.tZ;
    }

    public String getMerchantTransactionId() {
        return this.tI;
    }

    public OfferWalletObject[] getOfferWalletObjects() {
        return this.ua;
    }

    public String[] getPaymentDescriptions() {
        return this.tN;
    }

    public Address getShippingAddress() {
        return this.tM;
    }

    public int getVersionCode() {
        return this.iM;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        g.a(this, dest, flags);
    }
}

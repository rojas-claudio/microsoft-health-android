package com.google.android.gms.wallet;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
/* loaded from: classes.dex */
public class MaskedWalletRequest implements SafeParcelable {
    public static final Parcelable.Creator<MaskedWalletRequest> CREATOR = new h();
    private final int iM;
    String tE;
    String tI;
    Cart tO;
    boolean ub;
    boolean uc;
    boolean ud;
    String ue;
    String uf;
    boolean ug;
    boolean uh;

    /* loaded from: classes.dex */
    public final class Builder {
        private Builder() {
        }

        public MaskedWalletRequest build() {
            return MaskedWalletRequest.this;
        }

        public Builder setCart(Cart cart) {
            MaskedWalletRequest.this.tO = cart;
            return this;
        }

        public Builder setCurrencyCode(String currencyCode) {
            MaskedWalletRequest.this.tE = currencyCode;
            return this;
        }

        public Builder setEstimatedTotalPrice(String estimatedTotalPrice) {
            MaskedWalletRequest.this.ue = estimatedTotalPrice;
            return this;
        }

        public Builder setIsBillingAgreement(boolean isBillingAgreement) {
            MaskedWalletRequest.this.uh = isBillingAgreement;
            return this;
        }

        public Builder setMerchantName(String merchantName) {
            MaskedWalletRequest.this.uf = merchantName;
            return this;
        }

        public Builder setMerchantTransactionId(String merchantTransactionId) {
            MaskedWalletRequest.this.tI = merchantTransactionId;
            return this;
        }

        public Builder setPhoneNumberRequired(boolean phoneNumberRequired) {
            MaskedWalletRequest.this.ub = phoneNumberRequired;
            return this;
        }

        public Builder setShippingAddressRequired(boolean shippingAddressRequired) {
            MaskedWalletRequest.this.uc = shippingAddressRequired;
            return this;
        }

        public Builder setShouldRetrieveWalletObjects(boolean shouldRetrieveWalletObjects) {
            MaskedWalletRequest.this.ug = shouldRetrieveWalletObjects;
            return this;
        }

        public Builder setUseMinimalBillingAddress(boolean useMinimalBillingAddress) {
            MaskedWalletRequest.this.ud = useMinimalBillingAddress;
            return this;
        }
    }

    public MaskedWalletRequest() {
        this.iM = 2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public MaskedWalletRequest(int versionCode, String merchantTransactionId, boolean phoneNumberRequired, boolean shippingAddressRequired, boolean useMinimalBillingAddress, String estimatedTotalPrice, String currencyCode, String merchantName, Cart cart, boolean shouldRetrieveWalletObjects, boolean isBillingAgreement) {
        this.iM = versionCode;
        this.tI = merchantTransactionId;
        this.ub = phoneNumberRequired;
        this.uc = shippingAddressRequired;
        this.ud = useMinimalBillingAddress;
        this.ue = estimatedTotalPrice;
        this.tE = currencyCode;
        this.uf = merchantName;
        this.tO = cart;
        this.ug = shouldRetrieveWalletObjects;
        this.uh = isBillingAgreement;
    }

    public static Builder newBuilder() {
        MaskedWalletRequest maskedWalletRequest = new MaskedWalletRequest();
        maskedWalletRequest.getClass();
        return new Builder();
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public Cart getCart() {
        return this.tO;
    }

    public String getCurrencyCode() {
        return this.tE;
    }

    public String getEstimatedTotalPrice() {
        return this.ue;
    }

    public String getMerchantName() {
        return this.uf;
    }

    public String getMerchantTransactionId() {
        return this.tI;
    }

    public int getVersionCode() {
        return this.iM;
    }

    public boolean isBillingAgreement() {
        return this.uh;
    }

    public boolean isPhoneNumberRequired() {
        return this.ub;
    }

    public boolean isShippingAddressRequired() {
        return this.uc;
    }

    public boolean shouldRetrieveWalletObjects() {
        return this.ug;
    }

    public boolean useMinimalBillingAddress() {
        return this.ud;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        h.a(this, dest, flags);
    }
}

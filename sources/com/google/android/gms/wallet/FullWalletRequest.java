package com.google.android.gms.wallet;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
/* loaded from: classes.dex */
public class FullWalletRequest implements SafeParcelable {
    public static final Parcelable.Creator<FullWalletRequest> CREATOR = new d();
    private final int iM;
    String tH;
    String tI;
    Cart tO;

    /* loaded from: classes.dex */
    public final class Builder {
        private Builder() {
        }

        public FullWalletRequest build() {
            return FullWalletRequest.this;
        }

        public Builder setCart(Cart cart) {
            FullWalletRequest.this.tO = cart;
            return this;
        }

        public Builder setGoogleTransactionId(String googleTransactionId) {
            FullWalletRequest.this.tH = googleTransactionId;
            return this;
        }

        public Builder setMerchantTransactionId(String merchantTransactionId) {
            FullWalletRequest.this.tI = merchantTransactionId;
            return this;
        }
    }

    public FullWalletRequest() {
        this.iM = 1;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public FullWalletRequest(int versionCode, String googleTransactionId, String merchantTransactionId, Cart cart) {
        this.iM = versionCode;
        this.tH = googleTransactionId;
        this.tI = merchantTransactionId;
        this.tO = cart;
    }

    public static Builder newBuilder() {
        FullWalletRequest fullWalletRequest = new FullWalletRequest();
        fullWalletRequest.getClass();
        return new Builder();
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public Cart getCart() {
        return this.tO;
    }

    public String getGoogleTransactionId() {
        return this.tH;
    }

    public String getMerchantTransactionId() {
        return this.tI;
    }

    public int getVersionCode() {
        return this.iM;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        d.a(this, dest, flags);
    }
}

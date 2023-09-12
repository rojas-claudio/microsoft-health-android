package com.google.android.gms.wallet;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes.dex */
public class Cart implements SafeParcelable {
    public static final Parcelable.Creator<Cart> CREATOR = new b();
    private final int iM;
    String tD;
    String tE;
    ArrayList<LineItem> tF;

    /* loaded from: classes.dex */
    public final class Builder {
        private Builder() {
        }

        public Builder addLineItem(LineItem lineItem) {
            Cart.this.tF.add(lineItem);
            return this;
        }

        public Cart build() {
            return Cart.this;
        }

        public Builder setCurrencyCode(String currencyCode) {
            Cart.this.tE = currencyCode;
            return this;
        }

        public Builder setLineItems(List<LineItem> lineItems) {
            Cart.this.tF.clear();
            Cart.this.tF.addAll(lineItems);
            return this;
        }

        public Builder setTotalPrice(String totalPrice) {
            Cart.this.tD = totalPrice;
            return this;
        }
    }

    public Cart() {
        this.iM = 1;
        this.tF = new ArrayList<>();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Cart(int versionCode, String totalPrice, String currencyCode, ArrayList<LineItem> lineItems) {
        this.iM = versionCode;
        this.tD = totalPrice;
        this.tE = currencyCode;
        this.tF = lineItems;
    }

    public static Builder newBuilder() {
        Cart cart = new Cart();
        cart.getClass();
        return new Builder();
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public String getCurrencyCode() {
        return this.tE;
    }

    public ArrayList<LineItem> getLineItems() {
        return this.tF;
    }

    public String getTotalPrice() {
        return this.tD;
    }

    public int getVersionCode() {
        return this.iM;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        b.a(this, dest, flags);
    }
}

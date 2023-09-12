package com.google.android.gms.wallet;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
/* loaded from: classes.dex */
public class LineItem implements SafeParcelable {
    public static final Parcelable.Creator<LineItem> CREATOR = new e();
    String description;
    private final int iM;
    String tD;
    String tE;
    String tQ;
    String tR;
    int tS;

    /* loaded from: classes.dex */
    public final class Builder {
        private Builder() {
        }

        public LineItem build() {
            return LineItem.this;
        }

        public Builder setCurrencyCode(String currencyCode) {
            LineItem.this.tE = currencyCode;
            return this;
        }

        public Builder setDescription(String description) {
            LineItem.this.description = description;
            return this;
        }

        public Builder setQuantity(String quantity) {
            LineItem.this.tQ = quantity;
            return this;
        }

        public Builder setRole(int role) {
            LineItem.this.tS = role;
            return this;
        }

        public Builder setTotalPrice(String totalPrice) {
            LineItem.this.tD = totalPrice;
            return this;
        }

        public Builder setUnitPrice(String unitPrice) {
            LineItem.this.tR = unitPrice;
            return this;
        }
    }

    /* loaded from: classes.dex */
    public interface Role {
        public static final int REGULAR = 0;
        public static final int SHIPPING = 2;
        public static final int TAX = 1;
    }

    public LineItem() {
        this.tS = 0;
        this.iM = 1;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public LineItem(int versionCode, String description, String quantity, String unitPrice, String totalPrice, int role, String currencyCode) {
        this.tS = 0;
        this.iM = versionCode;
        this.description = description;
        this.tQ = quantity;
        this.tR = unitPrice;
        this.tD = totalPrice;
        this.tS = role;
        this.tE = currencyCode;
    }

    public static Builder newBuilder() {
        LineItem lineItem = new LineItem();
        lineItem.getClass();
        return new Builder();
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public String getCurrencyCode() {
        return this.tE;
    }

    public String getDescription() {
        return this.description;
    }

    public String getQuantity() {
        return this.tQ;
    }

    public int getRole() {
        return this.tS;
    }

    public String getTotalPrice() {
        return this.tD;
    }

    public String getUnitPrice() {
        return this.tR;
    }

    public int getVersionCode() {
        return this.iM;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        e.a(this, dest, flags);
    }
}

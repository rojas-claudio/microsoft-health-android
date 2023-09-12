package com.google.android.gms.wallet;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.internal.dm;
/* loaded from: classes.dex */
public class NotifyTransactionStatusRequest implements SafeParcelable {
    public static final Parcelable.Creator<NotifyTransactionStatusRequest> CREATOR = new i();
    final int iM;
    int status;
    String tH;
    String uj;

    /* loaded from: classes.dex */
    public final class Builder {
        private Builder() {
        }

        public NotifyTransactionStatusRequest build() {
            boolean z = true;
            dm.b(!TextUtils.isEmpty(NotifyTransactionStatusRequest.this.tH), "googleTransactionId is required");
            if (NotifyTransactionStatusRequest.this.status < 1 || NotifyTransactionStatusRequest.this.status > 8) {
                z = false;
            }
            dm.b(z, "status is an unrecognized value");
            return NotifyTransactionStatusRequest.this;
        }

        public Builder setDetailedReason(String detailedReason) {
            NotifyTransactionStatusRequest.this.uj = detailedReason;
            return this;
        }

        public Builder setGoogleTransactionId(String googleTransactionId) {
            NotifyTransactionStatusRequest.this.tH = googleTransactionId;
            return this;
        }

        public Builder setStatus(int status) {
            NotifyTransactionStatusRequest.this.status = status;
            return this;
        }
    }

    /* loaded from: classes.dex */
    public interface Status {
        public static final int SUCCESS = 1;

        /* loaded from: classes.dex */
        public interface Error {
            public static final int AVS_DECLINE = 7;
            public static final int BAD_CARD = 4;
            public static final int BAD_CVC = 3;
            public static final int DECLINED = 5;
            public static final int FRAUD_DECLINE = 8;
            public static final int OTHER = 6;
            public static final int UNKNOWN = 2;
        }
    }

    public NotifyTransactionStatusRequest() {
        this.iM = 1;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public NotifyTransactionStatusRequest(int versionCode, String googleTransactionId, int status, String detailedReason) {
        this.iM = versionCode;
        this.tH = googleTransactionId;
        this.status = status;
        this.uj = detailedReason;
    }

    public static Builder newBuilder() {
        NotifyTransactionStatusRequest notifyTransactionStatusRequest = new NotifyTransactionStatusRequest();
        notifyTransactionStatusRequest.getClass();
        return new Builder();
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public String getDetailedReason() {
        return this.uj;
    }

    public String getGoogleTransactionId() {
        return this.tH;
    }

    public int getStatus() {
        return this.status;
    }

    public int getVersionCode() {
        return this.iM;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        i.a(this, out, flags);
    }
}

package com.microsoft.kapp.telephony;

import android.os.Parcel;
import android.os.Parcelable;
import com.microsoft.band.device.DeviceConstants;
import com.microsoft.kapp.ContactResolver;
import com.microsoft.kapp.diagnostics.Validate;
/* loaded from: classes.dex */
public abstract class IncomingCallContext implements Parcelable {
    private PhoneState mPhoneState;

    public abstract String getDisplayName();

    public abstract DeviceConstants.NotificationFlag getNotificationFlags();

    public abstract String getNumber();

    public abstract void resolveDisplayName(ContactResolver contactResolver);

    /* JADX INFO: Access modifiers changed from: protected */
    public IncomingCallContext() {
        this.mPhoneState = PhoneState.NONE;
        this.mPhoneState = PhoneState.RINGING;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public IncomingCallContext(Parcel in) {
        this.mPhoneState = PhoneState.NONE;
        Validate.notNull(in, "in");
        this.mPhoneState = PhoneState.valueOf(in.readInt());
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        Validate.notNull(dest, "dest");
        dest.writeInt(this.mPhoneState.value());
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public PhoneState getState() {
        return this.mPhoneState;
    }

    public void setState(PhoneState state) {
        this.mPhoneState = state;
    }
}

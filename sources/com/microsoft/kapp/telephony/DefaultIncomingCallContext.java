package com.microsoft.kapp.telephony;

import android.os.Parcel;
import android.os.Parcelable;
import com.microsoft.band.device.DeviceConstants;
import com.microsoft.kapp.ContactResolver;
import com.microsoft.kapp.diagnostics.Validate;
/* loaded from: classes.dex */
public class DefaultIncomingCallContext extends IncomingCallContext {
    public static final Parcelable.Creator<DefaultIncomingCallContext> CREATOR = new Parcelable.Creator<DefaultIncomingCallContext>() { // from class: com.microsoft.kapp.telephony.DefaultIncomingCallContext.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public DefaultIncomingCallContext createFromParcel(Parcel in) {
            return new DefaultIncomingCallContext(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public DefaultIncomingCallContext[] newArray(int size) {
            return new DefaultIncomingCallContext[size];
        }
    };
    private String mDisplayName;
    private boolean mIsDisplayNameResolved;
    private final String mNumber;

    public DefaultIncomingCallContext(String number) {
        Validate.notNullOrEmpty(number, "number");
        this.mNumber = number;
    }

    private DefaultIncomingCallContext(Parcel in) {
        super(in);
        Validate.notNull(in, "in");
        this.mNumber = in.readString();
        this.mDisplayName = in.readString();
        this.mIsDisplayNameResolved = in.readByte() == 1;
    }

    @Override // com.microsoft.kapp.telephony.IncomingCallContext, android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        Validate.notNull(dest, "dest");
        super.writeToParcel(dest, flags);
        dest.writeString(this.mNumber);
        dest.writeString(this.mDisplayName);
        dest.writeByte(this.mIsDisplayNameResolved ? (byte) 1 : (byte) 0);
    }

    @Override // com.microsoft.kapp.telephony.IncomingCallContext
    public String getNumber() {
        return this.mNumber;
    }

    @Override // com.microsoft.kapp.telephony.IncomingCallContext
    public String getDisplayName() {
        return this.mDisplayName;
    }

    @Override // com.microsoft.kapp.telephony.IncomingCallContext
    public void resolveDisplayName(ContactResolver resolver) {
        Validate.notNull(resolver, "resolver");
        if (!this.mIsDisplayNameResolved) {
            this.mDisplayName = resolver.resolveDisplayName(this.mNumber);
            this.mIsDisplayNameResolved = true;
        }
    }

    @Override // com.microsoft.kapp.telephony.IncomingCallContext
    public DeviceConstants.NotificationFlag getNotificationFlags() {
        return null;
    }
}

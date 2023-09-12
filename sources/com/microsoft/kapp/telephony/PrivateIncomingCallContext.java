package com.microsoft.kapp.telephony;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import com.microsoft.band.device.DeviceConstants;
import com.microsoft.kapp.ContactResolver;
import com.microsoft.kapp.R;
import com.microsoft.kapp.diagnostics.Validate;
import com.microsoft.kapp.utils.Constants;
/* loaded from: classes.dex */
public class PrivateIncomingCallContext extends IncomingCallContext {
    public static final Parcelable.Creator<PrivateIncomingCallContext> CREATOR = new Parcelable.Creator<PrivateIncomingCallContext>() { // from class: com.microsoft.kapp.telephony.PrivateIncomingCallContext.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public PrivateIncomingCallContext createFromParcel(Parcel in) {
            return new PrivateIncomingCallContext(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public PrivateIncomingCallContext[] newArray(int size) {
            return new PrivateIncomingCallContext[size];
        }
    };
    private final String mDisplayName;

    public PrivateIncomingCallContext(Context context) {
        Validate.notNull(context, Constants.FRE_INTENT_EXTRA_INFO);
        this.mDisplayName = context.getResources().getString(R.string.private_incoming_call_context_display_name);
    }

    protected PrivateIncomingCallContext(Parcel in) {
        super(in);
        this.mDisplayName = in.readString();
    }

    @Override // com.microsoft.kapp.telephony.IncomingCallContext, android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        Validate.notNull(dest, "dest");
        super.writeToParcel(dest, flags);
        dest.writeString(this.mDisplayName);
    }

    @Override // com.microsoft.kapp.telephony.IncomingCallContext
    public String getNumber() {
        return this.mDisplayName;
    }

    @Override // com.microsoft.kapp.telephony.IncomingCallContext
    public String getDisplayName() {
        return this.mDisplayName;
    }

    @Override // com.microsoft.kapp.telephony.IncomingCallContext
    public void resolveDisplayName(ContactResolver resolver) {
    }

    @Override // com.microsoft.kapp.telephony.IncomingCallContext
    public DeviceConstants.NotificationFlag getNotificationFlags() {
        return DeviceConstants.NotificationFlag.SUPPRESS_SMS_REPLY;
    }
}

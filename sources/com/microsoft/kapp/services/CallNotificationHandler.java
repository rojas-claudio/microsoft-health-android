package com.microsoft.kapp.services;

import android.content.Context;
import android.os.Bundle;
import com.microsoft.kapp.CargoConnection;
import com.microsoft.kapp.ContactResolver;
import com.microsoft.kapp.diagnostics.Validate;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.kapp.models.FreStatus;
import com.microsoft.kapp.telephony.IncomingCallContext;
import com.microsoft.kapp.utils.Constants;
import com.microsoft.kapp.utils.LogScenarioTags;
import javax.inject.Inject;
/* loaded from: classes.dex */
public class CallNotificationHandler extends InjectableNotificationHandler {
    @Inject
    CargoConnection mCargoConnection;
    @Inject
    ContactResolver mContactResolver;

    public CallNotificationHandler(Context context) {
        super(context);
    }

    @Override // com.microsoft.kapp.services.NotificationHandler
    public void handleNotification(Bundle bundle) {
        Validate.notNull(bundle, "bundle");
        IncomingCallContext context = (IncomingCallContext) bundle.getParcelable(Constants.NOTIFICATION_CONTEXT);
        if (context == null) {
            KLog.e(LogScenarioTags.PhoneCall, "IncomingCallContext is missing from bundle for call notification.");
        } else if (this.mContactResolver == null || this.mCargoConnection == null || this.mSettingsProvider == null) {
            KLog.e(LogScenarioTags.PhoneCall, "ContactResolver and/or CargoConnection is null. Make sure dependency injection is setup correctly.");
        } else if (this.mSettingsProvider.getFreStatus() == FreStatus.SHOWN) {
            context.resolveDisplayName(this.mContactResolver);
            try {
                KLog.d(LogScenarioTags.PhoneCall, "Sending call state change notification");
                this.mCargoConnection.sendCallStateChangeNotification(context);
            } catch (Exception e) {
                KLog.e(LogScenarioTags.PhoneCall, "Unexpected error when sending the calls state change notification to the device", e);
            }
        }
    }
}

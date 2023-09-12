package com.microsoft.kapp.services;

import android.content.Context;
import com.microsoft.kapp.models.strapp.DefaultStrappUUID;
import com.microsoft.kapp.telephony.KNotification;
/* loaded from: classes.dex */
public class FacebookNotificationHandler extends BaseNotificationHandlerImpl {
    public FacebookNotificationHandler(Context context) {
        super(context);
    }

    @Override // com.microsoft.kapp.services.BaseNotificationHandler
    public void sendNotification(KNotification message) {
        if (this.mSettingsProvider.getUUIDsOnDevice().contains(DefaultStrappUUID.STRAPP_FACEBOOK)) {
            this.mCargoConnection.sendNotificationToStrapp(message, DefaultStrappUUID.STRAPP_FACEBOOK);
        }
    }
}

package com.microsoft.kapp.services;

import android.content.Context;
import com.microsoft.kapp.models.strapp.DefaultStrappUUID;
import com.microsoft.kapp.telephony.KNotification;
import java.util.ArrayList;
/* loaded from: classes.dex */
public class NotificationCenterHandler extends BaseNotificationHandlerImpl {
    public NotificationCenterHandler(Context context) {
        super(context);
    }

    @Override // com.microsoft.kapp.services.BaseNotificationHandler
    public void sendNotification(KNotification message) {
        ArrayList<String> apps = this.mSettingsProvider.getNotificationCenterApps();
        if (this.mSettingsProvider.getUUIDsOnDevice().contains(DefaultStrappUUID.STRAPP_NOTIFICATION_CENTER)) {
            if (apps.size() == 0 || apps.contains(message.getPackageName())) {
                this.mCargoConnection.sendNotificationToStrapp(message, DefaultStrappUUID.STRAPP_NOTIFICATION_CENTER);
            }
        }
    }
}

package com.microsoft.kapp.services;

import android.content.Context;
import android.os.Bundle;
import com.microsoft.kapp.CargoConnection;
import com.microsoft.kapp.diagnostics.Validate;
import com.microsoft.kapp.telephony.KNotification;
import com.microsoft.kapp.utils.Constants;
import javax.inject.Inject;
/* loaded from: classes.dex */
public abstract class BaseNotificationHandlerImpl extends InjectableNotificationHandler implements BaseNotificationHandler {
    @Inject
    CargoConnection mCargoConnection;

    public BaseNotificationHandlerImpl(Context context) {
        super(context);
    }

    @Override // com.microsoft.kapp.services.NotificationHandler
    public void handleNotification(Bundle bundle) {
        Validate.notNull(bundle, "bundle");
        KNotification message = (KNotification) bundle.getParcelable(Constants.NOTIFICATION_MESSAGE);
        if (message != null) {
            sendNotification(message);
        }
    }
}

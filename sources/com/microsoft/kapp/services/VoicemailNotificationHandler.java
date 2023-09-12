package com.microsoft.kapp.services;

import android.content.Context;
import android.os.Bundle;
import com.microsoft.kapp.CargoConnection;
import com.microsoft.kapp.models.FreStatus;
import javax.inject.Inject;
/* loaded from: classes.dex */
public class VoicemailNotificationHandler extends InjectableNotificationHandler {
    @Inject
    CargoConnection mCargoConnection;

    public VoicemailNotificationHandler(Context context) {
        super(context);
    }

    @Override // com.microsoft.kapp.services.NotificationHandler
    public void handleNotification(Bundle bundle) {
        if (this.mCargoConnection == null || this.mSettingsProvider == null || this.mSettingsProvider.getFreStatus() == FreStatus.SHOWN) {
        }
        this.mCargoConnection.sendVoicemailNotification();
    }
}

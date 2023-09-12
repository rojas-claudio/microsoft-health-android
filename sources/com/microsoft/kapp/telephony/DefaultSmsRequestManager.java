package com.microsoft.kapp.telephony;

import android.telephony.SmsManager;
import com.microsoft.kapp.CargoConnection;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.kapp.tasks.SendSMSTask;
import javax.inject.Inject;
/* loaded from: classes.dex */
public class DefaultSmsRequestManager implements SmsRequestManager, SmsReplyRequestedListener, SendSMSTask.SmsDeliveryListener {
    private static final String TAG = DefaultSmsRequestManager.class.getSimpleName();

    @Inject
    public DefaultSmsRequestManager(CargoConnection cargoConnection) {
        if (cargoConnection != null) {
            cargoConnection.setSmsReplyRequestedListener(this);
        }
    }

    @Override // com.microsoft.kapp.tasks.SendSMSTask.SmsDeliveryListener
    public void onSmsDeliverySucceeded() {
        KLog.i(TAG, "SMS Successful");
    }

    @Override // com.microsoft.kapp.tasks.SendSMSTask.SmsDeliveryListener
    public void onSmsDeliveryFailed(Exception exception) {
        KLog.i(TAG, "SMS Failed");
    }

    @Override // com.microsoft.kapp.telephony.SmsReplyRequestedListener
    public void onSmsReplyRequested(String textMessage, String receiverAddress) {
        SmsManager smsManager = SmsManager.getDefault();
        SendSMSTask sendSMSTask = new SendSMSTask(smsManager, receiverAddress, textMessage);
        sendSMSTask.execute(new Void[0]);
    }
}

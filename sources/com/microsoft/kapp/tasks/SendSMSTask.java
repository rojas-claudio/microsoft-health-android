package com.microsoft.kapp.tasks;

import android.os.AsyncTask;
import android.telephony.SmsManager;
import android.util.Log;
/* loaded from: classes.dex */
public class SendSMSTask extends AsyncTask<Void, Void, Void> {
    private static final String TAG = SendSMSTask.class.getSimpleName();
    private Exception mExceptionWhileSending;
    private String mReceiverAddress;
    private SmsDeliveryListener mSmsDeliveryListener;
    private SmsManager mSmsManager;
    private String mTextMessage;

    /* loaded from: classes.dex */
    public interface SmsDeliveryListener {
        void onSmsDeliveryFailed(Exception exc);

        void onSmsDeliverySucceeded();
    }

    public SendSMSTask(SmsManager smsManager, String receiverAddress, String textMessage) {
        this.mSmsManager = smsManager;
        this.mReceiverAddress = receiverAddress;
        this.mTextMessage = textMessage;
    }

    public void setOnSmsRequestListener(SmsDeliveryListener onDeliveryListener) {
        this.mSmsDeliveryListener = onDeliveryListener;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.os.AsyncTask
    public Void doInBackground(Void... params) {
        try {
            this.mSmsManager.sendTextMessage(this.mReceiverAddress, null, this.mTextMessage, null, null);
        } catch (Exception e) {
            Log.e(TAG, "The receiver's address or message aren't valid");
            this.mExceptionWhileSending = e;
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.os.AsyncTask
    public void onPostExecute(Void result) {
        super.onPostExecute((SendSMSTask) result);
        if (this.mSmsDeliveryListener != null) {
            if (this.mExceptionWhileSending == null) {
                this.mSmsDeliveryListener.onSmsDeliverySucceeded();
            } else {
                this.mSmsDeliveryListener.onSmsDeliveryFailed(this.mExceptionWhileSending);
            }
        }
    }
}

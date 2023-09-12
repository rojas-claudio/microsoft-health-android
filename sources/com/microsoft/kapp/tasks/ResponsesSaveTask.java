package com.microsoft.kapp.tasks;

import android.os.AsyncTask;
import com.microsoft.band.device.command.SmsResponseType;
import com.microsoft.kapp.CargoConnection;
import org.apache.commons.lang3.Validate;
/* loaded from: classes.dex */
public class ResponsesSaveTask extends AsyncTask<Void, Void, Boolean> {
    ResponsesSaveCallback mCallback;
    CargoConnection mCargoConnection;
    String mMessage1;
    String mMessage2;
    String mMessage3;
    String mMessage4;
    SmsResponseType mSmsResponseType;

    /* loaded from: classes.dex */
    public interface ResponsesSaveCallback {
        void onResponsesSaveResult(boolean z, String str, String str2, String str3, String str4);
    }

    public ResponsesSaveTask(ResponsesSaveCallback callback, CargoConnection cargoConnection, SmsResponseType smsResponseType, String message1, String message2, String message3, String message4) {
        Validate.notNull(callback, "callback", new Object[0]);
        Validate.notNull(cargoConnection, "cargoConnection", new Object[0]);
        Validate.notNull(smsResponseType, "smsResponseType", new Object[0]);
        Validate.notNull(message1, "message1", new Object[0]);
        Validate.notNull(message2, "message2", new Object[0]);
        Validate.notNull(message3, "message3", new Object[0]);
        Validate.notNull(message4, "message4", new Object[0]);
        this.mCallback = callback;
        this.mCargoConnection = cargoConnection;
        this.mSmsResponseType = smsResponseType;
        this.mMessage1 = message1;
        this.mMessage2 = message2;
        this.mMessage3 = message3;
        this.mMessage4 = message4;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.os.AsyncTask
    public Boolean doInBackground(Void... params) {
        boolean result = false;
        try {
            switch (this.mSmsResponseType) {
                case CALL:
                    result = this.mCargoConnection.savePhoneCallResponses(this.mMessage1, this.mMessage2, this.mMessage3, this.mMessage4);
                    break;
                case SMS:
                    result = this.mCargoConnection.saveSmsResponses(this.mMessage1, this.mMessage2, this.mMessage3, this.mMessage4);
                    break;
            }
        } catch (IllegalArgumentException e) {
        }
        return Boolean.valueOf(result);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.os.AsyncTask
    public void onPostExecute(Boolean result) {
        this.mCallback.onResponsesSaveResult(result.booleanValue(), this.mMessage1, this.mMessage2, this.mMessage3, this.mMessage4);
    }
}

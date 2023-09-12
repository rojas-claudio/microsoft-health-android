package com.microsoft.kapp.tasks;

import android.os.AsyncTask;
import com.microsoft.kapp.CargoConnection;
import java.util.UUID;
import org.apache.commons.lang3.Validate;
/* loaded from: classes.dex */
public class SetStrappNotificationsEnabledTask extends AsyncTask<Void, Void, Boolean> {
    SetStrappNotificationsEnabledCallback mCallback;
    CargoConnection mCargoConnection;
    boolean mNotificationsEnabled;
    UUID mStrappId;

    /* loaded from: classes.dex */
    public interface SetStrappNotificationsEnabledCallback {
        void onSetStrappNotificationsEnabledResult(boolean z, boolean z2);
    }

    public SetStrappNotificationsEnabledTask(SetStrappNotificationsEnabledCallback callback, CargoConnection cargoConnection, UUID strappId, boolean notificationsEnabled) {
        Validate.notNull(callback, "callback", new Object[0]);
        Validate.notNull(cargoConnection, "cargoConnection", new Object[0]);
        Validate.notNull(strappId, "strappId", new Object[0]);
        this.mCallback = callback;
        this.mCargoConnection = cargoConnection;
        this.mStrappId = strappId;
        this.mNotificationsEnabled = notificationsEnabled;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.os.AsyncTask
    public Boolean doInBackground(Void... params) {
        try {
            return Boolean.valueOf(this.mCargoConnection.setNotificationsEnabled(this.mStrappId, this.mNotificationsEnabled));
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.os.AsyncTask
    public void onPostExecute(Boolean result) {
        this.mCallback.onSetStrappNotificationsEnabledResult(result.booleanValue(), this.mNotificationsEnabled);
    }
}

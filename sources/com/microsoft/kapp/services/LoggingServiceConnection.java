package com.microsoft.kapp.services;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
import com.microsoft.kapp.logging.KLog;
/* loaded from: classes.dex */
public class LoggingServiceConnection implements ServiceConnection {
    private static final String TAG = LoggingServiceConnection.class.getSimpleName();

    @Override // android.content.ServiceConnection
    public void onServiceConnected(ComponentName name, IBinder service) {
        if (name != null) {
            KLog.i(TAG, "%s connected.", name.getShortClassName());
        }
    }

    @Override // android.content.ServiceConnection
    public void onServiceDisconnected(ComponentName name) {
        if (name != null) {
            KLog.i(TAG, "%s disconnected.", name.getShortClassName());
        }
    }
}

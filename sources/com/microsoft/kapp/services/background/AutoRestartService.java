package com.microsoft.kapp.services.background;

import android.app.Service;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Binder;
import android.os.IBinder;
import android.telephony.TelephonyManager;
import com.microsoft.kapp.KApplication;
import com.microsoft.kapp.calendar.CalendarManager;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.kapp.telephony.MessagesManager;
import com.microsoft.kapp.telephony.PhoneStateListener;
import com.microsoft.kapp.telephony.SmsRequestManager;
import javax.inject.Inject;
/* loaded from: classes.dex */
public class AutoRestartService extends Service {
    private static final String TAG = AutoRestartService.class.getSimpleName();
    private final IBinder mBinder = new AutoRestartSerivceBinder();
    @Inject
    CalendarManager mCalendarManager;
    @Inject
    MessagesManager mMessagesManager;
    @Inject
    SmsRequestManager mSmsRequestManager;

    /* loaded from: classes.dex */
    public class AutoRestartSerivceBinder extends Binder {
        public AutoRestartSerivceBinder() {
        }

        public AutoRestartService getService() {
            return AutoRestartService.this;
        }
    }

    @Override // android.app.Service
    public void onCreate() {
        KLog.i(TAG, "onCreate");
        super.onCreate();
        ((KApplication) getApplicationContext()).inject(this);
        TelephonyManager manager = (TelephonyManager) getSystemService("phone");
        manager.listen(new PhoneStateListener(this), 36);
    }

    @Override // android.app.Service
    public int onStartCommand(Intent intent, int flags, int startId) {
        KLog.i(TAG, "onStartCommand");
        return 1;
    }

    @Override // android.app.Service
    public void onDestroy() {
        KLog.i(TAG, "onDestroy");
        super.onDestroy();
    }

    @Override // android.app.Service, android.content.ComponentCallbacks
    public void onLowMemory() {
        KLog.i(TAG, "onLowMemory");
        super.onLowMemory();
    }

    @Override // android.app.Service
    public void onRebind(Intent intent) {
        KLog.i(TAG, "onRebind");
        super.onRebind(intent);
    }

    @Override // android.app.Service, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration newConfig) {
        KLog.i(TAG, "onConfigurationChanged");
        super.onConfigurationChanged(newConfig);
    }

    @Override // android.app.Service
    public void onTaskRemoved(Intent rootIntent) {
        KLog.i(TAG, "onTaskRemoved");
        super.onTaskRemoved(rootIntent);
    }

    @Override // android.app.Service, android.content.ComponentCallbacks2
    public void onTrimMemory(int level) {
        KLog.i(TAG, "onTrimMemory");
        super.onTrimMemory(level);
    }

    @Override // android.app.Service
    public boolean onUnbind(Intent intent) {
        KLog.i(TAG, "onUnbind");
        return super.onUnbind(intent);
    }

    @Override // android.app.Service
    public IBinder onBind(Intent intent) {
        KLog.i(TAG, "onBind");
        return this.mBinder;
    }
}

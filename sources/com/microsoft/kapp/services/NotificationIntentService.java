package com.microsoft.kapp.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.kapp.utils.Constants;
import java.util.HashMap;
/* loaded from: classes.dex */
public class NotificationIntentService extends IntentService {
    private HashMap<String, NotificationHandler> mHandlerMappings;
    private volatile boolean mInitialized;
    private static final String TAG = NotificationIntentService.class.getSimpleName();
    private static final String SERVICE_NAME = null;

    public NotificationIntentService() {
        super(SERVICE_NAME);
        this.mHandlerMappings = new HashMap<>();
    }

    @Override // android.app.IntentService, android.app.Service
    public void onCreate() {
        super.onCreate();
        if (!this.mInitialized) {
            synchronized (NotificationIntentService.class) {
                if (!this.mInitialized) {
                    Context context = getApplicationContext();
                    this.mHandlerMappings.put(Constants.NOTIFICATION_ACTION_CALL, new CallNotificationHandler(context));
                    this.mHandlerMappings.put(Constants.NOTIFICATION_ACTION_MESSAGE, new MessageNotificationHandler(context));
                    this.mHandlerMappings.put(Constants.NOTIFICATION_CALENDAR_SYNC, new CalendarSyncNotificationHandler(context));
                    this.mHandlerMappings.put(Constants.NOTIFICATION_ACTION_FACEBOOK, new FacebookNotificationHandler(context));
                    this.mHandlerMappings.put(Constants.NOTIFICATION_ACTION_TWITTER, new TwitterNotificationHandler(context));
                    this.mHandlerMappings.put(Constants.NOTIFICATION_ACTION_EMAIL, new EmailNotificationHandler(context));
                    this.mHandlerMappings.put(Constants.NOTIFICATION_ACTION_VOICEMAIL, new VoicemailNotificationHandler(context));
                    this.mHandlerMappings.put(Constants.NOTIFICATION_ACTION_NOTIFICATION_CENTER, new NotificationCenterHandler(context));
                    this.mHandlerMappings.put(Constants.NOTIFICATION_ACTION_FACEBOOK_MESSENGER, new FacebookMessengerNotificationHandler(context));
                    this.mInitialized = true;
                }
            }
        }
    }

    @Override // android.app.IntentService
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            String action = intent.getAction();
            NotificationHandler handler = this.mHandlerMappings.get(action);
            if (handler != null) {
                Bundle bundle = intent.getExtras();
                try {
                    handler.handleNotification(bundle);
                    return;
                } catch (Exception ex) {
                    String message = String.format("Handler for action '%s' has returned error", action);
                    KLog.e(TAG, message, ex);
                    return;
                }
            }
            String message2 = String.format("Handler for action '%s' does not exist", action);
            KLog.w(TAG, message2);
        }
    }
}

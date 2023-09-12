package com.microsoft.kapp.services.background;

import android.annotation.TargetApi;
import android.content.Intent;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.kapp.services.NotificationIntentService;
import com.microsoft.kapp.telephony.KNotification;
import com.microsoft.kapp.utils.Constants;
import com.microsoft.kapp.utils.NotificationFilter;
import com.microsoft.kapp.utils.StrappConstants;
@TargetApi(18)
/* loaded from: classes.dex */
public class NotificationRecieverService extends NotificationListenerService {
    public static final String TAG = NotificationRecieverService.class.getSimpleName();

    @Override // android.service.notification.NotificationListenerService
    public void onNotificationPosted(StatusBarNotification sbn) {
        String packageName = sbn.getPackageName();
        KLog.logPrivate(TAG, "StatusBarNotification sbn = %s", sbn.toString());
        if (StrappConstants.NOTIFICATION_SERVICE_TWITTER.equals(packageName)) {
            startIntentWithNotification(Constants.NOTIFICATION_ACTION_TWITTER, new KNotification(sbn));
        } else if (StrappConstants.NOTIFICATION_SERVICE_FACEBOOK.equals(packageName)) {
            startIntentWithNotification(Constants.NOTIFICATION_ACTION_FACEBOOK, new KNotification(sbn));
        } else if (StrappConstants.NOTIFICATION_SERVICE_FACEBOOK_MESSAGER.equals(packageName)) {
            startIntentWithNotification(Constants.NOTIFICATION_ACTION_FACEBOOK_MESSENGER, new KNotification(sbn));
        } else if (StrappConstants.isMailClient(packageName)) {
            startIntentWithNotification(Constants.NOTIFICATION_ACTION_EMAIL, new KNotification(sbn));
        } else {
            startIntentWithNotification(Constants.NOTIFICATION_ACTION_NOTIFICATION_CENTER, new KNotification(sbn));
        }
    }

    public void startIntentWithNotification(String action, KNotification notification) {
        if (NotificationFilter.isValidNotification(getResources(), notification)) {
            Intent intent = new Intent(getBaseContext(), NotificationIntentService.class);
            intent.setAction(action);
            intent.putExtra(Constants.NOTIFICATION_MESSAGE, notification);
            getBaseContext().startService(intent);
        }
    }

    @Override // android.service.notification.NotificationListenerService
    public void onNotificationRemoved(StatusBarNotification sbn) {
        KLog.d(TAG, sbn.toString());
    }
}

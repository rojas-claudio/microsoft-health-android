package com.microsoft.kapp.utils;

import android.content.res.Resources;
import com.microsoft.kapp.R;
import com.microsoft.kapp.telephony.KNotification;
/* loaded from: classes.dex */
public class NotificationFilter {
    private static boolean isValidTwitterNotification(Resources resources, KNotification notification) {
        return (notification.getTitle().equals(resources.getString(R.string.notification_validation_twitter_sending)) || notification.getTitle().equals(resources.getString(R.string.notification_validation_twitter_sent))) ? false : true;
    }

    private static boolean isValidFacebookMessenger(Resources resources, KNotification notification) {
        return !notification.getTitle().equals(resources.getString(R.string.notification_validation_facebook_messenger_chat_heads));
    }

    private static boolean isValidHtcEmail(Resources resources, KNotification notification) {
        return (notification.getTitle().equals(resources.getString(R.string.notification_validation_htc_title)) && (notification.getSubtitle().equals(resources.getString(R.string.notification_validation_htc_saving_draft)) || notification.getSubtitle().equals(resources.getString(R.string.notification_validation_htc_sending_mail)))) ? false : true;
    }

    public static boolean isValidNotification(Resources resources, KNotification notification) {
        if (notification.getTitle().trim().isEmpty() && notification.getSubtitle().trim().isEmpty()) {
            return false;
        }
        if (StrappConstants.NOTIFICATION_SERVICE_TWITTER.equals(notification.getPackageName())) {
            return isValidTwitterNotification(resources, notification);
        }
        if (StrappConstants.NOTIFICATION_SERVICE_HTC_EMAIL.equals(notification.getPackageName())) {
            return isValidHtcEmail(resources, notification);
        }
        if (StrappConstants.NOTIFICATION_SERVICE_FACEBOOK_MESSAGER.equals(notification.getPackageName())) {
            return isValidFacebookMessenger(resources, notification);
        }
        return true;
    }
}

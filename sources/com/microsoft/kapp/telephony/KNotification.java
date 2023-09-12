package com.microsoft.kapp.telephony;

import android.annotation.TargetApi;
import android.app.Notification;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.service.notification.StatusBarNotification;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.kapp.style.utils.MetricSpannerUtils;
import com.microsoft.kapp.utils.StrappConstants;
import java.lang.reflect.Field;
@TargetApi(18)
/* loaded from: classes.dex */
public class KNotification implements Parcelable {
    private static final int INDEX_PACKAGE_NAME = 2;
    private static final int INDEX_SUBTITLE = 1;
    private static final int INDEX_TITLE = 0;
    private static final int PARCEL_LENGTH = 3;
    private static final String SUBTITLE_KEY = "android.text";
    private static final int SUBTITLE_TOTAL_ALLOWED_LENGTH = 160;
    private static final String TEXT_LINES_KEY = "android.textLines";
    private static final String TITLE_KEY = "android.title";
    private static final int TITLE_TOTAL_ALLOWED_LENGTH = 40;
    private String mPackageName;
    private String mSubtitle;
    private String mTitle;
    private static final String TAG = KNotification.class.getSimpleName();
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() { // from class: com.microsoft.kapp.telephony.KNotification.1
        @Override // android.os.Parcelable.Creator
        public KNotification createFromParcel(Parcel in) {
            return new KNotification(in);
        }

        @Override // android.os.Parcelable.Creator
        public KNotification[] newArray(int size) {
            return new KNotification[size];
        }
    };

    public void populateNotificationApi18(Notification incomingNotification) {
        Bundle bundle = getReflectedExtras(incomingNotification, "extras");
        if (bundle != null) {
            populateDataFromBundle(bundle);
        }
        if (this.mTitle == null) {
            this.mTitle = getReflectedField(incomingNotification, "titleText").toString();
        }
        if (this.mTitle == null) {
            this.mTitle = incomingNotification.tickerText.toString();
        }
        if (this.mSubtitle == null) {
            this.mSubtitle = getReflectedField(incomingNotification, "contentText").toString();
        }
        formatText();
    }

    private void formatText() {
        this.mTitle = (this.mTitle == null || this.mTitle.trim().equals("null")) ? MetricSpannerUtils.METRIC_UNITS_SEPARATOR_SPACE : this.mTitle;
        this.mSubtitle = (this.mSubtitle == null || this.mSubtitle.trim().equals("null")) ? MetricSpannerUtils.METRIC_UNITS_SEPARATOR_SPACE : this.mSubtitle;
        if (this.mSubtitle.length() > 159) {
            this.mSubtitle = this.mSubtitle.substring(0, 159);
        }
        if (this.mTitle.length() > 39) {
            this.mTitle = this.mTitle.substring(0, 39);
        }
        if (this.mPackageName.equals(StrappConstants.NOTIFICATION_SERVICE_FACEBOOK)) {
            this.mSubtitle = this.mSubtitle.replaceFirst("\n", " - ");
        }
        this.mSubtitle = this.mSubtitle.replaceAll("(\r\n|\n)", MetricSpannerUtils.METRIC_UNITS_SEPARATOR_SPACE);
    }

    private Bundle getReflectedExtras(Notification incomingNotification, String field) {
        Bundle declaredFieldValue;
        try {
            Field declaredField = incomingNotification.getClass().getDeclaredField(field);
            declaredField.setAccessible(true);
            declaredFieldValue = (Bundle) declaredField.get(incomingNotification);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (declaredFieldValue != null) {
            return declaredFieldValue;
        }
        return null;
    }

    private Object getReflectedField(Notification incomingNotification, String field) {
        Object declaredFieldValue;
        try {
            Field declaredField = incomingNotification.getClass().getDeclaredField(field);
            declaredField.setAccessible(true);
            declaredFieldValue = declaredField.get(incomingNotification);
        } catch (Exception e) {
            KLog.i(TAG, "Getting reflected field failed!", e);
        }
        if (declaredFieldValue != null) {
            return declaredFieldValue;
        }
        return null;
    }

    private void populateDataFromBundle(Bundle bundle) {
        CharSequence[] subtitleBundle;
        this.mTitle = String.valueOf(bundle.get("android.title"));
        this.mSubtitle = String.valueOf(bundle.get("android.text"));
        if ((this.mSubtitle == null || this.mSubtitle.equals("null")) && (subtitleBundle = bundle.getCharSequenceArray("android.textLines")) != null && subtitleBundle.length > 0) {
            if (this.mPackageName.equals(StrappConstants.NOTIFICATION_SERVICE_FACEBOOK) || this.mPackageName.equals(StrappConstants.NOTIFICATION_SERVICE_FACEBOOK_MESSAGER)) {
                this.mSubtitle = subtitleBundle[subtitleBundle.length - 1].toString();
            } else {
                this.mSubtitle = subtitleBundle[0].toString();
            }
        }
    }

    @TargetApi(19)
    private void populateNotificationApi19(Notification incomingNotification) {
        try {
            if (incomingNotification.extras != null) {
                populateDataFromBundle(incomingNotification.extras);
            }
        } catch (Exception e) {
        }
        formatText();
    }

    public KNotification(StatusBarNotification incomingSBN) {
        this.mPackageName = incomingSBN.getPackageName();
        if (Build.VERSION.SDK_INT >= 19) {
            populateNotificationApi19(incomingSBN.getNotification());
        } else {
            populateNotificationApi18(incomingSBN.getNotification());
        }
    }

    public KNotification(Parcel in) {
        String[] data = new String[3];
        in.readStringArray(data);
        this.mTitle = data[0];
        this.mSubtitle = data[1];
        this.mPackageName = data[2];
    }

    public String getTitle() {
        return this.mTitle;
    }

    public String getSubtitle() {
        return this.mSubtitle;
    }

    public String getPackageName() {
        return this.mPackageName;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[]{this.mTitle, this.mSubtitle, this.mPackageName});
    }
}

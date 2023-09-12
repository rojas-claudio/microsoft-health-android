package com.microsoft.kapp;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import com.microsoft.band.client.CargoException;
import com.microsoft.band.internal.BandServiceMessage;
import com.microsoft.kapp.CargoExtensions;
import com.microsoft.kapp.activities.DeviceConnectionErrorActivity;
import com.microsoft.kapp.utils.CommonUtils;
import org.joda.time.DateTime;
import org.joda.time.Seconds;
/* loaded from: classes.dex */
public class DefaultDeviceStateDisplayManager implements DeviceStateDisplayManager, DeviceCommunicationStateListener {
    private static final int DEVICE_ERROR_NOTIFICATION_ID = 1;
    private static final String DEVICE_ERROR_NOTIFICATION_TAG = "DeviceStateDisplayNotification";
    private ApplicationStatusChecker mApplicationStatusChecker;
    private int mBlockUiPeriodSeconds;
    private Context mContext;
    private DateTime mLastShownTime;
    private NotificationManager mNotificationManager;
    private Handler mHandler = new Handler();
    private boolean mIsUIDisplayed = false;
    private Object mSyncLock = new Object();

    public DefaultDeviceStateDisplayManager(Context context, CargoConnection cargoConnection, ApplicationStatusChecker applicationStatusChecker, NotificationManager notificationManager) {
        this.mContext = context;
        this.mBlockUiPeriodSeconds = context.getResources().getInteger(R.integer.single_device_check_block_ui_period_seconds);
        this.mApplicationStatusChecker = applicationStatusChecker;
        this.mNotificationManager = notificationManager;
        cargoConnection.setDeviceCommunicationStateListener(this);
    }

    @Override // com.microsoft.kapp.DeviceStateDisplayManager
    public void setUIDisplayed(boolean isDisplayed) {
        synchronized (this.mSyncLock) {
            this.mIsUIDisplayed = isDisplayed;
            this.mLastShownTime = new DateTime();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean canShowErrorMessageUI() {
        boolean isUIDisplayed;
        boolean shouldBlockUI = false;
        synchronized (this.mSyncLock) {
            isUIDisplayed = this.mIsUIDisplayed;
            DateTime now = new DateTime();
            if (this.mLastShownTime != null && Seconds.secondsBetween(this.mLastShownTime, now).getSeconds() < this.mBlockUiPeriodSeconds) {
                shouldBlockUI = true;
            }
        }
        return (isUIDisplayed || shouldBlockUI) ? false : true;
    }

    private static DeviceErrorState convertToDeviceErrorState(Exception exception) {
        if (exception instanceof CargoExtensions.SingleDeviceCheckFailedException) {
            CargoExtensions.SingleDeviceCheckFailedException singleDeviceCheckFailedException = (CargoExtensions.SingleDeviceCheckFailedException) exception;
            return singleDeviceCheckFailedException.getDeviceErrorState();
        }
        if (exception instanceof CargoException) {
            CargoException cargoException = (CargoException) exception;
            if (cargoException.getResponse() == BandServiceMessage.Response.DEVICE_NOT_CONNECTED_ERROR || cargoException.getResponse() == BandServiceMessage.Response.DEVICE_NOT_BONDED_ERROR) {
                return DeviceErrorState.CONNECTION_FAILED;
            }
        }
        return DeviceErrorState.NONE;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showErrorInNotificationCenter(DeviceErrorState deviceErrorState) {
        Integer errorMessageResId = CommonUtils.getResIdForDeviceErrorState(deviceErrorState);
        String errorMessage = this.mContext.getString(errorMessageResId != null ? errorMessageResId.intValue() : R.string.empty);
        String errorTitle = this.mContext.getString(R.string.error_notification_manager_title_text);
        Notification notification = new NotificationCompat.Builder(this.mContext).setContentText(errorMessage).setContentTitle(errorTitle).setTicker(errorTitle).setSmallIcon(R.drawable.ic_stat_warning).build();
        this.mNotificationManager.cancel(DEVICE_ERROR_NOTIFICATION_TAG, 1);
        this.mNotificationManager.notify(DEVICE_ERROR_NOTIFICATION_TAG, 1, notification);
    }

    @Override // com.microsoft.kapp.DeviceCommunicationStateListener
    public void onError(Exception ex, final boolean isUserInvokedAction) {
        final DeviceErrorState deviceErrorState = convertToDeviceErrorState(ex);
        if (deviceErrorState != DeviceErrorState.NONE) {
            this.mHandler.post(new Runnable() { // from class: com.microsoft.kapp.DefaultDeviceStateDisplayManager.1
                @Override // java.lang.Runnable
                public void run() {
                    if (!isUserInvokedAction || !DefaultDeviceStateDisplayManager.this.mApplicationStatusChecker.isRunningInForeground()) {
                        DefaultDeviceStateDisplayManager.this.showErrorInNotificationCenter(deviceErrorState);
                    } else if (DefaultDeviceStateDisplayManager.this.canShowErrorMessageUI()) {
                        KApplication application = (KApplication) DefaultDeviceStateDisplayManager.this.mContext.getApplicationContext();
                        Intent intent = new Intent(DefaultDeviceStateDisplayManager.this.mContext, DeviceConnectionErrorActivity.class);
                        intent.addFlags(268435456);
                        intent.putExtra(DeviceConnectionErrorActivity.DEVICE_ERROR_STATE_ID, deviceErrorState);
                        application.startActivity(intent);
                    }
                }
            });
        }
    }
}

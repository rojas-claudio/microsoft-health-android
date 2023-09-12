package com.microsoft.kapp.utils;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import com.microsoft.band.CargoKit;
import com.microsoft.band.client.CargoException;
import com.microsoft.kapp.CargoConnection;
import com.microsoft.kapp.R;
import com.microsoft.kapp.activities.HomeActivity;
import com.microsoft.kapp.device.CargoUserProfile;
import com.microsoft.kapp.diagnostics.Validate;
import com.microsoft.kapp.models.FreStatus;
import com.microsoft.kapp.multidevice.MultiDeviceManager;
import com.microsoft.kapp.services.KAppBroadcastReceiver;
import com.microsoft.kapp.services.SettingsProvider;
import com.microsoft.kapp.services.background.KAppsService;
import com.microsoft.krestsdk.auth.credentials.CredentialsManager;
import com.microsoft.krestsdk.auth.credentials.KCredential;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
/* loaded from: classes.dex */
public class SyncUtils {
    private static boolean mIsAppConfigurationRefreshInitialized;
    private static boolean mIsAppUpdateAlarmManagerInitialized;
    private static boolean mIsCacheCleanupInitialized;
    private static boolean mIsLogCleanupInitialized;
    private static boolean mIsProfileRefreshInitialized;
    private static boolean mIsSyncAlarmManagerInitialized;
    private static boolean mPreviousSyncEnabledStatus;

    public static synchronized void ensurePeriodicTasksSchedule(Context context, SettingsProvider settingsProvider) {
        synchronized (SyncUtils.class) {
            if (CargoKit.isBluetoothEnabled()) {
                ensureSyncSchedule(context, settingsProvider);
                ensureAppsUpdateSchedule(context);
            }
            ensureCacheCleanupSchedule(context);
            ensureLogCleanupSchedule(context);
            ensureProfileRefreshSchedule(context);
            ensureAppConfigurationSchedule(context);
        }
    }

    private static void ensureAppsUpdateSchedule(Context context) {
        if (!mIsAppUpdateAlarmManagerInitialized) {
            Intent receiver = new Intent(context, KAppBroadcastReceiver.class);
            receiver.setAction(Constants.INTENT_KAPPS_UPDATE);
            AlarmManager alarmManager = (AlarmManager) context.getSystemService("alarm");
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, receiver, 0);
            alarmManager.cancel(pendingIntent);
            alarmManager.setInexactRepeating(2, 900000L, 900000L, pendingIntent);
            mIsAppUpdateAlarmManagerInitialized = true;
        }
    }

    private static void ensureCacheCleanupSchedule(Context context) {
        if (!mIsCacheCleanupInitialized) {
            start24HourRefreshSchedule(context, Constants.INTENT_CACHE_CLEANUP);
            mIsCacheCleanupInitialized = true;
        }
    }

    private static void ensureProfileRefreshSchedule(Context context) {
        if (!mIsProfileRefreshInitialized) {
            start24HourRefreshSchedule(context, Constants.INTENT_REFRESH_PROFILE);
            mIsProfileRefreshInitialized = true;
        }
    }

    private static void ensureAppConfigurationSchedule(Context context) {
        if (!mIsAppConfigurationRefreshInitialized) {
            start24HourRefreshSchedule(context, Constants.INTENT_APP_CONFIGURATION);
            mIsAppConfigurationRefreshInitialized = true;
        }
    }

    private static void start24HourRefreshSchedule(Context context, String action) {
        Intent receiver = new Intent(context, KAppBroadcastReceiver.class);
        receiver.setAction(action);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService("alarm");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, receiver, 0);
        alarmManager.cancel(pendingIntent);
        Random random = new Random();
        int minutesOffset = random.nextInt(60);
        DateTime startTime = LocalDate.now().plusDays(1).toDateTimeAtStartOfDay().plusMinutes(minutesOffset);
        alarmManager.setInexactRepeating(0, startTime.getMillis(), 86400000L, pendingIntent);
    }

    private static void ensureLogCleanupSchedule(Context context) {
        if (!mIsLogCleanupInitialized) {
            Intent receiver = new Intent(context, KAppBroadcastReceiver.class);
            receiver.setAction(Constants.INTENT_LOG_CLEANUP);
            AlarmManager alarmManager = (AlarmManager) context.getSystemService("alarm");
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, receiver, 0);
            alarmManager.cancel(pendingIntent);
            Random random = new Random();
            int minutesOffset = random.nextInt(60);
            DateTime startTime = LocalDate.now().plusDays(1).toDateTimeAtStartOfDay().plusMinutes(minutesOffset);
            alarmManager.setInexactRepeating(0, startTime.getMillis(), 3600000L, pendingIntent);
            mIsLogCleanupInitialized = true;
        }
    }

    private static void ensureSyncSchedule(Context context, SettingsProvider settingsProvider) {
        if ((!mIsSyncAlarmManagerInitialized || settingsProvider.isAutoDeviceSyncEnabled() != mPreviousSyncEnabledStatus) && settingsProvider.getFreStatus() == FreStatus.SHOWN) {
            Intent receiver = new Intent(context, KAppBroadcastReceiver.class);
            receiver.setAction(Constants.INTENT_TRIGGER_SYNC);
            AlarmManager alarmManager = (AlarmManager) context.getSystemService("alarm");
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, receiver, 0);
            alarmManager.cancel(pendingIntent);
            if (settingsProvider.isAutoDeviceSyncEnabled()) {
                alarmManager.setInexactRepeating(2, 900000L, Constants.BACKGROUND_SYNC_INTERVAL_MS, pendingIntent);
            }
            mIsSyncAlarmManagerInitialized = true;
            mPreviousSyncEnabledStatus = settingsProvider.isAutoDeviceSyncEnabled();
        }
    }

    public static synchronized void clearSyncAndAppsUpdateSchedule(Context context) {
        synchronized (SyncUtils.class) {
            AlarmManager alarmManager = (AlarmManager) context.getSystemService("alarm");
            clearUpdateSchedule(context, alarmManager, Constants.INTENT_TRIGGER_SYNC);
            clearUpdateSchedule(context, alarmManager, Constants.INTENT_KAPPS_UPDATE);
            mIsSyncAlarmManagerInitialized = false;
            mIsAppUpdateAlarmManagerInitialized = false;
        }
    }

    private static void clearUpdateSchedule(Context context, AlarmManager alarmManager, String intentKey) {
        Intent receiver = new Intent(context, KAppBroadcastReceiver.class);
        receiver.setAction(intentKey);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, receiver, 0);
        alarmManager.cancel(pendingIntent);
    }

    public static boolean isDataSyncEnabled(Context context, SettingsProvider settingsProvider) {
        return true;
    }

    public static Notification buildSyncNotification(Context context) {
        Validate.notNull(context, Constants.FRE_INTENT_EXTRA_INFO);
        Intent notificationIntent = new Intent(context, HomeActivity.class);
        notificationIntent.addFlags(67108864);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);
        Notification notification = new NotificationCompat.Builder(context).setTicker(context.getString(R.string.sync_notification_ticker_text)).setContentTitle(context.getString(R.string.sync_notification_title)).setContentText(context.getString(R.string.sync_notification_in_progress_message)).setSmallIcon(R.drawable.ic_stat_action_refresh).setAutoCancel(false).setOngoing(true).setContentIntent(contentIntent).build();
        return notification;
    }

    public static void buildFirmwareUpdateRequiredSyncNotification(Context context, int notificationId) {
        Validate.notNull(context, Constants.FRE_INTENT_EXTRA_INFO);
        Intent notificationIntent = new Intent(context, HomeActivity.class);
        notificationIntent.addFlags(67108864);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);
        Notification notification = new NotificationCompat.Builder(context).setTicker(context.getString(R.string.sync_notification_update_required_ticker)).setContentTitle(context.getString(R.string.sync_notification_title)).setContentText(context.getString(R.string.sync_notification_update_required_text)).setSmallIcon(R.drawable.ic_stat_warning).setAutoCancel(false).setOngoing(false).setOnlyAlertOnce(true).setContentIntent(contentIntent).build();
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService("notification");
        mNotificationManager.notify(notificationId, notification);
    }

    public static void startStrappsDataSync(Context appContext) {
        if (appContext != null) {
            Intent updateIntent = new Intent(appContext, KAppsService.class);
            appContext.startService(updateIntent);
        }
    }

    public static boolean performCloudPairingCheckTask(CargoConnection mCargoConnection, CredentialsManager credentialsManager) throws CargoException {
        CargoUserProfile profile;
        KCredential credentials = credentialsManager.getCredentials();
        return credentials == null || mCargoConnection == null || (profile = mCargoConnection.getUserCloudProfile(credentials.getEndPoint(), credentials.getAccessToken(), credentials.getFUSEndPoint())) == null || profile.getDeviceId() == null || !profile.getDeviceId().toString().equals("00000000-0000-0000-0000-000000000000");
    }

    public static int canStrappSync(UUID strapId, SettingsProvider settingProvider, MultiDeviceManager multiDeviceManager) {
        boolean hasBand = multiDeviceManager.hasBand();
        if (!hasBand) {
            return 2;
        }
        List<UUID> uuid = settingProvider.getUUIDsOnDevice();
        boolean isStrappDisabled = uuid == null || !uuid.contains(strapId);
        return !isStrappDisabled ? 0 : 1;
    }
}

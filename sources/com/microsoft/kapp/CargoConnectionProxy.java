package com.microsoft.kapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import com.microsoft.band.CargoClient;
import com.microsoft.band.CargoCloudClient;
import com.microsoft.band.CargoConstants;
import com.microsoft.band.DeviceProfileStatus;
import com.microsoft.band.KDKSensorManager;
import com.microsoft.band.OOBEStage;
import com.microsoft.band.client.CargoException;
import com.microsoft.band.client.SyncResult;
import com.microsoft.band.client.UnitType;
import com.microsoft.band.cloud.CargoFirmwareUpdateInfo;
import com.microsoft.band.cloud.CargoServiceInfo;
import com.microsoft.band.cloud.EphemerisUpdateInfo;
import com.microsoft.band.cloud.TimeZoneSettingsUpdateInfo;
import com.microsoft.band.cloud.UserProfileInfo;
import com.microsoft.band.device.CalendarEvent;
import com.microsoft.band.device.CargoSms;
import com.microsoft.band.device.DeviceProfileInfo;
import com.microsoft.band.device.DeviceSettings;
import com.microsoft.band.device.FWVersion;
import com.microsoft.band.device.FirmwareVersions;
import com.microsoft.band.device.GoalsData;
import com.microsoft.band.device.StartStrip;
import com.microsoft.band.device.StrappColorPalette;
import com.microsoft.band.device.StrappMessage;
import com.microsoft.band.device.StrappPageElement;
import com.microsoft.band.device.TimeZoneInfo;
import com.microsoft.band.device.WorkoutActivity;
import com.microsoft.band.device.command.BikeDisplayMetricType;
import com.microsoft.band.device.command.NavigateToScreen;
import com.microsoft.band.device.command.RunDisplayMetricType;
import com.microsoft.band.device.command.SmsResponseType;
import com.microsoft.band.device.subscription.TextMessageData;
import com.microsoft.band.internal.BandServiceMessage;
import com.microsoft.band.internal.InternalBandConstants;
import com.microsoft.band.internal.device.DeviceInfo;
import com.microsoft.band.internal.util.VersionCheck;
import com.microsoft.band.sensors.BandBatteryLevelEvent;
import com.microsoft.band.sensors.BandBatteryLevelEventListener;
import com.microsoft.band.service.cloud.CloudDataResource;
import com.microsoft.band.webtiles.WebTile;
import com.microsoft.kapp.CargoConnection;
import com.microsoft.kapp.calendar.CalendarEvent;
import com.microsoft.kapp.device.CargoUserProfile;
import com.microsoft.kapp.diagnostics.Compatibility;
import com.microsoft.kapp.diagnostics.Validate;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.kapp.models.CustomStrappData;
import com.microsoft.kapp.models.FreStatus;
import com.microsoft.kapp.multidevice.SingleDeviceEnforcementManager;
import com.microsoft.kapp.personalization.DeviceTheme;
import com.microsoft.kapp.services.CallDismissManager;
import com.microsoft.kapp.services.SettingsProvider;
import com.microsoft.kapp.telephony.IncomingCallContext;
import com.microsoft.kapp.telephony.KNotification;
import com.microsoft.kapp.telephony.Message;
import com.microsoft.kapp.telephony.MmsMessage;
import com.microsoft.kapp.telephony.PhoneState;
import com.microsoft.kapp.telephony.SmsReplyRequestedListener;
import com.microsoft.kapp.utils.CommonUtils;
import com.microsoft.kapp.utils.Constants;
import com.microsoft.kapp.utils.FirmwareUpdateUtils;
import com.microsoft.kapp.utils.LogScenarioTags;
import com.microsoft.kapp.utils.SettingsUtils;
import com.microsoft.kapp.utils.StrappUtils;
import com.microsoft.kapp.version.CheckedFirmwareUpdateInfo;
import com.microsoft.kapp.version.Version;
import com.microsoft.krestsdk.auth.TokenOperations;
import com.microsoft.krestsdk.auth.credentials.CredentialsManager;
import com.microsoft.krestsdk.auth.credentials.KCredential;
import com.microsoft.krestsdk.models.BandVersion;
import com.microsoft.krestsdk.models.GoalType;
import com.microsoft.krestsdk.services.UserAgentProvider;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
/* loaded from: classes.dex */
public class CargoConnectionProxy implements CargoConnection {
    private static final int CLOUD_LOG_FILE_PROCESSING_WAIT_TIMEOUT_MILLISECONDS = 60000;
    private static final int CLOUD_PROCESSING_WAIT_TIMEOUT_MILLISECONDS = 300000;
    private static final int DISPLAY_NAME_MAX_LENGTH = 40;
    private static final int KDK_PROCESSING_WAIT_TIMEOUT_MILLISECONDS = 20000;
    private static final int MESSAGE_BODY_MAX_LENGTH = 160;
    private static final int SYNC_PROCESSING_WAIT_TIMEOUT_MILLISECONDS = 120000;
    private static final String TAG = CargoConnectionProxy.class.getSimpleName();
    private CountDownLatch mBatteryCompleteLatch;
    private CallDismissManager mCallDismissManager;
    private CargoFirmwareUpdateListener mCargoFirmwareUpdateListener;
    private final CargoLocalBroadcastReceiver mCargoLocalBroadcastReceiver;
    private CargoSyncListener mCargoSyncListener;
    private final Context mContext;
    private CredentialsManager mCredentialsManager;
    private DeviceCommunicationStateListener mDeviceCommunicationStateListener;
    private DeviceInfo mDeviceInfo;
    private EphemerisTimeZoneUpdateListener mEphemerisTimeZoneUpdateListener;
    private List<CalendarEvent> mLastSentCalendarEvents;
    private KDKSensorManager mSensorManager;
    private final SettingsProvider mSettingsProvider;
    private SingleDeviceEnforcementManager mSingleDeviceEnforcementManager;
    private Map<Integer, String> mSmsPhoneHashMap;
    private SmsReplyRequestedListener mSmsReplyListener;
    private SyncProgressListener mSyncProgressListener;
    private UserAgentProvider mUserAgentProvider;
    private int mBatteryValue = 0;
    private BandBatteryLevelEventListener mBatteryLevelEventListener = new BandBatteryLevelEventListener() { // from class: com.microsoft.kapp.CargoConnectionProxy.40
        @Override // com.microsoft.band.sensors.BandBatteryLevelEventListener
        public void onBatteryLevelChanged(BandBatteryLevelEvent event) {
            if (event != null) {
                CargoConnectionProxy.this.mBatteryValue = event.getBatteryLevel();
                CargoConnectionProxy.this.mBatteryCompleteLatch.countDown();
            }
            CargoConnectionProxy.this.unregisterBatteryEventListener();
        }
    };

    /* loaded from: classes.dex */
    public interface CargoEphemerisTimeZoneUpdateListener {
        void onDownloadError();

        void onEphemerisDownloadCompleted(BandServiceMessage.Response response);

        void onTimeZoneDownloadCompleted(BandServiceMessage.Response response);
    }

    /* loaded from: classes.dex */
    public interface CargoFirmwareUpdateListener {
        AtomicBoolean getDownloadFirmwareLatch();

        BandServiceMessage.Response getError();

        AtomicBoolean getUpgradeFirmwareLatch();

        void setError(BandServiceMessage.Response response);
    }

    /* loaded from: classes.dex */
    public interface CargoSyncListener {
        void onSyncDeviceToCloudCompleted(SyncResult syncResult, List<CloudDataResource> list);

        void onSyncWebtilesCompleted(SyncResult syncResult);

        void setError(BandServiceMessage.Response response);
    }

    public CargoConnectionProxy(Context context, CredentialsManager credentialsManager, SettingsProvider settingsProvider, UserAgentProvider userAgentProvider, SingleDeviceEnforcementManager singleDeviceEnforcementManager, CallDismissManager callDismissManager) {
        Validate.notNull(context, Constants.FRE_INTENT_EXTRA_INFO);
        Validate.notNull(userAgentProvider, "userAgentProvider");
        this.mContext = context;
        this.mCredentialsManager = credentialsManager;
        this.mSettingsProvider = settingsProvider;
        this.mLastSentCalendarEvents = null;
        this.mUserAgentProvider = userAgentProvider;
        this.mSingleDeviceEnforcementManager = singleDeviceEnforcementManager;
        this.mCallDismissManager = callDismissManager;
        this.mSmsPhoneHashMap = new HashMap();
        this.mCargoLocalBroadcastReceiver = new CargoLocalBroadcastReceiver();
        this.mCargoLocalBroadcastReceiver.register(this.mContext);
    }

    @Override // com.microsoft.kapp.CargoConnection
    public List<DeviceInfo> getBondedDevices() {
        return CargoClient.getBondedDevices();
    }

    @Override // com.microsoft.kapp.CargoConnection
    public boolean isDeviceAvailable(DeviceInfo device) {
        Boolean success;
        Boolean.valueOf(false);
        try {
            success = (Boolean) execute(true, device, false, new CargoConnectionExecutor<Boolean>() { // from class: com.microsoft.kapp.CargoConnectionProxy.1
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // com.microsoft.kapp.CargoConnectionExecutor
                public Boolean execute(CargoClient client) throws CargoException {
                    return Boolean.valueOf(client.isDeviceConnected());
                }
            });
        } catch (CargoException e) {
            success = false;
        }
        if (success == null) {
            return false;
        }
        return success.booleanValue();
    }

    @Override // com.microsoft.kapp.CargoConnection
    public void sendCalendarEvents(final List<CalendarEvent> calendarEvents) {
        Validate.notNull(calendarEvents, "calendarEvents");
        if (IsDifferentFromLastSent(calendarEvents)) {
            try {
                executeNonUserInvokedAction(new CargoConnectionExecutor<Void>() { // from class: com.microsoft.kapp.CargoConnectionProxy.2
                    @Override // com.microsoft.kapp.CargoConnectionExecutor
                    public Void execute(CargoClient client) throws CargoException {
                        client.sendCalendarEventClearNotification();
                        if (calendarEvents.size() > 0) {
                            if (!Compatibility.isPublicRelease()) {
                                try {
                                    StringBuilder stringBuilder = new StringBuilder();
                                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                                    for (CalendarEvent event : calendarEvents) {
                                        stringBuilder.append(String.format("CalendarEvent: Title=['%s'], Location=['%s'], StartDate=[%s], Duration[%d mins], HasReminder=[%b], Reminder[%d mins], Availability=[%s], IsCanceled=[%b], IsAllDay=[%b]", event.getTitle(), event.getLocation(), format.format(event.getStartDate()), Long.valueOf(event.getDurationInMinutes()), Boolean.valueOf(event.getHasReminder()), Integer.valueOf(event.getReminderMinutes()), event.getAvailability().toString(), Boolean.valueOf(event.getIsCanceled()), Boolean.valueOf(event.getIsAllDay())));
                                        stringBuilder.append("\n");
                                        stringBuilder.append("\n");
                                    }
                                    String calendarEventsLog = stringBuilder.toString();
                                    CargoConnectionProxy.this.mSettingsProvider.setCalendarLastErrorMessage("");
                                    CargoConnectionProxy.this.mSettingsProvider.setCalendarLastErrorTime("");
                                    CargoConnectionProxy.this.mSettingsProvider.setCalendarLastSyncEvents(calendarEventsLog);
                                    CargoConnectionProxy.this.mSettingsProvider.setCalendarLastSyncTime(DateTime.now().toString(DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:SSZ")));
                                } catch (Exception ex) {
                                    KLog.e(CargoConnectionProxy.TAG, "sendCalendarEvents failed.", ex);
                                }
                            }
                            com.microsoft.band.device.CalendarEvent[] events = CargoConnectionProxy.convertToDeviceCalendarEvents(calendarEvents);
                            client.sendCalendarEvents(events);
                        }
                        CargoConnectionProxy.this.mLastSentCalendarEvents = calendarEvents;
                        return null;
                    }
                });
            } catch (CargoException ex) {
                KLog.i(TAG, "Exception encountered while sending calendar events.");
                if (!Compatibility.isPublicRelease()) {
                    this.mSettingsProvider.setCalendarLastErrorMessage(ex.getMessage());
                    this.mSettingsProvider.setCalendarLastErrorTime(DateTime.now().toString(DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:SSZ")));
                }
            }
        }
    }

    @Override // com.microsoft.kapp.CargoConnection
    public void clearLastSentCalendarEventsCache() {
        this.mLastSentCalendarEvents = null;
    }

    @Override // com.microsoft.kapp.CargoConnection
    public void sendCallStateChangeNotification(final IncomingCallContext context) {
        if (this.mSettingsProvider.isCallsEnabled()) {
            Validate.notNull(context, Constants.FRE_INTENT_EXTRA_INFO);
            final PhoneState state = context.getState();
            KLog.d(LogScenarioTags.PhoneCall, "state " + state.toString());
            if (state == PhoneState.RINGING || state == PhoneState.MISSED || state == PhoneState.HUNG_UP || state == PhoneState.PICKED_UP) {
                try {
                    executeQueueAction(new CargoConnectionExecutor<Void>() { // from class: com.microsoft.kapp.CargoConnectionProxy.3
                        @Override // com.microsoft.kapp.CargoConnectionExecutor
                        public Void execute(CargoClient client) throws CargoException {
                            String displayName = context.getDisplayName();
                            KLog.logPrivate(LogScenarioTags.PhoneCall, "display name " + displayName);
                            if (StringUtils.isBlank(displayName)) {
                                throw new IllegalStateException("DisplayName cannot be null or empty.");
                            }
                            if (displayName.length() > 40) {
                                displayName = displayName.substring(0, 40);
                            }
                            String tmpNumber = context.getNumber();
                            int tmpNumberHash = tmpNumber.hashCode();
                            CargoConnectionProxy.this.mSmsPhoneHashMap.put(Integer.valueOf(tmpNumberHash), tmpNumber);
                            if (state == PhoneState.RINGING) {
                                KLog.d(LogScenarioTags.PhoneCall, "Sending incoming call notification");
                                client.sendIncomingCallNotification(displayName, tmpNumberHash, new Date(), context.getNotificationFlags());
                            } else if (state == PhoneState.MISSED) {
                                KLog.d(LogScenarioTags.PhoneCall, "Sending missed call notification");
                                client.queueMissedCallNotification(displayName, 0, new Date());
                            } else if (state == PhoneState.HUNG_UP) {
                                KLog.d(LogScenarioTags.PhoneCall, "Sending hung-up call notification");
                                client.sendHangupCallNotification(displayName, tmpNumberHash, new Date());
                            } else if (state == PhoneState.PICKED_UP) {
                                KLog.d(LogScenarioTags.PhoneCall, "Sending pick-up call notification");
                                client.sendAnsweredCallNotification(displayName, tmpNumberHash, new Date());
                            }
                            KLog.d(LogScenarioTags.PhoneCall, "Call notification complete");
                            return null;
                        }
                    });
                } catch (CargoException ex) {
                    KLog.i(LogScenarioTags.PhoneCall, "Failed to send call notification to the device", ex);
                }
            }
        }
    }

    @Override // com.microsoft.kapp.CargoConnection
    public Bitmap captureDeviceScreenshot() throws CargoException {
        return (Bitmap) execute(new CargoConnectionExecutor<Bitmap>() { // from class: com.microsoft.kapp.CargoConnectionProxy.4
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // com.microsoft.kapp.CargoConnectionExecutor
            public Bitmap execute(CargoClient client) throws CargoException {
                return client.readPegScreen();
            }
        });
    }

    @Override // com.microsoft.kapp.CargoConnection
    public boolean waitForCloudProcessingToComplete(final List<CloudDataResource> cloudDataResources) throws CargoException {
        return ((Boolean) executeCloudClient(new CargoCloudConnectionExecutor<Boolean>() { // from class: com.microsoft.kapp.CargoConnectionProxy.5
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // com.microsoft.kapp.CargoCloudConnectionExecutor
            public Boolean execute(CargoCloudClient client) throws CargoException {
                boolean result = client.waitForCloudProcessingToComplete(cloudDataResources, DateUtils.MILLIS_PER_MINUTE);
                return Boolean.valueOf(result);
            }
        })).booleanValue();
    }

    @Override // com.microsoft.kapp.CargoConnection
    public CargoConnection.SynchronizeDeviceToCloudResult synchronizeDeviceToCloud(final boolean isForegroundSync, SyncProgressListener syncProgressListener) throws CargoException {
        this.mSyncProgressListener = syncProgressListener;
        return (CargoConnection.SynchronizeDeviceToCloudResult) execute(false, null, true, new CargoConnectionExecutor<CargoConnection.SynchronizeDeviceToCloudResult>() { // from class: com.microsoft.kapp.CargoConnectionProxy.6
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // com.microsoft.kapp.CargoConnectionExecutor
            public CargoConnection.SynchronizeDeviceToCloudResult execute(CargoClient client) throws CargoException {
                boolean fullSync = !isForegroundSync;
                SyncListener syncListener = new SyncListener();
                CountDownLatch countDownLatch = syncListener.getCountDownLatch();
                CargoConnectionProxy.this.setCargoSyncListener(syncListener);
                if (!client.syncDeviceToCloud(fullSync)) {
                    throw new IllegalStateException("CargoClient.syncDeviceToCloud() failed");
                }
                try {
                    boolean isTimeout = !countDownLatch.await(120000L, TimeUnit.MILLISECONDS);
                    CargoConnectionProxy.this.setCargoSyncListener(null);
                    if (isTimeout) {
                        return new CargoConnection.SynchronizeDeviceToCloudResult(null, null, null, BandServiceMessage.Response.OPERATION_TIMEOUT_ERROR);
                    }
                    return new CargoConnection.SynchronizeDeviceToCloudResult(syncListener.getSyncResult(), syncListener.getWebtileSyncResult(), syncListener.getCloudDataList(), syncListener.getError());
                } catch (InterruptedException e) {
                    throw new IllegalStateException("countDownLatch.await() was interrupted", e);
                }
            }
        });
    }

    @Override // com.microsoft.kapp.CargoConnection
    public void linkDeviceToProfile() throws CargoException {
        linkDeviceToProfile(false);
    }

    @Override // com.microsoft.kapp.CargoConnection
    public void linkDeviceToProfile(boolean suppressSingleDeviceCheck) throws CargoException {
        execute(suppressSingleDeviceCheck, null, false, new CargoConnectionExecutor<Void>() { // from class: com.microsoft.kapp.CargoConnectionProxy.7
            @Override // com.microsoft.kapp.CargoConnectionExecutor
            public Void execute(CargoClient client) throws CargoException {
                client.linkDeviceToProfile(true);
                return null;
            }
        });
    }

    @Override // com.microsoft.kapp.CargoConnection
    public void unlinkDeviceToProfile() throws CargoException {
        CargoClient client = null;
        try {
            client = getCargoClient((DeviceInfo) null);
            client.unlinkDeviceAndCloud();
        } finally {
            if (client != null) {
                client.destroy();
            }
        }
    }

    @Override // com.microsoft.kapp.CargoConnection
    public int pushWorkoutData(final byte[] workoutData) {
        try {
            int pushWorkoutSucceeded = ((Integer) execute(new CargoConnectionExecutor<Integer>() { // from class: com.microsoft.kapp.CargoConnectionProxy.8
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // com.microsoft.kapp.CargoConnectionExecutor
                public Integer execute(CargoClient client) throws CargoException {
                    client.pushFitnessData(workoutData);
                    return 2;
                }
            })).intValue();
            return pushWorkoutSucceeded;
        } catch (CargoException exception) {
            KLog.w(TAG, "Exception encountered.", exception);
            if (!exception.getResponse().equals(BandServiceMessage.Response.DEVICE_FILE_ALREADY_OPEN_ERROR)) {
                return 0;
            }
            return 1;
        }
    }

    @Override // com.microsoft.kapp.CargoConnection
    public int pushGolfCourseData(final byte[] golfCourseData) {
        try {
            int pushGolfCourseSucceeded = ((Integer) execute(new CargoConnectionExecutor<Integer>() { // from class: com.microsoft.kapp.CargoConnectionProxy.9
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // com.microsoft.kapp.CargoConnectionExecutor
                public Integer execute(CargoClient client) throws CargoException {
                    if (!client.isDeviceConnected()) {
                        return 4;
                    }
                    client.pushGolfCourseData(golfCourseData);
                    return 2;
                }
            })).intValue();
            return pushGolfCourseSucceeded;
        } catch (CargoException exception) {
            KLog.w(TAG, "Exception encountered.", exception);
            BandServiceMessage.Response responseCode = exception.getResponse();
            if (responseCode.equals(BandServiceMessage.Response.DEVICE_FILE_ALREADY_OPEN_ERROR)) {
                return 1;
            }
            if (responseCode.equals(BandServiceMessage.Response.BLUETOOTH_DISCOVERY_FAILED_ERROR) || responseCode.equals(BandServiceMessage.Response.BLUETOOTH_DISCOVERY_TIMEOUT_ERROR) || responseCode.equals(BandServiceMessage.Response.BLUETOOTH_DISCOVERY_TIMEOUT_ERROR) || responseCode.equals(BandServiceMessage.Response.BLUETOOTH_DISCOVERY_TIMEOUT_ERROR)) {
                return 4;
            }
            if (responseCode.equals(BandServiceMessage.Response.OPERATION_TIMEOUT_ERROR)) {
                return 5;
            }
            if (!responseCode.equals(BandServiceMessage.Response.DEVICE_DISCONNECTED)) {
                return 0;
            }
            return 6;
        }
    }

    @Override // com.microsoft.kapp.CargoConnection
    public void sendMessageNotification(final Message message) {
        Validate.notNull(message, "message");
        KLog.v(TAG, "Sending Message Notification to Device");
        if (this.mSettingsProvider.isMessagingEnabled()) {
            try {
                executeQueueAction(new CargoConnectionExecutor<Void>() { // from class: com.microsoft.kapp.CargoConnectionProxy.10
                    @Override // com.microsoft.kapp.CargoConnectionExecutor
                    public Void execute(CargoClient client) throws CargoException {
                        String displayName = message.getDisplayName();
                        Validate.notNullOrEmpty(displayName, "displayName");
                        String displayName2 = CommonUtils.truncateString(displayName, 40);
                        String messageBody = message.getBody();
                        if (!StringUtils.isBlank(messageBody)) {
                            messageBody = CommonUtils.truncateString(messageBody, 160);
                        }
                        KLog.logPrivate(LogScenarioTags.SmsMmsMessage, "%d: Sending message %s.", Integer.valueOf(message.getId()), message);
                        int newSmsId = 0;
                        String tmpNumber = message.getNumber();
                        if (tmpNumber != null && !tmpNumber.isEmpty()) {
                            CargoConnectionProxy.this.mSmsPhoneHashMap.put(Integer.valueOf(tmpNumber.hashCode()), tmpNumber);
                            newSmsId = tmpNumber.hashCode();
                        }
                        CargoSms cargoMessage = new CargoSms(displayName2, messageBody, message.getTimestamp(), newSmsId);
                        if (message instanceof MmsMessage) {
                            MmsMessage mmsMessage = (MmsMessage) message;
                            cargoMessage.setMultimedia(mmsMessage.getMessageType());
                            cargoMessage.setParticipants(mmsMessage.getReceiversNameList());
                        }
                        client.queueSmsNotification(cargoMessage);
                        KLog.logPrivate(LogScenarioTags.SmsMmsMessage, "%d: Successfully sent message %s.", Integer.valueOf(message.getId()), message);
                        return null;
                    }
                });
            } catch (CargoException ex) {
                KLog.w(TAG, "Failed to send message notification to the device", ex);
                KLog.logPrivate(LogScenarioTags.SmsMmsMessage, "%d: Failed to send message %s.", Integer.valueOf(message.getId()), message);
            }
        }
    }

    @Override // com.microsoft.kapp.CargoConnection
    public void sendVoicemailNotification() {
        KLog.v(TAG, "Sending Message Notification to Device");
        if (this.mSettingsProvider.isMessagingEnabled() && this.mSettingsProvider.getFreStatus() == FreStatus.SHOWN) {
            try {
                executeQueueAction(new CargoConnectionExecutor<Void>() { // from class: com.microsoft.kapp.CargoConnectionProxy.11
                    @Override // com.microsoft.kapp.CargoConnectionExecutor
                    public Void execute(CargoClient client) throws CargoException {
                        client.queueVoicemailNotification("", 0, new Date());
                        return null;
                    }
                });
            } catch (CargoException e) {
                KLog.v(LogScenarioTags.VoicemailNotification, "Failed to send message to the device.");
            }
        }
    }

    @Override // com.microsoft.kapp.CargoConnection
    public void sendNotificationToStrapp(final KNotification notification, final UUID uuid) {
        Validate.notNull(notification, "notification");
        Validate.notNull(uuid, "uuid");
        KLog.logPrivate(TAG, "Sending Notification to Device : %s, %s", notification.getTitle(), notification.getSubtitle());
        if (this.mSettingsProvider.isNotificationDataDifferentThanLastSyncData(uuid, notification) && notification.getTitle() != null && !notification.getTitle().equals("null")) {
            this.mSettingsProvider.setNotificationDataForCustomStrapp(uuid, notification);
            try {
                executeQueueAction(new CargoConnectionExecutor<Void>() { // from class: com.microsoft.kapp.CargoConnectionProxy.12
                    @Override // com.microsoft.kapp.CargoConnectionExecutor
                    public Void execute(CargoClient client) throws CargoException {
                        StrappMessage msg = new StrappMessage(notification.getTitle(), notification.getSubtitle(), new Date());
                        client.queueStrappMessage(uuid, msg);
                        return null;
                    }
                });
            } catch (CargoException ex) {
                this.mSettingsProvider.clearLastSyncDataForCustomStrapp(uuid);
                KLog.i(TAG, "Failed to send message notification to the device", ex);
            }
        }
    }

    @Override // com.microsoft.kapp.CargoConnection
    public void sendCustomStrappData(final CustomStrappData strapp) {
        KLog.v(TAG, "Sending Custom Strapp Update Notifications to Device");
        try {
            Validate.notNull(strapp, "strapp");
            executeNonUserInvokedAction(new CargoConnectionExecutor<Void>() { // from class: com.microsoft.kapp.CargoConnectionProxy.13
                @Override // com.microsoft.kapp.CargoConnectionExecutor
                public Void execute(CargoClient client) throws CargoException {
                    UUID uuid = strapp.getUUID();
                    boolean addLastUpdatedLayout = strapp.isAddLastUpdatedLayout();
                    List<Integer> layoutArrays = strapp.getLayoutArrays();
                    ArrayList<ArrayList<StrappPageElement>> pageElements = strapp.getPageElements();
                    if (CargoConnectionProxy.this.mSettingsProvider.isSyncDataDifferentThanLastSyncData(uuid, pageElements)) {
                        CargoConnectionProxy.this.mSettingsProvider.setSyncDataForCustomStrapp(uuid, pageElements);
                        if (addLastUpdatedLayout) {
                            layoutArrays.add(Integer.valueOf(strapp.getLastLayoutIndex()));
                            pageElements.add(StrappUtils.createLastUpdatedStrappElement(strapp.getLastUpdatedText(), CargoConnectionProxy.this.mContext));
                        }
                        int strappSize = Math.min(strapp.getPageElements().size(), 8);
                        if (strapp.isVariableLength()) {
                            client.clearStrapp(uuid);
                        }
                        for (int i = strappSize - 1; i >= 0; i--) {
                            client.sendPageUpdate(uuid, strapp.getUUIDsForPages().get(i), layoutArrays.get(i).intValue(), pageElements.get(i));
                        }
                        return null;
                    }
                    return null;
                }
            });
        } catch (CargoException ex) {
            this.mSettingsProvider.clearLastSyncDataForCustomStrapp(strapp.getUUID());
            KLog.w(TAG, "Exception encountered while sending custom strapp.", ex);
        }
    }

    @Override // com.microsoft.kapp.CargoConnection
    public DeviceInfo getConnectedDevice() throws CargoException {
        return getConnectedDevice(false, false);
    }

    @Override // com.microsoft.kapp.CargoConnection
    public DeviceInfo getConnectedDevice(boolean suppressCheck, boolean notifyCommunicationStateErrorListener) throws CargoException {
        List<DeviceInfo> devices = getBondedDevices();
        if (devices == null || devices.size() == 0) {
            if (!suppressCheck) {
                throw new CargoException("No Devices Bonded!", BandServiceMessage.Response.DEVICE_NOT_CONNECTED_ERROR);
            }
        } else {
            for (DeviceInfo device : devices) {
                try {
                } catch (Exception e) {
                    KLog.logPrivate(TAG, String.format("Device %1$s threw an exception when checking to see if it belongs", device.getName()), e);
                }
                if (this.mSettingsProvider.getFreStatus() != FreStatus.SHOWN) {
                    if (isOobeCompletedForDevice(device)) {
                        continue;
                    } else {
                        KLog.logPrivate(TAG, "Oobe was not complete for device: " + device.getName());
                        this.mDeviceInfo = device;
                    }
                } else if (checkDeviceBelongsToUser(device)) {
                    KLog.logPrivate(TAG, "device belongs to user: " + device.getName());
                    this.mDeviceInfo = device;
                } else {
                    continue;
                }
                return device;
            }
        }
        return null;
    }

    @Override // com.microsoft.kapp.CargoConnection, com.microsoft.kapp.CargoVersionRetriever
    public Version getDeviceFirmwareVersion() {
        FirmwareVersions version = null;
        try {
            version = (FirmwareVersions) execute(new CargoConnectionExecutor<FirmwareVersions>() { // from class: com.microsoft.kapp.CargoConnectionProxy.14
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // com.microsoft.kapp.CargoConnectionExecutor
                public FirmwareVersions execute(CargoClient client) throws CargoException {
                    return client.getFirmwareVersions();
                }
            });
        } catch (CargoException ex) {
            KLog.w(TAG, "getDeviceFirmwareVersion failed with an exception.", ex);
        }
        if (version != null) {
            FWVersion firmwareVersion = version.getApplicationVersion();
            return new Version(firmwareVersion.getVersionMajor(), firmwareVersion.getVersionMinor(), firmwareVersion.getBuildNumber(), firmwareVersion.getRevision(), firmwareVersion.getPcbId(), firmwareVersion.getDebugBuild());
        }
        return null;
    }

    @Override // com.microsoft.kapp.CargoConnection
    public FirmwareVersions getDeviceFirmwareVersionsObject() {
        try {
            return (FirmwareVersions) execute(new CargoConnectionExecutor<FirmwareVersions>() { // from class: com.microsoft.kapp.CargoConnectionProxy.15
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // com.microsoft.kapp.CargoConnectionExecutor
                public FirmwareVersions execute(CargoClient client) throws CargoException {
                    return client.getFirmwareVersions();
                }
            });
        } catch (CargoException ex) {
            KLog.w(TAG, "getFirmwareVersions failed with an exception.", ex);
            return null;
        }
    }

    @Override // com.microsoft.kapp.CargoConnection
    public DeviceProfileStatus getDeviceAndProfileLinkStatus() {
        try {
            DeviceProfileStatus deviceProfileStatus = (DeviceProfileStatus) execute(new CargoConnectionExecutor<DeviceProfileStatus>() { // from class: com.microsoft.kapp.CargoConnectionProxy.16
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // com.microsoft.kapp.CargoConnectionExecutor
                public DeviceProfileStatus execute(CargoClient client) throws CargoException {
                    return client.getDeviceAndProfileLinkStatus();
                }
            });
            return deviceProfileStatus;
        } catch (CargoException ex) {
            KLog.w(TAG, "getDeviceAndProfileLinkStatus failed with an exception.", ex);
            return null;
        }
    }

    @Override // com.microsoft.kapp.CargoConnection, com.microsoft.kapp.CargoVersionRetriever
    public Version getMinimumRequiredFirmwareVersion() {
        return getDeviceFirmwareVersion();
    }

    @Override // com.microsoft.kapp.CargoConnection, com.microsoft.kapp.CargoVersionRetriever
    public CargoFirmwareUpdateInfo getLatestAvailableFirmwareVersion() throws CargoException {
        CargoFirmwareUpdateInfo result = (CargoFirmwareUpdateInfo) execute(new CargoConnectionExecutor<CargoFirmwareUpdateInfo>() { // from class: com.microsoft.kapp.CargoConnectionProxy.17
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // com.microsoft.kapp.CargoConnectionExecutor
            public CargoFirmwareUpdateInfo execute(CargoClient client) throws CargoException {
                CargoFirmwareUpdateInfo firmwareUpdateInfo = client.getLatestAvailableFirmwareVersion();
                if (firmwareUpdateInfo != null) {
                    CargoConnectionProxy.this.mSettingsProvider.setCheckedFirmwareUpdateInfo(new CheckedFirmwareUpdateInfo(firmwareUpdateInfo.isFirmwareUpdateAvailable(), firmwareUpdateInfo.isFirmwareUpdateOptional(), firmwareUpdateInfo.getFirmwareVersion(), DateTime.now()));
                }
                return firmwareUpdateInfo;
            }
        });
        if (result == null) {
            throw new IllegalStateException("getLatestAvailableFirmwareVersion() returned null.");
        }
        return result;
    }

    @Override // com.microsoft.kapp.CargoConnection
    public BandServiceMessage.Response downloadFirmwareUpdate(final CargoFirmwareUpdateInfo firmwareUpdateInfo) throws CargoException {
        Validate.notNull(firmwareUpdateInfo, "firmwareUpdateInfo");
        this.mCargoFirmwareUpdateListener = new FirmwareUpdateListener();
        try {
            return (BandServiceMessage.Response) execute(true, null, false, new CargoConnectionExecutor<BandServiceMessage.Response>() { // from class: com.microsoft.kapp.CargoConnectionProxy.18
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // com.microsoft.kapp.CargoConnectionExecutor
                public BandServiceMessage.Response execute(CargoClient client) throws CargoException {
                    if (client.downloadFirmwareUpdate(firmwareUpdateInfo)) {
                        return CargoConnectionProxy.this.mCargoLocalBroadcastReceiver.waitForFirmwareDownloadToComplete();
                    }
                    throw new IllegalStateException("CargoClient.downloadFirmwareUpdate() failed");
                }
            });
        } finally {
            this.mCargoFirmwareUpdateListener = null;
        }
    }

    @Override // com.microsoft.kapp.CargoConnection
    public BandServiceMessage.Response pushFirmwareUpdateToDevice(final CargoFirmwareUpdateInfo firmwareUpdateInfo) throws CargoException {
        Validate.notNull(firmwareUpdateInfo, "firmwareUpdateInfo");
        this.mCargoFirmwareUpdateListener = new FirmwareUpdateListener();
        try {
            return (BandServiceMessage.Response) execute(true, null, false, new CargoConnectionExecutor<BandServiceMessage.Response>() { // from class: com.microsoft.kapp.CargoConnectionProxy.19
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // com.microsoft.kapp.CargoConnectionExecutor
                public BandServiceMessage.Response execute(CargoClient client) throws CargoException {
                    if (client.pushFirmwareUpdateToDevice(firmwareUpdateInfo)) {
                        return CargoConnectionProxy.this.mCargoLocalBroadcastReceiver.waitForFirmwareUpgradeToComplete();
                    }
                    throw new IllegalStateException("CargoClient.pushFirmwareUpdateToDevice() failed");
                }
            });
        } finally {
            this.mCargoFirmwareUpdateListener = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setCargoSyncListener(CargoSyncListener cargoSyncListener) {
        this.mCargoSyncListener = cargoSyncListener;
    }

    @Override // com.microsoft.kapp.CargoConnection
    public StartStrip getDefaultStrapps() throws CargoException {
        StartStrip strip = (StartStrip) execute(new CargoConnectionExecutor<StartStrip>() { // from class: com.microsoft.kapp.CargoConnectionProxy.20
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // com.microsoft.kapp.CargoConnectionExecutor
            public StartStrip execute(CargoClient client) throws CargoException {
                return client.getDefaultStrapps();
            }
        });
        if (strip == null) {
            throw new IllegalStateException("getDefaultStrapps() returned null.");
        }
        return strip;
    }

    @Override // com.microsoft.kapp.CargoConnection
    public StartStrip getStartStrip() throws CargoException {
        StartStrip strip = (StartStrip) execute(new CargoConnectionExecutor<StartStrip>() { // from class: com.microsoft.kapp.CargoConnectionProxy.21
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // com.microsoft.kapp.CargoConnectionExecutor
            public StartStrip execute(CargoClient client) throws CargoException {
                return client.getStartStrip(true);
            }
        });
        if (strip == null) {
            throw new IllegalStateException("getStartStrip() returned null.");
        }
        return strip;
    }

    @Override // com.microsoft.kapp.CargoConnection
    public void setStartStrip(final StartStrip apps) throws CargoException {
        clearLastSentCalendarEventsCache();
        execute(new CargoConnectionExecutor<Void>() { // from class: com.microsoft.kapp.CargoConnectionProxy.22
            @Override // com.microsoft.kapp.CargoConnectionExecutor
            public Void execute(CargoClient client) throws CargoException {
                client.setStartStrip(apps);
                return null;
            }
        });
    }

    @Override // com.microsoft.kapp.CargoConnection
    public void personalizeDevice(DeviceTheme deviceTheme, Bitmap wallpaper, int wallpaperPatternId) throws CargoException {
        personalizeDevice(null, deviceTheme, wallpaper, wallpaperPatternId);
    }

    @Override // com.microsoft.kapp.CargoConnection
    public void personalizeDevice(final StartStrip apps, final DeviceTheme deviceTheme, final Bitmap wallpaper, final int wallpaperPatternId) throws CargoException {
        clearLastSentCalendarEventsCache();
        execute(new CargoConnectionExecutor<Void>() { // from class: com.microsoft.kapp.CargoConnectionProxy.23
            @Override // com.microsoft.kapp.CargoConnectionExecutor
            public Void execute(CargoClient client) throws CargoException {
                Bitmap newWallpaper = wallpaper;
                client.personalizeDevice(apps, newWallpaper, deviceTheme.getColorPalette(), wallpaperPatternId);
                return null;
            }
        });
    }

    @Override // com.microsoft.kapp.CargoConnection
    public int getDeviceWallpaperId() throws CargoException {
        Integer id = (Integer) execute(new CargoConnectionExecutor<Integer>() { // from class: com.microsoft.kapp.CargoConnectionProxy.24
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // com.microsoft.kapp.CargoConnectionExecutor
            public Integer execute(CargoClient client) throws CargoException {
                return Integer.valueOf((int) client.getMeTileId());
            }
        });
        if (id == null) {
            throw new CargoException("Getting wallpaper id Failed!", BandServiceMessage.Response.UNSPECIFIED);
        }
        return id.intValue();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static com.microsoft.band.device.CalendarEvent[] convertToDeviceCalendarEvents(List<CalendarEvent> calendarEvents) {
        Validate.notNull(calendarEvents, "calendarEvents");
        List<com.microsoft.band.device.CalendarEvent> events = new ArrayList<>(calendarEvents.size());
        for (CalendarEvent calendarEvent : calendarEvents) {
            String title = normalizeAppointmentString(calendarEvent.getTitle(), 20);
            String location = normalizeAppointmentString(calendarEvent.getLocation(), 160);
            com.microsoft.band.device.CalendarEvent event = new com.microsoft.band.device.CalendarEvent(title, location, calendarEvent.getStartDate(), calendarEvent.getHasReminder() ? calendarEvent.getReminderMinutes() : -1, (int) calendarEvent.getDurationInMinutes(), convertToCalendarEventAcceptedState(calendarEvent.getAvailability()), calendarEvent.getIsAllDay(), calendarEvent.getIsCanceled());
            events.add(event);
        }
        return (com.microsoft.band.device.CalendarEvent[]) events.toArray(new com.microsoft.band.device.CalendarEvent[events.size()]);
    }

    private static String normalizeAppointmentString(String str, int maxLimit) {
        return str != null ? CommonUtils.truncateString(str, maxLimit) : "";
    }

    private static CalendarEvent.CalendarEventAcceptedState convertToCalendarEventAcceptedState(CalendarEvent.Availability availability) {
        switch (availability) {
            case BUSY:
            case UNKNOWN:
                return CalendarEvent.CalendarEventAcceptedState.ACCEPTED;
            case TENTATIVE:
                return CalendarEvent.CalendarEventAcceptedState.TENTATIVE;
            case FREE:
                return CalendarEvent.CalendarEventAcceptedState.FREE;
            default:
                throw new IllegalArgumentException(String.format("cannot map %s to a CalendarEventAcceptedState", availability.toString()));
        }
    }

    private boolean IsDifferentFromLastSent(List<com.microsoft.kapp.calendar.CalendarEvent> calendarEvents) {
        Validate.notNull(calendarEvents, "calendarEvents");
        return (this.mLastSentCalendarEvents != null && calendarEvents.size() == this.mLastSentCalendarEvents.size() && calendarEvents.equals(this.mLastSentCalendarEvents)) ? false : true;
    }

    @Override // com.microsoft.kapp.CargoConnection
    public void setDeviceCommunicationStateListener(DeviceCommunicationStateListener listener) {
        this.mDeviceCommunicationStateListener = listener;
    }

    @Override // com.microsoft.kapp.CargoConnection
    public void navigateToScreen(final OOBEStage screen) throws CargoException {
        try {
            execute(true, null, false, new CargoConnectionExecutor<Void>() { // from class: com.microsoft.kapp.CargoConnectionProxy.25
                @Override // com.microsoft.kapp.CargoConnectionExecutor
                public Void execute(CargoClient client) throws CargoException {
                    if (CargoConnectionProxy.this.isV2Device(client)) {
                        client.setOOBEStageIndex(screen);
                        return null;
                    }
                    switch (AnonymousClass60.$SwitchMap$com$microsoft$band$OOBEStage[screen.ordinal()]) {
                        case 1:
                        case 2:
                        default:
                            return null;
                        case 3:
                            client.navigateToScreenId(NavigateToScreen.Screens.OOBE_ALMOST_THERE_SCREEN);
                            return null;
                        case 4:
                            client.navigateToScreenId(NavigateToScreen.Screens.OOBE_UPDATES_SCREEN);
                            return null;
                        case 5:
                            client.navigateToScreenId(NavigateToScreen.Screens.OOBE_ALMOST_THERE_SCREEN);
                            return null;
                        case 6:
                            client.navigateToScreenId(NavigateToScreen.Screens.OOBE_ALMOST_THERE_SCREEN);
                            return null;
                    }
                }
            });
        } catch (CargoException ex) {
            KLog.w(TAG, "Exception encountered while navigating to OOBE screen.", ex);
            throw ex;
        }
    }

    @Override // com.microsoft.kapp.CargoConnection
    public int finishOobe() throws CargoException {
        try {
            Integer result = (Integer) execute(true, null, false, new CargoConnectionExecutor<Integer>() { // from class: com.microsoft.kapp.CargoConnectionProxy.26
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // com.microsoft.kapp.CargoConnectionExecutor
                public Integer execute(CargoClient client) throws CargoException {
                    if (CargoConnectionProxy.this.isV2Device(client)) {
                        return Integer.valueOf(client.finalizeOOBE());
                    }
                    client.navigateToScreenId(NavigateToScreen.Screens.OOBE_PRESS_BUTTON_TO_START_SCREEN);
                    return 0;
                }
            });
            if (result == null) {
                return 0;
            }
            return result.intValue();
        } catch (CargoException ex) {
            KLog.i(TAG, "Exception encountered while navigating to OOBE screen.", ex);
            throw ex;
        }
    }

    private CargoServiceInfo getServiceInfo() throws CargoException {
        KCredential credentials = this.mCredentialsManager.getCredentials();
        if (credentials != null) {
            return getCargoServiceInfo(credentials.getEndPoint(), credentials.getAccessToken(), credentials.getFUSEndPoint());
        }
        return null;
    }

    private CargoServiceInfo getCargoServiceInfo(String phsUrl, String kAccessToken, String fileUpdateURL) {
        CargoServiceInfo cargoServiceInfo = new CargoServiceInfo(phsUrl, fileUpdateURL, TokenOperations.wrapSwtForHeader(kAccessToken), this.mUserAgentProvider.getUserAgent());
        return cargoServiceInfo;
    }

    private <T> T execute(CargoConnectionExecutor<T> executor) throws CargoException {
        return (T) execute(false, null, false, executor);
    }

    private void notifyCommunicationStateErrorQuitely(Exception exception) {
        notifyCommunicationStateErrorQuitely(exception, true);
    }

    private void notifyCommunicationStateErrorQuitely(Exception exception, boolean isUserInvokedAction) {
        try {
            if (this.mDeviceCommunicationStateListener != null) {
                this.mDeviceCommunicationStateListener.onError(exception, isUserInvokedAction);
            }
        } catch (Exception ex) {
            KLog.w(TAG, "notifyCommunicationStateErrorQuitely() failed.", ex);
        }
    }

    private <T> T executeQueueAction(CargoConnectionExecutor<T> executor) throws CargoException {
        return (T) execute(true, null, false, true, executor);
    }

    private <T> T executeNonUserInvokedAction(CargoConnectionExecutor<T> executor) throws CargoException {
        return (T) execute(false, null, false, executor);
    }

    private <T> T execute(boolean suppressSingleDevice, DeviceInfo deviceInfo, boolean checkForFirmwareUpdate, CargoConnectionExecutor<T> executor) throws CargoException {
        return (T) execute(suppressSingleDevice, deviceInfo, checkForFirmwareUpdate, false, executor);
    }

    private synchronized <T> T execute(boolean suppressSingleDevice, DeviceInfo deviceInfo, boolean checkForFirmwareUpdate, boolean isQueueAction, CargoConnectionExecutor<T> executor) throws CargoException {
        T result;
        SettingsUtils.throwExceptionIfOnMainThread();
        CargoClient client = null;
        try {
            if (isQueueAction) {
                if (this.mDeviceInfo != null) {
                    client = getCargoClient(this.mDeviceInfo);
                }
            } else if (deviceInfo != null) {
                client = getCargoClient(deviceInfo);
            } else if (this.mDeviceInfo != null) {
                client = getCargoClient(this.mDeviceInfo);
            } else {
                client = getCargoClient(suppressSingleDevice);
            }
            if (client == null) {
                throw new CargoException("No device connected!", BandServiceMessage.Response.DEVICE_NOT_CONNECTED_ERROR);
            }
            client.connectDevice(!isQueueAction);
            if (!isQueueAction) {
                if (deviceInfo == null && !suppressSingleDevice && this.mSettingsProvider.getFreStatus() == FreStatus.SHOWN && !this.mSingleDeviceEnforcementManager.isUserPairedDevice(client)) {
                    client = repairWithCorrectDevice(client);
                }
                if (checkForFirmwareUpdate && FirmwareUpdateUtils.isFirmwareUpdateCheckRequired(this.mSettingsProvider)) {
                    performFirmwareUpdateCheck();
                }
            }
            result = executor.execute(client);
            if (client != null) {
                client.destroy();
            }
        } catch (CargoException ex) {
            throw ex;
        }
        return result;
    }

    private void performFirmwareUpdateCheck() throws CargoException {
        boolean firmwareCheckPassed = true;
        try {
            CargoFirmwareUpdateInfo firmwareUpdateInfo = getLatestAvailableFirmwareVersion();
            if (firmwareUpdateInfo != null && firmwareUpdateInfo.isFirmwareUpdateAvailable() && !firmwareUpdateInfo.isFirmwareUpdateOptional() && this.mSettingsProvider.getFreStatus() == FreStatus.SHOWN) {
                KLog.d(TAG, "Unable to execute method as a mandatory firmware update is required!");
                firmwareCheckPassed = false;
            }
        } catch (Exception e) {
            KLog.d(TAG, "Unable to check for firmware updates");
            firmwareCheckPassed = true;
        }
        if (!firmwareCheckPassed) {
            Intent firmwareUpdateIntent = new Intent(Constants.FIRMWARE_UPDATE_REQUIRED_INTENT);
            LocalBroadcastManager.getInstance(this.mContext).sendBroadcast(firmwareUpdateIntent);
            throw new CargoException("Firmware update pending", BandServiceMessage.Response.DEVICE_FIRMWARE_VERSION_INCOMPATIBLE_ERROR);
        }
    }

    private CargoClient repairWithCorrectDevice(CargoClient client) throws CargoException {
        client.destroy();
        CargoClient client2 = getCargoClient(true);
        if (client2 == null) {
            throw new CargoException("No device found that belongs to user!", BandServiceMessage.Response.DEVICE_NOT_CONNECTED_ERROR);
        }
        client2.connectDevice();
        return client2;
    }

    private CargoClient getCargoClient(DeviceInfo device) throws CargoException {
        CargoServiceInfo serviceInfo = getServiceInfo();
        if (serviceInfo != null) {
            CargoClient cargoClient = CargoClient.create(this.mContext, serviceInfo, device);
            if (cargoClient != null) {
                cargoClient.waitForServiceToBind();
                return cargoClient;
            }
            return cargoClient;
        }
        KLog.i(TAG, "Cargo Client could not be created!");
        return null;
    }

    private CargoClient getCargoClient(boolean suppressSingleDevice) throws CargoException {
        DeviceInfo device = getConnectedDevice(suppressSingleDevice, false);
        if (device != null) {
            return getCargoClient(device);
        }
        return null;
    }

    private <T> T executeCloudClient(CargoCloudConnectionExecutor<T> executor) throws CargoException {
        return (T) executeCloudClient(null, executor);
    }

    private synchronized <T> T executeCloudClient(CargoCloudClient cloudClient, CargoCloudConnectionExecutor<T> executor) throws CargoException {
        T result;
        result = null;
        if (cloudClient == null) {
            cloudClient = CargoCloudClient.create(this.mContext, getServiceInfo());
        }
        if (cloudClient != null) {
            cloudClient.waitForServiceToBind();
            result = executor.execute(cloudClient);
        }
        if (cloudClient != null) {
            cloudClient.destroy();
        }
        return result;
    }

    @Override // com.microsoft.kapp.CargoConnection
    public void setSmsReplyRequestedListener(SmsReplyRequestedListener smsReplyListener) {
        this.mSmsReplyListener = smsReplyListener;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class CargoLocalBroadcastReceiver extends BroadcastReceiver {
        private static final String CargoLocalBroadcastReceiverTAG = "CargoLocalBroadcastReceiver";

        private CargoLocalBroadcastReceiver() {
        }

        public void register(Context context) {
            LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(context);
            localBroadcastManager.registerReceiver(this, new IntentFilter(CargoConstants.ACTION_DEVICE_CONNECTED));
            localBroadcastManager.registerReceiver(this, new IntentFilter(CargoConstants.ACTION_DEVICE_DISCONNECTED));
            localBroadcastManager.registerReceiver(this, new IntentFilter(CargoConstants.ACTION_SYNC_WEB_TILE_COMPLETED));
            localBroadcastManager.registerReceiver(this, new IntentFilter(CargoConstants.ACTION_SYNC_DEVICE_TO_CLOUD_STARTED));
            localBroadcastManager.registerReceiver(this, new IntentFilter(CargoConstants.ACTION_SYNC_DEVICE_TO_CLOUD_COMPLETED));
            localBroadcastManager.registerReceiver(this, new IntentFilter(CargoConstants.ACTION_DOWNLOAD_FIRMWARE_UPDATE_STARTED));
            localBroadcastManager.registerReceiver(this, new IntentFilter(CargoConstants.ACTION_DOWNLOAD_FIRMWARE_UPDATE_COMPLETED));
            localBroadcastManager.registerReceiver(this, new IntentFilter(CargoConstants.ACTION_UPGRADE_FIRMWARE_STARTED));
            localBroadcastManager.registerReceiver(this, new IntentFilter(CargoConstants.ACTION_UPGRADE_FIRMWARE_COMPLETED));
            localBroadcastManager.registerReceiver(this, new IntentFilter(CargoConstants.ACTION_DOWNLOAD_EPHEMERIS_UPDATE_STARTED));
            localBroadcastManager.registerReceiver(this, new IntentFilter(CargoConstants.ACTION_DOWNLOAD_EPHEMERIS_UPDATE_COMPLETED));
            localBroadcastManager.registerReceiver(this, new IntentFilter(CargoConstants.ACTION_UPGRADE_EPHEMERIS_STARTED));
            localBroadcastManager.registerReceiver(this, new IntentFilter(CargoConstants.ACTION_UPGRADE_EPHEMERIS_COMPLETED));
            localBroadcastManager.registerReceiver(this, new IntentFilter(CargoConstants.ACTION_DOWNLOAD_TIME_ZONE_SETTINGS_UPDATE_STARTED));
            localBroadcastManager.registerReceiver(this, new IntentFilter(CargoConstants.ACTION_DOWNLOAD_TIME_ZONE_SETTINGS_UPDATE_COMPLETED));
            localBroadcastManager.registerReceiver(this, new IntentFilter(CargoConstants.ACTION_UPGRADE_TIME_ZONE_SETTINGS_STARTED));
            localBroadcastManager.registerReceiver(this, new IntentFilter(CargoConstants.ACTION_UPGRADE_TIME_ZONE_SETTINGS_COMPLETED));
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(CargoConstants.ACTION_PUSH_SMS_DISMISSED);
            intentFilter.addAction(CargoConstants.ACTION_PUSH_CALL_DISMISSED);
            CargoConnectionProxy.this.mContext.registerReceiver(CargoConnectionProxy.this.mCargoLocalBroadcastReceiver, intentFilter);
        }

        public void unregister(Context context) {
            LocalBroadcastManager.getInstance(context).unregisterReceiver(this);
        }

        public BandServiceMessage.Response waitForFirmwareDownloadToComplete() {
            CargoFirmwareUpdateListener updateListener = CargoConnectionProxy.this.mCargoFirmwareUpdateListener;
            if (updateListener == null || !waitForFlag(updateListener.getDownloadFirmwareLatch(), true)) {
                return null;
            }
            waitForFlag(updateListener.getDownloadFirmwareLatch(), false);
            return updateListener.getError();
        }

        public BandServiceMessage.Response waitForFirmwareUpgradeToComplete() {
            CargoFirmwareUpdateListener updateListener = CargoConnectionProxy.this.mCargoFirmwareUpdateListener;
            if (updateListener == null || !waitForFlag(updateListener.getUpgradeFirmwareLatch(), true)) {
                return null;
            }
            waitForFlag(updateListener.getUpgradeFirmwareLatch(), false);
            return updateListener.getError();
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if (intent == null) {
                KLog.e(CargoConnectionProxy.TAG, "Broadcast intent from KDK should not be null!");
                return;
            }
            String action = intent.getAction();
            if (action == null) {
                KLog.e(CargoConnectionProxy.TAG, "Action cannot be null in KDK broadcast intent=%s", intent.toString());
                return;
            }
            if (action.equals(CargoConstants.ACTION_SYNC_PROGRESS)) {
                SyncProgressListener syncListener = CargoConnectionProxy.this.mSyncProgressListener;
                if (syncListener != null) {
                    syncListener.onSyncProgress(intent.getIntExtra(CargoConstants.PROGRESS_VALUE, 0));
                }
            } else if (action.equals(CargoConstants.ACTION_SYNC_DEVICE_TO_CLOUD_STARTED)) {
                KLog.i(CargoLocalBroadcastReceiverTAG, "Sync Device to Cloud started...");
                CargoSyncListener syncListener2 = CargoConnectionProxy.this.mCargoSyncListener;
                BandServiceMessage.Response response = checkForErrorMessage(intent.getExtras().get(CargoConstants.EXTRA_RESULT_CODE));
                checkForCargoSyncError(response, syncListener2);
            } else if (action.equals(CargoConstants.ACTION_SYNC_DEVICE_TO_CLOUD_COMPLETED)) {
                SyncResult syncResult = (SyncResult) intent.getParcelableExtra(CargoConstants.EXTRA_SYNC_RESULT);
                ArrayList<CloudDataResource> cloudDataList = intent.getParcelableArrayListExtra(CargoConstants.EXTRA_CLOUD_DATA);
                KLog.i(CargoLocalBroadcastReceiverTAG, String.format("Sync Device to Cloud completed: %s", syncResult));
                CargoSyncListener syncListener3 = CargoConnectionProxy.this.mCargoSyncListener;
                BandServiceMessage.Response response2 = checkForErrorMessage(intent.getExtras().get(CargoConstants.EXTRA_RESULT_CODE));
                checkForCargoSyncError(response2, syncListener3);
                if (syncListener3 != null) {
                    syncListener3.onSyncDeviceToCloudCompleted(syncResult, cloudDataList);
                }
            } else if (action.equals(CargoConstants.ACTION_SYNC_WEB_TILE_COMPLETED)) {
                SyncResult syncResult2 = (SyncResult) intent.getParcelableExtra(CargoConstants.EXTRA_DOWNLOAD_SYNC_RESULT);
                KLog.i(CargoLocalBroadcastReceiverTAG, String.format("Sync Webtiles to Cloud completed: %s", syncResult2));
                CargoSyncListener syncListener4 = CargoConnectionProxy.this.mCargoSyncListener;
                BandServiceMessage.Response response3 = checkForErrorMessage(intent.getExtras().get(CargoConstants.EXTRA_RESULT_CODE));
                checkForCargoSyncError(response3, syncListener4);
                if (syncListener4 != null) {
                    syncListener4.onSyncWebtilesCompleted(syncResult2);
                }
            } else if (action.equals(CargoConstants.ACTION_DEVICE_CONNECTED)) {
                CargoConnectionProxy.this.logInfoAndPublicMessage(CargoLocalBroadcastReceiverTAG, "Device connected", intent.getParcelableExtra(InternalBandConstants.EXTRA_DEVICE_INFO));
            } else if (action.equals(CargoConstants.ACTION_DEVICE_DISCONNECTED)) {
                CargoConnectionProxy.this.logInfoAndPublicMessage(CargoLocalBroadcastReceiverTAG, "Device disconnected", intent.getParcelableExtra(InternalBandConstants.EXTRA_DEVICE_INFO));
            } else if (action.equals(CargoConstants.ACTION_DOWNLOAD_FIRMWARE_UPDATE_STARTED)) {
                CargoConnectionProxy.this.logInfoAndPublicMessage(CargoLocalBroadcastReceiverTAG, "Download firware update started", intent.getParcelableExtra(CargoConstants.EXTRA_CLOUD_DATA));
                BandServiceMessage.Response response4 = checkForErrorMessage(intent.getExtras().get(CargoConstants.EXTRA_RESULT_CODE));
                CargoFirmwareUpdateListener updateListener = CargoConnectionProxy.this.mCargoFirmwareUpdateListener;
                if (updateListener != null) {
                    if (response4 == null || !response4.isError()) {
                        setFlag(updateListener.getDownloadFirmwareLatch(), true);
                    } else {
                        updateListener.setError(response4);
                        setFlag(updateListener.getDownloadFirmwareLatch(), false);
                    }
                }
            } else if (action.equals(CargoConstants.ACTION_DOWNLOAD_FIRMWARE_UPDATE_COMPLETED)) {
                CargoConnectionProxy.this.logInfoAndPublicMessage(CargoLocalBroadcastReceiverTAG, "Download firmware update completed", intent.getParcelableExtra(CargoConstants.EXTRA_CLOUD_DATA));
                BandServiceMessage.Response response5 = checkForErrorMessage(intent.getExtras().get(CargoConstants.EXTRA_RESULT_CODE));
                CargoFirmwareUpdateListener updateListener2 = CargoConnectionProxy.this.mCargoFirmwareUpdateListener;
                if (updateListener2 != null) {
                    if (response5 != null && response5.isError()) {
                        updateListener2.setError(response5);
                        setFlag(updateListener2.getDownloadFirmwareLatch(), true);
                    } else {
                        setFlag(updateListener2.getDownloadFirmwareLatch(), false);
                    }
                }
            } else if (action.equals(CargoConstants.ACTION_UPGRADE_FIRMWARE_STARTED)) {
                CargoConnectionProxy.this.logInfoAndPublicMessage(CargoLocalBroadcastReceiverTAG, "Upgrade Firmware started", intent.getParcelableExtra(InternalBandConstants.EXTRA_DEVICE_INFO));
                BandServiceMessage.Response response6 = checkForErrorMessage(intent.getExtras().get(CargoConstants.EXTRA_RESULT_CODE));
                CargoFirmwareUpdateListener updateListener3 = CargoConnectionProxy.this.mCargoFirmwareUpdateListener;
                if (updateListener3 != null) {
                    if (response6 != null && response6.isError()) {
                        updateListener3.setError(response6);
                    }
                    setFlag(updateListener3.getUpgradeFirmwareLatch(), true);
                }
            } else if (action.equals(CargoConstants.ACTION_UPGRADE_FIRMWARE_COMPLETED)) {
                CargoConnectionProxy.this.logInfoAndPublicMessage(CargoLocalBroadcastReceiverTAG, "Upgrade Firmware completed", intent.getParcelableExtra(InternalBandConstants.EXTRA_DEVICE_INFO));
                BandServiceMessage.Response response7 = checkForErrorMessage(intent.getExtras().get(CargoConstants.EXTRA_RESULT_CODE));
                CargoFirmwareUpdateListener updateListener4 = CargoConnectionProxy.this.mCargoFirmwareUpdateListener;
                if (updateListener4 != null) {
                    if (response7 != null && response7.isError()) {
                        updateListener4.setError(response7);
                    }
                    setFlag(updateListener4.getUpgradeFirmwareLatch(), false);
                }
            } else if (action.equals(CargoConstants.ACTION_DOWNLOAD_EPHEMERIS_UPDATE_STARTED)) {
                CargoConnectionProxy.this.logInfoAndPublicMessage(CargoLocalBroadcastReceiverTAG, "Download Ephemeris update started", intent.getParcelableExtra(CargoConstants.EXTRA_CLOUD_DATA));
                BandServiceMessage.Response response8 = checkForErrorMessage(intent.getExtras().get(CargoConstants.EXTRA_RESULT_CODE));
                checkForEphemerisTimeZoneError(response8);
            } else if (action.equals(CargoConstants.ACTION_DOWNLOAD_EPHEMERIS_UPDATE_COMPLETED)) {
                CargoConnectionProxy.this.logInfoAndPublicMessage(CargoLocalBroadcastReceiverTAG, "Download Ephemeris update completed", intent.getParcelableExtra(CargoConstants.EXTRA_CLOUD_DATA));
                BandServiceMessage.Response response9 = checkForErrorMessage(intent.getExtras().get(CargoConstants.EXTRA_RESULT_CODE));
                EphemerisTimeZoneUpdateListener updateListener5 = CargoConnectionProxy.this.mEphemerisTimeZoneUpdateListener;
                if (updateListener5 != null) {
                    updateListener5.onEphemerisDownloadCompleted(response9);
                }
            } else if (action.equals(CargoConstants.ACTION_UPGRADE_EPHEMERIS_STARTED)) {
                CargoConnectionProxy.this.logInfoAndPublicMessage(CargoLocalBroadcastReceiverTAG, "Upgrade Ephemeris started", intent.getParcelableExtra(InternalBandConstants.EXTRA_DEVICE_INFO));
                BandServiceMessage.Response response10 = checkForErrorMessage(intent.getExtras().get(CargoConstants.EXTRA_RESULT_CODE));
                checkForEphemerisTimeZoneError(response10);
            } else if (action.equals(CargoConstants.ACTION_UPGRADE_EPHEMERIS_COMPLETED)) {
                CargoConnectionProxy.this.logInfoAndPublicMessage(CargoLocalBroadcastReceiverTAG, "Upgrade Ephemeris completed", intent.getParcelableExtra(InternalBandConstants.EXTRA_DEVICE_INFO));
            } else if (action.equals(CargoConstants.ACTION_DOWNLOAD_TIME_ZONE_SETTINGS_UPDATE_STARTED)) {
                CargoConnectionProxy.this.logInfoAndPublicMessage(CargoLocalBroadcastReceiverTAG, "Download Time Zone Settings Update started", intent.getParcelableExtra(CargoConstants.EXTRA_CLOUD_DATA));
                BandServiceMessage.Response response11 = checkForErrorMessage(intent.getExtras().get(CargoConstants.EXTRA_RESULT_CODE));
                checkForEphemerisTimeZoneError(response11);
            } else if (action.equals(CargoConstants.ACTION_DOWNLOAD_TIME_ZONE_SETTINGS_UPDATE_COMPLETED)) {
                CargoConnectionProxy.this.logInfoAndPublicMessage(CargoLocalBroadcastReceiverTAG, "Download Time Zone Settings Update completed", intent.getParcelableExtra(CargoConstants.EXTRA_CLOUD_DATA));
                BandServiceMessage.Response response12 = checkForErrorMessage(intent.getExtras().get(CargoConstants.EXTRA_RESULT_CODE));
                EphemerisTimeZoneUpdateListener updateListener6 = CargoConnectionProxy.this.mEphemerisTimeZoneUpdateListener;
                if (updateListener6 != null) {
                    updateListener6.onTimeZoneDownloadCompleted(response12);
                }
            } else if (action.equals(CargoConstants.ACTION_UPGRADE_TIME_ZONE_SETTINGS_STARTED)) {
                CargoConnectionProxy.this.logInfoAndPublicMessage(CargoLocalBroadcastReceiverTAG, "Upgrade Time Zone Settings started", intent.getParcelableExtra(InternalBandConstants.EXTRA_DEVICE_INFO));
                BandServiceMessage.Response response13 = checkForErrorMessage(intent.getExtras().get(CargoConstants.EXTRA_RESULT_CODE));
                checkForEphemerisTimeZoneError(response13);
            } else if (action.equals(CargoConstants.ACTION_UPGRADE_TIME_ZONE_SETTINGS_COMPLETED)) {
                CargoConnectionProxy.this.logInfoAndPublicMessage(CargoLocalBroadcastReceiverTAG, "Upgrade Time Zone Settings completed", intent.getParcelableExtra(InternalBandConstants.EXTRA_DEVICE_INFO));
                BandServiceMessage.Response response14 = checkForErrorMessage(intent.getExtras().get(CargoConstants.EXTRA_RESULT_CODE));
                checkForEphemerisTimeZoneError(response14);
            } else if (action.equals(CargoConstants.ACTION_PUSH_CALL_DISMISSED)) {
                KLog.i(CargoLocalBroadcastReceiverTAG, "Dismissed Call");
                Executor eS = Executors.newSingleThreadExecutor();
                eS.execute(new Runnable() { // from class: com.microsoft.kapp.CargoConnectionProxy.CargoLocalBroadcastReceiver.1
                    @Override // java.lang.Runnable
                    public void run() {
                        CargoConnectionProxy.this.mCallDismissManager.dismissCall();
                    }
                });
            } else if (action.equals(CargoConstants.ACTION_PUSH_SMS_DISMISSED)) {
                KLog.i(CargoLocalBroadcastReceiverTAG, "Reply Canned SMS");
                notifySmsReplyRequested(intent);
            } else {
                KLog.i(CargoLocalBroadcastReceiverTAG, "Untrapped Action: " + action);
            }
            KLog.i(CargoLocalBroadcastReceiverTAG, String.format("Local broadcast received: %s", intent));
        }

        private void checkForCargoSyncError(BandServiceMessage.Response response, CargoSyncListener syncListener) {
            if (syncListener != null && response != null) {
                syncListener.setError(response);
            }
        }

        private void checkForEphemerisTimeZoneError(BandServiceMessage.Response response) {
            EphemerisTimeZoneUpdateListener updateListener = CargoConnectionProxy.this.mEphemerisTimeZoneUpdateListener;
            if (updateListener != null && response != null) {
                updateListener.onDownloadError();
            }
        }

        private BandServiceMessage.Response checkForErrorMessage(Object responseExtra) {
            if (responseExtra != null) {
                BandServiceMessage.Response response = BandServiceMessage.Response.lookup(((Integer) responseExtra).intValue());
                if (response.isError()) {
                    return response;
                }
            }
            return null;
        }

        private void notifySmsReplyRequested(Intent intent) {
            if (CargoConnectionProxy.this.mSmsReplyListener != null && intent != null) {
                TextMessageData textMessageData = (TextMessageData) intent.getParcelableExtra(InternalBandConstants.EXTRA_SUBSCRIPTION_DATA);
                String usePhoneNumber = (String) CargoConnectionProxy.this.mSmsPhoneHashMap.get(Integer.valueOf(textMessageData.getThreadID()));
                if (usePhoneNumber != null) {
                    CargoConnectionProxy.this.mSmsReplyListener.onSmsReplyRequested(textMessageData.getMessage(), usePhoneNumber);
                } else {
                    Log.i(CargoConnectionProxy.TAG, "Not sending SMS Replay - ThreadID not found in HashMap  ThreadID = " + String.valueOf(textMessageData.getThreadID()));
                }
            }
        }

        private void setFlag(AtomicBoolean flag, boolean state) {
            synchronized (flag) {
                if (flag.get() != state) {
                    flag.set(state);
                    flag.notifyAll();
                }
            }
        }

        private boolean waitForFlag(AtomicBoolean flag, boolean state) {
            boolean z;
            synchronized (flag) {
                if (flag.get() != state) {
                    try {
                        flag.wait();
                    } catch (InterruptedException e) {
                        KLog.v(CargoLocalBroadcastReceiverTAG, "Interrupted while waiting to flag state change.");
                    }
                }
                flag.notifyAll();
                z = flag.get() == state;
            }
            return z;
        }
    }

    @Override // com.microsoft.kapp.CargoConnection
    public CargoUserProfile getUserCloudProfile(String phsUrl, String kAccessToken, String FUSEndpoint) throws CargoException {
        KLog.i(TAG, "4: Creating cloud client");
        CargoCloudClient cloudClient = CargoCloudClient.create(this.mContext, getCargoServiceInfo(phsUrl, kAccessToken, FUSEndpoint));
        KLog.i(TAG, "5: Client created, executing");
        UserProfileInfo userProfileInfo = (UserProfileInfo) executeCloudClient(cloudClient, new CargoCloudConnectionExecutor<UserProfileInfo>() { // from class: com.microsoft.kapp.CargoConnectionProxy.27
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // com.microsoft.kapp.CargoCloudConnectionExecutor
            public UserProfileInfo execute(CargoCloudClient client) throws CargoException {
                KLog.i(CargoConnectionProxy.TAG, "6: CloudClient retrieving user profile");
                return client.getCloudProfile();
            }
        });
        KLog.i(TAG, "7: Cargo user profile retrieved");
        return new CargoUserProfile(userProfileInfo);
    }

    @Override // com.microsoft.kapp.CargoConnection
    public void importUserProfile() throws CargoException {
        execute(new CargoConnectionExecutor<Void>() { // from class: com.microsoft.kapp.CargoConnectionProxy.28
            @Override // com.microsoft.kapp.CargoConnectionExecutor
            public Void execute(CargoClient client) throws CargoException {
                client.ImportUserProfile();
                return null;
            }
        });
    }

    @Override // com.microsoft.kapp.CargoConnection
    public boolean saveUserCloudProfile(final CargoUserProfile userProfile, final long millis) {
        try {
            Boolean returnVal = (Boolean) executeCloudClient(new CargoCloudConnectionExecutor<Boolean>() { // from class: com.microsoft.kapp.CargoConnectionProxy.29
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // com.microsoft.kapp.CargoCloudConnectionExecutor
                public Boolean execute(CargoCloudClient client) throws CargoException {
                    UserProfileInfo cloudProfile = new UserProfileInfo();
                    userProfile.ApplyToProfile(cloudProfile);
                    client.saveCloudProfile(cloudProfile, millis);
                    return true;
                }
            });
            if (returnVal == null) {
                return false;
            }
            return returnVal.booleanValue();
        } catch (CargoException ex) {
            KLog.w(TAG, "Error saving user cloud profile!", ex);
            return false;
        }
    }

    @Override // com.microsoft.kapp.CargoConnection
    public boolean saveUserBandProfile(final CargoUserProfile userProfile, final long millis) {
        try {
            Boolean returnVal = (Boolean) execute(new CargoConnectionExecutor<Boolean>() { // from class: com.microsoft.kapp.CargoConnectionProxy.30
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // com.microsoft.kapp.CargoConnectionExecutor
                public Boolean execute(CargoClient client) throws CargoException {
                    UserProfileInfo profile = new UserProfileInfo();
                    userProfile.ApplyToProfile(profile);
                    DeviceProfileInfo deviceProfile = client.getDeviceProfile();
                    deviceProfile.updateUsingCloudUserProfileRegionUpdates(profile);
                    client.getDeviceClient().saveDeviceProfile(deviceProfile, millis);
                    return true;
                }
            });
            if (returnVal == null) {
                return false;
            }
            return returnVal.booleanValue();
        } catch (CargoException ex) {
            KLog.w(TAG, "Error saving user cloud profile!", ex);
            return false;
        }
    }

    @Override // com.microsoft.kapp.CargoConnection
    public void setUserProfileUnitPrefs(final boolean weightUnitType, final boolean distanceUnitType, final boolean temperatureUnitType) throws CargoException {
        executeCloudClient(new CargoCloudConnectionExecutor<Void>() { // from class: com.microsoft.kapp.CargoConnectionProxy.31
            @Override // com.microsoft.kapp.CargoCloudConnectionExecutor
            public Void execute(CargoCloudClient client) throws CargoException {
                client.setUserProfileUnitPrefs(weightUnitType ? UnitType.METRIC : UnitType.IMPERIAL, distanceUnitType ? UnitType.METRIC : UnitType.IMPERIAL, temperatureUnitType ? UnitType.METRIC : UnitType.IMPERIAL);
                return null;
            }
        });
    }

    @Override // com.microsoft.kapp.CargoConnection
    public void setDeviceProfileUnitPrefs(final boolean weightUnitType, final boolean distanceUnitType, final boolean temperatureUnitType) throws CargoException {
        execute(new CargoConnectionExecutor<Void>() { // from class: com.microsoft.kapp.CargoConnectionProxy.32
            @Override // com.microsoft.kapp.CargoConnectionExecutor
            public Void execute(CargoClient client) throws CargoException {
                client.setDeviceProfileUnitPrefs(weightUnitType ? UnitType.METRIC : UnitType.IMPERIAL, distanceUnitType ? UnitType.METRIC : UnitType.IMPERIAL, temperatureUnitType ? UnitType.METRIC : UnitType.IMPERIAL);
                return null;
            }
        });
    }

    @Override // com.microsoft.kapp.CargoConnection
    public void setOOBEComplete() throws CargoException {
        setOOBECompleteTo(true);
    }

    @Override // com.microsoft.kapp.CargoConnection
    public void resetOOBEComplete() throws CargoException {
        setOOBECompleteTo(false);
    }

    private void setOOBECompleteTo(final boolean isOobeComplete) throws CargoException {
        executeCloudClient(new CargoCloudConnectionExecutor<Void>() { // from class: com.microsoft.kapp.CargoConnectionProxy.33
            @Override // com.microsoft.kapp.CargoCloudConnectionExecutor
            public Void execute(CargoCloudClient client) throws CargoException {
                UserProfileInfo cloudProfile = new UserProfileInfo();
                cloudProfile.setHasCompletedOOBE(isOobeComplete);
                client.saveCloudProfile(cloudProfile, System.currentTimeMillis());
                return null;
            }
        });
    }

    @Override // com.microsoft.kapp.CargoConnection
    public void setDeviceName(final String deviceName) throws CargoException {
        execute(new CargoConnectionExecutor<Void>() { // from class: com.microsoft.kapp.CargoConnectionProxy.34
            @Override // com.microsoft.kapp.CargoConnectionExecutor
            public Void execute(CargoClient client) throws CargoException {
                DeviceProfileInfo profile = client.getDeviceProfile();
                profile.setDeviceName(deviceName);
                client.saveUserProfile(profile);
                return null;
            }
        });
    }

    @Override // com.microsoft.kapp.CargoConnection
    public void setTelemetryFlag(final boolean isTelemetryEnabled) throws CargoException {
        execute(new CargoConnectionExecutor<Void>() { // from class: com.microsoft.kapp.CargoConnectionProxy.35
            @Override // com.microsoft.kapp.CargoConnectionExecutor
            public Void execute(CargoClient client) throws CargoException {
                DeviceProfileInfo profile = client.getDeviceProfile();
                profile.setTelemetryEnabled(isTelemetryEnabled);
                client.saveDeviceProfile(profile);
                return null;
            }
        });
    }

    @Override // com.microsoft.kapp.CargoConnection
    public void setCloudProfileTelemetryFlag(final boolean isTelemetryEnabled) throws CargoException {
        executeCloudClient(new CargoCloudConnectionExecutor<Boolean>() { // from class: com.microsoft.kapp.CargoConnectionProxy.36
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // com.microsoft.kapp.CargoCloudConnectionExecutor
            public Boolean execute(CargoCloudClient client) throws CargoException {
                UserProfileInfo cloudProfile = client.getCloudProfile();
                cloudProfile.setTelemetryEnabled(isTelemetryEnabled);
                client.saveCloudProfile(cloudProfile, System.currentTimeMillis());
                return null;
            }
        });
    }

    @Override // com.microsoft.kapp.CargoConnection
    public String getDeviceSerialNo() throws CargoException {
        String deviceSerialNo = (String) execute(new CargoConnectionExecutor<String>() { // from class: com.microsoft.kapp.CargoConnectionProxy.37
            @Override // com.microsoft.kapp.CargoConnectionExecutor
            public String execute(CargoClient client) throws CargoException {
                return client.getProductSerialNumber();
            }
        });
        return deviceSerialNo;
    }

    @Override // com.microsoft.kapp.CargoConnection
    public String getDeviceName() throws CargoException {
        String deviceName = (String) execute(new CargoConnectionExecutor<String>() { // from class: com.microsoft.kapp.CargoConnectionProxy.38
            @Override // com.microsoft.kapp.CargoConnectionExecutor
            public String execute(CargoClient client) throws CargoException {
                DeviceProfileInfo deviceProfileInfo = client.getDeviceProfile();
                return deviceProfileInfo.getDeviceName();
            }
        });
        return deviceName;
    }

    @Override // com.microsoft.kapp.CargoConnection
    public synchronized int getDeviceBatteryLevel() {
        this.mBatteryValue = 0;
        this.mBatteryCompleteLatch = new CountDownLatch(1);
        try {
            execute(new CargoConnectionExecutor<Void>() { // from class: com.microsoft.kapp.CargoConnectionProxy.39
                @Override // com.microsoft.kapp.CargoConnectionExecutor
                public Void execute(CargoClient client) throws CargoException {
                    try {
                        CargoConnectionProxy.this.mSensorManager = client.getDeviceClient().getKDKSensorManager();
                        CargoConnectionProxy.this.mSensorManager.registerBatteryLevelEventListener(CargoConnectionProxy.this.mBatteryLevelEventListener);
                        CargoConnectionProxy.this.mBatteryCompleteLatch.await(20000L, TimeUnit.MILLISECONDS);
                        return null;
                    } catch (Exception e) {
                        KLog.w(CargoConnectionProxy.TAG, "Could not retrieve device battery!", e);
                        return null;
                    } finally {
                        CargoConnectionProxy.this.unregisterBatteryEventListener();
                    }
                }
            });
        } catch (CargoException e) {
            KLog.w(TAG, "Could not retrieve device battery!", e);
        }
        return this.mBatteryValue;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void unregisterBatteryEventListener() {
        if (this.mSensorManager != null && this.mBatteryLevelEventListener != null) {
            try {
                this.mSensorManager.unregisterBatteryLevelEventListener(this.mBatteryLevelEventListener);
            } catch (Exception e) {
                KLog.w(TAG, "Could not unregister!", e);
            }
        }
    }

    @Override // com.microsoft.kapp.CargoConnection
    public boolean setNotificationsEnabled(final UUID strappId, final boolean enabled) {
        Boolean success;
        Boolean.valueOf(false);
        try {
            success = (Boolean) execute(new CargoConnectionExecutor<Boolean>() { // from class: com.microsoft.kapp.CargoConnectionProxy.41
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // com.microsoft.kapp.CargoConnectionExecutor
                public Boolean execute(CargoClient client) throws CargoException {
                    if (enabled) {
                        client.setStrappSettingMask(strappId, 3);
                    } else {
                        client.setStrappSettingMask(strappId, 2);
                    }
                    return true;
                }
            });
        } catch (CargoException ex) {
            KLog.w(TAG, "Exception encountered while saving notifications setting to device.", ex);
            success = false;
        }
        if (success == null) {
            return false;
        }
        return success.booleanValue();
    }

    @Override // com.microsoft.kapp.CargoConnection
    public boolean savePhoneCallResponses(String m1, String m2, String m3, String m4) {
        return saveResponses(SmsResponseType.CALL, m1, m2, m3, m4);
    }

    @Override // com.microsoft.kapp.CargoConnection
    public boolean saveSmsResponses(String m1, String m2, String m3, String m4) {
        return saveResponses(SmsResponseType.SMS, m1, m2, m3, m4);
    }

    private boolean saveResponses(final SmsResponseType smsResponseType, final String m1, final String m2, final String m3, final String m4) {
        Boolean success;
        Boolean.valueOf(false);
        try {
            success = (Boolean) execute(new CargoConnectionExecutor<Boolean>() { // from class: com.microsoft.kapp.CargoConnectionProxy.42
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // com.microsoft.kapp.CargoConnectionExecutor
                public Boolean execute(CargoClient client) throws CargoException {
                    switch (AnonymousClass60.$SwitchMap$com$microsoft$band$device$command$SmsResponseType[smsResponseType.ordinal()]) {
                        case 1:
                            client.getDeviceClient().setPhoneCallResponses(m1, m2, m3, m4);
                            break;
                        case 2:
                            client.getDeviceClient().setSmsResponses(m1, m2, m3, m4);
                            break;
                        default:
                            return false;
                    }
                    return true;
                }
            });
        } catch (CargoException ex) {
            KLog.w(TAG, "Exception encountered while saving responses to device.", ex);
            success = false;
        }
        if (success == null) {
            return false;
        }
        return success.booleanValue();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.microsoft.kapp.CargoConnectionProxy$60  reason: invalid class name */
    /* loaded from: classes.dex */
    public static /* synthetic */ class AnonymousClass60 {
        static final /* synthetic */ int[] $SwitchMap$com$microsoft$band$OOBEStage;
        static final /* synthetic */ int[] $SwitchMap$com$microsoft$band$device$command$SmsResponseType = new int[SmsResponseType.values().length];

        static {
            try {
                $SwitchMap$com$microsoft$band$device$command$SmsResponseType[SmsResponseType.CALL.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$com$microsoft$band$device$command$SmsResponseType[SmsResponseType.SMS.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            $SwitchMap$com$microsoft$band$OOBEStage = new int[OOBEStage.values().length];
            try {
                $SwitchMap$com$microsoft$band$OOBEStage[OOBEStage.CHECKING_FOR_UPDATE.ordinal()] = 1;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$com$microsoft$band$OOBEStage[OOBEStage.ERROR_STATE.ordinal()] = 2;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$com$microsoft$band$OOBEStage[OOBEStage.PAIRING_SUCCESS.ordinal()] = 3;
            } catch (NoSuchFieldError e5) {
            }
            try {
                $SwitchMap$com$microsoft$band$OOBEStage[OOBEStage.STARTING_UPDATE.ordinal()] = 4;
            } catch (NoSuchFieldError e6) {
            }
            try {
                $SwitchMap$com$microsoft$band$OOBEStage[OOBEStage.UPDATE_COMPLETE.ordinal()] = 5;
            } catch (NoSuchFieldError e7) {
            }
            try {
                $SwitchMap$com$microsoft$band$OOBEStage[OOBEStage.WAITING_ON_PHONE_TO_COMPLETE_OOBE.ordinal()] = 6;
            } catch (NoSuchFieldError e8) {
            }
            $SwitchMap$com$microsoft$kapp$calendar$CalendarEvent$Availability = new int[CalendarEvent.Availability.values().length];
            try {
                $SwitchMap$com$microsoft$kapp$calendar$CalendarEvent$Availability[CalendarEvent.Availability.BUSY.ordinal()] = 1;
            } catch (NoSuchFieldError e9) {
            }
            try {
                $SwitchMap$com$microsoft$kapp$calendar$CalendarEvent$Availability[CalendarEvent.Availability.UNKNOWN.ordinal()] = 2;
            } catch (NoSuchFieldError e10) {
            }
            try {
                $SwitchMap$com$microsoft$kapp$calendar$CalendarEvent$Availability[CalendarEvent.Availability.TENTATIVE.ordinal()] = 3;
            } catch (NoSuchFieldError e11) {
            }
            try {
                $SwitchMap$com$microsoft$kapp$calendar$CalendarEvent$Availability[CalendarEvent.Availability.FREE.ordinal()] = 4;
            } catch (NoSuchFieldError e12) {
            }
        }
    }

    @Override // com.microsoft.kapp.CargoConnection
    public boolean setRunMetricsOrder(final RunDisplayMetricType... metrics) {
        Boolean success;
        Boolean.valueOf(false);
        try {
            success = (Boolean) execute(new CargoConnectionExecutor<Boolean>() { // from class: com.microsoft.kapp.CargoConnectionProxy.43
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // com.microsoft.kapp.CargoConnectionExecutor
                public Boolean execute(CargoClient client) throws CargoException {
                    client.getDeviceClient().setRunDisplayMetrics(metrics);
                    return true;
                }
            });
        } catch (CargoException ex) {
            KLog.w(TAG, "Exception encountered while saving run metrics order to device.", ex);
            success = false;
        } catch (IllegalArgumentException ex2) {
            KLog.e(TAG, "Exception encountered while saving run metrics order to device.", ex2);
            success = false;
        }
        if (success == null) {
            return false;
        }
        return success.booleanValue();
    }

    @Override // com.microsoft.kapp.CargoConnection
    public boolean setBikeMetricsOrder(final BikeDisplayMetricType... metrics) {
        Boolean success;
        Boolean.valueOf(false);
        try {
            success = (Boolean) execute(new CargoConnectionExecutor<Boolean>() { // from class: com.microsoft.kapp.CargoConnectionProxy.44
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // com.microsoft.kapp.CargoConnectionExecutor
                public Boolean execute(CargoClient client) throws CargoException {
                    client.getDeviceClient().setBikeDisplayMetrics(metrics);
                    return true;
                }
            });
        } catch (CargoException ex) {
            KLog.i(TAG, "Exception encountered while saving bike metrics order to device.", ex);
            success = false;
        } catch (IllegalArgumentException ex2) {
            KLog.e(TAG, "Exception encountered while saving bike metrics order to device.", ex2);
            success = false;
        }
        if (success == null) {
            return false;
        }
        return success.booleanValue();
    }

    @Override // com.microsoft.kapp.CargoConnection
    public boolean downloadEphemerisAndTimeZone() throws CargoException {
        execute(new CargoConnectionExecutor<Void>() { // from class: com.microsoft.kapp.CargoConnectionProxy.45
            @Override // com.microsoft.kapp.CargoConnectionExecutor
            public Void execute(CargoClient client) throws CargoException {
                EphemerisUpdateInfo ephemerisInfo = client.getLatestAvailableEphemerisVersion();
                TimeZoneSettingsUpdateInfo timezoneInfo = client.getLatestAvailableTimeZoneSettings();
                CargoConnectionProxy.this.mEphemerisTimeZoneUpdateListener = new EphemerisTimeZoneUpdateListener();
                boolean ephemerisSuccess = client.downloadEphemerisUpdate(ephemerisInfo);
                boolean timeZoneSuccess = client.downloadTimeZoneSettingsUpdate(timezoneInfo);
                if (!timeZoneSuccess || !ephemerisSuccess) {
                    throw new CargoException(CargoConnectionProxy.TAG, BandServiceMessage.Response.SERVICE_CLOUD_REQUEST_FAILED_ERROR);
                }
                try {
                    boolean timeout = !CargoConnectionProxy.this.mEphemerisTimeZoneUpdateListener.getDownloadCountdownLatch().await(300000L, TimeUnit.MILLISECONDS);
                    if (timeout) {
                        CargoConnectionProxy.this.mEphemerisTimeZoneUpdateListener.onTimeout();
                        throw new CargoException(CargoConnectionProxy.TAG, BandServiceMessage.Response.SERVICE_CLOUD_REQUEST_FAILED_ERROR);
                    }
                    boolean timeZoneSuccess2 = client.pushTimeZoneSettingsFileToDevice(timezoneInfo);
                    boolean ephemerisSuccess2 = client.pushEphemerisSettingsFileToDevice(ephemerisInfo);
                    if (!timeZoneSuccess2 || !ephemerisSuccess2) {
                        throw new CargoException(CargoConnectionProxy.TAG, BandServiceMessage.Response.OPERATION_EXCEPTION_ERROR);
                    }
                    return null;
                } catch (InterruptedException ex) {
                    KLog.i(CargoConnectionProxy.TAG, "Ephemeris and time zone update was interrupted!", ex);
                    throw new CargoException(CargoConnectionProxy.TAG, BandServiceMessage.Response.OPERATION_INTERRUPTED_ERROR);
                }
            }
        });
        if (this.mEphemerisTimeZoneUpdateListener.getSyncError()) {
            this.mEphemerisTimeZoneUpdateListener = null;
            return false;
        }
        this.mEphemerisTimeZoneUpdateListener = null;
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class SyncListener implements CargoSyncListener {
        private List<CloudDataResource> mCloudDataList;
        private CountDownLatch mCountDownLatch = new CountDownLatch(2);
        private BandServiceMessage.Response mError;
        private SyncResult mSyncResult;
        private SyncResult mWebtileSyncResult;

        @Override // com.microsoft.kapp.CargoConnectionProxy.CargoSyncListener
        public void onSyncDeviceToCloudCompleted(SyncResult syncResult, List<CloudDataResource> cloudDataList) {
            this.mSyncResult = syncResult;
            this.mCloudDataList = cloudDataList;
            this.mCountDownLatch.countDown();
        }

        public SyncResult getSyncResult() {
            return this.mSyncResult;
        }

        public SyncResult getWebtileSyncResult() {
            return this.mWebtileSyncResult;
        }

        public List<CloudDataResource> getCloudDataList() {
            return this.mCloudDataList;
        }

        public CountDownLatch getCountDownLatch() {
            return this.mCountDownLatch;
        }

        public BandServiceMessage.Response getError() {
            return this.mError;
        }

        @Override // com.microsoft.kapp.CargoConnectionProxy.CargoSyncListener
        public void setError(BandServiceMessage.Response response) {
            if (this.mError == null) {
                this.mError = response;
            }
        }

        @Override // com.microsoft.kapp.CargoConnectionProxy.CargoSyncListener
        public void onSyncWebtilesCompleted(SyncResult syncResult) {
            this.mWebtileSyncResult = syncResult;
            this.mCountDownLatch.countDown();
        }
    }

    @Override // com.microsoft.kapp.CargoConnection
    public void setDeviceTime() throws CargoException {
        execute(new CargoConnectionExecutor<Void>() { // from class: com.microsoft.kapp.CargoConnectionProxy.46
            @Override // com.microsoft.kapp.CargoConnectionExecutor
            public Void execute(CargoClient client) throws CargoException {
                client.setDeviceUTCTime();
                client.setDeviceTimeZone(new TimeZoneInfo());
                return null;
            }
        });
    }

    @Override // com.microsoft.kapp.CargoConnection
    public void setGoals(Map allGoalsMap) throws CargoException {
        final GoalsData goalData = new GoalsData();
        Long calories = (Long) allGoalsMap.get(GoalType.CALORIE);
        Long steps = (Long) allGoalsMap.get(GoalType.STEP);
        if (calories != null) {
            goalData.setCaloriesEnabled(true).setCaloriesGoal(calories.longValue());
        }
        if (steps != null) {
            goalData.setStepsEnabled(true).setStepsGoal(steps.longValue());
        }
        execute(new CargoConnectionExecutor<Void>() { // from class: com.microsoft.kapp.CargoConnectionProxy.47
            @Override // com.microsoft.kapp.CargoConnectionExecutor
            public Void execute(CargoClient client) throws CargoException {
                client.setGoals(goalData);
                return null;
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class EphemerisTimeZoneUpdateListener implements CargoEphemerisTimeZoneUpdateListener {
        private CountDownLatch mDownloadCountDownLatch = new CountDownLatch(2);
        private boolean mSyncError = false;

        public boolean getSyncError() {
            return this.mSyncError;
        }

        public CountDownLatch getDownloadCountdownLatch() {
            return this.mDownloadCountDownLatch;
        }

        @Override // com.microsoft.kapp.CargoConnectionProxy.CargoEphemerisTimeZoneUpdateListener
        public void onEphemerisDownloadCompleted(BandServiceMessage.Response error) {
            if (error == null) {
                this.mDownloadCountDownLatch.countDown();
            } else {
                onDownloadError();
            }
        }

        @Override // com.microsoft.kapp.CargoConnectionProxy.CargoEphemerisTimeZoneUpdateListener
        public void onTimeZoneDownloadCompleted(BandServiceMessage.Response error) {
            if (error == null) {
                this.mDownloadCountDownLatch.countDown();
            } else {
                onDownloadError();
            }
        }

        public void onTimeout() {
            this.mSyncError = true;
        }

        @Override // com.microsoft.kapp.CargoConnectionProxy.CargoEphemerisTimeZoneUpdateListener
        public void onDownloadError() {
            this.mSyncError = true;
        }
    }

    /* loaded from: classes.dex */
    private static class FirmwareUpdateListener implements CargoFirmwareUpdateListener {
        private final AtomicBoolean mDownloadingFirmwareFlag = new AtomicBoolean(false);
        private final AtomicBoolean mUpgradingFirmwareFlag = new AtomicBoolean(false);
        private BandServiceMessage.Response mErrorResponse = null;

        @Override // com.microsoft.kapp.CargoConnectionProxy.CargoFirmwareUpdateListener
        public AtomicBoolean getDownloadFirmwareLatch() {
            return this.mDownloadingFirmwareFlag;
        }

        @Override // com.microsoft.kapp.CargoConnectionProxy.CargoFirmwareUpdateListener
        public AtomicBoolean getUpgradeFirmwareLatch() {
            return this.mUpgradingFirmwareFlag;
        }

        @Override // com.microsoft.kapp.CargoConnectionProxy.CargoFirmwareUpdateListener
        public void setError(BandServiceMessage.Response response) {
            this.mErrorResponse = response;
        }

        @Override // com.microsoft.kapp.CargoConnectionProxy.CargoFirmwareUpdateListener
        public BandServiceMessage.Response getError() {
            return this.mErrorResponse;
        }
    }

    private boolean checkDeviceBelongsToUser(DeviceInfo deviceInfo) throws CargoException {
        Boolean success = true;
        if (this.mSettingsProvider.getFreStatus() == FreStatus.SHOWN) {
            success = (Boolean) execute(true, deviceInfo, false, new CargoConnectionExecutor<Boolean>() { // from class: com.microsoft.kapp.CargoConnectionProxy.48
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // com.microsoft.kapp.CargoConnectionExecutor
                public Boolean execute(CargoClient client) throws CargoException {
                    return Boolean.valueOf(CargoConnectionProxy.this.mSingleDeviceEnforcementManager.isUserPairedDevice(client));
                }
            });
        }
        if (success == null) {
            return false;
        }
        return success.booleanValue();
    }

    @Override // com.microsoft.kapp.CargoConnection
    public void checkSingleDeviceEnforcement() throws CargoException {
        if (this.mSettingsProvider.getFreStatus() == FreStatus.SHOWN) {
            execute(true, null, false, new CargoConnectionExecutor<Void>() { // from class: com.microsoft.kapp.CargoConnectionProxy.49
                @Override // com.microsoft.kapp.CargoConnectionExecutor
                public Void execute(CargoClient client) throws CargoException {
                    CargoConnectionProxy.this.mSingleDeviceEnforcementManager.performSDECheck(client);
                    return null;
                }
            });
        }
    }

    @Override // com.microsoft.kapp.CargoConnection
    public boolean isOobeCompleted() throws CargoException {
        Boolean success = (Boolean) execute(true, null, false, new CargoConnectionExecutor<Boolean>() { // from class: com.microsoft.kapp.CargoConnectionProxy.50
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // com.microsoft.kapp.CargoConnectionExecutor
            public Boolean execute(CargoClient client) throws CargoException {
                return Boolean.valueOf(client.isOOBECompleted());
            }
        });
        if (success == null) {
            return false;
        }
        return success.booleanValue();
    }

    @Override // com.microsoft.kapp.CargoConnection
    public boolean isOobeCompletedForDevice(DeviceInfo device) throws CargoException {
        Boolean success = (Boolean) execute(true, device, false, new CargoConnectionExecutor<Boolean>() { // from class: com.microsoft.kapp.CargoConnectionProxy.51
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // com.microsoft.kapp.CargoConnectionExecutor
            public Boolean execute(CargoClient client) throws CargoException {
                return Boolean.valueOf(client.isOOBECompleted());
            }
        });
        if (success == null) {
            return false;
        }
        return success.booleanValue();
    }

    @Override // com.microsoft.kapp.CargoConnection
    public int getNumberOfStrappsAllowedOnDevice() throws CargoException {
        return ((Integer) execute(new CargoConnectionExecutor<Integer>() { // from class: com.microsoft.kapp.CargoConnectionProxy.52
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // com.microsoft.kapp.CargoConnectionExecutor
            public Integer execute(CargoClient client) throws CargoException {
                return Integer.valueOf(client.getStrappMaxCount());
            }
        })).intValue();
    }

    @Override // com.microsoft.kapp.CargoConnection
    public void setStrappTheme(final StrappColorPalette colors, final UUID uuid) throws CargoException {
        execute(new CargoConnectionExecutor<Void>() { // from class: com.microsoft.kapp.CargoConnectionProxy.53
            @Override // com.microsoft.kapp.CargoConnectionExecutor
            public Void execute(CargoClient client) throws CargoException {
                client.setThemeColorForCustomStrappByUUID(colors, uuid);
                return null;
            }
        });
    }

    @Override // com.microsoft.kapp.CargoConnection
    public boolean setBikeSplitsDistance(final Integer bikeSplitMultiplier) {
        Boolean success;
        Boolean.valueOf(false);
        try {
            success = (Boolean) execute(new CargoConnectionExecutor<Boolean>() { // from class: com.microsoft.kapp.CargoConnectionProxy.54
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // com.microsoft.kapp.CargoConnectionExecutor
                public Boolean execute(CargoClient client) throws CargoException {
                    client.setBikeSplitMultiplier(bikeSplitMultiplier.intValue());
                    return true;
                }
            });
        } catch (Exception ex) {
            KLog.i(TAG, "Exception encountered while saving bike split distance to device.", ex);
            success = false;
        }
        if (success == null) {
            return false;
        }
        return success.booleanValue();
    }

    @Override // com.microsoft.kapp.CargoConnection
    public void registerDevice(final DeviceSettings deviceInfo) throws CargoException {
        executeCloudClient(new CargoCloudConnectionExecutor<Void>() { // from class: com.microsoft.kapp.CargoConnectionProxy.55
            @Override // com.microsoft.kapp.CargoCloudConnectionExecutor
            public Void execute(CargoCloudClient client) throws CargoException {
                UserProfileInfo info = client.getCloudProfile();
                info.addConnectedDevice(deviceInfo);
                client.saveCloudProfile(info, DateTime.now().getMillis());
                return null;
            }
        });
    }

    @Override // com.microsoft.kapp.CargoConnection
    public void saveWebTileToDevice(final WebTile webtile) throws CargoException {
        execute(new CargoConnectionExecutor<Void>() { // from class: com.microsoft.kapp.CargoConnectionProxy.56
            @Override // com.microsoft.kapp.CargoConnectionExecutor
            public Void execute(CargoClient client) throws CargoException {
                client.addWebTile(webtile);
                return null;
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void logInfoAndPublicMessage(String tag, String publicMessage, Object info) {
        KLog.i(tag, publicMessage);
        KLog.logPrivate(tag, String.format("%s: %s", publicMessage, info));
    }

    @Override // com.microsoft.kapp.CargoConnection
    public void sendFileToCloud(final String absolutePath) throws CargoException {
        executeCloudClient(new CargoCloudConnectionExecutor<Void>() { // from class: com.microsoft.kapp.CargoConnectionProxy.57
            @Override // com.microsoft.kapp.CargoCloudConnectionExecutor
            public Void execute(CargoCloudClient client) throws CargoException {
                client.sendFileToCloud(absolutePath, CloudDataResource.LogFileTypes.APPDUMP);
                return null;
            }
        });
    }

    @Override // com.microsoft.kapp.CargoConnection
    public boolean isUsingV2Band() throws CargoException {
        return isUsingV2Band(null);
    }

    @Override // com.microsoft.kapp.CargoConnection
    public boolean isUsingV2Band(DeviceInfo deviceInfo) throws CargoException {
        Boolean success = (Boolean) execute(false, deviceInfo, false, new CargoConnectionExecutor<Boolean>() { // from class: com.microsoft.kapp.CargoConnectionProxy.58
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // com.microsoft.kapp.CargoConnectionExecutor
            public Boolean execute(CargoClient client) throws CargoException {
                return Boolean.valueOf(CargoConnectionProxy.this.isV2Device(client));
            }
        });
        if (success == null) {
            return false;
        }
        return success.booleanValue();
    }

    @Override // com.microsoft.kapp.CargoConnection
    public BandVersion getBandVersion() throws CargoException {
        boolean isNeon = isUsingV2Band();
        return isNeon ? BandVersion.NEON : BandVersion.CARGO;
    }

    @Override // com.microsoft.kapp.CargoConnection
    public void saveExerciseOptions(final List<WorkoutActivity> exerciseOptions) {
        try {
            execute(new CargoConnectionExecutor<Void>() { // from class: com.microsoft.kapp.CargoConnectionProxy.59
                @Override // com.microsoft.kapp.CargoConnectionExecutor
                public Void execute(CargoClient client) throws CargoException {
                    if (VersionCheck.isV2DeviceOrGreater(client.getHardwareVersion())) {
                        client.setWorkoutActivities(exerciseOptions);
                        return null;
                    }
                    return null;
                }
            });
        } catch (CargoException ex) {
            KLog.w(TAG, "Exception encountered while saving exercise picker options to device.", ex);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isV2Device(CargoClient client) throws CargoException {
        return VersionCheck.isV2DeviceOrGreater(client.getHardwareVersion());
    }
}

package com.microsoft.kapp.mocks;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.support.v4.app.NotificationCompat;
import com.microsoft.band.CargoCloudClient;
import com.microsoft.band.DeviceProfileStatus;
import com.microsoft.band.OOBEStage;
import com.microsoft.band.client.CargoException;
import com.microsoft.band.client.SyncResult;
import com.microsoft.band.cloud.CargoFirmwareUpdateInfo;
import com.microsoft.band.device.CargoStrapp;
import com.microsoft.band.device.DeviceSettings;
import com.microsoft.band.device.FirmwareVersions;
import com.microsoft.band.device.StartStrip;
import com.microsoft.band.device.StrappColorPalette;
import com.microsoft.band.device.StrappLayout;
import com.microsoft.band.device.WorkoutActivity;
import com.microsoft.band.device.command.BikeDisplayMetricType;
import com.microsoft.band.device.command.RunDisplayMetricType;
import com.microsoft.band.internal.BandServiceMessage;
import com.microsoft.band.internal.device.DeviceInfo;
import com.microsoft.band.service.cloud.CloudDataResource;
import com.microsoft.band.webtiles.WebTile;
import com.microsoft.kapp.CargoConnection;
import com.microsoft.kapp.DeviceCommunicationStateListener;
import com.microsoft.kapp.R;
import com.microsoft.kapp.SyncProgressListener;
import com.microsoft.kapp.calendar.CalendarEvent;
import com.microsoft.kapp.device.CargoUserProfile;
import com.microsoft.kapp.diagnostics.Validate;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.kapp.models.CustomStrappData;
import com.microsoft.kapp.models.strapp.DefaultStrappUUID;
import com.microsoft.kapp.personalization.DeviceTheme;
import com.microsoft.kapp.tasks.SettingsPreferencesSaveTask;
import com.microsoft.kapp.telephony.IncomingCallContext;
import com.microsoft.kapp.telephony.KNotification;
import com.microsoft.kapp.telephony.Message;
import com.microsoft.kapp.telephony.SmsMessage;
import com.microsoft.kapp.telephony.SmsReplyRequestedListener;
import com.microsoft.kapp.utils.Constants;
import com.microsoft.kapp.version.Version;
import com.microsoft.krestsdk.models.BandVersion;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
/* loaded from: classes.dex */
public class MockCargoConnection implements CargoConnection {
    private static final String TAG = MockCargoConnection.class.getSimpleName();
    private Context mContext;

    public MockCargoConnection(Context context) {
        this.mContext = context;
    }

    @Override // com.microsoft.kapp.CargoConnection
    public List<DeviceInfo> getBondedDevices() {
        DeviceInfo device = new DeviceInfo("Mock Device", UUID.randomUUID().toString());
        try {
            Thread.sleep(CargoCloudClient.CLOUD_PROCESSING_SUCCESS_WAIT_TIME);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return Arrays.asList(device);
    }

    @Override // com.microsoft.kapp.CargoConnection
    public boolean isDeviceAvailable(DeviceInfo device) {
        return true;
    }

    @Override // com.microsoft.kapp.CargoConnection
    public DeviceInfo getConnectedDevice() {
        List<DeviceInfo> devices = getBondedDevices();
        if (devices == null || devices.size() == 0) {
            return null;
        }
        return devices.get(0);
    }

    @Override // com.microsoft.kapp.CargoConnection
    public DeviceInfo getConnectedDevice(boolean suppressCheck, boolean notifyCommunicationStateErrorListener) {
        return getConnectedDevice();
    }

    @Override // com.microsoft.kapp.CargoConnection
    public CargoConnection.SynchronizeDeviceToCloudResult synchronizeDeviceToCloud(boolean isForegroundSync, SyncProgressListener syncProgressListener) throws CargoException {
        KLog.v(TAG, "Synchronizing Device to Cloud");
        try {
            Thread.sleep(15000L);
        } catch (Exception e) {
            KLog.e(TAG, "Sync error", e);
        }
        SyncResult syncResult = new SyncResult();
        return new CargoConnection.SynchronizeDeviceToCloudResult(syncResult, null, null, null);
    }

    @Override // com.microsoft.kapp.CargoConnection
    public int pushWorkoutData(byte[] workoutData) {
        return 2;
    }

    @Override // com.microsoft.kapp.CargoConnection
    public void sendCalendarEvents(List<CalendarEvent> calendarEvents) {
        KLog.v(TAG, "Sending Calendar Events to Device");
    }

    @Override // com.microsoft.kapp.CargoConnection
    public void sendMessageNotification(Message message) {
        KLog.v(TAG, "Sending Message Notification to Device");
        if (message instanceof SmsMessage) {
            SmsMessage smsMessage = (SmsMessage) message;
            Resources resource = this.mContext.getResources();
            String string = resource.getString(R.string.incoming_message_status_message_format);
            Object[] objArr = new Object[5];
            objArr[0] = Integer.valueOf(message.getId());
            objArr[1] = message.getTimestamp();
            objArr[2] = message.getNumber();
            objArr[3] = smsMessage.getBody();
            objArr[4] = message.getIsRead() ? "Yes" : "No";
            String bigText = String.format(string, objArr);
            String title = String.format(resource.getString(R.string.incoming_message_status_title), message.getDisplayName());
            String ticker = String.format(resource.getString(R.string.incoming_message_ticker), message.getDisplayName());
            Notification notification = new NotificationCompat.Builder(this.mContext).setContentText(String.valueOf(smsMessage.getId())).setContentTitle(title).setStyle(new NotificationCompat.BigTextStyle().bigText(bigText)).setTicker(ticker).setSmallIcon(17301646).build();
            NotificationManager manager = (NotificationManager) this.mContext.getSystemService("notification");
            manager.notify(message.getTimestamp().hashCode(), notification);
        }
    }

    @Override // com.microsoft.kapp.CargoConnection
    public void sendCallStateChangeNotification(IncomingCallContext context) {
        int stateId;
        int iconId;
        Validate.notNull(context, Constants.FRE_INTENT_EXTRA_INFO);
        KLog.v(TAG, "Sending Incoming Call Notification to Device");
        switch (context.getState()) {
            case HUNG_UP:
                stateId = R.string.incoming_call_status_hung_up;
                iconId = 17301558;
                break;
            case MISSED:
                stateId = R.string.incoming_call_status_missed;
                iconId = 17301649;
                break;
            case PICKED_UP:
                stateId = R.string.incoming_call_status_picked_up;
                iconId = 17301650;
                break;
            case RINGING:
                stateId = R.string.incoming_call_status_ringing;
                iconId = 17301648;
                break;
            default:
                stateId = R.string.incoming_call_status_unknown;
                iconId = 17301642;
                break;
        }
        String state = this.mContext.getString(stateId);
        Date date = new Date();
        String message = this.mContext.getString(R.string.incoming_call_status_message_format, context.getNumber(), context.getDisplayName(), date);
        Notification notification = new NotificationCompat.Builder(this.mContext).setContentText(message).setContentTitle(state).setTicker(state).setSmallIcon(iconId).build();
        NotificationManager manager = (NotificationManager) this.mContext.getSystemService("notification");
        manager.notify(date.hashCode(), notification);
    }

    @Override // com.microsoft.kapp.CargoConnection, com.microsoft.kapp.CargoVersionRetriever
    public Version getDeviceFirmwareVersion() {
        return new Version(1000, SettingsPreferencesSaveTask.RESULT_ERROR_CLOUD);
    }

    @Override // com.microsoft.kapp.CargoConnection, com.microsoft.kapp.CargoVersionRetriever
    public Version getMinimumRequiredFirmwareVersion() {
        return getDeviceFirmwareVersion();
    }

    @Override // com.microsoft.kapp.CargoConnection, com.microsoft.kapp.CargoVersionRetriever
    public CargoFirmwareUpdateInfo getLatestAvailableFirmwareVersion() {
        return null;
    }

    @Override // com.microsoft.kapp.CargoConnection
    public BandServiceMessage.Response downloadFirmwareUpdate(CargoFirmwareUpdateInfo firmwareUpdateInfo) throws CargoException {
        return null;
    }

    @Override // com.microsoft.kapp.CargoConnection
    public BandServiceMessage.Response pushFirmwareUpdateToDevice(CargoFirmwareUpdateInfo firmwareUpdateInfo) throws CargoException {
        return null;
    }

    @Override // com.microsoft.kapp.CargoConnection
    public StartStrip getDefaultStrapps() {
        return getStartStrip();
    }

    @Override // com.microsoft.kapp.CargoConnection
    public StartStrip getStartStrip() {
        List<CargoStrapp> strip = new ArrayList<>();
        try {
            ArrayList<Bitmap> images = new ArrayList<>();
            images.add(Bitmap.createBitmap(5, 5, Bitmap.Config.ARGB_8888));
            images.add(Bitmap.createBitmap(5, 5, Bitmap.Config.ARGB_8888));
            ArrayList<StrappLayout> layouts = new ArrayList<>();
            layouts.add(new StrappLayout(new byte[100]));
            CargoStrapp strapp = new CargoStrapp(DefaultStrappUUID.STRAPP_CALLS, "Dummy", 0, 1L, images, (short) 0, layouts);
            strip.add(strapp);
            CargoStrapp strapp2 = new CargoStrapp(DefaultStrappUUID.STRAPP_MESSAGING, "Dummy", 0, 1L, images, (short) 0, layouts);
            strip.add(strapp2);
            CargoStrapp strapp3 = new CargoStrapp(DefaultStrappUUID.STRAPP_ALARM_STOPWATCH, "Dummy", 0, 1L, images, (short) 0, layouts);
            strip.add(strapp3);
            CargoStrapp strapp4 = new CargoStrapp(DefaultStrappUUID.STRAPP_BING_FINANCE, "Dummy", 0, 1L, images, (short) 0, layouts);
            strip.add(strapp4);
            CargoStrapp strapp5 = new CargoStrapp(DefaultStrappUUID.STRAPP_BING_WEATHER, "Dummy", 0, 1L, images, (short) 0, layouts);
            strip.add(strapp5);
            CargoStrapp strapp6 = new CargoStrapp(DefaultStrappUUID.STRAPP_CALENDAR, "Dummy", 0, 1L, images, (short) 0, layouts);
            strip.add(strapp6);
            CargoStrapp strapp7 = new CargoStrapp(DefaultStrappUUID.STRAPP_EXERCISE, "Dummy", 0, 1L, images, (short) 0, layouts);
            strip.add(strapp7);
            CargoStrapp strapp8 = new CargoStrapp(DefaultStrappUUID.STRAPP_RUN, "Dummy", 0, 1L, images, (short) 0, layouts);
            strip.add(strapp8);
            CargoStrapp strapp9 = new CargoStrapp(DefaultStrappUUID.STRAPP_SLEEP, "Dummy", 0, 1L, images, (short) 0, layouts);
            strip.add(strapp9);
            KLog.d(TAG, "Strapp size is %d", Integer.valueOf(strip.size()));
            return new StartStrip(strip);
        } catch (CargoException ex) {
            KLog.v(TAG, "getStartStrip()", ex);
            return null;
        }
    }

    @Override // com.microsoft.kapp.CargoConnection
    public void setStartStrip(StartStrip apps) {
    }

    @Override // com.microsoft.kapp.CargoConnection
    public void personalizeDevice(DeviceTheme deviceTheme, Bitmap wallpaper, int wallpaperPatternId) {
    }

    @Override // com.microsoft.kapp.CargoConnection
    public void personalizeDevice(StartStrip apps, DeviceTheme deviceTheme, Bitmap wallpaper, int wallpaperPatternId) {
    }

    @Override // com.microsoft.kapp.CargoConnection
    public int getDeviceWallpaperId() throws CargoException {
        return 0;
    }

    @Override // com.microsoft.kapp.CargoConnection
    public void setSmsReplyRequestedListener(SmsReplyRequestedListener smsReplyListener) {
        KLog.i(TAG, "SMS Reply requested");
    }

    @Override // com.microsoft.kapp.CargoConnection
    public void sendNotificationToStrapp(KNotification notification, UUID uuid) {
    }

    @Override // com.microsoft.kapp.CargoConnection
    public void sendVoicemailNotification() {
    }

    @Override // com.microsoft.kapp.CargoConnection
    public void importUserProfile() throws CargoException {
    }

    @Override // com.microsoft.kapp.CargoConnection
    public CargoUserProfile getUserCloudProfile(String phsUrl, String kAccessToken, String FUSEndpoint) throws CargoException {
        return null;
    }

    @Override // com.microsoft.kapp.CargoConnection
    public void setUserProfileUnitPrefs(boolean weightUnitType, boolean distanceUnitType, boolean temperatureUnitType) throws CargoException {
    }

    @Override // com.microsoft.kapp.CargoConnection
    public void setDeviceProfileUnitPrefs(boolean weightUnitType, boolean distanceUnitType, boolean temperatureUnitType) throws CargoException {
    }

    @Override // com.microsoft.kapp.CargoConnection
    public void setOOBEComplete() throws CargoException {
    }

    @Override // com.microsoft.kapp.CargoConnection
    public DeviceProfileStatus getDeviceAndProfileLinkStatus() {
        return null;
    }

    @Override // com.microsoft.kapp.CargoConnection
    public void setDeviceCommunicationStateListener(DeviceCommunicationStateListener listener) {
    }

    @Override // com.microsoft.kapp.CargoConnection
    public void setDeviceName(String deviceName) {
    }

    @Override // com.microsoft.kapp.CargoConnection
    public void linkDeviceToProfile() {
    }

    @Override // com.microsoft.kapp.CargoConnection
    public void unlinkDeviceToProfile() throws CargoException {
    }

    @Override // com.microsoft.kapp.CargoConnection
    public void resetOOBEComplete() throws CargoException {
    }

    @Override // com.microsoft.kapp.CargoConnection
    public void linkDeviceToProfile(boolean suppressSingleDeviceCheck) throws CargoException {
    }

    @Override // com.microsoft.kapp.CargoConnection
    public String getDeviceSerialNo() throws CargoException {
        return "";
    }

    @Override // com.microsoft.kapp.CargoConnection
    public String getDeviceName() throws CargoException {
        return "";
    }

    @Override // com.microsoft.kapp.CargoConnection
    public int getDeviceBatteryLevel() {
        return 0;
    }

    @Override // com.microsoft.kapp.CargoConnection
    public boolean waitForCloudProcessingToComplete(List<CloudDataResource> cloudDataResources) throws CargoException {
        return true;
    }

    @Override // com.microsoft.kapp.CargoConnection
    public void setDeviceTime() throws CargoException {
    }

    @Override // com.microsoft.kapp.CargoConnection
    public boolean setNotificationsEnabled(UUID strappId, boolean enabled) {
        return true;
    }

    @Override // com.microsoft.kapp.CargoConnection
    public boolean savePhoneCallResponses(String m1, String m2, String m3, String m4) {
        return true;
    }

    @Override // com.microsoft.kapp.CargoConnection
    public boolean saveSmsResponses(String m1, String m2, String m3, String m4) {
        return true;
    }

    @Override // com.microsoft.kapp.CargoConnection
    public boolean downloadEphemerisAndTimeZone() {
        return true;
    }

    @Override // com.microsoft.kapp.CargoConnection
    public boolean setRunMetricsOrder(RunDisplayMetricType... metrics) {
        return true;
    }

    @Override // com.microsoft.kapp.CargoConnection
    public void setGoals(Map allGoalsMap) throws CargoException {
    }

    @Override // com.microsoft.kapp.CargoConnection
    public void sendCustomStrappData(CustomStrappData strapp) {
    }

    @Override // com.microsoft.kapp.CargoConnection
    public void checkSingleDeviceEnforcement() throws CargoException {
    }

    @Override // com.microsoft.kapp.CargoConnection
    public boolean isOobeCompleted() {
        return true;
    }

    @Override // com.microsoft.kapp.CargoConnection
    public void setTelemetryFlag(boolean isTelemetryEnabled) throws CargoException {
    }

    @Override // com.microsoft.kapp.CargoConnection
    public void clearLastSentCalendarEventsCache() {
    }

    @Override // com.microsoft.kapp.CargoConnection
    public int getNumberOfStrappsAllowedOnDevice() {
        return 12;
    }

    @Override // com.microsoft.kapp.CargoConnection
    public void setStrappTheme(StrappColorPalette colors, UUID uuid) throws CargoException {
    }

    @Override // com.microsoft.kapp.CargoConnection
    public Bitmap captureDeviceScreenshot() throws CargoException {
        return null;
    }

    @Override // com.microsoft.kapp.CargoConnection
    public void setCloudProfileTelemetryFlag(boolean isTelemetryEnabled) throws CargoException {
    }

    @Override // com.microsoft.kapp.CargoConnection
    public boolean setBikeMetricsOrder(BikeDisplayMetricType... metrics) {
        return true;
    }

    @Override // com.microsoft.kapp.CargoConnection
    public boolean setBikeSplitsDistance(Integer mTransactionBikeSplitDistance) {
        return true;
    }

    @Override // com.microsoft.kapp.CargoConnection
    public void registerDevice(DeviceSettings device) {
    }

    @Override // com.microsoft.kapp.CargoConnection
    public boolean saveUserCloudProfile(CargoUserProfile userProfile, long millis) {
        return true;
    }

    @Override // com.microsoft.kapp.CargoConnection
    public boolean saveUserBandProfile(CargoUserProfile mUserProfile, long millis) {
        return true;
    }

    @Override // com.microsoft.kapp.CargoConnection
    public int pushGolfCourseData(byte[] golfCourseData) {
        return 2;
    }

    @Override // com.microsoft.kapp.CargoConnection
    public void saveWebTileToDevice(WebTile webtile) throws CargoException {
    }

    @Override // com.microsoft.kapp.CargoConnection
    public void sendFileToCloud(String absolutePath) throws CargoException {
    }

    @Override // com.microsoft.kapp.CargoConnection
    public FirmwareVersions getDeviceFirmwareVersionsObject() {
        return null;
    }

    @Override // com.microsoft.kapp.CargoConnection
    public void navigateToScreen(OOBEStage screen) throws CargoException {
    }

    @Override // com.microsoft.kapp.CargoConnection
    public int finishOobe() throws CargoException {
        return 0;
    }

    @Override // com.microsoft.kapp.CargoConnection
    public boolean isOobeCompletedForDevice(DeviceInfo device) throws CargoException {
        return true;
    }

    @Override // com.microsoft.kapp.CargoConnection
    public boolean isUsingV2Band() throws CargoException {
        return true;
    }

    @Override // com.microsoft.kapp.CargoConnection
    public BandVersion getBandVersion() throws CargoException {
        return BandVersion.CARGO;
    }

    @Override // com.microsoft.kapp.CargoConnection
    public boolean isUsingV2Band(DeviceInfo deviceInfo) throws CargoException {
        return true;
    }

    @Override // com.microsoft.kapp.CargoConnection
    public void saveExerciseOptions(List<WorkoutActivity> exerciseOptions) {
    }
}

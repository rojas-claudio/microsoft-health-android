package com.microsoft.band;

import android.content.Context;
import android.graphics.Bitmap;
import com.microsoft.band.DeviceProfileStatus;
import com.microsoft.band.client.CargoException;
import com.microsoft.band.client.UnitType;
import com.microsoft.band.cloud.CargoFirmwareUpdateInfo;
import com.microsoft.band.cloud.CargoServiceInfo;
import com.microsoft.band.cloud.CloudProfileLinker;
import com.microsoft.band.cloud.EphemerisUpdateInfo;
import com.microsoft.band.cloud.TimeZoneSettingsUpdateInfo;
import com.microsoft.band.cloud.UserProfileInfo;
import com.microsoft.band.device.CalendarEvent;
import com.microsoft.band.device.CargoCall;
import com.microsoft.band.device.CargoSms;
import com.microsoft.band.device.CargoStrapp;
import com.microsoft.band.device.DeviceConstants;
import com.microsoft.band.device.DeviceProfileInfo;
import com.microsoft.band.device.FirmwareVersions;
import com.microsoft.band.device.GoalsData;
import com.microsoft.band.device.Haptic;
import com.microsoft.band.device.SleepNotification;
import com.microsoft.band.device.StartStrip;
import com.microsoft.band.device.StatsRunData;
import com.microsoft.band.device.StatsSleepData;
import com.microsoft.band.device.StatsWorkoutData;
import com.microsoft.band.device.StrappColorPalette;
import com.microsoft.band.device.StrappMessage;
import com.microsoft.band.device.StrappPageElement;
import com.microsoft.band.device.SystemTimeInfo;
import com.microsoft.band.device.TimeZoneInfo;
import com.microsoft.band.device.WorkoutActivity;
import com.microsoft.band.device.command.BikeDisplayMetricType;
import com.microsoft.band.device.command.NavigateToScreen;
import com.microsoft.band.device.command.RunDisplayMetricType;
import com.microsoft.band.device.command.SmsResponseType;
import com.microsoft.band.internal.BandDeviceConstants;
import com.microsoft.band.internal.InternalBandConstants;
import com.microsoft.band.internal.ServiceCommand;
import com.microsoft.band.internal.device.DeviceInfo;
import com.microsoft.band.internal.util.KDKLog;
import com.microsoft.band.internal.util.Validation;
import com.microsoft.band.service.cloud.CloudDataResource;
import com.microsoft.band.webtiles.WebTile;
import com.microsoft.band.webtiles.WebTileManager;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
/* loaded from: classes.dex */
public class CargoClient extends CargoServicesClient {
    private static final String TAG = CargoClient.class.getSimpleName();
    private CargoCloudClient mCloudServices;
    private CargoDeviceClient mDeviceServices;
    private CargoServiceInfo mServiceInfo;

    /* loaded from: classes.dex */
    public enum ActivityPeriod {
        Minute,
        Hour
    }

    public static List<DeviceInfo> getBondedDevices() {
        return CargoKit.getPairedDevices();
    }

    protected CargoClient(BandServiceConnection csc, DeviceInfo deviceInfo, CargoServiceInfo serviceInfo) throws CargoException {
        super(csc);
        this.mDeviceServices = CargoDeviceClient.create(getServiceConnection());
        this.mCloudServices = CargoCloudClient.create(getServiceConnection());
        this.mServiceInfo = serviceInfo;
        KDKLog.i(TAG, "New instance of CargoClient created.");
    }

    public static CargoClient create(Context context, CargoServiceInfo serviceInfo, DeviceInfo deviceInfo) throws CargoException {
        BandServiceConnection csc = KDKServiceConnection.create(context, serviceInfo, deviceInfo);
        return new CargoClient(csc, deviceInfo, serviceInfo);
    }

    @Override // com.microsoft.band.CargoServicesClient
    public void destroy() {
        super.destroy();
        this.mDeviceServices = null;
        this.mCloudServices = null;
    }

    @Override // com.microsoft.band.CargoServicesClient
    public boolean isDestroyed() {
        return super.isDestroyed() && this.mDeviceServices == null && this.mCloudServices == null;
    }

    public void connectDevice() throws CargoException {
        getDeviceClient().connectDevice();
    }

    public void connectDevice(boolean waitForConnection) throws CargoException {
        getDeviceClient().connectDevice(waitForConnection);
    }

    public void disconnectDevice() throws CargoException {
        getDeviceClient().disconnectDevice();
    }

    public boolean isDeviceConnected() {
        return getDeviceClient().isDeviceConnected();
    }

    public int getHardwareVersion() throws CargoException {
        return getDeviceClient().getHardwareVersion();
    }

    public CargoCloudClient getCloudClient() {
        return this.mCloudServices;
    }

    public CargoDeviceClient getDeviceClient() {
        return this.mDeviceServices;
    }

    public BandClient getBandClient() throws CargoException {
        return new BandClientImpl(getServiceConnection());
    }

    public void sendSmsNotification(String name, String body, Date timestamp, int threadID) throws CargoException, IllegalArgumentException {
        CargoSms sms = new CargoSms(name, body, timestamp, threadID);
        sendSmsNotification(sms);
    }

    public void queueSmsNotification(String name, String body, Date timestamp, int threadID) throws CargoException, IllegalArgumentException {
        CargoSms sms = new CargoSms(name, body, timestamp, threadID);
        queueSmsNotification(sms);
    }

    public void sendSmsNotification(CargoSms sms) throws CargoException, IllegalArgumentException {
        getDeviceClient().sendSmsNotification(sms);
    }

    public void queueSmsNotification(CargoSms sms) throws CargoException, IllegalArgumentException {
        getDeviceClient().queueSmsNotification(sms);
    }

    public void sendInsightNotification(String line1, String line2) throws CargoException, IllegalArgumentException {
        getDeviceClient().sendInsightNotification(line1, line2);
    }

    public void queueInsightNotification(String line1, String line2) throws CargoException, IllegalArgumentException {
        getDeviceClient().queueInsightNotification(line1, line2);
    }

    public void sendInsightNotification(String message) throws CargoException, IllegalArgumentException {
        getDeviceClient().sendInsightNotification(message);
    }

    public void queueInsightNotification(String message) throws CargoException, IllegalArgumentException {
        getDeviceClient().queueInsightNotification(message);
    }

    public void sendIncomingCallNotification(String name, int callerId, Date timestamp) throws CargoException, IllegalArgumentException {
        sendIncomingCallNotification(name, callerId, timestamp, null);
    }

    public void sendIncomingCallNotification(String name, int callerId, Date timestamp, DeviceConstants.NotificationFlag flag) throws CargoException, IllegalArgumentException {
        CargoCall call = new CargoCall(callerId, name, timestamp, CargoCall.NotificationCallType.INCOMING, flag);
        getDeviceClient().sendCallNotification(call);
    }

    public void sendHangupCallNotification(String name, int callerId, Date timestamp) throws CargoException, IllegalArgumentException {
        CargoCall call = new CargoCall(callerId, name, timestamp, CargoCall.NotificationCallType.HANGUP);
        getDeviceClient().sendCallNotification(call);
    }

    public void sendAnsweredCallNotification(String name, int callerId, Date timestamp) throws CargoException, IllegalArgumentException {
        CargoCall call = new CargoCall(callerId, name, timestamp, CargoCall.NotificationCallType.ANSWERED);
        getDeviceClient().sendCallNotification(call);
    }

    public void sendVoicemailNotification(String name, int callerId, Date timestamp) throws CargoException, IllegalArgumentException {
        sendVoicemailNotification(name, callerId, timestamp, null);
    }

    public void sendVoicemailNotification(String name, int callerId, Date timestamp, DeviceConstants.NotificationFlag flag) throws CargoException, IllegalArgumentException {
        CargoCall call = new CargoCall(callerId, name, timestamp, CargoCall.NotificationCallType.VOICEMAIL, flag);
        getDeviceClient().sendCallNotification(call);
    }

    public void queueVoicemailNotification(String name, int callerId, Date timestamp) throws CargoException, IllegalArgumentException {
        queueVoicemailNotification(name, callerId, timestamp, null);
    }

    public void queueVoicemailNotification(String name, int callerId, Date timestamp, DeviceConstants.NotificationFlag flag) throws CargoException, IllegalArgumentException {
        CargoCall call = new CargoCall(callerId, name, timestamp, CargoCall.NotificationCallType.VOICEMAIL, flag);
        getDeviceClient().queueCallNotification(call);
    }

    public void sendMissedCallNotification(String name, int callerId, Date timestamp) throws CargoException, IllegalArgumentException {
        sendMissedCallNotification(name, callerId, timestamp, null);
    }

    public void sendMissedCallNotification(String name, int callerId, Date timestamp, DeviceConstants.NotificationFlag flag) throws CargoException, IllegalArgumentException {
        CargoCall call = new CargoCall(callerId, name, timestamp, CargoCall.NotificationCallType.MISSED, flag);
        getDeviceClient().sendCallNotification(call);
    }

    public void queueMissedCallNotification(String name, int callerId, Date timestamp) throws CargoException, IllegalArgumentException {
        queueMissedCallNotification(name, callerId, timestamp, null);
    }

    public void queueMissedCallNotification(String name, int callerId, Date timestamp, DeviceConstants.NotificationFlag flag) throws CargoException, IllegalArgumentException {
        CargoCall call = new CargoCall(callerId, name, timestamp, CargoCall.NotificationCallType.MISSED, flag);
        getDeviceClient().queueCallNotification(call);
    }

    public void setDeviceUTCTime() throws CargoException, IllegalArgumentException {
        getDeviceClient().setDeviceUTCTime(0);
    }

    public void sendHapticFeedback(Haptic haptic) throws CargoException {
        getDeviceClient().sendHapticFeedback(haptic);
    }

    public void navigateToScreenId(int screenId) throws CargoException {
        getDeviceClient().sendNavigateToScreen((byte) screenId);
    }

    public void navigateToScreenId(NavigateToScreen.Screens screen) throws CargoException {
        getDeviceClient().sendNavigateToScreen((byte) screen.getID());
    }

    public int finalizeOOBE() throws CargoException {
        return getDeviceClient().finalizeOOBE();
    }

    public OOBEStage getOOBEStageIndex() throws CargoException {
        return getDeviceClient().getOOBEStageIndex();
    }

    public void setOOBEStageIndex(OOBEStage index) throws CargoException {
        getDeviceClient().setOOBEStageIndex(index);
    }

    public Date getDeviceUTCTime() throws CargoException {
        return getDeviceClient().getDeviceUTCTime();
    }

    public UserProfileInfo getUserProfile() throws CargoException {
        return getCloudClient().getCloudProfile();
    }

    public DeviceProfileInfo getDeviceProfile() throws CargoException {
        return getDeviceClient().getDeviceProfile();
    }

    public void saveUserProfile(UserProfileInfo upi) throws CargoException {
        DeviceProfileInfo dpi = getDeviceProfile();
        dpi.updateUsingCloudUserProfile(upi);
        long currentTime = System.currentTimeMillis();
        getDeviceClient().saveDeviceProfile(dpi, currentTime);
        getCloudClient().saveCloudProfile(upi, currentTime);
    }

    public void saveUserProfile(DeviceProfileInfo dpi) throws CargoException {
        UserProfileInfo upi = new UserProfileInfo();
        upi.updateUsingDeviceUserProfile(dpi);
        long currentTime = System.currentTimeMillis();
        getDeviceClient().saveDeviceProfile(dpi, currentTime);
        getCloudClient().saveCloudProfile(upi, currentTime);
    }

    public void setUserProfileUnitPrefs(UnitType weightUnitType, UnitType distanceUnitType, UnitType temperatureUnitType) throws CargoException {
        getCloudClient().setUserProfileUnitPrefs(weightUnitType, distanceUnitType, temperatureUnitType);
    }

    public void setDeviceProfileUnitPrefs(UnitType weightUnitType, UnitType distanceUnitType, UnitType temperatureUnitType) throws CargoException {
        getDeviceClient().setDeviceProfileUnitPrefs(weightUnitType, distanceUnitType, temperatureUnitType);
    }

    public void resetDeviceProfile(UserProfileInfo upi, DeviceProfileInfo dpi, long currentTime) throws CargoException {
        getDeviceClient().resetDeviceProfile(upi, dpi, currentTime);
    }

    public void ImportUserProfile() throws CargoException {
        UserProfileInfo upi = getUserProfile();
        DeviceProfileInfo dpi = getDeviceProfile();
        dpi.updateUsingCloudUserProfile(upi);
        getDeviceClient().saveDeviceProfile(dpi, upi.getLastKDKSyncUpdateOn().getTime());
    }

    public void saveDeviceProfile(DeviceProfileInfo profile) throws CargoException {
        getDeviceClient().saveDeviceProfile(profile, System.currentTimeMillis());
    }

    public void saveCloudProfile(UserProfileInfo profile) throws CargoException {
        getCloudClient().saveCloudProfile(profile, System.currentTimeMillis());
    }

    public void linkDeviceToProfile(boolean importAlso) throws CargoException {
        UserProfileInfo upi = getUserProfile();
        DeviceProfileInfo dpi = getDeviceProfile();
        long currentTime = System.currentTimeMillis();
        CloudProfileLinker cloudLinker = new CloudProfileLinker();
        cloudLinker.setAppPairingDeviceID(getDeviceInfo().getDeviceUUID());
        cloudLinker.setDeviceID(getDeviceInfo().getDeviceUUID());
        cloudLinker.setSerialNumber(getDeviceInfo().getSerialNumber());
        getCloudClient().linkCloudProfile(cloudLinker, currentTime);
        if (importAlso) {
            resetDeviceProfile(upi, dpi, currentTime);
            return;
        }
        dpi.setProfileID(upi.getODSUserID());
        getDeviceClient().saveDeviceProfile(dpi, currentTime);
    }

    public boolean unlinkDeviceAndCloud() throws CargoException {
        UUID emptyID = UserProfileInfo.EMPTY_DEVICE_ID;
        long currentTime = System.currentTimeMillis();
        boolean deviceUnlinked = true;
        try {
            DeviceProfileInfo dpi = getDeviceProfile();
            dpi.setProfileID(emptyID);
            getDeviceClient().saveDeviceProfile(dpi, currentTime);
        } catch (CargoException e) {
            KDKLog.e(TAG, "Unpairing device from cloud unsuccessful");
            deviceUnlinked = false;
        }
        UserProfileInfo upi = new UserProfileInfo();
        upi.setAppPairingDeviceID(emptyID);
        getCloudClient().saveCloudProfile(upi, currentTime);
        return deviceUnlinked;
    }

    public DeviceProfileStatus getDeviceAndProfileLinkStatus() throws CargoException {
        DeviceProfileStatus.LinkStatus deviceID;
        DeviceProfileStatus.LinkStatus profileID;
        UserProfileInfo upi = getUserProfile();
        DeviceProfileInfo dpi = getDeviceProfile();
        if (upi.getAppPairingDeviceID() == null || upi.getAppPairingDeviceID().equals(UserProfileInfo.EMPTY_DEVICE_ID)) {
            deviceID = DeviceProfileStatus.LinkStatus.EMPTY;
        } else if (upi.getAppPairingDeviceID().equals(getDeviceInfo().getDeviceUUID())) {
            deviceID = DeviceProfileStatus.LinkStatus.MATCHING;
        } else {
            deviceID = DeviceProfileStatus.LinkStatus.NON_MATCHING;
        }
        if (dpi.getProfileID().equals(UserProfileInfo.EMPTY_DEVICE_ID)) {
            profileID = DeviceProfileStatus.LinkStatus.EMPTY;
        } else if (dpi.getProfileID().equals(upi.getODSUserID())) {
            profileID = DeviceProfileStatus.LinkStatus.MATCHING;
        } else {
            profileID = DeviceProfileStatus.LinkStatus.NON_MATCHING;
        }
        return new DeviceProfileStatus(deviceID, profileID);
    }

    public boolean syncDeviceToCloud() throws CargoException {
        return syncDeviceToCloud(true);
    }

    public boolean syncDeviceToCloud(boolean fullSync) throws CargoException {
        ServiceCommand cmd = new ServiceCommand(BandDeviceConstants.Command.CargoSyncDeviceToCloud);
        cmd.getBundle().putBoolean(InternalBandConstants.EXTRA_COMMAND_DATA, fullSync);
        boolean returnValue = getServiceConnection().sendCommand(cmd);
        if (returnValue) {
            syncWebTiles(!fullSync);
        }
        return returnValue;
    }

    @Deprecated
    public boolean cancelSync() throws CargoException {
        ServiceCommand cmd = new ServiceCommand(BandDeviceConstants.Command.CargoCancelSync);
        return getServiceConnection().sendCommand(cmd);
    }

    public DeviceInfo getDeviceInfo() throws CargoException {
        return getServiceConnection().getDeviceInfo();
    }

    public CargoServiceInfo getServiceInfo() throws CargoException {
        return this.mServiceInfo;
    }

    public FirmwareVersions getFirmwareVersions() throws CargoException {
        return getDeviceClient().getFirmwareVerison();
    }

    public String getProductSerialNumber() throws CargoException {
        return getDeviceClient().getProductSerialNumber();
    }

    public void sendCalendarEvents(CalendarEvent[] calendarArray) throws CargoException, IllegalArgumentException {
        getDeviceClient().sendCalendarEvents(calendarArray);
    }

    public void sendCalendarEventClearNotification() throws CargoException, IllegalArgumentException {
        getDeviceClient().sendCalendarEventClearNotification();
    }

    public StartStrip getDefaultStrapps() throws CargoException {
        return getDeviceClient().getDefaultStrapps(true);
    }

    public StartStrip getDefaultStrapps(boolean withImage) throws CargoException {
        return getDeviceClient().getDefaultStrapps(withImage);
    }

    public int getStrappMaxCount() throws CargoException {
        return getDeviceClient().getStrappMaxCount();
    }

    public StartStrip getStartStrip() throws CargoException {
        return getDeviceClient().getStartStrip(true);
    }

    public StartStrip getStartStrip(boolean withImage) throws CargoException {
        return getDeviceClient().getStartStrip(withImage);
    }

    public void setStartStrip(StartStrip apps) throws CargoException {
        getDeviceClient().setStartStrip(apps);
    }

    public CargoStrapp getStrapp(UUID id) throws CargoException {
        return getDeviceClient().getStrapp(id, true);
    }

    public CargoStrapp getStrapp(UUID id, boolean withImage) throws CargoException {
        return getDeviceClient().getStrapp(id, withImage);
    }

    public void updateStrapp(CargoStrapp strapp) throws CargoException {
        getDeviceClient().setStrapp(strapp);
    }

    public void setStrappTileImageIndex(UUID id, int tileImageIndex) throws CargoException {
        getDeviceClient().setStrappTileImageIndex(id, tileImageIndex);
    }

    public void setStrappBadgeImageIndex(UUID id, int badgeImageIndex) throws CargoException {
        getDeviceClient().setStrappBadgeImageIndex(id, badgeImageIndex);
    }

    public void setStrappNotificationImageIndex(UUID id, int notificationImageIndex) throws CargoException {
        getDeviceClient().setStrappNotificationImageIndex(id, notificationImageIndex);
    }

    public void sendPageUpdate(UUID strappId, UUID pageId, int pageLayoutIndex, List<StrappPageElement> textFields) throws CargoException {
        getDeviceClient().sendPageUpdate(strappId, pageId, pageLayoutIndex, textFields);
    }

    public void clearStrapp(UUID strappId) throws CargoException {
        getDeviceClient().clearStrapp(strappId);
    }

    public void clearPage(UUID strappId, UUID pageId) throws CargoException {
        getDeviceClient().clearPage(strappId, pageId);
    }

    public void sendStrappMessage(UUID strappId, StrappMessage message) throws CargoException {
        getDeviceClient().sendStrappMessage(strappId, message);
    }

    public void sendStrappDialog(UUID strappId, String lineOne, String lineTwo, boolean forceDialog) throws CargoException {
        getDeviceClient().sendStrappDialog(strappId, lineOne, lineTwo, forceDialog);
    }

    public void queueStrappMessage(UUID strappId, StrappMessage message) throws CargoException {
        getDeviceClient().queueStrappMessage(strappId, message);
    }

    public CargoFirmwareUpdateInfo getLatestAvailableFirmwareVersion() throws CargoException {
        return getLatestAvailableFirmwareVersion(null);
    }

    public CargoFirmwareUpdateInfo getLatestAvailableFirmwareVersion(Map<String, String> queryParams) throws CargoException {
        FirmwareVersions deviceVersion = getFirmwareVersions();
        Validation.validateNullParameter(deviceVersion, "getLatestAvailableFirmwareVersion: device firmware version");
        String deviceFamily = "" + ((int) deviceVersion.getApplicationVersion().getPcbId());
        Validation.validateNullAndEmptyString(deviceFamily, "getLatestAvailableFirmwareVersion: deviceFamily");
        boolean firmwareOnDeviceValid = false;
        if (getDeviceClient().getRunningApplication() == DeviceConstants.AppRunning.APP_RUNNING_APP) {
            firmwareOnDeviceValid = getDeviceClient().getFirmwareVerisonValidation();
        }
        return getCloudClient().getLatestAvailableFirmwareVersion(deviceFamily, deviceVersion, firmwareOnDeviceValid, queryParams);
    }

    public boolean downloadFirmwareUpdate(CargoFirmwareUpdateInfo firmwareUpdate) throws CargoException {
        return getCloudClient().downloadFirmwareUpdate(firmwareUpdate);
    }

    public boolean pushFirmwareUpdateToDevice(CargoFirmwareUpdateInfo firmwareUpdateInfo) throws CargoException {
        return getDeviceClient().pushFirmwareUpdateToDevice(firmwareUpdateInfo);
    }

    public EphemerisUpdateInfo getLatestAvailableEphemerisVersion() throws CargoException {
        return getCloudClient().getLatestAvailableEphemerisVersion();
    }

    public boolean downloadEphemerisUpdate(EphemerisUpdateInfo ephemerisUpdate) throws CargoException {
        return getCloudClient().downloadEphemerisUpdate(ephemerisUpdate);
    }

    public boolean pushEphemerisSettingsFileToDevice(EphemerisUpdateInfo ephemerisUpdateInfo) throws CargoException {
        return getDeviceClient().pushEphemerisSettingsFileToDevice(ephemerisUpdateInfo);
    }

    public TimeZoneSettingsUpdateInfo getLatestAvailableTimeZoneSettings() throws CargoException {
        ServiceCommand cmd = new ServiceCommand(BandDeviceConstants.Command.CargoGetTimeZoneSettingsUpdateInfo);
        getServiceConnection().sendCommand(cmd);
        return (TimeZoneSettingsUpdateInfo) cmd.getBundle().getParcelable(CargoConstants.EXTRA_CLOUD_DATA);
    }

    public boolean downloadTimeZoneSettingsUpdate(TimeZoneSettingsUpdateInfo timezoneSettingsInfo) throws CargoException {
        return getCloudClient().downloadTimeZoneSettingsUpdate(timezoneSettingsInfo);
    }

    public boolean pushTimeZoneSettingsFileToDevice(TimeZoneSettingsUpdateInfo timezoneSettingsInfo) throws CargoException {
        return getDeviceClient().pushTimeZoneSettingsFileToDevice(timezoneSettingsInfo);
    }

    public void pushFitnessData(byte[] data) throws CargoException {
        Validation.validateNullParameter(data, "data cannot be null");
        getDeviceClient().pushFitnessPlan(data);
    }

    public int getFitnessPlanMaxFileSize() throws CargoException {
        return getDeviceClient().getFitnessPlanMaxFileSize();
    }

    public int getGolfCourseMaxFileSize() throws CargoException {
        return getDeviceClient().getGolfCourseMaxFileSize();
    }

    public void pushGolfCourseData(byte[] data) throws CargoException {
        Validation.validateNullParameter(data, "data cannot be null");
        getDeviceClient().pushGolfCourse(data);
    }

    public void setGoals(GoalsData goals) throws CargoException {
        getDeviceClient().setGoals(goals);
    }

    public void setPhoneCallResponses(String m1, String m2, String m3, String m4) throws CargoException {
        getDeviceClient().setPhoneCallResponses(m1, m2, m3, m4);
    }

    public void setSmsResponses(String m1, String m2, String m3, String m4) throws CargoException {
        getDeviceClient().setSmsResponses(m1, m2, m3, m4);
    }

    public void setAllResponses(String... responses) throws CargoException {
        getDeviceClient().setAllResponses(responses);
    }

    public String[] getPhoneCallResponses() throws CargoException {
        return getSmsResponsesPartOf(SmsResponseType.CALL);
    }

    public String[] getSmsResponses() throws CargoException {
        return getSmsResponsesPartOf(SmsResponseType.SMS);
    }

    private String[] getSmsResponsesPartOf(SmsResponseType type) throws CargoException {
        String[] allResponses = getAllResponses();
        String[] newCopy = new String[4];
        System.arraycopy(allResponses, newCopy.length * type.mId, newCopy, 0, newCopy.length);
        return newCopy;
    }

    public String[] getAllResponses() throws CargoException {
        return getDeviceClient().getAllResponses();
    }

    @Deprecated
    public void setOOBECompleted() throws CargoException {
        getDeviceClient().setOOBECompleted();
    }

    public void setMeTileImage(Bitmap image) throws CargoException {
        getDeviceClient().setMeTileImage(image, 4294967295L);
    }

    public void setMeTileImage(Bitmap image, int id) throws CargoException {
        getDeviceClient().setMeTileImage(image, id);
    }

    public Bitmap getMeTileImage() throws CargoException {
        return getDeviceClient().readMeTileImage();
    }

    public void clearMeTileImage() throws CargoException {
        getDeviceClient().clearMeTileImage();
    }

    public void setThemeColorForDefaultStrapps(StrappColorPalette colorPalette) throws CargoException {
        getDeviceClient().setThemeColorForDefaultStrapps(colorPalette);
    }

    public StrappColorPalette getThemeColorForDefaultStrapps() throws CargoException {
        return getDeviceClient().getThemeColorForDefaultStrapps();
    }

    public void setThemeColorForCustomStrappByUUID(StrappColorPalette colorPalette, UUID appId) throws CargoException {
        getDeviceClient().setThemeColorForCustomStrappByUUID(colorPalette, appId);
    }

    public void resetThemeColorForAllStrapps() throws CargoException {
        getDeviceClient().resetThemeColorForAllStrapps();
    }

    public int getStrappSettingMask(UUID appId) throws CargoException {
        return getDeviceClient().getStrappSettingsMask(appId);
    }

    public void turnTelemetryLoggingOnOff(boolean f) throws CargoException {
        getDeviceClient().turnTelemetryLoggingOnOff(f);
    }

    public void turnPerformanceLoggingOnOff(boolean f) throws CargoException {
        getDeviceClient().turnPerformanceLoggingOnOff(f);
    }

    public void turnDiagnosticsLoggingOnOff(boolean f) throws CargoException {
        getDeviceClient().turnDiagnosticsLoggingOnOff(f);
    }

    public void setStrappSettingMask(UUID appId, int settingMask) throws CargoException {
        getDeviceClient().setStrappSettingsMask(appId, settingMask);
    }

    public TimeZoneInfo getDeviceTimeZone() throws CargoException {
        return getDeviceClient().getTimeZone();
    }

    public SystemTimeInfo getDeviceLocalTime() throws CargoException {
        return getDeviceClient().getLocalTime();
    }

    public void setDeviceTimeZone(TimeZoneInfo timeZoneInfo) throws CargoException {
        getDeviceClient().setTimeZone(timeZoneInfo);
    }

    public boolean getDeviceTimeSyncEnabled() throws CargoException {
        return getDeviceClient().isTimeSyncEnabled();
    }

    public void setDeviceTimeSyncEnabled(boolean timeSyncEnabled) throws CargoException {
        getDeviceClient().setTimeSyncEnabled(timeSyncEnabled);
    }

    public void setRunDisplayMetrics(RunDisplayMetricType[] metrics) throws CargoException {
        getDeviceClient().setRunDisplayMetrics(metrics);
    }

    public void setBikeDisplayMetrics(BikeDisplayMetricType[] metrics) throws CargoException {
        getDeviceClient().setBikeDisplayMetrics(metrics);
    }

    public RunDisplayMetricType[] getRunDisplayMetrics() throws CargoException {
        return getDeviceClient().getRunDisplayMetrics();
    }

    public BikeDisplayMetricType[] getBikeDisplayMetrics() throws CargoException {
        return getDeviceClient().getBikeDisplayMetrics();
    }

    public void setBikeSplitMultiplier(int multiplier) throws CargoException {
        getDeviceClient().setBikeSplitMultiplier(multiplier);
    }

    public int getBikeSplitMultiplier() throws CargoException {
        return getDeviceClient().getBikeSplitMultiplier();
    }

    public void setWorkoutActivities(List<WorkoutActivity> workoutActivityData) throws CargoException {
        getDeviceClient().setWorkoutActivities(workoutActivityData);
    }

    public List<WorkoutActivity> getWorkoutActivities() throws CargoException {
        return getDeviceClient().getWorkoutActivities();
    }

    public StatsRunData getRunStatistics() throws CargoException {
        return (StatsRunData) getDeviceClient().getStatistics(new StatsRunData());
    }

    public StatsWorkoutData getWorkoutStatistics() throws CargoException {
        return (StatsWorkoutData) getDeviceClient().getStatistics(new StatsWorkoutData());
    }

    public StatsSleepData getSleepStatistics() throws CargoException {
        return (StatsSleepData) getDeviceClient().getStatistics(new StatsSleepData());
    }

    public boolean waitForCloudProcessingToComplete(List<CloudDataResource> cdrList, long timeoutInMillis) throws CargoException {
        return getCloudClient().waitForCloudProcessingToComplete(cdrList, timeoutInMillis);
    }

    public void personalizeDevice(StartStrip startStrip, Bitmap image, StrappColorPalette colorPalette) throws CargoException {
        getDeviceClient().personalizeDevice(startStrip, image, colorPalette, 4294967295L, null);
    }

    public void personalizeDevice(StartStrip startStrip, Bitmap image, StrappColorPalette colorPalette, int imageId) throws CargoException {
        getDeviceClient().personalizeDevice(startStrip, image, colorPalette, imageId, null);
    }

    public void personalizeDevice(StartStrip startStrip, Bitmap image, StrappColorPalette colorPalette, Map<UUID, StrappColorPalette> customColors) throws CargoException {
        getDeviceClient().personalizeDevice(startStrip, image, colorPalette, 4294967295L, customColors);
    }

    public void personalizeDevice(StartStrip startStrip, Bitmap image, StrappColorPalette colorPalette, int imageId, Map<UUID, StrappColorPalette> customColors) throws CargoException {
        getDeviceClient().personalizeDevice(startStrip, image, colorPalette, imageId, customColors);
    }

    public void setCustomStrappThemes(Map<UUID, StrappColorPalette> customColors) throws CargoException {
        getDeviceClient().setStrappThemes(customColors);
    }

    public boolean isOOBECompleted() throws CargoException {
        return getDeviceClient().isOOBECompleted();
    }

    public void setSleepNotification(SleepNotification sNotification) throws CargoException {
        getDeviceClient().setSleepNotification(sNotification);
    }

    public SleepNotification getSleepNotification() throws CargoException {
        return getDeviceClient().getSleepNotification();
    }

    public void disableSleepNotification() throws CargoException {
        getDeviceClient().disableSleepNotification();
    }

    public long getMeTileId() throws CargoException {
        return getDeviceClient().getMeTileId();
    }

    public void uploadLogBytesToCloud(byte[] data, CloudDataResource.LogFileTypes type) throws CargoException {
        getCloudClient().uploadLogBytesToCloud(data, type);
    }

    public Bitmap readPegScreen() throws CargoException {
        return getDeviceClient().readPegScreen();
    }

    public void addWebTile(WebTile tile) throws CargoException {
        getDeviceClient().addWebTile(tile);
        WebTileManager.saveWebTileFromTemp(tile);
        syncWebTiles(false);
    }

    public BandPendingResult<Boolean> refreshWebTile(UUID tileId) {
        throw new UnsupportedOperationException("TODO");
    }

    public void syncWebTiles(boolean forceSync) throws CargoException {
        ServiceCommand cmd = new ServiceCommand(BandDeviceConstants.Command.CargoSyncWebTiles);
        cmd.getBundle().putBoolean(InternalBandConstants.EXTRA_COMMAND_DATA, forceSync);
        getServiceConnection().sendCommand(cmd);
    }

    public void sendFileToCloud(String fileName, CloudDataResource.LogFileTypes type) throws CargoException {
        getCloudClient().sendFileToCloud(fileName, type);
    }
}

package com.microsoft.kapp;

import android.graphics.Bitmap;
import com.microsoft.band.DeviceProfileStatus;
import com.microsoft.band.OOBEStage;
import com.microsoft.band.client.CargoException;
import com.microsoft.band.client.SyncResult;
import com.microsoft.band.cloud.CargoFirmwareUpdateInfo;
import com.microsoft.band.device.DeviceSettings;
import com.microsoft.band.device.FirmwareVersions;
import com.microsoft.band.device.StartStrip;
import com.microsoft.band.device.StrappColorPalette;
import com.microsoft.band.device.WorkoutActivity;
import com.microsoft.band.device.command.BikeDisplayMetricType;
import com.microsoft.band.device.command.RunDisplayMetricType;
import com.microsoft.band.internal.BandServiceMessage;
import com.microsoft.band.internal.device.DeviceInfo;
import com.microsoft.band.service.cloud.CloudDataResource;
import com.microsoft.band.webtiles.WebTile;
import com.microsoft.kapp.calendar.CalendarEvent;
import com.microsoft.kapp.device.CargoUserProfile;
import com.microsoft.kapp.models.CustomStrappData;
import com.microsoft.kapp.personalization.DeviceTheme;
import com.microsoft.kapp.telephony.IncomingCallContext;
import com.microsoft.kapp.telephony.KNotification;
import com.microsoft.kapp.telephony.Message;
import com.microsoft.kapp.telephony.SmsReplyRequestedListener;
import com.microsoft.kapp.version.Version;
import com.microsoft.krestsdk.models.BandVersion;
import java.util.List;
import java.util.Map;
import java.util.UUID;
/* loaded from: classes.dex */
public interface CargoConnection extends CargoVersionRetriever {
    Bitmap captureDeviceScreenshot() throws CargoException;

    void checkSingleDeviceEnforcement() throws CargoException;

    void clearLastSentCalendarEventsCache();

    boolean downloadEphemerisAndTimeZone() throws CargoException;

    BandServiceMessage.Response downloadFirmwareUpdate(CargoFirmwareUpdateInfo cargoFirmwareUpdateInfo) throws CargoException;

    int finishOobe() throws CargoException;

    BandVersion getBandVersion() throws CargoException;

    List<DeviceInfo> getBondedDevices();

    DeviceInfo getConnectedDevice() throws CargoException;

    DeviceInfo getConnectedDevice(boolean z, boolean z2) throws CargoException;

    StartStrip getDefaultStrapps() throws CargoException;

    DeviceProfileStatus getDeviceAndProfileLinkStatus();

    int getDeviceBatteryLevel();

    @Override // com.microsoft.kapp.CargoVersionRetriever
    Version getDeviceFirmwareVersion();

    FirmwareVersions getDeviceFirmwareVersionsObject();

    String getDeviceName() throws CargoException;

    String getDeviceSerialNo() throws CargoException;

    int getDeviceWallpaperId() throws CargoException;

    @Override // com.microsoft.kapp.CargoVersionRetriever
    CargoFirmwareUpdateInfo getLatestAvailableFirmwareVersion() throws CargoException;

    @Override // com.microsoft.kapp.CargoVersionRetriever
    Version getMinimumRequiredFirmwareVersion();

    int getNumberOfStrappsAllowedOnDevice() throws CargoException;

    StartStrip getStartStrip() throws CargoException;

    CargoUserProfile getUserCloudProfile(String str, String str2, String str3) throws CargoException;

    void importUserProfile() throws CargoException;

    boolean isDeviceAvailable(DeviceInfo deviceInfo);

    boolean isOobeCompleted() throws CargoException;

    boolean isOobeCompletedForDevice(DeviceInfo deviceInfo) throws CargoException;

    boolean isUsingV2Band() throws CargoException;

    boolean isUsingV2Band(DeviceInfo deviceInfo) throws CargoException;

    void linkDeviceToProfile() throws CargoException;

    void linkDeviceToProfile(boolean z) throws CargoException;

    void navigateToScreen(OOBEStage oOBEStage) throws CargoException;

    void personalizeDevice(StartStrip startStrip, DeviceTheme deviceTheme, Bitmap bitmap, int i) throws CargoException;

    void personalizeDevice(DeviceTheme deviceTheme, Bitmap bitmap, int i) throws CargoException;

    BandServiceMessage.Response pushFirmwareUpdateToDevice(CargoFirmwareUpdateInfo cargoFirmwareUpdateInfo) throws CargoException;

    int pushGolfCourseData(byte[] bArr);

    int pushWorkoutData(byte[] bArr);

    void registerDevice(DeviceSettings deviceSettings) throws CargoException;

    void resetOOBEComplete() throws CargoException;

    void saveExerciseOptions(List<WorkoutActivity> list);

    boolean savePhoneCallResponses(String str, String str2, String str3, String str4);

    boolean saveSmsResponses(String str, String str2, String str3, String str4);

    boolean saveUserBandProfile(CargoUserProfile cargoUserProfile, long j);

    boolean saveUserCloudProfile(CargoUserProfile cargoUserProfile, long j);

    void saveWebTileToDevice(WebTile webTile) throws CargoException;

    void sendCalendarEvents(List<CalendarEvent> list);

    void sendCallStateChangeNotification(IncomingCallContext incomingCallContext);

    void sendCustomStrappData(CustomStrappData customStrappData);

    void sendFileToCloud(String str) throws CargoException;

    void sendMessageNotification(Message message);

    void sendNotificationToStrapp(KNotification kNotification, UUID uuid);

    void sendVoicemailNotification();

    boolean setBikeMetricsOrder(BikeDisplayMetricType... bikeDisplayMetricTypeArr);

    boolean setBikeSplitsDistance(Integer num);

    void setCloudProfileTelemetryFlag(boolean z) throws CargoException;

    void setDeviceCommunicationStateListener(DeviceCommunicationStateListener deviceCommunicationStateListener);

    void setDeviceName(String str) throws CargoException;

    void setDeviceProfileUnitPrefs(boolean z, boolean z2, boolean z3) throws CargoException;

    void setDeviceTime() throws CargoException;

    void setGoals(Map map) throws CargoException;

    boolean setNotificationsEnabled(UUID uuid, boolean z);

    void setOOBEComplete() throws CargoException;

    boolean setRunMetricsOrder(RunDisplayMetricType... runDisplayMetricTypeArr);

    void setSmsReplyRequestedListener(SmsReplyRequestedListener smsReplyRequestedListener);

    void setStartStrip(StartStrip startStrip) throws CargoException;

    void setStrappTheme(StrappColorPalette strappColorPalette, UUID uuid) throws CargoException;

    void setTelemetryFlag(boolean z) throws CargoException;

    void setUserProfileUnitPrefs(boolean z, boolean z2, boolean z3) throws CargoException;

    SynchronizeDeviceToCloudResult synchronizeDeviceToCloud(boolean z, SyncProgressListener syncProgressListener) throws CargoException;

    void unlinkDeviceToProfile() throws CargoException;

    boolean waitForCloudProcessingToComplete(List<CloudDataResource> list) throws CargoException;

    /* loaded from: classes.dex */
    public static class SynchronizeDeviceToCloudResult {
        private List<CloudDataResource> mCloudDataList;
        private BandServiceMessage.Response mError;
        private SyncResult mSyncResult;
        private SyncResult mWebtileSyncResult;

        public SynchronizeDeviceToCloudResult(SyncResult syncResult, SyncResult webtileSyncResult, List<CloudDataResource> cloudDataList, BandServiceMessage.Response error) {
            this.mSyncResult = syncResult;
            this.mWebtileSyncResult = webtileSyncResult;
            this.mCloudDataList = cloudDataList;
            this.mError = error;
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

        public BandServiceMessage.Response getError() {
            return this.mError;
        }
    }
}

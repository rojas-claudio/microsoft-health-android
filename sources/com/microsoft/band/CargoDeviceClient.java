package com.microsoft.band;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import com.microsoft.band.client.CargoException;
import com.microsoft.band.client.UnitType;
import com.microsoft.band.cloud.CargoFirmwareUpdateInfo;
import com.microsoft.band.cloud.EphemerisUpdateInfo;
import com.microsoft.band.cloud.TimeZoneSettingsUpdateInfo;
import com.microsoft.band.cloud.UserProfileInfo;
import com.microsoft.band.device.CalendarEvent;
import com.microsoft.band.device.CargoBikeDisplayMetrics;
import com.microsoft.band.device.CargoCall;
import com.microsoft.band.device.CargoRunDisplayMetrics;
import com.microsoft.band.device.CargoSms;
import com.microsoft.band.device.CargoStrapp;
import com.microsoft.band.device.CommandStruct;
import com.microsoft.band.device.DeviceConstants;
import com.microsoft.band.device.DeviceProfileInfo;
import com.microsoft.band.device.FWVersion;
import com.microsoft.band.device.FirmwareVersions;
import com.microsoft.band.device.GoalsData;
import com.microsoft.band.device.Haptic;
import com.microsoft.band.device.NotificationGenericDialog;
import com.microsoft.band.device.NotificationGenericUpdate;
import com.microsoft.band.device.NotificationInsights;
import com.microsoft.band.device.ProfileByteArray;
import com.microsoft.band.device.SleepNotification;
import com.microsoft.band.device.StartStrip;
import com.microsoft.band.device.StrappColorPalette;
import com.microsoft.band.device.StrappMessage;
import com.microsoft.band.device.StrappPageElement;
import com.microsoft.band.device.SystemTimeInfo;
import com.microsoft.band.device.TimeZoneInfo;
import com.microsoft.band.device.WorkoutActivity;
import com.microsoft.band.device.command.BikeDisplayMetricType;
import com.microsoft.band.device.command.CargoFileName;
import com.microsoft.band.device.command.CommandRead;
import com.microsoft.band.device.command.CommandWrite;
import com.microsoft.band.device.command.DeviceProfileByteArraySet;
import com.microsoft.band.device.command.DeviceProfileGet;
import com.microsoft.band.device.command.DeviceProfileSet;
import com.microsoft.band.device.command.DisableSleepNotification;
import com.microsoft.band.device.command.FileGetSize;
import com.microsoft.band.device.command.FileWrite;
import com.microsoft.band.device.command.GetProductSerialNumber;
import com.microsoft.band.device.command.GetSleepNotification;
import com.microsoft.band.device.command.GetSmsResponseAll;
import com.microsoft.band.device.command.GetStatistics;
import com.microsoft.band.device.command.GetUTCTime;
import com.microsoft.band.device.command.GetVersion;
import com.microsoft.band.device.command.GetVersionValidation;
import com.microsoft.band.device.command.NavigateToScreen;
import com.microsoft.band.device.command.NavigateToScreenGuid;
import com.microsoft.band.device.command.NotificationCommand;
import com.microsoft.band.device.command.OOBEFinalizeCommand;
import com.microsoft.band.device.command.OOBEGetStageCommand;
import com.microsoft.band.device.command.OOBESetStageCommand;
import com.microsoft.band.device.command.RunDisplayMetricType;
import com.microsoft.band.device.command.SetSleepNotification;
import com.microsoft.band.device.command.SetSmsResponse;
import com.microsoft.band.device.command.SetSmsResponseAll;
import com.microsoft.band.device.command.SmsResponseType;
import com.microsoft.band.internal.BandDeviceConstants;
import com.microsoft.band.internal.BandServiceMessage;
import com.microsoft.band.internal.InternalBandConstants;
import com.microsoft.band.internal.ServiceCommand;
import com.microsoft.band.internal.device.DeviceInfo;
import com.microsoft.band.internal.util.BandImage;
import com.microsoft.band.internal.util.BitHelper;
import com.microsoft.band.internal.util.BufferUtil;
import com.microsoft.band.internal.util.UUIDHelper;
import com.microsoft.band.internal.util.Validation;
import com.microsoft.band.internal.util.VersionCheck;
import com.microsoft.band.webtiles.WebTile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
/* loaded from: classes.dex */
public class CargoDeviceClient extends CargoServicesClient {
    private KDKSensorManager mSensorManager;

    public static CargoDeviceClient create(BandServiceConnection serviceConnection) throws CargoException {
        return new CargoDeviceClient(serviceConnection);
    }

    protected CargoDeviceClient(BandServiceConnection serviceConnection) throws CargoException {
        super(serviceConnection);
    }

    public synchronized KDKSensorManager getKDKSensorManager() throws CargoException {
        if (this.mSensorManager == null) {
            this.mSensorManager = new KDKSensorManager(getServiceConnection());
        }
        return this.mSensorManager;
    }

    public static CargoDeviceClient create(Context context, DeviceInfo deviceInfo) throws CargoException {
        BandServiceConnection csc = BandServiceConnection.create(context, deviceInfo);
        return new CargoDeviceClient(csc);
    }

    @Override // com.microsoft.band.CargoServicesClient
    public void destroy() {
        super.destroy();
    }

    public void connectDevice() throws CargoException {
        connectDevice(true);
    }

    public void connectDevice(boolean waitForConnection) throws CargoException {
        ServiceCommand cmd = new ServiceCommand(BandDeviceConstants.Command.CargoConnectDevice);
        if (waitForConnection) {
            getServiceConnection().sendCommand(cmd);
        } else {
            getServiceConnection().sendCommandAsync(cmd);
        }
    }

    public void disconnectDevice() throws CargoException {
        ServiceCommand cmd = new ServiceCommand(BandDeviceConstants.Command.CargoDisconnectDevice);
        getServiceConnection().sendCommand(cmd);
    }

    public boolean isDeviceConnected() {
        try {
            return getServiceConnection().isDeviceConnected();
        } catch (CargoException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void verifyV2Band() throws CargoException {
        int hardwareVersion = getHardwareVersion();
        if (!VersionCheck.isV2DeviceOrGreater(hardwareVersion)) {
            throw new UnsupportedOperationException(String.format("This method cannot be used with devices of hardware version %d", Integer.valueOf(hardwareVersion)));
        }
    }

    public int getHardwareVersion() throws CargoException {
        DeviceInfo di = getServiceConnection().getDeviceInfo();
        if (di != null && di.getHardwareVersion() != 0) {
            return di.getHardwareVersion();
        }
        ServiceCommand cmd = new ServiceCommand(BandDeviceConstants.Command.BandGetHardwareVersion);
        getServiceConnection().sendCommand(cmd, true);
        return cmd.getBundle().getShort(InternalBandConstants.EXTRA_HARDWARE_VERSION);
    }

    public boolean sendSmsNotification(CargoSms sms) throws CargoException {
        return sendSmsNotification(sms, false);
    }

    public void queueSmsNotification(CargoSms sms) throws CargoException {
        sendSmsNotification(sms, true);
    }

    protected boolean sendSmsNotification(CargoSms sms, boolean enableQueuing) throws CargoException {
        Validation.validateNullParameter(sms, "Send SMS");
        NotificationCommand notification = new NotificationCommand(DeviceConstants.NotificationID.SMS, DeviceConstants.GUID_ID_SMS, sms.toBytes());
        if (enableQueuing) {
            notification.setQueueLimit(DeviceConstants.NotificationID.SMS.getQueueLimit());
        }
        return getServiceConnection().sendCommandAsync(notification);
    }

    public boolean sendInsightNotification(String line1, String line2) throws CargoException {
        NotificationInsights insight = new NotificationInsights(line1, line2);
        return sendInsightNotification(insight, false);
    }

    public void queueInsightNotification(String line1, String line2) throws CargoException {
        NotificationInsights insight = new NotificationInsights(line1, line2);
        sendInsightNotification(insight, true);
    }

    protected boolean sendInsightNotification(NotificationInsights insight, boolean enableQueuing) throws CargoException {
        NotificationCommand notification;
        if (insight.isTwoLine()) {
            notification = new NotificationCommand(DeviceConstants.NotificationID.INSIGHT, "00000000-0000-0000-0000-000000000000", insight.toBytes());
        } else {
            notification = new NotificationCommand(DeviceConstants.NotificationID.INSIGHT2, "00000000-0000-0000-0000-000000000000", insight.toBytes());
        }
        if (enableQueuing) {
            notification.setQueueLimit(DeviceConstants.NotificationID.INSIGHT.getQueueLimit());
        }
        return getServiceConnection().sendCommandAsync(notification);
    }

    public boolean sendInsightNotification(String message) throws CargoException {
        NotificationInsights insight = new NotificationInsights(message);
        return sendInsightNotification(insight, false);
    }

    public void queueInsightNotification(String message) throws CargoException {
        NotificationInsights insight = new NotificationInsights(message);
        sendInsightNotification(insight, true);
    }

    public boolean sendCallNotification(CargoCall call) throws CargoException {
        return sendCallNotification(call, false);
    }

    public void queueCallNotification(CargoCall call) throws CargoException {
        sendCallNotification(call, true);
    }

    protected boolean sendCallNotification(CargoCall call, boolean enableQueuing) throws CargoException {
        NotificationCommand notification = new NotificationCommand(call.getNotificationId(), DeviceConstants.GUID_ID_CALL, call.toBytes());
        notification.setQueueLimit(call.getNotificationId().getQueueLimit());
        return getServiceConnection().sendCommandAsync(notification);
    }

    public boolean setDeviceUTCTime(int minMinutes) throws CargoException {
        ServiceCommand cmd = new ServiceCommand(BandDeviceConstants.Command.CargoSyncDeviceTime);
        cmd.getBundle().putInt(CargoConstants.EXTRA_ALLOWED_DEVICE_TIME_DRIFT_IN_MINUTES, minMinutes);
        return getServiceConnection().sendCommandAsync(cmd);
    }

    public Date getDeviceUTCTime() throws CargoException {
        GetUTCTime cmdGetUTC = new GetUTCTime();
        getServiceConnection().sendCommand(cmdGetUTC);
        return cmdGetUTC.getDeviceUTC().toDate();
    }

    public DeviceProfileInfo getDeviceProfile() throws CargoException {
        int hardwareVersion = getHardwareVersion();
        DeviceProfileGet cmdDP = new DeviceProfileGet(hardwareVersion);
        getServiceConnection().sendCommand(cmdDP);
        return cmdDP.getDeviceProfileInfo();
    }

    public void setDeviceProfileUnitPrefs(UnitType weightUnitType, UnitType distanceUnitType, UnitType temperatureUnitType) throws CargoException {
        DeviceProfileInfo dpi = getDeviceProfile();
        dpi.setDisplayWeightUnit(weightUnitType);
        dpi.setDisplayDistanceUnit(distanceUnitType);
        dpi.setDisplayTemperatureUnit(temperatureUnitType);
        dpi.setRunDisplayUnits(UserProfileInfo.RunDisplayUnits.Local);
        saveDeviceProfile(dpi, System.currentTimeMillis());
    }

    public void saveDeviceProfile(DeviceProfileInfo deviceProfileInfo, long currentTime) throws CargoException {
        Validation.validateNullParameter(deviceProfileInfo, "Device profile object must not be null");
        deviceProfileInfo.setTimeStampUTC(currentTime);
        DeviceProfileSet cmd = new DeviceProfileSet(deviceProfileInfo);
        getServiceConnection().sendCommandAsync(cmd);
    }

    public void resetDeviceProfile(UserProfileInfo upi, DeviceProfileInfo dpi, long currentTime) throws CargoException {
        dpi.updateUsingCloudUserProfile(upi);
        saveDeviceProfile(dpi, currentTime);
        if (upi.getFirmwareByteArray() != null) {
            ProfileByteArray pba = new ProfileByteArray(dpi, upi.getFirmwareByteArray());
            DeviceProfileByteArraySet dpbas = new DeviceProfileByteArraySet(pba);
            getServiceConnection().sendCommandAsync(dpbas);
        }
    }

    public FirmwareVersions getFirmwareVerison() throws CargoException {
        GetVersion cmd = new GetVersion();
        getServiceConnection().sendCommand(cmd);
        FWVersion[] versions = cmd.getFWVersion();
        return new FirmwareVersions(versions);
    }

    public boolean getFirmwareVerisonValidation() throws CargoException {
        GetVersionValidation cmd = new GetVersionValidation();
        getServiceConnection().sendCommand(cmd);
        return cmd.isResourcesValid();
    }

    public String getProductSerialNumber() throws CargoException {
        GetProductSerialNumber cmd = new GetProductSerialNumber();
        getServiceConnection().sendCommand(cmd);
        return cmd.getSerialNumber();
    }

    public void sendCalendarEvents(CalendarEvent[] calendarArray) throws CargoException, IllegalArgumentException {
        Validation.validateNullParameter(calendarArray, "Send Calendar Events");
        int len = calendarArray.length;
        if (len == 0 || len > 8) {
            throw new IllegalArgumentException("Invalid amount of Calendar Events");
        }
        sendCalendarEventClearNotification();
        for (CalendarEvent calendarEvent : calendarArray) {
            sendCalendarEvent(calendarEvent);
        }
    }

    public boolean sendCalendarEvent(CalendarEvent calendarEvent) throws CargoException {
        NotificationCommand notification = new NotificationCommand(DeviceConstants.NotificationID.CALENDAR_EVENT_ADD, DeviceConstants.GUID_ID_CALENDAR, calendarEvent.toBytes());
        return getServiceConnection().sendCommand(notification);
    }

    public boolean sendCalendarEventClearNotification() throws CargoException {
        NotificationCommand notification = new NotificationCommand(DeviceConstants.NotificationID.CALENDAR_CLEAR, DeviceConstants.GUID_ID_CALENDAR, new byte[22]);
        return getServiceConnection().sendCommandAsync(notification);
    }

    public boolean sendHapticFeedback(Haptic haptic) throws CargoException {
        CommandWrite cmd = new CommandWrite(BandDeviceConstants.Command.CargoHapticPlayVibrationStream, null, new byte[]{haptic.mValue});
        return getServiceConnection().sendCommandAsync(cmd);
    }

    public boolean sendNavigateToScreen(byte screenId) throws CargoException {
        NavigateToScreen navigateToScreen = new NavigateToScreen(screenId);
        return getServiceConnection().sendCommandAsync(navigateToScreen);
    }

    public boolean sendNavigateToScreenGuid(byte screenGuid) throws CargoException {
        NavigateToScreenGuid navigateToScreenGuid = new NavigateToScreenGuid(screenGuid);
        return getServiceConnection().sendCommandAsync(navigateToScreenGuid);
    }

    public int finalizeOOBE() throws CargoException {
        OOBEFinalizeCommand cmd = new OOBEFinalizeCommand();
        getServiceConnection().sendCommand(cmd);
        return cmd.getResultCode();
    }

    public OOBEStage getOOBEStageIndex() throws CargoException {
        OOBEGetStageCommand cmd = new OOBEGetStageCommand();
        getServiceConnection().sendCommand(cmd);
        return OOBEStage.values()[cmd.getIndex()];
    }

    public void setOOBEStageIndex(OOBEStage index) throws CargoException {
        OOBESetStageCommand cmd = new OOBESetStageCommand((short) index.ordinal());
        getServiceConnection().sendCommand(cmd);
    }

    public void setStrappTileImageIndex(UUID id, int tileImageIndex) throws CargoException {
        validateAppId(id, "setStrappTileImageIndex: id");
        Validation.validateInRange("setStrappTileImageIndextileImageIndex", tileImageIndex, 0, InternalBandConstants.getMaxIconsPerTile(getHardwareVersion()) - 1);
        Bundle bundle = new Bundle();
        bundle.putSerializable(InternalBandConstants.EXTRA_UUID, id);
        bundle.putInt(InternalBandConstants.EXTRA_COMMAND_DATA, tileImageIndex);
        ServiceCommand cmd = new ServiceCommand(BandDeviceConstants.Command.CargoTileSetTileImageIndex, bundle);
        getServiceConnection().sendCommand(cmd);
    }

    public void setStrappBadgeImageIndex(UUID id, int badgeImageIndex) throws CargoException {
        validateAppId(id, "setStrappBadgeImageIndex: id");
        Validation.validateInRange("setStrappBadgeImageIndexbadgeImageIndex", badgeImageIndex, 0, InternalBandConstants.getMaxIconsPerTile(getHardwareVersion()) - 1);
        Bundle bundle = new Bundle();
        bundle.putSerializable(InternalBandConstants.EXTRA_UUID, id);
        bundle.putInt(InternalBandConstants.EXTRA_COMMAND_DATA, badgeImageIndex);
        ServiceCommand cmd = new ServiceCommand(BandDeviceConstants.Command.CargoTileSetBadgeImageIndex, bundle);
        getServiceConnection().sendCommand(cmd);
    }

    public void setStrappNotificationImageIndex(UUID id, int NotificationImageIndex) throws CargoException {
        verifyV2Band();
        validateAppId(id, "setStrappNotificationImageIndex: id");
        Validation.validateInRange("setStrappNotificationImageIndexNotificationImageIndex", NotificationImageIndex, 0, InternalBandConstants.getMaxIconsPerTile(getHardwareVersion()) - 1);
        Bundle bundle = new Bundle();
        bundle.putSerializable(InternalBandConstants.EXTRA_UUID, id);
        bundle.putInt(InternalBandConstants.EXTRA_COMMAND_DATA, NotificationImageIndex);
        ServiceCommand cmd = new ServiceCommand(BandDeviceConstants.Command.CargoTileSetNotificationImageIndex, bundle);
        getServiceConnection().sendCommand(cmd);
    }

    public CargoStrapp getStrapp(UUID id, boolean withImage) throws CargoException {
        Bundle bundle = new Bundle();
        bundle.putBoolean(InternalBandConstants.EXTRA_COMMAND_DATA, withImage);
        bundle.putSerializable(InternalBandConstants.EXTRA_UUID, id);
        ServiceCommand cmd = new ServiceCommand(BandDeviceConstants.Command.CargoTileGetStrapp, bundle);
        getServiceConnection().sendCommand(cmd);
        return (CargoStrapp) cmd.getBundle().getParcelable(InternalBandConstants.EXTRA_COMMAND_PAYLOAD);
    }

    private void validateAppId(UUID id, String paraName) throws CargoException {
        Validation.validateNullParameter(id, paraName);
        StartStrip strip = getStartStrip(false);
        if (!strip.contains(id)) {
            throw new IllegalArgumentException(String.format("%s is not a valid app on device", paraName));
        }
    }

    public void setStrapp(CargoStrapp strapp) throws CargoException {
        Validation.validateNullParameter(strapp, "updateStrapp:");
        validateStrappImages(strapp, getHardwareVersion());
        Bundle bundle = new Bundle();
        bundle.putParcelable(InternalBandConstants.EXTRA_COMMAND_DATA, strapp);
        ServiceCommand cmd = new ServiceCommand(BandDeviceConstants.Command.CargoTileUpdateStrapp, bundle);
        getServiceConnection().sendCommand(cmd);
    }

    public StartStrip getStartStrip(boolean withImage) throws CargoException {
        Bundle bundle = new Bundle();
        bundle.putBoolean(InternalBandConstants.EXTRA_COMMAND_DATA, withImage);
        ServiceCommand cmd = new ServiceCommand(BandDeviceConstants.Command.CargoTileGetStartStrip, bundle);
        getServiceConnection().sendCommand(cmd);
        return (StartStrip) cmd.getBundle().getParcelable(InternalBandConstants.EXTRA_COMMAND_PAYLOAD);
    }

    public void setStartStrip(StartStrip strapps) throws CargoException {
        setStartStripValidator(strapps);
        Bundle bundle = new Bundle();
        bundle.putParcelable(InternalBandConstants.EXTRA_COMMAND_DATA, strapps);
        ServiceCommand cmd = new ServiceCommand(BandDeviceConstants.Command.CargoTileSetStartStrip, bundle);
        getServiceConnection().sendCommand(cmd);
    }

    private void setStartStripValidator(StartStrip strapps) throws CargoException {
        Validation.validateNullParameter(strapps, "setStartStrip:apps");
        Validation.validateInRange("setStartStrip:apps count", strapps.getCount(), 0, getInstalledAppListMaxCount());
        int hardwareVersion = getHardwareVersion();
        for (CargoStrapp strapp : strapps.getAppList()) {
            validateStrappImages(strapp, hardwareVersion);
        }
    }

    private void validateStrappImages(CargoStrapp strapp, int hardwareVersion) {
        if (strapp.getImages().size() > InternalBandConstants.getMaxIconsPerTile(hardwareVersion)) {
            throw new IllegalArgumentException(String.format("Strapp can not have more than %d icons", Integer.valueOf(InternalBandConstants.getMaxIconsPerTile(hardwareVersion))));
        }
    }

    public StartStrip getDefaultStrapps(boolean withImage) throws CargoException {
        Bundle bundle = new Bundle();
        bundle.putBoolean(InternalBandConstants.EXTRA_COMMAND_DATA, withImage);
        ServiceCommand cmd = new ServiceCommand(BandDeviceConstants.Command.CargoTileGetDefaultStrip, bundle);
        getServiceConnection().sendCommand(cmd);
        return (StartStrip) cmd.getBundle().getParcelable(InternalBandConstants.EXTRA_COMMAND_PAYLOAD);
    }

    public int getStrappMaxCount() throws CargoException {
        ServiceCommand cmd = new ServiceCommand(BandDeviceConstants.Command.CargoTileGetMaxStrappCount);
        getServiceConnection().sendCommand(cmd);
        return cmd.getBundle().getInt(InternalBandConstants.EXTRA_COMMAND_PAYLOAD);
    }

    public int getStrappSettingsMask(UUID appId) throws CargoException {
        validateAppId(appId, "getStrappSettingMask: strappId");
        Bundle bundle = new Bundle();
        bundle.putSerializable(InternalBandConstants.EXTRA_UUID, appId);
        ServiceCommand cmd = new ServiceCommand(BandDeviceConstants.Command.CargoTileGetSettingsMask, bundle);
        getServiceConnection().sendCommand(cmd);
        return cmd.getBundle().getInt(InternalBandConstants.EXTRA_COMMAND_PAYLOAD);
    }

    public void setStrappSettingsMask(UUID appId, int settingMask) throws CargoException {
        validateAppId(appId, "setStrappSettingMask: strappId");
        Bundle bundle = new Bundle();
        bundle.putSerializable(InternalBandConstants.EXTRA_UUID, appId);
        bundle.putInt(InternalBandConstants.EXTRA_COMMAND_DATA, settingMask);
        ServiceCommand cmd = new ServiceCommand(BandDeviceConstants.Command.CargoTileSetSettingsMask, bundle);
        getServiceConnection().sendCommandAsync(cmd);
    }

    public DeviceConstants.AppRunning getRunningApplication() throws CargoException {
        CommandRead cmd = new CommandRead(BandDeviceConstants.Command.CargoCoreModuleWhoAmI, null, 1);
        getServiceConnection().sendCommand(cmd);
        return DeviceConstants.AppRunning.lookup(cmd.getResultByte()[0]);
    }

    public boolean sendPageUpdate(UUID strappId, UUID pageId, int pageLayoutIndex, List<StrappPageElement> textFields) throws CargoException {
        validateAppId(strappId, "sendPageUpdate: strappId");
        int hardwareVersion = getHardwareVersion();
        for (StrappPageElement element : textFields) {
            element.validate(hardwareVersion);
        }
        NotificationGenericUpdate genericUpdate = new NotificationGenericUpdate(pageId, pageLayoutIndex, textFields);
        Bundle bundle = new Bundle();
        bundle.putSerializable(InternalBandConstants.EXTRA_UUID, strappId);
        bundle.putByteArray(InternalBandConstants.EXTRA_COMMAND_DATA, genericUpdate.toBytes());
        ServiceCommand cmd = new ServiceCommand(BandDeviceConstants.Command.CargoTileSetPage, bundle);
        return getServiceConnection().sendCommand(cmd);
    }

    public boolean sendStrappDialog(UUID strappId, String lineOne, String lineTwo, boolean forceDialog) throws CargoException {
        validateAppId(strappId, "sendStrappDialog: strappId");
        NotificationGenericDialog genericDialog = new NotificationGenericDialog(lineOne, lineTwo, forceDialog);
        return sendGenericDialogNotification(strappId, genericDialog);
    }

    public boolean clearStrapp(UUID strappId) throws CargoException {
        validateAppId(strappId, "clearStrapp: strappId");
        Bundle bundle = new Bundle();
        bundle.putSerializable(InternalBandConstants.EXTRA_UUID, strappId);
        ServiceCommand cmd = new ServiceCommand(BandDeviceConstants.Command.CargoTileClearPages, bundle);
        return getServiceConnection().sendCommand(cmd);
    }

    public boolean clearPage(UUID strappId, UUID pageId) throws CargoException {
        validateAppId(strappId, "clearPage: strappId");
        Validation.validateNullParameter(pageId, "clearPage: pageId");
        return sendGenericClearPageNotification(strappId, pageId);
    }

    public boolean sendStrappMessage(UUID strappId, StrappMessage message) throws CargoException {
        return sendStrappMessage(strappId, message, false);
    }

    public void queueStrappMessage(UUID strappId, StrappMessage message) throws CargoException {
        sendStrappMessage(strappId, message, true);
    }

    protected boolean sendStrappMessage(UUID strappId, StrappMessage message, boolean enableQueuing) throws CargoException {
        Validation.validateNullParameter(strappId, "sendStrappMessage: strappId");
        Validation.validateNullParameter(message, "sendStrappMessage: message");
        NotificationCommand cmdNotification = new NotificationCommand(DeviceConstants.NotificationID.MESSAGING, strappId.toString(), message.toBytes());
        if (enableQueuing) {
            cmdNotification.setQueueLimit(DeviceConstants.NotificationID.MESSAGING.getQueueLimit());
        }
        return getServiceConnection().sendCommandAsync(cmdNotification);
    }

    public boolean pushFirmwareUpdateToDevice(CargoFirmwareUpdateInfo firmwareUpdateInfo) throws CargoException {
        ServiceCommand cmd = new ServiceCommand(BandDeviceConstants.Command.CargoUpgradeFirmare);
        cmd.getBundle().putParcelable(CargoConstants.EXTRA_CLOUD_DATA, firmwareUpdateInfo);
        return getServiceConnection().sendCommand(cmd);
    }

    public boolean pushEphemerisSettingsFileToDevice(EphemerisUpdateInfo ephemerisUpdateInfo) throws CargoException {
        ServiceCommand cmd = new ServiceCommand(BandDeviceConstants.Command.CargoUpgradeEphemeris);
        cmd.getBundle().putParcelable(CargoConstants.EXTRA_CLOUD_DATA, ephemerisUpdateInfo);
        return getServiceConnection().sendCommand(cmd);
    }

    public boolean pushTimeZoneSettingsFileToDevice(TimeZoneSettingsUpdateInfo timezoneSettingsInfo) throws CargoException {
        ServiceCommand cmd = new ServiceCommand(BandDeviceConstants.Command.CargoUpgradeTimeZoneSettings);
        cmd.getBundle().putParcelable(CargoConstants.EXTRA_CLOUD_DATA, timezoneSettingsInfo);
        return getServiceConnection().sendCommand(cmd);
    }

    public long getMeTileId() throws CargoException {
        ServiceCommand cmd = new ServiceCommand(BandDeviceConstants.Command.CargoPersonalizationGetMeTileId);
        getServiceConnection().sendCommand(cmd);
        return cmd.getBundle().getLong(InternalBandConstants.EXTRA_COMMAND_PAYLOAD);
    }

    public boolean setMeTileImage(Bitmap image, long id) throws CargoException {
        setMeTileImageValidator(image, id);
        Bundle bundle = new Bundle();
        bundle.putByteArray(InternalBandConstants.EXTRA_SERVICE_COMMAND_PAYLOAD, BandImage.bitmapToBGR565(image));
        bundle.putLong(InternalBandConstants.EXTRA_COMMAND_DATA, id);
        ServiceCommand cmd = new ServiceCommand(BandDeviceConstants.Command.BandPersonalizationSetMeTile, bundle);
        return getServiceConnection().sendCommand(cmd);
    }

    private void setMeTileImageValidator(Bitmap image, long id) throws CargoException {
        int hardwareVersion = getHardwareVersion();
        BandImage.validateMeTileImage(image, hardwareVersion);
        if (id <= 0) {
            throw new IllegalArgumentException("MeTile Id cannot be zero or negative.");
        }
    }

    public boolean clearMeTileImage() throws CargoException {
        return getServiceConnection().sendCommandAsync(new ServiceCommand(BandDeviceConstants.Command.BandPersonalizationClearMeTile));
    }

    public Bitmap readMeTileImage() throws CargoException {
        long id = getMeTileId();
        if (id == 0) {
            throw new CargoException("No valid Me Tile on device.", BandServiceMessage.Response.NO_METILE_IMAGE);
        }
        ServiceCommand cmd = new ServiceCommand(BandDeviceConstants.Command.BandPersonalizationGetMeTile);
        getServiceConnection().sendCommand(cmd);
        int hardwareVersion = getHardwareVersion();
        return VersionCheck.isV2DeviceOrGreater(hardwareVersion) ? BandImage.getBitmapFromBGR565(cmd.getBundle().getByteArray(InternalBandConstants.EXTRA_COMMAND_PAYLOAD), 128, 310) : BandImage.getBitmapFromBGR565(cmd.getBundle().getByteArray(InternalBandConstants.EXTRA_COMMAND_PAYLOAD), 102, 310);
    }

    public boolean setThemeColorForDefaultStrapps(StrappColorPalette colorPalette) throws CargoException {
        setDeviceThemeValidator(colorPalette);
        Bundle bundle = new Bundle();
        bundle.putParcelable(InternalBandConstants.EXTRA_SERVICE_COMMAND_PAYLOAD, colorPalette.getTheme());
        ServiceCommand cmd = new ServiceCommand(BandDeviceConstants.Command.BandPersonalizationSetTheme, bundle);
        return getServiceConnection().sendCommand(cmd);
    }

    private void setDeviceThemeValidator(StrappColorPalette colorPalette) {
        Validation.validateNullParameter(colorPalette, "Strapp ColorPalette");
    }

    public StrappColorPalette getThemeColorForDefaultStrapps() throws CargoException {
        ServiceCommand cmd = new ServiceCommand(BandDeviceConstants.Command.BandPersonalizationGetTheme);
        getServiceConnection().sendCommand(cmd);
        return new StrappColorPalette((BandTheme) cmd.getBundle().getParcelable(InternalBandConstants.EXTRA_COMMAND_PAYLOAD));
    }

    public void turnTelemetryLoggingOnOff(boolean f) throws CargoException {
        ServiceCommand command = new ServiceCommand(BandDeviceConstants.Command.CargoTurnTelemetryOnOff);
        command.getBundle().putBoolean(InternalBandConstants.EXTRA_COMMAND_DATA, f);
        getServiceConnection().sendCommand(command);
    }

    public void turnPerformanceLoggingOnOff(boolean f) throws CargoException {
        ServiceCommand command = new ServiceCommand(BandDeviceConstants.Command.CargoTurnPerformanceOnOff);
        command.getBundle().putBoolean(InternalBandConstants.EXTRA_COMMAND_DATA, f);
        getServiceConnection().sendCommand(command);
    }

    public void turnDiagnosticsLoggingOnOff(boolean f) throws CargoException {
        ServiceCommand command = new ServiceCommand(BandDeviceConstants.Command.CargoTurnDiagnosticsOnOff);
        command.getBundle().putBoolean(InternalBandConstants.EXTRA_COMMAND_DATA, f);
        getServiceConnection().sendCommand(command);
    }

    public boolean setThemeColorForCustomStrappByUUID(StrappColorPalette colorPalette, UUID strappId) throws CargoException {
        setDeviceThemeValidator(colorPalette);
        validateCustomAppId(strappId, "setThemeColorThirdParty: id", null);
        Bundle bundle = new Bundle();
        bundle.putSerializable(InternalBandConstants.EXTRA_COMMAND_DATA, strappId);
        bundle.putParcelable(InternalBandConstants.EXTRA_SERVICE_COMMAND_PAYLOAD, colorPalette.getTheme());
        ServiceCommand cmd = new ServiceCommand(BandDeviceConstants.Command.CargoPersonalizationSetCustomTileTheme, bundle);
        return getServiceConnection().sendCommand(cmd);
    }

    private void validateCustomAppId(UUID id, String paraName, StartStrip strip) throws CargoException {
        Validation.validateNullParameter(id, paraName);
        if (getDefaultStrapps(false).contains(id)) {
            throw new IllegalArgumentException(String.format("%s is from default strapp, not a valid custom strapp", paraName));
        }
        if (strip == null) {
            validateAppId(id, paraName);
        } else if (!strip.contains(id)) {
            throw new IllegalArgumentException(String.format("%s is not a valid custom strapp", paraName));
        }
    }

    public boolean resetThemeColorForAllStrapps() throws CargoException {
        return getServiceConnection().sendCommand(new ServiceCommand(BandDeviceConstants.Command.BandPersonalizationResetTheme));
    }

    public void personalizeDevice(StartStrip startStrip, Bitmap image, StrappColorPalette colorPalette, long imageId, Map<UUID, StrappColorPalette> customColors) throws CargoException {
        Bundle bundle = new Bundle();
        if (image != null) {
            setMeTileImageValidator(image, imageId);
            bundle.putByteArray(InternalBandConstants.EXTRA_PERSONALIZE_METILE, BandImage.bitmapToBGR565(image));
            bundle.putLong(InternalBandConstants.EXTRA_PERSONALIZE_METILE_ID, imageId);
        }
        if (colorPalette != null) {
            setDeviceThemeValidator(colorPalette);
            bundle.putParcelable(InternalBandConstants.EXTRA_PERSONALIZE_THEME, colorPalette.getTheme());
        }
        if (startStrip != null) {
            setStartStripValidator(startStrip);
            bundle.putParcelable(InternalBandConstants.EXTRA_PERSONALIZE_STRAPPS, startStrip);
        }
        if (customColors != null) {
            setStrappThemesValidator(customColors, startStrip);
            bundle.putSerializable(InternalBandConstants.EXTRA_PERSONALIZE_THEME_MAP, (HashMap) customColors);
        }
        ServiceCommand cmd = new ServiceCommand(BandDeviceConstants.Command.CargoPersonalizeDevice, bundle);
        getServiceConnection().sendCommand(cmd);
    }

    public void setStrappThemes(Map<UUID, StrappColorPalette> customColors) throws CargoException {
        setStrappThemesValidator(customColors, null);
        Bundle bundle = new Bundle();
        bundle.putSerializable(InternalBandConstants.EXTRA_PERSONALIZE_THEME_MAP, (HashMap) customColors);
        ServiceCommand cmd = new ServiceCommand(BandDeviceConstants.Command.CargoPersonalizationSetCustomTileThemes, bundle);
        getServiceConnection().sendCommand(cmd);
    }

    private void setStrappThemesValidator(Map<UUID, StrappColorPalette> customColors, StartStrip strip) throws CargoException {
        Validation.validateNullParameter(customColors, "Strapp ThemeColors");
        for (Map.Entry<UUID, StrappColorPalette> pair : customColors.entrySet()) {
            validateCustomAppId(pair.getKey(), "Strapp UUID", strip);
            setDeviceThemeValidator(pair.getValue());
        }
    }

    public void setGoals(GoalsData goals) throws CargoException {
        ByteBuffer writeBuf = BufferUtil.allocateLittleEndian(GoalsData.getDataSize(getHardwareVersion()));
        writeBuf.put(goals.toBytes(getHardwareVersion()));
        CommandWrite cmd = new CommandWrite(BandDeviceConstants.Command.CargoGoalTrackerSet, null, writeBuf.array());
        getServiceConnection().sendCommand(cmd);
    }

    public void setPhoneCallResponses(String m1, String m2, String m3, String m4) throws CargoException {
        setResponses(SmsResponseType.CALL, m1, m2, m3, m4);
    }

    public void setSmsResponses(String m1, String m2, String m3, String m4) throws CargoException {
        setResponses(SmsResponseType.SMS, m1, m2, m3, m4);
    }

    private void setResponses(SmsResponseType type, String m1, String m2, String m3, String m4) throws CargoException {
        if (m1 == null || m2 == null || m3 == null || m4 == null) {
            throw new IllegalArgumentException("message can't be null");
        }
        getServiceConnection().sendCommand(new SetSmsResponse(type, 0, m1));
        getServiceConnection().sendCommand(new SetSmsResponse(type, 1, m2));
        getServiceConnection().sendCommand(new SetSmsResponse(type, 2, m3));
        getServiceConnection().sendCommand(new SetSmsResponse(type, 3, m4));
    }

    public void setAllResponses(String... responses) throws CargoException {
        getServiceConnection().sendCommand(new SetSmsResponseAll(responses));
    }

    public String[] getAllResponses() throws CargoException {
        GetSmsResponseAll getSms = new GetSmsResponseAll();
        getServiceConnection().sendCommand(getSms);
        return getSms.getMessages();
    }

    public void setOOBECompleted() throws CargoException {
        CommandWrite cmd = new CommandWrite(BandDeviceConstants.Command.CargoUIEndConnectedOOBE, null, null);
        getServiceConnection().sendCommand(cmd);
    }

    private int getInstalledAppListMaxCount() throws CargoException {
        CommandRead cmd = new CommandRead(BandDeviceConstants.Command.CargoInstalledAppListGetMaxCount, null, 4);
        getServiceConnection().sendCommand(cmd);
        ByteBuffer buffer = ByteBuffer.wrap(cmd.getResultByte()).order(ByteOrder.LITTLE_ENDIAN);
        return (int) BitHelper.unsignedIntegerToLong(buffer.getInt());
    }

    private boolean sendGenericDialogNotification(UUID strappId, NotificationGenericDialog dialog) throws CargoException {
        NotificationCommand notification = new NotificationCommand(DeviceConstants.NotificationID.GENERIC_DIALOG, strappId.toString(), dialog.toBytes());
        return getServiceConnection().sendCommandAsync(notification);
    }

    private boolean sendGenericClearPageNotification(UUID strappId, UUID pageId) throws CargoException {
        ByteBuffer writeBuf = BufferUtil.allocateLittleEndian(18).put(UUIDHelper.toGuidArray(pageId)).put((byte) 0).put((byte) 0);
        NotificationCommand notification = new NotificationCommand(DeviceConstants.NotificationID.GENERIC_CLEAR_PAGE, strappId.toString(), writeBuf.array());
        return getServiceConnection().sendCommandAsync(notification);
    }

    public TimeZoneInfo getTimeZone() throws CargoException {
        CommandRead cmd = new CommandRead(BandDeviceConstants.Command.CargoSystemSettingsGetTimeZone, null, 96);
        getServiceConnection().sendCommand(cmd);
        if (!cmd.getResult()) {
            return null;
        }
        ByteBuffer buffer = ByteBuffer.wrap(cmd.getResultByte()).order(ByteOrder.LITTLE_ENDIAN);
        TimeZoneInfo timeZoneInfo = new TimeZoneInfo(buffer);
        return timeZoneInfo;
    }

    public SystemTimeInfo getLocalTime() throws CargoException {
        CommandRead cmd = new CommandRead(BandDeviceConstants.Command.CargoTimeGetLocalTime, null, 16);
        getServiceConnection().sendCommand(cmd);
        if (!cmd.getResult()) {
            return null;
        }
        ByteBuffer buffer = ByteBuffer.wrap(cmd.getResultByte()).order(ByteOrder.LITTLE_ENDIAN);
        SystemTimeInfo systemTimeInfo = new SystemTimeInfo(buffer);
        return systemTimeInfo;
    }

    public boolean setTimeZone(TimeZoneInfo timeZoneInfo) throws CargoException {
        Validation.validateNullParameter(timeZoneInfo, "setTimeZone: TimeZoneInfo");
        ByteBuffer writeBuf = BufferUtil.allocateLittleEndian(96).put(timeZoneInfo.toBytes());
        CommandWrite cmd = new CommandWrite(BandDeviceConstants.Command.CargoSystemSettingsSetTimeZone, null, writeBuf.array());
        return getServiceConnection().sendCommand(cmd);
    }

    public boolean isTimeSyncEnabled() throws CargoException {
        CommandRead cmd = new CommandRead(BandDeviceConstants.Command.CargoSystemSettingsGetTimeSyncEnabled, null, 4);
        getServiceConnection().sendCommand(cmd);
        if (!cmd.getResult()) {
            return false;
        }
        ByteBuffer buffer = ByteBuffer.wrap(cmd.getResultByte()).order(ByteOrder.LITTLE_ENDIAN);
        return buffer.getInt() != 0;
    }

    public boolean setTimeSyncEnabled(boolean timeSync) throws CargoException {
        Validation.validateNullParameter(Boolean.valueOf(timeSync), "setTimeSyncEnabled: timeSync");
        ByteBuffer writeBuf = BufferUtil.allocateLittleEndian(4).putInt(timeSync ? 1 : 0);
        CommandWrite cmd = new CommandWrite(BandDeviceConstants.Command.CargoSystemSettingsSetTimeSyncEnabled, null, writeBuf.array());
        return getServiceConnection().sendCommand(cmd);
    }

    public boolean isOOBECompleted() throws CargoException {
        CommandRead cmd = new CommandRead(BandDeviceConstants.Command.CargoSystemSettingsOOBEGet, null, 4);
        getServiceConnection().sendCommand(cmd);
        if (!cmd.getResult()) {
            return false;
        }
        ByteBuffer buffer = ByteBuffer.wrap(cmd.getResultByte()).order(ByteOrder.LITTLE_ENDIAN);
        return buffer.getInt() != 0;
    }

    public void setSleepNotification(SleepNotification sNotification) throws CargoException {
        verifyV2Band();
        Validation.validateNullParameter(sNotification, "SleepNotification");
        SetSleepNotification cmd = new SetSleepNotification(sNotification);
        getServiceConnection().sendCommand(cmd);
    }

    public SleepNotification getSleepNotification() throws CargoException {
        verifyV2Band();
        GetSleepNotification cmd = new GetSleepNotification();
        getServiceConnection().sendCommand(cmd);
        return cmd.getSleepNotification();
    }

    public void disableSleepNotification() throws CargoException {
        verifyV2Band();
        DisableSleepNotification cmd = new DisableSleepNotification();
        getServiceConnection().sendCommand(cmd);
    }

    public void setRunDisplayMetrics(RunDisplayMetricType[] metrics) throws CargoException {
        Validation.validateNullParameter(metrics, "setRunDisplayMetrics: metrics");
        CommandWrite cmd = new CommandWrite(BandDeviceConstants.Command.CargoAppDataSetRunMetrics, null, CargoRunDisplayMetrics.toBytesWithValidation(metrics, getHardwareVersion()));
        getServiceConnection().sendCommand(cmd);
    }

    public RunDisplayMetricType[] getRunDisplayMetrics() throws CargoException {
        int hardwareVersion = getHardwareVersion();
        CommandRead cmd = new CommandRead(BandDeviceConstants.Command.CargoAppDataGetRunMetrics, null, CargoRunDisplayMetrics.getStructSize(hardwareVersion));
        getServiceConnection().sendCommand(cmd);
        return CargoRunDisplayMetrics.getDisplayMetricsFromData(cmd.getResultByte(), hardwareVersion);
    }

    public void setBikeDisplayMetrics(BikeDisplayMetricType[] metrics) throws CargoException {
        Validation.validateNullParameter(metrics, "setBikeDisplayMetrics: metrics");
        CommandWrite cmd = new CommandWrite(BandDeviceConstants.Command.CargoAppDataSetBikeMetrics, null, CargoBikeDisplayMetrics.toBytesWithValidation(metrics, getHardwareVersion()));
        getServiceConnection().sendCommand(cmd);
    }

    public BikeDisplayMetricType[] getBikeDisplayMetrics() throws CargoException {
        int hardwareVersion = getHardwareVersion();
        CommandRead cmd = new CommandRead(BandDeviceConstants.Command.CargoAppDataGetBikeMetrics, null, CargoBikeDisplayMetrics.getStructSize(hardwareVersion));
        getServiceConnection().sendCommand(cmd);
        return CargoBikeDisplayMetrics.getDisplayMetricsFromData(cmd.getResultByte(), hardwareVersion);
    }

    public void setBikeSplitMultiplier(int multiplier) throws CargoException {
        Validation.validateInRange("Bike Split Multiplier", multiplier, 1, 255);
        ByteBuffer writeBuf = BufferUtil.allocateLittleEndian(4).putInt(multiplier);
        CommandWrite cmd = new CommandWrite(BandDeviceConstants.Command.CargoAppDataSetBikeSplitDist, null, writeBuf.array());
        getServiceConnection().sendCommand(cmd);
    }

    public int getBikeSplitMultiplier() throws CargoException {
        CommandRead cmd = new CommandRead(BandDeviceConstants.Command.CargoAppDataGetBikeSplitDist, null, 4);
        getServiceConnection().sendCommand(cmd);
        ByteBuffer buffer = ByteBuffer.wrap(cmd.getResultByte()).order(ByteOrder.LITTLE_ENDIAN);
        return buffer.getInt();
    }

    public void setWorkoutActivities(List<WorkoutActivity> workoutActivityData) throws CargoException {
        verifyV2Band();
        Validation.validateNullParameter(workoutActivityData, "Workout activity array");
        int activityCount = workoutActivityData.size();
        Validation.validateInRange("Workout activity type data", activityCount, 1, 15);
        for (WorkoutActivity workout : workoutActivityData) {
            Validation.validateNullParameter(workout, "Workout activity data");
        }
        ByteBuffer writeBuf = BufferUtil.allocateLittleEndian(WorkoutActivity.WORKOUT_ACTIVITY_STRUCTURE_SIZE);
        for (int i = 0; i < activityCount; i++) {
            writeBuf.put(workoutActivityData.get(i).toBytes());
        }
        writeBuf.position(1710);
        writeBuf.putInt(activityCount);
        CommandWrite cmd = new CommandWrite(BandDeviceConstants.Command.CargoAppDataSetWorkoutActivities, null, writeBuf.array());
        getServiceConnection().sendCommand(cmd);
    }

    public List<WorkoutActivity> getWorkoutActivities() throws CargoException {
        List<WorkoutActivity> result = null;
        verifyV2Band();
        CommandRead cmd = new CommandRead(BandDeviceConstants.Command.CargoAppDataGetWorkoutActivities, null, WorkoutActivity.WORKOUT_ACTIVITY_STRUCTURE_SIZE);
        getServiceConnection().sendCommand(cmd);
        ByteBuffer buffer = ByteBuffer.wrap(cmd.getResultByte()).order(ByteOrder.LITTLE_ENDIAN);
        buffer.position(1710);
        int activityCount = buffer.getInt();
        buffer.rewind();
        if (activityCount > 0 && activityCount <= 15) {
            result = new ArrayList<>();
            for (int i = 0; i < activityCount; i++) {
                result.add(new WorkoutActivity(buffer));
            }
        }
        return result;
    }

    public <T extends CommandStruct> T getStatistics(T statisticStruct) throws CargoException {
        GetStatistics<T> command = new GetStatistics<>(statisticStruct);
        getServiceConnection().sendCommand(command);
        return command.getStatisticsStruct();
    }

    public int getFitnessPlanMaxFileSize() throws CargoException {
        FileGetSize fileGetSize = new FileGetSize(CargoFileName.FITNESS_PLANS);
        getServiceConnection().sendCommand(fileGetSize);
        return fileGetSize.getFileSize();
    }

    public void pushFitnessPlan(byte[] data) throws CargoException {
        int maxSize = getFitnessPlanMaxFileSize();
        if (maxSize < data.length) {
            throw new CargoException("Maximum allowed size of file is " + maxSize + " bytes, but got " + data.length + " bytes", BandServiceMessage.Response.INVALID_ARG_ERROR);
        }
        getServiceConnection().sendCommand(new FileWrite(CargoFileName.FITNESS_PLANS, data));
    }

    public int getGolfCourseMaxFileSize() throws CargoException {
        FileGetSize fileGetSize = new FileGetSize(CargoFileName.GOLF_COURSE);
        getServiceConnection().sendCommand(fileGetSize);
        return fileGetSize.getFileSize();
    }

    public void pushGolfCourse(byte[] data) throws CargoException {
        int maxSize = getGolfCourseMaxFileSize();
        if (maxSize < data.length) {
            throw new CargoException("Maximum allowed size of file is " + maxSize + " bytes, but got " + data.length + " bytes", BandServiceMessage.Response.INVALID_ARG_ERROR);
        }
        getServiceConnection().sendCommand(new FileWrite(CargoFileName.GOLF_COURSE, data));
    }

    public Bitmap readPegScreen() throws CargoException {
        CommandRead cmd = new CommandRead(BandDeviceConstants.Command.CargoPegRead, null, 67840);
        getServiceConnection().sendCommand(cmd);
        return BandImage.getBitmapFromBGR565(cmd.getResultByte(), 106, 320);
    }

    public void addWebTile(WebTile tile) throws CargoException {
        Validation.validateNullParameter(tile, "web tile");
        Bundle bundle = new Bundle();
        bundle.putParcelable(InternalBandConstants.EXTRA_COMMAND_DATA, tile);
        ServiceCommand cmd = new ServiceCommand(BandDeviceConstants.Command.CargoWebTileAddTile, bundle);
        getServiceConnection().sendCommand(cmd);
    }
}

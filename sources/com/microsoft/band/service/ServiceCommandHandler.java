package com.microsoft.band.service;

import android.os.Bundle;
import android.os.Parcelable;
import com.microsoft.band.BandTheme;
import com.microsoft.band.CargoConstants;
import com.microsoft.band.client.CargoException;
import com.microsoft.band.cloud.CargoFirmwareUpdateInfo;
import com.microsoft.band.cloud.CloudJSONDataModel;
import com.microsoft.band.cloud.EphemerisUpdateInfo;
import com.microsoft.band.cloud.TimeZoneSettingsUpdateInfo;
import com.microsoft.band.cloud.UserProfileInfo;
import com.microsoft.band.device.CargoStrapp;
import com.microsoft.band.device.DeviceConstants;
import com.microsoft.band.device.DeviceProfileInfo;
import com.microsoft.band.device.Haptic;
import com.microsoft.band.device.NotificationGenericDialog;
import com.microsoft.band.device.StartStrip;
import com.microsoft.band.device.StrappColorPalette;
import com.microsoft.band.device.StrappMessage;
import com.microsoft.band.device.command.CommandWrite;
import com.microsoft.band.device.enums.VersionType;
import com.microsoft.band.internal.BandDeviceConstants;
import com.microsoft.band.internal.BandServiceMessage;
import com.microsoft.band.internal.InternalBandConstants;
import com.microsoft.band.internal.ServiceCommand;
import com.microsoft.band.notifications.MessageFlags;
import com.microsoft.band.notifications.VibrationType;
import com.microsoft.band.service.cloud.CloudDataResource;
import com.microsoft.band.service.device.DeviceServiceProvider;
import com.microsoft.band.tiles.BandTile;
import com.microsoft.band.tiles.TileUtils;
import com.microsoft.band.webtiles.WebTile;
import com.microsoft.band.webtiles.WebTileManager;
import java.util.ArrayList;
import java.util.Date;
import java.util.EnumMap;
import java.util.Map;
import java.util.UUID;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class ServiceCommandHandler {
    private static final Map<BandDeviceConstants.Command, ICommandHandler> mHandlers = new EnumMap(BandDeviceConstants.Command.class);

    /* loaded from: classes.dex */
    public interface ICommandHandler {
        BandServiceMessage.Response execute(CargoClientSession cargoClientSession, ServiceCommand serviceCommand, Bundle bundle) throws CargoServiceException;
    }

    static {
        mHandlers.put(BandDeviceConstants.Command.CargoGetCloudProfile, new CommandHandlerWithPermissionCheck() { // from class: com.microsoft.band.service.ServiceCommandHandler.1
            @Override // com.microsoft.band.service.CommandHandlerWithPermissionCheck
            public BandServiceMessage.Response executeChecked(CargoClientSession session, ServiceCommand command, Bundle responseBundle) throws CargoServiceException {
                UserProfileInfo data = session.getCloudProvider().getCloudProfile();
                responseBundle.putParcelable(CargoConstants.EXTRA_CLOUD_DATA, data);
                return BandServiceMessage.Response.SUCCESS;
            }
        });
        mHandlers.put(BandDeviceConstants.Command.CargoSaveUserProfile, new CommandHandlerWithPermissionCheck() { // from class: com.microsoft.band.service.ServiceCommandHandler.2
            @Override // com.microsoft.band.service.CommandHandlerWithPermissionCheck
            public BandServiceMessage.Response executeChecked(CargoClientSession session, ServiceCommand command, Bundle responseBundle) throws CargoServiceException {
                CloudJSONDataModel data = (CloudJSONDataModel) command.getBundle().getParcelable(CargoConstants.EXTRA_CLOUD_DATA);
                session.getCloudProvider().saveUserProfile(data);
                return BandServiceMessage.Response.SUCCESS;
            }
        });
        mHandlers.put(BandDeviceConstants.Command.CargoSendFileToCloud, new CommandHandlerWithPermissionCheck() { // from class: com.microsoft.band.service.ServiceCommandHandler.3
            @Override // com.microsoft.band.service.CommandHandlerWithPermissionCheck
            public BandServiceMessage.Response executeChecked(CargoClientSession session, ServiceCommand command, Bundle responseBundle) throws CargoServiceException {
                String fileName = command.getBundle().getString(CargoConstants.EXTRA_CLOUD_DATA);
                CloudDataResource.LogFileTypes type = (CloudDataResource.LogFileTypes) command.getBundle().getSerializable(CargoConstants.KEY_LOG_TYPE);
                return session.getCloudProvider().sendFileToCloud(fileName, type, session.getUploadMetadata());
            }
        });
        mHandlers.put(BandDeviceConstants.Command.CargoConnectDevice, new ICommandHandler() { // from class: com.microsoft.band.service.ServiceCommandHandler.4
            @Override // com.microsoft.band.service.ServiceCommandHandler.ICommandHandler
            public BandServiceMessage.Response execute(CargoClientSession session, ServiceCommand command, Bundle responseBundle) throws CargoServiceException {
                DeviceServiceProvider deviceProvider = session.getDeviceProvider();
                return deviceProvider == null ? BandServiceMessage.Response.DEVICE_NOT_BONDED_ERROR : deviceProvider.connectDevice();
            }
        });
        mHandlers.put(BandDeviceConstants.Command.CargoDisconnectDevice, new ICommandHandler() { // from class: com.microsoft.band.service.ServiceCommandHandler.5
            @Override // com.microsoft.band.service.ServiceCommandHandler.ICommandHandler
            public BandServiceMessage.Response execute(CargoClientSession session, ServiceCommand command, Bundle responseBundle) throws CargoServiceException {
                return BandServiceMessage.Response.OPERATION_NOT_REQUIRED;
            }
        });
        mHandlers.put(BandDeviceConstants.Command.CargoSyncDeviceToCloud, new CommandHandlerWithPermissionCheck() { // from class: com.microsoft.band.service.ServiceCommandHandler.6
            @Override // com.microsoft.band.service.CommandHandlerWithPermissionCheck
            public BandServiceMessage.Response executeChecked(CargoClientSession session, ServiceCommand command, Bundle responseBundle) throws CargoServiceException {
                boolean fullSync = command.getBundle().getBoolean(InternalBandConstants.EXTRA_COMMAND_DATA);
                return session.syncDeviceToCloud(fullSync);
            }
        });
        mHandlers.put(BandDeviceConstants.Command.CargoSyncWebTiles, new CommandHandlerWithPermissionCheck() { // from class: com.microsoft.band.service.ServiceCommandHandler.7
            @Override // com.microsoft.band.service.CommandHandlerWithPermissionCheck
            public BandServiceMessage.Response executeChecked(CargoClientSession session, ServiceCommand command, Bundle responseBundle) throws CargoServiceException {
                boolean foregroundSync = command.getBundle().getBoolean(InternalBandConstants.EXTRA_COMMAND_DATA);
                return session.syncWebTiles(foregroundSync);
            }
        });
        mHandlers.put(BandDeviceConstants.Command.CargoSyncDeviceTime, new CommandHandlerWithPermissionCheck() { // from class: com.microsoft.band.service.ServiceCommandHandler.8
            @Override // com.microsoft.band.service.CommandHandlerWithPermissionCheck
            public BandServiceMessage.Response executeChecked(CargoClientSession session, ServiceCommand command, Bundle responseBundle) throws CargoServiceException {
                int allowedTimeDifferenceInMinutes = command.getBundle().getInt(CargoConstants.EXTRA_ALLOWED_DEVICE_TIME_DRIFT_IN_MINUTES, 0);
                DeviceServiceProvider deviceProvider = session.getDeviceProvider();
                return deviceProvider == null ? BandServiceMessage.Response.DEVICE_NOT_BONDED_ERROR : deviceProvider.syncDeviceTime(session, allowedTimeDifferenceInMinutes);
            }
        });
        mHandlers.put(BandDeviceConstants.Command.CargoCancelSync, new CommandHandlerWithPermissionCheck() { // from class: com.microsoft.band.service.ServiceCommandHandler.9
            @Override // com.microsoft.band.service.CommandHandlerWithPermissionCheck
            public BandServiceMessage.Response executeChecked(CargoClientSession session, ServiceCommand command, Bundle responseBundle) throws CargoServiceException {
                session.cancelSync();
                return BandServiceMessage.Response.SUCCESS;
            }
        });
        mHandlers.put(BandDeviceConstants.Command.CargoGetFirmwareUpdateInfo, new CommandHandlerWithPermissionCheck() { // from class: com.microsoft.band.service.ServiceCommandHandler.10
            @Override // com.microsoft.band.service.CommandHandlerWithPermissionCheck
            public BandServiceMessage.Response executeChecked(CargoClientSession session, ServiceCommand command, Bundle responseBundle) throws CargoServiceException {
                String deviceFamily = command.getBundle().getString(CargoConstants.EXTRA_FIRMWARE_DEVICE_FAMILY);
                String firmwareVersionApp = command.getBundle().getString(InternalBandConstants.EXTRA_FIRMWARE_VERSION_APP);
                String firmwareVersionBL = command.getBundle().getString(CargoConstants.EXTRA_FIRMWARE_VERSION_BL);
                String firmwareVersionUP = command.getBundle().getString(CargoConstants.EXTRA_FIRMWARE_VERSION_UP);
                String queryParam = command.getBundle().getString(CargoConstants.EXTRA_FIRMWARE_QUERY_PARAM);
                boolean fwOnDeviceValidation = command.getBundle().getBoolean(CargoConstants.EXTRA_FIRMWARE_FW_VALIDATION);
                CargoFirmwareUpdateInfo firmwareUpdateInfo = session.getCloudProvider().getCargoFirmwareUpdateInfo(deviceFamily, firmwareVersionApp, firmwareVersionBL, firmwareVersionUP, fwOnDeviceValidation, queryParam);
                responseBundle.putParcelable(CargoConstants.EXTRA_CLOUD_DATA, firmwareUpdateInfo);
                return BandServiceMessage.Response.SUCCESS;
            }
        });
        mHandlers.put(BandDeviceConstants.Command.CargoDownloadFirmwareUpdate, new CommandHandlerWithPermissionCheck() { // from class: com.microsoft.band.service.ServiceCommandHandler.11
            @Override // com.microsoft.band.service.CommandHandlerWithPermissionCheck
            public BandServiceMessage.Response executeChecked(CargoClientSession session, ServiceCommand command, Bundle responseBundle) throws CargoServiceException {
                CargoFirmwareUpdateInfo firmwareUpdateInfo = (CargoFirmwareUpdateInfo) command.getBundle().getParcelable(CargoConstants.EXTRA_CLOUD_DATA);
                return session.downloadFirmwareUpdate(firmwareUpdateInfo);
            }
        });
        mHandlers.put(BandDeviceConstants.Command.CargoUpgradeFirmare, new CommandHandlerWithPermissionCheck() { // from class: com.microsoft.band.service.ServiceCommandHandler.12
            @Override // com.microsoft.band.service.CommandHandlerWithPermissionCheck
            public BandServiceMessage.Response executeChecked(CargoClientSession session, ServiceCommand command, Bundle responseBundle) throws CargoServiceException {
                CargoFirmwareUpdateInfo firmwareUpdateInfo = (CargoFirmwareUpdateInfo) command.getBundle().getParcelable(CargoConstants.EXTRA_CLOUD_DATA);
                return session.upgradeFirmwareUpdate(firmwareUpdateInfo);
            }
        });
        mHandlers.put(BandDeviceConstants.Command.CargoGetEphemerisUpdateInfo, new CommandHandlerWithPermissionCheck() { // from class: com.microsoft.band.service.ServiceCommandHandler.13
            @Override // com.microsoft.band.service.CommandHandlerWithPermissionCheck
            public BandServiceMessage.Response executeChecked(CargoClientSession session, ServiceCommand command, Bundle responseBundle) throws CargoServiceException {
                EphemerisUpdateInfo ephemerisUpdateInfo = session.getCloudProvider().getEphemerisUpdateInfo();
                responseBundle.putParcelable(CargoConstants.EXTRA_CLOUD_DATA, ephemerisUpdateInfo);
                return BandServiceMessage.Response.SUCCESS;
            }
        });
        mHandlers.put(BandDeviceConstants.Command.CargoDownloadEphemerisUpdate, new CommandHandlerWithPermissionCheck() { // from class: com.microsoft.band.service.ServiceCommandHandler.14
            @Override // com.microsoft.band.service.CommandHandlerWithPermissionCheck
            public BandServiceMessage.Response executeChecked(CargoClientSession session, ServiceCommand command, Bundle responseBundle) throws CargoServiceException {
                EphemerisUpdateInfo ephemerisUpdateInfo = (EphemerisUpdateInfo) command.getBundle().getParcelable(CargoConstants.EXTRA_CLOUD_DATA);
                return session.downloadEphemerisUpdate(ephemerisUpdateInfo);
            }
        });
        mHandlers.put(BandDeviceConstants.Command.CargoUpgradeEphemeris, new CommandHandlerWithPermissionCheck() { // from class: com.microsoft.band.service.ServiceCommandHandler.15
            @Override // com.microsoft.band.service.CommandHandlerWithPermissionCheck
            public BandServiceMessage.Response executeChecked(CargoClientSession session, ServiceCommand command, Bundle responseBundle) throws CargoServiceException {
                EphemerisUpdateInfo ephemerisUpdateInfo = (EphemerisUpdateInfo) command.getBundle().getParcelable(CargoConstants.EXTRA_CLOUD_DATA);
                return session.upgradeEphemerisUpdate(ephemerisUpdateInfo);
            }
        });
        mHandlers.put(BandDeviceConstants.Command.CargoGetTimeZoneSettingsUpdateInfo, new CommandHandlerWithPermissionCheck() { // from class: com.microsoft.band.service.ServiceCommandHandler.16
            @Override // com.microsoft.band.service.CommandHandlerWithPermissionCheck
            public BandServiceMessage.Response executeChecked(CargoClientSession session, ServiceCommand command, Bundle responseBundle) throws CargoServiceException {
                DeviceProfileInfo deviceProfileInfo = session.getDeviceProvider().getDeviceProfileInfo();
                TimeZoneSettingsUpdateInfo timezoneSettingsUpdateInfo = session.getCloudProvider().getTimeZoneUpdateInfo(deviceProfileInfo.getLocaleName());
                responseBundle.putParcelable(CargoConstants.EXTRA_CLOUD_DATA, timezoneSettingsUpdateInfo);
                return BandServiceMessage.Response.SUCCESS;
            }
        });
        mHandlers.put(BandDeviceConstants.Command.CargoDownloadTimeZoneSettingsUpdate, new CommandHandlerWithPermissionCheck() { // from class: com.microsoft.band.service.ServiceCommandHandler.17
            @Override // com.microsoft.band.service.CommandHandlerWithPermissionCheck
            public BandServiceMessage.Response executeChecked(CargoClientSession session, ServiceCommand command, Bundle responseBundle) throws CargoServiceException {
                TimeZoneSettingsUpdateInfo timezoneSettingsUpdateInfo = (TimeZoneSettingsUpdateInfo) command.getBundle().getParcelable(CargoConstants.EXTRA_CLOUD_DATA);
                return session.downloadTimeZoneSettingsUpdate(timezoneSettingsUpdateInfo);
            }
        });
        mHandlers.put(BandDeviceConstants.Command.CargoUpgradeTimeZoneSettings, new CommandHandlerWithPermissionCheck() { // from class: com.microsoft.band.service.ServiceCommandHandler.18
            @Override // com.microsoft.band.service.CommandHandlerWithPermissionCheck
            public BandServiceMessage.Response executeChecked(CargoClientSession session, ServiceCommand command, Bundle responseBundle) throws CargoServiceException {
                TimeZoneSettingsUpdateInfo timezoneSettingsUpdateInfo = (TimeZoneSettingsUpdateInfo) command.getBundle().getParcelable(CargoConstants.EXTRA_CLOUD_DATA);
                return session.upgradeTimeZoneSettingsUpdate(timezoneSettingsUpdateInfo);
            }
        });
        mHandlers.put(BandDeviceConstants.Command.CargoUpdateCloudDataResourceStatus, new CommandHandlerWithPermissionCheck() { // from class: com.microsoft.band.service.ServiceCommandHandler.19
            @Override // com.microsoft.band.service.CommandHandlerWithPermissionCheck
            public BandServiceMessage.Response executeChecked(CargoClientSession session, ServiceCommand command, Bundle responseBundle) throws CargoServiceException {
                ArrayList<CloudDataResource> cdrList = command.getBundle().getParcelableArrayList(CargoConstants.EXTRA_CLOUD_DATA);
                ArrayList<String> completedUploadIds = new ArrayList<>(cdrList.size());
                BandServiceMessage.Response response = session.getCloudProvider().updateCloudDataResource(cdrList, completedUploadIds);
                responseBundle.putStringArrayList(CargoConstants.EXTRA_CLOUD_DATA, completedUploadIds);
                return response;
            }
        });
        mHandlers.put(BandDeviceConstants.Command.CargoTurnTelemetryOnOff, new CommandHandlerWithPermissionCheck() { // from class: com.microsoft.band.service.ServiceCommandHandler.20
            @Override // com.microsoft.band.service.CommandHandlerWithPermissionCheck
            public BandServiceMessage.Response executeChecked(CargoClientSession session, ServiceCommand command, Bundle responseBundle) throws CargoServiceException {
                boolean onOff = command.getBundle().getBoolean(InternalBandConstants.EXTRA_COMMAND_DATA);
                return session.getDeviceProvider().turnTelemetryOnOff(onOff);
            }
        });
        mHandlers.put(BandDeviceConstants.Command.CargoTurnPerformanceOnOff, new CommandHandlerWithPermissionCheck() { // from class: com.microsoft.band.service.ServiceCommandHandler.21
            @Override // com.microsoft.band.service.CommandHandlerWithPermissionCheck
            public BandServiceMessage.Response executeChecked(CargoClientSession session, ServiceCommand command, Bundle responseBundle) throws CargoServiceException {
                boolean onOff = command.getBundle().getBoolean(InternalBandConstants.EXTRA_COMMAND_DATA);
                return session.getDeviceProvider().turnPerformanceLoggingOnOff(onOff);
            }
        });
        mHandlers.put(BandDeviceConstants.Command.CargoTurnDiagnosticsOnOff, new CommandHandlerWithPermissionCheck() { // from class: com.microsoft.band.service.ServiceCommandHandler.22
            @Override // com.microsoft.band.service.CommandHandlerWithPermissionCheck
            public BandServiceMessage.Response executeChecked(CargoClientSession session, ServiceCommand command, Bundle responseBundle) throws CargoServiceException {
                boolean onOff = command.getBundle().getBoolean(InternalBandConstants.EXTRA_COMMAND_DATA);
                return session.getDeviceProvider().turnDiagnosticsLoggingOnOff(onOff);
            }
        });
        mHandlers.put(BandDeviceConstants.Command.CargoUploadLogToCloud, new CommandHandlerWithPermissionCheck() { // from class: com.microsoft.band.service.ServiceCommandHandler.23
            @Override // com.microsoft.band.service.CommandHandlerWithPermissionCheck
            public BandServiceMessage.Response executeChecked(CargoClientSession session, ServiceCommand command, Bundle responseBundle) throws CargoServiceException {
                byte[] data = command.getBundle().getByteArray(CargoConstants.KEY_LOG_BYTES);
                CloudDataResource.LogFileTypes type = (CloudDataResource.LogFileTypes) command.getBundle().getSerializable(CargoConstants.KEY_LOG_TYPE);
                return session.uploadLogToToCloud(data, type);
            }
        });
        mHandlers.put(BandDeviceConstants.Command.CargoBenchmarkSync, new CommandHandlerWithPermissionCheck() { // from class: com.microsoft.band.service.ServiceCommandHandler.24
            @Override // com.microsoft.band.service.CommandHandlerWithPermissionCheck
            public BandServiceMessage.Response executeChecked(CargoClientSession session, ServiceCommand command, Bundle responseBundle) throws CargoServiceException {
                int maxTransferChunks = command.getBundle().getInt(CargoConstants.EXTRA_COMMAND);
                return session.benchmarkSync(maxTransferChunks);
            }
        });
        mHandlers.put(BandDeviceConstants.Command.BandGetFirmwareVersion, new ICommandHandler() { // from class: com.microsoft.band.service.ServiceCommandHandler.25
            @Override // com.microsoft.band.service.ServiceCommandHandler.ICommandHandler
            public BandServiceMessage.Response execute(CargoClientSession session, ServiceCommand command, Bundle responseBundle) throws CargoServiceException {
                return session.getDeviceProvider().getFirmwareAppVersion(responseBundle, VersionType.FIRMWARE, session.getClientVersion());
            }
        });
        mHandlers.put(BandDeviceConstants.Command.BandGetHardwareVersion, new ICommandHandler() { // from class: com.microsoft.band.service.ServiceCommandHandler.26
            @Override // com.microsoft.band.service.ServiceCommandHandler.ICommandHandler
            public BandServiceMessage.Response execute(CargoClientSession session, ServiceCommand command, Bundle responseBundle) throws CargoServiceException {
                return session.getDeviceProvider().getFirmwareAppVersion(responseBundle, VersionType.HARDWARE, session.getClientVersion());
            }
        });
        mHandlers.put(BandDeviceConstants.Command.BandNotificationVibrate, new ICommandHandler() { // from class: com.microsoft.band.service.ServiceCommandHandler.27
            @Override // com.microsoft.band.service.ServiceCommandHandler.ICommandHandler
            public BandServiceMessage.Response execute(CargoClientSession session, ServiceCommand command, Bundle responseBundle) throws CargoServiceException {
                VibrationType type = (VibrationType) command.getBundle().getSerializable(InternalBandConstants.EXTRA_COMMAND_DATA);
                CommandWrite cmdVibrate = new CommandWrite(BandDeviceConstants.Command.CargoHapticPlayVibrationStream, null, new byte[]{Haptic.vibrationToHaptic(type).mValue});
                return session.getDeviceProvider().processCommand(cmdVibrate);
            }
        });
        mHandlers.put(BandDeviceConstants.Command.BandNotificationShowDialog, new ICommandHandler() { // from class: com.microsoft.band.service.ServiceCommandHandler.28
            @Override // com.microsoft.band.service.ServiceCommandHandler.ICommandHandler
            public BandServiceMessage.Response execute(CargoClientSession session, ServiceCommand command, Bundle responseBundle) throws CargoServiceException {
                Bundle cmdBundle = command.getBundle();
                String tileId = cmdBundle.getString(InternalBandConstants.EXTRA_COMMAND_DATA);
                String title = cmdBundle.getString(InternalBandConstants.EXTRA_MESSAGE_TITLE);
                String body = cmdBundle.getString(InternalBandConstants.EXTRA_MESSAGE_BODY);
                NotificationGenericDialog dialog = new NotificationGenericDialog(title, body);
                return session.getDeviceProvider().showDialog(tileId, dialog, session.getCallingAppId());
            }
        });
        mHandlers.put(BandDeviceConstants.Command.BandNotificationSendMessage, new ICommandHandler() { // from class: com.microsoft.band.service.ServiceCommandHandler.29
            @Override // com.microsoft.band.service.ServiceCommandHandler.ICommandHandler
            public BandServiceMessage.Response execute(CargoClientSession session, ServiceCommand command, Bundle responseBundle) throws CargoServiceException {
                Bundle cmdBundle = command.getBundle();
                String tileId = cmdBundle.getString(InternalBandConstants.EXTRA_COMMAND_DATA);
                String title = cmdBundle.getString(InternalBandConstants.EXTRA_MESSAGE_TITLE);
                String body = cmdBundle.getString(InternalBandConstants.EXTRA_MESSAGE_BODY);
                long time = cmdBundle.getLong(InternalBandConstants.EXTRA_MESSAGE_TIMESTAMP);
                MessageFlags flags = (MessageFlags) cmdBundle.getSerializable(InternalBandConstants.EXTRA_MESSAGE_FLAG);
                StrappMessage message = new StrappMessage(title, body, new Date(time), flags);
                return session.getDeviceProvider().sendMessage(tileId, message, session.getCallingAppId());
            }
        });
        mHandlers.put(BandDeviceConstants.Command.CargoPersonalizationGetMeTileId, new ICommandHandler() { // from class: com.microsoft.band.service.ServiceCommandHandler.30
            @Override // com.microsoft.band.service.ServiceCommandHandler.ICommandHandler
            public BandServiceMessage.Response execute(CargoClientSession session, ServiceCommand command, Bundle responseBundle) throws CargoServiceException {
                return session.getDeviceProvider().getMeTileImageId(responseBundle);
            }
        });
        mHandlers.put(BandDeviceConstants.Command.BandPersonalizationGetMeTile, new ICommandHandler() { // from class: com.microsoft.band.service.ServiceCommandHandler.31
            @Override // com.microsoft.band.service.ServiceCommandHandler.ICommandHandler
            public BandServiceMessage.Response execute(CargoClientSession session, ServiceCommand command, Bundle responseBundle) throws CargoServiceException {
                return session.getDeviceProvider().getMeTileImage(responseBundle, session.getClientVersion());
            }
        });
        mHandlers.put(BandDeviceConstants.Command.BandPersonalizationSetMeTile, new ICommandHandler() { // from class: com.microsoft.band.service.ServiceCommandHandler.32
            @Override // com.microsoft.band.service.ServiceCommandHandler.ICommandHandler
            public BandServiceMessage.Response execute(CargoClientSession session, ServiceCommand command, Bundle responseBundle) throws CargoServiceException {
                byte[] data = command.getBundle().getByteArray(InternalBandConstants.EXTRA_SERVICE_COMMAND_PAYLOAD);
                long id = command.getBundle().getLong(InternalBandConstants.EXTRA_COMMAND_DATA);
                if (id == 0) {
                    id = 4294967295L;
                }
                return session.getDeviceProvider().setMeTileImage(data, id);
            }
        });
        mHandlers.put(BandDeviceConstants.Command.BandPersonalizationClearMeTile, new ICommandHandler() { // from class: com.microsoft.band.service.ServiceCommandHandler.33
            @Override // com.microsoft.band.service.ServiceCommandHandler.ICommandHandler
            public BandServiceMessage.Response execute(CargoClientSession session, ServiceCommand command, Bundle responseBundle) throws CargoServiceException {
                return session.getDeviceProvider().clearMeTileImage();
            }
        });
        mHandlers.put(BandDeviceConstants.Command.BandPersonalizationGetTheme, new ICommandHandler() { // from class: com.microsoft.band.service.ServiceCommandHandler.34
            @Override // com.microsoft.band.service.ServiceCommandHandler.ICommandHandler
            public BandServiceMessage.Response execute(CargoClientSession session, ServiceCommand command, Bundle responseBundle) throws CargoServiceException {
                return session.getDeviceProvider().getTheme(responseBundle);
            }
        });
        mHandlers.put(BandDeviceConstants.Command.BandPersonalizationSetTheme, new ICommandHandler() { // from class: com.microsoft.band.service.ServiceCommandHandler.35
            @Override // com.microsoft.band.service.ServiceCommandHandler.ICommandHandler
            public BandServiceMessage.Response execute(CargoClientSession session, ServiceCommand command, Bundle responseBundle) throws CargoServiceException {
                BandTheme theme = (BandTheme) command.getBundle().getParcelable(InternalBandConstants.EXTRA_SERVICE_COMMAND_PAYLOAD);
                return session.getDeviceProvider().setTheme(new StrappColorPalette(theme));
            }
        });
        mHandlers.put(BandDeviceConstants.Command.BandPersonalizationResetTheme, new ICommandHandler() { // from class: com.microsoft.band.service.ServiceCommandHandler.36
            @Override // com.microsoft.band.service.ServiceCommandHandler.ICommandHandler
            public BandServiceMessage.Response execute(CargoClientSession session, ServiceCommand command, Bundle responseBundle) throws CargoServiceException {
                return session.getDeviceProvider().resetTheme();
            }
        });
        mHandlers.put(BandDeviceConstants.Command.CargoPersonalizationSetCustomTileTheme, new CommandHandlerWithPermissionCheck() { // from class: com.microsoft.band.service.ServiceCommandHandler.37
            @Override // com.microsoft.band.service.CommandHandlerWithPermissionCheck
            public BandServiceMessage.Response executeChecked(CargoClientSession session, ServiceCommand command, Bundle responseBundle) throws CargoServiceException {
                UUID tileId = (UUID) command.getBundle().getSerializable(InternalBandConstants.EXTRA_COMMAND_DATA);
                BandTheme theme = (BandTheme) command.getBundle().getParcelable(InternalBandConstants.EXTRA_SERVICE_COMMAND_PAYLOAD);
                return session.getDeviceProvider().setTileTheme(tileId, new StrappColorPalette(theme));
            }
        });
        mHandlers.put(BandDeviceConstants.Command.CargoPersonalizationSetCustomTileThemes, new CommandHandlerWithPermissionCheck() { // from class: com.microsoft.band.service.ServiceCommandHandler.38
            @Override // com.microsoft.band.service.CommandHandlerWithPermissionCheck
            public BandServiceMessage.Response executeChecked(CargoClientSession session, ServiceCommand command, Bundle responseBundle) throws CargoServiceException {
                Map<UUID, StrappColorPalette> customThemeMap = (Map) command.getBundle().getSerializable(InternalBandConstants.EXTRA_PERSONALIZE_THEME_MAP);
                return session.getDeviceProvider().setTileThemes(customThemeMap);
            }
        });
        mHandlers.put(BandDeviceConstants.Command.SubscribeToSensor, new ICommandHandler() { // from class: com.microsoft.band.service.ServiceCommandHandler.39
            @Override // com.microsoft.band.service.ServiceCommandHandler.ICommandHandler
            public BandServiceMessage.Response execute(CargoClientSession session, ServiceCommand command, Bundle responseBundle) throws CargoServiceException {
                int sensor = command.getBundle().getInt(InternalBandConstants.EXTRA_COMMAND_DATA);
                return session.subscribeToSensor(sensor);
            }
        });
        mHandlers.put(BandDeviceConstants.Command.UnsubscribeToSensor, new ICommandHandler() { // from class: com.microsoft.band.service.ServiceCommandHandler.40
            @Override // com.microsoft.band.service.ServiceCommandHandler.ICommandHandler
            public BandServiceMessage.Response execute(CargoClientSession session, ServiceCommand command, Bundle responseBundle) throws CargoServiceException {
                int sensor = command.getBundle().getInt(InternalBandConstants.EXTRA_COMMAND_DATA);
                return session.unsubscribeToSensor(sensor);
            }
        });
        mHandlers.put(BandDeviceConstants.Command.UnsubscribeToAllSensors, new ICommandHandler() { // from class: com.microsoft.band.service.ServiceCommandHandler.41
            @Override // com.microsoft.band.service.ServiceCommandHandler.ICommandHandler
            public BandServiceMessage.Response execute(CargoClientSession session, ServiceCommand command, Bundle responseBundle) throws CargoServiceException {
                return session.unsubscribeToAllSensors();
            }
        });
        mHandlers.put(BandDeviceConstants.Command.BandTileGetTiles, new ICommandHandler() { // from class: com.microsoft.band.service.ServiceCommandHandler.42
            @Override // com.microsoft.band.service.ServiceCommandHandler.ICommandHandler
            public BandServiceMessage.Response execute(CargoClientSession session, ServiceCommand command, Bundle responseBundle) throws CargoServiceException {
                return session.getDeviceProvider().getTiles(responseBundle, session.getCallingAppId());
            }
        });
        mHandlers.put(BandDeviceConstants.Command.BandTileAddTile, new ICommandHandler() { // from class: com.microsoft.band.service.ServiceCommandHandler.43
            @Override // com.microsoft.band.service.ServiceCommandHandler.ICommandHandler
            public BandServiceMessage.Response execute(CargoClientSession session, ServiceCommand command, Bundle responseBundle) throws CargoServiceException {
                Bundle bundle = command.getBundle();
                try {
                    BandTile tile = (BandTile) bundle.getParcelable(InternalBandConstants.EXTRA_COMMAND_DATA);
                    CargoStrapp strapp = TileUtils.tileToStrapp(tile, session.getCallingAppId());
                    BandTheme theme = tile.getTheme();
                    return session.getDeviceProvider().addTile(strapp, theme, responseBundle);
                } catch (CargoException e) {
                    return e.getResponse();
                }
            }
        });
        mHandlers.put(BandDeviceConstants.Command.BandTileRemoveTile, new ICommandHandler() { // from class: com.microsoft.band.service.ServiceCommandHandler.44
            @Override // com.microsoft.band.service.ServiceCommandHandler.ICommandHandler
            public BandServiceMessage.Response execute(CargoClientSession session, ServiceCommand command, Bundle responseBundle) throws CargoServiceException {
                UUID tileId = (UUID) command.getBundle().getSerializable(InternalBandConstants.EXTRA_COMMAND_DATA);
                return session.getDeviceProvider().removeTile(tileId, responseBundle, session.getCallingAppId());
            }
        });
        mHandlers.put(BandDeviceConstants.Command.BandTileRemovePages, new ICommandHandler() { // from class: com.microsoft.band.service.ServiceCommandHandler.45
            @Override // com.microsoft.band.service.ServiceCommandHandler.ICommandHandler
            public BandServiceMessage.Response execute(CargoClientSession session, ServiceCommand command, Bundle responseBundle) throws CargoServiceException {
                UUID tileId = (UUID) command.getBundle().getSerializable(InternalBandConstants.EXTRA_COMMAND_DATA);
                return session.getDeviceProvider().removePages(tileId, responseBundle, session.getCallingAppId());
            }
        });
        mHandlers.put(BandDeviceConstants.Command.BandTileSetPages, new ICommandHandler() { // from class: com.microsoft.band.service.ServiceCommandHandler.46
            @Override // com.microsoft.band.service.ServiceCommandHandler.ICommandHandler
            public BandServiceMessage.Response execute(CargoClientSession session, ServiceCommand command, Bundle responseBundle) throws CargoServiceException {
                UUID tileId = (UUID) command.getBundle().getSerializable(InternalBandConstants.EXTRA_COMMAND_DATA);
                Parcelable[] pageData = command.getBundle().getParcelableArray(InternalBandConstants.PAGE_DATA);
                return session.getDeviceProvider().updatePages(tileId, pageData, responseBundle, session.getCallingAppId());
            }
        });
        mHandlers.put(BandDeviceConstants.Command.BandTileGetRemainingCapacity, new ICommandHandler() { // from class: com.microsoft.band.service.ServiceCommandHandler.47
            @Override // com.microsoft.band.service.ServiceCommandHandler.ICommandHandler
            public BandServiceMessage.Response execute(CargoClientSession session, ServiceCommand command, Bundle responseBundle) throws CargoServiceException {
                return session.getDeviceProvider().getRemainingTileCapacity(responseBundle);
            }
        });
        mHandlers.put(BandDeviceConstants.Command.CargoTileGetMaxStrappCount, new CommandHandlerWithPermissionCheck() { // from class: com.microsoft.band.service.ServiceCommandHandler.48
            @Override // com.microsoft.band.service.CommandHandlerWithPermissionCheck
            public BandServiceMessage.Response executeChecked(CargoClientSession session, ServiceCommand command, Bundle responseBundle) throws CargoServiceException {
                return session.getDeviceProvider().getMaxStrappCount(responseBundle);
            }
        });
        mHandlers.put(BandDeviceConstants.Command.CargoTileGetDefaultStrip, new CommandHandlerWithPermissionCheck() { // from class: com.microsoft.band.service.ServiceCommandHandler.49
            @Override // com.microsoft.band.service.CommandHandlerWithPermissionCheck
            public BandServiceMessage.Response executeChecked(CargoClientSession session, ServiceCommand command, Bundle responseBundle) throws CargoServiceException {
                return command.getBundle().getBoolean(InternalBandConstants.EXTRA_COMMAND_DATA) ? session.getDeviceProvider().getDefaultInstalledAppListWithImage(responseBundle) : session.getDeviceProvider().getDefaultInstalledAppListWithoutImage(responseBundle);
            }
        });
        mHandlers.put(BandDeviceConstants.Command.CargoTileGetStartStrip, new CommandHandlerWithPermissionCheck() { // from class: com.microsoft.band.service.ServiceCommandHandler.50
            @Override // com.microsoft.band.service.CommandHandlerWithPermissionCheck
            public BandServiceMessage.Response executeChecked(CargoClientSession session, ServiceCommand command, Bundle responseBundle) throws CargoServiceException {
                return command.getBundle().getBoolean(InternalBandConstants.EXTRA_COMMAND_DATA) ? session.getDeviceProvider().getInstalledAppListWithImage(responseBundle) : session.getDeviceProvider().getInstalledAppListWithoutImage(responseBundle);
            }
        });
        mHandlers.put(BandDeviceConstants.Command.CargoTileSetStartStrip, new CommandHandlerWithPermissionCheck() { // from class: com.microsoft.band.service.ServiceCommandHandler.51
            @Override // com.microsoft.band.service.CommandHandlerWithPermissionCheck
            public BandServiceMessage.Response executeChecked(CargoClientSession session, ServiceCommand command, Bundle responseBundle) throws CargoServiceException {
                StartStrip strip = (StartStrip) command.getBundle().getParcelable(InternalBandConstants.EXTRA_COMMAND_DATA);
                return session.getDeviceProvider().setStartStrip(strip, responseBundle);
            }
        });
        mHandlers.put(BandDeviceConstants.Command.CargoTileGetStrapp, new CommandHandlerWithPermissionCheck() { // from class: com.microsoft.band.service.ServiceCommandHandler.52
            @Override // com.microsoft.band.service.CommandHandlerWithPermissionCheck
            public BandServiceMessage.Response executeChecked(CargoClientSession session, ServiceCommand command, Bundle responseBundle) throws CargoServiceException {
                UUID tileId = (UUID) command.getBundle().getSerializable(InternalBandConstants.EXTRA_UUID);
                return command.getBundle().getBoolean(InternalBandConstants.EXTRA_COMMAND_DATA) ? session.getDeviceProvider().getStrappWithImage(tileId, responseBundle) : session.getDeviceProvider().getStrappWithoutImage(tileId, responseBundle);
            }
        });
        mHandlers.put(BandDeviceConstants.Command.CargoTileUpdateStrapp, new CommandHandlerWithPermissionCheck() { // from class: com.microsoft.band.service.ServiceCommandHandler.53
            @Override // com.microsoft.band.service.CommandHandlerWithPermissionCheck
            public BandServiceMessage.Response executeChecked(CargoClientSession session, ServiceCommand command, Bundle responseBundle) throws CargoServiceException {
                CargoStrapp strapp = (CargoStrapp) command.getBundle().getParcelable(InternalBandConstants.EXTRA_COMMAND_DATA);
                return session.getDeviceProvider().updateStrapp(strapp, responseBundle);
            }
        });
        mHandlers.put(BandDeviceConstants.Command.CargoTileGetSettingsMask, new CommandHandlerWithPermissionCheck() { // from class: com.microsoft.band.service.ServiceCommandHandler.54
            @Override // com.microsoft.band.service.CommandHandlerWithPermissionCheck
            public BandServiceMessage.Response executeChecked(CargoClientSession session, ServiceCommand command, Bundle responseBundle) throws CargoServiceException {
                UUID tileId = (UUID) command.getBundle().getSerializable(InternalBandConstants.EXTRA_UUID);
                return session.getDeviceProvider().getTileSettingsMask(tileId, responseBundle);
            }
        });
        mHandlers.put(BandDeviceConstants.Command.CargoTileSetSettingsMask, new CommandHandlerWithPermissionCheck() { // from class: com.microsoft.band.service.ServiceCommandHandler.55
            @Override // com.microsoft.band.service.CommandHandlerWithPermissionCheck
            public BandServiceMessage.Response executeChecked(CargoClientSession session, ServiceCommand command, Bundle responseBundle) throws CargoServiceException {
                UUID tileId = (UUID) command.getBundle().getSerializable(InternalBandConstants.EXTRA_UUID);
                int settingMask = command.getBundle().getInt(InternalBandConstants.EXTRA_COMMAND_DATA);
                return session.getDeviceProvider().setTileSettingsMask(tileId, settingMask);
            }
        });
        mHandlers.put(BandDeviceConstants.Command.CargoTileSetTileImageIndex, new CommandHandlerWithPermissionCheck() { // from class: com.microsoft.band.service.ServiceCommandHandler.56
            @Override // com.microsoft.band.service.CommandHandlerWithPermissionCheck
            public BandServiceMessage.Response executeChecked(CargoClientSession session, ServiceCommand command, Bundle responseBundle) throws CargoServiceException {
                UUID tileId = (UUID) command.getBundle().getSerializable(InternalBandConstants.EXTRA_UUID);
                int tileImageIndex = command.getBundle().getInt(InternalBandConstants.EXTRA_COMMAND_DATA);
                return session.getDeviceProvider().setTileImageIndex(tileId, tileImageIndex);
            }
        });
        mHandlers.put(BandDeviceConstants.Command.CargoTileSetBadgeImageIndex, new CommandHandlerWithPermissionCheck() { // from class: com.microsoft.band.service.ServiceCommandHandler.57
            @Override // com.microsoft.band.service.CommandHandlerWithPermissionCheck
            public BandServiceMessage.Response executeChecked(CargoClientSession session, ServiceCommand command, Bundle responseBundle) throws CargoServiceException {
                UUID tileId = (UUID) command.getBundle().getSerializable(InternalBandConstants.EXTRA_UUID);
                int badgeImageIndex = command.getBundle().getInt(InternalBandConstants.EXTRA_COMMAND_DATA);
                return session.getDeviceProvider().setTileBadgeIndex(tileId, badgeImageIndex);
            }
        });
        mHandlers.put(BandDeviceConstants.Command.CargoTileSetNotificationImageIndex, new CommandHandlerWithPermissionCheck() { // from class: com.microsoft.band.service.ServiceCommandHandler.58
            @Override // com.microsoft.band.service.CommandHandlerWithPermissionCheck
            public BandServiceMessage.Response executeChecked(CargoClientSession session, ServiceCommand command, Bundle responseBundle) throws CargoServiceException {
                UUID tileId = (UUID) command.getBundle().getSerializable(InternalBandConstants.EXTRA_UUID);
                int notificationImageIndex = command.getBundle().getInt(InternalBandConstants.EXTRA_COMMAND_DATA);
                return session.getDeviceProvider().setTileNotificationIndex(tileId, notificationImageIndex);
            }
        });
        mHandlers.put(BandDeviceConstants.Command.CargoPersonalizeDevice, new CommandHandlerWithPermissionCheck() { // from class: com.microsoft.band.service.ServiceCommandHandler.59
            @Override // com.microsoft.band.service.CommandHandlerWithPermissionCheck
            public BandServiceMessage.Response executeChecked(CargoClientSession session, ServiceCommand command, Bundle responseBundle) throws CargoServiceException {
                StartStrip strip = (StartStrip) command.getBundle().getParcelable(InternalBandConstants.EXTRA_PERSONALIZE_STRAPPS);
                BandTheme theme = (BandTheme) command.getBundle().getParcelable(InternalBandConstants.EXTRA_PERSONALIZE_THEME);
                byte[] image = command.getBundle().getByteArray(InternalBandConstants.EXTRA_PERSONALIZE_METILE);
                long imageId = command.getBundle().getLong(InternalBandConstants.EXTRA_PERSONALIZE_METILE_ID);
                if (imageId == 0) {
                    imageId = 4294967295L;
                }
                Map<UUID, StrappColorPalette> customThemeMap = (Map) command.getBundle().getSerializable(InternalBandConstants.EXTRA_PERSONALIZE_THEME_MAP);
                StrappColorPalette palate = null;
                if (theme != null) {
                    palate = new StrappColorPalette(theme);
                }
                return session.getDeviceProvider().personalizeDevice(strip, image, palate, imageId, customThemeMap);
            }
        });
        mHandlers.put(BandDeviceConstants.Command.CargoTileClearPages, new CommandHandlerWithPermissionCheck() { // from class: com.microsoft.band.service.ServiceCommandHandler.60
            @Override // com.microsoft.band.service.CommandHandlerWithPermissionCheck
            public BandServiceMessage.Response executeChecked(CargoClientSession session, ServiceCommand command, Bundle responseBundle) throws CargoServiceException {
                UUID tileId = (UUID) command.getBundle().getSerializable(InternalBandConstants.EXTRA_UUID);
                return session.getDeviceProvider().clearTile(tileId);
            }
        });
        mHandlers.put(BandDeviceConstants.Command.CargoTileSetPage, new CommandHandlerWithPermissionCheck() { // from class: com.microsoft.band.service.ServiceCommandHandler.61
            @Override // com.microsoft.band.service.CommandHandlerWithPermissionCheck
            public BandServiceMessage.Response executeChecked(CargoClientSession session, ServiceCommand command, Bundle responseBundle) throws CargoServiceException {
                UUID tileId = (UUID) command.getBundle().getSerializable(InternalBandConstants.EXTRA_UUID);
                byte[] pageData = command.getBundle().getByteArray(InternalBandConstants.EXTRA_COMMAND_DATA);
                return session.getDeviceProvider().updatePage(tileId, pageData);
            }
        });
        mHandlers.put(BandDeviceConstants.Command.CargoWebTileAddTile, new CommandHandlerWithPermissionCheck() { // from class: com.microsoft.band.service.ServiceCommandHandler.62
            @Override // com.microsoft.band.service.CommandHandlerWithPermissionCheck
            public BandServiceMessage.Response executeChecked(CargoClientSession session, ServiceCommand command, Bundle responseBundle) throws CargoServiceException {
                Bundle bundle = command.getBundle();
                try {
                    WebTile tile = (WebTile) bundle.getParcelable(InternalBandConstants.EXTRA_COMMAND_DATA);
                    int hardwareVersion = session.getDeviceProvider().getHardwareVersion();
                    CargoStrapp strapp = WebTileManager.webTileToCargoStrapp(tile, session.getService(), DeviceConstants.WEB_TILE_HASH, hardwareVersion);
                    BandTheme theme = tile.getTileTheme();
                    return session.getDeviceProvider().addTile(strapp, theme, responseBundle);
                } catch (CargoException e) {
                    return e.getResponse();
                } catch (Exception e2) {
                    return BandServiceMessage.Response.WEB_TILE_READ_ERROR;
                }
            }
        });
    }

    private ServiceCommandHandler() {
        throw new UnsupportedOperationException();
    }

    public static ICommandHandler getHandler(ServiceCommand command) {
        return mHandlers.get(command.getCommandType());
    }
}

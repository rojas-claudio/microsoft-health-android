package com.microsoft.band.internal;

import android.support.v4.media.TransportMediator;
import com.microsoft.band.device.DeviceConstants;
import com.microsoft.band.device.WorkoutActivity;
import com.microsoft.kapp.utils.Constants;
import java.util.Locale;
/* loaded from: classes.dex */
public final class BandDeviceConstants {
    public static final int BAND_TILE_NAME_MAX_LENGTH = 42;
    public static final int BYTES_IN_CHAR = 2;
    public static final int BYTES_IN_INT = 4;
    public static final byte COMMAND_INDEX_BITS = 7;
    public static final short COMMAND_STATUS_PACKET_SIZE = 6;
    public static final byte COMMAND_TX_BITS = 1;
    public static final int GET_UNIQUE_ID_STRUCT_SIZE = 66;
    public static final byte GUID_BYTE_LENGTH = 16;
    public static final String GUID_CARGO_BLUETOOTH_PROTOCOL = "A502CA97-2BA5-413C-A4E0-13804E47B38F";
    public static final int LOGGER_SUBSCRIPTION_BITSET_BYTE_SIZE = 8;
    public static final int LOGGER_SUBSCRIPTION_SUBSCRIBERS_PAYLOAD_SIZE = 16;
    public static final int MAX_COMMAND_PAYLOAD_SIZE = Integer.MAX_VALUE;
    public static final byte MAX_COMMAND_RELATED_DATA_SIZE = 55;
    public static final int ME_TILE_HEIGHT = 102;
    public static final int ME_TILE_WIDTH = 310;
    public static final short PACKET_TYPE_COMMAND = 12025;
    public static final short PACKET_TYPE_STATUS = -22786;
    private static final int REMOTE_SUBSCRIPTION_SUBSCRIBE_ARG_SIZE = 5;
    private static final int REMOTE_SUBSCRIPTION_UNSUBSCRIBE_ARG_SIZE = 1;
    public static final int RESULT_CODE_CUSTOM_BIT_FlAG = 536870912;
    public static final int RESULT_CODE_SEVERITY_BIT_FLAG = Integer.MIN_VALUE;
    public static final int SENSORLOG_METADATA_STUCTURE_LENGTH = 12;
    public static final int STRAPP_ICON_SIZE = 1024;
    public static final int V2_ME_TILE_HEIGHT = 128;
    public static final int V2_ME_TILE_WIDTH = 310;

    private BandDeviceConstants() {
        throw new UnsupportedOperationException();
    }

    /* loaded from: classes.dex */
    public enum Facility {
        UNKNOWN(0),
        DRIVER_RTC(40),
        DRIVER_CRASHDUMP(58),
        LOGGER(140),
        BATTERY(125),
        LIBRARY_TIME(117),
        LIBRARY_JUTIL(118),
        LIBRARY_CONFIGURATION(Constants.GUIDED_WORKOUT_SECONDS_120S_THRESHOLD),
        LIBRARY_FILE(142),
        LIBRARY_REMOTE_SUBSCRIPTION(143),
        LIBRARY_LOGGER(140),
        LIBRARY_PEG(141),
        LIBRARY_SRAM_FWUPDATE(-104),
        LIBRARY_HAPTIC(154),
        LIBRARY_FITNESS_PLANS(155),
        LIBRARY_KEYBOARD(159),
        LIBRARY_GOLF(161),
        MODULE_OOBE(173),
        MODULE_FIREBALLUI(DeviceConstants.NOTIFICATION_GENERIC_UPDATE_LINE_MAX_LENGTH_IN_CHAR),
        MODULE_PROFILE(197),
        MODULE_LOGGERSUBSCRIPTIONS(198),
        MODULE_SYSTEMSETTINGS(202),
        MODULE_NOTIFICATION(204),
        MODULE_PERSISTED_STATISTICS(206),
        MODULE_PERSISTED_APPLICATION_DATA(208),
        MODULE_INSTRUMENTATION(210),
        MODULE_FIREBALL_APPSMANAGEMENT(211),
        MODULE_INSTALLED_APP_LIST(212),
        MODULE_FIREBALL_PAGEMANAGEMENT(213),
        MODULE_THEME_COLOR(216),
        MODULE_GOAL_TRACKER(217),
        CARGO_SERVICE(255);
        
        private byte mCode;

        Facility(int b) {
            this.mCode = (byte) (b & 255);
        }

        public byte getCode() {
            return this.mCode;
        }

        public static Facility toFacility(int commandId) {
            Facility[] arr$ = values();
            for (Facility item : arr$) {
                if ((item.mCode & 255) == ((65280 & commandId) >> 8)) {
                    return item;
                }
            }
            return UNKNOWN;
        }
    }

    /* loaded from: classes.dex */
    public enum Command {
        CargoGetCloudProfile(Facility.CARGO_SERVICE, 2, true),
        CargoGetUserIdentity(Facility.CARGO_SERVICE, 3, true),
        CargoSaveUserProfile(Facility.CARGO_SERVICE, 5, false),
        CargoConnectDevice(Facility.CARGO_SERVICE, 6, false),
        CargoDisconnectDevice(Facility.CARGO_SERVICE, 7, false),
        CargoSyncDeviceToCloud(Facility.CARGO_SERVICE, 10, false),
        CargoSyncWebTiles(Facility.CARGO_SERVICE, 11, false),
        CargoSyncDeviceTime(Facility.CARGO_SERVICE, 12, false),
        CargoCancelSync(Facility.CARGO_SERVICE, 13, false),
        CargoGetFirmwareUpdateInfo(Facility.CARGO_SERVICE, 14, true),
        CargoDownloadFirmwareUpdate(Facility.CARGO_SERVICE, 15, false),
        CargoGetEphemerisUpdateInfo(Facility.CARGO_SERVICE, 16, true),
        CargoDownloadEphemerisUpdate(Facility.CARGO_SERVICE, 17, false),
        CargoGetTimeZoneSettingsUpdateInfo(Facility.CARGO_SERVICE, 18, true),
        CargoDownloadTimeZoneSettingsUpdate(Facility.CARGO_SERVICE, 19, false),
        CargoUpgradeFirmare(Facility.CARGO_SERVICE, 20, false),
        CargoUpgradeEphemeris(Facility.CARGO_SERVICE, 21, false),
        CargoUpgradeTimeZoneSettings(Facility.CARGO_SERVICE, 22, false),
        CargoUpdateCloudDataResourceStatus(Facility.CARGO_SERVICE, 23, true),
        CargoSendFileToCloud(Facility.CARGO_SERVICE, 26, false),
        CargoSetUserAgentHeader(Facility.CARGO_SERVICE, 32, false),
        CargoTurnTelemetryOnOff(Facility.CARGO_SERVICE, 33, false),
        CargoTurnPerformanceOnOff(Facility.CARGO_SERVICE, 34, false),
        CargoTurnDiagnosticsOnOff(Facility.CARGO_SERVICE, 35, false),
        CargoUploadLogToCloud(Facility.CARGO_SERVICE, 36, false),
        CargoBenchmarkSync(Facility.CARGO_SERVICE, 37, false),
        BandGetFirmwareVersion(Facility.CARGO_SERVICE, 38, true),
        BandGetHardwareVersion(Facility.CARGO_SERVICE, 39, true),
        BandNotificationVibrate(Facility.CARGO_SERVICE, 40, false),
        BandNotificationShowDialog(Facility.CARGO_SERVICE, 41, false),
        BandNotificationSendMessage(Facility.CARGO_SERVICE, 48, false),
        BandPersonalizationGetMeTile(Facility.CARGO_SERVICE, 49, true),
        BandPersonalizationSetMeTile(Facility.CARGO_SERVICE, 50, false),
        BandPersonalizationClearMeTile(Facility.CARGO_SERVICE, 51, false),
        BandPersonalizationGetTheme(Facility.CARGO_SERVICE, 52, true),
        BandPersonalizationSetTheme(Facility.CARGO_SERVICE, 53, false),
        BandPersonalizationResetTheme(Facility.CARGO_SERVICE, 54, false),
        BandTileGetRemainingCapacity(Facility.CARGO_SERVICE, 64, false),
        BandTileGetTiles(Facility.CARGO_SERVICE, 65, false),
        BandTileAddTile(Facility.CARGO_SERVICE, 66, false),
        BandTileRemoveTile(Facility.CARGO_SERVICE, 67, false),
        BandTileRemovePages(Facility.CARGO_SERVICE, 68, false),
        BandTileRemovePage(Facility.CARGO_SERVICE, 69, false),
        BandTileSetPages(Facility.CARGO_SERVICE, 70, false),
        SubscribeToSensor(Facility.CARGO_SERVICE, 80, false),
        UnsubscribeToSensor(Facility.CARGO_SERVICE, 81, false),
        UnsubscribeToAllSensors(Facility.CARGO_SERVICE, 82, false),
        CargoPersonalizationGetMeTileId(Facility.CARGO_SERVICE, 96, true),
        CargoPersonalizationSetCustomTileTheme(Facility.CARGO_SERVICE, 97, false),
        CargoPersonalizationSetCustomTileThemes(Facility.CARGO_SERVICE, 98, false),
        CargoPersonalizeDevice(Facility.CARGO_SERVICE, 99, false),
        CargoTileGetMaxStrappCount(Facility.CARGO_SERVICE, 112, true),
        CargoTileGetDefaultStrip(Facility.CARGO_SERVICE, 113, true),
        CargoTileGetStartStrip(Facility.CARGO_SERVICE, WorkoutActivity.WORKOUT_ACTIVITY_TYPE_DATA_STRUCTURE_SIZE, true),
        CargoTileSetStartStrip(Facility.CARGO_SERVICE, 115, false),
        CargoTileGetStrapp(Facility.CARGO_SERVICE, 116, true),
        CargoTileUpdateStrapp(Facility.CARGO_SERVICE, 117, false),
        CargoTileGetSettingsMask(Facility.CARGO_SERVICE, 118, true),
        CargoTileSetSettingsMask(Facility.CARGO_SERVICE, 119, false),
        CargoTileSetTileImageIndex(Facility.CARGO_SERVICE, Constants.GUIDED_WORKOUT_SECONDS_120S_THRESHOLD, false),
        CargoTileSetBadgeImageIndex(Facility.CARGO_SERVICE, 121, false),
        CargoTileSetNotificationImageIndex(Facility.CARGO_SERVICE, 122, false),
        CargoTileSetPage(Facility.CARGO_SERVICE, 123, false),
        CargoTileClearPages(Facility.CARGO_SERVICE, 124, false),
        CargoWebTileAddTile(Facility.CARGO_SERVICE, 128, false),
        CargoCoreModuleReset(Facility.LIBRARY_JUTIL, 0, false),
        CargoCoreModuleGetVersion(Facility.LIBRARY_JUTIL, 1, true),
        CargoCoreModuleGetUniqueID(Facility.LIBRARY_JUTIL, 2, true, 0, 66),
        CargoCoreModuleWhoAmI(Facility.LIBRARY_JUTIL, 3, true, 0, 1),
        CargoCoreModuleGetLogVersion(Facility.LIBRARY_JUTIL, 5, true, 0, 2),
        CargoConfigurationGetPermanentConfig(Facility.LIBRARY_CONFIGURATION, 0, true, 0, 32),
        CargoConfigurationGetProductSerialNumber(Facility.LIBRARY_CONFIGURATION, 8, true, 0, 19),
        CargoTimeGetUtcTime(Facility.LIBRARY_TIME, 0, true),
        CargoTimeSetUtcTime(Facility.LIBRARY_TIME, 1, false),
        CargoTimeGetLocalTime(Facility.LIBRARY_TIME, 2, true),
        CargoTimeUpdateTimezoneFile(Facility.LIBRARY_TIME, 4, false),
        CargoProfileGet(Facility.MODULE_PROFILE, 6, true),
        CargoProfileSet(Facility.MODULE_PROFILE, 7, false),
        CargoProfileByteArrayGet(Facility.MODULE_PROFILE, 8, true),
        CargoProfileByteArraySet(Facility.MODULE_PROFILE, 9, false),
        CargoHapticPlayVibrationStream(Facility.LIBRARY_HAPTIC, 0, false),
        CargoHapticGetVibrationStream(Facility.LIBRARY_HAPTIC, 1, true),
        CargoGoalTrackerSet(Facility.MODULE_GOAL_TRACKER, 0, false),
        CargoCrashDumpGetFileSize(Facility.DRIVER_CRASHDUMP, 1, true),
        CargoCrashDumpReadAndDeleteFile(Facility.DRIVER_CRASHDUMP, 2, true),
        LoggerGetChunkData(Facility.LOGGER, 1, true),
        LoggerGetChunkMetadata(Facility.LOGGER, 6, true, 0, 8),
        LoggerGetCounters(Facility.LOGGER, 9, true, 0, 8),
        LoggerDeleteChunk(Facility.LOGGER, 2, false),
        LoggerFlush(Facility.LOGGER, 13, false),
        LoggerGetChunkRangeMetadata(Facility.LOGGER, 14, true, 4, 12),
        LoggerGetChunkRangeData(Facility.LOGGER, 15, true),
        LoggerDeleteChunkRange(Facility.LOGGER, 16, false),
        CargoNotification(Facility.MODULE_NOTIFICATION, 0, false),
        CargoInstFileGetSize(Facility.MODULE_INSTRUMENTATION, 4, true),
        CargoInstFileRead(Facility.MODULE_INSTRUMENTATION, 5, true),
        CargoDynamicAppRegisterApp(Facility.MODULE_FIREBALL_APPSMANAGEMENT, 0, false),
        CargoDynamicAppRemoveApp(Facility.MODULE_FIREBALL_APPSMANAGEMENT, 1, false),
        CargoDynamicAppRegisterAppIcons(Facility.MODULE_FIREBALL_APPSMANAGEMENT, 2, false),
        CargoDynamicAppSetAppTileIndex(Facility.MODULE_FIREBALL_APPSMANAGEMENT, 3, false),
        CargoDynamicAppSetAppOrder(Facility.MODULE_FIREBALL_APPSMANAGEMENT, 4, false),
        CargoDynamicAppSetAppBadgeTileIndex(Facility.MODULE_FIREBALL_APPSMANAGEMENT, 5, false),
        CargoDynamicAppSetAppNotificationIndex(Facility.MODULE_FIREBALL_APPSMANAGEMENT, 11, false),
        CargoDynamicPageLayoutSet(Facility.MODULE_FIREBALL_PAGEMANAGEMENT, 0, false),
        CargoDynamicPageLayoutRemove(Facility.MODULE_FIREBALL_PAGEMANAGEMENT, 1, false),
        CargoDynamicPageLayoutGet(Facility.MODULE_FIREBALL_PAGEMANAGEMENT, 2, true),
        CargoInstalledAppListGet(Facility.MODULE_INSTALLED_APP_LIST, 0, true),
        CargoInstalledAppListSet(Facility.MODULE_INSTALLED_APP_LIST, 1, false),
        CargoInstalledAppUISyncStart(Facility.MODULE_INSTALLED_APP_LIST, 2, false),
        CargoInstalledAppUISyncEnd(Facility.MODULE_INSTALLED_APP_LIST, 3, false),
        CargoInstalledAppListGetDefaults(Facility.MODULE_INSTALLED_APP_LIST, 4, true),
        CargoInstalledAppListGetDefaultCount(Facility.MODULE_INSTALLED_APP_LIST, 5, true),
        CargoInstalledAppListSetStrapp(Facility.MODULE_INSTALLED_APP_LIST, 6, false),
        CargoInstalledAppListGetStrapp(Facility.MODULE_INSTALLED_APP_LIST, 7, true),
        CargoInstalledAppGetStrappSettingsMask(Facility.MODULE_INSTALLED_APP_LIST, 13, true),
        CargoInstalledAppSetStrappSettingsMask(Facility.MODULE_INSTALLED_APP_LIST, 14, false),
        CargoInstalledAppListGetNoImages(Facility.MODULE_INSTALLED_APP_LIST, 18, true),
        CargoInstalledAppListGetDefaultsNoImages(Facility.MODULE_INSTALLED_APP_LIST, 19, true),
        CargoInstalledAppListGetStrappNoImage(Facility.MODULE_INSTALLED_APP_LIST, 20, true),
        CargoInstalledAppListGetMaxCount(Facility.MODULE_INSTALLED_APP_LIST, 21, true),
        CargoFitnessPlansFileWrite(Facility.LIBRARY_FITNESS_PLANS, 4, false),
        CargoFitnessPlanFileMaxSize(Facility.LIBRARY_FITNESS_PLANS, 5, true),
        CargoKeyboardCommand(Facility.LIBRARY_KEYBOARD, 0, false),
        CargoGolfCourseFileWrite(Facility.LIBRARY_GOLF, 0, false),
        CargoGolfCourseFileGetMaxSize(Facility.LIBRARY_GOLF, 1, true),
        CargoPersistedStatisticsRunGet(Facility.MODULE_PERSISTED_STATISTICS, 2, true),
        CargoPersistedStatisticsWorkoutGet(Facility.MODULE_PERSISTED_STATISTICS, 3, true),
        CargoPersistedStatisticsSleepGet(Facility.MODULE_PERSISTED_STATISTICS, 4, true),
        CargoSRAMFWUpdateLoadData(Facility.LIBRARY_SRAM_FWUPDATE, 0, false),
        CargoSRAMFWUpdateBootIntoUpdateMode(Facility.LIBRARY_SRAM_FWUPDATE, 1, false),
        CargoSRAMFWUpdateValidateAssets(Facility.LIBRARY_SRAM_FWUPDATE, 2, true),
        CargoUIEndConnectedOOBE(Facility.MODULE_FIREBALLUI, 4, false),
        CargoFireballUINavigateToScreen(Facility.MODULE_FIREBALLUI, 0, false),
        CargoFireballUIGetCurrentScreen(Facility.MODULE_FIREBALLUI, 1, true),
        CargoFireballUINavigateToScreenGUID(Facility.MODULE_FIREBALLUI, 2, false),
        CargoFireballUIWriteMeTileImage(Facility.MODULE_FIREBALLUI, 5, false),
        CargoFireballUIClearMeTileImage(Facility.MODULE_FIREBALLUI, 6, false),
        CargoFireballSetSmsResponse(Facility.MODULE_FIREBALLUI, 7, false),
        CargoFireballSetAllSmsResponse(Facility.MODULE_FIREBALLUI, 8, false),
        CargoFireballGetAllSmsResponse(Facility.MODULE_FIREBALLUI, 11, true),
        CargoFireballUIReadMeTileImage(Facility.MODULE_FIREBALLUI, 14, true),
        CargoFireballUIWriteMeTileImageWithID(Facility.MODULE_FIREBALLUI, 17, false),
        CargoSubscriptionLoggerSubscribe(Facility.MODULE_LOGGERSUBSCRIPTIONS, 0, false),
        CargoSubscriptionLoggerUnsubscribe(Facility.MODULE_LOGGERSUBSCRIPTIONS, 1, false),
        CargoSubscriptionLoggerGetList(Facility.MODULE_LOGGERSUBSCRIPTIONS, 2, true, 0, 16),
        CargoLoggerEnableLogging(Facility.LIBRARY_LOGGER, 3, false),
        CargoLoggerDisableLogging(Facility.LIBRARY_LOGGER, 4, false),
        CargoLoggerDeleteLog(Facility.LIBRARY_LOGGER, 10, false),
        CargoThemeColorSetFirstPartyTheme(Facility.MODULE_THEME_COLOR, 0, false),
        CargoThemeColorGetFirstPartyTheme(Facility.MODULE_THEME_COLOR, 1, true),
        CargoThemeColorSetCustomTheme(Facility.MODULE_THEME_COLOR, 2, false),
        CargoThemeColorSetCustomThemeByIndex(Facility.MODULE_THEME_COLOR, 3, false),
        CargoThemeColorReset(Facility.MODULE_THEME_COLOR, 4, false),
        RemoteSubscriptionSubscribe(Facility.LIBRARY_REMOTE_SUBSCRIPTION, 0, false, 5, 0),
        RemoteSubscriptionUnsubscribe(Facility.LIBRARY_REMOTE_SUBSCRIPTION, 1, false, 1, 0),
        RemoteSubscriptionGetSubscriptions(Facility.LIBRARY_REMOTE_SUBSCRIPTION, 4, true),
        CargoSystemSettingsGetTimeZone(Facility.MODULE_SYSTEMSETTINGS, 10, true),
        CargoSystemSettingsSetTimeZone(Facility.MODULE_SYSTEMSETTINGS, 11, false),
        CargoSystemSettingsSetTimeSyncEnabled(Facility.MODULE_SYSTEMSETTINGS, 12, false),
        CargoSystemSettingsGetTimeSyncEnabled(Facility.MODULE_SYSTEMSETTINGS, 13, true, 0, 4),
        CargoSystemSettingsEphemerisFileWrite(Facility.MODULE_SYSTEMSETTINGS, 15, false),
        CargoSystemSettingsGetMeTileImageId(Facility.MODULE_SYSTEMSETTINGS, 18, true),
        CargoSystemSettingsOOBEGet(Facility.MODULE_SYSTEMSETTINGS, 19, true, 0, 4),
        CargoAppDataSetRunMetrics(Facility.MODULE_PERSISTED_APPLICATION_DATA, 0, false),
        CargoAppDataGetRunMetrics(Facility.MODULE_PERSISTED_APPLICATION_DATA, 1, true),
        CargoAppDataSetBikeMetrics(Facility.MODULE_PERSISTED_APPLICATION_DATA, 2, false),
        CargoAppDataGetBikeMetrics(Facility.MODULE_PERSISTED_APPLICATION_DATA, 3, true),
        CargoAppDataSetBikeSplitDist(Facility.MODULE_PERSISTED_APPLICATION_DATA, 4, false),
        CargoAppDataGetBikeSplitDist(Facility.MODULE_PERSISTED_APPLICATION_DATA, 5, true),
        CargoAppDataSetWorkoutActivities(Facility.MODULE_PERSISTED_APPLICATION_DATA, 9, false),
        CargoAppDataGetWorkoutActivities(Facility.MODULE_PERSISTED_APPLICATION_DATA, 16, true),
        CargoAppDataSetSleepNotification(Facility.MODULE_PERSISTED_APPLICATION_DATA, 17, false),
        CargoAppDataGetSleepNotification(Facility.MODULE_PERSISTED_APPLICATION_DATA, 18, true),
        CargoAppDataDisableSleepNotification(Facility.MODULE_PERSISTED_APPLICATION_DATA, 19, false),
        CargoPegRead(Facility.LIBRARY_PEG, 0, true),
        CargoOOBESetStage(Facility.MODULE_OOBE, 0, false),
        CargoOOBEGetStage(Facility.MODULE_OOBE, 1, true),
        CargoOOBEFinalize(Facility.MODULE_OOBE, 2, false),
        Unknown(Facility.UNKNOWN, 0, false);
        
        private final int mArgSize;
        private final int mCode;
        private final Facility mFacility;
        private final int mPayloadSize;

        public Facility getFacility() {
            return this.mFacility;
        }

        public final int getCode() {
            return this.mCode;
        }

        public final int getArgSize() {
            return this.mArgSize;
        }

        public final int getPayloadSize() {
            return this.mPayloadSize;
        }

        public final boolean isTX() {
            return BandDeviceConstants.isTxCommand(this.mCode);
        }

        Command(Facility facility, int index, boolean isTX) {
            this(facility, index, isTX, 0, 0);
        }

        Command(Facility facility, int index, boolean isTX, int argSize, int payloadSize) {
            this.mFacility = facility;
            this.mCode = BandDeviceConstants.makeCommand(facility.getCode(), (byte) (index & TransportMediator.KEYCODE_MEDIA_PAUSE), isTX);
            this.mArgSize = argSize;
            this.mPayloadSize = payloadSize;
        }

        public static Command lookup(int commandId) {
            Command[] arr$ = values();
            for (Command command : arr$) {
                if (command.getCode() == commandId) {
                    return command;
                }
            }
            return Unknown;
        }
    }

    public static final boolean isTxCommand(int commandId) {
        return ((commandId & 255) >> 7) != 0;
    }

    static final int makeCommand(byte facility, byte index, boolean isTX) {
        return (((isTX ? 1 : 0) << 7) | (facility << 8) | index) & 65535;
    }

    /* loaded from: classes.dex */
    public enum ResultCode {
        BATTERY_READ_BUSY_ERROR(Facility.BATTERY, 7, true),
        LOGGER_BUSY_ERROR(Facility.LOGGER, 2, true),
        LOGGER_FLASH_OPERATION_IN_PROGRESS_ERROR(Facility.LOGGER, 10, true),
        SRAMFWUPDATE_BOOT_INTO_UPDATE_MODE(Facility.LIBRARY_SRAM_FWUPDATE, 0, false),
        SRAMFWUPDATE_RESET_REASON_SRAM(Facility.LIBRARY_SRAM_FWUPDATE, 1, false),
        SRAMFWUPDATE_RESET_REASON_SRAM_TIMEOUT(Facility.LIBRARY_SRAM_FWUPDATE, 2, true),
        SRAMFWUPDATE_BATTERY_TOO_LOW(Facility.LIBRARY_SRAM_FWUPDATE, 3, true),
        TIME_SYNC_DISABLED(Facility.DRIVER_RTC, 2, true),
        FILE_STRUCT_IN_USE(Facility.LIBRARY_FILE, 1, true),
        FILE_PENDING(Facility.LIBRARY_FILE, 0, false),
        FILE_ALREADY_OPEN(Facility.LIBRARY_FILE, 2, true),
        FBUI_SYNC_IN_PROGRESS(Facility.MODULE_FIREBALLUI, 7, true),
        NOTIFICATION_GENERIC_DATA_MULT_LONG_STR_NOT_SUPP(Facility.MODULE_NOTIFICATION, 105, true),
        INSTALLED_APP_LIST_APP_NOT_FOUND(Facility.MODULE_INSTALLED_APP_LIST, 10, true),
        NOTIFICATION_GENERIC_DATA_NO_LAYOUT(Facility.MODULE_NOTIFICATION, 106, true),
        END_OF_RESULT_CODES(Facility.UNKNOWN, -1, true);
        
        private int mResultCode;

        public int getCode() {
            return this.mResultCode;
        }

        ResultCode(Facility facility, int code, boolean severe) {
            this.mResultCode = BandDeviceConstants.MakeResultCode(facility, code, severe);
        }

        @Override // java.lang.Enum
        public String toString() {
            return String.format(Locale.getDefault(), "%s(%08X)", super.toString(), Integer.valueOf(getCode()));
        }
    }

    public static final int MakeResultCode(Facility facility, int code, boolean severe) {
        int resultCode = 536870912 | ((facility.getCode() & 255) << 16) | code;
        if (severe) {
            return resultCode | Integer.MIN_VALUE;
        }
        return resultCode;
    }

    public static final int MakeResultCode(Facility facility, byte categoryCode, int code, boolean severe) {
        int resultCode = 536870912 | ((facility.getCode() & 255) << 16) | (categoryCode << 8) | code;
        if (severe) {
            return resultCode | Integer.MIN_VALUE;
        }
        return resultCode;
    }

    public static byte getFacilityCodeFromResultCode(int resultCode) {
        return (byte) ((resultCode >> 16) & 255);
    }

    public static Facility getFacilityFromResultCode(int resultCode) {
        int fCode = getFacilityCodeFromResultCode(resultCode);
        Facility[] arr$ = Facility.values();
        for (Facility item : arr$) {
            if ((item.getCode() & 255) == fCode) {
                return item;
            }
        }
        return Facility.UNKNOWN;
    }

    public static final boolean isResultSevere(int resultCode) {
        return (Integer.MIN_VALUE & resultCode) == Integer.MIN_VALUE;
    }
}

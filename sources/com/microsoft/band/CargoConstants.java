package com.microsoft.band;

import com.microsoft.band.internal.BandServiceMessage;
import com.microsoft.band.internal.device.DeviceInfo;
/* loaded from: classes.dex */
public final class CargoConstants {
    public static final String ACS_TEST_TOKEN_PLACEHOLDER = "nameidentifier=%s&identityprovider=test&ExpiresOn=%d";
    public static final String ACTION_BIND_BAND_SERVICE_ADMIN = "com.microsoft.band.service.access.BIND_BAND_SERVICE_ADMIN";
    public static final String ACTION_COMMAND_RESULT = "com.microsoft.band.action.commandResult";
    public static final String ACTION_PAIRING_REACTION_FINISHED = "com.microsoft.band.action.ACTION_PAIRING_ATTEMPT_OVER";
    public static final String ACTION_PUSH_CALL_DISMISSED = "com.microsoft.band.device.subscription.ACTION_PUSH_CALL_DISMISSED";
    public static final String ACTION_PUSH_SMS_DISMISSED = "com.microsoft.band.device.subscription.ACTION_PUSH_SMS_DISMISSED";
    public static final String ACTION_REGISTRATION_COMPLETE = "com.microsoft.band.action.ACTION_REGISTRATION_COMPLETE";
    public static final byte CARGO_PCB = 9;
    public static final int CRYPTO_VERSION = 0;
    public static final String CrashDumps = "CrashDump";
    public static final int DEFAULT_ALLOWED_DEVICE_TIME_DRIFT_IN_MINUTES = 0;
    public static final long DEVICE_BONDED_CHECK_RETRY_IN_MILLIS = 200;
    public static final String DISCOVERY_TAG = "CargoDiscovery";
    public static final String EXTRA_ALLOWED_DEVICE_TIME_DRIFT_IN_MINUTES = "com.microsoft.band.extra.Allowed.Device.Time.Drift";
    public static final String EXTRA_CLOUD_DATA = "com.microsoft.band.extra.Cloud.Data";
    public static final String EXTRA_COMMAND = "com.microsoft.band.extra.command";
    public static final String EXTRA_DOWNLOAD_SYNC_RESULT = "com.microsoft.band.extra.download.Sync.Result";
    public static final String EXTRA_FIRMWARE_DEVICE_FAMILY = "com.microsoft.band.extra.Firmware.Device.Family";
    public static final String EXTRA_FIRMWARE_FW_VALIDATION = "com.microsoft.band.extra.Firmware.FWOnDeviceValidation";
    public static final String EXTRA_FIRMWARE_QUERY_PARAM = "com.microsoft.band.extra.Firmware.QueryParams";
    public static final String EXTRA_FIRMWARE_VERSION_BL = "com.microsoft.band.extra.Firmware.Version.BL";
    public static final String EXTRA_FIRMWARE_VERSION_UP = "com.microsoft.band.extra.Firmware.Version.Up";
    public static final String EXTRA_REGISTERED_DEVICE = "com.microsoft.band.device";
    public static final String EXTRA_RESULT_CODE = "com.microsoft.band.extra.upload.Result.Code";
    public static final String EXTRA_SUBSCRIPTION_DEVICE_INFO = "com.microsoft.band.device.subscription.EXTRA_SUBSCRIPTION_DEVICE_INFO";
    public static final String EXTRA_SYNC_RESULT = "com.microsoft.band.extra.Sync.Result";
    public static final String EXTRA_UPLOAD_SYNC_RESULT = "com.microsoft.band.extra.upload.Sync.Result";
    public static final String HASH_TO_APP_NAME_MAP = "HTAN";
    public static final String Instrumentation = "Instrumentation";
    public static final String KEY_LOG_BYTES = "com.microsoft.band.LOG_BYTES";
    public static final String KEY_LOG_TYPE = "com.microsoft.band.LOG_TYPES";
    public static final String KEY_PREFIX = "com.microsoft.band.";
    public static final String OAUTH_LOGIN_URL = "https://login.live.com/oauth20_authorize.srf?client_id=000000004811DB42&scope=service::%s::MBI_SSL&response_type=token&redirect_uri=https://login.live.com/oauth20_desktop.srf";
    public static final String PROGRESS_CODE = "ProgressCode";
    public static final String PROGRESS_VALUE = "ProgressValue";
    public static final String last_ephemeris_check_time = "last_ephemeris_check_time";
    public static final String last_ephemeris_download_time = "last_ephemeris_download_time";
    public static final String last_ephemeris_json = "last_ephemeris_json";
    public static final String last_ephemeris_upgrade_time = "last_ephemeris_upgrade_time";
    public static final String last_telemetry_file_retrieved_attempt_time = "last_telemetry_file_retrieved_time";
    public static final String last_timezone_check_time = "last_timezone_check_time";
    public static final String last_timezone_download_time = "last_timezone_download_time";
    public static final String last_timezone_json = "last_timezone_json";
    public static final String last_timezone_upgrade_time = "last_timezone_upgrade_time";
    public static final String ACTION_SYNC_DEVICE_TO_CLOUD_STARTED = "com.microsoft.band..action." + BandServiceMessage.Response.SYNC_DEVICE_TO_CLOUD_STARTED;
    public static final String ACTION_SYNC_DEVICE_TO_CLOUD_COMPLETED = "com.microsoft.band..action." + BandServiceMessage.Response.SYNC_DEVICE_TO_CLOUD_COMPLETED;
    public static final String ACTION_SYNC_PROGRESS = "com.microsoft.band..action." + BandServiceMessage.SYNC_PROGRESS;
    public static final String ACTION_FW_UPGRADE_PROGRESS = "com.microsoft.band..action." + BandServiceMessage.FIRMWARE_UPGRADE_PROGRESS;
    public static final String ACTION_DOWNLOAD_FIRMWARE_UPDATE_STARTED = "com.microsoft.band..action." + BandServiceMessage.Response.DOWNLOAD_FIRMWARE_UPDATE_STARTED;
    public static final String ACTION_DOWNLOAD_FIRMWARE_UPDATE_COMPLETED = "com.microsoft.band..action." + BandServiceMessage.Response.DOWNLOAD_FIRMWARE_UPDATE_COMPLETED;
    public static final String ACTION_DOWNLOAD_EPHEMERIS_UPDATE_STARTED = "com.microsoft.band..action." + BandServiceMessage.Response.DOWNLOAD_EPHEMERIS_UPDATE_STARTED;
    public static final String ACTION_DOWNLOAD_EPHEMERIS_UPDATE_COMPLETED = "com.microsoft.band..action." + BandServiceMessage.Response.DOWNLOAD_EPHEMERIS_UPDATE_COMPLETED;
    public static final String ACTION_DOWNLOAD_TIME_ZONE_SETTINGS_UPDATE_STARTED = "com.microsoft.band..action." + BandServiceMessage.Response.DOWNLOAD_TIMEZONE_SETTINGS_UPDATE_STARTED;
    public static final String ACTION_DOWNLOAD_TIME_ZONE_SETTINGS_UPDATE_COMPLETED = "com.microsoft.band..action." + BandServiceMessage.Response.DOWNLOAD_TIMEZONE_SETTINGS_UPDATE_COMPLETED;
    public static final String ACTION_DEVICE_CONNECTED = "com.microsoft.band..action." + BandServiceMessage.Response.DEVICE_CONNECTED;
    public static final String ACTION_DEVICE_DISCONNECTED = "com.microsoft.band..action." + BandServiceMessage.Response.DEVICE_DISCONNECTED;
    public static final String ACTION_UPGRADE_FIRMWARE_STARTED = "com.microsoft.band..action." + BandServiceMessage.Response.UPGRADE_FIRMWARE_STARTED;
    public static final String ACTION_UPGRADE_FIRMWARE_COMPLETED = "com.microsoft.band..action." + BandServiceMessage.Response.UPGRADE_FIRMWARE_COMPLETED;
    public static final String ACTION_UPGRADE_EPHEMERIS_STARTED = "com.microsoft.band..action." + BandServiceMessage.Response.UPGRADE_EPHEMERIS_STARTED;
    public static final String ACTION_UPGRADE_EPHEMERIS_COMPLETED = "com.microsoft.band..action." + BandServiceMessage.Response.UPGRADE_EPHEMERIS_COMPLETED;
    public static final String ACTION_UPGRADE_TIME_ZONE_SETTINGS_STARTED = "com.microsoft.band..action." + BandServiceMessage.Response.UPGRADE_TIMEZONE_SETTINGS_STARTED;
    public static final String ACTION_UPGRADE_TIME_ZONE_SETTINGS_COMPLETED = "com.microsoft.band..action." + BandServiceMessage.Response.UPGRADE_TIMEZONE_SETTINGS_COMPLETED;
    public static final String ACTION_SYNC_TIMEZONE_COMPLETED = "com.microsoft.band..action." + BandServiceMessage.Response.SYNC_TIMEZONE_COMPLETED;
    public static final String ACTION_SYNC_TIME_COMPLETED = "com.microsoft.band..action." + BandServiceMessage.Response.SYNC_TIME_COMPLETED;
    public static final String ACTION_SYNC_WEB_TILE_COMPLETED = "com.microsoft.band..action." + BandServiceMessage.Response.SYNC_WEB_TILE_COMPLETED;
    public static final String EXTRA_DEVICE_INFO = DeviceInfo.class.getName();

    private CargoConstants() {
        throw new UnsupportedOperationException();
    }
}

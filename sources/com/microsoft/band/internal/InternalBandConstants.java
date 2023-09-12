package com.microsoft.band.internal;

import com.microsoft.band.internal.device.DeviceInfo;
import com.microsoft.band.internal.util.VersionCheck;
/* loaded from: classes.dex */
public final class InternalBandConstants {
    public static final int ACCELEROMETER_128MS_CODE = 0;
    public static final int ACCELEROMETER_16MS_CODE = 48;
    public static final int ACCELEROMETER_32MS_CODE = 1;
    public static final String ACTION_BIND_BAND_SERVICE = "com.microsoft.band.service.action.BIND_BAND_SERVICE";
    public static final int ALS = 25;
    public static final int BAND_SDK_MAJOR_MINOR_VERSION_MASK = -65536;
    public static final int BAND_SDK_MAJOR_VERSION_MASK = -16777216;
    public static final int BAND_SDK_MINOR_VERSION_MASK = 16711680;
    public static final int BAND_SDK_PATCH_VERSION_MASK = 65535;
    public static final int BAND_SDK_V2_CHANGE_VERSION = 16842752;
    public static final int BAND_SDK_VERSION = 16842752;
    public static final int BAND_SDK_VERSION_UNKNOWN = -1;
    public static final int BAROMETER = 58;
    public static final int CALORIES = 46;
    public static final int CONTACT_CODE = 35;
    public static final int DIALOG_BODY_MAX_LENGTH = 20;
    public static final int DIALOG_TITLE_MAX_LENGTH = 20;
    public static final int DISTANCE_CODE = 13;
    public static final int ELEVATION = 71;
    public static final String EXTRA_COMMAND_DATA = "com.microsoft.band.extra.data";
    public static final String EXTRA_COMMAND_INDEX = "com.microsoft.band.extra.index";
    public static final String EXTRA_COMMAND_PAYLOAD = "com.microsoft.band.extra.payload";
    public static final String EXTRA_COMMAND_PAYLOAD_SIZE = "com.microsoft.band.extra.payloadSize";
    public static final String EXTRA_COMMAND_QUEUE_LIMIT = "com.microsoft.band.extra.queue.limit";
    public static final String EXTRA_COMMAND_RESULT = "com.microsoft.band.extra.result";
    public static final String EXTRA_COMMAND_RESULT_CODE = "com.microsoft.band.extra.resultCode";
    public static final String EXTRA_DEVICE_INFO = DeviceInfo.class.getName();
    public static final String EXTRA_FIRMWARE_VERSION_APP = "com.microsoft.band.extra.Firmware.Version.App";
    public static final String EXTRA_HARDWARE_VERSION = "com.microsoft.band.extra.Hardware.Version";
    public static final String EXTRA_MESSAGE_BODY = "com.microsoft.band.extra.body";
    public static final String EXTRA_MESSAGE_FLAG = "com.microsoft.band.extra.flag";
    public static final String EXTRA_MESSAGE_TIMESTAMP = "com.microsoft.band.extra.Message.Timestamp";
    public static final String EXTRA_MESSAGE_TITLE = "com.microsoft.band.extra.title";
    public static final String EXTRA_PERSONALIZE_METILE = "com.microsoft.band.extra.METILE";
    public static final String EXTRA_PERSONALIZE_METILE_ID = "com.microsoft.band.extra.METILE_ID";
    public static final String EXTRA_PERSONALIZE_STRAPPS = "com.microsoft.band.extra.STRAPPS";
    public static final String EXTRA_PERSONALIZE_THEME = "com.microsoft.band.extra.THEME";
    public static final String EXTRA_PERSONALIZE_THEME_MAP = "com.microsoft.band.extra.THEME_MAP";
    public static final String EXTRA_SERVICE_COMMAND_PAYLOAD = "com.microsoft.band.extra.service.payload";
    public static final String EXTRA_SUBSCRIPTION_DATA = "com.microsoft.band.device.subscription.EXTRA_SUBSCRIPTION_DATA";
    public static final String EXTRA_UUID = "com.microsoft.band.extra.UUID";
    public static final int GYROSCOPE_128MS_CODE = 4;
    public static final int GYROSCOPE_16MS_CODE = 49;
    public static final int GYROSCOPE_32MS_CODE = 5;
    public static final int Gsr = 15;
    public static final int HEART_RATE_CODE = 16;
    public static final String HR_ALLOWED = "c3";
    public static final String KEYBOARD_BASE_TAG = "Keyboard";
    public static final String KEY_PREFIX = "com.microsoft.band.";
    public static final int MAX_ADDITIONAL_ICONS_PER_TILE = 8;
    public static final int MAX_ALLOWED_SIMULTANEOUS_COMMANDS_PER_CLIENT = 100;
    public static final int MAX_ICONS_PER_TILE = 10;
    public static final int MAX_TILE_LAYOUTS = 5;
    public static final int MAX_TILE_NAME_LENGTH = 21;
    public static final int MAX_TILE_PAGES = 8;
    public static final int MESSAGE_BODY_MAX_LENGTH = 160;
    public static final int MESSAGE_TITLE_MAX_LENGTH = 40;
    public static final String PAGE_DATA = "com.microsoft.band..tiles.pages.PageData";
    public static final int PAGE_ELEMENT_TEXT_MAX_LENGTH = 160;
    public static final int PEDOMETER_CODE = 19;
    public static final String PREFERENCES = "p7";
    public static final int RR_INTERVAL = 26;
    public static final int SKIN_TEMP_CODE = 20;
    public static final int SMALL_ICON_WIDTH_AND_HEIGHT = 24;
    public static final String STREAM_TAG = "StreamTest";
    public static final int TILE_ICON_WIDTH_AND_HEIGHT = 46;
    public static final int UV_CODE = 21;
    public static final byte V2_HARDWARE_THRESHOLD = 20;
    public static final int V2_MAX_ADDITIONAL_ICONS_PER_TILE = 13;
    public static final int V2_MAX_ICONS_PER_TILE = 15;

    private InternalBandConstants() {
        throw new UnsupportedOperationException();
    }

    public static int getMaxIconsPerTile(int hardwareVersion) {
        return VersionCheck.isV2DeviceOrGreater(hardwareVersion) ? 15 : 10;
    }

    public static int getAdditionalMaxIconsPerTile(int hardwareVersion) {
        return VersionCheck.isV2DeviceOrGreater(hardwareVersion) ? 13 : 8;
    }
}

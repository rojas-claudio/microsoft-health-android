package com.microsoft.band.internal;

import com.google.android.gms.appstate.AppStateClient;
import com.google.android.gms.location.LocationRequest;
import com.microsoft.band.internal.BandDeviceConstants;
import com.microsoft.kapp.utils.Constants;
/* loaded from: classes.dex */
public enum BandServiceMessage {
    NULL_MESSAGE(0),
    REGISTER_CLIENT(100),
    REGISTER_CLIENT_RESPONSE(101),
    UNREGISTER_CLIENT(Constants.SLEEP_FRAGMENT_EVENT_DETAILS_REQUEST),
    QUERY_IS_CLIENT_ALIVE(LocationRequest.PRIORITY_LOW_POWER),
    QUERY_IS_CLIENT_ALIVE_RESPONSE(105),
    REGISTER_CLIENT_WITH_VERSION(106),
    PROCESS_COMMAND(1000),
    PROCESS_COMMAND_RESPONSE(1001),
    PROCESS_PUSH_DATA(1002),
    DEVICE_STATUS_NOTIFICATION(AppStateClient.STATUS_WRITE_OUT_OF_DATE_VERSION),
    SYNC_NOTIFICATION(AppStateClient.STATUS_WRITE_SIZE_EXCEEDED),
    DOWNLOAD_NOTIFICATION(AppStateClient.STATUS_STATE_KEY_NOT_FOUND),
    UPGRADE_NOTIFICATION(AppStateClient.STATUS_STATE_KEY_LIMIT_EXCEEDED),
    SYNC_PROGRESS(2004),
    FIRMWARE_UPGRADE_PROGRESS(2005);
    
    private final int mMessageId;

    /* loaded from: classes.dex */
    public enum Response {
        UNSPECIFIED(0),
        SUCCESS(BandDeviceConstants.MakeResultCode(BandDeviceConstants.Facility.CARGO_SERVICE, (byte) 0, 0, false)),
        PENDING(BandDeviceConstants.MakeResultCode(BandDeviceConstants.Facility.CARGO_SERVICE, (byte) 0, 1, false)),
        CANCELED(BandDeviceConstants.MakeResultCode(BandDeviceConstants.Facility.CARGO_SERVICE, (byte) 0, 2, false)),
        INTERRUPTED(BandDeviceConstants.MakeResultCode(BandDeviceConstants.Facility.CARGO_SERVICE, (byte) 0, 3, false)),
        ENQUEUED(BandDeviceConstants.MakeResultCode(BandDeviceConstants.Facility.CARGO_SERVICE, (byte) 0, 4, false)),
        OPERATION_NOT_REQUIRED(BandDeviceConstants.MakeResultCode(BandDeviceConstants.Facility.CARGO_SERVICE, (byte) 0, 5, false)),
        PERMISSION_DENIED(BandDeviceConstants.MakeResultCode(BandDeviceConstants.Facility.CARGO_SERVICE, (byte) 0, 6, true)),
        TOO_MANY_SIMULTANEOUS_COMMANDS_ERROR(BandDeviceConstants.MakeResultCode(BandDeviceConstants.Facility.CARGO_SERVICE, (byte) 0, 7, true)),
        CLIENT_VERSION_UNSUPPORTED_ERROR(BandDeviceConstants.MakeResultCode(BandDeviceConstants.Facility.CARGO_SERVICE, (byte) 0, 8, true)),
        DEVICE_CONNECTED(BandDeviceConstants.MakeResultCode(BandDeviceConstants.Facility.CARGO_SERVICE, (byte) 1, 0, false)),
        DEVICE_DISCONNECTED(BandDeviceConstants.MakeResultCode(BandDeviceConstants.Facility.CARGO_SERVICE, (byte) 1, 1, false)),
        SYNC_DEVICE_TO_CLOUD_STARTED(BandDeviceConstants.MakeResultCode(BandDeviceConstants.Facility.CARGO_SERVICE, (byte) 1, 16, false)),
        SYNC_DEVICE_TO_CLOUD_COMPLETED(BandDeviceConstants.MakeResultCode(BandDeviceConstants.Facility.CARGO_SERVICE, (byte) 1, 17, false)),
        SYNC_WEB_TILE_COMPLETED(BandDeviceConstants.MakeResultCode(BandDeviceConstants.Facility.CARGO_SERVICE, (byte) 1, 18, false)),
        DOWNLOAD_EPHEMERIS_UPDATE_STARTED(BandDeviceConstants.MakeResultCode(BandDeviceConstants.Facility.CARGO_SERVICE, (byte) 1, 48, false)),
        DOWNLOAD_EPHEMERIS_UPDATE_COMPLETED(BandDeviceConstants.MakeResultCode(BandDeviceConstants.Facility.CARGO_SERVICE, (byte) 1, 49, false)),
        UPGRADE_EPHEMERIS_STARTED(BandDeviceConstants.MakeResultCode(BandDeviceConstants.Facility.CARGO_SERVICE, (byte) 1, 50, false)),
        UPGRADE_EPHEMERIS_COMPLETED(BandDeviceConstants.MakeResultCode(BandDeviceConstants.Facility.CARGO_SERVICE, (byte) 1, 51, false)),
        EPHEMERIS_DOWNLOAD_NOT_REQUIRED(BandDeviceConstants.MakeResultCode(BandDeviceConstants.Facility.CARGO_SERVICE, (byte) 1, 52, false)),
        DOWNLOAD_TIMEZONE_SETTINGS_UPDATE_STARTED(BandDeviceConstants.MakeResultCode(BandDeviceConstants.Facility.CARGO_SERVICE, (byte) 1, 64, false)),
        DOWNLOAD_TIMEZONE_SETTINGS_UPDATE_COMPLETED(BandDeviceConstants.MakeResultCode(BandDeviceConstants.Facility.CARGO_SERVICE, (byte) 1, 65, false)),
        UPGRADE_TIMEZONE_SETTINGS_STARTED(BandDeviceConstants.MakeResultCode(BandDeviceConstants.Facility.CARGO_SERVICE, (byte) 1, 66, false)),
        UPGRADE_TIMEZONE_SETTINGS_COMPLETED(BandDeviceConstants.MakeResultCode(BandDeviceConstants.Facility.CARGO_SERVICE, (byte) 1, 67, false)),
        TIMEZONE_DOWNLOAD_NOT_REQUIRED(BandDeviceConstants.MakeResultCode(BandDeviceConstants.Facility.CARGO_SERVICE, (byte) 1, 68, false)),
        DOWNLOAD_FIRMWARE_UPDATE_STARTED(BandDeviceConstants.MakeResultCode(BandDeviceConstants.Facility.CARGO_SERVICE, (byte) 1, 80, false)),
        DOWNLOAD_FIRMWARE_UPDATE_COMPLETED(BandDeviceConstants.MakeResultCode(BandDeviceConstants.Facility.CARGO_SERVICE, (byte) 1, 81, false)),
        UPGRADE_FIRMWARE_STARTED(BandDeviceConstants.MakeResultCode(BandDeviceConstants.Facility.CARGO_SERVICE, (byte) 1, 82, false)),
        UPGRADE_FIRMWARE_COMPLETED(BandDeviceConstants.MakeResultCode(BandDeviceConstants.Facility.CARGO_SERVICE, (byte) 1, 83, false)),
        UPGRADE_FIRMWARE_LOADING_FIRMWARE(BandDeviceConstants.MakeResultCode(BandDeviceConstants.Facility.CARGO_SERVICE, (byte) 1, 84, false)),
        UPGRADE_FIRMWARE_ENTERING_UPGRADE_MODE(BandDeviceConstants.MakeResultCode(BandDeviceConstants.Facility.CARGO_SERVICE, (byte) 1, 85, false)),
        UPGRADE_FIRMWARE_INSTALLING_FIRMWARE(BandDeviceConstants.MakeResultCode(BandDeviceConstants.Facility.CARGO_SERVICE, (byte) 1, 86, false)),
        UPGRADE_FIRMWARE_FINALIZING_UPGRADE(BandDeviceConstants.MakeResultCode(BandDeviceConstants.Facility.CARGO_SERVICE, (byte) 1, 87, false)),
        UPGRADE_FIRMWARE_DEALING_WITH_SENSOR_LOGS(BandDeviceConstants.MakeResultCode(BandDeviceConstants.Facility.CARGO_SERVICE, (byte) 1, 88, false)),
        SYNC_TIME_COMPLETED(BandDeviceConstants.MakeResultCode(BandDeviceConstants.Facility.CARGO_SERVICE, (byte) 1, 96, false)),
        SYNC_TIMEZONE_COMPLETED(BandDeviceConstants.MakeResultCode(BandDeviceConstants.Facility.CARGO_SERVICE, (byte) 1, 97, false)),
        FILE_NOT_ON_DEVICE(BandDeviceConstants.MakeResultCode(BandDeviceConstants.Facility.CARGO_SERVICE, (byte) 1, 112, false)),
        SERVICE_CLOUD_UPLOAD_DATA_CORRUPT(BandDeviceConstants.MakeResultCode(BandDeviceConstants.Facility.CARGO_SERVICE, (byte) 4, 113, false)),
        INVALID_SESSION_TOKEN_ERROR(BandDeviceConstants.MakeResultCode(BandDeviceConstants.Facility.CARGO_SERVICE, (byte) 5, 0, true)),
        INVALID_ARG_ERROR(BandDeviceConstants.MakeResultCode(BandDeviceConstants.Facility.CARGO_SERVICE, (byte) 5, 1, true)),
        INVALID_OPERATION_ERROR(BandDeviceConstants.MakeResultCode(BandDeviceConstants.Facility.CARGO_SERVICE, (byte) 5, 2, true)),
        NO_METILE_IMAGE(BandDeviceConstants.MakeResultCode(BandDeviceConstants.Facility.CARGO_SERVICE, (byte) 5, 3, true)),
        TILE_SECURITY_ERROR(BandDeviceConstants.MakeResultCode(BandDeviceConstants.Facility.CARGO_SERVICE, (byte) 5, 4, true)),
        TILE_ICON_ERROR(BandDeviceConstants.MakeResultCode(BandDeviceConstants.Facility.CARGO_SERVICE, (byte) 5, 5, true)),
        TILE_NOT_FOUND_ERROR(BandDeviceConstants.MakeResultCode(BandDeviceConstants.Facility.CARGO_SERVICE, (byte) 5, 6, true)),
        TILE_ALREADY_EXISTS_ERROR(BandDeviceConstants.MakeResultCode(BandDeviceConstants.Facility.CARGO_SERVICE, (byte) 5, 7, true)),
        BAND_IS_FULL_ERROR(BandDeviceConstants.MakeResultCode(BandDeviceConstants.Facility.CARGO_SERVICE, (byte) 5, 8, true)),
        UPDATE_DEFAULT_STRAPP_ERROR(BandDeviceConstants.MakeResultCode(BandDeviceConstants.Facility.CARGO_SERVICE, (byte) 5, 16, true)),
        TILE_LAYOUT_ERROR(BandDeviceConstants.MakeResultCode(BandDeviceConstants.Facility.CARGO_SERVICE, (byte) 5, 17, true)),
        TILE_PAGE_DATA_ERROR(BandDeviceConstants.MakeResultCode(BandDeviceConstants.Facility.CARGO_SERVICE, (byte) 5, 18, true)),
        WEB_TILE_LBLOB_ERROR(BandDeviceConstants.MakeResultCode(BandDeviceConstants.Facility.CARGO_SERVICE, (byte) 5, 19, true)),
        TILE_LAYOUT_INDEX_ERROR(BandDeviceConstants.MakeResultCode(BandDeviceConstants.Facility.CARGO_SERVICE, (byte) 5, 20, true)),
        WEB_TILE_READ_ERROR(BandDeviceConstants.MakeResultCode(BandDeviceConstants.Facility.CARGO_SERVICE, (byte) 5, 21, true)),
        OPERATION_TIMEOUT_ERROR(BandDeviceConstants.MakeResultCode(BandDeviceConstants.Facility.CARGO_SERVICE, (byte) 6, 0, true)),
        OPERATION_INTERRUPTED_ERROR(BandDeviceConstants.MakeResultCode(BandDeviceConstants.Facility.CARGO_SERVICE, (byte) 6, 1, true)),
        OPERATION_EXCEPTION_ERROR(BandDeviceConstants.MakeResultCode(BandDeviceConstants.Facility.CARGO_SERVICE, (byte) 6, 2, true)),
        DEVICE_COMMAND_ERROR(BandDeviceConstants.MakeResultCode(BandDeviceConstants.Facility.CARGO_SERVICE, (byte) 3, 0, true)),
        DEVICE_COMMAND_RESPONSE_ERROR(BandDeviceConstants.MakeResultCode(BandDeviceConstants.Facility.CARGO_SERVICE, (byte) 3, 1, true)),
        DEVICE_NOT_CONNECTED_ERROR(BandDeviceConstants.MakeResultCode(BandDeviceConstants.Facility.CARGO_SERVICE, (byte) 3, 2, true)),
        DEVICE_NOT_BONDED_ERROR(BandDeviceConstants.MakeResultCode(BandDeviceConstants.Facility.CARGO_SERVICE, (byte) 3, 3, true)),
        DEVICE_IO_ERROR(BandDeviceConstants.MakeResultCode(BandDeviceConstants.Facility.CARGO_SERVICE, (byte) 3, 4, true)),
        DEVICE_TIMEOUT_ERROR(BandDeviceConstants.MakeResultCode(BandDeviceConstants.Facility.CARGO_SERVICE, (byte) 3, 5, true)),
        DEVICE_DATA_ERROR(BandDeviceConstants.MakeResultCode(BandDeviceConstants.Facility.CARGO_SERVICE, (byte) 3, 6, true)),
        DEVICE_STATE_ERROR(BandDeviceConstants.MakeResultCode(BandDeviceConstants.Facility.CARGO_SERVICE, (byte) 3, 7, true)),
        DEVICE_BATTERY_LOW_ERROR(BandDeviceConstants.MakeResultCode(BandDeviceConstants.Facility.CARGO_SERVICE, (byte) 3, 9, true)),
        DEVICE_FIRMWARE_VERSION_INCOMPATIBLE_ERROR(BandDeviceConstants.MakeResultCode(BandDeviceConstants.Facility.CARGO_SERVICE, (byte) 3, 10, true)),
        DEVICE_TIME_SYNC_DISABLE_ERROR(BandDeviceConstants.MakeResultCode(BandDeviceConstants.Facility.CARGO_SERVICE, (byte) 3, 11, true)),
        DEVICE_FIRMWARE_UPGRADE_VERSION_FAILED_ERROR(BandDeviceConstants.MakeResultCode(BandDeviceConstants.Facility.CARGO_SERVICE, (byte) 3, 12, true)),
        DEVICE_FBUI_SYNC_IN_PROGRESS_ERROR(BandDeviceConstants.MakeResultCode(BandDeviceConstants.Facility.CARGO_SERVICE, (byte) 3, 14, true)),
        DEVICE_NOTIFICATION_DATA_LONG_STRING_ERROR(BandDeviceConstants.MakeResultCode(BandDeviceConstants.Facility.CARGO_SERVICE, (byte) 3, 32, true)),
        DEVICE_FILE_ALREADY_OPEN_ERROR(BandDeviceConstants.MakeResultCode(BandDeviceConstants.Facility.CARGO_SERVICE, (byte) 3, 33, true)),
        BLUETOOTH_NOT_AVAILABLE_ERROR(BandDeviceConstants.MakeResultCode(BandDeviceConstants.Facility.CARGO_SERVICE, (byte) 7, 0, true)),
        BLUETOOTH_NOT_ENABLED_ERROR(BandDeviceConstants.MakeResultCode(BandDeviceConstants.Facility.CARGO_SERVICE, (byte) 7, 1, true)),
        BLUETOOTH_DISCOVERY_TIMEOUT_ERROR(BandDeviceConstants.MakeResultCode(BandDeviceConstants.Facility.CARGO_SERVICE, (byte) 7, 2, true)),
        BLUETOOTH_DISCOVERY_FAILED_ERROR(BandDeviceConstants.MakeResultCode(BandDeviceConstants.Facility.CARGO_SERVICE, (byte) 7, 3, true)),
        SERVICE_CLOUD_DATA_ERROR(BandDeviceConstants.MakeResultCode(BandDeviceConstants.Facility.CARGO_SERVICE, (byte) 4, 0, true)),
        SERVICE_CLOUD_DATA_NOT_AVAILABLE_ERROR(BandDeviceConstants.MakeResultCode(BandDeviceConstants.Facility.CARGO_SERVICE, (byte) 4, 1, true)),
        SERVICE_CLOUD_INVALID_URL_ERROR(BandDeviceConstants.MakeResultCode(BandDeviceConstants.Facility.CARGO_SERVICE, (byte) 4, 2, true)),
        SERVICE_CLOUD_NETWORK_NOT_AVAILABLE_ERROR(BandDeviceConstants.MakeResultCode(BandDeviceConstants.Facility.CARGO_SERVICE, (byte) 4, 3, true)),
        SERVICE_CLOUD_AUTHENTICATION_ERROR(BandDeviceConstants.MakeResultCode(BandDeviceConstants.Facility.CARGO_SERVICE, (byte) 4, 4, true)),
        SERVICE_CLOUD_REQUEST_FAILED_ERROR(BandDeviceConstants.MakeResultCode(BandDeviceConstants.Facility.CARGO_SERVICE, (byte) 4, 5, true)),
        SERVICE_CLOUD_OPERATION_FAILED_ERROR(BandDeviceConstants.MakeResultCode(BandDeviceConstants.Facility.CARGO_SERVICE, (byte) 4, 6, true)),
        SERVICE_CLOUD_DOWNLOAD_REQUIRED_ERROR(BandDeviceConstants.MakeResultCode(BandDeviceConstants.Facility.CARGO_SERVICE, (byte) 4, 7, true)),
        SERVICE_CLOUD_REQUEST_TIMEOUT_ERROR(BandDeviceConstants.MakeResultCode(BandDeviceConstants.Facility.CARGO_SERVICE, (byte) 4, 8, true)),
        SERVICE_COMMAND_ERROR(BandDeviceConstants.MakeResultCode(BandDeviceConstants.Facility.CARGO_SERVICE, (byte) 2, 0, true)),
        SERVICE_COMMAND_RESPONSE_ERROR(BandDeviceConstants.MakeResultCode(BandDeviceConstants.Facility.CARGO_SERVICE, (byte) 2, 1, true)),
        SERVICE_FILE_IO_ERROR(BandDeviceConstants.MakeResultCode(BandDeviceConstants.Facility.CARGO_SERVICE, (byte) 2, 2, true)),
        SERVICE_FILE_CREATION_ERROR(BandDeviceConstants.MakeResultCode(BandDeviceConstants.Facility.CARGO_SERVICE, (byte) 2, 3, true)),
        SERVICE_FILE_DELETION_ERROR(BandDeviceConstants.MakeResultCode(BandDeviceConstants.Facility.CARGO_SERVICE, (byte) 2, 4, true)),
        SERVICE_FILE_NOT_FOUND_ERROR(BandDeviceConstants.MakeResultCode(BandDeviceConstants.Facility.CARGO_SERVICE, (byte) 2, 5, true)),
        SERVICE_SYNC_FAILED_ERROR(BandDeviceConstants.MakeResultCode(BandDeviceConstants.Facility.CARGO_SERVICE, (byte) 2, 6, true)),
        SERVICE_NOT_BOUND_ERROR(BandDeviceConstants.MakeResultCode(BandDeviceConstants.Facility.CARGO_SERVICE, (byte) 2, Constants.FILTER_TYPE_EXERCISES, true)),
        SERVICE_TERMINATED_ERROR(BandDeviceConstants.MakeResultCode(BandDeviceConstants.Facility.CARGO_SERVICE, (byte) 2, 255, true));
        
        private final int mCode;

        Response(int code) {
            this.mCode = code;
        }

        public int getCode() {
            return this.mCode;
        }

        public byte getCategory() {
            return (byte) ((this.mCode >> 8) & 255);
        }

        public int getStatus() {
            return this.mCode & 255;
        }

        public boolean isError() {
            return isErrorCode(getCode());
        }

        public static boolean isErrorCode(int responseCode) {
            return BandDeviceConstants.isResultSevere(responseCode);
        }

        public static Response lookup(int code) {
            Response[] arr$ = values();
            for (Response response : arr$) {
                if (response.getCode() == code) {
                    return response;
                }
            }
            return UNSPECIFIED;
        }

        /* loaded from: classes.dex */
        public static final class Category {
            public static final byte BLUETOOTH = 7;
            public static final byte CLOUD = 4;
            public static final byte DEVICE = 3;
            public static final byte NOTIFICATION = 1;
            public static final byte OPERATION = 6;
            public static final byte SERVICE = 2;
            public static final byte STATUS = 0;
            public static final byte VALIDATION = 5;

            private Category() {
                throw new UnsupportedOperationException();
            }
        }
    }

    BandServiceMessage(int messageId) {
        this.mMessageId = messageId;
    }

    public int getMessageId() {
        return this.mMessageId;
    }

    public boolean isEqual(int messageId) {
        return this.mMessageId == messageId;
    }

    public static boolean isMessageSpecified(BandServiceMessage serviceMessage) {
        return (serviceMessage == null || serviceMessage == NULL_MESSAGE) ? false : true;
    }

    public static BandServiceMessage lookup(int messageId) {
        BandServiceMessage[] arr$ = values();
        for (BandServiceMessage serviceMessage : arr$) {
            if (serviceMessage.mMessageId == messageId) {
                return serviceMessage;
            }
        }
        return null;
    }
}

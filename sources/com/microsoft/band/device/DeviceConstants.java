package com.microsoft.band.device;

import com.microsoft.band.internal.util.StringUtil;
import com.microsoft.band.webtiles.PageLayoutStyle;
import com.microsoft.kapp.utils.Constants;
/* loaded from: classes.dex */
public final class DeviceConstants {
    public static final int BADGE_ICON_INDEX = 1;
    public static final int BIKE_SPLIT_MAX_MULTIPLIER = 255;
    public static final int BIKE_SPLIT_MIN_MULTIPLIER = 1;
    public static final int BYTES_IN_CHAR = 2;
    public static final int CARGO_VERSION_APP_COUNT = 3;
    public static final int ERROR_PAGE_LAYOUT_INDEX = 0;
    public static final int EXPECTED_MANIFEST_VERSION = 1;
    public static final String GUID_CARGO_BLUETOOTH_PUSH_PROTOCOL = "C742E1A2-6320-5ABC-9643-D206C677E580";
    public static final String GUID_ID_CALENDAR = "ec149021-ce45-40e9-aeee-08f86e4746a7";
    public static final String GUID_ID_CALL = "22b1c099-f2be-4bac-8ed8-2d6b0b3c25d1";
    public static final String GUID_ID_FINANCE = "5992928a-bd79-4bb5-9678-f08246d03e68";
    public static final String GUID_ID_GOLF = "fb9d005a-c3da-49d4-8e7b-c6f674fc4710";
    public static final String GUID_ID_RUN = "65bd93db-4293-46af-9a28-bdd6513b4677";
    public static final String GUID_ID_SLEEP = "23e7bc94-f90d-44e0-843f-250910fdf74e";
    public static final String GUID_ID_SMS = "b4edbc35-027b-4d10-a797-1099cd2ad98a";
    public static final String GUID_ID_TIMER = "d36a92ea-3e85-4aed-a726-2898a6f2769b";
    public static final String GUID_ID_WEATHER = "69a39b4e-084b-4b53-9a1b-581826df9e36";
    public static final String GUID_ID_WEBTILE_ERROR_PAGE = "A5EECE73-4969-45D1-A863-CFA76DC485FF";
    public static final String GUID_ID_WEBTILE_PAGE_PREFIX = "A5EECE73-4969-45D1-A863-CFA76DC485";
    public static final String GUID_ID_WORKOUT = "a708f02a-03cd-4da0-bb33-be904e6a2924";
    public static final String GUID_ZERO = "00000000-0000-0000-0000-000000000000";
    public static final byte ID_STRING_DESCRIPTOR_TYPE = 3;
    public static final short LAYOUT_VERSION_FLAG = 0;
    public static final short LAYOUT_VERSION_MAJOR = 1;
    public static final short LAYOUT_VERSION_MINOR = 0;
    public static final int LOG_MAX_CHUNK = 12288;
    public static final long LOG_TRANSFER_CHUNK_SIZE = 4096;
    public static final int LOG_TRANSFER_MAX_CHUNKS = 128;
    public static final int MAX_ALLOCATED_STRAPP = 15;
    public static final int MAX_AUTHOR_LENGTH = 50;
    public static final int MAX_BIN_LAYOUT_BLOB_SIZE_BYTES = 768;
    public static final int MAX_DESCRIPTION_LENGTH = 100;
    public static final int MAX_EMAIL_LENGTH = 100;
    public static final int MAX_INSTALLED_STRAPP = 13;
    public static final int MAX_ORGANIZATION_LENGTH = 100;
    public static final int MAX_REFRESH_INTERVAL = 180;
    public static final int MAX_VERSION_LENGTH = 10;
    public static final int MD5_HASH_LENGTH = 16;
    public static final long ME_TILE_ID = 4294967295L;
    public static final long ME_TILE_ZERO_ID = 0;
    public static final int MIN_REFRESH_INTERVAL = 15;
    public static final int NOTIFICATION_CALENDAR_LONGSTRING_MAX_LENGTH = 320;
    public static final int NOTIFICATION_CALENDAR_LONGSTRING_MAX_LENGTH_IN_CHAR = 160;
    public static final int NOTIFICATION_CALENDAR_SHORTSTRING_MAX_LENGTH = 40;
    public static final int NOTIFICATION_CALENDAR_SHORTSTRING_MAX_LENGTH_IN_CHAR = 20;
    public static final int NOTIFICATION_CALL_NAME_MAX_LENGTH = 80;
    public static final int NOTIFICATION_CALL_NAME_MAX_LENGTH_IN_CHAR = 40;
    public static final int NOTIFICATION_CALL_NUMBER_MAX_LENGTH = 40;
    public static final int NOTIFICATION_CALL_NUMBER_MAX_LENGTH_IN_CHAR = 20;
    public static final int NOTIFICATION_EMAIL_NAME_MAX_LENGTH = 80;
    public static final int NOTIFICATION_EMAIL_SUBJECT_MAX_LENGTH = 72;
    public static final int NOTIFICATION_FINANCE_DIALOG_MESSAGE_MAX_LENGTH = 100;
    public static final int NOTIFICATION_FINANCE_DIALOG_MESSAGE_MAX_LENGTH_IN_CHAR = 50;
    public static final int NOTIFICATION_FINANCE_UPDATE_STOCKNAME_MAX_LENGTH = 10;
    public static final int NOTIFICATION_FINANCE_UPDATE_STOCKNAME_MAX_LENGTH_IN_CHAR = 5;
    public static final int NOTIFICATION_GENERIC_DIALOG_LINE_ONE_MAX_LENGTH = 40;
    public static final int NOTIFICATION_GENERIC_DIALOG_LINE_ONE_MAX_LENGTH_IN_CHAR = 20;
    public static final int NOTIFICATION_GENERIC_DIALOG_LINE_TWO_MAX_LENGTH = 40;
    public static final int NOTIFICATION_GENERIC_DIALOG_LINE_TWO_MAX_LENGTH_IN_CHAR = 20;
    public static final int NOTIFICATION_GENERIC_UPDATE_LINE_MAX_LENGTH = 390;
    public static final int NOTIFICATION_GENERIC_UPDATE_LINE_MAX_LENGTH_IN_CHAR = 195;
    public static final int NOTIFICATION_INSIGHT2_MAX_LENGTH = 320;
    public static final int NOTIFICATION_INSIGHT2_MAX_LENGTH_IN_CHAR = 160;
    public static final int NOTIFICATION_INSIGHT_F1_MAX_LENGTH = 80;
    public static final int NOTIFICATION_INSIGHT_F1_MAX_LENGTH_IN_CHAR = 40;
    public static final int NOTIFICATION_INSIGHT_F2_MAX_LENGTH = 320;
    public static final int NOTIFICATION_INSIGHT_F2_MAX_LENGTH_IN_CHAR = 160;
    public static final int NOTIFICATION_MAX_CALENDAR_EVENT_IN_TRANS = 8;
    public static final int NOTIFICATION_MAX_SMS_IN_TRANS = 10;
    public static final int NOTIFICATION_MESSAGING_BODY_MAX_LENGTH = 320;
    public static final int NOTIFICATION_MESSAGING_BODY_MAX_LENGTH_IN_CHAR = 160;
    public static final int NOTIFICATION_MESSAGING_TITLE_MAX_LENGTH = 80;
    public static final int NOTIFICATION_MESSAGING_TITLE_MAX_LENGTH_IN_CHAR = 40;
    public static final int NOTIFICATION_MOTHER_STRUCT_SIZE = 18;
    public static final int NOTIFICATION_SMS_BODY_MAX_LENGTH = 320;
    public static final int NOTIFICATION_SMS_BODY_MAX_LENGTH_IN_CHAR = 160;
    public static final int NOTIFICATION_SMS_MMS_IMAGE = 1;
    public static final int NOTIFICATION_SMS_MMS_NONE = 0;
    public static final int NOTIFICATION_SMS_MMS_UNKNOWN = 4;
    public static final int NOTIFICATION_SMS_MMS_VIDEO = 2;
    public static final int NOTIFICATION_SMS_NAME_MAX_LENGTH = 80;
    public static final int NOTIFICATION_SMS_NAME_MAX_LENGTH_IN_CHAR = 40;
    public static final int NOTIFICATION_WEATHER_MESSAGE_MAX_LENGTH = 100;
    public static final int NOTIFICATION_WEATHER_MESSAGE_MAX_LENGTH_IN_CHAR = 50;
    public static final int NOTIFICATION_WEATHER_TIME_MAX_LENGTH = 10;
    public static final int NOTIFICATION_WEATHER_TIME_MAX_LENGTH_IN_CHAR = 5;
    public static final int NOTIFICATION_WEATHER_TIME_OF_DAY_MAX_LENGTH = 20;
    public static final int NOTIFICATION_WEATHER_TIME_OF_DAY_MAX_LENGTH_IN_CHAR = 10;
    public static final int NUMBER_LAYOUTS_ALLOWED = 4;
    public static final int NUMBER_OF_TILE_THEMES = 6;
    public static final int NUMBER_PAGES_ALLOWED = 7;
    public static final int PEG_SCREEN_HEIGHT = 106;
    public static final int PEG_SCREEN_WIDTH = 320;
    public static final int PROFILE_DEVICE_NAME_LENGTH_IN_CHAR = 15;
    public static final int REMOTE_SUBSCRIPTION_BITSET_BYTE_SIZE_CARGO = 7;
    public static final int REMOTE_SUBSCRIPTION_BITSET_BYTE_SIZE_ENVOY = 16;
    public static final int STRAPP_NAME_MAX_LENGTH = 60;
    public static final int STRAPP_NAME_MAX_LENGTH_IN_CHAR = 30;
    public static final int TILE_ICON_INDEX = 0;
    public static final int WEB_TILE_PAGE_TEXT_LINE_MAX_LENGTH_IN_CHAR = 20;
    public static final byte[] WEB_TILE_HASH = StringUtil.toMD5Hash("com.microsoft.band.webtile");
    public static final PageLayoutStyle ERROR_PAGE_LAYOUT = PageLayoutStyle.SCROLLING_TEXT;

    private DeviceConstants() {
        throw new UnsupportedOperationException();
    }

    /* loaded from: classes.dex */
    public enum FileIndex {
        CONFIG(1),
        EPHEMERIS(2),
        UNITTEST(3),
        PROFILE(4),
        SYSTEM_SETTINGS(5),
        LINK_KEYS(6),
        INSTRUMENTATION(12),
        TIMEZONES(53),
        WORKOUT_PLAN(71),
        CRASH_DUMP(72);
        
        private int mIndex;

        FileIndex(int index) {
            this.mIndex = index;
        }

        public int getIndex() {
            return this.mIndex;
        }
    }

    /* loaded from: classes.dex */
    public enum NotificationID {
        SMS(1, 8),
        EMAIL(2, 8),
        INSIGHT(3, 1),
        INSIGHT2(4, 1),
        FINANCE_UPDATE(5),
        FINANCE_DIALOG(6),
        WEATHER_UPDATE(7),
        WEATHER_DIALOG(8),
        FINANCE_CLEAR(9),
        WEATHER_CLEAR(10),
        INCOMING_CALL(11),
        ANSWERED_CALL(12),
        MISSED_CALL(13, 8),
        HANGUP_CALL(14),
        VOICEMAIL(15, 8),
        CALENDAR_EVENT_ADD(16),
        CALENDAR_CLEAR(17),
        MESSAGING(18, 8),
        GENERIC_DIALOG(100),
        GENERIC_UPDATE(101),
        GENERIC_CLEAR_STRAPP(102),
        GENERIC_CLEAR_PAGE(Constants.SLEEP_FRAGMENT_EVENT_DETAILS_REQUEST);
        
        private final int mId;
        private final int mQueueLimit;

        NotificationID(int in) {
            this.mId = in;
            this.mQueueLimit = 0;
        }

        NotificationID(int id, int queueLimit) {
            this.mId = id;
            this.mQueueLimit = queueLimit;
        }

        public int getId() {
            return this.mId;
        }

        public int getQueueLimit() {
            return this.mQueueLimit;
        }
    }

    /* loaded from: classes.dex */
    public enum NotificationFlag {
        UNMODIFIED_SETTING((byte) 0),
        FORCE_GENERIC_DIALOG((byte) 1),
        SUPPRESS_NOTIFICATION_DIALOG((byte) 2),
        SUPPRESS_SMS_REPLY((byte) 4);
        
        private final byte mFlag;

        NotificationFlag(byte in) {
            this.mFlag = in;
        }

        public byte getFlag() {
            return this.mFlag;
        }
    }

    /* loaded from: classes.dex */
    public enum AppRunning {
        APP_RUNNING_1BL(1),
        APP_RUNNING_2UP(2),
        APP_RUNNING_APP(3),
        APP_RUNNING_UPAPP(4),
        APP_RUNNING_INVALID(0);
        
        private final int mAppID;

        AppRunning(int appID) {
            this.mAppID = appID;
        }

        public int getAppID() {
            return this.mAppID;
        }

        public int getFirmwareVersionIndex() {
            return this.mAppID - 1;
        }

        public static AppRunning lookup(int appID) {
            AppRunning app = APP_RUNNING_INVALID;
            AppRunning[] arr$ = values();
            for (AppRunning entry : arr$) {
                if (entry.getAppID() == appID) {
                    return entry;
                }
            }
            return app;
        }
    }
}

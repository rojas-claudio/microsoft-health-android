package com.microsoft.kapp.diagnostics;

import android.net.Uri;
import java.util.List;
/* loaded from: classes.dex */
public class TelemetryConstants {

    /* loaded from: classes.dex */
    public static class Events {
        public static final String BIKE_DATA_POINTS_CHANGE = "Fitness/Settings/Band/Manage Tiles/Edit Bike Data Points";
        public static final String CONNECTED_SETTINGS_BAND_MANAGE_TILES_STARBUCKS_SEND_CARD_TO_BAND = "Connected/Settings/Band/Manage Tiles/Starbucks/Send card to band";
        public static final String OOBE_NO_BAND_CLICKED = "Events/OOBE/Instruction/No band clicked";
        public static final String OOBE_NO_PHONE_MARKETING_UPSELL = "Events/OOBE/Instruction/Learn more clicked";
        public static final String RUN_DATA_POINTS_CHANGE = "Fitness/Settings/Band/Manage Tiles/Edit Run Data Points";

        /* loaded from: classes.dex */
        public static class Error {
            public static final String APP_CRASHED_EVENT_NAME = "Errors/Crash";

            /* loaded from: classes.dex */
            public static class Dimensions {
                public static final String APP_VERSION = "Application Version";
                public static final String BRAND = "Brand";
                public static final String EXTRA = "Extra";
                public static final String LOG_MESSAGE = "Log Message";
                public static final String LOG_TAG = "Log Tag";
                public static final String LOG_TYPE = "Log Type";
                public static final String LOG_TYPE_ERROR = "Error";
                public static final String MODEL = "Model";
                public static final String OS_VERSION = "OS Version";
                public static final String STACK_TRACE = "Stack Trace";
                public static final String USER_ID = "User Id";
            }
        }

        /* loaded from: classes.dex */
        public static class FtuClick {
            public static final String EVENT_NAME = "Pages/Home/FTU Learn more";
            public static final String GOLF_EVENT_NAME = "Pages/Home/FTU/Find a Golf Course";

            /* loaded from: classes.dex */
            public static class Dimensions {
                public static final String PAGE = "Page";
            }
        }

        /* loaded from: classes.dex */
        public static class Golf {
            public static final String GOLF_COURSE_ID = "Course ID";
            public static final String GOLF_TEE_ID = "Tee ID";
        }

        /* loaded from: classes.dex */
        public static class GolfWatchVideo {
            public static final String EVENT_NAME = "Fitness/Golf/Intro Video";
        }

        /* loaded from: classes.dex */
        public static class GuidedWorkoutFavorite {
            public static final String EVENT_NAME = "Fitness/Guided Workouts/Favorite";

            /* loaded from: classes.dex */
            public static class Dimensions {
                public static final String IS_FAVORITE = "Is Favorite";
                public static final String WORKOUT_PLAN_ID = "Workout Plan ID";
            }
        }

        /* loaded from: classes.dex */
        public static class GuidedWorkoutFilterResults {
            public static final String EVENT_NAME = "Fitness/Guided Workouts/Filter Plan Results";

            /* loaded from: classes.dex */
            public static class Dimensions {
                public static final String BRAND_CATEGORY_NAME = "brandcategory";
                public static final String COUNT = "Count";
                public static final String LEVEL = "level";
            }
        }

        /* loaded from: classes.dex */
        public static class GuidedWorkoutSubscribe {
            public static final String EVENT_NAME = "Fitness/Guided Workouts/Subscribe";

            /* loaded from: classes.dex */
            public static class Dimensions {
                public static final String IS_SUBSCRIBED = "Is Subscribed";
                public static final String WORKOUT_PLAN_ID = "Workout Plan ID";
            }
        }

        /* loaded from: classes.dex */
        public static class GuidedWorkoutSync {
            public static final String EVENT_NAME = "Fitness/Guided Workouts/Sync";

            /* loaded from: classes.dex */
            public static class Dimensions {
                public static final String IS_FAVORITE = "Is Favorite";
                public static final String WORKOUT_PLAN_ID = "Workout Plan ID";
            }
        }

        /* loaded from: classes.dex */
        public static class GuidedWorkoutWatchVideo {
            public static final String EVENT_NAME = "Fitness/Guided Workouts/Watch Video";

            /* loaded from: classes.dex */
            public static class Dimensions {
                public static final String EXERCISE_ID = "Exercise ID";
                public static final String EXERCISE_NUMBER = "Exercise Number";
                public static final String VIDEO_ID = "Video ID";
            }
        }

        /* loaded from: classes.dex */
        public static class LogHomeTileTap {
            public static final String EVENT_NAME = "Fitness/Home Tile Tap";

            /* loaded from: classes.dex */
            public static class Dimensions {
                public static final String ACTION = "Action";
                public static final String ACTION_CLOSE = "Close";
                public static final String ACTION_OPEN = "Open";
                public static final String FIRST_TIME_USE = "First Time Use";
                public static final String FTU_VALUE_NO = "No";
                public static final String FTU_VALUE_YES = "Yes";
                public static final String TILE_NAME = "Tile Name";
                public static final String TILE_NAME_ADD_A_BAND = "Add A Band";
                public static final String TILE_NAME_BIKE = "Bike";
                public static final String TILE_NAME_CALORIES = "Calories";
                public static final String TILE_NAME_GOLF = "Golf";
                public static final String TILE_NAME_GOLF_MINI = "Golf Mini Tile";
                public static final String TILE_NAME_GW = "Guided Workouts";
                public static final String TILE_NAME_GW_CALENDAR = "Calendar";
                public static final String TILE_NAME_MANAGE_TILES = "Manage Tiles";
                public static final String TILE_NAME_RUN = "Run";
                public static final String TILE_NAME_SLEEP = "Sleep";
                public static final String TILE_NAME_STEPS = "Steps";
                public static final String TILE_NAME_WORKOUT = "Workout";
            }
        }

        /* loaded from: classes.dex */
        public static class OobeComplete {
            public static final String EVENT_NAME = "OOBE/Complete";

            /* loaded from: classes.dex */
            public static class Dimensions {
                public static final String AGE_GROUP = "Age Group";
                public static final String GENDER = "Gender";
                public static final String GENDER_FEMALE = "Female";
                public static final String GENDER_MALE = "Male";
                public static final String MOTION_TRACKING = "Motion Tracking";
                public static final String MOTION_TRACKING_DISABLED = "Disabled";
                public static final String MOTION_TRACKING_ENABLED = "Enabled";
                public static final String MOTION_TRACKING_UNSUPPORTED = "Not Supported";
                public static final String PAIRED_BAND = "Paired Band";
                public static final String PAIRED_BAND_NO = "No";
                public static final String PAIRED_BAND_YES = "Yes";
            }
        }

        /* loaded from: classes.dex */
        public static class OobeHavingTroubleClicked {
            public static final String EVENT_NAME = "OOBE/Bluetooth Pairing/Having Trouble Clicked";
        }

        /* loaded from: classes.dex */
        public static class Phone {
            public static final String ENABLE_MOTION_TRACKING_EVENT_NAME = "Phone/Enable motion tracking";

            /* loaded from: classes.dex */
            public static class Dimensions {
                public static final String TRACKING_STATE = "Tracking State";
                public static final String TRACKING_STATE_DISABLED = "Disabled";
                public static final String TRACKING_STATE_ENABLED = "Enabled";
            }
        }

        /* loaded from: classes.dex */
        public static class RenameBand {
            public static final String EVENT_NAME = "Personalize/Rename your band";

            /* loaded from: classes.dex */
            public static class Dimensions {
                public static final String IN_OOBE = "In OOBE";
            }
        }

        /* loaded from: classes.dex */
        public static class SendFeedbackComplete {
            public static final String EVENT_NAME = "Feedback/Complete";

            /* loaded from: classes.dex */
            public static class Dimensions {
                public static final String DESCRIPTION = "Description";
                public static final String IMAGES = "Images";
                public static final String LOGS = "Logs";
                public static final String NO = "No";
                public static final String YES = "Yes";
            }
        }

        /* loaded from: classes.dex */
        public static class SendFeedbackFailure {
            public static final String EVENT_NAME = "Feedback/SendFailure";
        }

        /* loaded from: classes.dex */
        public static class SendFeedbackSelection {
            public static final String EVENT_NAME = "Feedback/Selection";

            /* loaded from: classes.dex */
            public static class Dimensions {
                public static final String BUG_REPORT = "Bugreport";
                public static final String HELP = "Help";
                public static final String SELECTION = "Selection";
                public static final String USERVOICE = "UserVoice";
            }
        }

        /* loaded from: classes.dex */
        public static class ShakeDialog {
            public static final String EVENT_NAME = "Feedback/ShakeDialog";

            /* loaded from: classes.dex */
            public static class Dimensions {
                public static final String CANCEL = "Cancel";
                public static final String DEBUG = "Debug";
                public static final String SELECTION = "Selection";
                public static final String SEND_FEEDBACK = "Send Feedback";
            }
        }

        /* loaded from: classes.dex */
        public static class ShakeDialogPreferences {
            public static final String EVENT_NAME = "Settings/User/Feedback";

            /* loaded from: classes.dex */
            public static class Dimensions {
                public static final String OFF = "Off";
                public static final String ON = "On";
                public static final String VALUE = "Value";
            }
        }

        /* loaded from: classes.dex */
        public static class Share {
            public static final String EVENT_NAME = "Fitness/Share";

            /* loaded from: classes.dex */
            public static class Dimensions {
                public static final String EVENT_TYPE = "Event Type";
            }
        }

        /* loaded from: classes.dex */
        public static class SleepEvent {
            public static final String FITNESS_SLEEP_AUTO_DETECT = "Fitness/Sleep/Summary/AutoDetect";

            /* loaded from: classes.dex */
            public static final class AutoDetectDelete {
                public static final String FITNESS_SLEEP_AUTO_DETECT_DELETE = "Fitness/Sleep/Summary/AutoDetect/Delete";
            }

            /* loaded from: classes.dex */
            public static final class AutoDetectReport {
                public static final String FITNESS_SLEEP_AUTO_DETECT_REPORT = "Fitness/Sleep/Summary/AutoDetect/Report";

                /* loaded from: classes.dex */
                public static final class Dimensions {
                    public static final String SLEEP_ID = "ID2";
                    public static final String USER_ID = "ID1";
                }
            }
        }

        /* loaded from: classes.dex */
        public static class StockWatchListChange {
            public static final String EVENT_NAME = "Connected/Settings/Band/Manage Tiles/Edit Watch List";

            /* loaded from: classes.dex */
            public static class Dimensions {
                public static final String NUMBER_OF_SYMBOLS = "Number of Symbols";
            }
        }

        /* loaded from: classes.dex */
        public static class ThemeChange {
            public static final String EVENT_NAME = "Personalize/Theme";

            /* loaded from: classes.dex */
            public static class Dimensions {
                public static final String IN_OOBE = "In OOBE";
                public static final String THEME_COLOR_NAME = "Theme Color Name";
                public static final String THEME_WALLPAPER_NAME = "Theme Wallpaper Name";
            }
        }

        /* loaded from: classes.dex */
        public static class TilesChange {
            public static final String EVENT_NAME = "Settings/Band/Manage Tiles";

            /* loaded from: classes.dex */
            public static class Dimensions {
                public static final String ACTIVE_TILES = "Active Tiles";
                public static final String CHANGED_ORDER = "Changed Order";
                public static final String TILE_ENABLED = "True";
                public static final String TILE_GOLF = "Golf";
            }
        }

        /* loaded from: classes.dex */
        public static class WebTileInstallationFailure {
            public static final String EVENT_NAME = "WebTile/Installation/Failure";

            /* loaded from: classes.dex */
            public static class Dimensions {
                public static final String FAILURE_REASON = "WebTile Install Failure Reason";
                public static final String NAME = "WebTile Name";
                public static final String SOURCE = "WebTile Source";
            }
        }

        /* loaded from: classes.dex */
        public static class WebTileInstallationSuccess {
            public static final String EVENT_NAME = "WebTile/Installation/Success";

            /* loaded from: classes.dex */
            public static class Dimensions {
                public static final String NAME = "WebTile Name";
                public static final String SOURCE = "WebTile Source";
            }
        }

        /* loaded from: classes.dex */
        public static class WebTileSync {
            public static final String EVENT_NAME = "WebTile/Sync";

            /* loaded from: classes.dex */
            public static class Dimensions {
                public static final String DOWNLOADED_BYTES = "WebTile Sync Downloaded Bytes";
                public static final String NAME = "WebTile Name";
                public static final String TILE_UPDATED = "WebTile Sync Tile Updated";
            }
        }

        /* loaded from: classes.dex */
        public static class WhatsNew {
            public static final String EVENT_NAME = "Event/Settings/What's new";
            public static final String OPEN = "Event/Settings/What's new/Open";
            public static final String SESSIONS = "Event/Settings/What's new/Sessions";

            /* loaded from: classes.dex */
            public static class Cards {
                public static final String CARDS = "Event/Settings/What's new/Cards";

                /* loaded from: classes.dex */
                public static class Dimensions {
                    public static final String GOLF = "Golf";
                    public static final String GUIDED_WORKOUT = "Guided workout";
                    public static final String MULTI_DEVICE = "Multi device";
                    public static final String SEND_FEEDBACK = "Send feedback";
                    public static final String WEB_DASHBOARD = "Web dashboard";
                }
            }

            /* loaded from: classes.dex */
            public static class LearnMore {
                public static final String LEARN_MORE = "Event/Settings/What's new/Learn more";

                /* loaded from: classes.dex */
                public static class Dimensions {
                    public static final String GOLF = "Golf";
                    public static final String GUIDED_WORKOUT = "Guided workout";
                    public static final String MULTI_DEVICE = "Multi device";
                }
            }
        }
    }

    /* loaded from: classes.dex */
    public static class PageViews {
        public static final String FITNESS_BIKE_EXPANDED_MAP = "Fitness/Bike/Expanded Map";
        public static final String FITNESS_BIKE_SPLITS = "Fitness/Bike/Splits";
        public static final String FITNESS_BIKE_SUMMARY = "Fitness/Bike/Summary Map";
        public static final String FITNESS_CALORIES_DAILY = "Fitness/Calories/Daily";
        public static final String FITNESS_CALORIES_EDIT_GOAL = "Fitness/Calories/Edit Goal";
        public static final String FITNESS_CALORIES_WEEKLY = "Fitness/Calories/Weekly";
        public static final String FITNESS_EXERCISE_SUMMARY = "Fitness/Exercise/Summary";
        public static final String FITNESS_GOLF_CONNECT = "Fitness/Golf/Connect";
        public static final String FITNESS_GOLF_COURSE_DEATILS = "Fitness/Golf/Browse/Course";
        public static final String FITNESS_GOLF_COURSE_PRO_TIP = "Fitness/Golf/Course/ProTip";
        public static final String FITNESS_GOLF_COURSE_TEE_PICK = "Fitness/Golf/Course/TeePick";
        public static final String FITNESS_GOLF_INTRO_VIDEO = "Fitness/Golf/Intro Video";
        public static final String FITNESS_GOLF_LANDING_PAGE = "Fitness/Golf/Connect/Summary";
        public static final String FITNESS_GOLF_NEARBY_COURSES = "Fitness/Golf/FindCourse";
        public static final String FITNESS_GOLF_RECENT_SEARCH_RESULTS = "Fitness/Golf/FindCourse";
        public static final String FITNESS_GOLF_SCORECARD = "Pages/Golf Scorecard";
        public static final String FITNESS_GOLF_SEARCH_RESULTS = "Fitness/Golf/FindCourse";
        public static final String FITNESS_GOLF_SUMMARY = "Pages/Golf Summary";
        public static final String FITNESS_GOLF_SYNC = "Fitness/Golf/Sync";
        public static final String FITNESS_GUIDED_WORKOUTS_BROWSE_FAVORITES = "Fitness/Guided Workouts/Browse/Favorites";
        public static final String FITNESS_GUIDED_WORKOUTS_BROWSE_FITNESS_GOAL = "Fitness/Guided Workouts/Browse/Fitness Goal";
        public static final String FITNESS_GUIDED_WORKOUTS_BROWSE_FITNESS_PROS = "Fitness/Guided Workouts/Browse/Fitness Pros";
        public static final String FITNESS_GUIDED_WORKOUTS_BROWSE_MYWORKOUTS = "Fitness/Guided Workouts/Browse/MyWorkouts";
        public static final String FITNESS_GUIDED_WORKOUTS_DETAILS = "Fitness/Guided Workouts/Completed Workout/Reps";
        public static final String FITNESS_GUIDED_WORKOUTS_FILTERS_SUMMARY = "Fitness/Guided Workouts/Filters Summary";
        public static final String FITNESS_GUIDED_WORKOUTS_FIND_A_WORKOUT = "Fitness/Guided Workouts/Find a Workout";
        public static final String FITNESS_GUIDED_WORKOUTS_PLAN_SCHEDULE = "Fitness/Guided Workouts/Plan/Schedule";
        public static final String FITNESS_GUIDED_WORKOUTS_PLAN_SUMMARY = "Fitness/Guided Workouts/Plan/Summary";
        public static final String FITNESS_GUIDED_WORKOUTS_PLAN_WORKOUT_DETAIL = "Fitness/Guided Workouts/Plan/Workout Detail";
        public static final String FITNESS_GUIDED_WORKOUTS_RESULTS = "Fitness/Guided Workouts/Results";
        public static final String FITNESS_GUIDED_WORKOUTS_SEARCH = "Fitness/Guided Workouts/Search";
        public static final String FITNESS_GUIDED_WORKOUTS_SUMMARY = "Fitness/Guided Workouts/Completed Workout/Summary";
        public static final String FITNESS_HISTORY_ALL = "Fitness/History/All";
        public static final String FITNESS_HISTORY_BESTS = "Fitness/History/Bests";
        public static final String FITNESS_HISTORY_BIKES = "Fitness/History/Bikes";
        public static final String FITNESS_HISTORY_EXERCISES = "Fitness/History/Exercises";
        public static final String FITNESS_HISTORY_FILTERS_SUMMARY = "Fitness/History/Filters Summary";
        public static final String FITNESS_HISTORY_GOLF = "Fitness/History/Golf";
        public static final String FITNESS_HISTORY_GUIDED_WORKOUTS = "Fitness/History/Guided Workouts";
        public static final String FITNESS_HISTORY_RUNS = "Fitness/History/Runs";
        public static final String FITNESS_HISTORY_SLEEP = "Fitness/History/Sleep";
        public static final String FITNESS_RUN_EXPANDED_MAP = "Fitness/Run/Expanded Map";
        public static final String FITNESS_RUN_SPLITS = "Fitness/Run/Splits";
        public static final String FITNESS_RUN_SUMMARY = "Fitness/Run/Summary Map";
        public static final String FITNESS_SLEEP_SUMMARY = "Fitness/Sleep/Summary";
        public static final String FITNESS_STEPS_DAILY = "Fitness/Steps/Daily";
        public static final String FITNESS_STEPS_EDIT_GOAL = "Fitness/Steps/Edit Goal";
        public static final String FITNESS_STEPS_WEEKLY = "Fitness/Steps/Weekly";
        public static final String HOME = "Home";
        public static final String HOME_FTU = "Phone/Motion tracking";
        public static final String NAVIGATION_LEFT_NAVIGATION = "Navigation/Left Navigation";
        public static final String NAVIGATION_RIGHT_NAVIGATION = "Navigation/Right Navigation";
        public static final String OOBE_BLUETOOTH_PAIRING = "OOBE/Bluetooth Pairing/Pairing";
        public static final String OOBE_BLUETOOTH_PAIRING_SUCCESS = "OOBE/Bluetooth Pairing/Success";
        public static final String OOBE_FINISH = "OOBE/Finish";
        public static final String OOBE_FIRMWARE_UPDATE = "OOBE/Firmware Update";
        public static final String OOBE_FIRMWARE_UPDATE_APPLYING_UPDATE = "OOBE/Firmware Update/Apply Update";
        public static final String OOBE_FIRMWARE_UPDATE_COMPLETE = "OOBE/Firmware Update/Firmware Update Complete";
        public static final String OOBE_FIRMWARE_UPDATE_DOWNLOADING = "OOBE/Firmware Update/Firmware Downloading";
        public static final String OOBE_FIRMWARE_UPDATE_REBOOTING = "OOBE/Firmware Update/Reboot band";
        public static final String OOBE_FIRMWARE_UPDATE_SENDING_TO_BAND = "OOBE/Firmware Update/Send to band";
        public static final String OOBE_FIRMWARE_UPDATE_UPDATING = "OOBE/Firmware Update/Firmware Updating";
        public static final String OOBE_LOGIN_AUTHENTICATE = "OOBE/Login/Authenticate";
        public static final String OOBE_LOGIN_MARKETING = "OOBE/Login/Marketing";
        public static final String OOBE_LOGIN_MSA = "OOBE/Login/MSA";
        public static final String OOBE_PERSONALIZE_COLOR_CHOOSER = "OOBE/Personalize/Color Chooser";
        public static final String OOBE_PERSONALIZE_NAME_YOUR_BAND = "OOBE/Personalize/Name Your Band";
        public static final String OOBE_PERSONALIZE_THEME_CHOOSER = "OOBE/Personalize/Theme Chooser";
        public static final String OOBE_PERSONALIZE_WALLPAPER_CHOOSER = "OOBE/Personalize/Wallpaper Chooser";
        public static final String OOBE_PROFILE_EDITING = "OOBE/Profile Editing";
        public static final String SEND_FEEDBACK_DESCRIPTION = "Feedback/Description";
        public static final String SEND_FEEDBACK_LAUNCH = "Feedback/Launch";
        public static final String SEND_FEEDBACK_SELECTION = "Feedback/Selection";
        public static final String SEND_FEEDBACK_SUMMARY = "Feedback/Summary";
        public static final String SETTINGS_BAND_MANAGE_TILES = "Settings/Band/Manage Tiles";
        public static final String SETTINGS_BAND_MANAGE_TILES_CONNECTED_EDIT_CUSTOM_RESPONSES_CALLS = "Settings/Band/Manage Tiles/Connected/Edit Custom Responses/Calls";
        public static final String SETTINGS_BAND_MANAGE_TILES_CONNECTED_EDIT_CUSTOM_RESPONSES_SMS = "Settings/Band/Manage Tiles/Connected/Edit Custom Responses/SMS";
        public static final String SETTINGS_BAND_MANAGE_TILES_CONNECTED_NOTIFICATIONS_CALENDAR = "Settings/Band/Manage Tiles/Connected/Notifications/Calendar";
        public static final String SETTINGS_BAND_MANAGE_TILES_CONNECTED_NOTIFICATIONS_CALLS = "Settings/Band/Manage Tiles/Connected/Notifications/Calls";
        public static final String SETTINGS_BAND_MANAGE_TILES_CONNECTED_NOTIFICATIONS_EMAIL = "Settings/Band/Manage Tiles/Connected/Notifications/Email";
        public static final String SETTINGS_BAND_MANAGE_TILES_CONNECTED_NOTIFICATIONS_FACEBOOK = "Settings/Band/Manage Tiles/Connected/Notifications/Facebook";
        public static final String SETTINGS_BAND_MANAGE_TILES_CONNECTED_NOTIFICATIONS_FACEBOOK_MESSENGER = "Settings/Band/Manage Tiles/Connected/Notifications/Facebook Messenger";
        public static final String SETTINGS_BAND_MANAGE_TILES_CONNECTED_NOTIFICATIONS_NOTIFICATION_CENTER = "Settings/Band/Manage Tiles/Connected/Notifications/Notification Center";
        public static final String SETTINGS_BAND_MANAGE_TILES_CONNECTED_NOTIFICATIONS_NOTIFICATION_CENTER_APPS = "Settings/Band/Manage Tiles/Connected/Notifications/Notification Center Apps";
        public static final String SETTINGS_BAND_MANAGE_TILES_CONNECTED_NOTIFICATIONS_SMS = "Settings/Band/Manage Tiles/Connected/Notifications/SMS";
        public static final String SETTINGS_BAND_MANAGE_TILES_CONNECTED_NOTIFICATIONS_TWITTER = "Settings/Band/Manage Tiles/Connected/Notifications/Twitter";
        public static final String SETTINGS_BAND_MANAGE_TILES_CONNECTED_STARBUCKS_ADD_CARD = "Settings/Band/Manage Tiles/Connected/Starbucks/Add Card";
        public static final String SETTINGS_BAND_MANAGE_TILES_CONNECTED_STARBUCKS_OVERVIEW = "Settings/Band/Manage Tiles/Connected/Starbucks/Overview";
        public static final String SETTINGS_BAND_MANAGE_TILES_CONNECTED_STOCKS = "Settings/Band/Manage Tiles/Connected/Stocks";
        public static final String SETTINGS_BAND_MANAGE_TILES_CONNECTED_STOCKS_SEARCH = "Settings/Band/Manage Tiles/Connected/Stocks/Search";
        public static final String SETTINGS_BAND_MANAGE_TILES_CONNECTED_STOCKS_WATCHLIST = "Settings/Band/Manage Tiles/Connected/Stocks/Watchlist";
        public static final String SETTINGS_BAND_MANAGE_TILES_FITNESS_BIKE = "Settings/Band/Manage Tiles/Fitness/Bike";
        public static final String SETTINGS_BAND_MANAGE_TILES_FITNESS_BIKE_ADVANCED_SETTINGS = "Settings/Band/Manage Tiles/Fitness/Bike Customize";
        public static final String SETTINGS_BAND_MANAGE_TILES_FITNESS_BIKE_DATA_POINTS = "Settings/Band/Manage Tiles/Fitness/Bike Data Points";
        public static final String SETTINGS_BAND_MANAGE_TILES_FITNESS_RUN = "Settings/Band/Manage Tiles/Fitness/Run";
        public static final String SETTINGS_BAND_MANAGE_TILES_FITNESS_RUN_DATA_POINTS = "Settings/Band/Manage Tiles/Fitness/Run Data Points";
        public static final String SETTINGS_BAND_MANAGE_TILES_REORDER = "Settings/Band/Manage Tiles/Re-Order";
        public static final String SETTINGS_BAND_MY_BAND = "Settings/Band/My Band";
        public static final String SETTINGS_MOTION_TRACKING = "Phone/Motion tracking";
        public static final String SETTINGS_PERSONALIZE_COLOR_CHOOSER = "Settings/Personalize/Color Chooser";
        public static final String SETTINGS_PERSONALIZE_THEME_CHOOSER = "Settings/Personalize/Theme Chooser";
        public static final String SETTINGS_PERSONALIZE_WALLPAPER_CHOOSER = "Settings/Personalize/Wallpaper Chooser";
        public static final String SETTINGS_USER_ABOUT = "Settings/User/About";
        public static final String SETTINGS_USER_PREFERENCES = "Settings/User/Preferences";
        public static final String SETTINGS_USER_PROFILE = "Settings/User/Profile";

        /* loaded from: classes.dex */
        public static class Referrers {
            public static final String CALENDAR_TILE = "Workout Calendar Tile";
            public static final String FTU = "FTU";
            public static final String GOLF_DETAILS = "Golf Details";
            public static final String GOLF_MINI_HOME_TILE = "Golf Mini Home Tile";
            public static final String GOLF_SEARCH_BY_NAME = "Browse by Name";
            public static final String GOLF_SEARCH_NEARBY = "Nearby Courses";
            public static final String GOLF_SEARCH_RECENT = "Recent Courses";
            public static final String GOLF_SEARCH_REGION = "Browse by Country";
            public static final String KEY_REFERRER = "Referrer";
            public static final String LEFT_NAV = "Left Nav";
            public static final String SHAKE = "Shake";
        }
    }

    /* loaded from: classes.dex */
    public static class TimedEvents {
        public static final String DIMENSION_STATUS = "Status";
        public static final String DIMENSION_STATUS_FAILURE = "Failure";
        public static final String DIMENSION_STATUS_SUCCESS = "Success";
        public static final String SAVE_SETTINGS = "Settings/Band/Manage Tiles/Save Settings";

        /* loaded from: classes.dex */
        public static class HomePage {
            public static final String EVENT_NAME = "Pages/Home";

            /* loaded from: classes.dex */
            public static class Dimensions {
                public static final String NUMBER_OF_TILES = "NumberOfTiles";
                public static final String STATUS = "Status";
                public static final String STATUS_FAILURE = "Failure";
                public static final String STATUS_SUCCESS = "Success";
            }
        }

        /* loaded from: classes.dex */
        public static class Sync {
            public static final String EVENT_NAME = "Utilities/Sync";

            /* loaded from: classes.dex */
            public static class Dimensions {
                public static final String BYTESUPLOADED = "BytesUploaded";
                public static final String STATUS = "Status";
                public static final String STATUS_FAILURE = "Failure";
                public static final String STATUS_SUCCESS = "Success";
                public static final String TYPE = "Type";
                public static final String TYPE_BACKGROUND = "Background";
                public static final String TYPE_MANUAL = "Manual";
            }
        }

        /* loaded from: classes.dex */
        public static class SyncWaitForCloudProcessing {
            public static final String EVENT_NAME = "Utilities/Sync/Poll cloud status";

            /* loaded from: classes.dex */
            public static class Dimensions {
                public static final String STATUS = "Status";
                public static final String STATUS_FAILURE = "Failure";
                public static final String STATUS_SUCCESS = "Success";
            }
        }

        /* loaded from: classes.dex */
        public static class Cloud {
            public static final String CLOUD_CALL_UUID = "AppEx-Activity-Id";
            private static final String EVENT_PREFIX = "Cloud";
            public static final String SERVICE_BING = "Bing";
            public static final String SERVICE_GOLF = "TMAG";
            public static final String SERVICE_KCLOUD = "Khronos";
            public static final String SERVICE_OTHER = "Other";

            /* loaded from: classes.dex */
            public static class Dimensions {
                public static final String DURATION = "DurationMS";
                public static final String HTTP_VERB = "HTTP Verb";
                public static final String STATUS = "Status";
                public static final String STATUS_FAILURE = "Failure";
                public static final String STATUS_SUCCESS = "Success";
                public static final String TELEMETRY_UUID = "Request ID";
                public static final String VERB_DELETE = "DELETE";
                public static final String VERB_GET = "GET";
                public static final String VERB_POST = "POST";
                public static final String VERB_PUT = "PUT";
            }

            /* loaded from: classes.dex */
            public static class Golf {
                public static final String COURSE = "Cloud/Golf/Course";
                public static final String COURSE_ID = "Course ID";
                public static final String COURSE_TYPE = "Course Type";
                public static final String HOLE_COUNT = "Hole Count";
                public static final String REGION_ID = "Region ID";
                public static final String SEARCH_TYPE = "Type";
                public static final String SEARCH_TYPE_NAME = "Name";
                public static final String SEARCH_TYPE_NEARBY = "Near By";
                public static final String STATE_ID = "State ID";
            }

            public static String getEventName(String serviceName, Uri uri) {
                StringBuilder path = new StringBuilder();
                List<String> pathSegments = uri.getPathSegments();
                if (pathSegments != null && pathSegments.size() > 0) {
                    if (pathSegments.size() > 1) {
                        for (int i = 0; i < pathSegments.size() - 1; i++) {
                            path.append(pathSegments.get(i)).append("_");
                        }
                    }
                    String lastSegment = pathSegments.get(pathSegments.size() - 1);
                    int argIndex = lastSegment.indexOf(40);
                    if (argIndex != -1) {
                        lastSegment = lastSegment.substring(0, argIndex).trim();
                    }
                    path.append(lastSegment);
                }
                return String.format("%s/%s/%s", EVENT_PREFIX, serviceName, path);
            }
        }
    }
}

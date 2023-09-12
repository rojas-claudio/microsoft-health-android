package net.hockeyapp.android;

import com.microsoft.kapp.diagnostics.TelemetryConstants;
import java.util.HashMap;
import java.util.Map;
/* loaded from: classes.dex */
public class Strings {
    public static final int CRASH_DIALOG_MESSAGE_ID = 1;
    public static final int CRASH_DIALOG_NEGATIVE_BUTTON_ID = 2;
    public static final int CRASH_DIALOG_NEUTRAL_BUTTON_ID = 3;
    public static final int CRASH_DIALOG_POSITIVE_BUTTON_ID = 4;
    public static final int CRASH_DIALOG_TITLE_ID = 0;
    private static final Map<Integer, String> DEFAULT = new HashMap();
    public static final int DOWNLOAD_FAILED_DIALOG_MESSAGE_ID = 257;
    public static final int DOWNLOAD_FAILED_DIALOG_NEGATIVE_BUTTON_ID = 258;
    public static final int DOWNLOAD_FAILED_DIALOG_POSITIVE_BUTTON_ID = 259;
    public static final int DOWNLOAD_FAILED_DIALOG_TITLE_ID = 256;
    public static final int EXPIRY_INFO_TEXT_ID = 769;
    public static final int EXPIRY_INFO_TITLE_ID = 768;
    public static final int FEEDBACK_ATTACHMENT_BUTTON_TEXT_ID = 1031;
    public static final int FEEDBACK_EMAIL_INPUT_HINT_ID = 1027;
    public static final int FEEDBACK_FAILED_TEXT_ID = 1025;
    public static final int FEEDBACK_FAILED_TITLE_ID = 1024;
    public static final int FEEDBACK_GENERIC_ERROR_ID = 1040;
    public static final int FEEDBACK_LAST_UPDATED_TEXT_ID = 1030;
    public static final int FEEDBACK_MESSAGE_INPUT_HINT_ID = 1029;
    public static final int FEEDBACK_NAME_INPUT_HINT_ID = 1026;
    public static final int FEEDBACK_REFRESH_BUTTON_TEXT_ID = 1034;
    public static final int FEEDBACK_RESPONSE_BUTTON_TEXT_ID = 1033;
    public static final int FEEDBACK_SEND_BUTTON_TEXT_ID = 1032;
    public static final int FEEDBACK_SEND_GENERIC_ERROR_ID = 1036;
    public static final int FEEDBACK_SEND_NETWORK_ERROR_ID = 1037;
    public static final int FEEDBACK_SUBJECT_INPUT_HINT_ID = 1028;
    public static final int FEEDBACK_TITLE_ID = 1035;
    public static final int FEEDBACK_VALIDATE_EMAIL_EMPTY_ID = 1042;
    public static final int FEEDBACK_VALIDATE_EMAIL_ERROR_ID = 1039;
    public static final int FEEDBACK_VALIDATE_NAME_ERROR_ID = 1041;
    public static final int FEEDBACK_VALIDATE_SUBJECT_ERROR_ID = 1038;
    public static final int FEEDBACK_VALIDATE_TEXT_ERROR_ID = 1043;
    public static final int LOGIN_EMAIL_INPUT_HINT_ID = 1282;
    public static final int LOGIN_HEADLINE_TEXT_ID = 1280;
    public static final int LOGIN_LOGIN_BUTTON_TEXT_ID = 1284;
    public static final int LOGIN_MISSING_CREDENTIALS_TOAST_ID = 1281;
    public static final int LOGIN_PASSWORD_INPUT_HINT_ID = 1283;
    public static final int PAINT_DIALOG_MESSAGE_ID = 1540;
    public static final int PAINT_DIALOG_NEGATIVE_BUTTON_ID = 1541;
    public static final int PAINT_DIALOG_POSITIVE_BUTTON_ID = 1542;
    public static final int PAINT_INDICATOR_TOAST_ID = 1536;
    public static final int PAINT_MENU_CLEAR_ID = 1539;
    public static final int PAINT_MENU_SAVE_ID = 1537;
    public static final int PAINT_MENU_UNDO_ID = 1538;
    public static final int UPDATE_DIALOG_MESSAGE_ID = 514;
    public static final int UPDATE_DIALOG_NEGATIVE_BUTTON_ID = 515;
    public static final int UPDATE_DIALOG_POSITIVE_BUTTON_ID = 516;
    public static final int UPDATE_DIALOG_TITLE_ID = 513;
    public static final int UPDATE_MANDATORY_TOAST_ID = 512;

    static {
        DEFAULT.put(0, "Crash Data");
        DEFAULT.put(1, "The app found information about previous crashes. Would you like to send this data to the developer?");
        DEFAULT.put(2, "Dismiss");
        DEFAULT.put(3, "Always send");
        DEFAULT.put(4, "Send");
        DEFAULT.put(256, "Download Failed");
        DEFAULT.put(257, "The update could not be downloaded. Would you like to try again?");
        DEFAULT.put(Integer.valueOf((int) DOWNLOAD_FAILED_DIALOG_NEGATIVE_BUTTON_ID), TelemetryConstants.Events.ShakeDialog.Dimensions.CANCEL);
        DEFAULT.put(Integer.valueOf((int) DOWNLOAD_FAILED_DIALOG_POSITIVE_BUTTON_ID), "Retry");
        DEFAULT.put(512, "Please install the latest version to continue to use this app.");
        DEFAULT.put(Integer.valueOf((int) UPDATE_DIALOG_TITLE_ID), "Update Available");
        DEFAULT.put(Integer.valueOf((int) UPDATE_DIALOG_MESSAGE_ID), "Show information about the new update?");
        DEFAULT.put(Integer.valueOf((int) UPDATE_DIALOG_NEGATIVE_BUTTON_ID), "Dismiss");
        DEFAULT.put(Integer.valueOf((int) UPDATE_DIALOG_POSITIVE_BUTTON_ID), "Show");
        DEFAULT.put(768, "Build Expired");
        DEFAULT.put(Integer.valueOf((int) EXPIRY_INFO_TEXT_ID), "This has build has expired. Please check HockeyApp for any updates.");
        DEFAULT.put(1024, "Feedback Failed");
        DEFAULT.put(Integer.valueOf((int) FEEDBACK_FAILED_TEXT_ID), "Would you like to send your feedback again?");
        DEFAULT.put(Integer.valueOf((int) FEEDBACK_NAME_INPUT_HINT_ID), TelemetryConstants.TimedEvents.Cloud.Golf.SEARCH_TYPE_NAME);
        DEFAULT.put(Integer.valueOf((int) FEEDBACK_EMAIL_INPUT_HINT_ID), "Email");
        DEFAULT.put(Integer.valueOf((int) FEEDBACK_SUBJECT_INPUT_HINT_ID), "Subject");
        DEFAULT.put(Integer.valueOf((int) FEEDBACK_MESSAGE_INPUT_HINT_ID), com.microsoft.kapp.utils.Constants.NOTIFICATION_MESSAGE);
        DEFAULT.put(Integer.valueOf((int) FEEDBACK_LAST_UPDATED_TEXT_ID), "Last Updated: ");
        DEFAULT.put(Integer.valueOf((int) FEEDBACK_ATTACHMENT_BUTTON_TEXT_ID), "Add Attachment");
        DEFAULT.put(Integer.valueOf((int) FEEDBACK_SEND_BUTTON_TEXT_ID), TelemetryConstants.Events.ShakeDialog.Dimensions.SEND_FEEDBACK);
        DEFAULT.put(Integer.valueOf((int) FEEDBACK_RESPONSE_BUTTON_TEXT_ID), "Add a Response");
        DEFAULT.put(Integer.valueOf((int) FEEDBACK_REFRESH_BUTTON_TEXT_ID), "Refresh");
        DEFAULT.put(Integer.valueOf((int) FEEDBACK_TITLE_ID), "Feedback");
        DEFAULT.put(Integer.valueOf((int) FEEDBACK_SEND_GENERIC_ERROR_ID), "Message couldn't be posted. Please check your input values and your connection, then try again.");
        DEFAULT.put(Integer.valueOf((int) FEEDBACK_SEND_NETWORK_ERROR_ID), "No response from server. Please check your connection, then try again.");
        DEFAULT.put(Integer.valueOf((int) FEEDBACK_VALIDATE_SUBJECT_ERROR_ID), "Please enter a subject");
        DEFAULT.put(Integer.valueOf((int) FEEDBACK_VALIDATE_NAME_ERROR_ID), "Please enter a name");
        DEFAULT.put(Integer.valueOf((int) FEEDBACK_VALIDATE_EMAIL_EMPTY_ID), "Please enter an email address");
        DEFAULT.put(Integer.valueOf((int) FEEDBACK_VALIDATE_TEXT_ERROR_ID), "Please enter a feedback text");
        DEFAULT.put(Integer.valueOf((int) FEEDBACK_VALIDATE_EMAIL_ERROR_ID), "Message couldn't be posted. Please check the format of your email address.");
        DEFAULT.put(Integer.valueOf((int) FEEDBACK_GENERIC_ERROR_ID), "An error has occured");
        DEFAULT.put(Integer.valueOf((int) LOGIN_HEADLINE_TEXT_ID), "Please enter your account credentials.");
        DEFAULT.put(Integer.valueOf((int) LOGIN_MISSING_CREDENTIALS_TOAST_ID), "Please fill in the missing account credentials.");
        DEFAULT.put(Integer.valueOf((int) LOGIN_EMAIL_INPUT_HINT_ID), "Email");
        DEFAULT.put(Integer.valueOf((int) LOGIN_PASSWORD_INPUT_HINT_ID), "Password");
        DEFAULT.put(Integer.valueOf((int) LOGIN_LOGIN_BUTTON_TEXT_ID), "Login");
        DEFAULT.put(Integer.valueOf((int) PAINT_INDICATOR_TOAST_ID), "Draw something!");
        DEFAULT.put(Integer.valueOf((int) PAINT_MENU_SAVE_ID), "Save");
        DEFAULT.put(Integer.valueOf((int) PAINT_MENU_UNDO_ID), "Undo");
        DEFAULT.put(Integer.valueOf((int) PAINT_MENU_CLEAR_ID), "Clear");
        DEFAULT.put(Integer.valueOf((int) PAINT_DIALOG_MESSAGE_ID), "Discard your drawings?");
        DEFAULT.put(Integer.valueOf((int) PAINT_DIALOG_NEGATIVE_BUTTON_ID), "No");
        DEFAULT.put(Integer.valueOf((int) PAINT_DIALOG_POSITIVE_BUTTON_ID), "Yes");
    }

    public static String get(int resourceID) {
        return get(null, resourceID);
    }

    public static void set(int resourceID, String string) {
        if (string != null) {
            DEFAULT.put(Integer.valueOf(resourceID), string);
        }
    }

    public static String get(StringListener listener, int resourceID) {
        String result = null;
        if (listener != null) {
            result = listener.getStringForResource(resourceID);
        }
        if (result == null) {
            String result2 = DEFAULT.get(Integer.valueOf(resourceID));
            return result2;
        }
        return result;
    }
}

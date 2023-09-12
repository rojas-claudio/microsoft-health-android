package net.hockeyapp.android.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
/* loaded from: classes.dex */
public class PrefsUtil {
    private SharedPreferences feedbackTokenPrefs;
    private SharedPreferences.Editor feedbackTokenPrefsEditor;
    private SharedPreferences nameEmailSubjectPrefs;
    private SharedPreferences.Editor nameEmailSubjectPrefsEditor;

    private PrefsUtil() {
    }

    /* loaded from: classes.dex */
    private static class PrefsUtilHolder {
        public static final PrefsUtil INSTANCE = new PrefsUtil();

        private PrefsUtilHolder() {
        }
    }

    public static PrefsUtil getInstance() {
        return PrefsUtilHolder.INSTANCE;
    }

    public void saveFeedbackTokenToPrefs(Context context, String token) {
        if (context != null) {
            this.feedbackTokenPrefs = context.getSharedPreferences(Util.PREFS_FEEDBACK_TOKEN, 0);
            if (this.feedbackTokenPrefs != null) {
                this.feedbackTokenPrefsEditor = this.feedbackTokenPrefs.edit();
                this.feedbackTokenPrefsEditor.putString(Util.PREFS_KEY_FEEDBACK_TOKEN, token);
                applyChanges(this.feedbackTokenPrefsEditor);
            }
        }
    }

    public String getFeedbackTokenFromPrefs(Context context) {
        if (context == null) {
            return null;
        }
        this.feedbackTokenPrefs = context.getSharedPreferences(Util.PREFS_FEEDBACK_TOKEN, 0);
        if (this.feedbackTokenPrefs != null) {
            return this.feedbackTokenPrefs.getString(Util.PREFS_KEY_FEEDBACK_TOKEN, null);
        }
        return null;
    }

    public void saveNameEmailSubjectToPrefs(Context context, String name, String email, String subject) {
        if (context != null) {
            this.nameEmailSubjectPrefs = context.getSharedPreferences(Util.PREFS_NAME_EMAIL_SUBJECT, 0);
            if (this.nameEmailSubjectPrefs != null) {
                this.nameEmailSubjectPrefsEditor = this.nameEmailSubjectPrefs.edit();
                if (name == null || email == null || subject == null) {
                    this.nameEmailSubjectPrefsEditor.putString(Util.PREFS_KEY_NAME_EMAIL_SUBJECT, null);
                } else {
                    this.nameEmailSubjectPrefsEditor.putString(Util.PREFS_KEY_NAME_EMAIL_SUBJECT, String.format("%s|%s|%s", name, email, subject));
                }
                applyChanges(this.nameEmailSubjectPrefsEditor);
            }
        }
    }

    public String getNameEmailFromPrefs(Context context) {
        if (context == null) {
            return null;
        }
        this.nameEmailSubjectPrefs = context.getSharedPreferences(Util.PREFS_NAME_EMAIL_SUBJECT, 0);
        if (this.nameEmailSubjectPrefs != null) {
            return this.nameEmailSubjectPrefs.getString(Util.PREFS_KEY_NAME_EMAIL_SUBJECT, null);
        }
        return null;
    }

    public static void applyChanges(SharedPreferences.Editor editor) {
        if (applySupported().booleanValue()) {
            editor.apply();
        } else {
            editor.commit();
        }
    }

    public static Boolean applySupported() {
        try {
            return Boolean.valueOf(Build.VERSION.SDK_INT >= 9);
        } catch (NoClassDefFoundError e) {
            return false;
        }
    }
}

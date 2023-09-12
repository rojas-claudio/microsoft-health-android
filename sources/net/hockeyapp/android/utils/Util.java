package net.hockeyapp.android.utils;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.content.res.Configuration;
import android.os.Build;
import android.text.TextUtils;
import android.util.Patterns;
import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/* loaded from: classes.dex */
public class Util {
    public static final int APP_IDENTIFIER_LENGTH = 32;
    public static final String LOG_IDENTIFIER = "HockeyApp";
    public static final String PREFS_FEEDBACK_TOKEN = "net.hockeyapp.android.prefs_feedback_token";
    public static final String PREFS_KEY_FEEDBACK_TOKEN = "net.hockeyapp.android.prefs_key_feedback_token";
    public static final String PREFS_KEY_NAME_EMAIL_SUBJECT = "net.hockeyapp.android.prefs_key_name_email";
    public static final String PREFS_NAME_EMAIL_SUBJECT = "net.hockeyapp.android.prefs_name_email";
    public static final String APP_IDENTIFIER_PATTERN = "[0-9a-f]+";
    private static final Pattern appIdentifierPattern = Pattern.compile(APP_IDENTIFIER_PATTERN, 2);

    public static String encodeParam(String param) {
        try {
            return URLEncoder.encode(param, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "";
        }
    }

    @TargetApi(8)
    public static final boolean isValidEmail(String value) {
        return Build.VERSION.SDK_INT >= 8 ? !TextUtils.isEmpty(value) && Patterns.EMAIL_ADDRESS.matcher(value).matches() : !TextUtils.isEmpty(value);
    }

    @SuppressLint({"NewApi"})
    public static Boolean fragmentsSupported() {
        try {
            return Boolean.valueOf(Build.VERSION.SDK_INT >= 11 && Fragment.class != 0);
        } catch (NoClassDefFoundError e) {
            return false;
        }
    }

    public static Boolean runsOnTablet(WeakReference<Activity> weakActivity) {
        Activity activity;
        boolean z = false;
        if (weakActivity == null || (activity = weakActivity.get()) == null) {
            return false;
        }
        Configuration configuration = activity.getResources().getConfiguration();
        return Boolean.valueOf(((configuration.screenLayout & 15) == 3 || (configuration.screenLayout & 15) == 4) ? true : true);
    }

    public static String sanitizeAppIdentifier(String appIdentifier) throws IllegalArgumentException {
        if (appIdentifier == null) {
            throw new IllegalArgumentException("App ID must not be null.");
        }
        String sAppIdentifier = appIdentifier.trim();
        Matcher matcher = appIdentifierPattern.matcher(sAppIdentifier);
        if (sAppIdentifier.length() != 32) {
            throw new IllegalArgumentException("App ID length must be 32 characters.");
        }
        if (!matcher.matches()) {
            throw new IllegalArgumentException("App ID must match regex pattern /[0-9a-f]+/i");
        }
        return sAppIdentifier;
    }
}

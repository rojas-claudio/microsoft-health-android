package org.acra.collector;

import android.content.ContentResolver;
import android.content.Context;
import android.provider.Settings;
import android.util.Log;
import com.j256.ormlite.stmt.query.SimpleComparison;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.acra.ACRA;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class SettingsCollector {
    SettingsCollector() {
    }

    public static String collectSystemSettings(Context ctx) {
        StringBuilder result = new StringBuilder();
        Field[] keys = Settings.System.class.getFields();
        for (Field key : keys) {
            if (!key.isAnnotationPresent(Deprecated.class) && key.getType() == String.class) {
                try {
                    String value = Settings.System.getString(ctx.getContentResolver(), (String) key.get(null));
                    if (value != null) {
                        result.append(key.getName()).append(SimpleComparison.EQUAL_TO_OPERATION).append((Object) value).append("\n");
                    }
                } catch (IllegalAccessException e) {
                    Log.w(ACRA.LOG_TAG, "Error : ", e);
                } catch (IllegalArgumentException e2) {
                    Log.w(ACRA.LOG_TAG, "Error : ", e2);
                }
            }
        }
        return result.toString();
    }

    public static String collectSecureSettings(Context ctx) {
        StringBuilder result = new StringBuilder();
        Field[] keys = Settings.Secure.class.getFields();
        for (Field key : keys) {
            if (!key.isAnnotationPresent(Deprecated.class) && key.getType() == String.class && isAuthorized(key)) {
                try {
                    String value = Settings.Secure.getString(ctx.getContentResolver(), (String) key.get(null));
                    if (value != null) {
                        result.append(key.getName()).append(SimpleComparison.EQUAL_TO_OPERATION).append((Object) value).append("\n");
                    }
                } catch (IllegalAccessException e) {
                    Log.w(ACRA.LOG_TAG, "Error : ", e);
                } catch (IllegalArgumentException e2) {
                    Log.w(ACRA.LOG_TAG, "Error : ", e2);
                }
            }
        }
        return result.toString();
    }

    public static String collectGlobalSettings(Context ctx) {
        Object value;
        if (Compatibility.getAPILevel() < 17) {
            return "";
        }
        StringBuilder result = new StringBuilder();
        try {
            Class<?> globalClass = Class.forName("android.provider.Settings$Global");
            Field[] keys = globalClass.getFields();
            Method getString = globalClass.getMethod("getString", ContentResolver.class, String.class);
            for (Field key : keys) {
                if (!key.isAnnotationPresent(Deprecated.class) && key.getType() == String.class && isAuthorized(key) && (value = getString.invoke(null, ctx.getContentResolver(), (String) key.get(null))) != null) {
                    result.append(key.getName()).append(SimpleComparison.EQUAL_TO_OPERATION).append(value).append("\n");
                }
            }
        } catch (ClassNotFoundException e) {
            Log.w(ACRA.LOG_TAG, "Error : ", e);
        } catch (IllegalAccessException e2) {
            Log.w(ACRA.LOG_TAG, "Error : ", e2);
        } catch (IllegalArgumentException e3) {
            Log.w(ACRA.LOG_TAG, "Error : ", e3);
        } catch (NoSuchMethodException e4) {
            Log.w(ACRA.LOG_TAG, "Error : ", e4);
        } catch (SecurityException e5) {
            Log.w(ACRA.LOG_TAG, "Error : ", e5);
        } catch (InvocationTargetException e6) {
            Log.w(ACRA.LOG_TAG, "Error : ", e6);
        }
        return result.toString();
    }

    private static boolean isAuthorized(Field key) {
        if (key == null || key.getName().startsWith("WIFI_AP")) {
            return false;
        }
        String[] arr$ = ACRA.getConfig().excludeMatchingSettingsKeys();
        for (String regex : arr$) {
            if (key.getName().matches(regex)) {
                return false;
            }
        }
        return true;
    }
}

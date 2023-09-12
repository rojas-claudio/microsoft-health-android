package org.acra.collector;

import android.content.Context;
import android.os.Build;
import java.lang.reflect.Field;
/* loaded from: classes.dex */
public final class Compatibility {
    public static int getAPILevel() {
        try {
            Field SDK_INT = Build.VERSION.class.getField("SDK_INT");
            int apiLevel = SDK_INT.getInt(null);
            return apiLevel;
        } catch (IllegalAccessException e) {
            int apiLevel2 = Integer.parseInt(Build.VERSION.SDK);
            return apiLevel2;
        } catch (IllegalArgumentException e2) {
            int apiLevel3 = Integer.parseInt(Build.VERSION.SDK);
            return apiLevel3;
        } catch (NoSuchFieldException e3) {
            int apiLevel4 = Integer.parseInt(Build.VERSION.SDK);
            return apiLevel4;
        } catch (SecurityException e4) {
            int apiLevel5 = Integer.parseInt(Build.VERSION.SDK);
            return apiLevel5;
        }
    }

    public static String getDropBoxServiceName() throws NoSuchFieldException, IllegalAccessException {
        Field serviceName = Context.class.getField("DROPBOX_SERVICE");
        if (serviceName != null) {
            return (String) serviceName.get(null);
        }
        return null;
    }
}

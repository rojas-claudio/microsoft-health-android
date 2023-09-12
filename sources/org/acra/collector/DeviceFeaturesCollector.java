package org.acra.collector;

import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;
import com.microsoft.kapp.services.healthandfitness.models.WorkoutSummary;
import java.lang.reflect.Method;
import org.acra.ACRA;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class DeviceFeaturesCollector {
    DeviceFeaturesCollector() {
    }

    public static String getFeatures(Context ctx) {
        if (Compatibility.getAPILevel() < 5) {
            return "Data available only with API Level >= 5";
        }
        StringBuilder result = new StringBuilder();
        try {
            PackageManager pm = ctx.getPackageManager();
            Method getSystemAvailableFeatures = PackageManager.class.getMethod("getSystemAvailableFeatures", null);
            Object[] features = (Object[]) getSystemAvailableFeatures.invoke(pm, new Object[0]);
            for (Object feature : features) {
                String featureName = (String) feature.getClass().getField(WorkoutSummary.NAME).get(feature);
                if (featureName != null) {
                    result.append(featureName);
                } else {
                    Method getGlEsVersion = feature.getClass().getMethod("getGlEsVersion", null);
                    String glEsVersion = (String) getGlEsVersion.invoke(feature, new Object[0]);
                    result.append("glEsVersion = ");
                    result.append(glEsVersion);
                }
                result.append("\n");
            }
        } catch (Throwable e) {
            Log.w(ACRA.LOG_TAG, "Couldn't retrieve DeviceFeatures for " + ctx.getPackageName(), e);
            result.append("Could not retrieve data: ");
            result.append(e.getMessage());
        }
        return result.toString();
    }
}

package com.microsoft.kapp.sensor;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import com.microsoft.band.device.DeviceSettings;
import com.microsoft.kapp.device.CargoUserProfile;
import java.math.BigInteger;
import java.util.Iterator;
import java.util.Locale;
import java.util.UUID;
import org.apache.commons.lang3.Validate;
/* loaded from: classes.dex */
public class SensorUtils {
    private Context mContext;

    public SensorUtils(Context context) {
        this.mContext = context;
    }

    @TargetApi(19)
    public boolean isKitkatWithStepSensor() {
        int currentApiVersion = Build.VERSION.SDK_INT;
        PackageManager packageManager = this.mContext.getPackageManager();
        return currentApiVersion >= 19 && packageManager.hasSystemFeature("android.hardware.sensor.stepcounter");
    }

    public boolean hasBand(CargoUserProfile profile) {
        Validate.notNull(profile);
        return profile.isBandPaired();
    }

    public boolean isPhoneConnected(CargoUserProfile profile) {
        String phoneUuid = createUuidFromPhoneId().toString().toUpperCase(Locale.US);
        if (profile != null && profile.getDevices() != null) {
            Iterator i$ = profile.getDevices().iterator();
            while (i$.hasNext()) {
                DeviceSettings device = i$.next();
                if (device.getDeviceId().equalsIgnoreCase(phoneUuid)) {
                    return true;
                }
            }
        }
        return false;
    }

    public String getPhoneId() {
        return Settings.Secure.getString(this.mContext.getContentResolver(), "android_id");
    }

    public static String getUuidFromString(String uuidString) {
        long second;
        long first = 0;
        if (uuidString == null) {
            return new UUID(0L, 0L).toString();
        }
        if (uuidString.length() > 16) {
            first = new BigInteger(uuidString.substring(0, uuidString.length() - 16), 16).longValue();
            second = new BigInteger(uuidString.substring(uuidString.length() - 16), 16).longValue();
        } else {
            second = new BigInteger(uuidString, 16).longValue();
        }
        UUID uuid = new UUID(first, second);
        return uuid.toString();
    }

    public String createUuidFromPhoneId() {
        return getUuidFromString(getPhoneId());
    }
}

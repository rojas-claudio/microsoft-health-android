package com.microsoft.kapp.utils;

import com.microsoft.kapp.services.SettingsProvider;
import com.microsoft.kapp.version.CheckedFirmwareUpdateInfo;
import org.joda.time.DateTime;
import org.joda.time.Minutes;
/* loaded from: classes.dex */
public class FirmwareUpdateUtils {
    public static boolean isFirmwareUpdateCheckRequired(SettingsProvider settingsProvider) {
        CheckedFirmwareUpdateInfo cachedFirmwareUpdateInfo = settingsProvider.getCheckedFirmwareUpdateInfo();
        if (cachedFirmwareUpdateInfo != null) {
            return (cachedFirmwareUpdateInfo.isUpdateNeeded() && !cachedFirmwareUpdateInfo.isIsUpdateOptional()) || Minutes.minutesBetween(cachedFirmwareUpdateInfo.getLastSkipped(), DateTime.now()).getMinutes() >= 1440;
        }
        return true;
    }

    public static void clearCachedFirmwareUpdateInfo(SettingsProvider settingsProvider) {
        settingsProvider.setCheckedFirmwareUpdateInfo(null);
    }
}

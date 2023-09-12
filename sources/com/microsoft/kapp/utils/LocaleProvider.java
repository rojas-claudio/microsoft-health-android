package com.microsoft.kapp.utils;

import android.content.Context;
import com.google.gson.Gson;
import com.microsoft.band.client.CargoLocation;
import com.microsoft.kapp.device.ProfileRegionSettings;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.krestsdk.services.KCloudConstants;
import java.util.HashMap;
import java.util.Locale;
import org.json.JSONArray;
import org.json.JSONObject;
/* loaded from: classes.dex */
public class LocaleProvider {
    private static final String TAG = LocaleProvider.class.getSimpleName();

    public static ProfileRegionSettings getLocaleSettings(Context context) {
        return getLocaleSettings(context, Locale.getDefault().getCountry());
    }

    public static ProfileRegionSettings getLocaleSettings(Context context, String locale) {
        HashMap<String, ProfileRegionSettings> localeSettingsMap = getLocaleSettingsMap(context);
        return localeSettingsMap.containsKey(locale) ? localeSettingsMap.get(locale) : localeSettingsMap.get(Locale.UK.getCountry());
    }

    private static HashMap<String, ProfileRegionSettings> getLocaleSettingsMap(Context context) {
        HashMap<String, ProfileRegionSettings> supportedLocales = new HashMap<>();
        try {
            String jsonTxt = FileUtils.readFileContentFromAssets("localesettings.json", context);
            JSONObject json = new JSONObject(jsonTxt);
            JSONArray localeMap = json.getJSONArray("locales");
            Gson gson = new Gson();
            for (int i = 0; i < localeMap.length(); i++) {
                try {
                    JSONObject locale = (JSONObject) localeMap.get(i);
                    String localeString = locale.getString(KCloudConstants.LOCALE);
                    ProfileRegionSettings configuration = (ProfileRegionSettings) gson.fromJson(locale.getString("configuration"), (Class<Object>) ProfileRegionSettings.class);
                    configuration.setCargoLocation(CargoLocation.valueOf(localeString));
                    supportedLocales.put(localeString, configuration);
                } catch (Exception ex) {
                    KLog.e(TAG, "Couldn't create locale!", ex);
                }
            }
        } catch (Exception e) {
            KLog.e(TAG, "Couldn't deserialize locales!", e);
        }
        return supportedLocales;
    }
}

package com.microsoft.krestsdk.services;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.text.TextUtils;
import com.microsoft.kapp.services.SettingsProvider;
import java.util.Locale;
/* loaded from: classes.dex */
public class UserAgentProviderImpl implements UserAgentProvider {
    private SettingsProvider mSettingsProvider;
    private String mUserAgent;

    public UserAgentProviderImpl(Context context, SettingsProvider settingsProvider) {
        this.mSettingsProvider = settingsProvider;
        try {
            String appVersion = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
            this.mUserAgent = String.format("KApp/%s (Android/%s; %s/%s; %s-%s)", appVersion, Integer.valueOf(Build.VERSION.SDK_INT), Build.MODEL, Build.DEVICE, Locale.getDefault().getLanguage(), Locale.getDefault().getCountry());
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException("The app's package has no version- may not be installed correctly!");
        }
    }

    @Override // com.microsoft.krestsdk.services.UserAgentProvider
    public String getUserAgent() {
        String firmwareVersion = this.mSettingsProvider.getDeviceFirmwareVersion();
        return !TextUtils.isEmpty(firmwareVersion) ? String.format("%s Cargo/%s", this.mUserAgent, firmwareVersion) : this.mUserAgent;
    }
}

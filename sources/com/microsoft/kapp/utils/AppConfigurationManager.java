package com.microsoft.kapp.utils;

import android.content.Context;
import android.text.TextUtils;
import com.google.gson.stream.JsonReader;
import com.microsoft.band.util.StreamUtils;
import com.microsoft.kapp.Callback;
import com.microsoft.kapp.R;
import com.microsoft.kapp.cache.Cache;
import com.microsoft.kapp.diagnostics.Validate;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.kapp.models.strapp.DefaultStrappUUID;
import com.microsoft.kapp.models.strapp.StrappDefinition;
import com.microsoft.kapp.services.SettingsProvider;
import com.microsoft.kapp.services.finance.StockCompanyInformation;
import com.microsoft.krestsdk.models.AppConfigInfo;
import com.microsoft.krestsdk.models.AppConfiguration;
import com.microsoft.krestsdk.models.Configuration;
import com.microsoft.krestsdk.models.DefaultStock;
import com.microsoft.krestsdk.models.FeaturesConfiguration;
import com.microsoft.krestsdk.services.KCloudConstants;
import com.microsoft.krestsdk.services.RestService;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
/* loaded from: classes.dex */
public class AppConfigurationManager {
    private AppConfigInfo mAppConfigInfo;
    private AppConfiguration mAppConfiguration;
    private Cache mCache;
    private RestService mRestService;
    private SettingsProvider mSettingsProvider;
    private static final String TAG = AppConfigurationManager.class.getSimpleName();
    private static int DefaultAppConfigResId = R.raw.defaultconfig;

    public AppConfigurationManager(RestService restService, Cache cache, SettingsProvider settingsProvider) {
        Validate.notNull(restService, "restService");
        Validate.notNull(cache, "cache");
        Validate.notNull(settingsProvider, "settingsProvider");
        this.mCache = cache;
        this.mRestService = restService;
        this.mSettingsProvider = settingsProvider;
    }

    public AppConfiguration getAppConfiguration() {
        return this.mAppConfiguration;
    }

    public Configuration getConfiguration() {
        if (this.mAppConfiguration != null) {
            return this.mAppConfiguration.getConfiguration();
        }
        return null;
    }

    public AppConfigInfo getAppConfigInfo() {
        return this.mAppConfigInfo;
    }

    public void loadConfiguration(Context context) {
        InputStream appInfoStream;
        InputStream appConfigStream;
        if (!this.mSettingsProvider.canDynamicConfigurationLoadFromCloud()) {
            loadDefaultAppConfiguration(context);
            return;
        }
        String fusEndPoint = this.mSettingsProvider.getFUSEndPoint();
        if (!TextUtils.isEmpty(fusEndPoint) && (appInfoStream = this.mCache.getCache(fusEndPoint + KCloudConstants.APP_CONFIG_INFO)) != null) {
            AppConfigInfo appInfo = (AppConfigInfo) deserializeStream(appInfoStream, AppConfigInfo.class);
            AppConfiguration appConfiguration = null;
            if (appInfo != null) {
                handleAppUpgradeCase(context);
                if (requireConfigDownload(appInfo) && (appConfigStream = this.mCache.getCache(appInfo.getPrimaryURL())) != null) {
                    appConfiguration = (AppConfiguration) deserializeStream(appConfigStream, AppConfiguration.class);
                }
                if (appConfiguration != null) {
                    this.mAppConfigInfo = appInfo;
                    this.mAppConfiguration = appConfiguration;
                }
            }
        }
        if (this.mAppConfiguration == null) {
            loadDefaultAppConfiguration(context);
        }
    }

    private void handleAppUpgradeCase(Context context) {
        boolean appJustUpgraded = UpgradeUtils.isAppJustUpgraded(this.mSettingsProvider, context);
        if (appJustUpgraded) {
            loadDefaultAppConfiguration(context);
        }
    }

    public void downloadAppConfiguration(final Callback<Void> callback) {
        if (!this.mSettingsProvider.canDynamicConfigurationLoadFromCloud()) {
            callback.callback(null);
            return;
        }
        try {
            String fusEndPoint = this.mSettingsProvider.getFUSEndPoint();
            if (!TextUtils.isEmpty(fusEndPoint)) {
                Callback<AppConfigInfo> appConfigInfoCallback = new Callback<AppConfigInfo>() { // from class: com.microsoft.kapp.utils.AppConfigurationManager.1
                    @Override // com.microsoft.kapp.Callback
                    public void callback(AppConfigInfo result) {
                        boolean requireDownload = AppConfigurationManager.this.requireConfigDownload(result);
                        if (requireDownload) {
                            AppConfigurationManager.this.mRestService.downloadAppConfiguration(result, callback);
                        } else if (callback != null) {
                            callback.callback(null);
                        }
                    }

                    @Override // com.microsoft.kapp.Callback
                    public void onError(Exception ex) {
                        KLog.e(AppConfigurationManager.TAG, "Unexpected exception while getting for app config", ex);
                        if (callback != null) {
                            callback.onError(ex);
                        }
                    }
                };
                this.mRestService.getAppConfigurationInfo(fusEndPoint, appConfigInfoCallback);
            }
        } catch (Exception exception) {
            KLog.e(TAG, "Unexpected exception during downloading application config.", exception);
        }
    }

    public void downloadAppConfiguration() {
        downloadAppConfiguration(null);
    }

    public void downloadAndApplyAppConfiguration(final Context context, final Callback<Void> callback) {
        try {
            Callback<Void> loadConfigCallback = new Callback<Void>() { // from class: com.microsoft.kapp.utils.AppConfigurationManager.2
                @Override // com.microsoft.kapp.Callback
                public void callback(Void result) {
                    AppConfigurationManager.this.loadConfiguration(context);
                    callback.callback(null);
                }

                @Override // com.microsoft.kapp.Callback
                public void onError(Exception ex) {
                    AppConfigurationManager.this.loadDefaultAppConfiguration(context);
                    callback.callback(null);
                }
            };
            downloadAppConfiguration(loadConfigCallback);
        } catch (Exception exception) {
            KLog.e(TAG, "Unexpected exception during downloading application config.", exception);
        }
    }

    public boolean isStrappEnabled(StrappDefinition strappDefinition) {
        Validate.notNull(strappDefinition, "strappDefinition");
        if (this.mAppConfiguration == null || this.mAppConfiguration.getConfiguration() == null || this.mAppConfiguration.getConfiguration().getFeaturesConfiguration() == null) {
            return true;
        }
        FeaturesConfiguration featuresConfiguration = this.mAppConfiguration.getConfiguration().getFeaturesConfiguration();
        UUID strapId = strappDefinition.getStrappId();
        if (DefaultStrappUUID.STRAPP_STARBUCKS.equals(strapId)) {
            return featuresConfiguration.getStarbucksFeatureConfiguration() != null && featuresConfiguration.getStarbucksFeatureConfiguration().IsEnabled();
        } else if (DefaultStrappUUID.STRAPP_FACEBOOK.equals(strapId)) {
            return featuresConfiguration.getFacebookFeatureConfiguration() != null && featuresConfiguration.getFacebookFeatureConfiguration().IsEnabled();
        } else if (DefaultStrappUUID.STRAPP_FACEBOOK_MESSENGER.equals(strapId)) {
            return featuresConfiguration.getFacebookMessengerFeatureConfiguration() != null && featuresConfiguration.getFacebookMessengerFeatureConfiguration().IsEnabled();
        } else if (DefaultStrappUUID.STRAPP_TWITTER.equals(strapId)) {
            return featuresConfiguration.getTwitterFeatureConfiguration() != null && featuresConfiguration.getTwitterFeatureConfiguration().IsEnabled();
        } else if (DefaultStrappUUID.STRAPP_BING_FINANCE.equals(strapId)) {
            return featuresConfiguration.getFinanceFeatureConfiguration() != null && featuresConfiguration.getFinanceFeatureConfiguration().IsEnabled();
        } else {
            return true;
        }
    }

    public void loadDefaultAppConfiguration(Context context, Locale locale) {
        int resId = context.getResources().getIdentifier(locale.getCountry().toLowerCase(Locale.ENGLISH), R.raw.class.getSimpleName(), R.raw.class.getPackage().getName());
        if (resId == 0) {
            resId = DefaultAppConfigResId;
        }
        InputStream ins = context.getResources().openRawResource(resId);
        this.mAppConfiguration = (AppConfiguration) deserializeStream(ins, AppConfiguration.class);
        this.mAppConfigInfo = new AppConfigInfo();
        this.mAppConfigInfo.setmVersion(this.mAppConfiguration.getVersion());
        this.mAppConfigInfo.setRegion(RegionUtils.getMarketString(locale));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void loadDefaultAppConfiguration(Context context) {
        loadDefaultAppConfiguration(context, Locale.getDefault());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean requireConfigDownload(AppConfigInfo appConfigInfo) {
        return getAppConfiguration() == null || getAppConfigInfo() == null || getAppConfigInfo().compareTo(appConfigInfo) < 0;
    }

    private <T> T deserializeStream(InputStream ins, Class<T> typeClass) {
        try {
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(ins, "UTF-8"), 8192);
                JsonReader jsonReader = new JsonReader(reader);
                return (T) GsonUtils.getCustomDeserializer().fromJson(jsonReader, typeClass);
            } catch (UnsupportedEncodingException e) {
                KLog.e(TAG, "Exception while parsing app config from cache", e);
                StreamUtils.closeQuietly(ins);
                return null;
            }
        } finally {
            StreamUtils.closeQuietly(ins);
        }
    }

    public List<StockCompanyInformation> getStockInformation() {
        List<StockCompanyInformation> stocks = new ArrayList<>();
        List<StockCompanyInformation> stockCompanies = this.mSettingsProvider.getStockCompanies();
        if (stockCompanies == null) {
            if (getConfiguration() != null) {
                Configuration config = getConfiguration();
                if (config.getFeaturesConfiguration() != null && config.getFeaturesConfiguration().getFinanceFeatureConfiguration() != null && config.getFeaturesConfiguration().getFinanceFeatureConfiguration().getDefaultStockList() != null) {
                    stocks = convertToStockCompanyInformation(config.getFeaturesConfiguration().getFinanceFeatureConfiguration().getDefaultStockList());
                }
            }
            return stocks;
        }
        return stockCompanies;
    }

    private List<StockCompanyInformation> convertToStockCompanyInformation(DefaultStock[] defaultStockList) {
        List<StockCompanyInformation> watchedStocksList = new ArrayList<>();
        for (DefaultStock stockInfo : defaultStockList) {
            watchedStocksList.add(new StockCompanyInformation(stockInfo.getSymbol(), stockInfo.getId()));
        }
        return watchedStocksList;
    }

    public String getStarbucksUrl() {
        if (getConfiguration() == null) {
            return Constants.STARBUCKS_LOGIN_URL;
        }
        Configuration config = getConfiguration();
        if (config.getFeaturesConfiguration() == null || config.getFeaturesConfiguration().getStarbucksFeatureConfiguration() == null || config.getFeaturesConfiguration().getStarbucksFeatureConfiguration().getDisplayUrl() == null) {
            return Constants.STARBUCKS_LOGIN_URL;
        }
        String starbucksURL = config.getFeaturesConfiguration().getStarbucksFeatureConfiguration().getDisplayUrl();
        return starbucksURL;
    }

    public String getStarbucksUrlDisplayString(Context context) {
        String starbucksURLDisplayString = context.getResources().getString(R.string.settings_starbucks_card_settings_descriptions_link_description);
        if (getConfiguration() != null) {
            Configuration config = getConfiguration();
            if (config.getFeaturesConfiguration() != null && config.getFeaturesConfiguration().getStarbucksFeatureConfiguration() != null && config.getFeaturesConfiguration().getStarbucksFeatureConfiguration().getDisplayUrlString() != null) {
                return config.getFeaturesConfiguration().getStarbucksFeatureConfiguration().getDisplayUrlString();
            }
            return starbucksURLDisplayString;
        }
        return starbucksURLDisplayString;
    }

    public String getStarbucksBackImageUrl() {
        if (getConfiguration() != null) {
            Configuration config = getConfiguration();
            if (config.getFeaturesConfiguration() != null && config.getFeaturesConfiguration().getStarbucksFeatureConfiguration() != null && config.getFeaturesConfiguration().getStarbucksFeatureConfiguration().getCardBackUrl() != null) {
                return config.getFeaturesConfiguration().getStarbucksFeatureConfiguration().getCardBackUrl();
            }
        }
        return null;
    }

    public String getStarbucksFrontImageUrl() {
        if (getConfiguration() != null) {
            Configuration config = getConfiguration();
            if (config.getFeaturesConfiguration() != null && config.getFeaturesConfiguration().getStarbucksFeatureConfiguration() != null && config.getFeaturesConfiguration().getStarbucksFeatureConfiguration().getCardFrontUrl() != null) {
                return config.getFeaturesConfiguration().getStarbucksFeatureConfiguration().getCardFrontUrl();
            }
        }
        return null;
    }
}

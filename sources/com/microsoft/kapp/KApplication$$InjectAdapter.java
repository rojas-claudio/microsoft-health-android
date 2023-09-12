package com.microsoft.kapp;

import com.microsoft.kapp.cache.CacheService;
import com.microsoft.kapp.logging.http.FiddlerLogger;
import com.microsoft.kapp.multidevice.MultiDeviceManager;
import com.microsoft.kapp.sensor.SensorUtils;
import com.microsoft.kapp.services.SettingsProvider;
import com.microsoft.kapp.utils.AppConfigurationManager;
import com.microsoft.krestsdk.auth.credentials.CredentialsManager;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class KApplication$$InjectAdapter extends Binding<KApplication> implements Provider<KApplication>, MembersInjector<KApplication> {
    private Binding<AppConfigurationManager> mAppConfigurationManager;
    private Binding<CacheService> mCacheService;
    private Binding<CargoConnection> mCargoConnection;
    private Binding<CredentialsManager> mCredentialsManager;
    private Binding<DeviceStateDisplayManager> mDeviceStateDisplayManager;
    private Binding<FiddlerLogger> mFiddlerLogger;
    private Binding<MultiDeviceManager> mMultiDeviceManager;
    private Binding<SensorUtils> mSensorUtils;
    private Binding<SettingsProvider> mSettingsProvider;
    private Binding<ShakeDetector> mShakeDetector;

    public KApplication$$InjectAdapter() {
        super("com.microsoft.kapp.KApplication", "members/com.microsoft.kapp.KApplication", false, KApplication.class);
    }

    @Override // dagger.internal.Binding
    public void attach(Linker linker) {
        this.mSettingsProvider = linker.requestBinding("com.microsoft.kapp.services.SettingsProvider", KApplication.class, getClass().getClassLoader());
        this.mDeviceStateDisplayManager = linker.requestBinding("com.microsoft.kapp.DeviceStateDisplayManager", KApplication.class, getClass().getClassLoader());
        this.mCargoConnection = linker.requestBinding("com.microsoft.kapp.CargoConnection", KApplication.class, getClass().getClassLoader());
        this.mMultiDeviceManager = linker.requestBinding("com.microsoft.kapp.multidevice.MultiDeviceManager", KApplication.class, getClass().getClassLoader());
        this.mCredentialsManager = linker.requestBinding("com.microsoft.krestsdk.auth.credentials.CredentialsManager", KApplication.class, getClass().getClassLoader());
        this.mFiddlerLogger = linker.requestBinding("com.microsoft.kapp.logging.http.FiddlerLogger", KApplication.class, getClass().getClassLoader());
        this.mSensorUtils = linker.requestBinding("com.microsoft.kapp.sensor.SensorUtils", KApplication.class, getClass().getClassLoader());
        this.mShakeDetector = linker.requestBinding("com.microsoft.kapp.ShakeDetector", KApplication.class, getClass().getClassLoader());
        this.mAppConfigurationManager = linker.requestBinding("com.microsoft.kapp.utils.AppConfigurationManager", KApplication.class, getClass().getClassLoader());
        this.mCacheService = linker.requestBinding("com.microsoft.kapp.cache.CacheService", KApplication.class, getClass().getClassLoader());
    }

    @Override // dagger.internal.Binding
    public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
        injectMembersBindings.add(this.mSettingsProvider);
        injectMembersBindings.add(this.mDeviceStateDisplayManager);
        injectMembersBindings.add(this.mCargoConnection);
        injectMembersBindings.add(this.mMultiDeviceManager);
        injectMembersBindings.add(this.mCredentialsManager);
        injectMembersBindings.add(this.mFiddlerLogger);
        injectMembersBindings.add(this.mSensorUtils);
        injectMembersBindings.add(this.mShakeDetector);
        injectMembersBindings.add(this.mAppConfigurationManager);
        injectMembersBindings.add(this.mCacheService);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // dagger.internal.Binding, javax.inject.Provider
    public KApplication get() {
        KApplication result = new KApplication();
        injectMembers(result);
        return result;
    }

    @Override // dagger.internal.Binding, dagger.MembersInjector
    public void injectMembers(KApplication object) {
        object.mSettingsProvider = this.mSettingsProvider.get();
        object.mDeviceStateDisplayManager = this.mDeviceStateDisplayManager.get();
        object.mCargoConnection = this.mCargoConnection.get();
        object.mMultiDeviceManager = this.mMultiDeviceManager.get();
        object.mCredentialsManager = this.mCredentialsManager.get();
        object.mFiddlerLogger = this.mFiddlerLogger.get();
        object.mSensorUtils = this.mSensorUtils.get();
        object.mShakeDetector = this.mShakeDetector.get();
        object.mAppConfigurationManager = this.mAppConfigurationManager.get();
        object.mCacheService = this.mCacheService.get();
    }
}

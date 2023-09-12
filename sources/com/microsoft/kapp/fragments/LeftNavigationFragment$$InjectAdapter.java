package com.microsoft.kapp.fragments;

import com.microsoft.kapp.UserProfileFetcher;
import com.microsoft.kapp.cache.CacheService;
import com.microsoft.kapp.logging.http.FiddlerLogger;
import com.microsoft.kapp.sensor.SensorUtils;
import com.microsoft.kapp.services.SettingsProvider;
import com.microsoft.kapp.services.guidedworkout.GuidedWorkoutService;
import com.microsoft.krestsdk.auth.MsaAuth;
import com.microsoft.krestsdk.auth.credentials.CredentialsManager;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class LeftNavigationFragment$$InjectAdapter extends Binding<LeftNavigationFragment> implements Provider<LeftNavigationFragment>, MembersInjector<LeftNavigationFragment> {
    private Binding<MsaAuth> mAuthService;
    private Binding<CacheService> mCacheService;
    private Binding<CredentialsManager> mCredentialsManager;
    private Binding<FiddlerLogger> mFiddlerLogger;
    private Binding<GuidedWorkoutService> mGuidedWorkoutService;
    private Binding<SensorUtils> mSensorUtils;
    private Binding<SettingsProvider> mSettingsProvider;
    private Binding<UserProfileFetcher> mUserProfileFetcher;
    private Binding<BaseNavigationFragment> supertype;

    public LeftNavigationFragment$$InjectAdapter() {
        super("com.microsoft.kapp.fragments.LeftNavigationFragment", "members/com.microsoft.kapp.fragments.LeftNavigationFragment", false, LeftNavigationFragment.class);
    }

    @Override // dagger.internal.Binding
    public void attach(Linker linker) {
        this.mCredentialsManager = linker.requestBinding("com.microsoft.krestsdk.auth.credentials.CredentialsManager", LeftNavigationFragment.class, getClass().getClassLoader());
        this.mSettingsProvider = linker.requestBinding("com.microsoft.kapp.services.SettingsProvider", LeftNavigationFragment.class, getClass().getClassLoader());
        this.mCacheService = linker.requestBinding("com.microsoft.kapp.cache.CacheService", LeftNavigationFragment.class, getClass().getClassLoader());
        this.mAuthService = linker.requestBinding("com.microsoft.krestsdk.auth.MsaAuth", LeftNavigationFragment.class, getClass().getClassLoader());
        this.mFiddlerLogger = linker.requestBinding("com.microsoft.kapp.logging.http.FiddlerLogger", LeftNavigationFragment.class, getClass().getClassLoader());
        this.mGuidedWorkoutService = linker.requestBinding("com.microsoft.kapp.services.guidedworkout.GuidedWorkoutService", LeftNavigationFragment.class, getClass().getClassLoader());
        this.mUserProfileFetcher = linker.requestBinding("com.microsoft.kapp.UserProfileFetcher", LeftNavigationFragment.class, getClass().getClassLoader());
        this.mSensorUtils = linker.requestBinding("com.microsoft.kapp.sensor.SensorUtils", LeftNavigationFragment.class, getClass().getClassLoader());
        this.supertype = linker.requestBinding("members/com.microsoft.kapp.fragments.BaseNavigationFragment", LeftNavigationFragment.class, getClass().getClassLoader(), false, true);
    }

    @Override // dagger.internal.Binding
    public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
        injectMembersBindings.add(this.mCredentialsManager);
        injectMembersBindings.add(this.mSettingsProvider);
        injectMembersBindings.add(this.mCacheService);
        injectMembersBindings.add(this.mAuthService);
        injectMembersBindings.add(this.mFiddlerLogger);
        injectMembersBindings.add(this.mGuidedWorkoutService);
        injectMembersBindings.add(this.mUserProfileFetcher);
        injectMembersBindings.add(this.mSensorUtils);
        injectMembersBindings.add(this.supertype);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // dagger.internal.Binding, javax.inject.Provider
    public LeftNavigationFragment get() {
        LeftNavigationFragment result = new LeftNavigationFragment();
        injectMembers(result);
        return result;
    }

    @Override // dagger.internal.Binding, dagger.MembersInjector
    public void injectMembers(LeftNavigationFragment object) {
        object.mCredentialsManager = this.mCredentialsManager.get();
        object.mSettingsProvider = this.mSettingsProvider.get();
        object.mCacheService = this.mCacheService.get();
        object.mAuthService = this.mAuthService.get();
        object.mFiddlerLogger = this.mFiddlerLogger.get();
        object.mGuidedWorkoutService = this.mGuidedWorkoutService.get();
        object.mUserProfileFetcher = this.mUserProfileFetcher.get();
        object.mSensorUtils = this.mSensorUtils.get();
        this.supertype.injectMembers(object);
    }
}

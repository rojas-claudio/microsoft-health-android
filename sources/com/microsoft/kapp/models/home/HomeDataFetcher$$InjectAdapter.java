package com.microsoft.kapp.models.home;

import android.content.Context;
import com.microsoft.kapp.multidevice.MultiDeviceManager;
import com.microsoft.kapp.services.SettingsProvider;
import com.microsoft.kapp.services.healthandfitness.HealthAndFitnessService;
import com.microsoft.krestsdk.auth.KdsFetcher;
import com.microsoft.krestsdk.auth.MsaAuth;
import com.microsoft.krestsdk.auth.credentials.CredentialsManager;
import com.microsoft.krestsdk.services.RestService;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;
/* loaded from: classes.dex */
public final class HomeDataFetcher$$InjectAdapter extends Binding<HomeDataFetcher> implements MembersInjector<HomeDataFetcher> {
    private Binding<MsaAuth> mAuthService;
    private Binding<Context> mContext;
    private Binding<CredentialsManager> mCredentialsManager;
    private Binding<HealthAndFitnessService> mHealthAndFitnessRestService;
    private Binding<KdsFetcher> mKdsFetcher;
    private Binding<MultiDeviceManager> mMultiDeviceManager;
    private Binding<RestService> mRestService;
    private Binding<SettingsProvider> mSettingsProvider;

    public HomeDataFetcher$$InjectAdapter() {
        super(null, "members/com.microsoft.kapp.models.home.HomeDataFetcher", false, HomeDataFetcher.class);
    }

    @Override // dagger.internal.Binding
    public void attach(Linker linker) {
        this.mRestService = linker.requestBinding("com.microsoft.krestsdk.services.RestService", HomeDataFetcher.class, getClass().getClassLoader());
        this.mSettingsProvider = linker.requestBinding("com.microsoft.kapp.services.SettingsProvider", HomeDataFetcher.class, getClass().getClassLoader());
        this.mHealthAndFitnessRestService = linker.requestBinding("com.microsoft.kapp.services.healthandfitness.HealthAndFitnessService", HomeDataFetcher.class, getClass().getClassLoader());
        this.mCredentialsManager = linker.requestBinding("com.microsoft.krestsdk.auth.credentials.CredentialsManager", HomeDataFetcher.class, getClass().getClassLoader());
        this.mAuthService = linker.requestBinding("com.microsoft.krestsdk.auth.MsaAuth", HomeDataFetcher.class, getClass().getClassLoader());
        this.mKdsFetcher = linker.requestBinding("com.microsoft.krestsdk.auth.KdsFetcher", HomeDataFetcher.class, getClass().getClassLoader());
        this.mContext = linker.requestBinding("android.content.Context", HomeDataFetcher.class, getClass().getClassLoader());
        this.mMultiDeviceManager = linker.requestBinding("com.microsoft.kapp.multidevice.MultiDeviceManager", HomeDataFetcher.class, getClass().getClassLoader());
    }

    @Override // dagger.internal.Binding
    public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
        injectMembersBindings.add(this.mRestService);
        injectMembersBindings.add(this.mSettingsProvider);
        injectMembersBindings.add(this.mHealthAndFitnessRestService);
        injectMembersBindings.add(this.mCredentialsManager);
        injectMembersBindings.add(this.mAuthService);
        injectMembersBindings.add(this.mKdsFetcher);
        injectMembersBindings.add(this.mContext);
        injectMembersBindings.add(this.mMultiDeviceManager);
    }

    @Override // dagger.internal.Binding, dagger.MembersInjector
    public void injectMembers(HomeDataFetcher object) {
        object.mRestService = this.mRestService.get();
        object.mSettingsProvider = this.mSettingsProvider.get();
        object.mHealthAndFitnessRestService = this.mHealthAndFitnessRestService.get();
        object.mCredentialsManager = this.mCredentialsManager.get();
        object.mAuthService = this.mAuthService.get();
        object.mKdsFetcher = this.mKdsFetcher.get();
        object.mContext = this.mContext.get();
        object.mMultiDeviceManager = this.mMultiDeviceManager.get();
    }
}

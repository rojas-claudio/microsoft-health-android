package com.microsoft.kapp.fragments;

import com.microsoft.kapp.UserProfileFetcher;
import com.microsoft.kapp.services.SettingsProvider;
import com.microsoft.kapp.utils.AppConfigurationManager;
import com.microsoft.krestsdk.auth.MsaAuth;
import com.microsoft.krestsdk.auth.credentials.CredentialsManager;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class SettingsProfileFragment$$InjectAdapter extends Binding<SettingsProfileFragment> implements Provider<SettingsProfileFragment>, MembersInjector<SettingsProfileFragment> {
    private Binding<AppConfigurationManager> mAppConfigurationManager;
    private Binding<MsaAuth> mAuthService;
    private Binding<CredentialsManager> mCredentialsManager;
    private Binding<SettingsProvider> mSettingsProvider;
    private Binding<UserProfileFetcher> mUserProfileFetcher;
    private Binding<BaseFragmentWithOfflineSupport> supertype;

    public SettingsProfileFragment$$InjectAdapter() {
        super("com.microsoft.kapp.fragments.SettingsProfileFragment", "members/com.microsoft.kapp.fragments.SettingsProfileFragment", false, SettingsProfileFragment.class);
    }

    @Override // dagger.internal.Binding
    public void attach(Linker linker) {
        this.mCredentialsManager = linker.requestBinding("com.microsoft.krestsdk.auth.credentials.CredentialsManager", SettingsProfileFragment.class, getClass().getClassLoader());
        this.mSettingsProvider = linker.requestBinding("com.microsoft.kapp.services.SettingsProvider", SettingsProfileFragment.class, getClass().getClassLoader());
        this.mAuthService = linker.requestBinding("com.microsoft.krestsdk.auth.MsaAuth", SettingsProfileFragment.class, getClass().getClassLoader());
        this.mUserProfileFetcher = linker.requestBinding("com.microsoft.kapp.UserProfileFetcher", SettingsProfileFragment.class, getClass().getClassLoader());
        this.mAppConfigurationManager = linker.requestBinding("com.microsoft.kapp.utils.AppConfigurationManager", SettingsProfileFragment.class, getClass().getClassLoader());
        this.supertype = linker.requestBinding("members/com.microsoft.kapp.fragments.BaseFragmentWithOfflineSupport", SettingsProfileFragment.class, getClass().getClassLoader(), false, true);
    }

    @Override // dagger.internal.Binding
    public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
        injectMembersBindings.add(this.mCredentialsManager);
        injectMembersBindings.add(this.mSettingsProvider);
        injectMembersBindings.add(this.mAuthService);
        injectMembersBindings.add(this.mUserProfileFetcher);
        injectMembersBindings.add(this.mAppConfigurationManager);
        injectMembersBindings.add(this.supertype);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // dagger.internal.Binding, javax.inject.Provider
    public SettingsProfileFragment get() {
        SettingsProfileFragment result = new SettingsProfileFragment();
        injectMembers(result);
        return result;
    }

    @Override // dagger.internal.Binding, dagger.MembersInjector
    public void injectMembers(SettingsProfileFragment object) {
        object.mCredentialsManager = this.mCredentialsManager.get();
        object.mSettingsProvider = this.mSettingsProvider.get();
        object.mAuthService = this.mAuthService.get();
        object.mUserProfileFetcher = this.mUserProfileFetcher.get();
        object.mAppConfigurationManager = this.mAppConfigurationManager.get();
        this.supertype.injectMembers(object);
    }
}

package com.microsoft.kapp.fragments;

import com.microsoft.kapp.services.SettingsProvider;
import com.microsoft.krestsdk.auth.MsaAuth;
import com.microsoft.krestsdk.auth.credentials.CredentialsManager;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class TopMenuFragment$$InjectAdapter extends Binding<TopMenuFragment> implements Provider<TopMenuFragment>, MembersInjector<TopMenuFragment> {
    private Binding<MsaAuth> mAuthService;
    private Binding<CredentialsManager> mCredentialsManager;
    private Binding<SettingsProvider> mSettingsProvider;
    private Binding<BaseFragment> supertype;

    public TopMenuFragment$$InjectAdapter() {
        super("com.microsoft.kapp.fragments.TopMenuFragment", "members/com.microsoft.kapp.fragments.TopMenuFragment", false, TopMenuFragment.class);
    }

    @Override // dagger.internal.Binding
    public void attach(Linker linker) {
        this.mSettingsProvider = linker.requestBinding("com.microsoft.kapp.services.SettingsProvider", TopMenuFragment.class, getClass().getClassLoader());
        this.mCredentialsManager = linker.requestBinding("com.microsoft.krestsdk.auth.credentials.CredentialsManager", TopMenuFragment.class, getClass().getClassLoader());
        this.mAuthService = linker.requestBinding("com.microsoft.krestsdk.auth.MsaAuth", TopMenuFragment.class, getClass().getClassLoader());
        this.supertype = linker.requestBinding("members/com.microsoft.kapp.fragments.BaseFragment", TopMenuFragment.class, getClass().getClassLoader(), false, true);
    }

    @Override // dagger.internal.Binding
    public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
        injectMembersBindings.add(this.mSettingsProvider);
        injectMembersBindings.add(this.mCredentialsManager);
        injectMembersBindings.add(this.mAuthService);
        injectMembersBindings.add(this.supertype);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // dagger.internal.Binding, javax.inject.Provider
    public TopMenuFragment get() {
        TopMenuFragment result = new TopMenuFragment();
        injectMembers(result);
        return result;
    }

    @Override // dagger.internal.Binding, dagger.MembersInjector
    public void injectMembers(TopMenuFragment object) {
        object.mSettingsProvider = this.mSettingsProvider.get();
        object.mCredentialsManager = this.mCredentialsManager.get();
        object.mAuthService = this.mAuthService.get();
        this.supertype.injectMembers(object);
    }
}

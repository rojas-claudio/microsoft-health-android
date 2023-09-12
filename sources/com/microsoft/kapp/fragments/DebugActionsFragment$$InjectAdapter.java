package com.microsoft.kapp.fragments;

import com.microsoft.kapp.cache.CacheService;
import com.microsoft.kapp.services.SettingsProvider;
import com.microsoft.krestsdk.auth.credentials.CredentialsManager;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class DebugActionsFragment$$InjectAdapter extends Binding<DebugActionsFragment> implements Provider<DebugActionsFragment>, MembersInjector<DebugActionsFragment> {
    private Binding<CacheService> mCacheService;
    private Binding<CredentialsManager> mCredentialsManager;
    private Binding<SettingsProvider> mSettingsProvider;
    private Binding<BaseFragment> supertype;

    public DebugActionsFragment$$InjectAdapter() {
        super("com.microsoft.kapp.fragments.DebugActionsFragment", "members/com.microsoft.kapp.fragments.DebugActionsFragment", false, DebugActionsFragment.class);
    }

    @Override // dagger.internal.Binding
    public void attach(Linker linker) {
        this.mCredentialsManager = linker.requestBinding("com.microsoft.krestsdk.auth.credentials.CredentialsManager", DebugActionsFragment.class, getClass().getClassLoader());
        this.mSettingsProvider = linker.requestBinding("com.microsoft.kapp.services.SettingsProvider", DebugActionsFragment.class, getClass().getClassLoader());
        this.mCacheService = linker.requestBinding("com.microsoft.kapp.cache.CacheService", DebugActionsFragment.class, getClass().getClassLoader());
        this.supertype = linker.requestBinding("members/com.microsoft.kapp.fragments.BaseFragment", DebugActionsFragment.class, getClass().getClassLoader(), false, true);
    }

    @Override // dagger.internal.Binding
    public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
        injectMembersBindings.add(this.mCredentialsManager);
        injectMembersBindings.add(this.mSettingsProvider);
        injectMembersBindings.add(this.mCacheService);
        injectMembersBindings.add(this.supertype);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // dagger.internal.Binding, javax.inject.Provider
    public DebugActionsFragment get() {
        DebugActionsFragment result = new DebugActionsFragment();
        injectMembers(result);
        return result;
    }

    @Override // dagger.internal.Binding, dagger.MembersInjector
    public void injectMembers(DebugActionsFragment object) {
        object.mCredentialsManager = this.mCredentialsManager.get();
        object.mSettingsProvider = this.mSettingsProvider.get();
        object.mCacheService = this.mCacheService.get();
        this.supertype.injectMembers(object);
    }
}

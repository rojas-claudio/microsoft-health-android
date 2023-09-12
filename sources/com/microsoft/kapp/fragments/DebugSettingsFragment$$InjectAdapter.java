package com.microsoft.kapp.fragments;

import com.microsoft.kapp.cache.CacheService;
import com.microsoft.kapp.feedback.FeedbackService;
import com.microsoft.kapp.services.SettingsProvider;
import com.microsoft.krestsdk.auth.MsaAuth;
import com.microsoft.krestsdk.auth.credentials.CredentialsManager;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class DebugSettingsFragment$$InjectAdapter extends Binding<DebugSettingsFragment> implements Provider<DebugSettingsFragment>, MembersInjector<DebugSettingsFragment> {
    private Binding<MsaAuth> mAuthService;
    private Binding<CacheService> mCacheService;
    private Binding<CredentialsManager> mCredentialsManager;
    private Binding<FeedbackService> mFeedbackService;
    private Binding<SettingsProvider> mSettingsProvider;
    private Binding<BaseFragment> supertype;

    public DebugSettingsFragment$$InjectAdapter() {
        super("com.microsoft.kapp.fragments.DebugSettingsFragment", "members/com.microsoft.kapp.fragments.DebugSettingsFragment", false, DebugSettingsFragment.class);
    }

    @Override // dagger.internal.Binding
    public void attach(Linker linker) {
        this.mSettingsProvider = linker.requestBinding("com.microsoft.kapp.services.SettingsProvider", DebugSettingsFragment.class, getClass().getClassLoader());
        this.mAuthService = linker.requestBinding("com.microsoft.krestsdk.auth.MsaAuth", DebugSettingsFragment.class, getClass().getClassLoader());
        this.mCredentialsManager = linker.requestBinding("com.microsoft.krestsdk.auth.credentials.CredentialsManager", DebugSettingsFragment.class, getClass().getClassLoader());
        this.mCacheService = linker.requestBinding("com.microsoft.kapp.cache.CacheService", DebugSettingsFragment.class, getClass().getClassLoader());
        this.mFeedbackService = linker.requestBinding("com.microsoft.kapp.feedback.FeedbackService", DebugSettingsFragment.class, getClass().getClassLoader());
        this.supertype = linker.requestBinding("members/com.microsoft.kapp.fragments.BaseFragment", DebugSettingsFragment.class, getClass().getClassLoader(), false, true);
    }

    @Override // dagger.internal.Binding
    public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
        injectMembersBindings.add(this.mSettingsProvider);
        injectMembersBindings.add(this.mAuthService);
        injectMembersBindings.add(this.mCredentialsManager);
        injectMembersBindings.add(this.mCacheService);
        injectMembersBindings.add(this.mFeedbackService);
        injectMembersBindings.add(this.supertype);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // dagger.internal.Binding, javax.inject.Provider
    public DebugSettingsFragment get() {
        DebugSettingsFragment result = new DebugSettingsFragment();
        injectMembers(result);
        return result;
    }

    @Override // dagger.internal.Binding, dagger.MembersInjector
    public void injectMembers(DebugSettingsFragment object) {
        object.mSettingsProvider = this.mSettingsProvider.get();
        object.mAuthService = this.mAuthService.get();
        object.mCredentialsManager = this.mCredentialsManager.get();
        object.mCacheService = this.mCacheService.get();
        object.mFeedbackService = this.mFeedbackService.get();
        this.supertype.injectMembers(object);
    }
}

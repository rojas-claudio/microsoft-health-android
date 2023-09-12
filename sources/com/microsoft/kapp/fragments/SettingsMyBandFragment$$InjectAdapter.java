package com.microsoft.kapp.fragments;

import com.microsoft.kapp.multidevice.SingleDeviceEnforcementManager;
import com.microsoft.kapp.personalization.PersonalizationManagerFactory;
import com.microsoft.kapp.services.SettingsProvider;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class SettingsMyBandFragment$$InjectAdapter extends Binding<SettingsMyBandFragment> implements Provider<SettingsMyBandFragment>, MembersInjector<SettingsMyBandFragment> {
    private Binding<PersonalizationManagerFactory> mPersonalizationManagerFactory;
    private Binding<SettingsProvider> mSettingsProvider;
    private Binding<SingleDeviceEnforcementManager> mSingleDeviceEnforcementManager;
    private Binding<BaseFragment> supertype;

    public SettingsMyBandFragment$$InjectAdapter() {
        super("com.microsoft.kapp.fragments.SettingsMyBandFragment", "members/com.microsoft.kapp.fragments.SettingsMyBandFragment", false, SettingsMyBandFragment.class);
    }

    @Override // dagger.internal.Binding
    public void attach(Linker linker) {
        this.mSettingsProvider = linker.requestBinding("com.microsoft.kapp.services.SettingsProvider", SettingsMyBandFragment.class, getClass().getClassLoader());
        this.mPersonalizationManagerFactory = linker.requestBinding("com.microsoft.kapp.personalization.PersonalizationManagerFactory", SettingsMyBandFragment.class, getClass().getClassLoader());
        this.mSingleDeviceEnforcementManager = linker.requestBinding("com.microsoft.kapp.multidevice.SingleDeviceEnforcementManager", SettingsMyBandFragment.class, getClass().getClassLoader());
        this.supertype = linker.requestBinding("members/com.microsoft.kapp.fragments.BaseFragment", SettingsMyBandFragment.class, getClass().getClassLoader(), false, true);
    }

    @Override // dagger.internal.Binding
    public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
        injectMembersBindings.add(this.mSettingsProvider);
        injectMembersBindings.add(this.mPersonalizationManagerFactory);
        injectMembersBindings.add(this.mSingleDeviceEnforcementManager);
        injectMembersBindings.add(this.supertype);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // dagger.internal.Binding, javax.inject.Provider
    public SettingsMyBandFragment get() {
        SettingsMyBandFragment result = new SettingsMyBandFragment();
        injectMembers(result);
        return result;
    }

    @Override // dagger.internal.Binding, dagger.MembersInjector
    public void injectMembers(SettingsMyBandFragment object) {
        object.mSettingsProvider = this.mSettingsProvider.get();
        object.mPersonalizationManagerFactory = this.mPersonalizationManagerFactory.get();
        object.mSingleDeviceEnforcementManager = this.mSingleDeviceEnforcementManager.get();
        this.supertype.injectMembers(object);
    }
}

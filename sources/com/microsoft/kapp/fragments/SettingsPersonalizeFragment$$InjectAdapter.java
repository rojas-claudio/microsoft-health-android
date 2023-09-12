package com.microsoft.kapp.fragments;

import com.microsoft.kapp.CargoConnection;
import com.microsoft.kapp.personalization.PersonalizationManagerFactory;
import com.microsoft.kapp.services.SettingsProvider;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class SettingsPersonalizeFragment$$InjectAdapter extends Binding<SettingsPersonalizeFragment> implements Provider<SettingsPersonalizeFragment>, MembersInjector<SettingsPersonalizeFragment> {
    private Binding<CargoConnection> mCargoconnection;
    private Binding<PersonalizationManagerFactory> mPersonalizationManagerFactory;
    private Binding<SettingsProvider> mSettingsProvider;
    private Binding<BaseFragment> supertype;

    public SettingsPersonalizeFragment$$InjectAdapter() {
        super("com.microsoft.kapp.fragments.SettingsPersonalizeFragment", "members/com.microsoft.kapp.fragments.SettingsPersonalizeFragment", false, SettingsPersonalizeFragment.class);
    }

    @Override // dagger.internal.Binding
    public void attach(Linker linker) {
        this.mSettingsProvider = linker.requestBinding("com.microsoft.kapp.services.SettingsProvider", SettingsPersonalizeFragment.class, getClass().getClassLoader());
        this.mPersonalizationManagerFactory = linker.requestBinding("com.microsoft.kapp.personalization.PersonalizationManagerFactory", SettingsPersonalizeFragment.class, getClass().getClassLoader());
        this.mCargoconnection = linker.requestBinding("com.microsoft.kapp.CargoConnection", SettingsPersonalizeFragment.class, getClass().getClassLoader());
        this.supertype = linker.requestBinding("members/com.microsoft.kapp.fragments.BaseFragment", SettingsPersonalizeFragment.class, getClass().getClassLoader(), false, true);
    }

    @Override // dagger.internal.Binding
    public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
        injectMembersBindings.add(this.mSettingsProvider);
        injectMembersBindings.add(this.mPersonalizationManagerFactory);
        injectMembersBindings.add(this.mCargoconnection);
        injectMembersBindings.add(this.supertype);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // dagger.internal.Binding, javax.inject.Provider
    public SettingsPersonalizeFragment get() {
        SettingsPersonalizeFragment result = new SettingsPersonalizeFragment();
        injectMembers(result);
        return result;
    }

    @Override // dagger.internal.Binding, dagger.MembersInjector
    public void injectMembers(SettingsPersonalizeFragment object) {
        object.mSettingsProvider = this.mSettingsProvider.get();
        object.mPersonalizationManagerFactory = this.mPersonalizationManagerFactory.get();
        object.mCargoconnection = this.mCargoconnection.get();
        this.supertype.injectMembers(object);
    }
}

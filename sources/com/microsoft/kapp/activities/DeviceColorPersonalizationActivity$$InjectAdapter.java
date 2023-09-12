package com.microsoft.kapp.activities;

import com.microsoft.kapp.personalization.PersonalizationManagerFactory;
import com.microsoft.kapp.services.SettingsProvider;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class DeviceColorPersonalizationActivity$$InjectAdapter extends Binding<DeviceColorPersonalizationActivity> implements Provider<DeviceColorPersonalizationActivity>, MembersInjector<DeviceColorPersonalizationActivity> {
    private Binding<PersonalizationManagerFactory> mPersonalizationManagerFactory;
    private Binding<SettingsProvider> mSettingsProvider;
    private Binding<BaseActivity> supertype;

    public DeviceColorPersonalizationActivity$$InjectAdapter() {
        super("com.microsoft.kapp.activities.DeviceColorPersonalizationActivity", "members/com.microsoft.kapp.activities.DeviceColorPersonalizationActivity", false, DeviceColorPersonalizationActivity.class);
    }

    @Override // dagger.internal.Binding
    public void attach(Linker linker) {
        this.mSettingsProvider = linker.requestBinding("com.microsoft.kapp.services.SettingsProvider", DeviceColorPersonalizationActivity.class, getClass().getClassLoader());
        this.mPersonalizationManagerFactory = linker.requestBinding("com.microsoft.kapp.personalization.PersonalizationManagerFactory", DeviceColorPersonalizationActivity.class, getClass().getClassLoader());
        this.supertype = linker.requestBinding("members/com.microsoft.kapp.activities.BaseActivity", DeviceColorPersonalizationActivity.class, getClass().getClassLoader(), false, true);
    }

    @Override // dagger.internal.Binding
    public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
        injectMembersBindings.add(this.mSettingsProvider);
        injectMembersBindings.add(this.mPersonalizationManagerFactory);
        injectMembersBindings.add(this.supertype);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // dagger.internal.Binding, javax.inject.Provider
    public DeviceColorPersonalizationActivity get() {
        DeviceColorPersonalizationActivity result = new DeviceColorPersonalizationActivity();
        injectMembers(result);
        return result;
    }

    @Override // dagger.internal.Binding, dagger.MembersInjector
    public void injectMembers(DeviceColorPersonalizationActivity object) {
        object.mSettingsProvider = this.mSettingsProvider.get();
        object.mPersonalizationManagerFactory = this.mPersonalizationManagerFactory.get();
        this.supertype.injectMembers(object);
    }
}

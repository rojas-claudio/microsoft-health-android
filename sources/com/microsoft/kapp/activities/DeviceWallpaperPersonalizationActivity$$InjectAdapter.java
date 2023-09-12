package com.microsoft.kapp.activities;

import com.microsoft.kapp.personalization.PersonalizationManagerFactory;
import com.microsoft.kapp.services.SettingsProvider;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class DeviceWallpaperPersonalizationActivity$$InjectAdapter extends Binding<DeviceWallpaperPersonalizationActivity> implements Provider<DeviceWallpaperPersonalizationActivity>, MembersInjector<DeviceWallpaperPersonalizationActivity> {
    private Binding<PersonalizationManagerFactory> mPersonalizationManagerFactory;
    private Binding<SettingsProvider> mSettingsProvider;
    private Binding<BaseActivity> supertype;

    public DeviceWallpaperPersonalizationActivity$$InjectAdapter() {
        super("com.microsoft.kapp.activities.DeviceWallpaperPersonalizationActivity", "members/com.microsoft.kapp.activities.DeviceWallpaperPersonalizationActivity", false, DeviceWallpaperPersonalizationActivity.class);
    }

    @Override // dagger.internal.Binding
    public void attach(Linker linker) {
        this.mSettingsProvider = linker.requestBinding("com.microsoft.kapp.services.SettingsProvider", DeviceWallpaperPersonalizationActivity.class, getClass().getClassLoader());
        this.mPersonalizationManagerFactory = linker.requestBinding("com.microsoft.kapp.personalization.PersonalizationManagerFactory", DeviceWallpaperPersonalizationActivity.class, getClass().getClassLoader());
        this.supertype = linker.requestBinding("members/com.microsoft.kapp.activities.BaseActivity", DeviceWallpaperPersonalizationActivity.class, getClass().getClassLoader(), false, true);
    }

    @Override // dagger.internal.Binding
    public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
        injectMembersBindings.add(this.mSettingsProvider);
        injectMembersBindings.add(this.mPersonalizationManagerFactory);
        injectMembersBindings.add(this.supertype);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // dagger.internal.Binding, javax.inject.Provider
    public DeviceWallpaperPersonalizationActivity get() {
        DeviceWallpaperPersonalizationActivity result = new DeviceWallpaperPersonalizationActivity();
        injectMembers(result);
        return result;
    }

    @Override // dagger.internal.Binding, dagger.MembersInjector
    public void injectMembers(DeviceWallpaperPersonalizationActivity object) {
        object.mSettingsProvider = this.mSettingsProvider.get();
        object.mPersonalizationManagerFactory = this.mPersonalizationManagerFactory.get();
        this.supertype.injectMembers(object);
    }
}

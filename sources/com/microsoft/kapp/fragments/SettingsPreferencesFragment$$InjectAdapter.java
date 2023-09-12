package com.microsoft.kapp.fragments;

import com.microsoft.kapp.UserProfileFetcher;
import com.microsoft.kapp.sensor.SensorUtils;
import com.microsoft.kapp.services.SettingsProvider;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class SettingsPreferencesFragment$$InjectAdapter extends Binding<SettingsPreferencesFragment> implements Provider<SettingsPreferencesFragment>, MembersInjector<SettingsPreferencesFragment> {
    private Binding<SensorUtils> mSensorUtils;
    private Binding<SettingsProvider> mSettingsProvider;
    private Binding<UserProfileFetcher> mUserProfileFetcher;
    private Binding<BaseFragmentWithOfflineSupport> supertype;

    public SettingsPreferencesFragment$$InjectAdapter() {
        super("com.microsoft.kapp.fragments.SettingsPreferencesFragment", "members/com.microsoft.kapp.fragments.SettingsPreferencesFragment", false, SettingsPreferencesFragment.class);
    }

    @Override // dagger.internal.Binding
    public void attach(Linker linker) {
        this.mSettingsProvider = linker.requestBinding("com.microsoft.kapp.services.SettingsProvider", SettingsPreferencesFragment.class, getClass().getClassLoader());
        this.mUserProfileFetcher = linker.requestBinding("com.microsoft.kapp.UserProfileFetcher", SettingsPreferencesFragment.class, getClass().getClassLoader());
        this.mSensorUtils = linker.requestBinding("com.microsoft.kapp.sensor.SensorUtils", SettingsPreferencesFragment.class, getClass().getClassLoader());
        this.supertype = linker.requestBinding("members/com.microsoft.kapp.fragments.BaseFragmentWithOfflineSupport", SettingsPreferencesFragment.class, getClass().getClassLoader(), false, true);
    }

    @Override // dagger.internal.Binding
    public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
        injectMembersBindings.add(this.mSettingsProvider);
        injectMembersBindings.add(this.mUserProfileFetcher);
        injectMembersBindings.add(this.mSensorUtils);
        injectMembersBindings.add(this.supertype);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // dagger.internal.Binding, javax.inject.Provider
    public SettingsPreferencesFragment get() {
        SettingsPreferencesFragment result = new SettingsPreferencesFragment();
        injectMembers(result);
        return result;
    }

    @Override // dagger.internal.Binding, dagger.MembersInjector
    public void injectMembers(SettingsPreferencesFragment object) {
        object.mSettingsProvider = this.mSettingsProvider.get();
        object.mUserProfileFetcher = this.mUserProfileFetcher.get();
        object.mSensorUtils = this.mSensorUtils.get();
        this.supertype.injectMembers(object);
    }
}

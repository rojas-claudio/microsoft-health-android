package com.microsoft.kapp.fragments;

import com.microsoft.kapp.UserProfileFetcher;
import com.microsoft.kapp.sensor.SensorUtils;
import com.microsoft.kapp.services.SettingsProvider;
import com.microsoft.krestsdk.auth.credentials.CredentialsManager;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class MotionSettingsFragment$$InjectAdapter extends Binding<MotionSettingsFragment> implements Provider<MotionSettingsFragment>, MembersInjector<MotionSettingsFragment> {
    private Binding<CredentialsManager> mCredentialsManager;
    private Binding<UserProfileFetcher> mProfileFetcher;
    private Binding<SensorUtils> mSensorUtils;
    private Binding<SettingsProvider> mSettingsProvider;
    private Binding<BaseFragment> supertype;

    public MotionSettingsFragment$$InjectAdapter() {
        super("com.microsoft.kapp.fragments.MotionSettingsFragment", "members/com.microsoft.kapp.fragments.MotionSettingsFragment", false, MotionSettingsFragment.class);
    }

    @Override // dagger.internal.Binding
    public void attach(Linker linker) {
        this.mSettingsProvider = linker.requestBinding("com.microsoft.kapp.services.SettingsProvider", MotionSettingsFragment.class, getClass().getClassLoader());
        this.mCredentialsManager = linker.requestBinding("com.microsoft.krestsdk.auth.credentials.CredentialsManager", MotionSettingsFragment.class, getClass().getClassLoader());
        this.mSensorUtils = linker.requestBinding("com.microsoft.kapp.sensor.SensorUtils", MotionSettingsFragment.class, getClass().getClassLoader());
        this.mProfileFetcher = linker.requestBinding("com.microsoft.kapp.UserProfileFetcher", MotionSettingsFragment.class, getClass().getClassLoader());
        this.supertype = linker.requestBinding("members/com.microsoft.kapp.fragments.BaseFragment", MotionSettingsFragment.class, getClass().getClassLoader(), false, true);
    }

    @Override // dagger.internal.Binding
    public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
        injectMembersBindings.add(this.mSettingsProvider);
        injectMembersBindings.add(this.mCredentialsManager);
        injectMembersBindings.add(this.mSensorUtils);
        injectMembersBindings.add(this.mProfileFetcher);
        injectMembersBindings.add(this.supertype);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // dagger.internal.Binding, javax.inject.Provider
    public MotionSettingsFragment get() {
        MotionSettingsFragment result = new MotionSettingsFragment();
        injectMembers(result);
        return result;
    }

    @Override // dagger.internal.Binding, dagger.MembersInjector
    public void injectMembers(MotionSettingsFragment object) {
        object.mSettingsProvider = this.mSettingsProvider.get();
        object.mCredentialsManager = this.mCredentialsManager.get();
        object.mSensorUtils = this.mSensorUtils.get();
        object.mProfileFetcher = this.mProfileFetcher.get();
        this.supertype.injectMembers(object);
    }
}

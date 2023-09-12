package com.microsoft.kapp.services;

import com.microsoft.kapp.UserProfileFetcher;
import com.microsoft.kapp.multidevice.MultiDeviceManager;
import com.microsoft.kapp.services.timeZone.TimeZoneChangeHandler;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class KAppBroadcastReceiver$$InjectAdapter extends Binding<KAppBroadcastReceiver> implements Provider<KAppBroadcastReceiver>, MembersInjector<KAppBroadcastReceiver> {
    private Binding<MultiDeviceManager> mMultiDeviceManager;
    private Binding<SettingsProvider> mSettingsProvider;
    private Binding<TimeZoneChangeHandler> mTimeZoneChangeHandler;
    private Binding<UserProfileFetcher> mUserProfileFetcher;

    public KAppBroadcastReceiver$$InjectAdapter() {
        super("com.microsoft.kapp.services.KAppBroadcastReceiver", "members/com.microsoft.kapp.services.KAppBroadcastReceiver", false, KAppBroadcastReceiver.class);
    }

    @Override // dagger.internal.Binding
    public void attach(Linker linker) {
        this.mSettingsProvider = linker.requestBinding("com.microsoft.kapp.services.SettingsProvider", KAppBroadcastReceiver.class, getClass().getClassLoader());
        this.mMultiDeviceManager = linker.requestBinding("com.microsoft.kapp.multidevice.MultiDeviceManager", KAppBroadcastReceiver.class, getClass().getClassLoader());
        this.mTimeZoneChangeHandler = linker.requestBinding("com.microsoft.kapp.services.timeZone.TimeZoneChangeHandler", KAppBroadcastReceiver.class, getClass().getClassLoader());
        this.mUserProfileFetcher = linker.requestBinding("com.microsoft.kapp.UserProfileFetcher", KAppBroadcastReceiver.class, getClass().getClassLoader());
    }

    @Override // dagger.internal.Binding
    public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
        injectMembersBindings.add(this.mSettingsProvider);
        injectMembersBindings.add(this.mMultiDeviceManager);
        injectMembersBindings.add(this.mTimeZoneChangeHandler);
        injectMembersBindings.add(this.mUserProfileFetcher);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // dagger.internal.Binding, javax.inject.Provider
    public KAppBroadcastReceiver get() {
        KAppBroadcastReceiver result = new KAppBroadcastReceiver();
        injectMembers(result);
        return result;
    }

    @Override // dagger.internal.Binding, dagger.MembersInjector
    public void injectMembers(KAppBroadcastReceiver object) {
        object.mSettingsProvider = this.mSettingsProvider.get();
        object.mMultiDeviceManager = this.mMultiDeviceManager.get();
        object.mTimeZoneChangeHandler = this.mTimeZoneChangeHandler.get();
        object.mUserProfileFetcher = this.mUserProfileFetcher.get();
    }
}

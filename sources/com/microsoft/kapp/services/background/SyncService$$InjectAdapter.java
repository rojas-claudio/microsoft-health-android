package com.microsoft.kapp.services.background;

import android.content.Context;
import com.microsoft.kapp.multidevice.MultiDeviceManager;
import com.microsoft.kapp.services.InjectableIntentService;
import com.microsoft.kapp.services.SettingsProvider;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class SyncService$$InjectAdapter extends Binding<SyncService> implements Provider<SyncService>, MembersInjector<SyncService> {
    private Binding<Context> mContext;
    private Binding<MultiDeviceManager> mMultiDeviceManager;
    private Binding<SettingsProvider> mSettingsProvider;
    private Binding<InjectableIntentService> supertype;

    public SyncService$$InjectAdapter() {
        super("com.microsoft.kapp.services.background.SyncService", "members/com.microsoft.kapp.services.background.SyncService", false, SyncService.class);
    }

    @Override // dagger.internal.Binding
    public void attach(Linker linker) {
        this.mSettingsProvider = linker.requestBinding("com.microsoft.kapp.services.SettingsProvider", SyncService.class, getClass().getClassLoader());
        this.mContext = linker.requestBinding("android.content.Context", SyncService.class, getClass().getClassLoader());
        this.mMultiDeviceManager = linker.requestBinding("com.microsoft.kapp.multidevice.MultiDeviceManager", SyncService.class, getClass().getClassLoader());
        this.supertype = linker.requestBinding("members/com.microsoft.kapp.services.InjectableIntentService", SyncService.class, getClass().getClassLoader(), false, true);
    }

    @Override // dagger.internal.Binding
    public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
        injectMembersBindings.add(this.mSettingsProvider);
        injectMembersBindings.add(this.mContext);
        injectMembersBindings.add(this.mMultiDeviceManager);
        injectMembersBindings.add(this.supertype);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // dagger.internal.Binding, javax.inject.Provider
    public SyncService get() {
        SyncService result = new SyncService();
        injectMembers(result);
        return result;
    }

    @Override // dagger.internal.Binding, dagger.MembersInjector
    public void injectMembers(SyncService object) {
        object.mSettingsProvider = this.mSettingsProvider.get();
        object.mContext = this.mContext.get();
        object.mMultiDeviceManager = this.mMultiDeviceManager.get();
        this.supertype.injectMembers(object);
    }
}

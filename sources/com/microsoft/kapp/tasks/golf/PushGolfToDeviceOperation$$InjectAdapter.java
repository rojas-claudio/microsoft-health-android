package com.microsoft.kapp.tasks.golf;

import com.microsoft.kapp.CargoConnection;
import com.microsoft.kapp.cache.CacheService;
import com.microsoft.kapp.multidevice.MultiDeviceManager;
import com.microsoft.kapp.services.SettingsProvider;
import com.microsoft.kapp.services.golf.GolfService;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;
/* loaded from: classes.dex */
public final class PushGolfToDeviceOperation$$InjectAdapter extends Binding<PushGolfToDeviceOperation> implements MembersInjector<PushGolfToDeviceOperation> {
    private Binding<CacheService> mCacheService;
    private Binding<CargoConnection> mCargoConnection;
    private Binding<GolfService> mGolfService;
    private Binding<MultiDeviceManager> mMultiDeviceManger;
    private Binding<SettingsProvider> mSettingsProvider;

    public PushGolfToDeviceOperation$$InjectAdapter() {
        super(null, "members/com.microsoft.kapp.tasks.golf.PushGolfToDeviceOperation", false, PushGolfToDeviceOperation.class);
    }

    @Override // dagger.internal.Binding
    public void attach(Linker linker) {
        this.mGolfService = linker.requestBinding("com.microsoft.kapp.services.golf.GolfService", PushGolfToDeviceOperation.class, getClass().getClassLoader());
        this.mCargoConnection = linker.requestBinding("com.microsoft.kapp.CargoConnection", PushGolfToDeviceOperation.class, getClass().getClassLoader());
        this.mMultiDeviceManger = linker.requestBinding("com.microsoft.kapp.multidevice.MultiDeviceManager", PushGolfToDeviceOperation.class, getClass().getClassLoader());
        this.mSettingsProvider = linker.requestBinding("com.microsoft.kapp.services.SettingsProvider", PushGolfToDeviceOperation.class, getClass().getClassLoader());
        this.mCacheService = linker.requestBinding("com.microsoft.kapp.cache.CacheService", PushGolfToDeviceOperation.class, getClass().getClassLoader());
    }

    @Override // dagger.internal.Binding
    public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
        injectMembersBindings.add(this.mGolfService);
        injectMembersBindings.add(this.mCargoConnection);
        injectMembersBindings.add(this.mMultiDeviceManger);
        injectMembersBindings.add(this.mSettingsProvider);
        injectMembersBindings.add(this.mCacheService);
    }

    @Override // dagger.internal.Binding, dagger.MembersInjector
    public void injectMembers(PushGolfToDeviceOperation object) {
        object.mGolfService = this.mGolfService.get();
        object.mCargoConnection = this.mCargoConnection.get();
        object.mMultiDeviceManger = this.mMultiDeviceManger.get();
        object.mSettingsProvider = this.mSettingsProvider.get();
        object.mCacheService = this.mCacheService.get();
    }
}

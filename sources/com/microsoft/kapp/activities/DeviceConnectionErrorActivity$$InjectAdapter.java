package com.microsoft.kapp.activities;

import com.microsoft.kapp.DeviceStateDisplayManager;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class DeviceConnectionErrorActivity$$InjectAdapter extends Binding<DeviceConnectionErrorActivity> implements Provider<DeviceConnectionErrorActivity>, MembersInjector<DeviceConnectionErrorActivity> {
    private Binding<DeviceStateDisplayManager> mDeviceStateDisplayManager;

    public DeviceConnectionErrorActivity$$InjectAdapter() {
        super("com.microsoft.kapp.activities.DeviceConnectionErrorActivity", "members/com.microsoft.kapp.activities.DeviceConnectionErrorActivity", false, DeviceConnectionErrorActivity.class);
    }

    @Override // dagger.internal.Binding
    public void attach(Linker linker) {
        this.mDeviceStateDisplayManager = linker.requestBinding("com.microsoft.kapp.DeviceStateDisplayManager", DeviceConnectionErrorActivity.class, getClass().getClassLoader());
    }

    @Override // dagger.internal.Binding
    public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
        injectMembersBindings.add(this.mDeviceStateDisplayManager);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // dagger.internal.Binding, javax.inject.Provider
    public DeviceConnectionErrorActivity get() {
        DeviceConnectionErrorActivity result = new DeviceConnectionErrorActivity();
        injectMembers(result);
        return result;
    }

    @Override // dagger.internal.Binding, dagger.MembersInjector
    public void injectMembers(DeviceConnectionErrorActivity object) {
        object.mDeviceStateDisplayManager = this.mDeviceStateDisplayManager.get();
    }
}

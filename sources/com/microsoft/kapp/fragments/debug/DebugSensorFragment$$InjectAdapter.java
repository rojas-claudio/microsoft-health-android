package com.microsoft.kapp.fragments.debug;

import android.content.Context;
import com.microsoft.kapp.fragments.BaseFragment;
import com.microsoft.kapp.multidevice.MultiDeviceManager;
import com.microsoft.kapp.sensor.PhoneSensorDataProvider;
import com.microsoft.kapp.sensor.SensorDataDebugProvider;
import com.microsoft.kapp.sensor.SensorUtils;
import com.microsoft.kapp.services.SettingsProvider;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class DebugSensorFragment$$InjectAdapter extends Binding<DebugSensorFragment> implements Provider<DebugSensorFragment>, MembersInjector<DebugSensorFragment> {
    private Binding<Context> mContext;
    private Binding<MultiDeviceManager> mMultiDeviceManager;
    private Binding<PhoneSensorDataProvider> mPhoneSensorDataProvider;
    private Binding<SensorDataDebugProvider> mSensorDataDebugProvider;
    private Binding<SensorUtils> mSensorUtils;
    private Binding<SettingsProvider> mSettingsProvider;
    private Binding<BaseFragment> supertype;

    public DebugSensorFragment$$InjectAdapter() {
        super("com.microsoft.kapp.fragments.debug.DebugSensorFragment", "members/com.microsoft.kapp.fragments.debug.DebugSensorFragment", false, DebugSensorFragment.class);
    }

    @Override // dagger.internal.Binding
    public void attach(Linker linker) {
        this.mSettingsProvider = linker.requestBinding("com.microsoft.kapp.services.SettingsProvider", DebugSensorFragment.class, getClass().getClassLoader());
        this.mSensorDataDebugProvider = linker.requestBinding("com.microsoft.kapp.sensor.SensorDataDebugProvider", DebugSensorFragment.class, getClass().getClassLoader());
        this.mPhoneSensorDataProvider = linker.requestBinding("com.microsoft.kapp.sensor.PhoneSensorDataProvider", DebugSensorFragment.class, getClass().getClassLoader());
        this.mContext = linker.requestBinding("android.content.Context", DebugSensorFragment.class, getClass().getClassLoader());
        this.mSensorUtils = linker.requestBinding("com.microsoft.kapp.sensor.SensorUtils", DebugSensorFragment.class, getClass().getClassLoader());
        this.mMultiDeviceManager = linker.requestBinding("com.microsoft.kapp.multidevice.MultiDeviceManager", DebugSensorFragment.class, getClass().getClassLoader());
        this.supertype = linker.requestBinding("members/com.microsoft.kapp.fragments.BaseFragment", DebugSensorFragment.class, getClass().getClassLoader(), false, true);
    }

    @Override // dagger.internal.Binding
    public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
        injectMembersBindings.add(this.mSettingsProvider);
        injectMembersBindings.add(this.mSensorDataDebugProvider);
        injectMembersBindings.add(this.mPhoneSensorDataProvider);
        injectMembersBindings.add(this.mContext);
        injectMembersBindings.add(this.mSensorUtils);
        injectMembersBindings.add(this.mMultiDeviceManager);
        injectMembersBindings.add(this.supertype);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // dagger.internal.Binding, javax.inject.Provider
    public DebugSensorFragment get() {
        DebugSensorFragment result = new DebugSensorFragment();
        injectMembers(result);
        return result;
    }

    @Override // dagger.internal.Binding, dagger.MembersInjector
    public void injectMembers(DebugSensorFragment object) {
        object.mSettingsProvider = this.mSettingsProvider.get();
        object.mSensorDataDebugProvider = this.mSensorDataDebugProvider.get();
        object.mPhoneSensorDataProvider = this.mPhoneSensorDataProvider.get();
        object.mContext = this.mContext.get();
        object.mSensorUtils = this.mSensorUtils.get();
        object.mMultiDeviceManager = this.mMultiDeviceManager.get();
        this.supertype.injectMembers(object);
    }
}

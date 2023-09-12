package com.microsoft.kapp.fragments;

import com.microsoft.kapp.sensor.SensorUtils;
import com.microsoft.kapp.services.SettingsProvider;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class OobeBluetoothConnectionFragment$$InjectAdapter extends Binding<OobeBluetoothConnectionFragment> implements Provider<OobeBluetoothConnectionFragment>, MembersInjector<OobeBluetoothConnectionFragment> {
    private Binding<SensorUtils> mSensorUtils;
    private Binding<SettingsProvider> mSettingsProvider;
    private Binding<BaseFragment> supertype;

    public OobeBluetoothConnectionFragment$$InjectAdapter() {
        super("com.microsoft.kapp.fragments.OobeBluetoothConnectionFragment", "members/com.microsoft.kapp.fragments.OobeBluetoothConnectionFragment", false, OobeBluetoothConnectionFragment.class);
    }

    @Override // dagger.internal.Binding
    public void attach(Linker linker) {
        this.mSettingsProvider = linker.requestBinding("com.microsoft.kapp.services.SettingsProvider", OobeBluetoothConnectionFragment.class, getClass().getClassLoader());
        this.mSensorUtils = linker.requestBinding("com.microsoft.kapp.sensor.SensorUtils", OobeBluetoothConnectionFragment.class, getClass().getClassLoader());
        this.supertype = linker.requestBinding("members/com.microsoft.kapp.fragments.BaseFragment", OobeBluetoothConnectionFragment.class, getClass().getClassLoader(), false, true);
    }

    @Override // dagger.internal.Binding
    public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
        injectMembersBindings.add(this.mSettingsProvider);
        injectMembersBindings.add(this.mSensorUtils);
        injectMembersBindings.add(this.supertype);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // dagger.internal.Binding, javax.inject.Provider
    public OobeBluetoothConnectionFragment get() {
        OobeBluetoothConnectionFragment result = new OobeBluetoothConnectionFragment();
        injectMembers(result);
        return result;
    }

    @Override // dagger.internal.Binding, dagger.MembersInjector
    public void injectMembers(OobeBluetoothConnectionFragment object) {
        object.mSettingsProvider = this.mSettingsProvider.get();
        object.mSensorUtils = this.mSensorUtils.get();
        this.supertype.injectMembers(object);
    }
}

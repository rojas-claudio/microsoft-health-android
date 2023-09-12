package com.microsoft.kapp.fragments;

import com.microsoft.kapp.services.SettingsProvider;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class OobeBluetoothCompleteFragment$$InjectAdapter extends Binding<OobeBluetoothCompleteFragment> implements Provider<OobeBluetoothCompleteFragment>, MembersInjector<OobeBluetoothCompleteFragment> {
    private Binding<SettingsProvider> mSettingsProvider;
    private Binding<BaseFragment> supertype;

    public OobeBluetoothCompleteFragment$$InjectAdapter() {
        super("com.microsoft.kapp.fragments.OobeBluetoothCompleteFragment", "members/com.microsoft.kapp.fragments.OobeBluetoothCompleteFragment", false, OobeBluetoothCompleteFragment.class);
    }

    @Override // dagger.internal.Binding
    public void attach(Linker linker) {
        this.mSettingsProvider = linker.requestBinding("com.microsoft.kapp.services.SettingsProvider", OobeBluetoothCompleteFragment.class, getClass().getClassLoader());
        this.supertype = linker.requestBinding("members/com.microsoft.kapp.fragments.BaseFragment", OobeBluetoothCompleteFragment.class, getClass().getClassLoader(), false, true);
    }

    @Override // dagger.internal.Binding
    public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
        injectMembersBindings.add(this.mSettingsProvider);
        injectMembersBindings.add(this.supertype);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // dagger.internal.Binding, javax.inject.Provider
    public OobeBluetoothCompleteFragment get() {
        OobeBluetoothCompleteFragment result = new OobeBluetoothCompleteFragment();
        injectMembers(result);
        return result;
    }

    @Override // dagger.internal.Binding, dagger.MembersInjector
    public void injectMembers(OobeBluetoothCompleteFragment object) {
        object.mSettingsProvider = this.mSettingsProvider.get();
        this.supertype.injectMembers(object);
    }
}

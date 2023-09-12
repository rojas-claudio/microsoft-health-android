package com.microsoft.kapp.activities;

import com.microsoft.kapp.CargoConnection;
import com.microsoft.kapp.services.SettingsProvider;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;
/* loaded from: classes.dex */
public final class BaseFragmentActivity$$InjectAdapter extends Binding<BaseFragmentActivity> implements MembersInjector<BaseFragmentActivity> {
    private Binding<CargoConnection> mCargoConnection;
    private Binding<SettingsProvider> mSettingsProvider;

    public BaseFragmentActivity$$InjectAdapter() {
        super(null, "members/com.microsoft.kapp.activities.BaseFragmentActivity", false, BaseFragmentActivity.class);
    }

    @Override // dagger.internal.Binding
    public void attach(Linker linker) {
        this.mCargoConnection = linker.requestBinding("com.microsoft.kapp.CargoConnection", BaseFragmentActivity.class, getClass().getClassLoader());
        this.mSettingsProvider = linker.requestBinding("com.microsoft.kapp.services.SettingsProvider", BaseFragmentActivity.class, getClass().getClassLoader());
    }

    @Override // dagger.internal.Binding
    public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
        injectMembersBindings.add(this.mCargoConnection);
        injectMembersBindings.add(this.mSettingsProvider);
    }

    @Override // dagger.internal.Binding, dagger.MembersInjector
    public void injectMembers(BaseFragmentActivity object) {
        object.mCargoConnection = this.mCargoConnection.get();
        object.mSettingsProvider = this.mSettingsProvider.get();
    }
}

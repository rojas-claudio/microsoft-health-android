package com.microsoft.kapp.activities;

import com.microsoft.kapp.CargoConnection;
import com.microsoft.kapp.sensor.SensorUtils;
import com.microsoft.kapp.services.SettingsProvider;
import com.microsoft.kapp.utils.DialogManagerImpl;
import com.microsoft.kapp.utils.StubDialogManager;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;
/* loaded from: classes.dex */
public final class OobeBaseActivity$$InjectAdapter extends Binding<OobeBaseActivity> implements MembersInjector<OobeBaseActivity> {
    private Binding<CargoConnection> mCargoConnection;
    private Binding<DialogManagerImpl> mDialogManager;
    private Binding<SensorUtils> mSensorUtils;
    private Binding<SettingsProvider> mSettingsProvider;
    private Binding<StubDialogManager> mStubDialogManager;

    public OobeBaseActivity$$InjectAdapter() {
        super(null, "members/com.microsoft.kapp.activities.OobeBaseActivity", false, OobeBaseActivity.class);
    }

    @Override // dagger.internal.Binding
    public void attach(Linker linker) {
        this.mSettingsProvider = linker.requestBinding("com.microsoft.kapp.services.SettingsProvider", OobeBaseActivity.class, getClass().getClassLoader());
        this.mCargoConnection = linker.requestBinding("com.microsoft.kapp.CargoConnection", OobeBaseActivity.class, getClass().getClassLoader());
        this.mDialogManager = linker.requestBinding("com.microsoft.kapp.utils.DialogManagerImpl", OobeBaseActivity.class, getClass().getClassLoader());
        this.mStubDialogManager = linker.requestBinding("com.microsoft.kapp.utils.StubDialogManager", OobeBaseActivity.class, getClass().getClassLoader());
        this.mSensorUtils = linker.requestBinding("com.microsoft.kapp.sensor.SensorUtils", OobeBaseActivity.class, getClass().getClassLoader());
    }

    @Override // dagger.internal.Binding
    public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
        injectMembersBindings.add(this.mSettingsProvider);
        injectMembersBindings.add(this.mCargoConnection);
        injectMembersBindings.add(this.mDialogManager);
        injectMembersBindings.add(this.mStubDialogManager);
        injectMembersBindings.add(this.mSensorUtils);
    }

    @Override // dagger.internal.Binding, dagger.MembersInjector
    public void injectMembers(OobeBaseActivity object) {
        object.mSettingsProvider = this.mSettingsProvider.get();
        object.mCargoConnection = this.mCargoConnection.get();
        object.mDialogManager = this.mDialogManager.get();
        object.mStubDialogManager = this.mStubDialogManager.get();
        object.mSensorUtils = this.mSensorUtils.get();
    }
}

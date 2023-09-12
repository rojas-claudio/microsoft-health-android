package com.microsoft.kapp.activities;

import com.microsoft.kapp.CargoConnection;
import com.microsoft.kapp.services.SettingsProvider;
import com.microsoft.kapp.utils.DialogManagerImpl;
import com.microsoft.kapp.utils.StubDialogManager;
import com.microsoft.kapp.version.VersionUpdateInteractionCoordinator;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;
/* loaded from: classes.dex */
public final class BaseActivityAdapter$$InjectAdapter extends Binding<BaseActivityAdapter> implements MembersInjector<BaseActivityAdapter> {
    private Binding<CargoConnection> mCargoConnection;
    private Binding<DialogManagerImpl> mDialogManager;
    private Binding<SettingsProvider> mSettingsProvider;
    private Binding<StubDialogManager> mStubDialogManager;
    private Binding<VersionUpdateInteractionCoordinator> mVersionUpdateInteractionCoordinator;

    public BaseActivityAdapter$$InjectAdapter() {
        super(null, "members/com.microsoft.kapp.activities.BaseActivityAdapter", false, BaseActivityAdapter.class);
    }

    @Override // dagger.internal.Binding
    public void attach(Linker linker) {
        this.mVersionUpdateInteractionCoordinator = linker.requestBinding("com.microsoft.kapp.version.VersionUpdateInteractionCoordinator", BaseActivityAdapter.class, getClass().getClassLoader());
        this.mSettingsProvider = linker.requestBinding("com.microsoft.kapp.services.SettingsProvider", BaseActivityAdapter.class, getClass().getClassLoader());
        this.mCargoConnection = linker.requestBinding("com.microsoft.kapp.CargoConnection", BaseActivityAdapter.class, getClass().getClassLoader());
        this.mDialogManager = linker.requestBinding("com.microsoft.kapp.utils.DialogManagerImpl", BaseActivityAdapter.class, getClass().getClassLoader());
        this.mStubDialogManager = linker.requestBinding("com.microsoft.kapp.utils.StubDialogManager", BaseActivityAdapter.class, getClass().getClassLoader());
    }

    @Override // dagger.internal.Binding
    public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
        injectMembersBindings.add(this.mVersionUpdateInteractionCoordinator);
        injectMembersBindings.add(this.mSettingsProvider);
        injectMembersBindings.add(this.mCargoConnection);
        injectMembersBindings.add(this.mDialogManager);
        injectMembersBindings.add(this.mStubDialogManager);
    }

    @Override // dagger.internal.Binding, dagger.MembersInjector
    public void injectMembers(BaseActivityAdapter object) {
        object.mVersionUpdateInteractionCoordinator = this.mVersionUpdateInteractionCoordinator.get();
        object.mSettingsProvider = this.mSettingsProvider.get();
        object.mCargoConnection = this.mCargoConnection.get();
        object.mDialogManager = this.mDialogManager.get();
        object.mStubDialogManager = this.mStubDialogManager.get();
    }
}

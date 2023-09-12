package com.microsoft.kapp.fragments;

import com.microsoft.kapp.CargoConnection;
import com.microsoft.kapp.FontManager;
import com.microsoft.kapp.multidevice.MultiDeviceManager;
import com.microsoft.kapp.services.timeZone.TimeZoneChangeHandler;
import com.microsoft.kapp.version.VersionUpdateInteractionCoordinator;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;
/* loaded from: classes.dex */
public final class BaseFragment$$InjectAdapter extends Binding<BaseFragment> implements MembersInjector<BaseFragment> {
    private Binding<CargoConnection> mCargoConnection;
    private Binding<FontManager> mFontManager;
    private Binding<MultiDeviceManager> mMultiDeviceManager;
    private Binding<TimeZoneChangeHandler> mTimeZoneChangeHandler;
    private Binding<VersionUpdateInteractionCoordinator> mVersionUpdateInteractionCoordinator;

    public BaseFragment$$InjectAdapter() {
        super(null, "members/com.microsoft.kapp.fragments.BaseFragment", false, BaseFragment.class);
    }

    @Override // dagger.internal.Binding
    public void attach(Linker linker) {
        this.mVersionUpdateInteractionCoordinator = linker.requestBinding("com.microsoft.kapp.version.VersionUpdateInteractionCoordinator", BaseFragment.class, getClass().getClassLoader());
        this.mMultiDeviceManager = linker.requestBinding("com.microsoft.kapp.multidevice.MultiDeviceManager", BaseFragment.class, getClass().getClassLoader());
        this.mTimeZoneChangeHandler = linker.requestBinding("com.microsoft.kapp.services.timeZone.TimeZoneChangeHandler", BaseFragment.class, getClass().getClassLoader());
        this.mCargoConnection = linker.requestBinding("com.microsoft.kapp.CargoConnection", BaseFragment.class, getClass().getClassLoader());
        this.mFontManager = linker.requestBinding("com.microsoft.kapp.FontManager", BaseFragment.class, getClass().getClassLoader());
    }

    @Override // dagger.internal.Binding
    public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
        injectMembersBindings.add(this.mVersionUpdateInteractionCoordinator);
        injectMembersBindings.add(this.mMultiDeviceManager);
        injectMembersBindings.add(this.mTimeZoneChangeHandler);
        injectMembersBindings.add(this.mCargoConnection);
        injectMembersBindings.add(this.mFontManager);
    }

    @Override // dagger.internal.Binding, dagger.MembersInjector
    public void injectMembers(BaseFragment object) {
        object.mVersionUpdateInteractionCoordinator = this.mVersionUpdateInteractionCoordinator.get();
        object.mMultiDeviceManager = this.mMultiDeviceManager.get();
        object.mTimeZoneChangeHandler = this.mTimeZoneChangeHandler.get();
        object.mCargoConnection = this.mCargoConnection.get();
        object.mFontManager = this.mFontManager.get();
    }
}

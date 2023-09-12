package com.microsoft.kapp.widgets;

import com.microsoft.kapp.multidevice.MultiDeviceManager;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;
/* loaded from: classes.dex */
public final class HeaderBar$$InjectAdapter extends Binding<HeaderBar> implements MembersInjector<HeaderBar> {
    private Binding<MultiDeviceManager> mMultiDeviceManager;

    public HeaderBar$$InjectAdapter() {
        super(null, "members/com.microsoft.kapp.widgets.HeaderBar", false, HeaderBar.class);
    }

    @Override // dagger.internal.Binding
    public void attach(Linker linker) {
        this.mMultiDeviceManager = linker.requestBinding("com.microsoft.kapp.multidevice.MultiDeviceManager", HeaderBar.class, getClass().getClassLoader());
    }

    @Override // dagger.internal.Binding
    public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
        injectMembersBindings.add(this.mMultiDeviceManager);
    }

    @Override // dagger.internal.Binding, dagger.MembersInjector
    public void injectMembers(HeaderBar object) {
        object.mMultiDeviceManager = this.mMultiDeviceManager.get();
    }
}

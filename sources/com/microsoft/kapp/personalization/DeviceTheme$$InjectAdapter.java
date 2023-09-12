package com.microsoft.kapp.personalization;

import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;
/* loaded from: classes.dex */
public final class DeviceTheme$$InjectAdapter extends Binding<DeviceTheme> implements MembersInjector<DeviceTheme> {
    private Binding<PersonalizationManagerFactory> mPersonalizationManagerFactory;

    public DeviceTheme$$InjectAdapter() {
        super(null, "members/com.microsoft.kapp.personalization.DeviceTheme", false, DeviceTheme.class);
    }

    @Override // dagger.internal.Binding
    public void attach(Linker linker) {
        this.mPersonalizationManagerFactory = linker.requestBinding("com.microsoft.kapp.personalization.PersonalizationManagerFactory", DeviceTheme.class, getClass().getClassLoader());
    }

    @Override // dagger.internal.Binding
    public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
        injectMembersBindings.add(this.mPersonalizationManagerFactory);
    }

    @Override // dagger.internal.Binding, dagger.MembersInjector
    public void injectMembers(DeviceTheme object) {
        object.mPersonalizationManagerFactory = this.mPersonalizationManagerFactory.get();
    }
}

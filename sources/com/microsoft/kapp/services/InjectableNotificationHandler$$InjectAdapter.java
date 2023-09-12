package com.microsoft.kapp.services;

import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;
/* loaded from: classes.dex */
public final class InjectableNotificationHandler$$InjectAdapter extends Binding<InjectableNotificationHandler> implements MembersInjector<InjectableNotificationHandler> {
    private Binding<SettingsProvider> mSettingsProvider;

    public InjectableNotificationHandler$$InjectAdapter() {
        super(null, "members/com.microsoft.kapp.services.InjectableNotificationHandler", false, InjectableNotificationHandler.class);
    }

    @Override // dagger.internal.Binding
    public void attach(Linker linker) {
        this.mSettingsProvider = linker.requestBinding("com.microsoft.kapp.services.SettingsProvider", InjectableNotificationHandler.class, getClass().getClassLoader());
    }

    @Override // dagger.internal.Binding
    public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
        injectMembersBindings.add(this.mSettingsProvider);
    }

    @Override // dagger.internal.Binding, dagger.MembersInjector
    public void injectMembers(InjectableNotificationHandler object) {
        object.mSettingsProvider = this.mSettingsProvider.get();
    }
}

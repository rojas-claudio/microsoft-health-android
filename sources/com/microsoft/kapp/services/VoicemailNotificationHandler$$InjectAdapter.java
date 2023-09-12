package com.microsoft.kapp.services;

import com.microsoft.kapp.CargoConnection;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;
/* loaded from: classes.dex */
public final class VoicemailNotificationHandler$$InjectAdapter extends Binding<VoicemailNotificationHandler> implements MembersInjector<VoicemailNotificationHandler> {
    private Binding<CargoConnection> mCargoConnection;
    private Binding<InjectableNotificationHandler> supertype;

    public VoicemailNotificationHandler$$InjectAdapter() {
        super(null, "members/com.microsoft.kapp.services.VoicemailNotificationHandler", false, VoicemailNotificationHandler.class);
    }

    @Override // dagger.internal.Binding
    public void attach(Linker linker) {
        this.mCargoConnection = linker.requestBinding("com.microsoft.kapp.CargoConnection", VoicemailNotificationHandler.class, getClass().getClassLoader());
        this.supertype = linker.requestBinding("members/com.microsoft.kapp.services.InjectableNotificationHandler", VoicemailNotificationHandler.class, getClass().getClassLoader(), false, true);
    }

    @Override // dagger.internal.Binding
    public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
        injectMembersBindings.add(this.mCargoConnection);
        injectMembersBindings.add(this.supertype);
    }

    @Override // dagger.internal.Binding, dagger.MembersInjector
    public void injectMembers(VoicemailNotificationHandler object) {
        object.mCargoConnection = this.mCargoConnection.get();
        this.supertype.injectMembers(object);
    }
}

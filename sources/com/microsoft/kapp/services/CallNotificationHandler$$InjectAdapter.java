package com.microsoft.kapp.services;

import com.microsoft.kapp.CargoConnection;
import com.microsoft.kapp.ContactResolver;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;
/* loaded from: classes.dex */
public final class CallNotificationHandler$$InjectAdapter extends Binding<CallNotificationHandler> implements MembersInjector<CallNotificationHandler> {
    private Binding<CargoConnection> mCargoConnection;
    private Binding<ContactResolver> mContactResolver;
    private Binding<InjectableNotificationHandler> supertype;

    public CallNotificationHandler$$InjectAdapter() {
        super(null, "members/com.microsoft.kapp.services.CallNotificationHandler", false, CallNotificationHandler.class);
    }

    @Override // dagger.internal.Binding
    public void attach(Linker linker) {
        this.mCargoConnection = linker.requestBinding("com.microsoft.kapp.CargoConnection", CallNotificationHandler.class, getClass().getClassLoader());
        this.mContactResolver = linker.requestBinding("com.microsoft.kapp.ContactResolver", CallNotificationHandler.class, getClass().getClassLoader());
        this.supertype = linker.requestBinding("members/com.microsoft.kapp.services.InjectableNotificationHandler", CallNotificationHandler.class, getClass().getClassLoader(), false, true);
    }

    @Override // dagger.internal.Binding
    public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
        injectMembersBindings.add(this.mCargoConnection);
        injectMembersBindings.add(this.mContactResolver);
        injectMembersBindings.add(this.supertype);
    }

    @Override // dagger.internal.Binding, dagger.MembersInjector
    public void injectMembers(CallNotificationHandler object) {
        object.mCargoConnection = this.mCargoConnection.get();
        object.mContactResolver = this.mContactResolver.get();
        this.supertype.injectMembers(object);
    }
}

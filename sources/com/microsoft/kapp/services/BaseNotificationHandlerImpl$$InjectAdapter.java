package com.microsoft.kapp.services;

import com.microsoft.kapp.CargoConnection;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;
/* loaded from: classes.dex */
public final class BaseNotificationHandlerImpl$$InjectAdapter extends Binding<BaseNotificationHandlerImpl> implements MembersInjector<BaseNotificationHandlerImpl> {
    private Binding<CargoConnection> mCargoConnection;
    private Binding<InjectableNotificationHandler> supertype;

    public BaseNotificationHandlerImpl$$InjectAdapter() {
        super(null, "members/com.microsoft.kapp.services.BaseNotificationHandlerImpl", false, BaseNotificationHandlerImpl.class);
    }

    @Override // dagger.internal.Binding
    public void attach(Linker linker) {
        this.mCargoConnection = linker.requestBinding("com.microsoft.kapp.CargoConnection", BaseNotificationHandlerImpl.class, getClass().getClassLoader());
        this.supertype = linker.requestBinding("members/com.microsoft.kapp.services.InjectableNotificationHandler", BaseNotificationHandlerImpl.class, getClass().getClassLoader(), false, true);
    }

    @Override // dagger.internal.Binding
    public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
        injectMembersBindings.add(this.mCargoConnection);
        injectMembersBindings.add(this.supertype);
    }

    @Override // dagger.internal.Binding, dagger.MembersInjector
    public void injectMembers(BaseNotificationHandlerImpl object) {
        object.mCargoConnection = this.mCargoConnection.get();
        this.supertype.injectMembers(object);
    }
}

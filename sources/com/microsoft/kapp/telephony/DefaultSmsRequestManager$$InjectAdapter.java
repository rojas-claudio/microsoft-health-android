package com.microsoft.kapp.telephony;

import com.microsoft.kapp.CargoConnection;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class DefaultSmsRequestManager$$InjectAdapter extends Binding<DefaultSmsRequestManager> implements Provider<DefaultSmsRequestManager> {
    private Binding<CargoConnection> cargoConnection;

    public DefaultSmsRequestManager$$InjectAdapter() {
        super("com.microsoft.kapp.telephony.DefaultSmsRequestManager", "members/com.microsoft.kapp.telephony.DefaultSmsRequestManager", false, DefaultSmsRequestManager.class);
    }

    @Override // dagger.internal.Binding
    public void attach(Linker linker) {
        this.cargoConnection = linker.requestBinding("com.microsoft.kapp.CargoConnection", DefaultSmsRequestManager.class, getClass().getClassLoader());
    }

    @Override // dagger.internal.Binding
    public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
        getBindings.add(this.cargoConnection);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // dagger.internal.Binding, javax.inject.Provider
    public DefaultSmsRequestManager get() {
        DefaultSmsRequestManager result = new DefaultSmsRequestManager(this.cargoConnection.get());
        return result;
    }
}

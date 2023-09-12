package com.microsoft.kapp.telephony;

import android.content.Context;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class IncomingCallContextFactory$$InjectAdapter extends Binding<IncomingCallContextFactory> implements Provider<IncomingCallContextFactory> {
    private Binding<Context> context;

    public IncomingCallContextFactory$$InjectAdapter() {
        super("com.microsoft.kapp.telephony.IncomingCallContextFactory", "members/com.microsoft.kapp.telephony.IncomingCallContextFactory", true, IncomingCallContextFactory.class);
    }

    @Override // dagger.internal.Binding
    public void attach(Linker linker) {
        this.context = linker.requestBinding("android.content.Context", IncomingCallContextFactory.class, getClass().getClassLoader());
    }

    @Override // dagger.internal.Binding
    public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
        getBindings.add(this.context);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // dagger.internal.Binding, javax.inject.Provider
    public IncomingCallContextFactory get() {
        IncomingCallContextFactory result = new IncomingCallContextFactory(this.context.get());
        return result;
    }
}

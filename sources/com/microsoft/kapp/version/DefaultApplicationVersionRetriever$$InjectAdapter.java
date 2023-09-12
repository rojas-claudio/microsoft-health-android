package com.microsoft.kapp.version;

import com.microsoft.krestsdk.services.NetworkProvider;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class DefaultApplicationVersionRetriever$$InjectAdapter extends Binding<DefaultApplicationVersionRetriever> implements Provider<DefaultApplicationVersionRetriever> {
    private Binding<NetworkProvider> provider;

    public DefaultApplicationVersionRetriever$$InjectAdapter() {
        super("com.microsoft.kapp.version.DefaultApplicationVersionRetriever", "members/com.microsoft.kapp.version.DefaultApplicationVersionRetriever", false, DefaultApplicationVersionRetriever.class);
    }

    @Override // dagger.internal.Binding
    public void attach(Linker linker) {
        this.provider = linker.requestBinding("com.microsoft.krestsdk.services.NetworkProvider", DefaultApplicationVersionRetriever.class, getClass().getClassLoader());
    }

    @Override // dagger.internal.Binding
    public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
        getBindings.add(this.provider);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // dagger.internal.Binding, javax.inject.Provider
    public DefaultApplicationVersionRetriever get() {
        DefaultApplicationVersionRetriever result = new DefaultApplicationVersionRetriever(this.provider.get());
        return result;
    }
}

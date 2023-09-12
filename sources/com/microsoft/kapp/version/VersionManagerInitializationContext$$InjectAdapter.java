package com.microsoft.kapp.version;

import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class VersionManagerInitializationContext$$InjectAdapter extends Binding<VersionManagerInitializationContext> implements Provider<VersionManagerInitializationContext> {
    private Binding<ApplicationVersionUpdateNotifier> applicationVersionUpdateNotifier;

    public VersionManagerInitializationContext$$InjectAdapter() {
        super("com.microsoft.kapp.version.VersionManagerInitializationContext", "members/com.microsoft.kapp.version.VersionManagerInitializationContext", true, VersionManagerInitializationContext.class);
    }

    @Override // dagger.internal.Binding
    public void attach(Linker linker) {
        this.applicationVersionUpdateNotifier = linker.requestBinding("com.microsoft.kapp.version.ApplicationVersionUpdateNotifier", VersionManagerInitializationContext.class, getClass().getClassLoader());
    }

    @Override // dagger.internal.Binding
    public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
        getBindings.add(this.applicationVersionUpdateNotifier);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // dagger.internal.Binding, javax.inject.Provider
    public VersionManagerInitializationContext get() {
        VersionManagerInitializationContext result = new VersionManagerInitializationContext(this.applicationVersionUpdateNotifier.get());
        return result;
    }
}

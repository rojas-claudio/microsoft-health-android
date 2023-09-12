package com.microsoft.kapp.version;

import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class VersionManager$$InjectAdapter extends Binding<VersionManager> implements Provider<VersionManager> {
    private Binding<VersionManagerInitializationContext> context;
    private Binding<VersionUpdateInteractionCoordinator> coordinator;

    public VersionManager$$InjectAdapter() {
        super("com.microsoft.kapp.version.VersionManager", "members/com.microsoft.kapp.version.VersionManager", true, VersionManager.class);
    }

    @Override // dagger.internal.Binding
    public void attach(Linker linker) {
        this.context = linker.requestBinding("com.microsoft.kapp.version.VersionManagerInitializationContext", VersionManager.class, getClass().getClassLoader());
        this.coordinator = linker.requestBinding("com.microsoft.kapp.version.VersionUpdateInteractionCoordinator", VersionManager.class, getClass().getClassLoader());
    }

    @Override // dagger.internal.Binding
    public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
        getBindings.add(this.context);
        getBindings.add(this.coordinator);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // dagger.internal.Binding, javax.inject.Provider
    public VersionManager get() {
        VersionManager result = new VersionManager(this.context.get(), this.coordinator.get());
        return result;
    }
}

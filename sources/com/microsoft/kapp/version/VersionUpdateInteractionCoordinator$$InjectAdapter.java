package com.microsoft.kapp.version;

import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class VersionUpdateInteractionCoordinator$$InjectAdapter extends Binding<VersionUpdateInteractionCoordinator> implements Provider<VersionUpdateInteractionCoordinator> {
    private Binding<ApplicationUpdateLauncher> launcher;

    public VersionUpdateInteractionCoordinator$$InjectAdapter() {
        super("com.microsoft.kapp.version.VersionUpdateInteractionCoordinator", "members/com.microsoft.kapp.version.VersionUpdateInteractionCoordinator", true, VersionUpdateInteractionCoordinator.class);
    }

    @Override // dagger.internal.Binding
    public void attach(Linker linker) {
        this.launcher = linker.requestBinding("com.microsoft.kapp.version.ApplicationUpdateLauncher", VersionUpdateInteractionCoordinator.class, getClass().getClassLoader());
    }

    @Override // dagger.internal.Binding
    public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
        getBindings.add(this.launcher);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // dagger.internal.Binding, javax.inject.Provider
    public VersionUpdateInteractionCoordinator get() {
        VersionUpdateInteractionCoordinator result = new VersionUpdateInteractionCoordinator(this.launcher.get());
        return result;
    }
}

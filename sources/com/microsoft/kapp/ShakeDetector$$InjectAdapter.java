package com.microsoft.kapp;

import com.microsoft.kapp.services.SettingsProvider;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class ShakeDetector$$InjectAdapter extends Binding<ShakeDetector> implements Provider<ShakeDetector>, MembersInjector<ShakeDetector> {
    private Binding<SettingsProvider> mSettingsProvider;

    public ShakeDetector$$InjectAdapter() {
        super("com.microsoft.kapp.ShakeDetector", "members/com.microsoft.kapp.ShakeDetector", false, ShakeDetector.class);
    }

    @Override // dagger.internal.Binding
    public void attach(Linker linker) {
        this.mSettingsProvider = linker.requestBinding("com.microsoft.kapp.services.SettingsProvider", ShakeDetector.class, getClass().getClassLoader());
    }

    @Override // dagger.internal.Binding
    public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
        injectMembersBindings.add(this.mSettingsProvider);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // dagger.internal.Binding, javax.inject.Provider
    public ShakeDetector get() {
        ShakeDetector result = new ShakeDetector();
        injectMembers(result);
        return result;
    }

    @Override // dagger.internal.Binding, dagger.MembersInjector
    public void injectMembers(ShakeDetector object) {
        object.mSettingsProvider = this.mSettingsProvider.get();
    }
}

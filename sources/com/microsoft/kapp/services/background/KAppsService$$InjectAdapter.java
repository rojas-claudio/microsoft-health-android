package com.microsoft.kapp.services.background;

import com.microsoft.kapp.services.InjectableIntentService;
import com.microsoft.kapp.services.KAppsUpdater;
import com.microsoft.kapp.services.SettingsProvider;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class KAppsService$$InjectAdapter extends Binding<KAppsService> implements Provider<KAppsService>, MembersInjector<KAppsService> {
    private Binding<SettingsProvider> mSettingsProvider;
    private Binding<KAppsUpdater> mUpdater;
    private Binding<InjectableIntentService> supertype;

    public KAppsService$$InjectAdapter() {
        super("com.microsoft.kapp.services.background.KAppsService", "members/com.microsoft.kapp.services.background.KAppsService", false, KAppsService.class);
    }

    @Override // dagger.internal.Binding
    public void attach(Linker linker) {
        this.mSettingsProvider = linker.requestBinding("com.microsoft.kapp.services.SettingsProvider", KAppsService.class, getClass().getClassLoader());
        this.mUpdater = linker.requestBinding("com.microsoft.kapp.services.KAppsUpdater", KAppsService.class, getClass().getClassLoader());
        this.supertype = linker.requestBinding("members/com.microsoft.kapp.services.InjectableIntentService", KAppsService.class, getClass().getClassLoader(), false, true);
    }

    @Override // dagger.internal.Binding
    public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
        injectMembersBindings.add(this.mSettingsProvider);
        injectMembersBindings.add(this.mUpdater);
        injectMembersBindings.add(this.supertype);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // dagger.internal.Binding, javax.inject.Provider
    public KAppsService get() {
        KAppsService result = new KAppsService();
        injectMembers(result);
        return result;
    }

    @Override // dagger.internal.Binding, dagger.MembersInjector
    public void injectMembers(KAppsService object) {
        object.mSettingsProvider = this.mSettingsProvider.get();
        object.mUpdater = this.mUpdater.get();
        this.supertype.injectMembers(object);
    }
}

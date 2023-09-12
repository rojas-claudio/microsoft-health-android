package com.microsoft.kapp.services.background;

import com.microsoft.kapp.services.InjectableIntentService;
import com.microsoft.kapp.services.SettingsProvider;
import com.microsoft.kapp.utils.AppConfigurationManager;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class AppConfigurationService$$InjectAdapter extends Binding<AppConfigurationService> implements Provider<AppConfigurationService>, MembersInjector<AppConfigurationService> {
    private Binding<AppConfigurationManager> mAppConfigurationManager;
    private Binding<SettingsProvider> mSettingsProvider;
    private Binding<InjectableIntentService> supertype;

    public AppConfigurationService$$InjectAdapter() {
        super("com.microsoft.kapp.services.background.AppConfigurationService", "members/com.microsoft.kapp.services.background.AppConfigurationService", false, AppConfigurationService.class);
    }

    @Override // dagger.internal.Binding
    public void attach(Linker linker) {
        this.mAppConfigurationManager = linker.requestBinding("com.microsoft.kapp.utils.AppConfigurationManager", AppConfigurationService.class, getClass().getClassLoader());
        this.mSettingsProvider = linker.requestBinding("com.microsoft.kapp.services.SettingsProvider", AppConfigurationService.class, getClass().getClassLoader());
        this.supertype = linker.requestBinding("members/com.microsoft.kapp.services.InjectableIntentService", AppConfigurationService.class, getClass().getClassLoader(), false, true);
    }

    @Override // dagger.internal.Binding
    public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
        injectMembersBindings.add(this.mAppConfigurationManager);
        injectMembersBindings.add(this.mSettingsProvider);
        injectMembersBindings.add(this.supertype);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // dagger.internal.Binding, javax.inject.Provider
    public AppConfigurationService get() {
        AppConfigurationService result = new AppConfigurationService();
        injectMembers(result);
        return result;
    }

    @Override // dagger.internal.Binding, dagger.MembersInjector
    public void injectMembers(AppConfigurationService object) {
        object.mAppConfigurationManager = this.mAppConfigurationManager.get();
        object.mSettingsProvider = this.mSettingsProvider.get();
        this.supertype.injectMembers(object);
    }
}

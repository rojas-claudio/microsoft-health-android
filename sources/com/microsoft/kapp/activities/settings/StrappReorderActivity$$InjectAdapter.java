package com.microsoft.kapp.activities.settings;

import com.microsoft.kapp.activities.BaseActivity;
import com.microsoft.kapp.personalization.PersonalizationManagerFactory;
import com.microsoft.kapp.services.SettingsProvider;
import com.microsoft.kapp.strappsettings.StrappSettingsManager;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class StrappReorderActivity$$InjectAdapter extends Binding<StrappReorderActivity> implements Provider<StrappReorderActivity>, MembersInjector<StrappReorderActivity> {
    private Binding<PersonalizationManagerFactory> mPersonalizationManagerFactory;
    private Binding<SettingsProvider> mSettingsProvider;
    private Binding<StrappSettingsManager> mStrappSettingsManager;
    private Binding<BaseActivity> supertype;

    public StrappReorderActivity$$InjectAdapter() {
        super("com.microsoft.kapp.activities.settings.StrappReorderActivity", "members/com.microsoft.kapp.activities.settings.StrappReorderActivity", false, StrappReorderActivity.class);
    }

    @Override // dagger.internal.Binding
    public void attach(Linker linker) {
        this.mSettingsProvider = linker.requestBinding("com.microsoft.kapp.services.SettingsProvider", StrappReorderActivity.class, getClass().getClassLoader());
        this.mStrappSettingsManager = linker.requestBinding("com.microsoft.kapp.strappsettings.StrappSettingsManager", StrappReorderActivity.class, getClass().getClassLoader());
        this.mPersonalizationManagerFactory = linker.requestBinding("com.microsoft.kapp.personalization.PersonalizationManagerFactory", StrappReorderActivity.class, getClass().getClassLoader());
        this.supertype = linker.requestBinding("members/com.microsoft.kapp.activities.BaseActivity", StrappReorderActivity.class, getClass().getClassLoader(), false, true);
    }

    @Override // dagger.internal.Binding
    public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
        injectMembersBindings.add(this.mSettingsProvider);
        injectMembersBindings.add(this.mStrappSettingsManager);
        injectMembersBindings.add(this.mPersonalizationManagerFactory);
        injectMembersBindings.add(this.supertype);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // dagger.internal.Binding, javax.inject.Provider
    public StrappReorderActivity get() {
        StrappReorderActivity result = new StrappReorderActivity();
        injectMembers(result);
        return result;
    }

    @Override // dagger.internal.Binding, dagger.MembersInjector
    public void injectMembers(StrappReorderActivity object) {
        object.mSettingsProvider = this.mSettingsProvider.get();
        object.mStrappSettingsManager = this.mStrappSettingsManager.get();
        object.mPersonalizationManagerFactory = this.mPersonalizationManagerFactory.get();
        this.supertype.injectMembers(object);
    }
}

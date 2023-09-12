package com.microsoft.kapp.fragments;

import android.content.Context;
import com.microsoft.kapp.models.strapp.DefaultStrappManager;
import com.microsoft.kapp.personalization.PersonalizationManagerFactory;
import com.microsoft.kapp.services.SettingsProvider;
import com.microsoft.kapp.strappsettings.StrappSettingsManager;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class ManageTilesFragment$$InjectAdapter extends Binding<ManageTilesFragment> implements Provider<ManageTilesFragment>, MembersInjector<ManageTilesFragment> {
    private Binding<Context> mAppContext;
    private Binding<DefaultStrappManager> mDefaultStrappManager;
    private Binding<PersonalizationManagerFactory> mPersonalizationManagerFactory;
    private Binding<SettingsProvider> mSettingsProvider;
    private Binding<StrappSettingsManager> mStrappSettingsManager;
    private Binding<BaseFragment> supertype;

    public ManageTilesFragment$$InjectAdapter() {
        super("com.microsoft.kapp.fragments.ManageTilesFragment", "members/com.microsoft.kapp.fragments.ManageTilesFragment", false, ManageTilesFragment.class);
    }

    @Override // dagger.internal.Binding
    public void attach(Linker linker) {
        this.mAppContext = linker.requestBinding("android.content.Context", ManageTilesFragment.class, getClass().getClassLoader());
        this.mPersonalizationManagerFactory = linker.requestBinding("com.microsoft.kapp.personalization.PersonalizationManagerFactory", ManageTilesFragment.class, getClass().getClassLoader());
        this.mStrappSettingsManager = linker.requestBinding("com.microsoft.kapp.strappsettings.StrappSettingsManager", ManageTilesFragment.class, getClass().getClassLoader());
        this.mSettingsProvider = linker.requestBinding("com.microsoft.kapp.services.SettingsProvider", ManageTilesFragment.class, getClass().getClassLoader());
        this.mDefaultStrappManager = linker.requestBinding("com.microsoft.kapp.models.strapp.DefaultStrappManager", ManageTilesFragment.class, getClass().getClassLoader());
        this.supertype = linker.requestBinding("members/com.microsoft.kapp.fragments.BaseFragment", ManageTilesFragment.class, getClass().getClassLoader(), false, true);
    }

    @Override // dagger.internal.Binding
    public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
        injectMembersBindings.add(this.mAppContext);
        injectMembersBindings.add(this.mPersonalizationManagerFactory);
        injectMembersBindings.add(this.mStrappSettingsManager);
        injectMembersBindings.add(this.mSettingsProvider);
        injectMembersBindings.add(this.mDefaultStrappManager);
        injectMembersBindings.add(this.supertype);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // dagger.internal.Binding, javax.inject.Provider
    public ManageTilesFragment get() {
        ManageTilesFragment result = new ManageTilesFragment();
        injectMembers(result);
        return result;
    }

    @Override // dagger.internal.Binding, dagger.MembersInjector
    public void injectMembers(ManageTilesFragment object) {
        object.mAppContext = this.mAppContext.get();
        object.mPersonalizationManagerFactory = this.mPersonalizationManagerFactory.get();
        object.mStrappSettingsManager = this.mStrappSettingsManager.get();
        object.mSettingsProvider = this.mSettingsProvider.get();
        object.mDefaultStrappManager = this.mDefaultStrappManager.get();
        this.supertype.injectMembers(object);
    }
}

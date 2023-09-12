package com.microsoft.kapp.fragments;

import com.microsoft.kapp.strappsettings.StrappSettingsManager;
import com.microsoft.kapp.utils.AppConfigurationManager;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class StarbucksOverviewFragment$$InjectAdapter extends Binding<StarbucksOverviewFragment> implements Provider<StarbucksOverviewFragment>, MembersInjector<StarbucksOverviewFragment> {
    private Binding<AppConfigurationManager> mAppConfigurationManager;
    private Binding<StrappSettingsManager> mSettingsManager;
    private Binding<BaseFragment> supertype;

    public StarbucksOverviewFragment$$InjectAdapter() {
        super("com.microsoft.kapp.fragments.StarbucksOverviewFragment", "members/com.microsoft.kapp.fragments.StarbucksOverviewFragment", false, StarbucksOverviewFragment.class);
    }

    @Override // dagger.internal.Binding
    public void attach(Linker linker) {
        this.mSettingsManager = linker.requestBinding("com.microsoft.kapp.strappsettings.StrappSettingsManager", StarbucksOverviewFragment.class, getClass().getClassLoader());
        this.mAppConfigurationManager = linker.requestBinding("com.microsoft.kapp.utils.AppConfigurationManager", StarbucksOverviewFragment.class, getClass().getClassLoader());
        this.supertype = linker.requestBinding("members/com.microsoft.kapp.fragments.BaseFragment", StarbucksOverviewFragment.class, getClass().getClassLoader(), false, true);
    }

    @Override // dagger.internal.Binding
    public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
        injectMembersBindings.add(this.mSettingsManager);
        injectMembersBindings.add(this.mAppConfigurationManager);
        injectMembersBindings.add(this.supertype);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // dagger.internal.Binding, javax.inject.Provider
    public StarbucksOverviewFragment get() {
        StarbucksOverviewFragment result = new StarbucksOverviewFragment();
        injectMembers(result);
        return result;
    }

    @Override // dagger.internal.Binding, dagger.MembersInjector
    public void injectMembers(StarbucksOverviewFragment object) {
        object.mSettingsManager = this.mSettingsManager.get();
        object.mAppConfigurationManager = this.mAppConfigurationManager.get();
        this.supertype.injectMembers(object);
    }
}

package com.microsoft.kapp.fragments;

import com.microsoft.kapp.strappsettings.StrappSettingsManager;
import com.microsoft.kapp.utils.AppConfigurationManager;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class StarbucksAddCardFragment$$InjectAdapter extends Binding<StarbucksAddCardFragment> implements Provider<StarbucksAddCardFragment>, MembersInjector<StarbucksAddCardFragment> {
    private Binding<AppConfigurationManager> mAppConfigurationManager;
    private Binding<StrappSettingsManager> mSettingsManager;
    private Binding<BaseFragment> supertype;

    public StarbucksAddCardFragment$$InjectAdapter() {
        super("com.microsoft.kapp.fragments.StarbucksAddCardFragment", "members/com.microsoft.kapp.fragments.StarbucksAddCardFragment", false, StarbucksAddCardFragment.class);
    }

    @Override // dagger.internal.Binding
    public void attach(Linker linker) {
        this.mSettingsManager = linker.requestBinding("com.microsoft.kapp.strappsettings.StrappSettingsManager", StarbucksAddCardFragment.class, getClass().getClassLoader());
        this.mAppConfigurationManager = linker.requestBinding("com.microsoft.kapp.utils.AppConfigurationManager", StarbucksAddCardFragment.class, getClass().getClassLoader());
        this.supertype = linker.requestBinding("members/com.microsoft.kapp.fragments.BaseFragment", StarbucksAddCardFragment.class, getClass().getClassLoader(), false, true);
    }

    @Override // dagger.internal.Binding
    public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
        injectMembersBindings.add(this.mSettingsManager);
        injectMembersBindings.add(this.mAppConfigurationManager);
        injectMembersBindings.add(this.supertype);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // dagger.internal.Binding, javax.inject.Provider
    public StarbucksAddCardFragment get() {
        StarbucksAddCardFragment result = new StarbucksAddCardFragment();
        injectMembers(result);
        return result;
    }

    @Override // dagger.internal.Binding, dagger.MembersInjector
    public void injectMembers(StarbucksAddCardFragment object) {
        object.mSettingsManager = this.mSettingsManager.get();
        object.mAppConfigurationManager = this.mAppConfigurationManager.get();
        this.supertype.injectMembers(object);
    }
}

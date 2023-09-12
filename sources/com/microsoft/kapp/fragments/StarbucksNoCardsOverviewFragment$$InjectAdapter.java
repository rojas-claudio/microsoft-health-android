package com.microsoft.kapp.fragments;

import com.microsoft.kapp.strappsettings.StrappSettingsManager;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class StarbucksNoCardsOverviewFragment$$InjectAdapter extends Binding<StarbucksNoCardsOverviewFragment> implements Provider<StarbucksNoCardsOverviewFragment>, MembersInjector<StarbucksNoCardsOverviewFragment> {
    private Binding<StrappSettingsManager> mSettingsManager;
    private Binding<BaseFragment> supertype;

    public StarbucksNoCardsOverviewFragment$$InjectAdapter() {
        super("com.microsoft.kapp.fragments.StarbucksNoCardsOverviewFragment", "members/com.microsoft.kapp.fragments.StarbucksNoCardsOverviewFragment", false, StarbucksNoCardsOverviewFragment.class);
    }

    @Override // dagger.internal.Binding
    public void attach(Linker linker) {
        this.mSettingsManager = linker.requestBinding("com.microsoft.kapp.strappsettings.StrappSettingsManager", StarbucksNoCardsOverviewFragment.class, getClass().getClassLoader());
        this.supertype = linker.requestBinding("members/com.microsoft.kapp.fragments.BaseFragment", StarbucksNoCardsOverviewFragment.class, getClass().getClassLoader(), false, true);
    }

    @Override // dagger.internal.Binding
    public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
        injectMembersBindings.add(this.mSettingsManager);
        injectMembersBindings.add(this.supertype);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // dagger.internal.Binding, javax.inject.Provider
    public StarbucksNoCardsOverviewFragment get() {
        StarbucksNoCardsOverviewFragment result = new StarbucksNoCardsOverviewFragment();
        injectMembers(result);
        return result;
    }

    @Override // dagger.internal.Binding, dagger.MembersInjector
    public void injectMembers(StarbucksNoCardsOverviewFragment object) {
        object.mSettingsManager = this.mSettingsManager.get();
        this.supertype.injectMembers(object);
    }
}

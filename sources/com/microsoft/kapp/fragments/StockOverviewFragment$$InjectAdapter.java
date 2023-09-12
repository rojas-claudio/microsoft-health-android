package com.microsoft.kapp.fragments;

import com.microsoft.kapp.strappsettings.StrappSettingsManager;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class StockOverviewFragment$$InjectAdapter extends Binding<StockOverviewFragment> implements Provider<StockOverviewFragment>, MembersInjector<StockOverviewFragment> {
    private Binding<StrappSettingsManager> mStrappSettingsManager;
    private Binding<BaseFragment> supertype;

    public StockOverviewFragment$$InjectAdapter() {
        super("com.microsoft.kapp.fragments.StockOverviewFragment", "members/com.microsoft.kapp.fragments.StockOverviewFragment", false, StockOverviewFragment.class);
    }

    @Override // dagger.internal.Binding
    public void attach(Linker linker) {
        this.mStrappSettingsManager = linker.requestBinding("com.microsoft.kapp.strappsettings.StrappSettingsManager", StockOverviewFragment.class, getClass().getClassLoader());
        this.supertype = linker.requestBinding("members/com.microsoft.kapp.fragments.BaseFragment", StockOverviewFragment.class, getClass().getClassLoader(), false, true);
    }

    @Override // dagger.internal.Binding
    public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
        injectMembersBindings.add(this.mStrappSettingsManager);
        injectMembersBindings.add(this.supertype);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // dagger.internal.Binding, javax.inject.Provider
    public StockOverviewFragment get() {
        StockOverviewFragment result = new StockOverviewFragment();
        injectMembers(result);
        return result;
    }

    @Override // dagger.internal.Binding, dagger.MembersInjector
    public void injectMembers(StockOverviewFragment object) {
        object.mStrappSettingsManager = this.mStrappSettingsManager.get();
        this.supertype.injectMembers(object);
    }
}

package com.microsoft.kapp.fragments;

import com.microsoft.kapp.services.SettingsProvider;
import com.microsoft.kapp.services.finance.FinanceService;
import com.microsoft.krestsdk.services.RestService;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class StockAddFragment$$InjectAdapter extends Binding<StockAddFragment> implements Provider<StockAddFragment>, MembersInjector<StockAddFragment> {
    private Binding<FinanceService> mFinanceService;
    private Binding<RestService> mRestService;
    private Binding<SettingsProvider> mSettingsProvider;
    private Binding<BaseFragment> supertype;

    public StockAddFragment$$InjectAdapter() {
        super("com.microsoft.kapp.fragments.StockAddFragment", "members/com.microsoft.kapp.fragments.StockAddFragment", false, StockAddFragment.class);
    }

    @Override // dagger.internal.Binding
    public void attach(Linker linker) {
        this.mSettingsProvider = linker.requestBinding("com.microsoft.kapp.services.SettingsProvider", StockAddFragment.class, getClass().getClassLoader());
        this.mRestService = linker.requestBinding("com.microsoft.krestsdk.services.RestService", StockAddFragment.class, getClass().getClassLoader());
        this.mFinanceService = linker.requestBinding("com.microsoft.kapp.services.finance.FinanceService", StockAddFragment.class, getClass().getClassLoader());
        this.supertype = linker.requestBinding("members/com.microsoft.kapp.fragments.BaseFragment", StockAddFragment.class, getClass().getClassLoader(), false, true);
    }

    @Override // dagger.internal.Binding
    public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
        injectMembersBindings.add(this.mSettingsProvider);
        injectMembersBindings.add(this.mRestService);
        injectMembersBindings.add(this.mFinanceService);
        injectMembersBindings.add(this.supertype);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // dagger.internal.Binding, javax.inject.Provider
    public StockAddFragment get() {
        StockAddFragment result = new StockAddFragment();
        injectMembers(result);
        return result;
    }

    @Override // dagger.internal.Binding, dagger.MembersInjector
    public void injectMembers(StockAddFragment object) {
        object.mSettingsProvider = this.mSettingsProvider.get();
        object.mRestService = this.mRestService.get();
        object.mFinanceService = this.mFinanceService.get();
        this.supertype.injectMembers(object);
    }
}

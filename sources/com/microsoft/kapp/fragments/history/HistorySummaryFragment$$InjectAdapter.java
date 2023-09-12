package com.microsoft.kapp.fragments.history;

import com.microsoft.kapp.fragments.BaseFragmentWithOfflineSupport;
import com.microsoft.kapp.services.SettingsProvider;
import com.microsoft.krestsdk.services.RestService;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class HistorySummaryFragment$$InjectAdapter extends Binding<HistorySummaryFragment> implements Provider<HistorySummaryFragment>, MembersInjector<HistorySummaryFragment> {
    private Binding<RestService> mService;
    private Binding<SettingsProvider> mSettingsProvider;
    private Binding<BaseFragmentWithOfflineSupport> supertype;

    public HistorySummaryFragment$$InjectAdapter() {
        super("com.microsoft.kapp.fragments.history.HistorySummaryFragment", "members/com.microsoft.kapp.fragments.history.HistorySummaryFragment", false, HistorySummaryFragment.class);
    }

    @Override // dagger.internal.Binding
    public void attach(Linker linker) {
        this.mService = linker.requestBinding("com.microsoft.krestsdk.services.RestService", HistorySummaryFragment.class, getClass().getClassLoader());
        this.mSettingsProvider = linker.requestBinding("com.microsoft.kapp.services.SettingsProvider", HistorySummaryFragment.class, getClass().getClassLoader());
        this.supertype = linker.requestBinding("members/com.microsoft.kapp.fragments.BaseFragmentWithOfflineSupport", HistorySummaryFragment.class, getClass().getClassLoader(), false, true);
    }

    @Override // dagger.internal.Binding
    public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
        injectMembersBindings.add(this.mService);
        injectMembersBindings.add(this.mSettingsProvider);
        injectMembersBindings.add(this.supertype);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // dagger.internal.Binding, javax.inject.Provider
    public HistorySummaryFragment get() {
        HistorySummaryFragment result = new HistorySummaryFragment();
        injectMembers(result);
        return result;
    }

    @Override // dagger.internal.Binding, dagger.MembersInjector
    public void injectMembers(HistorySummaryFragment object) {
        object.mService = this.mService.get();
        object.mSettingsProvider = this.mSettingsProvider.get();
        this.supertype.injectMembers(object);
    }
}

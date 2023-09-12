package com.microsoft.kapp.fragments;

import com.microsoft.kapp.services.SettingsProvider;
import com.microsoft.kapp.services.golf.GolfService;
import com.microsoft.krestsdk.services.RestService;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class EventHistorySummaryFragment$$InjectAdapter extends Binding<EventHistorySummaryFragment> implements Provider<EventHistorySummaryFragment>, MembersInjector<EventHistorySummaryFragment> {
    private Binding<GolfService> mGolfService;
    private Binding<RestService> mService;
    private Binding<SettingsProvider> mSettingsProvider;
    private Binding<BaseFragment> supertype;

    public EventHistorySummaryFragment$$InjectAdapter() {
        super("com.microsoft.kapp.fragments.EventHistorySummaryFragment", "members/com.microsoft.kapp.fragments.EventHistorySummaryFragment", false, EventHistorySummaryFragment.class);
    }

    @Override // dagger.internal.Binding
    public void attach(Linker linker) {
        this.mService = linker.requestBinding("com.microsoft.krestsdk.services.RestService", EventHistorySummaryFragment.class, getClass().getClassLoader());
        this.mSettingsProvider = linker.requestBinding("com.microsoft.kapp.services.SettingsProvider", EventHistorySummaryFragment.class, getClass().getClassLoader());
        this.mGolfService = linker.requestBinding("com.microsoft.kapp.services.golf.GolfService", EventHistorySummaryFragment.class, getClass().getClassLoader());
        this.supertype = linker.requestBinding("members/com.microsoft.kapp.fragments.BaseFragment", EventHistorySummaryFragment.class, getClass().getClassLoader(), false, true);
    }

    @Override // dagger.internal.Binding
    public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
        injectMembersBindings.add(this.mService);
        injectMembersBindings.add(this.mSettingsProvider);
        injectMembersBindings.add(this.mGolfService);
        injectMembersBindings.add(this.supertype);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // dagger.internal.Binding, javax.inject.Provider
    public EventHistorySummaryFragment get() {
        EventHistorySummaryFragment result = new EventHistorySummaryFragment();
        injectMembers(result);
        return result;
    }

    @Override // dagger.internal.Binding, dagger.MembersInjector
    public void injectMembers(EventHistorySummaryFragment object) {
        object.mService = this.mService.get();
        object.mSettingsProvider = this.mSettingsProvider.get();
        object.mGolfService = this.mGolfService.get();
        this.supertype.injectMembers(object);
    }
}

package com.microsoft.kapp.fragments.golf;

import com.microsoft.kapp.fragments.BaseFragmentWithOfflineSupport;
import com.microsoft.kapp.services.SettingsProvider;
import com.microsoft.kapp.services.golf.GolfService;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;
/* loaded from: classes.dex */
public final class GolfSearchResultsFragment$$InjectAdapter extends Binding<GolfSearchResultsFragment> implements MembersInjector<GolfSearchResultsFragment> {
    private Binding<GolfService> mGolfService;
    private Binding<SettingsProvider> mSettingsProvider;
    private Binding<BaseFragmentWithOfflineSupport> supertype;

    public GolfSearchResultsFragment$$InjectAdapter() {
        super(null, "members/com.microsoft.kapp.fragments.golf.GolfSearchResultsFragment", false, GolfSearchResultsFragment.class);
    }

    @Override // dagger.internal.Binding
    public void attach(Linker linker) {
        this.mGolfService = linker.requestBinding("com.microsoft.kapp.services.golf.GolfService", GolfSearchResultsFragment.class, getClass().getClassLoader());
        this.mSettingsProvider = linker.requestBinding("com.microsoft.kapp.services.SettingsProvider", GolfSearchResultsFragment.class, getClass().getClassLoader());
        this.supertype = linker.requestBinding("members/com.microsoft.kapp.fragments.BaseFragmentWithOfflineSupport", GolfSearchResultsFragment.class, getClass().getClassLoader(), false, true);
    }

    @Override // dagger.internal.Binding
    public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
        injectMembersBindings.add(this.mGolfService);
        injectMembersBindings.add(this.mSettingsProvider);
        injectMembersBindings.add(this.supertype);
    }

    @Override // dagger.internal.Binding, dagger.MembersInjector
    public void injectMembers(GolfSearchResultsFragment object) {
        object.mGolfService = this.mGolfService.get();
        object.mSettingsProvider = this.mSettingsProvider.get();
        this.supertype.injectMembers(object);
    }
}

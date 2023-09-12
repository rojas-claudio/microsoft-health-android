package com.microsoft.kapp.fragments;

import com.microsoft.kapp.services.SettingsProvider;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;
/* loaded from: classes.dex */
public final class SearchBaseFragment$$InjectAdapter extends Binding<SearchBaseFragment> implements MembersInjector<SearchBaseFragment> {
    private Binding<SettingsProvider> mSettings;
    private Binding<BaseFragmentWithOfflineSupport> supertype;

    public SearchBaseFragment$$InjectAdapter() {
        super(null, "members/com.microsoft.kapp.fragments.SearchBaseFragment", false, SearchBaseFragment.class);
    }

    @Override // dagger.internal.Binding
    public void attach(Linker linker) {
        this.mSettings = linker.requestBinding("com.microsoft.kapp.services.SettingsProvider", SearchBaseFragment.class, getClass().getClassLoader());
        this.supertype = linker.requestBinding("members/com.microsoft.kapp.fragments.BaseFragmentWithOfflineSupport", SearchBaseFragment.class, getClass().getClassLoader(), false, true);
    }

    @Override // dagger.internal.Binding
    public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
        injectMembersBindings.add(this.mSettings);
        injectMembersBindings.add(this.supertype);
    }

    @Override // dagger.internal.Binding, dagger.MembersInjector
    public void injectMembers(SearchBaseFragment object) {
        object.mSettings = this.mSettings.get();
        this.supertype.injectMembers(object);
    }
}

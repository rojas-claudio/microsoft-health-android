package com.microsoft.kapp.fragments;

import com.microsoft.kapp.services.SettingsProvider;
import com.microsoft.krestsdk.services.RestService;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;
/* loaded from: classes.dex */
public final class BaseEventSummaryFragment$$InjectAdapter extends Binding<BaseEventSummaryFragment> implements MembersInjector<BaseEventSummaryFragment> {
    private Binding<RestService> mRestService;
    private Binding<SettingsProvider> mSettings;
    private Binding<BaseHomeTileFragment> supertype;

    public BaseEventSummaryFragment$$InjectAdapter() {
        super(null, "members/com.microsoft.kapp.fragments.BaseEventSummaryFragment", false, BaseEventSummaryFragment.class);
    }

    @Override // dagger.internal.Binding
    public void attach(Linker linker) {
        this.mRestService = linker.requestBinding("com.microsoft.krestsdk.services.RestService", BaseEventSummaryFragment.class, getClass().getClassLoader());
        this.mSettings = linker.requestBinding("com.microsoft.kapp.services.SettingsProvider", BaseEventSummaryFragment.class, getClass().getClassLoader());
        this.supertype = linker.requestBinding("members/com.microsoft.kapp.fragments.BaseHomeTileFragment", BaseEventSummaryFragment.class, getClass().getClassLoader(), false, true);
    }

    @Override // dagger.internal.Binding
    public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
        injectMembersBindings.add(this.mRestService);
        injectMembersBindings.add(this.mSettings);
        injectMembersBindings.add(this.supertype);
    }

    @Override // dagger.internal.Binding, dagger.MembersInjector
    public void injectMembers(BaseEventSummaryFragment object) {
        object.mRestService = this.mRestService.get();
        object.mSettings = this.mSettings.get();
        this.supertype.injectMembers(object);
    }
}

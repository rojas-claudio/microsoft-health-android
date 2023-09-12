package com.microsoft.kapp.fragments;

import com.microsoft.kapp.services.SettingsProvider;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class HomeTileNoDataFragment$$InjectAdapter extends Binding<HomeTileNoDataFragment> implements Provider<HomeTileNoDataFragment>, MembersInjector<HomeTileNoDataFragment> {
    private Binding<SettingsProvider> mSettingsProvider;
    private Binding<BaseHomeTileFragment> supertype;

    public HomeTileNoDataFragment$$InjectAdapter() {
        super("com.microsoft.kapp.fragments.HomeTileNoDataFragment", "members/com.microsoft.kapp.fragments.HomeTileNoDataFragment", false, HomeTileNoDataFragment.class);
    }

    @Override // dagger.internal.Binding
    public void attach(Linker linker) {
        this.mSettingsProvider = linker.requestBinding("com.microsoft.kapp.services.SettingsProvider", HomeTileNoDataFragment.class, getClass().getClassLoader());
        this.supertype = linker.requestBinding("members/com.microsoft.kapp.fragments.BaseHomeTileFragment", HomeTileNoDataFragment.class, getClass().getClassLoader(), false, true);
    }

    @Override // dagger.internal.Binding
    public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
        injectMembersBindings.add(this.mSettingsProvider);
        injectMembersBindings.add(this.supertype);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // dagger.internal.Binding, javax.inject.Provider
    public HomeTileNoDataFragment get() {
        HomeTileNoDataFragment result = new HomeTileNoDataFragment();
        injectMembers(result);
        return result;
    }

    @Override // dagger.internal.Binding, dagger.MembersInjector
    public void injectMembers(HomeTileNoDataFragment object) {
        object.mSettingsProvider = this.mSettingsProvider.get();
        this.supertype.injectMembers(object);
    }
}

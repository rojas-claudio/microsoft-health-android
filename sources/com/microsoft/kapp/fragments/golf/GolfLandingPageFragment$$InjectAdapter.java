package com.microsoft.kapp.fragments.golf;

import com.microsoft.kapp.fragments.BaseFragmentWithOfflineSupport;
import com.microsoft.kapp.services.SettingsProvider;
import com.microsoft.kapp.services.golf.GolfService;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class GolfLandingPageFragment$$InjectAdapter extends Binding<GolfLandingPageFragment> implements Provider<GolfLandingPageFragment>, MembersInjector<GolfLandingPageFragment> {
    private Binding<GolfService> mGolfService;
    private Binding<SettingsProvider> mSettingsProvider;
    private Binding<BaseFragmentWithOfflineSupport> supertype;

    public GolfLandingPageFragment$$InjectAdapter() {
        super("com.microsoft.kapp.fragments.golf.GolfLandingPageFragment", "members/com.microsoft.kapp.fragments.golf.GolfLandingPageFragment", false, GolfLandingPageFragment.class);
    }

    @Override // dagger.internal.Binding
    public void attach(Linker linker) {
        this.mGolfService = linker.requestBinding("com.microsoft.kapp.services.golf.GolfService", GolfLandingPageFragment.class, getClass().getClassLoader());
        this.mSettingsProvider = linker.requestBinding("com.microsoft.kapp.services.SettingsProvider", GolfLandingPageFragment.class, getClass().getClassLoader());
        this.supertype = linker.requestBinding("members/com.microsoft.kapp.fragments.BaseFragmentWithOfflineSupport", GolfLandingPageFragment.class, getClass().getClassLoader(), false, true);
    }

    @Override // dagger.internal.Binding
    public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
        injectMembersBindings.add(this.mGolfService);
        injectMembersBindings.add(this.mSettingsProvider);
        injectMembersBindings.add(this.supertype);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // dagger.internal.Binding, javax.inject.Provider
    public GolfLandingPageFragment get() {
        GolfLandingPageFragment result = new GolfLandingPageFragment();
        injectMembers(result);
        return result;
    }

    @Override // dagger.internal.Binding, dagger.MembersInjector
    public void injectMembers(GolfLandingPageFragment object) {
        object.mGolfService = this.mGolfService.get();
        object.mSettingsProvider = this.mSettingsProvider.get();
        this.supertype.injectMembers(object);
    }
}

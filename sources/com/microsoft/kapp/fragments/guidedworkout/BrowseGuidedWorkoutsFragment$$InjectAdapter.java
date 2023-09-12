package com.microsoft.kapp.fragments.guidedworkout;

import com.microsoft.kapp.fragments.BaseFragmentWithOfflineSupport;
import com.microsoft.kapp.services.SettingsProvider;
import com.microsoft.kapp.services.guidedworkout.GuidedWorkoutService;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class BrowseGuidedWorkoutsFragment$$InjectAdapter extends Binding<BrowseGuidedWorkoutsFragment> implements Provider<BrowseGuidedWorkoutsFragment>, MembersInjector<BrowseGuidedWorkoutsFragment> {
    private Binding<GuidedWorkoutService> mGuidedWorkoutService;
    private Binding<SettingsProvider> mSettingsProvider;
    private Binding<BaseFragmentWithOfflineSupport> supertype;

    public BrowseGuidedWorkoutsFragment$$InjectAdapter() {
        super("com.microsoft.kapp.fragments.guidedworkout.BrowseGuidedWorkoutsFragment", "members/com.microsoft.kapp.fragments.guidedworkout.BrowseGuidedWorkoutsFragment", false, BrowseGuidedWorkoutsFragment.class);
    }

    @Override // dagger.internal.Binding
    public void attach(Linker linker) {
        this.mGuidedWorkoutService = linker.requestBinding("com.microsoft.kapp.services.guidedworkout.GuidedWorkoutService", BrowseGuidedWorkoutsFragment.class, getClass().getClassLoader());
        this.mSettingsProvider = linker.requestBinding("com.microsoft.kapp.services.SettingsProvider", BrowseGuidedWorkoutsFragment.class, getClass().getClassLoader());
        this.supertype = linker.requestBinding("members/com.microsoft.kapp.fragments.BaseFragmentWithOfflineSupport", BrowseGuidedWorkoutsFragment.class, getClass().getClassLoader(), false, true);
    }

    @Override // dagger.internal.Binding
    public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
        injectMembersBindings.add(this.mGuidedWorkoutService);
        injectMembersBindings.add(this.mSettingsProvider);
        injectMembersBindings.add(this.supertype);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // dagger.internal.Binding, javax.inject.Provider
    public BrowseGuidedWorkoutsFragment get() {
        BrowseGuidedWorkoutsFragment result = new BrowseGuidedWorkoutsFragment();
        injectMembers(result);
        return result;
    }

    @Override // dagger.internal.Binding, dagger.MembersInjector
    public void injectMembers(BrowseGuidedWorkoutsFragment object) {
        object.mGuidedWorkoutService = this.mGuidedWorkoutService.get();
        object.mSettingsProvider = this.mSettingsProvider.get();
        this.supertype.injectMembers(object);
    }
}

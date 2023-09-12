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
public final class GuidedWorkoutNextFragment$$InjectAdapter extends Binding<GuidedWorkoutNextFragment> implements Provider<GuidedWorkoutNextFragment>, MembersInjector<GuidedWorkoutNextFragment> {
    private Binding<GuidedWorkoutService> mGuidedWorkoutService;
    private Binding<SettingsProvider> mSettingsProvider;
    private Binding<BaseFragmentWithOfflineSupport> supertype;

    public GuidedWorkoutNextFragment$$InjectAdapter() {
        super("com.microsoft.kapp.fragments.guidedworkout.GuidedWorkoutNextFragment", "members/com.microsoft.kapp.fragments.guidedworkout.GuidedWorkoutNextFragment", false, GuidedWorkoutNextFragment.class);
    }

    @Override // dagger.internal.Binding
    public void attach(Linker linker) {
        this.mSettingsProvider = linker.requestBinding("com.microsoft.kapp.services.SettingsProvider", GuidedWorkoutNextFragment.class, getClass().getClassLoader());
        this.mGuidedWorkoutService = linker.requestBinding("com.microsoft.kapp.services.guidedworkout.GuidedWorkoutService", GuidedWorkoutNextFragment.class, getClass().getClassLoader());
        this.supertype = linker.requestBinding("members/com.microsoft.kapp.fragments.BaseFragmentWithOfflineSupport", GuidedWorkoutNextFragment.class, getClass().getClassLoader(), false, true);
    }

    @Override // dagger.internal.Binding
    public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
        injectMembersBindings.add(this.mSettingsProvider);
        injectMembersBindings.add(this.mGuidedWorkoutService);
        injectMembersBindings.add(this.supertype);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // dagger.internal.Binding, javax.inject.Provider
    public GuidedWorkoutNextFragment get() {
        GuidedWorkoutNextFragment result = new GuidedWorkoutNextFragment();
        injectMembers(result);
        return result;
    }

    @Override // dagger.internal.Binding, dagger.MembersInjector
    public void injectMembers(GuidedWorkoutNextFragment object) {
        object.mSettingsProvider = this.mSettingsProvider.get();
        object.mGuidedWorkoutService = this.mGuidedWorkoutService.get();
        this.supertype.injectMembers(object);
    }
}

package com.microsoft.kapp.fragments.guidedworkout;

import com.microsoft.kapp.fragments.SearchBaseFragment;
import com.microsoft.kapp.services.SettingsProvider;
import com.microsoft.kapp.services.guidedworkout.GuidedWorkoutService;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class GuidedWorkoutsSearchFragment$$InjectAdapter extends Binding<GuidedWorkoutsSearchFragment> implements Provider<GuidedWorkoutsSearchFragment>, MembersInjector<GuidedWorkoutsSearchFragment> {
    private Binding<GuidedWorkoutService> mGuidedWorkoutService;
    private Binding<SettingsProvider> mSettings;
    private Binding<SearchBaseFragment> supertype;

    public GuidedWorkoutsSearchFragment$$InjectAdapter() {
        super("com.microsoft.kapp.fragments.guidedworkout.GuidedWorkoutsSearchFragment", "members/com.microsoft.kapp.fragments.guidedworkout.GuidedWorkoutsSearchFragment", false, GuidedWorkoutsSearchFragment.class);
    }

    @Override // dagger.internal.Binding
    public void attach(Linker linker) {
        this.mGuidedWorkoutService = linker.requestBinding("com.microsoft.kapp.services.guidedworkout.GuidedWorkoutService", GuidedWorkoutsSearchFragment.class, getClass().getClassLoader());
        this.mSettings = linker.requestBinding("com.microsoft.kapp.services.SettingsProvider", GuidedWorkoutsSearchFragment.class, getClass().getClassLoader());
        this.supertype = linker.requestBinding("members/com.microsoft.kapp.fragments.SearchBaseFragment", GuidedWorkoutsSearchFragment.class, getClass().getClassLoader(), false, true);
    }

    @Override // dagger.internal.Binding
    public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
        injectMembersBindings.add(this.mGuidedWorkoutService);
        injectMembersBindings.add(this.mSettings);
        injectMembersBindings.add(this.supertype);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // dagger.internal.Binding, javax.inject.Provider
    public GuidedWorkoutsSearchFragment get() {
        GuidedWorkoutsSearchFragment result = new GuidedWorkoutsSearchFragment();
        injectMembers(result);
        return result;
    }

    @Override // dagger.internal.Binding, dagger.MembersInjector
    public void injectMembers(GuidedWorkoutsSearchFragment object) {
        object.mGuidedWorkoutService = this.mGuidedWorkoutService.get();
        object.mSettings = this.mSettings.get();
        this.supertype.injectMembers(object);
    }
}

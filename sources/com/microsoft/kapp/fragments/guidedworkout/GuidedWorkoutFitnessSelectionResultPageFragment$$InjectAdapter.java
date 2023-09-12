package com.microsoft.kapp.fragments.guidedworkout;

import com.microsoft.kapp.fragments.BaseFragment;
import com.microsoft.kapp.services.guidedworkout.GuidedWorkoutService;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class GuidedWorkoutFitnessSelectionResultPageFragment$$InjectAdapter extends Binding<GuidedWorkoutFitnessSelectionResultPageFragment> implements Provider<GuidedWorkoutFitnessSelectionResultPageFragment>, MembersInjector<GuidedWorkoutFitnessSelectionResultPageFragment> {
    private Binding<GuidedWorkoutService> mGuidedWorkoutService;
    private Binding<BaseFragment> supertype;

    public GuidedWorkoutFitnessSelectionResultPageFragment$$InjectAdapter() {
        super("com.microsoft.kapp.fragments.guidedworkout.GuidedWorkoutFitnessSelectionResultPageFragment", "members/com.microsoft.kapp.fragments.guidedworkout.GuidedWorkoutFitnessSelectionResultPageFragment", false, GuidedWorkoutFitnessSelectionResultPageFragment.class);
    }

    @Override // dagger.internal.Binding
    public void attach(Linker linker) {
        this.mGuidedWorkoutService = linker.requestBinding("com.microsoft.kapp.services.guidedworkout.GuidedWorkoutService", GuidedWorkoutFitnessSelectionResultPageFragment.class, getClass().getClassLoader());
        this.supertype = linker.requestBinding("members/com.microsoft.kapp.fragments.BaseFragment", GuidedWorkoutFitnessSelectionResultPageFragment.class, getClass().getClassLoader(), false, true);
    }

    @Override // dagger.internal.Binding
    public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
        injectMembersBindings.add(this.mGuidedWorkoutService);
        injectMembersBindings.add(this.supertype);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // dagger.internal.Binding, javax.inject.Provider
    public GuidedWorkoutFitnessSelectionResultPageFragment get() {
        GuidedWorkoutFitnessSelectionResultPageFragment result = new GuidedWorkoutFitnessSelectionResultPageFragment();
        injectMembers(result);
        return result;
    }

    @Override // dagger.internal.Binding, dagger.MembersInjector
    public void injectMembers(GuidedWorkoutFitnessSelectionResultPageFragment object) {
        object.mGuidedWorkoutService = this.mGuidedWorkoutService.get();
        this.supertype.injectMembers(object);
    }
}

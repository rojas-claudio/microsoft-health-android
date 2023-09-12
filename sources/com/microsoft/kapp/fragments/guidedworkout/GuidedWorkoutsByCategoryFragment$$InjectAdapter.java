package com.microsoft.kapp.fragments.guidedworkout;

import com.microsoft.kapp.fragments.BaseFragmentWithOfflineSupport;
import com.microsoft.kapp.services.guidedworkout.GuidedWorkoutService;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;
/* loaded from: classes.dex */
public final class GuidedWorkoutsByCategoryFragment$$InjectAdapter extends Binding<GuidedWorkoutsByCategoryFragment> implements MembersInjector<GuidedWorkoutsByCategoryFragment> {
    private Binding<GuidedWorkoutService> mGuidedWorkoutService;
    private Binding<BaseFragmentWithOfflineSupport> supertype;

    public GuidedWorkoutsByCategoryFragment$$InjectAdapter() {
        super(null, "members/com.microsoft.kapp.fragments.guidedworkout.GuidedWorkoutsByCategoryFragment", false, GuidedWorkoutsByCategoryFragment.class);
    }

    @Override // dagger.internal.Binding
    public void attach(Linker linker) {
        this.mGuidedWorkoutService = linker.requestBinding("com.microsoft.kapp.services.guidedworkout.GuidedWorkoutService", GuidedWorkoutsByCategoryFragment.class, getClass().getClassLoader());
        this.supertype = linker.requestBinding("members/com.microsoft.kapp.fragments.BaseFragmentWithOfflineSupport", GuidedWorkoutsByCategoryFragment.class, getClass().getClassLoader(), false, true);
    }

    @Override // dagger.internal.Binding
    public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
        injectMembersBindings.add(this.mGuidedWorkoutService);
        injectMembersBindings.add(this.supertype);
    }

    @Override // dagger.internal.Binding, dagger.MembersInjector
    public void injectMembers(GuidedWorkoutsByCategoryFragment object) {
        object.mGuidedWorkoutService = this.mGuidedWorkoutService.get();
        this.supertype.injectMembers(object);
    }
}

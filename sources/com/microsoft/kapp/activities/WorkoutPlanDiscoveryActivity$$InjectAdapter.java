package com.microsoft.kapp.activities;

import com.microsoft.kapp.services.guidedworkout.GuidedWorkoutService;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class WorkoutPlanDiscoveryActivity$$InjectAdapter extends Binding<WorkoutPlanDiscoveryActivity> implements Provider<WorkoutPlanDiscoveryActivity>, MembersInjector<WorkoutPlanDiscoveryActivity> {
    private Binding<GuidedWorkoutService> mGuidedWorkoutService;
    private Binding<BaseFragmentActivityWithOfflineSupport> supertype;

    public WorkoutPlanDiscoveryActivity$$InjectAdapter() {
        super("com.microsoft.kapp.activities.WorkoutPlanDiscoveryActivity", "members/com.microsoft.kapp.activities.WorkoutPlanDiscoveryActivity", false, WorkoutPlanDiscoveryActivity.class);
    }

    @Override // dagger.internal.Binding
    public void attach(Linker linker) {
        this.mGuidedWorkoutService = linker.requestBinding("com.microsoft.kapp.services.guidedworkout.GuidedWorkoutService", WorkoutPlanDiscoveryActivity.class, getClass().getClassLoader());
        this.supertype = linker.requestBinding("members/com.microsoft.kapp.activities.BaseFragmentActivityWithOfflineSupport", WorkoutPlanDiscoveryActivity.class, getClass().getClassLoader(), false, true);
    }

    @Override // dagger.internal.Binding
    public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
        injectMembersBindings.add(this.mGuidedWorkoutService);
        injectMembersBindings.add(this.supertype);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // dagger.internal.Binding, javax.inject.Provider
    public WorkoutPlanDiscoveryActivity get() {
        WorkoutPlanDiscoveryActivity result = new WorkoutPlanDiscoveryActivity();
        injectMembers(result);
        return result;
    }

    @Override // dagger.internal.Binding, dagger.MembersInjector
    public void injectMembers(WorkoutPlanDiscoveryActivity object) {
        object.mGuidedWorkoutService = this.mGuidedWorkoutService.get();
        this.supertype.injectMembers(object);
    }
}

package com.microsoft.kapp.activities;

import com.microsoft.kapp.services.healthandfitness.HealthAndFitnessService;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class WorkoutPlanFilterActivity$$InjectAdapter extends Binding<WorkoutPlanFilterActivity> implements Provider<WorkoutPlanFilterActivity>, MembersInjector<WorkoutPlanFilterActivity> {
    private Binding<HealthAndFitnessService> mFitnessService;
    private Binding<BaseFragmentActivity> supertype;

    public WorkoutPlanFilterActivity$$InjectAdapter() {
        super("com.microsoft.kapp.activities.WorkoutPlanFilterActivity", "members/com.microsoft.kapp.activities.WorkoutPlanFilterActivity", false, WorkoutPlanFilterActivity.class);
    }

    @Override // dagger.internal.Binding
    public void attach(Linker linker) {
        this.mFitnessService = linker.requestBinding("com.microsoft.kapp.services.healthandfitness.HealthAndFitnessService", WorkoutPlanFilterActivity.class, getClass().getClassLoader());
        this.supertype = linker.requestBinding("members/com.microsoft.kapp.activities.BaseFragmentActivity", WorkoutPlanFilterActivity.class, getClass().getClassLoader(), false, true);
    }

    @Override // dagger.internal.Binding
    public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
        injectMembersBindings.add(this.mFitnessService);
        injectMembersBindings.add(this.supertype);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // dagger.internal.Binding, javax.inject.Provider
    public WorkoutPlanFilterActivity get() {
        WorkoutPlanFilterActivity result = new WorkoutPlanFilterActivity();
        injectMembers(result);
        return result;
    }

    @Override // dagger.internal.Binding, dagger.MembersInjector
    public void injectMembers(WorkoutPlanFilterActivity object) {
        object.mFitnessService = this.mFitnessService.get();
        this.supertype.injectMembers(object);
    }
}

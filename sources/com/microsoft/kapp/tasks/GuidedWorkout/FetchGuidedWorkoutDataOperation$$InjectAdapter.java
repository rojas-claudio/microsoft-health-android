package com.microsoft.kapp.tasks.GuidedWorkout;

import com.microsoft.kapp.services.guidedworkout.GuidedWorkoutService;
import com.microsoft.kapp.services.healthandfitness.HealthAndFitnessService;
import com.microsoft.krestsdk.services.RestService;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;
/* loaded from: classes.dex */
public final class FetchGuidedWorkoutDataOperation$$InjectAdapter extends Binding<FetchGuidedWorkoutDataOperation> implements MembersInjector<FetchGuidedWorkoutDataOperation> {
    private Binding<GuidedWorkoutService> mGuidedWorkoutService;
    private Binding<HealthAndFitnessService> mHnFService;
    private Binding<RestService> mRestService;

    public FetchGuidedWorkoutDataOperation$$InjectAdapter() {
        super(null, "members/com.microsoft.kapp.tasks.GuidedWorkout.FetchGuidedWorkoutDataOperation", false, FetchGuidedWorkoutDataOperation.class);
    }

    @Override // dagger.internal.Binding
    public void attach(Linker linker) {
        this.mRestService = linker.requestBinding("com.microsoft.krestsdk.services.RestService", FetchGuidedWorkoutDataOperation.class, getClass().getClassLoader());
        this.mHnFService = linker.requestBinding("com.microsoft.kapp.services.healthandfitness.HealthAndFitnessService", FetchGuidedWorkoutDataOperation.class, getClass().getClassLoader());
        this.mGuidedWorkoutService = linker.requestBinding("com.microsoft.kapp.services.guidedworkout.GuidedWorkoutService", FetchGuidedWorkoutDataOperation.class, getClass().getClassLoader());
    }

    @Override // dagger.internal.Binding
    public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
        injectMembersBindings.add(this.mRestService);
        injectMembersBindings.add(this.mHnFService);
        injectMembersBindings.add(this.mGuidedWorkoutService);
    }

    @Override // dagger.internal.Binding, dagger.MembersInjector
    public void injectMembers(FetchGuidedWorkoutDataOperation object) {
        object.mRestService = this.mRestService.get();
        object.mHnFService = this.mHnFService.get();
        object.mGuidedWorkoutService = this.mGuidedWorkoutService.get();
    }
}

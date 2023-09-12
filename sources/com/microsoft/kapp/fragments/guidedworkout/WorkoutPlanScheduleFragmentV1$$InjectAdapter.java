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
public final class WorkoutPlanScheduleFragmentV1$$InjectAdapter extends Binding<WorkoutPlanScheduleFragmentV1> implements Provider<WorkoutPlanScheduleFragmentV1>, MembersInjector<WorkoutPlanScheduleFragmentV1> {
    private Binding<GuidedWorkoutService> mGuidedWorkoutService;
    private Binding<SettingsProvider> mSettingsProvider;
    private Binding<BaseFragmentWithOfflineSupport> supertype;

    public WorkoutPlanScheduleFragmentV1$$InjectAdapter() {
        super("com.microsoft.kapp.fragments.guidedworkout.WorkoutPlanScheduleFragmentV1", "members/com.microsoft.kapp.fragments.guidedworkout.WorkoutPlanScheduleFragmentV1", false, WorkoutPlanScheduleFragmentV1.class);
    }

    @Override // dagger.internal.Binding
    public void attach(Linker linker) {
        this.mSettingsProvider = linker.requestBinding("com.microsoft.kapp.services.SettingsProvider", WorkoutPlanScheduleFragmentV1.class, getClass().getClassLoader());
        this.mGuidedWorkoutService = linker.requestBinding("com.microsoft.kapp.services.guidedworkout.GuidedWorkoutService", WorkoutPlanScheduleFragmentV1.class, getClass().getClassLoader());
        this.supertype = linker.requestBinding("members/com.microsoft.kapp.fragments.BaseFragmentWithOfflineSupport", WorkoutPlanScheduleFragmentV1.class, getClass().getClassLoader(), false, true);
    }

    @Override // dagger.internal.Binding
    public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
        injectMembersBindings.add(this.mSettingsProvider);
        injectMembersBindings.add(this.mGuidedWorkoutService);
        injectMembersBindings.add(this.supertype);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // dagger.internal.Binding, javax.inject.Provider
    public WorkoutPlanScheduleFragmentV1 get() {
        WorkoutPlanScheduleFragmentV1 result = new WorkoutPlanScheduleFragmentV1();
        injectMembers(result);
        return result;
    }

    @Override // dagger.internal.Binding, dagger.MembersInjector
    public void injectMembers(WorkoutPlanScheduleFragmentV1 object) {
        object.mSettingsProvider = this.mSettingsProvider.get();
        object.mGuidedWorkoutService = this.mGuidedWorkoutService.get();
        this.supertype.injectMembers(object);
    }
}

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
public final class WorkoutPlanSummaryFragmentV1$$InjectAdapter extends Binding<WorkoutPlanSummaryFragmentV1> implements Provider<WorkoutPlanSummaryFragmentV1>, MembersInjector<WorkoutPlanSummaryFragmentV1> {
    private Binding<GuidedWorkoutService> mGuidedWorkoutService;
    private Binding<SettingsProvider> mSettingsProvider;
    private Binding<BaseFragmentWithOfflineSupport> supertype;

    public WorkoutPlanSummaryFragmentV1$$InjectAdapter() {
        super("com.microsoft.kapp.fragments.guidedworkout.WorkoutPlanSummaryFragmentV1", "members/com.microsoft.kapp.fragments.guidedworkout.WorkoutPlanSummaryFragmentV1", false, WorkoutPlanSummaryFragmentV1.class);
    }

    @Override // dagger.internal.Binding
    public void attach(Linker linker) {
        this.mSettingsProvider = linker.requestBinding("com.microsoft.kapp.services.SettingsProvider", WorkoutPlanSummaryFragmentV1.class, getClass().getClassLoader());
        this.mGuidedWorkoutService = linker.requestBinding("com.microsoft.kapp.services.guidedworkout.GuidedWorkoutService", WorkoutPlanSummaryFragmentV1.class, getClass().getClassLoader());
        this.supertype = linker.requestBinding("members/com.microsoft.kapp.fragments.BaseFragmentWithOfflineSupport", WorkoutPlanSummaryFragmentV1.class, getClass().getClassLoader(), false, true);
    }

    @Override // dagger.internal.Binding
    public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
        injectMembersBindings.add(this.mSettingsProvider);
        injectMembersBindings.add(this.mGuidedWorkoutService);
        injectMembersBindings.add(this.supertype);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // dagger.internal.Binding, javax.inject.Provider
    public WorkoutPlanSummaryFragmentV1 get() {
        WorkoutPlanSummaryFragmentV1 result = new WorkoutPlanSummaryFragmentV1();
        injectMembers(result);
        return result;
    }

    @Override // dagger.internal.Binding, dagger.MembersInjector
    public void injectMembers(WorkoutPlanSummaryFragmentV1 object) {
        object.mSettingsProvider = this.mSettingsProvider.get();
        object.mGuidedWorkoutService = this.mGuidedWorkoutService.get();
        this.supertype.injectMembers(object);
    }
}

package com.microsoft.kapp.tasks.GuidedWorkout;

import com.microsoft.kapp.multidevice.MultiDeviceManager;
import com.microsoft.kapp.services.SettingsProvider;
import com.microsoft.kapp.services.guidedworkout.GuidedWorkoutService;
import com.microsoft.kapp.services.healthandfitness.HealthAndFitnessService;
import com.microsoft.krestsdk.services.RestService;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;
/* loaded from: classes.dex */
public final class CalculateNextWorkoutOperation$$InjectAdapter extends Binding<CalculateNextWorkoutOperation> implements MembersInjector<CalculateNextWorkoutOperation> {
    private Binding<GuidedWorkoutService> mGuidedWorkoutService;
    private Binding<HealthAndFitnessService> mHnFService;
    private Binding<MultiDeviceManager> mMultiDeviceManager;
    private Binding<RestService> mRestService;
    private Binding<SettingsProvider> mSettingsProvider;

    public CalculateNextWorkoutOperation$$InjectAdapter() {
        super(null, "members/com.microsoft.kapp.tasks.GuidedWorkout.CalculateNextWorkoutOperation", false, CalculateNextWorkoutOperation.class);
    }

    @Override // dagger.internal.Binding
    public void attach(Linker linker) {
        this.mRestService = linker.requestBinding("com.microsoft.krestsdk.services.RestService", CalculateNextWorkoutOperation.class, getClass().getClassLoader());
        this.mHnFService = linker.requestBinding("com.microsoft.kapp.services.healthandfitness.HealthAndFitnessService", CalculateNextWorkoutOperation.class, getClass().getClassLoader());
        this.mGuidedWorkoutService = linker.requestBinding("com.microsoft.kapp.services.guidedworkout.GuidedWorkoutService", CalculateNextWorkoutOperation.class, getClass().getClassLoader());
        this.mSettingsProvider = linker.requestBinding("com.microsoft.kapp.services.SettingsProvider", CalculateNextWorkoutOperation.class, getClass().getClassLoader());
        this.mMultiDeviceManager = linker.requestBinding("com.microsoft.kapp.multidevice.MultiDeviceManager", CalculateNextWorkoutOperation.class, getClass().getClassLoader());
    }

    @Override // dagger.internal.Binding
    public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
        injectMembersBindings.add(this.mRestService);
        injectMembersBindings.add(this.mHnFService);
        injectMembersBindings.add(this.mGuidedWorkoutService);
        injectMembersBindings.add(this.mSettingsProvider);
        injectMembersBindings.add(this.mMultiDeviceManager);
    }

    @Override // dagger.internal.Binding, dagger.MembersInjector
    public void injectMembers(CalculateNextWorkoutOperation object) {
        object.mRestService = this.mRestService.get();
        object.mHnFService = this.mHnFService.get();
        object.mGuidedWorkoutService = this.mGuidedWorkoutService.get();
        object.mSettingsProvider = this.mSettingsProvider.get();
        object.mMultiDeviceManager = this.mMultiDeviceManager.get();
    }
}

package com.microsoft.kapp.tasks.GuidedWorkout;

import android.content.Context;
import com.microsoft.kapp.CargoConnection;
import com.microsoft.kapp.services.SettingsProvider;
import com.microsoft.kapp.services.guidedworkout.GuidedWorkoutService;
import com.microsoft.kapp.services.healthandfitness.HealthAndFitnessService;
import com.microsoft.krestsdk.services.RestService;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;
/* loaded from: classes.dex */
public final class PushWorkoutToDeviceOperation$$InjectAdapter extends Binding<PushWorkoutToDeviceOperation> implements MembersInjector<PushWorkoutToDeviceOperation> {
    private Binding<CargoConnection> mCargoConnection;
    private Binding<Context> mContext;
    private Binding<GuidedWorkoutService> mGuidedWorkoutService;
    private Binding<HealthAndFitnessService> mHnFService;
    private Binding<RestService> mRestService;
    private Binding<SettingsProvider> mSettingsProvider;

    public PushWorkoutToDeviceOperation$$InjectAdapter() {
        super(null, "members/com.microsoft.kapp.tasks.GuidedWorkout.PushWorkoutToDeviceOperation", false, PushWorkoutToDeviceOperation.class);
    }

    @Override // dagger.internal.Binding
    public void attach(Linker linker) {
        this.mRestService = linker.requestBinding("com.microsoft.krestsdk.services.RestService", PushWorkoutToDeviceOperation.class, getClass().getClassLoader());
        this.mHnFService = linker.requestBinding("com.microsoft.kapp.services.healthandfitness.HealthAndFitnessService", PushWorkoutToDeviceOperation.class, getClass().getClassLoader());
        this.mGuidedWorkoutService = linker.requestBinding("com.microsoft.kapp.services.guidedworkout.GuidedWorkoutService", PushWorkoutToDeviceOperation.class, getClass().getClassLoader());
        this.mCargoConnection = linker.requestBinding("com.microsoft.kapp.CargoConnection", PushWorkoutToDeviceOperation.class, getClass().getClassLoader());
        this.mSettingsProvider = linker.requestBinding("com.microsoft.kapp.services.SettingsProvider", PushWorkoutToDeviceOperation.class, getClass().getClassLoader());
        this.mContext = linker.requestBinding("android.content.Context", PushWorkoutToDeviceOperation.class, getClass().getClassLoader());
    }

    @Override // dagger.internal.Binding
    public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
        injectMembersBindings.add(this.mRestService);
        injectMembersBindings.add(this.mHnFService);
        injectMembersBindings.add(this.mGuidedWorkoutService);
        injectMembersBindings.add(this.mCargoConnection);
        injectMembersBindings.add(this.mSettingsProvider);
        injectMembersBindings.add(this.mContext);
    }

    @Override // dagger.internal.Binding, dagger.MembersInjector
    public void injectMembers(PushWorkoutToDeviceOperation object) {
        object.mRestService = this.mRestService.get();
        object.mHnFService = this.mHnFService.get();
        object.mGuidedWorkoutService = this.mGuidedWorkoutService.get();
        object.mCargoConnection = this.mCargoConnection.get();
        object.mSettingsProvider = this.mSettingsProvider.get();
        object.mContext = this.mContext.get();
    }
}

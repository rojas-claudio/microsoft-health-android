package com.microsoft.kapp.widgets;

import com.microsoft.kapp.models.goal.GoalProcessorManager;
import com.microsoft.kapp.sensor.SensorDataDebugProvider;
import com.microsoft.kapp.services.SettingsProvider;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;
/* loaded from: classes.dex */
public final class StepsHomeTile$$InjectAdapter extends Binding<StepsHomeTile> implements MembersInjector<StepsHomeTile> {
    private Binding<GoalProcessorManager> mGoalProcessorManager;
    private Binding<SensorDataDebugProvider> mSensorDataDebugProvider;
    private Binding<SettingsProvider> mSettingsProvider;
    private Binding<HomeTile> supertype;

    public StepsHomeTile$$InjectAdapter() {
        super(null, "members/com.microsoft.kapp.widgets.StepsHomeTile", false, StepsHomeTile.class);
    }

    @Override // dagger.internal.Binding
    public void attach(Linker linker) {
        this.mGoalProcessorManager = linker.requestBinding("com.microsoft.kapp.models.goal.GoalProcessorManager", StepsHomeTile.class, getClass().getClassLoader());
        this.mSettingsProvider = linker.requestBinding("com.microsoft.kapp.services.SettingsProvider", StepsHomeTile.class, getClass().getClassLoader());
        this.mSensorDataDebugProvider = linker.requestBinding("com.microsoft.kapp.sensor.SensorDataDebugProvider", StepsHomeTile.class, getClass().getClassLoader());
        this.supertype = linker.requestBinding("members/com.microsoft.kapp.widgets.HomeTile", StepsHomeTile.class, getClass().getClassLoader(), false, true);
    }

    @Override // dagger.internal.Binding
    public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
        injectMembersBindings.add(this.mGoalProcessorManager);
        injectMembersBindings.add(this.mSettingsProvider);
        injectMembersBindings.add(this.mSensorDataDebugProvider);
        injectMembersBindings.add(this.supertype);
    }

    @Override // dagger.internal.Binding, dagger.MembersInjector
    public void injectMembers(StepsHomeTile object) {
        object.mGoalProcessorManager = this.mGoalProcessorManager.get();
        object.mSettingsProvider = this.mSettingsProvider.get();
        object.mSensorDataDebugProvider = this.mSensorDataDebugProvider.get();
        this.supertype.injectMembers(object);
    }
}

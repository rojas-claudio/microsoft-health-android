package com.microsoft.kapp.widgets;

import com.microsoft.kapp.models.goal.GoalProcessorManager;
import com.microsoft.kapp.services.SettingsProvider;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;
/* loaded from: classes.dex */
public final class CaloriesHomeTile$$InjectAdapter extends Binding<CaloriesHomeTile> implements MembersInjector<CaloriesHomeTile> {
    private Binding<GoalProcessorManager> mGoalProcessorManager;
    private Binding<SettingsProvider> mSettingsProvider;
    private Binding<HomeTile> supertype;

    public CaloriesHomeTile$$InjectAdapter() {
        super(null, "members/com.microsoft.kapp.widgets.CaloriesHomeTile", false, CaloriesHomeTile.class);
    }

    @Override // dagger.internal.Binding
    public void attach(Linker linker) {
        this.mGoalProcessorManager = linker.requestBinding("com.microsoft.kapp.models.goal.GoalProcessorManager", CaloriesHomeTile.class, getClass().getClassLoader());
        this.mSettingsProvider = linker.requestBinding("com.microsoft.kapp.services.SettingsProvider", CaloriesHomeTile.class, getClass().getClassLoader());
        this.supertype = linker.requestBinding("members/com.microsoft.kapp.widgets.HomeTile", CaloriesHomeTile.class, getClass().getClassLoader(), false, true);
    }

    @Override // dagger.internal.Binding
    public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
        injectMembersBindings.add(this.mGoalProcessorManager);
        injectMembersBindings.add(this.mSettingsProvider);
        injectMembersBindings.add(this.supertype);
    }

    @Override // dagger.internal.Binding, dagger.MembersInjector
    public void injectMembers(CaloriesHomeTile object) {
        object.mGoalProcessorManager = this.mGoalProcessorManager.get();
        object.mSettingsProvider = this.mSettingsProvider.get();
        this.supertype.injectMembers(object);
    }
}

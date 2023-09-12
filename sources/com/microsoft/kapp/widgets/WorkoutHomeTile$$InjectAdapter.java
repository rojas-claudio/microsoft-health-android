package com.microsoft.kapp.widgets;

import com.microsoft.kapp.services.SettingsProvider;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;
/* loaded from: classes.dex */
public final class WorkoutHomeTile$$InjectAdapter extends Binding<WorkoutHomeTile> implements MembersInjector<WorkoutHomeTile> {
    private Binding<SettingsProvider> mSettingsProvider;
    private Binding<HomeTile> supertype;

    public WorkoutHomeTile$$InjectAdapter() {
        super(null, "members/com.microsoft.kapp.widgets.WorkoutHomeTile", false, WorkoutHomeTile.class);
    }

    @Override // dagger.internal.Binding
    public void attach(Linker linker) {
        this.mSettingsProvider = linker.requestBinding("com.microsoft.kapp.services.SettingsProvider", WorkoutHomeTile.class, getClass().getClassLoader());
        this.supertype = linker.requestBinding("members/com.microsoft.kapp.widgets.HomeTile", WorkoutHomeTile.class, getClass().getClassLoader(), false, true);
    }

    @Override // dagger.internal.Binding
    public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
        injectMembersBindings.add(this.mSettingsProvider);
        injectMembersBindings.add(this.supertype);
    }

    @Override // dagger.internal.Binding, dagger.MembersInjector
    public void injectMembers(WorkoutHomeTile object) {
        object.mSettingsProvider = this.mSettingsProvider.get();
        this.supertype.injectMembers(object);
    }
}

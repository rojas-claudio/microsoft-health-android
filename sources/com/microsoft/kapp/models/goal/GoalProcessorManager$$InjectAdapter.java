package com.microsoft.kapp.models.goal;

import android.content.Context;
import com.microsoft.kapp.services.SettingsProvider;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class GoalProcessorManager$$InjectAdapter extends Binding<GoalProcessorManager> implements Provider<GoalProcessorManager> {
    private Binding<Context> context;
    private Binding<SettingsProvider> settingsProvider;

    public GoalProcessorManager$$InjectAdapter() {
        super("com.microsoft.kapp.models.goal.GoalProcessorManager", "members/com.microsoft.kapp.models.goal.GoalProcessorManager", false, GoalProcessorManager.class);
    }

    @Override // dagger.internal.Binding
    public void attach(Linker linker) {
        this.context = linker.requestBinding("android.content.Context", GoalProcessorManager.class, getClass().getClassLoader());
        this.settingsProvider = linker.requestBinding("com.microsoft.kapp.services.SettingsProvider", GoalProcessorManager.class, getClass().getClassLoader());
    }

    @Override // dagger.internal.Binding
    public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
        getBindings.add(this.context);
        getBindings.add(this.settingsProvider);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // dagger.internal.Binding, javax.inject.Provider
    public GoalProcessorManager get() {
        GoalProcessorManager result = new GoalProcessorManager(this.context.get(), this.settingsProvider.get());
        return result;
    }
}

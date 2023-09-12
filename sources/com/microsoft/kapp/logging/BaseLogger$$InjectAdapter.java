package com.microsoft.kapp.logging;

import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;
/* loaded from: classes.dex */
public final class BaseLogger$$InjectAdapter extends Binding<BaseLogger> implements MembersInjector<BaseLogger> {
    private Binding<LogConfiguration> mLogConfiguration;

    public BaseLogger$$InjectAdapter() {
        super(null, "members/com.microsoft.kapp.logging.BaseLogger", false, BaseLogger.class);
    }

    @Override // dagger.internal.Binding
    public void attach(Linker linker) {
        this.mLogConfiguration = linker.requestBinding("com.microsoft.kapp.logging.LogConfiguration", BaseLogger.class, getClass().getClassLoader());
    }

    @Override // dagger.internal.Binding
    public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
        injectMembersBindings.add(this.mLogConfiguration);
    }

    @Override // dagger.internal.Binding, dagger.MembersInjector
    public void injectMembers(BaseLogger object) {
        object.mLogConfiguration = this.mLogConfiguration.get();
    }
}

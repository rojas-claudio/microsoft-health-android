package com.microsoft.kapp.logging;

import android.content.Context;
import com.microsoft.kapp.services.InjectableIntentService;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class LogCleanupService$$InjectAdapter extends Binding<LogCleanupService> implements Provider<LogCleanupService>, MembersInjector<LogCleanupService> {
    private Binding<Context> mContext;
    private Binding<LogConfiguration> mLogConfiguration;
    private Binding<InjectableIntentService> supertype;

    public LogCleanupService$$InjectAdapter() {
        super("com.microsoft.kapp.logging.LogCleanupService", "members/com.microsoft.kapp.logging.LogCleanupService", false, LogCleanupService.class);
    }

    @Override // dagger.internal.Binding
    public void attach(Linker linker) {
        this.mContext = linker.requestBinding("android.content.Context", LogCleanupService.class, getClass().getClassLoader());
        this.mLogConfiguration = linker.requestBinding("com.microsoft.kapp.logging.LogConfiguration", LogCleanupService.class, getClass().getClassLoader());
        this.supertype = linker.requestBinding("members/com.microsoft.kapp.services.InjectableIntentService", LogCleanupService.class, getClass().getClassLoader(), false, true);
    }

    @Override // dagger.internal.Binding
    public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
        injectMembersBindings.add(this.mContext);
        injectMembersBindings.add(this.mLogConfiguration);
        injectMembersBindings.add(this.supertype);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // dagger.internal.Binding, javax.inject.Provider
    public LogCleanupService get() {
        LogCleanupService result = new LogCleanupService();
        injectMembers(result);
        return result;
    }

    @Override // dagger.internal.Binding, dagger.MembersInjector
    public void injectMembers(LogCleanupService object) {
        object.mContext = this.mContext.get();
        object.mLogConfiguration = this.mLogConfiguration.get();
        this.supertype.injectMembers(object);
    }
}

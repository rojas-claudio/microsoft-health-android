package com.microsoft.kapp.database;

import android.content.Context;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class LoggingContentResolver$$InjectAdapter extends Binding<LoggingContentResolver> implements Provider<LoggingContentResolver> {
    private Binding<Context> context;

    public LoggingContentResolver$$InjectAdapter() {
        super("com.microsoft.kapp.database.LoggingContentResolver", "members/com.microsoft.kapp.database.LoggingContentResolver", false, LoggingContentResolver.class);
    }

    @Override // dagger.internal.Binding
    public void attach(Linker linker) {
        this.context = linker.requestBinding("android.content.Context", LoggingContentResolver.class, getClass().getClassLoader());
    }

    @Override // dagger.internal.Binding
    public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
        getBindings.add(this.context);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // dagger.internal.Binding, javax.inject.Provider
    public LoggingContentResolver get() {
        LoggingContentResolver result = new LoggingContentResolver(this.context.get());
        return result;
    }
}

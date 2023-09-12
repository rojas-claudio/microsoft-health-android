package com.microsoft.kapp.logging;

import dagger.internal.Binding;
import dagger.internal.Linker;
import dagger.internal.StaticInjection;
/* loaded from: classes.dex */
public final class KLog$$StaticInjection extends StaticInjection {
    private Binding<LogConfiguration> mLogConfiguration;
    private Binding<LogFormatManager> mLogFormatManager;

    @Override // dagger.internal.StaticInjection
    public void attach(Linker linker) {
        this.mLogFormatManager = linker.requestBinding("com.microsoft.kapp.logging.LogFormatManager", KLog.class, getClass().getClassLoader());
        this.mLogConfiguration = linker.requestBinding("com.microsoft.kapp.logging.LogConfiguration", KLog.class, getClass().getClassLoader());
    }

    @Override // dagger.internal.StaticInjection
    public void inject() {
        KLog.mLogFormatManager = this.mLogFormatManager.get();
        KLog.mLogConfiguration = this.mLogConfiguration.get();
    }
}

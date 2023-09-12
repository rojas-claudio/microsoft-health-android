package com.microsoft.kapp.version;

import dagger.internal.Binding;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class ApplicationUpdateLauncher$$InjectAdapter extends Binding<ApplicationUpdateLauncher> implements Provider<ApplicationUpdateLauncher> {
    public ApplicationUpdateLauncher$$InjectAdapter() {
        super("com.microsoft.kapp.version.ApplicationUpdateLauncher", "members/com.microsoft.kapp.version.ApplicationUpdateLauncher", true, ApplicationUpdateLauncher.class);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // dagger.internal.Binding, javax.inject.Provider
    public ApplicationUpdateLauncher get() {
        ApplicationUpdateLauncher result = new ApplicationUpdateLauncher();
        return result;
    }
}

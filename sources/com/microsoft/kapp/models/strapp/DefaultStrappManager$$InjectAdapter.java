package com.microsoft.kapp.models.strapp;

import com.microsoft.kapp.utils.AppConfigurationManager;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class DefaultStrappManager$$InjectAdapter extends Binding<DefaultStrappManager> implements Provider<DefaultStrappManager>, MembersInjector<DefaultStrappManager> {
    private Binding<AppConfigurationManager> mAppConfigurationManager;

    public DefaultStrappManager$$InjectAdapter() {
        super("com.microsoft.kapp.models.strapp.DefaultStrappManager", "members/com.microsoft.kapp.models.strapp.DefaultStrappManager", false, DefaultStrappManager.class);
    }

    @Override // dagger.internal.Binding
    public void attach(Linker linker) {
        this.mAppConfigurationManager = linker.requestBinding("com.microsoft.kapp.utils.AppConfigurationManager", DefaultStrappManager.class, getClass().getClassLoader());
    }

    @Override // dagger.internal.Binding
    public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
        injectMembersBindings.add(this.mAppConfigurationManager);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // dagger.internal.Binding, javax.inject.Provider
    public DefaultStrappManager get() {
        DefaultStrappManager result = new DefaultStrappManager();
        injectMembers(result);
        return result;
    }

    @Override // dagger.internal.Binding, dagger.MembersInjector
    public void injectMembers(DefaultStrappManager object) {
        object.mAppConfigurationManager = this.mAppConfigurationManager.get();
    }
}

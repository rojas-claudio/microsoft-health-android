package com.microsoft.kapp.version;

import android.content.Context;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class ApplicationVersionUpdateNotifier$$InjectAdapter extends Binding<ApplicationVersionUpdateNotifier> implements Provider<ApplicationVersionUpdateNotifier>, MembersInjector<ApplicationVersionUpdateNotifier> {
    private Binding<Context> context;
    private Binding<DefaultApplicationVersionRetriever> retriever;
    private Binding<VersionUpdateNotifier> supertype;

    public ApplicationVersionUpdateNotifier$$InjectAdapter() {
        super("com.microsoft.kapp.version.ApplicationVersionUpdateNotifier", "members/com.microsoft.kapp.version.ApplicationVersionUpdateNotifier", true, ApplicationVersionUpdateNotifier.class);
    }

    @Override // dagger.internal.Binding
    public void attach(Linker linker) {
        this.context = linker.requestBinding("android.content.Context", ApplicationVersionUpdateNotifier.class, getClass().getClassLoader());
        this.retriever = linker.requestBinding("com.microsoft.kapp.version.DefaultApplicationVersionRetriever", ApplicationVersionUpdateNotifier.class, getClass().getClassLoader());
        this.supertype = linker.requestBinding("members/com.microsoft.kapp.version.VersionUpdateNotifier", ApplicationVersionUpdateNotifier.class, getClass().getClassLoader(), false, true);
    }

    @Override // dagger.internal.Binding
    public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
        getBindings.add(this.context);
        getBindings.add(this.retriever);
        injectMembersBindings.add(this.supertype);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // dagger.internal.Binding, javax.inject.Provider
    public ApplicationVersionUpdateNotifier get() {
        ApplicationVersionUpdateNotifier result = new ApplicationVersionUpdateNotifier(this.context.get(), this.retriever.get());
        injectMembers(result);
        return result;
    }

    @Override // dagger.internal.Binding, dagger.MembersInjector
    public void injectMembers(ApplicationVersionUpdateNotifier object) {
        this.supertype.injectMembers(object);
    }
}

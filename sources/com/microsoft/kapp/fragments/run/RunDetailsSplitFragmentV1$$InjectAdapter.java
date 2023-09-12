package com.microsoft.kapp.fragments.run;

import com.microsoft.kapp.fragments.BaseHomeTileFragment;
import com.microsoft.kapp.services.SettingsProvider;
import com.microsoft.krestsdk.services.RestService;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class RunDetailsSplitFragmentV1$$InjectAdapter extends Binding<RunDetailsSplitFragmentV1> implements Provider<RunDetailsSplitFragmentV1>, MembersInjector<RunDetailsSplitFragmentV1> {
    private Binding<RestService> mRestService;
    private Binding<SettingsProvider> mSettings;
    private Binding<BaseHomeTileFragment> supertype;

    public RunDetailsSplitFragmentV1$$InjectAdapter() {
        super("com.microsoft.kapp.fragments.run.RunDetailsSplitFragmentV1", "members/com.microsoft.kapp.fragments.run.RunDetailsSplitFragmentV1", false, RunDetailsSplitFragmentV1.class);
    }

    @Override // dagger.internal.Binding
    public void attach(Linker linker) {
        this.mSettings = linker.requestBinding("com.microsoft.kapp.services.SettingsProvider", RunDetailsSplitFragmentV1.class, getClass().getClassLoader());
        this.mRestService = linker.requestBinding("com.microsoft.krestsdk.services.RestService", RunDetailsSplitFragmentV1.class, getClass().getClassLoader());
        this.supertype = linker.requestBinding("members/com.microsoft.kapp.fragments.BaseHomeTileFragment", RunDetailsSplitFragmentV1.class, getClass().getClassLoader(), false, true);
    }

    @Override // dagger.internal.Binding
    public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
        injectMembersBindings.add(this.mSettings);
        injectMembersBindings.add(this.mRestService);
        injectMembersBindings.add(this.supertype);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // dagger.internal.Binding, javax.inject.Provider
    public RunDetailsSplitFragmentV1 get() {
        RunDetailsSplitFragmentV1 result = new RunDetailsSplitFragmentV1();
        injectMembers(result);
        return result;
    }

    @Override // dagger.internal.Binding, dagger.MembersInjector
    public void injectMembers(RunDetailsSplitFragmentV1 object) {
        object.mSettings = this.mSettings.get();
        object.mRestService = this.mRestService.get();
        this.supertype.injectMembers(object);
    }
}

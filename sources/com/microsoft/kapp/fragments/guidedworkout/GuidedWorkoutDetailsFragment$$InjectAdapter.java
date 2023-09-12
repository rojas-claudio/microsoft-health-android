package com.microsoft.kapp.fragments.guidedworkout;

import com.microsoft.kapp.fragments.BaseHomeTileFragment;
import com.microsoft.kapp.services.SettingsProvider;
import com.microsoft.kapp.services.guidedworkout.GuidedWorkoutService;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class GuidedWorkoutDetailsFragment$$InjectAdapter extends Binding<GuidedWorkoutDetailsFragment> implements Provider<GuidedWorkoutDetailsFragment>, MembersInjector<GuidedWorkoutDetailsFragment> {
    private Binding<GuidedWorkoutService> mGuidedWorkoutService;
    private Binding<SettingsProvider> mSettingsProvider;
    private Binding<BaseHomeTileFragment> supertype;

    public GuidedWorkoutDetailsFragment$$InjectAdapter() {
        super("com.microsoft.kapp.fragments.guidedworkout.GuidedWorkoutDetailsFragment", "members/com.microsoft.kapp.fragments.guidedworkout.GuidedWorkoutDetailsFragment", false, GuidedWorkoutDetailsFragment.class);
    }

    @Override // dagger.internal.Binding
    public void attach(Linker linker) {
        this.mGuidedWorkoutService = linker.requestBinding("com.microsoft.kapp.services.guidedworkout.GuidedWorkoutService", GuidedWorkoutDetailsFragment.class, getClass().getClassLoader());
        this.mSettingsProvider = linker.requestBinding("com.microsoft.kapp.services.SettingsProvider", GuidedWorkoutDetailsFragment.class, getClass().getClassLoader());
        this.supertype = linker.requestBinding("members/com.microsoft.kapp.fragments.BaseHomeTileFragment", GuidedWorkoutDetailsFragment.class, getClass().getClassLoader(), false, true);
    }

    @Override // dagger.internal.Binding
    public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
        injectMembersBindings.add(this.mGuidedWorkoutService);
        injectMembersBindings.add(this.mSettingsProvider);
        injectMembersBindings.add(this.supertype);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // dagger.internal.Binding, javax.inject.Provider
    public GuidedWorkoutDetailsFragment get() {
        GuidedWorkoutDetailsFragment result = new GuidedWorkoutDetailsFragment();
        injectMembers(result);
        return result;
    }

    @Override // dagger.internal.Binding, dagger.MembersInjector
    public void injectMembers(GuidedWorkoutDetailsFragment object) {
        object.mGuidedWorkoutService = this.mGuidedWorkoutService.get();
        object.mSettingsProvider = this.mSettingsProvider.get();
        this.supertype.injectMembers(object);
    }
}

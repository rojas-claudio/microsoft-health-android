package com.microsoft.kapp.models.home;

import com.microsoft.kapp.services.guidedworkout.GuidedWorkoutService;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class GuidedWorkoutDataFetcher$$InjectAdapter extends Binding<GuidedWorkoutDataFetcher> implements Provider<GuidedWorkoutDataFetcher>, MembersInjector<GuidedWorkoutDataFetcher> {
    private Binding<GuidedWorkoutService> mGuidedWorkoutService;

    public GuidedWorkoutDataFetcher$$InjectAdapter() {
        super("com.microsoft.kapp.models.home.GuidedWorkoutDataFetcher", "members/com.microsoft.kapp.models.home.GuidedWorkoutDataFetcher", false, GuidedWorkoutDataFetcher.class);
    }

    @Override // dagger.internal.Binding
    public void attach(Linker linker) {
        this.mGuidedWorkoutService = linker.requestBinding("com.microsoft.kapp.services.guidedworkout.GuidedWorkoutService", GuidedWorkoutDataFetcher.class, getClass().getClassLoader());
    }

    @Override // dagger.internal.Binding
    public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
        injectMembersBindings.add(this.mGuidedWorkoutService);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // dagger.internal.Binding, javax.inject.Provider
    public GuidedWorkoutDataFetcher get() {
        GuidedWorkoutDataFetcher result = new GuidedWorkoutDataFetcher();
        injectMembers(result);
        return result;
    }

    @Override // dagger.internal.Binding, dagger.MembersInjector
    public void injectMembers(GuidedWorkoutDataFetcher object) {
        object.mGuidedWorkoutService = this.mGuidedWorkoutService.get();
    }
}

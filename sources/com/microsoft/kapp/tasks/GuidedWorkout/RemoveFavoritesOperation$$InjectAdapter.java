package com.microsoft.kapp.tasks.GuidedWorkout;

import com.microsoft.kapp.services.guidedworkout.GuidedWorkoutService;
import com.microsoft.krestsdk.services.RestService;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;
/* loaded from: classes.dex */
public final class RemoveFavoritesOperation$$InjectAdapter extends Binding<RemoveFavoritesOperation> implements MembersInjector<RemoveFavoritesOperation> {
    private Binding<GuidedWorkoutService> mGuidedWorkoutService;
    private Binding<RestService> mRestService;

    public RemoveFavoritesOperation$$InjectAdapter() {
        super(null, "members/com.microsoft.kapp.tasks.GuidedWorkout.RemoveFavoritesOperation", false, RemoveFavoritesOperation.class);
    }

    @Override // dagger.internal.Binding
    public void attach(Linker linker) {
        this.mRestService = linker.requestBinding("com.microsoft.krestsdk.services.RestService", RemoveFavoritesOperation.class, getClass().getClassLoader());
        this.mGuidedWorkoutService = linker.requestBinding("com.microsoft.kapp.services.guidedworkout.GuidedWorkoutService", RemoveFavoritesOperation.class, getClass().getClassLoader());
    }

    @Override // dagger.internal.Binding
    public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
        injectMembersBindings.add(this.mRestService);
        injectMembersBindings.add(this.mGuidedWorkoutService);
    }

    @Override // dagger.internal.Binding, dagger.MembersInjector
    public void injectMembers(RemoveFavoritesOperation object) {
        object.mRestService = this.mRestService.get();
        object.mGuidedWorkoutService = this.mGuidedWorkoutService.get();
    }
}

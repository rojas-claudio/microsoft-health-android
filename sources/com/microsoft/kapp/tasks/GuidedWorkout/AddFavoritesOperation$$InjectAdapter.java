package com.microsoft.kapp.tasks.GuidedWorkout;

import com.microsoft.kapp.services.guidedworkout.GuidedWorkoutService;
import com.microsoft.krestsdk.services.RestService;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;
/* loaded from: classes.dex */
public final class AddFavoritesOperation$$InjectAdapter extends Binding<AddFavoritesOperation> implements MembersInjector<AddFavoritesOperation> {
    private Binding<GuidedWorkoutService> mGuidedWorkoutService;
    private Binding<RestService> mRestService;

    public AddFavoritesOperation$$InjectAdapter() {
        super(null, "members/com.microsoft.kapp.tasks.GuidedWorkout.AddFavoritesOperation", false, AddFavoritesOperation.class);
    }

    @Override // dagger.internal.Binding
    public void attach(Linker linker) {
        this.mRestService = linker.requestBinding("com.microsoft.krestsdk.services.RestService", AddFavoritesOperation.class, getClass().getClassLoader());
        this.mGuidedWorkoutService = linker.requestBinding("com.microsoft.kapp.services.guidedworkout.GuidedWorkoutService", AddFavoritesOperation.class, getClass().getClassLoader());
    }

    @Override // dagger.internal.Binding
    public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
        injectMembersBindings.add(this.mRestService);
        injectMembersBindings.add(this.mGuidedWorkoutService);
    }

    @Override // dagger.internal.Binding, dagger.MembersInjector
    public void injectMembers(AddFavoritesOperation object) {
        object.mRestService = this.mRestService.get();
        object.mGuidedWorkoutService = this.mGuidedWorkoutService.get();
    }
}

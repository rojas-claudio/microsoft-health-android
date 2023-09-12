package com.microsoft.kapp.tasks.GuidedWorkout;

import com.microsoft.kapp.services.guidedworkout.GuidedWorkoutService;
import com.microsoft.krestsdk.services.RestService;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;
/* loaded from: classes.dex */
public final class UnsubscribeOperation$$InjectAdapter extends Binding<UnsubscribeOperation> implements MembersInjector<UnsubscribeOperation> {
    private Binding<GuidedWorkoutService> mGuidedWorkoutService;
    private Binding<RestService> mRestService;

    public UnsubscribeOperation$$InjectAdapter() {
        super(null, "members/com.microsoft.kapp.tasks.GuidedWorkout.UnsubscribeOperation", false, UnsubscribeOperation.class);
    }

    @Override // dagger.internal.Binding
    public void attach(Linker linker) {
        this.mRestService = linker.requestBinding("com.microsoft.krestsdk.services.RestService", UnsubscribeOperation.class, getClass().getClassLoader());
        this.mGuidedWorkoutService = linker.requestBinding("com.microsoft.kapp.services.guidedworkout.GuidedWorkoutService", UnsubscribeOperation.class, getClass().getClassLoader());
    }

    @Override // dagger.internal.Binding
    public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
        injectMembersBindings.add(this.mRestService);
        injectMembersBindings.add(this.mGuidedWorkoutService);
    }

    @Override // dagger.internal.Binding, dagger.MembersInjector
    public void injectMembers(UnsubscribeOperation object) {
        object.mRestService = this.mRestService.get();
        object.mGuidedWorkoutService = this.mGuidedWorkoutService.get();
    }
}

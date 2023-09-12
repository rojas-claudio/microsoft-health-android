package com.microsoft.kapp.tasks.GuidedWorkout;

import android.content.Context;
import com.microsoft.kapp.services.guidedworkout.GuidedWorkoutService;
import com.microsoft.krestsdk.services.RestService;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;
/* loaded from: classes.dex */
public final class SyncFirstWorkoutOperation$$InjectAdapter extends Binding<SyncFirstWorkoutOperation> implements MembersInjector<SyncFirstWorkoutOperation> {
    private Binding<Context> mContext;
    private Binding<GuidedWorkoutService> mGuidedWorkoutService;
    private Binding<RestService> mRestService;

    public SyncFirstWorkoutOperation$$InjectAdapter() {
        super(null, "members/com.microsoft.kapp.tasks.GuidedWorkout.SyncFirstWorkoutOperation", false, SyncFirstWorkoutOperation.class);
    }

    @Override // dagger.internal.Binding
    public void attach(Linker linker) {
        this.mRestService = linker.requestBinding("com.microsoft.krestsdk.services.RestService", SyncFirstWorkoutOperation.class, getClass().getClassLoader());
        this.mContext = linker.requestBinding("android.content.Context", SyncFirstWorkoutOperation.class, getClass().getClassLoader());
        this.mGuidedWorkoutService = linker.requestBinding("com.microsoft.kapp.services.guidedworkout.GuidedWorkoutService", SyncFirstWorkoutOperation.class, getClass().getClassLoader());
    }

    @Override // dagger.internal.Binding
    public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
        injectMembersBindings.add(this.mRestService);
        injectMembersBindings.add(this.mContext);
        injectMembersBindings.add(this.mGuidedWorkoutService);
    }

    @Override // dagger.internal.Binding, dagger.MembersInjector
    public void injectMembers(SyncFirstWorkoutOperation object) {
        object.mRestService = this.mRestService.get();
        object.mContext = this.mContext.get();
        object.mGuidedWorkoutService = this.mGuidedWorkoutService.get();
    }
}

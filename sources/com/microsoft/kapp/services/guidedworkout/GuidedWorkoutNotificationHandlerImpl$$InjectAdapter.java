package com.microsoft.kapp.services.guidedworkout;

import android.content.Context;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;
/* loaded from: classes.dex */
public final class GuidedWorkoutNotificationHandlerImpl$$InjectAdapter extends Binding<GuidedWorkoutNotificationHandlerImpl> implements MembersInjector<GuidedWorkoutNotificationHandlerImpl> {
    private Binding<Context> mApplicationContext;

    public GuidedWorkoutNotificationHandlerImpl$$InjectAdapter() {
        super(null, "members/com.microsoft.kapp.services.guidedworkout.GuidedWorkoutNotificationHandlerImpl", false, GuidedWorkoutNotificationHandlerImpl.class);
    }

    @Override // dagger.internal.Binding
    public void attach(Linker linker) {
        this.mApplicationContext = linker.requestBinding("android.content.Context", GuidedWorkoutNotificationHandlerImpl.class, getClass().getClassLoader());
    }

    @Override // dagger.internal.Binding
    public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
        injectMembersBindings.add(this.mApplicationContext);
    }

    @Override // dagger.internal.Binding, dagger.MembersInjector
    public void injectMembers(GuidedWorkoutNotificationHandlerImpl object) {
        object.mApplicationContext = this.mApplicationContext.get();
    }
}

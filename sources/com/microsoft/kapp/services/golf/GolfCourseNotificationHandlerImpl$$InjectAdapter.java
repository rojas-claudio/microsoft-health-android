package com.microsoft.kapp.services.golf;

import android.content.Context;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class GolfCourseNotificationHandlerImpl$$InjectAdapter extends Binding<GolfCourseNotificationHandlerImpl> implements Provider<GolfCourseNotificationHandlerImpl>, MembersInjector<GolfCourseNotificationHandlerImpl> {
    private Binding<Context> mApplicationContext;

    public GolfCourseNotificationHandlerImpl$$InjectAdapter() {
        super("com.microsoft.kapp.services.golf.GolfCourseNotificationHandlerImpl", "members/com.microsoft.kapp.services.golf.GolfCourseNotificationHandlerImpl", false, GolfCourseNotificationHandlerImpl.class);
    }

    @Override // dagger.internal.Binding
    public void attach(Linker linker) {
        this.mApplicationContext = linker.requestBinding("android.content.Context", GolfCourseNotificationHandlerImpl.class, getClass().getClassLoader());
    }

    @Override // dagger.internal.Binding
    public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
        injectMembersBindings.add(this.mApplicationContext);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // dagger.internal.Binding, javax.inject.Provider
    public GolfCourseNotificationHandlerImpl get() {
        GolfCourseNotificationHandlerImpl result = new GolfCourseNotificationHandlerImpl();
        injectMembers(result);
        return result;
    }

    @Override // dagger.internal.Binding, dagger.MembersInjector
    public void injectMembers(GolfCourseNotificationHandlerImpl object) {
        object.mApplicationContext = this.mApplicationContext.get();
    }
}

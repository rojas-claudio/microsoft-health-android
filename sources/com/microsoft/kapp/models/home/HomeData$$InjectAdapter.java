package com.microsoft.kapp.models.home;

import android.content.Context;
import com.microsoft.kapp.services.guidedworkout.GuidedWorkoutService;
import com.microsoft.krestsdk.services.RestService;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class HomeData$$InjectAdapter extends Binding<HomeData> implements Provider<HomeData>, MembersInjector<HomeData> {
    private Binding<Context> mContext;
    private Binding<GuidedWorkoutService> mGuidedWorkoutService;
    private Binding<RestService> mRestService;

    public HomeData$$InjectAdapter() {
        super("com.microsoft.kapp.models.home.HomeData", "members/com.microsoft.kapp.models.home.HomeData", false, HomeData.class);
    }

    @Override // dagger.internal.Binding
    public void attach(Linker linker) {
        this.mRestService = linker.requestBinding("com.microsoft.krestsdk.services.RestService", HomeData.class, getClass().getClassLoader());
        this.mContext = linker.requestBinding("android.content.Context", HomeData.class, getClass().getClassLoader());
        this.mGuidedWorkoutService = linker.requestBinding("com.microsoft.kapp.services.guidedworkout.GuidedWorkoutService", HomeData.class, getClass().getClassLoader());
    }

    @Override // dagger.internal.Binding
    public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
        injectMembersBindings.add(this.mRestService);
        injectMembersBindings.add(this.mContext);
        injectMembersBindings.add(this.mGuidedWorkoutService);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // dagger.internal.Binding, javax.inject.Provider
    public HomeData get() {
        HomeData result = new HomeData();
        injectMembers(result);
        return result;
    }

    @Override // dagger.internal.Binding, dagger.MembersInjector
    public void injectMembers(HomeData object) {
        object.mRestService = this.mRestService.get();
        object.mContext = this.mContext.get();
        object.mGuidedWorkoutService = this.mGuidedWorkoutService.get();
    }
}

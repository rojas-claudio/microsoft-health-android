package com.microsoft.kapp.fragments.golf;

import com.microsoft.kapp.fragments.BaseFragmentWithOfflineSupport;
import com.microsoft.kapp.services.golf.GolfService;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class GolfCourseProTipFragment$$InjectAdapter extends Binding<GolfCourseProTipFragment> implements Provider<GolfCourseProTipFragment>, MembersInjector<GolfCourseProTipFragment> {
    private Binding<GolfService> golfService;
    private Binding<BaseFragmentWithOfflineSupport> supertype;

    public GolfCourseProTipFragment$$InjectAdapter() {
        super("com.microsoft.kapp.fragments.golf.GolfCourseProTipFragment", "members/com.microsoft.kapp.fragments.golf.GolfCourseProTipFragment", false, GolfCourseProTipFragment.class);
    }

    @Override // dagger.internal.Binding
    public void attach(Linker linker) {
        this.golfService = linker.requestBinding("com.microsoft.kapp.services.golf.GolfService", GolfCourseProTipFragment.class, getClass().getClassLoader());
        this.supertype = linker.requestBinding("members/com.microsoft.kapp.fragments.BaseFragmentWithOfflineSupport", GolfCourseProTipFragment.class, getClass().getClassLoader(), false, true);
    }

    @Override // dagger.internal.Binding
    public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
        injectMembersBindings.add(this.golfService);
        injectMembersBindings.add(this.supertype);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // dagger.internal.Binding, javax.inject.Provider
    public GolfCourseProTipFragment get() {
        GolfCourseProTipFragment result = new GolfCourseProTipFragment();
        injectMembers(result);
        return result;
    }

    @Override // dagger.internal.Binding, dagger.MembersInjector
    public void injectMembers(GolfCourseProTipFragment object) {
        object.golfService = this.golfService.get();
        this.supertype.injectMembers(object);
    }
}

package com.microsoft.kapp.activities.golf;

import com.microsoft.kapp.activities.BaseFragmentActivityWithOfflineSupport;
import com.microsoft.kapp.services.golf.GolfService;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;
/* loaded from: classes.dex */
public final class GolfFindCourseBaseActivity$$InjectAdapter extends Binding<GolfFindCourseBaseActivity> implements MembersInjector<GolfFindCourseBaseActivity> {
    private Binding<GolfService> mGolfService;
    private Binding<BaseFragmentActivityWithOfflineSupport> supertype;

    public GolfFindCourseBaseActivity$$InjectAdapter() {
        super(null, "members/com.microsoft.kapp.activities.golf.GolfFindCourseBaseActivity", false, GolfFindCourseBaseActivity.class);
    }

    @Override // dagger.internal.Binding
    public void attach(Linker linker) {
        this.mGolfService = linker.requestBinding("com.microsoft.kapp.services.golf.GolfService", GolfFindCourseBaseActivity.class, getClass().getClassLoader());
        this.supertype = linker.requestBinding("members/com.microsoft.kapp.activities.BaseFragmentActivityWithOfflineSupport", GolfFindCourseBaseActivity.class, getClass().getClassLoader(), false, true);
    }

    @Override // dagger.internal.Binding
    public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
        injectMembersBindings.add(this.mGolfService);
        injectMembersBindings.add(this.supertype);
    }

    @Override // dagger.internal.Binding, dagger.MembersInjector
    public void injectMembers(GolfFindCourseBaseActivity object) {
        object.mGolfService = this.mGolfService.get();
        this.supertype.injectMembers(object);
    }
}

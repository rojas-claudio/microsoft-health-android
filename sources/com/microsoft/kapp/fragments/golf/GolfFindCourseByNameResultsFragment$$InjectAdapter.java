package com.microsoft.kapp.fragments.golf;

import android.content.Context;
import com.microsoft.kapp.services.LocationService;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class GolfFindCourseByNameResultsFragment$$InjectAdapter extends Binding<GolfFindCourseByNameResultsFragment> implements Provider<GolfFindCourseByNameResultsFragment>, MembersInjector<GolfFindCourseByNameResultsFragment> {
    private Binding<Context> mContext;
    private Binding<LocationService> mLocationService;
    private Binding<GolfSearchResultsFragment> supertype;

    public GolfFindCourseByNameResultsFragment$$InjectAdapter() {
        super("com.microsoft.kapp.fragments.golf.GolfFindCourseByNameResultsFragment", "members/com.microsoft.kapp.fragments.golf.GolfFindCourseByNameResultsFragment", false, GolfFindCourseByNameResultsFragment.class);
    }

    @Override // dagger.internal.Binding
    public void attach(Linker linker) {
        this.mLocationService = linker.requestBinding("com.microsoft.kapp.services.LocationService", GolfFindCourseByNameResultsFragment.class, getClass().getClassLoader());
        this.mContext = linker.requestBinding("android.content.Context", GolfFindCourseByNameResultsFragment.class, getClass().getClassLoader());
        this.supertype = linker.requestBinding("members/com.microsoft.kapp.fragments.golf.GolfSearchResultsFragment", GolfFindCourseByNameResultsFragment.class, getClass().getClassLoader(), false, true);
    }

    @Override // dagger.internal.Binding
    public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
        injectMembersBindings.add(this.mLocationService);
        injectMembersBindings.add(this.mContext);
        injectMembersBindings.add(this.supertype);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // dagger.internal.Binding, javax.inject.Provider
    public GolfFindCourseByNameResultsFragment get() {
        GolfFindCourseByNameResultsFragment result = new GolfFindCourseByNameResultsFragment();
        injectMembers(result);
        return result;
    }

    @Override // dagger.internal.Binding, dagger.MembersInjector
    public void injectMembers(GolfFindCourseByNameResultsFragment object) {
        object.mLocationService = this.mLocationService.get();
        object.mContext = this.mContext.get();
        this.supertype.injectMembers(object);
    }
}

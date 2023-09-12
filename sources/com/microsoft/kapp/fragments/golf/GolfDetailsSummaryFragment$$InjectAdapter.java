package com.microsoft.kapp.fragments.golf;

import com.microsoft.kapp.fragments.BaseEventSummaryFragment;
import com.microsoft.kapp.services.golf.GolfService;
import com.microsoft.kapp.utils.GolfUtils;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class GolfDetailsSummaryFragment$$InjectAdapter extends Binding<GolfDetailsSummaryFragment> implements Provider<GolfDetailsSummaryFragment>, MembersInjector<GolfDetailsSummaryFragment> {
    private Binding<GolfService> mGolfService;
    private Binding<GolfUtils> mGolfUtils;
    private Binding<BaseEventSummaryFragment> supertype;

    public GolfDetailsSummaryFragment$$InjectAdapter() {
        super("com.microsoft.kapp.fragments.golf.GolfDetailsSummaryFragment", "members/com.microsoft.kapp.fragments.golf.GolfDetailsSummaryFragment", false, GolfDetailsSummaryFragment.class);
    }

    @Override // dagger.internal.Binding
    public void attach(Linker linker) {
        this.mGolfService = linker.requestBinding("com.microsoft.kapp.services.golf.GolfService", GolfDetailsSummaryFragment.class, getClass().getClassLoader());
        this.mGolfUtils = linker.requestBinding("com.microsoft.kapp.utils.GolfUtils", GolfDetailsSummaryFragment.class, getClass().getClassLoader());
        this.supertype = linker.requestBinding("members/com.microsoft.kapp.fragments.BaseEventSummaryFragment", GolfDetailsSummaryFragment.class, getClass().getClassLoader(), false, true);
    }

    @Override // dagger.internal.Binding
    public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
        injectMembersBindings.add(this.mGolfService);
        injectMembersBindings.add(this.mGolfUtils);
        injectMembersBindings.add(this.supertype);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // dagger.internal.Binding, javax.inject.Provider
    public GolfDetailsSummaryFragment get() {
        GolfDetailsSummaryFragment result = new GolfDetailsSummaryFragment();
        injectMembers(result);
        return result;
    }

    @Override // dagger.internal.Binding, dagger.MembersInjector
    public void injectMembers(GolfDetailsSummaryFragment object) {
        object.mGolfService = this.mGolfService.get();
        object.mGolfUtils = this.mGolfUtils.get();
        this.supertype.injectMembers(object);
    }
}

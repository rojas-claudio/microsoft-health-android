package com.microsoft.kapp.fragments.calories;

import com.microsoft.kapp.fragments.BaseTrackerDailyFragment;
import com.microsoft.kapp.utils.stat.StatUtils;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class CaloriesDailyFragment$$InjectAdapter extends Binding<CaloriesDailyFragment> implements Provider<CaloriesDailyFragment>, MembersInjector<CaloriesDailyFragment> {
    private Binding<StatUtils> mStatUtils;
    private Binding<BaseTrackerDailyFragment> supertype;

    public CaloriesDailyFragment$$InjectAdapter() {
        super("com.microsoft.kapp.fragments.calories.CaloriesDailyFragment", "members/com.microsoft.kapp.fragments.calories.CaloriesDailyFragment", false, CaloriesDailyFragment.class);
    }

    @Override // dagger.internal.Binding
    public void attach(Linker linker) {
        this.mStatUtils = linker.requestBinding("com.microsoft.kapp.utils.stat.StatUtils", CaloriesDailyFragment.class, getClass().getClassLoader());
        this.supertype = linker.requestBinding("members/com.microsoft.kapp.fragments.BaseTrackerDailyFragment", CaloriesDailyFragment.class, getClass().getClassLoader(), false, true);
    }

    @Override // dagger.internal.Binding
    public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
        injectMembersBindings.add(this.mStatUtils);
        injectMembersBindings.add(this.supertype);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // dagger.internal.Binding, javax.inject.Provider
    public CaloriesDailyFragment get() {
        CaloriesDailyFragment result = new CaloriesDailyFragment();
        injectMembers(result);
        return result;
    }

    @Override // dagger.internal.Binding, dagger.MembersInjector
    public void injectMembers(CaloriesDailyFragment object) {
        object.mStatUtils = this.mStatUtils.get();
        this.supertype.injectMembers(object);
    }
}

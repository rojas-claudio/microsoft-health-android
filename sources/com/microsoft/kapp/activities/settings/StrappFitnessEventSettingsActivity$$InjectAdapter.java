package com.microsoft.kapp.activities.settings;

import com.microsoft.kapp.activities.BaseActivity;
import com.microsoft.kapp.strappsettings.StrappSettingsManager;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;
/* loaded from: classes.dex */
public final class StrappFitnessEventSettingsActivity$$InjectAdapter extends Binding<StrappFitnessEventSettingsActivity> implements MembersInjector<StrappFitnessEventSettingsActivity> {
    private Binding<StrappSettingsManager> mStrappSettingsManager;
    private Binding<BaseActivity> supertype;

    public StrappFitnessEventSettingsActivity$$InjectAdapter() {
        super(null, "members/com.microsoft.kapp.activities.settings.StrappFitnessEventSettingsActivity", false, StrappFitnessEventSettingsActivity.class);
    }

    @Override // dagger.internal.Binding
    public void attach(Linker linker) {
        this.mStrappSettingsManager = linker.requestBinding("com.microsoft.kapp.strappsettings.StrappSettingsManager", StrappFitnessEventSettingsActivity.class, getClass().getClassLoader());
        this.supertype = linker.requestBinding("members/com.microsoft.kapp.activities.BaseActivity", StrappFitnessEventSettingsActivity.class, getClass().getClassLoader(), false, true);
    }

    @Override // dagger.internal.Binding
    public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
        injectMembersBindings.add(this.mStrappSettingsManager);
        injectMembersBindings.add(this.supertype);
    }

    @Override // dagger.internal.Binding, dagger.MembersInjector
    public void injectMembers(StrappFitnessEventSettingsActivity object) {
        object.mStrappSettingsManager = this.mStrappSettingsManager.get();
        this.supertype.injectMembers(object);
    }
}

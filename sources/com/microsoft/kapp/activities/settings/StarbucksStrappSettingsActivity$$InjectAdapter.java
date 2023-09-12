package com.microsoft.kapp.activities.settings;

import com.microsoft.kapp.activities.BaseFragmentActivity;
import com.microsoft.kapp.strappsettings.StrappSettingsManager;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class StarbucksStrappSettingsActivity$$InjectAdapter extends Binding<StarbucksStrappSettingsActivity> implements Provider<StarbucksStrappSettingsActivity>, MembersInjector<StarbucksStrappSettingsActivity> {
    private Binding<StrappSettingsManager> mStrappSettingsManager;
    private Binding<BaseFragmentActivity> supertype;

    public StarbucksStrappSettingsActivity$$InjectAdapter() {
        super("com.microsoft.kapp.activities.settings.StarbucksStrappSettingsActivity", "members/com.microsoft.kapp.activities.settings.StarbucksStrappSettingsActivity", false, StarbucksStrappSettingsActivity.class);
    }

    @Override // dagger.internal.Binding
    public void attach(Linker linker) {
        this.mStrappSettingsManager = linker.requestBinding("com.microsoft.kapp.strappsettings.StrappSettingsManager", StarbucksStrappSettingsActivity.class, getClass().getClassLoader());
        this.supertype = linker.requestBinding("members/com.microsoft.kapp.activities.BaseFragmentActivity", StarbucksStrappSettingsActivity.class, getClass().getClassLoader(), false, true);
    }

    @Override // dagger.internal.Binding
    public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
        injectMembersBindings.add(this.mStrappSettingsManager);
        injectMembersBindings.add(this.supertype);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // dagger.internal.Binding, javax.inject.Provider
    public StarbucksStrappSettingsActivity get() {
        StarbucksStrappSettingsActivity result = new StarbucksStrappSettingsActivity();
        injectMembers(result);
        return result;
    }

    @Override // dagger.internal.Binding, dagger.MembersInjector
    public void injectMembers(StarbucksStrappSettingsActivity object) {
        object.mStrappSettingsManager = this.mStrappSettingsManager.get();
        this.supertype.injectMembers(object);
    }
}

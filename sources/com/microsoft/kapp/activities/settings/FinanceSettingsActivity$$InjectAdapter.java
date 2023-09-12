package com.microsoft.kapp.activities.settings;

import com.microsoft.kapp.activities.BaseFragmentActivity;
import com.microsoft.kapp.strappsettings.StrappSettingsManager;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class FinanceSettingsActivity$$InjectAdapter extends Binding<FinanceSettingsActivity> implements Provider<FinanceSettingsActivity>, MembersInjector<FinanceSettingsActivity> {
    private Binding<StrappSettingsManager> mStrappSettingsManager;
    private Binding<BaseFragmentActivity> supertype;

    public FinanceSettingsActivity$$InjectAdapter() {
        super("com.microsoft.kapp.activities.settings.FinanceSettingsActivity", "members/com.microsoft.kapp.activities.settings.FinanceSettingsActivity", false, FinanceSettingsActivity.class);
    }

    @Override // dagger.internal.Binding
    public void attach(Linker linker) {
        this.mStrappSettingsManager = linker.requestBinding("com.microsoft.kapp.strappsettings.StrappSettingsManager", FinanceSettingsActivity.class, getClass().getClassLoader());
        this.supertype = linker.requestBinding("members/com.microsoft.kapp.activities.BaseFragmentActivity", FinanceSettingsActivity.class, getClass().getClassLoader(), false, true);
    }

    @Override // dagger.internal.Binding
    public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
        injectMembersBindings.add(this.mStrappSettingsManager);
        injectMembersBindings.add(this.supertype);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // dagger.internal.Binding, javax.inject.Provider
    public FinanceSettingsActivity get() {
        FinanceSettingsActivity result = new FinanceSettingsActivity();
        injectMembers(result);
        return result;
    }

    @Override // dagger.internal.Binding, dagger.MembersInjector
    public void injectMembers(FinanceSettingsActivity object) {
        object.mStrappSettingsManager = this.mStrappSettingsManager.get();
        this.supertype.injectMembers(object);
    }
}

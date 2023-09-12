package com.microsoft.kapp.activities.settings;

import com.microsoft.kapp.activities.BaseActivity;
import com.microsoft.kapp.strappsettings.StrappSettingsManager;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class StrappSettingsActivity$$InjectAdapter extends Binding<StrappSettingsActivity> implements Provider<StrappSettingsActivity>, MembersInjector<StrappSettingsActivity> {
    private Binding<StrappSettingsManager> mStrappSettingsManager;
    private Binding<BaseActivity> supertype;

    public StrappSettingsActivity$$InjectAdapter() {
        super("com.microsoft.kapp.activities.settings.StrappSettingsActivity", "members/com.microsoft.kapp.activities.settings.StrappSettingsActivity", false, StrappSettingsActivity.class);
    }

    @Override // dagger.internal.Binding
    public void attach(Linker linker) {
        this.mStrappSettingsManager = linker.requestBinding("com.microsoft.kapp.strappsettings.StrappSettingsManager", StrappSettingsActivity.class, getClass().getClassLoader());
        this.supertype = linker.requestBinding("members/com.microsoft.kapp.activities.BaseActivity", StrappSettingsActivity.class, getClass().getClassLoader(), false, true);
    }

    @Override // dagger.internal.Binding
    public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
        injectMembersBindings.add(this.mStrappSettingsManager);
        injectMembersBindings.add(this.supertype);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // dagger.internal.Binding, javax.inject.Provider
    public StrappSettingsActivity get() {
        StrappSettingsActivity result = new StrappSettingsActivity();
        injectMembers(result);
        return result;
    }

    @Override // dagger.internal.Binding, dagger.MembersInjector
    public void injectMembers(StrappSettingsActivity object) {
        object.mStrappSettingsManager = this.mStrappSettingsManager.get();
        this.supertype.injectMembers(object);
    }
}

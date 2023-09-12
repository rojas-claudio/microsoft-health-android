package com.microsoft.kapp.activities.settings;

import com.microsoft.kapp.activities.BaseActivity;
import com.microsoft.kapp.services.SettingsProvider;
import com.microsoft.kapp.strappsettings.StrappSettingsManager;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;
/* loaded from: classes.dex */
public final class SelectMetricsActivity$$InjectAdapter extends Binding<SelectMetricsActivity> implements MembersInjector<SelectMetricsActivity> {
    private Binding<SettingsProvider> mSettingsProvider;
    private Binding<StrappSettingsManager> mStrappSettingsManager;
    private Binding<BaseActivity> supertype;

    public SelectMetricsActivity$$InjectAdapter() {
        super(null, "members/com.microsoft.kapp.activities.settings.SelectMetricsActivity", false, SelectMetricsActivity.class);
    }

    @Override // dagger.internal.Binding
    public void attach(Linker linker) {
        this.mStrappSettingsManager = linker.requestBinding("com.microsoft.kapp.strappsettings.StrappSettingsManager", SelectMetricsActivity.class, getClass().getClassLoader());
        this.mSettingsProvider = linker.requestBinding("com.microsoft.kapp.services.SettingsProvider", SelectMetricsActivity.class, getClass().getClassLoader());
        this.supertype = linker.requestBinding("members/com.microsoft.kapp.activities.BaseActivity", SelectMetricsActivity.class, getClass().getClassLoader(), false, true);
    }

    @Override // dagger.internal.Binding
    public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
        injectMembersBindings.add(this.mStrappSettingsManager);
        injectMembersBindings.add(this.mSettingsProvider);
        injectMembersBindings.add(this.supertype);
    }

    @Override // dagger.internal.Binding, dagger.MembersInjector
    public void injectMembers(SelectMetricsActivity object) {
        object.mStrappSettingsManager = this.mStrappSettingsManager.get();
        object.mSettingsProvider = this.mSettingsProvider.get();
        this.supertype.injectMembers(object);
    }
}

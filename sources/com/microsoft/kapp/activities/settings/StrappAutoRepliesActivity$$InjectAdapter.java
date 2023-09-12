package com.microsoft.kapp.activities.settings;

import com.microsoft.kapp.activities.BaseActivity;
import com.microsoft.kapp.strappsettings.StrappSettingsManager;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class StrappAutoRepliesActivity$$InjectAdapter extends Binding<StrappAutoRepliesActivity> implements Provider<StrappAutoRepliesActivity>, MembersInjector<StrappAutoRepliesActivity> {
    private Binding<StrappSettingsManager> mStrappSettingsManager;
    private Binding<BaseActivity> supertype;

    public StrappAutoRepliesActivity$$InjectAdapter() {
        super("com.microsoft.kapp.activities.settings.StrappAutoRepliesActivity", "members/com.microsoft.kapp.activities.settings.StrappAutoRepliesActivity", false, StrappAutoRepliesActivity.class);
    }

    @Override // dagger.internal.Binding
    public void attach(Linker linker) {
        this.mStrappSettingsManager = linker.requestBinding("com.microsoft.kapp.strappsettings.StrappSettingsManager", StrappAutoRepliesActivity.class, getClass().getClassLoader());
        this.supertype = linker.requestBinding("members/com.microsoft.kapp.activities.BaseActivity", StrappAutoRepliesActivity.class, getClass().getClassLoader(), false, true);
    }

    @Override // dagger.internal.Binding
    public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
        injectMembersBindings.add(this.mStrappSettingsManager);
        injectMembersBindings.add(this.supertype);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // dagger.internal.Binding, javax.inject.Provider
    public StrappAutoRepliesActivity get() {
        StrappAutoRepliesActivity result = new StrappAutoRepliesActivity();
        injectMembers(result);
        return result;
    }

    @Override // dagger.internal.Binding, dagger.MembersInjector
    public void injectMembers(StrappAutoRepliesActivity object) {
        object.mStrappSettingsManager = this.mStrappSettingsManager.get();
        this.supertype.injectMembers(object);
    }
}

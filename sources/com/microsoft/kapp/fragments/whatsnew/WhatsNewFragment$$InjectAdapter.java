package com.microsoft.kapp.fragments.whatsnew;

import com.microsoft.kapp.fragments.BaseFragment;
import com.microsoft.kapp.services.SettingsProvider;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class WhatsNewFragment$$InjectAdapter extends Binding<WhatsNewFragment> implements Provider<WhatsNewFragment>, MembersInjector<WhatsNewFragment> {
    private Binding<SettingsProvider> mSettingsProvider;
    private Binding<BaseFragment> supertype;

    public WhatsNewFragment$$InjectAdapter() {
        super("com.microsoft.kapp.fragments.whatsnew.WhatsNewFragment", "members/com.microsoft.kapp.fragments.whatsnew.WhatsNewFragment", false, WhatsNewFragment.class);
    }

    @Override // dagger.internal.Binding
    public void attach(Linker linker) {
        this.mSettingsProvider = linker.requestBinding("com.microsoft.kapp.services.SettingsProvider", WhatsNewFragment.class, getClass().getClassLoader());
        this.supertype = linker.requestBinding("members/com.microsoft.kapp.fragments.BaseFragment", WhatsNewFragment.class, getClass().getClassLoader(), false, true);
    }

    @Override // dagger.internal.Binding
    public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
        injectMembersBindings.add(this.mSettingsProvider);
        injectMembersBindings.add(this.supertype);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // dagger.internal.Binding, javax.inject.Provider
    public WhatsNewFragment get() {
        WhatsNewFragment result = new WhatsNewFragment();
        injectMembers(result);
        return result;
    }

    @Override // dagger.internal.Binding, dagger.MembersInjector
    public void injectMembers(WhatsNewFragment object) {
        object.mSettingsProvider = this.mSettingsProvider.get();
        this.supertype.injectMembers(object);
    }
}

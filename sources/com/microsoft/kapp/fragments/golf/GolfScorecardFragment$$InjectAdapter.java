package com.microsoft.kapp.fragments.golf;

import com.microsoft.kapp.fragments.BaseHomeTileFragment;
import com.microsoft.kapp.providers.golf.ScorecardProvider;
import com.microsoft.kapp.services.SettingsProvider;
import com.microsoft.kapp.services.TMAG.TMAGService;
import com.microsoft.kapp.services.golf.GolfService;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class GolfScorecardFragment$$InjectAdapter extends Binding<GolfScorecardFragment> implements Provider<GolfScorecardFragment>, MembersInjector<GolfScorecardFragment> {
    private Binding<GolfService> mGolfService;
    private Binding<ScorecardProvider> mScorecardProvider;
    private Binding<SettingsProvider> mSettingsProvider;
    private Binding<TMAGService> mTMAGService;
    private Binding<BaseHomeTileFragment> supertype;

    public GolfScorecardFragment$$InjectAdapter() {
        super("com.microsoft.kapp.fragments.golf.GolfScorecardFragment", "members/com.microsoft.kapp.fragments.golf.GolfScorecardFragment", false, GolfScorecardFragment.class);
    }

    @Override // dagger.internal.Binding
    public void attach(Linker linker) {
        this.mScorecardProvider = linker.requestBinding("com.microsoft.kapp.providers.golf.ScorecardProvider", GolfScorecardFragment.class, getClass().getClassLoader());
        this.mTMAGService = linker.requestBinding("com.microsoft.kapp.services.TMAG.TMAGService", GolfScorecardFragment.class, getClass().getClassLoader());
        this.mSettingsProvider = linker.requestBinding("com.microsoft.kapp.services.SettingsProvider", GolfScorecardFragment.class, getClass().getClassLoader());
        this.mGolfService = linker.requestBinding("com.microsoft.kapp.services.golf.GolfService", GolfScorecardFragment.class, getClass().getClassLoader());
        this.supertype = linker.requestBinding("members/com.microsoft.kapp.fragments.BaseHomeTileFragment", GolfScorecardFragment.class, getClass().getClassLoader(), false, true);
    }

    @Override // dagger.internal.Binding
    public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
        injectMembersBindings.add(this.mScorecardProvider);
        injectMembersBindings.add(this.mTMAGService);
        injectMembersBindings.add(this.mSettingsProvider);
        injectMembersBindings.add(this.mGolfService);
        injectMembersBindings.add(this.supertype);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // dagger.internal.Binding, javax.inject.Provider
    public GolfScorecardFragment get() {
        GolfScorecardFragment result = new GolfScorecardFragment();
        injectMembers(result);
        return result;
    }

    @Override // dagger.internal.Binding, dagger.MembersInjector
    public void injectMembers(GolfScorecardFragment object) {
        object.mScorecardProvider = this.mScorecardProvider.get();
        object.mTMAGService = this.mTMAGService.get();
        object.mSettingsProvider = this.mSettingsProvider.get();
        object.mGolfService = this.mGolfService.get();
        this.supertype.injectMembers(object);
    }
}

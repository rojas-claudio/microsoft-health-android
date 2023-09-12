package com.microsoft.kapp.widgets;

import com.microsoft.kapp.ThemeManager;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;
/* loaded from: classes.dex */
public final class BaseTile$$InjectAdapter extends Binding<BaseTile> implements MembersInjector<BaseTile> {
    private Binding<ThemeManager> mThemeManager;

    public BaseTile$$InjectAdapter() {
        super(null, "members/com.microsoft.kapp.widgets.BaseTile", false, BaseTile.class);
    }

    @Override // dagger.internal.Binding
    public void attach(Linker linker) {
        this.mThemeManager = linker.requestBinding("com.microsoft.kapp.ThemeManager", BaseTile.class, getClass().getClassLoader());
    }

    @Override // dagger.internal.Binding
    public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
        injectMembersBindings.add(this.mThemeManager);
    }

    @Override // dagger.internal.Binding, dagger.MembersInjector
    public void injectMembers(BaseTile object) {
        object.mThemeManager = this.mThemeManager.get();
    }
}

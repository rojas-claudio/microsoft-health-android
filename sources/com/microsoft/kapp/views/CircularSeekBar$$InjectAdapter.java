package com.microsoft.kapp.views;

import com.microsoft.kapp.FontManager;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;
/* loaded from: classes.dex */
public final class CircularSeekBar$$InjectAdapter extends Binding<CircularSeekBar> implements MembersInjector<CircularSeekBar> {
    private Binding<FontManager> mFontManager;

    public CircularSeekBar$$InjectAdapter() {
        super(null, "members/com.microsoft.kapp.views.CircularSeekBar", false, CircularSeekBar.class);
    }

    @Override // dagger.internal.Binding
    public void attach(Linker linker) {
        this.mFontManager = linker.requestBinding("com.microsoft.kapp.FontManager", CircularSeekBar.class, getClass().getClassLoader());
    }

    @Override // dagger.internal.Binding
    public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
        injectMembersBindings.add(this.mFontManager);
    }

    @Override // dagger.internal.Binding, dagger.MembersInjector
    public void injectMembers(CircularSeekBar object) {
        object.mFontManager = this.mFontManager.get();
    }
}

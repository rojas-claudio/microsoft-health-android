package com.microsoft.kapp.widgets;

import com.microsoft.kapp.FontManager;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;
/* loaded from: classes.dex */
public final class PagerTitleStrip$$InjectAdapter extends Binding<PagerTitleStrip> implements MembersInjector<PagerTitleStrip> {
    private Binding<FontManager> mFontManager;

    public PagerTitleStrip$$InjectAdapter() {
        super(null, "members/com.microsoft.kapp.widgets.PagerTitleStrip", false, PagerTitleStrip.class);
    }

    @Override // dagger.internal.Binding
    public void attach(Linker linker) {
        this.mFontManager = linker.requestBinding("com.microsoft.kapp.FontManager", PagerTitleStrip.class, getClass().getClassLoader());
    }

    @Override // dagger.internal.Binding
    public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
        injectMembersBindings.add(this.mFontManager);
    }

    @Override // dagger.internal.Binding, dagger.MembersInjector
    public void injectMembers(PagerTitleStrip object) {
        object.mFontManager = this.mFontManager.get();
    }
}

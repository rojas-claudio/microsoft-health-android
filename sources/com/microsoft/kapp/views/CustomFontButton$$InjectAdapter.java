package com.microsoft.kapp.views;

import com.microsoft.kapp.FontManager;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;
/* loaded from: classes.dex */
public final class CustomFontButton$$InjectAdapter extends Binding<CustomFontButton> implements MembersInjector<CustomFontButton> {
    private Binding<FontManager> mFontManager;

    public CustomFontButton$$InjectAdapter() {
        super(null, "members/com.microsoft.kapp.views.CustomFontButton", false, CustomFontButton.class);
    }

    @Override // dagger.internal.Binding
    public void attach(Linker linker) {
        this.mFontManager = linker.requestBinding("com.microsoft.kapp.FontManager", CustomFontButton.class, getClass().getClassLoader());
    }

    @Override // dagger.internal.Binding
    public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
        injectMembersBindings.add(this.mFontManager);
    }

    @Override // dagger.internal.Binding, dagger.MembersInjector
    public void injectMembers(CustomFontButton object) {
        object.mFontManager = this.mFontManager.get();
    }
}

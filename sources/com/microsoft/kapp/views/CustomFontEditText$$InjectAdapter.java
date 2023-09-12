package com.microsoft.kapp.views;

import com.microsoft.kapp.FontManager;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;
/* loaded from: classes.dex */
public final class CustomFontEditText$$InjectAdapter extends Binding<CustomFontEditText> implements MembersInjector<CustomFontEditText> {
    private Binding<FontManager> mFontManager;

    public CustomFontEditText$$InjectAdapter() {
        super(null, "members/com.microsoft.kapp.views.CustomFontEditText", false, CustomFontEditText.class);
    }

    @Override // dagger.internal.Binding
    public void attach(Linker linker) {
        this.mFontManager = linker.requestBinding("com.microsoft.kapp.FontManager", CustomFontEditText.class, getClass().getClassLoader());
    }

    @Override // dagger.internal.Binding
    public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
        injectMembersBindings.add(this.mFontManager);
    }

    @Override // dagger.internal.Binding, dagger.MembersInjector
    public void injectMembers(CustomFontEditText object) {
        object.mFontManager = this.mFontManager.get();
    }
}

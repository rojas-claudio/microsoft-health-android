package com.microsoft.kapp.views;

import com.microsoft.kapp.FontManager;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;
/* loaded from: classes.dex */
public final class CustomFontTextView$$InjectAdapter extends Binding<CustomFontTextView> implements MembersInjector<CustomFontTextView> {
    private Binding<FontManager> mFontManager;

    public CustomFontTextView$$InjectAdapter() {
        super(null, "members/com.microsoft.kapp.views.CustomFontTextView", false, CustomFontTextView.class);
    }

    @Override // dagger.internal.Binding
    public void attach(Linker linker) {
        this.mFontManager = linker.requestBinding("com.microsoft.kapp.FontManager", CustomFontTextView.class, getClass().getClassLoader());
    }

    @Override // dagger.internal.Binding
    public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
        injectMembersBindings.add(this.mFontManager);
    }

    @Override // dagger.internal.Binding, dagger.MembersInjector
    public void injectMembers(CustomFontTextView object) {
        object.mFontManager = this.mFontManager.get();
    }
}

package com.microsoft.kapp.style.text;

import com.microsoft.kapp.FontManager;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class CustomStyleToSpan$$InjectAdapter extends Binding<CustomStyleToSpan> implements Provider<CustomStyleToSpan>, MembersInjector<CustomStyleToSpan> {
    private Binding<FontManager> mFontManager;
    private Binding<StyleToSpan> supertype;

    public CustomStyleToSpan$$InjectAdapter() {
        super("com.microsoft.kapp.style.text.CustomStyleToSpan", "members/com.microsoft.kapp.style.text.CustomStyleToSpan", false, CustomStyleToSpan.class);
    }

    @Override // dagger.internal.Binding
    public void attach(Linker linker) {
        this.mFontManager = linker.requestBinding("com.microsoft.kapp.FontManager", CustomStyleToSpan.class, getClass().getClassLoader());
        this.supertype = linker.requestBinding("members/com.microsoft.kapp.style.text.StyleToSpan", CustomStyleToSpan.class, getClass().getClassLoader(), false, true);
    }

    @Override // dagger.internal.Binding
    public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
        injectMembersBindings.add(this.mFontManager);
        injectMembersBindings.add(this.supertype);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // dagger.internal.Binding, javax.inject.Provider
    public CustomStyleToSpan get() {
        CustomStyleToSpan result = new CustomStyleToSpan();
        injectMembers(result);
        return result;
    }

    @Override // dagger.internal.Binding, dagger.MembersInjector
    public void injectMembers(CustomStyleToSpan object) {
        object.mFontManager = this.mFontManager.get();
        this.supertype.injectMembers(object);
    }
}

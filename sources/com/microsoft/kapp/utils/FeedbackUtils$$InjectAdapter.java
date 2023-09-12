package com.microsoft.kapp.utils;

import dagger.internal.Binding;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class FeedbackUtils$$InjectAdapter extends Binding<FeedbackUtils> implements Provider<FeedbackUtils> {
    public FeedbackUtils$$InjectAdapter() {
        super("com.microsoft.kapp.utils.FeedbackUtils", "members/com.microsoft.kapp.utils.FeedbackUtils", false, FeedbackUtils.class);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // dagger.internal.Binding, javax.inject.Provider
    public FeedbackUtils get() {
        FeedbackUtils result = new FeedbackUtils();
        return result;
    }
}

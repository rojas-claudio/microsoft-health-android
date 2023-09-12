package com.microsoft.kapp.utils;

import com.microsoft.kapp.feedback.FeedbackService;
import dagger.internal.Binding;
import dagger.internal.Linker;
import dagger.internal.StaticInjection;
/* loaded from: classes.dex */
public final class FeedbackUtils$$StaticInjection extends StaticInjection {
    private Binding<FeedbackService> mFeedbackService;

    @Override // dagger.internal.StaticInjection
    public void attach(Linker linker) {
        this.mFeedbackService = linker.requestBinding("com.microsoft.kapp.feedback.FeedbackService", FeedbackUtils.class, getClass().getClassLoader());
    }

    @Override // dagger.internal.StaticInjection
    public void inject() {
        FeedbackUtils.mFeedbackService = this.mFeedbackService.get();
    }
}

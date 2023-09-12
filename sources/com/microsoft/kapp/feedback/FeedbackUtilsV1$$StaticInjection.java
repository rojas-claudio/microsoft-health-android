package com.microsoft.kapp.feedback;

import dagger.internal.Binding;
import dagger.internal.Linker;
import dagger.internal.StaticInjection;
/* loaded from: classes.dex */
public final class FeedbackUtilsV1$$StaticInjection extends StaticInjection {
    private Binding<FeedbackService> mFeedbackService;

    @Override // dagger.internal.StaticInjection
    public void attach(Linker linker) {
        this.mFeedbackService = linker.requestBinding("com.microsoft.kapp.feedback.FeedbackService", FeedbackUtilsV1.class, getClass().getClassLoader());
    }

    @Override // dagger.internal.StaticInjection
    public void inject() {
        FeedbackUtilsV1.mFeedbackService = this.mFeedbackService.get();
    }
}

package com.microsoft.kapp.feedback;

import dagger.internal.Binding;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class FeedbackUtilsV1$$InjectAdapter extends Binding<FeedbackUtilsV1> implements Provider<FeedbackUtilsV1> {
    public FeedbackUtilsV1$$InjectAdapter() {
        super("com.microsoft.kapp.feedback.FeedbackUtilsV1", "members/com.microsoft.kapp.feedback.FeedbackUtilsV1", false, FeedbackUtilsV1.class);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // dagger.internal.Binding, javax.inject.Provider
    public FeedbackUtilsV1 get() {
        FeedbackUtilsV1 result = new FeedbackUtilsV1();
        return result;
    }
}

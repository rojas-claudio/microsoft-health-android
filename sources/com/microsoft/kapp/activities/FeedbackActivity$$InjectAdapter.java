package com.microsoft.kapp.activities;

import com.microsoft.kapp.feedback.FeedbackService;
import com.microsoft.kapp.logging.http.FiddlerLogger;
import com.microsoft.krestsdk.auth.credentials.CredentialsManager;
import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class FeedbackActivity$$InjectAdapter extends Binding<FeedbackActivity> implements Provider<FeedbackActivity>, MembersInjector<FeedbackActivity> {
    private Binding<CredentialsManager> mCredentialsManager;
    private Binding<FeedbackService> mFeedbackService;
    private Binding<FiddlerLogger> mFiddlerLogger;
    private Binding<BaseFragmentActivity> supertype;

    public FeedbackActivity$$InjectAdapter() {
        super("com.microsoft.kapp.activities.FeedbackActivity", "members/com.microsoft.kapp.activities.FeedbackActivity", false, FeedbackActivity.class);
    }

    @Override // dagger.internal.Binding
    public void attach(Linker linker) {
        this.mCredentialsManager = linker.requestBinding("com.microsoft.krestsdk.auth.credentials.CredentialsManager", FeedbackActivity.class, getClass().getClassLoader());
        this.mFiddlerLogger = linker.requestBinding("com.microsoft.kapp.logging.http.FiddlerLogger", FeedbackActivity.class, getClass().getClassLoader());
        this.mFeedbackService = linker.requestBinding("com.microsoft.kapp.feedback.FeedbackService", FeedbackActivity.class, getClass().getClassLoader());
        this.supertype = linker.requestBinding("members/com.microsoft.kapp.activities.BaseFragmentActivity", FeedbackActivity.class, getClass().getClassLoader(), false, true);
    }

    @Override // dagger.internal.Binding
    public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
        injectMembersBindings.add(this.mCredentialsManager);
        injectMembersBindings.add(this.mFiddlerLogger);
        injectMembersBindings.add(this.mFeedbackService);
        injectMembersBindings.add(this.supertype);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // dagger.internal.Binding, javax.inject.Provider
    public FeedbackActivity get() {
        FeedbackActivity result = new FeedbackActivity();
        injectMembers(result);
        return result;
    }

    @Override // dagger.internal.Binding, dagger.MembersInjector
    public void injectMembers(FeedbackActivity object) {
        object.mCredentialsManager = this.mCredentialsManager.get();
        object.mFiddlerLogger = this.mFiddlerLogger.get();
        object.mFeedbackService = this.mFeedbackService.get();
        this.supertype.injectMembers(object);
    }
}

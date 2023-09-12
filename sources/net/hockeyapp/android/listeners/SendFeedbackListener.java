package net.hockeyapp.android.listeners;

import net.hockeyapp.android.StringListener;
import net.hockeyapp.android.tasks.SendFeedbackTask;
/* loaded from: classes.dex */
public abstract class SendFeedbackListener extends StringListener {
    public void feedbackSuccessful(SendFeedbackTask task) {
    }

    public void feedbackFailed(SendFeedbackTask task, Boolean userWantsRetry) {
    }
}

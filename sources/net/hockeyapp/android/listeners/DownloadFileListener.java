package net.hockeyapp.android.listeners;

import net.hockeyapp.android.StringListener;
import net.hockeyapp.android.tasks.DownloadFileTask;
/* loaded from: classes.dex */
public abstract class DownloadFileListener extends StringListener {
    public void downloadFailed(DownloadFileTask task, Boolean userWantsRetry) {
    }

    public void downloadSuccessful(DownloadFileTask task) {
    }
}

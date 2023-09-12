package net.hockeyapp.android.tasks;

import android.content.Context;
import java.net.URL;
import java.net.URLConnection;
import net.hockeyapp.android.listeners.DownloadFileListener;
/* loaded from: classes.dex */
public class GetFileSizeTask extends DownloadFileTask {
    private long size;

    public GetFileSizeTask(Context context, String urlString, DownloadFileListener notifier) {
        super(context, urlString, notifier);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // net.hockeyapp.android.tasks.DownloadFileTask, android.os.AsyncTask
    public Long doInBackground(Void... args) {
        try {
            URL url = new URL(getURLString());
            URLConnection connection = createConnection(url, 6);
            return Long.valueOf(connection.getContentLength());
        } catch (Exception e) {
            e.printStackTrace();
            return 0L;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // net.hockeyapp.android.tasks.DownloadFileTask, android.os.AsyncTask
    public void onProgressUpdate(Integer... args) {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // net.hockeyapp.android.tasks.DownloadFileTask, android.os.AsyncTask
    public void onPostExecute(Long result) {
        this.size = result.longValue();
        if (this.size > 0) {
            this.notifier.downloadSuccessful(this);
        } else {
            this.notifier.downloadFailed(this, false);
        }
    }

    public long getSize() {
        return this.size;
    }
}

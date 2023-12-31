package net.hockeyapp.android;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.google.android.gms.appstate.AppStateClient;
import java.io.File;
import java.io.FileOutputStream;
import net.hockeyapp.android.tasks.ParseFeedbackTask;
import net.hockeyapp.android.tasks.SendFeedbackTask;
import net.hockeyapp.android.utils.AsyncTaskUtils;
import net.hockeyapp.android.utils.PrefsUtil;
import net.hockeyapp.android.utils.Util;
/* loaded from: classes.dex */
public class FeedbackManager {
    private static final String BROADCAST_ACTION = "net.hockeyapp.android.SCREENSHOT";
    private static final int BROADCAST_REQUEST_CODE = 1;
    private static final int SCREENSHOT_NOTIFICATION_ID = 1;
    private static Activity currentActivity;
    private static BroadcastReceiver receiver = null;
    private static boolean notificationActive = false;
    private static String identifier = null;
    private static String urlString = null;
    private static FeedbackManagerListener lastListener = null;

    public static void register(Context context, String appIdentifier) {
        register(context, appIdentifier, null);
    }

    public static void register(Context context, String appIdentifier, FeedbackManagerListener listener) {
        register(context, Constants.BASE_URL, appIdentifier, listener);
    }

    public static void register(Context context, String urlString2, String appIdentifier, FeedbackManagerListener listener) {
        if (context != null) {
            identifier = Util.sanitizeAppIdentifier(appIdentifier);
            urlString = urlString2;
            lastListener = listener;
            Constants.loadFromContext(context);
        }
    }

    public static void unregister() {
        lastListener = null;
    }

    public static void showFeedbackActivity(Context context) {
        if (context != null) {
            Class<?> activityClass = null;
            if (lastListener != null) {
                activityClass = lastListener.getFeedbackActivityClass();
            }
            if (activityClass == null) {
                activityClass = FeedbackActivity.class;
            }
            Intent intent = new Intent();
            intent.setFlags(268435456);
            intent.setClass(context, activityClass);
            intent.putExtra("url", getURLString(context));
            context.startActivity(intent);
        }
    }

    public static void checkForAnswersAndNotify(final Context context) {
        String token = PrefsUtil.getInstance().getFeedbackTokenFromPrefs(context);
        if (token != null) {
            int lastMessageId = context.getSharedPreferences(ParseFeedbackTask.PREFERENCES_NAME, 0).getInt(ParseFeedbackTask.ID_LAST_MESSAGE_SEND, -1);
            SendFeedbackTask sendFeedbackTask = new SendFeedbackTask(context, getURLString(context), null, null, null, null, null, token, new Handler() { // from class: net.hockeyapp.android.FeedbackManager.1
                @Override // android.os.Handler
                public void handleMessage(Message msg) {
                    Bundle bundle = msg.getData();
                    String responseString = bundle.getString("feedback_response");
                    if (responseString != null) {
                        ParseFeedbackTask task = new ParseFeedbackTask(context, responseString, null, "fetch");
                        task.setUrlString(FeedbackManager.getURLString(context));
                        AsyncTaskUtils.execute(task);
                    }
                }
            }, true);
            sendFeedbackTask.setShowProgressDialog(false);
            sendFeedbackTask.setLastMessageId(lastMessageId);
            AsyncTaskUtils.execute(sendFeedbackTask);
        }
    }

    public static FeedbackManagerListener getLastListener() {
        return lastListener;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String getURLString(Context context) {
        return urlString + "api/2/apps/" + identifier + "/feedback/";
    }

    public static void setActivityForScreenshot(Activity activity) {
        currentActivity = activity;
        if (!notificationActive) {
            startNotification();
        }
    }

    public static void unsetCurrentActivityForScreenshot(Activity activity) {
        if (currentActivity != null && currentActivity == activity) {
            endNotification();
            currentActivity = null;
        }
    }

    /* JADX WARN: Type inference failed for: r8v8, types: [net.hockeyapp.android.FeedbackManager$2] */
    public static void takeScreenshot(final Context context) {
        View view = currentActivity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        final Bitmap bitmap = view.getDrawingCache();
        String filename = currentActivity.getLocalClassName();
        File dir = Constants.getHockeyAppStorageDir();
        File result = new File(dir, filename + ".jpg");
        int suffix = 1;
        while (result.exists()) {
            result = new File(dir, filename + "_" + suffix + ".jpg");
            suffix++;
        }
        new AsyncTask<File, Void, Boolean>() { // from class: net.hockeyapp.android.FeedbackManager.2
            /* JADX INFO: Access modifiers changed from: protected */
            @Override // android.os.AsyncTask
            public Boolean doInBackground(File... args) {
                try {
                    FileOutputStream out = new FileOutputStream(args[0]);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                    out.close();
                    return true;
                } catch (Exception e) {
                    Log.e("HockeyApp", "Could not save screenshot.", e);
                    return false;
                }
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // android.os.AsyncTask
            public void onPostExecute(Boolean success) {
                if (!success.booleanValue()) {
                    Toast.makeText(context, "Screenshot could not be created. Sorry.", (int) AppStateClient.STATUS_WRITE_OUT_OF_DATE_VERSION).show();
                }
            }
        }.execute(result);
        MediaScannerClient client = new MediaScannerClient(result.getAbsolutePath());
        MediaScannerConnection connection = new MediaScannerConnection(currentActivity, client);
        client.setConnection(connection);
        connection.connect();
        Toast.makeText(context, "Screenshot '" + result.getName() + "' is available in gallery.", (int) AppStateClient.STATUS_WRITE_OUT_OF_DATE_VERSION).show();
    }

    private static void startNotification() {
        notificationActive = true;
        NotificationManager notificationManager = (NotificationManager) currentActivity.getSystemService("notification");
        int iconId = currentActivity.getResources().getIdentifier("ic_menu_camera", "drawable", "android");
        Notification notification = new Notification(iconId, "", System.currentTimeMillis());
        Intent intent = new Intent();
        intent.setAction(BROADCAST_ACTION);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(currentActivity, 1, intent, 1073741824);
        notification.setLatestEventInfo(currentActivity, "HockeyApp Feedback", "Take a screenshot for your feedback.", pendingIntent);
        notificationManager.notify(1, notification);
        if (receiver == null) {
            receiver = new BroadcastReceiver() { // from class: net.hockeyapp.android.FeedbackManager.3
                @Override // android.content.BroadcastReceiver
                public void onReceive(Context context, Intent intent2) {
                    FeedbackManager.takeScreenshot(context);
                }
            };
        }
        currentActivity.registerReceiver(receiver, new IntentFilter(BROADCAST_ACTION));
    }

    private static void endNotification() {
        notificationActive = false;
        currentActivity.unregisterReceiver(receiver);
        NotificationManager notificationManager = (NotificationManager) currentActivity.getSystemService("notification");
        notificationManager.cancel(1);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class MediaScannerClient implements MediaScannerConnection.MediaScannerConnectionClient {
        private MediaScannerConnection connection;
        private String path;

        private MediaScannerClient(String path) {
            this.connection = null;
            this.path = path;
        }

        public void setConnection(MediaScannerConnection connection) {
            this.connection = connection;
        }

        @Override // android.media.MediaScannerConnection.MediaScannerConnectionClient
        public void onMediaScannerConnected() {
            if (this.connection != null) {
                this.connection.scanFile(this.path, null);
            }
        }

        @Override // android.media.MediaScannerConnection.OnScanCompletedListener
        public void onScanCompleted(String path, Uri uri) {
            Log.i("HockeyApp", String.format("Scanned path %s -> URI = %s", path, uri.toString()));
            this.connection.disconnect();
        }
    }
}

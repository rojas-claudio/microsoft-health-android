package com.microsoft.kapp.feedback;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import com.microsoft.kapp.Callback;
import com.microsoft.kapp.CargoConnection;
import com.microsoft.kapp.R;
import com.microsoft.kapp.diagnostics.Validate;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.kapp.logging.models.FeedbackDescription;
import com.microsoft.kapp.utils.Constants;
import com.microsoft.kapp.utils.FileUtils;
import com.microsoft.kapp.utils.ToastUtils;
import com.microsoft.kapp.utils.ViewUtils;
import com.microsoft.kapp.version.Version;
import com.shinobicontrols.charts.ChartView;
import com.shinobicontrols.charts.ShinobiChart;
import com.shinobicontrols.kcompanionapp.charts.internal.ChartThemeCache;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
/* loaded from: classes.dex */
public class FeedbackUtilsV1 {
    public static final String FEEDBACK_INTENT = "Kapp.feedback";
    private static final int FEEDBACK_NOTIFICATION_ID = 2452;
    private static final String TAG = FeedbackUtilsV1.class.getSimpleName();
    private static Bitmap chartScreenshot;
    @Inject
    static FeedbackService mFeedbackService;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class DeviceInfo {
        public String fwVersion;
        public Bitmap screenshot;

        private DeviceInfo() {
        }
    }

    public static void sendFeedbackAsync(View view, Activity activity, CargoConnection cargoConnection) {
        sendFeedbackAsync(view, activity, cargoConnection, null);
    }

    public static void sendFeedbackAsync(final View view, final Activity activity, final CargoConnection cargoConnection, final String emailText) {
        AsyncTask<Void, Void, DeviceInfo> task = new AsyncTask<Void, Void, DeviceInfo>() { // from class: com.microsoft.kapp.feedback.FeedbackUtilsV1.1
            @Override // android.os.AsyncTask
            protected void onPreExecute() {
                ToastUtils.showLongToast(activity, (int) R.string.debug_message_capturing_diagnostic_info);
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // android.os.AsyncTask
            public DeviceInfo doInBackground(Void... params) {
                DeviceInfo info = new DeviceInfo();
                try {
                    Version version = cargoConnection.getDeviceFirmwareVersion();
                    if (version != null) {
                        info.fwVersion = version.toShortString();
                    }
                } catch (Exception ex) {
                    KLog.i(FeedbackUtilsV1.TAG, "getDeviceFirmwareVersion failed", ex);
                }
                try {
                    Bitmap devicescreenshot = cargoConnection.captureDeviceScreenshot();
                    KLog.image(FeedbackUtilsV1.TAG, devicescreenshot, "device screenshot");
                } catch (Exception ex2) {
                    KLog.e(FeedbackUtilsV1.TAG, "captureDeviceScreenshot failed", ex2);
                }
                try {
                    if (view != null) {
                        Bitmap image = FeedbackUtilsV1.captureScreenshot(view);
                        if (image != null) {
                            KLog.image(FeedbackUtilsV1.TAG, image, "screenshot");
                        }
                        Bitmap chartBitmap = FeedbackUtilsV1.captureChartScreenshot(activity, view);
                        if (chartBitmap != null) {
                            KLog.image(FeedbackUtilsV1.TAG, chartBitmap, "chart screenshot");
                        }
                    }
                } catch (Exception ex3) {
                    KLog.e(FeedbackUtilsV1.TAG, "capture Screenshot failed", ex3);
                }
                return info;
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // android.os.AsyncTask
            public final void onPostExecute(DeviceInfo info) {
                FeedbackUtilsV1.mFeedbackService.sendFeedbackAsync(activity, "sendFeedbackAsync", new FeedbackDescription(""), emailText, null, null, true, null);
            }
        };
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
    }

    public static ArrayList<Uri> captureScreenshots(Activity activity) {
        ArrayList<Uri> results = new ArrayList<>();
        try {
            View view = activity.getWindow().getDecorView();
            if (view != null) {
                Bitmap bitmap = captureScreenshot(view);
                if (bitmap != null) {
                    results.add(getScreenshotAttachmentUri(bitmap, activity));
                }
                Bitmap chartBitmap = captureChartScreenshot(activity, view);
                if (chartBitmap != null) {
                    results.add(getScreenshotAttachmentUri(chartBitmap, activity));
                }
            }
        } catch (Exception e) {
            KLog.e(TAG, "Trouble capturing screenshots", e);
        }
        return results;
    }

    private static Uri getScreenshotAttachmentUri(Bitmap bitmap, Context context) throws FileNotFoundException, IOException {
        return getAttachmentUriFromBitmap(bitmap, UUID.randomUUID() + ".png", context);
    }

    private static Uri getAttachmentUriFromBitmap(Bitmap bitmap, String filename, Context context) throws FileNotFoundException, IOException {
        File imageFile = FileUtils.saveBitmapToLocalStorage(bitmap, filename, context);
        return Uri.fromFile(imageFile);
    }

    public static void captureScreenshotsAsync(final Activity activity, final Callback<ArrayList<String>> callback) {
        AsyncTask<Void, Void, ArrayList<String>> task = new AsyncTask<Void, Void, ArrayList<String>>() { // from class: com.microsoft.kapp.feedback.FeedbackUtilsV1.2
            /* JADX INFO: Access modifiers changed from: protected */
            @Override // android.os.AsyncTask
            public ArrayList<String> doInBackground(Void... params) {
                ArrayList<Uri> screenshots = FeedbackUtilsV1.captureScreenshots(activity);
                ArrayList<String> mScreenshotStrings = new ArrayList<>();
                Iterator i$ = screenshots.iterator();
                while (i$.hasNext()) {
                    Uri uri = i$.next();
                    mScreenshotStrings.add(uri.toString());
                }
                return mScreenshotStrings;
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // android.os.AsyncTask
            public final void onPostExecute(ArrayList<String> result) {
                callback.callback(result);
            }
        };
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static Bitmap captureScreenshot(View view) {
        boolean wasDrawingCacheEnabled = view.isDrawingCacheEnabled();
        Bitmap result = null;
        try {
            view.setDrawingCacheEnabled(true);
            result = Bitmap.createBitmap(view.getDrawingCache());
        } catch (Error ex) {
            KLog.e(TAG, "unable to capture screenshot", ex);
        } catch (Exception ex2) {
            KLog.w(TAG, "unable to capture screenshot", ex2);
        } finally {
            view.setDrawingCacheEnabled(wasDrawingCacheEnabled);
        }
        return result;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static Bitmap captureChartScreenshot(final Activity activity, View view) {
        ChartView chartView;
        final CountDownLatch done = new CountDownLatch(1);
        boolean captureChartScreenshotComplete = false;
        Bitmap result = null;
        if (activity == null || view == null || !(view instanceof ViewGroup)) {
            return null;
        }
        ViewPager pager = (ViewPager) ViewUtils.findViewByType(view, ViewPager.class);
        if (pager != null) {
            View page = pager.getChildAt(pager.getCurrentItem());
            chartView = (ChartView) ViewUtils.findViewByType(page, ChartView.class);
        } else {
            chartView = (ChartView) ViewUtils.findViewByType(view, ChartView.class);
        }
        if (chartView != null) {
            final ShinobiChart chart = chartView.getShinobiChart();
            chart.setOnSnapshotDoneListener(new ShinobiChart.OnSnapshotDoneListener() { // from class: com.microsoft.kapp.feedback.FeedbackUtilsV1.3
                @Override // com.shinobicontrols.charts.ShinobiChart.OnSnapshotDoneListener
                public void onSnapshotDone(Bitmap bitmap) {
                    Bitmap unused = FeedbackUtilsV1.chartScreenshot = bitmap;
                    ShinobiChart.this.getStyle().setCanvasBackgroundColor(0);
                    ShinobiChart.this.getStyle().setPlotAreaBackgroundColor(0);
                    ShinobiChart.this.redrawChart();
                    done.countDown();
                }
            });
            activity.runOnUiThread(new Runnable() { // from class: com.microsoft.kapp.feedback.FeedbackUtilsV1.4
                @Override // java.lang.Runnable
                public void run() {
                    ChartThemeCache chartThemeCache = new ChartThemeCache(activity.getResources());
                    int chartBackgroundColor = chartThemeCache.getChartBackgroundColor();
                    chart.getStyle().setCanvasBackgroundColor(chartBackgroundColor);
                    chart.getStyle().setPlotAreaBackgroundColor(chartBackgroundColor);
                    chart.requestSnapshot();
                }
            });
            try {
                captureChartScreenshotComplete = done.await(10L, TimeUnit.SECONDS);
            } catch (InterruptedException ex) {
                chartScreenshot = null;
                KLog.w(TAG, "unable to capture chart screenshot", ex);
            }
            if (captureChartScreenshotComplete) {
                result = chartScreenshot;
            }
        }
        chartScreenshot = null;
        return result;
    }

    public static void buildFeedbackNotification(Context context) {
        Validate.notNull(context, Constants.FRE_INTENT_EXTRA_INFO);
        Intent notificationIntent = new Intent("Kapp.feedback");
        PendingIntent contentIntent = PendingIntent.getBroadcast(context, 0, notificationIntent, 0);
        Notification notification = new NotificationCompat.Builder(context).setTicker(context.getString(R.string.feedback_notification_ticker_text)).setContentTitle(context.getString(R.string.feedback_notification_title)).setContentText(context.getString(R.string.feedback_notification_in_progress_message)).setSmallIcon(R.drawable.feedback_icon).setAutoCancel(false).setOngoing(true).setContentIntent(contentIntent).build();
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService("notification");
        mNotificationManager.notify(FEEDBACK_NOTIFICATION_ID, notification);
    }

    public static void cancelFeedbackNotification(Context context) {
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService("notification");
        mNotificationManager.cancel(FEEDBACK_NOTIFICATION_ID);
    }
}

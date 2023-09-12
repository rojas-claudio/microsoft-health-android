package com.microsoft.kapp.utils;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import com.microsoft.kapp.R;
import com.microsoft.kapp.feedback.FeedbackService;
import com.microsoft.kapp.logging.KLog;
import com.shinobicontrols.charts.ChartView;
import com.shinobicontrols.charts.ShinobiChart;
import com.shinobicontrols.kcompanionapp.charts.internal.ChartThemeCache;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import org.apache.commons.lang3.StringEscapeUtils;
@Deprecated
/* loaded from: classes.dex */
public class FeedbackUtils {
    public static final String FEEDBACK_INTENT = "Kapp.feedback";
    private static final String LOGCAT_FILENAME = "logcat.txt";
    private static final String TAG = FeedbackUtils.class.getSimpleName();
    private static final String ZIP_FILENAME = "diagnosis.diagz";
    private static Bitmap chartScreenshot;
    @Inject
    static FeedbackService mFeedbackService;

    /* loaded from: classes.dex */
    private static class DeviceInfo {
        public String fwVersion;
        public Bitmap screenshot;

        private DeviceInfo() {
        }
    }

    @Deprecated
    private static Uri getLogcatFileUri() {
        List<Pair<String, InputStream>> inputStreams = new ArrayList<>();
        try {
            Process process = Runtime.getRuntime().exec("logcat -d");
            inputStreams.add(new Pair<>("logcat.txt", process.getInputStream()));
            File file = FileUtils.zip(inputStreams, Environment.getExternalStorageDirectory(), ZIP_FILENAME);
            Uri uri = Uri.fromFile(file);
            return uri;
        } catch (Exception e) {
            KLog.e(TAG, "getLogcatFileUri() failed.", e);
            return null;
        }
    }

    @Deprecated
    private static String getEmailSubject(Activity activity) throws PackageManager.NameNotFoundException {
        String appName = activity.getResources().getString(R.string.app_name);
        String version = getApplicationVersion(activity);
        return String.format("Feedback: Android %s App v%s", appName, version);
    }

    private static Uri getScreenshotAttachmentUri(Bitmap bitmap) throws FileNotFoundException, IOException {
        return getAttachmentUriFromBitmap(bitmap, "Screenshot.png");
    }

    private static Uri getChartScreenshotAttachmentUri(Bitmap bitmap) throws FileNotFoundException, IOException {
        return getAttachmentUriFromBitmap(bitmap, "ChartScreenshot.png");
    }

    private static Uri getDeviceScreenshotAttachmentUri(Bitmap bitmap) throws FileNotFoundException, IOException {
        return getAttachmentUriFromBitmap(bitmap, "DeviceScreenshot.png");
    }

    private static Uri getAttachmentUriFromBitmap(Bitmap bitmap, String filename) throws FileNotFoundException, IOException {
        File imageFile = FileUtils.saveBitmapToDisk(bitmap, filename);
        return Uri.fromFile(imageFile);
    }

    private static String getApplicationVersion(Activity activity) throws PackageManager.NameNotFoundException {
        PackageInfo pInfo = activity.getPackageManager().getPackageInfo(activity.getPackageName(), 0);
        String version = pInfo.versionName;
        return version;
    }

    @Deprecated
    public static void sendEmail(Activity activity, String[] recipients, String subject, String htmlBody, ArrayList<Uri> attachmentUris) {
        Intent emailIntent = new Intent("android.intent.action.SEND_MULTIPLE");
        emailIntent.setType("text/html");
        if (recipients != null) {
            emailIntent.putExtra("android.intent.extra.EMAIL", recipients);
        }
        if (subject != null) {
            emailIntent.putExtra("android.intent.extra.SUBJECT", subject);
        }
        if (htmlBody != null) {
            emailIntent.putExtra("android.intent.extra.TEXT", Html.fromHtml(htmlBody));
        }
        if (attachmentUris != null && !attachmentUris.isEmpty()) {
            emailIntent.putParcelableArrayListExtra("android.intent.extra.STREAM", attachmentUris);
        }
        activity.startActivity(Intent.createChooser(emailIntent, activity.getString(R.string.feedback_select_email_app)));
    }

    private static Bitmap captureScreenshot(View view) {
        boolean wasDrawingCacheEnabled = view.isDrawingCacheEnabled();
        Bitmap result = null;
        try {
            view.setDrawingCacheEnabled(true);
            result = Bitmap.createBitmap(view.getDrawingCache());
        } catch (Error ex) {
            KLog.e(TAG, "unable to capture screenshot", ex);
        } catch (Exception ex2) {
            KLog.e(TAG, "unable to capture screenshot", ex2);
        } finally {
            view.setDrawingCacheEnabled(wasDrawingCacheEnabled);
        }
        return result;
    }

    private static Bitmap captureChartScreenshot(final Activity activity, View view) {
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
            chart.setOnSnapshotDoneListener(new ShinobiChart.OnSnapshotDoneListener() { // from class: com.microsoft.kapp.utils.FeedbackUtils.1
                @Override // com.shinobicontrols.charts.ShinobiChart.OnSnapshotDoneListener
                public void onSnapshotDone(Bitmap bitmap) {
                    Bitmap unused = FeedbackUtils.chartScreenshot = bitmap;
                    ShinobiChart.this.getStyle().setCanvasBackgroundColor(0);
                    ShinobiChart.this.getStyle().setPlotAreaBackgroundColor(0);
                    ShinobiChart.this.redrawChart();
                    done.countDown();
                }
            });
            activity.runOnUiThread(new Runnable() { // from class: com.microsoft.kapp.utils.FeedbackUtils.2
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
                KLog.e(TAG, "unable to capture chart screenshot", ex);
            }
            if (captureChartScreenshotComplete) {
                result = chartScreenshot;
            }
        }
        chartScreenshot = null;
        return result;
    }

    private static String appendRowAndColumnTags(String key, String value) {
        return String.format("<b>%s&nbsp</b>%s</br>", StringEscapeUtils.escapeHtml3(key), StringEscapeUtils.escapeHtml3(value));
    }
}

package com.microsoft.kapp.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;
import com.microsoft.kapp.R;
import com.microsoft.kapp.factories.ShareObject;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.krestsdk.models.GolfEvent;
import java.io.File;
import java.io.IOException;
import java.util.Locale;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
/* loaded from: classes.dex */
public class ShareUtils {
    private static String TAG = ShareUtils.class.getSimpleName();

    public static final ShareObject createGolfShareEvent(String chooserTitle, GolfEvent event, String shareTitle, String shareTag, Bitmap screenshot, Activity activity) {
        ShareObject.Builder builder = new ShareObject.Builder();
        String eventStartTime = KAppDateFormatter.formatToMonthDay(event.getStartTime());
        String title = String.format(Locale.getDefault(), shareTitle, eventStartTime, Formatter.formatGolfScore(activity, event.getTotalScore(), event.getTotalScore() - event.getParForHolesPlayed()), event.getDisplayName());
        builder.setChooserTitle(chooserTitle);
        builder.setTitle(title);
        DateTimeFormatter fileNamePosfixFormat = DateTimeFormat.forPattern(activity.getString(R.string.time_format_temp_files));
        String fileName = shareTag + "_chart_" + fileNamePosfixFormat.print(event.getStartTime()) + ".jpg";
        Bitmap mapBitmap = CommonUtils.drawTextOnBitmap(screenshot, title);
        try {
            File tmpImage = FileUtils.saveBitmapToDisk(mapBitmap, fileName);
            builder.addImage(Uri.fromFile(tmpImage));
        } catch (IOException e) {
            KLog.w(TAG, "The Bitmap failed to be saved to disk");
        }
        return builder.build(activity);
    }
}

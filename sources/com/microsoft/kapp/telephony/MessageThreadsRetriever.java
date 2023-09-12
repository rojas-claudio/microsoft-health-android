package com.microsoft.kapp.telephony;

import android.database.Cursor;
import com.microsoft.kapp.database.LoggingContentResolver;
import com.microsoft.kapp.database.Projection;
import com.microsoft.kapp.diagnostics.Validate;
import com.microsoft.kapp.utils.Constants;
import java.util.Locale;
import javax.inject.Inject;
/* loaded from: classes.dex */
public class MessageThreadsRetriever {
    private final LoggingContentResolver mContentResolver;
    private long mThreadLatestDate;

    @Inject
    public MessageThreadsRetriever(LoggingContentResolver contentResolver) {
        Validate.notNull(contentResolver, "contentResolver");
        this.mContentResolver = contentResolver;
        this.mThreadLatestDate = 0L;
    }

    public int[] retrieveThreads() {
        int count;
        Cursor cursor = null;
        int[] threads = null;
        try {
            Projection projection = new Projection();
            int idColumnIndex = projection.addColumn("_id");
            int dateColumnIndex = projection.addColumn("date");
            Locale locale = Locale.getDefault();
            String selectionClause = String.format(locale, "%1$s > %2$d", "date", Long.valueOf(this.mThreadLatestDate));
            cursor = this.mContentResolver.query(Constants.MMS_SMS_CONVERSATION_SIMPLE_CONTENT_PROVIDER_URI, projection, selectionClause, null, null);
            int cursorIndex = 0;
            if (cursor != null && (count = cursor.getCount()) > 0) {
                threads = new int[count];
                while (true) {
                    int cursorIndex2 = cursorIndex;
                    if (!cursor.moveToNext()) {
                        break;
                    }
                    cursorIndex = cursorIndex2 + 1;
                    threads[cursorIndex2] = cursor.getInt(idColumnIndex);
                    long date = cursor.getLong(dateColumnIndex);
                    if (date > this.mThreadLatestDate) {
                        this.mThreadLatestDate = date;
                    }
                }
            }
            return threads;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
}

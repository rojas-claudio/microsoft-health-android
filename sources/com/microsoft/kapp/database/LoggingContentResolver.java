package com.microsoft.kapp.database;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import com.microsoft.kapp.diagnostics.Validate;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.kapp.utils.Constants;
import java.io.FileNotFoundException;
import java.io.InputStream;
import javax.inject.Inject;
import org.apache.commons.lang3.StringUtils;
/* loaded from: classes.dex */
public class LoggingContentResolver {
    private static final String TAG = LoggingContentResolver.class.getSimpleName();
    private final ContentResolver mResolver;

    @Inject
    public LoggingContentResolver(Context context) {
        Validate.notNull(context, Constants.FRE_INTENT_EXTRA_INFO);
        this.mResolver = context.getContentResolver();
    }

    public Cursor query(Uri uri, Projection projection, String selection, String[] selectionArgs, String sortOrder) {
        String str = TAG;
        Object[] objArr = new Object[5];
        objArr[0] = uri.toString();
        objArr[1] = projection;
        objArr[2] = selection;
        objArr[3] = selectionArgs == null ? "null" : StringUtils.join(selectionArgs, ", ");
        objArr[4] = sortOrder;
        KLog.logPrivate(str, "Query: %1$s\nProjection: %2$s\nSelection: %3$s\nSelectionArgs: %4$s\nSortOrder: %5$s", objArr);
        Cursor cursor = this.mResolver.query(uri, projection == null ? null : projection.getColumns(), selection, selectionArgs, sortOrder);
        if (cursor == null) {
            return null;
        }
        return new LoggingCursor(cursor);
    }

    public InputStream openInputStream(Uri uri) throws FileNotFoundException {
        KLog.d(TAG, "Opening Input Stream %s", uri.toString());
        try {
            return this.mResolver.openInputStream(uri);
        } catch (FileNotFoundException ex) {
            KLog.e(TAG, ex.getMessage(), ex);
            throw ex;
        }
    }
}

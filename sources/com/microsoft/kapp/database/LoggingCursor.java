package com.microsoft.kapp.database;

import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.database.CharArrayBuffer;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import com.microsoft.kapp.diagnostics.Validate;
import com.microsoft.kapp.logging.KLog;
/* loaded from: classes.dex */
public class LoggingCursor implements Cursor {
    private static final String TAG = LoggingCursor.class.getSimpleName();
    private final Cursor mCursor;

    public LoggingCursor(Cursor cursor) {
        Validate.notNull(cursor, "cursor");
        this.mCursor = cursor;
    }

    @Override // android.database.Cursor
    public int getCount() {
        return this.mCursor.getCount();
    }

    @Override // android.database.Cursor
    public int getPosition() {
        return this.mCursor.getPosition();
    }

    @Override // android.database.Cursor
    public boolean move(int offset) {
        boolean result = this.mCursor.move(offset);
        if (result) {
            logCurrentRow();
        }
        return result;
    }

    @Override // android.database.Cursor
    public boolean moveToPosition(int position) {
        boolean result = this.mCursor.moveToPosition(position);
        if (result) {
            logCurrentRow();
        }
        return result;
    }

    @Override // android.database.Cursor
    public boolean moveToFirst() {
        boolean result = this.mCursor.moveToFirst();
        if (result) {
            logCurrentRow();
        }
        return result;
    }

    @Override // android.database.Cursor
    public boolean moveToLast() {
        boolean result = this.mCursor.moveToLast();
        if (result) {
            logCurrentRow();
        }
        return result;
    }

    @Override // android.database.Cursor
    public boolean moveToNext() {
        boolean result = this.mCursor.moveToNext();
        if (result) {
            logCurrentRow();
        }
        return result;
    }

    @Override // android.database.Cursor
    public boolean moveToPrevious() {
        boolean result = this.mCursor.moveToPrevious();
        if (result) {
            logCurrentRow();
        }
        return result;
    }

    @Override // android.database.Cursor
    public boolean isFirst() {
        return this.mCursor.isFirst();
    }

    @Override // android.database.Cursor
    public boolean isLast() {
        return this.mCursor.isLast();
    }

    @Override // android.database.Cursor
    public boolean isBeforeFirst() {
        return this.mCursor.isBeforeFirst();
    }

    @Override // android.database.Cursor
    public boolean isAfterLast() {
        return this.mCursor.isAfterLast();
    }

    @Override // android.database.Cursor
    public int getColumnIndex(String columnName) {
        return this.mCursor.getColumnIndex(columnName);
    }

    @Override // android.database.Cursor
    public int getColumnIndexOrThrow(String columnName) throws IllegalArgumentException {
        return this.mCursor.getColumnIndexOrThrow(columnName);
    }

    @Override // android.database.Cursor
    public String getColumnName(int columnIndex) {
        return this.mCursor.getColumnName(columnIndex);
    }

    @Override // android.database.Cursor
    public String[] getColumnNames() {
        return this.mCursor.getColumnNames();
    }

    @Override // android.database.Cursor
    public int getColumnCount() {
        return this.mCursor.getColumnCount();
    }

    @Override // android.database.Cursor
    public byte[] getBlob(int columnIndex) {
        return this.mCursor.getBlob(columnIndex);
    }

    @Override // android.database.Cursor
    public String getString(int columnIndex) {
        return this.mCursor.getString(columnIndex);
    }

    @Override // android.database.Cursor
    public void copyStringToBuffer(int columnIndex, CharArrayBuffer buffer) {
        this.mCursor.copyStringToBuffer(columnIndex, buffer);
    }

    @Override // android.database.Cursor
    public short getShort(int columnIndex) {
        return this.mCursor.getShort(columnIndex);
    }

    @Override // android.database.Cursor
    public int getInt(int columnIndex) {
        return this.mCursor.getInt(columnIndex);
    }

    @Override // android.database.Cursor
    public long getLong(int columnIndex) {
        return this.mCursor.getLong(columnIndex);
    }

    @Override // android.database.Cursor
    public float getFloat(int columnIndex) {
        return this.mCursor.getFloat(columnIndex);
    }

    @Override // android.database.Cursor
    public double getDouble(int columnIndex) {
        return this.mCursor.getDouble(columnIndex);
    }

    @Override // android.database.Cursor
    public int getType(int columnIndex) {
        return this.mCursor.getType(columnIndex);
    }

    @Override // android.database.Cursor
    public boolean isNull(int columnIndex) {
        return this.mCursor.isNull(columnIndex);
    }

    @Override // android.database.Cursor
    public void deactivate() {
    }

    @Override // android.database.Cursor
    public boolean requery() {
        return false;
    }

    @Override // android.database.Cursor, java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        this.mCursor.close();
    }

    @Override // android.database.Cursor
    public boolean isClosed() {
        return this.mCursor.isClosed();
    }

    @Override // android.database.Cursor
    public void registerContentObserver(ContentObserver observer) {
        this.mCursor.registerContentObserver(observer);
    }

    @Override // android.database.Cursor
    public void unregisterContentObserver(ContentObserver observer) {
        this.mCursor.unregisterContentObserver(observer);
    }

    @Override // android.database.Cursor
    public void registerDataSetObserver(DataSetObserver observer) {
        this.mCursor.registerDataSetObserver(observer);
    }

    @Override // android.database.Cursor
    public void unregisterDataSetObserver(DataSetObserver observer) {
        this.mCursor.unregisterDataSetObserver(observer);
    }

    @Override // android.database.Cursor
    public void setNotificationUri(ContentResolver cr, Uri uri) {
        this.mCursor.setNotificationUri(cr, uri);
    }

    @Override // android.database.Cursor
    public boolean getWantsAllOnMoveCalls() {
        return this.mCursor.getWantsAllOnMoveCalls();
    }

    @Override // android.database.Cursor
    public Bundle getExtras() {
        return this.mCursor.getExtras();
    }

    @Override // android.database.Cursor
    public Bundle respond(Bundle extras) {
        return this.mCursor.respond(extras);
    }

    private void logCurrentRow() {
        StringBuilder sb = new StringBuilder();
        int lastIndex = this.mCursor.getColumnCount() - 1;
        for (int i = 0; i <= lastIndex; i++) {
            String value = null;
            switch (this.mCursor.getType(i)) {
                case 0:
                    value = "";
                    break;
                case 1:
                    value = Long.toString(this.mCursor.getLong(i));
                    break;
                case 2:
                    value = Double.toString(this.mCursor.getDouble(i));
                    break;
                case 3:
                    value = this.mCursor.getString(i);
                    break;
                case 4:
                    value = Base64.encodeToString(this.mCursor.getBlob(i), 0);
                    break;
            }
            sb.append(value);
            if (i != lastIndex) {
                sb.append(" | ");
            }
        }
        KLog.logPrivate(TAG, sb.toString());
    }

    @Override // android.database.Cursor
    @TargetApi(19)
    public Uri getNotificationUri() {
        return this.mCursor.getNotificationUri();
    }

    protected void finalize() throws Throwable {
        super.finalize();
    }
}

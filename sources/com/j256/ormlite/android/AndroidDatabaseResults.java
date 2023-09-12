package com.j256.ormlite.android;

import android.database.Cursor;
import com.j256.ormlite.dao.ObjectCache;
import com.j256.ormlite.db.DatabaseType;
import com.j256.ormlite.db.SqliteAndroidDatabaseType;
import com.j256.ormlite.support.DatabaseResults;
import com.microsoft.kapp.utils.Constants;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
/* loaded from: classes.dex */
public class AndroidDatabaseResults implements DatabaseResults {
    private static final int MIN_NUM_COLUMN_NAMES_MAP = 8;
    private static final DatabaseType databaseType = new SqliteAndroidDatabaseType();
    private final Map<String, Integer> columnNameMap;
    private final String[] columnNames;
    private final Cursor cursor;
    private final ObjectCache objectCache;

    public AndroidDatabaseResults(Cursor cursor, ObjectCache objectCache) {
        this.cursor = cursor;
        this.columnNames = cursor.getColumnNames();
        if (this.columnNames.length >= 8) {
            this.columnNameMap = new HashMap();
            for (int i = 0; i < this.columnNames.length; i++) {
                this.columnNameMap.put(this.columnNames[i], Integer.valueOf(i));
            }
        } else {
            this.columnNameMap = null;
        }
        this.objectCache = objectCache;
    }

    @Deprecated
    public AndroidDatabaseResults(Cursor cursor, boolean firstCall, ObjectCache objectCache) {
        this(cursor, objectCache);
    }

    @Override // com.j256.ormlite.support.DatabaseResults
    public int getColumnCount() {
        return this.cursor.getColumnCount();
    }

    @Override // com.j256.ormlite.support.DatabaseResults
    public String[] getColumnNames() {
        int colN = getColumnCount();
        String[] columnNames = new String[colN];
        for (int colC = 0; colC < colN; colC++) {
            columnNames[colC] = this.cursor.getColumnName(colC);
        }
        return columnNames;
    }

    @Override // com.j256.ormlite.support.DatabaseResults
    public boolean first() {
        return this.cursor.moveToFirst();
    }

    @Override // com.j256.ormlite.support.DatabaseResults
    public boolean next() {
        return this.cursor.moveToNext();
    }

    @Override // com.j256.ormlite.support.DatabaseResults
    public boolean last() {
        return this.cursor.moveToLast();
    }

    @Override // com.j256.ormlite.support.DatabaseResults
    public boolean previous() {
        return this.cursor.moveToPrevious();
    }

    @Override // com.j256.ormlite.support.DatabaseResults
    public boolean moveRelative(int offset) {
        return this.cursor.move(offset);
    }

    @Override // com.j256.ormlite.support.DatabaseResults
    public boolean moveAbsolute(int position) {
        return this.cursor.moveToPosition(position);
    }

    public int getCount() {
        return this.cursor.getCount();
    }

    public int getPosition() {
        return this.cursor.getPosition();
    }

    @Override // com.j256.ormlite.support.DatabaseResults
    public int findColumn(String columnName) throws SQLException {
        int index = lookupColumn(columnName);
        if (index >= 0) {
            return index;
        }
        StringBuilder sb = new StringBuilder(columnName.length() + 4);
        databaseType.appendEscapedEntityName(sb, columnName);
        int index2 = lookupColumn(sb.toString());
        if (index2 >= 0) {
            return index2;
        }
        String[] columnNames = this.cursor.getColumnNames();
        throw new SQLException("Unknown field '" + columnName + "' from the Android sqlite cursor, not in:" + Arrays.toString(columnNames));
    }

    @Override // com.j256.ormlite.support.DatabaseResults
    public String getString(int columnIndex) {
        return this.cursor.getString(columnIndex);
    }

    @Override // com.j256.ormlite.support.DatabaseResults
    public boolean getBoolean(int columnIndex) {
        return (this.cursor.isNull(columnIndex) || this.cursor.getShort(columnIndex) == 0) ? false : true;
    }

    @Override // com.j256.ormlite.support.DatabaseResults
    public char getChar(int columnIndex) throws SQLException {
        String string = this.cursor.getString(columnIndex);
        if (string == null || string.length() == 0) {
            return (char) 0;
        }
        if (string.length() == 1) {
            return string.charAt(0);
        }
        throw new SQLException("More than 1 character stored in database column: " + columnIndex);
    }

    @Override // com.j256.ormlite.support.DatabaseResults
    public byte getByte(int columnIndex) {
        return (byte) getShort(columnIndex);
    }

    @Override // com.j256.ormlite.support.DatabaseResults
    public byte[] getBytes(int columnIndex) {
        return this.cursor.getBlob(columnIndex);
    }

    @Override // com.j256.ormlite.support.DatabaseResults
    public short getShort(int columnIndex) {
        return this.cursor.getShort(columnIndex);
    }

    @Override // com.j256.ormlite.support.DatabaseResults
    public int getInt(int columnIndex) {
        return this.cursor.getInt(columnIndex);
    }

    @Override // com.j256.ormlite.support.DatabaseResults
    public long getLong(int columnIndex) {
        return this.cursor.getLong(columnIndex);
    }

    @Override // com.j256.ormlite.support.DatabaseResults
    public float getFloat(int columnIndex) {
        return this.cursor.getFloat(columnIndex);
    }

    @Override // com.j256.ormlite.support.DatabaseResults
    public double getDouble(int columnIndex) {
        return this.cursor.getDouble(columnIndex);
    }

    @Override // com.j256.ormlite.support.DatabaseResults
    public Timestamp getTimestamp(int columnIndex) throws SQLException {
        throw new SQLException("Android does not support timestamp.  Use JAVA_DATE_LONG or JAVA_DATE_STRING types");
    }

    @Override // com.j256.ormlite.support.DatabaseResults
    public InputStream getBlobStream(int columnIndex) {
        return new ByteArrayInputStream(this.cursor.getBlob(columnIndex));
    }

    @Override // com.j256.ormlite.support.DatabaseResults
    public BigDecimal getBigDecimal(int columnIndex) throws SQLException {
        throw new SQLException("Android does not support BigDecimal type.  Use BIG_DECIMAL or BIG_DECIMAL_STRING types");
    }

    @Override // com.j256.ormlite.support.DatabaseResults
    public boolean wasNull(int columnIndex) {
        return this.cursor.isNull(columnIndex);
    }

    @Override // com.j256.ormlite.support.DatabaseResults
    public ObjectCache getObjectCache() {
        return this.objectCache;
    }

    @Override // com.j256.ormlite.support.DatabaseResults
    public void close() {
        this.cursor.close();
    }

    @Override // com.j256.ormlite.support.DatabaseResults
    public void closeQuietly() {
        close();
    }

    public Cursor getRawCursor() {
        return this.cursor;
    }

    public String toString() {
        return getClass().getSimpleName() + Constants.CHAR_AT + Integer.toHexString(super.hashCode());
    }

    private int lookupColumn(String columnName) {
        if (this.columnNameMap == null) {
            for (int i = 0; i < this.columnNames.length; i++) {
                if (this.columnNames[i].equals(columnName)) {
                    return i;
                }
            }
            return -1;
        }
        Integer index = this.columnNameMap.get(columnName);
        if (index == null) {
            return -1;
        }
        int i2 = index.intValue();
        return i2;
    }
}

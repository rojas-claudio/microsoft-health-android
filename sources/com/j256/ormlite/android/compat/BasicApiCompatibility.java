package com.j256.ormlite.android.compat;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.j256.ormlite.android.compat.ApiCompatibility;
/* loaded from: classes.dex */
public class BasicApiCompatibility implements ApiCompatibility {
    @Override // com.j256.ormlite.android.compat.ApiCompatibility
    public Cursor rawQuery(SQLiteDatabase db, String sql, String[] selectionArgs, ApiCompatibility.CancellationHook cancellationHook) {
        return db.rawQuery(sql, selectionArgs);
    }

    @Override // com.j256.ormlite.android.compat.ApiCompatibility
    public ApiCompatibility.CancellationHook createCancellationHook() {
        return null;
    }
}

package com.j256.ormlite.android.compat;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.CancellationSignal;
import com.j256.ormlite.android.compat.ApiCompatibility;
/* loaded from: classes.dex */
public class JellyBeanApiCompatibility extends BasicApiCompatibility {
    @Override // com.j256.ormlite.android.compat.BasicApiCompatibility, com.j256.ormlite.android.compat.ApiCompatibility
    public Cursor rawQuery(SQLiteDatabase db, String sql, String[] selectionArgs, ApiCompatibility.CancellationHook cancellationHook) {
        if (cancellationHook == null) {
            return db.rawQuery(sql, selectionArgs);
        }
        return db.rawQuery(sql, selectionArgs, ((JellyBeanCancellationHook) cancellationHook).cancellationSignal);
    }

    @Override // com.j256.ormlite.android.compat.BasicApiCompatibility, com.j256.ormlite.android.compat.ApiCompatibility
    public ApiCompatibility.CancellationHook createCancellationHook() {
        return new JellyBeanCancellationHook();
    }

    /* loaded from: classes.dex */
    protected static class JellyBeanCancellationHook implements ApiCompatibility.CancellationHook {
        private final CancellationSignal cancellationSignal = new CancellationSignal();

        @Override // com.j256.ormlite.android.compat.ApiCompatibility.CancellationHook
        public void cancel() {
            this.cancellationSignal.cancel();
        }
    }
}

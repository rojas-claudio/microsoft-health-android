package com.microsoft.kapp.sensor.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.microsoft.kapp.cache.DBHelper;
import java.sql.SQLException;
/* loaded from: classes.dex */
public class SensorDBHelper extends OrmLiteSqliteOpenHelper {
    private static final String DATABASE_NAME = "sensordata.sqlite";
    private static final int DATABASE_VERSION = 1;
    private static final String TAG = DBHelper.class.getName();
    private Dao<DBSensorEvent, String> sensorEventDao;

    public SensorDBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        this.sensorEventDao = null;
    }

    @Override // com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper
    public void onCreate(SQLiteDatabase database, ConnectionSource connSource) {
        try {
            TableUtils.createTable(connSource, DBSensorEvent.class);
        } catch (SQLException e) {
            Log.e(TAG, "unable to create sensor DB");
        }
    }

    @Override // com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connSource, int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(connSource, DBSensorEvent.class, false);
            onCreate(db, connSource);
        } catch (SQLException e) {
            Log.e(TAG, "fatal exception during onUpgrade", e);
            throw new RuntimeException(e);
        }
    }

    public Dao<DBSensorEvent, String> getSensorEventDao() {
        if (this.sensorEventDao == null) {
            try {
                this.sensorEventDao = getDao(DBSensorEvent.class);
            } catch (SQLException e) {
                Log.d(TAG, "unable to create KSensorEvent data access obj");
            }
        }
        return this.sensorEventDao;
    }

    public void Clear() {
        try {
            TableUtils.clearTable(this.connectionSource, DBSensorEvent.class);
        } catch (SQLException e) {
            Log.d(TAG, "unable to clear sensor DB");
        }
    }
}

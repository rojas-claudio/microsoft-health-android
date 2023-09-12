package com.microsoft.kapp.sensor.db;

import android.content.Context;
import com.j256.ormlite.dao.GenericRawResults;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;
import com.microsoft.kapp.diagnostics.Validate;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.kapp.utils.Constants;
import java.sql.SQLException;
import org.joda.time.DateTime;
/* loaded from: classes.dex */
public class SensorDBImpl implements SensorDB {
    private static final String TAG = SensorDBHelper.class.getSimpleName();
    private SensorDBHelper mHelper;

    public SensorDBImpl(Context context) {
        Validate.notNull(context, Constants.FRE_INTENT_EXTRA_INFO);
        this.mHelper = new SensorDBHelper(context);
    }

    @Override // com.microsoft.kapp.sensor.db.SensorDB
    public synchronized boolean addEvent(DBSensorType sensorType, DateTime eventTime, Double value) {
        Validate.notNull(sensorType, "sensorType");
        Validate.notNull(eventTime, "eventTime");
        Validate.notNull(value, "value");
        return addEvent(new DBSensorEvent(sensorType, eventTime, value));
    }

    @Override // com.microsoft.kapp.sensor.db.SensorDB
    public synchronized boolean addEvent(DBSensorEvent sensorEvent) {
        boolean success;
        Validate.notNull(sensorEvent, "sensorEvent");
        success = false;
        try {
            this.mHelper.getSensorEventDao().createOrUpdate(sensorEvent);
            success = true;
        } catch (SQLException ex) {
            KLog.d(TAG, "unable to create/update sensor item", ex);
        }
        return success;
    }

    @Override // com.microsoft.kapp.sensor.db.SensorDB
    public synchronized Iterable<DBSensorEvent> getEventsBetween(DBSensorType sensorType, DateTime startTime, DateTime endTime) {
        GenericRawResults genericRawResults;
        try {
            QueryBuilder<DBSensorEvent, String> qb = this.mHelper.getSensorEventDao().queryBuilder();
            qb.orderBy("EventTime", true).where().between("EventTime", Long.valueOf(startTime.getMillis()), Long.valueOf(endTime.getMillis())).and().in("SensorType", sensorType);
            genericRawResults = this.mHelper.getSensorEventDao().queryRaw(qb.prepareStatementString(), this.mHelper.getSensorEventDao().getRawRowMapper(), new String[0]);
        } catch (SQLException ex) {
            KLog.e(TAG, "unable to query for sensor events between " + startTime.toString() + " and " + endTime.toString(), ex);
            genericRawResults = null;
        }
        return genericRawResults;
    }

    @Override // com.microsoft.kapp.sensor.db.SensorDB
    public synchronized Iterable<DBSensorEvent> getAllEventsAfter(DBSensorType sensorType, DateTime startTime) {
        GenericRawResults genericRawResults;
        try {
            QueryBuilder<DBSensorEvent, String> qb = this.mHelper.getSensorEventDao().queryBuilder();
            qb.where().gt("EventTime", Long.valueOf(startTime.getMillis())).and().in("SensorType", sensorType);
            genericRawResults = this.mHelper.getSensorEventDao().queryRaw(qb.prepareStatementString(), this.mHelper.getSensorEventDao().getRawRowMapper(), new String[0]);
        } catch (SQLException ex) {
            KLog.e(TAG, "unable to query for sensor events after " + startTime.toString(), ex);
            genericRawResults = null;
        }
        return genericRawResults;
    }

    @Override // com.microsoft.kapp.sensor.db.SensorDB
    public synchronized Iterable<DBSensorEvent> getAllEventsBefore(DBSensorType sensorType, DateTime endTime) {
        GenericRawResults genericRawResults;
        try {
            QueryBuilder<DBSensorEvent, String> qb = this.mHelper.getSensorEventDao().queryBuilder();
            qb.where().lt("EventTime", Long.valueOf(endTime.getMillis())).and().in("SensorType", sensorType);
            genericRawResults = this.mHelper.getSensorEventDao().queryRaw(qb.prepareStatementString(), this.mHelper.getSensorEventDao().getRawRowMapper(), new String[0]);
        } catch (SQLException ex) {
            KLog.e(TAG, "unable to query for sensor events before " + endTime.toString(), ex);
            genericRawResults = null;
        }
        return genericRawResults;
    }

    @Override // com.microsoft.kapp.sensor.db.SensorDB
    public synchronized boolean clearAllEventsAfter(DBSensorType sensorType, DateTime startTime) {
        boolean success;
        success = true;
        try {
            DeleteBuilder<DBSensorEvent, String> deleteBuilder = this.mHelper.getSensorEventDao().deleteBuilder();
            deleteBuilder.where().ge("EventTime", Long.valueOf(startTime.getMillis())).and().in("SensorType", sensorType);
            deleteBuilder.delete();
        } catch (SQLException ex) {
            KLog.e(TAG, "unable to query for sensor events after " + startTime.toString(), ex);
            success = false;
        }
        return success;
    }

    @Override // com.microsoft.kapp.sensor.db.SensorDB
    public synchronized boolean clearAllEventsBefore(DBSensorType sensorType, DateTime endTime) {
        boolean success;
        success = true;
        try {
            DeleteBuilder<DBSensorEvent, String> deleteBuilder = this.mHelper.getSensorEventDao().deleteBuilder();
            deleteBuilder.where().le("EventTime", Long.valueOf(endTime.getMillis())).and().in("SensorType", sensorType);
            deleteBuilder.delete();
        } catch (SQLException ex) {
            KLog.e(TAG, "unable to query for sensor events after " + endTime.toString(), ex);
            success = false;
        }
        return success;
    }

    @Override // com.microsoft.kapp.sensor.db.SensorDB
    public synchronized boolean clearEventsBetween(DBSensorType sensorType, DateTime startTime, DateTime endTime) {
        boolean success;
        success = true;
        try {
            DeleteBuilder<DBSensorEvent, String> deleteBuilder = this.mHelper.getSensorEventDao().deleteBuilder();
            deleteBuilder.where().ge("EventTime", Long.valueOf(startTime.getMillis())).and().le("EventTime", Long.valueOf(endTime.getMillis())).and().in("SensorType", sensorType);
            deleteBuilder.delete();
        } catch (SQLException ex) {
            KLog.e(TAG, "unable to delete sensor events between " + startTime.toString() + " and " + endTime.toString(), ex);
            success = false;
        }
        return success;
    }

    @Override // com.microsoft.kapp.sensor.db.SensorDB
    public synchronized DateTime getOldestEventTime(DBSensorType sensorType) {
        DateTime dateTime;
        DBSensorEvent firstEvent;
        try {
            QueryBuilder<DBSensorEvent, String> qb = this.mHelper.getSensorEventDao().queryBuilder();
            qb.orderBy("EventTime", true).where().in("SensorType", sensorType);
            firstEvent = (DBSensorEvent) this.mHelper.getSensorEventDao().queryRaw(qb.prepareStatementString(), this.mHelper.getSensorEventDao().getRawRowMapper(), new String[0]).getFirstResult();
        } catch (SQLException ex) {
            KLog.e(TAG, "unable to query for first event", ex);
        }
        if (firstEvent != null) {
            dateTime = firstEvent.getEventTime();
        }
        dateTime = null;
        return dateTime;
    }

    @Override // com.microsoft.kapp.sensor.db.SensorDB
    public synchronized DateTime getOldestEventTime() {
        DateTime dateTime;
        DBSensorEvent firstEvent;
        try {
            QueryBuilder<DBSensorEvent, String> qb = this.mHelper.getSensorEventDao().queryBuilder();
            qb.orderBy("EventTime", true);
            firstEvent = (DBSensorEvent) this.mHelper.getSensorEventDao().queryRaw(qb.prepareStatementString(), this.mHelper.getSensorEventDao().getRawRowMapper(), new String[0]).getFirstResult();
        } catch (SQLException ex) {
            KLog.e(TAG, "unable to query for first event", ex);
        }
        if (firstEvent != null) {
            dateTime = firstEvent.getEventTime();
        }
        dateTime = null;
        return dateTime;
    }

    @Override // com.microsoft.kapp.sensor.db.SensorDB
    public synchronized Long getTotalEventCount(DBSensorType sensorType) {
        Long l;
        try {
            QueryBuilder<DBSensorEvent, String> qb = this.mHelper.getSensorEventDao().queryBuilder();
            long count = qb.where().in("SensorType", sensorType).countOf();
            l = Long.valueOf(count);
        } catch (SQLException ex) {
            KLog.e(TAG, "unable to query for total event count", ex);
            l = null;
        }
        return l;
    }
}

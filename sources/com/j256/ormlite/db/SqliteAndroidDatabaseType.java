package com.j256.ormlite.db;

import com.j256.ormlite.android.DatabaseTableConfigUtil;
import com.j256.ormlite.field.DataPersister;
import com.j256.ormlite.field.FieldConverter;
import com.j256.ormlite.field.FieldType;
import com.j256.ormlite.field.types.DateStringType;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.DatabaseTableConfig;
import java.sql.SQLException;
/* loaded from: classes.dex */
public class SqliteAndroidDatabaseType extends BaseSqliteDatabaseType {
    @Override // com.j256.ormlite.db.BaseDatabaseType, com.j256.ormlite.db.DatabaseType
    public void loadDriver() {
    }

    @Override // com.j256.ormlite.db.DatabaseType
    public boolean isDatabaseUrlThisType(String url, String dbTypePart) {
        return true;
    }

    @Override // com.j256.ormlite.db.BaseDatabaseType
    protected String getDriverClassName() {
        return null;
    }

    @Override // com.j256.ormlite.db.DatabaseType
    public String getDatabaseName() {
        return "Android SQLite";
    }

    @Override // com.j256.ormlite.db.BaseDatabaseType
    protected void appendDateType(StringBuilder sb, FieldType fieldType, int fieldWidth) {
        appendStringType(sb, fieldType, fieldWidth);
    }

    @Override // com.j256.ormlite.db.BaseDatabaseType
    protected void appendBooleanType(StringBuilder sb, FieldType fieldType, int fieldWidth) {
        appendShortType(sb, fieldType, fieldWidth);
    }

    @Override // com.j256.ormlite.db.BaseSqliteDatabaseType, com.j256.ormlite.db.BaseDatabaseType, com.j256.ormlite.db.DatabaseType
    public FieldConverter getFieldConverter(DataPersister dataPersister) {
        switch (dataPersister.getSqlType()) {
            case DATE:
                return DateStringType.getSingleton();
            default:
                return super.getFieldConverter(dataPersister);
        }
    }

    @Override // com.j256.ormlite.db.BaseDatabaseType, com.j256.ormlite.db.DatabaseType
    public boolean isNestedSavePointsSupported() {
        return false;
    }

    @Override // com.j256.ormlite.db.BaseDatabaseType, com.j256.ormlite.db.DatabaseType
    public boolean isBatchUseTransaction() {
        return true;
    }

    @Override // com.j256.ormlite.db.BaseDatabaseType, com.j256.ormlite.db.DatabaseType
    public <T> DatabaseTableConfig<T> extractDatabaseTableConfig(ConnectionSource connectionSource, Class<T> clazz) throws SQLException {
        return DatabaseTableConfigUtil.fromClass(connectionSource, clazz);
    }
}

package com.j256.ormlite.db;

import com.j256.ormlite.db.BaseDatabaseType;
import com.j256.ormlite.field.DataPersister;
import com.j256.ormlite.field.FieldConverter;
import com.j256.ormlite.field.FieldType;
import com.j256.ormlite.field.SqlType;
import com.j256.ormlite.field.types.BigDecimalStringType;
import java.util.List;
/* loaded from: classes.dex */
public abstract class BaseSqliteDatabaseType extends BaseDatabaseType {
    private static final FieldConverter booleanConverter = new BaseDatabaseType.BooleanNumberFieldConverter();

    @Override // com.j256.ormlite.db.BaseDatabaseType
    protected void appendLongType(StringBuilder sb, FieldType fieldType, int fieldWidth) {
        if (fieldType.getSqlType() == SqlType.LONG && fieldType.isGeneratedId()) {
            sb.append("INTEGER");
        } else {
            sb.append("BIGINT");
        }
    }

    @Override // com.j256.ormlite.db.BaseDatabaseType
    protected void configureGeneratedId(String tableName, StringBuilder sb, FieldType fieldType, List<String> statementsBefore, List<String> statementsAfter, List<String> additionalArgs, List<String> queriesAfter) {
        if (fieldType.getSqlType() != SqlType.INTEGER && fieldType.getSqlType() != SqlType.LONG) {
            throw new IllegalArgumentException("Sqlite requires that auto-increment generated-id be integer or long type");
        }
        sb.append("PRIMARY KEY AUTOINCREMENT ");
    }

    @Override // com.j256.ormlite.db.BaseDatabaseType
    protected boolean generatedIdSqlAtEnd() {
        return false;
    }

    @Override // com.j256.ormlite.db.BaseDatabaseType, com.j256.ormlite.db.DatabaseType
    public boolean isVarcharFieldWidthSupported() {
        return false;
    }

    @Override // com.j256.ormlite.db.BaseDatabaseType, com.j256.ormlite.db.DatabaseType
    public boolean isCreateTableReturnsZero() {
        return false;
    }

    @Override // com.j256.ormlite.db.BaseDatabaseType, com.j256.ormlite.db.DatabaseType
    public boolean isCreateIfNotExistsSupported() {
        return true;
    }

    @Override // com.j256.ormlite.db.BaseDatabaseType, com.j256.ormlite.db.DatabaseType
    public FieldConverter getFieldConverter(DataPersister dataPersister) {
        switch (dataPersister.getSqlType()) {
            case BOOLEAN:
                return booleanConverter;
            case BIG_DECIMAL:
                return BigDecimalStringType.getSingleton();
            default:
                return super.getFieldConverter(dataPersister);
        }
    }

    @Override // com.j256.ormlite.db.BaseDatabaseType, com.j256.ormlite.db.DatabaseType
    public void appendInsertNoColumns(StringBuilder sb) {
        sb.append("DEFAULT VALUES");
    }
}

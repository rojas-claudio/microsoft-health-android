package com.j256.ormlite.db;

import com.j256.ormlite.field.BaseFieldConverter;
import com.j256.ormlite.field.DataPersister;
import com.j256.ormlite.field.FieldConverter;
import com.j256.ormlite.field.FieldType;
import com.j256.ormlite.field.SqlType;
import com.j256.ormlite.misc.SqlExceptionUtil;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.support.DatabaseResults;
import com.j256.ormlite.table.DatabaseTableConfig;
import java.sql.Driver;
import java.sql.SQLException;
import java.util.List;
/* loaded from: classes.dex */
public abstract class BaseDatabaseType implements DatabaseType {
    protected static String DEFAULT_SEQUENCE_SUFFIX = "_id_seq";
    protected Driver driver;

    protected abstract String getDriverClassName();

    @Override // com.j256.ormlite.db.DatabaseType
    public void loadDriver() throws SQLException {
        String className = getDriverClassName();
        if (className != null) {
            try {
                Class.forName(className);
            } catch (ClassNotFoundException e) {
                throw SqlExceptionUtil.create("Driver class was not found for " + getDatabaseName() + " database.  Missing jar with class " + className + ".", e);
            }
        }
    }

    @Override // com.j256.ormlite.db.DatabaseType
    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    @Override // com.j256.ormlite.db.DatabaseType
    public void appendColumnArg(String tableName, StringBuilder sb, FieldType fieldType, List<String> additionalArgs, List<String> statementsBefore, List<String> statementsAfter, List<String> queriesAfter) throws SQLException {
        appendEscapedEntityName(sb, fieldType.getColumnName());
        sb.append(' ');
        DataPersister dataPersister = fieldType.getDataPersister();
        int fieldWidth = fieldType.getWidth();
        if (fieldWidth == 0) {
            fieldWidth = dataPersister.getDefaultWidth();
        }
        switch (dataPersister.getSqlType()) {
            case STRING:
                appendStringType(sb, fieldType, fieldWidth);
                break;
            case LONG_STRING:
                appendLongStringType(sb, fieldType, fieldWidth);
                break;
            case BOOLEAN:
                appendBooleanType(sb, fieldType, fieldWidth);
                break;
            case DATE:
                appendDateType(sb, fieldType, fieldWidth);
                break;
            case CHAR:
                appendCharType(sb, fieldType, fieldWidth);
                break;
            case BYTE:
                appendByteType(sb, fieldType, fieldWidth);
                break;
            case BYTE_ARRAY:
                appendByteArrayType(sb, fieldType, fieldWidth);
                break;
            case SHORT:
                appendShortType(sb, fieldType, fieldWidth);
                break;
            case INTEGER:
                appendIntegerType(sb, fieldType, fieldWidth);
                break;
            case LONG:
                appendLongType(sb, fieldType, fieldWidth);
                break;
            case FLOAT:
                appendFloatType(sb, fieldType, fieldWidth);
                break;
            case DOUBLE:
                appendDoubleType(sb, fieldType, fieldWidth);
                break;
            case SERIALIZABLE:
                appendSerializableType(sb, fieldType, fieldWidth);
                break;
            case BIG_DECIMAL:
                appendBigDecimalNumericType(sb, fieldType, fieldWidth);
                break;
            default:
                throw new IllegalArgumentException("Unknown SQL-type " + dataPersister.getSqlType());
        }
        sb.append(' ');
        if (fieldType.isGeneratedIdSequence() && !fieldType.isSelfGeneratedId()) {
            configureGeneratedIdSequence(sb, fieldType, statementsBefore, additionalArgs, queriesAfter);
        } else if (fieldType.isGeneratedId() && !fieldType.isSelfGeneratedId()) {
            configureGeneratedId(tableName, sb, fieldType, statementsBefore, statementsAfter, additionalArgs, queriesAfter);
        } else if (fieldType.isId()) {
            configureId(sb, fieldType, statementsBefore, additionalArgs, queriesAfter);
        }
        if (!fieldType.isGeneratedId()) {
            Object defaultValue = fieldType.getDefaultValue();
            if (defaultValue != null) {
                sb.append("DEFAULT ");
                appendDefaultValue(sb, fieldType, defaultValue);
                sb.append(' ');
            }
            if (fieldType.isCanBeNull()) {
                appendCanBeNull(sb, fieldType);
            } else {
                sb.append("NOT NULL ");
            }
            if (fieldType.isUnique()) {
                addSingleUnique(sb, fieldType, additionalArgs, statementsAfter);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void appendStringType(StringBuilder sb, FieldType fieldType, int fieldWidth) {
        if (isVarcharFieldWidthSupported()) {
            sb.append("VARCHAR(").append(fieldWidth).append(")");
        } else {
            sb.append("VARCHAR");
        }
    }

    protected void appendLongStringType(StringBuilder sb, FieldType fieldType, int fieldWidth) {
        sb.append("TEXT");
    }

    protected void appendDateType(StringBuilder sb, FieldType fieldType, int fieldWidth) {
        sb.append("TIMESTAMP");
    }

    protected void appendBooleanType(StringBuilder sb, FieldType fieldType, int fieldWidth) {
        sb.append("BOOLEAN");
    }

    protected void appendCharType(StringBuilder sb, FieldType fieldType, int fieldWidth) {
        sb.append("CHAR");
    }

    protected void appendByteType(StringBuilder sb, FieldType fieldType, int fieldWidth) {
        sb.append("TINYINT");
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void appendShortType(StringBuilder sb, FieldType fieldType, int fieldWidth) {
        sb.append("SMALLINT");
    }

    private void appendIntegerType(StringBuilder sb, FieldType fieldType, int fieldWidth) {
        sb.append("INTEGER");
    }

    protected void appendLongType(StringBuilder sb, FieldType fieldType, int fieldWidth) {
        sb.append("BIGINT");
    }

    private void appendFloatType(StringBuilder sb, FieldType fieldType, int fieldWidth) {
        sb.append("FLOAT");
    }

    private void appendDoubleType(StringBuilder sb, FieldType fieldType, int fieldWidth) {
        sb.append("DOUBLE PRECISION");
    }

    protected void appendByteArrayType(StringBuilder sb, FieldType fieldType, int fieldWidth) {
        sb.append("BLOB");
    }

    protected void appendSerializableType(StringBuilder sb, FieldType fieldType, int fieldWidth) {
        sb.append("BLOB");
    }

    protected void appendBigDecimalNumericType(StringBuilder sb, FieldType fieldType, int fieldWidth) {
        sb.append("NUMERIC");
    }

    private void appendDefaultValue(StringBuilder sb, FieldType fieldType, Object defaultValue) {
        if (fieldType.isEscapedDefaultValue()) {
            appendEscapedWord(sb, defaultValue.toString());
        } else {
            sb.append(defaultValue);
        }
    }

    protected void configureGeneratedIdSequence(StringBuilder sb, FieldType fieldType, List<String> statementsBefore, List<String> additionalArgs, List<String> queriesAfter) throws SQLException {
        throw new SQLException("GeneratedIdSequence is not supported by database " + getDatabaseName() + " for field " + fieldType);
    }

    protected void configureGeneratedId(String tableName, StringBuilder sb, FieldType fieldType, List<String> statementsBefore, List<String> statementsAfter, List<String> additionalArgs, List<String> queriesAfter) {
        throw new IllegalStateException("GeneratedId is not supported by database " + getDatabaseName() + " for field " + fieldType);
    }

    protected void configureId(StringBuilder sb, FieldType fieldType, List<String> statementsBefore, List<String> additionalArgs, List<String> queriesAfter) {
    }

    @Override // com.j256.ormlite.db.DatabaseType
    public void addPrimaryKeySql(FieldType[] fieldTypes, List<String> additionalArgs, List<String> statementsBefore, List<String> statementsAfter, List<String> queriesAfter) {
        StringBuilder sb = null;
        for (FieldType fieldType : fieldTypes) {
            if ((!fieldType.isGeneratedId() || generatedIdSqlAtEnd() || fieldType.isSelfGeneratedId()) && fieldType.isId()) {
                if (sb == null) {
                    sb = new StringBuilder(48);
                    sb.append("PRIMARY KEY (");
                } else {
                    sb.append(',');
                }
                appendEscapedEntityName(sb, fieldType.getColumnName());
            }
        }
        if (sb != null) {
            sb.append(") ");
            additionalArgs.add(sb.toString());
        }
    }

    protected boolean generatedIdSqlAtEnd() {
        return true;
    }

    @Override // com.j256.ormlite.db.DatabaseType
    public void addUniqueComboSql(FieldType[] fieldTypes, List<String> additionalArgs, List<String> statementsBefore, List<String> statementsAfter, List<String> queriesAfter) {
        StringBuilder sb = null;
        for (FieldType fieldType : fieldTypes) {
            if (fieldType.isUniqueCombo()) {
                if (sb == null) {
                    sb = new StringBuilder(48);
                    sb.append("UNIQUE (");
                } else {
                    sb.append(',');
                }
                appendEscapedEntityName(sb, fieldType.getColumnName());
            }
        }
        if (sb != null) {
            sb.append(") ");
            additionalArgs.add(sb.toString());
        }
    }

    @Override // com.j256.ormlite.db.DatabaseType
    public void dropColumnArg(FieldType fieldType, List<String> statementsBefore, List<String> statementsAfter) {
    }

    @Override // com.j256.ormlite.db.DatabaseType
    public void appendEscapedWord(StringBuilder sb, String word) {
        sb.append('\'').append(word).append('\'');
    }

    @Override // com.j256.ormlite.db.DatabaseType
    public void appendEscapedEntityName(StringBuilder sb, String name) {
        sb.append('`').append(name).append('`');
    }

    @Override // com.j256.ormlite.db.DatabaseType
    public String generateIdSequenceName(String tableName, FieldType idFieldType) {
        String name = tableName + DEFAULT_SEQUENCE_SUFFIX;
        if (isEntityNamesMustBeUpCase()) {
            return name.toUpperCase();
        }
        return name;
    }

    @Override // com.j256.ormlite.db.DatabaseType
    public String getCommentLinePrefix() {
        return "-- ";
    }

    @Override // com.j256.ormlite.db.DatabaseType
    public FieldConverter getFieldConverter(DataPersister dataPersister) {
        return dataPersister;
    }

    @Override // com.j256.ormlite.db.DatabaseType
    public boolean isIdSequenceNeeded() {
        return false;
    }

    @Override // com.j256.ormlite.db.DatabaseType
    public boolean isVarcharFieldWidthSupported() {
        return true;
    }

    @Override // com.j256.ormlite.db.DatabaseType
    public boolean isLimitSqlSupported() {
        return true;
    }

    @Override // com.j256.ormlite.db.DatabaseType
    public boolean isOffsetSqlSupported() {
        return true;
    }

    @Override // com.j256.ormlite.db.DatabaseType
    public boolean isOffsetLimitArgument() {
        return false;
    }

    @Override // com.j256.ormlite.db.DatabaseType
    public boolean isLimitAfterSelect() {
        return false;
    }

    @Override // com.j256.ormlite.db.DatabaseType
    public void appendLimitValue(StringBuilder sb, long limit, Long offset) {
        sb.append("LIMIT ").append(limit).append(' ');
    }

    @Override // com.j256.ormlite.db.DatabaseType
    public void appendOffsetValue(StringBuilder sb, long offset) {
        sb.append("OFFSET ").append(offset).append(' ');
    }

    @Override // com.j256.ormlite.db.DatabaseType
    public void appendSelectNextValFromSequence(StringBuilder sb, String sequenceName) {
    }

    @Override // com.j256.ormlite.db.DatabaseType
    public void appendCreateTableSuffix(StringBuilder sb) {
    }

    @Override // com.j256.ormlite.db.DatabaseType
    public boolean isCreateTableReturnsZero() {
        return true;
    }

    @Override // com.j256.ormlite.db.DatabaseType
    public boolean isCreateTableReturnsNegative() {
        return false;
    }

    @Override // com.j256.ormlite.db.DatabaseType
    public boolean isEntityNamesMustBeUpCase() {
        return false;
    }

    @Override // com.j256.ormlite.db.DatabaseType
    public boolean isNestedSavePointsSupported() {
        return true;
    }

    @Override // com.j256.ormlite.db.DatabaseType
    public String getPingStatement() {
        return "SELECT 1";
    }

    @Override // com.j256.ormlite.db.DatabaseType
    public boolean isBatchUseTransaction() {
        return false;
    }

    @Override // com.j256.ormlite.db.DatabaseType
    public boolean isTruncateSupported() {
        return false;
    }

    @Override // com.j256.ormlite.db.DatabaseType
    public boolean isCreateIfNotExistsSupported() {
        return false;
    }

    @Override // com.j256.ormlite.db.DatabaseType
    public boolean isCreateIndexIfNotExistsSupported() {
        return isCreateIfNotExistsSupported();
    }

    @Override // com.j256.ormlite.db.DatabaseType
    public boolean isSelectSequenceBeforeInsert() {
        return false;
    }

    @Override // com.j256.ormlite.db.DatabaseType
    public boolean isAllowGeneratedIdInsertSupported() {
        return true;
    }

    @Override // com.j256.ormlite.db.DatabaseType
    public <T> DatabaseTableConfig<T> extractDatabaseTableConfig(ConnectionSource connectionSource, Class<T> clazz) throws SQLException {
        return null;
    }

    @Override // com.j256.ormlite.db.DatabaseType
    public void appendInsertNoColumns(StringBuilder sb) {
        sb.append("() VALUES ()");
    }

    private void appendCanBeNull(StringBuilder sb, FieldType fieldType) {
    }

    private void addSingleUnique(StringBuilder sb, FieldType fieldType, List<String> additionalArgs, List<String> statementsAfter) {
        StringBuilder alterSb = new StringBuilder();
        alterSb.append(" UNIQUE (");
        appendEscapedEntityName(alterSb, fieldType.getColumnName());
        alterSb.append(")");
        additionalArgs.add(alterSb.toString());
    }

    /* loaded from: classes.dex */
    protected static class BooleanNumberFieldConverter extends BaseFieldConverter {
        @Override // com.j256.ormlite.field.FieldConverter
        public SqlType getSqlType() {
            return SqlType.BOOLEAN;
        }

        @Override // com.j256.ormlite.field.FieldConverter
        public Object parseDefaultString(FieldType fieldType, String defaultStr) {
            boolean bool = Boolean.parseBoolean(defaultStr);
            return bool ? (byte) 1 : (byte) 0;
        }

        @Override // com.j256.ormlite.field.BaseFieldConverter, com.j256.ormlite.field.FieldConverter
        public Object javaToSqlArg(FieldType fieldType, Object obj) {
            Boolean bool = (Boolean) obj;
            return bool.booleanValue() ? (byte) 1 : (byte) 0;
        }

        @Override // com.j256.ormlite.field.FieldConverter
        public Object resultToSqlArg(FieldType fieldType, DatabaseResults results, int columnPos) throws SQLException {
            return Byte.valueOf(results.getByte(columnPos));
        }

        @Override // com.j256.ormlite.field.BaseFieldConverter, com.j256.ormlite.field.FieldConverter
        public Object sqlArgToJava(FieldType fieldType, Object sqlArg, int columnPos) {
            byte arg = ((Byte) sqlArg).byteValue();
            return arg == 1;
        }

        @Override // com.j256.ormlite.field.FieldConverter
        public Object resultStringToJava(FieldType fieldType, String stringValue, int columnPos) {
            return sqlArgToJava(fieldType, Byte.valueOf(Byte.parseByte(stringValue)), columnPos);
        }
    }
}

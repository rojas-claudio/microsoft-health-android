package com.j256.ormlite.table;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.db.DatabaseType;
import com.j256.ormlite.field.FieldType;
import com.j256.ormlite.logger.Logger;
import com.j256.ormlite.logger.LoggerFactory;
import com.j256.ormlite.misc.SqlExceptionUtil;
import com.j256.ormlite.stmt.StatementBuilder;
import com.j256.ormlite.support.CompiledStatement;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.support.DatabaseConnection;
import com.j256.ormlite.support.DatabaseResults;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
/* loaded from: classes.dex */
public class TableUtils {
    private static Logger logger = LoggerFactory.getLogger(TableUtils.class);
    private static final FieldType[] noFieldTypes = new FieldType[0];

    private TableUtils() {
    }

    public static <T> int createTable(ConnectionSource connectionSource, Class<T> dataClass) throws SQLException {
        return createTable(connectionSource, (Class) dataClass, false);
    }

    public static <T> int createTableIfNotExists(ConnectionSource connectionSource, Class<T> dataClass) throws SQLException {
        return createTable(connectionSource, (Class) dataClass, true);
    }

    public static <T> int createTable(ConnectionSource connectionSource, DatabaseTableConfig<T> tableConfig) throws SQLException {
        return createTable(connectionSource, (DatabaseTableConfig) tableConfig, false);
    }

    public static <T> int createTableIfNotExists(ConnectionSource connectionSource, DatabaseTableConfig<T> tableConfig) throws SQLException {
        return createTable(connectionSource, (DatabaseTableConfig) tableConfig, true);
    }

    public static <T, ID> List<String> getCreateTableStatements(ConnectionSource connectionSource, Class<T> dataClass) throws SQLException {
        Dao<T, ID> dao = DaoManager.createDao(connectionSource, dataClass);
        if (dao instanceof BaseDaoImpl) {
            return addCreateTableStatements(connectionSource, ((BaseDaoImpl) dao).getTableInfo(), false);
        }
        TableInfo<T, ID> tableInfo = new TableInfo<>(connectionSource, (BaseDaoImpl) null, dataClass);
        return addCreateTableStatements(connectionSource, tableInfo, false);
    }

    public static <T, ID> List<String> getCreateTableStatements(ConnectionSource connectionSource, DatabaseTableConfig<T> tableConfig) throws SQLException {
        Dao<T, ID> dao = DaoManager.createDao(connectionSource, tableConfig);
        if (dao instanceof BaseDaoImpl) {
            return addCreateTableStatements(connectionSource, ((BaseDaoImpl) dao).getTableInfo(), false);
        }
        tableConfig.extractFieldTypes(connectionSource);
        TableInfo<T, ID> tableInfo = new TableInfo<>(connectionSource.getDatabaseType(), (BaseDaoImpl) null, tableConfig);
        return addCreateTableStatements(connectionSource, tableInfo, false);
    }

    public static <T, ID> int dropTable(ConnectionSource connectionSource, Class<T> dataClass, boolean ignoreErrors) throws SQLException {
        DatabaseType databaseType = connectionSource.getDatabaseType();
        Dao<T, ID> dao = DaoManager.createDao(connectionSource, dataClass);
        if (dao instanceof BaseDaoImpl) {
            return doDropTable(databaseType, connectionSource, ((BaseDaoImpl) dao).getTableInfo(), ignoreErrors);
        }
        TableInfo<T, ID> tableInfo = new TableInfo<>(connectionSource, (BaseDaoImpl) null, dataClass);
        return doDropTable(databaseType, connectionSource, tableInfo, ignoreErrors);
    }

    public static <T, ID> int dropTable(ConnectionSource connectionSource, DatabaseTableConfig<T> tableConfig, boolean ignoreErrors) throws SQLException {
        DatabaseType databaseType = connectionSource.getDatabaseType();
        Dao<T, ID> dao = DaoManager.createDao(connectionSource, tableConfig);
        if (dao instanceof BaseDaoImpl) {
            return doDropTable(databaseType, connectionSource, ((BaseDaoImpl) dao).getTableInfo(), ignoreErrors);
        }
        tableConfig.extractFieldTypes(connectionSource);
        TableInfo<T, ID> tableInfo = new TableInfo<>(databaseType, (BaseDaoImpl) null, tableConfig);
        return doDropTable(databaseType, connectionSource, tableInfo, ignoreErrors);
    }

    public static <T> int clearTable(ConnectionSource connectionSource, Class<T> dataClass) throws SQLException {
        String tableName = DatabaseTableConfig.extractTableName(dataClass);
        if (connectionSource.getDatabaseType().isEntityNamesMustBeUpCase()) {
            tableName = tableName.toUpperCase();
        }
        return clearTable(connectionSource, tableName);
    }

    public static <T> int clearTable(ConnectionSource connectionSource, DatabaseTableConfig<T> tableConfig) throws SQLException {
        return clearTable(connectionSource, tableConfig.getTableName());
    }

    private static <T, ID> int createTable(ConnectionSource connectionSource, Class<T> dataClass, boolean ifNotExists) throws SQLException {
        Dao<T, ID> dao = DaoManager.createDao(connectionSource, dataClass);
        if (dao instanceof BaseDaoImpl) {
            return doCreateTable(connectionSource, ((BaseDaoImpl) dao).getTableInfo(), ifNotExists);
        }
        TableInfo<T, ID> tableInfo = new TableInfo<>(connectionSource, (BaseDaoImpl) null, dataClass);
        return doCreateTable(connectionSource, tableInfo, ifNotExists);
    }

    private static <T, ID> int createTable(ConnectionSource connectionSource, DatabaseTableConfig<T> tableConfig, boolean ifNotExists) throws SQLException {
        Dao<T, ID> dao = DaoManager.createDao(connectionSource, tableConfig);
        if (dao instanceof BaseDaoImpl) {
            return doCreateTable(connectionSource, ((BaseDaoImpl) dao).getTableInfo(), ifNotExists);
        }
        tableConfig.extractFieldTypes(connectionSource);
        TableInfo<T, ID> tableInfo = new TableInfo<>(connectionSource.getDatabaseType(), (BaseDaoImpl) null, tableConfig);
        return doCreateTable(connectionSource, tableInfo, ifNotExists);
    }

    private static <T> int clearTable(ConnectionSource connectionSource, String tableName) throws SQLException {
        DatabaseType databaseType = connectionSource.getDatabaseType();
        StringBuilder sb = new StringBuilder(48);
        if (databaseType.isTruncateSupported()) {
            sb.append("TRUNCATE TABLE ");
        } else {
            sb.append("DELETE FROM ");
        }
        databaseType.appendEscapedEntityName(sb, tableName);
        String statement = sb.toString();
        logger.info("clearing table '{}' with '{}", tableName, statement);
        CompiledStatement compiledStmt = null;
        DatabaseConnection connection = connectionSource.getReadWriteConnection();
        try {
            compiledStmt = connection.compileStatement(statement, StatementBuilder.StatementType.EXECUTE, noFieldTypes, -1);
            return compiledStmt.runExecute();
        } finally {
            if (compiledStmt != null) {
                compiledStmt.close();
            }
            connectionSource.releaseConnection(connection);
        }
    }

    private static <T, ID> int doDropTable(DatabaseType databaseType, ConnectionSource connectionSource, TableInfo<T, ID> tableInfo, boolean ignoreErrors) throws SQLException {
        logger.info("dropping table '{}'", tableInfo.getTableName());
        List<String> statements = new ArrayList<>();
        addDropIndexStatements(databaseType, tableInfo, statements);
        addDropTableStatements(databaseType, tableInfo, statements);
        DatabaseConnection connection = connectionSource.getReadWriteConnection();
        try {
            return doStatements(connection, "drop", statements, ignoreErrors, databaseType.isCreateTableReturnsNegative(), false);
        } finally {
            connectionSource.releaseConnection(connection);
        }
    }

    private static <T, ID> void addDropIndexStatements(DatabaseType databaseType, TableInfo<T, ID> tableInfo, List<String> statements) {
        Set<String> indexSet = new HashSet<>();
        FieldType[] arr$ = tableInfo.getFieldTypes();
        for (FieldType fieldType : arr$) {
            String indexName = fieldType.getIndexName();
            if (indexName != null) {
                indexSet.add(indexName);
            }
            String uniqueIndexName = fieldType.getUniqueIndexName();
            if (uniqueIndexName != null) {
                indexSet.add(uniqueIndexName);
            }
        }
        StringBuilder sb = new StringBuilder(48);
        for (String indexName2 : indexSet) {
            logger.info("dropping index '{}' for table '{}", indexName2, tableInfo.getTableName());
            sb.append("DROP INDEX ");
            databaseType.appendEscapedEntityName(sb, indexName2);
            statements.add(sb.toString());
            sb.setLength(0);
        }
    }

    private static <T, ID> void addCreateTableStatements(DatabaseType databaseType, TableInfo<T, ID> tableInfo, List<String> statements, List<String> queriesAfter, boolean ifNotExists) throws SQLException {
        StringBuilder sb = new StringBuilder(256);
        sb.append("CREATE TABLE ");
        if (ifNotExists && databaseType.isCreateIfNotExistsSupported()) {
            sb.append("IF NOT EXISTS ");
        }
        databaseType.appendEscapedEntityName(sb, tableInfo.getTableName());
        sb.append(" (");
        List<String> additionalArgs = new ArrayList<>();
        List<String> statementsBefore = new ArrayList<>();
        List<String> statementsAfter = new ArrayList<>();
        boolean first = true;
        FieldType[] arr$ = tableInfo.getFieldTypes();
        for (FieldType fieldType : arr$) {
            if (!fieldType.isForeignCollection()) {
                if (first) {
                    first = false;
                } else {
                    sb.append(", ");
                }
                String columnDefinition = fieldType.getColumnDefinition();
                if (columnDefinition == null) {
                    databaseType.appendColumnArg(tableInfo.getTableName(), sb, fieldType, additionalArgs, statementsBefore, statementsAfter, queriesAfter);
                } else {
                    databaseType.appendEscapedEntityName(sb, fieldType.getColumnName());
                    sb.append(' ').append(columnDefinition).append(' ');
                }
            }
        }
        databaseType.addPrimaryKeySql(tableInfo.getFieldTypes(), additionalArgs, statementsBefore, statementsAfter, queriesAfter);
        databaseType.addUniqueComboSql(tableInfo.getFieldTypes(), additionalArgs, statementsBefore, statementsAfter, queriesAfter);
        for (String arg : additionalArgs) {
            sb.append(", ").append(arg);
        }
        sb.append(") ");
        databaseType.appendCreateTableSuffix(sb);
        statements.addAll(statementsBefore);
        statements.add(sb.toString());
        statements.addAll(statementsAfter);
        addCreateIndexStatements(databaseType, tableInfo, statements, ifNotExists, false);
        addCreateIndexStatements(databaseType, tableInfo, statements, ifNotExists, true);
    }

    private static <T, ID> void addCreateIndexStatements(DatabaseType databaseType, TableInfo<T, ID> tableInfo, List<String> statements, boolean ifNotExists, boolean unique) {
        String indexName;
        Map<String, List<String>> indexMap = new HashMap<>();
        FieldType[] arr$ = tableInfo.getFieldTypes();
        for (FieldType fieldType : arr$) {
            if (unique) {
                indexName = fieldType.getUniqueIndexName();
            } else {
                indexName = fieldType.getIndexName();
            }
            if (indexName != null) {
                List<String> columnList = indexMap.get(indexName);
                if (columnList == null) {
                    columnList = new ArrayList<>();
                    indexMap.put(indexName, columnList);
                }
                columnList.add(fieldType.getColumnName());
            }
        }
        StringBuilder sb = new StringBuilder(128);
        for (Map.Entry<String, List<String>> indexEntry : indexMap.entrySet()) {
            logger.info("creating index '{}' for table '{}", indexEntry.getKey(), tableInfo.getTableName());
            sb.append("CREATE ");
            if (unique) {
                sb.append("UNIQUE ");
            }
            sb.append("INDEX ");
            if (ifNotExists && databaseType.isCreateIndexIfNotExistsSupported()) {
                sb.append("IF NOT EXISTS ");
            }
            databaseType.appendEscapedEntityName(sb, indexEntry.getKey());
            sb.append(" ON ");
            databaseType.appendEscapedEntityName(sb, tableInfo.getTableName());
            sb.append(" ( ");
            boolean first = true;
            for (String columnName : indexEntry.getValue()) {
                if (first) {
                    first = false;
                } else {
                    sb.append(", ");
                }
                databaseType.appendEscapedEntityName(sb, columnName);
            }
            sb.append(" )");
            statements.add(sb.toString());
            sb.setLength(0);
        }
    }

    private static <T, ID> void addDropTableStatements(DatabaseType databaseType, TableInfo<T, ID> tableInfo, List<String> statements) {
        List<String> statementsBefore = new ArrayList<>();
        List<String> statementsAfter = new ArrayList<>();
        FieldType[] arr$ = tableInfo.getFieldTypes();
        for (FieldType fieldType : arr$) {
            databaseType.dropColumnArg(fieldType, statementsBefore, statementsAfter);
        }
        StringBuilder sb = new StringBuilder(64);
        sb.append("DROP TABLE ");
        databaseType.appendEscapedEntityName(sb, tableInfo.getTableName());
        sb.append(' ');
        statements.addAll(statementsBefore);
        statements.add(sb.toString());
        statements.addAll(statementsAfter);
    }

    private static <T, ID> int doCreateTable(ConnectionSource connectionSource, TableInfo<T, ID> tableInfo, boolean ifNotExists) throws SQLException {
        DatabaseType databaseType = connectionSource.getDatabaseType();
        logger.info("creating table '{}'", tableInfo.getTableName());
        List<String> statements = new ArrayList<>();
        List<String> queriesAfter = new ArrayList<>();
        addCreateTableStatements(databaseType, tableInfo, statements, queriesAfter, ifNotExists);
        DatabaseConnection connection = connectionSource.getReadWriteConnection();
        try {
            int stmtC = doStatements(connection, "create", statements, false, databaseType.isCreateTableReturnsNegative(), databaseType.isCreateTableReturnsZero());
            return stmtC + doCreateTestQueries(connection, databaseType, queriesAfter);
        } finally {
            connectionSource.releaseConnection(connection);
        }
    }

    private static int doStatements(DatabaseConnection connection, String label, Collection<String> statements, boolean ignoreErrors, boolean returnsNegative, boolean expectingZero) throws SQLException {
        int stmtC = 0;
        for (String statement : statements) {
            int rowC = 0;
            CompiledStatement compiledStmt = null;
            try {
                try {
                    compiledStmt = connection.compileStatement(statement, StatementBuilder.StatementType.EXECUTE, noFieldTypes, -1);
                    rowC = compiledStmt.runExecute();
                    logger.info("executed {} table statement changed {} rows: {}", label, Integer.valueOf(rowC), statement);
                } catch (SQLException e) {
                    if (ignoreErrors) {
                        logger.info("ignoring {} error '{}' for statement: {}", label, e, statement);
                        if (compiledStmt != null) {
                            compiledStmt.close();
                        }
                    } else {
                        throw SqlExceptionUtil.create("SQL statement failed: " + statement, e);
                    }
                }
                if (rowC < 0) {
                    if (!returnsNegative) {
                        throw new SQLException("SQL statement " + statement + " updated " + rowC + " rows, we were expecting >= 0");
                    }
                } else if (rowC > 0 && expectingZero) {
                    throw new SQLException("SQL statement updated " + rowC + " rows, we were expecting == 0: " + statement);
                }
                stmtC++;
            } finally {
                if (compiledStmt != null) {
                    compiledStmt.close();
                }
            }
        }
        return stmtC;
    }

    private static int doCreateTestQueries(DatabaseConnection connection, DatabaseType databaseType, List<String> queriesAfter) throws SQLException {
        int stmtC = 0;
        for (String query : queriesAfter) {
            CompiledStatement compiledStmt = null;
            try {
                try {
                    compiledStmt = connection.compileStatement(query, StatementBuilder.StatementType.SELECT, noFieldTypes, -1);
                    DatabaseResults results = compiledStmt.runQuery(null);
                    int rowC = 0;
                    for (boolean isThereMore = results.first(); isThereMore; isThereMore = results.next()) {
                        rowC++;
                    }
                    logger.info("executing create table after-query got {} results: {}", Integer.valueOf(rowC), query);
                    stmtC++;
                } catch (SQLException e) {
                    throw SqlExceptionUtil.create("executing create table after-query failed: " + query, e);
                }
            } finally {
                if (compiledStmt != null) {
                    compiledStmt.close();
                }
            }
        }
        return stmtC;
    }

    private static <T, ID> List<String> addCreateTableStatements(ConnectionSource connectionSource, TableInfo<T, ID> tableInfo, boolean ifNotExists) throws SQLException {
        List<String> statements = new ArrayList<>();
        List<String> queriesAfter = new ArrayList<>();
        addCreateTableStatements(connectionSource.getDatabaseType(), tableInfo, statements, queriesAfter, ifNotExists);
        return statements;
    }
}

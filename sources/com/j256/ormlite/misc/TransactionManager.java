package com.j256.ormlite.misc;

import com.j256.ormlite.db.DatabaseType;
import com.j256.ormlite.logger.Logger;
import com.j256.ormlite.logger.LoggerFactory;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.support.DatabaseConnection;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;
/* loaded from: classes.dex */
public class TransactionManager {
    private static final String SAVE_POINT_PREFIX = "ORMLITE";
    private static final Logger logger = LoggerFactory.getLogger(TransactionManager.class);
    private static AtomicInteger savePointCounter = new AtomicInteger();
    private ConnectionSource connectionSource;

    public TransactionManager() {
    }

    public TransactionManager(ConnectionSource connectionSource) {
        this.connectionSource = connectionSource;
        initialize();
    }

    public void initialize() {
        if (this.connectionSource == null) {
            throw new IllegalStateException("dataSource was not set on " + getClass().getSimpleName());
        }
    }

    public <T> T callInTransaction(Callable<T> callable) throws SQLException {
        return (T) callInTransaction(this.connectionSource, callable);
    }

    public static <T> T callInTransaction(ConnectionSource connectionSource, Callable<T> callable) throws SQLException {
        DatabaseConnection connection = connectionSource.getReadWriteConnection();
        try {
            boolean saved = connectionSource.saveSpecialConnection(connection);
            return (T) callInTransaction(connection, saved, connectionSource.getDatabaseType(), callable);
        } finally {
            connectionSource.clearSpecialConnection(connection);
            connectionSource.releaseConnection(connection);
        }
    }

    public static <T> T callInTransaction(DatabaseConnection connection, DatabaseType databaseType, Callable<T> callable) throws SQLException {
        return (T) callInTransaction(connection, false, databaseType, callable);
    }

    /* JADX WARN: Code restructure failed: missing block: B:5:0x000a, code lost:
        if (r12.isNestedSavePointsSupported() != false) goto L3;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static <T> T callInTransaction(com.j256.ormlite.support.DatabaseConnection r10, boolean r11, com.j256.ormlite.db.DatabaseType r12, java.util.concurrent.Callable<T> r13) throws java.sql.SQLException {
        /*
            r9 = 1
            r0 = 0
            r3 = 0
            r5 = 0
            if (r11 != 0) goto Lc
            boolean r6 = r12.isNestedSavePointsSupported()     // Catch: java.lang.Throwable -> L71
            if (r6 == 0) goto L4d
        Lc:
            boolean r6 = r10.isAutoCommitSupported()     // Catch: java.lang.Throwable -> L71
            if (r6 == 0) goto L24
            boolean r0 = r10.isAutoCommit()     // Catch: java.lang.Throwable -> L71
            if (r0 == 0) goto L24
            r6 = 0
            r10.setAutoCommit(r6)     // Catch: java.lang.Throwable -> L71
            com.j256.ormlite.logger.Logger r6 = com.j256.ormlite.misc.TransactionManager.logger     // Catch: java.lang.Throwable -> L71
            java.lang.String r7 = "had to set auto-commit to false"
            r6.debug(r7)     // Catch: java.lang.Throwable -> L71
        L24:
            java.lang.StringBuilder r6 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> L71
            r6.<init>()     // Catch: java.lang.Throwable -> L71
            java.lang.String r7 = "ORMLITE"
            java.lang.StringBuilder r6 = r6.append(r7)     // Catch: java.lang.Throwable -> L71
            java.util.concurrent.atomic.AtomicInteger r7 = com.j256.ormlite.misc.TransactionManager.savePointCounter     // Catch: java.lang.Throwable -> L71
            int r7 = r7.incrementAndGet()     // Catch: java.lang.Throwable -> L71
            java.lang.StringBuilder r6 = r6.append(r7)     // Catch: java.lang.Throwable -> L71
            java.lang.String r6 = r6.toString()     // Catch: java.lang.Throwable -> L71
            java.sql.Savepoint r5 = r10.setSavePoint(r6)     // Catch: java.lang.Throwable -> L71
            if (r5 != 0) goto L64
            com.j256.ormlite.logger.Logger r6 = com.j256.ormlite.misc.TransactionManager.logger     // Catch: java.lang.Throwable -> L71
            java.lang.String r7 = "started savePoint transaction"
            r6.debug(r7)     // Catch: java.lang.Throwable -> L71
        L4c:
            r3 = 1
        L4d:
            java.lang.Object r4 = r13.call()     // Catch: java.lang.Throwable -> L71 java.sql.SQLException -> L80 java.lang.Exception -> L91
            if (r3 == 0) goto L56
            commit(r10, r5)     // Catch: java.lang.Throwable -> L71 java.sql.SQLException -> L80 java.lang.Exception -> L91
        L56:
            if (r0 == 0) goto L63
            r10.setAutoCommit(r9)
            com.j256.ormlite.logger.Logger r6 = com.j256.ormlite.misc.TransactionManager.logger
            java.lang.String r7 = "restored auto-commit to true"
            r6.debug(r7)
        L63:
            return r4
        L64:
            com.j256.ormlite.logger.Logger r6 = com.j256.ormlite.misc.TransactionManager.logger     // Catch: java.lang.Throwable -> L71
            java.lang.String r7 = "started savePoint transaction {}"
            java.lang.String r8 = r5.getSavepointName()     // Catch: java.lang.Throwable -> L71
            r6.debug(r7, r8)     // Catch: java.lang.Throwable -> L71
            goto L4c
        L71:
            r6 = move-exception
            if (r0 == 0) goto L7f
            r10.setAutoCommit(r9)
            com.j256.ormlite.logger.Logger r7 = com.j256.ormlite.misc.TransactionManager.logger
            java.lang.String r8 = "restored auto-commit to true"
            r7.debug(r8)
        L7f:
            throw r6
        L80:
            r1 = move-exception
            if (r3 == 0) goto L86
            rollBack(r10, r5)     // Catch: java.lang.Throwable -> L71 java.sql.SQLException -> L87
        L86:
            throw r1     // Catch: java.lang.Throwable -> L71
        L87:
            r2 = move-exception
            com.j256.ormlite.logger.Logger r6 = com.j256.ormlite.misc.TransactionManager.logger     // Catch: java.lang.Throwable -> L71
            java.lang.String r7 = "after commit exception, rolling back to save-point also threw exception"
            r6.error(r1, r7)     // Catch: java.lang.Throwable -> L71
            goto L86
        L91:
            r1 = move-exception
            if (r3 == 0) goto L97
            rollBack(r10, r5)     // Catch: java.lang.Throwable -> L71 java.sql.SQLException -> L9f
        L97:
            java.lang.String r6 = "Transaction callable threw non-SQL exception"
            java.sql.SQLException r6 = com.j256.ormlite.misc.SqlExceptionUtil.create(r6, r1)     // Catch: java.lang.Throwable -> L71
            throw r6     // Catch: java.lang.Throwable -> L71
        L9f:
            r2 = move-exception
            com.j256.ormlite.logger.Logger r6 = com.j256.ormlite.misc.TransactionManager.logger     // Catch: java.lang.Throwable -> L71
            java.lang.String r7 = "after commit exception, rolling back to save-point also threw exception"
            r6.error(r1, r7)     // Catch: java.lang.Throwable -> L71
            goto L97
        */
        throw new UnsupportedOperationException("Method not decompiled: com.j256.ormlite.misc.TransactionManager.callInTransaction(com.j256.ormlite.support.DatabaseConnection, boolean, com.j256.ormlite.db.DatabaseType, java.util.concurrent.Callable):java.lang.Object");
    }

    public void setConnectionSource(ConnectionSource connectionSource) {
        this.connectionSource = connectionSource;
    }

    private static void commit(DatabaseConnection connection, Savepoint savePoint) throws SQLException {
        String name = savePoint == null ? null : savePoint.getSavepointName();
        connection.commit(savePoint);
        if (name == null) {
            logger.debug("committed savePoint transaction");
        } else {
            logger.debug("committed savePoint transaction {}", name);
        }
    }

    private static void rollBack(DatabaseConnection connection, Savepoint savePoint) throws SQLException {
        String name = savePoint == null ? null : savePoint.getSavepointName();
        connection.rollback(savePoint);
        if (name == null) {
            logger.debug("rolled back savePoint transaction");
        } else {
            logger.debug("rolled back savePoint transaction {}", name);
        }
    }
}

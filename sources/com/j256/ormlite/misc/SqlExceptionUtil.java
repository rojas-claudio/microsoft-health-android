package com.j256.ormlite.misc;

import java.sql.SQLException;
/* loaded from: classes.dex */
public class SqlExceptionUtil {
    private SqlExceptionUtil() {
    }

    public static SQLException create(String message, Throwable cause) {
        SQLException sqlException = new SQLException(message);
        sqlException.initCause(cause);
        return sqlException;
    }
}

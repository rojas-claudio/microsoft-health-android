package com.j256.ormlite.dao;

import java.sql.SQLException;
/* loaded from: classes.dex */
public interface CloseableWrappedIterable<T> extends CloseableIterable<T> {
    void close() throws SQLException;
}

package com.j256.ormlite.dao;

import java.sql.SQLException;
import java.util.List;
/* loaded from: classes.dex */
public interface GenericRawResults<T> extends CloseableWrappedIterable<T> {
    @Override // com.j256.ormlite.dao.CloseableWrappedIterable
    void close() throws SQLException;

    String[] getColumnNames();

    T getFirstResult() throws SQLException;

    int getNumberColumns();

    List<T> getResults() throws SQLException;
}

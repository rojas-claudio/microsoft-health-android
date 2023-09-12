package com.j256.ormlite.dao;

import java.sql.SQLException;
/* loaded from: classes.dex */
public class CloseableWrappedIterableImpl<T> implements CloseableWrappedIterable<T> {
    private final CloseableIterable<T> iterable;
    private CloseableIterator<T> iterator;

    public CloseableWrappedIterableImpl(CloseableIterable<T> iterable) {
        this.iterable = iterable;
    }

    @Override // java.lang.Iterable
    public CloseableIterator<T> iterator() {
        return closeableIterator();
    }

    @Override // com.j256.ormlite.dao.CloseableIterable
    public CloseableIterator<T> closeableIterator() {
        try {
            close();
        } catch (SQLException e) {
        }
        this.iterator = this.iterable.closeableIterator();
        return this.iterator;
    }

    @Override // com.j256.ormlite.dao.CloseableWrappedIterable
    public void close() throws SQLException {
        if (this.iterator != null) {
            this.iterator.close();
            this.iterator = null;
        }
    }
}

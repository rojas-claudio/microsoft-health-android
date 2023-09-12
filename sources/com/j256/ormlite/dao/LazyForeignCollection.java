package com.j256.ormlite.dao;

import com.j256.ormlite.field.FieldType;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
/* loaded from: classes.dex */
public class LazyForeignCollection<T, ID> extends BaseForeignCollection<T, ID> implements ForeignCollection<T>, Serializable {
    private static final long serialVersionUID = -5460708106909626233L;
    private transient CloseableIterator<T> lastIterator;

    public LazyForeignCollection(Dao<T, ID> dao, Object parent, Object parentId, FieldType foreignFieldType, String orderColumn, boolean orderAscending) {
        super(dao, parent, parentId, foreignFieldType, orderColumn, orderAscending);
    }

    @Override // java.util.Collection, java.lang.Iterable
    public CloseableIterator<T> iterator() {
        return closeableIterator(-1);
    }

    @Override // com.j256.ormlite.dao.ForeignCollection
    public CloseableIterator<T> iterator(int flags) {
        return closeableIterator(flags);
    }

    @Override // com.j256.ormlite.dao.CloseableIterable
    public CloseableIterator<T> closeableIterator() {
        return closeableIterator(-1);
    }

    @Override // com.j256.ormlite.dao.ForeignCollection
    public CloseableIterator<T> closeableIterator(int flags) {
        try {
            return iteratorThrow(flags);
        } catch (SQLException e) {
            throw new IllegalStateException("Could not build lazy iterator for " + this.dao.getDataClass(), e);
        }
    }

    @Override // com.j256.ormlite.dao.ForeignCollection
    public CloseableIterator<T> iteratorThrow() throws SQLException {
        return iteratorThrow(-1);
    }

    @Override // com.j256.ormlite.dao.ForeignCollection
    public CloseableIterator<T> iteratorThrow(int flags) throws SQLException {
        this.lastIterator = seperateIteratorThrow(flags);
        return this.lastIterator;
    }

    @Override // com.j256.ormlite.dao.ForeignCollection
    public CloseableWrappedIterable<T> getWrappedIterable() {
        return getWrappedIterable(-1);
    }

    @Override // com.j256.ormlite.dao.ForeignCollection
    public CloseableWrappedIterable<T> getWrappedIterable(final int flags) {
        return new CloseableWrappedIterableImpl(new CloseableIterable<T>() { // from class: com.j256.ormlite.dao.LazyForeignCollection.1
            @Override // java.lang.Iterable
            public CloseableIterator<T> iterator() {
                return closeableIterator();
            }

            @Override // com.j256.ormlite.dao.CloseableIterable
            public CloseableIterator<T> closeableIterator() {
                try {
                    return LazyForeignCollection.this.seperateIteratorThrow(flags);
                } catch (Exception e) {
                    throw new IllegalStateException("Could not build lazy iterator for " + LazyForeignCollection.this.dao.getDataClass(), e);
                }
            }
        });
    }

    @Override // com.j256.ormlite.dao.ForeignCollection
    public void closeLastIterator() throws SQLException {
        if (this.lastIterator != null) {
            this.lastIterator.close();
            this.lastIterator = null;
        }
    }

    @Override // com.j256.ormlite.dao.ForeignCollection
    public boolean isEager() {
        return false;
    }

    @Override // java.util.Collection
    public int size() {
        CloseableIterator<T> iterator = iterator();
        int sizeC = 0;
        while (iterator.hasNext()) {
            try {
                iterator.moveToNext();
                sizeC++;
            } finally {
                try {
                    iterator.close();
                } catch (SQLException e) {
                }
            }
        }
        return sizeC;
    }

    @Override // java.util.Collection
    public boolean isEmpty() {
        CloseableIterator<T> iterator = iterator();
        try {
            return !iterator.hasNext();
        } finally {
            try {
                iterator.close();
            } catch (SQLException e) {
            }
        }
    }

    @Override // java.util.Collection
    public boolean contains(Object obj) {
        boolean z;
        CloseableIterator<T> iterator = iterator();
        while (true) {
            try {
                if (iterator.hasNext()) {
                    if (iterator.next().equals(obj)) {
                        z = true;
                        try {
                            break;
                        } catch (SQLException e) {
                        }
                    }
                } else {
                    z = false;
                    try {
                        iterator.close();
                        break;
                    } catch (SQLException e2) {
                    }
                }
            } finally {
                try {
                    iterator.close();
                } catch (SQLException e3) {
                }
            }
        }
        return z;
    }

    @Override // java.util.Collection
    public boolean containsAll(Collection<?> collection) {
        Set<Object> leftOvers = new HashSet<>((Collection<? extends Object>) collection);
        CloseableIterator<T> iterator = iterator();
        while (iterator.hasNext()) {
            try {
                leftOvers.remove(iterator.next());
            } finally {
                try {
                    iterator.close();
                } catch (SQLException e) {
                }
            }
        }
        return leftOvers.isEmpty();
    }

    /* JADX WARN: Code restructure failed: missing block: B:7:0x0014, code lost:
        r0.remove();
     */
    /* JADX WARN: Code restructure failed: missing block: B:8:0x0017, code lost:
        r1 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:9:0x0018, code lost:
        r0.close();
     */
    @Override // com.j256.ormlite.dao.BaseForeignCollection, java.util.Collection
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public boolean remove(java.lang.Object r4) {
        /*
            r3 = this;
            com.j256.ormlite.dao.CloseableIterator r0 = r3.iterator()
        L4:
            boolean r1 = r0.hasNext()     // Catch: java.lang.Throwable -> L23
            if (r1 == 0) goto L1c
            java.lang.Object r1 = r0.next()     // Catch: java.lang.Throwable -> L23
            boolean r1 = r1.equals(r4)     // Catch: java.lang.Throwable -> L23
            if (r1 == 0) goto L4
            r0.remove()     // Catch: java.lang.Throwable -> L23
            r1 = 1
            r0.close()     // Catch: java.sql.SQLException -> L28
        L1b:
            return r1
        L1c:
            r1 = 0
            r0.close()     // Catch: java.sql.SQLException -> L21
            goto L1b
        L21:
            r2 = move-exception
            goto L1b
        L23:
            r1 = move-exception
            r0.close()     // Catch: java.sql.SQLException -> L2a
        L27:
            throw r1
        L28:
            r2 = move-exception
            goto L1b
        L2a:
            r2 = move-exception
            goto L27
        */
        throw new UnsupportedOperationException("Method not decompiled: com.j256.ormlite.dao.LazyForeignCollection.remove(java.lang.Object):boolean");
    }

    @Override // com.j256.ormlite.dao.BaseForeignCollection, java.util.Collection
    public boolean removeAll(Collection<?> collection) {
        boolean changed = false;
        CloseableIterator<T> iterator = iterator();
        while (iterator.hasNext()) {
            try {
                if (collection.contains(iterator.next())) {
                    iterator.remove();
                    changed = true;
                }
            } finally {
                try {
                    iterator.close();
                } catch (SQLException e) {
                }
            }
        }
        return changed;
    }

    @Override // java.util.Collection
    public Object[] toArray() {
        List<T> items = new ArrayList<>();
        CloseableIterator<T> iterator = iterator();
        while (iterator.hasNext()) {
            try {
                items.add(iterator.next());
            } finally {
                try {
                    iterator.close();
                } catch (SQLException e) {
                }
            }
        }
        return items.toArray();
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.Collection
    public <E> E[] toArray(E[] array) {
        List<E> items;
        List<E> items2 = null;
        int itemC = 0;
        CloseableIterator<T> iterator = iterator();
        while (true) {
            try {
                items = items2;
                if (iterator.hasNext()) {
                    T next = iterator.next();
                    if (itemC >= array.length) {
                        if (items == null) {
                            items2 = new ArrayList<>();
                            try {
                                for (E arrayData : array) {
                                    items2.add(arrayData);
                                }
                            } catch (Throwable th) {
                                th = th;
                                try {
                                    iterator.close();
                                } catch (SQLException e) {
                                }
                                throw th;
                            }
                        } else {
                            items2 = items;
                        }
                        items2.add(next);
                    } else {
                        array[itemC] = next;
                        items2 = items;
                    }
                    itemC++;
                } else {
                    try {
                        break;
                    } catch (SQLException e2) {
                    }
                }
            } catch (Throwable th2) {
                th = th2;
            }
        }
        iterator.close();
        if (items == null) {
            if (itemC < array.length - 1) {
                array[itemC] = 0;
                return array;
            }
            return array;
        }
        return (E[]) items.toArray(array);
    }

    @Override // com.j256.ormlite.dao.ForeignCollection
    public int updateAll() {
        throw new UnsupportedOperationException("Cannot call updateAll() on a lazy collection.");
    }

    @Override // com.j256.ormlite.dao.ForeignCollection
    public int refreshAll() {
        throw new UnsupportedOperationException("Cannot call updateAll() on a lazy collection.");
    }

    @Override // com.j256.ormlite.dao.ForeignCollection
    public int refreshCollection() {
        return 0;
    }

    @Override // java.util.Collection
    public boolean equals(Object other) {
        return super.equals(other);
    }

    @Override // java.util.Collection
    public int hashCode() {
        return super.hashCode();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public CloseableIterator<T> seperateIteratorThrow(int flags) throws SQLException {
        if (this.dao == null) {
            throw new IllegalStateException("Internal DAO object is null.  Lazy collections cannot be used if they have been deserialized.");
        }
        return this.dao.iterator(getPreparedQuery(), flags);
    }
}

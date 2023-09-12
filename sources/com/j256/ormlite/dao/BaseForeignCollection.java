package com.j256.ormlite.dao;

import com.j256.ormlite.field.FieldType;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.SelectArg;
import com.j256.ormlite.stmt.mapped.MappedPreparedStmt;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.Collection;
/* loaded from: classes.dex */
public abstract class BaseForeignCollection<T, ID> implements ForeignCollection<T>, Serializable {
    private static final long serialVersionUID = -5158840898186237589L;
    protected final transient Dao<T, ID> dao;
    private final transient FieldType foreignFieldType;
    private final transient boolean orderAscending;
    private final transient String orderColumn;
    private final transient Object parent;
    private final transient Object parentId;
    private transient PreparedQuery<T> preparedQuery;

    @Override // java.util.Collection
    public abstract boolean remove(Object obj);

    @Override // java.util.Collection
    public abstract boolean removeAll(Collection<?> collection);

    /* JADX INFO: Access modifiers changed from: protected */
    public BaseForeignCollection(Dao<T, ID> dao, Object parent, Object parentId, FieldType foreignFieldType, String orderColumn, boolean orderAscending) {
        this.dao = dao;
        this.foreignFieldType = foreignFieldType;
        this.parentId = parentId;
        this.orderColumn = orderColumn;
        this.orderAscending = orderAscending;
        this.parent = parent;
    }

    @Override // com.j256.ormlite.dao.ForeignCollection, java.util.Collection
    public boolean add(T data) {
        try {
            return addElement(data);
        } catch (SQLException e) {
            throw new IllegalStateException("Could not create data element in dao", e);
        }
    }

    @Override // java.util.Collection
    public boolean addAll(Collection<? extends T> collection) {
        boolean changed = false;
        for (T data : collection) {
            try {
                if (addElement(data)) {
                    changed = true;
                }
            } catch (SQLException e) {
                throw new IllegalStateException("Could not create data elements in dao", e);
            }
        }
        return changed;
    }

    @Override // java.util.Collection
    public boolean retainAll(Collection<?> collection) {
        if (this.dao == null) {
            return false;
        }
        boolean changed = false;
        CloseableIterator<T> iterator = closeableIterator();
        while (iterator.hasNext()) {
            try {
                T data = iterator.next();
                if (!collection.contains(data)) {
                    iterator.remove();
                    changed = true;
                }
            } catch (Throwable th) {
                try {
                    iterator.close();
                } catch (SQLException e) {
                }
                throw th;
            }
        }
        try {
            iterator.close();
            return changed;
        } catch (SQLException e2) {
            return changed;
        }
    }

    @Override // java.util.Collection
    public void clear() {
        if (this.dao != null) {
            CloseableIterator<T> iterator = closeableIterator();
            while (iterator.hasNext()) {
                try {
                    iterator.next();
                    iterator.remove();
                } catch (Throwable th) {
                    try {
                        iterator.close();
                    } catch (SQLException e) {
                    }
                    throw th;
                }
            }
            try {
                iterator.close();
            } catch (SQLException e2) {
            }
        }
    }

    @Override // com.j256.ormlite.dao.ForeignCollection
    public int update(T data) throws SQLException {
        if (this.dao == null) {
            return 0;
        }
        return this.dao.update((Dao<T, ID>) data);
    }

    @Override // com.j256.ormlite.dao.ForeignCollection
    public int refresh(T data) throws SQLException {
        if (this.dao == null) {
            return 0;
        }
        return this.dao.refresh(data);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public PreparedQuery<T> getPreparedQuery() throws SQLException {
        if (this.dao == null) {
            return null;
        }
        if (this.preparedQuery == null) {
            SelectArg fieldArg = new SelectArg();
            fieldArg.setValue(this.parentId);
            QueryBuilder<T, ID> qb = this.dao.queryBuilder();
            if (this.orderColumn != null) {
                qb.orderBy(this.orderColumn, this.orderAscending);
            }
            this.preparedQuery = qb.where().eq(this.foreignFieldType.getColumnName(), fieldArg).prepare();
            if (this.preparedQuery instanceof MappedPreparedStmt) {
                MappedPreparedStmt<T, Object> mappedStmt = (MappedPreparedStmt) this.preparedQuery;
                mappedStmt.setParentInformation(this.parent, this.parentId);
            }
        }
        return this.preparedQuery;
    }

    private boolean addElement(T data) throws SQLException {
        if (this.dao == null) {
            return false;
        }
        if (this.parent != null && this.foreignFieldType.getFieldValueIfNotDefault(data) == null) {
            this.foreignFieldType.assignField(data, this.parent, true, null);
        }
        this.dao.create(data);
        return true;
    }
}

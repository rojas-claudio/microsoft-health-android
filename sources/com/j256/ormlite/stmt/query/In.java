package com.j256.ormlite.stmt.query;

import com.j256.ormlite.db.DatabaseType;
import com.j256.ormlite.field.FieldType;
import com.j256.ormlite.stmt.ArgumentHolder;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
/* loaded from: classes.dex */
public class In extends BaseComparison {
    private final boolean in;
    private Iterable<?> objects;

    @Override // com.j256.ormlite.stmt.query.BaseComparison, com.j256.ormlite.stmt.query.Clause
    public /* bridge */ /* synthetic */ void appendSql(DatabaseType x0, String x1, StringBuilder x2, List x3) throws SQLException {
        super.appendSql(x0, x1, x2, x3);
    }

    @Override // com.j256.ormlite.stmt.query.BaseComparison, com.j256.ormlite.stmt.query.Comparison
    public /* bridge */ /* synthetic */ String getColumnName() {
        return super.getColumnName();
    }

    @Override // com.j256.ormlite.stmt.query.BaseComparison
    public /* bridge */ /* synthetic */ String toString() {
        return super.toString();
    }

    public In(String columnName, FieldType fieldType, Iterable<?> objects, boolean in) throws SQLException {
        super(columnName, fieldType, null, true);
        this.objects = objects;
        this.in = in;
    }

    public In(String columnName, FieldType fieldType, Object[] objects, boolean in) throws SQLException {
        super(columnName, fieldType, null, true);
        this.objects = Arrays.asList(objects);
        this.in = in;
    }

    @Override // com.j256.ormlite.stmt.query.BaseComparison, com.j256.ormlite.stmt.query.Comparison
    public void appendOperation(StringBuilder sb) {
        if (this.in) {
            sb.append("IN ");
        } else {
            sb.append("NOT IN ");
        }
    }

    @Override // com.j256.ormlite.stmt.query.BaseComparison, com.j256.ormlite.stmt.query.Comparison
    public void appendValue(DatabaseType databaseType, StringBuilder sb, List<ArgumentHolder> columnArgList) throws SQLException {
        sb.append('(');
        boolean first = true;
        for (Object value : this.objects) {
            if (value == null) {
                throw new IllegalArgumentException("one of the IN values for '" + this.columnName + "' is null");
            }
            if (first) {
                first = false;
            } else {
                sb.append(',');
            }
            super.appendArgOrValue(databaseType, this.fieldType, sb, columnArgList, value);
        }
        sb.append(") ");
    }
}

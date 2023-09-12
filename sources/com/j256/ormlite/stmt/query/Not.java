package com.j256.ormlite.stmt.query;

import com.j256.ormlite.db.DatabaseType;
import com.j256.ormlite.stmt.ArgumentHolder;
import java.sql.SQLException;
import java.util.List;
import org.apache.commons.lang3.ClassUtils;
/* loaded from: classes.dex */
public class Not implements Clause, NeedsFutureClause {
    private Comparison comparison = null;
    private Exists exists = null;

    public Not() {
    }

    public Not(Clause clause) {
        setMissingClause(clause);
    }

    @Override // com.j256.ormlite.stmt.query.NeedsFutureClause
    public void setMissingClause(Clause clause) {
        if (this.comparison != null) {
            throw new IllegalArgumentException("NOT operation already has a comparison set");
        }
        if (clause instanceof Comparison) {
            this.comparison = (Comparison) clause;
        } else if (clause instanceof Exists) {
            this.exists = (Exists) clause;
        } else {
            throw new IllegalArgumentException("NOT operation can only work with comparison SQL clauses, not " + clause);
        }
    }

    @Override // com.j256.ormlite.stmt.query.Clause
    public void appendSql(DatabaseType databaseType, String tableName, StringBuilder sb, List<ArgumentHolder> selectArgList) throws SQLException {
        if (this.comparison == null && this.exists == null) {
            throw new IllegalStateException("Clause has not been set in NOT operation");
        }
        if (this.comparison == null) {
            sb.append("(NOT ");
            this.exists.appendSql(databaseType, tableName, sb, selectArgList);
        } else {
            sb.append("(NOT ");
            if (tableName != null) {
                databaseType.appendEscapedEntityName(sb, tableName);
                sb.append(ClassUtils.PACKAGE_SEPARATOR_CHAR);
            }
            databaseType.appendEscapedEntityName(sb, this.comparison.getColumnName());
            sb.append(' ');
            this.comparison.appendOperation(sb);
            this.comparison.appendValue(databaseType, sb, selectArgList);
        }
        sb.append(") ");
    }

    public String toString() {
        return this.comparison == null ? "NOT without comparison" : "NOT comparison " + this.comparison;
    }
}

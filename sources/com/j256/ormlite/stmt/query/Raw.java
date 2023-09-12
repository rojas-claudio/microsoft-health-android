package com.j256.ormlite.stmt.query;

import com.j256.ormlite.db.DatabaseType;
import com.j256.ormlite.stmt.ArgumentHolder;
import java.util.List;
/* loaded from: classes.dex */
public class Raw implements Clause {
    private final ArgumentHolder[] args;
    private final String statement;

    public Raw(String statement, ArgumentHolder[] args) {
        this.statement = statement;
        this.args = args;
    }

    @Override // com.j256.ormlite.stmt.query.Clause
    public void appendSql(DatabaseType databaseType, String tableName, StringBuilder sb, List<ArgumentHolder> argList) {
        sb.append(this.statement);
        sb.append(' ');
        ArgumentHolder[] arr$ = this.args;
        for (ArgumentHolder arg : arr$) {
            argList.add(arg);
        }
    }
}

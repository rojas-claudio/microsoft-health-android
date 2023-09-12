package com.microsoft.kapp.database;

import com.microsoft.kapp.diagnostics.Validate;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
/* loaded from: classes.dex */
public class Projection {
    private Map<String, Integer> mColumns;
    private String[] mProjectedColumns;

    public Projection() {
        this.mColumns = new HashMap();
    }

    public Projection(String... columns) {
        this();
        Validate.notNull(columns, "columns");
        for (String column : columns) {
            addColumn(column);
        }
    }

    public int addColumn(String columnName) {
        Validate.notNullOrEmpty(columnName, "columnName");
        if (this.mColumns.containsKey(columnName)) {
            String message = String.format("The column '%s' already exists.", columnName);
            throw new IllegalArgumentException(message);
        }
        this.mProjectedColumns = null;
        int index = this.mColumns.size();
        this.mColumns.put(columnName, Integer.valueOf(index));
        return index;
    }

    public int getColumnIndex(String columnName) {
        Validate.notNullOrEmpty(columnName, "columnName");
        Integer index = this.mColumns.get(columnName);
        if (index == null) {
            String message = String.format("The column '%s' does not exist.", columnName);
            throw new IllegalArgumentException(message);
        }
        return index.intValue();
    }

    public String[] getColumns() {
        if (this.mProjectedColumns == null) {
            this.mProjectedColumns = new String[this.mColumns.size()];
            for (Map.Entry<String, Integer> entry : this.mColumns.entrySet()) {
                this.mProjectedColumns[entry.getValue().intValue()] = entry.getKey();
            }
        }
        return this.mProjectedColumns;
    }

    public String toString() {
        return StringUtils.join(this.mColumns, ", ");
    }
}

package com.j256.ormlite.stmt;

import com.j256.ormlite.dao.CloseableIterator;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.GenericRawResults;
import com.j256.ormlite.db.DatabaseType;
import com.j256.ormlite.field.FieldType;
import com.j256.ormlite.stmt.StatementBuilder;
import com.j256.ormlite.stmt.query.OrderBy;
import com.j256.ormlite.table.TableInfo;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.commons.lang3.ClassUtils;
/* loaded from: classes.dex */
public class QueryBuilder<T, ID> extends StatementBuilder<T, ID> {
    private boolean distinct;
    private List<String> groupByList;
    private String groupByRaw;
    private String having;
    private final FieldType idField;
    private boolean isCountOfQuery;
    private boolean isInnerQuery;
    private List<QueryBuilder<T, ID>.JoinInfo> joinList;
    private Long limit;
    private Long offset;
    private ArgumentHolder[] orderByArgs;
    private List<OrderBy> orderByList;
    private String orderByRaw;
    private FieldType[] resultFieldTypes;
    private List<String> selectColumnList;
    private boolean selectIdColumn;
    private List<String> selectRawList;

    public QueryBuilder(DatabaseType databaseType, TableInfo<T, ID> tableInfo, Dao<T, ID> dao) {
        super(databaseType, tableInfo, dao, StatementBuilder.StatementType.SELECT);
        this.selectIdColumn = true;
        this.idField = tableInfo.getIdField();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void enableInnerQuery() {
        this.isInnerQuery = true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getSelectColumnCount() {
        if (this.isCountOfQuery) {
            return 1;
        }
        if (this.selectRawList != null && !this.selectRawList.isEmpty()) {
            return this.selectRawList.size();
        }
        if (this.selectColumnList == null) {
            return 0;
        }
        return this.selectColumnList.size();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public List<String> getSelectColumns() {
        if (this.isCountOfQuery) {
            return Collections.singletonList("COUNT(*)");
        }
        if (this.selectRawList != null && !this.selectRawList.isEmpty()) {
            return this.selectRawList;
        }
        if (this.selectColumnList == null) {
            return Collections.emptyList();
        }
        return this.selectColumnList;
    }

    public PreparedQuery<T> prepare() throws SQLException {
        return super.prepareStatement(this.limit);
    }

    public QueryBuilder<T, ID> selectColumns(String... columns) {
        if (this.selectColumnList == null) {
            this.selectColumnList = new ArrayList();
        }
        for (String column : columns) {
            addSelectColumnToList(column);
        }
        return this;
    }

    public QueryBuilder<T, ID> selectColumns(Iterable<String> columns) {
        if (this.selectColumnList == null) {
            this.selectColumnList = new ArrayList();
        }
        for (String column : columns) {
            addSelectColumnToList(column);
        }
        return this;
    }

    public QueryBuilder<T, ID> selectRaw(String... columns) {
        if (this.selectRawList == null) {
            this.selectRawList = new ArrayList();
        }
        for (String column : columns) {
            this.selectRawList.add(column);
        }
        return this;
    }

    public QueryBuilder<T, ID> groupBy(String columnName) {
        FieldType fieldType = verifyColumnName(columnName);
        if (fieldType.isForeignCollection()) {
            throw new IllegalArgumentException("Can't groupBy foreign colletion field: " + columnName);
        }
        if (this.groupByList == null) {
            this.groupByList = new ArrayList();
        }
        this.groupByList.add(columnName);
        this.selectIdColumn = false;
        return this;
    }

    public QueryBuilder<T, ID> groupByRaw(String rawSql) {
        this.groupByRaw = rawSql;
        return this;
    }

    public QueryBuilder<T, ID> orderBy(String columnName, boolean ascending) {
        FieldType fieldType = verifyColumnName(columnName);
        if (fieldType.isForeignCollection()) {
            throw new IllegalArgumentException("Can't orderBy foreign colletion field: " + columnName);
        }
        if (this.orderByList == null) {
            this.orderByList = new ArrayList();
        }
        this.orderByList.add(new OrderBy(columnName, ascending));
        return this;
    }

    public QueryBuilder<T, ID> orderByRaw(String rawSql) {
        return orderByRaw(rawSql, null);
    }

    public QueryBuilder<T, ID> orderByRaw(String rawSql, ArgumentHolder... args) {
        this.orderByRaw = rawSql;
        this.orderByArgs = args;
        return this;
    }

    public QueryBuilder<T, ID> distinct() {
        this.distinct = true;
        this.selectIdColumn = false;
        return this;
    }

    @Deprecated
    public QueryBuilder<T, ID> limit(int maxRows) {
        return limit(Long.valueOf(maxRows));
    }

    public QueryBuilder<T, ID> limit(Long maxRows) {
        this.limit = maxRows;
        return this;
    }

    @Deprecated
    public QueryBuilder<T, ID> offset(int startRow) throws SQLException {
        return offset(Long.valueOf(startRow));
    }

    public QueryBuilder<T, ID> offset(Long startRow) throws SQLException {
        if (this.databaseType.isOffsetSqlSupported()) {
            this.offset = startRow;
            return this;
        }
        throw new SQLException("Offset is not supported by this database");
    }

    public QueryBuilder<T, ID> setCountOf(boolean countOf) {
        this.isCountOfQuery = countOf;
        return this;
    }

    public QueryBuilder<T, ID> having(String having) {
        this.having = having;
        return this;
    }

    public QueryBuilder<T, ID> join(QueryBuilder<?, ?> joinedQueryBuilder) throws SQLException {
        addJoinInfo("INNER", joinedQueryBuilder, StatementBuilder.WhereOperation.AND);
        return this;
    }

    public QueryBuilder<T, ID> joinOr(QueryBuilder<?, ?> joinedQueryBuilder) throws SQLException {
        addJoinInfo("INNER", joinedQueryBuilder, StatementBuilder.WhereOperation.OR);
        return this;
    }

    public QueryBuilder<T, ID> leftJoin(QueryBuilder<?, ?> joinedQueryBuilder) throws SQLException {
        addJoinInfo("LEFT", joinedQueryBuilder, StatementBuilder.WhereOperation.AND);
        return this;
    }

    public QueryBuilder<T, ID> leftJoinOr(QueryBuilder<?, ?> joinedQueryBuilder) throws SQLException {
        addJoinInfo("LEFT", joinedQueryBuilder, StatementBuilder.WhereOperation.OR);
        return this;
    }

    public List<T> query() throws SQLException {
        return this.dao.query(prepare());
    }

    public GenericRawResults<String[]> queryRaw() throws SQLException {
        return this.dao.queryRaw(prepareStatementString(), new String[0]);
    }

    public T queryForFirst() throws SQLException {
        return this.dao.queryForFirst(prepare());
    }

    public String[] queryRawFirst() throws SQLException {
        return this.dao.queryRaw(prepareStatementString(), new String[0]).getFirstResult();
    }

    public CloseableIterator<T> iterator() throws SQLException {
        return this.dao.iterator(prepare());
    }

    public long countOf() throws SQLException {
        setCountOf(true);
        return this.dao.countOf(prepare());
    }

    @Override // com.j256.ormlite.stmt.StatementBuilder
    @Deprecated
    public void clear() {
        reset();
    }

    @Override // com.j256.ormlite.stmt.StatementBuilder
    public void reset() {
        super.reset();
        this.distinct = false;
        this.selectIdColumn = true;
        this.selectColumnList = null;
        this.selectRawList = null;
        this.orderByList = null;
        this.orderByRaw = null;
        this.groupByList = null;
        this.groupByRaw = null;
        this.isInnerQuery = false;
        this.isCountOfQuery = false;
        this.having = null;
        this.limit = null;
        this.offset = null;
        if (this.joinList != null) {
            this.joinList.clear();
            this.joinList = null;
        }
        this.addTableName = false;
    }

    @Override // com.j256.ormlite.stmt.StatementBuilder
    protected void appendStatementStart(StringBuilder sb, List<ArgumentHolder> argList) {
        if (this.joinList == null) {
            setAddTableName(false);
        } else {
            setAddTableName(true);
        }
        sb.append("SELECT ");
        if (this.databaseType.isLimitAfterSelect()) {
            appendLimit(sb);
        }
        if (this.distinct) {
            sb.append("DISTINCT ");
        }
        if (this.isCountOfQuery) {
            this.type = StatementBuilder.StatementType.SELECT_LONG;
            sb.append("COUNT(*) ");
        } else if (this.selectRawList != null && !this.selectRawList.isEmpty()) {
            this.type = StatementBuilder.StatementType.SELECT_RAW;
            appendSelectRaw(sb);
        } else {
            this.type = StatementBuilder.StatementType.SELECT;
            appendColumns(sb);
        }
        sb.append("FROM ");
        this.databaseType.appendEscapedEntityName(sb, this.tableName);
        sb.append(' ');
        if (this.joinList != null) {
            appendJoinSql(sb);
        }
    }

    @Override // com.j256.ormlite.stmt.StatementBuilder
    protected FieldType[] getResultFieldTypes() {
        return this.resultFieldTypes;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.j256.ormlite.stmt.StatementBuilder
    public boolean appendWhereStatement(StringBuilder sb, List<ArgumentHolder> argList, StatementBuilder.WhereOperation operation) throws SQLException {
        StatementBuilder.WhereOperation operation2;
        boolean first = operation == StatementBuilder.WhereOperation.FIRST;
        if (this.where != null) {
            first = super.appendWhereStatement(sb, argList, operation);
        }
        if (this.joinList != null) {
            for (QueryBuilder<T, ID>.JoinInfo joinInfo : this.joinList) {
                if (first) {
                    operation2 = StatementBuilder.WhereOperation.FIRST;
                } else {
                    operation2 = joinInfo.operation;
                }
                first = joinInfo.queryBuilder.appendWhereStatement(sb, argList, operation2);
            }
        }
        return first;
    }

    @Override // com.j256.ormlite.stmt.StatementBuilder
    protected void appendStatementEnd(StringBuilder sb, List<ArgumentHolder> argList) throws SQLException {
        appendGroupBys(sb);
        appendHaving(sb);
        appendOrderBys(sb, argList);
        if (!this.databaseType.isLimitAfterSelect()) {
            appendLimit(sb);
        }
        appendOffset(sb);
        setAddTableName(false);
    }

    @Override // com.j256.ormlite.stmt.StatementBuilder
    protected boolean shouldPrependTableNameToColumns() {
        return this.joinList != null;
    }

    private void setAddTableName(boolean addTableName) {
        this.addTableName = addTableName;
        if (this.joinList != null) {
            for (QueryBuilder<T, ID>.JoinInfo joinInfo : this.joinList) {
                joinInfo.queryBuilder.setAddTableName(addTableName);
            }
        }
    }

    private void addJoinInfo(String type, QueryBuilder<?, ?> joinedQueryBuilder, StatementBuilder.WhereOperation operation) throws SQLException {
        QueryBuilder<T, ID>.JoinInfo joinInfo = new JoinInfo(type, joinedQueryBuilder, operation);
        matchJoinedFields(joinInfo, joinedQueryBuilder);
        if (this.joinList == null) {
            this.joinList = new ArrayList();
        }
        this.joinList.add(joinInfo);
    }

    private void matchJoinedFields(QueryBuilder<T, ID>.JoinInfo joinInfo, QueryBuilder<?, ?> joinedQueryBuilder) throws SQLException {
        FieldType[] arr$ = this.tableInfo.getFieldTypes();
        for (FieldType fieldType : arr$) {
            FieldType foreignIdField = fieldType.getForeignIdField();
            if (fieldType.isForeign() && foreignIdField.equals(joinedQueryBuilder.tableInfo.getIdField())) {
                joinInfo.localField = fieldType;
                joinInfo.remoteField = foreignIdField;
                return;
            }
        }
        FieldType[] arr$2 = joinedQueryBuilder.tableInfo.getFieldTypes();
        for (FieldType fieldType2 : arr$2) {
            if (fieldType2.isForeign() && fieldType2.getForeignIdField().equals(this.idField)) {
                joinInfo.localField = this.idField;
                joinInfo.remoteField = fieldType2;
                return;
            }
        }
        throw new SQLException("Could not find a foreign " + this.tableInfo.getDataClass() + " field in " + joinedQueryBuilder.tableInfo.getDataClass() + " or vice versa");
    }

    private void addSelectColumnToList(String columnName) {
        verifyColumnName(columnName);
        this.selectColumnList.add(columnName);
    }

    private void appendJoinSql(StringBuilder sb) {
        for (QueryBuilder<T, ID>.JoinInfo joinInfo : this.joinList) {
            sb.append(joinInfo.type).append(" JOIN ");
            this.databaseType.appendEscapedEntityName(sb, joinInfo.queryBuilder.tableName);
            sb.append(" ON ");
            this.databaseType.appendEscapedEntityName(sb, this.tableName);
            sb.append(ClassUtils.PACKAGE_SEPARATOR_CHAR);
            this.databaseType.appendEscapedEntityName(sb, joinInfo.localField.getColumnName());
            sb.append(" = ");
            this.databaseType.appendEscapedEntityName(sb, joinInfo.queryBuilder.tableName);
            sb.append(ClassUtils.PACKAGE_SEPARATOR_CHAR);
            this.databaseType.appendEscapedEntityName(sb, joinInfo.remoteField.getColumnName());
            sb.append(' ');
            if (joinInfo.queryBuilder.joinList != null) {
                joinInfo.queryBuilder.appendJoinSql(sb);
            }
        }
    }

    private void appendSelectRaw(StringBuilder sb) {
        boolean first = true;
        for (String column : this.selectRawList) {
            if (first) {
                first = false;
            } else {
                sb.append(", ");
            }
            sb.append(column);
        }
        sb.append(' ');
    }

    private void appendColumns(StringBuilder sb) {
        boolean hasId;
        if (this.selectColumnList == null) {
            if (this.addTableName) {
                this.databaseType.appendEscapedEntityName(sb, this.tableName);
                sb.append(ClassUtils.PACKAGE_SEPARATOR_CHAR);
            }
            sb.append("* ");
            this.resultFieldTypes = this.tableInfo.getFieldTypes();
            return;
        }
        boolean first = true;
        if (this.isInnerQuery) {
            hasId = true;
        } else {
            hasId = false;
        }
        List<FieldType> fieldTypeList = new ArrayList<>(this.selectColumnList.size() + 1);
        for (String columnName : this.selectColumnList) {
            FieldType fieldType = this.tableInfo.getFieldTypeByColumnName(columnName);
            if (fieldType.isForeignCollection()) {
                fieldTypeList.add(fieldType);
            } else {
                if (first) {
                    first = false;
                } else {
                    sb.append(',');
                }
                appendFieldColumnName(sb, fieldType, fieldTypeList);
                if (fieldType == this.idField) {
                    hasId = true;
                }
            }
        }
        if (!hasId && this.selectIdColumn) {
            if (!first) {
                sb.append(',');
            }
            appendFieldColumnName(sb, this.idField, fieldTypeList);
        }
        sb.append(' ');
        this.resultFieldTypes = (FieldType[]) fieldTypeList.toArray(new FieldType[fieldTypeList.size()]);
    }

    private void appendFieldColumnName(StringBuilder sb, FieldType fieldType, List<FieldType> fieldTypeList) {
        appendColumnName(sb, fieldType.getColumnName());
        if (fieldTypeList != null) {
            fieldTypeList.add(fieldType);
        }
    }

    private void appendLimit(StringBuilder sb) {
        if (this.limit != null && this.databaseType.isLimitSqlSupported()) {
            this.databaseType.appendLimitValue(sb, this.limit.longValue(), this.offset);
        }
    }

    private void appendOffset(StringBuilder sb) throws SQLException {
        if (this.offset != null) {
            if (this.databaseType.isOffsetLimitArgument()) {
                if (this.limit == null) {
                    throw new SQLException("If the offset is specified, limit must also be specified with this database");
                }
                return;
            }
            this.databaseType.appendOffsetValue(sb, this.offset.longValue());
        }
    }

    private void appendGroupBys(StringBuilder sb) {
        boolean first = true;
        if (hasGroupStuff()) {
            appendGroupBys(sb, true);
            first = false;
        }
        if (this.joinList != null) {
            for (QueryBuilder<T, ID>.JoinInfo joinInfo : this.joinList) {
                if (joinInfo.queryBuilder != null && joinInfo.queryBuilder.hasGroupStuff()) {
                    joinInfo.queryBuilder.appendGroupBys(sb, first);
                }
            }
        }
    }

    private boolean hasGroupStuff() {
        return ((this.groupByList == null || this.groupByList.isEmpty()) && this.groupByRaw == null) ? false : true;
    }

    private void appendGroupBys(StringBuilder sb, boolean first) {
        if (first) {
            sb.append("GROUP BY ");
        }
        if (this.groupByRaw != null) {
            if (!first) {
                sb.append(',');
            }
            sb.append(this.groupByRaw);
        } else {
            for (String columnName : this.groupByList) {
                if (first) {
                    first = false;
                } else {
                    sb.append(',');
                }
                appendColumnName(sb, columnName);
            }
        }
        sb.append(' ');
    }

    private void appendOrderBys(StringBuilder sb, List<ArgumentHolder> argList) {
        boolean first = true;
        if (hasOrderStuff()) {
            appendOrderBys(sb, true, argList);
            first = false;
        }
        if (this.joinList != null) {
            for (QueryBuilder<T, ID>.JoinInfo joinInfo : this.joinList) {
                if (joinInfo.queryBuilder != null && joinInfo.queryBuilder.hasOrderStuff()) {
                    joinInfo.queryBuilder.appendOrderBys(sb, first, argList);
                }
            }
        }
    }

    private boolean hasOrderStuff() {
        return ((this.orderByList == null || this.orderByList.isEmpty()) && this.orderByRaw == null) ? false : true;
    }

    private void appendOrderBys(StringBuilder sb, boolean first, List<ArgumentHolder> argList) {
        if (first) {
            sb.append("ORDER BY ");
        }
        if (this.orderByRaw != null) {
            if (!first) {
                sb.append(',');
            }
            sb.append(this.orderByRaw);
            if (this.orderByArgs != null) {
                ArgumentHolder[] arr$ = this.orderByArgs;
                for (ArgumentHolder arg : arr$) {
                    argList.add(arg);
                }
            }
            first = false;
        }
        if (this.orderByList != null) {
            for (OrderBy orderBy : this.orderByList) {
                if (first) {
                    first = false;
                } else {
                    sb.append(',');
                }
                appendColumnName(sb, orderBy.getColumnName());
                if (!orderBy.isAscending()) {
                    sb.append(" DESC");
                }
            }
        }
        sb.append(' ');
    }

    private void appendColumnName(StringBuilder sb, String columnName) {
        if (this.addTableName) {
            this.databaseType.appendEscapedEntityName(sb, this.tableName);
            sb.append(ClassUtils.PACKAGE_SEPARATOR_CHAR);
        }
        this.databaseType.appendEscapedEntityName(sb, columnName);
    }

    private void appendHaving(StringBuilder sb) {
        if (this.having != null) {
            sb.append("HAVING ").append(this.having).append(' ');
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class JoinInfo {
        FieldType localField;
        StatementBuilder.WhereOperation operation;
        final QueryBuilder<?, ?> queryBuilder;
        FieldType remoteField;
        final String type;

        public JoinInfo(String type, QueryBuilder<?, ?> queryBuilder, StatementBuilder.WhereOperation operation) {
            this.type = type;
            this.queryBuilder = queryBuilder;
            this.operation = operation;
        }
    }

    /* loaded from: classes.dex */
    public static class InternalQueryBuilderWrapper {
        private final QueryBuilder<?, ?> queryBuilder;

        /* JADX INFO: Access modifiers changed from: package-private */
        public InternalQueryBuilderWrapper(QueryBuilder<?, ?> queryBuilder) {
            this.queryBuilder = queryBuilder;
        }

        public void appendStatementString(StringBuilder sb, List<ArgumentHolder> argList) throws SQLException {
            this.queryBuilder.appendStatementString(sb, argList);
        }

        public FieldType[] getResultFieldTypes() {
            return this.queryBuilder.getResultFieldTypes();
        }
    }
}

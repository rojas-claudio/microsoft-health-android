package com.j256.ormlite.dao;

import com.j256.ormlite.db.DatabaseType;
import com.j256.ormlite.logger.Logger;
import com.j256.ormlite.logger.LoggerFactory;
import com.j256.ormlite.misc.SqlExceptionUtil;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.DatabaseTable;
import com.j256.ormlite.table.DatabaseTableConfig;
import java.lang.reflect.Constructor;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
/* loaded from: classes.dex */
public class DaoManager {
    private static Map<Class<?>, DatabaseTableConfig<?>> configMap = null;
    private static Map<ClassConnectionSource, Dao<?, ?>> classMap = null;
    private static Map<TableConfigConnectionSource, Dao<?, ?>> tableConfigMap = null;
    private static Logger logger = LoggerFactory.getLogger(DaoManager.class);

    public static synchronized <D extends Dao<T, ?>, T> D createDao(ConnectionSource connectionSource, Class<T> clazz) throws SQLException {
        Dao<T, ?> daoTmp;
        Dao<T, ?> dao;
        D castDao;
        synchronized (DaoManager.class) {
            if (connectionSource == null) {
                throw new IllegalArgumentException("connectionSource argument cannot be null");
            }
            ClassConnectionSource key = new ClassConnectionSource(connectionSource, clazz);
            Dao<?, ?> dao2 = lookupDao(key);
            if (dao2 != null) {
                castDao = (D) dao2;
            } else {
                Dao<?, ?> dao3 = (Dao) createDaoFromConfig(connectionSource, clazz);
                if (dao3 != null) {
                    castDao = (D) dao3;
                } else {
                    DatabaseTable databaseTable = (DatabaseTable) clazz.getAnnotation(DatabaseTable.class);
                    if (databaseTable == null || databaseTable.daoClass() == Void.class || databaseTable.daoClass() == BaseDaoImpl.class) {
                        DatabaseType databaseType = connectionSource.getDatabaseType();
                        DatabaseTableConfig<T> config = databaseType.extractDatabaseTableConfig(connectionSource, clazz);
                        if (config == null) {
                            daoTmp = BaseDaoImpl.createDao(connectionSource, clazz);
                        } else {
                            daoTmp = BaseDaoImpl.createDao(connectionSource, config);
                        }
                        dao = daoTmp;
                        logger.debug("created dao for class {} with reflection", clazz);
                    } else {
                        Class<?> daoClass = databaseTable.daoClass();
                        Object[] arguments = {connectionSource, clazz};
                        Constructor<?> daoConstructor = findConstructor(daoClass, arguments);
                        if (daoConstructor == null && (daoConstructor = findConstructor(daoClass, (arguments = new Object[]{connectionSource}))) == null) {
                            throw new SQLException("Could not find public constructor with ConnectionSource and optional Class parameters " + daoClass + ".  Missing static on class?");
                        }
                        try {
                            dao = (Dao) daoConstructor.newInstance(arguments);
                            logger.debug("created dao for class {} from constructor", clazz);
                        } catch (Exception e) {
                            throw SqlExceptionUtil.create("Could not call the constructor in class " + daoClass, e);
                        }
                    }
                    registerDao(connectionSource, dao);
                    castDao = (D) dao;
                }
            }
        }
        return castDao;
    }

    public static synchronized <D extends Dao<T, ?>, T> D lookupDao(ConnectionSource connectionSource, Class<T> clazz) {
        D d;
        synchronized (DaoManager.class) {
            if (connectionSource == null) {
                throw new IllegalArgumentException("connectionSource argument cannot be null");
            }
            ClassConnectionSource key = new ClassConnectionSource(connectionSource, clazz);
            d = (D) lookupDao(key);
        }
        return d;
    }

    public static synchronized <D extends Dao<T, ?>, T> D createDao(ConnectionSource connectionSource, DatabaseTableConfig<T> tableConfig) throws SQLException {
        D d;
        synchronized (DaoManager.class) {
            if (connectionSource == null) {
                throw new IllegalArgumentException("connectionSource argument cannot be null");
            }
            d = (D) doCreateDao(connectionSource, tableConfig);
        }
        return d;
    }

    public static synchronized <D extends Dao<T, ?>, T> D lookupDao(ConnectionSource connectionSource, DatabaseTableConfig<T> tableConfig) {
        D d;
        synchronized (DaoManager.class) {
            if (connectionSource == null) {
                throw new IllegalArgumentException("connectionSource argument cannot be null");
            }
            TableConfigConnectionSource key = new TableConfigConnectionSource(connectionSource, tableConfig);
            Dao<?, ?> dao = lookupDao(key);
            if (dao == null) {
                d = null;
            } else {
                d = (D) dao;
            }
        }
        return d;
    }

    public static synchronized void registerDao(ConnectionSource connectionSource, Dao<?, ?> dao) {
        synchronized (DaoManager.class) {
            if (connectionSource == null) {
                throw new IllegalArgumentException("connectionSource argument cannot be null");
            }
            addDaoToClassMap(new ClassConnectionSource(connectionSource, dao.getDataClass()), dao);
        }
    }

    public static synchronized void unregisterDao(ConnectionSource connectionSource, Dao<?, ?> dao) {
        synchronized (DaoManager.class) {
            if (connectionSource == null) {
                throw new IllegalArgumentException("connectionSource argument cannot be null");
            }
            removeDaoToClassMap(new ClassConnectionSource(connectionSource, dao.getDataClass()), dao);
        }
    }

    public static synchronized void registerDaoWithTableConfig(ConnectionSource connectionSource, Dao<?, ?> dao) {
        DatabaseTableConfig<?> tableConfig;
        synchronized (DaoManager.class) {
            if (connectionSource == null) {
                throw new IllegalArgumentException("connectionSource argument cannot be null");
            }
            if ((dao instanceof BaseDaoImpl) && (tableConfig = ((BaseDaoImpl) dao).getTableConfig()) != null) {
                addDaoToTableMap(new TableConfigConnectionSource(connectionSource, tableConfig), dao);
            } else {
                addDaoToClassMap(new ClassConnectionSource(connectionSource, dao.getDataClass()), dao);
            }
        }
    }

    public static synchronized void clearCache() {
        synchronized (DaoManager.class) {
            if (configMap != null) {
                configMap.clear();
                configMap = null;
            }
            clearDaoCache();
        }
    }

    public static synchronized void clearDaoCache() {
        synchronized (DaoManager.class) {
            if (classMap != null) {
                classMap.clear();
                classMap = null;
            }
            if (tableConfigMap != null) {
                tableConfigMap.clear();
                tableConfigMap = null;
            }
        }
    }

    public static synchronized void addCachedDatabaseConfigs(Collection<DatabaseTableConfig<?>> configs) {
        Map<Class<?>, DatabaseTableConfig<?>> newMap;
        synchronized (DaoManager.class) {
            if (configMap == null) {
                newMap = new HashMap<>();
            } else {
                newMap = new HashMap<>(configMap);
            }
            for (DatabaseTableConfig<?> config : configs) {
                newMap.put(config.getDataClass(), config);
                logger.info("Loaded configuration for {}", config.getDataClass());
            }
            configMap = newMap;
        }
    }

    private static void addDaoToClassMap(ClassConnectionSource key, Dao<?, ?> dao) {
        if (classMap == null) {
            classMap = new HashMap();
        }
        classMap.put(key, dao);
    }

    private static void removeDaoToClassMap(ClassConnectionSource key, Dao<?, ?> dao) {
        if (classMap != null) {
            classMap.remove(key);
        }
    }

    private static void addDaoToTableMap(TableConfigConnectionSource key, Dao<?, ?> dao) {
        if (tableConfigMap == null) {
            tableConfigMap = new HashMap();
        }
        tableConfigMap.put(key, dao);
    }

    private static <T> Dao<?, ?> lookupDao(ClassConnectionSource key) {
        if (classMap == null) {
            classMap = new HashMap();
        }
        Dao<?, ?> dao = classMap.get(key);
        if (dao == null) {
            return null;
        }
        return dao;
    }

    private static <T> Dao<?, ?> lookupDao(TableConfigConnectionSource key) {
        if (tableConfigMap == null) {
            tableConfigMap = new HashMap();
        }
        Dao<?, ?> dao = tableConfigMap.get(key);
        if (dao == null) {
            return null;
        }
        return dao;
    }

    private static Constructor<?> findConstructor(Class<?> daoClass, Object[] params) {
        Constructor<?>[] arr$ = daoClass.getConstructors();
        for (Constructor<?> constructor : arr$) {
            Class<?>[] paramsTypes = constructor.getParameterTypes();
            if (paramsTypes.length == params.length) {
                boolean match = true;
                int i = 0;
                while (true) {
                    if (i < paramsTypes.length) {
                        if (paramsTypes[i].isAssignableFrom(params[i].getClass())) {
                            i++;
                        } else {
                            match = false;
                            break;
                        }
                    } else {
                        break;
                    }
                }
                if (match) {
                    return constructor;
                }
            }
        }
        return null;
    }

    private static <D, T> D createDaoFromConfig(ConnectionSource connectionSource, Class<T> clazz) throws SQLException {
        DatabaseTableConfig<?> databaseTableConfig;
        if (configMap == null || (databaseTableConfig = configMap.get(clazz)) == null) {
            return null;
        }
        return (D) doCreateDao(connectionSource, databaseTableConfig);
    }

    private static <D extends Dao<T, ?>, T> D doCreateDao(ConnectionSource connectionSource, DatabaseTableConfig<T> tableConfig) throws SQLException {
        Dao<T, ?> dao;
        TableConfigConnectionSource tableKey = new TableConfigConnectionSource(connectionSource, tableConfig);
        D d = (D) lookupDao(tableKey);
        if (d != null) {
            return d;
        }
        Class<T> dataClass = tableConfig.getDataClass();
        ClassConnectionSource classKey = new ClassConnectionSource(connectionSource, dataClass);
        Dao<?, ?> dao2 = lookupDao(classKey);
        D d2 = (D) dao2;
        if (d2 != null) {
            addDaoToTableMap(tableKey, d2);
            return d2;
        }
        DatabaseTable databaseTable = (DatabaseTable) tableConfig.getDataClass().getAnnotation(DatabaseTable.class);
        if (databaseTable == null || databaseTable.daoClass() == Void.class || databaseTable.daoClass() == BaseDaoImpl.class) {
            Dao<T, ?> daoTmp = BaseDaoImpl.createDao(connectionSource, tableConfig);
            dao = daoTmp;
        } else {
            Class<?> daoClass = databaseTable.daoClass();
            Object[] arguments = {connectionSource, tableConfig};
            Constructor<?> constructor = findConstructor(daoClass, arguments);
            if (constructor == null) {
                throw new SQLException("Could not find public constructor with ConnectionSource, DatabaseTableConfig parameters in class " + daoClass);
            }
            try {
                dao = (Dao) constructor.newInstance(arguments);
            } catch (Exception e) {
                throw SqlExceptionUtil.create("Could not call the constructor in class " + daoClass, e);
            }
        }
        addDaoToTableMap(tableKey, dao);
        logger.debug("created dao for class {} from table config", dataClass);
        if (lookupDao(classKey) == null) {
            addDaoToClassMap(classKey, dao);
        }
        D castDao = (D) dao;
        return castDao;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class ClassConnectionSource {
        Class<?> clazz;
        ConnectionSource connectionSource;

        public ClassConnectionSource(ConnectionSource connectionSource, Class<?> clazz) {
            this.connectionSource = connectionSource;
            this.clazz = clazz;
        }

        public int hashCode() {
            int result = this.clazz.hashCode() + 31;
            return (result * 31) + this.connectionSource.hashCode();
        }

        public boolean equals(Object obj) {
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            ClassConnectionSource other = (ClassConnectionSource) obj;
            return this.clazz.equals(other.clazz) && this.connectionSource.equals(other.connectionSource);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class TableConfigConnectionSource {
        ConnectionSource connectionSource;
        DatabaseTableConfig<?> tableConfig;

        public TableConfigConnectionSource(ConnectionSource connectionSource, DatabaseTableConfig<?> tableConfig) {
            this.connectionSource = connectionSource;
            this.tableConfig = tableConfig;
        }

        public int hashCode() {
            int result = this.tableConfig.hashCode() + 31;
            return (result * 31) + this.connectionSource.hashCode();
        }

        public boolean equals(Object obj) {
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            TableConfigConnectionSource other = (TableConfigConnectionSource) obj;
            return this.tableConfig.equals(other.tableConfig) && this.connectionSource.equals(other.connectionSource);
        }
    }
}

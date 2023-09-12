package com.j256.ormlite.android.apptools;

import android.content.Context;
import android.content.res.Resources;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.logger.Logger;
import com.j256.ormlite.logger.LoggerFactory;
import java.lang.reflect.Constructor;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
/* loaded from: classes.dex */
public class OpenHelperManager {
    private static final String HELPER_CLASS_RESOURCE_NAME = "open_helper_classname";
    private static Logger logger = LoggerFactory.getLogger(OpenHelperManager.class);
    private static Class<? extends OrmLiteSqliteOpenHelper> helperClass = null;
    private static volatile OrmLiteSqliteOpenHelper helper = null;
    private static boolean wasClosed = false;
    private static int instanceCount = 0;

    public static synchronized void setOpenHelperClass(Class<? extends OrmLiteSqliteOpenHelper> openHelperClass) {
        synchronized (OpenHelperManager.class) {
            if (openHelperClass == null) {
                helperClass = null;
            } else {
                innerSetHelperClass(openHelperClass);
            }
        }
    }

    public static synchronized void setHelper(OrmLiteSqliteOpenHelper helper2) {
        synchronized (OpenHelperManager.class) {
            helper = helper2;
        }
    }

    public static synchronized <T extends OrmLiteSqliteOpenHelper> T getHelper(Context context, Class<T> openHelperClass) {
        T t;
        synchronized (OpenHelperManager.class) {
            if (openHelperClass == null) {
                throw new IllegalArgumentException("openHelperClass argument is null");
            }
            innerSetHelperClass(openHelperClass);
            t = (T) loadHelper(context, openHelperClass);
        }
        return t;
    }

    @Deprecated
    public static synchronized OrmLiteSqliteOpenHelper getHelper(Context context) {
        OrmLiteSqliteOpenHelper loadHelper;
        synchronized (OpenHelperManager.class) {
            if (helperClass == null) {
                if (context == null) {
                    throw new IllegalArgumentException("context argument is null");
                }
                Context appContext = context.getApplicationContext();
                innerSetHelperClass(lookupHelperClass(appContext, context.getClass()));
            }
            loadHelper = loadHelper(context, helperClass);
        }
        return loadHelper;
    }

    @Deprecated
    public static void release() {
        releaseHelper();
    }

    public static synchronized void releaseHelper() {
        synchronized (OpenHelperManager.class) {
            instanceCount--;
            logger.trace("releasing helper {}, instance count = {}", helper, Integer.valueOf(instanceCount));
            if (instanceCount <= 0) {
                if (helper != null) {
                    logger.trace("zero instances, closing helper {}", helper);
                    helper.close();
                    helper = null;
                    wasClosed = true;
                }
                if (instanceCount < 0) {
                    logger.error("too many calls to release helper, instance count = {}", Integer.valueOf(instanceCount));
                }
            }
        }
    }

    private static void innerSetHelperClass(Class<? extends OrmLiteSqliteOpenHelper> openHelperClass) {
        if (openHelperClass == null) {
            throw new IllegalStateException("Helper class was trying to be reset to null");
        }
        if (helperClass == null) {
            helperClass = openHelperClass;
        } else if (helperClass != openHelperClass) {
            throw new IllegalStateException("Helper class was " + helperClass + " but is trying to be reset to " + openHelperClass);
        }
    }

    private static <T extends OrmLiteSqliteOpenHelper> T loadHelper(Context context, Class<T> openHelperClass) {
        if (helper == null) {
            if (wasClosed) {
                logger.info("helper was already closed and is being re-opened");
            }
            if (context == null) {
                throw new IllegalArgumentException("context argument is null");
            }
            Context appContext = context.getApplicationContext();
            helper = constructHelper(appContext, openHelperClass);
            logger.trace("zero instances, created helper {}", helper);
            BaseDaoImpl.clearAllInternalObjectCaches();
            DaoManager.clearDaoCache();
            instanceCount = 0;
        }
        instanceCount++;
        logger.trace("returning helper {}, instance count = {} ", helper, Integer.valueOf(instanceCount));
        T castHelper = (T) helper;
        return castHelper;
    }

    private static OrmLiteSqliteOpenHelper constructHelper(Context context, Class<? extends OrmLiteSqliteOpenHelper> openHelperClass) {
        try {
            Constructor<?> constructor = openHelperClass.getConstructor(Context.class);
            try {
                return constructor.newInstance(context);
            } catch (Exception e) {
                throw new IllegalStateException("Could not construct instance of helper class " + openHelperClass, e);
            }
        } catch (Exception e2) {
            throw new IllegalStateException("Could not find public constructor that has a single (Context) argument for helper class " + openHelperClass, e2);
        }
    }

    private static Class<? extends OrmLiteSqliteOpenHelper> lookupHelperClass(Context context, Class<?> componentClass) {
        Type[] types;
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier(HELPER_CLASS_RESOURCE_NAME, "string", context.getPackageName());
        if (resourceId != 0) {
            String className = resources.getString(resourceId);
            try {
                return Class.forName(className);
            } catch (Exception e) {
                throw new IllegalStateException("Could not create helper instance for class " + className, e);
            }
        }
        for (Class<?> componentClassWalk = componentClass; componentClassWalk != null; componentClassWalk = componentClassWalk.getSuperclass()) {
            Type superType = componentClassWalk.getGenericSuperclass();
            if (superType != null && (superType instanceof ParameterizedType) && (types = ((ParameterizedType) superType).getActualTypeArguments()) != null && types.length != 0) {
                for (Type type : types) {
                    if (type instanceof Class) {
                        Class<? extends OrmLiteSqliteOpenHelper> cls = (Class) type;
                        if (OrmLiteSqliteOpenHelper.class.isAssignableFrom(cls)) {
                            return cls;
                        }
                    }
                }
                continue;
            }
        }
        throw new IllegalStateException("Could not find OpenHelperClass because none of the generic parameters of class " + componentClass + " extends OrmLiteSqliteOpenHelper.  You should use getHelper(Context, Class) instead.");
    }
}

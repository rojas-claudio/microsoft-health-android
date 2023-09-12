package org.apache.commons.lang3.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ClassUtils;
/* loaded from: classes.dex */
public class ConstructorUtils {
    public static <T> T invokeConstructor(Class<T> cls, Object... args) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        if (args == null) {
            args = ArrayUtils.EMPTY_OBJECT_ARRAY;
        }
        Class<?>[] parameterTypes = new Class[args.length];
        for (int i = 0; i < args.length; i++) {
            parameterTypes[i] = args[i].getClass();
        }
        return (T) invokeConstructor(cls, args, parameterTypes);
    }

    public static <T> T invokeConstructor(Class<T> cls, Object[] args, Class<?>[] parameterTypes) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        if (parameterTypes == null) {
            parameterTypes = ArrayUtils.EMPTY_CLASS_ARRAY;
        }
        if (args == null) {
            args = ArrayUtils.EMPTY_OBJECT_ARRAY;
        }
        Constructor<T> ctor = getMatchingAccessibleConstructor(cls, parameterTypes);
        if (ctor == null) {
            throw new NoSuchMethodException("No such accessible constructor on object: " + cls.getName());
        }
        return ctor.newInstance(args);
    }

    public static <T> T invokeExactConstructor(Class<T> cls, Object... args) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        if (args == null) {
            args = ArrayUtils.EMPTY_OBJECT_ARRAY;
        }
        int arguments = args.length;
        Class<?>[] parameterTypes = new Class[arguments];
        for (int i = 0; i < arguments; i++) {
            parameterTypes[i] = args[i].getClass();
        }
        return (T) invokeExactConstructor(cls, args, parameterTypes);
    }

    public static <T> T invokeExactConstructor(Class<T> cls, Object[] args, Class<?>[] parameterTypes) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        if (args == null) {
            args = ArrayUtils.EMPTY_OBJECT_ARRAY;
        }
        if (parameterTypes == null) {
            parameterTypes = ArrayUtils.EMPTY_CLASS_ARRAY;
        }
        Constructor<T> ctor = getAccessibleConstructor(cls, parameterTypes);
        if (ctor == null) {
            throw new NoSuchMethodException("No such accessible constructor on object: " + cls.getName());
        }
        return ctor.newInstance(args);
    }

    public static <T> Constructor<T> getAccessibleConstructor(Class<T> cls, Class<?>... parameterTypes) {
        try {
            return getAccessibleConstructor(cls.getConstructor(parameterTypes));
        } catch (NoSuchMethodException e) {
            return null;
        }
    }

    public static <T> Constructor<T> getAccessibleConstructor(Constructor<T> ctor) {
        if (MemberUtils.isAccessible(ctor) && Modifier.isPublic(ctor.getDeclaringClass().getModifiers())) {
            return ctor;
        }
        return null;
    }

    public static <T> Constructor<T> getMatchingAccessibleConstructor(Class<T> cls, Class<?>... parameterTypes) {
        Constructor<T> accessibleConstructor;
        try {
            Constructor<T> ctor = cls.getConstructor(parameterTypes);
            MemberUtils.setAccessibleWorkaround(ctor);
            return ctor;
        } catch (NoSuchMethodException e) {
            Constructor<T> result = null;
            Constructor<?>[] ctors = cls.getConstructors();
            for (Constructor<?> ctor2 : ctors) {
                if (ClassUtils.isAssignable(parameterTypes, ctor2.getParameterTypes(), true) && (accessibleConstructor = getAccessibleConstructor(ctor2)) != null) {
                    MemberUtils.setAccessibleWorkaround(accessibleConstructor);
                    if (result == null || MemberUtils.compareParameterTypes(accessibleConstructor.getParameterTypes(), result.getParameterTypes(), parameterTypes) < 0) {
                        result = accessibleConstructor;
                    }
                }
            }
            return result;
        }
    }
}

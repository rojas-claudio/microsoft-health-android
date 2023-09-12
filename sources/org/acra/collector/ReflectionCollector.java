package org.acra.collector;

import com.j256.ormlite.stmt.query.SimpleComparison;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.apache.commons.lang3.ClassUtils;
/* loaded from: classes.dex */
final class ReflectionCollector {
    ReflectionCollector() {
    }

    public static String collectConstants(Class<?> someClass, String prefix) {
        StringBuilder result = new StringBuilder();
        Field[] fields = someClass.getFields();
        for (Field field : fields) {
            if (prefix != null && prefix.length() > 0) {
                result.append(prefix).append(ClassUtils.PACKAGE_SEPARATOR_CHAR);
            }
            result.append(field.getName()).append(SimpleComparison.EQUAL_TO_OPERATION);
            try {
                result.append(field.get(null).toString());
            } catch (IllegalAccessException e) {
                result.append("N/A");
            } catch (IllegalArgumentException e2) {
                result.append("N/A");
            }
            result.append("\n");
        }
        return result.toString();
    }

    public static String collectStaticGettersResults(Class<?> someClass) {
        StringBuilder result = new StringBuilder();
        Method[] methods = someClass.getMethods();
        for (Method method : methods) {
            if (method.getParameterTypes().length == 0 && ((method.getName().startsWith("get") || method.getName().startsWith("is")) && !method.getName().equals("getClass"))) {
                try {
                    result.append(method.getName());
                    result.append('=');
                    result.append(method.invoke(null, null));
                    result.append("\n");
                } catch (IllegalAccessException e) {
                } catch (IllegalArgumentException e2) {
                } catch (InvocationTargetException e3) {
                }
            }
        }
        return result.toString();
    }

    public static String collectConstants(Class<?> someClass) {
        return collectConstants(someClass, "");
    }
}

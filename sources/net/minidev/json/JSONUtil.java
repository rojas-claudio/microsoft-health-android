package net.minidev.json;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import net.minidev.asm.FieldFilter;
import net.minidev.json.annotate.JsonIgnore;
/* loaded from: classes.dex */
public class JSONUtil {
    public static final JsonSmartFieldFilter JSON_SMART_FIELD_FILTER = new JsonSmartFieldFilter();

    public static Object convertToStrict(Object obj, Class<?> dest) {
        if (obj == null) {
            return null;
        }
        if (!dest.isAssignableFrom(obj.getClass())) {
            if (dest.isPrimitive()) {
                if (dest == Integer.TYPE) {
                    if (obj instanceof Number) {
                        return Integer.valueOf(((Number) obj).intValue());
                    }
                    return Integer.valueOf(obj.toString());
                } else if (dest == Short.TYPE) {
                    if (obj instanceof Number) {
                        return Short.valueOf(((Number) obj).shortValue());
                    }
                    return Short.valueOf(obj.toString());
                } else if (dest == Long.TYPE) {
                    if (obj instanceof Number) {
                        return Long.valueOf(((Number) obj).longValue());
                    }
                    return Long.valueOf(obj.toString());
                } else if (dest == Byte.TYPE) {
                    if (obj instanceof Number) {
                        return Byte.valueOf(((Number) obj).byteValue());
                    }
                    return Byte.valueOf(obj.toString());
                } else if (dest == Float.TYPE) {
                    if (obj instanceof Number) {
                        return Float.valueOf(((Number) obj).floatValue());
                    }
                    return Float.valueOf(obj.toString());
                } else if (dest == Double.TYPE) {
                    if (obj instanceof Number) {
                        return Double.valueOf(((Number) obj).doubleValue());
                    }
                    return Double.valueOf(obj.toString());
                } else {
                    if (dest == Character.TYPE) {
                        String asString = dest.toString();
                        if (asString.length() > 0) {
                            return Character.valueOf(asString.charAt(0));
                        }
                    } else if (dest == Boolean.TYPE) {
                        return (Boolean) obj;
                    }
                    throw new RuntimeException("Primitive: Can not convert " + obj.getClass().getName() + " to " + dest.getName());
                }
            } else if (dest.isEnum()) {
                return Enum.valueOf(dest, obj.toString());
            } else {
                if (dest == Integer.class) {
                    if (obj instanceof Number) {
                        return Integer.valueOf(((Number) obj).intValue());
                    }
                    return Integer.valueOf(obj.toString());
                } else if (dest == Long.class) {
                    if (obj instanceof Number) {
                        return Long.valueOf(((Number) obj).longValue());
                    }
                    return Long.valueOf(obj.toString());
                } else if (dest == Short.class) {
                    if (obj instanceof Number) {
                        return Short.valueOf(((Number) obj).shortValue());
                    }
                    return Short.valueOf(obj.toString());
                } else if (dest == Byte.class) {
                    if (obj instanceof Number) {
                        return Byte.valueOf(((Number) obj).byteValue());
                    }
                    return Byte.valueOf(obj.toString());
                } else if (dest == Float.class) {
                    if (obj instanceof Number) {
                        return Float.valueOf(((Number) obj).floatValue());
                    }
                    return Float.valueOf(obj.toString());
                } else if (dest == Double.class) {
                    if (obj instanceof Number) {
                        return Double.valueOf(((Number) obj).doubleValue());
                    }
                    return Double.valueOf(obj.toString());
                } else {
                    if (dest == Character.class) {
                        String asString2 = dest.toString();
                        if (asString2.length() > 0) {
                            return Character.valueOf(asString2.charAt(0));
                        }
                    }
                    throw new RuntimeException("Object: Can not Convert " + obj.getClass().getName() + " to " + dest.getName());
                }
            }
        }
        return obj;
    }

    public static Object convertToX(Object obj, Class<?> dest) {
        if (obj == null) {
            return null;
        }
        if (!dest.isAssignableFrom(obj.getClass())) {
            if (dest.isPrimitive()) {
                if (!(obj instanceof Number)) {
                    if (dest == Integer.TYPE) {
                        return Integer.valueOf(obj.toString());
                    }
                    if (dest == Short.TYPE) {
                        return Short.valueOf(obj.toString());
                    }
                    if (dest == Long.TYPE) {
                        return Long.valueOf(obj.toString());
                    }
                    if (dest == Byte.TYPE) {
                        return Byte.valueOf(obj.toString());
                    }
                    if (dest == Float.TYPE) {
                        return Float.valueOf(obj.toString());
                    }
                    if (dest == Double.TYPE) {
                        return Double.valueOf(obj.toString());
                    }
                    if (dest == Character.TYPE) {
                        String asString = dest.toString();
                        if (asString.length() > 0) {
                            return Character.valueOf(asString.charAt(0));
                        }
                    } else if (dest == Boolean.TYPE) {
                        return (Boolean) obj;
                    }
                    throw new RuntimeException("Primitive: Can not convert " + obj.getClass().getName() + " to " + dest.getName());
                }
                return obj;
            } else if (dest.isEnum()) {
                return Enum.valueOf(dest, obj.toString());
            } else {
                if (dest == Integer.class) {
                    if (obj instanceof Number) {
                        return Integer.valueOf(((Number) obj).intValue());
                    }
                    return Integer.valueOf(obj.toString());
                } else if (dest == Long.class) {
                    if (obj instanceof Number) {
                        return Long.valueOf(((Number) obj).longValue());
                    }
                    return Long.valueOf(obj.toString());
                } else if (dest == Short.class) {
                    if (obj instanceof Number) {
                        return Short.valueOf(((Number) obj).shortValue());
                    }
                    return Short.valueOf(obj.toString());
                } else if (dest == Byte.class) {
                    if (obj instanceof Number) {
                        return Byte.valueOf(((Number) obj).byteValue());
                    }
                    return Byte.valueOf(obj.toString());
                } else if (dest == Float.class) {
                    if (obj instanceof Number) {
                        return Float.valueOf(((Number) obj).floatValue());
                    }
                    return Float.valueOf(obj.toString());
                } else if (dest == Double.class) {
                    if (obj instanceof Number) {
                        return Double.valueOf(((Number) obj).doubleValue());
                    }
                    return Double.valueOf(obj.toString());
                } else {
                    if (dest == Character.class) {
                        String asString2 = dest.toString();
                        if (asString2.length() > 0) {
                            return Character.valueOf(asString2.charAt(0));
                        }
                    }
                    throw new RuntimeException("Object: Can not Convert " + obj.getClass().getName() + " to " + dest.getName());
                }
            }
        }
        return obj;
    }

    /* loaded from: classes.dex */
    public static class JsonSmartFieldFilter implements FieldFilter {
        @Override // net.minidev.asm.FieldFilter
        public boolean canUse(Field field) {
            JsonIgnore ignore = (JsonIgnore) field.getAnnotation(JsonIgnore.class);
            return ignore == null || !ignore.value();
        }

        @Override // net.minidev.asm.FieldFilter
        public boolean canUse(Field field, Method method) {
            JsonIgnore ignore = (JsonIgnore) method.getAnnotation(JsonIgnore.class);
            return ignore == null || !ignore.value();
        }

        @Override // net.minidev.asm.FieldFilter
        public boolean canRead(Field field) {
            return true;
        }

        @Override // net.minidev.asm.FieldFilter
        public boolean canWrite(Field field) {
            return true;
        }
    }

    public static String getSetterName(String key) {
        int len = key.length();
        char[] b = new char[len + 3];
        b[0] = 's';
        b[1] = 'e';
        b[2] = 't';
        char c = key.charAt(0);
        if (c >= 'a' && c <= 'z') {
            c = (char) (c - ' ');
        }
        b[3] = c;
        for (int i = 1; i < len; i++) {
            b[i + 3] = key.charAt(i);
        }
        return new String(b);
    }

    public static String getGetterName(String key) {
        int len = key.length();
        char[] b = new char[len + 3];
        b[0] = 'g';
        b[1] = 'e';
        b[2] = 't';
        char c = key.charAt(0);
        if (c >= 'a' && c <= 'z') {
            c = (char) (c - ' ');
        }
        b[3] = c;
        for (int i = 1; i < len; i++) {
            b[i + 3] = key.charAt(i);
        }
        return new String(b);
    }

    public static String getIsName(String key) {
        int len = key.length();
        char[] b = new char[len + 2];
        b[0] = 'i';
        b[1] = 's';
        char c = key.charAt(0);
        if (c >= 'a' && c <= 'z') {
            c = (char) (c - ' ');
        }
        b[2] = c;
        for (int i = 1; i < len; i++) {
            b[i + 2] = key.charAt(i);
        }
        return new String(b);
    }
}

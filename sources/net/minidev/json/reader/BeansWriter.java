package net.minidev.json.reader;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import net.minidev.json.JSONStyle;
import net.minidev.json.JSONUtil;
/* loaded from: classes.dex */
public class BeansWriter implements JsonWriterI<Object> {
    @Override // net.minidev.json.reader.JsonWriterI
    public <E> void writeJSONString(E value, Appendable out, JSONStyle compression) throws IOException {
        Object v;
        Class<?> c2;
        try {
            boolean needSep = false;
            compression.objectStart(out);
            for (Class<?> nextClass = value.getClass(); nextClass != Object.class; nextClass = nextClass.getSuperclass()) {
                Field[] fields = nextClass.getDeclaredFields();
                for (Field field : fields) {
                    int m = field.getModifiers();
                    if ((m & 152) <= 0) {
                        if ((m & 1) > 0) {
                            v = field.get(value);
                        } else {
                            String g = JSONUtil.getGetterName(field.getName());
                            Method mtd = null;
                            try {
                                mtd = nextClass.getDeclaredMethod(g, new Class[0]);
                            } catch (Exception e) {
                            }
                            if (mtd == null && ((c2 = field.getType()) == Boolean.TYPE || c2 == Boolean.class)) {
                                String g2 = JSONUtil.getIsName(field.getName());
                                mtd = nextClass.getDeclaredMethod(g2, new Class[0]);
                            }
                            if (mtd != null) {
                                v = mtd.invoke(value, new Object[0]);
                            }
                        }
                        if (v != null || !compression.ignoreNull()) {
                            if (needSep) {
                                compression.objectNext(out);
                            } else {
                                needSep = true;
                            }
                            String key = field.getName();
                            JsonWriter.writeJSONKV(key, v, out, compression);
                        }
                    }
                }
            }
            compression.objectStop(out);
        } catch (Exception e2) {
            throw new RuntimeException(e2);
        }
    }
}

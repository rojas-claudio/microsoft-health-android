package net.minidev.asm;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
/* loaded from: classes.dex */
public class Accessor {
    protected Field field;
    protected String fieldName;
    protected Type genericType;
    protected Method getter;
    protected int index;
    protected Method setter;
    protected Class<?> type;

    public int getIndex() {
        return this.index;
    }

    public boolean isPublic() {
        return this.setter == null;
    }

    public boolean isEnum() {
        return this.type.isEnum();
    }

    public String getName() {
        return this.fieldName;
    }

    public Class<?> getType() {
        return this.type;
    }

    public Type getGenericType() {
        return this.genericType;
    }

    public boolean isUsable() {
        return (this.field == null && this.getter == null && this.setter == null) ? false : true;
    }

    public boolean isReadable() {
        return (this.field == null && this.getter == null) ? false : true;
    }

    public boolean isWritable() {
        return (this.field == null && this.getter == null) ? false : true;
    }

    public Accessor(Class<?> c, Field field, FieldFilter filter) {
        String name;
        this.fieldName = field.getName();
        int m = field.getModifiers();
        if ((m & 136) <= 0) {
            if ((m & 1) > 0) {
                this.field = field;
            }
            String name2 = ASMUtil.getSetterName(field.getName());
            try {
                this.setter = c.getDeclaredMethod(name2, field.getType());
            } catch (Exception e) {
            }
            boolean isBool = field.getType().equals(Boolean.TYPE);
            if (isBool) {
                name = ASMUtil.getIsName(field.getName());
            } else {
                name = ASMUtil.getGetterName(field.getName());
            }
            try {
                this.getter = c.getDeclaredMethod(name, new Class[0]);
            } catch (Exception e2) {
            }
            if (this.getter == null && isBool) {
                try {
                    this.getter = c.getDeclaredMethod(ASMUtil.getGetterName(field.getName()), new Class[0]);
                } catch (Exception e3) {
                }
            }
            if (this.field != null || this.getter != null || this.setter != null) {
                if (this.getter != null && !filter.canUse(field, this.getter)) {
                    this.getter = null;
                }
                if (this.setter != null && !filter.canUse(field, this.setter)) {
                    this.setter = null;
                }
                if (this.getter != null || this.setter != null || this.field != null) {
                    this.type = field.getType();
                    this.genericType = field.getGenericType();
                }
            }
        }
    }
}

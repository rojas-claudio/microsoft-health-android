package net.minidev.asm;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import net.minidev.asm.ex.NoSuchFieldException;
/* loaded from: classes.dex */
public abstract class BeansAccess<T> {
    private static ConcurrentHashMap<Class<?>, BeansAccess<?>> cache = new ConcurrentHashMap<>();
    private Accessor[] accs;
    private HashMap<String, Accessor> map;

    public abstract Object get(T t, int i);

    public abstract T newInstance();

    public abstract void set(T t, int i, Object obj);

    protected void setAccessor(Accessor[] accs) {
        this.accs = accs;
        this.map = new HashMap<>();
        int len$ = accs.length;
        int i$ = 0;
        int i = 0;
        while (i$ < len$) {
            Accessor acc = accs[i$];
            acc.index = i;
            this.map.put(acc.getName(), acc);
            i$++;
            i++;
        }
    }

    public HashMap<String, Accessor> getMap() {
        return this.map;
    }

    public Accessor[] getAccessors() {
        return this.accs;
    }

    public static <P> BeansAccess<P> get(Class<P> type) {
        return get(type, (FieldFilter) null);
    }

    public static <P> BeansAccess<P> get(Class<P> type, FieldFilter filter) {
        String accessClassName;
        BeansAccess<P> access = (BeansAccess<P>) cache.get(type);
        if (access == null) {
            Accessor[] accs = ASMUtil.getAccessors(type, filter);
            String className = type.getName();
            if (className.startsWith("java.util.")) {
                accessClassName = "net.minidev.asm." + className + "AccAccess";
            } else {
                accessClassName = className.concat("AccAccess");
            }
            DynamicClassLoader loader = new DynamicClassLoader(type.getClassLoader());
            Class<?> accessClass = null;
            try {
                accessClass = loader.loadClass(accessClassName);
            } catch (ClassNotFoundException e) {
            }
            LinkedList<Class<?>> parentClasses = getParents(type);
            if (accessClass == null) {
                BeansAccessBuilder builder = new BeansAccessBuilder(type, accs, loader);
                Iterator i$ = parentClasses.iterator();
                while (i$.hasNext()) {
                    Class<?> c = i$.next();
                    builder.addConversion(BeansAccessConfig.classMapper.get(c));
                }
                accessClass = builder.bulid();
            }
            try {
                access = (BeansAccess) accessClass.newInstance();
                access.setAccessor(accs);
                cache.putIfAbsent(type, access);
                Iterator i$2 = parentClasses.iterator();
                while (i$2.hasNext()) {
                    Class<?> c2 = i$2.next();
                    addAlias(access, BeansAccessConfig.classFiledNameMapper.get(c2));
                }
            } catch (Exception ex) {
                throw new RuntimeException("Error constructing accessor class: " + accessClassName, ex);
            }
        }
        return access;
    }

    private static LinkedList<Class<?>> getParents(Class<?> type) {
        LinkedList<Class<?>> m = new LinkedList<>();
        while (type != null && !type.equals(Object.class)) {
            m.addLast(type);
            Class<?>[] arr$ = type.getInterfaces();
            for (Class<?> c : arr$) {
                m.addLast(c);
            }
            type = type.getSuperclass();
        }
        m.addLast(Object.class);
        return m;
    }

    private static void addAlias(BeansAccess<?> access, HashMap<String, String> m) {
        if (m != null) {
            HashMap<String, Accessor> changes = new HashMap<>();
            for (Map.Entry<String, String> e : m.entrySet()) {
                Accessor a1 = ((BeansAccess) access).map.get(e.getValue());
                if (a1 != null) {
                    changes.put(e.getValue(), a1);
                }
            }
            ((BeansAccess) access).map.putAll(changes);
        }
    }

    public void set(T object, String methodName, Object value) {
        int i = getIndex(methodName);
        if (i == -1) {
            throw new NoSuchFieldException(methodName + " in " + object.getClass() + " to put value : " + value);
        }
        set((BeansAccess<T>) object, i, value);
    }

    public Object get(T object, String methodName) {
        return get((BeansAccess<T>) object, getIndex(methodName));
    }

    public int getIndex(String name) {
        Accessor ac = this.map.get(name);
        if (ac == null) {
            return -1;
        }
        return ac.index;
    }
}

package net.minidev.asm;

import java.lang.reflect.Method;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class DynamicClassLoader extends ClassLoader {
    private static final String BEAN_AC = BeansAccess.class.getName();
    private static final Class<?>[] DEF_CLASS_SIG = {String.class, byte[].class, Integer.TYPE, Integer.TYPE};

    /* JADX INFO: Access modifiers changed from: package-private */
    public DynamicClassLoader(ClassLoader parent) {
        super(parent);
    }

    public static <T> Class<T> directLoad(Class<? extends T> parent, String clsName, byte[] clsData) {
        DynamicClassLoader loader = new DynamicClassLoader(parent.getClassLoader());
        Class<T> clzz = (Class<T>) loader.defineClass(clsName, clsData);
        return clzz;
    }

    public static <T> T directInstance(Class<? extends T> parent, String clsName, byte[] clsData) throws InstantiationException, IllegalAccessException {
        Class<T> clzz = directLoad(parent, clsName, clsData);
        return clzz.newInstance();
    }

    @Override // java.lang.ClassLoader
    protected synchronized Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        return name.equals(BEAN_AC) ? BeansAccess.class : super.loadClass(name, resolve);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Class<?> defineClass(String name, byte[] bytes) throws ClassFormatError {
        try {
            Method method = ClassLoader.class.getDeclaredMethod("defineClass", DEF_CLASS_SIG);
            method.setAccessible(true);
            return (Class) method.invoke(getParent(), name, bytes, 0, Integer.valueOf(bytes.length));
        } catch (Exception e) {
            return defineClass(name, bytes, 0, bytes.length);
        }
    }
}

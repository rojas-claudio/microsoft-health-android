package dagger.internal;
/* loaded from: classes.dex */
public abstract class Loader {
    private final Memoizer<ClassLoader, Memoizer<String, Class<?>>> caches = new Memoizer<ClassLoader, Memoizer<String, Class<?>>>() { // from class: dagger.internal.Loader.1
        /* JADX INFO: Access modifiers changed from: protected */
        @Override // dagger.internal.Memoizer
        public Memoizer<String, Class<?>> create(final ClassLoader classLoader) {
            return new Memoizer<String, Class<?>>() { // from class: dagger.internal.Loader.1.1
                /* JADX INFO: Access modifiers changed from: protected */
                @Override // dagger.internal.Memoizer
                public Class<?> create(String className) {
                    try {
                        return classLoader.loadClass(className);
                    } catch (ClassNotFoundException e) {
                        return Void.class;
                    }
                }
            };
        }
    };

    public abstract Binding<?> getAtInjectBinding(String str, String str2, ClassLoader classLoader, boolean z);

    public abstract <T> ModuleAdapter<T> getModuleAdapter(Class<T> cls);

    public abstract StaticInjection getStaticInjection(Class<?> cls);

    /* JADX INFO: Access modifiers changed from: protected */
    public Class<?> loadClass(ClassLoader classLoader, String name) {
        if (classLoader == null) {
            classLoader = ClassLoader.getSystemClassLoader();
        }
        return this.caches.get(classLoader).get(name);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public <T> T instantiate(String name, ClassLoader classLoader) {
        try {
            Class<?> generatedClass = loadClass(classLoader, name);
            if (generatedClass == Void.class) {
                return null;
            }
            return (T) generatedClass.newInstance();
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Failed to initialize " + name, e);
        } catch (InstantiationException e2) {
            throw new RuntimeException("Failed to initialize " + name, e2);
        }
    }
}

package org.slf4j.helpers;
/* loaded from: classes.dex */
public final class Util {
    private static final ClassContextSecurityManager SECURITY_MANAGER = new ClassContextSecurityManager();

    private Util() {
    }

    /* loaded from: classes.dex */
    private static final class ClassContextSecurityManager extends SecurityManager {
        private ClassContextSecurityManager() {
        }

        @Override // java.lang.SecurityManager
        protected Class<?>[] getClassContext() {
            return super.getClassContext();
        }
    }

    public static Class<?> getCallingClass() {
        Class<?>[] trace = SECURITY_MANAGER.getClassContext();
        String thisClassName = Util.class.getName();
        int i = 0;
        while (i < trace.length && !thisClassName.equals(trace[i].getName())) {
            i++;
        }
        if (i >= trace.length || i + 2 >= trace.length) {
            throw new IllegalStateException("Failed to find org.slf4j.helpers.Util or its caller in the stack; this should not happen");
        }
        return trace[i + 2];
    }

    public static final void report(String msg, Throwable t) {
        System.err.println(msg);
        System.err.println("Reported exception:");
        t.printStackTrace();
    }

    public static final void report(String msg) {
        System.err.println("SLF4J: " + msg);
    }
}

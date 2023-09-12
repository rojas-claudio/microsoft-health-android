package com.jayway.jsonpath.internal;

import com.jayway.jsonpath.JsonPathException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamClass;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
/* loaded from: classes.dex */
public final class Utils {
    public static final String CR = System.getProperty("line.separator");

    public static List<Integer> createRange(int start, int end) {
        if (end <= start) {
            throw new IllegalArgumentException("Cannot create range from " + start + " to " + end + ", end must be greater than start.");
        }
        if (start == end - 1) {
            return Collections.emptyList();
        }
        List<Integer> range = new ArrayList<>((end - start) - 1);
        for (int i = start; i < end; i++) {
            range.add(Integer.valueOf(i));
        }
        return range;
    }

    public static String join(String delimiter, String wrap, Iterable<? extends Object> objs) {
        Iterator<? extends Object> iter = objs.iterator();
        if (!iter.hasNext()) {
            return "";
        }
        StringBuilder buffer = new StringBuilder();
        buffer.append(wrap).append(iter.next()).append(wrap);
        while (iter.hasNext()) {
            buffer.append(delimiter).append(wrap).append(iter.next()).append(wrap);
        }
        return buffer.toString();
    }

    public static String join(String delimiter, Iterable<? extends Object> objs) {
        return join(delimiter, "", objs);
    }

    public static void closeQuietly(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
            }
        }
    }

    public static boolean isInt(String str) {
        if (str == null) {
            return false;
        }
        int sz = str.length();
        for (int i = 0; i < sz; i++) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static boolean isNumeric(String str) {
        if (str == null) {
            return false;
        }
        int sz = str.length();
        for (int i = 0; i < sz; i++) {
            if (!Character.isDigit(str.charAt(i)) && str.charAt(i) != '.') {
                return false;
            }
        }
        return true;
    }

    public static boolean isEmpty(CharSequence cs) {
        return cs == null || cs.length() == 0;
    }

    static int indexOf(CharSequence cs, CharSequence searchChar, int start) {
        return cs.toString().indexOf(searchChar.toString(), start);
    }

    public static int countMatches(CharSequence str, CharSequence sub) {
        if (isEmpty(str) || isEmpty(sub)) {
            return 0;
        }
        int count = 0;
        int idx = 0;
        while (true) {
            int idx2 = indexOf(str, sub, idx);
            if (idx2 != -1) {
                count++;
                idx = idx2 + sub.length();
            } else {
                return count;
            }
        }
    }

    public static <T> T notNull(T object, String message, Object... values) {
        if (object == null) {
            throw new IllegalArgumentException(String.format(message, values));
        }
        return object;
    }

    public static void isTrue(boolean expression, String message) {
        if (!expression) {
            throw new IllegalArgumentException(message);
        }
    }

    public static <T extends CharSequence> T notEmpty(T chars, String message, Object... values) {
        if (chars == null) {
            throw new IllegalArgumentException(String.format(message, values));
        }
        if (chars.length() == 0) {
            throw new IllegalArgumentException(String.format(message, values));
        }
        return chars;
    }

    public static String toString(Object o) {
        if (o == null) {
            return null;
        }
        return o.toString();
    }

    public static void serialize(Serializable obj, OutputStream outputStream) {
        ObjectOutputStream out;
        if (outputStream == null) {
            throw new IllegalArgumentException("The OutputStream must not be null");
        }
        ObjectOutputStream out2 = null;
        try {
            try {
                out = new ObjectOutputStream(outputStream);
            } catch (Throwable th) {
                th = th;
            }
        } catch (IOException e) {
            ex = e;
        }
        try {
            out.writeObject(obj);
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e2) {
                }
            }
        } catch (IOException e3) {
            ex = e3;
            throw new JsonPathException(ex);
        } catch (Throwable th2) {
            th = th2;
            out2 = out;
            if (out2 != null) {
                try {
                    out2.close();
                } catch (IOException e4) {
                }
            }
            throw th;
        }
    }

    public static byte[] serialize(Serializable obj) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(512);
        serialize(obj, baos);
        return baos.toByteArray();
    }

    public static Object deserialize(InputStream inputStream) {
        ObjectInputStream in;
        if (inputStream == null) {
            throw new IllegalArgumentException("The InputStream must not be null");
        }
        ObjectInputStream in2 = null;
        try {
            try {
                in = new ObjectInputStream(inputStream);
            } catch (Throwable th) {
                th = th;
            }
        } catch (IOException e) {
            ex = e;
        } catch (ClassNotFoundException e2) {
            ex = e2;
        }
        try {
            Object readObject = in.readObject();
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e3) {
                }
            }
            return readObject;
        } catch (IOException e4) {
            ex = e4;
            throw new JsonPathException(ex);
        } catch (ClassNotFoundException e5) {
            ex = e5;
            throw new JsonPathException(ex);
        } catch (Throwable th2) {
            th = th2;
            in2 = in;
            if (in2 != null) {
                try {
                    in2.close();
                } catch (IOException e6) {
                }
            }
            throw th;
        }
    }

    public static Object deserialize(byte[] objectData) {
        if (objectData == null) {
            throw new IllegalArgumentException("The byte[] must not be null");
        }
        ByteArrayInputStream bais = new ByteArrayInputStream(objectData);
        return deserialize(bais);
    }

    /* loaded from: classes.dex */
    static class ClassLoaderAwareObjectInputStream extends ObjectInputStream {
        private ClassLoader classLoader;

        public ClassLoaderAwareObjectInputStream(InputStream in, ClassLoader classLoader) throws IOException {
            super(in);
            this.classLoader = classLoader;
        }

        @Override // java.io.ObjectInputStream
        protected Class<?> resolveClass(ObjectStreamClass desc) throws IOException, ClassNotFoundException {
            String name = desc.getName();
            try {
                return Class.forName(name, false, this.classLoader);
            } catch (ClassNotFoundException e) {
                return Class.forName(name, false, Thread.currentThread().getContextClassLoader());
            }
        }
    }

    private Utils() {
    }
}

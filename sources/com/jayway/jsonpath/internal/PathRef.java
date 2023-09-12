package com.jayway.jsonpath.internal;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.InvalidModificationException;
import com.jayway.jsonpath.spi.json.JsonProvider;
import java.util.Collection;
/* loaded from: classes.dex */
public abstract class PathRef implements Comparable<PathRef> {
    public static final PathRef NO_OP = new PathRef(null) { // from class: com.jayway.jsonpath.internal.PathRef.1
        @Override // com.jayway.jsonpath.internal.PathRef, java.lang.Comparable
        public /* bridge */ /* synthetic */ int compareTo(PathRef pathRef) {
            return super.compareTo(pathRef);
        }

        @Override // com.jayway.jsonpath.internal.PathRef
        public Object getAccessor() {
            return null;
        }

        @Override // com.jayway.jsonpath.internal.PathRef
        public void set(Object newVal, Configuration configuration) {
        }

        @Override // com.jayway.jsonpath.internal.PathRef
        public void delete(Configuration configuration) {
        }

        @Override // com.jayway.jsonpath.internal.PathRef
        public void add(Object newVal, Configuration configuration) {
        }

        @Override // com.jayway.jsonpath.internal.PathRef
        public void put(String key, Object newVal, Configuration configuration) {
        }
    };
    protected Object parent;

    public abstract void add(Object obj, Configuration configuration);

    public abstract void delete(Configuration configuration);

    abstract Object getAccessor();

    public abstract void put(String str, Object obj, Configuration configuration);

    public abstract void set(Object obj, Configuration configuration);

    private PathRef(Object parent) {
        this.parent = parent;
    }

    @Override // java.lang.Comparable
    public int compareTo(PathRef o) {
        return getAccessor().toString().compareTo(o.getAccessor().toString()) * (-1);
    }

    public static PathRef create(Object obj, String property) {
        return new ObjectPropertyPathRef(obj, property);
    }

    public static PathRef create(Object obj, Collection<String> properties) {
        return new ObjectMultiPropertyPathRef(obj, properties);
    }

    public static PathRef create(Object array, int index) {
        return new ArrayIndexPathRef(array, index);
    }

    public static PathRef createRoot(Object root) {
        return new RootPathRef(root);
    }

    /* loaded from: classes.dex */
    private static class RootPathRef extends PathRef {
        @Override // com.jayway.jsonpath.internal.PathRef, java.lang.Comparable
        public /* bridge */ /* synthetic */ int compareTo(PathRef pathRef) {
            return super.compareTo(pathRef);
        }

        private RootPathRef(Object parent) {
            super(parent);
        }

        @Override // com.jayway.jsonpath.internal.PathRef
        Object getAccessor() {
            return "$";
        }

        @Override // com.jayway.jsonpath.internal.PathRef
        public void set(Object newVal, Configuration configuration) {
            throw new InvalidModificationException("Invalid delete operation");
        }

        @Override // com.jayway.jsonpath.internal.PathRef
        public void delete(Configuration configuration) {
            throw new InvalidModificationException("Invalid delete operation");
        }

        @Override // com.jayway.jsonpath.internal.PathRef
        public void add(Object newVal, Configuration configuration) {
            if (configuration.jsonProvider().isArray(this.parent)) {
                configuration.jsonProvider().setProperty(this.parent, null, newVal);
                return;
            }
            throw new InvalidModificationException("Invalid add operation. $ is not an array");
        }

        @Override // com.jayway.jsonpath.internal.PathRef
        public void put(String key, Object newVal, Configuration configuration) {
            if (configuration.jsonProvider().isMap(this.parent)) {
                configuration.jsonProvider().setProperty(this.parent, key, newVal);
                return;
            }
            throw new InvalidModificationException("Invalid put operation. $ is not a map");
        }
    }

    /* loaded from: classes.dex */
    private static class ArrayIndexPathRef extends PathRef {
        private int index;

        @Override // com.jayway.jsonpath.internal.PathRef, java.lang.Comparable
        public /* bridge */ /* synthetic */ int compareTo(PathRef pathRef) {
            return super.compareTo(pathRef);
        }

        private ArrayIndexPathRef(Object parent, int index) {
            super(parent);
            this.index = index;
        }

        @Override // com.jayway.jsonpath.internal.PathRef
        public void set(Object newVal, Configuration configuration) {
            configuration.jsonProvider().setArrayIndex(this.parent, this.index, newVal);
        }

        @Override // com.jayway.jsonpath.internal.PathRef
        public void delete(Configuration configuration) {
            configuration.jsonProvider().removeProperty(this.parent, Integer.valueOf(this.index));
        }

        @Override // com.jayway.jsonpath.internal.PathRef
        public void add(Object value, Configuration configuration) {
            Object target = configuration.jsonProvider().getArrayIndex(this.parent, this.index);
            if (target != JsonProvider.UNDEFINED && target != null) {
                if (configuration.jsonProvider().isArray(target)) {
                    configuration.jsonProvider().setProperty(target, null, value);
                    return;
                }
                throw new InvalidModificationException("Can only add to an array");
            }
        }

        @Override // com.jayway.jsonpath.internal.PathRef
        public void put(String key, Object value, Configuration configuration) {
            Object target = configuration.jsonProvider().getArrayIndex(this.parent, this.index);
            if (target != JsonProvider.UNDEFINED && target != null) {
                if (configuration.jsonProvider().isMap(target)) {
                    configuration.jsonProvider().setProperty(target, key, value);
                    return;
                }
                throw new InvalidModificationException("Can only add properties to a map");
            }
        }

        @Override // com.jayway.jsonpath.internal.PathRef
        public Object getAccessor() {
            return Integer.valueOf(this.index);
        }
    }

    /* loaded from: classes.dex */
    private static class ObjectPropertyPathRef extends PathRef {
        private String property;

        @Override // com.jayway.jsonpath.internal.PathRef, java.lang.Comparable
        public /* bridge */ /* synthetic */ int compareTo(PathRef pathRef) {
            return super.compareTo(pathRef);
        }

        private ObjectPropertyPathRef(Object parent, String property) {
            super(parent);
            this.property = property;
        }

        @Override // com.jayway.jsonpath.internal.PathRef
        public void set(Object newVal, Configuration configuration) {
            configuration.jsonProvider().setProperty(this.parent, this.property, newVal);
        }

        @Override // com.jayway.jsonpath.internal.PathRef
        public void delete(Configuration configuration) {
            configuration.jsonProvider().removeProperty(this.parent, this.property);
        }

        @Override // com.jayway.jsonpath.internal.PathRef
        public void add(Object value, Configuration configuration) {
            Object target = configuration.jsonProvider().getMapValue(this.parent, this.property);
            if (target != JsonProvider.UNDEFINED && target != null) {
                if (configuration.jsonProvider().isArray(target)) {
                    configuration.jsonProvider().setProperty(target, null, value);
                    return;
                }
                throw new InvalidModificationException("Can only add to an array");
            }
        }

        @Override // com.jayway.jsonpath.internal.PathRef
        public void put(String key, Object value, Configuration configuration) {
            Object target = configuration.jsonProvider().getMapValue(this.parent, this.property);
            if (target != JsonProvider.UNDEFINED && target != null) {
                if (configuration.jsonProvider().isMap(target)) {
                    configuration.jsonProvider().setProperty(target, key, value);
                    return;
                }
                throw new InvalidModificationException("Can only add properties to a map");
            }
        }

        @Override // com.jayway.jsonpath.internal.PathRef
        public Object getAccessor() {
            return this.property;
        }
    }

    /* loaded from: classes.dex */
    private static class ObjectMultiPropertyPathRef extends PathRef {
        private Collection<String> properties;

        @Override // com.jayway.jsonpath.internal.PathRef, java.lang.Comparable
        public /* bridge */ /* synthetic */ int compareTo(PathRef pathRef) {
            return super.compareTo(pathRef);
        }

        private ObjectMultiPropertyPathRef(Object parent, Collection<String> properties) {
            super(parent);
            this.properties = properties;
        }

        @Override // com.jayway.jsonpath.internal.PathRef
        public void set(Object newVal, Configuration configuration) {
            for (String property : this.properties) {
                configuration.jsonProvider().setProperty(this.parent, property, newVal);
            }
        }

        @Override // com.jayway.jsonpath.internal.PathRef
        public void delete(Configuration configuration) {
            for (String property : this.properties) {
                configuration.jsonProvider().removeProperty(this.parent, property);
            }
        }

        @Override // com.jayway.jsonpath.internal.PathRef
        public void add(Object newVal, Configuration configuration) {
            throw new InvalidModificationException("Add can not be performed to multiple properties");
        }

        @Override // com.jayway.jsonpath.internal.PathRef
        public void put(String key, Object newVal, Configuration configuration) {
            throw new InvalidModificationException("Add can not be performed to multiple properties");
        }

        @Override // com.jayway.jsonpath.internal.PathRef
        public Object getAccessor() {
            return Utils.join("&&", this.properties);
        }
    }
}

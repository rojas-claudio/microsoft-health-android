package com.google.gson.internal;

import com.j256.ormlite.stmt.query.SimpleComparison;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Properties;
/* renamed from: com.google.gson.internal.$Gson$Types  reason: invalid class name */
/* loaded from: classes.dex */
public final class C$Gson$Types {
    static final Type[] EMPTY_TYPE_ARRAY = new Type[0];

    private C$Gson$Types() {
    }

    public static ParameterizedType newParameterizedTypeWithOwner(Type ownerType, Type rawType, Type... typeArguments) {
        return new ParameterizedTypeImpl(ownerType, rawType, typeArguments);
    }

    public static GenericArrayType arrayOf(Type componentType) {
        return new GenericArrayTypeImpl(componentType);
    }

    public static WildcardType subtypeOf(Type bound) {
        return new WildcardTypeImpl(new Type[]{bound}, EMPTY_TYPE_ARRAY);
    }

    public static WildcardType supertypeOf(Type bound) {
        return new WildcardTypeImpl(new Type[]{Object.class}, new Type[]{bound});
    }

    public static Type canonicalize(Type type) {
        if (type instanceof Class) {
            Class<?> c = (Class) type;
            return c.isArray() ? new GenericArrayTypeImpl(canonicalize(c.getComponentType())) : c;
        } else if (type instanceof ParameterizedType) {
            ParameterizedType p = (ParameterizedType) type;
            return new ParameterizedTypeImpl(p.getOwnerType(), p.getRawType(), p.getActualTypeArguments());
        } else if (type instanceof GenericArrayType) {
            GenericArrayType g = (GenericArrayType) type;
            return new GenericArrayTypeImpl(g.getGenericComponentType());
        } else if (type instanceof WildcardType) {
            WildcardType w = (WildcardType) type;
            return new WildcardTypeImpl(w.getUpperBounds(), w.getLowerBounds());
        } else {
            return type;
        }
    }

    public static Class<?> getRawType(Type type) {
        if (type instanceof Class) {
            return (Class) type;
        }
        if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            Type rawType = parameterizedType.getRawType();
            C$Gson$Preconditions.checkArgument(rawType instanceof Class);
            return (Class) rawType;
        } else if (type instanceof GenericArrayType) {
            Type componentType = ((GenericArrayType) type).getGenericComponentType();
            return Array.newInstance(getRawType(componentType), 0).getClass();
        } else if (type instanceof TypeVariable) {
            return Object.class;
        } else {
            if (type instanceof WildcardType) {
                return getRawType(((WildcardType) type).getUpperBounds()[0]);
            }
            String className = type == null ? "null" : type.getClass().getName();
            throw new IllegalArgumentException("Expected a Class, ParameterizedType, or GenericArrayType, but <" + type + "> is of type " + className);
        }
    }

    static boolean equal(Object a, Object b) {
        return a == b || (a != null && a.equals(b));
    }

    public static boolean equals(Type a, Type b) {
        boolean z = true;
        if (a == b) {
            return true;
        }
        if (a instanceof Class) {
            return a.equals(b);
        }
        if (a instanceof ParameterizedType) {
            if (b instanceof ParameterizedType) {
                ParameterizedType pa = (ParameterizedType) a;
                ParameterizedType pb = (ParameterizedType) b;
                if (!equal(pa.getOwnerType(), pb.getOwnerType()) || !pa.getRawType().equals(pb.getRawType()) || !Arrays.equals(pa.getActualTypeArguments(), pb.getActualTypeArguments())) {
                    z = false;
                }
                return z;
            }
            return false;
        } else if (a instanceof GenericArrayType) {
            if (b instanceof GenericArrayType) {
                GenericArrayType ga = (GenericArrayType) a;
                GenericArrayType gb = (GenericArrayType) b;
                return equals(ga.getGenericComponentType(), gb.getGenericComponentType());
            }
            return false;
        } else if (a instanceof WildcardType) {
            if (b instanceof WildcardType) {
                WildcardType wa = (WildcardType) a;
                WildcardType wb = (WildcardType) b;
                if (!Arrays.equals(wa.getUpperBounds(), wb.getUpperBounds()) || !Arrays.equals(wa.getLowerBounds(), wb.getLowerBounds())) {
                    z = false;
                }
                return z;
            }
            return false;
        } else if ((a instanceof TypeVariable) && (b instanceof TypeVariable)) {
            TypeVariable<?> va = (TypeVariable) a;
            TypeVariable<?> vb = (TypeVariable) b;
            if (va.getGenericDeclaration() != vb.getGenericDeclaration() || !va.getName().equals(vb.getName())) {
                z = false;
            }
            return z;
        } else {
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int hashCodeOrZero(Object o) {
        if (o != null) {
            return o.hashCode();
        }
        return 0;
    }

    public static String typeToString(Type type) {
        return type instanceof Class ? ((Class) type).getName() : type.toString();
    }

    static Type getGenericSupertype(Type context, Class<?> rawType, Class<?> toResolve) {
        if (toResolve != rawType) {
            if (toResolve.isInterface()) {
                Class<?>[] interfaces = rawType.getInterfaces();
                int length = interfaces.length;
                for (int i = 0; i < length; i++) {
                    if (interfaces[i] == toResolve) {
                        Type context2 = rawType.getGenericInterfaces()[i];
                        return context2;
                    } else if (toResolve.isAssignableFrom(interfaces[i])) {
                        Type context3 = getGenericSupertype(rawType.getGenericInterfaces()[i], interfaces[i], toResolve);
                        return context3;
                    }
                }
            }
            if (!rawType.isInterface()) {
                while (rawType != Object.class) {
                    Class<?> rawSupertype = rawType.getSuperclass();
                    if (rawSupertype == toResolve) {
                        Type context4 = rawType.getGenericSuperclass();
                        return context4;
                    } else if (toResolve.isAssignableFrom(rawSupertype)) {
                        Type context5 = getGenericSupertype(rawType.getGenericSuperclass(), rawSupertype, toResolve);
                        return context5;
                    } else {
                        rawType = rawSupertype;
                    }
                }
            }
            return toResolve;
        }
        return context;
    }

    static Type getSupertype(Type context, Class<?> contextRawType, Class<?> supertype) {
        C$Gson$Preconditions.checkArgument(supertype.isAssignableFrom(contextRawType));
        return resolve(context, contextRawType, getGenericSupertype(context, contextRawType, supertype));
    }

    public static Type getArrayComponentType(Type array) {
        return array instanceof GenericArrayType ? ((GenericArrayType) array).getGenericComponentType() : ((Class) array).getComponentType();
    }

    public static Type getCollectionElementType(Type context, Class<?> contextRawType) {
        Type collectionType = getSupertype(context, contextRawType, Collection.class);
        if (collectionType instanceof WildcardType) {
            collectionType = ((WildcardType) collectionType).getUpperBounds()[0];
        }
        return collectionType instanceof ParameterizedType ? ((ParameterizedType) collectionType).getActualTypeArguments()[0] : Object.class;
    }

    public static Type[] getMapKeyAndValueTypes(Type context, Class<?> contextRawType) {
        if (context == Properties.class) {
            return new Type[]{String.class, String.class};
        }
        Type mapType = getSupertype(context, contextRawType, Map.class);
        if (mapType instanceof ParameterizedType) {
            ParameterizedType mapParameterizedType = (ParameterizedType) mapType;
            return mapParameterizedType.getActualTypeArguments();
        }
        return new Type[]{Object.class, Object.class};
    }

    public static Type resolve(Type context, Class<?> contextRawType, Type toResolve) {
        while (toResolve instanceof TypeVariable) {
            TypeVariable<?> typeVariable = (TypeVariable) toResolve;
            toResolve = resolveTypeVariable(context, contextRawType, typeVariable);
            if (toResolve == typeVariable) {
                return toResolve;
            }
        }
        if ((toResolve instanceof Class) && ((Class) toResolve).isArray()) {
            Class<?> original = (Class) toResolve;
            Type componentType = original.getComponentType();
            Type newComponentType = resolve(context, contextRawType, componentType);
            Class<?> original2 = original;
            if (componentType != newComponentType) {
                original2 = arrayOf(newComponentType);
            }
            return original2;
        } else if (toResolve instanceof GenericArrayType) {
            GenericArrayType original3 = (GenericArrayType) toResolve;
            Type componentType2 = original3.getGenericComponentType();
            Type newComponentType2 = resolve(context, contextRawType, componentType2);
            return componentType2 != newComponentType2 ? arrayOf(newComponentType2) : original3;
        } else if (toResolve instanceof ParameterizedType) {
            ParameterizedType original4 = (ParameterizedType) toResolve;
            Type ownerType = original4.getOwnerType();
            Type newOwnerType = resolve(context, contextRawType, ownerType);
            boolean changed = newOwnerType != ownerType;
            Type[] args = original4.getActualTypeArguments();
            int length = args.length;
            for (int t = 0; t < length; t++) {
                Type resolvedTypeArgument = resolve(context, contextRawType, args[t]);
                if (resolvedTypeArgument != args[t]) {
                    if (!changed) {
                        args = (Type[]) args.clone();
                        changed = true;
                    }
                    args[t] = resolvedTypeArgument;
                }
            }
            return changed ? newParameterizedTypeWithOwner(newOwnerType, original4.getRawType(), args) : original4;
        } else if (toResolve instanceof WildcardType) {
            WildcardType original5 = (WildcardType) toResolve;
            Type[] originalLowerBound = original5.getLowerBounds();
            Type[] originalUpperBound = original5.getUpperBounds();
            if (originalLowerBound.length == 1) {
                Type lowerBound = resolve(context, contextRawType, originalLowerBound[0]);
                if (lowerBound != originalLowerBound[0]) {
                    return supertypeOf(lowerBound);
                }
                return original5;
            } else if (originalUpperBound.length == 1) {
                Type upperBound = resolve(context, contextRawType, originalUpperBound[0]);
                if (upperBound != originalUpperBound[0]) {
                    return subtypeOf(upperBound);
                }
                return original5;
            } else {
                return original5;
            }
        } else {
            return toResolve;
        }
    }

    static Type resolveTypeVariable(Type context, Class<?> contextRawType, TypeVariable<?> unknown) {
        Class<?> declaredByRaw = declaringClassOf(unknown);
        if (declaredByRaw != null) {
            Type declaredBy = getGenericSupertype(context, contextRawType, declaredByRaw);
            if (declaredBy instanceof ParameterizedType) {
                int index = indexOf(declaredByRaw.getTypeParameters(), unknown);
                return ((ParameterizedType) declaredBy).getActualTypeArguments()[index];
            }
            return unknown;
        }
        return unknown;
    }

    private static int indexOf(Object[] array, Object toFind) {
        for (int i = 0; i < array.length; i++) {
            if (toFind.equals(array[i])) {
                return i;
            }
        }
        throw new NoSuchElementException();
    }

    private static Class<?> declaringClassOf(TypeVariable<?> typeVariable) {
        Object genericDeclaration = typeVariable.getGenericDeclaration();
        if (genericDeclaration instanceof Class) {
            return (Class) genericDeclaration;
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void checkNotPrimitive(Type type) {
        C$Gson$Preconditions.checkArgument(((type instanceof Class) && ((Class) type).isPrimitive()) ? false : true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: $Gson$Types.java */
    /* renamed from: com.google.gson.internal.$Gson$Types$ParameterizedTypeImpl */
    /* loaded from: classes.dex */
    public static final class ParameterizedTypeImpl implements ParameterizedType, Serializable {
        private static final long serialVersionUID = 0;
        private final Type ownerType;
        private final Type rawType;
        private final Type[] typeArguments;

        public ParameterizedTypeImpl(Type ownerType, Type rawType, Type... typeArguments) {
            boolean z = false;
            if (rawType instanceof Class) {
                Class<?> rawTypeAsClass = (Class) rawType;
                boolean isStaticOrTopLevelClass = Modifier.isStatic(rawTypeAsClass.getModifiers()) || rawTypeAsClass.getEnclosingClass() == null;
                C$Gson$Preconditions.checkArgument((ownerType != null || isStaticOrTopLevelClass) ? true : z);
            }
            this.ownerType = ownerType == null ? null : C$Gson$Types.canonicalize(ownerType);
            this.rawType = C$Gson$Types.canonicalize(rawType);
            this.typeArguments = (Type[]) typeArguments.clone();
            for (int t = 0; t < this.typeArguments.length; t++) {
                C$Gson$Preconditions.checkNotNull(this.typeArguments[t]);
                C$Gson$Types.checkNotPrimitive(this.typeArguments[t]);
                this.typeArguments[t] = C$Gson$Types.canonicalize(this.typeArguments[t]);
            }
        }

        @Override // java.lang.reflect.ParameterizedType
        public Type[] getActualTypeArguments() {
            return (Type[]) this.typeArguments.clone();
        }

        @Override // java.lang.reflect.ParameterizedType
        public Type getRawType() {
            return this.rawType;
        }

        @Override // java.lang.reflect.ParameterizedType
        public Type getOwnerType() {
            return this.ownerType;
        }

        public boolean equals(Object other) {
            return (other instanceof ParameterizedType) && C$Gson$Types.equals(this, (ParameterizedType) other);
        }

        public int hashCode() {
            return (Arrays.hashCode(this.typeArguments) ^ this.rawType.hashCode()) ^ C$Gson$Types.hashCodeOrZero(this.ownerType);
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder((this.typeArguments.length + 1) * 30);
            stringBuilder.append(C$Gson$Types.typeToString(this.rawType));
            if (this.typeArguments.length == 0) {
                return stringBuilder.toString();
            }
            stringBuilder.append(SimpleComparison.LESS_THAN_OPERATION).append(C$Gson$Types.typeToString(this.typeArguments[0]));
            for (int i = 1; i < this.typeArguments.length; i++) {
                stringBuilder.append(", ").append(C$Gson$Types.typeToString(this.typeArguments[i]));
            }
            return stringBuilder.append(SimpleComparison.GREATER_THAN_OPERATION).toString();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: $Gson$Types.java */
    /* renamed from: com.google.gson.internal.$Gson$Types$GenericArrayTypeImpl */
    /* loaded from: classes.dex */
    public static final class GenericArrayTypeImpl implements GenericArrayType, Serializable {
        private static final long serialVersionUID = 0;
        private final Type componentType;

        public GenericArrayTypeImpl(Type componentType) {
            this.componentType = C$Gson$Types.canonicalize(componentType);
        }

        @Override // java.lang.reflect.GenericArrayType
        public Type getGenericComponentType() {
            return this.componentType;
        }

        public boolean equals(Object o) {
            return (o instanceof GenericArrayType) && C$Gson$Types.equals(this, (GenericArrayType) o);
        }

        public int hashCode() {
            return this.componentType.hashCode();
        }

        public String toString() {
            return C$Gson$Types.typeToString(this.componentType) + "[]";
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: $Gson$Types.java */
    /* renamed from: com.google.gson.internal.$Gson$Types$WildcardTypeImpl */
    /* loaded from: classes.dex */
    public static final class WildcardTypeImpl implements WildcardType, Serializable {
        private static final long serialVersionUID = 0;
        private final Type lowerBound;
        private final Type upperBound;

        public WildcardTypeImpl(Type[] upperBounds, Type[] lowerBounds) {
            C$Gson$Preconditions.checkArgument(lowerBounds.length <= 1);
            C$Gson$Preconditions.checkArgument(upperBounds.length == 1);
            if (lowerBounds.length == 1) {
                C$Gson$Preconditions.checkNotNull(lowerBounds[0]);
                C$Gson$Types.checkNotPrimitive(lowerBounds[0]);
                C$Gson$Preconditions.checkArgument(upperBounds[0] == Object.class);
                this.lowerBound = C$Gson$Types.canonicalize(lowerBounds[0]);
                this.upperBound = Object.class;
                return;
            }
            C$Gson$Preconditions.checkNotNull(upperBounds[0]);
            C$Gson$Types.checkNotPrimitive(upperBounds[0]);
            this.lowerBound = null;
            this.upperBound = C$Gson$Types.canonicalize(upperBounds[0]);
        }

        @Override // java.lang.reflect.WildcardType
        public Type[] getUpperBounds() {
            return new Type[]{this.upperBound};
        }

        @Override // java.lang.reflect.WildcardType
        public Type[] getLowerBounds() {
            return this.lowerBound != null ? new Type[]{this.lowerBound} : C$Gson$Types.EMPTY_TYPE_ARRAY;
        }

        public boolean equals(Object other) {
            return (other instanceof WildcardType) && C$Gson$Types.equals(this, (WildcardType) other);
        }

        public int hashCode() {
            return (this.lowerBound != null ? this.lowerBound.hashCode() + 31 : 1) ^ (this.upperBound.hashCode() + 31);
        }

        public String toString() {
            if (this.lowerBound != null) {
                return "? super " + C$Gson$Types.typeToString(this.lowerBound);
            }
            if (this.upperBound == Object.class) {
                return "?";
            }
            return "? extends " + C$Gson$Types.typeToString(this.upperBound);
        }
    }
}

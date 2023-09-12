package com.facebook.model;

import com.facebook.FacebookGraphObjectException;
import com.facebook.internal.Utility;
import com.facebook.internal.Validate;
import com.microsoft.kapp.utils.Constants;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.AbstractList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
/* loaded from: classes.dex */
public interface GraphObject {
    Map<String, Object> asMap();

    <T extends GraphObject> T cast(Class<T> cls);

    JSONObject getInnerJSONObject();

    Object getProperty(String str);

    <T extends GraphObject> T getPropertyAs(String str, Class<T> cls);

    <T extends GraphObject> GraphObjectList<T> getPropertyAsList(String str, Class<T> cls);

    void removeProperty(String str);

    void setProperty(String str, Object obj);

    /* loaded from: classes.dex */
    public static final class Factory {
        private static final HashSet<Class<?>> verifiedGraphObjectClasses = new HashSet<>();
        private static final SimpleDateFormat[] dateFormats = {new SimpleDateFormat(Constants.SETTINGS_DATETIME_PATTERN, Locale.US), new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US), new SimpleDateFormat("yyyy-MM-dd", Locale.US)};

        private Factory() {
        }

        public static GraphObject create(JSONObject json) {
            return create(json, GraphObject.class);
        }

        public static <T extends GraphObject> T create(JSONObject json, Class<T> graphObjectClass) {
            return (T) createGraphObjectProxy(graphObjectClass, json);
        }

        public static GraphObject create() {
            return create(GraphObject.class);
        }

        public static <T extends GraphObject> T create(Class<T> graphObjectClass) {
            return (T) createGraphObjectProxy(graphObjectClass, new JSONObject());
        }

        public static boolean hasSameId(GraphObject a, GraphObject b) {
            if (a == null || b == null || !a.asMap().containsKey("id") || !b.asMap().containsKey("id")) {
                return false;
            }
            if (a.equals(b)) {
                return true;
            }
            Object idA = a.getProperty("id");
            Object idB = b.getProperty("id");
            if (idA == null || idB == null || !(idA instanceof String) || !(idB instanceof String)) {
                return false;
            }
            return idA.equals(idB);
        }

        public static <T> GraphObjectList<T> createList(JSONArray array, Class<T> graphObjectClass) {
            return new GraphObjectListImpl(array, graphObjectClass);
        }

        public static <T> GraphObjectList<T> createList(Class<T> graphObjectClass) {
            return createList(new JSONArray(), graphObjectClass);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static <T extends GraphObject> T createGraphObjectProxy(Class<T> graphObjectClass, JSONObject state) {
            verifyCanProxyClass(graphObjectClass);
            Class[] interfaces = {graphObjectClass};
            GraphObjectProxy graphObjectProxy = new GraphObjectProxy(state, graphObjectClass);
            T graphObject = (T) Proxy.newProxyInstance(GraphObject.class.getClassLoader(), interfaces, graphObjectProxy);
            return graphObject;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static Map<String, Object> createGraphObjectProxyForMap(JSONObject state) {
            Class[] interfaces = {Map.class};
            GraphObjectProxy graphObjectProxy = new GraphObjectProxy(state, Map.class);
            Map<String, Object> graphObject = (Map) Proxy.newProxyInstance(GraphObject.class.getClassLoader(), interfaces, graphObjectProxy);
            return graphObject;
        }

        private static synchronized <T extends GraphObject> boolean hasClassBeenVerified(Class<T> graphObjectClass) {
            boolean contains;
            synchronized (Factory.class) {
                contains = verifiedGraphObjectClasses.contains(graphObjectClass);
            }
            return contains;
        }

        private static synchronized <T extends GraphObject> void recordClassHasBeenVerified(Class<T> graphObjectClass) {
            synchronized (Factory.class) {
                verifiedGraphObjectClasses.add(graphObjectClass);
            }
        }

        private static <T extends GraphObject> void verifyCanProxyClass(Class<T> graphObjectClass) {
            if (!hasClassBeenVerified(graphObjectClass)) {
                if (!graphObjectClass.isInterface()) {
                    throw new FacebookGraphObjectException("Factory can only wrap interfaces, not class: " + graphObjectClass.getName());
                }
                Method[] methods = graphObjectClass.getMethods();
                for (Method method : methods) {
                    String methodName = method.getName();
                    int parameterCount = method.getParameterTypes().length;
                    Class<?> returnType = method.getReturnType();
                    boolean hasPropertyNameOverride = method.isAnnotationPresent(PropertyName.class);
                    if (!method.getDeclaringClass().isAssignableFrom(GraphObject.class)) {
                        if (parameterCount == 1 && returnType == Void.TYPE) {
                            if (hasPropertyNameOverride) {
                                if (Utility.isNullOrEmpty(((PropertyName) method.getAnnotation(PropertyName.class)).value())) {
                                    throw new FacebookGraphObjectException("Factory can't proxy method: " + method.toString());
                                }
                            } else {
                                if (methodName.startsWith("set") && methodName.length() > 3) {
                                }
                                throw new FacebookGraphObjectException("Factory can't proxy method: " + method.toString());
                            }
                        } else {
                            if (parameterCount == 0 && returnType != Void.TYPE) {
                                if (hasPropertyNameOverride) {
                                    if (!Utility.isNullOrEmpty(((PropertyName) method.getAnnotation(PropertyName.class)).value())) {
                                    }
                                } else if (methodName.startsWith("get") && methodName.length() > 3) {
                                }
                            }
                            throw new FacebookGraphObjectException("Factory can't proxy method: " + method.toString());
                        }
                    }
                }
                recordClassHasBeenVerified(graphObjectClass);
            }
        }

        static <U> U coerceValueToExpectedType(Object value, Class<U> expectedType, ParameterizedType expectedTypeAsParameterizedType) {
            SimpleDateFormat[] simpleDateFormatArr;
            U u;
            if (value == 0) {
                if (Boolean.TYPE.equals(expectedType)) {
                    return (U) false;
                }
                if (Character.TYPE.equals(expectedType)) {
                    return (U) (char) 0;
                }
                if (expectedType.isPrimitive()) {
                    return (U) 0;
                }
                return null;
            }
            Class<?> valueType = value.getClass();
            if (expectedType.isAssignableFrom(valueType)) {
                return value;
            }
            if (expectedType.isPrimitive()) {
                return value;
            }
            if (GraphObject.class.isAssignableFrom(expectedType)) {
                if (JSONObject.class.isAssignableFrom(valueType)) {
                    return (U) createGraphObjectProxy(expectedType, (JSONObject) value);
                }
                if (GraphObject.class.isAssignableFrom(valueType)) {
                    return (U) ((GraphObject) value).cast(expectedType);
                }
                throw new FacebookGraphObjectException("Can't create GraphObject from " + valueType.getName());
            } else if (Iterable.class.equals(expectedType) || Collection.class.equals(expectedType) || List.class.equals(expectedType) || GraphObjectList.class.equals(expectedType)) {
                if (expectedTypeAsParameterizedType == null) {
                    throw new FacebookGraphObjectException("can't infer generic type of: " + expectedType.toString());
                }
                Type[] actualTypeArguments = expectedTypeAsParameterizedType.getActualTypeArguments();
                if (actualTypeArguments == null || actualTypeArguments.length != 1 || !(actualTypeArguments[0] instanceof Class)) {
                    throw new FacebookGraphObjectException("Expect collection properties to be of a type with exactly one generic parameter.");
                }
                Class<?> collectionGenericArgument = (Class) actualTypeArguments[0];
                if (JSONArray.class.isAssignableFrom(valueType)) {
                    JSONArray jsonArray = (JSONArray) value;
                    return (U) createList(jsonArray, collectionGenericArgument);
                }
                throw new FacebookGraphObjectException("Can't create Collection from " + valueType.getName());
            } else {
                if (String.class.equals(expectedType)) {
                    if (Double.class.isAssignableFrom(valueType) || Float.class.isAssignableFrom(valueType)) {
                        return (U) String.format("%f", value);
                    }
                    if (Number.class.isAssignableFrom(valueType)) {
                        return (U) String.format("%d", value);
                    }
                } else if (Date.class.equals(expectedType) && String.class.isAssignableFrom(valueType)) {
                    for (SimpleDateFormat format : dateFormats) {
                        try {
                            u = (U) format.parse((String) value);
                        } catch (ParseException e) {
                        }
                        if (u != null) {
                            return u;
                        }
                    }
                }
                throw new FacebookGraphObjectException("Can't convert type" + valueType.getName() + " to " + expectedType.getName());
            }
        }

        static String convertCamelCaseToLowercaseWithUnderscores(String string) {
            return string.replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase(Locale.US);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static Object getUnderlyingJSONObject(Object obj) {
            if (obj == null) {
                return null;
            }
            Class<?> objClass = obj.getClass();
            if (GraphObject.class.isAssignableFrom(objClass)) {
                GraphObject graphObject = (GraphObject) obj;
                return graphObject.getInnerJSONObject();
            } else if (GraphObjectList.class.isAssignableFrom(objClass)) {
                GraphObjectList<?> graphObjectList = (GraphObjectList) obj;
                return graphObjectList.getInnerJSONArray();
            } else if (Iterable.class.isAssignableFrom(objClass)) {
                JSONArray jsonArray = new JSONArray();
                Iterable<?> iterable = (Iterable) obj;
                for (Object o : iterable) {
                    if (GraphObject.class.isAssignableFrom(o.getClass())) {
                        jsonArray.put(((GraphObject) o).getInnerJSONObject());
                    } else {
                        jsonArray.put(o);
                    }
                }
                return jsonArray;
            } else {
                return obj;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes.dex */
        public static abstract class ProxyBase<STATE> implements InvocationHandler {
            private static final String EQUALS_METHOD = "equals";
            private static final String TOSTRING_METHOD = "toString";
            protected final STATE state;

            protected ProxyBase(STATE state) {
                this.state = state;
            }

            protected final Object throwUnexpectedMethodSignature(Method method) {
                throw new FacebookGraphObjectException(String.valueOf(getClass().getName()) + " got an unexpected method signature: " + method.toString());
            }

            protected final Object proxyObjectMethods(Object proxy, Method method, Object[] args) throws Throwable {
                String methodName = method.getName();
                if (methodName.equals(EQUALS_METHOD)) {
                    Object other = args[0];
                    if (other == null) {
                        return false;
                    }
                    InvocationHandler handler = Proxy.getInvocationHandler(other);
                    if (!(handler instanceof GraphObjectProxy)) {
                        return false;
                    }
                    GraphObjectProxy otherProxy = (GraphObjectProxy) handler;
                    return Boolean.valueOf(this.state.equals(otherProxy.state));
                } else if (methodName.equals(TOSTRING_METHOD)) {
                    return toString();
                } else {
                    return method.invoke(this.state, args);
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes.dex */
        public static final class GraphObjectProxy extends ProxyBase<JSONObject> {
            private static final String CASTTOMAP_METHOD = "asMap";
            private static final String CAST_METHOD = "cast";
            private static final String CLEAR_METHOD = "clear";
            private static final String CONTAINSKEY_METHOD = "containsKey";
            private static final String CONTAINSVALUE_METHOD = "containsValue";
            private static final String ENTRYSET_METHOD = "entrySet";
            private static final String GETINNERJSONOBJECT_METHOD = "getInnerJSONObject";
            private static final String GETPROPERTYASLIST_METHOD = "getPropertyAsList";
            private static final String GETPROPERTYAS_METHOD = "getPropertyAs";
            private static final String GETPROPERTY_METHOD = "getProperty";
            private static final String GET_METHOD = "get";
            private static final String ISEMPTY_METHOD = "isEmpty";
            private static final String KEYSET_METHOD = "keySet";
            private static final String PUTALL_METHOD = "putAll";
            private static final String PUT_METHOD = "put";
            private static final String REMOVEPROPERTY_METHOD = "removeProperty";
            private static final String REMOVE_METHOD = "remove";
            private static final String SETPROPERTY_METHOD = "setProperty";
            private static final String SIZE_METHOD = "size";
            private static final String VALUES_METHOD = "values";
            private final Class<?> graphObjectClass;

            public GraphObjectProxy(JSONObject state, Class<?> graphObjectClass) {
                super(state);
                this.graphObjectClass = graphObjectClass;
            }

            public String toString() {
                return String.format("GraphObject{graphObjectClass=%s, state=%s}", this.graphObjectClass.getSimpleName(), this.state);
            }

            @Override // java.lang.reflect.InvocationHandler
            public final Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                Class<?> declaringClass = method.getDeclaringClass();
                if (declaringClass == Object.class) {
                    return proxyObjectMethods(proxy, method, args);
                }
                if (declaringClass == Map.class) {
                    return proxyMapMethods(method, args);
                }
                if (declaringClass == GraphObject.class) {
                    return proxyGraphObjectMethods(proxy, method, args);
                }
                if (GraphObject.class.isAssignableFrom(declaringClass)) {
                    return proxyGraphObjectGettersAndSetters(method, args);
                }
                return throwUnexpectedMethodSignature(method);
            }

            private final Object proxyMapMethods(Method method, Object[] args) {
                Map<String, Object> map;
                String methodName = method.getName();
                if (methodName.equals(CLEAR_METHOD)) {
                    JsonUtil.jsonObjectClear((JSONObject) this.state);
                    return null;
                } else if (methodName.equals(CONTAINSKEY_METHOD)) {
                    return Boolean.valueOf(((JSONObject) this.state).has((String) args[0]));
                } else {
                    if (methodName.equals(CONTAINSVALUE_METHOD)) {
                        return Boolean.valueOf(JsonUtil.jsonObjectContainsValue((JSONObject) this.state, args[0]));
                    }
                    if (methodName.equals(ENTRYSET_METHOD)) {
                        return JsonUtil.jsonObjectEntrySet((JSONObject) this.state);
                    }
                    if (methodName.equals(GET_METHOD)) {
                        return ((JSONObject) this.state).opt((String) args[0]);
                    }
                    if (methodName.equals(ISEMPTY_METHOD)) {
                        return ((JSONObject) this.state).length() == 0;
                    } else if (methodName.equals(KEYSET_METHOD)) {
                        return JsonUtil.jsonObjectKeySet((JSONObject) this.state);
                    } else {
                        if (methodName.equals(PUT_METHOD)) {
                            return setJSONProperty(args);
                        }
                        if (methodName.equals(PUTALL_METHOD)) {
                            if (args[0] instanceof Map) {
                                Map<String, Object> castMap = (Map) args[0];
                                map = castMap;
                            } else if (!(args[0] instanceof GraphObject)) {
                                return null;
                            } else {
                                map = ((GraphObject) args[0]).asMap();
                            }
                            JsonUtil.jsonObjectPutAll((JSONObject) this.state, map);
                            return null;
                        } else if (methodName.equals(REMOVE_METHOD)) {
                            ((JSONObject) this.state).remove((String) args[0]);
                            return null;
                        } else if (methodName.equals(SIZE_METHOD)) {
                            return Integer.valueOf(((JSONObject) this.state).length());
                        } else {
                            if (methodName.equals(VALUES_METHOD)) {
                                return JsonUtil.jsonObjectValues((JSONObject) this.state);
                            }
                            return throwUnexpectedMethodSignature(method);
                        }
                    }
                }
            }

            private final Object proxyGraphObjectMethods(Object proxy, Method method, Object[] args) {
                String methodName = method.getName();
                if (methodName.equals(CAST_METHOD)) {
                    Class<? extends GraphObject> graphObjectClass = (Class) args[0];
                    if (graphObjectClass == null || !graphObjectClass.isAssignableFrom(this.graphObjectClass)) {
                        return Factory.createGraphObjectProxy(graphObjectClass, (JSONObject) this.state);
                    }
                    return proxy;
                } else if (methodName.equals(GETINNERJSONOBJECT_METHOD)) {
                    InvocationHandler handler = Proxy.getInvocationHandler(proxy);
                    GraphObjectProxy otherProxy = (GraphObjectProxy) handler;
                    return otherProxy.state;
                } else if (methodName.equals(CASTTOMAP_METHOD)) {
                    return Factory.createGraphObjectProxyForMap((JSONObject) this.state);
                } else {
                    if (methodName.equals(GETPROPERTY_METHOD)) {
                        return ((JSONObject) this.state).opt((String) args[0]);
                    }
                    if (methodName.equals(GETPROPERTYAS_METHOD)) {
                        Object value = ((JSONObject) this.state).opt((String) args[0]);
                        Class<?> expectedType = (Class) args[1];
                        return Factory.coerceValueToExpectedType(value, expectedType, null);
                    } else if (methodName.equals(GETPROPERTYASLIST_METHOD)) {
                        Object value2 = ((JSONObject) this.state).opt((String) args[0]);
                        final Class<?> expectedType2 = (Class) args[1];
                        ParameterizedType parameterizedType = new ParameterizedType() { // from class: com.facebook.model.GraphObject.Factory.GraphObjectProxy.1
                            @Override // java.lang.reflect.ParameterizedType
                            public Type[] getActualTypeArguments() {
                                return new Type[]{expectedType2};
                            }

                            @Override // java.lang.reflect.ParameterizedType
                            public Type getOwnerType() {
                                return null;
                            }

                            @Override // java.lang.reflect.ParameterizedType
                            public Type getRawType() {
                                return GraphObjectList.class;
                            }
                        };
                        return Factory.coerceValueToExpectedType(value2, GraphObjectList.class, parameterizedType);
                    } else if (methodName.equals(SETPROPERTY_METHOD)) {
                        return setJSONProperty(args);
                    } else {
                        if (methodName.equals(REMOVEPROPERTY_METHOD)) {
                            ((JSONObject) this.state).remove((String) args[0]);
                            return null;
                        }
                        return throwUnexpectedMethodSignature(method);
                    }
                }
            }

            private Object createGraphObjectsFromParameters(CreateGraphObject createGraphObject, Object value) {
                if (createGraphObject != null && !Utility.isNullOrEmpty(createGraphObject.value())) {
                    String propertyName = createGraphObject.value();
                    if (List.class.isAssignableFrom(value.getClass())) {
                        GraphObjectList<GraphObject> graphObjects = Factory.createList(GraphObject.class);
                        List<Object> values = (List) value;
                        for (Object obj : values) {
                            GraphObject graphObject = Factory.create();
                            graphObject.setProperty(propertyName, obj);
                            graphObjects.add(graphObject);
                        }
                        return graphObjects;
                    }
                    GraphObject graphObject2 = Factory.create();
                    graphObject2.setProperty(propertyName, value);
                    return graphObject2;
                }
                return value;
            }

            private final Object proxyGraphObjectGettersAndSetters(Method method, Object[] args) throws JSONException {
                String methodName = method.getName();
                int parameterCount = method.getParameterTypes().length;
                PropertyName propertyNameOverride = (PropertyName) method.getAnnotation(PropertyName.class);
                String key = propertyNameOverride != null ? propertyNameOverride.value() : Factory.convertCamelCaseToLowercaseWithUnderscores(methodName.substring(3));
                if (parameterCount == 0) {
                    Object value = ((JSONObject) this.state).opt(key);
                    Class<?> expectedType = method.getReturnType();
                    Type genericReturnType = method.getGenericReturnType();
                    ParameterizedType parameterizedReturnType = null;
                    if (genericReturnType instanceof ParameterizedType) {
                        parameterizedReturnType = (ParameterizedType) genericReturnType;
                    }
                    return Factory.coerceValueToExpectedType(value, expectedType, parameterizedReturnType);
                } else if (parameterCount == 1) {
                    CreateGraphObject createGraphObjectAnnotation = (CreateGraphObject) method.getAnnotation(CreateGraphObject.class);
                    Object value2 = createGraphObjectsFromParameters(createGraphObjectAnnotation, args[0]);
                    ((JSONObject) this.state).putOpt(key, Factory.getUnderlyingJSONObject(value2));
                    return null;
                } else {
                    Object value3 = throwUnexpectedMethodSignature(method);
                    return value3;
                }
            }

            private Object setJSONProperty(Object[] args) {
                String name = (String) args[0];
                Object property = args[1];
                Object value = Factory.getUnderlyingJSONObject(property);
                try {
                    ((JSONObject) this.state).putOpt(name, value);
                    return null;
                } catch (JSONException e) {
                    throw new IllegalArgumentException(e);
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes.dex */
        public static final class GraphObjectListImpl<T> extends AbstractList<T> implements GraphObjectList<T> {
            private final Class<?> itemType;
            private final JSONArray state;

            public GraphObjectListImpl(JSONArray state, Class<?> itemType) {
                Validate.notNull(state, "state");
                Validate.notNull(itemType, "itemType");
                this.state = state;
                this.itemType = itemType;
            }

            @Override // java.util.AbstractCollection
            public String toString() {
                return String.format("GraphObjectList{itemType=%s, state=%s}", this.itemType.getSimpleName(), this.state);
            }

            @Override // java.util.AbstractList, java.util.List
            public void add(int location, T object) {
                if (location < 0) {
                    throw new IndexOutOfBoundsException();
                }
                if (location < size()) {
                    throw new UnsupportedOperationException("Only adding items at the end of the list is supported.");
                }
                put(location, object);
            }

            @Override // java.util.AbstractList, java.util.List
            public T set(int location, T object) {
                checkIndex(location);
                T result = get(location);
                put(location, object);
                return result;
            }

            @Override // java.util.AbstractList, java.util.Collection, java.util.List
            public int hashCode() {
                return this.state.hashCode();
            }

            @Override // java.util.AbstractList, java.util.Collection, java.util.List
            public boolean equals(Object obj) {
                if (obj == null) {
                    return false;
                }
                if (this == obj) {
                    return true;
                }
                if (getClass() == obj.getClass()) {
                    GraphObjectListImpl<T> other = (GraphObjectListImpl) obj;
                    return this.state.equals(other.state);
                }
                return false;
            }

            @Override // java.util.AbstractList, java.util.List
            public T get(int location) {
                checkIndex(location);
                Object value = this.state.opt(location);
                T result = (T) Factory.coerceValueToExpectedType(value, this.itemType, null);
                return result;
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
            public int size() {
                return this.state.length();
            }

            @Override // com.facebook.model.GraphObjectList
            public final <U extends GraphObject> GraphObjectList<U> castToListOf(Class<U> graphObjectClass) {
                if (GraphObject.class.isAssignableFrom(this.itemType)) {
                    return graphObjectClass.isAssignableFrom(this.itemType) ? this : Factory.createList(this.state, graphObjectClass);
                }
                throw new FacebookGraphObjectException("Can't cast GraphObjectCollection of non-GraphObject type " + this.itemType);
            }

            @Override // com.facebook.model.GraphObjectList
            public final JSONArray getInnerJSONArray() {
                return this.state;
            }

            @Override // java.util.AbstractList, java.util.AbstractCollection, java.util.Collection, java.util.List
            public void clear() {
                throw new UnsupportedOperationException();
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
            public boolean remove(Object o) {
                throw new UnsupportedOperationException();
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
            public boolean removeAll(Collection<?> c) {
                throw new UnsupportedOperationException();
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
            public boolean retainAll(Collection<?> c) {
                throw new UnsupportedOperationException();
            }

            private void checkIndex(int index) {
                if (index < 0 || index >= this.state.length()) {
                    throw new IndexOutOfBoundsException();
                }
            }

            private void put(int index, T obj) {
                Object underlyingObject = Factory.getUnderlyingJSONObject(obj);
                try {
                    this.state.put(index, underlyingObject);
                } catch (JSONException e) {
                    throw new IllegalArgumentException(e);
                }
            }
        }
    }
}

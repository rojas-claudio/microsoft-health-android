package dagger.internal.codegen;

import com.j256.ormlite.stmt.query.SimpleComparison;
import dagger.internal.Keys;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.AnnotationValueVisitor;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.ErrorType;
import javax.lang.model.type.PrimitiveType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVariable;
import javax.lang.model.util.SimpleAnnotationValueVisitor6;
import javax.lang.model.util.SimpleTypeVisitor6;
import org.apache.commons.lang3.ClassUtils;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class Util {
    private static final AnnotationValueVisitor<Object, Void> VALUE_EXTRACTOR = new SimpleAnnotationValueVisitor6<Object, Void>() { // from class: dagger.internal.codegen.Util.2
        public /* bridge */ /* synthetic */ Object visitArray(List x0, Object x1) {
            return visitArray((List<? extends AnnotationValue>) x0, (Void) x1);
        }

        public Object visitString(String s, Void p) {
            if ("<error>".equals(s)) {
                throw new CodeGenerationIncompleteException("Unknown type returned as <error>.");
            }
            if ("<any>".equals(s)) {
                throw new CodeGenerationIncompleteException("Unknown type returned as <any>.");
            }
            return s;
        }

        public Object visitType(TypeMirror t, Void p) {
            return t;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        public Object defaultAction(Object o, Void v) {
            return o;
        }

        public Object visitArray(List<? extends AnnotationValue> values, Void v) {
            Object[] result = new Object[values.size()];
            for (int i = 0; i < values.size(); i++) {
                result[i] = values.get(i).accept(this, (Object) null);
            }
            return result;
        }
    };

    private Util() {
    }

    public static PackageElement getPackage(Element type) {
        while (type.getKind() != ElementKind.PACKAGE) {
            type = type.getEnclosingElement();
        }
        return (PackageElement) type;
    }

    public static TypeMirror getApplicationSupertype(TypeElement type) {
        TypeMirror supertype = type.getSuperclass();
        if (Keys.isPlatformType(supertype.toString())) {
            return null;
        }
        return supertype;
    }

    public static String adapterName(TypeElement typeElement, String suffix) {
        StringBuilder builder = new StringBuilder();
        rawTypeToString(builder, typeElement, '$');
        builder.append(suffix);
        return builder.toString();
    }

    public static String typeToString(TypeMirror type) {
        StringBuilder result = new StringBuilder();
        typeToString(type, result, ClassUtils.PACKAGE_SEPARATOR_CHAR);
        return result.toString();
    }

    public static String rawTypeToString(TypeMirror type, char innerClassSeparator) {
        if (!(type instanceof DeclaredType)) {
            throw new IllegalArgumentException("Unexpected type: " + type);
        }
        StringBuilder result = new StringBuilder();
        DeclaredType declaredType = (DeclaredType) type;
        rawTypeToString(result, declaredType.asElement(), innerClassSeparator);
        return result.toString();
    }

    public static void typeToString(final TypeMirror type, final StringBuilder result, final char innerClassSeparator) {
        type.accept(new SimpleTypeVisitor6<Void, Void>() { // from class: dagger.internal.codegen.Util.1
            public Void visitDeclared(DeclaredType declaredType, Void v) {
                TypeElement typeElement = declaredType.asElement();
                Util.rawTypeToString(result, typeElement, innerClassSeparator);
                List<? extends TypeMirror> typeArguments = declaredType.getTypeArguments();
                if (!typeArguments.isEmpty()) {
                    result.append(SimpleComparison.LESS_THAN_OPERATION);
                    for (int i = 0; i < typeArguments.size(); i++) {
                        if (i != 0) {
                            result.append(", ");
                        }
                        Util.typeToString((TypeMirror) typeArguments.get(i), result, innerClassSeparator);
                    }
                    result.append(SimpleComparison.GREATER_THAN_OPERATION);
                    return null;
                }
                return null;
            }

            public Void visitPrimitive(PrimitiveType primitiveType, Void v) {
                result.append(Util.box(type).getName());
                return null;
            }

            public Void visitArray(ArrayType arrayType, Void v) {
                TypeMirror type2 = arrayType.getComponentType();
                if (type2 instanceof PrimitiveType) {
                    result.append(type2.toString());
                } else {
                    Util.typeToString(arrayType.getComponentType(), result, innerClassSeparator);
                }
                result.append("[]");
                return null;
            }

            public Void visitTypeVariable(TypeVariable typeVariable, Void v) {
                result.append((CharSequence) typeVariable.asElement().getSimpleName());
                return null;
            }

            public Void visitError(ErrorType errorType, Void v) {
                if ("<any>".equals(errorType.toString())) {
                    throw new CodeGenerationIncompleteException("Type reported as <any> is likely a not-yet generated parameterized type.");
                }
                result.append(errorType.toString());
                return null;
            }

            /* JADX INFO: Access modifiers changed from: protected */
            public Void defaultAction(TypeMirror typeMirror, Void v) {
                throw new UnsupportedOperationException("Unexpected TypeKind " + typeMirror.getKind() + " for " + typeMirror);
            }
        }, (Object) null);
    }

    public static Map<String, Object> getAnnotation(Class<?> annotationType, Element element) {
        for (AnnotationMirror annotation : element.getAnnotationMirrors()) {
            if (rawTypeToString(annotation.getAnnotationType(), '$').equals(annotationType.getName())) {
                Map<String, Object> result = new LinkedHashMap<>();
                Method[] arr$ = annotationType.getMethods();
                for (Method m : arr$) {
                    result.put(m.getName(), m.getDefaultValue());
                }
                for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> e : annotation.getElementValues().entrySet()) {
                    String name = ((ExecutableElement) e.getKey()).getSimpleName().toString();
                    Object value = ((AnnotationValue) e.getValue()).accept(VALUE_EXTRACTOR, (Object) null);
                    Object defaultValue = result.get(name);
                    if (!lenientIsInstance(defaultValue.getClass(), value)) {
                        Object[] objArr = new Object[5];
                        objArr[0] = annotationType;
                        objArr[1] = name;
                        objArr[2] = value.getClass().getName();
                        objArr[3] = defaultValue.getClass().getName();
                        if (value instanceof Object[]) {
                            value = Arrays.toString((Object[]) value);
                        }
                        objArr[4] = value;
                        throw new IllegalStateException(String.format("Value of %s.%s is a %s but expected a %s\n    value: %s", objArr));
                    }
                    result.put(name, value);
                }
                return result;
            }
        }
        return null;
    }

    private static boolean lenientIsInstance(Class<?> expectedClass, Object value) {
        if (expectedClass.isArray()) {
            Class<?> componentType = expectedClass.getComponentType();
            if (value instanceof Object[]) {
                Object[] arr$ = (Object[]) value;
                for (Object element : arr$) {
                    if (!lenientIsInstance(componentType, element)) {
                        return false;
                    }
                }
                return true;
            }
            return false;
        } else if (expectedClass == Class.class) {
            return value instanceof TypeMirror;
        } else {
            return expectedClass == value.getClass();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String elementToString(Element element) {
        switch (AnonymousClass3.$SwitchMap$javax$lang$model$element$ElementKind[element.getKind().ordinal()]) {
            case 1:
            case 2:
            case 3:
                return element.getEnclosingElement() + "." + element;
            default:
                return element.toString();
        }
    }

    static void rawTypeToString(StringBuilder result, TypeElement type, char innerClassSeparator) {
        String packageName = getPackage(type).getQualifiedName().toString();
        String qualifiedName = type.getQualifiedName().toString();
        if (packageName.isEmpty()) {
            result.append(qualifiedName.replace(ClassUtils.PACKAGE_SEPARATOR_CHAR, innerClassSeparator));
            return;
        }
        result.append(packageName);
        result.append(ClassUtils.PACKAGE_SEPARATOR_CHAR);
        result.append(qualifiedName.substring(packageName.length() + 1).replace(ClassUtils.PACKAGE_SEPARATOR_CHAR, innerClassSeparator));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: dagger.internal.codegen.Util$3  reason: invalid class name */
    /* loaded from: classes.dex */
    public static /* synthetic */ class AnonymousClass3 {
        static final /* synthetic */ int[] $SwitchMap$javax$lang$model$element$ElementKind;
        static final /* synthetic */ int[] $SwitchMap$javax$lang$model$type$TypeKind = new int[TypeKind.values().length];

        static {
            try {
                $SwitchMap$javax$lang$model$type$TypeKind[TypeKind.BYTE.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$javax$lang$model$type$TypeKind[TypeKind.SHORT.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$javax$lang$model$type$TypeKind[TypeKind.INT.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$javax$lang$model$type$TypeKind[TypeKind.LONG.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$javax$lang$model$type$TypeKind[TypeKind.FLOAT.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            try {
                $SwitchMap$javax$lang$model$type$TypeKind[TypeKind.DOUBLE.ordinal()] = 6;
            } catch (NoSuchFieldError e6) {
            }
            try {
                $SwitchMap$javax$lang$model$type$TypeKind[TypeKind.BOOLEAN.ordinal()] = 7;
            } catch (NoSuchFieldError e7) {
            }
            try {
                $SwitchMap$javax$lang$model$type$TypeKind[TypeKind.CHAR.ordinal()] = 8;
            } catch (NoSuchFieldError e8) {
            }
            try {
                $SwitchMap$javax$lang$model$type$TypeKind[TypeKind.VOID.ordinal()] = 9;
            } catch (NoSuchFieldError e9) {
            }
            $SwitchMap$javax$lang$model$element$ElementKind = new int[ElementKind.values().length];
            try {
                $SwitchMap$javax$lang$model$element$ElementKind[ElementKind.FIELD.ordinal()] = 1;
            } catch (NoSuchFieldError e10) {
            }
            try {
                $SwitchMap$javax$lang$model$element$ElementKind[ElementKind.CONSTRUCTOR.ordinal()] = 2;
            } catch (NoSuchFieldError e11) {
            }
            try {
                $SwitchMap$javax$lang$model$element$ElementKind[ElementKind.METHOD.ordinal()] = 3;
            } catch (NoSuchFieldError e12) {
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static Class<?> box(PrimitiveType primitiveType) {
        switch (AnonymousClass3.$SwitchMap$javax$lang$model$type$TypeKind[primitiveType.getKind().ordinal()]) {
            case 1:
                return Byte.class;
            case 2:
                return Short.class;
            case 3:
                return Integer.class;
            case 4:
                return Long.class;
            case 5:
                return Float.class;
            case 6:
                return Double.class;
            case 7:
                return Boolean.class;
            case 8:
                return Character.class;
            case 9:
                return Void.class;
            default:
                throw new AssertionError();
        }
    }

    public static ExecutableElement getNoArgsConstructor(TypeElement type) {
        for (ExecutableElement executableElement : type.getEnclosedElements()) {
            if (executableElement.getKind() == ElementKind.CONSTRUCTOR) {
                ExecutableElement constructor = executableElement;
                if (constructor.getParameters().isEmpty()) {
                    return constructor;
                }
            }
        }
        return null;
    }

    public static boolean isCallableConstructor(ExecutableElement constructor) {
        if (constructor.getModifiers().contains(Modifier.PRIVATE)) {
            return false;
        }
        TypeElement type = constructor.getEnclosingElement();
        return type.getEnclosingElement().getKind() == ElementKind.PACKAGE || type.getModifiers().contains(Modifier.STATIC);
    }

    public static String className(ExecutableElement method) {
        return method.getEnclosingElement().getQualifiedName().toString();
    }

    public static boolean isInterface(TypeMirror typeMirror) {
        return (typeMirror instanceof DeclaredType) && ((DeclaredType) typeMirror).asElement().getKind() == ElementKind.INTERFACE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean isStatic(Element element) {
        for (Modifier modifier : element.getModifiers()) {
            if (modifier.equals(Modifier.STATIC)) {
                return true;
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static final class CodeGenerationIncompleteException extends IllegalStateException {
        public CodeGenerationIncompleteException(String s) {
            super(s);
        }
    }
}

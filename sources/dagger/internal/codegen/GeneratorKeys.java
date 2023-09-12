package dagger.internal.codegen;

import com.j256.ormlite.stmt.query.SimpleComparison;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.inject.Qualifier;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
/* loaded from: classes.dex */
final class GeneratorKeys {
    private static final String SET_PREFIX = Set.class.getCanonicalName() + SimpleComparison.LESS_THAN_OPERATION;

    private GeneratorKeys() {
    }

    public static String rawMembersKey(TypeMirror type) {
        return "members/" + Util.rawTypeToString(type, '$');
    }

    public static String get(TypeMirror type) {
        StringBuilder result = new StringBuilder();
        Util.typeToString(type, result, '$');
        return result.toString();
    }

    public static String get(ExecutableElement method) {
        StringBuilder result = new StringBuilder();
        AnnotationMirror qualifier = getQualifier(method.getAnnotationMirrors());
        if (qualifier != null) {
            qualifierToString(qualifier, result);
        }
        Util.typeToString(method.getReturnType(), result, '$');
        return result.toString();
    }

    public static String getSetKey(ExecutableElement method) {
        StringBuilder result = new StringBuilder();
        AnnotationMirror qualifier = getQualifier(method.getAnnotationMirrors());
        if (qualifier != null) {
            qualifierToString(qualifier, result);
        }
        result.append(SET_PREFIX);
        Util.typeToString(method.getReturnType(), result, '$');
        result.append(SimpleComparison.GREATER_THAN_OPERATION);
        return result.toString();
    }

    public static String get(VariableElement variable) {
        StringBuilder result = new StringBuilder();
        AnnotationMirror qualifier = getQualifier(variable.getAnnotationMirrors());
        if (qualifier != null) {
            qualifierToString(qualifier, result);
        }
        Util.typeToString(variable.asType(), result, '$');
        return result.toString();
    }

    private static void qualifierToString(AnnotationMirror qualifier, StringBuilder result) {
        result.append('@');
        Util.typeToString(qualifier.getAnnotationType(), result, '$');
        result.append('(');
        for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry : qualifier.getElementValues().entrySet()) {
            result.append((CharSequence) ((ExecutableElement) entry.getKey()).getSimpleName());
            result.append('=');
            result.append(((AnnotationValue) entry.getValue()).getValue());
        }
        result.append(")/");
    }

    private static AnnotationMirror getQualifier(List<? extends AnnotationMirror> annotations) {
        AnnotationMirror qualifier = null;
        for (AnnotationMirror annotation : annotations) {
            if (annotation.getAnnotationType().asElement().getAnnotation(Qualifier.class) != null) {
                qualifier = annotation;
            }
        }
        return qualifier;
    }
}

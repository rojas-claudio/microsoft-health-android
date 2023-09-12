package dagger.internal.codegen;

import dagger.Module;
import dagger.Provides;
import dagger.internal.codegen.Util;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.inject.Inject;
import javax.inject.Qualifier;
import javax.inject.Scope;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
@SupportedAnnotationTypes({"*"})
/* loaded from: classes.dex */
public final class ValidationProcessor extends AbstractProcessor {
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    public boolean process(Set<? extends TypeElement> types, RoundEnvironment env) {
        List<Element> allElements = new ArrayList<>();
        Map<Element, Element> parametersToTheirMethods = new LinkedHashMap<>();
        getAllElements(env, allElements, parametersToTheirMethods);
        for (Element element : allElements) {
            try {
                validateProvides(element);
                validateScoping(element);
                validateQualifiers(element, parametersToTheirMethods);
            } catch (Util.CodeGenerationIncompleteException e) {
            }
        }
        return false;
    }

    private void validateProvides(Element element) {
        if (element.getAnnotation(Provides.class) != null && Util.getAnnotation(Module.class, element.getEnclosingElement()) == null) {
            error("@Provides methods must be declared in modules: " + Util.elementToString(element), element);
        }
    }

    private void validateQualifiers(Element element, Map<Element, Element> parametersToTheirMethods) {
        boolean suppressWarnings = element.getAnnotation(SuppressWarnings.class) != null && Arrays.asList(((SuppressWarnings) element.getAnnotation(SuppressWarnings.class)).value()).contains("qualifiers");
        int numberOfQualifiersOnElement = 0;
        for (AnnotationMirror annotation : element.getAnnotationMirrors()) {
            if (annotation.getAnnotationType().asElement().getAnnotation(Qualifier.class) != null) {
                switch (AnonymousClass1.$SwitchMap$javax$lang$model$element$ElementKind[element.getKind().ordinal()]) {
                    case 1:
                        numberOfQualifiersOnElement++;
                        if (element.getAnnotation(Inject.class) == null) {
                            if (suppressWarnings) {
                                break;
                            } else {
                                warning("Dagger will ignore qualifier annotations on fields that are not annotated with @Inject: " + Util.elementToString(element), element);
                                break;
                            }
                        } else {
                            continue;
                        }
                    case 2:
                        numberOfQualifiersOnElement++;
                        if (isProvidesMethod(element)) {
                            continue;
                        } else if (suppressWarnings) {
                            break;
                        } else {
                            warning("Dagger will ignore qualifier annotations on methods that are not @Provides methods: " + Util.elementToString(element), element);
                            break;
                        }
                    case 3:
                        numberOfQualifiersOnElement++;
                        if (isInjectableConstructorParameter(element, parametersToTheirMethods)) {
                            continue;
                        } else if (!isProvidesMethodParameter(element, parametersToTheirMethods) && !suppressWarnings) {
                            warning("Dagger will ignore qualifier annotations on parameters that are not @Inject constructor parameters or @Provides method parameters: " + Util.elementToString(element), element);
                            break;
                        }
                        break;
                    default:
                        error("Qualifier annotations are only allowed on fields, methods, and parameters: " + Util.elementToString(element), element);
                        continue;
                }
            }
        }
        if (numberOfQualifiersOnElement > 1) {
            error("Only one qualifier annotation is allowed per element: " + Util.elementToString(element), element);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: dagger.internal.codegen.ValidationProcessor$1  reason: invalid class name */
    /* loaded from: classes.dex */
    public static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$javax$lang$model$element$ElementKind = new int[ElementKind.values().length];

        static {
            try {
                $SwitchMap$javax$lang$model$element$ElementKind[ElementKind.FIELD.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$javax$lang$model$element$ElementKind[ElementKind.METHOD.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$javax$lang$model$element$ElementKind[ElementKind.PARAMETER.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$javax$lang$model$element$ElementKind[ElementKind.CLASS.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
        }
    }

    private void validateScoping(Element element) {
        boolean suppressWarnings = element.getAnnotation(SuppressWarnings.class) != null && Arrays.asList(((SuppressWarnings) element.getAnnotation(SuppressWarnings.class)).value()).contains("scoping");
        int numberOfScopingAnnotationsOnElement = 0;
        for (AnnotationMirror annotation : element.getAnnotationMirrors()) {
            if (annotation.getAnnotationType().asElement().getAnnotation(Scope.class) != null) {
                switch (AnonymousClass1.$SwitchMap$javax$lang$model$element$ElementKind[element.getKind().ordinal()]) {
                    case 2:
                        numberOfScopingAnnotationsOnElement++;
                        if (!isProvidesMethod(element) && !suppressWarnings) {
                            warning("Dagger will ignore scoping annotations on methods that are not @Provides methods: " + Util.elementToString(element), element);
                            break;
                        }
                        break;
                    case 3:
                    default:
                        error("Scoping annotations are only allowed on concrete types and @Provides methods: " + Util.elementToString(element), element);
                        break;
                    case 4:
                        if (!element.getModifiers().contains(Modifier.ABSTRACT)) {
                            numberOfScopingAnnotationsOnElement++;
                            break;
                        } else {
                            error("Scoping annotations are only allowed on concrete types and @Provides methods: " + Util.elementToString(element), element);
                            break;
                        }
                }
            }
        }
        if (numberOfScopingAnnotationsOnElement > 1) {
            error("Only one scoping annotation is allowed per element: " + Util.elementToString(element), element);
        }
    }

    private void getAllElements(RoundEnvironment env, List<Element> result, Map<Element, Element> parametersToTheirMethods) {
        for (Element element : env.getRootElements()) {
            addAllEnclosed(element, result, parametersToTheirMethods);
        }
    }

    private void addAllEnclosed(Element element, List<Element> result, Map<Element, Element> parametersToTheirMethods) {
        result.add(element);
        for (ExecutableElement executableElement : element.getEnclosedElements()) {
            addAllEnclosed(executableElement, result, parametersToTheirMethods);
            if (executableElement.getKind() == ElementKind.METHOD || executableElement.getKind() == ElementKind.CONSTRUCTOR) {
                for (Element parameter : executableElement.getParameters()) {
                    result.add(parameter);
                    parametersToTheirMethods.put(parameter, executableElement);
                }
            }
        }
    }

    private boolean isProvidesMethod(Element element) {
        return element.getKind() == ElementKind.METHOD && element.getAnnotation(Provides.class) != null;
    }

    private boolean isProvidesMethodParameter(Element parameter, Map<Element, Element> parametersToTheirMethods) {
        return parametersToTheirMethods.get(parameter).getAnnotation(Provides.class) != null;
    }

    private boolean isInjectableConstructorParameter(Element parameter, Map<Element, Element> parametersToTheirMethods) {
        return parametersToTheirMethods.get(parameter).getKind() == ElementKind.CONSTRUCTOR && parametersToTheirMethods.get(parameter).getAnnotation(Inject.class) != null;
    }

    private void error(String msg, Element element) {
        this.processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, msg, element);
    }

    private void warning(String msg, Element element) {
        this.processingEnv.getMessager().printMessage(Diagnostic.Kind.WARNING, msg, element);
    }
}
